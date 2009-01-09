import ecc

def createAndShowIDE():
	ide = ecc.dennisc.alice.ide.storytelling.StorytellingIDE()
	ide.setSize( 1280, 1024 )
	import sys
	ide.handleArgs( sys.argv[1:] )
	ide.maximize()
	ide.setVisible( True )
	
import javax
javax.swing.SwingUtilities.invokeLater( ecc.dennisc.lang.ApplyRunnable( createAndShowIDE ) )

