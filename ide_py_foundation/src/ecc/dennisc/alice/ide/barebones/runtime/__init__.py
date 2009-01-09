#print "-->", __name__

import javax
import ecc

class RunOperation( ecc.dennisc.alice.ide.operations.run.AbstractRunOperation ):
	def perform(self):
		javax.swing.JOptionPane.showMessageDialog( None, "imagine the program running here" )

#print "<--", __name__
