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

package org.alice.stageide.custom;

import org.alice.ide.croquet.models.cascade.literals.DoubleLiteralFillIn;
import org.alice.ide.custom.ExpressionWithRecentValuesCreatorComposite;
import org.alice.stageide.custom.components.VolumeLevelCustomExpressionCreatorView;
import org.lgna.croquet.BoundedIntegerState;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.StringValue;
import org.lgna.project.ast.DoubleLiteral;
import org.lgna.project.ast.Expression;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class VolumeLevelCustomExpressionCreatorComposite extends ExpressionWithRecentValuesCreatorComposite<VolumeLevelCustomExpressionCreatorView, Double> {
  private static class SingletonHolder {
    private static VolumeLevelCustomExpressionCreatorComposite instance = new VolumeLevelCustomExpressionCreatorComposite();
  }

  public static VolumeLevelCustomExpressionCreatorComposite getInstance() {
    return SingletonHolder.instance;
  }

  private final BoundedIntegerState valueState = this.createBoundedIntegerState("valueState", VolumeLevelUtilities.createDetails());
  private final StringValue silentLabel = this.createStringValue("silentLabel");
  private final StringValue normalLabel = this.createStringValue("normalLabel");
  private final StringValue louderLabel = this.createStringValue("louderLabel");

  private VolumeLevelCustomExpressionCreatorComposite() {
    super(UUID.fromString("1c80a46b-6ff8-4fbd-8003-5bbab71a3fca"));
  }

  @Override
  protected VolumeLevelCustomExpressionCreatorView createView() {
    return new VolumeLevelCustomExpressionCreatorView(this);
  }

  public BoundedIntegerState getValueState() {
    return this.valueState;
  }

  public StringValue getLouderLabel() {
    return this.louderLabel;
  }

  public StringValue getNormalLabel() {
    return this.normalLabel;
  }

  public StringValue getSilentLabel() {
    return this.silentLabel;
  }

  @Override
  protected Expression createValue() {
    double actualVolume = VolumeLevelUtilities.toDouble(this.valueState.getValue());
    return new DoubleLiteral(actualVolume);
  }

  @Override
  protected Status getStatusPreRejectorCheck() {
    return IS_GOOD_TO_GO_STATUS;
  }

  @Override
  protected void initializeToPreviousExpression(Expression expression) {
    double actualVolume;
    if (expression instanceof DoubleLiteral) {
      DoubleLiteral doubleLiteral = (DoubleLiteral) expression;
      actualVolume = doubleLiteral.value.getValue();
    } else {
      actualVolume = Double.NaN;
    }
    if (Double.isNaN(actualVolume)) {
      //pass
    } else {
      int value = VolumeLevelUtilities.toInt(actualVolume);
      this.valueState.setValueTransactionlessly(value);
    }
  }

  @Override
  protected Double getLastCustomValue() {
    return ((DoubleLiteral) this.getValue()).value.getValue();
  }

  @Override
  protected CascadeBlankChild getValueFillIn(Double value) {
    return DoubleLiteralFillIn.getInstance(value);
  }
}
