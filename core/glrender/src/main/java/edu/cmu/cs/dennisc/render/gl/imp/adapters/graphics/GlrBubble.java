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
package edu.cmu.cs.dennisc.render.gl.imp.adapters.graphics;

import edu.cmu.cs.dennisc.java.awt.MultilineText;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.Graphics2D;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.graphics.Bubble;
import edu.cmu.cs.dennisc.scenegraph.graphics.BubbleManager;
import edu.cmu.cs.dennisc.scenegraph.graphics.OnscreenBubble;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

public abstract class GlrBubble<T extends Bubble> extends GlrShapeEnclosedText<T> {
  private Point2D.Float originOfTail = new Point2D.Float();
  private Point2D.Float bodyConnectionLocationOfTail = new Point2D.Float();
  private Point2D.Float textBoundsOffset = new Point2D.Float();

  @Override
  protected float getWrapWidth(Rectangle actualViewport) {
    return (float) (actualViewport.getWidth() * 0.9);
  }

  protected abstract void render(Graphics2D g2, RenderTarget renderTarget, Rectangle actualViewport, AbstractCamera camera, MultilineText multilineText, Font font, Color textColor, float wrapWidth, Color fillColor, Color outlineColor, OnscreenBubble bubble, double portion);

  @Override
  protected void render(Graphics2D g2, RenderTarget renderTarget, Rectangle actualViewport, AbstractCamera camera, MultilineText multilineText, Font font, Color textColor, float wrapWidth, Color fillColor, Color outlineColor) {
    Bubble.Originator originator = this.owner.getOriginator();
    if (originator != null) {

      //Scale the font size to try to match the viewport
      double DEFAULT_WIDTH = 640;
      double DEFAULT_HEIGHT = 360;
      double viewportRatio = actualViewport.getWidth() / actualViewport.getHeight();
      float scaleFactor = 1.0f;
      if (viewportRatio >= SymmetricPerspectiveCamera.DEFAULT_WIDTH_TO_HEIGHT_RATIO) {
        scaleFactor = (float) (actualViewport.getHeight() / DEFAULT_HEIGHT);
      } else {
        scaleFactor = (float) (actualViewport.getWidth() / DEFAULT_WIDTH);
      }
      Font scaledFont = font.deriveFont(font.getSize2D() * scaleFactor);
      g2.setFont(scaledFont);
      Dimension2D size = multilineText.getDimension(g2, wrapWidth);
      originator.calculate(originOfTail, bodyConnectionLocationOfTail, textBoundsOffset, this.owner, renderTarget, actualViewport, camera, size);
      OnscreenBubble bubble = BubbleManager.getInstance().getBubble(this.owner);
      if (bubble == null) {
        float padding;
        if (this instanceof GlrThoughtBubble) {
          padding = font.getSize2D() * 1.2f;
        } else {
          padding = font.getSize2D() * .4f;
        }
        bubble = BubbleManager.getInstance().addBubble(this.owner, originOfTail, size, padding, scaleFactor, actualViewport);
      } else {
        bubble.updateOriginOfTail(originOfTail, actualViewport);
      }

      this.render(g2, renderTarget, actualViewport, camera, multilineText, scaledFont, textColor, wrapWidth, fillColor, outlineColor, bubble, owner.portion.getValue());
    }
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.portion) {
      //pass
    } else {
      super.propertyChanged(property);
    }
  }
}
