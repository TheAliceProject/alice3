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
package edu.cmu.cs.dennisc.javax.swing.components;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsContext;
import edu.cmu.cs.dennisc.java.util.Arrays;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.event.UnifiedDocumentListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentListener;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

/**
 * @author Dennis Cosgrove
 */
//TODO: provide custom java.awt.FocusTraversalPolicy to skip JSubtleTextFields?
public class JSubdudeTextField extends JSuggestiveTextField {
  public JSubdudeTextField() {
    InputMap inputMap = this.getInputMap();
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), this.transferFocusAction);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), this.transferFocusAction);
  }

  @Override
  public Dimension getPreferredSize() {
    Insets insets = this.getInsets();
    return DimensionUtilities.constrainToMinimumWidth(super.getPreferredSize(), 24 + insets.left + insets.right);
  }

  @Override
  public Dimension getMaximumSize() {
    return this.getPreferredSize();
  }

  private void installListeners() {
    if (Arrays.contains(this.getMouseListeners(), this.mouseListener)) {
      //pass
    } else {
      this.addMouseListener(this.mouseListener);
      this.addFocusListener(this.focusListener);
      this.getDocument().addDocumentListener(this.documentListener);
    }
  }

  private void uninstallListeners() {
    if (Arrays.contains(this.getMouseListeners(), this.mouseListener)) {
      this.getDocument().removeDocumentListener(this.documentListener);
      this.removeFocusListener(this.focusListener);
      this.removeMouseListener(this.mouseListener);
    } else {
      //pass
    }
  }

  @Override
  public void addNotify() {
    super.addNotify();
    this.installListeners();
  }

  @Override
  public void removeNotify() {
    this.uninstallListeners();
    super.removeNotify();
  }

  @Override
  public void paint(Graphics g) {
    if (this.hasFocus()) {
      super.paint(g);
    } else {
      GraphicsContext gc = GraphicsContext.getInstanceAndPushGraphics(g);
      gc.pushAndSetTextAntialiasing(true);
      gc.pushFont();
      gc.pushPaint();
      Color backgroundColor = this.getParent().getBackground();
      if (this.buttonModel.isRollover()) {
        backgroundColor = ColorUtilities.scaleHSB(backgroundColor, 1.0, 0.9, 0.9);
      }
      g.setColor(backgroundColor);
      g.fillRect(0, 0, this.getWidth(), this.getHeight());
      g.setColor(this.getForeground());
      g.setFont(this.getFont());
      FontMetrics fm = g.getFontMetrics();
      Insets insets = this.getInsets();
      ComponentOrientation componentOrientation = this.getComponentOrientation();
      String text = this.getText();
      int x;
      if (componentOrientation.isLeftToRight()) {
        x = insets.left;
      } else {
        x = this.getWidth() - insets.right;
        Rectangle2D bounds = fm.getStringBounds(text, g);
        x -= (int) (Math.ceil(bounds.getWidth()));
      }
      int y = insets.top + fm.getAscent();

      g.drawString(text, x, y);
      gc.popAll();
    }
  }

  private final AWTEventListener globalListener = new AWTEventListener() {
    @Override
    public void eventDispatched(AWTEvent e) {
      if (e instanceof MouseEvent) {
        MouseEvent mouseEvent = (MouseEvent) e;
        if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
          if (mouseEvent.getComponent() == JSubdudeTextField.this) {
            //pass
            Logger.outln(mouseEvent);
          } else {
            transferFocus();
          }
        }
      }
    }
  };

  private final FocusListener focusListener = new FocusListener() {
    @Override
    public void focusGained(FocusEvent e) {
      Toolkit.getDefaultToolkit().addAWTEventListener(globalListener, AWTEvent.MOUSE_EVENT_MASK);
    }

    @Override
    public void focusLost(FocusEvent e) {
      Toolkit.getDefaultToolkit().removeAWTEventListener(globalListener);
    }
  };

  private final MouseListener mouseListener = new MouseListener() {
    @Override
    public void mouseEntered(MouseEvent e) {
      buttonModel.setRollover(true);
      repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
      buttonModel.setRollover(false);
      repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
  };

  private final Action transferFocusAction = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      transferFocus();
    }
  };

  private final DocumentListener documentListener = new UnifiedDocumentListener(() -> {
    Container parent = getParent();
    if (parent instanceof JComponent) {
      parent.revalidate();
      parent.repaint();
    }
  });

  private final ButtonModel buttonModel = new DefaultButtonModel();
}
