import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

from AbstractStatementTemplatePane import AbstractStatementTemplatePane

class DeclareLocalOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, method ):
		self._method = method
		self.putValue( javax.swing.Action.NAME, "create new local..." )

	def prepare( self, e, observer ):
		inputPane = ecc.dennisc.alice.ide.inputpanes.CreateLocalPane( self._method )
		self._local = inputPane.showInJDialog( owner=self.getIDE(), title="Create Local" )
		if self._local:
			return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
		else:
			return alice.ide.Operation.PreparationResult.CANCEL

	def perform( self ):
		self.redo()
	def undo( self ):
		print "todo: remove", self._local
	def redo( self ):
		print "todo: add", self._local

class DeclareLocalTemplatePane( AbstractStatementTemplatePane ):
	def __init__( self ):
		AbstractStatementTemplatePane.__init__( self, alice.ast.VariableDeclarationStatement )
	def _createEmptyStatementArgs( self ):
		type = ecc.dennisc.alice.ast.getType( java.lang.Object )
		variable = alice.ast.VariableDeclaredInAlice( "???", type )
		initializer = alice.ide.editors.code.EmptyExpression( type )
		return [ variable, initializer ]
	def _createActualStatementArgs( self ):
		raise "never invoked"
	def createStatement( self, e ):
		method = None

		op = DeclareLocalOperation( method )
		op.prepare( None, None )
		if op._local:
			op.getIDE().markChanged( "declare local" )
		return op._local

