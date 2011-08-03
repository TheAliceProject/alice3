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
	private static org.lgna.project.ast.TypeDeclaredInAlice createType( String name, org.lgna.project.ast.AbstractType< ?,?,? > superType ) {
		org.lgna.project.ast.TypeDeclaredInAlice rv = new org.lgna.project.ast.TypeDeclaredInAlice();
		rv.name.setValue( name );
		rv.superType.setValue( superType );
		org.lgna.project.ast.ConstructorDeclaredInAlice constructor = new org.lgna.project.ast.ConstructorDeclaredInAlice();
		org.lgna.project.ast.ConstructorBlockStatement constructorBlockStatement = new org.lgna.project.ast.ConstructorBlockStatement();
		org.lgna.project.ast.SuperConstructorInvocationStatement superConstructorInvocationStatement = new org.lgna.project.ast.SuperConstructorInvocationStatement();
		superConstructorInvocationStatement.contructor.setValue( superType.getDeclaredConstructor() );
		constructorBlockStatement.constructorInvocationStatement.setValue( superConstructorInvocationStatement );
		constructor.body.setValue( constructorBlockStatement );
		rv.constructors.add( constructor );
		return rv;
	}
	private static org.lgna.project.ast.TypeDeclaredInAlice createType( String name, Class<?> superCls ) {
		return createType( name, org.lgna.project.ast.TypeDeclaredInJava.get( superCls ) );
	}
	
	private static org.lgna.project.ast.FieldDeclaredInAlice createPrivateFinalField( org.lgna.project.ast.AbstractType< ?,?,? > valueType, String name ) {
		org.lgna.project.ast.FieldDeclaredInAlice rv = new org.lgna.project.ast.FieldDeclaredInAlice();
		rv.access.setValue( org.lgna.project.ast.Access.PRIVATE );
		rv.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		rv.valueType.setValue( valueType );
		rv.name.setValue( name );
		rv.initializer.setValue( org.alice.ide.ast.NodeUtilities.createInstanceCreation( valueType ) );
		return rv;
	}
	private static org.lgna.project.ast.FieldDeclaredInAlice createPrivateFinalField( Class< ? > cls, String name ) {
		return createPrivateFinalField( org.lgna.project.ast.TypeDeclaredInJava.get( cls ), name );
	}
	
	private static org.lgna.project.ast.MethodDeclaredInAlice createMethod( org.lgna.project.ast.Access access, org.lgna.project.ast.AbstractType< ?,?,? > returnType, String name ) {
		org.lgna.project.ast.MethodDeclaredInAlice rv = new org.lgna.project.ast.MethodDeclaredInAlice();
		rv.access.setValue( access );
		rv.returnType.setValue( returnType );
		rv.name.setValue( name );
		rv.body.setValue( new org.lgna.project.ast.BlockStatement() );
		return rv;
	}
	private static org.lgna.project.ast.MethodDeclaredInAlice createMethod( org.lgna.project.ast.Access access, Class< ? > cls, String name ) {
		return createMethod( access, org.lgna.project.ast.TypeDeclaredInJava.get( cls ), name );
	}
	
	private static org.lgna.project.ast.FieldAccess createThisFieldAccess( org.lgna.project.ast.AbstractField field ) {
		return new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field );
	}
	private static org.lgna.project.ast.ExpressionStatement createMethodInvocationStatement( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression... argumentExpressions ) {
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( expression, method, argumentExpressions );
	}

	private static org.lgna.project.ast.VariableDeclarationStatement createVariableDeclarationStatementInitializedByInstanceCreation( String name, org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		org.lgna.project.ast.VariableDeclaredInAlice variable = new org.lgna.project.ast.VariableDeclaredInAlice( name, type );
		return org.alice.ide.ast.NodeUtilities.createVariableDeclarationStatement( variable, new org.lgna.project.ast.InstanceCreation( type.getDeclaredConstructor() ) );
	}
	
	private static org.lgna.project.ast.FieldAccess createFieldAccess( Enum<?> value ) {
		return org.alice.ide.ast.NodeUtilities.createStaticFieldAccess( value.getClass(), value.name() );
	}
	
	public static org.lgna.project.ast.TypeDeclaredInAlice createProgramType( org.lgna.story.Ground.Appearance appearance ) {
		org.lgna.project.ast.FieldDeclaredInAlice sunField = createPrivateFinalField( org.lgna.story.Sun.class, "sun" );
		org.lgna.project.ast.FieldDeclaredInAlice groundField = createPrivateFinalField( org.lgna.story.Ground.class, "ground" );
		org.lgna.project.ast.FieldDeclaredInAlice cameraField = createPrivateFinalField( org.lgna.story.Camera.class, "camera" );

		org.lgna.project.ast.MethodDeclaredInAlice myFirstMethod = createMethod( org.lgna.project.ast.Access.PUBLIC, Void.TYPE, "myFirstMethod" );

		org.lgna.project.ast.MethodDeclaredInAlice performGeneratedSetupMethod = createMethod( org.lgna.project.ast.Access.PRIVATE, Void.TYPE, org.alice.ide.IDE.GENERATED_SET_UP_METHOD_NAME );
		org.lgna.project.ast.BlockStatement performGeneratedSetupBody = performGeneratedSetupMethod.body.getValue();
		
		for( org.lgna.project.ast.FieldDeclaredInAlice field : new org.lgna.project.ast.FieldDeclaredInAlice[] { cameraField, sunField, groundField } ) {
			java.lang.reflect.Method mthd;
			try {
				mthd = ((org.lgna.project.ast.TypeDeclaredInJava)field.getValueType()).getClassReflectionProxy().getReification().getMethod( "setVehicle", org.lgna.story.Entity.class );
			} catch( NoSuchMethodException nsme ) {
				throw new RuntimeException( nsme );
			}
			org.lgna.project.ast.AbstractMethod method = org.lgna.project.ast.MethodDeclaredInJava.get( mthd );
			//edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = field.getValueType().findMethod( "setVehicle", org.lookingglassandalice.storytelling.Entity.class );

			performGeneratedSetupBody.statements.add( 
					createMethodInvocationStatement( 
							createThisFieldAccess( field ), 
							method, 
							new org.lgna.project.ast.ThisExpression() 
					) 
			);
		}
		
		org.lgna.project.ast.MethodDeclaredInJava setAppearanceMethod = org.lgna.project.ast.MethodDeclaredInJava.get( org.lgna.story.Ground.class, "setAppearance", org.lgna.story.Ground.Appearance.class );
		
		performGeneratedSetupBody.statements.add( createMethodInvocationStatement( createThisFieldAccess( groundField ), setAppearanceMethod, createFieldAccess( appearance ) ) );

		org.lgna.project.ast.MethodDeclaredInAlice performCustomSetupMethod = createMethod( org.lgna.project.ast.Access.PRIVATE, Void.TYPE, "performCustomSetup" );

		org.lgna.project.ast.MethodDeclaredInAlice handleActiveChangedMethod = createMethod( org.lgna.project.ast.Access.PROTECTED, Void.TYPE, "handleActiveChanged" );
		org.lgna.project.ast.ParameterDeclaredInAlice isActiveParameter = new org.lgna.project.ast.ParameterDeclaredInAlice( "isActive", Boolean.class );
		org.lgna.project.ast.ParameterDeclaredInAlice activeCountParameter = new org.lgna.project.ast.ParameterDeclaredInAlice( "activeCount", Integer.class );
		handleActiveChangedMethod.parameters.add( isActiveParameter );
		handleActiveChangedMethod.parameters.add( activeCountParameter );

		org.lgna.project.ast.BlockStatement handleActiveChangedBody = handleActiveChangedMethod.body.getValue();
		
		org.lgna.project.ast.ConditionalStatement ifOuter = org.alice.ide.ast.NodeUtilities.createConditionalStatement( 
				new org.lgna.project.ast.ParameterAccess( isActiveParameter ) 
		);
		org.lgna.project.ast.ConditionalStatement ifInner = org.alice.ide.ast.NodeUtilities.createConditionalStatement( 
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
		
		org.lgna.project.ast.MethodDeclaredInJava preserveVehiclesAndVantagePointsMethod = org.lgna.project.ast.MethodDeclaredInJava.get( sceneCls, "preserveVehiclesAndVantagePoints" );
		org.lgna.project.ast.MethodDeclaredInJava restoreVehiclesAndVantagePointsMethod = org.lgna.project.ast.MethodDeclaredInJava.get( sceneCls, "restoreVehiclesAndVantagePoints" );
		ifInnerFalseBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), restoreVehiclesAndVantagePointsMethod ) );
		ifOuterFalseBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.ThisExpression(), preserveVehiclesAndVantagePointsMethod ) );

		handleActiveChangedBody.statements.add( ifOuter );
		
		org.lgna.project.ast.TypeDeclaredInAlice sceneType = createType( "MyScene", org.lgna.story.Scene.class );
		sceneType.fields.add( sunField );
		sceneType.fields.add( groundField );
		sceneType.fields.add( cameraField );
		sceneType.methods.add( performCustomSetupMethod );
		sceneType.methods.add( performGeneratedSetupMethod );
		sceneType.methods.add( handleActiveChangedMethod );
		sceneType.methods.add( myFirstMethod );

		org.lgna.project.ast.FieldDeclaredInAlice sceneField = createPrivateFinalField( sceneType, "myScene" );
		org.lgna.project.ast.MethodDeclaredInAlice playOutStoryMethod = createMethod( org.lgna.project.ast.Access.PUBLIC, Void.TYPE, "playOutStory" );
		org.lgna.project.ast.BlockStatement playOutStoryBody = playOutStoryMethod.body.getValue();
		playOutStoryBody.statements.add( 
				createMethodInvocationStatement( 
						new org.lgna.project.ast.ThisExpression(), 
						org.lgna.project.ast.MethodDeclaredInJava.get( org.lgna.story.Program.class, "setActiveScene", org.lgna.story.Scene.class ),
						createThisFieldAccess( sceneField )
				)
		);
		playOutStoryBody.statements.add( 
				createMethodInvocationStatement( 
						createThisFieldAccess( sceneField ),
						myFirstMethod
				)
		);
		
		
		org.lgna.project.ast.TypeDeclaredInAlice rv = createType( "MyProgram", org.lgna.story.Program.class );
		rv.fields.add( sceneField );
		rv.methods.add( playOutStoryMethod );

		
		
		org.lgna.project.ast.ParameterDeclaredInAlice argsParameter = new org.lgna.project.ast.ParameterDeclaredInAlice( "args", String[].class );
		org.lgna.project.ast.MethodDeclaredInAlice mainMethod = createMethod( org.lgna.project.ast.Access.PUBLIC, Void.TYPE, "main" );
		mainMethod.parameters.add( argsParameter );
		org.lgna.project.ast.BlockStatement mainBody = mainMethod.body.getValue();

		mainMethod.isStatic.setValue( true );
		
		org.lgna.project.ast.VariableDeclarationStatement variableDeclarationStatement = createVariableDeclarationStatementInitializedByInstanceCreation( "story", rv );
		org.lgna.project.ast.VariableDeclaredInAlice storyVariable = variableDeclarationStatement.variable.getValue();
		mainBody.statements.add( variableDeclarationStatement );
		mainBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.VariableAccess( storyVariable ), rv.findMethod( "initializeInFrame", String[].class ), new org.lgna.project.ast.ParameterAccess( argsParameter ) ) );
		mainBody.statements.add( createMethodInvocationStatement( new org.lgna.project.ast.VariableAccess( storyVariable ), playOutStoryMethod ) );
		rv.methods.add( mainMethod );
		
		return rv;
	}
}
