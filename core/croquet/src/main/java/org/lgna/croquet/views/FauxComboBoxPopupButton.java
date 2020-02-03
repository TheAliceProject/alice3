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
package org.lgna.croquet.views;

import edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities;
import edu.cmu.cs.dennisc.javax.swing.icons.DropDownArrowIcon;
import org.lgna.croquet.PopupPrepModel;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Dennis Cosgrove
 */
public class FauxComboBoxPopupButton<T> extends AbstractPopupButton<PopupPrepModel> {
  public FauxComboBoxPopupButton(PopupPrepModel model) {
    super(model);
  }

  private static final Color TOP_COLOR = new Color(255, 255, 255, 91);
  private static final Color BOTTOM_COLOR = new Color(57, 105, 138, 91);
  private static final Color LINE_COLOR = new Color(169, 176, 190);

  private static final Color SELECTED_COLOR = new Color(57, 105, 138);
  private static final Color SELECTED_HIGHTLIGHT_COLOR = SELECTED_COLOR.brighter();
  private static final Color SELECTED_LINE_COLOR = Color.DARK_GRAY;

  protected class JFauxComboBoxPopupButton extends JPopupButton {
    private static final int OUTER_PAD = 6;

    protected JFauxComboBoxPopupButton() {
      this.setHorizontalTextPosition(SwingConstants.LEADING);
    }

    private int getArrowSize() {
      return this.getHeight() / 4;
    }

    private int getComboPad() {
      return this.getArrowSize() / 2;
    }

    @Override
    public Insets getMargin() {
      Insets rv = super.getMargin();
      if (rv != null) {
        ComponentOrientation componentOrientation = this.getComponentOrientation();
        int increment = this.getArrowSize() + this.getComboPad() + OUTER_PAD + TRAILING_PAD;
        if (componentOrientation.isLeftToRight()) {
          rv.right += increment;
        } else {
          rv.left += increment;
        }
      }
      return rv;
    }

    @Override
    protected void paintBorder(Graphics g) {
      super.paintBorder(g);
      final int SIZE = this.getArrowSize();
      final Icon ARROW_ICON = new DropDownArrowIcon(SIZE, Color.WHITE);

      Insets insets = this.getInsets();
      Graphics2D g2 = (Graphics2D) g;
      int width = this.getWidth();
      int height = this.getHeight();

      ComponentOrientation componentOrientation = this.getComponentOrientation();
      int x;
      int w;
      int xArrow;
      int xLine;
      if (componentOrientation.isLeftToRight()) {
        x = (width - insets.right) - TRAILING_PAD;
        x += OUTER_PAD;
        w = width - x;
        xArrow = x + this.getComboPad();
        xLine = x;

      } else {
        xArrow = OUTER_PAD + TRAILING_PAD + this.getComboPad();
        x = 0;
        w = insets.left + TRAILING_PAD;
        w -= OUTER_PAD;
        xLine = w;
      }
      if (this.getUI().getClass().getSimpleName().contains("Synth")) {
        ButtonModel buttonModel = this.getModel();
        boolean isPressedOrSelected = buttonModel.isPressed() || buttonModel.isSelected();
        double round = 8;
        double inset = isPressedOrSelected ? 2 : 3;
        double offsetY = isPressedOrSelected ? 1 : 0;

        RoundRectangle2D r = new RoundRectangle2D.Double(inset, inset + offsetY, width - (inset * 2), (height - (inset * 2)), round, round);

        Shape prevClip = g2.getClip();

        g2.setClip(AreaUtilities.createIntersection(prevClip, r));

        if (isPressedOrSelected) {
          g2.setPaint(new GradientPaint(width, 0, SELECTED_HIGHTLIGHT_COLOR, width, height / 6, SELECTED_COLOR));
          g.fillRect(x, 0, w, height / 2);
          g2.setPaint(new GradientPaint(width, (5 * height) / 6, SELECTED_COLOR, width, height, SELECTED_HIGHTLIGHT_COLOR));
          g.fillRect(x, height / 2, w, (height / 2));
        } else {
          g2.setPaint(new GradientPaint(width, 0, TOP_COLOR, width, (2 * height) / 3, BOTTOM_COLOR));
          g.fillRect(x, 0, w, height);
        }
        if (isPressedOrSelected) {
          g2.setPaint(SELECTED_LINE_COLOR);
        } else {
          g2.setPaint(LINE_COLOR);
        }
        g2.fillRect(xLine, 0, 1, height);
        g2.setClip(prevClip);
      }
      ARROW_ICON.paintIcon(this, g2, xArrow, (height - SIZE) / 2);
    }
  }

  @Override
  protected javax.swing.AbstractButton createSwingButton() {
    JFauxComboBoxPopupButton rv = new JFauxComboBoxPopupButton();
    return rv;
  }
}
