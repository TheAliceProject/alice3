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
import com.jogamp.opengl.awt.GLJPanel;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.ProxyGraphics2D;
import edu.cmu.cs.dennisc.render.LightweightOnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.RenderCapabilities;

import javax.swing.GrayFilter;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class GlrLightweightOnscreenRenderTarget extends GlrOnscreenRenderTarget<JPanel> implements LightweightOnscreenRenderTarget {
  private static final boolean IS_IMAGE_TRACKING_READY_FOR_PRIME_TIME = false;

  private class RenderPane extends GLJPanel {
    private Image mostRecentDrawnImage;
    private Image disabledImage;

    private class ImageTrackingProxyGraphics extends ProxyGraphics2D {
      @Override
      public Graphics create() {
        ImageTrackingProxyGraphics rv = new ImageTrackingProxyGraphics();
        rv.setOther((Graphics2D) super.create());
        return rv;
      }

      @Override
      public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        mostRecentDrawnImage = img;
        return super.drawImage(img, x, y, width, height, observer);
      }
    }

    public RenderPane(RenderCapabilities requestedCapabilities) {
      super(GlDrawableUtils.createGlCapabilitiesForLightweightComponent(requestedCapabilities), GlDrawableUtils.getPerhapsMultisampledGlCapabilitiesChooser());
    }

    @Override
    public void display() {
      if (GlrLightweightOnscreenRenderTarget.this.isRenderingEnabled()) {
        super.display();
      }
    }

    @Override
    protected void paintComponent(Graphics g) {
      if (GlrLightweightOnscreenRenderTarget.this.isRenderingEnabled()) {
        if (GlrLightweightOnscreenRenderTarget.this.getSgCameraCount() > 0) {
          try {
            super.paintComponent(g);
            this.prevThrowable = null;
          } catch (Throwable throwable) {
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            GraphicsUtilities.drawCenteredText(g, "error in attempting to render scene", this.getSize());
            //edu.cmu.cs.dennisc.awt.GraphicsUtilities.drawCenteredText( g, t.getClass().getSimpleName() + " in attempting to render scene", this.getSize() );
            if (this.prevThrowable != null) {
              //pass
            } else {
              this.prevThrowable = throwable;
              throwable.printStackTrace();
            }
          }
        } else {
          g.setColor(Color.DARK_GRAY);
          g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
      }
    }

    @Override
    public void paint(Graphics g) {
      if (GlrLightweightOnscreenRenderTarget.this.isRenderingEnabled()) {
        if (imageTrackingProxyGraphics != null) {
          Graphics2D g2 = (Graphics2D) g;
          imageTrackingProxyGraphics.setOther(g2);
          mostRecentDrawnImage = null;
          disabledImage = null;
          super.paint(imageTrackingProxyGraphics);
          imageTrackingProxyGraphics.setOther(null);
        } else {
          super.paint(g);
        }
      } else {
        if (disabledImage != null) {
          //pass
        } else {
          if (mostRecentDrawnImage != null) {
            disabledImage = GrayFilter.createDisabledImage(mostRecentDrawnImage);
          }
        }
        Dimension size = this.getSize();
        if (disabledImage != null) {
          g.drawImage(disabledImage, 0, 0, this);
        } else {
          g.setColor(Color.GRAY);
          g.fillRect(0, 0, size.width, size.height);
        }
        String text = "rendering disabled for performance considerations";
        g.setColor(Color.BLACK);
        GraphicsUtilities.drawCenteredText(g, text, size);
        g.setColor(Color.YELLOW);
        g.translate(-1, -1);
        GraphicsUtilities.drawCenteredText(g, text, size);
        g.translate(1, 1);
      }
    }

    private final ImageTrackingProxyGraphics imageTrackingProxyGraphics = IS_IMAGE_TRACKING_READY_FOR_PRIME_TIME ? new ImageTrackingProxyGraphics() : null;
    private Throwable prevThrowable = null;
  }

  /* package-private */ GlrLightweightOnscreenRenderTarget(GlrRenderFactory lookingGlassFactory, RenderCapabilities requestedCapabilities) {
    super(lookingGlassFactory, requestedCapabilities);
    this.glPanel = new RenderPane(requestedCapabilities);
    this.glPanel.setFocusable(true);
  }

  @Override
  public JPanel getAwtComponent() {
    return this.glPanel;
  }

  @Override
  protected Dimension getSurfaceSize(Dimension rv) {
    return this.glPanel.getSize(rv);
  }

  @Override
  protected Dimension getDrawableSize(Dimension rv) {
    return this.glPanel.getSize(rv);
  }

  @Override
  public void repaint() {
    this.glPanel.repaint();
  }

  @Override
  public GLAutoDrawable getGLAutoDrawable() {
    return this.glPanel;
  }

  private final GLJPanel glPanel;
}
