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
package org.alice.stageide.icons;

import edu.cmu.cs.dennisc.math.GoldenRatio;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * @author Dennis Cosgrove
 */
public class BillboardIcon extends ShapeIcon {
  public BillboardIcon(Dimension size) {
    super(size);
  }

  @Override
  protected void paintIcon(Component c, Graphics2D g2, int width, int height, Paint fillPaint, Paint drawPaint) {
    float h = width / (float) GoldenRatio.PHI;
    float x = 0.0f;
    float y = (height - h) * 0.5f;
    Shape outerShape = new Rectangle2D.Float(x, y, width, h);
    g2.setPaint(fillPaint);
    g2.fill(outerShape);
    g2.setPaint(drawPaint);
    g2.draw(outerShape);

    float offset;
    if (width > 64) {
      offset = 0.05f * width;
    } else {
      offset = 0.1f * width;
    }
    Rectangle2D.Float innerShape = new Rectangle2D.Float(x + offset, y + offset, width - (offset * 2), h - (offset * 2));

    Paint innerFillPaint;
    if (fillPaint instanceof Color) {
      Color fillColor = (Color) fillPaint;
      innerFillPaint = new GradientPaint((float) innerShape.getMinX(), (float) innerShape.getMinY(), fillColor.brighter(), (float) innerShape.getCenterX(), (float) innerShape.getMaxY(), fillColor);
    } else {
      innerFillPaint = fillPaint;
    }

    g2.setPaint(innerFillPaint);
    g2.fill(innerShape);
    g2.setPaint(Color.DARK_GRAY);
    g2.draw(innerShape);

  }
}
