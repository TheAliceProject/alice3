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
package edu.cmu.cs.dennisc.codec;

import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;

import java.lang.reflect.Array;
import java.nio.*;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractBinaryEncoder implements BinaryEncoder {
  //todo: handle null arrays
  private void encodeArrayLength(Object array) {
    //todo: encode -1 for null?
    int arrayLength;
    if (array != null) {
      arrayLength = Array.getLength(array);
    } else {
      arrayLength = -1;
    }
    this.encode(arrayLength);
  }

  @Override
  public final void encode(boolean[] array) {
    this.encodeArrayLength(array);
    for (boolean element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(byte[] array) {
    this.encodeArrayLength(array);
    this.write(array);
  }

  @Override
  public final void encode(char[] array) {
    this.encodeArrayLength(array);
    for (char element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(double[] array) {
    this.encodeArrayLength(array);
    for (double element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(float[] array) {
    this.encodeArrayLength(array);
    for (float element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(int[] array) {
    this.encodeArrayLength(array);
    for (int element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(long[] array) {
    this.encodeArrayLength(array);
    for (long element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(short[] array) {
    this.encodeArrayLength(array);
    for (short element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(String[] array) {
    this.encodeArrayLength(array);
    for (String element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(Enum<?>[] array) {
    this.encodeArrayLength(array);
    for (Enum<?> element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(UUID[] array) {
    this.encodeArrayLength(array);
    for (UUID element : array) {
      this.encode(element);
    }
  }

  @Override
  public final void encode(BinaryEncodableAndDecodable[] array) {
    this.encodeArrayLength(array);
    for (BinaryEncodableAndDecodable element : array) {
      this.encode(element);
      //array[ i ].encode( this );
    }
  }

  @Override
  public final void encode(ReferenceableBinaryEncodableAndDecodable[] array, Map<ReferenceableBinaryEncodableAndDecodable, Integer> map) {
    this.encodeArrayLength(array);
    for (ReferenceableBinaryEncodableAndDecodable element : array) {
      this.encode(element, map);
      //array[ i ].encode( this, map );
    }
  }

  @Override
  public final void encode(Enum<?> value) {
    boolean isNotNull = value != null;
    this.encode(isNotNull);
    if (isNotNull) {
      this.encode(value.getClass().getName());
      this.encode(value.name());
    }
  }

  @Override
  public final void encode(UUID value) {
    boolean isNotNull = value != null;
    this.encode(isNotNull);
    if (isNotNull) {
      this.encode(value.getMostSignificantBits());
      this.encode(value.getLeastSignificantBits());
    }
  }

  @Override
  public final void encode(BinaryEncodableAndDecodable value) {
    if (value != null) {
      this.encode(value.getClass().getName());
      value.encode(this);
    } else {
      this.encode("");
    }
  }

  @Override
  public final void encode(ReferenceableBinaryEncodableAndDecodable value, Map<ReferenceableBinaryEncodableAndDecodable, Integer> map) {
    if (value != null) {
      this.encode(value.getClass().getName());
      this.encode(value.hashCode());
      if (!map.containsKey(value)) {
        map.put(value, value.hashCode());
        value.encode(this, map);
      }
    } else {
      encode("");
    }
  }

  public void encodeProperties(InstancePropertyOwner owner, Map<ReferenceableBinaryEncodableAndDecodable, Integer> map) {
    for (InstanceProperty<?> property : owner.getProperties()) {
      encode(property.getName());
      Object value = property.getValue();
      if (value != null) {
        Class<?> valueCls = value.getClass();
        encode(valueCls.getName());
        if (valueCls.isArray()) {
          if (boolean[].class == valueCls) {
            encode((boolean[]) value);
          } else if (byte[].class == valueCls) {
            encode((byte[]) value);
          } else if (char[].class == valueCls) {
            encode((char[]) value);
          } else if (double[].class == valueCls) {
            encode((double[]) value);
          } else if (float[].class == valueCls) {
            encode((float[]) value);
          } else if (int[].class == valueCls) {
            encode((int[]) value);
          } else if (long[].class == valueCls) {
            encode((long[]) value);
          } else if (short[].class == valueCls) {
            encode((short[]) value);
          } else if (String[].class == valueCls) {
            encode((String[]) value);
          } else if (Enum[].class.isAssignableFrom(valueCls)) {
            encode((Enum[]) value);
          } else if (BinaryEncodableAndDecodable[].class.isAssignableFrom(valueCls)) {
            encode((BinaryEncodableAndDecodable[]) value);
          } else if (ReferenceableBinaryEncodableAndDecodable[].class.isAssignableFrom(valueCls)) {
            encode((ReferenceableBinaryEncodableAndDecodable[]) value, map);
          } else {
            int length = Array.getLength(value);
            encode(length);
            for (int i = 0; i < length; i++) {
              encodeObject(Array.get(value, i), map);
            }
          }
        } else if (Collection.class.isAssignableFrom(valueCls)) {
          Collection<?> collection = (Collection<?>) value;
          int size = collection.size();
          encode(size);
          for (Object o : collection) {
            encodeObject(o, map);
          }
        } else {
          encodeObject(value, map);
        }
      } else {
        encode("");
      }
    }
    encode("");
  }

  private void encodeObject(Object value, Map<ReferenceableBinaryEncodableAndDecodable, Integer> map) {
    if (value != null) {
      Class<?> valueCls = value.getClass();

      if (valueCls.isRecord()) {
        encodeRecord((Record) value);
      } else if (BinaryEncodableAndDecodable.class.isAssignableFrom(valueCls)) {
        encode((BinaryEncodableAndDecodable) value);
      } else if (ReferenceableBinaryEncodableAndDecodable.class.isAssignableFrom(valueCls)) {
        encode((ReferenceableBinaryEncodableAndDecodable) value, map);
      } else if (ByteBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(this, (ByteBuffer) value);
      } else if (CharBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(this, (CharBuffer) value);
      } else if (ShortBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(this, (ShortBuffer) value);
      } else if (IntBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(this, (IntBuffer) value);
      } else if (LongBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(this, (LongBuffer) value);
      } else if (FloatBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(this, (FloatBuffer) value);
      } else if (DoubleBuffer.class.isAssignableFrom(valueCls)) {
        BufferUtilities.encode(this, (DoubleBuffer) value);
      } else if (Boolean.class == valueCls) {
        encode((Boolean) value);
      } else if (Byte.class == valueCls) {
        encode((Byte) value);
      } else if (Character.class == valueCls) {
        encode((Character) value);
      } else if (Double.class == valueCls) {
        encode((Double) value);
      } else if (Float.class == valueCls) {
        encode((Float) value);
      } else if (Integer.class == valueCls) {
        encode((Integer) value);
      } else if (Long.class == valueCls) {
        encode((Long) value);
      } else if (Short.class == valueCls) {
        encode((Short) value);
      } else if (String.class == valueCls) {
        encode((String) value);
      } else if (Enum.class.isAssignableFrom(valueCls)) {
        encode((Enum) value);
      } else {
        throw new RuntimeException(value.getClass().getName() + " " + value.toString());
      }
    } else {
      encode("");
    }
  }
}
