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

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import edu.cmu.cs.dennisc.javax.swing.plaf.SmallerFootprintScrollBarUI;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

/**
 * @author Dennis Cosgrove
 */
public class JScrollPaneCoveringLinuxPaintBug extends javax.swing.JScrollPane {

  protected static class JViewBasedBackgroundColorScrollBar extends JScrollBar {
    public JViewBasedBackgroundColorScrollBar(int orientation) {
      super(orientation);
    }

    @Override
    public Color getBackground() {
      javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane) this.getParent();
      if (scrollPane != null) {
        Color scrollPaneBackground = scrollPane.getBackground();
        return scrollPaneBackground;
        //todo?
        //return edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( scrollPaneBackground, 1.0, 0.7, 1.1 );
      }
      return super.getBackground();
    }
  }

  public JScrollPaneCoveringLinuxPaintBug(Component view) {
    super(view);
    if (SystemUtilities.isLinux()) {
      this.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
    }
    int inset = SmallerFootprintScrollBarUI.INSET;
    Border border = BorderFactory.createEmptyBorder(inset, inset, inset, inset);
    JScrollBar verticalScrollBar = this.getVerticalScrollBar();
    verticalScrollBar.setUI(SmallerFootprintScrollBarUI.createUI());
    verticalScrollBar.setBorder(border);

    JScrollBar horizontalScrollBar = this.getHorizontalScrollBar();
    horizontalScrollBar.setUI(SmallerFootprintScrollBarUI.createUI());
    horizontalScrollBar.setBorder(border);
  }

  public JScrollPaneCoveringLinuxPaintBug() {
    this(null);
  }

  @Override
  public Color getBackground() {
    JViewport viewport = this.getViewport();
    if (viewport != null) {
      Component view = viewport.getView();
      if (view != null) {
        return view.getBackground();
      }
    }
    return super.getBackground();
  }

  @Override
  public JScrollBar createHorizontalScrollBar() {
    return new JViewBasedBackgroundColorScrollBar(JScrollBar.HORIZONTAL);
  }

  @Override
  public JScrollBar createVerticalScrollBar() {
    return new JViewBasedBackgroundColorScrollBar(JScrollBar.VERTICAL);
  }

  public static void main(String[] args) {
    UIManagerUtilities.setLookAndFeel("Nimbus");
    UIManager.put("ScrollBar.width", 11);
    JPanel view = new JPanel();
    view.setPreferredSize(new Dimension(1000, 400));
    JScrollPaneCoveringLinuxPaintBug scrollPane = new JScrollPaneCoveringLinuxPaintBug(view);
    JFrame frame = new JFrame();
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(320, 240);
    frame.setVisible(true);
  }
}
