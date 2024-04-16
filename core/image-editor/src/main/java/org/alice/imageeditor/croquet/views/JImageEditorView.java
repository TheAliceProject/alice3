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
package org.alice.imageeditor.croquet.views;

import edu.cmu.cs.dennisc.java.awt.ToolkitUtilities;
import edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.imageeditor.croquet.ImageEditorFrame;
import org.alice.imageeditor.croquet.Tool;
import org.alice.imageeditor.croquet.edits.AddShapeEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.MouseEventTrigger;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Dennis Cosgrove
 */
public class JImageEditorView extends JComponent {
  private static final int CLICK_THRESHOLD = 3;
  private static final Paint CROP_PAINT = new Color(0, 0, 0, 127);

  private static Shape createShape(Point a, Point b, double scale, Rectangle cropRectangle) {
    int x = Math.min(a.x, b.x);
    int y = Math.min(a.y, b.y);
    int width = Math.abs(b.x - a.x);
    int height = Math.abs(b.y - a.y);

    x -= 1;
    y -= 1;

    if ((width > CLICK_THRESHOLD) || (height > CLICK_THRESHOLD)) {
      double sx = x / scale;
      double sy = y / scale;
      double sw = width / scale;
      double sh = height / scale;
      if (cropRectangle != null) {
        sx += cropRectangle.x;
        sy += cropRectangle.y;
      }
      return new Rectangle2D.Double(sx, sy, sw, sh);
    } else {
      return null;
    }
  }

  private static final Stroke SHAPE_STROKE = new BasicStroke(8.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final Stroke OUTLINE_STROKE = new BasicStroke(0.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 8.0f, new float[] {8.0f}, 0.0f);
  private static final KeyStroke ESCAPE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
  private static final KeyStroke CLEAR_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK);

  private static final Stroke[] DROP_SHADOW_STROKES;

  static {
    final int N = 5;
    DROP_SHADOW_STROKES = new Stroke[N];
    for (int i = 0; i < N; i++) {
      DROP_SHADOW_STROKES[i] = new BasicStroke((i + 1) * 5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }
  }

  private static final Paint DROP_SHADOW_PAINT = new Color(127, 127, 127, 15);

  private static Point getClampedPoint(MouseEvent e) {
    Component awtComponent = e.getComponent();
    Point rv = e.getPoint();
    rv.x = Math.min(Math.max(1, e.getX()), awtComponent.getWidth() - 2);
    rv.y = Math.min(Math.max(1, e.getY()), awtComponent.getHeight() - 2);
    return rv;
  }

  private final MouseListener mouseListener = new MouseListener() {
    @Override
    public void mousePressed(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        ptPressed = getClampedPoint(e);
      } else {
        ptPressed = null;
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (ptPressed != null) {
        handleMouseReleased(e);
        ptPressed = null;
        ptDragged = null;
        repaint();
      }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
  };

  private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (ptPressed != null) {
        ptDragged = e.getPoint();
        repaint();
      }
    }
  };

  private final ActionListener escapeListener = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      ptPressed = null;
      ptDragged = null;
      repaint();
    }
  };

  private Point ptPressed;
  private Point ptDragged;

  private final ImageEditorFrame imageEditorFrame;

  public JImageEditorView(ImageEditorFrame imageEditorFrame) {
    this.imageEditorFrame = imageEditorFrame;
  }

  private void handleMouseReleased(final MouseEvent e) {
    Point p = getClampedPoint(e);
    Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();
    Shape shape = createShape(ptPressed, p, getScale(), crop);
    Tool tool = this.imageEditorFrame.getToolState().getValue();
    if (tool == Tool.ADD_RECTANGLE) {
      if (shape != null) {
        // NB This activity is not expected to have any child activities
        UserActivity activity = MouseEventTrigger.createUserActivity(e);
        AddShapeEdit edit = new AddShapeEdit(activity, shape, imageEditorFrame);
        activity.commitAndInvokeDo(edit);
      }
    } else if (tool == Tool.CROP_SELECT) {
      this.imageEditorFrame.getCropSelectHolder().setValue(shape != null ? shape.getBounds() : null);
    }
  }

  private void drawShapes(Graphics2D g2, AffineTransform mOriginal) {
    Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();

    Stroke prevStroke = g2.getStroke();
    Object prevAntialiasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (imageEditorFrame.getDropShadowState().getValue()) {
      final int DROP_SHADOW_OFFSET = 8;
      g2.translate(DROP_SHADOW_OFFSET, DROP_SHADOW_OFFSET);
      g2.setPaint(DROP_SHADOW_PAINT);
      for (Stroke stroke : DROP_SHADOW_STROKES) {
        g2.setStroke(stroke);
        for (Shape shape : imageEditorFrame.getShapes()) {
          g2.draw(shape);
        }
      }
      g2.translate(-DROP_SHADOW_OFFSET, -DROP_SHADOW_OFFSET);
    }

    g2.setPaint(Color.RED);
    g2.setStroke(SHAPE_STROKE);
    for (Shape shape : imageEditorFrame.getShapes()) {
      g2.draw(shape);
    }

    Shape cropShape = null;

    boolean isInTheMidstOfDragging = (ptPressed != null) && (ptDragged != null);
    Tool tool = this.imageEditorFrame.getToolState().getValue();
    if (isInTheMidstOfDragging) {
      Shape shape = createShape(ptPressed, ptDragged, this.getScale(), crop);
      if (shape != null) {
        if (tool == Tool.ADD_RECTANGLE) {
          g2.draw(shape);
        } else if (tool == Tool.CROP_SELECT) {
          cropShape = shape;
        }
      }
    }

    Rectangle selection = this.imageEditorFrame.getCropSelectHolder().getValue();
    if (selection != null) {
      if (cropShape == null) {
        cropShape = selection;
      }
    }

    if (cropShape != null) {
      try {
        AffineTransform m = g2.getTransform();
        Point2D.Double ptA = new Point2D.Double(0, 0);
        Point2D.Double ptB = new Point2D.Double(this.getWidth(), this.getHeight());
        mOriginal.transform(ptA, ptA);
        mOriginal.transform(ptB, ptB);
        m.inverseTransform(ptA, ptA);
        m.inverseTransform(ptB, ptB);

        Shape bounds = new Rectangle2D.Double(ptA.getX(), ptA.getY(), ptB.getX() - ptA.getX(), ptB.getY() - ptA.getY());
        Area area = AreaUtilities.createSubtraction(bounds, cropShape);
        g2.setPaint(CROP_PAINT);
        g2.fill(area);
      } catch (NoninvertibleTransformException nte) {
        throw new RuntimeException(nte);
      }
    }

    g2.setStroke(prevStroke);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, prevAntialiasing == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : prevAntialiasing);
  }

  private int getImageResolution() {
    //edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: getImageResolution()" );
    return 300;
  }

  private int getScreenResolution() {
    return ToolkitUtilities.getScreenResolution(this);
  }

  private double getScale() {
    if (imageEditorFrame.getShowInScreenResolutionState().getValue()) {
      return this.getScreenResolution() / (double) this.getImageResolution();
    } else {
      return 1.0;
    }
  }

  private int scaledImageWidth = -1;
  private int scaledImageHeight = -1;
  private Image scaledImage = null;

  private Image getScaledImage(Image image, int width, int height) {
    if ((width != this.scaledImageWidth) || (height != this.scaledImageHeight)) {
      this.scaledImage = null;
    }
    if (this.scaledImage == null) {
      this.scaledImageWidth = width;
      this.scaledImageHeight = height;
      this.scaledImage = image.getScaledInstance(this.scaledImageWidth, this.scaledImageHeight, Image.SCALE_SMOOTH);
    }
    return this.scaledImage;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    Image image = imageEditorFrame.getImageHolder().getValue();

    if (image != null) {
      AffineTransform m = g2.getTransform();
      g2.translate(1, 1);
      Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();
      double scale = this.getScale();
      if (scale != 1.0) {
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);
        int scaledImageWidth = (int) Math.ceil(imageWidth * scale);
        int scaledImageHeight = (int) Math.ceil(imageHeight * scale);
        if (crop != null) {
          g2.scale(scale, scale);
          if (crop != null) {
            g2.translate(-crop.x, -crop.y);
          }
          g2.drawImage(image, 0, 0, this);
        } else {
          g2.drawImage(this.getScaledImage(image, scaledImageWidth, scaledImageHeight), 0, 0, this);
          g2.scale(scale, scale);
        }
      } else {
        if (crop != null) {
          g2.translate(-crop.x, -crop.y);
        }
        g2.drawImage(image, 0, 0, this);
      }

      this.drawShapes(g2, m);
      g2.setTransform(m);
    }
    if (imageEditorFrame.getShowDashedBorderState().getValue()) {
      Stroke prevStroke = g2.getStroke();
      g2.setColor(Color.DARK_GRAY);
      g2.setStroke(OUTLINE_STROKE);
      g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
      g2.setStroke(prevStroke);
    }
  }

  /* package-private */void render(Graphics2D g2) {
    Image image = imageEditorFrame.getImageHolder().getValue();
    if (image != null) {
      Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();
      if (crop != null) {
        g2.translate(-crop.x, -crop.y);
      }
      g2.drawImage(image, 0, 0, this);
      this.drawShapes(g2, g2.getTransform());
    } else {
      Logger.severe(this);
    }
  }

  @Override
  public Dimension getMinimumSize() {
    return this.getPreferredSize();
  }

  @Override
  public Dimension getPreferredSize() {
    Image image = imageEditorFrame.getImageHolder().getValue();
    if (image != null) {
      Rectangle crop = this.imageEditorFrame.getCropCommitHolder().getValue();
      int srcWidth = image.getWidth(this);
      int srcHeight = image.getHeight(this);
      int width;
      int height;
      if (crop != null) {
        srcWidth = crop.width;
        srcHeight = crop.height;
      }
      if (imageEditorFrame.getShowInScreenResolutionState().getValue()) {
        double scale = this.getScale();
        width = (int) Math.ceil(srcWidth * scale);
        height = (int) Math.ceil(srcHeight * scale);
      } else {
        width = srcWidth;
        height = srcHeight;
      }
      return new Dimension(width + 2, height + 2);
    } else {
      return super.getPreferredSize();
    }
  }

  @Override
  public void addNotify() {
    super.addNotify();
    this.addMouseListener(this.mouseListener);
    this.addMouseMotionListener(this.mouseMotionListener);
    this.registerKeyboardAction(this.escapeListener, ESCAPE_KEY_STROKE, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  @Override
  public void removeNotify() {
    this.unregisterKeyboardAction(CLEAR_KEY_STROKE);
    this.unregisterKeyboardAction(ESCAPE_KEY_STROKE);
    this.removeMouseMotionListener(this.mouseMotionListener);
    this.removeMouseListener(this.mouseListener);
    super.removeNotify();
  }
}
