/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package org.alice.stageide.ast;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import org.alice.stageide.StageIDE;
import org.alice.stageide.StoryApiConfigurationManager;
import org.alice.stageide.sceneeditor.SetUpMethodGenerator;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AccessLevel;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.Comment;
import org.lgna.project.ast.ConditionalStatement;
import org.lgna.project.ast.ConstructorBlockStatement;
import org.lgna.project.ast.DoubleLiteral;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.FieldModifierFinalVolatileOrNeither;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.IntegerLiteral;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.LambdaExpression;
import org.lgna.project.ast.LocalAccess;
import org.lgna.project.ast.LocalDeclarationStatement;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.RelationalInfixExpression;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.SuperConstructorInvocationStatement;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserLambda;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.Color;
import org.lgna.story.Orientation;
import org.lgna.story.Paint;
import org.lgna.story.Position;
import org.lgna.story.SCamera;
import org.lgna.story.SGround;
import org.lgna.story.SProgram;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.SetAtmosphereColor;
import org.lgna.story.SetFogDensity;
import org.lgna.story.SetFromAboveLightColor;
import org.lgna.story.SetFromBelowLightColor;
import org.lgna.story.SetOpacity;
import org.lgna.story.SetPaint;
import org.lgna.story.event.SceneActivationListener;

import java.util.ArrayList;

/**
 * @author Dennis Cosgrove
 */
public class BootstrapUtilties {
	private static NamedUserType createType( String name, AbstractType<?, ?, ?> superType ) {
		NamedUserType rv = new NamedUserType();
		rv.name.setValue( name );
		rv.superType.setValue( superType );
		NamedUserConstructor constructor = new NamedUserConstructor();
		ConstructorBlockStatement constructorBlockStatement = new ConstructorBlockStatement();
		SuperConstructorInvocationStatement superConstructorInvocationStatement = new SuperConstructorInvocationStatement();
		superConstructorInvocationStatement.constructor.setValue( superType.getDeclaredConstructor() );
		constructorBlockStatement.constructorInvocationStatement.setValue( superConstructorInvocationStatement );
		constructor.body.setValue( constructorBlockStatement );
		rv.constructors.add( constructor );
		return rv;
	}

	private static NamedUserType createType( String name, Class<?> superCls ) {
		return createType( name, JavaType.getInstance( superCls ) );
	}

	private static UserField createPrivateFinalField( AbstractType<?, ?, ?> valueType, String name ) {
		UserField rv = new UserField();
		rv.accessLevel.setValue( AccessLevel.PRIVATE );
		rv.finalVolatileOrNeither.setValue( FieldModifierFinalVolatileOrNeither.FINAL );
		rv.valueType.setValue( valueType );
		rv.name.setValue( name );
		rv.initializer.setValue( AstUtilities.createInstanceCreation( valueType ) );
		return rv;
	}

	static UserField createPrivateFinalField(Class<?> cls, String name) {
		return createPrivateFinalField( JavaType.getInstance( cls ), name );
	}

	private static void addCommentIfNecessaryToMethod(UserMethod userMethod ) {
		String innerComment = StoryApiSpecificAstUtilities.getInnerCommentForMethodName( userMethod.getDeclaringType(), userMethod.getName() );
		if( innerComment != null ) {
			StatementListProperty bodyStatementsProperty = userMethod.body.getValue().statements;
			bodyStatementsProperty.add( 0, new Comment( innerComment ) );
		}
	}

	private static UserMethod createProcedure(AccessLevel access, String name) {
		UserMethod rv = new UserMethod();
		rv.accessLevel.setValue( access );
		rv.returnType.setValue( JavaType.getInstance(Void.TYPE) );
		rv.name.setValue( name );
		rv.body.setValue( new BlockStatement() );
		return rv;
	}

	static ExpressionStatement createMethodInvocationStatement(Expression expression, AbstractMethod method, Expression... argumentExpressions) {
		return AstUtilities.createMethodInvocationStatement( expression, method, argumentExpressions );
	}

	private static LocalDeclarationStatement createStoryDeclaration(AbstractType<?, ?, ?> type) {
		UserLocal local = new UserLocal("story", type, true);
		return AstUtilities.createLocalDeclarationStatement( local, new InstanceCreation( type.getDeclaredConstructor() ) );
	}

	static FieldAccess createFieldAccess(Enum<?> value) {
		return AstUtilities.createStaticFieldAccess( value.getClass(), value.name() );
	}

	//todo
	public static String MY_FIRST_PROCEDURE_NAME = "myFirstMethod";

	static NamedUserType createProgramType(UserField[] modelFields, ExpressionStatement[] setupStatements, Color atmosphereColor, double fogDensity, Color aboveLightColor, Color belowLightColor) {
		UserField cameraField = createPrivateFinalField( SCamera.class, "camera" );
		cameraField.isDeletionAllowed.setValue( false );
		cameraField.managementLevel.setValue( ManagementLevel.MANAGED );

		UserMethod myFirstMethod = createProcedure(AccessLevel.PUBLIC, MY_FIRST_PROCEDURE_NAME );

		UserMethod performGeneratedSetupMethod = createProcedure(AccessLevel.PRIVATE, StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME );
		performGeneratedSetupMethod.managementLevel.setValue( ManagementLevel.MANAGED );
		BlockStatement performGeneratedSetupBody = performGeneratedSetupMethod.body.getValue();

		UserMethod initializeEventListenersMethod = createProcedure(AccessLevel.PRIVATE, StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME );

		UserLambda sceneActivationListener = AstUtilities.createUserLambda( SceneActivationListener.class );
		LambdaExpression sceneActivationListenerExpression = new LambdaExpression( sceneActivationListener );

		JavaMethod addSceneActivationListenerMethod = JavaMethod.getInstance( SScene.class, "addSceneActivationListener", SceneActivationListener.class );

		initializeEventListenersMethod.body.getValue().statements.add( AstUtilities.createMethodInvocationStatement( new ThisExpression(), addSceneActivationListenerMethod, sceneActivationListenerExpression ) );

		UserField[] fields = new UserField[ modelFields.length + 1 ];
		System.arraycopy( modelFields, 0, fields, 0, modelFields.length );
		fields[ modelFields.length ] = cameraField;

		for( UserField field : fields ) {
			AbstractMethod method = field.getValueType().findMethod( "setVehicle", SThing.class );
			performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new FieldAccess(field), method, new ThisExpression() ) );
		}

		AffineMatrix4x4 m = AffineMatrix4x4.createIdentity();
		m.applyRotationAboutYAxis( new AngleInRadians( Math.PI ) );
		m.applyRotationAboutXAxis( new AngleInRadians( -Math.PI / 16.0 ) );
		m.applyTranslationAlongZAxis( 8 );

		UnitQuaternion quat = new UnitQuaternion( m.orientation );
		try {
			performGeneratedSetupBody.statements.add( SetUpMethodGenerator.createOrientationStatement( false, cameraField, new Orientation( quat.x, quat.y, quat.z, quat.w ) ) );
			performGeneratedSetupBody.statements.add( SetUpMethodGenerator.createPositionStatement( false, cameraField, new Position( m.translation.x, m.translation.y, m.translation.z ) ) );
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
			throw new RuntimeException( ccee );
		}
		if( atmosphereColor != null ) {
			JavaMethod setAtmosphereColorMethod = JavaMethod.getInstance( SScene.class, "setAtmosphereColor", Color.class, SetAtmosphereColor.Detail[].class );
			try {
				Expression colorExpression = StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( atmosphereColor );
				performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new ThisExpression(), setAtmosphereColorMethod, colorExpression ) );
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e ) {
				Logger.severe( "This exception should not occure: " + e );
			}
		}
		if (!Double.isNaN(fogDensity)) {
			JavaMethod setFogDensityMethod = JavaMethod.getInstance( SScene.class, "setFogDensity", Number.class, SetFogDensity.Detail[].class );
			performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new ThisExpression(), setFogDensityMethod, new DoubleLiteral( fogDensity ) ) );
		}
		if( aboveLightColor != null ) {
			JavaMethod setAboveLightColorMethod = JavaMethod.getInstance( SScene.class, "setFromAboveLightColor", Color.class, SetFromAboveLightColor.Detail[].class );
			try {
				Expression colorExpression = StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( aboveLightColor );
				performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new ThisExpression(), setAboveLightColorMethod, colorExpression ) );
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e ) {
				Logger.severe( "This exception should not occure: " + e );
			}
		}
		if( belowLightColor != null ) {
			JavaMethod setBelowLightColorMethod = JavaMethod.getInstance( SScene.class, "setFromBelowLightColor", Color.class, SetFromBelowLightColor.Detail[].class );
			try {
				Expression colorExpression = StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( belowLightColor );
				performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new ThisExpression(), setBelowLightColorMethod, colorExpression ) );
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e ) {
				Logger.severe( "This exception should not occure: " + e );
			}
		}
		performGeneratedSetupBody.statements.add( setupStatements );

		UserMethod performCustomSetupMethod = createProcedure(AccessLevel.PRIVATE, "performCustomSetup" );

		UserMethod handleActiveChangedMethod = createProcedure(AccessLevel.PROTECTED, "handleActiveChanged" );
		UserParameter isActiveParameter = new UserParameter( "isActive", Boolean.class );
		UserParameter activeCountParameter = new UserParameter( "activationCount", Integer.class );
		handleActiveChangedMethod.requiredParameters.add( isActiveParameter );
		handleActiveChangedMethod.requiredParameters.add( activeCountParameter );
		handleActiveChangedMethod.isSignatureLocked.setValue( true );
		handleActiveChangedMethod.managementLevel.setValue( ManagementLevel.GENERATED );

		BlockStatement handleActiveChangedBody = handleActiveChangedMethod.body.getValue();

		ConditionalStatement ifOuter = AstUtilities.createConditionalStatement( new ParameterAccess( isActiveParameter ) );
		ConditionalStatement ifInner = AstUtilities.createConditionalStatement( new RelationalInfixExpression( new ParameterAccess( activeCountParameter ), RelationalInfixExpression.Operator.EQUALS, new IntegerLiteral( 1 ), Integer.class, Integer.class ) );
		BlockStatement ifOuterTrueBody = ifOuter.booleanExpressionBodyPairs.get( 0 ).body.getValue();
		BlockStatement ifInnerTrueBody = ifInner.booleanExpressionBodyPairs.get( 0 ).body.getValue();
		BlockStatement ifInnerFalseBody = ifInner.elseBody.getValue();
		BlockStatement ifOuterFalseBody = ifOuter.elseBody.getValue();

		ifOuterTrueBody.statements.add( ifInner );

		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new ThisExpression(), performGeneratedSetupMethod ) );
		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new ThisExpression(), performCustomSetupMethod ) );
		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new ThisExpression(), initializeEventListenersMethod ) );

		Class<?> sceneCls = SScene.class;

		JavaMethod preserveVehiclesAndVantagePointsMethod = JavaMethod.getInstance( sceneCls, "preserveStateAndEventListeners" );
		JavaMethod restoreVehiclesAndVantagePointsMethod = JavaMethod.getInstance( sceneCls, "restoreStateAndEventListeners" );
		ifInnerFalseBody.statements.add( createMethodInvocationStatement( new ThisExpression(), restoreVehiclesAndVantagePointsMethod ) );
		ifOuterFalseBody.statements.add( createMethodInvocationStatement( new ThisExpression(), preserveVehiclesAndVantagePointsMethod ) );

		handleActiveChangedBody.statements.add( ifOuter );

		NamedUserType sceneType = createType( "Scene", SScene.class );
		sceneType.fields.add( fields );
		sceneType.methods.add( performCustomSetupMethod );
		sceneType.methods.add( performGeneratedSetupMethod );
		sceneType.methods.add( initializeEventListenersMethod );
		sceneType.methods.add( handleActiveChangedMethod );
		sceneType.methods.add( myFirstMethod );

		//Go through all the generated methods and add comments to the body if there is a comment defined in the CodeComments.properties file
		for( UserMethod method : sceneType.methods.getValue() ) {
			addCommentIfNecessaryToMethod( method );
		}

		UserField sceneField = createPrivateFinalField( sceneType, "myScene" );
		sceneActivationListener.body.getValue().statements.add( createMethodInvocationStatement( new ThisExpression(), myFirstMethod ) );

		NamedUserType rv = createType( "Program", SProgram.class );
		rv.fields.add( sceneField );

		UserParameter argsParameter = new UserParameter( "args", String[].class );
		UserMethod mainMethod = createProcedure(AccessLevel.PUBLIC, "main" );
		mainMethod.requiredParameters.add( argsParameter );
		BlockStatement mainBody = mainMethod.body.getValue();

		mainMethod.isStatic.setValue( true );
		mainMethod.isSignatureLocked.setValue( true );

		LocalDeclarationStatement storyDeclaration = createStoryDeclaration(rv);
		UserLocal storyLocal = storyDeclaration.local.getValue();
		mainBody.statements.add( storyDeclaration );
		mainBody.statements.add( createMethodInvocationStatement( new LocalAccess( storyLocal ), rv.findMethod( "initializeInFrame", String[].class ), new ParameterAccess( argsParameter ) ) );
		mainBody.statements.add( createMethodInvocationStatement( new LocalAccess( storyLocal ), StoryApiConfigurationManager.SET_ACTIVE_SCENE_METHOD, new MethodInvocation( new LocalAccess( storyLocal ), sceneField.getGetter() ) ) );

		rv.methods.add( mainMethod );
		addCommentIfNecessaryToMethod( mainMethod );

		return rv;
	}

	public static NamedUserType createProgramType( SGround.SurfaceAppearance appearance, Color atmosphereColor, double fogDensity, Color aboveLightColor, Color belowLightColor, double groundOpacity ) {

		UserField groundField = createPrivateFinalField( SGround.class, "ground" );

		groundField.managementLevel.setValue( ManagementLevel.MANAGED );

		UserField[] modelFields = { groundField
		};
		ArrayList<ExpressionStatement> setupStatements = new ArrayList<>();

		JavaMethod setPaintMethod = JavaMethod.getInstance( SGround.class, "setPaint", Paint.class, SetPaint.Detail[].class );
		setupStatements.add( createMethodInvocationStatement(new FieldAccess(groundField), setPaintMethod, createFieldAccess(appearance ) ) );

		if( groundOpacity != 1 ) {
			JavaMethod setGroundOpacityMethod = JavaMethod.getInstance( SGround.class, "setOpacity", Number.class, SetOpacity.Detail[].class );
			setupStatements.add( createMethodInvocationStatement(new FieldAccess(groundField), setGroundOpacityMethod, new DoubleLiteral(groundOpacity ) ) );
		}

		return createProgramType(modelFields, setupStatements.toArray(new ExpressionStatement[0]), atmosphereColor, fogDensity, aboveLightColor, belowLightColor );
	}
}
