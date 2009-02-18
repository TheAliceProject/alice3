#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import zoot

import ecc

from ecc.dennisc.alice.ide.PaneWithValidation import PaneWithValidation

from CreateTypedDeclarationPane import CreateTypedDeclarationPane	

from ecc.dennisc.alice.ide.NodeListPropertyPane import *

def _promptForExpression( me, type, taskObserver ):
	fillIn = ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( type )
	fillIn.showPopupMenu( me.getSource(), me.getX(), me.getY(), taskObserver )		

def _getDefaultExpressionForType( type ):
	if type in alice.ast.TypeDeclaredInJava.DOUBLE_TYPES:
		return alice.ast.DoubleLiteral( 0.0 )
	elif type in alice.ast.TypeDeclaredInJava.INTEGER_TYPES:
		return alice.ast.IntegerLiteral( 0 )
	elif type in alice.ast.TypeDeclaredInJava.BOOLEAN_TYPES:
		return alice.ast.BooleanLiteral( False )
	elif type is alice.ast.TypeDeclaredInJava.get( java.lang.String ):
		return alice.ast.StringLiteral( "hello" )
	else:
		return alice.ast.NullLiteral()

class InitializerView( javax.swing.JPanel, edu.cmu.cs.dennisc.pattern.Validator ):
	def __init__( self, type, expression, paneWithValidation ):
		javax.swing.JPanel.__init__( self )
		self.setLayout( java.awt.GridLayout( 1, 1 ) )
		self._paneWithValidation = paneWithValidation
		self._type = type
		self._mapTypeToExpression = {}
		self.setExpression( expression )

	def isValid( self ):
		return self._type != None

	def isDropDownAffordanceDesired( self ):
		return self.isValid()

	def getExpression( self ):
		if self._mapTypeToExpression.has_key( self._type ):
			rv = self._mapTypeToExpression[ self._type ]
#			if rv.getType().isAssignableTo( self._type ):
#				pass
#			else:
#				rv = _getDefaultExpressionForType( type )
		else:
			rv = _getDefaultExpressionForType( self._type )
		return rv
			
	
	def setExpression( self, expression ):
		if expression:
			pass
		else:
			expression = _getDefaultExpressionForType( self._type )
		self._mapTypeToExpression[ self._type ] = expression
		self.refresh()
		self._paneWithValidation.updateOKButton()

	def setType( self, type ):
		self._type = type
		self.refresh()
		self._paneWithValidation.updateOKButton()
		

	def refresh( self ):
		self.removeAll()
		text = None
		if self._type:
			expression = self.getExpression()
			view = alice.ide.editors.code.ExpressionPane( expression )
		else:
			view = javax.swing.JLabel( "<fill in type first>" )
			view.setOpaque( True )
			view.setBackground( java.awt.Color.RED )
		self.add( view )
		self._paneWithValidation.repaint()
		springPane = self._paneWithValidation.getParent()
		if springPane:
			inputPane = springPane.getParent()
			if inputPane:
				try:
					inputPane.updateSizeIfNecessary()
				except:
					print "warning: could not update input pane"
		self.revalidate()
		self.repaint()

class InitializerPane( alice.ide.editors.code.DropDownPane, java.awt.event.MouseListener, edu.cmu.cs.dennisc.task.TaskObserver ):
	def __init__( self, type, paneWithValidation ):
		self._view = InitializerView( type, None, paneWithValidation )
		alice.ide.editors.code.DropDownPane.__init__( self, None, self._view, None )
		self.addMouseListener( self )
	def mouseEntered( self, e ):
		pass
	def mouseExited( self, e ):
		pass
	def mousePressed( self, e ):
		if self._view._type:
			_promptForExpression( e, self._view._type, self )
		else:
			javax.swing.JOptionPane.showMessageDialog( e.getSource(), "fill in type first" )
	def mouseReleased( self, e ):
		pass
	def mouseClicked( self, e ):
		pass
	
	def handleCompletion( self, value ):
		self._view.setExpression( value )
	def handleCancelation( self ):
		pass

	def _handleTypeChange( self, type ):
		self._view.setType( type )
	def isValid( self ):
		return self._view.isValid()
		
class ItemInitializerPane( javax.swing.JPanel ):
	def __init__( self, type, paneWithValidation ):
		self._pane = InitializerPane( type, paneWithValidation )
		javax.swing.JPanel.__init__( self )
		self.setLayout( java.awt.FlowLayout( java.awt.FlowLayout.LEADING ) )
		self.add( self._pane )
	def _getType( self ):
		return self._pane._view._type
	def getPreferredSize( self ):
		type = self._getType()
		if type and type.isArray() == False:
			rv = NodeListPropertyPane.getPreferredSize( self )
		else:
			rv = java.awt.Dimension( 0, 0 )
		return rv
	def _handleTypeChange( self, type ):
		self._pane._handleTypeChange( type )
	def _getInitializer( self ):
		return self._pane._view.getExpression()
	def isValid( self ):
		return self._pane.isValid()

class ArrayInitializerListCellRenderer( zoot.ZLineAxisPane, javax.swing.ListCellRenderer ):
	def __init__( self, type, paneWithValidation ):
		zoot.ZLineAxisPane.__init__( self )
		self._type = type
		self._paneWithValidation = paneWithValidation
		self._label = javax.swing.JLabel( "[ ??? ]" )
		self._initializerPane = InitializerPane( self._type, self._paneWithValidation )
		self.add( self._label )
		self.add( self._initializerPane )
		self.setBackground( java.awt.Color( 191, 191, 255 ) )
	def _handleTypeChange( self, type ):
		self._type = type
		self._initializerPane._handleTypeChange( type.getComponentType() )
	def getListCellRendererComponent( self, list, value, index, isSelected, cellHasFocus ):
		self._label.setText( "[ " + str( index ) + " ]" )
		self._initializerPane._view.setExpression( value )
		self.setOpaque( isSelected )
		return self 

class _ArrayInitializerMouseAdapter( java.awt.event.MouseAdapter, edu.cmu.cs.dennisc.task.TaskObserver ):
	def __init__( self, listVC ):
		self._listVC = listVC
	
	def handleCompletion( self, value ):
		item = self._listVC.getModel().handleSet( self._index, value )
		self._listVC._handleItemChange( self._index, value )
		del self._index
		
	def handleCancelation( self ):
		del self._index

	def mousePressed( self, e ):
		self._index = self._listVC.locationToIndex( e.getPoint() )
		if self._index >= 0:
			_promptForExpression( e, self._listVC.getType().getComponentType(), self )
		if self._listVC and self._index >= 0:
			self._listVC.setSelectedIndex( self._index )
		

class ArrayInitializerListPropertyVC( ListPropertyVC ):
	def __init__( self, type, paneWithValidation ):
		ListPropertyVC.__init__( self, type, paneWithValidation )
		self._initializeInitializer()
		self._handleTypeChange( type )
		self.addMouseListener( _ArrayInitializerMouseAdapter( self ) )
	def _initializeInitializer( self ):
		self._initializer = alice.ast.ArrayInstanceCreation()
		self._initializer.lengths.add( [ - 1 ] )
		self.setListProperty( self._initializer.expressions ) 
	def _createListCellRenderer( self, type, paneWithValidation ):
		return ArrayInitializerListCellRenderer( type, paneWithValidation )
	def getType( self ):
		return self.getCellRenderer()._type
	def _handleTypeChange( self, type ):
		if type.isArray():
			N = self._initializer.expressions.size()
			i = 0
			while i<N:
				self._initializer.expressions.set( i, [ _getDefaultExpressionForType( type.getComponentType() ) ] )
				i += 1
			self.getCellRenderer()._handleTypeChange( type )
			self.repaint()
	def _getInitializer( self ):
		self._initializer.arrayType.setValue( self.getType() )
		self._initializer.lengths.set( 0, [ self._initializer.expressions.size() ] )
		return self._initializer
	def isValid( self ):
		for expression in self._initializer.expressions.iterator():
			if expression:
				pass
			else:
				return False
		return True
	
	def _updateOKButton( self ):
		self.getCellRenderer()._paneWithValidation.updateOKButton()
	
	def _handleItemChange( self, index, value ):
		self._updateOKButton()

class ArrayInitializerPane( NodeListPropertyPane, javax.swing.event.ListDataListener ):
	def _getType( self ):
		return self._listVC.getType()
	def getPreferredSize( self ):
		type = self._getType()
		if type and type.isArray() == True:
			rv = NodeListPropertyPane.getPreferredSize( self )
		else:
			rv = java.awt.Dimension( 0, 0 )
		return rv

	
	def contentsChanged( self, e ):
		self._listVC._updateOKButton()
	def intervalAdded( self, e ):
		self._listVC._updateOKButton()
	def intervalRemoved( self, e ):
		self._listVC._updateOKButton()

	def _createListVC( self, type, paneWithValidation ):
		rv = ArrayInitializerListPropertyVC( type, paneWithValidation )
		rv.getModel().addListDataListener( self )
		return rv
	
	def _isDialogTriggeredOnAdd( self ):
		return False
	def _isRenameDesired( self ):
		return False
	def _handleAdd( self, e ):
		expression = _getDefaultExpressionForType( self._getType().getComponentType() )
		self._listVC.getModel().handleAdd( expression )
		
	def setArrayInstanceCreation( self, arrayInstanceCreation ):
		#self._listVC.setComponentType( arrayInstanceCreation.arrayType.getValue().getComponentType() )
		self._listVC.setListProperty( arrayInstanceCreation.expressions )

	def _handleTypeChange( self, type ):
		self._listVC._handleTypeChange( type )
	def _getInitializer( self ):
		return self._listVC._getInitializer()
	def isValid( self ):
		return self._listVC.isValid()


class InitializerCardPane( PaneWithValidation ):
	ITEM_KEY = "ITEM_KEY"
	ARRAY_KEY = "ARRAY_KEY"
	def __init__( self, type ):
		PaneWithValidation.__init__( self, inset=0 )
		
		self._itemPane = ItemInitializerPane( type, self )
		self._arrayPane = ArrayInitializerPane( type, self )

		self._layout = java.awt.CardLayout()
		self.setLayout( self._layout )
		
		self.add( self._itemPane, InitializerCardPane.ITEM_KEY )
		self.add( self._arrayPane, InitializerCardPane.ARRAY_KEY )
		
		self._layout.show( self, InitializerCardPane.ITEM_KEY )

	def _getCurrentCard( self ):
		if self._itemPane.isVisible():
			return self._itemPane
		else:
			return self._arrayPane

	def isValid( self ):
		return self._getCurrentCard().isValid()

	def _handleTypeChange( self, type ):
		self._itemPane._handleTypeChange( type )
		self._arrayPane._handleTypeChange( type )
		if type.isArray():
			key = InitializerCardPane.ARRAY_KEY
		else:
			key = InitializerCardPane.ITEM_KEY
		self._layout.show( self, key )
		
	def _getInitializer( self ):
		rv = self._getCurrentCard()._getInitializer()
		if rv:
			pass
		else:
			#todo
			rv = alice.ast.NullLiteral()
		return rv
		
class CreateTypedDeclarationWithInitializerPane( CreateTypedDeclarationPane ):
	def _createComponentRows( self ):
		rv = CreateTypedDeclarationPane._createComponentRows( self )
		self._initializerCardPane = InitializerCardPane( self._typeVC.getSelectedItem() )
		self._initializerCardPane.setInputPane( self )
		
		self._typeVC.addItemListener( ecc.dennisc.swing.event.FilteredItemAdapter( self._handleLeafTypeChange ) )
		self._isArrayVC.addItemListener( ecc.dennisc.swing.event.ItemAdapter( self._handleIsArrayChange ) )

		self.addOKButtonValidator( self._initializerCardPane )
		rv.append( ( javax.swing.JLabel( "initializer:", javax.swing.JLabel.TRAILING ), self._initializerCardPane ) )

		self._isConstantVC = javax.swing.JCheckBox( "is constant" )
		rv.append( ( javax.swing.JLabel( "", javax.swing.JLabel.TRAILING ), self._isConstantVC ) )

		return rv

	def _getInitializer( self ):
		return self._initializerCardPane._getInitializer()

	def _handleTypeChange( self ):
		self._initializerCardPane._handleTypeChange( self._getType() )
		self.updateOKButton()

	def _handleLeafTypeChange( self, type ):
		self._handleTypeChange()
	def _handleIsArrayChange( self, e ):
		self._handleTypeChange()
		self.updateSizeIfNecessary()

#print "<--", __name__
