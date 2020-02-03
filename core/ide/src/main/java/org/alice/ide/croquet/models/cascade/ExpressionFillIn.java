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

package org.alice.ide.croquet.models.cascade;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.javax.swing.LabelUtilities;
import edu.cmu.cs.dennisc.javax.swing.border.EmptyBorder;
import edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.ImmutableCascadeFillIn;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.Expression;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionFillIn<F extends Expression, B> extends ImmutableCascadeFillIn<F, B> {
  private String text;

  public ExpressionFillIn(UUID id, CascadeBlank<B>... blanks) {
    super(id, blanks);
  }

  @Override
  protected void localize() {
    super.localize();
    this.text = this.findDefaultLocalizedText();
  }

  protected Icon getLeadingIcon(ItemNode<? super F, B> step) {
    return null;
  }

  @Override
  protected JComponent createMenuItemIconProxy(ItemNode<? super F, B> step) {
    Expression expression = this.getTransientValue(step);

    Icon leadingIcon = this.getLeadingIcon(step);
    JLabel trailingLabel;
    if ((this.text != null) && (this.text.length() > 0)) {
      trailingLabel = LabelUtilities.createLabel(this.text, TextPosture.OBLIQUE, TextWeight.LIGHT);
    } else {
      trailingLabel = null;
    }
    JComponent expressionPane = PreviewAstI18nFactory.getInstance().createExpressionPane(expression).getAwtComponent();
    if ((leadingIcon != null) || (trailingLabel != null)) {
      JLineAxisPane rv = new JLineAxisPane();
      if (leadingIcon != null) {
        rv.add(new JLabel(leadingIcon));
      }
      rv.add(expressionPane);
      if (trailingLabel != null) {
        trailingLabel.setBorder(new EmptyBorder(0, 16, 0, 0));
        rv.add(trailingLabel);
      }
      return rv;
    } else {
      return expressionPane;
    }
  }
  //  @Override
  //  public final javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode< ? super F, B > step ) {
  //    return super.getMenuItemIcon( step );
  //  }
  //  @Override
  //  public final String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super F, B > step ) {
  //    return super.getMenuItemText( step );
  //  }
}
