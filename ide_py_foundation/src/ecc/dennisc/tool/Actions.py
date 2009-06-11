import java
import javax

class Action( javax.swing.AbstractAction ):
	def __init__( self, actionHandler ):
		javax.swing.AbstractAction.__init__( self )
		self._actionHandler = actionHandler
	def actionPerformed( self, e ):
		self._actionHandler.handleActionPerformed( self, e )

class NewAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "New..." )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK ) )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_N )
class OpenAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Open..." )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK ) )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_O )
class SaveAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Save" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK ) )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_S )
class SaveAsAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Save As..." )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK | java.awt.event.InputEvent.SHIFT_MASK ) )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_A )
class RevertAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Revert..." )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_R )
class ExitAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Exit" )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_X )
class UndoAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Undo" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK ) )
class RedoAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Redo" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK ) )
class CutAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Cut" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK ) )
class CopyAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Copy" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK ) )
class PasteAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Paste" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK ) )
class HelpAction( Action ):
	def __init__( self, actionHandler ):
		Action.__init__( self, actionHandler )
		self.putValue( javax.swing.Action.NAME, "Help..." )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F1, 0 ) )
