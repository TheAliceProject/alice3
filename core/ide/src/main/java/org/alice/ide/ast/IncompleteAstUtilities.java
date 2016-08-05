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
package org.alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public class IncompleteAstUtilities {
	private IncompleteAstUtilities() {
		throw new AssertionError();
	}

	public static org.lgna.project.ast.ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( org.lgna.project.ast.Expression leftOperand, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, org.lgna.project.ast.AbstractType<?, ?, ?> rightOperandType, org.lgna.project.ast.AbstractType<?, ?, ?> expressionType ) {
		return new org.lgna.project.ast.ArithmeticInfixExpression( leftOperand, operator, new org.alice.ide.ast.EmptyExpression( rightOperandType ), expressionType );
	}

	public static org.lgna.project.ast.ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( org.lgna.project.ast.Expression leftOperand, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, Class<?> rightOperandCls, Class<?> expressionCls ) {
		return createIncompleteArithmeticInfixExpression( leftOperand, operator, org.lgna.project.ast.JavaType.getInstance( rightOperandCls ), org.lgna.project.ast.JavaType.getInstance( expressionCls ) );
	}

	public static org.lgna.project.ast.ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( org.lgna.project.ast.AbstractType<?, ?, ?> leftOperandType, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, org.lgna.project.ast.AbstractType<?, ?, ?> rightOperandType, org.lgna.project.ast.AbstractType<?, ?, ?> expressionType ) {
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

	public static org.lgna.project.ast.RelationalInfixExpression createIncompleteRelationalInfixExpression( org.lgna.project.ast.AbstractType<?, ?, ?> leftOperandType, org.lgna.project.ast.RelationalInfixExpression.Operator operator, org.lgna.project.ast.AbstractType<?, ?, ?> rightOperandType ) {
		return new org.lgna.project.ast.RelationalInfixExpression( new org.alice.ide.ast.EmptyExpression( rightOperandType ), operator, new org.alice.ide.ast.EmptyExpression( rightOperandType ), leftOperandType, rightOperandType );
	}

	public static org.lgna.project.ast.RelationalInfixExpression createIncompleteRelationalInfixExpression( Class<?> leftOperandCls, org.lgna.project.ast.RelationalInfixExpression.Operator operator, Class<?> rightOperandCls ) {
		return createIncompleteRelationalInfixExpression( org.lgna.project.ast.JavaType.getInstance( leftOperandCls ), operator, org.lgna.project.ast.JavaType.getInstance( rightOperandCls ) );
	}

	public static org.lgna.project.ast.LogicalComplement createIncompleteLogicalComplement() {
		return new org.lgna.project.ast.LogicalComplement( new EmptyExpression( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ) );
	}

	public static org.lgna.project.ast.LocalDeclarationStatement createIncompleteLocalDeclarationStatement() {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = org.lgna.project.ast.JavaType.OBJECT_TYPE;
		return org.lgna.project.ast.AstUtilities.createLocalDeclarationStatement( new org.lgna.project.ast.UserLocal( "???", type, false ), new org.alice.ide.ast.EmptyExpression( type ) );
	}

	public static org.lgna.project.ast.CountLoop createIncompleteCountLoop() {
		return org.lgna.project.ast.AstUtilities.createCountLoop( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ) );
	}

	public static org.lgna.project.ast.WhileLoop createIncompleteWhileLoop() {
		return org.lgna.project.ast.AstUtilities.createWhileLoop( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ) );
	}

	public static org.lgna.project.ast.ConditionalStatement createIncompleteConditionalStatement() {
		return org.lgna.project.ast.AstUtilities.createConditionalStatement( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ) );
	}

	public static org.lgna.project.ast.ForEachInArrayLoop createIncompleteForEachInArrayLoop() {
		return org.lgna.project.ast.AstUtilities.createForEachInArrayLoop( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.getInstance( Object[].class ) ) );
	}

	public static org.lgna.project.ast.EachInArrayTogether createIncompleteEachInArrayTogether() {
		return org.lgna.project.ast.AstUtilities.createEachInArrayTogether( new org.alice.ide.ast.EmptyExpression( org.lgna.project.ast.JavaType.getInstance( Object[].class ) ) );
	}

	public static org.lgna.project.ast.MethodInvocation createIncompleteMethodInvocation( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractMethod method ) {
		org.lgna.project.ast.MethodInvocation rv = new org.lgna.project.ast.MethodInvocation();
		rv.expression.setValue( expression );
		rv.method.setValue( method );
		for( org.lgna.project.ast.AbstractParameter parameter : method.getRequiredParameters() ) {
			org.lgna.project.ast.SimpleArgument argument = new org.lgna.project.ast.SimpleArgument( parameter, new EmptyExpression( parameter.getValueType() ) );
			rv.requiredArguments.add( argument );
		}
		return rv;
	}

	public static org.lgna.project.ast.MethodInvocation createIncompleteMethodInvocation( org.lgna.project.ast.AbstractMethod method ) {
		return IncompleteAstUtilities.createIncompleteMethodInvocation( new SelectedInstanceFactoryExpression( method.getDeclaringType() ), method );
	}

	public static org.lgna.project.ast.ExpressionStatement createIncompleteMethodInvocationStatement( org.lgna.project.ast.AbstractMethod method ) {
		return new org.lgna.project.ast.ExpressionStatement( createIncompleteMethodInvocation( method ) );
	}

	public static org.lgna.project.ast.MethodInvocation createIncompleteStaticMethodInvocation( org.lgna.project.ast.AbstractMethod method ) {
		return IncompleteAstUtilities.createIncompleteMethodInvocation( new org.lgna.project.ast.TypeExpression( method.getDeclaringType() ), method );
	}

	public static org.lgna.project.ast.ExpressionStatement createIncompleteStaticMethodInvocationStatement( org.lgna.project.ast.AbstractMethod method ) {
		return new org.lgna.project.ast.ExpressionStatement( createIncompleteStaticMethodInvocation( method ) );
	}

	public static org.lgna.project.ast.MethodInvocation completeMethodInvocation( org.lgna.project.ast.MethodInvocation rv, org.lgna.project.ast.Expression... argumentExpressions ) {
		org.alice.ide.ProjectDocumentFrame projectDocumentFrame = org.alice.ide.IDE.getActiveInstance().getDocumentFrame();
		return org.lgna.project.ast.AstUtilities.completeMethodInvocation( rv, projectDocumentFrame.getInstanceFactoryState().getValue().createExpression(), argumentExpressions );
	}

	public static org.lgna.project.ast.FieldAccess createIncompleteFieldAccess( org.lgna.project.ast.AbstractField field ) {
		return org.lgna.project.ast.AstUtilities.createFieldAccess( new SelectedInstanceFactoryExpression( field.getDeclaringType() ), field );
	}

	public static org.lgna.project.ast.AssignmentExpression createIncompleteAssignmentExpression( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.FieldAccess fieldAccess = org.lgna.project.ast.AstUtilities.createFieldAccess( expression, field );
		org.lgna.project.ast.AbstractType<?, ?, ?> valueType = field.getValueType();
		return new org.lgna.project.ast.AssignmentExpression( valueType, fieldAccess, org.lgna.project.ast.AssignmentExpression.Operator.ASSIGN, new EmptyExpression( valueType ) );
	}

	private static org.lgna.project.ast.AssignmentExpression createIncompleteAssignmentExpression() {
		org.lgna.project.ast.AbstractType<?, ?, ?> valueType = null;
		org.lgna.project.ast.FieldAccess leftSideExpression = null;
		return new org.lgna.project.ast.AssignmentExpression( valueType, leftSideExpression, org.lgna.project.ast.AssignmentExpression.Operator.ASSIGN, new EmptyExpression( valueType ) );
	}

	public static org.lgna.project.ast.AssignmentExpression createIncompleteAssignmentExpression( org.lgna.project.ast.AbstractField field ) {
		return IncompleteAstUtilities.createIncompleteAssignmentExpression( new SelectedInstanceFactoryExpression( field.getDeclaringType() ), field );
	}

	public static org.lgna.project.ast.ExpressionStatement createIncompleteAssignmentExpressionStatement( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractField field ) {
		return new org.lgna.project.ast.ExpressionStatement( createIncompleteAssignmentExpression( expression, field ) );
	}

	public static org.lgna.project.ast.ExpressionStatement createIncompleteAssignmentExpressionStatement( org.lgna.project.ast.AbstractField field ) {
		return new org.lgna.project.ast.ExpressionStatement( createIncompleteAssignmentExpression( field ) );
	}

	public static org.lgna.project.ast.ExpressionStatement createIncompleteAssignmentExpressionStatement() {
		return new org.lgna.project.ast.ExpressionStatement( createIncompleteAssignmentExpression() );
	}

	public static org.lgna.project.ast.InstanceCreation createIncompleteInstanceCreation( org.lgna.project.ast.AbstractConstructor constructor ) {
		org.lgna.project.ast.InstanceCreation rv = new org.lgna.project.ast.InstanceCreation( constructor );
		for( org.lgna.project.ast.AbstractParameter parameter : constructor.getRequiredParameters() ) {
			org.lgna.project.ast.SimpleArgument argument = new org.lgna.project.ast.SimpleArgument( parameter, new EmptyExpression( parameter.getValueType() ) );
			rv.requiredArguments.add( argument );
		}
		return rv;
	}

	public static org.lgna.project.ast.ReturnStatement createIncompleteReturnStatement( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return org.lgna.project.ast.AstUtilities.createReturnStatement( type, new EmptyExpression( type ) );
	}

	public static org.lgna.project.ast.AssignmentExpression createIncompleteLocalAssignment( org.lgna.project.ast.UserLocal local ) {
		return org.lgna.project.ast.AstUtilities.createLocalAssignment( local, new EmptyExpression( local.valueType.getValue() ) );
	}

	public static org.lgna.project.ast.ExpressionStatement createIncompleteLocalAssignmentStatement( org.lgna.project.ast.UserLocal local ) {
		return org.lgna.project.ast.AstUtilities.createLocalAssignmentStatement( local, new EmptyExpression( local.valueType.getValue() ) );
	}

	public static org.lgna.project.ast.AssignmentExpression createIncompleteLocalArrayAssignment( org.lgna.project.ast.UserLocal local ) {
		return org.lgna.project.ast.AstUtilities.createLocalArrayAssignment( local, new EmptyExpression( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( local.valueType.getValue().getComponentType() ) );
	}

	public static org.lgna.project.ast.ExpressionStatement createIncompleteLocalArrayAssignmentStatement( org.lgna.project.ast.UserLocal local ) {
		return org.lgna.project.ast.AstUtilities.createLocalArrayAssignmentStatement( local, new EmptyExpression( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( local.valueType.getValue().getComponentType() ) );
	}

	public static org.lgna.project.ast.AssignmentExpression createIncompleteParameterArrayAssignment( org.lgna.project.ast.UserParameter parameter ) {
		return org.lgna.project.ast.AstUtilities.createParameterArrayAssignment( parameter, new EmptyExpression( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( parameter.valueType.getValue().getComponentType() ) );
	}

	public static org.lgna.project.ast.ExpressionStatement createIncompleteParameterArrayAssignmentStatement( org.lgna.project.ast.UserParameter parameter ) {
		return org.lgna.project.ast.AstUtilities.createParameterArrayAssignmentStatement( parameter, new EmptyExpression( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( parameter.valueType.getValue().getComponentType() ) );
	}

	public static org.lgna.project.ast.StringConcatenation createIncompleteStringConcatenation( org.lgna.project.ast.Expression leftOperand ) {
		return org.lgna.project.ast.AstUtilities.createStringConcatenation( leftOperand, new EmptyExpression( org.lgna.project.ast.JavaType.OBJECT_TYPE ) );
	}

	public static org.lgna.project.ast.StringConcatenation createIncompleteStringConcatenation() {
		return createIncompleteStringConcatenation( new EmptyExpression( org.lgna.project.ast.JavaType.OBJECT_TYPE ) );
	}
}
