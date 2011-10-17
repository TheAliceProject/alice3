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
	private static org.lgna.project.ast.NamedUserType createType( String name, org.lgna.project.ast.AbstractType< ?,?,? > superType ) {
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
	
	private static org.lgna.project.ast.UserField createPrivateFinalField( org.lgna.project.ast.AbstractType< ?,?,? > valueType, String name ) {
		org.lgna.project.ast.UserField rv = new org.lgna.project.ast.UserField();
		rv.access.setValue( org.lgna.project.ast.Access.PRIVATE );
		rv.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		rv.valueType.setValue( valueType );
		rv.name.setValue( name );
		rv.initializer.setValue( org.alice.ide.ast.AstUtilities.createInstanceCreation( valueType ) );
		return rv;
	}
	private static org.lgna.project.ast.UserField createPrivateFinalField( Class< ? > cls, String name ) {
		return createPrivateFinalField( org.lgna.project.ast.JavaType.getInstance( cls ), name );
	}
	
	private static org.lgna.project.ast.UserMethod createMethod( org.lgna.project.ast.Access access, org.lgna.project.ast.AbstractType< ?,?,? > returnType, String name ) {
		org.lgna.project.ast.UserMethod rv = new org.lgna.project.ast.UserMethod();
		rv.access.setValue( access );
		rv.returnType.setValue( returnType );
		rv.name.setValue( name );
		rv.body.setValue( new org.lgna.project.ast.BlockStatement() );
		return rv;
	}
	private static org.lgna.project.ast.UserMethod createMethod( org.lgna.project.ast.Access access, Class< ? > cls, String name ) {
		return createMethod( access, org.lgna.project.ast.JavaType.getInstance( cls ), name );
	}
	
	private static org.lgna.project.ast.FieldAccess createThisFieldAccess( org.lgna.project.ast.AbstractField field ) {
		return new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field );
	}
	private static org.lgna.project.ast.ExpressionStatement createMethodInvocationStatement( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression... argumentExpressions ) {
		return org.alice.ide.ast.AstUtilities.createMethodInvocationStatement( expression, method, argumentExpressions );
	}

	private static org.lgna.project.ast.VariableDeclarationStatement createVariableDeclarationStatementInitializedByInstanceCreation( String name, org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		org.lgna.project.ast.UserVariable variable = new org.lgna.project.ast.UserVariable( name, type );
		return org.alice.ide.ast.AstUtilities.createVariableDeclarationStatement( variable, new org.lgna.project.ast.InstanceCreation( type.getDeclaredConstructor() ) );
	}
	
	private static org.lgna.project.ast.FieldAccess createFieldAccess( Enum<?> value ) {
		return org.alice.ide.ast.AstUtilities.createStaticFieldAccess( value.getClass(), value.name() );
	}
	
	public static org.lgna.project.ast.NamedUserType createProgramType( org.lgna.story.Ground.SurfaceAppearance appearance ) {
		org.lgna.project.ast.UserField sunField = createPrivateFinalField( org.lgna.story.Sun.class, "sun" );
		org.lgna.project.ast.UserField groundField = createPrivateFinalField( org.lgna.story.Ground.class, "ground" );
		org.lgna.project.ast.UserField cameraField = createPrivateFinalField( org.lgna.story.Camera.class, "camera" );
		cameraField.isDeletionAllowed.setValue( false );

		sunField.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );
		groundField.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );
		cameraField.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );

		org.lgna.project.ast.UserMethod myFirstMethod = createMethod( org.lgna.project.ast.Access.PUBLIC, Void.TYPE, "myFirstMethod" );

		org.lgna.project.ast.UserMethod performGeneratedSetupMethod = createMethod( org.lgna.project.ast.Access.PRIVATE, Void.TYPE, org.alice.ide.IDE.GENERATED_SET_UP_METHOD_NAME );
		performGeneratedSetupMethod.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );
		org.lgna.project.ast.BlockStatement performGeneratedSetupBody = performGeneratedSetupMethod.body.getValue();
		
		for( org.lgna.project.ast.UserField field : new org.lgna.project.ast.UserField[] { cameraField, sunField, groundField } ) {
//			java.lang.reflect.Method mthd;
//			try {
//				mthd = ((org.lgna.project.ast.JavaType)field.getValueType()).getClassReflectionProxy().getReification().getMethod( "setVehicle", org.lgna.story.Entity.class );
//			} catch( NoSuchMethodException nsme ) {
//				throw new RuntimeException( nsme );
//			}
			//org.lgna.project.ast.AbstractMethod method = org.lgna.project.ast.JavaMethod.getInstance( mthd );
			org.lgna.project.ast.AbstractMethod method = field.getValueType().findMethod( "setVehicle", org.lgna.story.Entity.class );

			performGeneratedSetupBody.statements.add( 
					createMethodInvocationStatement( 
							createThisFieldAccess( field ), 
							method, 
							new org.lgna.project.ast.ThisExpression() 
					) 
			);
		}

		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
		m.applyRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( Math.PI ) );
		m.applyRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -Math.PI/16.0 ) );
		m.applyTranslationAlongZAxis( 8 );
		
		edu.cmu.cs.dennisc.math.UnitQuaternion quat = new edu.cmu.cs.dennisc.math.UnitQuaternion( m.orientation );
		try {
			performGeneratedSetupBody.statements.add(
					org.alice.stageide.sceneeditor.SetUpMethodGenerator.createOrientationStatement( false, cameraField, new org.lgna.story.Orientation( quat.x, quat.y, quat.z, quat.w ) )
			);
			performGeneratedSetupBody.statements.add(
					org.alice.stageide.sceneeditor.SetUpMethodGenerator.createPositionStatement( false, cameraField, new org.lgna.story.Position( m.translation.x, m.translation.y, m.translation.z ) )
			);
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
			throw new RuntimeException( ccee );
		}
		
		org.lgna.project.ast.JavaMethod setPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.Ground.class, "setPaint", org.lgna.story.Paint.class );
		performGeneratedSetupBody.statements.add( createMethodInvocationStatement( createThisFieldAccess( groundField ), setPaintMethod, createFieldAccess( appearance ) ) );

		org.lgna.project.ast.UserMethod performCustomSetupMethod = createMethod( org.lgna.project.ast.Access.PRIVATE, Void.TYPE, "performCustomSetup" );

		org.lgna.project.ast.UserMethod handleActiveChangedMethod = createMethod( org.lgna.project.ast.Access.PROTECTED, Void.TYPE, "handleActiveChanged" );
		org.lgna.project.ast.UserParameter isActiveParameter = new org.lgna.project.ast.UserParameter( "isActive", Boolean.class );
		org.lgna.project.ast.UserParameter activeCountParameter = new org.lgna.project.ast.UserParameter( "activeCount", Integer.class );
		handleActiveChangedMethod.parameters.add( isActiveParameter );
		handleActiveChangedMethod.parameters.add( activeCountParameter );
		handleActiveChangedMethod.isSignatureLocked.setValue( true );
		handleActiveChangedMethod.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.GENERATED );

		org.lgna.project.ast.BlockStatement handleActiveChangedBody = handleActiveChangedMethod.body.getValue();
		
		org.lgna.project.ast.ConditionalStatement ifOuter = org.alice.ide.ast.AstUtilities.createConditionalStatement( 
				new org.lgna.project.ast.ParameterAccess( isActiveParameter ) 
		);
		org.lgna.project.ast.ConditionalStatement ifInner = org.alice.ide.ast.AstUtilities.createConditionalStatement( 
				new org.lgna.project.ast.RelationalInfixExpression( 
						new org.lgna.project.ast.ParameterAccess( activeCountParameter ), 
						org.lgna.project.ast.RelationalInfixExpression.Operator.EQUALS, 
						new org.lgna.project.ast.IntegerLiteral( 1 ), 
						Integer.class, 
						Integer.class 
				)
		);
		org.lgna.project.ast.BlockStatement ifOuterTrueBody = ifOuter.booleanExpressionBodyPairs.get( 0 ).body.getValue(); 
		org.lgna.project.ast.BlockStatement ifInnerTrueBody = ifInner.booleanExpressionBodyPairs.get( 0 ).body.getValue(); 
		org.lgna.project.ast.BlockStatement ifInnerFalseBody = ifInner.elseBody.getValue(); 
		org.lgna.project.ast.BlockStatement ifOuterFalseBody = ifOuter.elseBody.getValue(); 

		ifOuterTrueBody.statements.add( ifInner );

		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), performGeneratedSetupMethod ) );
		ifInnerTrueBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), performGeneratedSetupMethod ) );

		Class< ? > sceneCls = org.lgna.story.Scene.class;
		
		org.lgna.project.ast.JavaMethod preserveVehiclesAndVantagePointsMethod = org.lgna.project.ast.JavaMethod.getInstance( sceneCls, "preserveVehiclesAndVantagePoints" );
		org.lgna.project.ast.JavaMethod restoreVehiclesAndVantagePointsMethod = org.lgna.project.ast.JavaMethod.getInstance( sceneCls, "restoreVehiclesAndVantagePoints" );
		ifInnerFalseBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), restoreVehiclesAndVantagePointsMethod ) );
		ifOuterFalseBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), preserveVehiclesAndVantagePointsMethod ) );

		handleActiveChangedBody.statements.add( ifOuter );
		
		org.lgna.project.ast.NamedUserType sceneType = createType( "MyScene", org.lgna.story.Scene.class );
		sceneType.fields.add( sunField );
		sceneType.fields.add( groundField );
		sceneType.fields.add( cameraField );
		sceneType.methods.add( performCustomSetupMethod );
		sceneType.methods.add( performGeneratedSetupMethod );
		sceneType.methods.add( handleActiveChangedMethod );
		sceneType.methods.add( myFirstMethod );

		org.lgna.project.ast.UserField sceneField = createPrivateFinalField( sceneType, "myScene" );
		org.lgna.project.ast.UserMethod playOutStoryMethod = createMethod( org.lgna.project.ast.Access.PUBLIC, Void.TYPE, "playOutStory" );
		org.lgna.project.ast.BlockStatement playOutStoryBody = playOutStoryMethod.body.getValue();
		playOutStoryBody.statements.add( 
				createMethodInvocationStatement( 
						new org.lgna.project.ast.ThisExpression(), 
						org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.Program.class, "setActiveScene", org.lgna.story.Scene.class ),
						createThisFieldAccess( sceneField )
				)
		);
		playOutStoryBody.statements.add( 
				createMethodInvocationStatement( 
						createThisFieldAccess( sceneField ),
						myFirstMethod
				)
		);
		
		org.lgna.project.ast.NamedUserType rv = createType( "MyProgram", org.lgna.story.Program.class );
		rv.fields.add( sceneField );
		rv.methods.add( playOutStoryMethod );

		
		
		org.lgna.project.ast.UserParameter argsParameter = new org.lgna.project.ast.UserParameter( "args", String[].class );
		org.lgna.project.ast.UserMethod mainMethod = createMethod( org.lgna.project.ast.Access.PUBLIC, Void.TYPE, "main" );
		mainMethod.parameters.add( argsParameter );
		org.lgna.project.ast.BlockStatement mainBody = mainMethod.body.getValue();

		mainMethod.isStatic.setValue( true );
		mainMethod.isSignatureLocked.setValue( true );
		
		org.lgna.project.ast.VariableDeclarationStatement variableDeclarationStatement = createVariableDeclarationStatementInitializedByInstanceCreation( "story", rv );
		org.lgna.project.ast.UserVariable storyVariable = variableDeclarationStatement.variable.getValue();
		mainBody.statements.add( variableDeclarationStatement );
		mainBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.VariableAccess( storyVariable ), rv.findMethod( "initializeInFrame", String[].class ), new org.lgna.project.ast.ParameterAccess( argsParameter ) ) );
		mainBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.VariableAccess( storyVariable ), playOutStoryMethod ) );
		rv.methods.add( mainMethod );
		
		return rv;
	}
}
