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

package edu.cmu.cs.dennisc.javax.swing.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Dennis Cosgrove
 */
public final class JCloseButton extends JButton {
  private static class CloseButtonUI extends BasicButtonUI {
    private static final int SIZE = 14;

    @Override
    public void paint(Graphics g, JComponent c) {
      AbstractButton button = (AbstractButton) c;
      ButtonModel model = button.getModel();

      Graphics2D g2 = (Graphics2D) g;
      Paint prevPaint = g2.getPaint();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      Area area = getArea();
      if (model.isRollover() || model.isArmed()) {
        g2.setPaint(UIManager.getColor("Button.hoverBorderColor"));
        g2.fill(area);
      } else {
        g2.setPaint(UIManager.getColor("Button.foreground"));
        g2.draw(area);
      }
      g2.setPaint(prevPaint);
    }

    private Area getArea() {
      // here we re-create an x from first principles.
      float diagLength = SIZE * 0.9f;
      float lineWidth = diagLength * 0.25f;
      float xC = -diagLength * 0.5f;
      float yC = -lineWidth * 0.5f;
      // make a long skinny rectangle that would be halfway outside our drawing area, so that 0, 0 is where the x will cross
      RoundRectangle2D.Float rr = new RoundRectangle2D.Float(xC, yC, diagLength, lineWidth, lineWidth, lineWidth);

      // take our rectangle and rotate it to draw half of our x
      Area area0 = new Area(rr);
      area0.transform(AffineTransform.getRotateInstance(Math.PI * 0.25));

      // make the other side of our x and combine them
      Area area1 = new Area(rr);
      area1.transform(AffineTransform.getRotateInstance(Math.PI * 0.75));
      area0.add(area1);

      // center our completed x in our space
      AffineTransform m = new AffineTransform();
      m.translate(SIZE * .5d, SIZE * .5d);
      area0.transform(m);
      return area0;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
      return new Dimension(SIZE, SIZE);
    }
  }

  public JCloseButton() {
    this.setOpaque(false);
    this.setAlignmentY(Component.CENTER_ALIGNMENT);
    this.setBorder(null);
    this.setRolloverEnabled(true);
  }

  @Override
  public void updateUI() {
    this.setUI(new CloseButtonUI());
  }

  @Override
  public Dimension getMaximumSize() {
    return this.getPreferredSize();
  }

  @Override
  public boolean contains(int x, int y) {
    Container parent = this.getParent();
    if (parent instanceof AbstractButton button && !button.isSelected()) {
      return false;
    }
    return super.contains(x, y);
  }

  @Override
  public boolean isVisible() {
    Container parent = this.getParent();
    if (parent instanceof AbstractButton button && !button.isSelected()) {
      return false;
    }
    return super.isVisible();
  }
}
