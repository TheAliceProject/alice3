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
package org.alice.ide.recyclebin;

import edu.cmu.cs.dennisc.java.awt.ConsistentMouseDragEventQueue;
import org.alice.ide.icons.Icons;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;

/**
 * @author Dennis Cosgrove
 */
public enum RecycleBinIcon implements Icon {
  SINGLETON;

  private static final Icon[] ICONS = {Icons.TRASH_CAN_EMPTY_ICON, Icons.TRASH_CAN_FULL_ICON};

  @Override
  public int getIconWidth() {
    int rv = 0;
    for (Icon icon : ICONS) {
      rv = Math.max(rv, icon.getIconWidth());
    }
    return rv;
  }

  @Override
  public int getIconHeight() {
    int rv = 0;
    for (Icon icon : ICONS) {
      rv = Math.max(rv, icon.getIconHeight());
    }
    return rv;
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    Icons.TRASH_CAN_EMPTY_ICON.paintIcon(c, g, x, y);
    Paint paint;
    final int ALPHA = 15;
    if (ConsistentMouseDragEventQueue.getInstance().isDragActive()) {
      paint = new Color(255, 255, 0, ALPHA);
    } else {
      //paint = new java.awt.Color( 0, 255, 0, ALPHA );
      paint = null;
    }
    if (paint != null) {
      Graphics2D g2 = (Graphics2D) g;
      Paint prevPaint = g2.getPaint();
      g2.setPaint(paint);
      float xCenter = x + (this.getIconWidth() * 0.5f);
      float yCenter = y + (this.getIconHeight() * 0.5f);
      for (float radius = 4.0f; radius <= 16.0f; radius += 1.0f) {
        g2.fill(new Ellipse2D.Float(xCenter - radius, yCenter - radius, radius + radius, radius + radius));
      }
      g2.setPaint(prevPaint);
    }
  }
}
