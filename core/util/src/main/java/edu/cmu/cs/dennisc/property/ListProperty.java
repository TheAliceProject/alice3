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
package edu.cmu.cs.dennisc.property;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ListPropertyListener;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class ListProperty<E> extends InstanceProperty<ArrayList<E>> implements Iterable<E> {
  public ListProperty(InstancePropertyOwner owner) {
    super(owner, new ArrayList<E>());
  }

  public void addListPropertyListener(ListPropertyListener<E> l) {
    if (this.listPropertyListeners != null) {
      //pass
    } else {
      this.listPropertyListeners = Lists.newCopyOnWriteArrayList();
    }
    this.listPropertyListeners.add(l);
  }

  public void removeListPropertyListener(ListPropertyListener<E> l) {
    assert this.listPropertyListeners != null : this;
    this.listPropertyListeners.remove(l);
  }

  private void fireAdding(AddListPropertyEvent<E> e) {
    getOwner().fireAdding(e);
    if (this.listPropertyListeners != null) {
      for (ListPropertyListener<E> l : this.listPropertyListeners) {
        l.adding(e);
      }
    }
  }

  private void fireAdded(AddListPropertyEvent<E> e) {
    getOwner().fireAdded(e);
    if (this.listPropertyListeners != null) {
      for (ListPropertyListener<E> l : this.listPropertyListeners) {
        l.added(e);
      }
    }
  }

  private void fireClearing(ClearListPropertyEvent<E> e) {
    getOwner().fireClearing(e);
    if (this.listPropertyListeners != null) {
      for (ListPropertyListener<E> l : this.listPropertyListeners) {
        l.clearing(e);
      }
    }
  }

  private void fireCleared(ClearListPropertyEvent<E> e) {
    getOwner().fireCleared(e);
    if (this.listPropertyListeners != null) {
      for (ListPropertyListener<E> l : this.listPropertyListeners) {
        l.cleared(e);
      }
    }
  }

  private void fireRemoving(RemoveListPropertyEvent<E> e) {
    getOwner().fireRemoving(e);
    if (this.listPropertyListeners != null) {
      for (ListPropertyListener<E> l : this.listPropertyListeners) {
        l.removing(e);
      }
    }
  }

  private void fireRemoved(RemoveListPropertyEvent<E> e) {
    getOwner().fireRemoved(e);
    if (this.listPropertyListeners != null) {
      for (ListPropertyListener<E> l : this.listPropertyListeners) {
        l.removed(e);
      }
    }
  }

  private void fireSetting(SetListPropertyEvent<E> e) {
    getOwner().fireSetting(e);
    if (this.listPropertyListeners != null) {
      for (ListPropertyListener<E> l : this.listPropertyListeners) {
        l.setting(e);
      }
    }
  }

  private void fireSet(SetListPropertyEvent<E> e) {
    getOwner().fireSet(e);
    if (this.listPropertyListeners != null) {
      for (ListPropertyListener<E> l : this.listPropertyListeners) {
        l.set(e);
      }
    }
  }

  public Object[] toArray() {
    return getValue().toArray();
  }

  public <T> T[] toArray(T[] a) {
    return getValue().toArray(a);
  }

  public <T> T[] toArray(Class<T> componentType) {
    return this.toArray((T[]) Array.newInstance(componentType, this.size()));
  }

  public boolean isEmpty() {
    return getValue().isEmpty();
  }

  public boolean contains(Object o) {
    return getValue().contains(o);
  }

  public boolean containsAll(Collection<?> c) {
    return getValue().containsAll(c);
  }

  public int size() {
    return getValue().size();
  }

  public List<E> subList(int fromIndex, int upToButExcludingIndex) {
    return getValue().subList(fromIndex, upToButExcludingIndex);
  }

  public List<E> subListCopy(int fromIndex, int upToButExcludingIndex) {
    ArrayList<E> rv = Lists.newArrayList();
    rv.ensureCapacity(((upToButExcludingIndex - fromIndex) + 1) - 1);
    rv.addAll(this.subList(fromIndex, upToButExcludingIndex));
    return rv;
  }

  public int indexOf(Object element) {
    return getValue().indexOf(element);
  }

  public int lastIndexOf(Object element) {
    return getValue().lastIndexOf(element);
  }

  public E get(int index) {
    return getValue().get(index);
  }

  @Override
  public Iterator<E> iterator() {
    return getValue().iterator();
  }

  public void add(int index, E... elements) {
    AddListPropertyEvent<E> e = new AddListPropertyEvent<E>(this, index, elements);
    fireAdding(e);
    getValue().ensureCapacity(size() + elements.length);
    int i = index;
    for (E element : elements) {
      getValue().add(i++, element);
    }
    fireAdded(e);
  }

  public void add(E... elements) {
    add(size(), elements);
  }

  public boolean addAll(int index, Collection<? extends E> collection) {
    AddListPropertyEvent<E> e = new AddListPropertyEvent<E>(this, index, collection);
    fireAdding(e);
    boolean rv = getValue().addAll(index, collection);
    fireAdded(e);
    return rv;
  }

  public boolean addAll(Collection<? extends E> c) {
    return addAll(size(), c);
  }

  public void clear() {
    //assert isLocked() == false;
    ClearListPropertyEvent<E> e = new ClearListPropertyEvent<E>(this);
    fireClearing(e);
    getValue().clear();
    fireCleared(e);
  }

  public void removeExclusive(int fromIndex, int upToButExcludingIndex) {
    //assert isLocked() == false;
    RemoveListPropertyEvent<E> e = new RemoveListPropertyEvent<E>(this, fromIndex, this.subListCopy(fromIndex, upToButExcludingIndex));
    fireRemoving(e);
    for (int i = fromIndex; i < upToButExcludingIndex; i++) {
      getValue().remove(fromIndex);
    }
    fireRemoved(e);
  }

  public void removeInclusive(int fromIndex, int upToAndIncludingIndex) {
    this.removeExclusive(fromIndex, upToAndIncludingIndex + 1);
  }

  public E remove(int index) {
    E rv = get(index);
    removeInclusive(index, index);
    return rv;
  }

  public void set(int index, E... elements) {
    SetListPropertyEvent<E> e = new SetListPropertyEvent<E>(this, index, elements);
    fireSetting(e);
    for (int i = 0; i < elements.length; i++) {
      getValue().set(index + i, elements[i]);
    }
    fireSet(e);
  }

  public void set(int index, List<E> elements) {
    SetListPropertyEvent<E> e = new SetListPropertyEvent<E>(this, index, elements);
    fireSetting(e);
    for (int i = 0; i < elements.size(); i++) {
      getValue().set(index + i, elements.get(i));
    }
    fireSet(e);
  }

  public void swap(int indexA, int indexB) {
    if (indexA != indexB) {
      //todo: test
      int indexMin = Math.min(indexA, indexB);
      int indexMax = Math.max(indexA, indexB);
      List<E> subList = this.subList(indexMin, indexMax + 1);
      final int N = subList.size();
      E eMin = subList.get(0);
      E eMax = subList.get(N - 1);
      subList.set(0, eMax);
      subList.set(N - 1, eMin);
      this.set(indexMin, subList);
    }
  }

  public void slide(int prevIndex, int nextIndex) {
    if (prevIndex != nextIndex) {

      final int ONE_TO_EXCLUDE = 1;
      //todo: test
      E element = this.getValue().get(prevIndex);
      if (prevIndex < nextIndex) {
        List<E> subList = this.subListCopy(prevIndex + 1, nextIndex + ONE_TO_EXCLUDE);
        subList.add(element);
        this.set(prevIndex, subList);
      } else {
        List<E> subList = this.subListCopy(nextIndex, (prevIndex - 1) + ONE_TO_EXCLUDE);
        subList.add(0, element);
        this.set(nextIndex, subList);
      }
    }
  }

  @Override
  public void setValue(ArrayList<E> value) {

    //todo?

    ClearListPropertyEvent<E> eClear = new ClearListPropertyEvent<E>(this);
    AddListPropertyEvent<E> eAdd = new AddListPropertyEvent<E>(this, 0, value);
    fireClearing(eClear);
    fireAdding(eAdd);
    super.setValue(value);
    fireCleared(eClear);
    fireAdded(eAdd);
  }

  private List<ListPropertyListener<E>> listPropertyListeners = null;
}
