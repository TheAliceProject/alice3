import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

def getBonusCopyText():
	rv = "\n\nNOTE: one can copy by dragging with the "
	if edu.cmu.cs.dennisc.lang.SystemUtilities.isMac():
		rv += "Alt"
	else:
		rv += "Control"
	rv += " key pressed."
	return rv

class UndoOperation( alice.ide.AbstractOperation ):
	def __init__( self ):
		alice.ide.AbstractOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Undo" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_Z, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) )
	def prepare( self, e, observer ):
		title = "Undo coming soon"
		message = "Undo is not yet implemented.  Apologies."
		javax.swing.JOptionPane.showMessageDialog( self.getIDE(), message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE ) 
		return alice.ide.Operation.PreparationResult.CANCEL
	def perform( self ):
		raise self

class RedoOperation( alice.ide.AbstractOperation ):
	def __init__( self ):
		alice.ide.AbstractOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Redo" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_Y, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) )
	def prepare( self, e, observer ):
		title = "Redo coming soon"
		message = "Redo is not yet implemented.  Apologies."
		javax.swing.JOptionPane.showMessageDialog( self.getIDE(), message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE ) 
		return alice.ide.Operation.PreparationResult.CANCEL
	def perform( self ):
		raise self

class CutOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self ):
		alice.ide.AbstractUndoableOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Cut" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_X, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) )
	def prepare( self, e, observer ):
		title = "Cut coming soon"
		message = "Cut is not yet implemented.  Apologies."
		javax.swing.JOptionPane.showMessageDialog( self.getIDE(), message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE ) 
		return alice.ide.Operation.PreparationResult.CANCEL
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
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_C, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) )
	def prepare( self, e, observer ):
		title = "Copy coming soon"
		message = "Copy is not yet implemented.  Apologies."  + getBonusCopyText()
		javax.swing.JOptionPane.showMessageDialog( self.getIDE(), message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE ) 
		return alice.ide.Operation.PreparationResult.CANCEL
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
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_V, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) )
	def prepare( self, e, observer ):
		title = "Paste coming soon"
		message = "Paste is not yet implemented.  Apologies."
		javax.swing.JOptionPane.showMessageDialog( self.getIDE(), message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE ) 
		return alice.ide.Operation.PreparationResult.CANCEL
	def perform( self ):
		raise self
	def undo( self ):
		raise self
	def redo( self ):
		raise self
