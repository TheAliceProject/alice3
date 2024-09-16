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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeItem;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.project.annotations.NumberValueDetails;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.DoubleLiteral;
import org.lgna.project.ast.Expression;

import org.alice.ide.ApiConfigurationManager;
import org.alice.ide.IDE;
import org.alice.ide.croquet.models.cascade.literals.DoubleLiteralFillIn;
import org.alice.ide.croquet.models.cascade.number.IntegerToRealCascadeMenu;
import org.alice.ide.croquet.models.cascade.number.MathCascadeMenu;
import org.alice.ide.croquet.models.cascade.number.RandomCascadeMenu;
import org.alice.ide.custom.DoubleCustomExpressionCreatorComposite;

/**
 * @author Dennis Cosgrove
 */
public class DoubleFillerInner extends AbstractNumberFillerInner<Double> {
  public static double[] getLiterals(ValueDetails<?> details) {
    if (details instanceof NumberValueDetails) {
      return ((NumberValueDetails) details).getLiterals();
    } else {
      return new double[] {0, 0.25, 0.5, 1.0, 2.0, 10.0};
    }
  }

  public DoubleFillerInner() {
    super(Double.class);
  }

  protected Double getLastCustomValue() {
    DoubleLiteral literal = (DoubleLiteral) DoubleCustomExpressionCreatorComposite.getInstance().getNumberModel().getExpressionValue();

    return literal != null
            ? literal.value.getValue()
            : null;
  }

  @Override
  public void appendItems(List<CascadeBlankChild> items, ValueDetails<?> details, boolean isTop, Expression prevExpression) {
    super.appendItems(items, details, isTop, prevExpression);

    List<Double> literals = Arrays.asList(ArrayUtils.toObject(getLiterals(details)));

    for (double d : literals) {
      items.add(DoubleLiteralFillIn.getInstance(d));
    }

    updateRecentValues(literals);

    for (double d : recentValues) {
      items.add(DoubleLiteralFillIn.getInstance(d));
    }

    if (isTop && (prevExpression != null)) {
      items.add(CascadeLineSeparator.getInstance());
      items.add(RandomCascadeMenu.getInstance());
      items.add(CascadeLineSeparator.getInstance());
      items.add(IntegerToRealCascadeMenu.getInstance());
      items.add(CascadeLineSeparator.getInstance());
      items.add(MathCascadeMenu.getInstance());
    }
    items.add(CascadeLineSeparator.getInstance());
    IDE ide = IDE.getActiveInstance();
    ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
    CascadeItem<?, ?> item = apiConfigurationManager.getCustomFillInFor(details);
    if (item != null) {
      items.add(item);
    } else {
      items.add(DoubleCustomExpressionCreatorComposite.getInstance().getValueCreator().getFillIn());
    }
  }
}
