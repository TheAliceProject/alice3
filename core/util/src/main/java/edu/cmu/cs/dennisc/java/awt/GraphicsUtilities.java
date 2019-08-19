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

import edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
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
    if (s_bufferedImage != null) {
      //pass
    } else {
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

  public static void drawCenteredImage(Graphics g, Image image, Component component) {
    int x = (component.getWidth() - image.getWidth(component)) / 2;
    int y = (component.getHeight() - image.getHeight(component)) / 2;
    g.drawImage(image, x, y, component);
  }

  public static void drawCenteredScaledToFitImage(Graphics g, Image image, Component component) {
    int imageWidth = image.getWidth(component);
    int imageHeight = image.getHeight(component);
    int componentWidth = component.getWidth();
    int componentHeight = component.getHeight();
    double widthRatio = imageWidth / (double) componentWidth;
    double heightRatio = imageHeight / (double) componentHeight;
    int widthFactor = -1;
    int heightFactor = -1;
    if ((widthRatio > heightRatio) && (widthRatio > 1.0)) {
      widthFactor = componentWidth;
    } else if ((heightRatio > widthRatio) && (heightRatio > 1.0)) {
      heightFactor = componentHeight;
    }
    if ((widthFactor > 0) || (heightFactor > 0)) {
      image = image.getScaledInstance(widthFactor, heightFactor, Image.SCALE_SMOOTH);
    }
    drawCenteredImage(g, image, component);
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
    if (icon instanceof ImageIcon) {
      return ((ImageIcon) icon).getImage();
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

  public static void drawTriangle(Graphics g, Heading heading, Dimension size) {
    drawTriangle(g, heading, 0, 0, size.width, size.height);
  }

  public static void drawTriangle(Graphics g, Heading heading, Rectangle rect) {
    drawTriangle(g, heading, rect.x, rect.y, rect.width, rect.height);
  }

  public static void fillTriangle(Graphics g, Heading heading, int x, int y, int width, int height) {
    renderTriangle(g, heading, x, y, width, height, true);
  }

  public static void fillTriangle(Graphics g, Heading heading, Dimension size) {
    fillTriangle(g, heading, 0, 0, size.width, size.height);
  }

  public static void fillTriangle(Graphics g, Heading heading, Rectangle rect) {
    fillTriangle(g, heading, rect.x, rect.y, rect.width, rect.height);
  }

  private static GeneralPath createPath(float x, float y, float width, float height, boolean isTopLeft) {
    GeneralPath rv = new GeneralPath();
    float halfSize = Math.min(width / 2, height / 2);
    if (isTopLeft) {
      rv.moveTo(x, y);
    } else {
      rv.moveTo(x + width, y + height);
    }
    rv.lineTo(x + width, y);
    rv.lineTo((x + width) - halfSize, y + halfSize);
    rv.lineTo(x + halfSize, (y + height) - halfSize);
    rv.lineTo(x, y + height);
    rv.closePath();
    return rv;
  }

  private static Shape createClip(Shape prevClip, Shape shape, boolean isTopLeft) {
    Rectangle2D bounds = shape.getBounds2D();
    Area rv = new Area(shape);
    if (prevClip != null) {
      rv.intersect(new Area(prevClip));
    }
    rv.subtract(new Area(createPath((float) bounds.getX(), (float) bounds.getY(), (float) bounds.getWidth(), (float) bounds.getHeight(), isTopLeft == false)));
    return rv;
  }

  public static void draw3DRoundRectangle(Graphics g, RoundRectangle2D rr, Paint topLeftPaint, Paint bottomRightPaint, Stroke stroke) {
    Graphics2D g2 = (Graphics2D) g;
    Paint prevPaint = g2.getPaint();
    Stroke prevStroke = g2.getStroke();
    Shape prevClip = g2.getClip();
    Object prevAntialiasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setStroke(stroke);
    g2.setClip(createClip(prevClip, rr, true));
    g2.setPaint(topLeftPaint);
    g2.draw(rr);

    g2.setClip(createClip(prevClip, rr, false));
    g2.setPaint(bottomRightPaint);
    g2.draw(rr);

    g2.setClip(prevClip);
    g2.setStroke(prevStroke);
    g2.setPaint(prevPaint);

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, prevAntialiasing == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : prevAntialiasing);
  }

  public static void draw3DishShape(Graphics g, Shape shape, Paint topLeftPaint, Paint bottomRightPaint, Stroke stroke) {
    Graphics2D g2 = (Graphics2D) g;
    Shape prevClip = g2.getClip();

    GraphicsContext gc = GraphicsContext.getInstanceAndPushGraphics(g2);
    try {
      gc.pushPaint();
      gc.pushStroke();
      gc.pushClip();
      gc.pushAndSetAntialiasing(true);

      g2.setStroke(stroke);
      g2.setClip(createClip(prevClip, shape, true));
      g2.setPaint(topLeftPaint);
      g2.draw(shape);

      g2.setClip(createClip(prevClip, shape, false));
      g2.setPaint(bottomRightPaint);
      g2.draw(shape);
    } finally {
      gc.popAll();
    }
  }

  public static void fillGradientRectangle(Graphics g, Rectangle rect, Color colorA, float yA, Color colorB, float yB, Color colorC, float yC, Color colorD, float yD) {
    Graphics2D g2 = (Graphics2D) g;
    Shape prevClip = g2.getClip();
    GraphicsContext gc = GraphicsContext.getInstanceAndPushGraphics(g2);

    try {
      gc.pushClip();
      gc.pushPaint();

      int x = 0;
      int y0 = rect.y;
      int y1 = y0 + rect.height;
      int yCenter = y1 / 2;

      GradientPaint paintTop = new GradientPaint(x, y0 + (yA * rect.height), colorA, x, y0 + (yB * rect.height), colorB);

      Area topArea = AreaUtilities.createIntersection(prevClip, new Rectangle(x, rect.y, rect.width, yCenter - rect.y));
      g2.setClip(topArea);
      g2.setPaint(paintTop);
      g2.fill(rect);

      GradientPaint paintBottom = new GradientPaint(x, y0 + (yC * rect.height), colorC, x, y0 + (yD * rect.height), colorD);
      Area bottomArea = AreaUtilities.createIntersection(prevClip, new Rectangle(x, yCenter, rect.width, y1 - yCenter));
      g2.setClip(bottomArea);
      g2.setPaint(paintBottom);
      g2.fill(rect);
    } finally {
      gc.popAll();
    }

  }

  public static void fillGradientRectangle(Graphics g, Rectangle rect, Color colorTop, Color colorInner, Color colorBottom, float portion) {
    fillGradientRectangle(g, rect, colorTop, 0.0f, colorInner, portion, colorInner, 1.0f - portion, colorBottom, 1.0f);
  }

  public static void paintIconCentered(Icon icon, Component c, Graphics g) {
    int x = (c.getWidth() - icon.getIconWidth()) / 2;
    int y = (c.getHeight() - icon.getIconHeight()) / 2;
    icon.paintIcon(c, g, x, y);
  }
}
