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
package org.alice.ide.ast.data;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.property.ListProperty;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ListPropertyListener;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.data.RefreshableListData;

import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class FilteredListPropertyData<E> extends RefreshableListData<E> {
  private final ListPropertyListener<E> listPropertyListener = new ListPropertyListener<E>() {
    @Override
    public void added(AddListPropertyEvent<E> e) {
      FilteredListPropertyData.this.refresh();
    }

    @Override
    public void cleared(ClearListPropertyEvent<E> e) {
      FilteredListPropertyData.this.refresh();
    }

    @Override
    public void removed(RemoveListPropertyEvent<E> e) {
      FilteredListPropertyData.this.refresh();
    }

    @Override
    public void set(SetListPropertyEvent<E> e) {
      FilteredListPropertyData.this.refresh();
    }
  };

  private final PropertyListener propertyListener = e -> FilteredListPropertyData.this.refresh();

  private ListProperty<E> listProperty;

  public FilteredListPropertyData(ItemCodec<E> itemCodec) {
    super(itemCodec);
  }

  protected abstract boolean isAcceptableItem(E item);

  @Override
  protected List<E> createValues() {
    if (this.listProperty != null) {
      List<E> list = Lists.newLinkedList();

      for (E item : this.listProperty) {
        if (this.isAcceptableItem(item)) {
          list.add(item);
        }
      }

      return list;
    } else {
      return Collections.emptyList();
    }
  }

  protected ListProperty<E> getListProperty() {
    return this.listProperty;
  }

  public void setListProperty(ListProperty<E> listProperty) {
    if (this.listProperty != null) {
      this.listProperty.removePropertyListener(this.propertyListener);
      this.listProperty.removeListPropertyListener(this.listPropertyListener);
    }
    this.listProperty = listProperty;
    if (this.listProperty != null) {
      this.listProperty.addPropertyListener(this.propertyListener);
      this.listProperty.addListPropertyListener(this.listPropertyListener);
      this.refresh();
    }
  }
}
