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
package org.lgna.croquet.views.imp;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;

import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.JMenuItem;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
/* package-private */class JScrollMenuItem extends JMenuItem {
  private static final Dimension ARROW_SIZE = new Dimension(10, 10);
  private static final int SPACE = 10;

  private final ChangeListener changeListener = new ChangeListener() {
    @Override
    public void stateChanged(ChangeEvent e) {
      ButtonModel buttonModel = getModel();
      if (buttonModel.isArmed()) {
        if (timer.isRunning()) {
          //pass
        } else {
          timer.start();
        }
      } else {
        if (timer.isRunning()) {
          timer.stop();
        } else {
          //pass
        }
      }
    }
  };

  private static class ScrollAction extends AbstractAction {
    private final ScrollingPopupMenuLayout layout;
    private final ScrollDirection scrollDirection;

    public ScrollAction(ScrollingPopupMenuLayout layout, ScrollDirection scrollDirection) {
      this.layout = layout;
      this.scrollDirection = scrollDirection;
    }

    public ScrollDirection getScrollDirection() {
      return this.scrollDirection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      this.layout.adjustIndex(this.scrollDirection.getDelta());
    }
  }

  private final ScrollAction scrollAction;
  private final Timer timer;

  private int count;

  public JScrollMenuItem(ScrollingPopupMenuLayout layout, ScrollDirection scrollDirection) {
    this.scrollAction = new ScrollAction(layout, scrollDirection);
    this.timer = new Timer(100, this.scrollAction);
  }

  public int getCount() {
    return this.count;
  }

  public void setCount(int count) {
    if (this.count != count) {
      this.count = count;
      this.repaint();
    }
  }

  @Override
  protected void processMouseEvent(MouseEvent e) {
    int id = e.getID();
    if ((id == MouseEvent.MOUSE_PRESSED) || (id == MouseEvent.MOUSE_RELEASED)) {
      //pass
    } else {
      super.processMouseEvent(e);
    }
  }

  @Override
  public void addNotify() {
    this.addChangeListener(this.changeListener);
    super.addNotify();
  }

  @Override
  public void removeNotify() {
    super.removeNotify();
    this.removeChangeListener(this.changeListener);
  }

  @Override
  public Dimension getPreferredSize() {
    Font font = this.getFont();
    FontMetrics fontMetrics = this.getFontMetrics(font);
    int height = Math.max(fontMetrics.getHeight(), ARROW_SIZE.height);
    // Width enough for centered arrows with string on either side
    int width = 5 * (ARROW_SIZE.width + SPACE) + 2 * fontMetrics.stringWidth(" (9999)");
    return DimensionUtilities.constrainToMinimumSize(super.getPreferredSize(), width, height + 8);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    ButtonModel model = this.getModel();
    //  if( model.isArmed() ) {
    //    //g.setColor( javax.swing.UIManager.getColor( "textBackground" ) );
    //  } else {
    //    g.setColor( javax.swing.UIManager.getColor( "menu" ) );
    //    g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
    //  }
    super.paintComponent(g);
    if (model.isArmed()) {
      g.setColor(UIManager.getColor("textHighlightText"));
    } else {
      g.setColor(UIManager.getColor("textForeground"));
    }
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    GraphicsUtilities.Heading heading = this.scrollAction.getScrollDirection().getArrowHeading();
    int x = (this.getWidth() - ARROW_SIZE.width) / 2;
    int y = (this.getHeight() - ARROW_SIZE.height) / 2;

    for (int i = -2; i < 3; i++) {
      GraphicsUtilities.fillTriangle(g2, heading, x + (i * (ARROW_SIZE.width + SPACE)), y, ARROW_SIZE.width, ARROW_SIZE.height);
    }
    if (count != 0) {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      sb.append(this.count);
      sb.append(")");
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g2.drawString(sb.toString(), x + (3 * (ARROW_SIZE.width + SPACE)), (y + ARROW_SIZE.height) - 2);
    }
  }
}
