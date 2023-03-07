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

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.Painter;
import edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.SingleSelectListState;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class List<T> extends ViewController<JList, SingleSelectListState<T, ?>> {
  public enum LayoutOrientation {
    VERTICAL(JList.VERTICAL), VERTICAL_WRAP(JList.VERTICAL_WRAP), HORIZONTAL_WRAP(JList.HORIZONTAL_WRAP);
    private int internal;

    private LayoutOrientation(int internal) {
      this.internal = internal;
    }
  }

  private static class DefaultEmptyListPainter<T> implements Painter<List<T>> {
    private static final Map<TextAttribute, Object> mapDeriveFont;

    static {
      mapDeriveFont = Maps.newHashMap();
      mapDeriveFont.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
      mapDeriveFont.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_LIGHT);
    }

    @Override
    public void paint(Graphics2D g2, List<T> listView, int width, int height) {
      SingleSelectListState<T, ?> state = listView.getModel();
      PlainStringValue emptyConditionText = state.getEmptyConditionText();
      String text = emptyConditionText.getText();
      if ((text != null) && (text.length() > 0)) {
        GraphicsUtilities.setRenderingHint(g2, RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setPaint(Color.DARK_GRAY);
        g2.setFont(g2.getFont().deriveFont(mapDeriveFont));
        final int OFFSET = 4;
        g2.drawString(text, OFFSET, OFFSET + g2.getFontMetrics().getAscent());
      }
    }
  }

  private Painter<List<T>> emptyConditionPainter = new DefaultEmptyListPainter<T>();

  public List(SingleSelectListState<T, ?> model) {
    super(model);
    this.getAwtComponent().setModel(model.getSwingModel().getComboBoxModel());
    this.getAwtComponent().setSelectionModel(model.getSwingModel().getListSelectionModel());
  }

  private final LenientMouseClickAdapter mouseAdapter = new LenientMouseClickAdapter() {
    @Override
    protected void mouseQuoteClickedUnquote(MouseEvent e, int quoteClickCountUnquote) {
      if (quoteClickCountUnquote == 2) {
        AbstractWindow<?> window = List.this.getRoot();
        if (window != null) {
          Button defaultButton = window.getDefaultButton();
          if (defaultButton != null) {
            defaultButton.doClick();
          }
        }
      }
    }
  };

  public void enableClickingDefaultButtonOnDoubleClick() {
    this.addMouseListener(this.mouseAdapter);
    this.addMouseMotionListener(this.mouseAdapter);
  }

  public void disableClickingDefaultButtonOnDoubleClick() {
    this.removeMouseMotionListener(this.mouseAdapter);
    this.removeMouseListener(this.mouseAdapter);
  }

  protected class JDefaultList extends JList {
    @Override
    public Dimension getPreferredSize() {
      return constrainPreferredSizeIfNecessary(super.getPreferredSize());
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      ListModel model = this.getModel();
      if (model.getSize() == 0) {
        if (emptyConditionPainter != null) {
          emptyConditionPainter.paint((Graphics2D) g, List.this, this.getWidth(), this.getHeight());
        }
      }
    }
  }

  @Override
  protected JList createAwtComponent() {
    return new JDefaultList();
  }

  public Painter<List<T>> getEmptyConditionPainter() {
    return this.emptyConditionPainter;
  }

  public void setEmptyConditionPainter(Painter<List<T>> emptyConditionPainter) {
    this.emptyConditionPainter = emptyConditionPainter;
  }

  public ListCellRenderer getCellRenderer() {
    return this.getAwtComponent().getCellRenderer();
  }

  public void setCellRenderer(ListCellRenderer listCellRenderer) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setCellRenderer(listCellRenderer);
  }

  public int getVisibleRowCount() {
    return this.getAwtComponent().getVisibleRowCount();
  }

  public void setVisibleRowCount(int visibleRowCount) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setVisibleRowCount(visibleRowCount);
  }

  public int getSelectedIndex() {
    return this.getAwtComponent().getSelectedIndex();
  }

  public void ensureIndexIsVisible(int index) {
    this.checkEventDispatchThread();
    this.getAwtComponent().ensureIndexIsVisible(index);
  }

  public void setLayoutOrientation(LayoutOrientation layoutOrientation) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setLayoutOrientation(layoutOrientation.internal);
  }
}
