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

import org.lgna.project.code.SymbolicOperator;

/**
 * @author Dennis Cosgrove
 */
public final class ShiftInfixExpression extends InfixExpression<ShiftInfixExpression.Operator> {
  public static enum Operator implements SymbolicOperator {
    LEFT_SHIFT("<<") {
      @Override
      public Object operate(Object leftOperand, Object rightOperand) {
        //todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
        if ((leftOperand instanceof Long) || (rightOperand instanceof Long)) {
          return (Long) leftOperand << (Long) rightOperand;
        } else if ((leftOperand instanceof Integer) || (rightOperand instanceof Integer)) {
          return (Integer) leftOperand << (Integer) rightOperand;
        } else if ((leftOperand instanceof Short) || (rightOperand instanceof Short)) {
          return (Short) leftOperand << (Short) rightOperand;
        } else if ((leftOperand instanceof Byte) || (rightOperand instanceof Byte)) {
          return (Byte) leftOperand << (Byte) rightOperand;
        } else if ((leftOperand instanceof Character) || (rightOperand instanceof Character)) {
          return (Character) leftOperand << (Character) rightOperand;
        } else {
          throw new RuntimeException();
        }
      }
    }, RIGHT_SHIFT_SIGNED(">>") {
      @Override
      public Object operate(Object leftOperand, Object rightOperand) {
        //todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
        if ((leftOperand instanceof Long) || (rightOperand instanceof Long)) {
          return (Long) leftOperand >> (Long) rightOperand;
        } else if ((leftOperand instanceof Integer) || (rightOperand instanceof Integer)) {
          return (Integer) leftOperand >> (Integer) rightOperand;
        } else if ((leftOperand instanceof Short) || (rightOperand instanceof Short)) {
          return (Short) leftOperand >> (Short) rightOperand;
        } else if ((leftOperand instanceof Byte) || (rightOperand instanceof Byte)) {
          return (Byte) leftOperand >> (Byte) rightOperand;
        } else if ((leftOperand instanceof Character) || (rightOperand instanceof Character)) {
          return (Character) leftOperand >> (Character) rightOperand;
        } else {
          throw new RuntimeException();
        }
      }
    }, RIGHT_SHIFT_UNSIGNED(">>>") {
      @Override
      public Object operate(Object leftOperand, Object rightOperand) {
        //todo AtomicInteger, AtomicLong, BigDecimal, BigInteger ?
        if ((leftOperand instanceof Long) || (rightOperand instanceof Long)) {
          return (Long) leftOperand >>> (Long) rightOperand;
        } else if ((leftOperand instanceof Integer) || (rightOperand instanceof Integer)) {
          return (Integer) leftOperand >>> (Integer) rightOperand;
        } else if ((leftOperand instanceof Short) || (rightOperand instanceof Short)) {
          return (Short) leftOperand >>> (Short) rightOperand;
        } else if ((leftOperand instanceof Byte) || (rightOperand instanceof Byte)) {
          return (Byte) leftOperand >>> (Byte) rightOperand;
        } else if ((leftOperand instanceof Character) || (rightOperand instanceof Character)) {
          return (Character) leftOperand >>> (Character) rightOperand;
        } else {
          throw new RuntimeException();
        }
      }
    };

    Operator(String symbol) {
      this.symbol = symbol;
    }

    public abstract Object operate(Object leftOperand, Object rightOperand);

    @Override
    public String getSymbol() {
      return symbol;
    }

    @Override
    public int getLevelOfPrecedence() {
      return 10;
    }

    private final String symbol;
  }

  public ShiftInfixExpression() {
  }

  public ShiftInfixExpression(AbstractType<?, ?, ?> expressionType, Expression leftOperand, Operator operator, Expression rightOperand) {
    assert JavaType.getInstance(Long.class).isAssignableFrom(expressionType) || JavaType.getInstance(Integer.class).isAssignableFrom(expressionType) || JavaType.getInstance(Short.class).isAssignableFrom(expressionType) || JavaType.getInstance(Byte.class).isAssignableFrom(expressionType) || JavaType.getInstance(Character.class).isAssignableFrom(expressionType);
    this.expressionType.setValue(expressionType);
    this.leftOperand.setValue(leftOperand);
    this.operator.setValue(operator);
    this.rightOperand.setValue(rightOperand);
  }

  @Override
  public int getLevelOfPrecedence() {
    return operator.getValue().getLevelOfPrecedence();
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

  public final DeclarationProperty<AbstractType<?, ?, ?>> expressionType = DeclarationProperty.createReferenceInstance(this);
}
