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
package edu.cmu.cs.dennisc.java.awt;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class GraphicsUtilities {
  private GraphicsUtilities() {
    throw new Error();
  }

  private static BufferedImage s_bufferedImage = null;

  public static Graphics getGraphics() {
    if (s_bufferedImage == null) {
      s_bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
    }
    return s_bufferedImage.getGraphics();
  }

  public static void setRenderingHint(Graphics g, RenderingHints.Key key, Object value) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(key, value);
  }

  public static Object setAntialiasing(Graphics g, Object nextAntialiasing) {
    Graphics2D g2 = (Graphics2D) g;
    Object prevAntialiasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, nextAntialiasing);
    return prevAntialiasing;
  }

  public static Shape setClip(Graphics g, Shape nextClip) {
    Graphics2D g2 = (Graphics2D) g;
    Shape prevClip = g2.getClip();
    g2.setClip(nextClip);
    return prevClip;
  }

  //Scales an image to fit a destination image and then draws that image centered in the destination image
  public static void drawCenteredScaledToFitImage(Image image, Image destImage) {
    Graphics g = destImage.getGraphics();
    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    int destWidth = destImage.getWidth(null);
    int destHeight = destImage.getHeight(null);
    double widthRatio = (double) destWidth / imageWidth;
    double heightRatio = (double) destHeight / imageHeight;
    if (widthRatio < heightRatio) {
      imageWidth = destWidth;
      imageHeight = (int) (imageHeight * widthRatio);
    } else if (heightRatio < widthRatio) {
      imageWidth = (int) (imageWidth * heightRatio);
      imageHeight = destHeight;
    }
    int x = (destWidth - imageWidth) / 2;
    int y = (destHeight - imageHeight) / 2;
    g.drawImage(image, x, y, imageWidth, imageHeight, null);
  }

  public static Image getImageForIcon(Icon icon) {
    if (icon instanceof ImageIcon imageIcon) {
      return imageIcon.getImage();
    } else {
      int width = icon.getIconWidth();
      int height = icon.getIconHeight();
      BufferedImage newIconImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics2D g = newIconImage.createGraphics();
      icon.paintIcon(null, g, 0, 0);
      g.dispose();
      return newIconImage;
    }
  }

  public static void drawCenteredText(Graphics g, String s, int x, int y, int width, int height) {
    if (s != null) {
      FontMetrics fm = g.getFontMetrics();
      int messageWidth = fm.stringWidth(s);
      int ascent = fm.getMaxAscent();
      int descent = fm.getMaxDescent();
      g.drawString(s, (x + (width / 2)) - (messageWidth / 2), (y + (height / 2) + (ascent / 2)) - (descent / 2));
    }
  }

  public static void drawCenteredText(Graphics g, String s, Dimension size) {
    drawCenteredText(g, s, 0, 0, size.width, size.height);
  }

  public static void drawCenteredText(Graphics g, String s, Rectangle rect) {
    drawCenteredText(g, s, rect.x, rect.y, rect.width, rect.height);
  }

  public static void drawCenteredText(Graphics g, String s, Rectangle2D rect) {
    drawCenteredText(g, s, (int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
  }

  private static void renderTriangle(Graphics g, Heading heading, int x, int y, int width, int height, boolean isFill) {
    if (heading != null) {
      int x0 = x;
      int x1 = (x + width) - 1;
      int xC = (x0 + x1) / 2;

      int y0 = y;
      int y1 = (y + height) - 1;
      int yC = (y0 + y1) / 2;

      int[] xPoints;
      int[] yPoints;
      if (heading == Heading.NORTH) {
        xPoints = new int[] {xC, x1, x0};
        yPoints = new int[] {y0, y1, y1};
      } else if (heading == Heading.EAST) {
        xPoints = new int[] {x1, x0, x0};
        yPoints = new int[] {yC, y1, y0};
      } else if (heading == Heading.SOUTH) {
        xPoints = new int[] {xC, x0, x1};
        yPoints = new int[] {y1, y0, y0};
      } else if (heading == Heading.WEST) {
        xPoints = new int[] {x0, x1, x1};
        yPoints = new int[] {yC, y0, y1};
      } else {
        throw new IllegalArgumentException();
      }
      if (isFill) {
        g.fillPolygon(xPoints, yPoints, 3);
      } else {
        g.drawPolygon(xPoints, yPoints, 3);
      }
    } else {
      throw new IllegalArgumentException();
    }
  }

  public enum Heading {
    NORTH, EAST, SOUTH, WEST
  }

  public static void drawTriangle(Graphics g, Heading heading, int x, int y, int width, int height) {
    renderTriangle(g, heading, x, y, width, height, false);
  }

  public static void fillTriangle(Graphics g, Heading heading, int x, int y, int width, int height) {
    renderTriangle(g, heading, x, y, width, height, true);
  }
}
