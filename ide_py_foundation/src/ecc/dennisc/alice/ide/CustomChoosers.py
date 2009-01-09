import java
import javax
import edu
import org

import ecc


from PaneWithValidation import PaneWithValidation
from TextPaneWithValidation import TextPaneWithValidation

class TextChooser( TextPaneWithValidation ):
	def __init__( self, previousValue, label="value:" ):
		TextPaneWithValidation.__init__( self, label, previousValue )

class StringChooser( TextChooser ):
	def _isValidText(self, text ):
		return True
	def getActualInputValue( self ):
		return self.getText()

class NumberChooser( TextChooser ):
	def __init__( self, value, label="value:" ):
		if value != None:
			value = str( value )
		else:
			value = ""
		TextChooser.__init__( self, value, label )
	def _isValidText(self, text ):
		try:
			self.getActualInputValue()
			return True
		except:
			return False
	
class IntegerChooser( NumberChooser ):
	def getActualInputValue( self ):
		return java.lang.Integer.valueOf( self.getText() )

class RealNumberChooser( NumberChooser ):
	pass
class FloatChooser( RealNumberChooser ):
	def getActualInputValue( self ):
		return java.lang.Float.valueOf( self.getText() )
class DoubleChooser( RealNumberChooser ):
	def getActualInputValue( self ):
		return java.lang.Double.valueOf( self.getText() )

class AngleChooser( DoubleChooser ):
	def __init__( self, value ):
		try:
			value = value.getAsRevolutions()
		except:
			value = 1.0
		DoubleChooser.__init__( self, value )
		self.add( javax.swing.JLabel( "revolutions" ), java.awt.BorderLayout.EAST )
	def getActualInputValue( self ):
		return edu.cmu.cs.dennisc.math.AngleInRevolutions( DoubleChooser.getActualInputValue( self ) )

class PortionChooser( PaneWithValidation ):
	def __init__( self, value ):
		PaneWithValidation.__init__( self )
		try:
			value = value.doubleValue()
		except:
			value = 1.0
		v = int( ( value+0.005 )*100 )
		
		self._slider = javax.swing.JSlider( javax.swing.JSlider.HORIZONTAL, 0, 100, v )
		self._slider.setMajorTickSpacing( 10 );
		#self.setMinorTickSpacing(1);

		labelTable = java.util.Hashtable()
		labelTable.put( 0, javax.swing.JLabel( "0.0" ) ) 
		labelTable.put( 100, javax.swing.JLabel( "1.0" ) )
		self._slider.setLabelTable( labelTable )

		self._slider.setPaintLabels( True )
		self._slider.setPaintTicks( True )
		
		self._slider.addChangeListener( ecc.dennisc.swing.event.ChangeAdapter( self._handleChange ) )
		
		self._label = javax.swing.JLabel()
		self._updateLabel()
				
		self.add( self._slider )
		self.add( self._label )
	
	def _handleChange( self, e ):
		self._updateLabel()
		self.updateOKButton()
	
	def _updateLabel( self ):
		value = self._slider.getValue() * 0.01
		self._label.setText( "value: " + ( "%.2f" % value ) )

	def isValid( self ):
		return True

	def getActualInputValue( self ):
		return org.alice.apis.moveandturn.Portion( self._slider.getValue() * 0.01 )

class AWTColorChooser( PaneWithValidation ):
	def __init__( self, value ):
		PaneWithValidation.__init__( self )
		self._colorVC = javax.swing.JColorChooser()
		if value:
			self._colorVC.setColor(value)
		self.setLayout( java.awt.GridLayout( 1, 1 ) )
		self.add( self._colorVC )

	def isValid( self ):
		return True
	def getActualInputValue( self ):
		return self._colorVC.getColor()

class ColorChooser( AWTColorChooser ):
	def __init__( self, value ):
		if value:
			value = value.getAsAWTColor()
		AWTColorChooser.__init__( self, value )
	def getActualInputValue( self ):
		return org.alice.apis.moveandturn.Color( edu.cmu.cs.dennis.color.Color4f( AWTColorChooser.getActualInputValue( self ) ) )


class FontChooser( PaneWithValidation ):
	def __init__( self, value ):
		PaneWithValidation.__init__( self )
		self._fontVC = org.alice.apis.moveandturn.ui.FontChooser()
		if value:
			self._fontVC.setValue(value)
		self.setLayout( java.awt.GridLayout( 1, 1 ) )
		self.add( self._fontVC )

	def isValid( self ):
		return True
	def getActualInputValue( self ):
		return self._fontVC.getValue()

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import zoot
#from ecc.dennisc.alice.ide.PaneWithValidation import PaneWithValidation
#from CreateTypedDeclarationPane import CreateTypedDeclarationPane	
from ecc.dennisc.alice.ide.NodeListPropertyPane import *

def _promptForExpression( me, type, taskObserver ):
	fillIn = ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( type )
	fillIn.showPopupMenu( me.getSource(), me.getX(), me.getY(), taskObserver )		

class InitializerView( javax.swing.JPanel, edu.cmu.cs.dennisc.pattern.Validator ):
	def __init__( self, type, expression, paneWithValidation ):
		javax.swing.JPanel.__init__( self )
		self.setLayout( java.awt.GridLayout( 1, 1 ) )
		self._type = type
		self._expression = expression
		self._paneWithValidation = paneWithValidation
		self.refresh()

	def isDropDownAffordanceDesired( self ):
		return self._type != None

	def getExpression( self ):
		return self._expression
	
	def setExpression( self, expression ):
		self._expression = expression
		self.refresh()
		self._paneWithValidation.updateOKButton()

	def setType( self, type ):
		self._type = type
		self.refresh()
		self._paneWithValidation.updateOKButton()
		
	def isValid( self ):
		if self._type:
			if self._expression:
				if self._expression.getType().isAssignableTo( self._type ):
					return True
		return False

	def refresh( self ):
		self.removeAll()
		if self._type:
			if self._expression:
				if self._expression.getType().isAssignableTo( self._type ):
					view = alice.ide.editors.code.ExpressionPane( self._expression )
				else:
					view = javax.swing.JLabel( "invalid: <click here>" )
			else:
				view = javax.swing.JLabel( "<click here>" )
		else:
			view = javax.swing.JLabel( "<fill in type first>" )
		self.add( view )
		self._paneWithValidation.repaint()
#		springPane = self._paneWithValidation.getParent()
#		if springPane:
#			inputPane = springPane.getParent()
#			if inputPane:
#				try:
#					inputPane.updateSizeIfNecessary()
#				except:
#					print "warning: could not update input pane", inputPane
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
		self._initializerPane._handleTypeChange( self._type.getComponentType() )
		self.repaint()

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

	def getPreferredSize( self ):
		rv = NodeListPropertyPane.getPreferredSize( self )
		rv.width = max( rv.width, 400 )
		rv.height = max( rv.height, 300 )
		return rv
	
	def _isDialogTriggeredOnAdd( self ):
		return False
	def _isRenameDesired( self ):
		return False
	def _handleAdd( self, e ):
		self._listVC.getModel().handleAdd( None )
		
	def setArrayInstanceCreation( self, arrayInstanceCreation ):
		#self._listVC.setComponentType( arrayInstanceCreation.arrayType.getValue().getComponentType() )
		self._listVC.setListProperty( arrayInstanceCreation.expressions )

	def _handleTypeChange( self, type ):
		self._listVC._handleTypeChange( type )
	def _getInitializer( self ):
		return self._listVC._getInitializer()
	def isValid( self ):
		return self._listVC.isValid()
	
from ecc.dennisc.alice.ide.TypeComboBox import TypeComboBox

class ArrayChooser( PaneWithValidation ):
	def __init__( self, value ):
		PaneWithValidation.__init__( self )
		self._typeVC = TypeComboBox()
		self._isArrayVC = javax.swing.JCheckBox( "is array" )
		self._isArrayVC.setSelected( True )
		self._isArrayVC.setEnabled( False )
		self._arrayInitializerPane = ArrayInitializerPane( self._typeVC.getSelectedItem(), self )
		self._typeVC.addItemListener( ecc.dennisc.swing.event.FilteredItemAdapter( self._handleLeafTypeChange ) )
		self._handleTypeChange()
		#self._isArrayVC.addItemListener( ecc.dennisc.swing.event.ItemAdapter( self._handleIsArrayChange ) )
		self.setLayout( java.awt.BorderLayout() )
		pane = zoot.ZLineAxisPane()
		pane.add( self._typeVC )
		pane.add( self._isArrayVC )
		self.add( pane, java.awt.BorderLayout.NORTH )
		self.add( self._arrayInitializerPane, java.awt.BorderLayout.CENTER )

	def _getType( self ):
		rv = self._typeVC.getSelectedItem()
		if self._isArrayVC.isSelected():
			rv = rv.getArrayType()
		return rv
	
	def isValid( self ):
		return True
	def getActualInputValue( self ):
		return self._arrayInitializerPane._getInitializer()

	def _handleTypeChange( self ):
		self._arrayInitializerPane._handleTypeChange( self._getType() )
		self.updateOKButton()
		
	def updateSizeIfNecessary(self):
		pass

	def _handleLeafTypeChange( self, type ):
		self._handleTypeChange()
	def _handleIsArrayChange( self, e ):
		self._handleTypeChange()
		self.updateSizeIfNecessary()
