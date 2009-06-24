from edu.cmu.cs.dennisc import alice
import org
import types

import ecc

def _createTypeDeclaredInAlice( nameable, typeName=None ):
	javaSuperCls = nameable.__class__
	while type( javaSuperCls ).__name__ != "javaclass":
		javaSuperCls = javaSuperCls.__bases__[ 0 ]

	methods = []
	cls = nameable.__class__
	while type( cls ) == types.ClassType:
		for key, value in cls.__dict__.items():
			if type( value ) == types.FunctionType:
				if key[ 0 ] == "_":
					pass
				else:
					methods.append( alice.ast.MethodDeclaredInAlice( key, alice.ast.TypeDeclaredInJava.VOID_TYPE, [], alice.ast.BlockStatement( [] ) ) )
		cls = cls.__bases__[ 0 ]

	if typeName:
		pass
	else:
		typeName = nameable.__class__.__name__
	package = None
	constructors = [ alice.ast.ConstructorDeclaredInAlice( [], alice.ast.BlockStatement( [] ) ) ]
	fields = []
	return alice.ast.TypeDeclaredInAlice( typeName, package, javaSuperCls, constructors, methods, fields )

def _createProgramType( typeName, package, javaSuperCls, sceneType ):
	constructors = [ alice.ast.ConstructorDeclaredInAlice( [], alice.ast.BlockStatement( [] ) ) ]
	
	sceneField = alice.ast.FieldDeclaredInAlice( "scene", sceneType, alice.ast.InstanceCreation( sceneType.getDeclaredConstructor( [] ), [] ) )
	sceneField.finalVolatileOrNeither.setValue( alice.ast.FieldModifierFinalVolatileOrNeither.FINAL )
	sceneField.access.setValue( alice.ast.Access.PRIVATE )
	sceneField.isDeletionAllowed.setValueAsInteger( False )

	#todo
	import edu
	setSceneMethod = alice.ast.TypeDeclaredInJava.getMethod( javaSuperCls.getMethod( "setScene", [ org.alice.apis.moveandturn.Scene ] ) )
	
	sceneAutomaticSetUpMethod = sceneType.getDeclaredMethod( "performSceneEditorGeneratedSetUp", [] )
	sceneAutomaticSetUpMethod.access.setValue( alice.ast.Access.PROTECTED )
	sceneAutomaticSetUpMethod.isSignatureLocked.setValueAsInteger( True )
	sceneAutomaticSetUpMethod.isDeletionAllowed.setValueAsInteger( False )

	performCustomPropertySetUpMethod = sceneType.getDeclaredMethod( "performCustomPropertySetUp", [] )
	performCustomPropertySetUpMethod.access.setValue( alice.ast.Access.PROTECTED )
	performCustomPropertySetUpMethod.isSignatureLocked.setValueAsInteger( True )
	performCustomPropertySetUpMethod.isDeletionAllowed.setValueAsInteger( False )

	sceneSetUpMethod = sceneType.getDeclaredMethod( "performSetUp", [] )
	sceneSetUpMethod.body.getValue().statements.add( [ 
		ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.ThisExpression(), sceneAutomaticSetUpMethod, [] ), 
		ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.ThisExpression(), performCustomPropertySetUpMethod, [] ) 
	] )
	
	
	sceneType.getDeclaredConstructor( [] ).body.getValue().statements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.ThisExpression(), sceneSetUpMethod, [] ) ] )
	
	sceneRunMethod = sceneType.getDeclaredMethod( "run", [] )
	sceneRunMethod.isSignatureLocked.setValueAsInteger( True )
	sceneRunMethod.isDeletionAllowed.setValueAsInteger( False )
	
	initializeMethod = alice.ast.MethodDeclaredInAlice( "initialize", alice.ast.TypeDeclaredInJava.VOID_TYPE, [], alice.ast.BlockStatement( [
		#ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), sceneField ), sceneSetUpMethod, [] ),																																							
		ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.ThisExpression(), setSceneMethod, [alice.ast.FieldAccess( alice.ast.ThisExpression(), sceneField )] )																																							
	] ) )
	initializeMethod.access.setValue( alice.ast.Access.PROTECTED )
	
	runMethod = alice.ast.MethodDeclaredInAlice( "run", alice.ast.TypeDeclaredInJava.VOID_TYPE, [], alice.ast.BlockStatement( [
		ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), sceneField ), sceneRunMethod, [] )																																							
	] ) )
	runMethod.access.setValue( alice.ast.Access.PROTECTED )

	return alice.ast.TypeDeclaredInAlice( typeName, package, javaSuperCls, constructors, [ initializeMethod, runMethod ], [ sceneField ] )

class Bootstrap( ecc.dennisc.alice.ide.ProgramWithSceneMixin ):
	def __init__( self, scene, javaSuperCls ):
		ecc.dennisc.alice.ide.ProgramWithSceneMixin.__init__( self )
		name = scene.getName()

		sceneType = _createTypeDeclaredInAlice( scene, "MyScene" )

		self.setProgramType( _createProgramType( "MyProgram", None, javaSuperCls, sceneType ) )
		
		self._mapFieldToInstance = { self.getSceneField() : scene }

		for key, value in scene.__dict__.items():
			value.setName( key )
			if type( value.__class__ ).__name__ == "javaclass":
				astType = alice.ast.TypeDeclaredInJava.get( value.__class__ )
			else:
				astType = _createTypeDeclaredInAlice( value )
			astField = alice.ast.FieldDeclaredInAlice( key, astType, alice.ast.InstanceCreation( astType.getDeclaredConstructors().get( 0 ), [] ) )
			astField.finalVolatileOrNeither.setValue( alice.ast.FieldModifierFinalVolatileOrNeither.FINAL )
			astField.access.setValue( alice.ast.Access.PRIVATE )
			sceneType.fields.add( [ astField ] )
			self._mapFieldToInstance[ astField ] = value
			if key == "camera":
				astField.isDeletionAllowed.setValueAsInteger( False )
