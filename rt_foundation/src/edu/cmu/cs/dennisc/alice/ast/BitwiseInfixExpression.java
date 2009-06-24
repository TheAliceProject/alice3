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
public class BitwiseInfixExpression extends Expression {
	public enum Operator {
		AND() { 
			@Override
			public Object operate( Object leftOperand, Object rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return (Long)leftOperand & (Long)rightOperand;
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return (Integer)leftOperand & (Integer)rightOperand;
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return (Short)leftOperand & (Short)rightOperand;
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return (Byte)leftOperand & (Byte)rightOperand;
				} else if( leftOperand instanceof Character || rightOperand instanceof Character ) {
					return (Character)leftOperand & (Character)rightOperand;
				} else {
					throw new RuntimeException();
				}
			}			
		},
		OR() { 
			@Override
			public Object operate( Object leftOperand, Object rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return (Long)leftOperand | (Long)rightOperand;
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return (Integer)leftOperand | (Integer)rightOperand;
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return (Short)leftOperand | (Short)rightOperand;
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return (Byte)leftOperand | (Byte)rightOperand;
				} else if( leftOperand instanceof Character || rightOperand instanceof Character ) {
					return (Character)leftOperand | (Character)rightOperand;
				} else {
					throw new RuntimeException();
				}
			}			
		},
		XOR() { 
			@Override
			public Object operate( Object leftOperand, Object rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return (Long)leftOperand ^ (Long)rightOperand;
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return (Integer)leftOperand ^ (Integer)rightOperand;
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return (Short)leftOperand ^ (Short)rightOperand;
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return (Byte)leftOperand ^ (Byte)rightOperand;
				} else if( leftOperand instanceof Character || rightOperand instanceof Character ) {
					return (Character)leftOperand ^ (Character)rightOperand;
				} else {
					throw new RuntimeException();
				}
			}			
		};
		public abstract Object operate( Object leftOperand, Object rightOperand );
	}
	public DeclarationProperty<AbstractType> expressionType = new DeclarationProperty<AbstractType>( this );
	public ExpressionProperty leftOperand = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return BitwiseInfixExpression.this.expressionType.getValue();
		}
	};
	public edu.cmu.cs.dennisc.property.InstanceProperty< Operator > operator = new edu.cmu.cs.dennisc.property.InstanceProperty< Operator >( this, null );
	public ExpressionProperty rightOperand = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return BitwiseInfixExpression.this.expressionType.getValue();
		}
	};
	public BitwiseInfixExpression() {
	}
	public BitwiseInfixExpression( AbstractType expressionType, Expression leftOperand, Operator operator, Expression rightOperand ) {
		assert 
			TypeDeclaredInJava.get( Long.class ).isAssignableFrom( expressionType ) || 
			TypeDeclaredInJava.get( Integer.class ).isAssignableFrom( expressionType ) || 
			TypeDeclaredInJava.get( Short.class ).isAssignableFrom( expressionType ) || 
			TypeDeclaredInJava.get( Byte.class ).isAssignableFrom( expressionType ) || 
			TypeDeclaredInJava.get( Character.class ).isAssignableFrom( expressionType )
		;
		this.expressionType.setValue( expressionType );
		this.leftOperand.setValue( leftOperand );
		this.operator.setValue( operator );
		this.rightOperand.setValue( rightOperand );
	}
	@Override
	public AbstractType getType() {
		return this.expressionType.getValue();
	}
}
