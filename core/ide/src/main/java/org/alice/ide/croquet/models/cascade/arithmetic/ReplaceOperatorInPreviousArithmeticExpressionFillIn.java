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

package org.alice.ide.croquet.models.cascade.arithmetic;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.croquet.models.cascade.PreviousExpressionBasedFillInWithoutBlanks;
import org.lgna.project.ast.ArithmeticInfixExpression;
import org.lgna.project.ast.Expression;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ReplaceOperatorInPreviousArithmeticExpressionFillIn extends PreviousExpressionBasedFillInWithoutBlanks<ArithmeticInfixExpression> {
  private static Map<ArithmeticInfixExpression.Operator, ReplaceOperatorInPreviousArithmeticExpressionFillIn> map = Maps.newHashMap();

  public static ReplaceOperatorInPreviousArithmeticExpressionFillIn getInstance(ArithmeticInfixExpression.Operator operator) {
    synchronized (map) {
      ReplaceOperatorInPreviousArithmeticExpressionFillIn rv = map.get(operator);
      if (rv == null) {
        rv = new ReplaceOperatorInPreviousArithmeticExpressionFillIn(operator);
        map.put(operator, rv);
      }
      return rv;
    }
  }

  private final ArithmeticInfixExpression.Operator operator;

  private ReplaceOperatorInPreviousArithmeticExpressionFillIn(ArithmeticInfixExpression.Operator operator) {
    super(UUID.fromString("7699fe5b-b1bc-4bc2-9632-eace7166bdb6"));
    this.operator = operator;
  }

  //  @Override
  //  protected boolean isInclusionDesired( org.lgna.croquet.steps.CascadeFillInStep< org.lgna.project.ast.ArithmeticInfixExpression, Void > context, org.lgna.project.ast.Expression previousExpression ) {
  //    return previousExpression instanceof org.lgna.project.ast.ArithmeticInfixExpression;
  //  }
  @Override
  protected ArithmeticInfixExpression createValue(Expression previousExpression) {
    assert previousExpression instanceof ArithmeticInfixExpression;
    ArithmeticInfixExpression previousArithmetic = (ArithmeticInfixExpression) previousExpression;
    return new ArithmeticInfixExpression(previousArithmetic.leftOperand.getValue(), this.operator, previousArithmetic.rightOperand.getValue(), previousArithmetic.expressionType.getValue());
  }
}
