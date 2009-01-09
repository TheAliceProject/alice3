#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import zoot

import ecc

from ecc.dennisc.alice.ide.IdentifierPane import IdentifierPane
from ecc.dennisc.alice.ide.TypeComboBox import TypeComboBox
from ecc.dennisc.alice.ide.PaneWithValidation import PaneWithValidation

from CreateDeclarationPane import CreateDeclarationPane
from CreateTypedDeclarationPane import CreateTypedDeclarationPane
from CreateTypedDeclarationWithInitializerPane import CreateTypedDeclarationWithInitializerPane



class CreateProcedurePane( CreateDeclarationPane ):
	def __init__( self, type ):
		CreateDeclarationPane.__init__( self, type.getDeclaredMethods() )

	def getActualInputValue( self ):
		return alice.ast.MethodDeclaredInAlice( self._getName(), alice.ast.TypeDeclaredInJava.VOID_TYPE, [], alice.ast.BlockStatement( [] ) )

class CreateFunctionPane( CreateTypedDeclarationPane ):
	def __init__( self, type ):
		CreateTypedDeclarationPane.__init__( self, type.getDeclaredMethods(), "return type:" )
	def getActualInputValue( self ):
		returnStatement = alice.ast.ReturnStatement( self._getType(), alice.ast.NullLiteral() )
		body = alice.ast.BlockStatement( [ returnStatement ] )
		return alice.ast.MethodDeclaredInAlice( self._getName(), self._getType(), [], body )

class WarningPane( zoot.ZPageAxisPane ):
	def __init__( self, invocationReferences ):
		zoot.ZPageAxisPane.__init__( self )
		self.add( javax.swing.JLabel( "There are invocations to this method in your program." ) )
		self.add( javax.swing.JLabel( "You will need to fill in argument values for this new parameter." ) )
		pane = zoot.ZLineAxisPane()
		pane.add( javax.swing.JLabel( "Look for:" ) )
		pane.add( alice.ide.editors.code.ExpressionPane( alice.ast.NullLiteral() ) )
		self.add( pane )

class MethodInvocationsNotificationPane( PaneWithValidation ):
	def __init__( self, invocationReferences ):
		PaneWithValidation.__init__( self )

		self.setBorder( javax.swing.border.EtchedBorder() )

		self._component = WarningPane( invocationReferences )

		self._checkbox = javax.swing.JCheckBox( "I understand that I need to update the invocations to this method." )
		self._checkbox.addItemListener( ecc.dennisc.swing.event.ItemAdapter( self._handleCheckBoxChange ) )

		self.setLayout( java.awt.BorderLayout() )
		self.add( self._component, java.awt.BorderLayout.NORTH )
		self.add( self._checkbox, java.awt.BorderLayout.SOUTH )

	def _handleCheckBoxChange( self, e ):
		self.updateOKButton()

	def isValid( self ):
		return self._checkbox.isSelected()

class CreateParameterPane( CreateTypedDeclarationPane ):
	def __init__( self, siblings, invocationReferences ):
		self._invocationReferences = invocationReferences
		CreateTypedDeclarationPane.__init__( self, siblings, "type:" )

	def _createComponentRows( self ):
		rv = CreateTypedDeclarationPane._createComponentRows( self )
		if len( self._invocationReferences ):
			pane = MethodInvocationsNotificationPane( self._invocationReferences )
			pane.setInputPane( self )
			self.addOKButtonValidator( pane )
			rv.append( ( javax.swing.JLabel( "WARNING:", javax.swing.JLabel.TRAILING ), pane ) )
		return rv

	def getActualInputValue( self ):
		return alice.ast.ParameterDeclaredInAlice( self._getName(), self._getType() )

class CreateFieldPane( CreateTypedDeclarationWithInitializerPane ):
	def __init__( self, type ):
		CreateTypedDeclarationWithInitializerPane.__init__( self, type.getDeclaredFields(), "type:" )
	def getActualInputValue( self ):
		rv = alice.ast.FieldDeclaredInAlice( self._getName(), self._getType(), self._getInitializer() )
		if self._isConstantVC.isSelected():
			rv.finalVolatileOrNeither.setValue( alice.ast.FieldModifierFinalVolatileOrNeither.FINAL )
		return rv

class CreateLocalPane( CreateTypedDeclarationWithInitializerPane ):
	def __init__( self, method ):
		siblings = []
		#todo: fill in siblings with parameters and local variables
		CreateTypedDeclarationWithInitializerPane.__init__( self, siblings, "type:" )

#	def _createComponentRows( self ):
#		rv = CreateTypedDeclarationWithInitializerPane._createComponentRows( self )
#		self._isConstantVC = javax.swing.JCheckBox( "is constant" )
#		rv.append( ( javax.swing.JLabel( "", javax.swing.JLabel.TRAILING ), self._isConstantVC ) )
#		return rv

	def getActualInputValue( self ):
		if self._isConstantVC.isSelected():
			constant = alice.ast.ConstantDeclaredInAlice( self._getName(), self._getType() )
			return alice.ast.ConstantDeclarationStatement( constant, self._getInitializer() )
		else:
			variable = alice.ast.VariableDeclaredInAlice( self._getName(), self._getType() )
			return alice.ast.VariableDeclarationStatement( variable, self._getInitializer() )

#print "<--", __name__
