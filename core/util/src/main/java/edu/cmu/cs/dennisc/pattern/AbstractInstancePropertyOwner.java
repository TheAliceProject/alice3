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
package edu.cmu.cs.dennisc.pattern;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import edu.cmu.cs.dennisc.property.event.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractInstancePropertyOwner extends AbstractNameable implements InstancePropertyOwner, ReferenceableBinaryEncodableAndDecodable {
  public void addPropertyListener(PropertyListener propertyListener) {
    this.propertyListeners.add(propertyListener);
  }

  public void removePropertyListener(PropertyListener propertyListener) {
    this.propertyListeners.remove(propertyListener);
  }

  public Collection<PropertyListener> getPropertyListeners() {
    return Collections.unmodifiableCollection(this.propertyListeners);
  }

  @Override
  public void firePropertyChanging(PropertyEvent e) {
  }

  @Override
  public void firePropertyChanged(PropertyEvent e) {
    for (PropertyListener propertyListener : this.propertyListeners) {
      propertyListener.propertyChanged(e);
    }
  }

  public void addListPropertyListener(ListPropertyListener<?> listPropertyListener) {
    this.listPropertyListeners.add(listPropertyListener);
  }

  public void removeListPropertyListener(ListPropertyListener<?> listPropertyListener) {
    this.listPropertyListeners.remove(listPropertyListener);
  }

  public Collection<ListPropertyListener<?>> getListPropertyListeners() {
    return Collections.unmodifiableCollection(this.listPropertyListeners);
  }

  // TODO remove
  @Override
  public void fireAdding(AddListPropertyEvent<?> e) {
  }

  @Override
  public void fireAdded(AddListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.added(e);
    }
  }

  @Override
  public void fireClearing(ClearListPropertyEvent<?> e) {
  }

  @Override
  public void fireCleared(ClearListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.cleared(e);
    }
  }

  @Override
  public void fireRemoving(RemoveListPropertyEvent<?> e) {
  }

  @Override
  public void fireRemoved(RemoveListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.removed(e);
    }
  }

  @Override
  public void fireSetting(SetListPropertyEvent<?> e) {
  }

  @Override
  public void fireSet(SetListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.set(e);
    }
  }

  @Override
  public InstanceProperty<?> getPropertyNamed(String name) {
    //todo: remove
    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
    try {
      Field field = getClass().getField(name);
      return (InstanceProperty<?>) ReflectionUtilities.get(field, this);
    } catch (NoSuchFieldException nsfe) {
      return null;
    }
  }

  @Override
  public List<InstanceProperty<?>> getProperties() {
    if (this.properties == null) {
      Class<? extends InstancePropertyOwner> cls = getClass();
      this.properties = new LinkedList<InstanceProperty<?>>();
      for (Field field : cls.getFields()) {
        int modifiers = field.getModifiers();
        if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) && InstanceProperty.class.isAssignableFrom(field.getType())) {
            InstanceProperty instanceProperty = (InstanceProperty) ReflectionUtilities.get(field, this);
            assert instanceProperty.getOwner() == this;
            this.properties.add(instanceProperty);
          }
        }
      }
    return this.properties;
  }

  @Override
  public String lookupNameFor(InstanceProperty<?> instanceProperty) {
    for (Field field : getClass().getFields()) {
      if (InstanceProperty.class.isAssignableFrom(field.getType())) {
        int modifiers = field.getModifiers();
        if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) && ReflectionUtilities.get(field, this) == instanceProperty) {
          return field.getName();
        }
      }
    }
    return null;
  }

  @Override
  public void decode(BinaryDecoder binaryDecoder, Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    binaryDecoder.decodeProperties(this, map);
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder, Map<ReferenceableBinaryEncodableAndDecodable, Integer> map) {
    binaryEncoder.encodeProperties(this, map);
  }

  @Override
  public final boolean equals(Object obj) {
    return super.equals(obj);
  }

  public boolean isEquivalentTo(Object other) {
    if ((this == other) || super.equals(other)) {
      return true;
    } else {
      if (other instanceof AbstractInstancePropertyOwner otherDIPO) {
        int propertyCount = 0;
        for (InstanceProperty thisProperty : this.getProperties()) {
          String propertyName = thisProperty.getName();
          try {
            InstanceProperty otherProperty = otherDIPO.getPropertyNamed(propertyName);
            if (otherProperty != null) {
              Object thisValue = thisProperty.getValue();
              Object otherValue = otherProperty.getValue();
              if (thisValue instanceof AbstractInstancePropertyOwner owner) {
                if (!owner.isEquivalentTo(otherValue)) {
                  return false;
                }
              } else {
                if (!Objects.equals(thisValue, otherValue)) {
                  return false;
                }
              }
            } else {
              return false;
            }
            propertyCount++;
          } catch (Exception e) {
            Logger.throwable(e, this, other);
            return false;
          }
        }
        for (InstanceProperty otherProperty : otherDIPO.getProperties()) {
          propertyCount--;
        }
        return propertyCount == 0;
      } else {
        return false;
      }
    }
  }

  private List<InstanceProperty<?>> properties = null;
  private final List<PropertyListener> propertyListeners = Lists.newCopyOnWriteArrayList();
  private final List<ListPropertyListener<?>> listPropertyListeners = Lists.newCopyOnWriteArrayList();
}
