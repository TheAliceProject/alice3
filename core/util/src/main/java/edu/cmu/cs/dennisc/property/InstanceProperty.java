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
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class InstanceProperty<T> {
  public InstanceProperty(InstancePropertyOwner owner, T value) {
    this.owner = owner;
    this.value = value;
  }

  public String getName() {
    if (this.name == null) {
      this.name = this.owner.lookupNameFor(this);
    }
    return this.name;
  }

  public void addPropertyListener(PropertyListener propertyListener) {
    assert propertyListener != null : this;
    this.propertyListeners.add(propertyListener);
  }

  public void removePropertyListener(PropertyListener propertyListener) {
    this.propertyListeners.remove(propertyListener);
  }

  public Collection<PropertyListener> getPropertyListeners() {
    return Collections.unmodifiableCollection(this.propertyListeners);
  }

  private void firePropertyChanging(PropertyEvent e) {
    InstancePropertyOwner owner = this.getOwner();
    if (owner != null) {
      owner.firePropertyChanging(e);
    }
  }

  private void firePropertyChanged(PropertyEvent e) {
    for (PropertyListener propertyListener : this.propertyListeners) {
      propertyListener.propertyChanged(e);
    }
    InstancePropertyOwner owner = this.getOwner();
    if (owner != null) {
      owner.firePropertyChanged(e);
    }
  }

  public InstancePropertyOwner getOwner() {
    return this.owner;
  }

  public final T getValue() {
    return this.value;
  }

  public void setValue(T value) {
    PropertyEvent e = new PropertyEvent(this, this.owner, value);
    firePropertyChanging(e);
    this.value = value;
    firePropertyChanged(e);
  }

  protected void readValue(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    this.value = (T) ois.readObject();
  }

  @Override
  public String toString() {
    return getClass().getName() + "[owner=" + getOwner() + ";name=" + getName() + "]";
  }

  private final List<PropertyListener> propertyListeners = Lists.newCopyOnWriteArrayList();
  private final InstancePropertyOwner owner;
  private T value;
  private String name;
}
