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
public class ConditionalInfixExpression extends InfixExpression< ConditionalInfixExpression.Operator > {
	public enum Operator {
		AND() { 
			@Override
			public Boolean operate( Boolean leftOperand, Boolean rightOperand ) {
				return leftOperand && rightOperand;
			}			
		},
		OR() { 
			@Override
			public Boolean operate( Boolean leftOperand, Boolean rightOperand ) {
				return leftOperand || rightOperand;
			}			
		};
		public abstract Boolean operate( Boolean leftOperand, Boolean rightOperand );
	}
	public ConditionalInfixExpression() {
	}
	public ConditionalInfixExpression( Expression leftOperand, Operator operator, Expression rightOperand ) {
		super( leftOperand, operator, rightOperand );
	}
	@Override
	protected AbstractType getLeftOperandType() {
		return TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE;
	}
	@Override
	protected AbstractType getRightOperandType() {
		return TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getType() {
		return TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE;
	}
	@Override
	protected void handleMissingProperty( String propertyName, Object value ) {
		assert propertyName.equals( "expressionType" );
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( propertyName, value );
	}
	
}
