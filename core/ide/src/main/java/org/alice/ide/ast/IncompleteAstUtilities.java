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

import org.alice.ide.IDE;
import org.alice.ide.ProjectDocumentFrame;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ArithmeticInfixExpression;
import org.lgna.project.ast.AssignmentExpression;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.ConditionalInfixExpression;
import org.lgna.project.ast.ConditionalStatement;
import org.lgna.project.ast.CountLoop;
import org.lgna.project.ast.EachInArrayTogether;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.ForEachInArrayLoop;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.LocalDeclarationStatement;
import org.lgna.project.ast.LogicalComplement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.RelationalInfixExpression;
import org.lgna.project.ast.ReturnStatement;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.StringConcatenation;
import org.lgna.project.ast.TypeExpression;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserParameter;
import org.lgna.project.ast.WhileLoop;

/**
 * @author Dennis Cosgrove
 */
public class IncompleteAstUtilities {
	private IncompleteAstUtilities() {
		throw new AssertionError();
	}

	public static ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( Expression leftOperand, ArithmeticInfixExpression.Operator operator, AbstractType<?, ?, ?> rightOperandType, AbstractType<?, ?, ?> expressionType ) {
		return new ArithmeticInfixExpression( leftOperand, operator, new EmptyExpression( rightOperandType ), expressionType );
	}

	public static ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( Expression leftOperand, ArithmeticInfixExpression.Operator operator, Class<?> rightOperandCls, Class<?> expressionCls ) {
		return createIncompleteArithmeticInfixExpression( leftOperand, operator, JavaType.getInstance( rightOperandCls ), JavaType.getInstance( expressionCls ) );
	}

	public static ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( AbstractType<?, ?, ?> leftOperandType, ArithmeticInfixExpression.Operator operator, AbstractType<?, ?, ?> rightOperandType, AbstractType<?, ?, ?> expressionType ) {
		return createIncompleteArithmeticInfixExpression( new EmptyExpression( rightOperandType ), operator, rightOperandType, expressionType );
	}

	public static ArithmeticInfixExpression createIncompleteArithmeticInfixExpression( Class<?> leftOperandCls, ArithmeticInfixExpression.Operator operator, Class<?> rightOperandCls, Class<?> expressionCls ) {
		return createIncompleteArithmeticInfixExpression( JavaType.getInstance( leftOperandCls ), operator, JavaType.getInstance( rightOperandCls ), JavaType.getInstance( expressionCls ) );
	}

	public static ConditionalInfixExpression createIncompleteConditionalInfixExpression( Expression leftOperand, ConditionalInfixExpression.Operator operator ) {
		return new ConditionalInfixExpression( leftOperand, operator, new EmptyExpression( JavaType.BOOLEAN_OBJECT_TYPE ) );
	}

	public static ConditionalInfixExpression createIncompleteConditionalInfixExpression( ConditionalInfixExpression.Operator operator ) {
		return createIncompleteConditionalInfixExpression( new EmptyExpression( JavaType.BOOLEAN_OBJECT_TYPE ), operator );
	}

	public static RelationalInfixExpression createIncompleteRelationalInfixExpression( AbstractType<?, ?, ?> leftOperandType, RelationalInfixExpression.Operator operator, AbstractType<?, ?, ?> rightOperandType ) {
		return new RelationalInfixExpression( new EmptyExpression( rightOperandType ), operator, new EmptyExpression( rightOperandType ), leftOperandType, rightOperandType );
	}

	public static RelationalInfixExpression createIncompleteRelationalInfixExpression( Class<?> leftOperandCls, RelationalInfixExpression.Operator operator, Class<?> rightOperandCls ) {
		return createIncompleteRelationalInfixExpression( JavaType.getInstance( leftOperandCls ), operator, JavaType.getInstance( rightOperandCls ) );
	}

	public static LogicalComplement createIncompleteLogicalComplement() {
		return new LogicalComplement( new EmptyExpression( JavaType.BOOLEAN_OBJECT_TYPE ) );
	}

	public static LocalDeclarationStatement createIncompleteLocalDeclarationStatement() {
		AbstractType<?, ?, ?> type = JavaType.OBJECT_TYPE;
		return AstUtilities.createLocalDeclarationStatement( new UserLocal( "???", type, false ), new EmptyExpression( type ) );
	}

	public static CountLoop createIncompleteCountLoop() {
		return AstUtilities.createCountLoop( new EmptyExpression( JavaType.INTEGER_OBJECT_TYPE ) );
	}

	public static WhileLoop createIncompleteWhileLoop() {
		return AstUtilities.createWhileLoop( new EmptyExpression( JavaType.BOOLEAN_OBJECT_TYPE ) );
	}

	public static ConditionalStatement createIncompleteConditionalStatement() {
		return AstUtilities.createConditionalStatement( new EmptyExpression( JavaType.BOOLEAN_OBJECT_TYPE ) );
	}

	public static ForEachInArrayLoop createIncompleteForEachInArrayLoop() {
		return AstUtilities.createForEachInArrayLoop( new EmptyExpression( JavaType.getInstance( Object[].class ) ) );
	}

	public static EachInArrayTogether createIncompleteEachInArrayTogether() {
		return AstUtilities.createEachInArrayTogether( new EmptyExpression( JavaType.getInstance( Object[].class ) ) );
	}

	public static MethodInvocation createIncompleteMethodInvocation( Expression expression, AbstractMethod method ) {
		MethodInvocation rv = new MethodInvocation();
		rv.expression.setValue( expression );
		rv.method.setValue( method );
		for( AbstractParameter parameter : method.getRequiredParameters() ) {
			SimpleArgument argument = new SimpleArgument( parameter, new EmptyExpression( parameter.getValueType() ) );
			rv.requiredArguments.add( argument );
		}
		return rv;
	}

	public static MethodInvocation createIncompleteMethodInvocation( AbstractMethod method ) {
		return IncompleteAstUtilities.createIncompleteMethodInvocation( new SelectedInstanceFactoryExpression( method.getDeclaringType() ), method );
	}

	public static ExpressionStatement createIncompleteMethodInvocationStatement( AbstractMethod method ) {
		return new ExpressionStatement( createIncompleteMethodInvocation( method ) );
	}

	public static MethodInvocation createIncompleteStaticMethodInvocation( AbstractMethod method ) {
		return IncompleteAstUtilities.createIncompleteMethodInvocation( new TypeExpression( method.getDeclaringType() ), method );
	}

	public static ExpressionStatement createIncompleteStaticMethodInvocationStatement( AbstractMethod method ) {
		return new ExpressionStatement( createIncompleteStaticMethodInvocation( method ) );
	}

	public static MethodInvocation completeMethodInvocation( MethodInvocation rv, Expression... argumentExpressions ) {
		ProjectDocumentFrame projectDocumentFrame = IDE.getActiveInstance().getDocumentFrame();
		return AstUtilities.completeMethodInvocation( rv, projectDocumentFrame.getInstanceFactoryState().getValue().createExpression(), argumentExpressions );
	}

	public static FieldAccess createIncompleteFieldAccess( AbstractField field ) {
		return AstUtilities.createFieldAccess( new SelectedInstanceFactoryExpression( field.getDeclaringType() ), field );
	}

	public static AssignmentExpression createIncompleteAssignmentExpression( Expression expression, AbstractField field ) {
		FieldAccess fieldAccess = AstUtilities.createFieldAccess( expression, field );
		AbstractType<?, ?, ?> valueType = field.getValueType();
		return new AssignmentExpression( valueType, fieldAccess, AssignmentExpression.Operator.ASSIGN, new EmptyExpression( valueType ) );
	}

	private static AssignmentExpression createIncompleteAssignmentExpression() {
		AbstractType<?, ?, ?> valueType = null;
		FieldAccess leftSideExpression = null;
		return new AssignmentExpression( valueType, leftSideExpression, AssignmentExpression.Operator.ASSIGN, new EmptyExpression( valueType ) );
	}

	public static AssignmentExpression createIncompleteAssignmentExpression( AbstractField field ) {
		return IncompleteAstUtilities.createIncompleteAssignmentExpression( new SelectedInstanceFactoryExpression( field.getDeclaringType() ), field );
	}

	public static ExpressionStatement createIncompleteAssignmentExpressionStatement( Expression expression, AbstractField field ) {
		return new ExpressionStatement( createIncompleteAssignmentExpression( expression, field ) );
	}

	public static ExpressionStatement createIncompleteAssignmentExpressionStatement( AbstractField field ) {
		return new ExpressionStatement( createIncompleteAssignmentExpression( field ) );
	}

	public static ExpressionStatement createIncompleteAssignmentExpressionStatement() {
		return new ExpressionStatement( createIncompleteAssignmentExpression() );
	}

	public static InstanceCreation createIncompleteInstanceCreation( AbstractConstructor constructor ) {
		InstanceCreation rv = new InstanceCreation( constructor );
		for( AbstractParameter parameter : constructor.getRequiredParameters() ) {
			SimpleArgument argument = new SimpleArgument( parameter, new EmptyExpression( parameter.getValueType() ) );
			rv.requiredArguments.add( argument );
		}
		return rv;
	}

	public static ReturnStatement createIncompleteReturnStatement( AbstractType<?, ?, ?> type ) {
		return AstUtilities.createReturnStatement( type, new EmptyExpression( type ) );
	}

	public static AssignmentExpression createIncompleteLocalAssignment( UserLocal local ) {
		return AstUtilities.createLocalAssignment( local, new EmptyExpression( local.valueType.getValue() ) );
	}

	public static ExpressionStatement createIncompleteLocalAssignmentStatement( UserLocal local ) {
		return AstUtilities.createLocalAssignmentStatement( local, new EmptyExpression( local.valueType.getValue() ) );
	}

	public static AssignmentExpression createIncompleteLocalArrayAssignment( UserLocal local ) {
		return AstUtilities.createLocalArrayAssignment( local, new EmptyExpression( JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( local.valueType.getValue().getComponentType() ) );
	}

	public static ExpressionStatement createIncompleteLocalArrayAssignmentStatement( UserLocal local ) {
		return AstUtilities.createLocalArrayAssignmentStatement( local, new EmptyExpression( JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( local.valueType.getValue().getComponentType() ) );
	}

	public static AssignmentExpression createIncompleteParameterArrayAssignment( UserParameter parameter ) {
		return AstUtilities.createParameterArrayAssignment( parameter, new EmptyExpression( JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( parameter.valueType.getValue().getComponentType() ) );
	}

	public static ExpressionStatement createIncompleteParameterArrayAssignmentStatement( UserParameter parameter ) {
		return AstUtilities.createParameterArrayAssignmentStatement( parameter, new EmptyExpression( JavaType.INTEGER_OBJECT_TYPE ), new EmptyExpression( parameter.valueType.getValue().getComponentType() ) );
	}

	public static StringConcatenation createIncompleteStringConcatenation( Expression leftOperand ) {
		return AstUtilities.createStringConcatenation( leftOperand, new EmptyExpression( JavaType.OBJECT_TYPE ) );
	}

	public static StringConcatenation createIncompleteStringConcatenation() {
		return createIncompleteStringConcatenation( new EmptyExpression( JavaType.OBJECT_TYPE ) );
	}
}
