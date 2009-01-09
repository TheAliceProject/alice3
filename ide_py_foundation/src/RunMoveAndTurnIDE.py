import java
import javax
import edu

import ecc


def createAndShowIDE():
	ide = ecc.dennisc.alice.ide.moveandturn.MoveAndTurnIDE()
	ide.setSize( 1280, 1024 )
	import sys
	ide.handleArgs( sys.argv[1:] )
	ide.maximize()
	ide.setVisible( True )
	
javax.swing.SwingUtilities.invokeLater( ecc.dennisc.lang.ApplyRunnable( createAndShowIDE ) )

