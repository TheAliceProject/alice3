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
package org.alice.ide.icons;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * These are icons that show up on the scene tab and in the dropdown menu that shows up to the left of the scene tab
 * * @author Dennis Cosgrove
 */
public class TabIcon extends ShapeIcon {
  private static final Stroke STROKE = new BasicStroke(0.0f);
  private final Paint fillPaint;
  private final GeneralPath shape = new GeneralPath();

  public TabIcon(Dimension size, Color fillColor) {
    super(size);
    this.fillPaint = fillColor;

    float a = 0.1f;
    float b = 0.3f;
    float d = b + a;
    float f = d + 0.2f;
    float g = 0.2f;

    float w = size.width - (PAD * 2);
    float h = size.height - (PAD * 2);

    shape.moveTo(0.0f, h);
    shape.lineTo(0.0f, a * h);
    shape.quadTo(0.0f, 0.0f, a * w, 0.0f);
    shape.lineTo(b * w, 0.0f);
    shape.quadTo(d * w, 0.0f, d * w, a * h);
    shape.quadTo(d * w, g * h, f * w, g * h);
    shape.lineTo(w, g * h);
    shape.lineTo(w, h);
    shape.closePath();
  }

  @Override
  protected void paintIcon(Component c, Graphics2D g2, int width, int height, Paint fillPaint, Paint drawPaint) {
    boolean isArmed;
    if (c instanceof AbstractButton button) {
      ButtonModel buttonModel = button.getModel();
      isArmed = buttonModel.isArmed();
    } else {
      isArmed = false;
    }
    g2.setPaint(this.fillPaint);
    g2.fill(shape);
    if (!isArmed) {
      Stroke prevStroke = g2.getStroke();
      g2.setStroke(STROKE);
      g2.setPaint(Color.DARK_GRAY);
      g2.draw(shape);
      g2.setStroke(prevStroke);
    }
  }
}
