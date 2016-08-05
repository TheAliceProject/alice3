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
public final class ArithmeticInfixExpression extends InfixExpression<ArithmeticInfixExpression.Operator> {
	public static enum Operator implements CodeAppender {
		PLUS() {
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				assert leftOperand != null : this;
				assert rightOperand != null : this;
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( ( leftOperand instanceof Double ) || ( rightOperand instanceof Double ) ) {
					return leftOperand.doubleValue() + rightOperand.doubleValue();
				} else if( ( leftOperand instanceof Float ) || ( rightOperand instanceof Float ) ) {
					return leftOperand.floatValue() + rightOperand.floatValue();
				} else if( ( leftOperand instanceof Long ) || ( rightOperand instanceof Long ) ) {
					return leftOperand.longValue() + rightOperand.longValue();
				} else if( ( leftOperand instanceof Integer ) || ( rightOperand instanceof Integer ) ) {
					return leftOperand.intValue() + rightOperand.intValue();
				} else if( ( leftOperand instanceof Short ) || ( rightOperand instanceof Short ) ) {
					return leftOperand.shortValue() + rightOperand.shortValue();
				} else if( ( leftOperand instanceof Byte ) || ( rightOperand instanceof Byte ) ) {
					return leftOperand.byteValue() + rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '+' );
			}
		},
		MINUS() {
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				assert leftOperand != null : this;
				assert rightOperand != null : this;
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( ( leftOperand instanceof Double ) || ( rightOperand instanceof Double ) ) {
					return leftOperand.doubleValue() - rightOperand.doubleValue();
				} else if( ( leftOperand instanceof Float ) || ( rightOperand instanceof Float ) ) {
					return leftOperand.floatValue() - rightOperand.floatValue();
				} else if( ( leftOperand instanceof Long ) || ( rightOperand instanceof Long ) ) {
					return leftOperand.longValue() - rightOperand.longValue();
				} else if( ( leftOperand instanceof Integer ) || ( rightOperand instanceof Integer ) ) {
					return leftOperand.intValue() - rightOperand.intValue();
				} else if( ( leftOperand instanceof Short ) || ( rightOperand instanceof Short ) ) {
					return leftOperand.shortValue() - rightOperand.shortValue();
				} else if( ( leftOperand instanceof Byte ) || ( rightOperand instanceof Byte ) ) {
					return leftOperand.byteValue() - rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '-' );
			}
		},
		TIMES() {
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				assert leftOperand != null : this;
				assert rightOperand != null : this;
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( ( leftOperand instanceof Double ) || ( rightOperand instanceof Double ) ) {
					return leftOperand.doubleValue() * rightOperand.doubleValue();
				} else if( ( leftOperand instanceof Float ) || ( rightOperand instanceof Float ) ) {
					return leftOperand.floatValue() * rightOperand.floatValue();
				} else if( ( leftOperand instanceof Long ) || ( rightOperand instanceof Long ) ) {
					return leftOperand.longValue() * rightOperand.longValue();
				} else if( ( leftOperand instanceof Integer ) || ( rightOperand instanceof Integer ) ) {
					return leftOperand.intValue() * rightOperand.intValue();
				} else if( ( leftOperand instanceof Short ) || ( rightOperand instanceof Short ) ) {
					return leftOperand.shortValue() * rightOperand.shortValue();
				} else if( ( leftOperand instanceof Byte ) || ( rightOperand instanceof Byte ) ) {
					return leftOperand.byteValue() * rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '*' );
			}
		},
		REAL_DIVIDE() {
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				assert leftOperand != null : this;
				assert rightOperand != null : this;
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( ( leftOperand instanceof Double ) || ( rightOperand instanceof Double ) ) {
					return leftOperand.doubleValue() / rightOperand.doubleValue();
				} else if( ( leftOperand instanceof Float ) || ( rightOperand instanceof Float ) ) {
					return leftOperand.floatValue() / rightOperand.floatValue();
				} else if( ( leftOperand instanceof Long ) || ( rightOperand instanceof Long ) ) {
					return leftOperand.longValue() / (double)rightOperand.longValue();
				} else if( ( leftOperand instanceof Integer ) || ( rightOperand instanceof Integer ) ) {
					return leftOperand.intValue() / (double)rightOperand.intValue();
				} else if( ( leftOperand instanceof Short ) || ( rightOperand instanceof Short ) ) {
					return leftOperand.shortValue() / (double)rightOperand.shortValue();
				} else if( ( leftOperand instanceof Byte ) || ( rightOperand instanceof Byte ) ) {
					return leftOperand.byteValue() / (double)rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '/' );
			}
		},
		INTEGER_DIVIDE() {
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				assert leftOperand != null : this;
				assert rightOperand != null : this;
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( ( leftOperand instanceof Double ) || ( rightOperand instanceof Double ) ) {
					return ( (int)leftOperand.doubleValue() ) / (int)rightOperand.doubleValue();
				} else if( ( leftOperand instanceof Float ) || ( rightOperand instanceof Float ) ) {
					return ( (int)leftOperand.floatValue() ) / (int)rightOperand.floatValue();
				} else if( ( leftOperand instanceof Long ) || ( rightOperand instanceof Long ) ) {
					return leftOperand.longValue() / rightOperand.longValue();
				} else if( ( leftOperand instanceof Integer ) || ( rightOperand instanceof Integer ) ) {
					return leftOperand.intValue() / rightOperand.intValue();
				} else if( ( leftOperand instanceof Short ) || ( rightOperand instanceof Short ) ) {
					return leftOperand.shortValue() / rightOperand.shortValue();
				} else if( ( leftOperand instanceof Byte ) || ( rightOperand instanceof Byte ) ) {
					return leftOperand.byteValue() / rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '/' );
			}
		},
		REAL_REMAINDER() {
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				assert leftOperand != null : this;
				assert rightOperand != null : this;
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( ( leftOperand instanceof Double ) || ( rightOperand instanceof Double ) ) {
					return leftOperand.doubleValue() % rightOperand.doubleValue();
				} else if( ( leftOperand instanceof Float ) || ( rightOperand instanceof Float ) ) {
					return leftOperand.floatValue() % rightOperand.floatValue();
				} else if( ( leftOperand instanceof Long ) || ( rightOperand instanceof Long ) ) {
					return leftOperand.longValue() % (double)rightOperand.longValue();
				} else if( ( leftOperand instanceof Integer ) || ( rightOperand instanceof Integer ) ) {
					return leftOperand.intValue() % (double)rightOperand.intValue();
				} else if( ( leftOperand instanceof Short ) || ( rightOperand instanceof Short ) ) {
					return leftOperand.shortValue() % (double)rightOperand.shortValue();
				} else if( ( leftOperand instanceof Byte ) || ( rightOperand instanceof Byte ) ) {
					return leftOperand.byteValue() % (double)rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '%' );
			}
		},
		INTEGER_REMAINDER() {
			@Override
			public Number operate( Number leftOperand, Number rightOperand ) {
				assert leftOperand != null : this;
				assert rightOperand != null : this;
				//todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
				if( ( leftOperand instanceof Double ) || ( rightOperand instanceof Double ) ) {
					return ( (int)leftOperand.doubleValue() ) % (int)rightOperand.doubleValue();
				} else if( ( leftOperand instanceof Float ) || ( rightOperand instanceof Float ) ) {
					return ( (int)leftOperand.floatValue() ) % (int)rightOperand.floatValue();
				} else if( ( leftOperand instanceof Long ) || ( rightOperand instanceof Long ) ) {
					return leftOperand.longValue() % rightOperand.longValue();
				} else if( ( leftOperand instanceof Integer ) || ( rightOperand instanceof Integer ) ) {
					return leftOperand.intValue() % rightOperand.intValue();
				} else if( ( leftOperand instanceof Short ) || ( rightOperand instanceof Short ) ) {
					return leftOperand.shortValue() % rightOperand.shortValue();
				} else if( ( leftOperand instanceof Byte ) || ( rightOperand instanceof Byte ) ) {
					return leftOperand.byteValue() % rightOperand.byteValue();
				} else {
					throw new RuntimeException();
				}
			}

			@Override
			public void appendJava( JavaCodeGenerator generator ) {
				generator.appendChar( '%' );
			}
		};
		public abstract Number operate( Number leftOperand, Number rightOperand );

		@Override
		public abstract void appendJava( JavaCodeGenerator generator );
	}

	public ArithmeticInfixExpression() {
	}

	public ArithmeticInfixExpression( Expression leftOperand, Operator operator, Expression rightOperand, AbstractType<?, ?, ?> expressionType ) {
		super( leftOperand, operator, rightOperand );
		//todo
		assert JavaType.getInstance( Number.class ).isAssignableFrom( expressionType )
				||
				JavaType.getInstance( Double.TYPE ).isAssignableFrom( expressionType )
				||
				JavaType.getInstance( Integer.TYPE ).isAssignableFrom( expressionType ) : expressionType;
		this.expressionType.setValue( expressionType );
	}

	public ArithmeticInfixExpression( Expression leftOperand, Operator operator, Expression rightOperand, Class<?> expressionCls ) {
		this( leftOperand, operator, rightOperand, JavaType.getInstance( expressionCls ) );
	}

	@Override
	protected AbstractType<?, ?, ?> getLeftOperandType() {
		return this.expressionType.getValue();
	}

	@Override
	protected AbstractType<?, ?, ?> getRightOperandType() {
		return this.expressionType.getValue();
	}

	@Override
	public AbstractType<?, ?, ?> getType() {
		return this.expressionType.getValue();
	}

	@Override
	public boolean contentEquals( Node o, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( super.contentEquals( o, strictness, filter ) ) {
			ArithmeticInfixExpression other = (ArithmeticInfixExpression)o;
			return this.expressionType.valueContentEquals( other.expressionType, strictness, filter );
		}
		return false;
	}

	@Override
	public void appendJava( JavaCodeGenerator generator ) {
		generator.appendExpression( this.leftOperand.getValue() );
		this.operator.getValue().appendJava( generator );
		generator.appendExpression( this.rightOperand.getValue() );
	}

	public final DeclarationProperty<AbstractType<?, ?, ?>> expressionType = DeclarationProperty.createReferenceInstance( this );
}
