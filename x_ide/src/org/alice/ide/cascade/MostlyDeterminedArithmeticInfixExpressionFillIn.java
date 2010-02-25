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
public class MostlyDeterminedArithmeticInfixExpressionFillIn extends MostlyDeterminedInfixExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression, edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType;
	public MostlyDeterminedArithmeticInfixExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.Expression leftExpression, edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator, edu.cmu.cs.dennisc.alice.ast.AbstractType rightOperandType, edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType ) {
		super( leftExpression, operator, rightOperandType );
		this.expressionType = expressionType;
	}
	public MostlyDeterminedArithmeticInfixExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.Expression leftExpression, edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator, Class<?> rightOperandCls, Class<?> expressionCls ) {
		this( leftExpression, operator, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( rightOperandCls ), edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( expressionCls ) );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression createIncompleteExpression( edu.cmu.cs.dennisc.alice.ast.Expression leftOperand, edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator, edu.cmu.cs.dennisc.alice.ast.AbstractType rightOperandType ) {
		return org.alice.ide.ast.NodeUtilities.createIncompleteArithmeticInfixExpression( leftOperand, operator, rightOperandType, this.expressionType );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression leftOperand, edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator, edu.cmu.cs.dennisc.alice.ast.Expression rightOperand ) {
		return new edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression( leftOperand, operator, rightOperand, this.expressionType );
	}
}
