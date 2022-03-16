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
package org.alice.ide.ast.components;

import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.property.StringProperty;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.AliceFormatter;
import org.alice.ide.formatter.Formatter;
import org.lgna.croquet.views.Label;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractMethodContainedByUserField;
import org.lgna.project.ast.Declaration;

import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationNameLabel extends Label {
  private final AbstractDeclaration declaration;

  private PropertyListener namePropertyAdapter = e -> DeclarationNameLabel.this.updateText();

  public DeclarationNameLabel(AbstractDeclaration declaration) {
    this.declaration = declaration;
    this.updateText();
    this.setForegroundColor(Color.BLACK);
  }

  public DeclarationNameLabel(AbstractDeclaration declaration, float fontScaleFactor) {
    this(declaration);
    this.scaleFont(fontScaleFactor);
  }

  private StringProperty getNamePropertyIfItExistsForListening() {
    Declaration declarationForNameProperty;
    if (this.declaration instanceof AbstractMethodContainedByUserField) {
      AbstractMethodContainedByUserField methodContainedByUserField = (AbstractMethodContainedByUserField) this.declaration;
      declarationForNameProperty = methodContainedByUserField.getField();
    } else {
      declarationForNameProperty = this.declaration;
    }
    return declarationForNameProperty.getNamePropertyIfItExists();
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    if (this.declaration != null) {
      StringProperty nameProperty = this.getNamePropertyIfItExistsForListening();
      if (nameProperty != null) {
        nameProperty.addPropertyListener(this.namePropertyAdapter);
        this.updateText();
      }
    }
  }

  @Override
  protected void handleUndisplayable() {
    if (this.declaration != null) {
      StringProperty nameProperty = this.getNamePropertyIfItExistsForListening();
      if (nameProperty != null) {
        nameProperty.removePropertyListener(this.namePropertyAdapter);
      }
    }
    super.handleUndisplayable();
  }

  protected AbstractDeclaration getDeclaration() {
    return this.declaration;
  }

  protected String getNameText() {
    Formatter formatter = FormatterState.getInstance().getValue();
    return formatter.getNameForDeclaration(this.declaration);
  }

  protected String getTextForNullName() {
    return FormatterState.getInstance().getValue().getTextForNull();
  }

  protected final String getTextForBlankName() {
    // Show "unset", localized for user
    return "<" + AliceFormatter.getInstance().getTextForNull() + ">";
  }

  private void updateText() {
    String text;
    if (this.declaration != null) {
      text = this.getNameText();
    } else {
      text = null;
    }
    if (text != null) {
      //pass
    } else {
      text = this.getTextForNullName();
    }
    if (text.length() > 0) {
      //pass
    } else {
      text = this.getTextForBlankName();
    }
    if (Objects.equals(this.getText(), text)) {
      //pass
    } else {
      this.setText(text);
      this.repaint();
    }
  }
}
