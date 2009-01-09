class ProgramWithSceneMixin:
	def __init__( self ):
		self._programType = None
		
	def getProgramType( self ):
		return self._programType
	def setProgramType( self, programType ):
		self._programType = programType

	def getSceneField( self ):
		#todo
		return self._programType.fields.get( 0 )

	def getSceneType( self ):
		return self.getSceneField().getValueType()

	def getSceneAutomaticSetUpMethod( self ):
		return self.getSceneType().getDeclaredMethod( "performSceneEditorGeneratedSetUp", [] )

	def getFilledInSceneAutomaticSetUpMethod( self, fillerInner ):
		rv = self.getSceneAutomaticSetUpMethod()
		#todo
		map = self._mapFieldToInstance
		fillerInner.fillInSceneAutomaticSetUpMethod( rv, self.getSceneField(), map )
		return rv

