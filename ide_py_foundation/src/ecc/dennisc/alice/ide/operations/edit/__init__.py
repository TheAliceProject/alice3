import java
import javax

from edu.cmu.cs.dennisc import alice

class UndoOperation( alice.ide.AbstractOperation ):
	def __init__( self ):
		alice.ide.AbstractOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Undo" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK ) )
	def prepare( self, e, observer ):
		raise self
	def perform( self ):
		raise self

class RedoOperation( alice.ide.AbstractOperation ):
	def __init__( self ):
		alice.ide.AbstractOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Redo" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK ) )
	def prepare( self, e, observer ):
		raise self
	def perform( self ):
		raise self

class CutOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self ):
		alice.ide.AbstractUndoableOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Cut" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK ) )
	def prepare( self, e, observer ):
		raise self
	def perform( self ):
		raise self
	def undo( self ):
		raise self
	def redo( self ):
		raise self

class CopyOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self ):
		alice.ide.AbstractUndoableOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Copy" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK ) )
	def prepare( self, e, observer ):
		raise self
	def perform( self ):
		raise self
	def undo( self ):
		raise self
	def redo( self ):
		raise self

class PasteOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self ):
		alice.ide.AbstractUndoableOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Paste" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK ) )
	def prepare( self, e, observer ):
		raise self
	def perform( self ):
		raise self
	def undo( self ):
		raise self
	def redo( self ):
		raise self
