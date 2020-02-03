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

package org.alice.ide.declarationseditor.components;

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.javax.swing.icons.DropDownArrowIcon;
import org.alice.ide.codedrop.CodePanelWithDropReceptor;
import org.alice.ide.common.TypeBorder;
import org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.DeclarationsEditorComposite;
import org.alice.ide.declarationseditor.code.components.CodeDeclarationView;
import org.lgna.croquet.views.AbstractPopupButton;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.FolderTabbedPane;
import org.lgna.croquet.views.SwingComponentView;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
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

class DeclarationMenuIcon extends DropDownArrowIcon {
  private final TypeBorder border = TypeBorder.getSingletonForUserType();
  private final Font typeFont;

  private final int PAD = 4;

  public DeclarationMenuIcon() {
    super(10, Color.DARK_GRAY);
    this.typeFont = new Font(null, 0, 12);
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

  protected Font getTypeFont() {
    return this.typeFont;
  }

  private String getTypeText() {
    return "    ";
  }

  private Rectangle2D getTypeTextBounds() {
    return getTextBounds(this.getTypeText(), this.getTypeFont());
  }

  private int getBorderWidth() {
    Insets insets = this.border.getBorderInsets(null);
    Rectangle2D typeTextBounds = this.getTypeTextBounds();
    return insets.left + insets.right + (int) typeTextBounds.getWidth() + PAD;
  }

  private int getBorderHeight() {
    Insets insets = this.border.getBorderInsets(null);
    Rectangle2D bounds = this.getTypeTextBounds();
    return insets.top + insets.bottom + (int) bounds.getHeight();
  }

  @Override
  public int getIconWidth() {
    return super.getIconWidth() + this.getBorderWidth();
  }

  @Override
  public int getIconHeight() {
    return Math.max(super.getIconHeight(), this.getBorderHeight());
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    AffineTransform prevTransform = g2.getTransform();

    int w = this.getBorderWidth();
    int h = this.getBorderHeight();

    TypeBorder.getSingletonForUserType().paintBorder(c, g, x, y, w, h);
    g.setColor(c.getForeground());

    Font prevFont = g.getFont();
    g.setFont(this.getTypeFont());
    GraphicsUtilities.drawCenteredText(g, this.getTypeText(), x, y, w, h);

    g.setFont(prevFont);
    g2.setTransform(prevTransform);

    int superHeight = super.getIconHeight();
    int yOffset = (h - superHeight) / 2;
    super.paintIcon(c, g, x + w + PAD, y + yOffset);
  }
}

/**
 * @author Dennis Cosgrove
 */
public class TypeEditor extends BorderPanel {
  private final FolderTabbedPane<DeclarationComposite<?, ?>> tabbedPane;
  private final AbstractPopupButton<?> startButton;

  public TypeEditor(DeclarationsEditorComposite composite) {
    super(composite);
    //    org.lgna.croquet.components.FlowPanel headerTrailingComponent = new org.lgna.croquet.components.FlowPanel(
    //        composite.getControlsComposite().getView(),
    //        org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 12 ),
    //        org.alice.ide.clipboard.Clipboard.SINGLETON.getDragComponent()
    //        );

    SwingComponentView<?> headerTrailingComponent = composite.getControlsComposite().getView();

    //    final boolean IS_RECYCLE_BIN_READY_FOR_PRIME_TIME = false;
    //    if( IS_RECYCLE_BIN_READY_FOR_PRIME_TIME ) {
    //      headerTrailingComponent.addComponent( new org.alice.ide.recyclebin.RecycleBinView() );
    //    }
    headerTrailingComponent.setBorder(BorderFactory.createEmptyBorder(2, 2, 0, 2));

    final boolean IS_CUSTOM_DRAWING_DESIRED = false;
    if (IS_CUSTOM_DRAWING_DESIRED) {
      this.tabbedPane = new FolderTabbedPane<DeclarationComposite<?, ?>>(composite.getTabState()) {
        @Override
        protected TitlesPanel createTitlesPanel() {
          return new TitlesPanel() {
            @Override
            protected JPanel createJPanel() {
              return new JTitlesPanel() {
                @Override
                public void paint(Graphics g) {
                  super.paint(g);
                  g.setColor(Color.RED);
                  g.drawString("possibilities abound", 100, 10);
                }
              };
            }
          };
        }
      };
    } else {
      this.tabbedPane = composite.getTabState().createFolderTabbedPane();
    }
    this.tabbedPane.setHeaderTrailingComponent(headerTrailingComponent);
    this.startButton = composite.getDeclarationMenu().getPopupPrepModel().createPopupButton();

    this.startButton.setClobberIcon(new DeclarationMenuIcon());
    this.addCenterComponent(tabbedPane);
    SwingComponentView<?> component;
    if (IsEmphasizingClassesState.getInstance().getValue()) {
      component = this.startButton;
    } else {
      component = null;
    }
    this.tabbedPane.setHeaderLeadingComponent(component);
  }

  public CodePanelWithDropReceptor getCodeDropReceptorInFocus() {
    DeclarationsEditorComposite composite = (DeclarationsEditorComposite) this.getComposite();
    DeclarationComposite<?, ?> item = composite.getTabState().getValue();
    if (item != null) {
      SwingComponentView<?> component = this.tabbedPane.getMainComponentFor(item);
      if (component instanceof CodeDeclarationView) {
        return ((CodeDeclarationView) component).getCodePanelWithDropReceptor();
      }
    }
    return null;
  }
}
