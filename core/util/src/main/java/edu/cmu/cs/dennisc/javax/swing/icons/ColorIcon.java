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

package edu.cmu.cs.dennisc.javax.swing.icons;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

public class ColorIcon implements Icon {
  public static final int DEFAULT_SIZE = 16;
  private Color fillColor;
  private Color outlineColor;
  private int width;
  private int height;

  public ColorIcon(Color color) {
    this(color, DEFAULT_SIZE);
  }

  public ColorIcon(Color color, int size) {
    this(color, size, size);
  }

  public ColorIcon(Color color, Dimension size) {
    this(color, size.width, size.height);
  }

  public ColorIcon(Color color, int width, int height) {
    this.fillColor = color;
    this.width = width;
    this.height = height;
    float[] hsb = new float[3];
    //todo
    Color.RGBtoHSB(this.fillColor.getRed(), this.fillColor.getGreen(), this.fillColor.getBlue(), hsb);
    if (hsb[2] > 0.9f) {
      this.outlineColor = Color.GRAY;
    } else {
      this.outlineColor = null;
    }
  }

  public Color getFillColor() {
    return this.fillColor;
  }

  @Override
  public int getIconWidth() {
    return this.width;
  }

  @Override
  public int getIconHeight() {
    return this.height;
  }

  @Override
  public void paintIcon(Component arg0, Graphics g, int x, int y) {
    g.setColor(this.fillColor);
    g.fillRect(x, y, this.width, this.height);
    if (this.outlineColor != null) {
      g.setColor(this.outlineColor);
      g.drawRect(x, y, this.width, this.height);
    }
  }

}
