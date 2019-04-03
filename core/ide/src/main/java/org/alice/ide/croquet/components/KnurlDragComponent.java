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

package org.alice.ide.croquet.components;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.KnurlUtilities;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.imp.JDragView;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
public abstract class KnurlDragComponent<M extends DragModel> extends DragComponent<M> {
  protected static final int KNURL_WIDTH = 8;

  public KnurlDragComponent(M model, boolean isAlphaDesiredWhenOverDropReceptor) {
    super(model, isAlphaDesiredWhenOverDropReceptor);
  }

  protected Paint getForegroundPaint(int x, int y, int width, int height) {
    return this.getForegroundColor();
  }

  protected Paint getBackgroundPaint(int x, int y, int width, int height) {
    return this.getBackgroundColor();
  }

  protected final boolean isKnurlDesired() {
    return this.getModel() != null;
  }

  protected abstract int getInsetTop();

  protected abstract int getDockInsetLeft();

  protected final int getKnurlInsetLeft() {
    if (this.isKnurlDesired()) {
      return KNURL_WIDTH;
    } else {
      return 0;
    }
  }

  protected abstract int getInternalInsetLeft();

  protected final int getInsetLeft() {
    int rv = 0;
    rv += this.getDockInsetLeft();
    rv += this.getKnurlInsetLeft();
    rv += this.getInternalInsetLeft();
    return rv;
  }

  protected abstract int getInsetBottom();

  protected abstract int getInsetRight();

  protected abstract LayoutManager createLayoutManager(JPanel jComponent);

  @Override
  protected JDragView createAwtComponent() {
    JDragView rv = new JDragView() {
      @Override
      public boolean isOpaque() {
        return false;
      }

      @Override
      public boolean contains(int x, int y) {
        return KnurlDragComponent.this.contains(x, y, super.contains(x, y));
      }

      @Override
      public JToolTip createToolTip() {
        return KnurlDragComponent.this.createToolTip(super.createToolTip());
      }

      @Override
      public Point getToolTipLocation(MouseEvent event) {
        return KnurlDragComponent.this.getToolTipLocation(super.getToolTipLocation(event), event);
      }

      @Override
      public Dimension getMaximumSize() {
        if (KnurlDragComponent.this.isMaximumSizeClampedToPreferredSize()) {
          return this.getPreferredSize();
        } else {
          return super.getMaximumSize();
        }
      }

      @Override
      public Dimension getPreferredSize() {
        return KnurlDragComponent.this.getPreferredSize(super.getPreferredSize());
      }

      @Override
      public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int x = 0;
        int y = 0;
        int width = this.getWidth();
        int height = this.getHeight();

        Paint prevPaint;
        prevPaint = g2.getPaint();
        try {
          g2.setPaint(KnurlDragComponent.this.getBackgroundPaint(x, y, width, height));
          KnurlDragComponent.this.paintPrologue(g2, x, y, width, height);
        } finally {
          g2.setPaint(prevPaint);
        }

        this.paintBorder(g2);
        this.paintComponent(g2);
        this.paintChildren(g);

        prevPaint = g2.getPaint();
        g2.setPaint(KnurlDragComponent.this.getForegroundPaint(x, y, width, height));
        try {
          KnurlDragComponent.this.paintEpilogue(g2, x, y, width, height);
        } finally {
          g2.setPaint(prevPaint);
        }
      }

    };

    rv.setBorder(BorderFactory.createEmptyBorder(this.getInsetTop(), this.getInsetLeft(), this.getInsetBottom(), this.getInsetRight()));
    if (this.isKnurlDesired()) {
      rv.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    LayoutManager layoutManager = this.createLayoutManager(rv);
    rv.setLayout(layoutManager);

    rv.setOpaque(false);
    rv.setBackground(null);

    rv.setAlignmentX(Component.LEFT_ALIGNMENT);
    rv.setAlignmentY(Component.CENTER_ALIGNMENT);

    return rv;
  }

  protected boolean contains(int x, int y, boolean jContains) {
    return jContains;
  }

  protected Dimension getPreferredSize(Dimension size) {
    return size;
  }

  protected JToolTip createToolTip(JToolTip jToolTip) {
    return jToolTip;
  }

  protected Point getToolTipLocation(Point location, MouseEvent event) {
    return location;
  }

  private static final Stroke ACTIVE_STROKE = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final Stroke PASSIVE_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final Color HIGHLIGHT_COLOR = new Color(255, 255, 255);
  private static final Color SHADOW_COLOR = new Color(0, 0, 0);

  protected Paint getPassiveOutlinePaint() {
    return Color.GRAY;
  }

  protected void paintOutline(Graphics2D g2, Shape shape) {
    if (shape != null) {
      Stroke prevStroke = g2.getStroke();
      if (this.isActive()) {
        GraphicsUtilities.draw3DishShape(g2, shape, HIGHLIGHT_COLOR, SHADOW_COLOR, ACTIVE_STROKE);
      } else {
        g2.setPaint(this.getPassiveOutlinePaint());
        g2.setStroke(PASSIVE_STROKE);
        g2.draw(shape);
      }
      g2.setStroke(prevStroke);
    }
  }

  protected abstract Shape createShape(int x, int y, int width, int height);

  @Override
  protected void paintEpilogue(Graphics2D g2, int x, int y, int width, int height) {
    Shape shape = this.createShape(x, y, width, height);
    this.paintOutline(g2, shape);
    if (isKnurlDesired()) {
      int grayscale;
      if (this.isActive()) {
        grayscale = 0;
      } else {
        grayscale = 127;
      }
      g2.setColor(ColorUtilities.createGray(grayscale));
      KnurlUtilities.paintKnurl5(g2, x + this.getDockInsetLeft(), y + 2, KNURL_WIDTH, height - 5);
    }
  }

  public void addComponent(AwtComponentView<?> component) {
    this.internalAddComponent(component);
  }

  public void addComponent(AwtComponentView<?> component, Object constraints) {
    this.internalAddComponent(component, constraints);
  }

  public void forgetAndRemoveComponent(AwtComponentView<?> component) {
    this.internalForgetAndRemoveComponent(component);
  }

  public void removeAllComponents() {
    this.internalRemoveAllComponents();
  }

  public void forgetAndRemoveAllComponents() {
    this.internalForgetAndRemoveAllComponents();
  }

  protected void fillBounds(Graphics2D g2) {
    this.fillBounds(g2, 0, 0, this.getWidth(), this.getHeight());
  }
}
