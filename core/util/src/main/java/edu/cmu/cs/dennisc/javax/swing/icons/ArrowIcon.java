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
package edu.cmu.cs.dennisc.javax.swing.icons;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * A chevron icon that does it's very best to mimic the menu icons that we get from swing components
 * @author Dennis Cosgrove
 */
public class ArrowIcon implements Icon {

  private boolean rotateIfSelected = false;
  private int size;

  public ArrowIcon(int size) {
    this(size, false);
  }

  public ArrowIcon(int size, boolean rotateIfSelected) {
    this.size = size;
    this.rotateIfSelected = rotateIfSelected;
  }

  public void setSize(int s) {
    size = s;
  }

  protected ButtonModel getButtonModel(Component c) {
    AbstractButton button = (AbstractButton) c;
    return button.getModel();
  }

  protected Color getColor(ButtonModel model) {
    if (!model.isEnabled()) {
      return UIManager.getColor("ComboBox.buttonDisabledArrowColor");
    } else if (model.isRollover() || model.isArmed()) {
      return UIManager.getColor("ComboBox.buttonHoverArrowColor");
    } else if (model.isPressed() || model.isSelected()) {
      return UIManager.getColor("ComboBox.buttonPressedArrowColor");
    } else {
      return UIManager.getColor("ComboBox.buttonArrowColor");
    }
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    ButtonModel buttonModel = getButtonModel(c);

    Heading heading = !rotateIfSelected || (buttonModel.isSelected() || buttonModel.isPressed()) ? Heading.SOUTH : Heading.EAST;
    GeneralPath path = heading.asPath(x, y, size);

    Graphics2D g2 = (Graphics2D) g;
    Object prevAntialiasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Paint drawPaint = getColor(buttonModel);
    if (drawPaint != null) {
      g2.setPaint(drawPaint);
      g2.setStroke(new BasicStroke(1.5F));
      g2.draw(path);
    }
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, prevAntialiasing == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : prevAntialiasing);
  }

  @Override
  public int getIconWidth() {
    return size;
  }

  @Override
  public int getIconHeight() {
    return size;
  }

  protected enum Heading {
    // our arrows are an obtuse angle, centered in the square as defined by size
    EAST() {
      @Override
      public GeneralPath asPath(float x, float y, float size) {
        final float quarter = (size - 1) / 4;
        GeneralPath path = new GeneralPath();
        // top left
        path.moveTo(x + quarter, y);
        // center point
        path.lineTo(x + quarter * 3, y + quarter * 2);
        // bottom left
        path.lineTo(x + quarter, y + quarter * 4);
        return path;
      }
    }, SOUTH() {
      @Override
      public GeneralPath asPath(float x, float y, float size) {
        final float quarter = (size - 1) / 4;
        GeneralPath path = new GeneralPath();
        // top left
        path.moveTo(x, y + quarter);
        // center point
        path.lineTo(x + quarter * 2, y + quarter * 3);
        // top right
        path.lineTo(x + quarter * 4, y +  quarter);
        return path;
      }
    };

    protected abstract GeneralPath asPath(float x, float y, float size);
  }
}
