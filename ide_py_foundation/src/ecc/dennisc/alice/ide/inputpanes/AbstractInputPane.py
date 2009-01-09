#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

class AbstractInputPane( edu.cmu.cs.dennisc.swing.InputPane ):
	def __init__( self ):
		edu.cmu.cs.dennisc.swing.InputPane.__init__( self )
	def showInJDialog(self, owner=None, title=None, isModal=True):
		if owner:
			pass
		else:
			owner = alice.ide.IDE.getSingleton()
		return edu.cmu.cs.dennisc.swing.InputPane.showInJDialog( self, owner, title, isModal )
	
#print "<--", __name__
