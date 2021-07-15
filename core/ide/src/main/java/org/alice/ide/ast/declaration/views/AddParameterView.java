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

import org.alice.ide.IDE;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.declaration.AddParameterComposite;
import org.alice.ide.codeeditor.TypedParameterPane;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserParameter;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class AddParameterView extends DeclarationView<UserParameter> {
  private final Label label = new Label();
  private final BorderPanel warningPanel;

  public AddParameterView(AddParameterComposite composite) {
    super(composite);
    PageAxisPanel pane = new PageAxisPanel();
    pane.addComponent(this.label);
    pane.addComponent(BoxUtilities.createVerticalSliver(8));
    //TODO: Localize
    pane.addComponent(new LineAxisPanel(new Label("Tip: look for "), PreviewAstI18nFactory.getInstance().createExpressionPane(new NullLiteral())));
    pane.addComponent(BoxUtilities.createVerticalSliver(8));
    pane.addComponent(composite.getIsRequirementToUpdateInvocationsUnderstoodState().createCheckBox());

    Label warningLabel = new Label();
    warningLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
    this.warningPanel = new BorderPanel.Builder().hgap(32).lineStart(warningLabel).center(pane).build();

    this.warningPanel.setBorder(BorderFactory.createEmptyBorder(32, 8, 32, 8));
    this.setBackgroundColor(ThemeUtilities.getActiveTheme().getParameterColor());
  }

  @Override
  public SwingComponentView<?> createPreviewSubComponent() {
    AddParameterComposite composite = (AddParameterComposite) this.getComposite();
    UserParameter parameter = composite.getPreviewValue();
    return new TypedParameterPane(null, parameter);
  }

  @Override
  protected BorderPanel createMainComponent() {
    BorderPanel rv = super.createMainComponent();
    rv.addPageEndComponent(this.warningPanel);
    return rv;
  }

  @Override
  protected void handleDisplayable() {
    AddParameterComposite composite = (AddParameterComposite) this.getComposite();
    UserCode code = composite.getCode();
    List<SimpleArgumentListProperty> argumentLists = IDE.getActiveInstance().getArgumentLists(code);
    final int N = argumentLists.size();
    this.warningPanel.setVisible(N > 0);
    if (this.warningPanel.isVisible()) {
      String codeText;
      if (code instanceof AbstractMethod) {
        AbstractMethod method = (AbstractMethod) code;
        if (method.isProcedure()) {
          codeText = "procedure";
        } else {
          codeText = "function";
        }
      } else {
        codeText = "constructor";
      }
      // TODO I18n
      StringBuilder sb = new StringBuilder();
      sb.append("<html><body>There ");
      if (N == 1) {
        sb.append("is 1 invocation");
      } else {
        sb.append("are ");
        sb.append(N);
        sb.append(" invocations");
      }
      sb.append(" to this ");
      sb.append(codeText);
      sb.append(" in your program.<br>You will need to fill in ");
      if (N == 1) {
        sb.append("a value");
      } else {
        sb.append("values");
      }
      sb.append(" for the new ");
      if (N == 1) {
        sb.append("argument at the");
      } else {
        sb.append("arguments at each");
      }
      sb.append(" invocation.</body></html>");
      this.label.setText(sb.toString());
    }
    super.handleDisplayable();
  }
}
