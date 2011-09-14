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
package org.alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public class AstUtilities {
	public static org.lgna.project.ast.UserMethod createMethod( String name, org.lgna.project.ast.AbstractType<?,?,?> returnType ) {
		return new org.lgna.project.ast.UserMethod( name, returnType, new org.lgna.project.ast.UserParameter[] {}, new org.lgna.project.ast.BlockStatement() );
	}
	public static org.lgna.project.ast.UserMethod createFunction( String name, org.lgna.project.ast.AbstractType<?,?,?> returnType ) {
		return createMethod( name, returnType );
	}
	public static org.lgna.project.ast.UserMethod createProcedure( String name ) {
		return createMethod( name, org.lgna.project.ast.JavaType.VOID_TYPE );
	}
	
	public static org.lgna.project.ast.NamedUserType createType( String name, org.lgna.project.ast.AbstractType<?,?,?> superType ) {
		org.lgna.project.ast.NamedUserConstructor constructor = new org.lgna.project.ast.NamedUserConstructor();
		constructor.body.setValue( new org.lgna.project.ast.ConstructorBlockStatement() );
		org.lgna.project.ast.NamedUserType rv = new org.lgna.project.ast.NamedUserType();
		rv.name.setValue( name );
		rv.superType.setValue( superType );
		rv.constructors.add( constructor );
		return rv;
	}
	public static org.lgna.project.ast.DoInOrder createDoInOrder() {
		return new org.lgna.project.ast.DoInOrder( new org.lgna.project.ast.BlockStatement() );
	}
	public static org.lgna.project.ast.DoTogether createDoTogether() {
		return new org.lgna.project.ast.DoTogether( new org.lgna.project.ast.BlockStatement() );
	}
	public static org.lgna.project.ast.DoInThread createDoInThread() {
		return new org.lgna.project.ast.DoInThread( new org.lgna.project.ast.BlockStatement() );
	}
	public static org.lgna.project.ast.Comment createComment() {
		return new org.lgna.project.ast.Comment();
	}
	public static org.lgna.project.ast.ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( org.lgna.project.ast.Expression leftOperand, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, org.lgna.project.ast.AbstractType<?,?,?> rightOperandType, org.lgna.project.ast.AbstractType<?,?,?> expressionType ) {
		return new org.lgna.project.ast.ArithmeticInfixExpression( leftOperand, operator, new org.alice.ide.ast.EmptyExpression( rightOperandType ), expressionType );
	}
	public static org.lgna.project.ast.ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( org.lgna.project.ast.Expression leftOperand, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, Class<?> rightOperandCls, Class<?> expressionCls ) {
		return createIncompleteArithmeticInfixExpression( leftOperand, operator, org.lgna.project.ast.JavaType.getInstance( rightOperandCls ), org.lgna.project.ast.JavaType.getInstance( expressionCls ) );
	}
	public static org.lgna.project.ast.ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( org.lgna.project.ast.AbstractType<?,?,?> leftOperandType, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, org.lgna.project.ast.AbstractType<?,?,?> rightOperandType, org.lgna.project.ast.AbstractType<?,?,?> expressionType ) {
		return createIncompleteArithmeticInfixExpression( new org.alice.ide.ast.EmptyExpression( rightOperandType ), operator, rightOperandType, expressionType );
	}
	public static org.lgna.project.ast.ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( Class<?> leftOperandCls, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, Class<?> rightOperandCls, Class<?> expressionCls ) {
		return createIncompleteArithmeticInfixExpression( org.lgna.project.ast.JavaType.getInstance( leftOperandCls ), operator, org.lgna.project.ast.JavaType.getInstance( rightOperandCls ), org.lgna.project.ast.JavaType.getInstance( expressionCls ) );
	}
	public static org.lgna.project.ast.ConditionalInfixExpression createIncompleteConditionalInfixExpression( org.lgna.project.ast.Expression leftOperand, org.lgna.project.ast.ConditionalInfixExpression.Operator operator ) {
		return new org.lgna.project.ast.ConditionalInfixExpression( leftOperand, operator, new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ) );
	}
	public static org.lgna.project.ast.ConditionalInfixExpression createIncompleteConditionalInfixExpression( org.lgna.project.ast.ConditionalInfixExpression.Operator operator ) {
		return createIncompleteConditionalInfixExpression( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ), operator );
	}
	
	public static org.lgna.project.ast.RelationalInfixExpression createIncompleteRelationalInfixExpression( org.lgna.project.ast.AbstractType<?,?,?> leftOperandType, org.lgna.project.ast.RelationalInfixExpression.Operator operator, org.lgna.project.ast.AbstractType<?,?,?> rightOperandType ) {
		return new org.lgna.project.ast.RelationalInfixExpression( new org.alice.ide.ast.EmptyExpression( rightOperandType ), operator, new org.alice.ide.ast.EmptyExpression( rightOperandType ), leftOperandType, rightOperandType );
	}
	public static org.lgna.project.ast.RelationalInfixExpression createIncompleteRelationalInfixExpression( Class<?> leftOperandCls, org.lgna.project.ast.RelationalInfixExpression.Operator operator, Class<?> rightOperandCls ) {
		return createIncompleteRelationalInfixExpression( org.lgna.project.ast.JavaType.getInstance( leftOperandCls ), operator, org.lgna.project.ast.JavaType.getInstance( rightOperandCls ) );
	}

	public static org.lgna.project.ast.LogicalComplement createIncompleteLogicalComplement() {
		return new org.lgna.project.ast.LogicalComplement( new EmptyExpression( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ) );
	}
	
	public static org.lgna.project.ast.VariableDeclarationStatement createVariableDeclarationStatement( org.lgna.project.ast.UserVariable variable, org.lgna.project.ast.Expression initializerExpression ) {
		return new org.lgna.project.ast.VariableDeclarationStatement(
				variable,
				initializerExpression 
		);
	}
	public static org.lgna.project.ast.VariableDeclarationStatement createIncompleteVariableDeclarationStatement() {
		org.lgna.project.ast.AbstractType<?,?,?> type = org.lgna.project.ast.JavaType.OBJECT_TYPE;
		return createVariableDeclarationStatement( new org.lgna.project.ast.UserVariable( "???", type ), new org.alice.ide.ast.EmptyExpression( type ) );
	}

	public static org.lgna.project.ast.CountLoop createCountLoop( org.lgna.project.ast.Expression count ) {
		return new org.lgna.project.ast.CountLoop(
				new org.lgna.project.ast.UserVariable( null, org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ),
				new org.lgna.project.ast.UserConstant( null, org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ),
				count, 
				new org.lgna.project.ast.BlockStatement() 
		);
	}
	public static org.lgna.project.ast.CountLoop createIncompleteCountLoop() {
		return createCountLoop( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ) );
	}
	public static org.lgna.project.ast.WhileLoop createWhileLoop( org.lgna.project.ast.Expression conditional ) {
		return new org.lgna.project.ast.WhileLoop(
				conditional, 
				new org.lgna.project.ast.BlockStatement() 
		);
	}
	public static org.lgna.project.ast.WhileLoop createIncompleteWhileLoop() {
		return createWhileLoop( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ) );
	}
	public static org.lgna.project.ast.ConditionalStatement createConditionalStatement( org.lgna.project.ast.Expression conditional ) {
		return new org.lgna.project.ast.ConditionalStatement(
				new org.lgna.project.ast.BooleanExpressionBodyPair[] {
						new org.lgna.project.ast.BooleanExpressionBodyPair( 
								conditional, 
								new org.lgna.project.ast.BlockStatement()
						)
				}, 
				new org.lgna.project.ast.BlockStatement() 
		);
	}
	public static org.lgna.project.ast.ConditionalStatement createIncompleteConditionalStatement() {
		return createConditionalStatement( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ) );
	}

	public static org.lgna.project.ast.ForEachInArrayLoop createForEachInArrayLoop( org.lgna.project.ast.Expression arrayExpression ) {
		org.lgna.project.ast.UserVariable variable = new org.lgna.project.ast.UserVariable( null, arrayExpression.getType().getComponentType() );
		return new org.lgna.project.ast.ForEachInArrayLoop(
				variable,
				arrayExpression, 
				new org.lgna.project.ast.BlockStatement() 
		);
	}
	public static org.lgna.project.ast.ForEachInArrayLoop createIncompleteForEachInArrayLoop() {
		return createForEachInArrayLoop( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.getInstance( Object[].class ) ) );
	}

	public static org.lgna.project.ast.EachInArrayTogether createEachInArrayTogether( org.lgna.project.ast.Expression arrayExpression ) {
		org.lgna.project.ast.UserVariable variable = new org.lgna.project.ast.UserVariable( null, arrayExpression.getType().getComponentType() );
		return new org.lgna.project.ast.EachInArrayTogether(
				variable,
				arrayExpression, 
				new org.lgna.project.ast.BlockStatement() 
		);
	}
	public static org.lgna.project.ast.EachInArrayTogether createIncompleteEachInArrayTogether() {
		return createEachInArrayTogether( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.getInstance( Object[].class ) ) );
	}
	
	public static org.lgna.project.ast.MethodInvocation createIncompleteMethodInvocation( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractMethod method ) {
		org.lgna.project.ast.MethodInvocation rv = new org.lgna.project.ast.MethodInvocation();
		rv.expression.setValue( expression );
		rv.method.setValue( method );
		for( org.lgna.project.ast.AbstractParameter parameter : method.getParameters() ) {
			org.lgna.project.ast.Argument argument = new org.lgna.project.ast.Argument( parameter, new EmptyExpression( parameter.getValueType() ) );
			rv.arguments.add( argument );
		}
		return rv;
	}
	public static org.lgna.project.ast.MethodInvocation createIncompleteMethodInvocation( org.lgna.project.ast.AbstractMethod method ) {
		return AstUtilities.createIncompleteMethodInvocation( new SelectedFieldExpression( method.getDeclaringType() ), method );
	}
	public static org.lgna.project.ast.MethodInvocation createIncompleteStaticMethodInvocation( org.lgna.project.ast.AbstractMethod method ) {
		return AstUtilities.createIncompleteMethodInvocation( new org.lgna.project.ast.TypeExpression( method.getDeclaringType() ), method );
	}
	public static org.lgna.project.ast.MethodInvocation createStaticMethodInvocation( org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression... argumentExpressions ) {
		return AstUtilities.createMethodInvocation( new org.lgna.project.ast.TypeExpression( method.getDeclaringType() ), method, argumentExpressions );
	}
	public static org.lgna.project.ast.FieldAccess createFieldAccess( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.FieldAccess rv = new org.lgna.project.ast.FieldAccess();
		rv.expression.setValue( expression );
		rv.field.setValue( field );
		return rv;
	}
	public static org.lgna.project.ast.FieldAccess createStaticFieldAccess( org.lgna.project.ast.AbstractField field ) {
		assert field.isStatic();
		return createFieldAccess( new org.lgna.project.ast.TypeExpression( field.getDeclaringType() ), field );
	}
	public static org.lgna.project.ast.FieldAccess createStaticFieldAccess( Class<?> cls, String fieldName ) {
		java.lang.reflect.Field fld = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getDeclaredField( cls, fieldName );
		org.lgna.project.ast.JavaField field = org.lgna.project.ast.JavaField.getInstance( fld );
		return createStaticFieldAccess( field );
	}
	
	public static org.lgna.project.ast.FieldAccess createIncompleteFieldAccess( org.lgna.project.ast.AbstractField field ) {
		return AstUtilities.createFieldAccess( new SelectedFieldExpression( field.getDeclaringType() ), field );
	}
	private static org.lgna.project.ast.AssignmentExpression createIncompleteAssignmentExpression( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.FieldAccess fieldAccess = AstUtilities.createFieldAccess( expression, field );
		org.lgna.project.ast.AbstractType<?,?,?> valueType = field.getValueType();
		return new org.lgna.project.ast.AssignmentExpression( valueType, fieldAccess, org.lgna.project.ast.AssignmentExpression.Operator.ASSIGN, new EmptyExpression( valueType ) );
	}
	public static org.lgna.project.ast.AssignmentExpression createIncompleteAssignmentExpression( org.lgna.project.ast.AbstractField field ) {
		return AstUtilities.createIncompleteAssignmentExpression( new SelectedFieldExpression( field.getDeclaringType() ), field );
	}
	
	public static org.lgna.project.ast.MethodInvocation createNextMethodInvocation( org.lgna.project.ast.MethodInvocation prevMethodInvocation, org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractMethod nextMethod ) {
		org.lgna.project.ast.MethodInvocation rv = new org.lgna.project.ast.MethodInvocation();
		rv.expression.setValue( prevMethodInvocation.expression.getValue() );
		rv.method.setValue( nextMethod );
		java.util.ArrayList< ? extends org.lgna.project.ast.AbstractParameter > parameters = nextMethod.getParameters();
		final int N = parameters.size();
		for( int i=0; i<N-1; i++ ) {
			rv.arguments.add( new org.lgna.project.ast.Argument( parameters.get( i ), prevMethodInvocation.arguments.get( i ).expression.getValue() ) );
		}
		rv.arguments.add( new org.lgna.project.ast.Argument( parameters.get( N-1 ), expression ) );
		return rv;
	}

	public static org.lgna.project.ast.MethodInvocation completeMethodInvocation( org.lgna.project.ast.MethodInvocation rv, org.lgna.project.ast.Expression instanceExpression, org.lgna.project.ast.Expression... argumentExpressions ) {
		rv.expression.setValue( instanceExpression );
		int i = 0;
		for( org.lgna.project.ast.Argument argument : rv.arguments ) {
			argument.expression.setValue( argumentExpressions[ i ] );
			i ++;
		}
		return rv;
	}
	public static org.lgna.project.ast.MethodInvocation completeMethodInvocation( org.lgna.project.ast.MethodInvocation rv, org.lgna.project.ast.Expression... argumentExpressions ) {
		return completeMethodInvocation( rv, org.alice.ide.instancefactory.InstanceFactoryState.getInstance().getValue().createExpression(), argumentExpressions );
	}
	
	public static org.lgna.project.ast.MethodInvocation createMethodInvocation( org.lgna.project.ast.Expression instanceExpression, org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression... argumentExpressions ) {
		org.lgna.project.ast.MethodInvocation rv = new org.lgna.project.ast.MethodInvocation();
		rv.expression.setValue( instanceExpression );
		rv.method.setValue( method );
		int i = 0;
		for( org.lgna.project.ast.AbstractParameter parameter : method.getParameters() ) {
			org.lgna.project.ast.Argument argument = new org.lgna.project.ast.Argument( parameter, argumentExpressions[ i ] );
			rv.arguments.add( argument );
			i++;
		}
		return rv;
	}
	public static org.lgna.project.ast.ExpressionStatement createMethodInvocationStatement( org.lgna.project.ast.Expression instanceExpression, org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression... argumentExpressions ) {
		return new org.lgna.project.ast.ExpressionStatement( createMethodInvocation( instanceExpression, method, argumentExpressions ) );
	}
	public static org.lgna.project.ast.TypeExpression createTypeExpression( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		return new org.lgna.project.ast.TypeExpression( type );
	}
	public static org.lgna.project.ast.TypeExpression createTypeExpression( Class<?> cls ) {
		return createTypeExpression( org.lgna.project.ast.JavaType.getInstance( cls ) );
	}
	
	public static org.lgna.project.ast.InstanceCreation createInstanceCreation( org.lgna.project.ast.AbstractConstructor constructor, org.lgna.project.ast.Expression... argumentExpressions ) {
		org.lgna.project.ast.InstanceCreation rv = new org.lgna.project.ast.InstanceCreation( constructor );
		int i = 0;
		for( org.lgna.project.ast.AbstractParameter parameter : constructor.getParameters() ) {
			org.lgna.project.ast.Argument argument = new org.lgna.project.ast.Argument( parameter, argumentExpressions[ i ] );
			rv.arguments.add( argument );
			i++;
		}
		return rv;
	}
	public static org.lgna.project.ast.InstanceCreation createInstanceCreation( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		return createInstanceCreation( type.getDeclaredConstructor() );
	}
	public static org.lgna.project.ast.InstanceCreation createInstanceCreation( Class<?> cls, Class<?>[] parameterClses, org.lgna.project.ast.Expression... argumentExpressions ) {
		return createInstanceCreation( org.lgna.project.ast.JavaConstructor.getInstance( cls, parameterClses ), argumentExpressions );
	}
	public static org.lgna.project.ast.InstanceCreation createInstanceCreation( Class<?> cls ) {
		return createInstanceCreation( org.lgna.project.ast.JavaType.getInstance( cls ) );
	}
	
	public static org.lgna.project.ast.InstanceCreation createIncompleteInstanceCreation( org.lgna.project.ast.AbstractConstructor constructor ) {
		org.lgna.project.ast.InstanceCreation rv = new org.lgna.project.ast.InstanceCreation( constructor );
		for( org.lgna.project.ast.AbstractParameter parameter : constructor.getParameters() ) {
			org.lgna.project.ast.Argument argument = new org.lgna.project.ast.Argument( parameter, new EmptyExpression( parameter.getValueType() ) );
			rv.arguments.add( argument );
		}
		return rv;
	}
	
	public static org.lgna.project.ast.ArrayInstanceCreation createArrayInstanceCreation( org.lgna.project.ast.AbstractType<?,?,?> arrayType, org.lgna.project.ast.Expression... expressions ) {
		Integer[] lengths = { expressions.length };
		return new org.lgna.project.ast.ArrayInstanceCreation( arrayType, lengths, expressions );
	}
	public static org.lgna.project.ast.ArrayInstanceCreation createArrayInstanceCreation( Class<?> arrayCls, org.lgna.project.ast.Expression... expressions ) {
		return createArrayInstanceCreation( org.lgna.project.ast.JavaType.getInstance( arrayCls ), expressions );
	}
	public static org.lgna.project.ast.ArrayInstanceCreation createArrayInstanceCreation( org.lgna.project.ast.AbstractType<?,?,?> arrayType, java.util.Collection< org.lgna.project.ast.Expression > expressions ) {
		return createArrayInstanceCreation( arrayType, edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( expressions, org.lgna.project.ast.Expression.class ) );
	}
	public static org.lgna.project.ast.ArrayInstanceCreation createArrayInstanceCreation( Class<?> arrayCls, java.util.Collection< org.lgna.project.ast.Expression > expressions ) {
		return createArrayInstanceCreation( org.lgna.project.ast.JavaType.getInstance( arrayCls ), edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( expressions, org.lgna.project.ast.Expression.class ) );
	}

	public static org.lgna.project.ast.JavaMethod lookupMethod( Class<?> cls, String methodName, Class<?>... parameterTypes ) {
		return org.lgna.project.ast.JavaMethod.getInstance( cls, methodName, parameterTypes );
	}

	
	public static org.lgna.project.ast.ReturnStatement createIncompleteReturnStatement( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		return createReturnStatement( type, new EmptyExpression( type ) );
	}
	public static org.lgna.project.ast.ReturnStatement createReturnStatement( org.lgna.project.ast.AbstractType<?,?,?> type, org.lgna.project.ast.Expression expression ) {
		return new org.lgna.project.ast.ReturnStatement( type, expression );
	}
	
	public static org.lgna.project.ast.Expression createVariableAssignment( org.lgna.project.ast.UserVariable variable, org.lgna.project.ast.Expression valueExpression ) {
		org.lgna.project.ast.Expression variableAccess = new org.lgna.project.ast.VariableAccess( variable ); 
		return new org.lgna.project.ast.AssignmentExpression( variable.valueType.getValue(), variableAccess, org.lgna.project.ast.AssignmentExpression.Operator.ASSIGN, valueExpression ); 
	}
	public static org.lgna.project.ast.ExpressionStatement createVariableAssignmentStatement( org.lgna.project.ast.UserVariable variable, org.lgna.project.ast.Expression valueExpression ) {
		return new org.lgna.project.ast.ExpressionStatement( createVariableAssignment( variable, valueExpression) );
	}
	public static org.lgna.project.ast.ExpressionStatement createIncompleteVariableAssignmentStatement( org.lgna.project.ast.UserVariable variable ) {
		return createVariableAssignmentStatement( variable, new EmptyExpression( variable.valueType.getValue() ) );
	}

	public static org.lgna.project.ast.ExpressionStatement createVariableArrayAssignmentStatement( org.lgna.project.ast.UserVariable variable, org.lgna.project.ast.Expression indexExpression, org.lgna.project.ast.Expression valueExpression ) {
		org.lgna.project.ast.Expression variableAccess = new org.lgna.project.ast.VariableAccess( variable ); 
		org.lgna.project.ast.ArrayAccess arrayAccess = new org.lgna.project.ast.ArrayAccess( variable.valueType.getValue(), variableAccess, indexExpression ); 
		org.lgna.project.ast.Expression expression = new org.lgna.project.ast.AssignmentExpression( variable.valueType.getValue().getComponentType(), arrayAccess, org.lgna.project.ast.AssignmentExpression.Operator.ASSIGN, valueExpression ); 
		return new org.lgna.project.ast.ExpressionStatement( expression );
	}
	public static org.lgna.project.ast.ExpressionStatement createIncompleteVariableArrayAssignmentStatement( org.lgna.project.ast.UserVariable variable ) {
		return createVariableArrayAssignmentStatement( variable, new EmptyExpression( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( variable.valueType.getValue().getComponentType() ) );
	}
	public static org.lgna.project.ast.ExpressionStatement createParameterArrayAssignmentStatement( org.lgna.project.ast.UserParameter parameter, org.lgna.project.ast.Expression indexExpression, org.lgna.project.ast.Expression valueExpression ) {
		org.lgna.project.ast.Expression parameterAccess = new org.lgna.project.ast.ParameterAccess( parameter ); 
		org.lgna.project.ast.ArrayAccess arrayAccess = new org.lgna.project.ast.ArrayAccess( parameter.valueType.getValue(), parameterAccess, indexExpression ); 
		org.lgna.project.ast.Expression expression = new org.lgna.project.ast.AssignmentExpression( parameter.valueType.getValue().getComponentType(), arrayAccess, org.lgna.project.ast.AssignmentExpression.Operator.ASSIGN, valueExpression ); 
		return new org.lgna.project.ast.ExpressionStatement( expression );
	}
	public static org.lgna.project.ast.ExpressionStatement createIncompleteParameterArrayAssignmentStatement( org.lgna.project.ast.UserParameter parameter ) {
		return createParameterArrayAssignmentStatement( parameter, new EmptyExpression( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( parameter.valueType.getValue().getComponentType() ) );
	}

	
	public static org.lgna.project.ast.StringConcatenation createStringConcatenation( org.lgna.project.ast.Expression left, org.lgna.project.ast.Expression right ) {
		return new org.lgna.project.ast.StringConcatenation( left, right );
	}
	public static org.lgna.project.ast.StringConcatenation createIncompleteStringConcatenation( org.lgna.project.ast.Expression leftOperand ) {
		return createStringConcatenation( leftOperand, new EmptyExpression( org.lgna.project.ast.JavaType.OBJECT_TYPE ) );
	}
	public static org.lgna.project.ast.StringConcatenation createIncompleteStringConcatenation() {
		return createIncompleteStringConcatenation( new EmptyExpression( org.lgna.project.ast.JavaType.OBJECT_TYPE ) );
	}

//	public static edu.cmu.cs.dennisc.alice.ast.AbstractParameter getNextParameter( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation ) {
//		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
//		final edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getNextLongerInChain();
//		
//		java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = nextLongerMethod.getParameters();
//		return parameters.get( parameters.size()-1 );
//	}
	
	public static java.util.Map< org.lgna.project.ast.ArgumentListProperty, org.lgna.project.ast.Argument > removeParameter( java.util.Map< org.lgna.project.ast.ArgumentListProperty, org.lgna.project.ast.Argument > rv, org.lgna.project.ast.UserCode code, org.lgna.project.ast.UserParameter parameterDeclaredInAlice, int index, java.util.List< org.lgna.project.ast.ArgumentListProperty > argumentListProperties ) {
		assert rv != null;
		assert code.getParamtersProperty().get( index ) == parameterDeclaredInAlice;
		rv.clear();
		code.getParamtersProperty().remove( index );
		for( org.lgna.project.ast.ArgumentListProperty argumentListProperty : argumentListProperties ) {
			org.lgna.project.ast.Argument argument = argumentListProperty.remove( index );
			if( argument != null ) {
				rv.put( argumentListProperty, argument );
			}
		}
		return rv;
	}
	public static void addParameter( java.util.Map< org.lgna.project.ast.ArgumentListProperty, org.lgna.project.ast.Argument > map, org.lgna.project.ast.UserCode code, org.lgna.project.ast.UserParameter parameterDeclaredInAlice, int index, java.util.List< org.lgna.project.ast.ArgumentListProperty > argumentListProperties ) {
		code.getParamtersProperty().add( index, parameterDeclaredInAlice );
		for( org.lgna.project.ast.ArgumentListProperty argumentListProperty : argumentListProperties ) {
			org.lgna.project.ast.Argument argument = map.get( code );
			if( argument != null ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: addParameter" );
				argument = new org.lgna.project.ast.Argument( parameterDeclaredInAlice, new org.lgna.project.ast.NullLiteral() );
			}
			argumentListProperty.add( index, argument );
		}
	}
	
	public static org.lgna.project.ast.AbstractType<?,?,?>[] getParameterValueTypes( org.lgna.project.ast.AbstractMethod method ) {
		java.util.ArrayList< ? extends org.lgna.project.ast.AbstractParameter > parameters = method.getParameters();
		org.lgna.project.ast.AbstractType<?,?,?>[] rv = new org.lgna.project.ast.AbstractType[ parameters.size() ];
		int i = 0;
		for( org.lgna.project.ast.AbstractParameter parameter : parameters ) {
			rv[ i ] = parameter.getValueType();
			i++;
		}
		return rv;
	}
}
