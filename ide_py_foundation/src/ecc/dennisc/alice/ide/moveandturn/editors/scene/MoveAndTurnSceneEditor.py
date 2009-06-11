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

#class BogusSelectionOperation( alice.ide.AbstractUndoableOperation ):
#	def __init__(self, nextSelection, prevSelection ):
#		alice.ide.AbstractUndoableOperation.__init__( self )
#		self._nextSelection = nextSelection
#		self._prevSelection = prevSelection
#	def prepare(self, e, observer):
#		return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
#	def perform(self):
#		self.redo()
#	def redo(self):
#		self.getIDE().setFieldSelection( self._nextSelection )
#	def undo(self):
#		self.getIDE().setFieldSelection( self._prevSelection )
#

class OrientToUprightOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, field ):
		self._field = field
		self.putValue( javax.swing.Action.NAME, "orient to upright" )
	def prepare( self, e, observer ):
		return alice.ide.Operation.PreparationResult.PERFORM
	def perform( self ):
		alice.ide.IDE.getSingleton()._scenePane.performOrientToUpright( self._field )

class PlaceOnTopOfGroundOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, field ):
		self._field = field
		self.putValue( javax.swing.Action.NAME, "place on top of ground" )
	def prepare( self, e, observer ):
		return alice.ide.Operation.PreparationResult.PERFORM
	def perform( self ):
		alice.ide.IDE.getSingleton()._scenePane.performPlaceOnTopOfGround( self._field )

class MyFieldAltTriggerMouseAdapter( ecc.dennisc.alice.ide.operations.ast.FieldAltTriggerMouseAdapter ):
	def createOperations(self):
		rv = ecc.dennisc.alice.ide.operations.ast.FieldAltTriggerMouseAdapter.createOperations( self )
		if self._field.getValueType().isAssignableTo( org.alice.apis.moveandturn.Transformable ):
			rv.append( None )
			rv.append( OrientToUprightOperation( self._field ) )
			rv.append( PlaceOnTopOfGroundOperation( self._field ) )
		return rv

class MyFieldTile( ecc.dennisc.alice.ide.barebones.editors.scene.MyFieldTile ):
	def createAltTriggerMouseAdapter(self, field):
		return MyFieldAltTriggerMouseAdapter( field )

class MyControlsForOverlayPane( ecc.dennisc.alice.ide.barebones.editors.scene.ControlsForOverlayPane ):
	def createFieldTile(self, field ):
		return MyFieldTile( field )


class MoveAndTurnSceneEditor( org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor ):
	def __init__( self, isLightweightDesired ):
		org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor.__init__( self )
		self._currSelectedField = None
		#todo: remove
		#print "isLightweightDesired paramater not used", isLightweightDesired

	def createControlsForOverlayPane( self ):
		return MyControlsForOverlayPane()
	
	def performOrientToUpright(self, field):
		instanceInJava = self.getInstanceInJavaForField( field )
		instanceInJava.orientToUpright()
		
	def performPlaceOnTopOfGround(self, field):
		instanceInJava = self.getInstanceInJavaForField( field )
		asSeenBy = org.alice.apis.moveandturn.AsSeenBy.SCENE
		bb = instanceInJava.getAxisAlignedMinimumBoundingBox()
		position = instanceInJava.getPosition( asSeenBy )
		position.y = -bb.minimum.y
		instanceInJava.moveTo( instanceInJava.acquireStandIn( asSeenBy, position ) )
	
	def getScene(self):
		return self.getSceneInstanceInJava()
	
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

#	def _select(self, nextField ):
#		prevField = self._currSelectedField
#		operation = self._createSelectionOperation( nextField, prevField )
#		event = None
#		alice.ide.IDE.getSingleton().performIfAppropriate( operation, event )

	def addASTFieldFor( self, instance ):
		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
		name = instanceInJava.getName()
		#programType = alice.ast.TypeDeclaredInJava.get( nameable.__class__ )
		type = instance.getType()
		astField = alice.ast.FieldDeclaredInAlice( name, type, alice.ast.InstanceCreation( type.getDeclaredConstructor( [] ), [] ) )
		astField.finalVolatileOrNeither.setValue( alice.ast.FieldModifierFinalVolatileOrNeither.FINAL )
		astField.access.setValue( alice.ast.Access.PRIVATE )
		self.getSceneType().fields.add( [ astField ] )

		self.putInstanceForField( astField, instance )
		self.getIDE().setFieldSelection( astField )
#		self._select( astField )

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

	def createScene( self, sceneField ):
		program = self.getProgram()
		if program:
			lg = program.getOnscreenLookingGlass()
			lg.clearCameras()
			program.setScene( None )

		sceneInstance = org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor.createScene( self, sceneField )
		if sceneInstance:
			self.restoreProjectProperties()
		return sceneInstance

	def getSceneAutomaticSetUpMethod( self ):
		return self.getSceneType().getDeclaredMethod( "performSceneEditorGeneratedSetUp", [] )

	def getFilledInSceneAutomaticSetUpMethod( self, fillerInner ):
		rv = self.getSceneAutomaticSetUpMethod()
		map = {}
		for key in self.mapFieldToInstance.keySet():
			map[ key ] = self.mapFieldToInstance.get( key )
		fillerInner.fillInSceneAutomaticSetUpMethod( rv, self.getSceneField(), map )
		return rv

	def generateCodeForSceneSetUp( self, setUpMethodGenerator ):
		self.getFilledInSceneAutomaticSetUpMethod( setUpMethodGenerator )

	def _createSelectionOperation(self, nextField, prevField ):
		return BogusSelectionOperation( nextField, prevField )

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

	def handleDelete(self, node):
		instance = self.mapFieldToInstance.get( node )
		if instance:
			self.getScene().removeComponent( ecc.dennisc.alice.vm.getInstanceInJava( instance ) )
		
