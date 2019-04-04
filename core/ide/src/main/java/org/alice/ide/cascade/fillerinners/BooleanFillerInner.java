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
package org.alice.ide.cascade.fillerinners;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.croquet.models.cascade.RelationalIntegerCascadeMenu;
import org.alice.ide.croquet.models.cascade.RelationalNumberCascadeMenu;
import org.alice.ide.croquet.models.cascade.RelationalObjectCascadeMenu;
import org.alice.ide.croquet.models.cascade.StaticMethodInvocationFillIn;
import org.alice.ide.croquet.models.cascade.conditional.ConditionalExpressionLeftAndRightOperandsFillIn;
import org.alice.ide.croquet.models.cascade.conditional.ConditionalExpressionRightOperandOnlyFillIn;
import org.alice.ide.croquet.models.cascade.conditional.ReduceToLeftOperandInPreviousConditionalExpressionFillIn;
import org.alice.ide.croquet.models.cascade.conditional.ReduceToRightOperandInPreviousConditionalExpressionFillIn;
import org.alice.ide.croquet.models.cascade.conditional.ReplaceOperatorInPreviousConditionalExpressionFillIn;
import org.alice.ide.croquet.models.cascade.literals.BooleanLiteralFillIn;
import org.alice.ide.croquet.models.cascade.logicalcomplement.LogicalComplementOfPreviousExpressionFillIn;
import org.alice.ide.croquet.models.cascade.logicalcomplement.LogicalComplementOperandFillIn;
import org.alice.ide.croquet.models.cascade.logicalcomplement.ReduceToInnerOperandInPreviousLogicalComplementFillIn;
import org.alice.ide.croquet.models.cascade.string.StringComparisonCascadeMenu;
import org.lgna.common.RandomUtilities;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ConditionalInfixExpression;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.LogicalComplement;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class BooleanFillerInner extends ExpressionFillerInner {
  private final List<AbstractType<?, ?, ?>> relationalTypes = Lists.newCopyOnWriteArrayList();

  public BooleanFillerInner() {
    super(JavaType.BOOLEAN_OBJECT_TYPE);
  }

  public void addRelationalType(AbstractType<?, ?, ?> type) {
    this.relationalTypes.add(type);
  }

  @Override
  public void appendItems(List<CascadeBlankChild> items, ValueDetails<?> details, boolean isTop, Expression prevExpression) {
    if (isTop && (prevExpression instanceof ConditionalInfixExpression)) {
      // previous conditional
      ConditionalInfixExpression conditionalInfixExpression = (ConditionalInfixExpression) prevExpression;
      for (ConditionalInfixExpression.Operator operator : ConditionalInfixExpression.Operator.values()) {
        if (operator == conditionalInfixExpression.operator.getValue()) {
          //pass
        } else {
          items.add(ReplaceOperatorInPreviousConditionalExpressionFillIn.getInstance(operator));
        }
      }
      items.add(CascadeLineSeparator.getInstance());
      items.add(ReduceToLeftOperandInPreviousConditionalExpressionFillIn.getInstance());
      items.add(ReduceToRightOperandInPreviousConditionalExpressionFillIn.getInstance());
      items.add(CascadeLineSeparator.getInstance());
    }

    if (isTop && (prevExpression instanceof LogicalComplement)) {
      // previous logical complement
      items.add(ReduceToInnerOperandInPreviousLogicalComplementFillIn.getInstance());
      items.add(CascadeLineSeparator.getInstance());
    }

    items.add(BooleanLiteralFillIn.getInstance(true));
    items.add(BooleanLiteralFillIn.getInstance(false));
    items.add(CascadeLineSeparator.getInstance());

    if (isTop && (prevExpression != null)) {
      items.add(StaticMethodInvocationFillIn.getInstance(RandomUtilities.class, "nextBoolean"));
      items.add(CascadeLineSeparator.getInstance());
    }

    if (isTop && (prevExpression != null)) {
      items.add(LogicalComplementOfPreviousExpressionFillIn.getInstance());
      items.add(LogicalComplementOperandFillIn.getInstance());
      items.add(CascadeLineSeparator.getInstance());
    }

    if (isTop && (prevExpression != null)) {
      for (ConditionalInfixExpression.Operator operator : ConditionalInfixExpression.Operator.values()) {
        items.add(ConditionalExpressionRightOperandOnlyFillIn.getInstance(operator));
      }
      for (ConditionalInfixExpression.Operator operator : ConditionalInfixExpression.Operator.values()) {
        items.add(ConditionalExpressionLeftAndRightOperandsFillIn.getInstance(operator));
      }
      items.add(CascadeLineSeparator.getInstance());
    }

    if (isTop && (prevExpression != null)) {
      items.add(RelationalNumberCascadeMenu.getInstance());
      items.add(RelationalIntegerCascadeMenu.getInstance());
      for (AbstractType<?, ?, ?> type : this.relationalTypes) {
        items.add(new RelationalObjectCascadeMenu(type));
      }
      items.add(StringComparisonCascadeMenu.getInstance());
    }
  }
}
