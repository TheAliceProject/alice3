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
package org.alice.ide.codeeditor;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.IDE;
import org.alice.ide.ast.declaration.AddParameterComposite;
import org.alice.ide.croquet.components.AbstractListPropertyPane;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.x.AstI18nFactory;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Label;
import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserParameter;

import javax.swing.BoxLayout;

/**
 * @author Dennis Cosgrove
 */
public class ParametersPane extends AbstractListPropertyPane<NodeListProperty<UserParameter>, UserParameter> {
  public ParametersPane(AstI18nFactory factory, UserCode code) {
    super(factory, code.getRequiredParamtersProperty(), BoxLayout.LINE_AXIS);
  }

  protected IDE getIDE() {
    return IDE.getActiveInstance();
  }

  private UserCode getCode() {
    return (UserCode) getProperty().getOwner();
  }

  @Override
  protected AwtComponentView<?> createComponent(UserParameter parameter) {
    return new TypedParameterPane(getProperty(), parameter);
  }

  @Override
  protected void addPrefixComponents() {
    //super.addPrefixComponents();
    if (FormatterState.isJava()) {
      this.addComponent(new Label("( "));
    } else {
      int n = this.getProperty().size();
      String text;
      switch (n) {
      case 0:
        text = null;
        break;
      case 1:
        text = " " + localize("withParameter") + ": ";
        break;
      default:
        text = " " + localize("withParameters") + ": ";
      }
      if (text != null) {
        this.addComponent(new Label(text, TextPosture.OBLIQUE, TextWeight.LIGHT));
      }
    }
  }

  private String localize(String key) {
    return ResourceBundleUtilities.getStringForKey(key, "org.alice.ide.codeeditor.CodeEditor");
  }

  @Override
  protected AwtComponentView<?> createInterstitial(int i, int N) {
    if (i < (N - 1)) {
      return new Label(", ");
    } else {
      return BoxUtilities.createHorizontalSliver(4);
    }
  }

  @Override
  protected void addPostfixComponents() {
    super.addPostfixComponents();
    AstI18nFactory factory = this.getFactory();
    if (factory.isSignatureLocked(this.getCode())) {
      //pass
    } else {
      this.addComponent(AddParameterComposite.getInstance(this.getCode()).getLaunchOperation().createButton());
    }
    if (FormatterState.isJava()) {
      this.addComponent(new Label(" )"));
    }
  }
}
