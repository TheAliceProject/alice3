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

package org.alice.stageide.sceneeditor.viewmanager;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.alice.ide.icons.Icons;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class MoveToImageIcon implements Icon {

  private Icon leftIcon;
  private Icon disabledLeftIcon;
  private Icon rightIcon;
  private Icon disabledRightIcon;

  private final FlatSVGIcon arrowIcon;
  private final FlatSVGIcon unknownIcon;

  public MoveToImageIcon() {
    super();
    arrowIcon = new FlatSVGIcon(Icons.class.getResource("images/moveToArrowIcon.svg"));
    unknownIcon = new FlatSVGIcon(Icons.class.getResource("images/unknownIcon.svg"));
  }
  private Icon getLeftIcon() {
    return getLeftIcon(false);
  }

  private Icon getLeftIcon(boolean disabled) {
    if (leftIcon != null) {
      return disabled ? disabledLeftIcon : leftIcon;
    }
    return disabled ? unknownIcon.getDisabledIcon() : unknownIcon;
  }

  private Icon getRightIcon() {
    return getRightIcon(false);
  }

  private Icon getRightIcon(boolean disabled) {
    if (rightIcon != null) {
      return disabled ? disabledRightIcon : rightIcon;
    }
    return disabled ? unknownIcon.getDisabledIcon() : unknownIcon;
  }

  private Icon getArrowIcon(boolean disabled) {
   return disabled ? arrowIcon.getDisabledIcon() : arrowIcon;
  }

  @Override
  public int getIconWidth() {
    return getLeftIcon().getIconWidth() + arrowIcon.getIconWidth() + getRightIcon().getIconWidth();
  }

  @Override
  public int getIconHeight() {
    return Math.max(arrowIcon.getIconHeight(), Math.max(getLeftIcon().getIconHeight(), getRightIcon().getIconHeight()));
  }

  public void setLeftIcon(Icon icon) {
    leftIcon = icon;
    if (leftIcon != null && leftIcon instanceof FlatSVGIcon svgIcon) {
      disabledLeftIcon = svgIcon.getDisabledIcon();
    } else {
      disabledLeftIcon = desaturate(leftIcon);
    }
  }

  public void setRightIcon(Icon icon) {
    rightIcon = icon;
    if (rightIcon != null && rightIcon instanceof FlatSVGIcon svgIcon) {
      disabledRightIcon = svgIcon.getDisabledIcon();
    } else {
      disabledRightIcon = desaturate(rightIcon);
    }
  }

  public static Icon desaturate(Icon source) {
    BufferedImage imgSrc = null;
    if (source instanceof ImageIcon icon) {
      Image image = icon.getImage();
      if (image instanceof BufferedImage bufferedImage) {
        imgSrc = bufferedImage;
      }
    }
    if ((imgSrc == null) && (source != null)) {
      imgSrc = new BufferedImage(source.getIconWidth(), source.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      source.paintIcon(null, imgSrc.getGraphics(), 0, 0);
    }
    if (imgSrc != null) {
      ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
      colorConvert.filter(imgSrc, imgSrc);
      return new ImageIcon(imgSrc);
    }
    return null;
  }

  @Override
  public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
    final boolean disabled = (c != null) && !c.isEnabled();

    // draw the left icon
    Icon left = getLeftIcon(disabled);
    int xPos = 0;
    int yOffset = (int) ((getIconHeight() - left.getIconHeight()) * .5);
    left.paintIcon(c, g, x + xPos, y + yOffset);

    // draw the arrow
    Icon arrow = getArrowIcon(disabled);
    xPos += left.getIconWidth();
    yOffset = (int) ((getIconHeight() - arrow.getIconHeight()) * .5);
    arrow.paintIcon(c, g, x + xPos, y + yOffset);

    // draw the right icon
    Icon right =  getRightIcon(disabled);
    xPos += arrow.getIconWidth();
    yOffset = (int) ((this.getIconHeight() - right.getIconHeight()) * .5);
    right.paintIcon(c, g, x + xPos, y + yOffset);
  }

}
