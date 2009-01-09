import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import zoot

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

class SetVariableTemplatePane( CascadingUbiquitousStatementTemplatePane ):
	def __init__( self, variable ):
		self._variable = variable
		CascadingUbiquitousStatementTemplatePane.__init__( self, alice.ast.ExpressionStatement, variable.valueType.getValue() )
	def createComponent(self):
		return zoot.ZLabel( self._variable.getName() + " <-- " )
	def _createArgs( self, expression ):
		return [ alice.ast.AssignmentExpression( self._variable.valueType.getValue(), alice.ast.VariableAccess( self._variable ), alice.ast.AssignmentExpression.Operator.ASSIGN, expression ) ]


_mapVariableToTemplatePane = {} 
def _getTemplatePaneForVariable( variable ):
	if _mapVariableToTemplatePane.has_key( variable ):
		return _mapVariableToTemplatePane[ variable ]
	else:
		value = SetVariableTemplatePane( variable )
		_mapVariableToTemplatePane[ variable ] = value
		return value

class SetVariableTemplatesOwnerPane( zoot.ZLineAxisPane ):
	def __init__( self ):
		zoot.ZLineAxisPane.__init__( self )
		self._method = None
	def refreshTemplates(self):
		templatePanesNeedingToBeAdded = []
		if self._method:
			for variableDeclarationStatement in ecc.dennisc.alice.ast.getVariableDeclarationStatements( self._method ):
				templatePanesNeedingToBeAdded.append( _getTemplatePaneForVariable( variableDeclarationStatement.variable.getValue() ) )
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
		self._setVariableTemplatesOwnerPane = SetVariableTemplatesOwnerPane()
		self._commentPane = CommentTemplatePane()
		self._mathFunctionsPane = MathFunctionsTemplatePane()

		self.setLayout( javax.swing.BoxLayout( self, javax.swing.BoxLayout.LINE_AXIS ) )

		SMALL_FILLER = 2
		LARGE_FILLER = 6
		self._addWithFiller( self._doInOrderPane, LARGE_FILLER )
		self._addWithFiller( self._countLoopPane, SMALL_FILLER )
		self._addWithFiller( self._whileLoopPane, SMALL_FILLER )
		self._addWithFiller( self._forEachInArrayLoopPane, LARGE_FILLER )
		self._addWithFiller( self._conditionalStatementPane, LARGE_FILLER )
		self._addWithFiller( self._doTogetherPane, SMALL_FILLER )
		self._addWithFiller( self._forEachInArrayTogetherPane, LARGE_FILLER )
		self._addWithFiller( self._declareVariablePane, SMALL_FILLER )
		self._addWithFiller( self._setVariableTemplatesOwnerPane, LARGE_FILLER )

		self._addWithFiller( self._returnStatementOwnerPane, LARGE_FILLER )
		
		self._addWithFiller( self._commentPane, LARGE_FILLER )

		self._addWithFiller( self._mathFunctionsPane, LARGE_FILLER )
		#self.add( javax.swing.Box.createHorizontalGlue() )
	

	def _addWithFiller( self, component, filler ):
		self.add( component )
		self.add( javax.swing.Box.createHorizontalStrut( filler ) )

	def getMinimumSize( self ):
		return self.getPreferredSize()

	def refreshVariableTemplates( self ):
		self._setVariableTemplatesOwnerPane.refreshTemplates()
		self.revalidate()

	def focusedCodeChanged( self, e ):
		method = e.getNextValue()
		self._returnStatementOwnerPane.handleMethodChange( method )
		self._setVariableTemplatesOwnerPane.handleMethodChange( method )
		self.revalidate()
		self.repaint()
