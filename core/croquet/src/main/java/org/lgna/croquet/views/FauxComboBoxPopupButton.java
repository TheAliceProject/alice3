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

import edu.cmu.cs.dennisc.javax.swing.icons.ArrowIcon;
import org.lgna.croquet.PopupPrepModel;

import javax.swing.SwingConstants;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Objects;

/**
 * * this faux dropdown button is on the large object selecting dropdown in both the code and scene views.
 * @author Dennis Cosgrove
 */
public class FauxComboBoxPopupButton<T> extends AbstractPopupButton<PopupPrepModel> {
  public FauxComboBoxPopupButton(PopupPrepModel model) {
    super(model);
  }

  protected class JFauxComboBoxPopupButton extends JPopupButton {
    private static final int OUTER_PAD = 6;
    ArrowIcon icon;

    protected JFauxComboBoxPopupButton() {
      this.setHorizontalTextPosition(SwingConstants.LEADING);
      icon = new ArrowIcon(calculateIconSize(), false);
    }

    private int calculateIconSize() {
      int size = getHeight() / 2;
      if (icon != null) {
        icon.setSize(size);
      }
      return size;
    }

    @Override
    public Insets getMargin() {
      Insets insets = Objects.requireNonNullElse(super.getMargin(), new Insets(1, 2, 1, 2));

      // add horizontal space for our dropdown arrow, on whatever side it will appear
      int increment = calculateIconSize() + OUTER_PAD;
      if (getComponentOrientation().isLeftToRight()) {
        insets.right += increment;
      } else {
        insets.left += increment;
      }
      return insets;
    }

    @Override
    protected void paintBorder(Graphics g) {
      super.paintBorder(g);
      Insets insets = getInsets();
      final int height = this.getHeight();
      int iconSize = calculateIconSize();
      final int y = insets.bottom + (height - insets.bottom - insets.top - iconSize) / 2;
      final int x = getComponentOrientation().isLeftToRight() ? getWidth() - iconSize - OUTER_PAD : OUTER_PAD;
      icon.paintIcon(this, g, x, y);
    }
  }

  @Override
  protected javax.swing.AbstractButton createSwingButton() {
    return new JFauxComboBoxPopupButton();
  }
}
