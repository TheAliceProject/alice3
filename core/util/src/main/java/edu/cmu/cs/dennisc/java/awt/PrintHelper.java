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
package edu.cmu.cs.dennisc.java.awt;

import edu.cmu.cs.dennisc.java.awt.print.PageFormatUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * @author Dennis Cosgrove
 */
public class PrintHelper implements Printable {
  public static class Builder {
    private final Insets insets;
    private final Paint clearPaint;
    private Component lineStart;
    private Component lineEnd;
    private Component pageStart;
    private Component pageEnd;
    private Component center;

    public Builder(Insets insets, Paint clearPaint) {
      this.insets = insets;
      this.clearPaint = clearPaint;
    }

    public Builder lineStart(Component lineStart) {
      this.lineStart = lineStart;
      return this;
    }

    public Builder lineEnd(Component lineEnd) {
      this.lineEnd = lineEnd;
      return this;
    }

    public Builder pageStart(Component pageStart) {
      this.pageStart = pageStart;
      return this;
    }

    public Builder pageEnd(Component pageEnd) {
      this.pageEnd = pageEnd;
      return this;
    }

    public Builder center(Component center) {
      this.center = center;
      return this;
    }

    public PrintHelper build() {
      return new PrintHelper(this);
    }
  }

  private final Insets insets;
  private final Paint clearPaint;
  private final Component lineStart;
  private final Component lineEnd;
  private final Component pageStart;
  private final Component pageEnd;
  private final Component center;

  private static Component getViewportViewIfNecessary(Component component) {
    if (component instanceof JScrollPane) {
      JScrollPane jScrollPane = (JScrollPane) component;
      return jScrollPane.getViewport().getView();
    } else {
      return component;
    }
  }

  private static void printAll(Graphics2D g2, Component component) {
    if (component != null) {
      int x = component.getX();
      int y = component.getY();
      g2.translate(x, y);
      try {
        Shape prevClip = g2.getClip();
        try {
          Component c = getViewportViewIfNecessary(component);
          Dimension size = c.getPreferredSize();
          g2.setClip(0, 0, size.width, size.height);
          getViewportViewIfNecessary(component).printAll(g2);
        } finally {
          g2.setClip(prevClip);
        }
      } finally {
        g2.translate(-x, -y);
      }
    }
  }

  private PrintHelper(Builder builder) {
    this.insets = builder.insets;
    this.clearPaint = builder.clearPaint;
    this.lineStart = builder.lineStart;
    this.lineEnd = builder.lineEnd;
    this.pageStart = builder.pageStart;
    this.pageEnd = builder.pageEnd;
    this.center = builder.center;
  }

  @Override
  public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
    if (pageIndex > 0) {
      return Printable.NO_SUCH_PAGE;
    } else {
      Dimension size = getViewportViewIfNecessary(this.center).getPreferredSize();
      int width = size.width;
      int height = size.height;
      for (Component component : new Component[] {lineStart, lineEnd}) {
        if (component != null) {
          Dimension componentSize = component.getPreferredSize();
          width += componentSize.width;
          height = Math.max(height, componentSize.height);
        }
      }
      for (Component component : new Component[] {pageStart, pageEnd}) {
        if (component != null) {
          Dimension componentSize = component.getPreferredSize();
          width = Math.max(width, componentSize.width);
          height += componentSize.height;
        }
      }

      width += insets.left + insets.right;
      height += insets.top + insets.bottom;

      Graphics2D g2 = (Graphics2D) g;
      AffineTransform prevTransform = g2.getTransform();
      try {
        double scale = PageFormatUtilities.calculateScale(pageFormat, width, height);
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        if (scale > 1.0) {
          g2.scale(1.0 / scale, 1.0 / scale);
        }

        g2.setPaint(this.clearPaint);
        g2.fillRect(0, 0, width, height);

        printAll(g2, this.center);
        printAll(g2, this.pageStart);
        printAll(g2, this.lineStart);

        if (lineEnd != null) {
          Logger.todo(lineEnd);
        }

        if (pageEnd != null) {
          Logger.todo(pageEnd);
        }

      } finally {
        g2.setTransform(prevTransform);
      }
      return Printable.PAGE_EXISTS;
    }
  }

}
