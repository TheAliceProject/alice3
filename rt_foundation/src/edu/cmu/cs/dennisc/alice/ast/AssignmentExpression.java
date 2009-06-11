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
package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class AssignmentExpression extends Expression {
	public DeclarationProperty<AbstractType> expressionType = new DeclarationProperty<AbstractType>( this );
	public ExpressionProperty leftHandSide = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return AssignmentExpression.this.expressionType.getValue();
		}
	};
	public enum Operator {
		ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, TIMES_ASSIGN, DIVIDE_ASSIGN, BIT_AND_ASSIGN, BIT_OR_ASSIGN, BIT_XOR_ASSIGN, REMAINDER_ASSIGN, LEFT_SHIFT_ASSIGN, RIGHT_SHIFT_SIGNED_ASSIGN, RIGHT_SHIFT_UNSIGNED_ASSIGN;
	}
	public edu.cmu.cs.dennisc.property.InstanceProperty< Operator > operator = new edu.cmu.cs.dennisc.property.InstanceProperty< Operator >( this, null );
	//todo: new name
	public ExpressionProperty rightHandSide = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return AssignmentExpression.this.expressionType.getValue();
		}
	};
	public AssignmentExpression() {
	}
	public AssignmentExpression( AbstractType expressionType, Expression leftHandSide, Operator operator, Expression rightHandSide ) {
		this.expressionType.setValue( expressionType );
		this.leftHandSide.setValue( leftHandSide );
		this.operator.setValue( operator );
		this.rightHandSide.setValue( rightHandSide );
	}
	@Override
	public AbstractType getType() {
		return TypeDeclaredInJava.VOID_TYPE;
	}
}
