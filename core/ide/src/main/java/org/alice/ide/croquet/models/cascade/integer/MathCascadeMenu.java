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

package org.alice.ide.croquet.models.cascade.integer;

import org.alice.ide.IDE;
import org.alice.ide.croquet.models.cascade.ExpressionCascadeMenu;
import org.alice.ide.croquet.models.cascade.StaticMethodInvocationFillIn;
import org.alice.ide.croquet.models.cascade.arithmetic.ArithmeticUtilities;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.ArithmeticInfixExpression;
import org.lgna.project.ast.Expression;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class MathCascadeMenu extends ExpressionCascadeMenu<Expression> {
  private static class SingletonHolder {
    private static MathCascadeMenu instance = new MathCascadeMenu();
  }

  public static MathCascadeMenu getInstance() {
    return SingletonHolder.instance;
  }

  private MathCascadeMenu() {
    super(UUID.fromString("a7c69555-3232-4091-96f6-8f9b6ec2ee3a"));
  }

  @Override
  protected void updateBlankChildren(List<CascadeBlankChild> blankChildren, BlankNode<Expression> context) {
    Expression prevExpression = IDE.getActiveInstance().getExpressionCascadeManager().getPreviousExpression();
    if (prevExpression != null) {
      if (prevExpression.getType().isAssignableTo(Integer.class)) {
        for (ArithmeticInfixExpression.Operator operator : ArithmeticUtilities.PRIME_TIME_INTEGER_ARITHMETIC_OPERATORS) {
          blankChildren.add(IntegerArithmeticExpressionRightOperandOnlyFillIn.getInstance(operator));
        }
      }
    }
    for (ArithmeticInfixExpression.Operator operator : ArithmeticUtilities.PRIME_TIME_INTEGER_ARITHMETIC_OPERATORS) {
      blankChildren.add(IntegerArithmeticExpressionLeftAndRightOperandsFillIn.getInstance(operator));
    }
    blankChildren.add(IncompleteDivideRemainderCascadeMenu.getInstance());

    blankChildren.add(CascadeLineSeparator.getInstance());
    blankChildren.add(StaticMethodInvocationFillIn.getInstance(Math.class, "abs", Integer.TYPE));
    blankChildren.add(CascadeLineSeparator.getInstance());
    blankChildren.add(StaticMethodInvocationFillIn.getInstance(Math.class, "min", Integer.TYPE, Integer.TYPE));
    blankChildren.add(StaticMethodInvocationFillIn.getInstance(Math.class, "max", Integer.TYPE, Integer.TYPE));
  }
}
