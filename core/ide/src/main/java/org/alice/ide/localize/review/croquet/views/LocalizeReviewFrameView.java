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
package org.alice.ide.localize.review.croquet.views;

import edu.cmu.cs.dennisc.java.awt.DesktopUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.localize.review.croquet.LocalizeReviewFrame;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.ComboBox;
import org.lgna.croquet.views.LineAxisPanel;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.Locale;

/**
 * @author Dennis Cosgrove
 */
public class LocalizeReviewFrameView extends BorderPanel {
  public LocalizeReviewFrameView(LocalizeReviewFrame composite) {
    super(composite);
    ComboBox<Locale> comboBox = composite.getLocaleState().getPrepModel().createComboBox();
    comboBox.setRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Locale locale = (Locale) value;
        this.setText(locale.getDisplayName(locale) + " (" + locale.getDisplayName(Locale.US) + ")");
        return this;
      }
    });

    this.addPageStartComponent(new LineAxisPanel(composite.getLocaleState().getSidekickLabel().createLabel(), comboBox, composite.getIsIncludingUntranslatedState().createCheckBox()));
    jTable = new JTable(composite.getTableModel());
    TableColumnModel tableColumnModel = jTable.getColumnModel();
    TableColumn columnIndex = tableColumnModel.getColumn(0);
    columnIndex.setHeaderValue("index");
    columnIndex.setMaxWidth(64);
    TableColumn columnContext = tableColumnModel.getColumn(1);
    columnContext.setHeaderValue("Context");
    TableColumn columnOriginal = tableColumnModel.getColumn(2);
    columnOriginal.setHeaderValue("Original text");
    TableColumn columnTranslated = tableColumnModel.getColumn(3);
    columnTranslated.setHeaderValue("Translated text");

    TableColumn columnReview = tableColumnModel.getColumn(4);
    columnReview.setHeaderValue("Review");

    final JLabel label = new JLabel("<html><a href=\"\">review</a> [web]</html>");
    columnReview.setCellRenderer(new TableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return label;
      }
    });
    JScrollPane jScrollPane = new JScrollPane(jTable);
    this.getAwtComponent().add(jScrollPane, BorderLayout.CENTER);
  }

  @Override
  public void handleCompositePreActivation() {
    this.jTable.addMouseListener(this.mouseListener);
    super.handleCompositePreActivation();
  }

  @Override
  public void handleCompositePostDeactivation() {
    super.handleCompositePostDeactivation();
    this.jTable.removeMouseListener(this.mouseListener);
  }

  private final JTable jTable;
  private final MouseListener mouseListener = new MouseListener() {
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      Point pt = e.getPoint();
      int columnIndex = jTable.columnAtPoint(pt);
      if (columnIndex == 4) {
        int rowIndex = jTable.rowAtPoint(pt);
        if (rowIndex >= 0) {
          LocalizeReviewFrame composite = (LocalizeReviewFrame) getComposite();
          URI uri = composite.createUri(rowIndex);
          try {
            DesktopUtilities.browse(uri);
          } catch (Exception exc) {
            throw new RuntimeException(exc);
          }
        }
        Logger.outln(rowIndex, columnIndex, jTable.getModel().getValueAt(rowIndex, columnIndex));
      }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
  };
}
