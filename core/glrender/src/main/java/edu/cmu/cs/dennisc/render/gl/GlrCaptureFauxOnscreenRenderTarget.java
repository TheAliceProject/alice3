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
package edu.cmu.cs.dennisc.render.gl;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.RenderCapabilities;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class GlrCaptureFauxOnscreenRenderTarget extends GlrRenderTarget implements OnscreenRenderTarget<JPanel> {
  public static interface Observer {
    public void handleImage(BufferedImage image, boolean isUpSideDown);
  }

  private class JRecordPanel extends JPanel {
    public JRecordPanel() {
      this.setBackground(Color.BLACK);
    }

    @Override
    public void paint(Graphics g) {
      if (image != null) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform m = g2.getTransform();
        if (atIsUpSideDown[0]) {
          g2.translate(0, this.getHeight());
          g2.scale(1.0, -1.0);
        }
        try {
          int imageWidth = image.getWidth();
          int imageHeight = image.getHeight();

          Dimension componentSize = this.getSize();

          if ((imageWidth == componentSize.width) || (imageHeight == componentSize.height)) {
            g.drawImage(image, 0, 0, this);
          } else {
            Dimension imageSize = new Dimension(imageWidth, imageHeight);
            Dimension size = DimensionUtilities.calculateBestFittingSize(componentSize, imageSize.width / (double) imageSize.height);

            int x0 = (componentSize.width - size.width) / 2;
            int x1 = x0 + size.width;
            int y0 = (componentSize.height - size.height) / 2;
            int y1 = y0 + size.height;

            super.paint(g);
            g.drawImage(image, x0, y0, x1, y1, 0, 0, imageWidth, imageHeight, this);
          }
        } finally {
          g2.setTransform(m);
        }
      } else {
        super.paint(g);
      }
    }
  }

  private final Dimension size;

  private final JRecordPanel jPanel = new JRecordPanel();

  private final GLOffscreenAutoDrawable glPixelBuffer;

  private boolean[] atIsUpSideDown = {false};
  private BufferedImage image;

  public GlrCaptureFauxOnscreenRenderTarget(Dimension size, GlrRenderTarget renderTargetToShareContextWith, RenderCapabilities requestedCapabilities) {
    super(GlrRenderFactory.getInstance(), requestedCapabilities);
    this.size = size;
    this.glPixelBuffer = GlDrawableUtils.createGlPixelBuffer(GlDrawableUtils.createGlCapabilities(requestedCapabilities), GlDrawableUtils.getPerhapsMultisampledGlCapabilitiesChooser(), size.width, size.height, GlDrawableUtils.getGlContextToShare(renderTargetToShareContextWith));
  }

  @Override
  protected Dimension getSurfaceSize(Dimension rv) {
    rv.setSize(this.size);
    return rv;
  }

  @Override
  protected Dimension getDrawableSize(Dimension rv) {
    //Drawable size and surface size are the same for this render target
    rv.setSize(this.size);
    return rv;
  }

  @Override
  public GLAutoDrawable getGLAutoDrawable() {
    return this.glPixelBuffer;
  }

  public JPanel getJPanel() {
    return this.jPanel;
  }

  public BufferedImage getImage() {
    return this.image;
  }

  public void captureImage(Observer observer) {
    if (this.image != null) {
      //pass
    } else {
      this.image = this.getSynchronousImageCapturer().createBufferedImageForUseAsColorBuffer();
    }
    this.getSynchronousImageCapturer().getColorBufferNotBotheringToFlipVertically(this.image, this.atIsUpSideDown);
    observer.handleImage(this.image, this.atIsUpSideDown[0]);
    this.jPanel.repaint();
  }

  @Override
  protected void repaintIfAppropriate() {
  }

  @Override
  public JPanel getAwtComponent() {
    return this.jPanel;
  }

  @Override
  public void repaint() {
  }
}
