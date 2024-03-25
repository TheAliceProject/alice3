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

package org.alice.ide.controlflow.components;

import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.tooltips.JToolTip;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.draganddrop.statement.ExpressionStatementTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.StatementTemplateDragModel;
import org.alice.ide.templates.StatementTemplate;
import org.alice.ide.x.TemplateAstI18nFactory;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.Comment;
import org.lgna.project.ast.Statement;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
public class MiniControlFlowStatementTemplate extends StatementTemplate {
  private final Statement incompleteStatement;
  private SwingComponentView<?> incompleteStatementPane;
  private Label label;
  private javax.swing.JToolTip toolTip;

  public MiniControlFlowStatementTemplate(StatementTemplateDragModel dragModel) {
    super(dragModel, dragModel.getStatementCls());
    this.incompleteStatement = dragModel.getPossiblyIncompleteStatement();
  }

  private String labelText;

  private String getLabelText() {
    if (this.labelText == null) {
      Class<?> cls;
      DragModel model = this.getModel();
      if (model instanceof ExpressionStatementTemplateDragModel) {
        ExpressionStatementTemplateDragModel expressionStatementTemplateDragModel = (ExpressionStatementTemplateDragModel) model;
        cls = expressionStatementTemplateDragModel.getExpressionCls();
      } else {
        cls = this.getStatementCls();
      }
      this.labelText = ResourceBundleUtilities.getStringFromSimpleNames(cls, "org.alice.ide.controlflow.Templates");
    }
    return this.labelText;
  }

  private SwingComponentView<?> getIncompleteStatementPane() {
    if (this.incompleteStatementPane == null) {
      this.incompleteStatementPane = TemplateAstI18nFactory.getInstance().createStatementPane(this.incompleteStatement);
    }
    return this.incompleteStatementPane;
  }

  @Override
  public SwingComponentView<?> getSubject() {
    return this.getIncompleteStatementPane();
  }

  @Override
  protected Point getToolTipLocation(Point location, MouseEvent event) {
    javax.swing.JToolTip toolTip = this.createToolTip(null);
    if (toolTip != null) {
      int offset = toolTip.getPreferredSize().height;
      //      if( offset == 0 ) {
      //        edu.cmu.cs.dennisc.javax.swing.SwingUtilities.invalidateTree( toolTip );
      //        edu.cmu.cs.dennisc.javax.swing.SwingUtilities.doLayoutTree( toolTip );
      //        edu.cmu.cs.dennisc.javax.swing.SwingUtilities.setSizeToPreferredSizeTree( toolTip );
      //        offset = toolTip.getPreferredSize().height;
      //      }
      offset += 4;
      return new Point(0, -offset);
    } else {
      return location;
    }
  }

  @Override
  protected javax.swing.JToolTip createToolTip(javax.swing.JToolTip jToolTip) {
    if (this.toolTip == null) {
      this.toolTip = new JToolTip(this.getIncompleteStatementPane().getAwtComponent());
    }
    return this.toolTip;
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    if (this.label == null) {
      this.label = new Label(this.getLabelText());
      if (Comment.class.isAssignableFrom(this.getStatementCls())) {
        this.label.setForegroundColor(ThemeUtilities.getActiveTheme().getCommentForegroundColor());
      }
      //this.label.setFontToScaledFont( 1.2f );
      this.addComponent(this.label);
      this.setToolTipText("");
    }
  }

  @Override
  protected void handleUndisplayable() {
    //    this.removeAllComponents();
    super.handleUndisplayable();
  }
}
