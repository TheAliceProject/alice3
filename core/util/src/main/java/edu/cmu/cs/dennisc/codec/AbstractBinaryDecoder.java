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

import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import org.alice.math.immutable.*;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.*;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractBinaryDecoder implements BinaryDecoder {
  //todo: handle null arrays
  private Object createArray(Class<?> componentType) {
    int length = this.decodeInt();
    if (length != -1) {
      return Array.newInstance(componentType, length);
    } else {
      return null;
    }
  }

  @Override
  public final boolean[] decodeBooleanArray() {
    boolean[] rv = (boolean[]) createArray(Boolean.TYPE);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeBoolean();
    }
    return rv;
  }

  @Override
  public final byte[] decodeByteArray() {
    byte[] rv = (byte[]) createArray(Byte.TYPE);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeByte();
    }
    return rv;
  }

  @Override
  public final char[] decodeCharArray() {
    char[] rv = (char[]) createArray(Character.TYPE);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeChar();
    }
    return rv;
  }

  @Override
  public final double[] decodeDoubleArray() {
    double[] rv = (double[]) createArray(Double.TYPE);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeDouble();
    }
    return rv;
  }

  @Override
  public final float[] decodeFloatArray() {
    float[] rv = (float[]) createArray(Float.TYPE);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeFloat();
    }
    return rv;
  }

  @Override
  public final int[] decodeIntArray() {
    int[] rv = (int[]) createArray(Integer.TYPE);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeInt();
    }
    return rv;
  }

  @Override
  public final long[] decodeLongArray() {
    long[] rv = (long[]) createArray(Long.TYPE);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeLong();
    }
    return rv;
  }

  @Override
  public final short[] decodeShortArray() {
    short[] rv = (short[]) createArray(Short.TYPE);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeShort();
    }
    return rv;
  }

  @Override
  public final String[] decodeStringArray() {
    String[] rv = (String[]) createArray(String.class);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeString();
    }
    return rv;
  }

  @Override
  public final <E extends Enum<E>> E[] decodeEnumArray(Class<E> cls) {
    E[] rv = (E[]) createArray(cls);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeEnum(cls);
    }
    return rv;
  }

  @Override
  public final UUID[] decodeIdArray() {
    UUID[] rv = (UUID[]) createArray(UUID.class);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeId();
    }
    return rv;
  }

  @Override
  public final <E extends BinaryEncodableAndDecodable> E[] decodeBinaryEncodableAndDecodableArray(Class<E> componentCls) {
    E[] rv = (E[]) createArray(componentCls);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = (E) decodeBinaryEncodableAndDecodable();
    }
    return rv;
  }

  @Override
  public final <E extends ReferenceableBinaryEncodableAndDecodable> E[] decodeReferenceableBinaryEncodableAndDecodableArray(Class<E> componentCls, Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    E[] rv = (E[]) createArray(componentCls);
    for (int i = 0; i < rv.length; i++) {
      rv[i] = decodeReferenceableBinaryEncodableAndDecodable(componentCls, map);
    }
    return rv;
  }

  @Override
  public final <E extends Enum<E>> E decodeEnum() {
    boolean isNotNull = decodeBoolean();
    if (isNotNull) {
      String clsName = decodeString();
      String name = decodeString();
      Class<E> clsActual = (Class<E>) ReflectionUtilities.getClassForName(clsName);
      return Enum.valueOf(clsActual, name);
    } else {
      return null;
    }
  }

  @Deprecated
  public final <E extends Enum<E>> E decodeEnum(Class<E> cls) {
    E rv = decodeEnum();
    assert cls.isInstance(rv);
    return rv;
  }

  @Override
  public final UUID decodeId() {
    boolean isNotNull = this.decodeBoolean();
    if (isNotNull) {
      long mostSigBits = this.decodeLong();
      long leastSigBits = this.decodeLong();
      return new UUID(mostSigBits, leastSigBits);
    } else {
      return null;
    }
  }

  private <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable(Class<?>[] parameterTypes, Object[] args) {
    String storedClassName = decodeString();

    if (storedClassName.isEmpty()) {
      return null;
    }
    try {
      if (storedClassName.startsWith("edu.cmu.cs.dennisc.math.")) {
        return (E) decodeObsoleteClass(storedClassName);
      }
      Class<E> cls = (Class<E>) Class.forName(storedClassName);
      if (cls.isRecord()) {
        return decodeRecord();
      }
      try {
        return instantiateWithConstructor(cls, parameterTypes, args);
      } catch (NoSuchMethodException nsme) {
        return instantiateWithDecode(cls);
      }
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(cnfe);
    }
  }

  // Catch older classes to be replaced so earlier files can still be read.
  // For this to work the return values continue to implement BinaryEncodableAndDecodable
  // although the encode methods should never be called
  private BinaryEncodableAndDecodable decodeObsoleteClass(String storedClassName) {
    switch (storedClassName) {
      case "edu.cmu.cs.dennisc.math.EulerAngles" -> {
        return new EulerAngles(decodeAngle(), decodeAngle(), decodeAngle(), decodeEnum());
      }
      case "edu.cmu.cs.dennisc.math.Matrix3x3" -> {
        return Matrix3x3.create(decodeVector3(), decodeVector3(), decodeVector3());
      }
      case "edu.cmu.cs.dennisc.math.AxisAlignedBox" -> {
        return new AxisAlignedBox(decodePoint3(), decodePoint3());
      }
      case "edu.cmu.cs.dennisc.math.AffineMatrix4x4" -> {
        return new AffineMatrix4x4((OrthogonalMatrix3x3) Matrix3x3.create(decodeVector3(), decodeVector3(), decodeVector3()), decodePoint3());
      }
      case "edu.cmu.cs.dennisc.math.Vector3f" -> {
        return new Vector3f(decodeFloat(), decodeFloat(), decodeFloat());
      }
    }
    throw new RuntimeException("Unexpected math class : " + storedClassName);
  }

  private Vector3 decodeVector3() {
    return new Vector3(decodeDouble(), decodeDouble(), decodeDouble());
  }

  private Point3 decodePoint3() {
    return new Point3(decodeDouble(), decodeDouble(), decodeDouble());
  }

  private Angle decodeAngle() {
    return new AngleInRadians(decodeDouble());
  }

  private static <E extends BinaryEncodableAndDecodable> E instantiateWithConstructor(Class<E> cls, Class<?>[] parameterTypes, Object[] args) throws ClassNotFoundException, NoSuchMethodException {
    Constructor<E> cnstrctr = cls.getConstructor(parameterTypes);
    return ReflectionUtilities.newInstance(cnstrctr, args);
  }

  private <E extends BinaryEncodableAndDecodable> E instantiateWithDecode(Class<E> cls) {
    Constructor<E> cnstrctr = ReflectionUtilities.getConstructor(cls);
    E newInstance = ReflectionUtilities.newInstance(cnstrctr);

    Method decode = ReflectionUtilities.getMethod(cls, "decode", BinaryDecoder.class);
    ReflectionUtilities.invoke(newInstance, decode, this);
    return newInstance;
  }

  private static final Class<?>[] EMPTY_PARAMETER_TYPES = {BinaryDecoder.class};
  private static final Class<?>[] OBJECT_PARAMETER_TYPES = {BinaryDecoder.class, Object.class};

  @Override
  public final <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable() {
    return (E) decodeBinaryEncodableAndDecodable(EMPTY_PARAMETER_TYPES, new Object[] {this});
  }

  @Override
  public final <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable(Object context) {
    return (E) decodeBinaryEncodableAndDecodable(OBJECT_PARAMETER_TYPES, new Object[] {this, context});
  }
  @Override
  public final <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable(Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    String clsName = decodeString();
    if (clsName.isEmpty()) {
      return null;
    }
    int reference = decodeInt();
    if (map.containsKey(reference)) {
      return (E) map.get(reference);
    } else {
      E instance = (E) ReflectionUtilities.newInstance(clsName);
      map.put(reference, instance);
      instance.decode(this, map);
      return instance;
    }
  }

  @Deprecated
  public final <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable(Class<E> cls, Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    E rv = (E) decodeReferenceableBinaryEncodableAndDecodable(map);
    assert cls.isInstance(rv);
    return rv;
  }

  @Override
  public void decodeProperties(InstancePropertyOwner owner, Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    while (true) {
      String propertyName = decodeString();
      if (propertyName.isEmpty()) {
        break;
      }
      InstanceProperty property = owner.getPropertyNamed(propertyName);
      assert property != null;
      property.setValue(decodeValue(map));
    }
  }

  private Object decodeValue(Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    String valueClsName = decodeString();
    assert valueClsName != null;
    if (valueClsName.isEmpty()) {
      return null;
    }
    Class<?> valueCls = ReflectionUtilities.getClassForName(valueClsName);
    if (valueCls.isArray()) {
      return decodeArray(map, valueCls);
    }
    if (Collection.class.isAssignableFrom(valueCls)) {
      int size = decodeInt();
      var collection = (Collection) ReflectionUtilities.newInstance(valueCls);
      for (int i = 0; i < size; i++) {
        String componentTypeName = decodeString();
        Class<?> componentType = ReflectionUtilities.getClassForName(componentTypeName);
        collection.add(decodeObject(componentType, map));
      }
      return null;
    }
    return decodeObject(valueCls, map);
  }

  private Object decodeArray(Map<Integer, ReferenceableBinaryEncodableAndDecodable> map, Class valueCls) {
    if (boolean[].class == valueCls) {
      return decodeBooleanArray();
    }
    if (byte[].class == valueCls) {
      return decodeByteArray();
    }
    if (char[].class == valueCls) {
      return decodeCharArray();
    }
    if (double[].class == valueCls) {
      return decodeDoubleArray();
    }
    if (float[].class == valueCls) {
      return decodeFloatArray();
    }
    if (int[].class == valueCls) {
      return decodeIntArray();
    }
    if (long[].class == valueCls) {
      return decodeLongArray();
    }
    if (short[].class == valueCls) {
      return decodeShortArray();
    }
    if (String[].class == valueCls) {
      return decodeStringArray();
    }
    if (Enum[].class.isAssignableFrom(valueCls)) {
      return decodeEnumArray(valueCls.getComponentType());
    }
    if (BinaryEncodableAndDecodable[].class.isAssignableFrom(valueCls)) {
      return decodeBinaryEncodableAndDecodableArray(valueCls.getComponentType());
    }
    if (ReferenceableBinaryEncodableAndDecodable[].class.isAssignableFrom(valueCls)) {
      return decodeReferenceableBinaryEncodableAndDecodableArray(valueCls.getComponentType(), map);
    }
    int length = decodeInt();
    Object value = Array.newInstance(valueCls.getComponentType(), length);
    for (int i = 0; i < length; i++) {
      Array.set(value, i, decodeObject(valueCls.getComponentType(), map));
    }
    return value;
  }

  private Object decodeObject(Class valueCls, Map<Integer, ReferenceableBinaryEncodableAndDecodable> map) {
    if (BinaryEncodableAndDecodable.class.isAssignableFrom(valueCls)) {
      return decodeBinaryEncodableAndDecodable();
    }
    if (ReferenceableBinaryEncodableAndDecodable.class.isAssignableFrom(valueCls)) {
      return decodeReferenceableBinaryEncodableAndDecodable(map);
    }
    if (ByteBuffer.class.isAssignableFrom(valueCls)) {
      return BufferUtilities.decodeByteBuffer(this);
    }
    if (CharBuffer.class.isAssignableFrom(valueCls)) {
      return BufferUtilities.decodeCharBuffer(this);
    }
    if (ShortBuffer.class.isAssignableFrom(valueCls)) {
      return BufferUtilities.decodeShortBuffer(this);
    }
    if (IntBuffer.class.isAssignableFrom(valueCls)) {
      return BufferUtilities.decodeIntBuffer(this);
    }
    if (LongBuffer.class.isAssignableFrom(valueCls)) {
      return BufferUtilities.decodeLongBuffer(this);
    }
    if (FloatBuffer.class.isAssignableFrom(valueCls)) {
      return BufferUtilities.decodeFloatBuffer(this);
    }
    if (DoubleBuffer.class.isAssignableFrom(valueCls)) {
      return BufferUtilities.decodeDoubleBuffer(this);
    }
    if (Boolean.class == valueCls) {
      return decodeBoolean();
    }
    if (Byte.class == valueCls) {
      return decodeByte();
    }
    if (Character.class == valueCls) {
      return decodeChar();
    }
    if (Double.class == valueCls) {
      return decodeDouble();
    }
    if (Float.class == valueCls) {
      return decodeFloat();
    }
    if (Integer.class == valueCls) {
      return decodeInt();
    }
    if (Long.class == valueCls) {
      return decodeLong();
    }
    if (Short.class == valueCls) {
      return decodeShort();
    }
    if (String.class == valueCls) {
      return decodeString();
    }
    if (Enum.class.isAssignableFrom(valueCls)) {
      return this.<Enum>decodeEnum();
    }
    throw new RuntimeException(valueCls.getName());
  }
}
