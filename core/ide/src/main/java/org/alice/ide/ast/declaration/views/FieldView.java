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
package org.alice.ide.ast.declaration.views;

import org.alice.ide.Theme;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.declaration.AddFieldComposite;
import org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite;
import org.alice.ide.common.FieldDeclarationPane;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.alice.stageide.croquet.models.gallerybrowser.preferences.IsPromptIncludingPreviewState;
import org.alice.stageide.icons.IconFactoryManager;
import org.lgna.croquet.icon.EmptyIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.UserField;

/**
 * @author Dennis Cosgrove
 */
public abstract class FieldView extends DeclarationView<UserField> {
  public FieldView(DeclarationLikeSubstanceComposite<UserField> composite) {
    super(composite);
  }

  private final Label typeIconView = new Label(EmptyIconFactory.getInstance().getIconToFit(Theme.LARGE_RECT_ICON_SIZE));

  public FieldView(AddFieldComposite composite) {
    super(composite);
    this.setBackgroundColor(ThemeUtilities.getActiveTheme().getFieldColor());
  }

  @Override
  public SwingComponentView<?> createPreviewSubComponent() {
    DeclarationLikeSubstanceComposite<UserField> composite = (DeclarationLikeSubstanceComposite<UserField>) this.getComposite();
    UserField field = composite.getPreviewValue();
    return new FieldDeclarationPane(PreviewAstI18nFactory.getInstance(), field);
  }

  protected SwingComponentView<?> getSideView() {
    return this.typeIconView;
  }

  @Override
  protected SwingComponentView<?> createPageStartComponent() {
    return new BorderPanel.Builder().lineStart(super.createPageStartComponent()).lineEnd(this.getSideView()).build();
  }

  @Override
  protected boolean isPreviewDesired() {
    return IsPromptIncludingPreviewState.getInstance().getValue();
  }

  //  @Override
  //  public void handleValueTypeChanged( org.lgna.project.ast.AbstractType<?, ?, ?> nextType ) {
  //    super.handleValueTypeChanged( nextType );
  //
  //    org.lgna.croquet.icon.IconFactory iconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForType( nextType );
  //    this.typeIconView.setIcon( iconFactory.getIconToFit(org.alice.ide.Theme.DEFAULT_LARGE_ICON_SIZE ) ) );
  //    this.typeIconView.revalidateAndRepaint();
  //  }

  @Override
  public void handleInitializerChanged(Expression expression) {
    super.handleInitializerChanged(expression);
    DeclarationLikeSubstanceComposite<UserField> composite = (DeclarationLikeSubstanceComposite<UserField>) this.getComposite();
    UserField field = composite.getPreviewValue();
    IconFactory iconFactory = IconFactoryManager.getIconFactoryForField(field);
    this.typeIconView.setIcon(iconFactory.getIconToFit(Theme.LARGE_RECT_ICON_SIZE));
    this.typeIconView.revalidateAndRepaint();
  }
}
