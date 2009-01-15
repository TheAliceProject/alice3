import java
import javax
import edu

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

class CameraNavigationDragAdapter( edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter ):
	def __init__( self, program ):
		edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter.__init__( self )
		self._program = program
	def getAWTComponentToAddListenersTo(self, onscreenLookingGlass ):
		return self._program._awtComponentToAddListenersTo

class ModelManipulationDragAdapter( edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter ):
	def __init__( self, program ):
		edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter.__init__( self )
		self._program = program
	
	def getAWTComponentToAddListenersTo(self, onscreenLookingGlass ):
		return self._program._awtComponentToAddListenersTo

	def getFirstClass( self, c ):
		if edu.cmu.cs.dennisc.scenegraph.Scene.isAssignableFrom( c.getParent().getClass() ):
			return c
		else:
			return self.getFirstClass( c.getParent() )
	
	def lookupDragAcceptor( self, sgVisual ):
		firstClass = self.getFirstClass( sgVisual )
		self._program.handleModelSelection( firstClass )
		return firstClass

	def handleMouseRelease( self, rv, dragStyle, isOriginalAsOpposedToStyleChange ):
		rv = edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter.handleMouseRelease( self, rv, dragStyle, isOriginalAsOpposedToStyleChange )
		if isOriginalAsOpposedToStyleChange:
			ecc.dennisc.alice.ide.getIDE().markChanged( "scene program model manipulator" )
		return rv

class ProgramAdapter( apis.moveandturn.Program, ecc.dennisc.alice.ide.ProgramWithSceneMixin, javax.swing.event.ListSelectionListener ):
	def __init__( self, vm, runOperation, awtComponentToAddListenersTo ):
		apis.moveandturn.Program.__init__( self )
		ecc.dennisc.alice.ide.ProgramWithSceneMixin.__init__( self )
		self._awtComponentToAddListenersTo = awtComponentToAddListenersTo

		self._vm = vm

		self._mapFieldToInstance = {}
		self._mapInstanceInJavaToField = {}
		
		self._prevSelection = None
		self._currSelectedField = None

		self._modelManipulationDragAdapter = ModelManipulationDragAdapter( self )
		self._cameraNavigationDragAdapter = CameraNavigationDragAdapter( self )
		
		self._typeMap = {}
		
		self._currScene = None

	def generateCodeForSceneSetUp( self, setUpMethodGenerator ):
		self.getFilledInSceneAutomaticSetUpMethod( setUpMethodGenerator )
	
	def isSpeedMultiplierControlPanelDesired( self ):
		return False

	def initialize( self ):
		self._modelManipulationDragAdapter.setAnimator( self.getAnimator() )
		self._modelManipulationDragAdapter.setOnscreenLookingGlass( self.getOnscreenLookingGlass() )
		
		self._cameraNavigationDragAdapter.requestDistance( 16 )
		self._cameraNavigationDragAdapter.setAnimator( self.getAnimator() )
		self._cameraNavigationDragAdapter.setOnscreenLookingGlass( self.getOnscreenLookingGlass() )

	def run( self ):
		pass
	

	def isDragInProgress(self):
		return self._modelManipulationDragAdapter.isDragInProgress() or self._cameraNavigationDragAdapter.isDragInProgress()
	
	def _createSelectionOperation(self, nextField, prevField ):
		return BogusSelectionOperation( nextField, prevField )

	def _registerType( self, type ):
		self._typeMap[ type.getName() ] = type

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

	def _getGoodLookAtShowInstanceAndReturnCamera( self, camera, instance ):
		isEnabled = self._cameraNavigationDragAdapter.isEnabled()
		self._cameraNavigationDragAdapter.setEnabled( False )
		try:
			instance.setOpacity( 0.0, apis.moveandturn.Composite.RIGHT_NOW )
			pov = camera.getPointOfView( self._scene )
			camera.getGoodLookAt( instance, 0.5 )
			self._scene.addComponent( instance )
			instance.setOpacity( 1.0 )
			camera.moveAndOrientTo( self._scene.createOffsetStandIn( pov.getInternal() ), 0.5 ) 
		finally:
			self._cameraNavigationDragAdapter.setEnabled( isEnabled )

	def addInstance( self, instance ):
		self.addASTFieldFor( instance )
		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
		if isinstance( instanceInJava, apis.moveandturn.Model ):
			camera = self._scene.findFirstMatch( apis.moveandturn.AbstractCamera )
			if camera:
				java.lang.Thread( ecc.dennisc.lang.ApplyRunnable( self._getGoodLookAtShowInstanceAndReturnCamera, ( camera, instanceInJava, ) ) ).start()
			else:
				self._scene.addComponent( instanceInJava )
		else:
			self._scene.addComponent( instanceInJava )
		alice.ide.IDE.getSingleton().markChanged( "scene program addInstance" )

	def createInstance( self, type ):
		constructor = type.getDeclaredConstructor( [] )
		return self._vm.createInstanceEntryPoint( constructor, [] )

	def addASTField( self, astField, instance ):
		#self._sceneFieldsComposite._addDatum( astField )
		self._mapFieldToInstance[ astField ] = instance
		self._mapInstanceInJavaToField[ ecc.dennisc.alice.vm.getInstanceInJava( instance ) ] = astField

	def handleDelete(self, node):
		print "handleDelete:", node
		if self._mapFieldToInstance.has_key( node ):
			instance = self._mapFieldToInstance[ node ]
			self._scene.removeComponent( ecc.dennisc.alice.vm.getInstanceInJava( instance ) )
			#print "handleDelete: (instance)", instance

	def addASTFieldFor( self, instance ):
		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
		name = instanceInJava.getName()
		#programType = alice.ast.TypeDeclaredInJava.get( nameable.__class__ )
		type = instance.getType()
		astField = alice.ast.FieldDeclaredInAlice( name, type, alice.ast.InstanceCreation( type.getDeclaredConstructor( [] ), [] ) )
		astField.finalVolatileOrNeither.setValue( alice.ast.FieldModifierFinalVolatileOrNeither.FINAL )
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

	def valueChanged( self, e ):
		field = e.getSource().getSelectedValue()
		operation = self._createSelectionOperation( field, self._currSelectedField )
		self._currSelectedField = field
		selection = ecc.dennisc.alice.vm.getInstanceInJava( self._mapFieldToInstance[ field ] )
		if self._prevSelection != selection:
			if self._prevSelection:
				try:
					self._prevSelection.setBoundingBoxShowing( False )
				except:
					#import traceback
					#traceback.print_exc()
					print "valueChanged: setBoundingBoxShowing( False ) failed: ", self._prevSelection
			self._prevSelection = selection
			if self._prevSelection:
				try:
					self._prevSelection.setBoundingBoxShowing( True )
				except:
					#import traceback
					#traceback.print_exc()
					print "valueChanged: setBoundingBoxShowing( True ) failed: ", self._prevSelection
					
			alice.ide.IDE.getSingleton().performIfAppropriate( operation, e )

	def setProgramType( self, programType ):
		if self._programType:
			self.setScene( None )
			self.getOnscreenLookingGlass().removeCamera( self.getOnscreenLookingGlass().getCameraAt( 0 ) )
			#self._sceneFieldsComposite.setListData( [] )

		self._typeMap = {}
		ecc.dennisc.alice.ide.ProgramWithSceneMixin.setProgramType( self, programType )
		if self._programType:
			sceneType = self.getSceneType()
			constructor = sceneType.getDeclaredConstructor( [] )

			self._vm.setConstructorBodyExecutionDesired( False )
			try:
				sceneInstance = self._vm.createInstanceEntryPoint( constructor, [] )
			finally:
				self._vm.setConstructorBodyExecutionDesired( True )
			
			#self._astField = alice.ast.FieldDeclaredInAlice( "scene", self._programType, alice.ast.NullLiteral() )
	
			automaticSetUpMethod = self.getSceneAutomaticSetUpMethod()
			self._vm.invokeEntryPoint( automaticSetUpMethod, sceneInstance, [] )

			self.addASTField( self.getSceneField(), sceneInstance )
			for field in sceneType.fields.iterator():
				self.addASTField( field, self._vm.get( field, sceneInstance ) )
				self._registerType( field.getValueType() )
			#self._sceneFieldsComposite.setSelectedValue( field )


			self._scene = sceneInstance.getInstanceInJava()
			self.setScene( self._scene )
			self.getContentPane().revalidate()

