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
package org.lgna.croquet.views.imp;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.SwingComponentView;

import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public abstract class JProxy extends JPanel {
  private static BufferedImage image;

  private static BufferedImage getOffscreenImage(int width, int height) {
    if ((image == null) || (image.getWidth() != width) || (image.getHeight() != height)) {
      image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
      //image = getGraphicsConfiguration().createCompatibleImage( width, height, java.awt.Transparency.TRANSLUCENT );
    }
    return image;
  }

  private DragComponent<?> dragComponent;
  private boolean isOverDropAcceptor = false;
  private boolean isCopyDesired = false;

  public JProxy(DragComponent<?> dragComponent) {
    this.dragComponent = dragComponent;
    this.setOpaque(false);
  }

  protected DragComponent<?> getDragComponent() {
    return this.dragComponent;
  }

  protected SwingComponentView<?> getSubject() {
    return this.dragComponent.getSubject();
  }

  public Dimension getProxySize() {
    Component subject = this.getSubject().getAwtComponent();
    if (subject.getParent() != null) {
      //pass
    } else {
      ComponentUtilities.doLayoutTree(subject);
      ComponentUtilities.setSizeToPreferredSizeTree(subject);
    }
    return subject.getSize();
  }

  //  public int getProxyWidth() {
  //  return this.getSubject().getWidth();
  //  }
  //  public int getProxyHeight() {
  //  return this.getSubject().getHeight();
  //  }

  protected void fillBounds(Graphics2D g2) {
    SwingComponentView<?> subject = this.getSubject();
    int x = 0;
    int y = 0;
    int width = subject.getWidth();
    int height = subject.getHeight();
    g2.fillRect(x, y, width, height);
  }

  protected abstract void paintProxy(Graphics2D g2);

  protected abstract float getAlpha();

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Dimension size = this.getProxySize();
    if ((size.width > 0) && (size.height > 0)) {
      BufferedImage image = JProxy.getOffscreenImage(size.width, size.height);
      //todo: synchronize
      //if( LayeredPaneProxy.image == null || LayeredPaneProxy.image.getWidth() < width || LayeredPaneProxy.image.getHeight() < height ) {
      Graphics2D g2Image = (Graphics2D) image.getGraphics();
      g2Image.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      this.paintProxy(g2Image);
      g2Image.dispose();

      Graphics2D g2 = (Graphics2D) g;

      Composite prevComposite = g2.getComposite();

      //g2.setComposite( java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.CLEAR, 0.0f ) );
      //g2.clearRect( 0, 0, width, height );
      float alpha = this.getAlpha();
      if (alpha < 0.99f) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()));
      }
      //java.awt.Color bgColor = new java.awt.Color( 0, 0, 0, 0 );
      //g2.drawImage( LayeredPaneimage, 0, 0, width, height, 0, 0, width, height, bgColor, this );
      int availableHeight = this.getAvailableHeight();
      if ((availableHeight != -1) && (availableHeight < size.height)) {
        g2.drawImage(image, 0, 0, size.width, availableHeight, 0, 0, size.width, size.height, this);
      } else {
        g2.drawImage(image, 0, 0, size.width, size.height, this);
      }
      g2.dispose();
      g2.setComposite(prevComposite);
    }
  }

  public int getAvailableHeight() {
    return this.getProxySize().height;
  }

  public boolean isOverDropAcceptor() {
    return this.isOverDropAcceptor;
  }

  public void setOverDropAcceptor(boolean isOverDropAcceptor) {
    if (this.isOverDropAcceptor != isOverDropAcceptor) {
      this.isOverDropAcceptor = isOverDropAcceptor;
      this.repaint();
    }
  }

  public boolean isCopyDesired() {
    return this.isCopyDesired;
  }

  public void setCopyDesired(boolean isCopyDesired) {
    if (this.isCopyDesired != isCopyDesired) {
      this.isCopyDesired = isCopyDesired;
      this.repaint();
    }
  }

  public int getDropWidth() {
    return this.getWidth();
  }

  public int getDropHeight() {
    return this.getHeight();
  }
}
