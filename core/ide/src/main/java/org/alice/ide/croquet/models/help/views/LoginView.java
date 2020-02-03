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
package org.alice.ide.croquet.models.help.views;

import java.util.List;

import org.alice.ide.croquet.models.help.AbstractLoginComposite;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.FormPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LabeledFormRow;
import org.lgna.croquet.views.PasswordField;
import org.lgna.croquet.views.TextField;

/**
 * @author Matt May
 */
public class LoginView extends FormPanel {
  private final ValueListener<Boolean> isPasswordExposedListener = new ValueListener<Boolean>() {
    @Override
    public void valueChanged(ValueEvent<Boolean> e) {
      passwordField.setExposed(e.getNextValue());
    }
  };
  private final TextField userNameField;
  private final PasswordField passwordField;

  public LoginView(AbstractLoginComposite bugLoginComposite) {
    super(bugLoginComposite);
    this.userNameField = bugLoginComposite.getUserNameState().createTextField();
    this.passwordField = bugLoginComposite.getPasswordState().createPasswordField();
    this.setMinimumPreferredHeight(240);
  }

  @Override
  protected void appendRows(List<LabeledFormRow> rows) {
    AbstractLoginComposite loginComposite = (AbstractLoginComposite) this.getComposite();
    rows.add(new LabeledFormRow(loginComposite.getUserNameState().getSidekickLabel(), this.userNameField));
    rows.add(new LabeledFormRow(loginComposite.getPasswordState().getSidekickLabel(), this.passwordField));
    rows.add(new LabeledFormRow(null, loginComposite.getDisplayPasswordValue().createCheckBox()));
    rows.add(new LabeledFormRow(null, new Label()));
    rows.add(new LabeledFormRow(null, loginComposite.getIsRememberingState().createCheckBox()));
  }

  @Override
  protected void handleDisplayable() {
    AbstractLoginComposite bugLoginComposite = (AbstractLoginComposite) this.getComposite();
    bugLoginComposite.getDisplayPasswordValue().addAndInvokeNewSchoolValueListener(this.isPasswordExposedListener);
    this.userNameField.requestFocus();
    super.handleDisplayable();
  }

  @Override
  protected void handleUndisplayable() {
    super.handleUndisplayable();
    AbstractLoginComposite loginComposite = (AbstractLoginComposite) this.getComposite();
    loginComposite.getDisplayPasswordValue().removeNewSchoolValueListener(this.isPasswordExposedListener);
  }
}
