/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public class NodeUtilities {
	public static edu.cmu.cs.dennisc.alice.ast.DoInOrder createDoInOrder() {
		return new edu.cmu.cs.dennisc.alice.ast.DoInOrder( new edu.cmu.cs.dennisc.alice.ast.BlockStatement() );
	}
	public static edu.cmu.cs.dennisc.alice.ast.DoTogether createDoTogether() {
		return new edu.cmu.cs.dennisc.alice.ast.DoTogether( new edu.cmu.cs.dennisc.alice.ast.BlockStatement() );
	}
	public static edu.cmu.cs.dennisc.alice.ast.Comment createComment() {
		return new edu.cmu.cs.dennisc.alice.ast.Comment();
	}
	
	public static edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement createVariableDeclarationStatement( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable, edu.cmu.cs.dennisc.alice.ast.Expression initializerExpression ) {
		return new edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement(
				variable,
				initializerExpression 
		);
	}
	public static edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement createIncompleteVariableDeclarationStatement() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class );
		return createVariableDeclarationStatement( new edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice( "???", type ), new org.alice.ide.ast.EmptyExpression( type ) );
	}

	public static edu.cmu.cs.dennisc.alice.ast.CountLoop createCountLoop( edu.cmu.cs.dennisc.alice.ast.Expression count ) {
		return new edu.cmu.cs.dennisc.alice.ast.CountLoop(
				new edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice( null, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ),
				new edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice( null, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ),
				count, 
				new edu.cmu.cs.dennisc.alice.ast.BlockStatement() 
		);
	}
	public static edu.cmu.cs.dennisc.alice.ast.CountLoop createIncompleteCountLoop() {
		return createCountLoop( new org.alice.ide.ast.EmptyExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) );
	}
	public static edu.cmu.cs.dennisc.alice.ast.WhileLoop createWhileLoop( edu.cmu.cs.dennisc.alice.ast.Expression conditional ) {
		return new edu.cmu.cs.dennisc.alice.ast.WhileLoop(
				conditional, 
				new edu.cmu.cs.dennisc.alice.ast.BlockStatement() 
		);
	}
	public static edu.cmu.cs.dennisc.alice.ast.WhileLoop createIncompleteWhileLoop() {
		return createWhileLoop( new org.alice.ide.ast.EmptyExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) );
	}
	public static edu.cmu.cs.dennisc.alice.ast.ConditionalStatement createConditionalStatement( edu.cmu.cs.dennisc.alice.ast.Expression conditional ) {
		return new edu.cmu.cs.dennisc.alice.ast.ConditionalStatement(
				new edu.cmu.cs.dennisc.alice.ast.BooleanExpressionBodyPair[] {
						new edu.cmu.cs.dennisc.alice.ast.BooleanExpressionBodyPair( 
								conditional, 
								new edu.cmu.cs.dennisc.alice.ast.BlockStatement()
						)
				}, 
				new edu.cmu.cs.dennisc.alice.ast.BlockStatement() 
		);
	}
	public static edu.cmu.cs.dennisc.alice.ast.ConditionalStatement createIncompleteConditionalStatement() {
		return createConditionalStatement( new org.alice.ide.ast.EmptyExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) );
	}
	public static edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop createForEachInArrayLoop( edu.cmu.cs.dennisc.alice.ast.Expression arrayExpression ) {
		edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = new edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice( null, arrayExpression.getType().getComponentType() );
		return new edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop(
				variable,
				arrayExpression, 
				new edu.cmu.cs.dennisc.alice.ast.BlockStatement() 
		);
	}
	public static edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop createIncompleteForEachInArrayLoop() {
		return createForEachInArrayLoop( new org.alice.ide.ast.EmptyExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object[].class ) ) );
	}
	public static edu.cmu.cs.dennisc.alice.ast.MethodInvocation createIncompleteMethodInvocation( edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation();
		rv.expression.setValue( expression );
		rv.method.setValue( method );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : method.getParameters() ) {
			edu.cmu.cs.dennisc.alice.ast.Argument argument = new edu.cmu.cs.dennisc.alice.ast.Argument( parameter, new EmptyExpression( parameter.getDesiredValueType() ) );
			rv.arguments.add( argument );
		}
		return rv;
	}
	public static edu.cmu.cs.dennisc.alice.ast.MethodInvocation createIncompleteMethodInvocation( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		return NodeUtilities.createIncompleteMethodInvocation( new SelectedFieldExpression( method.getDeclaringType() ), method );
	}
	private static edu.cmu.cs.dennisc.alice.ast.FieldAccess createIncompleteFieldAccess( edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		edu.cmu.cs.dennisc.alice.ast.FieldAccess rv = new edu.cmu.cs.dennisc.alice.ast.FieldAccess();
		rv.expression.setValue( expression );
		rv.field.setValue( field );
		return rv;
	}
	public static edu.cmu.cs.dennisc.alice.ast.FieldAccess createIncompleteFieldAccess( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return NodeUtilities.createIncompleteFieldAccess( new SelectedFieldExpression( field.getDeclaringType() ), field );
	}
	private static edu.cmu.cs.dennisc.alice.ast.AssignmentExpression createIncompleteAssignmentExpression( edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = NodeUtilities.createIncompleteFieldAccess( expression, field );
		edu.cmu.cs.dennisc.alice.ast.AbstractType valueType = field.getValueType();
		return new edu.cmu.cs.dennisc.alice.ast.AssignmentExpression( valueType, fieldAccess, edu.cmu.cs.dennisc.alice.ast.AssignmentExpression.Operator.ASSIGN, new EmptyExpression( valueType ) );
	}
	public static edu.cmu.cs.dennisc.alice.ast.AssignmentExpression createIncompleteAssignmentExpression( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return NodeUtilities.createIncompleteAssignmentExpression( new SelectedFieldExpression( field.getDeclaringType() ), field );
	}
	
	public static edu.cmu.cs.dennisc.alice.ast.MethodInvocation createNextMethodInvocation( edu.cmu.cs.dennisc.alice.ast.MethodInvocation prevMethodInvocation, edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextMethod ) {
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation();
		rv.expression.setValue( prevMethodInvocation.expression.getValue() );
		rv.method.setValue( nextMethod );
		java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = nextMethod.getParameters();
		final int N = parameters.size();
		for( int i=0; i<N-1; i++ ) {
			rv.arguments.add( new edu.cmu.cs.dennisc.alice.ast.Argument( parameters.get( i ), prevMethodInvocation.arguments.get( i ).expression.getValue() ) );
		}
		rv.arguments.add( new edu.cmu.cs.dennisc.alice.ast.Argument( parameters.get( N-1 ), expression ) );
		return rv;
	}
	
	public static edu.cmu.cs.dennisc.alice.ast.AbstractType[] getDesiredParameterValueTypes( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = method.getParameters();
		edu.cmu.cs.dennisc.alice.ast.AbstractType[] rv = new edu.cmu.cs.dennisc.alice.ast.AbstractType[ parameters.size() ];
		int i = 0;
		for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : parameters ) {
			rv[ i ] = parameter.getDesiredValueType();
			i++;
		}
		return rv;
	}
	public static edu.cmu.cs.dennisc.alice.ast.MethodInvocation completeMethodInvocation( edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv, edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression, edu.cmu.cs.dennisc.alice.ast.Expression[] argumentExpressions ) {
		rv.expression.setValue( instanceExpression );
		int i = 0;
		for( edu.cmu.cs.dennisc.alice.ast.Argument argument : rv.arguments ) {
			argument.expression.setValue( argumentExpressions[ i ] );
			i ++;
		}
		return rv;
	}
	public static edu.cmu.cs.dennisc.alice.ast.MethodInvocation completeMethodInvocation( edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv, edu.cmu.cs.dennisc.alice.ast.Expression[] argumentExpressions ) {
		return completeMethodInvocation( rv, org.alice.ide.IDE.getSingleton().createInstanceExpression(), argumentExpressions );
	}
	
	public static edu.cmu.cs.dennisc.alice.ast.TypeExpression createTypeExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return new edu.cmu.cs.dennisc.alice.ast.TypeExpression( type );
	}
	public static edu.cmu.cs.dennisc.alice.ast.TypeExpression createTypeExpression( Class<?> cls ) {
		return createTypeExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ) );
	}
}
