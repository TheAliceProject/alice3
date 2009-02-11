from javax.swing import SwingUtilities
import java
import javax
import edu
import org

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import awt
from org.alice import apis
from edu.cmu.cs.dennisc import lookingglass

import ecc

class BogusSelectionOperation( alice.ide.AbstractUndoableOperation ):
	def __init__(self, nextSelection, prevSelection ):
		alice.ide.AbstractUndoableOperation.__init__( self )
		self._nextSelection = nextSelection
		self._prevSelection = prevSelection
	def prepare(self, e, observer):
		return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
	def perform(self):
		self.redo()
	def redo(self):
		self.getIDE().setFieldSelection( self._nextSelection )
	def undo(self):
		self.getIDE().setFieldSelection( self._prevSelection )
	

class MoveAndTurnSceneEditor( org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor ):
	def __init__( self, isLightweightDesired ):
		org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor.__init__( self )
		self._sceneField = None
		self._currSelectedField = None
		self._mapFieldToInstance = {}
		self._mapInstanceInJavaToField = {}
		self._typeMap = {}
		print "isLightweightDesired paramater not used", isLightweightDesired

	def createControlsForOverlayPane( self ):
		return ecc.dennisc.alice.ide.barebones.editors.scene.ControlsForOverlayPane()
	def handleExpandContractChange( self, isExpanded ):
		print "todo: handleExpandContractChange", isExpanded
		#self._controlsForOverlayPane.handleExpandContractChange( isExpanded )
	
	def getScene(self):
		return self.getProgram().getScene()
	
	def getSceneType( self ):
		return self.getSceneField().getValueType()

	def _isFieldNameFree( self, name ):
		sceneType = self.getSceneType()
		if sceneType:
			for field in sceneType.fields.iterator():
				if field.getName() == name:
					return False
		return True 

	def _getAvailableFieldName( self, superClassBaseName ):
		name = superClassBaseName[ 0 ].lower() + superClassBaseName[ 1: ]
		rv = name
		i = 2
		while not self._isFieldNameFree( rv ):
			rv = name + `i`
			i += 1
		return rv

	def _handleCreateInstance( self, type ):
		#todo
		t = type.getFirstTypeEncounteredDeclaredInJava()
		cls = t.getCls()
		clsName = cls.__name__
		thumbnailsRoot = self._api._getGalleryThumbnailsRoot()
		
		prefixList = []
		prefixList.append( "org.alice.apis.moveandturn.gallery" )
		prefixList.append( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery" )
		for prefix in prefixList:
			if clsName.startswith( prefix ):
				file = java.io.File( thumbnailsRoot, prefix + clsName[ len( prefix ): ].replace( ".", "/" ) + ".png" )

		inputPane = ecc.dennisc.alice.ide.moveandturn.editors.gallery.CreateInstancePane( self, file, thumbnailsRoot, type )
		owner = alice.ide.IDE.getSingleton()
		instance = inputPane.showInJDialog( owner, "Create Instance", True )
		if instance:
			self.addInstance( instance )

	

	def createScene( self, sceneField ):

		if self._sceneField:
			program = self.getProgram()
			lg = program.getOnscreenLookingGlass()
			program.setScene( None )
			lg.removeCamera( lg.getCameraAt( 0 ) )
			#self._sceneFieldsComposite.setListData( [] )

		self._sceneField = sceneField
		self._typeMap = {}
		sceneInstance = org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor.createScene( self, sceneField )
		if self._sceneField:
			sceneType = self._sceneField.getValueType()

			self.addASTField( sceneField, sceneInstance )
			for field in sceneType.fields.iterator():
				self.addASTField( field, self.getVM().get( field, sceneInstance ) )
				self._registerType( field.getValueType() )
			#self._sceneFieldsComposite.setSelectedValue( field )

			#self.getProgram().setScene( sceneInstance.getInstanceInJava() )
			#self.getContentPane().revalidate()
			self.restoreProjectProperties()
			
		return sceneInstance

	def getSceneAutomaticSetUpMethod( self ):
		return self.getSceneType().getDeclaredMethod( "performSceneEditorGeneratedSetUp", [] )

	def getFilledInSceneAutomaticSetUpMethod( self, fillerInner ):
		rv = self.getSceneAutomaticSetUpMethod()
		#todo
		map = self._mapFieldToInstance
		fillerInner.fillInSceneAutomaticSetUpMethod( rv, self.getSceneField(), map )
		return rv

	def generateCodeForSceneSetUp( self, setUpMethodGenerator ):
		self.getFilledInSceneAutomaticSetUpMethod( setUpMethodGenerator )

	def _createSelectionOperation(self, nextField, prevField ):
		return BogusSelectionOperation( nextField, prevField )

	def _registerType( self, type ):
		self._typeMap[ type.getName() ] = type

	def _getGoodLookAtShowInstanceAndReturnCamera( self, camera, instance ):
		self.pushAndSetCameraNavigationDragAdapterEnabled( False )
		try:
			instance.setOpacity( 0.0, apis.moveandturn.Composite.RIGHT_NOW )
			pov = camera.getPointOfView( self.getScene() )
			camera.getGoodLookAt( instance, 0.5 )
			self.getScene().addComponent( instance )
			instance.setOpacity( 1.0 )
			camera.moveAndOrientTo( self.getScene().createOffsetStandIn( pov.getInternal() ), 0.5 ) 
		finally:
			self.popCameraNavigationDragAdapterEnabled()


	def addInstance( self, instance ):
		self.addASTFieldFor( instance )
		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
		if isinstance( instanceInJava, apis.moveandturn.Model ):
			camera = self.getScene().findFirstMatch( apis.moveandturn.AbstractCamera )
			if camera:
				java.lang.Thread( ecc.dennisc.lang.ApplyRunnable( self._getGoodLookAtShowInstanceAndReturnCamera, ( camera, instanceInJava, ) ) ).start()
			else:
				self.getScene().addComponent( instanceInJava )
		else:
			self.getScene().addComponent( instanceInJava )
		alice.ide.IDE.getSingleton().markChanged( "scene program addInstance" )

	def addASTField( self, astField, instance ):
		#self.getScene()FieldsComposite._addDatum( astField )
		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
		self._mapFieldToInstance[ astField ] = instance
		self._mapInstanceInJavaToField[ instanceInJava ] = astField
		
		if isinstance( instanceInJava, apis.moveandturn.Model ):
			print instanceInJava
			instanceInJava.getSGComposite().putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS )
			instanceInJava.getSGComposite().putBonusDataFor( org.alice.interact.GlobalDragAdapter.BOUNDING_BOX_KEY, instanceInJava.getAxisAlignedMinimumBoundingBox())
		
		

	def handleDelete(self, node):
		print "handleDelete:", node
		if self._mapFieldToInstance.has_key( node ):
			instance = self._mapFieldToInstance[ node ]
			self.getScene().removeComponent( ecc.dennisc.alice.vm.getInstanceInJava( instance ) )
			#print "handleDelete: (instance)", instance

	def addASTFieldFor( self, instance ):
		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
		name = instanceInJava.getName()
		#programType = alice.ast.TypeDeclaredInJava.get( nameable.__class__ )
		type = instance.getType()
		astField = alice.ast.FieldDeclaredInAlice( name, type, alice.ast.InstanceCreation( type.getDeclaredConstructor( [] ), [] ) )
		astField.finalVolatileOrNeither.setValue( alice.ast.FieldModifierFinalVolatileOrNeither.FINAL )
		astField.access.setValue( alice.ast.Access.PRIVATE )
		self.getSceneType().fields.add( [ astField ] )
		self.addASTField( astField, instance )
		self._registerType( type )
		#self._sceneFieldsComposite.setSelectedValue( astField )
		self._select( astField )

	def removeASTField( self, astField ):
		#instance = self._mapFieldToInstance[ astField ]
		sceneType = self.getSceneType()
		index = sceneType.fields.indexOf( astField )
		if index != - 1:
			sceneType.fields.remove( index )
		else:
			print astField
			raise sceneType
		#self._sceneFieldsComposite._removeDatum( astField )
		instance = self._mapFieldToInstance[ astField ]
		self._scene.removeComponent( ecc.dennisc.alice.vm.getInstanceInJava( instance ) )
	
	def _select(self, nextField ):
		prevField = self._currSelectedField
		operation = self._createSelectionOperation( nextField, prevField )
		event = None
		alice.ide.IDE.getSingleton().performIfAppropriate( operation, event )
		
	def handleModelSelection( self, sgModel ):
		if sgModel:
			model = apis.moveandturn.Element.getElement( sgModel )
			if model:
				try:
					nextField = self._mapInstanceInJavaToField[ model ]
					self._select( nextField )
				except KeyError:
					print "could not find field for", instance

#	def valueChanged( self, e ):
#		field = e.getSource().getSelectedValue()
#		operation = self._createSelectionOperation( field, self._currSelectedField )
#		self._currSelectedField = field
#		selection = ecc.dennisc.alice.vm.getInstanceInJava( self._mapFieldToInstance[ field ] )
#		if self._prevSelection != selection:
#			if self._prevSelection:
#				try:
#					self._prevSelection.setBoundingBoxShowing( False )
#				except:
#					#import traceback
#					#traceback.print_exc()
#					print "valueChanged: setBoundingBoxShowing( False ) failed: ", self._prevSelection
#			self._prevSelection = selection
#			if self._prevSelection:
#				try:
#					self._prevSelection.setBoundingBoxShowing( True )
#				except:
#					#import traceback
#					#traceback.print_exc()
#					print "valueChanged: setBoundingBoxShowing( True ) failed: ", self._prevSelection
#					
#			alice.ide.IDE.getSingleton().performIfAppropriate( operation, e )

#	def setProgramType( self, programType ):
#		if self._programType:
#			self.setScene( None )
#			self.getOnscreenLookingGlass().removeCamera( self.getOnscreenLookingGlass().getCameraAt( 0 ) )
#			#self._sceneFieldsComposite.setListData( [] )
#
#		self._typeMap = {}
#		ecc.dennisc.alice.ide.ProgramWithSceneMixin.setProgramType( self, programType )
#		if self._programType:
#			sceneType = self.getSceneType()
#
#			self._vm.setConstructorBodyExecutionDesired( False )
#			try:
#				sceneInstance = self.createInstance( sceneType )
#			finally:
#				self._vm.setConstructorBodyExecutionDesired( True )
#			
#			#self._astField = alice.ast.FieldDeclaredInAlice( "scene", self._programType, alice.ast.NullLiteral() )
#	
#			automaticSetUpMethod = self.getSceneAutomaticSetUpMethod()
#			self._vm.invokeEntryPoint( automaticSetUpMethod, sceneInstance, [] )
#
#			self.addASTField( self.getSceneField(), sceneInstance )
#			for field in sceneType.fields.iterator():
#				self.addASTField( field, self._vm.get( field, sceneInstance ) )
#				self._registerType( field.getValueType() )
#			#self._sceneFieldsComposite.setSelectedValue( field )
#
#
#			self._scene = sceneInstance.getInstanceInJava()
#			self.setScene( self._scene )
#			self.getContentPane().revalidate()
