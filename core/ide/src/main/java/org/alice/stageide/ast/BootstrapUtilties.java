/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.alice.stageide.ast;

/**
 * @author Dennis Cosgrove
 */
public class BootstrapUtilties {
	private static org.lgna.project.ast.NamedUserType createType( String name, org.lgna.project.ast.AbstractType<?, ?, ?> superType ) {
		org.lgna.project.ast.NamedUserType rv = new org.lgna.project.ast.NamedUserType();
		rv.name.setValue( name );
		rv.superType.setValue( superType );
		org.lgna.project.ast.NamedUserConstructor constructor = new org.lgna.project.ast.NamedUserConstructor();
		org.lgna.project.ast.ConstructorBlockStatement constructorBlockStatement = new org.lgna.project.ast.ConstructorBlockStatement();
		org.lgna.project.ast.SuperConstructorInvocationStatement superConstructorInvocationStatement = new org.lgna.project.ast.SuperConstructorInvocationStatement();
		superConstructorInvocationStatement.constructor.setValue( superType.getDeclaredConstructor() );
		constructorBlockStatement.constructorInvocationStatement.setValue( superConstructorInvocationStatement );
		constructor.body.setValue( constructorBlockStatement );
		rv.constructors.add( constructor );
		return rv;
	}

	private static org.lgna.project.ast.NamedUserType createType( String name, Class<?> superCls ) {
		return createType( name, org.lgna.project.ast.JavaType.getInstance( superCls ) );
	}

	private static org.lgna.project.ast.UserField createPrivateFinalField( org.lgna.project.ast.AbstractType<?, ?, ?> valueType, String name ) {
		org.lgna.project.ast.UserField rv = new org.lgna.project.ast.UserField();
		rv.accessLevel.setValue( org.lgna.project.ast.AccessLevel.PRIVATE );
		rv.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		rv.valueType.setValue( valueType );
		rv.name.setValue( name );
		rv.initializer.setValue( org.lgna.project.ast.AstUtilities.createInstanceCreation( valueType ) );
		return rv;
	}

	private static org.lgna.project.ast.UserField createPrivateFinalField( Class<?> cls, String name ) {
		return createPrivateFinalField( org.lgna.project.ast.JavaType.getInstance( cls ), name );
	}

	private static org.lgna.project.ast.UserMethod addCommentIfNecessaryToMethod( org.lgna.project.ast.UserMethod userMethod ) {
		org.lgna.project.ast.StatementListProperty bodyStatementsProperty = userMethod.body.getValue().statements;
		String innerComment = StoryApiSpecificAstUtilities.getInnerCommentForMethodName( userMethod.getDeclaringType(), userMethod.getName() );
		if( innerComment != null ) {
			bodyStatementsProperty.add( 0, new org.lgna.project.ast.Comment( innerComment ) );
		}
		return userMethod;
	}

	private static org.lgna.project.ast.UserMethod createMethod( org.lgna.project.ast.AccessLevel access, org.lgna.project.ast.AbstractType<?, ?, ?> returnType, String name ) {
		org.lgna.project.ast.UserMethod rv = new org.lgna.project.ast.UserMethod();
		rv.accessLevel.setValue( access );
		rv.returnType.setValue( returnType );
		rv.name.setValue( name );
		rv.body.setValue( new org.lgna.project.ast.BlockStatement() );
		return rv;
	}

	private static org.lgna.project.ast.UserMethod createMethod( org.lgna.project.ast.AccessLevel access, Class<?> cls, String name ) {
		return createMethod( access, org.lgna.project.ast.JavaType.getInstance( cls ), name );
	}

	private static org.lgna.project.ast.FieldAccess createThisFieldAccess( org.lgna.project.ast.AbstractField field ) {
		return new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field );
	}

	private static org.lgna.project.ast.ExpressionStatement createMethodInvocationStatement( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression... argumentExpressions ) {
		return org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( expression, method, argumentExpressions );
	}

	private static org.lgna.project.ast.LocalDeclarationStatement createLocalDeclarationStatementInitializedByInstanceCreation( String name, org.lgna.project.ast.AbstractType<?, ?, ?> type, boolean isFinal ) {
		org.lgna.project.ast.UserLocal local = new org.lgna.project.ast.UserLocal( name, type, isFinal );
		return org.lgna.project.ast.AstUtilities.createLocalDeclarationStatement( local, new org.lgna.project.ast.InstanceCreation( type.getDeclaredConstructor() ) );
	}

	private static org.lgna.project.ast.FieldAccess createFieldAccess( Enum<?> value ) {
		return org.lgna.project.ast.AstUtilities.createStaticFieldAccess( value.getClass(), value.name() );
	}

	//todo
	public static String MY_FIRST_PROCEDURE_NAME = "myFirstMethod";

	public static org.lgna.project.ast.NamedUserType createProgramType( org.lgna.project.ast.UserField[] modelFields, org.lgna.project.ast.ExpressionStatement[] setupStatements, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor, org.lgna.story.Color belowLightColor ) {
		org.lgna.project.ast.UserField cameraField = createPrivateFinalField( org.lgna.story.SCamera.class, "camera" );
		cameraField.isDeletionAllowed.setValue( false );
		cameraField.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );

		org.lgna.project.ast.UserMethod myFirstMethod = createMethod( org.lgna.project.ast.AccessLevel.PUBLIC, Void.TYPE, MY_FIRST_PROCEDURE_NAME );

		org.lgna.project.ast.UserMethod performGeneratedSetupMethod = createMethod( org.lgna.project.ast.AccessLevel.PRIVATE, Void.TYPE, org.alice.stageide.StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME );
		performGeneratedSetupMethod.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );
		org.lgna.project.ast.BlockStatement performGeneratedSetupBody = performGeneratedSetupMethod.body.getValue();

		org.lgna.project.ast.UserMethod initializeEventListenersMethod = createMethod( org.lgna.project.ast.AccessLevel.PRIVATE, Void.TYPE, org.alice.stageide.StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME );

		org.lgna.project.ast.UserLambda sceneActivationListener = org.lgna.project.ast.AstUtilities.createUserLambda( org.lgna.story.event.SceneActivationListener.class );
		org.lgna.project.ast.LambdaExpression sceneActivationListenerExpression = new org.lgna.project.ast.LambdaExpression( sceneActivationListener );

		org.lgna.project.ast.JavaMethod addSceneActivationListenerMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SScene.class, "addSceneActivationListener", org.lgna.story.event.SceneActivationListener.class );

		initializeEventListenersMethod.body.getValue().statements.add( org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), addSceneActivationListenerMethod, sceneActivationListenerExpression ) );

		org.lgna.project.ast.UserField[] fields = new org.lgna.project.ast.UserField[ modelFields.length + 1 ];
		System.arraycopy( modelFields, 0, fields, 0, modelFields.length );
		fields[ modelFields.length ] = cameraField;

		for( org.lgna.project.ast.UserField field : fields ) {
			org.lgna.project.ast.AbstractMethod method = field.getValueType().findMethod( "setVehicle", org.lgna.story.SThing.class );
			performGeneratedSetupBody.statements.add( createMethodInvocationStatement( createThisFieldAccess( field ), method, new org.lgna.project.ast.ThisExpression() ) );
		}

		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
		m.applyRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( Math.PI ) );
		m.applyRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -Math.PI / 16.0 ) );
		m.applyTranslationAlongZAxis( 8 );

		edu.cmu.cs.dennisc.math.UnitQuaternion quat = new edu.cmu.cs.dennisc.math.UnitQuaternion( m.orientation );
		try {
			performGeneratedSetupBody.statements.add( org.alice.stageide.sceneeditor.SetUpMethodGenerator.createOrientationStatement( false, cameraField, new org.lgna.story.Orientation( quat.x, quat.y, quat.z, quat.w ) ) );
			performGeneratedSetupBody.statements.add( org.alice.stageide.sceneeditor.SetUpMethodGenerator.createPositionStatement( false, cameraField, new org.lgna.story.Position( m.translation.x, m.translation.y, m.translation.z ) ) );
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
			throw new RuntimeException( ccee );
		}
		if( atmosphereColor != null ) {
			org.lgna.project.ast.JavaMethod setAtmosphereColorMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SScene.class, "setAtmosphereColor", org.lgna.story.Color.class, org.lgna.story.SetAtmosphereColor.Detail[].class );
			try {
				org.lgna.project.ast.Expression colorExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( atmosphereColor );
				performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), setAtmosphereColorMethod, colorExpression ) );
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "This exception should not occure: " + e );
			}
		}
		if( Double.isNaN( fogDensity ) ) {
			//pass
		} else {
			org.lgna.project.ast.JavaMethod setFogDensityMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SScene.class, "setFogDensity", Number.class, org.lgna.story.SetFogDensity.Detail[].class );
			performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), setFogDensityMethod, new org.lgna.project.ast.DoubleLiteral( fogDensity ) ) );
		}
		if( aboveLightColor != null ) {
			org.lgna.project.ast.JavaMethod setAboveLightColorMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SScene.class, "setFromAboveLightColor", org.lgna.story.Color.class, org.lgna.story.SetFromAboveLightColor.Detail[].class );
			try {
				org.lgna.project.ast.Expression colorExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( aboveLightColor );
				performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), setAboveLightColorMethod, colorExpression ) );
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "This exception should not occure: " + e );
			}
		}
		if( belowLightColor != null ) {
			org.lgna.project.ast.JavaMethod setBelowLightColorMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SScene.class, "setFromBelowLightColor", org.lgna.story.Color.class, org.lgna.story.SetFromBelowLightColor.Detail[].class );
			try {
				org.lgna.project.ast.Expression colorExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( belowLightColor );
				performGeneratedSetupBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), setBelowLightColorMethod, colorExpression ) );
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "This exception should not occure: " + e );
			}
		}
		performGeneratedSetupBody.statements.add( setupStatements );

		org.lgna.project.ast.UserMethod performCustomSetupMethod = createMethod( org.lgna.project.ast.AccessLevel.PRIVATE, Void.TYPE, "performCustomSetup" );

		org.lgna.project.ast.UserMethod handleActiveChangedMethod = createMethod( org.lgna.project.ast.AccessLevel.PROTECTED, Void.TYPE, "handleActiveChanged" );
		org.lgna.project.ast.UserParameter isActiveParameter = new org.lgna.project.ast.UserParameter( "isActive", Boolean.class );
		org.lgna.project.ast.UserParameter activeCountParameter = new org.lgna.project.ast.UserParameter( "activationCount", Integer.class );
		handleActiveChangedMethod.requiredParameters.add( isActiveParameter );
		handleActiveChangedMethod.requiredParameters.add( activeCountParameter );
		handleActiveChangedMethod.isSignatureLocked.setValue( true );
		handleActiveChangedMethod.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.GENERATED );

		org.lgna.project.ast.BlockStatement handleActiveChangedBody = handleActiveChangedMethod.body.getValue();

		org.lgna.project.ast.ConditionalStatement ifOuter = org.lgna.project.ast.AstUtilities.createConditionalStatement( new org.lgna.project.ast.ParameterAccess( isActiveParameter ) );
		org.lgna.project.ast.ConditionalStatement ifInner = org.lgna.project.ast.AstUtilities.createConditionalStatement( new org.lgna.project.ast.RelationalInfixExpression( new org.lgna.project.ast.ParameterAccess( activeCountParameter ), org.lgna.project.ast.RelationalInfixExpression.Operator.EQUALS, new org.lgna.project.ast.IntegerLiteral( 1 ), Integer.class, Integer.class ) );
		org.lgna.project.ast.BlockStatement ifOuterTrueBody = ifOuter.booleanExpressionBodyPairs.get( 0 ).body.getValue();
		org.lgna.project.ast.BlockStatement ifInnerTrueBody = ifInner.booleanExpressionBodyPairs.get( 0 ).body.getValue();
		org.lgna.project.ast.BlockStatement ifInnerFalseBody = ifInner.elseBody.getValue();
		org.lgna.project.ast.BlockStatement ifOuterFalseBody = ifOuter.elseBody.getValue();

		ifOuterTrueBody.statements.add( ifInner );

		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), performGeneratedSetupMethod ) );
		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), performCustomSetupMethod ) );
		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), initializeEventListenersMethod ) );

		Class<?> sceneCls = org.lgna.story.SScene.class;

		org.lgna.project.ast.JavaMethod preserveVehiclesAndVantagePointsMethod = org.lgna.project.ast.JavaMethod.getInstance( sceneCls, "preserveStateAndEventListeners" );
		org.lgna.project.ast.JavaMethod restoreVehiclesAndVantagePointsMethod = org.lgna.project.ast.JavaMethod.getInstance( sceneCls, "restoreStateAndEventListeners" );
		ifInnerFalseBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), restoreVehiclesAndVantagePointsMethod ) );
		ifOuterFalseBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), preserveVehiclesAndVantagePointsMethod ) );

		handleActiveChangedBody.statements.add( ifOuter );

		org.lgna.project.ast.NamedUserType sceneType = createType( "Scene", org.lgna.story.SScene.class );
		sceneType.fields.add( fields );
		sceneType.methods.add( performCustomSetupMethod );
		sceneType.methods.add( performGeneratedSetupMethod );
		sceneType.methods.add( initializeEventListenersMethod );
		sceneType.methods.add( handleActiveChangedMethod );
		sceneType.methods.add( myFirstMethod );

		//Go through all the generated methods and add comments to the body if there is a comment defined in the CodeComments.properties file
		for( org.lgna.project.ast.UserMethod method : sceneType.methods.getValue() ) {
			addCommentIfNecessaryToMethod( method );
		}

		org.lgna.project.ast.UserField sceneField = createPrivateFinalField( sceneType, "myScene" );
		sceneActivationListener.body.getValue().statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), myFirstMethod ) );

		org.lgna.project.ast.NamedUserType rv = createType( "Program", org.lgna.story.SProgram.class );
		rv.fields.add( sceneField );

		org.lgna.project.ast.UserParameter argsParameter = new org.lgna.project.ast.UserParameter( "args", String[].class );
		org.lgna.project.ast.UserMethod mainMethod = createMethod( org.lgna.project.ast.AccessLevel.PUBLIC, Void.TYPE, "main" );
		mainMethod.requiredParameters.add( argsParameter );
		org.lgna.project.ast.BlockStatement mainBody = mainMethod.body.getValue();

		mainMethod.isStatic.setValue( true );
		mainMethod.isSignatureLocked.setValue( true );

		org.lgna.project.ast.LocalDeclarationStatement localDeclarationStatement = createLocalDeclarationStatementInitializedByInstanceCreation( "story", rv, true );
		org.lgna.project.ast.UserLocal storyLocal = localDeclarationStatement.local.getValue();
		mainBody.statements.add( localDeclarationStatement );
		mainBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.LocalAccess( storyLocal ), rv.findMethod( "initializeInFrame", String[].class ), new org.lgna.project.ast.ParameterAccess( argsParameter ) ) );
		mainBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.LocalAccess( storyLocal ), org.alice.stageide.StoryApiConfigurationManager.SET_ACTIVE_SCENE_METHOD, new org.lgna.project.ast.MethodInvocation( new org.lgna.project.ast.LocalAccess( storyLocal ), sceneField.getGetter() ) ) );

		rv.methods.add( mainMethod );
		addCommentIfNecessaryToMethod( mainMethod );

		return rv;
	}

	public static org.lgna.project.ast.NamedUserType createProgramType( org.lgna.story.SGround.SurfaceAppearance appearance, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor, org.lgna.story.Color belowLightColor ) {

		org.lgna.project.ast.UserField groundField = createPrivateFinalField( org.lgna.story.SGround.class, "ground" );

		groundField.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );

		org.lgna.project.ast.UserField[] modelFields = { groundField
		};
		java.util.ArrayList<org.lgna.project.ast.ExpressionStatement> setupStatements = new java.util.ArrayList<org.lgna.project.ast.ExpressionStatement>();

		org.lgna.project.ast.JavaMethod setPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SGround.class, "setPaint", org.lgna.story.Paint.class, org.lgna.story.SetPaint.Detail[].class );
		setupStatements.add( createMethodInvocationStatement( createThisFieldAccess( groundField ), setPaintMethod, createFieldAccess( appearance ) ) );

		return createProgramType( modelFields, setupStatements.toArray( new org.lgna.project.ast.ExpressionStatement[ setupStatements.size() ] ), atmosphereColor, fogDensity, aboveLightColor, belowLightColor );
	}

	//groundAppearance is optional and will cause the program to generate a ground in addition to a room
	public static org.lgna.project.ast.NamedUserType createProgramType( org.lgna.story.SGround.SurfaceAppearance groundAppearance, org.lgna.story.Paint floorAppearance, org.lgna.story.Paint wallAppearance, org.lgna.story.Paint ceilingAppearance, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor, org.lgna.story.Color belowLightColor ) {

		org.lgna.project.ast.UserField roomField = createPrivateFinalField( org.lgna.story.SRoom.class, "room" );
		roomField.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );

		org.lgna.project.ast.UserField groundField = groundAppearance != null ? createPrivateFinalField( org.lgna.story.SGround.class, "ground" ) : null;
		if( groundField != null ) {
			groundField.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );
		}

		org.lgna.project.ast.UserField[] modelFields;
		if( groundField != null ) {
			modelFields = new org.lgna.project.ast.UserField[] { roomField, groundField };
		} else {
			modelFields = new org.lgna.project.ast.UserField[] { roomField };
		}

		java.util.ArrayList<org.lgna.project.ast.ExpressionStatement> setupStatements = new java.util.ArrayList<org.lgna.project.ast.ExpressionStatement>();

		org.lgna.project.ast.JavaMethod setFloorPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SRoom.class, "setFloorPaint", org.lgna.story.Paint.class, org.lgna.story.SetFloorPaint.Detail[].class );
		org.lgna.project.ast.Expression floorPaintExpression = null;
		org.lgna.project.ast.Expression wallPaintExpression = null;
		org.lgna.project.ast.Expression ceilingPaintExpression = null;

		try {
			if( floorAppearance instanceof org.lgna.story.SRoom.FloorAppearance ) {
				floorPaintExpression = createFieldAccess( (org.lgna.story.SRoom.FloorAppearance)floorAppearance );
			} else {
				floorPaintExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( floorAppearance );
			}

			if( wallAppearance instanceof org.lgna.story.SRoom.WallAppearance ) {
				wallPaintExpression = createFieldAccess( (org.lgna.story.SRoom.WallAppearance)wallAppearance );
			} else {
				wallPaintExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( wallAppearance );
			}

			if( ceilingAppearance instanceof org.lgna.story.SRoom.CeilingAppearance ) {
				ceilingPaintExpression = createFieldAccess( (org.lgna.story.SRoom.CeilingAppearance)ceilingAppearance );
			} else {
				ceilingPaintExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( ceilingAppearance );
			}
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "cannot create expression", e );
		}

		setupStatements.add( createMethodInvocationStatement( createThisFieldAccess( roomField ), setFloorPaintMethod, floorPaintExpression ) );
		org.lgna.project.ast.JavaMethod setWallPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SRoom.class, "setWallPaint", org.lgna.story.Paint.class, org.lgna.story.SetWallPaint.Detail[].class );
		setupStatements.add( createMethodInvocationStatement( createThisFieldAccess( roomField ), setWallPaintMethod, wallPaintExpression ) );
		org.lgna.project.ast.JavaMethod setCeilingPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SRoom.class, "setCeilingPaint", org.lgna.story.Paint.class, org.lgna.story.SetCeilingPaint.Detail[].class );
		setupStatements.add( createMethodInvocationStatement( createThisFieldAccess( roomField ), setCeilingPaintMethod, ceilingPaintExpression ) );

		if( groundAppearance != null ) {
			org.lgna.project.ast.JavaMethod setGroundPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SGround.class, "setPaint", org.lgna.story.Paint.class, org.lgna.story.SetPaint.Detail[].class );
			setupStatements.add( createMethodInvocationStatement( createThisFieldAccess( groundField ), setGroundPaintMethod, createFieldAccess( groundAppearance ) ) );
		}

		return createProgramType( modelFields, setupStatements.toArray( new org.lgna.project.ast.ExpressionStatement[ setupStatements.size() ] ), atmosphereColor, fogDensity, aboveLightColor, belowLightColor );
	}
}
