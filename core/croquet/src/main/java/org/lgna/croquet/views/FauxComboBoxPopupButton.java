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

import edu.cmu.cs.dennisc.javax.swing.icons.DropDownArrowIcon;
import org.lgna.croquet.PopupPrepModel;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import java.awt.*;

/**
 * @author Dennis Cosgrove
 */
public class FauxComboBoxPopupButton<T> extends AbstractPopupButton<PopupPrepModel> {
  public FauxComboBoxPopupButton(PopupPrepModel model) {
    super(model);
  }

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
      int xArrow;
      if (componentOrientation.isLeftToRight()) {
        x = (width - insets.right) - TRAILING_PAD;
        x += OUTER_PAD;
        xArrow = x + this.getComboPad();

      } else {
        xArrow = OUTER_PAD + TRAILING_PAD + this.getComboPad();
      }
      ARROW_ICON.paintIcon(this, g2, xArrow, (height - SIZE) / 2);
    }
  }

  @Override
  protected javax.swing.AbstractButton createSwingButton() {
    return new JFauxComboBoxPopupButton();
  }
}
