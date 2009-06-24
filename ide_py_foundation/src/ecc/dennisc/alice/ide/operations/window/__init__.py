import java
import javax

from edu.cmu.cs.dennisc import alice

#todo: remove Undoable
class SetLocaleExceptionOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, nextLocale ):
		alice.ide.AbstractOperation.__init__( self )
		self._nextLocale = nextLocale
		self.putValue( javax.swing.Action.NAME, self._nextLocale.getDisplayName() )
	def prepare( self, e, observer ):
		self._prevLocale = self.getIDE().getLocale()
		return alice.ide.Operation.PreparationResult.PERFORM
	def perform( self ):
		self.redo()
	def undo( self ):
		self.getIDE().setLocale( self._prevLocale )
	def redo( self ):
		self.getIDE().setLocale( self._nextLocale )

class RaiseExceptionOperation( alice.ide.AbstractOperation ):
	def __init__( self ):
		alice.ide.AbstractOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "throw new BogusException()" )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_R )
	def prepare( self, e, observer ):
		raise "Bogus Exception"
	def perform( self ):
		pass

class ToggleExpandContractSceneEditorOperation( alice.ide.AbstractOperation ):
	def __init__( self ):
		alice.ide.AbstractOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Expand/Contract Scene Editor" )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_X )
	def prepare( self, e, observer ):
		return alice.ide.Operation.PreparationResult.PERFORM
	def perform( self ):
		self.getIDE().toggleExpandContactOfSceneEditor();
