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

import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.SingleSelectListState;
import org.lgna.croquet.data.ListData;

import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Collection;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class ItemSelectablePanel<E> extends ViewController<JPanel, SingleSelectListState<E, ?>> {
  private final Map<E, BooleanStateButton<?>> mapItemToButton = Maps.newHashMap();

  private E[] prevItems;
  private final ListDataListener listDataListener = new ListDataListener() {
    @Override
    public void intervalAdded(ListDataEvent e) {
      ItemSelectablePanel.this.handleListDataChanged();
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
      ItemSelectablePanel.this.handleListDataChanged();
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
      ItemSelectablePanel.this.handleListDataChanged();
    }
  };

  public ItemSelectablePanel(SingleSelectListState<E, ?> model) {
    super(model);
  }

  private boolean isInitialized = false;

  @Override
  protected void handleDisplayable() {
    if (this.isInitialized) {
      //pass
    } else {
      this.getModel().getData().addListener(this.listDataListener);
      this.handleListDataChanged();
      this.isInitialized = true;
    }
    super.handleDisplayable();
  }

  protected abstract LayoutManager createLayoutManager(JPanel jPanel);

  protected class JItemSelectablePanel extends JPanel {
    public JItemSelectablePanel() {
      this.setOpaque(false);
      this.setAlignmentX(Component.LEFT_ALIGNMENT);
      this.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    @Override
    public Dimension getPreferredSize() {
      return ItemSelectablePanel.this.constrainPreferredSizeIfNecessary(super.getPreferredSize());
    }

    @Override
    public Dimension getMaximumSize() {
      Dimension rv = super.getMaximumSize();
      if (ItemSelectablePanel.this.isMaximumSizeClampedToPreferredSize()) {
        rv.setSize(this.getPreferredSize());
      }
      return rv;
    }
  }

  protected JPanel createJPanel() {
    return new JItemSelectablePanel();
  }

  @Override
  protected JPanel createAwtComponent() {
    JPanel rv = this.createJPanel();
    LayoutManager layoutManager = this.createLayoutManager(rv);
    rv.setLayout(layoutManager);
    return rv;
  }

  protected BooleanStateButton<?> getItemDetails(E item) {
    return this.mapItemToButton.get(item);
  }

  protected Collection<BooleanStateButton<?>> getAllButtons() {
    return this.mapItemToButton.values();
  }

  protected abstract BooleanStateButton<?> createButtonForItemSelectedState(E item, BooleanState itemSelectedState);

  protected abstract void removeAllDetails();

  protected abstract void addPrologue(int count);

  protected abstract void addItem(E item, BooleanStateButton<?> button);

  protected void addSeparator() {
  }

  protected abstract void addEpilogue();

  private void handleListDataChanged() {
    ListData<E> data = this.getModel().getData();
    synchronized (data) {
      final int N = data.getItemCount();

      boolean isActuallyChanged;
      if ((prevItems != null) && (N == prevItems.length)) {
        isActuallyChanged = false;
        for (int i = 0; i < N; i++) {
          E item = data.getItemAt(i);
          if (item == prevItems[i]) {
            //pass
          } else {
            isActuallyChanged = true;
            break;
          }
        }
      } else {
        isActuallyChanged = true;
      }

      if (isActuallyChanged) {
        synchronized (this.getTreeLock()) {

          this.removeAllDetails();
          this.prevItems = data.toArray();
          this.addPrologue(N);
          for (int i = 0; i < N; i++) {
            E item = data.getItemAt(i);
            if (item != null) {
              BooleanStateButton<?> button = this.mapItemToButton.get(item);
              if (button == null) {
                button = this.createButtonForItemSelectedState(item, this.getModel().getItemSelectedState(item));
                this.mapItemToButton.put(item, button);
              }
              this.addItem(item, button);
            } else {
              this.addSeparator();
            }
          }
          this.addEpilogue();
        }
      }

      //    int i = this.listSelectionModel.getLeadSelectionIndex();
      //    E selectedItem;
      //    if( i > 0 ) {
      //      selectedItem = (E)this.model.getElementAt( i );
      //    } else {
      //      selectedItem = null;
      //    }
      //    this.handleItemSelected( selectedItem );
    }
    this.revalidateAndRepaint();
  }

}
