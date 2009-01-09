from org.alice import apis
import edu

class AbstractViewer( apis.moveandturn.Program ):
	def __init__( self ):
		apis.moveandturn.Program.__init__( self )
		self._scene = apis.moveandturn.Scene()
		self._camera = apis.moveandturn.SymmetricPerspectiveCamera()
		self._sunLight = apis.moveandturn.DirectionalLight()
	def isSpeedMultiplierControlPanelDesired( self ):
		return False
	def initialize( self ):
		self._scene.addComponent( self._camera )
		self._scene.addComponent( self._sunLight )
		self._sunLight.turn( apis.moveandturn.TurnDirection.FORWARD, apis.moveandturn.AngleInRevolutions( 0.25 ) )
		self.setScene( self._scene )
	def run( self ):
		pass

class ModelViewer( AbstractViewer ):
	def __init__( self ):
		AbstractViewer.__init__( self )
		self._model = None
	def getModel( self ):
		return self._model
	def setModel( self, model ):
		if self._model:
			self._scene.removeComponent( self._model )
		self._model = model
		if self._model:
			self._scene.addComponent( self._model )
