import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import moot

import ecc

from AbstractStatementTemplatePane import AbstractStatementTemplatePane
from AbstractStatementWithBodyTemplatePane import AbstractStatementWithBodyTemplatePane

class DoInOrderTemplatePane( AbstractStatementWithBodyTemplatePane ):
	def __init__( self ):
		AbstractStatementWithBodyTemplatePane.__init__( self, alice.ast.DoInOrder )

class DoTogetherTemplatePane( AbstractStatementWithBodyTemplatePane ):
	def __init__( self ):
		AbstractStatementWithBodyTemplatePane.__init__( self, alice.ast.DoTogether )

class CommentTemplatePane( AbstractStatementTemplatePane ):
	def __init__( self ):
		AbstractStatementTemplatePane.__init__( self, alice.ast.Comment )
	def _createActualStatementArgs( self ):
		return ( "", )
	def createStatement( self, e ):
		rv = AbstractStatementTemplatePane.createStatement( self, e )
		alice.ide.IDE.getSingleton().setCommentThatWantsFocus( rv )
		return rv

from DeclareLocalTemplatePane import DeclareLocalTemplatePane

from CascadingUbiquitousStatementTemplatePane import CascadingUbiquitousStatementTemplatePane

class CountLoopTemplatePane( CascadingUbiquitousStatementTemplatePane ):
	def __init__( self ):
		CascadingUbiquitousStatementTemplatePane.__init__( self, alice.ast.CountLoop, alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE )
	def _createArgs( self, expression ):
		return alice.ast.VariableDeclaredInAlice( None, java.lang.Integer ), alice.ast.ConstantDeclaredInAlice( None, java.lang.Integer ), expression, alice.ast.BlockStatement( [] )

class WhileLoopTemplatePane( CascadingUbiquitousStatementTemplatePane ):
	def __init__( self ):
		CascadingUbiquitousStatementTemplatePane.__init__( self, alice.ast.WhileLoop, alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE )
	def _createArgs( self, expression ):
		return expression, alice.ast.BlockStatement( [] )

class ForEachInArrayLoopTemplatePane( CascadingUbiquitousStatementTemplatePane ):
	def __init__( self ):
		CascadingUbiquitousStatementTemplatePane.__init__( self, alice.ast.ForEachInArrayLoop, alice.ast.TypeDeclaredInJava.get( java.lang.Object ).getArrayType() )
	def _createArgs( self, expression ):
		variable = alice.ast.VariableDeclaredInAlice( None, expression.getType().getComponentType() )
		return variable, expression, alice.ast.BlockStatement( [] )

class EachInArrayTogetherTemplatePane( CascadingUbiquitousStatementTemplatePane ):
	def __init__( self ):
		CascadingUbiquitousStatementTemplatePane.__init__( self, alice.ast.EachInArrayTogether, alice.ast.TypeDeclaredInJava.get( java.lang.Object ).getArrayType() )
	def _createArgs( self, expression ):
		variable = alice.ast.VariableDeclaredInAlice( None, expression.getType().getComponentType() )
		return variable, expression, alice.ast.BlockStatement( [] )

class ConditionalStatementTemplatePane( CascadingUbiquitousStatementTemplatePane ):
	def __init__( self ):
		CascadingUbiquitousStatementTemplatePane.__init__( self, alice.ast.ConditionalStatement, alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE )
	def _createArgs( self, expression ):
		return [ alice.ast.BooleanExpressionBodyPair( expression, alice.ast.BlockStatement( [] ) ) ], alice.ast.BlockStatement( [] )

from ReturnStatementOwnerPane import ReturnStatementOwnerPane
from MathFunctionsTemplatePane import MathFunctionsTemplatePane

#class MiniGetsPane( alice.ide.editors.code.GetsPane ):
#	def getPreferredSize(self):
#		return java.awt.Dimension( 36, 12 )
class SetVariableTemplatePane( CascadingUbiquitousStatementTemplatePane ):
	def __init__( self, variable ):
		self._variable = variable
		CascadingUbiquitousStatementTemplatePane.__init__( self, alice.ast.ExpressionStatement, variable.valueType.getValue() )
	def createComponent(self):
		return moot.ZLabel( self._variable.getName() + " <== " )
#		rv = moot.ZLineAxisPane()
#		rv.setOpaque( False )
#		rv.add( moot.ZLabel( self._variable.getName() ) )
#		rv.add( MiniGetsPane( True, 2 ) )
#		return rv
	def _createArgs( self, expression ):
		return [ alice.ast.AssignmentExpression( self._variable.valueType.getValue(), alice.ast.VariableAccess( self._variable ), alice.ast.AssignmentExpression.Operator.ASSIGN, expression ) ]


class SetArrayAtIndexTemplatePane( alice.ide.editors.ubiquitous.ExpressionRequiringUbiquitousStatementTemplatePane ):
	def __init__( self, name, arrayType ):
		self._name = name
		self._arrayType = arrayType
		alice.ide.editors.ubiquitous.ExpressionRequiringUbiquitousStatementTemplatePane.__init__( self, apply( alice.ast.ExpressionStatement, self._createArgsForEmpty() ) )
	def createComponent(self):
		return moot.ZLabel( self._name + "[ ? ] <== " )

	def _createAccess(self):
		raise "Override"

	def _createArgs( self, indexExpression, rightExpression ):
		accessExpression = alice.ast.ArrayAccess( self._arrayType, self._createAccess(), indexExpression )
		return [ alice.ast.AssignmentExpression( self._arrayType.getComponentType(), accessExpression, alice.ast.AssignmentExpression.Operator.ASSIGN, rightExpression ) ]
	def _createArgsForEmpty( self ):
		return self._createArgs( alice.ide.editors.code.EmptyExpression( alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ), alice.ide.editors.code.EmptyExpression( self._arrayType.getComponentType() ) )

	def createUbiquitousStatement( self, expression ):
		return apply( alice.ast.ExpressionStatement, [ expression ] )
	
	def _createNode(self):
		raise "Override"

	def promptUserForExpression( self, observer, dndEvent ):
		node = self._createNode()
		me = dndEvent.getEndingMouseEvent()
		node.showPopupMenu( me.getSource(), me.getX(), me.getY(), observer )		

class SetVariableArrayAtIndexTemplatePane( SetArrayAtIndexTemplatePane ):
	def __init__( self, variable ):
		self._variable = variable
		SetArrayAtIndexTemplatePane.__init__( self, self._variable.getName(), self._variable.valueType.getValue() )
	def _createAccess(self):
		return alice.ast.VariableAccess( self._variable )
	def _createNode(self):
		return ecc.dennisc.alice.ide.cascade.SetArrayVariableAtIndexFillIn( self._variable )

class SetParameterArrayAtIndexTemplatePane( SetArrayAtIndexTemplatePane ):
	def __init__( self, parameter ):
		self._parameter = parameter
		SetArrayAtIndexTemplatePane.__init__( self, self._parameter.getName(), self._parameter.valueType.getValue() )
	def _createAccess(self):
		return alice.ast.ParameterAccess( self._parameter )
	def _createNode(self):
		return ecc.dennisc.alice.ide.cascade.SetArrayParameterAtIndexFillIn( self._parameter )

class GetTemplatePane( alice.ide.editors.code.AccessiblePane ):
	def __init__( self ):
		alice.ide.editors.code.AccessiblePane.__init__( self )

	def isActuallyPotentiallyActive( self ):
		return True
	def isActuallyPotentiallyDraggable( self ):
		return True
	
class GetAtIndexTemplatePane( GetTemplatePane ):
	def __init__( self, name, arrayType ):
		self._arrayType = arrayType
		GetTemplatePane.__init__( self )
		self.add( alice.ide.editors.common.Label( name + "[ ? ]" ) )
		self.setBackground( alice.ide.IDE.getSingleton().getColorForASTClass( alice.ast.ArrayAccess ) )

	def getExpressionType( self ):
		return self._arrayType.getComponentType()
		
	def _createAccess(self):
		raise "Override"

	def createExpression( self, dndEvent ):
		class MyBlockingTaskObserver( edu.cmu.cs.dennisc.task.BlockingTaskObserver ):
			def run(self):
				me = dndEvent.getEndingMouseEvent()
				blank = ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE )
				blank.showPopupMenu( me.getSource(), me.getX(), me.getY(), self )	
		observer = MyBlockingTaskObserver()
		indexExpression = observer.getResult()
		accessExpression = alice.ast.ArrayAccess( self._arrayType, self._createAccess(), indexExpression )
		return accessExpression

class GetVariableArrayAtIndexTemplatePane( GetAtIndexTemplatePane ):
	def __init__( self, variable ):
		self._variable = variable
		GetAtIndexTemplatePane.__init__( self, self._variable.getName(), self._variable.valueType.getValue() )
	def _createAccess(self):
		return alice.ast.VariableAccess( self._variable )

class GetParameterArrayAtIndexTemplatePane( GetAtIndexTemplatePane ):
	def __init__( self, parameter ):
		self._parameter = parameter
		GetAtIndexTemplatePane.__init__( self, self._parameter.getName(), self._parameter.valueType.getValue() )
	def _createAccess(self):
		return alice.ast.ParameterAccess( self._parameter )

class GetArrayLengthTemplatePane( GetTemplatePane ):
	def __init__( self, name ):
		GetTemplatePane.__init__( self )
		self.add( alice.ide.editors.common.Label( name + ".length" ) )
		self.setBackground( alice.ide.IDE.getSingleton().getColorForASTClass( alice.ast.ArrayLength ) )

	def getExpressionType( self ):
		return alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE
	
	def _createAccess(self):
		raise "Override"
	
	def createExpression( self, dndEvent ):
		return alice.ast.ArrayLength( self._createAccess() )

class GetVariableArrayLengthTemplatePane( GetArrayLengthTemplatePane ):
	def __init__( self, variable ):
		self._variable = variable
		GetArrayLengthTemplatePane.__init__( self, self._variable.getName() )
	def _createAccess(self):
		return alice.ast.VariableAccess( self._variable )

class GetParameterArrayLengthTemplatePane( GetArrayLengthTemplatePane ):
	def __init__( self, parameter ):
		self._parameter = parameter
		GetArrayLengthTemplatePane.__init__( self, self._parameter.getName() )
	def _createAccess(self):
		return alice.ast.ParameterAccess( self._parameter )

_mapVariableToSetTemplatePane = {} 
_mapVariableToSetArrayAtIndexTemplatePane = {} 
_mapVariableToGetArrayAtIndexTemplatePane = {} 
_mapVariableToGetArrayLengthTemplatePane = {} 
_mapParameterToSetArrayAtIndexTemplatePane = {} 
_mapParameterToGetArrayAtIndexTemplatePane = {} 
_mapParameterToGetArrayLengthTemplatePane = {} 

def _getSetTemplatePaneForVariable( variable ):
	if _mapVariableToSetTemplatePane.has_key( variable ):
		return _mapVariableToSetTemplatePane[ variable ]
	else:
		value = SetVariableTemplatePane( variable )
		_mapVariableToSetTemplatePane[ variable ] = value
		return value

def _getSetArrayAtIndexTemplatePaneForVariable( variable ):
	if _mapVariableToSetArrayAtIndexTemplatePane.has_key( variable ):
		return _mapVariableToSetArrayAtIndexTemplatePane[ variable ]
	else:
		value = SetVariableArrayAtIndexTemplatePane( variable )
		_mapVariableToSetArrayAtIndexTemplatePane[ variable ] = value
		return value
def _getSetArrayAtIndexTemplatePaneForParameter( parameter ):
	if _mapParameterToSetArrayAtIndexTemplatePane.has_key( parameter ):
		return _mapParameterToSetArrayAtIndexTemplatePane[ parameter ]
	else:
		value = SetParameterArrayAtIndexTemplatePane( parameter )
		_mapParameterToSetArrayAtIndexTemplatePane[ parameter ] = value
		return value

def _getGetArrayAtIndexTemplatePaneForVariable( variable ):
	if _mapVariableToGetArrayAtIndexTemplatePane.has_key( variable ):
		return _mapVariableToGetArrayAtIndexTemplatePane[ variable ]
	else:
		value = GetVariableArrayAtIndexTemplatePane( variable )
		_mapVariableToGetArrayAtIndexTemplatePane[ variable ] = value
		return value

def _getGetArrayLengthTemplatePaneForVariable( variable ):
	if _mapVariableToGetArrayLengthTemplatePane.has_key( variable ):
		return _mapVariableToGetArrayLengthTemplatePane[ variable ]
	else:
		value = GetVariableArrayLengthTemplatePane( variable )
		_mapVariableToGetArrayLengthTemplatePane[ variable ] = value
		return value

def _getGetArrayAtIndexTemplatePaneForParameter( parameter ):
	if _mapParameterToGetArrayAtIndexTemplatePane.has_key( parameter ):
		return _mapParameterToGetArrayAtIndexTemplatePane[ parameter ]
	else:
		value = GetParameterArrayAtIndexTemplatePane( parameter )
		_mapParameterToGetArrayAtIndexTemplatePane[ parameter ] = value
		return value

def _getGetArrayLengthTemplatePaneForParameter( parameter ):
	if _mapParameterToGetArrayLengthTemplatePane.has_key( parameter ):
		return _mapParameterToGetArrayLengthTemplatePane[ parameter ]
	else:
		value = GetParameterArrayLengthTemplatePane( parameter )
		_mapParameterToGetArrayLengthTemplatePane[ parameter ] = value
		return value

class SetVariableTemplatesOwnerPane( moot.ZLineAxisPane ):
	def __init__( self ):
		moot.ZLineAxisPane.__init__( self )
		self._method = None
	def refreshTemplates(self):
		templatePanesNeedingToBeAdded = []
		if self._method:
			for parameter in self._method.getParameters():
				if parameter.valueType.getValue().isArray():
					templatePanesNeedingToBeAdded.append( _getSetArrayAtIndexTemplatePaneForParameter( parameter ) )
					templatePanesNeedingToBeAdded.append( _getGetArrayAtIndexTemplatePaneForParameter( parameter ) )
					templatePanesNeedingToBeAdded.append( _getGetArrayLengthTemplatePaneForParameter( parameter ) )
			for variableDeclarationStatement in ecc.dennisc.alice.ast.getVariableDeclarationStatements( self._method ):
				v = variableDeclarationStatement.variable.getValue()
				templatePanesNeedingToBeAdded.append( _getSetTemplatePaneForVariable( v ) )
				if v.valueType.getValue().isArray():
					templatePanesNeedingToBeAdded.append( _getSetArrayAtIndexTemplatePaneForVariable( v ) )
					templatePanesNeedingToBeAdded.append( _getGetArrayAtIndexTemplatePaneForVariable( v ) )
					templatePanesNeedingToBeAdded.append( _getGetArrayLengthTemplatePaneForVariable( v ) )
				
		for component in self.getComponents():
			if component in templatePanesNeedingToBeAdded:
				templatePanesNeedingToBeAdded.remove( component )
			else:
				self.remove( component )

		for component in templatePanesNeedingToBeAdded:
			self.add( component )

	def handleMethodChange( self, method ):
		self._method = method
		self.refreshTemplates()

class UbiquitousTemplatesPane( ecc.dennisc.alice.ide.IDEListenerPane ):
	def __init__( self ):
		ecc.dennisc.alice.ide.IDEListenerPane.__init__( self )
		self._method = None

		self._doInOrderPane = DoInOrderTemplatePane()
		self._countLoopPane = CountLoopTemplatePane()
		self._forEachInArrayLoopPane = ForEachInArrayLoopTemplatePane()
		self._whileLoopPane = WhileLoopTemplatePane()
		
		self._conditionalStatementPane = ConditionalStatementTemplatePane()
		self._doTogetherPane = DoTogetherTemplatePane()
		self._forEachInArrayTogetherPane = EachInArrayTogetherTemplatePane()
		self._returnStatementOwnerPane = ReturnStatementOwnerPane()
		self._declareVariablePane = DeclareLocalTemplatePane()
		self._commentPane = CommentTemplatePane()
		self._mathFunctionsPane = MathFunctionsTemplatePane()
		self._setVariableTemplatesOwnerPane = SetVariableTemplatesOwnerPane()

		panes = [
			self._doInOrderPane,
			self._countLoopPane,
			self._whileLoopPane,
			self._forEachInArrayLoopPane,
			self._conditionalStatementPane,
			self._doTogetherPane,
			self._forEachInArrayTogetherPane,
			self._declareVariablePane,
			self._setVariableTemplatesOwnerPane,
			self._returnStatementOwnerPane,
			self._commentPane,
			self._mathFunctionsPane,
			javax.swing.JLabel()
		]

		boxLayout = javax.swing.BoxLayout( self, javax.swing.BoxLayout.LINE_AXIS )
		self.setLayout( boxLayout )
		for component in panes:
			#component.setMinimumSize( java.awt.Dimension( 0, 0 ) )
			self.add( component )
			self.add( javax.swing.Box.createHorizontalStrut( 4 ) )
#		self.setLayout( java.awt.GridBagLayout() )

#		springLayout = javax.swing.SpringLayout()
#		self.setLayout( springLayout )
#		prev = None
#		springLayout.putConstraint( javax.swing.SpringLayout.WEST, panes[ 0 ], 0, javax.swing.SpringLayout.WEST, self );
#		for next in panes:
#			#next.setMinimumSize( java.awt.Dimension( 0, 0 ) )
#			#next.setPreferredSize( java.awt.Dimension( 0, 0 ) )
#			self.add( next )
#			if prev:
#				#springLayout.putConstraint( javax.swing.SpringLayout.NORTH, next, 10, javax.swing.SpringLayout.NORTH, prev );
#				springLayout.putConstraint( javax.swing.SpringLayout.WEST, next, 4, javax.swing.SpringLayout.EAST, prev )
#			prev = next
#		springLayout.putConstraint( javax.swing.SpringLayout.EAST, panes[ -1 ], 0, javax.swing.SpringLayout.EAST, self );
		
		
#		row = java.util.ArrayList()
#		for component in _row:
#			row.add( component )
#		import jarray
#		rows = java.util.ArrayList()
#		rows.add( jarray.array( panes, java.awt.Component ) )
#		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( self, rows, 0, 0 )
		#self.setLayout( javax.swing.BoxLayout( self, javax.swing.BoxLayout.LINE_AXIS ) )

#		self.setLayout( java.awt.GridBagLayout() )
#		self._gbc = java.awt.GridBagConstraints()
#		self._gbc.anchor = java.awt.GridBagConstraints.WEST
#		self._gbc.fill = java.awt.GridBagConstraints.BOTH
#		self._gbc.gridwidth = 1
#		self._gbc.weightx = 1.0
#		self._gbc.weighty = 1.0
#
#		SMALL_FILLER = 2
#		LARGE_FILLER = 6
#		self._addWithFiller( self._doInOrderPane, LARGE_FILLER )
#		self._addWithFiller( self._countLoopPane, SMALL_FILLER )
#		self._addWithFiller( self._whileLoopPane, SMALL_FILLER )
#		self._addWithFiller( self._forEachInArrayLoopPane, LARGE_FILLER )
#		self._addWithFiller( self._conditionalStatementPane, LARGE_FILLER )
#		self._addWithFiller( self._doTogetherPane, SMALL_FILLER )
#		self._addWithFiller( self._forEachInArrayTogetherPane, LARGE_FILLER )
#		self._addWithFiller( self._declareVariablePane, SMALL_FILLER )
#		self._addWithFiller( self._setVariableTemplatesOwnerPane, LARGE_FILLER )
#
#		self._addWithFiller( self._returnStatementOwnerPane, LARGE_FILLER )
#		
#		self._addWithFiller( self._commentPane, LARGE_FILLER )
#
#		self._addWithFiller( self._mathFunctionsPane, LARGE_FILLER )
#		#self.add( javax.swing.Box.createHorizontalGlue() )
#
#		self._gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER
#		self._gbc.weightx = 10.0
#		self.add( javax.swing.JLabel(), self._gbc )
#		del self._gbc
	

	def _addWithFiller( self, component, filler ):
		self.add( component, self._gbc )
		self.add( javax.swing.Box.createHorizontalStrut( filler ), self._gbc )

#	def getMinimumSize( self ):
#		return java.awt.Dimension( 0, 32 )
#		#return self.getPreferredSize()

	def refreshVariableTemplates( self ):
		self._setVariableTemplatesOwnerPane.refreshTemplates()
		self.revalidate()

	def focusedCodeChanged( self, e ):
		method = e.getNextValue()
		self._returnStatementOwnerPane.handleMethodChange( method )
		self._setVariableTemplatesOwnerPane.handleMethodChange( method )
		self.revalidate()
		self.repaint()
