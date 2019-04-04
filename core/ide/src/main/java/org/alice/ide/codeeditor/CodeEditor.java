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

import edu.cmu.cs.dennisc.java.awt.PrintHelper;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.codedrop.CodePanelWithDropReceptor;
import org.alice.ide.common.BodyPane;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState;
import org.alice.ide.formatter.Formatter;
import org.alice.ide.x.AbstractProjectEditorAstI18nFactory;
import org.alice.ide.x.ProjectEditorAstI18nFactory;
import org.alice.ide.x.components.StatementListPropertyView;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.AwtContainerView;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.croquet.views.Panel;
import org.lgna.croquet.views.ScreenElement;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.ConstructorBlockStatement;
import org.lgna.project.ast.ConstructorInvocationStatement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.UserCode;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.print.Printable;

/**
 * @author Dennis Cosgrove
 */
public class CodeEditor extends CodePanelWithDropReceptor {
  public CodeEditor(AbstractProjectEditorAstI18nFactory factory, AbstractCode code) {
    this.code = code;
    assert this.code instanceof UserCode : this.code;

    UserCode userCode = (UserCode) this.code;

    BlockStatement body = userCode.getBodyProperty().getValue();

    this.rootStatementListPropertyPane = new StatementListPropertyView(factory, body.statements, 32);

    SwingComponentView<?> statementListComponent = null;
    if (body instanceof ConstructorBlockStatement) {
      ConstructorBlockStatement constructorBlockStatement = (ConstructorBlockStatement) body;
      ConstructorInvocationStatement constructorInvocationStatement = constructorBlockStatement.constructorInvocationStatement.getValue();
      if (constructorInvocationStatement != null) {
        SwingComponentView<?> superComponent = ProjectEditorAstI18nFactory.getInstance().createStatementPane(constructorInvocationStatement);
        statementListComponent = new PageAxisPanel(new LineAxisPanel(BoxUtilities.createHorizontalSliver(8), superComponent), this.rootStatementListPropertyPane);
      }
    }

    if (statementListComponent != null) {
      //pass
    } else {
      statementListComponent = this.rootStatementListPropertyPane;
    }

    this.bodyPane = new BodyPane(statementListComponent);

    this.scrollPane.getAwtComponent().getViewport().setOpaque(false);
    this.scrollPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);

    this.header = (Panel) factory.createCodeHeader((UserCode) this.code);
    this.addPageStartComponent(this.header);

    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    Color color = ThemeUtilities.getActiveTheme().getCodeColor(this.code);
    this.setBackgroundColor(color);
  }

  @Override
  public TrackableShape getTrackableShape(DropSite potentialDropSite) {
    if (potentialDropSite instanceof BlockStatementIndexPair) {
      BlockStatementIndexPair blockStatementIndexPair = (BlockStatementIndexPair) potentialDropSite;
      StatementListProperty statementListProperty = blockStatementIndexPair.getBlockStatement().statements;
      int index = Math.max(0, blockStatementIndexPair.getIndex());
      return this.getTrackableShapeAtIndexOf(statementListProperty, index, false);
    } else {
      return null;
    }
  }

  @Override
  public AbstractCode getCode() {
    return this.code;
  }

  private final ValueListener<Boolean> typeFeedbackListener = new ValueListener<Boolean>() {
    @Override
    public void valueChanged(ValueEvent<Boolean> e) {
      CodeEditor.this.rootStatementListPropertyPane.refreshLater();
    }
  };
  private final ValueListener<Formatter> formatterListener = new ValueListener<Formatter>() {
    @Override
    public void valueChanged(ValueEvent<Formatter> e) {
      CodeEditor.this.header.refreshLater();
      CodeEditor.this.rootStatementListPropertyPane.refreshLater();
    }
  };

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    FormatterState.getInstance().addNewSchoolValueListener(this.formatterListener);
    IsIncludingTypeFeedbackForExpressionsState.getInstance().addAndInvokeNewSchoolValueListener(this.typeFeedbackListener);
  }

  @Override
  protected void handleUndisplayable() {
    IsIncludingTypeFeedbackForExpressionsState.getInstance().removeNewSchoolValueListener(this.typeFeedbackListener);
    FormatterState.getInstance().removeNewSchoolValueListener(this.formatterListener);
    super.handleUndisplayable();
  }

  private static int convertY(AwtComponentView<?> from, int y, AwtComponentView<?> to) {
    Point pt = from.convertPoint(new Point(0, y), to);
    return pt.y;
  }

  private static int capMinimum(int yPotentialMinimumBound, int y, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos, int index) {
    int rv = yPotentialMinimumBound;
    final int N = statementListPropertyPaneInfos.length;
    for (int i = 0; i < N; i++) {
      if (i == index) {
        //pass
      } else {
        Rectangle boundsI = statementListPropertyPaneInfos[i].getBounds();
        int yI = boundsI.y + boundsI.height;
        if (yI < y) {
          rv = Math.max(rv, yI);
        }
      }
    }
    return rv;
  }

  private static int capMaximum(int yMaximum, int yPlusHeight, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos, int index) {
    int rv = yMaximum;
    final int N = statementListPropertyPaneInfos.length;
    for (int i = 0; i < N; i++) {
      if (i == index) {
        //pass
      } else {
        Rectangle boundsI = statementListPropertyPaneInfos[i].getBounds();
        int yI = boundsI.y;
        if (yI > yPlusHeight) {
          rv = Math.min(rv, yI);
        }
      }
    }
    return rv;
  }

  private static boolean isWarningAlreadyPrinted = false;

  public class StatementListIndexTrackableShape implements TrackableShape {
    private StatementListProperty statementListProperty;
    private int index;
    private StatementListPropertyView statementListPropertyPane;
    private Rectangle boundsAtIndex;

    private StatementListIndexTrackableShape(StatementListProperty statementListProperty, int index, StatementListPropertyView statementListPropertyPane, Rectangle boundsAtIndex) {
      this.statementListProperty = statementListProperty;
      this.index = index;
      this.statementListPropertyPane = statementListPropertyPane;
      this.boundsAtIndex = boundsAtIndex;
    }

    private StatementListProperty getStatementListProperty() {
      return this.statementListProperty;
    }

    public BlockStatement getBlockStatement() {
      return (BlockStatement) this.statementListProperty.getOwner();
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public Shape getShape(ScreenElement asSeenBy, Insets insets) {
      AwtComponentView<?> src = CodeEditor.this.getAsSeenBy();
      if (src != null) {
        Rectangle rv = src.convertRectangle(this.boundsAtIndex, asSeenBy);
        //note: ignore insets
        return rv;
      } else {
        return null;
      }
    }

    @Override
    public Shape getVisibleShape(ScreenElement asSeenBy, Insets insets) {
      AwtComponentView<?> src = CodeEditor.this.getAsSeenBy();
      if (src != null) {
        Rectangle bounds = src.convertRectangle(this.boundsAtIndex, asSeenBy);
        //note: ignore insets
        //          java.awt.Rectangle visibleBounds = statementListPropertyPane.getVisibleRectangle( asSeenBy );
        //          return bounds.intersection( visibleBounds );
        return bounds;
      } else {
        return null;
      }
    }

    @Override
    public boolean isInView() {
      if (isWarningAlreadyPrinted) {
        //pass
      } else {
        Logger.info("getTrackableShapeAtIndexOf");
        isWarningAlreadyPrinted = true;
      }
      return true;
    }

    @Override
    public ScrollPane getScrollPaneAncestor() {
      return this.statementListPropertyPane.getScrollPaneAncestor();
    }

    @Override
    public void addComponentListener(ComponentListener listener) {
      this.statementListPropertyPane.addComponentListener(listener);
    }

    @Override
    public void removeComponentListener(ComponentListener listener) {
      this.statementListPropertyPane.removeComponentListener(listener);
    }

    @Override
    public void addHierarchyBoundsListener(HierarchyBoundsListener listener) {
      this.statementListPropertyPane.addHierarchyBoundsListener(listener);
    }

    @Override
    public void removeHierarchyBoundsListener(HierarchyBoundsListener listener) {
      this.statementListPropertyPane.removeHierarchyBoundsListener(listener);
    }
  }

  public TrackableShape getTrackableShapeAtIndexOf(StatementListProperty statementListProperty, int index, boolean EPIC_HACK_isDropConstraintDesired) {
    if (statementListProperty != null) {
      //choose any non-ancestor

      AwtContainerView<?> arbitrarilyChosenSource = IDE.getActiveInstance().getSceneEditor();
      DragModel dragModel = null;
      Logger.todo(dragModel);
      StatementListPropertyPaneInfo[] statementListPropertyPaneInfos = this.getDropReceptor().createStatementListPropertyPaneInfos(dragModel, arbitrarilyChosenSource);
      final int N = statementListPropertyPaneInfos.length;
      for (int i = 0; i < N; i++) {
        StatementListPropertyPaneInfo statementListPropertyPaneInfo = statementListPropertyPaneInfos[i];
        StatementListPropertyView statementListPropertyPane = statementListPropertyPaneInfo.getStatementListPropertyPane();
        if (statementListPropertyPane.getProperty() == statementListProperty) {
          StatementListPropertyView.BoundInformation yBounds = statementListPropertyPane.calculateYBounds(index);
          Rectangle bounds = statementListPropertyPaneInfo.getBounds();

          int yMinimum;
          if ((yBounds.yMinimum != null) && (yBounds.y != null)) {
            yMinimum = convertY(statementListPropertyPane, yBounds.yMinimum, CodeEditor.this.getAsSeenBy());
            int y = convertY(statementListPropertyPane, yBounds.y, CodeEditor.this.getAsSeenBy());
            yMinimum = capMinimum(yMinimum, y, statementListPropertyPaneInfos, index);
          } else {
            yMinimum = bounds.y;
          }
          int yMaximum;
          if ((yBounds.yMaximum != null) && (yBounds.yPlusHeight != null)) {
            yMaximum = convertY(statementListPropertyPane, yBounds.yMaximum, CodeEditor.this.getAsSeenBy());
            int yPlusHeight = convertY(statementListPropertyPane, yBounds.yPlusHeight, CodeEditor.this.getAsSeenBy());
            yMaximum = capMaximum(yMaximum, yPlusHeight, statementListPropertyPaneInfos, index);
          } else {
            yMaximum = (bounds.y + bounds.height) - 1;
          }

          Rectangle boundsAtIndex = new Rectangle(bounds.x, yMinimum, bounds.width, (yMaximum - yMinimum) + 1);

          return new StatementListIndexTrackableShape(statementListProperty, index, statementListPropertyPane, boundsAtIndex);
        }
      }
      //      org.lgna.project.ast.Node a = ((org.lgna.project.ast.BlockStatement)statementListProperty.getOwner()).getParent();
      //      org.lgna.project.ast.Node b = ((org.lgna.project.ast.BlockStatement)statementListPropertyPaneInfos[0].getStatementListPropertyPane().getProperty().getOwner()).getParent();
      //      edu.cmu.cs.dennisc.java.util.logging.Logger.severe( a, b, a.hashCode(), b.hashCode(), a.getId(), b.getId() );
    }

    return null;
  }

  @Override
  protected AwtComponentView<?> getAsSeenBy() {
    return this.bodyPane;
  }

  @Override
  public Printable getPrintable() {
    return new PrintHelper.Builder(this.getInsets(), this.getBackgroundColor()).center(this.getCenterComponent().getAwtComponent()).pageStart(this.getPageStartComponent().getAwtComponent()).build();
  }

  @Override
  public void setJavaCodeOnTheSide(boolean value, boolean isFirstTime) {
    if (value) {
      if (isFirstTime) {
        //pass
      } else {
        this.removeComponent(this.scrollPane);
      }
      this.scrollPane.setViewportView(null);
      this.addCenterComponent(this.bodyPane);
    } else {
      if (isFirstTime) {
        //pass
      } else {
        this.removeComponent(this.bodyPane);
      }
      this.scrollPane.setViewportView(this.bodyPane);
      this.addCenterComponent(this.scrollPane);
    }
  }

  private final AbstractCode code;
  private final Panel header;
  private final StatementListPropertyView rootStatementListPropertyPane;
  private final BodyPane bodyPane;
  private final ScrollPane scrollPane = new ScrollPane();
}
