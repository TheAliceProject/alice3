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
package org.alice.ide.common;

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.Formatter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.StaticAnalysisUtilities;
import org.lgna.project.ast.UserMethod;

import javax.swing.Icon;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * @author Dennis Cosgrove
 */
public class TypeIcon implements Icon {
  private static final int INDENT_PER_DEPTH = 12;
  private static final int BONUS_GAP = 4;
  private final AbstractType<?, ?, ?> type;
  private final TypeBorder border;
  private final boolean isIndentForDepthAndMemberCountTextDesired;
  private final Font typeFont;
  private final Font bonusFont;

  public static TypeIcon getInstance(AbstractType<?, ?, ?> type) {
    return new TypeIcon(type);
  }

  public TypeIcon(AbstractType<?, ?, ?> type, boolean isIndentForDepthAndMemberCountTextDesired, Font typeFont, Font bonusFont) {
    this.type = type;
    this.border = TypeBorder.getSingletonFor(type);
    this.isIndentForDepthAndMemberCountTextDesired = isIndentForDepthAndMemberCountTextDesired;
    this.typeFont = typeFont;
    this.bonusFont = bonusFont;
  }

  public TypeIcon(AbstractType<?, ?, ?> type) {
    this(type, false, UIManager.getFont("defaultFont"), null);
  }

  protected Font getTypeFont() {
    return this.typeFont;
  }

  public Font getBonusFont() {
    return this.bonusFont;
  }

  protected Color getTextColor(Component c) {
    if (c.isEnabled()) {
      return Color.BLACK;
    } else {
      return Color.GRAY;
    }
  }

  private String getTypeText() {
    Formatter formatter = FormatterState.getInstance().getValue();
    return formatter.getTextForType(this.type);
  }

  private String getBonusText() {
    if (isIndentForDepthAndMemberCountTextDesired) {
      if (this.type instanceof NamedUserType) {
        NamedUserType userType = (NamedUserType) this.type;
        int count = 0;
        for (UserMethod method : userType.methods) {
          if (method.getManagementLevel() == ManagementLevel.NONE) {
            count += 1;
          }
        }
        count += userType.fields.size();
        if (count > 0) {
          StringBuilder sb = new StringBuilder();
          sb.append("(");
          sb.append(count);
          sb.append(")");
          return sb.toString();
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  private static Rectangle2D getTextBounds(String text, Font font) {
    if (text != null) {
      Graphics g = GraphicsUtilities.getGraphics();
      FontMetrics fm;
      if (font != null) {
        fm = g.getFontMetrics(font);
      } else {
        fm = g.getFontMetrics();
      }
      return fm.getStringBounds(text, g);
    } else {
      return new Rectangle2D.Float(0, 0, 0, 0);
    }
  }

  private Rectangle2D getTypeTextBounds() {
    return getTextBounds(this.getTypeText(), this.getTypeFont());
  }

  private Rectangle2D getBonusTextBounds() {
    return getTextBounds(this.getBonusText(), this.getBonusFont());
  }

  private int getBorderWidth() {
    Insets insets = this.border.getBorderInsets(null);
    Rectangle2D typeTextBounds = this.getTypeTextBounds();
    return insets.left + insets.right + (int) typeTextBounds.getWidth();
  }

  private int getBorderHeight() {
    Insets insets = this.border.getBorderInsets(null);
    Rectangle2D bounds = this.getTypeTextBounds();
    return insets.top + insets.bottom + (int) bounds.getHeight();
  }

  @Override
  public int getIconWidth() {
    int rv = this.getBorderWidth();
    if (this.isIndentForDepthAndMemberCountTextDesired) {
      int depth = StaticAnalysisUtilities.getUserTypeDepth(type);
      if (depth > 0) {
        rv += (depth * INDENT_PER_DEPTH);
      }
    }
    if (this.isIndentForDepthAndMemberCountTextDesired) {
      rv += BONUS_GAP;
      Rectangle2D bonusTextBounds = this.getBonusTextBounds();
      rv += (int) bonusTextBounds.getWidth();
    }
    return rv;
  }

  @Override
  public int getIconHeight() {
    return this.getBorderHeight();
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    AffineTransform prevTransform = g2.getTransform();

    //g.setColor( java.awt.Color.BLUE );
    //g.fillRect( x, y, this.getIconWidth(), this.getIconHeight() );

    int typePlusBonusWidth = this.getIconWidth();
    if (this.isIndentForDepthAndMemberCountTextDesired) {
      int depth = StaticAnalysisUtilities.getUserTypeDepth(type);
      if (depth > 0) {
        int dx = depth * INDENT_PER_DEPTH;
        g2.translate(dx, 0);
        typePlusBonusWidth -= dx;
        typePlusBonusWidth -= BONUS_GAP;
      }
    }

    int w = this.getBorderWidth();
    int h = this.getBorderHeight();

    //g.setColor( java.awt.Color.GREEN );
    //g.fillRect( x, y, typePlusBonusWidth, this.getIconHeight() );

    //g.setColor( java.awt.Color.RED );
    //g.fillRect( x, y, w, h );
    this.border.paintBorder(c, g, x, y, w, h);
    g.setColor(this.getTextColor(c));

    Font prevFont = g.getFont();
    g.setFont(this.getTypeFont());
    GraphicsUtilities.drawCenteredText(g, this.getTypeText(), x, y, w, h);

    if (this.isIndentForDepthAndMemberCountTextDesired) {
      if (this.bonusFont != null) {
        g.setFont(this.bonusFont);
        GraphicsUtilities.drawCenteredText(g, this.getBonusText(), x + w + BONUS_GAP, y, typePlusBonusWidth - w, h);
      }
    }
    g.setFont(prevFont);
    g2.setTransform(prevTransform);
  }
}
