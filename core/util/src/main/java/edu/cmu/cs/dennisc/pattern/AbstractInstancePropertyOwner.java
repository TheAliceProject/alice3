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
import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.codec.BufferUtilities;
import edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ListPropertyListener;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractInstancePropertyOwner extends AbstractNameable implements InstancePropertyOwner, ReferenceableBinaryEncodableAndDecodable {
  private static final boolean IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS = true;

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
    for (PropertyListener propertyListener : this.propertyListeners) {
      propertyListener.propertyChanging(e);
    }
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

  @Override
  public void fireAdding(AddListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.adding(e);
    }
  }

  @Override
  public void fireAdded(AddListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.added(e);
    }
  }

  @Override
  public void fireClearing(ClearListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.clearing(e);
    }
  }

  @Override
  public void fireCleared(ClearListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.cleared(e);
    }
  }

  @Override
  public void fireRemoving(RemoveListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.removing(e);
    }
  }

  @Override
  public void fireRemoved(RemoveListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.removed(e);
    }
  }

  @Override
  public void fireSetting(SetListPropertyEvent e) {
    for (ListPropertyListener<?> l : this.listPropertyListeners) {
      l.setting(e);
    }
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
        if (Modifier.isPublic(modifiers)) {
          if (Modifier.isStatic(modifiers)) {
            //pass
          } else {
            if (InstanceProperty.class.isAssignableFrom(field.getType())) {
              InstanceProperty instanceProperty = (InstanceProperty) ReflectionUtilities.get(field, this);
              assert instanceProperty.getOwner() == this;
              this.properties.add(instanceProperty);
            }
          }
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
        if (Modifier.isPublic(modifiers)) {
          if (Modifier.isStatic(modifiers)) {
            //pass
          } else {
            if (ReflectionUtilities.get(field, this) == instanceProperty) {
              return field.getName();
            }
          }
        }
      }
    }
    return null;
  }

  private Object decodeObject(BinaryDecoder binaryDecoder, Class valueCls, Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    Object rv;
    if (BinaryEncodableAndDecodable.class.isAssignableFrom(valueCls)) {
      rv = binaryDecoder.decodeBinaryEncodableAndDecodable();
    } else if (ReferenceableBinaryEncodableAndDecodable.class.isAssignableFrom(valueCls)) {
      rv = binaryDecoder.decodeReferenceableBinaryEncodableAndDecodable(map);
    } else if (ByteBuffer.class.isAssignableFrom(valueCls)) {
      rv = BufferUtilities.decodeByteBuffer(binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
    } else if (CharBuffer.class.isAssignableFrom(valueCls)) {
      rv = BufferUtilities.decodeCharBuffer(binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
    } else if (ShortBuffer.class.isAssignableFrom(valueCls)) {
      rv = BufferUtilities.decodeShortBuffer(binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
    } else if (IntBuffer.class.isAssignableFrom(valueCls)) {
      rv = BufferUtilities.decodeIntBuffer(binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
    } else if (LongBuffer.class.isAssignableFrom(valueCls)) {
      rv = BufferUtilities.decodeLongBuffer(binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
    } else if (FloatBuffer.class.isAssignableFrom(valueCls)) {
      rv = BufferUtilities.decodeFloatBuffer(binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
    } else if (DoubleBuffer.class.isAssignableFrom(valueCls)) {
      rv = BufferUtilities.decodeDoubleBuffer(binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
    } else if (Boolean.class == valueCls) {
      rv = binaryDecoder.decodeBoolean();
    } else if (Byte.class == valueCls) {
      rv = binaryDecoder.decodeByte();
    } else if (Character.class == valueCls) {
      rv = binaryDecoder.decodeChar();
    } else if (Double.class == valueCls) {
      rv = binaryDecoder.decodeDouble();
    } else if (Float.class == valueCls) {
      rv = binaryDecoder.decodeFloat();
    } else if (Integer.class == valueCls) {
      rv = binaryDecoder.decodeInt();
    } else if (Long.class == valueCls) {
      rv = binaryDecoder.decodeLong();
    } else if (Short.class == valueCls) {
      rv = binaryDecoder.decodeShort();
    } else if (String.class == valueCls) {
      rv = binaryDecoder.decodeString();
    } else if (Enum.class.isAssignableFrom(valueCls)) {
      Enum e = binaryDecoder.decodeEnum();
      rv = e;
    } else {
      throw new RuntimeException(valueCls.getName());
    }
    return rv;
  }

  @Override
  public void decode(BinaryDecoder binaryDecoder, Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    while (true) {
      String propertyName = binaryDecoder.decodeString();
      if (propertyName.length() > 0) {
        InstanceProperty property = getPropertyNamed(propertyName);
        assert property != null;
        String valueClsName = binaryDecoder.decodeString();
        assert valueClsName != null;
        Object value;
        if (valueClsName.equals("")) {
          value = null;
        } else {
          Class valueCls = ReflectionUtilities.getClassForName(valueClsName);
          if (valueCls.isArray()) {
            if (boolean[].class == valueCls) {
              value = binaryDecoder.decodeBooleanArray();
            } else if (byte[].class == valueCls) {
              value = binaryDecoder.decodeByteArray();
            } else if (char[].class == valueCls) {
              value = binaryDecoder.decodeCharArray();
            } else if (double[].class == valueCls) {
              value = binaryDecoder.decodeDoubleArray();
            } else if (float[].class == valueCls) {
              value = binaryDecoder.decodeFloatArray();
            } else if (int[].class == valueCls) {
              value = binaryDecoder.decodeIntArray();
            } else if (long[].class == valueCls) {
              value = binaryDecoder.decodeLongArray();
            } else if (short[].class == valueCls) {
              value = binaryDecoder.decodeShortArray();
            } else if (String[].class == valueCls) {
              value = binaryDecoder.decodeStringArray();
            } else if (Enum[].class.isAssignableFrom(valueCls)) {
              value = binaryDecoder.decodeEnumArray(valueCls.getComponentType());
            } else if (BinaryEncodableAndDecodable[].class.isAssignableFrom(valueCls)) {
              value = binaryDecoder.decodeBinaryEncodableAndDecodableArray(valueCls.getComponentType());
            } else if (ReferenceableBinaryEncodableAndDecodable[].class.isAssignableFrom(valueCls)) {
              value = binaryDecoder.decodeReferenceableBinaryEncodableAndDecodableArray(valueCls.getComponentType(), map);
            } else {
              int length = binaryDecoder.decodeInt();
              value = Array.newInstance(valueCls.getComponentType(), length);
              for (int i = 0; i < length; i++) {
                Array.set(value, i, decodeObject(binaryDecoder, valueCls.getComponentType(), map));
              }
            }
          } else if (Collection.class.isAssignableFrom(valueCls)) {
            int size = binaryDecoder.decodeInt();
            Collection collection = (Collection) ReflectionUtilities.newInstance(valueCls);
            for (int i = 0; i < size; i++) {
              String componentTypeName = binaryDecoder.decodeString();
              Class<?> componentType = ReflectionUtilities.getClassForName(componentTypeName);
              collection.add(decodeObject(binaryDecoder, componentType, map));
            }
            value = null;
          } else {
            value = decodeObject(binaryDecoder, valueCls, map);
          }
        }
        property.setValue(value);
      } else {
        break;
      }
    }
  }

  private void encodeObject(BinaryEncoder binaryEncoder, Object value, Map<ReferenceableBinaryEncodableAndDecodable, Integer> map) {
    if (value != null) {
      Class<?> valueCls = value.getClass();

      if (BinaryEncodableAndDecodable.class.isAssignableFrom(valueCls)) {
        binaryEncoder.encode((BinaryEncodableAndDecodable) value);
      } else if (ReferenceableBinaryEncodableAndDecodable.class.isAssignableFrom(valueCls)) {
        binaryEncoder.encode((ReferenceableBinaryEncodableAndDecodable) value, map);
      } else if (ByteBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(binaryEncoder, (ByteBuffer) value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
      } else if (CharBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(binaryEncoder, (CharBuffer) value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
      } else if (ShortBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(binaryEncoder, (ShortBuffer) value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
      } else if (IntBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(binaryEncoder, (IntBuffer) value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
      } else if (LongBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(binaryEncoder, (LongBuffer) value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
      } else if (FloatBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(binaryEncoder, (FloatBuffer) value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
      } else if (DoubleBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(binaryEncoder, (DoubleBuffer) value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS);
      } else if (Boolean.class == valueCls) {
        binaryEncoder.encode((Boolean) value);
      } else if (Byte.class == valueCls) {
        binaryEncoder.encode((Byte) value);
      } else if (Character.class == valueCls) {
        binaryEncoder.encode((Character) value);
      } else if (Double.class == valueCls) {
        binaryEncoder.encode((Double) value);
      } else if (Float.class == valueCls) {
        binaryEncoder.encode((Float) value);
      } else if (Integer.class == valueCls) {
        binaryEncoder.encode((Integer) value);
      } else if (Long.class == valueCls) {
        binaryEncoder.encode((Long) value);
      } else if (Short.class == valueCls) {
        binaryEncoder.encode((Short) value);
      } else if (String.class == valueCls) {
        binaryEncoder.encode((String) value);
      } else if (Enum.class.isAssignableFrom(valueCls)) {
        binaryEncoder.encode((Enum) value);
      } else {
        throw new RuntimeException(value.getClass().getName() + " " + value.toString());
      }
    } else {
      binaryEncoder.encode("");
    }
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder, Map<ReferenceableBinaryEncodableAndDecodable, Integer> map) {
    for (InstanceProperty<?> property : getProperties()) {
      // todo?
      // if( property.isTransient() ) {
      // //pass
      // } else {
      binaryEncoder.encode(property.getName());
      Object value = property.getValue();
      if (value != null) {
        Class<?> valueCls = value.getClass();
        binaryEncoder.encode(valueCls.getName());
        if (valueCls.isArray()) {
          if (boolean[].class == valueCls) {
            binaryEncoder.encode((boolean[]) value);
          } else if (byte[].class == valueCls) {
            binaryEncoder.encode((byte[]) value);
          } else if (char[].class == valueCls) {
            binaryEncoder.encode((char[]) value);
          } else if (double[].class == valueCls) {
            binaryEncoder.encode((double[]) value);
          } else if (float[].class == valueCls) {
            binaryEncoder.encode((float[]) value);
          } else if (int[].class == valueCls) {
            binaryEncoder.encode((int[]) value);
          } else if (long[].class == valueCls) {
            binaryEncoder.encode((long[]) value);
          } else if (short[].class == valueCls) {
            binaryEncoder.encode((short[]) value);
          } else if (String[].class == valueCls) {
            binaryEncoder.encode((String[]) value);
          } else if (Enum[].class.isAssignableFrom(valueCls)) {
            binaryEncoder.encode((Enum[]) value);
          } else if (BinaryEncodableAndDecodable[].class.isAssignableFrom(valueCls)) {
            binaryEncoder.encode((BinaryEncodableAndDecodable[]) value);
          } else if (ReferenceableBinaryEncodableAndDecodable[].class.isAssignableFrom(valueCls)) {
            binaryEncoder.encode((ReferenceableBinaryEncodableAndDecodable[]) value, map);
          } else {
            int length = Array.getLength(value);
            binaryEncoder.encode(length);
            for (int i = 0; i < length; i++) {
              encodeObject(binaryEncoder, Array.get(value, i), map);
            }
          }
        } else if (Collection.class.isAssignableFrom(valueCls)) {
          Collection<?> collection = (Collection<?>) value;
          int size = collection.size();
          binaryEncoder.encode(size);
          for (Object o : collection) {
            encodeObject(binaryEncoder, o, map);
          }
        } else {
          encodeObject(binaryEncoder, value, map);
        }
      } else {
        binaryEncoder.encode("");
      }
    }
    binaryEncoder.encode("");
  }

  @Override
  public final boolean equals(Object obj) {
    return super.equals(obj);
  }

  public boolean isEquivalentTo(Object other) {
    if ((this == other) || super.equals(other)) {
      return true;
    } else {
      if (other instanceof AbstractInstancePropertyOwner) {
        AbstractInstancePropertyOwner otherDIPO = (AbstractInstancePropertyOwner) other;
        int propertyCount = 0;
        for (InstanceProperty thisProperty : this.getProperties()) {
          String propertyName = thisProperty.getName();
          try {
            InstanceProperty otherProperty = otherDIPO.getPropertyNamed(propertyName);
            if (otherProperty != null) {
              Object thisValue = thisProperty.getValue();
              Object otherValue = otherProperty.getValue();
              if (thisValue instanceof AbstractInstancePropertyOwner) {
                if (((AbstractInstancePropertyOwner) thisValue).isEquivalentTo(otherValue)) {
                  //pass
                } else {
                  return false;
                }
              } else {
                if (Objects.equals(thisValue, otherValue)) {
                  //pass
                } else {
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
