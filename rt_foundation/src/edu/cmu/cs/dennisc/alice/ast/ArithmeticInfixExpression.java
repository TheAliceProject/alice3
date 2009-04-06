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
public class ArithmeticInfixExpression extends InfixExpression< ArithmeticInfixExpression.Operator > {
	public DeclarationProperty<AbstractType> expressionType = new DeclarationProperty<AbstractType>( this );
	public enum Operator {
		PLUS() { 
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Double || rightOperand instanceof Double ) {
					return leftOperand.doubleValue() + rightOperand.doubleValue();
				} else if( leftOperand instanceof Float || rightOperand instanceof Float ) {
					return leftOperand.floatValue() + rightOperand.floatValue();
				} else if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return leftOperand.longValue() + rightOperand.longValue();
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return leftOperand.intValue() + rightOperand.intValue();
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return leftOperand.shortValue() + rightOperand.shortValue();
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return leftOperand.byteValue() + rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}			
		},
		MINUS() { 
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Double || rightOperand instanceof Double ) {
					return leftOperand.doubleValue() - rightOperand.doubleValue();
				} else if( leftOperand instanceof Float || rightOperand instanceof Float ) {
					return leftOperand.floatValue() - rightOperand.floatValue();
				} else if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return leftOperand.longValue() - rightOperand.longValue();
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return leftOperand.intValue() - rightOperand.intValue();
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return leftOperand.shortValue() - rightOperand.shortValue();
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return leftOperand.byteValue() - rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}			
		},
		TIMES() { 
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Double || rightOperand instanceof Double ) {
					return leftOperand.doubleValue() * rightOperand.doubleValue();
				} else if( leftOperand instanceof Float || rightOperand instanceof Float ) {
					return leftOperand.floatValue() * rightOperand.floatValue();
				} else if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return leftOperand.longValue() * rightOperand.longValue();
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return leftOperand.intValue() * rightOperand.intValue();
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return leftOperand.shortValue() * rightOperand.shortValue();
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return leftOperand.byteValue() * rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}			
		},
		REAL_DIVIDE() { 
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Double || rightOperand instanceof Double ) {
					return leftOperand.doubleValue() / rightOperand.doubleValue();
				} else if( leftOperand instanceof Float || rightOperand instanceof Float ) {
					return leftOperand.floatValue() / rightOperand.floatValue();
				} else if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return leftOperand.longValue() / (double)rightOperand.longValue();
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return leftOperand.intValue() / (double)rightOperand.intValue();
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return leftOperand.shortValue() / (double)rightOperand.shortValue();
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return leftOperand.byteValue() / (double)rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}			
		},
		INTEGER_DIVIDE() { 
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Double || rightOperand instanceof Double ) {
					return ((int)leftOperand.doubleValue()) / (int)rightOperand.doubleValue();
				} else if( leftOperand instanceof Float || rightOperand instanceof Float ) {
					return ((int)leftOperand.floatValue()) / (int)rightOperand.floatValue();
				} else if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return leftOperand.longValue() / rightOperand.longValue();
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return leftOperand.intValue() / rightOperand.intValue();
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return leftOperand.shortValue() / rightOperand.shortValue();
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return leftOperand.byteValue() / rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}			
		},
		REAL_REMAINDER() { 
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Double || rightOperand instanceof Double ) {
					return leftOperand.doubleValue() % rightOperand.doubleValue();
				} else if( leftOperand instanceof Float || rightOperand instanceof Float ) {
					return leftOperand.floatValue() % rightOperand.floatValue();
				} else if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return leftOperand.longValue() % (double)rightOperand.longValue();
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return leftOperand.intValue() % (double)rightOperand.intValue();
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return leftOperand.shortValue() % (double)rightOperand.shortValue();
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return leftOperand.byteValue() % (double)rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}			
		},
		INTEGER_REMAINDER() { 
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( leftOperand instanceof Double || rightOperand instanceof Double ) {
					return ((int)leftOperand.doubleValue()) % (int)rightOperand.doubleValue();
				} else if( leftOperand instanceof Float || rightOperand instanceof Float ) {
					return ((int)leftOperand.floatValue()) % (int)rightOperand.floatValue();
				} else if( leftOperand instanceof Long || rightOperand instanceof Long ) {
					return leftOperand.longValue() % rightOperand.longValue();
				} else if( leftOperand instanceof Integer || rightOperand instanceof Integer ) {
					return leftOperand.intValue() % rightOperand.intValue();
				} else if( leftOperand instanceof Short || rightOperand instanceof Short ) {
					return leftOperand.shortValue() % rightOperand.shortValue();
				} else if( leftOperand instanceof Byte || rightOperand instanceof Byte ) {
					return leftOperand.byteValue() % rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}			
		};
		public abstract Number operate( Number leftOperand, Number rightOperand );
	}
	public ArithmeticInfixExpression() {
	}
	public ArithmeticInfixExpression( Expression leftOperand, Operator operator, Expression rightOperand, AbstractType expressionType ) {
		super( leftOperand, operator, rightOperand );
		//todo
		assert 
			TypeDeclaredInJava.get( Number.class ).isAssignableFrom( expressionType ) 
			|| 
			TypeDeclaredInJava.get( Double.TYPE ).isAssignableFrom( expressionType ) 
			|| 
			TypeDeclaredInJava.get( Integer.TYPE ).isAssignableFrom( expressionType );
		this.expressionType.setValue( expressionType );
	}
	public ArithmeticInfixExpression( Expression leftOperand, Operator operator, Expression rightOperand, Class<?> expressionCls ) {
		this( leftOperand, operator, rightOperand, TypeDeclaredInJava.get( expressionCls ) );
	}
	@Override
	protected AbstractType getLeftOperandType() {
		return this.expressionType.getValue();
	}
	@Override
	protected AbstractType getRightOperandType() {
		return this.expressionType.getValue();
	}
	@Override
	public AbstractType getType() {
		return this.expressionType.getValue();
	}
}
