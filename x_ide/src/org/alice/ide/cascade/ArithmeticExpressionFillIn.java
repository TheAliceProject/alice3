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
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
class ArithmeticInfixExpressionOperatorFillIn extends InfixExpressionOperatorFillIn< edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression, edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator > {
	private static edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression createArithmeticInfixExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType ) {
		edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression rv = new edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression();
		rv.expressionType.setValue( expressionType );
		return rv;
	}
	public ArithmeticInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator, edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType, Class<?> operandCls ) {
		super( createArithmeticInfixExpression( expressionType ), operandCls, operator, operandCls );
	}
}

/**
 * @author Dennis Cosgrove
 */
class ArithmeticInfixExpressionOperatorBlank extends cascade.Blank {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType;
	private Class<?> operandCls;
	public ArithmeticInfixExpressionOperatorBlank( edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType, Class<?> operandCls ) {
		this.expressionType = expressionType;
		this.operandCls = operandCls;
	}
	@Override
	protected void addChildren() {
		this.addChild( new ArithmeticInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.PLUS, this.expressionType, this.operandCls ) );
		this.addChild( new ArithmeticInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.MINUS, this.expressionType, this.operandCls ) );
		this.addChild( new ArithmeticInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.TIMES, this.expressionType, this.operandCls ) );
		this.addChild( new ArithmeticInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.REAL_DIVIDE, this.expressionType, this.operandCls ) );
		this.addChild( new ArithmeticInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.INTEGER_DIVIDE, this.expressionType, this.operandCls ) );
		this.addChild( new ArithmeticInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.REAL_REMAINDER, this.expressionType, this.operandCls ) );
		this.addChild( new ArithmeticInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.INTEGER_REMAINDER, this.expressionType, this.operandCls ) );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ArithmeticExpressionFillIn extends InfixExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType;
	private Class<?> operandCls;
	public ArithmeticExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType, Class<?> operandCls ) {
		super( "arithmetic { +, -, *, /, remainder }" );
		this.expressionType = expressionType;
		this.operandCls = operandCls;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getLeftOperandType() {
		return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( this.operandCls );
	}
	@Override
	protected cascade.Blank createOperatorBlank() {
		return new ArithmeticInfixExpressionOperatorBlank( this.expressionType, this.operandCls );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getRightOperandType() {
		return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( this.operandCls );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression createValue( edu.cmu.cs.dennisc.alice.ast.Expression left, Object operatorContainingExpression, edu.cmu.cs.dennisc.alice.ast.Expression right ) {
		edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression aie = (edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression)operatorContainingExpression;
		return new edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression( left, aie.operator.getValue(), right, this.expressionType );
	}
}
