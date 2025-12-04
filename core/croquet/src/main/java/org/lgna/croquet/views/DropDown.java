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

import org.lgna.croquet.PopupPrepModel;
import org.lgna.croquet.views.imp.DropDownButtonUI;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * @author Dennis Cosgrove
 */

/*
 This handles the drawing of the top-level item that can become a dropdown, not the menu that dangles off.
 See Cascades for dangling.
*/

public class DropDown<M extends PopupPrepModel> extends AbstractPopupButton<M> {
  private static final int ARROW_WIDTH = 7;
  private static final int ARROW_HEIGHT = 4;
  private static final int BORDER_WIDTH = 2;
  private static final int BORDER_HEIGHT = 1;

  private final SwingComponentView<?> prefixComponent;
  private SwingComponentView<?> mainComponent;
  private final SwingComponentView<?> postfixComponent;

  public DropDown(M model, SwingComponentView<?> prefixComponent, SwingComponentView<?> mainComponent, SwingComponentView<?> postfixComponent) {
    super(model);
    this.prefixComponent = prefixComponent;
    this.mainComponent = mainComponent;
    this.postfixComponent = postfixComponent;
    this.setMaximumSizeClampedToPreferredSize(true);
  }

  public DropDown(M model) {
    this(model, null, null, null);
  }

  public SwingComponentView<?> getMainComponent() {
    return this.mainComponent;
  }

  public void setMainComponent(SwingComponentView<?> mainComponent) {
    if (this.mainComponent != mainComponent) {
      this.mainComponent = mainComponent;
    }
  }

  protected boolean isInactiveFeedbackDesired() {
    return false;
  }

  private final class JDropDownButton extends JToggleButton {
    public JDropDownButton() {
      this.setRolloverEnabled(true);
    }

    @Override
    public void updateUI() {
      this.setUI(new DropDownButtonUI(this));
    }

    @Override
    public Dimension getPreferredSize() {
      return constrainPreferredSizeIfNecessary(super.getPreferredSize());
    }

    @Override
    public Dimension getMaximumSize() {
      if (DropDown.this.isMaximumSizeClampedToPreferredSize()) {
        return this.getPreferredSize();
      } else {
        return super.getMaximumSize();
      }
    }

    @Override
    public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      Paint prevPaint = g2.getPaint();

      int width = this.getWidth();
      int height = this.getHeight();

      // semi-transparent white box for the background
      boolean isActive = model.isRollover() || model.isPressed();
      g2.setColor(new Color(255, 255, 255, isActive ? 128 : 64));
      g2.fillRect(0, 0, width, height);

      // an extra highlight
      if (DropDown.this.isInactiveFeedbackDesired()) {
        g2.setColor(Color.WHITE);
        g2.drawLine(0, 0,  width, 0);
        g2.drawLine(0, 0, 0,  height);
      }

      // draw the contents
      super.paint(g);

      // draw the little arrow
      Color arrowColor = getArrowColor();
      g2.setColor(arrowColor);

      // get the arrow's x position from the rightmost side
      float x0 = width - 1 - BORDER_WIDTH - ARROW_WIDTH;
      float x1 = width - 1 - BORDER_WIDTH;
      float xC = (x0 + x1) / 2;

      // center the arrow vertically, borders are equal, so they can be ignored
      int y0 = height / 2 - ARROW_HEIGHT / 2;
      int y1 = height / 2 + ARROW_HEIGHT / 2;

      GeneralPath path = new GeneralPath();
      path.moveTo(x0, y0);
      path.lineTo(xC, y1);
      path.lineTo(x1, y0);

      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.draw(path);

      g2.setPaint(prevPaint);
    }

    private Color getArrowColor() {
      // this is basically a ComboBox, so color it like one
      Color arrowColor;
      if (!model.isEnabled()) {
        arrowColor = UIManager.getColor("ComboBox.buttonDisabledArrowColor");
      } else if (model.isRollover() || model.isArmed()) {
        arrowColor = UIManager.getColor("ComboBox.buttonHoverArrowColor");
      } else if (model.isPressed() || model.isSelected()) {
        arrowColor = UIManager.getColor("ComboBox.buttonPressedArrowColor");
      } else {
        arrowColor = UIManager.getColor("ComboBox.buttonArrowColor");
      }
      return arrowColor;
    }
  }

  @Override
  protected javax.swing.AbstractButton createSwingButton() {
    javax.swing.AbstractButton rv = new JDropDownButton();
    rv.setRolloverEnabled(true);
    rv.setOpaque(false);
    rv.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    rv.setFocusable(false);
    // add extra room for the arrow that is just snuck in via paint(), also allow for a border around the arrow
    rv.setBorder(BorderFactory.createEmptyBorder(BORDER_HEIGHT, BORDER_WIDTH, BORDER_HEIGHT, BORDER_WIDTH * 2 + ARROW_WIDTH));

    if ((this.prefixComponent != null) || (this.mainComponent != null) || (this.postfixComponent != null)) {
      rv.setLayout(new BorderLayout());
      if (this.prefixComponent != null) {
        rv.add(this.prefixComponent.getAwtComponent(), BorderLayout.LINE_START);
      }
      if (this.mainComponent != null) {
        rv.add(this.mainComponent.getAwtComponent(), BorderLayout.CENTER);
      }
      if (this.postfixComponent != null) {
        rv.add(this.postfixComponent.getAwtComponent(), BorderLayout.LINE_END);
      }
    }
    return rv;
  }
}
