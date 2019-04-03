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
package org.alice.ide.member.views;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.ast.draganddrop.statement.AssignmentTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.CommentTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.ConditionalStatementTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.CountLoopTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.DeclareLocalDragModel;
import org.alice.ide.ast.draganddrop.statement.DoInOrderTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.DoTogetherTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.EachInArrayTogetherTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.ForEachInArrayLoopTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.ReturnStatementTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.StatementTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.WhileLoopTemplateDragModel;
import org.alice.ide.controlflow.components.CompleteControlFlowStatementTemplate;
import org.alice.ide.controlflow.components.MiniControlFlowStatementTemplate;
import org.alice.ide.declarationseditor.CodeComposite;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.member.ControlFlowTabComposite;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.AbstractLabel;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.MigPanel;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractMethod;

import javax.swing.SwingUtilities;
import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class ControlFlowTabView extends MigPanel {
  private static final boolean IS_MINI_DESIRED = true;
  private static final int GAP_TOP = IS_MINI_DESIRED ? 8 : 16;

  private final AbstractLabel returnHeader;
  private final DragComponent<?> returnDragComponent;

  public ControlFlowTabView(ControlFlowTabComposite composite) {
    super(composite, "insets 4, gap 0");
    this.addHeader(composite.getDoInOrderHeader());
    this.addDragComponent(DoInOrderTemplateDragModel.getInstance());
    this.addHeader(composite.getDoTogetherHeader());
    this.addDragComponent(DoTogetherTemplateDragModel.getInstance());
    this.addHeader(composite.getLoopHeader());
    this.addDragComponent(CountLoopTemplateDragModel.getInstance());
    this.addDragComponent(WhileLoopTemplateDragModel.getInstance());
    this.addDragComponent(ForEachInArrayLoopTemplateDragModel.getInstance());
    this.addHeader(composite.getIfThenHeader());
    this.addDragComponent(ConditionalStatementTemplateDragModel.getInstance());
    this.addHeader(composite.getEachInTogetherHeader());
    this.addDragComponent(EachInArrayTogetherTemplateDragModel.getInstance());
    this.addHeader(composite.getCommentHeader());
    this.addDragComponent(CommentTemplateDragModel.getInstance());
    this.addHeader(composite.getLocalHeader());
    this.addDragComponent(DeclareLocalDragModel.getInstance());
    this.addDragComponent(AssignmentTemplateDragModel.getInstance());
    this.returnHeader = this.createHeader(composite.getReturnHeader());
    this.returnDragComponent = this.createDragComponent(ReturnStatementTemplateDragModel.getInstance(null));
    this.setBackgroundColor(new Color(0xd29669));
  }

  private AbstractLabel createHeader(PlainStringValue stringValue) {
    return stringValue.createLabel(TextPosture.OBLIQUE);
  }

  private void addHeader(AbstractLabel label) {
    StringBuilder sb = new StringBuilder();
    sb.append("wrap");
    if (this.getComponentCount() > 0) {
      sb.append(", gaptop ");
      sb.append(GAP_TOP);
    }
    this.addComponent(label, sb.toString());
  }

  private void addHeader(PlainStringValue stringValue) {
    this.addHeader(this.createHeader(stringValue));
  }

  private DragComponent<?> createDragComponent(StatementTemplateDragModel dragModel) {
    DragComponent<?> dragComponent;
    if (IS_MINI_DESIRED) {
      dragComponent = new MiniControlFlowStatementTemplate(dragModel);
    } else {
      dragComponent = new CompleteControlFlowStatementTemplate(dragModel);
    }
    return dragComponent;
  }

  private void addDragComponent(DragComponent<?> dragComponent) {
    this.addComponent(dragComponent, "wrap, gapleft 8");
  }

  private void addDragComponent(StatementTemplateDragModel dragModel) {
    this.addDragComponent(this.createDragComponent(dragModel));
  }

  private final ValueListener<DeclarationComposite<?, ?>> declarationTabListener = new ValueListener<DeclarationComposite<?, ?>>() {
    @Override
    public void valueChanged(ValueEvent<DeclarationComposite<?, ?>> e) {
      updateReturn(e.getNextValue());
    }
  };

  private void updateReturn(DeclarationComposite declarationComposite) {
    final boolean isReturnDesired;
    if (declarationComposite instanceof CodeComposite) {
      CodeComposite codeComposite = (CodeComposite) declarationComposite;
      AbstractCode code = codeComposite.getDeclaration();
      if (code instanceof AbstractMethod) {
        AbstractMethod method = (AbstractMethod) code;
        isReturnDesired = method.isFunction();
      } else {
        isReturnDesired = false;
      }
    } else {
      isReturnDesired = false;
    }
    boolean isReturnShowing = this.returnHeader.isShowing();
    if (isReturnDesired != isReturnShowing) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          synchronized (getTreeLock()) {
            if (isReturnDesired) {
              addHeader(returnHeader);
              addDragComponent(returnDragComponent);
            } else {
              removeComponent(returnDragComponent);
              removeComponent(returnHeader);
            }
          }
          revalidateAndRepaint();
        }
      });
    }
    Logger.outln(declarationComposite);
  }

  @Override
  public void handleCompositePreActivation() {
    super.handleCompositePreActivation();
    IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().addAndInvokeNewSchoolValueListener(this.declarationTabListener);
  }

  @Override
  public void handleCompositePostDeactivation() {
    IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().removeNewSchoolValueListener(this.declarationTabListener);
    super.handleCompositePostDeactivation();
  }
}
