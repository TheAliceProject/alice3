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
class ConditionalInfixExpressionOperatorFillIn extends InfixExpressionOperatorFillIn< edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression, edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator > {
	public ConditionalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator operator ) {
		super( new edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression(), Boolean.class, operator, Boolean.class );
	}
}
/**
 * @author Dennis Cosgrove
 */
class ConditionalInfixExpressionOperatorBlank extends cascade.Blank {
	@Override
	protected void addChildren() {
		this.addChild( new ConditionalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator.AND ) );
		this.addChild( new ConditionalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator.OR ) );
	}
}
/**
 * @author Dennis Cosgrove
 */
public class ConditionalExpressionFillIn extends InfixExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression > {
	public ConditionalExpressionFillIn() {
		super( "conditional { and, or }" );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getLeftOperandType() {
		return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE;
	}
	@Override
	protected cascade.Blank createOperatorBlank() {
		return new ConditionalInfixExpressionOperatorBlank();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getRightOperandType() {
		return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression createValue( edu.cmu.cs.dennisc.alice.ast.Expression left, Object operatorContainingExpression, edu.cmu.cs.dennisc.alice.ast.Expression right ) {
		edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression aie = (edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression)operatorContainingExpression;
		return new edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression( left, aie.operator.getValue(), right );
	}
}
