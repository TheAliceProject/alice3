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
package edu.cmu.cs.dennisc.javax.swing;

import edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @author Dennis Cosgrove
 *
 */
public class IconUtilities {
  private IconUtilities() {
    throw new AssertionError();
  }

  private static final Container privateContainer = new Container();

  // We have taken the approach of rendering our components as images to put them in our dropdown menus as icons
  // here is where that happens.
  public static Icon createIcon(Component component) {
    Icon rv;
    Dimension size = component.getPreferredSize();

    if ((size.width > 0) && (size.height > 0)) {
      // render things big and then scale down, so they don't look terrible
      int SCALE_FOR_BEAUTY = 2;
      BufferedImage image = new BufferedImage(size.width * SCALE_FOR_BEAUTY, size.height * SCALE_FOR_BEAUTY, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = (Graphics2D) image.getGraphics();
      g.scale(SCALE_FOR_BEAUTY, SCALE_FOR_BEAUTY);
      component.print(g);
      g.dispose();
      rv = ScaledImageIcon.createSafeInstanceInPixels(image, size.width, size.height);
    } else {
      rv = new Icon() {
        @Override
        public int getIconWidth() {
          return 24;
        }

        @Override
        public int getIconHeight() {
          return 12;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
        }
      };
    }
    return rv;
  }

  public static ImageIcon createImageIcon(URL url) {
    if (url != null) {
      return new ImageIcon(url);
    } else {
      return null;
    }
  }

  public static ImageIcon createImageIcon(Image image) {
    if (image != null) {
      return new ImageIcon(image);
    } else {
      return null;
    }
  }

  public static Icon getInformationIcon() {
    return UIManager.getIcon("OptionPane.informationIcon");
  }

  public static Icon getQuestionIcon() {
    return UIManager.getIcon("OptionPane.questionIcon");
  }

  public static Icon getWarningIcon() {
    return UIManager.getIcon("OptionPane.warningIcon");
  }

  public static Icon getErrorIcon() {
    return UIManager.getIcon("OptionPane.errorIcon");
  }
}
