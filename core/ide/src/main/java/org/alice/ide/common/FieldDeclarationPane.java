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
package org.alice.ide.common;

import edu.cmu.cs.dennisc.property.event.PropertyListener;
import org.alice.ide.croquet.components.ExpressionDropDown;
import org.alice.ide.croquet.models.ast.FieldInitializerState;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.croquet.models.ui.preferences.IsExposingReassignableStatusState;
import org.alice.ide.x.AstI18nFactory;
import org.alice.ide.x.DialogAstI18nFactory;
import org.alice.ide.x.components.ExpressionPropertyView;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.project.ast.UserField;

/**
 * @author Dennis Cosgrove
 */
public class FieldDeclarationPane extends LineAxisPanel {
  private final AstI18nFactory factory;
  private final UserField field;
  private final Label finalLabel = new Label();
  private final boolean isDropDownDesired;
  private final boolean isFinalDesiredIfAppropriate;

  public FieldDeclarationPane(AstI18nFactory factory, UserField field, boolean isDropDownDesired, boolean isFinalDesiredIfAppropriate) {
    this.factory = factory;
    this.field = field;
    this.isDropDownDesired = isDropDownDesired;
    this.isFinalDesiredIfAppropriate = isFinalDesiredIfAppropriate;
  }

  public FieldDeclarationPane(AstI18nFactory factory, UserField field, boolean isDropDownDesired) {
    this(factory, field, isDropDownDesired, true);
  }

  public FieldDeclarationPane(AstI18nFactory factory, UserField field) {
    this(factory, field, false);
  }

  @Override
  protected void internalRefresh() {
    super.internalRefresh();
    this.forgetAndRemoveAllComponents();
    if (isFinalDesiredIfAppropriate && IsExposingReassignableStatusState.getInstance().getValue()) {
      this.addComponent(finalLabel);
    }
    this.addComponent(TypeComponent.createInstance(field.getValueType()));
    this.addComponent(BoxUtilities.createHorizontalSliver(8));
    AwtComponentView<?> nameLabel = this.createNameLabel();
    nameLabel.scaleFont(1.5f);
    this.addComponent(nameLabel);
    this.addComponent(BoxUtilities.createHorizontalSliver(8));
    this.addComponent(new GetsPane(true));

    AwtComponentView<?> component;
    if (isDropDownDesired) {
      component = new ExpressionDropDown(FieldInitializerState.getInstance(this.field), DialogAstI18nFactory.getInstance());
    } else {
      component = new ExpressionPropertyView(factory, field.initializer);
    }
    this.addComponent(component);
  }

  protected AwtComponentView<?> createNameLabel() {
    return this.factory.createNameView(this.field);
  }

  private void updateFinalLabel() {
    String text;
    if (field.isFinal()) {
      text = FormatterState.getInstance().getValue().getFinalText() + " ";
    } else {
      text = "";
    }
    this.finalLabel.setText(text);
  }

  private PropertyListener propertyListener = e -> updateFinalLabel();

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    this.updateFinalLabel();
    this.field.finalVolatileOrNeither.addPropertyListener(this.propertyListener);
  }

  @Override
  protected void handleUndisplayable() {
    this.field.finalVolatileOrNeither.addPropertyListener(this.propertyListener);
    super.handleUndisplayable();
  }

  public UserField getField() {
    return this.field;
  }
}
