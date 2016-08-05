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
package org.lgna.project.ast;

import org.lgna.project.code.CodeAppender;

/**
 * @author Dennis Cosgrove
 */
public final class RelationalInfixExpression extends InfixExpression<RelationalInfixExpression.Operator> {
	private static boolean isNumberComparisonDesired( Object leftOperand, Object rightOperand ) {
		return ( leftOperand instanceof Number ) && ( rightOperand instanceof Number );
	}

	private static boolean isDoubleComparisonDesired( Object leftOperand, Object rightOperand ) {
		//		if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
		return ( leftOperand instanceof Double ) || ( rightOperand instanceof Double );
		//		} else {
		//			return false;
		//		}
	}

	private static boolean isFloatComparisonDesired( Object leftOperand, Object rightOperand ) {
		return ( leftOperand instanceof Float ) || ( rightOperand instanceof Float );
	}

	private static boolean isLongComparisonDesired( Object leftOperand, Object rightOperand ) {
		return ( leftOperand instanceof Long ) || ( rightOperand instanceof Long );
	}

	private static boolean isIntegerComparisonDesired( Object leftOperand, Object rightOperand ) {
		return ( leftOperand instanceof Integer ) || ( rightOperand instanceof Integer );
	}

	private static boolean isShortComparisonDesired( Object leftOperand, Object rightOperand ) {
		return ( leftOperand instanceof Short ) || ( rightOperand instanceof Short );
	}

	private static boolean isByteComparisonDesired( Object leftOperand, Object rightOperand ) {
		return ( leftOperand instanceof Byte ) || ( rightOperand instanceof Byte );
	}

	private static double doubleValue( Object o ) {
		assert o instanceof Number;
		return ( (Number)o ).doubleValue();
	}

	private static float floatValue( Object o ) {
		assert o instanceof Number;
		return ( (Number)o ).floatValue();
	}

	private static long longValue( Object o ) {
		assert o instanceof Number;
		return ( (Number)o ).longValue();
	}

	private static int intValue( Object o ) {
		assert o instanceof Number;
		return ( (Number)o ).intValue();
	}

	private static short shortValue( Object o ) {
		assert o instanceof Number;
		return ( (Number)o ).shortValue();
	}

	private static byte byteValue( Object o ) {
		assert o instanceof Number;
		return ( (Number)o ).byteValue();
	}

	public static enum Operator implements CodeAppender {
		LESS() {
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					assert leftOperand != null;
					assert rightOperand != null;
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

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '<' );
			}
		},
		LESS_EQUALS() {
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					assert leftOperand != null;
					assert rightOperand != null;
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

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendString( "<=" );
			}
		},
		GREATER() {
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					assert leftOperand != null;
					assert rightOperand != null;
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

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '>' );
			}
		},
		GREATER_EQUALS() {
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					assert leftOperand != null;
					assert rightOperand != null;
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

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendString( ">=" );
			}
		},
		EQUALS() {
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					assert leftOperand != null;
					assert rightOperand != null;
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
					return leftOperand == rightOperand;
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendString( "==" );
			}
		},
		NOT_EQUALS() {
			@Override
			public Boolean operate( Object leftOperand, Object rightOperand ) {
				//todo Character
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( isNumberComparisonDesired( leftOperand, rightOperand ) ) {
					assert leftOperand != null;
					assert rightOperand != null;
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
					return leftOperand != rightOperand;
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendString( "!=" );
			}
		};
		public abstract Boolean operate( Object leftOperand, Object rightOperand );

		@Override
		public abstract void appendJava( JavaCodeGenerator generator );
	}

	public RelationalInfixExpression() {
	}

	public RelationalInfixExpression( Expression leftOperand, Operator operator, Expression rightOperand, AbstractType<?, ?, ?> leftOperandType, AbstractType<?, ?, ?> rightOperandType ) {
		super( leftOperand, operator, rightOperand );
		this.leftOperandType.setValue( leftOperandType );
		this.rightOperandType.setValue( rightOperandType );
	}

	public RelationalInfixExpression( Expression leftOperand, Operator operator, Expression rightOperand, Class<?> leftOperandCls, Class<?> rightOperandCls ) {
		this( leftOperand, operator, rightOperand, JavaType.getInstance( leftOperandCls ), JavaType.getInstance( rightOperandCls ) );
	}

	@Override
	protected AbstractType<?, ?, ?> getLeftOperandType() {
		return this.leftOperandType.getValue();
	}

	@Override
	protected AbstractType<?, ?, ?> getRightOperandType() {
		return this.rightOperandType.getValue();
	}

	@Override
	public AbstractType<?, ?, ?> getType() {
		return JavaType.BOOLEAN_OBJECT_TYPE;
	}

	@Override
	protected void handleMissingProperty( String propertyName, Object value ) {
		assert propertyName.equals( "expressionType" );
		if( value == JavaType.DOUBLE_OBJECT_TYPE ) {
			value = JavaType.getInstance( Number.class );
		}
		this.leftOperandType.setValue( (AbstractType<?, ?, ?>)value );
		this.rightOperandType.setValue( (AbstractType<?, ?, ?>)value );
	}

	@Override
	public void appendJava( JavaCodeGenerator generator ) {
		generator.appendExpression( this.leftOperand.getValue() );
		this.operator.getValue().appendJava( generator );
		generator.appendExpression( this.rightOperand.getValue() );
	}

	public final DeclarationProperty<AbstractType<?, ?, ?>> leftOperandType = DeclarationProperty.createReferenceInstance( this );
	public final DeclarationProperty<AbstractType<?, ?, ?>> rightOperandType = DeclarationProperty.createReferenceInstance( this );
}
