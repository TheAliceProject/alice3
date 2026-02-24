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

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertCascade;
import org.lgna.project.ast.StatementListProperty;

import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * placeholder "drop statement here" that appears in new code blocks
 * @author Dennis Cosgrove
 */
public class StatementListBorder implements Border {
  private static String TEXT;

  private static final Stroke DASHED_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {9.0f, 3.0f}, 0);

  private static final int EMPTY_INSET_TOP = 3;
  private static final int EMPTY_INSET_BOTTOM = 16;
  private static final int EMPTY_INSET_LEADING = 12;
  private static final int EMPTY_INSET_TRAILING = 64;

  private static Insets createEmptyInsets(ComponentOrientation componentOrientation) {
    if (componentOrientation.isLeftToRight()) {
      return new Insets(EMPTY_INSET_TOP, EMPTY_INSET_LEADING, EMPTY_INSET_BOTTOM, EMPTY_INSET_TRAILING);
    } else {
      return new Insets(EMPTY_INSET_TOP, EMPTY_INSET_TRAILING, EMPTY_INSET_BOTTOM, EMPTY_INSET_LEADING);
    }
  }

  private final boolean isMutable;
  private final StatementListProperty alternateListProperty;

  private final Insets normalInsets;
  private final int minimum;

  private boolean isDrawingDesired = true;

  public StatementListBorder(boolean isMutable, StatementListProperty alternateListProperty, Insets normalInsets, int minimum) {
    this.isMutable = isMutable;
    this.alternateListProperty = alternateListProperty;
    this.normalInsets = normalInsets;
    this.minimum = minimum;
  }

  private static void initializeTextIfNecessary() {
    if (TEXT == null) {
      TEXT = ResourceBundleUtilities.getStringForKey("dropStatementHere", "org.alice.ide.codeeditor.CodeEditor");
    }
  }

  private static Rectangle2D getStringBounds(Component c, Graphics g) {
    FontMetrics fontMetrics = c.getFontMetrics(c.getFont());
    if (fontMetrics != null) {
      initializeTextIfNecessary();
      return fontMetrics.getStringBounds(TEXT, g);
    } else {
      return null;
    }
  }

  @Override
  public Insets getBorderInsets(Component c) {
    Container container = (Container) c;
    if (this.isVirtuallyEmpty(container)) {
      Graphics g = c.getGraphics();
      Rectangle2D bounds = getStringBounds(c, g);
      if (g != null) {
        g.dispose();
      }
      Insets EMPTY_INSETS = createEmptyInsets(container.getComponentOrientation());
      if (bounds != null) {
        int left = EMPTY_INSETS.left;
        int right = EMPTY_INSETS.right;
        if (this.isCompletelyEmpty(container)) {
          int textWidth = (int) Math.ceil(bounds.getWidth());
          if (container.getComponentOrientation().isLeftToRight()) {
            right += textWidth;
          } else {
            left += textWidth;
          }
        }
        return new Insets(EMPTY_INSETS.top, left, EMPTY_INSETS.bottom + (int) bounds.getHeight(), right);
      } else {
        return EMPTY_INSETS;
      }
    } else {
      return this.normalInsets;
    }
  }

  public int getMinimum() {
    return this.minimum;
  }

  private boolean isCompletelyEmpty(Container container) {
    return container.getComponentCount() == 0;
  }

  public boolean isVirtuallyEmpty(Container container) {
    return container.getComponentCount() <= this.minimum;
  }

  @Override
  public boolean isBorderOpaque() {
    return false;
  }

  private boolean isDashed() {
    return (alternateListProperty != null) && !alternateListProperty.isEmpty();
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
    Container container = (Container) c;
    if (!this.isVirtuallyEmpty(container) || !this.isDrawingDesired()) {
      return;
    }
    Rectangle2D bounds = getStringBounds(c, g);
    if (bounds == null) {
      return;
    }

    Insets EMPTY_INSETS = createEmptyInsets(container.getComponentOrientation());
    final int PADDING = 24;
    int width = (EMPTY_INSET_LEADING + (int) bounds.getWidth()) + PADDING;
    int height = (EMPTY_INSET_BOTTOM + (int) bounds.getHeight()) - 4;
    int dx;
    if (c.getComponentOrientation().isLeftToRight()) {
      dx = x + EMPTY_INSET_LEADING;
    } else {
      dx = w - EMPTY_INSET_LEADING - (int) Math.ceil(bounds.getWidth()) - PADDING - 12;
    }
    int dy;
    if (container.getComponentCount() == 0) {
      dy = y + EMPTY_INSETS.top + 2;
    } else {
      dy = (y + h) - height - 2;
    }
    g.translate(dx, dy);
    Graphics2D g2 = (Graphics2D) g;
    if (this.isMutable) {
      if (this.isDashed()) {
        g2.setColor(Color.GRAY);
        g2.setStroke(DASHED_STROKE);
        RoundRectangle2D.Float rr = new RoundRectangle2D.Float(1, 1, width - 3, height - 3, 8, 8);
        g2.draw(rr);
      } else {

        RoundRectangle2D.Float rr = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, 8, 8);
        g2.setPaint(UIManager.getColor("Alice.Procedure.color"));
        g2.fill(rr);
      }
      g.setColor(Color.BLACK);
      Object prevTextAntialiasing = g2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      int xText = 6;
      if (!c.getComponentOrientation().isLeftToRight()) {
        xText += PADDING;
      }
      initializeTextIfNecessary();
      GraphicsUtilities.drawCenteredText(g, TEXT, xText, 0, (int) bounds.getWidth(), height);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, prevTextAntialiasing == null ? RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT : prevTextAntialiasing);
    } else {
      RoundRectangle2D.Float rr = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, 8, 8);
      g2.setPaint(UIManager.getColor("Alice.Procedure.color"));
      g2.fill(rr);
    }
    g.translate(-dx, -dy);
  }

  public boolean isDrawingDesired() {
    return this.isDrawingDesired && !StatementInsertCascade.EPIC_HACK_isActive();
  }

  public void setDrawingDesired(boolean isDrawingDesired) {
    this.isDrawingDesired = isDrawingDesired;
  }
}
