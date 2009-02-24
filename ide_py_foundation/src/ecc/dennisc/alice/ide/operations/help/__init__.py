import java
import javax
import edu

from edu.cmu.cs.dennisc import alice


def getHelpText():
	rv = "one can copy by dragging with the "
	if edu.cmu.cs.dennisc.lang.SystemUtilities.isMac():
		rv += "Alt"
	else:
		rv += "Control"
	rv += " key pressed."
	return rv


class HelpOperation(alice.ide.AbstractOperation):
	def __init__(self):
		alice.ide.AbstractOperation.__init__(self)
		self.putValue( javax.swing.Action.NAME, "Help..." )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F1, 0 ) )
	def prepare(self, e, observer):
		title = "Help coming soon"
		message = getHelpText()
		javax.swing.JOptionPane.showMessageDialog( self.getIDE(), message, title, javax.swing.JOptionPane.INFORMATION_MESSAGE ) 
		return alice.ide.Operation.PreparationResult.CANCEL
	def perform(self):
		pass

class AboutOperation(alice.ide.AbstractOperation):
	def __init__(self):
		alice.ide.AbstractOperation.__init__(self)
		self.putValue( javax.swing.Action.NAME, "About..." )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_A )
	def prepare(self, e, observer):
		return alice.ide.Operation.PreparationResult.PERFORM
	def perform(self):
		aboutPane = alice.ide.about.AboutPane();
		javax.swing.JOptionPane.showMessageDialog( self.getIDE(), aboutPane, "About", javax.swing.JOptionPane.PLAIN_MESSAGE );
		#s = "Current Version: %s\n\nAlice and the Move and Turn API designed and implemented by Dennis Cosgrove http://www.cs.cmu.edu/~dennisc/\nLooking Glass and the Walk and Touch API designed and implemented by Caitlin Kelleher http://www.cse.wustl.edu/~ckelleher/" % edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText()
		#javax.swing.JOptionPane.showMessageDialog( self.getIDE(), s, "About", javax.swing.JOptionPane.PLAIN_MESSAGE )
