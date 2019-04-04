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
package org.lgna.debug.pick.croquet.views;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.javax.swing.icons.ColorIcon;
import edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer;
import edu.cmu.cs.dennisc.render.PickResult;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.debug.pick.croquet.PickDebugFrame;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class PickDebugFrameView extends BorderPanel {
  public PickDebugFrameView(PickDebugFrame composite) {
    super(composite);
    this.addPageEndComponent(composite.getRefreshOperation().createButton());
    this.getAwtComponent().add(this.pickView, BorderLayout.CENTER);
    JList jList = new JList(this.listModel);
    jList.setCellRenderer(new ListCellRenderer<Visual>() {
      @Override
      protected JLabel getListCellRendererComponent(JLabel rv, JList list, Visual value, int index, boolean isSelected, boolean cellHasFocus) {
        rv.setText(value != null ? value.getName() : null);
        rv.setIcon(mapSgVisualToColorIcon.get(value));
        return rv;
      }
    });
    this.getAwtComponent().add(new JScrollPane(jList), BorderLayout.LINE_END);
  }

  public void setPickResults(PickResult[][] pickResults) {
    synchronized (pickResultsLock) {
      this.mapSgVisualToColorIcon.clear();
      this.listModel.clear();
      this.pickResults = pickResults;
      if (this.pickResults != null) {
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.ORANGE, Color.WHITE,

        };
        int colorIndex = 0;
        for (PickResult[] line : pickResults) {
          for (PickResult pickResult : line) {
            Visual sgVisual = pickResult.getVisual();
            if (sgVisual != null) {
              if (this.mapSgVisualToColorIcon.containsKey(sgVisual)) {
                //pass
              } else {
                this.listModel.addElement(sgVisual);
                this.mapSgVisualToColorIcon.put(sgVisual, new ColorIcon(colors[colorIndex]));
                colorIndex += 1;
                colorIndex %= colors.length;
              }
            }
          }
        }
      }
    }
    this.repaint();
  }

  private Object pickResultsLock = "pickResultsLock";
  private PickResult[][] pickResults;
  private final Map<Visual, ColorIcon> mapSgVisualToColorIcon = Maps.newHashMap();

  private final DefaultListModel listModel = new DefaultListModel();

  private final JComponent pickView = new JComponent() {
    @Override
    protected void paintComponent(Graphics g) {
      synchronized (pickResultsLock) {
        if (pickResults != null) {
          final int PIXELS_PER_PICK = 10;
          for (int y = 0; y < pickResults.length; y++) {
            int yPixel = y * PIXELS_PER_PICK;
            for (int x = 0; x < pickResults[y].length; x++) {
              int xPixel = x * PIXELS_PER_PICK;
              PickResult pickResult = pickResults[y][x];
              Visual sgVisual = pickResult.getVisual();
              if (sgVisual != null) {
                g.setColor(mapSgVisualToColorIcon.get(sgVisual).getFillColor());
              } else {
                g.setColor(Color.BLACK);
              }
              g.fillRect(xPixel, yPixel, PIXELS_PER_PICK, PIXELS_PER_PICK);
            }
          }
        }
      }
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(800, 450);
    }
  };
}
