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
public class RelationalInfixExpression extends Expression {
	private static boolean isNumberComparisonDesired( Object leftOperand, Object rightOperand ) {
		return leftOperand instanceof Number && rightOperand instanceof Number;
	}
	private static boolean isDoubleComparisonDesired( Object leftOperand, Object rightOperand ) {
//		if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
			return leftOperand instanceof Double || rightOperand instanceof Double;
//		} else {
//			return false;
//		}
	}
	private static boolean isFloatComparisonDesired( Object leftOperand, Object rightOperand ) {
		return leftOperand instanceof Float || rightOperand instanceof Float;
	}
	private static boolean isLongComparisonDesired( Object leftOperand, Object rightOperand ) {
		return leftOperand instanceof Long || rightOperand instanceof Long;
	}
	private static boolean isIntegerComparisonDesired( Object leftOperand, Object rightOperand ) {
		return leftOperand instanceof Integer || rightOperand instanceof Integer;
	}
	private static boolean isShortComparisonDesired( Object leftOperand, Object rightOperand ) {
		return leftOperand instanceof Short || rightOperand instanceof Short;
	}
	private static boolean isByteComparisonDesired( Object leftOperand, Object rightOperand ) {
		return leftOperand instanceof Byte || rightOperand instanceof Byte;
	}
	private static double doubleValue( Object o ) {
		assert o instanceof Number;
		return ((Number)o).doubleValue();
	}
	private static float floatValue( Object o ) {
		assert o instanceof Number;
		return ((Number)o).floatValue();
	}
	private static long longValue( Object o ) {
		assert o instanceof Number;
		return ((Number)o).longValue();
	}
	private static int intValue( Object o ) {
		assert o instanceof Number;
		return ((Number)o).intValue();
	}
	private static short shortValue( Object o ) {
		assert o instanceof Number;
		return ((Number)o).shortValue();
	}
	private static byte byteValue( Object o ) {
		assert o instanceof Number;
		return ((Number)o).byteValue();
	}
	public enum Operator {
		LESS() { 
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					if( isDoubleComparisonDesired( leftOperand, rightOperand ) ) {
						return doubleValue( leftOperand ) < doubleValue( rightOperand );
					} else if( isFloatComparisonDesired( leftOperand, rightOperand ) ) {
						return floatValue( leftOperand ) < floatValue( rightOperand );
					} else if( isLongComparisonDesired( leftOperand, rightOperand ) ) {
						return longValue( leftOperand ) < longValue( rightOperand );
					} else if( isIntegerComparisonDesired( leftOperand, rightOperand ) ) {
						return intValue( leftOperand ) < intValue( rightOperand );
					} else if( isShortComparisonDesired( leftOperand, rightOperand ) ) {
						return shortValue( leftOperand ) < shortValue( rightOperand );
					} else if( isByteComparisonDesired( leftOperand, rightOperand ) ) {
						return byteValue( leftOperand ) < byteValue( rightOperand );
					} else {
						throw new RuntimeException();
					}
				} else {
					throw new RuntimeException();
				}
			}			
		},
		LESS_EQUALS() { 
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					if( isDoubleComparisonDesired( leftOperand, rightOperand ) ) {
						return doubleValue( leftOperand ) <= doubleValue( rightOperand );
					} else if( isFloatComparisonDesired( leftOperand, rightOperand ) ) {
						return floatValue( leftOperand ) <= floatValue( rightOperand );
					} else if( isLongComparisonDesired( leftOperand, rightOperand ) ) {
						return longValue( leftOperand ) <= longValue( rightOperand );
					} else if( isIntegerComparisonDesired( leftOperand, rightOperand ) ) {
						return intValue( leftOperand ) <= intValue( rightOperand );
					} else if( isShortComparisonDesired( leftOperand, rightOperand ) ) {
						return shortValue( leftOperand ) <= shortValue( rightOperand );
					} else if( isByteComparisonDesired( leftOperand, rightOperand ) ) {
						return byteValue( leftOperand ) <= byteValue( rightOperand );
					} else {
						throw new RuntimeException();
					}
				} else {
					throw new RuntimeException();
				}
			}			
		},
		GREATER() { 
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					if( isDoubleComparisonDesired( leftOperand, rightOperand ) ) {
						return doubleValue( leftOperand ) > doubleValue( rightOperand );
					} else if( isFloatComparisonDesired( leftOperand, rightOperand ) ) {
						return floatValue( leftOperand ) > floatValue( rightOperand );
					} else if( isLongComparisonDesired( leftOperand, rightOperand ) ) {
						return longValue( leftOperand ) > longValue( rightOperand );
					} else if( isIntegerComparisonDesired( leftOperand, rightOperand ) ) {
						return intValue( leftOperand ) > intValue( rightOperand );
					} else if( isShortComparisonDesired( leftOperand, rightOperand ) ) {
						return shortValue( leftOperand ) > shortValue( rightOperand );
					} else if( isByteComparisonDesired( leftOperand, rightOperand ) ) {
						return byteValue( leftOperand ) > byteValue( rightOperand );
					} else {
						throw new RuntimeException();
					}
				} else {
					throw new RuntimeException();
				}
			}			
		},
		GREATER_EQUALS() { 
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					if( isDoubleComparisonDesired( leftOperand, rightOperand ) ) {
						return doubleValue( leftOperand ) >= doubleValue( rightOperand );
					} else if( isFloatComparisonDesired( leftOperand, rightOperand ) ) {
						return floatValue( leftOperand ) >= floatValue( rightOperand );
					} else if( isLongComparisonDesired( leftOperand, rightOperand ) ) {
						return longValue( leftOperand ) >= longValue( rightOperand );
					} else if( isIntegerComparisonDesired( leftOperand, rightOperand ) ) {
						return intValue( leftOperand ) >= intValue( rightOperand );
					} else if( isShortComparisonDesired( leftOperand, rightOperand ) ) {
						return shortValue( leftOperand ) >= shortValue( rightOperand );
					} else if( isByteComparisonDesired( leftOperand, rightOperand ) ) {
						return byteValue( leftOperand ) >= byteValue( rightOperand );
					} else {
						throw new RuntimeException();
					}
				} else {
					throw new RuntimeException();
				}
			}			
		},
		EQUALS() { 
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					if( isDoubleComparisonDesired( leftOperand, rightOperand ) ) {
						return doubleValue( leftOperand ) == doubleValue( rightOperand );
					} else if( isFloatComparisonDesired( leftOperand, rightOperand ) ) {
						return floatValue( leftOperand ) == floatValue( rightOperand );
					} else if( isLongComparisonDesired( leftOperand, rightOperand ) ) {
						return longValue( leftOperand ) == longValue( rightOperand );
					} else if( isIntegerComparisonDesired( leftOperand, rightOperand ) ) {
						return intValue( leftOperand ) == intValue( rightOperand );
					} else if( isShortComparisonDesired( leftOperand, rightOperand ) ) {
						return shortValue( leftOperand ) == shortValue( rightOperand );
					} else if( isByteComparisonDesired( leftOperand, rightOperand ) ) {
						return byteValue( leftOperand ) == byteValue( rightOperand );
					} else {
						throw new RuntimeException();
					}
				} else {
					throw new RuntimeException();
				}
			}			
		},
		NOT_EQUALS() { 
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					if( isDoubleComparisonDesired( leftOperand, rightOperand ) ) {
						return doubleValue( leftOperand ) != doubleValue( rightOperand );
					} else if( isFloatComparisonDesired( leftOperand, rightOperand ) ) {
						return floatValue( leftOperand ) != floatValue( rightOperand );
					} else if( isLongComparisonDesired( leftOperand, rightOperand ) ) {
						return longValue( leftOperand ) != longValue( rightOperand );
					} else if( isIntegerComparisonDesired( leftOperand, rightOperand ) ) {
						return intValue( leftOperand ) != intValue( rightOperand );
					} else if( isShortComparisonDesired( leftOperand, rightOperand ) ) {
						return shortValue( leftOperand ) != shortValue( rightOperand );
					} else if( isByteComparisonDesired( leftOperand, rightOperand ) ) {
						return byteValue( leftOperand ) != byteValue( rightOperand );
					} else {
						throw new RuntimeException();
					}
				} else {
					throw new RuntimeException();
				}
			}
		};
		public abstract Boolean operate( Object leftOperand, Object rightOperand );
		
	}
	public DeclarationProperty<AbstractType> expressionType = new DeclarationProperty<AbstractType>( this );
	public ExpressionProperty leftOperand = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return RelationalInfixExpression.this.expressionType.getValue();
		}
	};
	public edu.cmu.cs.dennisc.property.InstanceProperty< Operator > operator = new edu.cmu.cs.dennisc.property.InstanceProperty< Operator >( this, null );
	public ExpressionProperty rightOperand = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return RelationalInfixExpression.this.expressionType.getValue();
		}
	};
	public RelationalInfixExpression() {
	}
	public RelationalInfixExpression( AbstractType expressionType, Expression leftOperand, Operator operator, Expression rightOperand ) {
		if( operator == Operator.EQUALS || operator == Operator.NOT_EQUALS ) {
			//pass
		} else {
			assert TypeDeclaredInJava.get( Number.class ).isAssignableFrom( expressionType ) || TypeDeclaredInJava.get( Character.class ).isAssignableFrom( expressionType ) ;
		}
		this.expressionType.setValue( expressionType );
		this.leftOperand.setValue( leftOperand );
		this.operator.setValue( operator );
		this.rightOperand.setValue( rightOperand );
	}
	@Override
	public AbstractType getType() {
		return TypeDeclaredInJava.get( Boolean.class );
	}
}
