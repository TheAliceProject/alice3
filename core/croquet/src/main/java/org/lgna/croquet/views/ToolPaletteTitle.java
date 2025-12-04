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
package org.lgna.croquet.views;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.javax.swing.icons.ArrowIcon;
import org.lgna.croquet.BooleanState;

import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * @author Dennis Cosgrove
 */
public class ToolPaletteTitle extends BooleanStateButton<javax.swing.AbstractButton> {

  private static final ArrowIcon ARROW_ICON = new ArrowIcon(12, true);

  private static final Insets SUPPRESSED_INSETS = new Insets(0, 0, 0, 0);
  private static final Insets INERT_INSETS = new Insets(2, 2, 2, 2);
  private static final Insets ACTIVE_INSETS = new Insets(2, 10 + ARROW_ICON.getIconWidth(), 2, 2);

  private enum ToolPaletteTitleBorder implements Border {
    SINGLETON;

    @Override
    public Insets getBorderInsets(Component c) {
      JToolPaletteTitle b = (JToolPaletteTitle) c;
      if (b.isSuppressed) {
        return SUPPRESSED_INSETS;
      } else {
        if (b.isInert) {
          return INERT_INSETS;
        } else {
          return ACTIVE_INSETS;
        }
      }
    }

    @Override
    public boolean isBorderOpaque() {
      return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      JToolPaletteTitle b = (JToolPaletteTitle) c;
      if (!b.isSuppressed() && b.isSeparatorShowing()) {
        g.setColor(ColorUtilities.scaleHSB(c.getBackground(), 1.0, 4.8, .74));
        g.fillRect(0, 0, width, 1);
      }
    }
  }

  private static class JToolPaletteTitle extends JToggleButton {
    private boolean separatorShowing = false;
    private boolean isInert = false;
    private boolean isSuppressed = false;

    @Override
    public boolean contains(int x, int y) {
      if (this.isInert) {
        return false;
      } else {
        return super.contains(x, y);
      }
    }

    public boolean isSeparatorShowing() {
      return this.separatorShowing;
    }

    public void setSeparatorShowing(boolean show) {
      if (this.separatorShowing != show) {
        this.separatorShowing = show;
        this.repaint();
      }
    }

    public boolean isInert() {
      return this.isInert;
    }

    public void setInert(boolean isInert) {
      if (this.isInert != isInert) {
        this.isInert = isInert;
        this.repaint();
      }
    }

    public boolean isSuppressed() {
      return this.isSuppressed;
    }

    public void setSuppressed(boolean isSuppressed) {
      if (this.isSuppressed != isSuppressed) {
        this.isSuppressed = isSuppressed;
        this.revalidate();
        this.repaint();
      }
    }

    @Override
    public boolean isOpaque() {
      return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      super.paintComponent(g);
      if (!this.isSuppressed && !this.isInert) {
        int x = 4;
        int height = this.getHeight();
        int iconHeight = ARROW_ICON.getIconHeight();
        int y = (height - iconHeight) / 2;
        ARROW_ICON.paintIcon(this, g2, x, y);
      }
    }

    @Override
    public void updateUI() {
      this.setUI(new ToolPaletteTitleButtonUI());
    }
  }

  private static class ToolPaletteTitleButtonUI extends BasicButtonUI {
    @Override
    public Dimension getMinimumSize(JComponent c) {
      JToolPaletteTitle b = (JToolPaletteTitle) c;
      if (b.isSuppressed) {
        return new Dimension(0, 0);
      } else {
        return super.getMinimumSize(c);
      }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
      JToolPaletteTitle b = (JToolPaletteTitle) c;
      if (b.isSuppressed) {
        return new Dimension(0, 0);
      } else {
        return super.getPreferredSize(c);
      }
    }

    @Override
    public Dimension getMaximumSize(JComponent c) {
      JToolPaletteTitle b = (JToolPaletteTitle) c;
      if (b.isSuppressed) {
        return new Dimension(0, 0);
      } else {
        return super.getMaximumSize(c);
      }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
      Graphics2D g2 = (Graphics2D) g;
      Shape prevClip = g2.getClip();
      try {
        JToolPaletteTitle b = (JToolPaletteTitle) c;
        if (!b.isSuppressed() && !b.isInert()) {
          Color background = c.getBackground();
          g2.setColor(background);
          g2.fillRect(0, 0, b.getWidth(), b.getHeight());
        }
        super.paint(g, c);
      } finally {
        g2.setClip(prevClip);
      }
    }
  }

  public ToolPaletteTitle(BooleanState booleanState) {
    super(booleanState);
  }

  public void setSeparatorShowing(boolean show) {
    this.getJPaletteTitle().setSeparatorShowing(show);
  }

  public void setInert(boolean isInert) {
    this.getJPaletteTitle().setInert(isInert);
  }

  public void setSuppressed(boolean isSuppressed) {
    this.getJPaletteTitle().setSuppressed(isSuppressed);
  }

  private JToolPaletteTitle getJPaletteTitle() {
    return (JToolPaletteTitle) this.getAwtComponent();
  }

  @Override
  protected javax.swing.AbstractButton createAwtComponent() {
    javax.swing.AbstractButton rv = new JToolPaletteTitle();
    rv.setRolloverEnabled(true);
    rv.setHorizontalAlignment(SwingConstants.LEADING);
    rv.setBorder(ToolPaletteTitleBorder.SINGLETON);
    rv.setOpaque(false);
    return rv;
  }
}
