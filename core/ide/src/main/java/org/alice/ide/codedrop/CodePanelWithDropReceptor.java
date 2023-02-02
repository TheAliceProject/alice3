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

package org.alice.ide.codedrop;

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import org.alice.ide.IDE;
import org.alice.ide.ast.code.EnvelopStatementsOperation;
import org.alice.ide.ast.code.MoveStatementOperation;
import org.alice.ide.ast.code.ShiftDragStatementUtilities;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel;
import org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel;
import org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel;
import org.alice.ide.ast.draganddrop.statement.StatementDragModel;
import org.alice.ide.ast.draganddrop.statement.StatementTemplateDragModel;
import org.alice.ide.cascade.BlockStatementIndexPairContext;
import org.alice.ide.cascade.ExpressionCascadeContext;
import org.alice.ide.clipboard.Clipboard;
import org.alice.ide.clipboard.CopyFromClipboardOperation;
import org.alice.ide.clipboard.PasteFromClipboardOperation;
import org.alice.ide.code.InsertCopiedStatementOperation;
import org.alice.ide.codeeditor.StatementListBorder;
import org.alice.ide.codeeditor.StatementListPropertyPaneInfo;
import org.alice.ide.common.AbstractStatementPane;
import org.alice.ide.common.DefaultStatementPane;
import org.alice.ide.meta.DeclarationMeta;
import org.alice.ide.preferences.recursion.IsRecursionAllowedState;
import org.alice.ide.templates.StatementTemplate;
import org.alice.ide.x.components.StatementListPropertyView;
import org.lgna.croquet.AbstractDropReceptor;
import org.lgna.croquet.Composite;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.AwtContainerView;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.HierarchyUtilities;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.UserCode;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Dennis Cosgrove
 */
public abstract class CodePanelWithDropReceptor extends BorderPanel {
  protected class InternalDropReceptor extends AbstractDropReceptor {
    private ExpressionCascadeContext pushedContext;

    @Override
    public final boolean isPotentiallyAcceptingOf(DragModel dragModel) {
      IDE ide = IDE.getActiveInstance();
      if (DeclarationMeta.getDeclaration() == getCode()) {
        if (dragModel instanceof AbstractStatementDragModel) {
          return true;
        } else if (dragModel instanceof AbstractExpressionDragModel) {
          AbstractExpressionDragModel expressionDragModel = (AbstractExpressionDragModel) dragModel;
          return expressionDragModel.isPotentialStatementCreator();
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    public StatementListPropertyView currentUnder;

    private void setCurrentUnder(StatementListPropertyView nextUnder, Dimension dropSize) {
      if (this.currentUnder != nextUnder) {
        if (this.currentUnder != null) {
          this.currentUnder.setIsCurrentUnder(false);
        }
        this.currentUnder = nextUnder;
        if (this.currentUnder != null) {
          this.currentUnder.setIsCurrentUnder(true);
          this.currentUnder.setDropSize(dropSize);
        }
      }
    }

    @Override
    public final void dragStarted(DragStep step) {
    }

    @Override
    public final void dragEntered(DragStep step) {
      DragComponent source = step.getDragSource();
      statementListPropertyPaneInfos = createStatementListPropertyPaneInfos(step.getModel(), source);
      repaint();
    }

    public StatementListPropertyPaneInfo[] createStatementListPropertyPaneInfos(DragModel dragModel, AwtContainerView<?> source) {
      List<StatementListPropertyView> statementListPropertyPanes = HierarchyUtilities.findAllMatches(CodePanelWithDropReceptor.this, StatementListPropertyView.class);

      boolean isAddEvent;
      if (dragModel instanceof AbstractStatementDragModel) {
        AbstractStatementDragModel statementDragModel = (AbstractStatementDragModel) dragModel;
        isAddEvent = statementDragModel.isAddEventListenerLikeSubstance();
      } else {
        isAddEvent = false;
      }

      ListIterator<StatementListPropertyView> listIterator = statementListPropertyPanes.listIterator();
      while (listIterator.hasNext()) {
        StatementListPropertyView view = listIterator.next();
        if (view.isAcceptingOfAddEventListenerMethodInvocationStatements() == isAddEvent) {
          //pass
        } else {
          listIterator.remove();
        }
      }

      StatementListPropertyPaneInfo[] rv = new StatementListPropertyPaneInfo[statementListPropertyPanes.size()];
      int i = 0;
      for (StatementListPropertyView statementListPropertyPane : statementListPropertyPanes) {
        if ((source != null) && source.isAncestorOf(statementListPropertyPane)) {
          continue;
        }
        //edu.cmu.cs.dennisc.print.PrintUtilities.println( statementListPropertyPane );
        DefaultStatementPane statementAncestor = statementListPropertyPane.getFirstAncestorAssignableTo(DefaultStatementPane.class);
        Rectangle bounds;
        if (statementAncestor != null) {
          bounds = statementAncestor.convertRectangle(statementListPropertyPane.getDropBounds(statementAncestor), getAsSeenBy());
        } else {
          bounds = statementListPropertyPane.getParent().getBounds(getAsSeenBy());
        }
        bounds.x = 0;
        bounds.width = getAsSeenBy().getWidth() - bounds.x;
        rv[i] = new StatementListPropertyPaneInfo(statementListPropertyPane, bounds);

        i++;
      }
      return rv;

    }

    private StatementListPropertyView getStatementListPropertyPaneUnder(MouseEvent e, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos) {
      StatementListPropertyView rv = null;
      for (StatementListPropertyPaneInfo statementListPropertyPaneInfo : statementListPropertyPaneInfos) {
        if (statementListPropertyPaneInfo != null) {
          if (statementListPropertyPaneInfo.contains(e)) {
            StatementListPropertyView slpp = statementListPropertyPaneInfo.getStatementListPropertyPane();
            if (rv != null) {
              if (rv.getHeight() > slpp.getHeight()) {
                rv = slpp;
              } else {
                //pass
              }
            } else {
              rv = slpp;
            }
          }
        }
      }
      return rv;
    }

    @Override
    public final BlockStatementIndexPair dragUpdated(DragStep step) {
      ComponentOrientation componentOrientation = getComponentOrientation();
      DragComponent source = step.getDragSource();
      if (source != null) {
        MouseEvent eSource = step.getLatestMouseEvent();
        MouseEvent eAsSeenBy = source.convertMouseEvent(eSource, getAsSeenBy());
        StatementListPropertyView nextUnder = getStatementListPropertyPaneUnder(eAsSeenBy, statementListPropertyPaneInfos);
        this.setCurrentUnder(nextUnder, source.getDropProxySize());
        if (this.currentUnder != null) {
          boolean isDropProxyAlreadyUpdated = false;
          if (InputEventUtilities.isQuoteControlUnquoteDown(eSource)) {
            //pass
          } else {
            AwtComponentView<?> subject = source.getSubject();
            if (subject instanceof AbstractStatementPane) {
              AbstractStatementPane abstractStatementPane = (AbstractStatementPane) subject;
              if (source instanceof StatementTemplate) {
                //pass
              } else {
                Statement statement = abstractStatementPane.getStatement();
                StatementListProperty prevOwner = abstractStatementPane.getOwner();
                StatementListProperty nextOwner = this.currentUnder.getProperty();

                int prevIndex = prevOwner.indexOf(statement);
                int nextIndex = this.currentUnder.calculateIndex(source.convertPoint(eSource.getPoint(), this.currentUnder));
                int currentPotentialDropIndex = nextIndex;
                if (prevOwner == nextOwner) {
                  if ((prevIndex == nextIndex) || (prevIndex == (nextIndex - 1))) {
                    Point p = new Point(0, 0);
                    source.setDropProxyLocationAndShowIfNecessary(p, source, null, -1);
                    isDropProxyAlreadyUpdated = true;
                    currentPotentialDropIndex = -1;
                  }
                }
                this.currentUnder.setCurrentPotentialDropIndexAndDragStep(currentPotentialDropIndex, step);
              }
            }
          }
          if (isDropProxyAlreadyUpdated) {
            //pass
          } else {
            MouseEvent eUnder = getAsSeenBy().convertMouseEvent(eAsSeenBy, this.currentUnder);
            Integer height = 0;
            Insets insets = this.currentUnder.getBorder().getBorderInsets(this.currentUnder.getAwtComponent());
            Point p = new Point(0, 0);

            int availableHeight = this.currentUnder.getAvailableDropProxyHeight();

            StatementListBorder statementListBorder = this.currentUnder.getStatementListBorder();
            int N = this.currentUnder.getComponentCount();
            if (N == 0) {
              p.y = insets.top;
              height = null;
            } else {
              int index = this.currentUnder.calculateIndex(eUnder.getPoint());
              this.currentUnder.setCurrentPotentialDropIndexAndDragStep(index, step);
              final boolean IS_SQUISHING_DESIRED = false;
              if (index == 0) {
                p.y = 0;
                if (IS_SQUISHING_DESIRED) {
                  height = null;
                }
              } else if ((index == statementListBorder.getMinimum()) && (N == 1)) {
                p.y = this.currentUnder.getHeight() - insets.bottom;
                height = null;
              } else if (index < N) {
                p.y = this.currentUnder.getAwtComponent().getComponent(index).getY();
              } else {
                Component lastComponent = this.currentUnder.getAwtComponent().getComponent(N - 1);
                p.y = lastComponent.getY() + lastComponent.getHeight();
                if (IS_SQUISHING_DESIRED) {
                  p.y -= availableHeight;
                  height = null;
                } else {
                  p.y += StatementListPropertyView.INTRASTICIAL_PAD;
                  if (this.currentUnder.getProperty() == ((UserCode) getCode()).getBodyProperty().getValue().statements) {
                    height = null;
                  }
                }
              }
            }
            if (componentOrientation.isLeftToRight()) {
              p.x = insets.left;
            } else {
              p.x = this.currentUnder.getWidth() - insets.right - step.getDragSource().getDropProxy().getWidth();

            }
            source.setDropProxyLocationAndShowIfNecessary(p, this.currentUnder, height, availableHeight);
          }
        } else {
          //          source.hideDropProxyIfNecessary();
        }
      }
      repaint();

      if (this.currentUnder != null) {
        BlockStatement blockStatement = (BlockStatement) this.currentUnder.getProperty().getOwner();
        MouseEvent eSource = step.getLatestMouseEvent();
        MouseEvent eAsSeenBy = source.convertMouseEvent(eSource, getAsSeenBy());
        MouseEvent eUnder = getAsSeenBy().convertMouseEvent(eAsSeenBy, this.currentUnder);
        int index = this.currentUnder.calculateIndex(eUnder.getPoint());
        BlockStatementIndexPair blockStatementIndexPair = new BlockStatementIndexPair(blockStatement, index);
        //edu.cmu.cs.dennisc.print.PrintUtilities.println( "blockStatementIndexPair", blockStatementIndexPair );
        return blockStatementIndexPair;
      } else {
        return null;
      }
    }

    @Override
    protected Triggerable dragDroppedPostRejectorCheck(DragStep step) {
      final DragModel dragModel = step.getModel();
      final MouseEvent eSource = step.getLatestMouseEvent();
      final StatementListPropertyView statementListPropertyPane = this.currentUnder;
      if (statementListPropertyPane == null) {
        return null;
      }
      DropSite potentialDropSite = step.getCurrentPotentialDropSite();
      if (!(potentialDropSite instanceof BlockStatementIndexPair)) {
        return null;
      }

      BlockStatementIndexPair dropSite = (BlockStatementIndexPair) potentialDropSite;
      if (dragModel instanceof StatementTemplateDragModel) {
        if (isRecursive(dragModel)) {
          return null;
        }
        this.pushedContext = new BlockStatementIndexPairContext(dropSite);
        IDE.getActiveInstance().getExpressionCascadeManager().pushContext(this.pushedContext);
        return dragModel.getDropOperation(step, dropSite);
      }
      if (dragModel == Clipboard.SINGLETON.getDragModel()) {
        return InputEventUtilities.isQuoteControlUnquoteDown(eSource)
            ? CopyFromClipboardOperation.getInstance(dropSite)
            : PasteFromClipboardOperation.getInstance(dropSite);
      }
      if (dragModel instanceof StatementDragModel) {
        return statementDropped((StatementDragModel) dragModel, eSource, dropSite);
      }
      if (dragModel instanceof AbstractExpressionDragModel) {
        return dragModel.getDropOperation(step, dropSite);
      }
      return null;
    }

    private boolean isRecursive(DragModel dragModel) {
      if (IsRecursionAllowedState.getInstance().getValue()
          || !(dragModel instanceof ProcedureInvocationTemplateDragModel)) {
        return false;
      }
      ProcedureInvocationTemplateDragModel procedureInvocationTemplateDragModel = (ProcedureInvocationTemplateDragModel) dragModel;
      AbstractMethod method = procedureInvocationTemplateDragModel.getMethod();
      if (method == getCode()) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        // TODO I18n
        sb.append("The code you have just dropped would create a <strong><em>recursive</em></strong> method call.<p><p>Recursion is disabled by default because otherwise many users unwittingly and mistakenly make recursive calls.");
        final boolean IS_POINTING_USER_TO_RECURSION_PREFERENCE_DESIRED = true;
        if (IS_POINTING_USER_TO_RECURSION_PREFERENCE_DESIRED) {
          sb.append("<p><p>For more information on recursion see the Window -> Preferences menu.");
        }
        sb.append("</html>");
        Dialogs.showInfo("Recursion is disabled.", sb.toString());
        return true;
      }
      return false;
    }

    private Triggerable statementDropped(StatementDragModel dragModel, MouseEvent eSource, BlockStatementIndexPair toLocation) {
      final Statement statement = dragModel.getStatement();
      Node parent = statement.getParent();
      if (!(parent instanceof BlockStatement)) {
        return null;
      }
      BlockStatement blockStatement = (BlockStatement) parent;
      final StatementListProperty prevOwner = blockStatement.statements;
      final StatementListProperty nextOwner = this.currentUnder.getProperty();
      final int prevIndex = prevOwner.indexOf(statement);
      final int index = toLocation.getIndex();

      BlockStatement prevBlockStatement = (BlockStatement) prevOwner.getOwner();
      BlockStatement nextBlockStatement = (BlockStatement) nextOwner.getOwner();
      if (InputEventUtilities.isQuoteControlUnquoteDown(eSource)) {
        IDE ide = IDE.getActiveInstance();
        Statement copy = ide.createCopy(statement);
        return new InsertCopiedStatementOperation(nextBlockStatement, index, copy);
      }

      if ((prevOwner == nextOwner) && ((prevIndex == index) || (prevIndex == (index - 1)))) {
        return null;
      }
      boolean isMultiple = eSource.isShiftDown();
      BlockStatementIndexPair fromLocation = new BlockStatementIndexPair(prevBlockStatement, prevIndex);
      if (isMultiple && ShiftDragStatementUtilities.isCandidateForEnvelop(dragModel)) {
        return EnvelopStatementsOperation.getInstance(fromLocation, toLocation);
      }
      int count = isMultiple ? ShiftDragStatementUtilities.calculateShiftMoveCount(fromLocation, toLocation) : 1;
      if (count > 0) {
        return new MoveStatementOperation(fromLocation, statement, toLocation, isMultiple);
      }
      return null;
    }

    @Override
    public final void dragExited(DragStep step, boolean isDropRecipient) {
      statementListPropertyPaneInfos = null;
      //todo: listen to step
      this.setCurrentUnder(null, null);
      repaint();
    }

    @Override
    public final void dragStopped(DragStep step) {
      if (this.pushedContext != null) {
        IDE.getActiveInstance().getExpressionCascadeManager().popAndCheckContext(this.pushedContext);
        this.pushedContext = null;
      }
    }

    @Override
    public TrackableShape getTrackableShape(DropSite potentialDropSite) {
      return CodePanelWithDropReceptor.this.getTrackableShape(potentialDropSite);
    }

    @Override
    public SwingComponentView<?> getViewController() {
      return CodePanelWithDropReceptor.this;
    }
  }

  public CodePanelWithDropReceptor(Composite<?> composite) {
    super(composite);
  }

  public CodePanelWithDropReceptor() {
    this(null);
  }

  public InternalDropReceptor getDropReceptor() {
    return this.dropReceptor;
  }

  @Override
  protected final JPanel createJPanel() {
    final boolean IS_FEEDBACK_DESIRED = false;
    JPanel rv;
    if (IS_FEEDBACK_DESIRED) {
      rv = new JPanel() {
        @Override
        public void paint(Graphics g) {
          super.paint(g);
          if (CodePanelWithDropReceptor.this.statementListPropertyPaneInfos != null) {
            Graphics2D g2 = (Graphics2D) g;
            int i = 0;
            for (StatementListPropertyPaneInfo statementListPropertyPaneInfo : CodePanelWithDropReceptor.this.statementListPropertyPaneInfos) {
              if (statementListPropertyPaneInfo != null) {
                Color color;
                if (CodePanelWithDropReceptor.this.dropReceptor.currentUnder == statementListPropertyPaneInfo.getStatementListPropertyPane()) {
                  color = new Color(0, 0, 0, 127);
                } else {
                  color = null;
                  //color = new java.awt.Color( 255, 0, 0, 31 );
                }
                Rectangle bounds = statementListPropertyPaneInfo.getBounds();
                bounds = SwingUtilities.convertRectangle(CodePanelWithDropReceptor.this.getAsSeenBy().getAwtComponent(), bounds, this);
                if (color != null) {
                  g2.setColor(color);
                  g2.fill(bounds);
                  g2.setColor(new Color(255, 255, 0, 255));
                  g2.draw(bounds);
                }
                g2.setColor(Color.BLACK);
                GraphicsUtilities.drawCenteredText(g2, Integer.toString(i), bounds.x, bounds.y, 32, bounds.height);
              }
              i++;
            }
          }
        }
      };
    } else {
      //todo: super.createJPanel() ?
      rv = new JPanel();
    }
    return rv;
  }

  public abstract void setJavaCodeOnTheSide(boolean value, boolean isFirstTime);

  protected abstract AwtComponentView<?> getAsSeenBy();

  public abstract AbstractCode getCode();

  public abstract TrackableShape getTrackableShape(DropSite potentialDropSite);

  private StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;
  private final InternalDropReceptor dropReceptor = new InternalDropReceptor();
}
