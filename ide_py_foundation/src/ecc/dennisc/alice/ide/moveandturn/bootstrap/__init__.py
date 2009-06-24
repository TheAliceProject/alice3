import java
import edu
from edu.cmu.cs.dennisc import alice
from org.alice import apis

import ecc

class MyCamera( apis.moveandturn.SymmetricPerspectiveCamera ):
	pass
class MyGrassyGround( apis.moveandturn.gallery.environments.grounds.GrassyGround ):
	pass
class MySunLight( apis.moveandturn.DirectionalLight ):
	pass

class DefaultScene( apis.moveandturn.Scene ):
	def __init__( self ):
		apis.moveandturn.Scene.__init__( self )
		self.setName( "scene" )
		self.camera = MyCamera()
		#self.camera = apis.moveandturn.SymmetricPerspectiveCamera()
		self.camera.turn( apis.moveandturn.TurnDirection.RIGHT, apis.moveandturn.AngleInRevolutions( 0.625 ) )
		self.camera.turn( apis.moveandturn.TurnDirection.FORWARD, apis.moveandturn.AngleInRevolutions( 0.025 ) )
		self.camera.move( apis.moveandturn.MoveDirection.BACKWARD, 8 )

		self.sunLight = MySunLight()
		self.sunLight.turn( apis.moveandturn.TurnDirection.FORWARD, apis.moveandturn.AngleInRevolutions( 0.25 ) )

	def performSceneEditorGeneratedSetUp( self ):
		pass
	def performCustomPropertySetUp( self ):
		pass
	def performSetUp( self ):
		pass
	def run( self ):
		pass

class DefaultSceneWithGrassyGround( DefaultScene ):
	def __init__( self ):
		DefaultScene.__init__( self )
		self.grassyGround = MyGrassyGround()

class MoveAndTurnBootstrap( ecc.dennisc.alice.ide.bootstrap.Bootstrap, ecc.dennisc.alice.ide.moveandturn.MoveAndTurnSceneAutomaticSetUpMethodFillerInner ):
	def __init__( self, scene ):
		ecc.dennisc.alice.ide.bootstrap.Bootstrap.__init__( self, scene, apis.moveandturn.Program )
		ecc.dennisc.alice.ide.moveandturn.MoveAndTurnSceneAutomaticSetUpMethodFillerInner.__init__( self )
