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
package org.alice.ide.clipboard.components;

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities;
import edu.cmu.cs.dennisc.javax.swing.tooltips.JToolTip;
import org.alice.ide.ast.draganddrop.statement.StatementDragModel;
import org.alice.ide.clipboard.Clipboard;
import org.alice.ide.clipboard.CopyToClipboardOperation;
import org.alice.ide.clipboard.CutToClipboardOperation;
import org.alice.ide.clipboard.DragReceptorState;
import org.alice.ide.clipboard.icons.ClipboardIcon;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.lgna.croquet.AbstractDropReceptor;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.FlowPanel;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.croquet.views.imp.JDragView;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.Statement;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * @author Dennis Cosgrove
 */
public class ClipboardDragComponent extends DragComponent<DragModel> {
  private class ClipboardDropReceptor extends AbstractDropReceptor {
    private DragReceptorState dragReceptorState = DragReceptorState.IDLE;

    @Override
    public boolean isPotentiallyAcceptingOf(DragModel dragModel) {
      return dragModel instanceof StatementDragModel;
    }

    private void setDragReceptorState(DragReceptorState dragReceptorState) {
      this.dragReceptorState = dragReceptorState;
      ClipboardDragComponent.this.repaint();
    }

    @Override
    public void dragStarted(DragStep step) {
      this.setDragReceptorState(DragReceptorState.STARTED);
    }

    @Override
    public void dragEntered(DragStep step) {
      this.setDragReceptorState(DragReceptorState.ENTERED);
      //      step.getDragSource().hideDragProxy();
    }

    @Override
    public DropSite dragUpdated(DragStep step) {
      return Clipboard.SINGLETON.getDropSite();
    }

    @Override
    protected Triggerable dragDroppedPostRejectorCheck(DragStep step) {
      DragModel dragModel = step.getModel();
      if (dragModel instanceof StatementDragModel) {
        StatementDragModel statementDragModel = (StatementDragModel) dragModel;
        Statement statement = statementDragModel.getStatement();
        boolean isCopy = InputEventUtilities.isQuoteControlUnquoteDown(step.getLatestMouseEvent());
        if (isCopy) {
          return CopyToClipboardOperation.getInstance(statement);
        } else {
          return CutToClipboardOperation.getInstance(statement);
        }
      } else {
        return null;
      }
    }

    @Override
    public void dragExited(DragStep step, boolean isDropRecipient) {
      //      step.getDragSource().showDragProxy();
      this.setDragReceptorState(DragReceptorState.STARTED);
    }

    @Override
    public void dragStopped(DragStep step) {
      this.setDragReceptorState(DragReceptorState.IDLE);
    }

    @Override
    public TrackableShape getTrackableShape(DropSite potentialDropSite) {
      return ClipboardDragComponent.this;
    }

    @Override
    public SwingComponentView<?> getViewController() {
      return ClipboardDragComponent.this;
    }
  }

  private final ClipboardDropReceptor dropReceptor = new ClipboardDropReceptor();
  private final FlowPanel subject = new FlowPanel();

  public ClipboardDragComponent(DragModel dragModel) {
    super(dragModel, true);
  }

  @Override
  protected boolean isClickAndClackAppropriate() {
    return true;
  }

  public DropReceptor getDropReceptor() {
    return this.dropReceptor;
  }

  @Override
  public DragModel getModel() {
    if (Clipboard.SINGLETON.isStackEmpty()) {
      return null;
    } else {
      return super.getModel();
    }
  }

  public void refresh() {
    this.subject.forgetAndRemoveAllComponents();
    if (Clipboard.SINGLETON.isStackEmpty()) {
      this.setToolTipText(null);
    } else {
      this.setToolTipText("");
      Node node = Clipboard.SINGLETON.peek();
      if (node instanceof Statement) {
        Statement statement = (Statement) node;
        subject.addComponent(PreviewAstI18nFactory.getInstance().createStatementPane(statement));
        subject.revalidateAndRepaint();
      }
    }
    this.repaint();
  }

  @Override
  public SwingComponentView<?> getSubject() {
    return this.subject;
  }

  private static final ClipboardIcon ICON = new ClipboardIcon();

  @Override
  protected JDragView createAwtComponent() {
    JDragView rv = new JDragView() {
      @Override
      public Dimension getPreferredSize() {
        return new Dimension((ICON.getOrigWidth() * 4) / 5, (ICON.getOrigHeight() * 4) / 5);
      }

      @Override
      public javax.swing.JToolTip createToolTip() {
        return new JToolTip(ClipboardDragComponent.this.subject.getAwtComponent());
      }

      @Override
      public void paint(Graphics g) {
        super.paint(g);
        synchronized (ICON) {
          ICON.setDimension(this.getSize());
          ICON.setFull(Clipboard.SINGLETON.isStackEmpty() == false);
          ICON.setDragReceptorState(dropReceptor.dragReceptorState);
          ICON.paintIcon(this, g, 0, 0);
        }
        if (Clipboard.SINGLETON.getStackSize() > 1) {
          Graphics2D g2 = (Graphics2D) g;
          Object prevTextAntialiasing = g2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
          g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
          GraphicsUtilities.drawCenteredText(g, Integer.toString(Clipboard.SINGLETON.getStackSize()), this.getSize());
          g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, prevTextAntialiasing == null ? RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT : prevTextAntialiasing);
        }
      }
    };
    rv.setOpaque(false);
    return rv;
  }

  @Override
  protected void fillBounds(Graphics2D g2, int x, int y, int width, int height) {
    g2.fillRect(x, y, width, height);
  }

  @Override
  protected void paintPrologue(Graphics2D g2, int x, int y, int width, int height) {
  }

  @Override
  protected void paintEpilogue(Graphics2D g2, int x, int y, int width, int height) {
  }
}
