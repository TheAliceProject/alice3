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
package org.alice.ide.croquet.components;

import edu.cmu.cs.dennisc.java.awt.print.PageFormatUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.Composite;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.ScrollPane;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 * @author Dennis Cosgrove
 */
public class PrintableBorderPanelWithCenterScrollPane extends BorderPanel implements Printable {
  public PrintableBorderPanelWithCenterScrollPane(Composite<?> composite) {
    super(composite);
    this.addCenterComponent(new ScrollPane());
  }

  public PrintableBorderPanelWithCenterScrollPane() {
    this(null);
  }

  public ScrollPane getScrollPane() {
    return (ScrollPane) this.getCenterComponent();
  }

  @Override
  public void addComponent(AwtComponentView<?> child, BorderPanel.Constraint constraint) {
    if (constraint == Constraint.CENTER) {
      assert child instanceof ScrollPane : child;
    }
    super.addComponent(child, constraint);
  }

  @Override
  public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
    if (pageIndex > 0) {
      return NO_SUCH_PAGE;
    } else {
      ScrollPane scrollPane = this.getScrollPane();
      AwtComponentView<?> lineStart = this.getLineStartComponent();
      AwtComponentView<?> lineEnd = this.getLineEndComponent();
      AwtComponentView<?> pageStart = this.getPageStartComponent();
      AwtComponentView<?> pageEnd = this.getPageEndComponent();

      //todo: this code will not suffice in the limit
      Dimension scrollSize = scrollPane.getViewportView().getAwtComponent().getPreferredSize();
      int width = scrollSize.width;
      int height = scrollSize.height;
      for (AwtComponentView<?> component : new AwtComponentView[] {lineStart, lineEnd}) {
        if (component != null) {
          Dimension componentSize = component.getAwtComponent().getPreferredSize();
          width += componentSize.width;
          height = Math.max(height, componentSize.height);
        }
      }
      for (AwtComponentView<?> component : new AwtComponentView[] {pageStart, pageEnd}) {
        if (component != null) {
          Dimension componentSize = component.getAwtComponent().getPreferredSize();
          width = Math.max(width, componentSize.width);
          height += componentSize.height;
        }
      }

      Insets insets = this.getInsets();
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

        g2.setPaint(this.getBackgroundColor());
        g2.fillRect(0, 0, width, height);

        if (pageStart != null) {
          int x = pageStart.getX();
          int y = pageStart.getY();
          g2.translate(x, y);
          try {
            pageStart.getAwtComponent().printAll(g2);
          } finally {
            g2.translate(-x, -y);
          }
        }
        if (lineStart != null) {
          int x = lineStart.getX();
          int y = lineStart.getY();
          g2.translate(x, y);
          try {
            lineStart.getAwtComponent().printAll(g2);
          } finally {
            g2.translate(-x, -y);
          }
        }
        int x = scrollPane.getX();
        int y = scrollPane.getY();
        g2.translate(x, y);
        try {
          scrollPane.getViewportView().getAwtComponent().printAll(g2);
        } finally {
          g2.translate(-x, -y);
        }

        if (lineEnd != null) {
          Logger.todo(lineEnd);
        }

        if (pageEnd != null) {
          Logger.todo(pageEnd);
        }

      } finally {
        g2.setTransform(prevTransform);
      }
      return PAGE_EXISTS;
    }
  }
}
