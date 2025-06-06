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

package org.lgna.story;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.color.property.Color4fProperty;
import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import org.lgna.project.annotations.ValueTemplate;
import org.lgna.story.annotation.PortionDetails;

/**
 * @author Dennis Cosgrove
 */
public final class Color implements Paint {
  public static final Color BLACK = new Color(Color4f.BLACK);
  public static final Color BLUE = new Color(Color4f.BLUE);
  public static final Color CYAN = new Color(Color4f.CYAN);
  public static final Color DARK_GRAY = new Color(Color4f.DARK_GRAY);
  public static final Color GRAY = new Color(Color4f.GRAY);
  public static final Color GREEN = new Color(Color4f.GREEN);
  public static final Color LIGHT_GRAY = new Color(Color4f.LIGHT_GRAY);
  public static final Color MAGENTA = new Color(Color4f.MAGENTA);
  public static final Color ORANGE = new Color(Color4f.ORANGE);
  public static final Color PINK = new Color(Color4f.PINK);
  public static final Color RED = new Color(Color4f.RED);
  public static final Color WHITE = new Color(Color4f.WHITE);
  public static final Color YELLOW = new Color(Color4f.YELLOW);
  public static final Color LIGHT_BLUE = new Color(149.0 / 255.0, 166.0 / 255.0, 216.0 / 255.0);
  public static final Color DARK_BLUE = new Color(0 / 255.0, 0 / 255.0, 150.0 / 255.0);

  public static final Color PURPLE = new Color(Color4f.PURPLE);
  public static final Color BROWN = new Color(Color4f.BROWN);

  private final Color4f internal;

  public Color(@ValueTemplate(detailsEnumCls = PortionDetails.class) Number red, @ValueTemplate(detailsEnumCls = PortionDetails.class) Number green, @ValueTemplate(detailsEnumCls = PortionDetails.class) Number blue) {
    this(new Color4f(red.floatValue(), green.floatValue(), blue.floatValue(), 1.0f));
  }

  public static Color fromProperty(Color4fProperty colorProp) {
    return createInstance(colorProp.getValue());
  }

  public Color interpolateTo(Color target, double portion) {
    return createInstance(Color4f.createInterpolation(toColor4f(), target.toColor4f(), (float) portion));
  }

  private Color(Color4f internal) {
    this.internal = internal;
  }

  private static Color createInstance(Color4f internal) {
    return internal != null ? new Color(internal) : null;
  }

  public static Color createInstance(java.awt.Color color) {
    return color != null ? new Color(Color4f.createInstance(color)) : null;
  }

  public Color4f toColor4f() {
    return this.internal;
  }

  public java.awt.Color toAwtColor() {
    return ColorUtilities.toAwtColor(internal);
  }

  public static Color4f getColor4fOrWhite(Paint paint) {
      return paint instanceof Color
              ? ((Color) paint).toColor4f()
              : Color4f.WHITE;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Color) {
      Color other = (Color) obj;
      return this.internal.equals(other.internal);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.internal.hashCode();
  }

  public Double getRed() {
    return (double) this.internal.red;
  }

  public Double getGreen() {
    return (double) this.internal.green;
  }

  public Double getBlue() {
    return (double) this.internal.blue;
  }

  public String rgbString() {
    return "Color(" + internal.red + "," + internal.green + "," + internal.blue + ")";
  }

  public java.awt.Color applyTo(java.awt.Color color) {
    float[] f = new float[4];
    color.getRGBComponents(f);
    return new java.awt.Color(
        internal.red * f[0],
        internal.green * f[1],
        internal.blue * f[2],
        f[3]);
  }
}
