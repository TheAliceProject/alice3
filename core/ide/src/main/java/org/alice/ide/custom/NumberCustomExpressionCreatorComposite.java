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

package org.alice.ide.custom;

import edu.cmu.cs.dennisc.math.GoldenRatio;
import org.alice.ide.croquet.models.numberpad.NumberModel;
import org.alice.ide.custom.components.NumberCustomExpressionCreatorView;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.Dialog;
import org.lgna.project.ast.Expression;

import java.awt.Dimension;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class NumberCustomExpressionCreatorComposite<T> extends ExpressionWithRecentValuesCreatorComposite<NumberCustomExpressionCreatorView, T> {
  private final ErrorStatus errorStatus = this.createErrorStatus("errorStatus");
  private final NumberModel numberModel;

  public NumberCustomExpressionCreatorComposite(UUID id, NumberModel numberModel) {
    super(id);
    this.numberModel = numberModel;
  }

  @Override
  protected Dimension calculateWindowSize(AbstractWindow<?> window) {
    Dimension rv = super.calculateWindowSize(window);
    //todo
    rv.width = (int) (rv.height / GoldenRatio.PHI);
    return rv;
  }

  @Override
  protected NumberCustomExpressionCreatorView createView() {
    return new NumberCustomExpressionCreatorView(this);
  }

  public NumberModel getNumberModel() {
    return this.numberModel;
  }

  @Override
  protected Expression createValue() {
    return this.numberModel.getExpressionValue();
  }

  @Override
  protected Status getStatusPreRejectorCheck() {
    String text = this.numberModel.getExplanationIfOkButtonShouldBeDisabled();
    if (text != null) {
      String errorFormat = findLocalizedText(text);
      this.errorStatus.setText(String.format(errorFormat, numberModel.getTextField().getText()));
      return errorStatus;
    } else {
      return IS_GOOD_TO_GO_STATUS;
    }
  }

  protected abstract String getTextForPreviousExpression(Expression expression);

  @Override
  protected final void initializeToPreviousExpression(Expression expression) {
    String text = this.getTextForPreviousExpression(expression);
    this.numberModel.setText(text);
    this.numberModel.selectAll();
  }

  @Override
  protected void handlePreShowDialog(Dialog dialog) {
    super.handlePreShowDialog(dialog);
    this.numberModel.getTextField().requestFocusInWindow();
  }
}
