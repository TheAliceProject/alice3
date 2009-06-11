#print "-->", __name__

import java
import javax
import edu
import org

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import awt
from org.alice import apis
from edu.cmu.cs.dennisc import lookingglass

import ecc

class RuntimeProgramMixin:
	def __init__( self, vm, sceneType ):
		self._sceneType = sceneType
		self._vm = vm

	def setUp( self ):
		#print "pre constructor"
		self._scene = self._vm.createInstanceEntryPoint( self._sceneType )
		#print "post constructor"
#		self._vm.invokeEntryPoint( self._sceneType.getDeclaredMethod( "performSetUp", [] ), self._scene, [] )

	def run( self ):
		self._vm.invokeEntryPoint( self._sceneType.getDeclaredMethod( "run", [] ), self._scene, [] )

class MoveAndTurnRuntimeProgram( apis.moveandturn.Program, RuntimeProgramMixin ):
	def __init__( self, vm, sceneType, restartOperation ):
		self._restartOperation = restartOperation
		apis.moveandturn.Program.__init__( self )
		RuntimeProgramMixin.__init__( self, vm, sceneType )
		
	def initialize( self ):
		self.setUp()
		sceneInJava = self._scene.getInstanceInJava()
		self.setScene( sceneInJava )
		
		for component in sceneInJava.getComponents():
			if isinstance( component, org.alice.apis.moveandturn.Model ):
				model = component
				sgTransformable = model.getSGTransformable()
				if org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor.isGround( model ):
					sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.GROUND )
				else:
					sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS )
					sgTransformable.putBonusDataFor( org.alice.interact.GlobalDragAdapter.BOUNDING_BOX_KEY, model.getAxisAlignedMinimumBoundingBox() )
		
		self.globalDragAdapter = org.alice.interact.GlobalDragAdapter()
		self.globalDragAdapter.setOnscreenLookingGlass( self.getOnscreenLookingGlass() )
		#self.cameraNavigationDragAdapter = edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter()
		#self.cameraNavigationDragAdapter.setOnscreenLookingGlass( self.getOnscreenLookingGlass() );
	
	def isRestartSupported(self):
		return self._restartOperation
	
	def restart(self):
		if self._restartOperation:
			javax.swing.SwingUtilities.getRoot( self ).setVisible( False )
			ecc.dennisc.alice.ide.getIDE().performIfAppropriate( self._restartOperation, None )

#print "<--", __name__
