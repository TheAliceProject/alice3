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
package org.alice.ide.highlight;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.codeeditor.ExpressionPropertyDropDownPane;
import org.alice.ide.common.AbstractStatementPane;
import org.alice.ide.common.FieldDeclarationPane;
import org.alice.ide.croquet.components.ExpressionDropDown;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.alice.ide.declarationseditor.type.components.TypeDeclarationView;
import org.alice.ide.x.components.AbstractExpressionView;
import org.lgna.croquet.Application;
import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.CustomItemState;
import org.lgna.croquet.resolvers.RuntimeResolver;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.LayerStencil;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserField;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class IdeHighlightStencil extends LayerStencil {
  private static final Color STENCIL_BASE_COLOR = new Color(181, 140, 140, 150);
  private static final Color STENCIL_LINE_COLOR = new Color(92, 48, 24, 63);
  private static final Painter GLOW_PAINTER = new GlowPainter(new Color(4, 142, 255, 23));

  private static final int PAD = 4;
  private static final Insets PAINT_INSETS = new Insets(PAD, PAD, PAD, PAD);

  private RuntimeResolver<? extends TrackableShape> trackableShapeResolver;
  private TrackableShape trackableShape;

  private static final KeyStroke HIDE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
  private final AWTEventListener awtEventListener = event -> {
    MouseEvent e = (MouseEvent) event;
    if (e.getID() == MouseEvent.MOUSE_PRESSED) {
      IdeHighlightStencil.this.hide();
    }
  };
  private final Paint stencilPaint = createStencilPaint();
  private final ActionListener hideAction = e -> hide();

  public IdeHighlightStencil(AbstractWindow<?> window, Integer layerId) {
    super(window, layerId);
  }

  public void showHighlightOverField(final UserField field) {
    if (field != null) {
      setResolver(() -> {
        DeclarationComposite<?, ?> declarationComposite = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
        if (declarationComposite != null) {
          DeclarationView view = declarationComposite.getView();
          if (view instanceof TypeDeclarationView) {
            List<JPanel> jPanels = ComponentUtilities.findAllMatches(view.getAwtComponent(), JPanel.class);
            for (JPanel jPanel : jPanels) {
              AwtComponentView<?> component = AwtComponentView.lookup(jPanel);
              if (component instanceof FieldDeclarationPane fieldDeclarationPane) {
                UserField candidate = fieldDeclarationPane.getField();
                if (candidate == field) {
                  return fieldDeclarationPane;
                }
              }
            }
          }
        }
        return null;
      });
    } else {
      Logger.severe("field is null");
    }
  }

  public void showHighlightOverExpression(final Expression expression) {
    if (expression != null) {
      setResolver(() -> {
        DeclarationComposite<?, ?> declarationComposite = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
        if (declarationComposite != null) {
          DeclarationView view = declarationComposite.getView();

          List<AbstractButton> jButtons = ComponentUtilities.findAllMatches(view.getAwtComponent(), AbstractButton.class);
          for (AbstractButton jButton : jButtons) {
            Expression candidate = null;
            AwtComponentView<?> component = AwtComponentView.lookup(jButton);
            if (component instanceof ExpressionPropertyDropDownPane expressionPropertyDropDownPane) {
              candidate = expressionPropertyDropDownPane.getExpressionProperty().getValue();
            } else if (component instanceof ExpressionDropDown) {
              ExpressionDropDown<Expression> expressionDropDown = (ExpressionDropDown<Expression>) component;
              CompletionModel completionModel = expressionDropDown.getModel().getCascadeRoot().getCompletionModel();
              if (completionModel instanceof CustomItemState) {
                CustomItemState<Expression> state = (CustomItemState<Expression>) completionModel;
                candidate = state.getValue();
              }
            } else if (component instanceof AbstractExpressionView<?> expressionView) {
              candidate = expressionView.getExpression();
            }
            if (candidate == expression) {
              return component;
            }
          }
        }
        return null;
      });
    } else {
      Logger.severe("expression is null");
    }
  }

  public void showHighlightOverStatement(final Statement statement) {
    if (statement != null) {
      setResolver(() -> {
        DeclarationComposite<?, ?> declarationComposite = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
        if (declarationComposite != null) {
          DeclarationView view = declarationComposite.getView();
          List<JPanel> jPanels = ComponentUtilities.findAllMatches(view.getAwtComponent(), JPanel.class);
          for (JPanel jPanel : jPanels) {
            Statement candidate = null;
            AwtComponentView<?> component = AwtComponentView.lookup(jPanel);
            if (component instanceof AbstractStatementPane statementPane) {
              candidate = statementPane.getStatement();
            }
            if (candidate == statement) {
              return component;
            }
          }
        }
        return null;
      });
    } else {
      Logger.severe("statement is null");
    }
  }

  protected Paint createStencilPaint() {
    int width = 8;
    int height = 8;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = (Graphics2D) image.getGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    g2.setColor(STENCIL_BASE_COLOR);
    g2.fillRect(0, 0, width, height);
    g2.setColor(STENCIL_LINE_COLOR);
    g2.drawLine(0, height, width, 0);
    g2.fillRect(0, 0, 1, 1);
    g2.dispose();
    return new TexturePaint(image, new Rectangle(0, 0, width, height));
  }

  private Shape getVisibleShape(AwtComponentView<?> asSeenBy, Insets insets) {
    if (trackableShape != null && trackableShape.isInView()) {
      return trackableShape.getVisibleShape(asSeenBy, insets);
    } else {
      return null;
    }
  }

  private Area getAreaForContains(Area area, AwtComponentView<?> asSeenBy) {
    if (trackableShapeResolver != null) {
      Shape featureAreaToSubtract = this.getVisibleShape(asSeenBy, null);
      if (featureAreaToSubtract != null) {
        area.subtract(new Area(featureAreaToSubtract));
      }
    }
    return area;
  }

  private Area getAreaForPaint(Area area, AwtComponentView<?> asSeenBy) {
    if (trackableShapeResolver != null) {
      Shape featureAreaToSubtract = this.getVisibleShape(asSeenBy, PAINT_INSETS);
      if (featureAreaToSubtract != null) {
        area.subtract(new Area(featureAreaToSubtract));
      }
    }
    return area;
  }

  @Override
  protected boolean contains(int x, int y, boolean superContains) {
    if (superContains) {
      Shape shape = this.getLocalBounds();
      shape = getAreaForContains(new Area(shape), IdeHighlightStencil.this);
      return shape.contains(x, y);
    } else {
      return false;
    }
  }

  @Override
  protected LayoutManager createLayoutManager(JPanel jPanel) {
    return new FlowLayout() {
    };
  }

  @Override
  protected void paintComponentPrologue(Graphics2D g2) {
    Shape prevClip = g2.getClip();
    Paint prevPaint = g2.getPaint();
    Stroke prevStroke = g2.getStroke();

    Shape shape = prevClip;
    shape = getAreaForPaint(new Area(shape), this);
    g2.setPaint(stencilPaint);
    g2.fill(shape);

    g2.setStroke(prevStroke);
    g2.setPaint(prevPaint);
  }

  @Override
  protected void paintComponentEpilogue(Graphics2D g2) {
    Shape shape = getVisibleShape(IdeHighlightStencil.this, null);
    if (shape != null) {
      Paint prevPaint = g2.getPaint();
      Stroke prevStroke = g2.getStroke();
      GLOW_PAINTER.paint(g2, shape);
      g2.setStroke(prevStroke);
      g2.setPaint(prevPaint);
    }
  }

  @Override
  protected void paintEpilogue(Graphics2D g2) {
  }

  private void show() {
    registerKeyboardAction(this.hideAction, HIDE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW);
    setStencilShowing(true);
    Toolkit.getDefaultToolkit().addAWTEventListener(this.awtEventListener, AWTEvent.MOUSE_EVENT_MASK);

  }

  private void hide() {
    Toolkit.getDefaultToolkit().removeAWTEventListener(this.awtEventListener);
    setStencilShowing(false);
    unregisterKeyboardAction(HIDE_KEY_STROKE);
  }

  public void hideIfNecessary() {
    if (isStencilShowing()) {
      hide();
    }
  }

  private void setResolver(RuntimeResolver<TrackableShape> resolver) {
    trackableShapeResolver = resolver;
    trackableShape = null;
    TrackableShape nextTrackableShape = this.trackableShapeResolver.getResolved();
    if (nextTrackableShape != this.trackableShape) {
      Logger.info("trackableShape change");
      if (this.trackableShape != null) {
        this.trackableShape.removeHierarchyBoundsListener(this.hierarchyBoundsListener);
        this.trackableShape.removeComponentListener(this.componentListener);
      }
      this.trackableShape = nextTrackableShape;
      if (this.trackableShape != null) {
        this.trackableShape.addComponentListener(this.componentListener);
        this.trackableShape.addHierarchyBoundsListener(this.hierarchyBoundsListener);
      }
    }
    show();
  }

  // this is highlighting things way outside the scope of our little view, we have to repaint a lot

  private static void repaintAll() {
    Application.getActiveInstance().getDocumentFrame().getFrame().getContentPane().repaint();
  }

  private final HierarchyBoundsListener hierarchyBoundsListener = new HierarchyBoundsListener() {
    @Override
    public void ancestorMoved(HierarchyEvent e) {
      repaintAll();
    }

    @Override
    public void ancestorResized(HierarchyEvent e) {
      repaintAll();
    }
  };
  private final ComponentListener componentListener = new ComponentListener() {
    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
      repaintAll();
    }

    @Override
    public void componentResized(ComponentEvent e) {
      repaintAll();
    }
  };

}
