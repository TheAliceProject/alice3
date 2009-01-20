#print "-->", __name__

import java
import javax
import edu
import org

from edu.cmu.cs.dennisc import alice

import ecc

from MoveAndTurnSceneAutomaticSetUpMethodFillerInner import MoveAndTurnSceneAutomaticSetUpMethodFillerInner

class RunOperation( ecc.dennisc.alice.ide.operations.run.AbstractRunOperation ):
	def perform(self):
		ide = self.getIDE()
		ide.generateCodeForSceneSetUp()
		program = ide.createRuntimeProgram()
		program.showInJDialog( ide, True, [] )

class MoveAndTurnIDE( ecc.dennisc.alice.ide.barebones.BarebonesIDE ):
	def __init__( self ):
		ecc.dennisc.alice.ide.barebones.BarebonesIDE.__init__( self )
		
	def _createSceneSetUpCodeGenerator( self ):
		return MoveAndTurnSceneAutomaticSetUpMethodFillerInner()

	def _createScenePane( self ):
		return ecc.dennisc.alice.ide.moveandturn.editors.scene.MoveAndTurnSceneEditor()

	def _getResourcesRootDirectory( self ):
		if self.__dict__.has_key( "_resourcesRootDirectory" ):
			pass
		else:
			self._resourcesRootDirectory = org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory()
		return self._resourcesRootDirectory

	def _getGalleryThumbnailsRoot( self ):
		return java.io.File( self._getResourcesRootDirectory(), "thumbnails/" )
	def _getGalleryThumbnailsSubPath( self ):
		return ""
	def _getGalleryThumbnailsMap( self ):
		return { "thumbnails" : "gallery", "org.alice.apis.moveandturn.gallery" : "Generic Alice Models", "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters" : "Looking Glass Characters", "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes" : "Looking Glass Scenery"  }

	def _createGalleryBrowser( self ):
		return ecc.dennisc.alice.ide.moveandturn.editors.gallery.GalleryBrowser( self._getGalleryThumbnailsRoot(), self._getGalleryThumbnailsSubPath(), self._getGalleryThumbnailsMap() )

#	def _setUpContentPane( self ):
#		ecc.dennisc.alice.ide.BarebonesIDE._setUpContentPane( self )
#		self._sceneEdidor.showInAWTContainer( self._sceneEdidorContainer, [] )

	def createVirtualMachineForSceneEditor( self ):
		return edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine()
	def createVirtualMachineForRuntimeProgram( self ):
		return edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine()

	def _createRuntimeProgram(self, vm, sceneType ):
		return ecc.dennisc.alice.ide.moveandturn.runtime.MoveAndTurnRuntimeProgram( vm, sceneType )
	def createRuntimeProgram(self):
		return self._createRuntimeProgram( self.createVirtualMachineForRuntimeProgram(), self.getSceneType() )

	def createRunOperation( self ):
		return RunOperation()

#print "<--", __name__
