#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

class IDEListenerPane( javax.swing.JPanel, alice.ide.event.IDEListener ):
	def __init__( self ):
		javax.swing.JPanel.__init__( self )
		self.getIDE().addIDEListener( self )
		#self.setBorder( javax.swing.BorderFactory.createEmptyBorder() )
	
	def getIDE(self):
		return alice.ide.IDE.getSingleton()
	
	def localeChanging( self, e ):
		pass
	def localeChanged( self, e ):
		pass
	def fieldSelectionChanging( self, e ):
		pass
	def fieldSelectionChanged( self, e ):
		pass
	def transientSelectionChanging( self, e ):
		pass
	def transientSelectionChanged( self, e ):
		pass
	def focusedCodeChanging( self, e ):
		pass
	def focusedCodeChanged( self, e ):
		pass
	def projectOpening( self, e ):
		pass
	def projectOpened( self, e ):
		pass

#print "<--", __name__
