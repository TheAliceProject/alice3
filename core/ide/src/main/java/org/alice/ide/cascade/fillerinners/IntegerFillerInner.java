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

import org.alice.ide.croquet.models.cascade.integer.MathCascadeMenu;
import org.alice.ide.croquet.models.cascade.integer.RandomCascadeMenu;
import org.alice.ide.croquet.models.cascade.integer.RealToIntegerCascadeMenu;
import org.alice.ide.croquet.models.cascade.literals.IntegerLiteralFillIn;
import org.alice.ide.custom.ExpressionWithRecentValuesCreatorComposite;
import org.alice.ide.custom.IntegerCustomExpressionCreatorComposite;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.project.annotations.IntegerValueDetails;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.Expression;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class IntegerFillerInner extends AbstractNumberFillerInner {
  public static int[] getLiterals(ValueDetails<?> details) {
    if (details instanceof IntegerValueDetails) {
      return ((IntegerValueDetails) details).getLiterals();
    } else {
      return new int[] {0, 1, 2, 3};
    }
  }

  public IntegerFillerInner() {
    super(Integer.class);
  }

  @Override
  public void appendItems(List<CascadeBlankChild> items, ValueDetails<?> details, boolean isTop, Expression prevExpression) {
    super.appendItems(items, details, isTop, prevExpression);

    List<Integer> literals = Arrays.asList(ArrayUtils.toObject(getLiterals(details)));

    for (int i : literals) {
      items.add(IntegerLiteralFillIn.getInstance(i));
    }

    ExpressionWithRecentValuesCreatorComposite creatorComposite = IntegerCustomExpressionCreatorComposite.getInstance();

    items.addAll(creatorComposite.getRecentFillIns(literals));

    if (isTop && (prevExpression != null)) {
      items.add(CascadeLineSeparator.getInstance());
      items.add(RandomCascadeMenu.getInstance());
      items.add(CascadeLineSeparator.getInstance());
      items.add(RealToIntegerCascadeMenu.getInstance());
      items.add(CascadeLineSeparator.getInstance());
      items.add(MathCascadeMenu.getInstance());
    }
    items.add(CascadeLineSeparator.getInstance());

    items.add(creatorComposite.getValueCreator().getFillIn());
  }
}
