import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

from ControlsForOverlayPane import ControlsForOverlayPane

class BarebonesScenePane( alice.ide.editors.scene.AbstractSceneEditor ):
	def __init__( self ):
		alice.ide.editors.scene.AbstractSceneEditor.__init__( self )
		self._controlsForOverlayPane = ControlsForOverlayPane()
		self.setLayout( java.awt.GridLayout( 1, 1 ) )
		self.add( self._controlsForOverlayPane )

	def setSceneField( self, sceneField ):
		alice.ide.editors.scene.AbstractSceneEditor.setSceneField( self, sceneField )
		self._controlsForOverlayPane.setRootField( sceneField )

	def handleExpandContractChange( self, isExpanded ):
		self._controlsForOverlayPane.handleExpandContractChange( isExpanded )
	
	def generateCodeForSceneSetUp( self, setUpMethodGenerator ):
		pass

	def setDragInProgress(self, isDragInProgress):
		pass

	def preserveProjectProperties( self, projectProperties ):
		pass
