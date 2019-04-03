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
package org.alice.imageeditor.croquet;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Objects;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.io.File;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
/* package-private */class FilenameComboBoxModel implements ComboBoxModel {
  private boolean isWorking;
  private List<File> data;

  private Object selectedItem;

  private final List<ListDataListener> listDataListeners = Lists.newCopyOnWriteArrayList();

  @Override
  public int getSize() {
    int isWorkingDelta = this.isWorking ? 1 : 0;
    return this.data != null ? this.data.size() + isWorkingDelta : 0;
  }

  @Override
  public Object getElementAt(int index) {
    File file = (index < this.data.size()) ? this.data.get(index) : null;
    return file != null ? file.getAbsolutePath() : null;
  }

  @Override
  public Object getSelectedItem() {
    return this.selectedItem;
  }

  @Override
  public void setSelectedItem(Object selectedItem) {
    if (Objects.equals(this.selectedItem, selectedItem)) {
      //pass
    } else {
      if ((this.selectedItem != null) && (selectedItem != null) && this.selectedItem.toString().contentEquals(selectedItem.toString())) {
        //pass
      } else {
        this.selectedItem = selectedItem;
        this.fireContentsChanged(-1, -1);
      }
    }
  }

  @Override
  public void addListDataListener(ListDataListener listener) {
    this.listDataListeners.add(listener);
  }

  @Override
  public void removeListDataListener(ListDataListener listener) {
    this.listDataListeners.remove(listener);
  }

  public void prologue() {
    this.isWorking = true;
    this.data = Lists.newArrayList();
    this.fireContentsChanged(0, 0);
  }

  public void addAll(List<File> files) {
    int indexA = this.data.size();
    this.data.addAll(files);
    this.fireContentsChanged(indexA, this.data.size() - 1);
  }

  public void done(File[] data) {
    //todo: check
    this.isWorking = false;
  }

  private void fireContentsChanged(int indexA, int indexB) {
    ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, indexA, indexB);
    for (ListDataListener listDataListener : listDataListeners) {
      listDataListener.contentsChanged(e);
    }
  }
}
