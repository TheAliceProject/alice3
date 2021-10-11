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
package edu.cmu.cs.dennisc.java.lang.reflect;

import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public final class ReflectionUtilities {
  //  private static java.util.HashMap< String, Class< ? >> s_primativeTypeMap = new java.util.HashMap< String, Class< ? >>();
  //  static {
  //    s_primativeTypeMap.put( Void.TYPE.getName(), Void.TYPE );
  //    s_primativeTypeMap.put( Boolean.TYPE.getName(), Boolean.TYPE );
  //    s_primativeTypeMap.put( Byte.TYPE.getName(), Byte.TYPE );
  //    s_primativeTypeMap.put( Character.TYPE.getName(), Character.TYPE );
  //    s_primativeTypeMap.put( Short.TYPE.getName(), Short.TYPE );
  //    s_primativeTypeMap.put( Integer.TYPE.getName(), Integer.TYPE );
  //    s_primativeTypeMap.put( Long.TYPE.getName(), Long.TYPE );
  //    s_primativeTypeMap.put( Double.TYPE.getName(), Double.TYPE );
  //    s_primativeTypeMap.put( Float.TYPE.getName(), Float.TYPE );
  //  }
  private ReflectionUtilities() {
  }

  public static boolean isPublic(Member mmbr) {
    return Modifier.isPublic(mmbr.getModifiers());
  }

  public static boolean isProtected(Member mmbr) {
    return Modifier.isProtected(mmbr.getModifiers());
  }

  public static boolean isPrivate(Member mmbr) {
    return Modifier.isPrivate(mmbr.getModifiers());
  }

  public static boolean isAbstract(Member mmbr) {
    return Modifier.isAbstract(mmbr.getModifiers());
  }

  public static boolean isAbstract(Class<?> cls) {
    return Modifier.isAbstract(cls.getModifiers());
  }

  public static boolean isFinal(Class<?> cls) {
    return Modifier.isFinal(cls.getModifiers());
  }

  public static boolean isStatic(Class<?> cls) {
    return Modifier.isStatic(cls.getModifiers());
  }

  public static boolean isStatic(Method mthd) {
    return Modifier.isStatic(mthd.getModifiers());
  }

  public static boolean isStatic(Field fld) {
    return Modifier.isStatic(fld.getModifiers());
  }

  //todo: add the rest of the modifiers

  @Deprecated
  public static Class<?> getClassForName(String className) {
    try {
      return ClassUtilities.forName(className);
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(className, cnfe);
    }
    //    assert className != null;
    //    assert className.length() > 0;
    //    try {
    //      return Class.forName( className );
    //    } catch( ClassNotFoundException cnfe ) {
    //      if( s_primativeTypeMap.containsKey( className ) ) {
    //        return s_primativeTypeMap.get( className );
    //      } else {
    //        throw new RuntimeException( className, cnfe );
    //      }
    //    }
  }

  public static Class<?> getArrayClass(Class<?> componentCls) {
    Object array = Array.newInstance(componentCls, 0);
    return array.getClass();
  }

  public static Class<?> getArrayClass(Class<?> componentCls, int dimensionCount) {
    assert dimensionCount > 0;
    Class<?> rv = componentCls;
    for (int i = 0; i < dimensionCount; i++) {
      rv = getArrayClass(rv);
    }
    return rv;
  }

  public static <T> T newInstance(Constructor<T> cnstrctr, Object... arguments) {
    try {
      return cnstrctr.newInstance(arguments);
    } catch (InvocationTargetException ite) {
      throw new RuntimeException(cnstrctr.toString(), ite);
    } catch (InstantiationException ie) {
      Logger.throwable(ie, cnstrctr);
      throw new RuntimeException(cnstrctr.toString(), ie);
    } catch (IllegalAccessException iae) {
      throw new RuntimeException(cnstrctr.toString(), iae);
    } catch (IllegalArgumentException iae) {
      throw new RuntimeException(cnstrctr.toString(), iae);
    }
  }

  public static <T> T newInstance(Class<T> cls, Class<?>[] parameterClses, Object... arguments) {
    return newInstance(getConstructor(cls, parameterClses), arguments);
  }

  public static <T> T newInstanceForArguments(Class<T> cls, Object... arguments) {
    return newInstance(getConstructorForArguments(cls, arguments), arguments);
  }

  public static <T> T newInstance(Class<T> cls) {
    try {
      return cls.newInstance();
    } catch (InstantiationException ie) {
      throw new RuntimeException(cls.getName(), ie);
    } catch (IllegalAccessException iae) {
      throw new RuntimeException(cls.getName(), iae);
    }
  }

  public static Object newInstance(String className) {
    return newInstance(getClassForName(className));
  }

  private static Object newArrayInstance(Class<?> componentType, int length) {
    try {
      return Array.newInstance(componentType, length);
    } catch (NegativeArraySizeException nase) {
      throw new RuntimeException(nase);
    }
  }

  public static Object newArrayInstance(String componentTypeName, int length) {
    return newArrayInstance(getClassForName(componentTypeName), length);
  }

  public static <T> T[] newTypedArrayInstance(Class<T> componentType, int length) {
    return (T[]) newArrayInstance(componentType, length);
  }

  public static Field getField(Class<?> cls, String name) {
    try {
      return cls.getField(name);
    } catch (NoSuchFieldException nsfe) {
      throw new RuntimeException(nsfe);
    }
  }

  public static Field getDeclaredField(Class<?> cls, String name) {
    try {
      return cls.getDeclaredField(name);
    } catch (NoSuchFieldException nsfe) {
      throw new RuntimeException(nsfe);
    }
  }

  public static Method getMethod(Class<?> cls, String name, Class<?>... parameterTypes) {
    try {
      return cls.getMethod(name, parameterTypes);
    } catch (NoSuchMethodException nsme) {
      throw new RuntimeException(nsme);
    }
  }

  public static Method getDeclaredMethod(Class<?> cls, String name, Class<?>... parameterTypes) {
    try {
      return cls.getDeclaredMethod(name, parameterTypes);
    } catch (NoSuchMethodException nsme) {
      throw new RuntimeException(nsme);
    }
  }

  public static <T> Constructor<T> getConstructor(Class<T> cls, Class<?>... parameterClses) {
    try {
      return cls.getConstructor(parameterClses);
    } catch (NoSuchMethodException nsme) {
      StringBuilder sb = new StringBuilder();
      sb.append(cls.getName());
      for (Class<?> parameterCls : parameterClses) {
        sb.append(" ");
        sb.append(parameterCls == null ? "null" : parameterCls.getName());
      }
      throw new RuntimeException(sb.toString(), nsme);
    }
  }

  public static <T> Constructor<T> getDeclaredConstructor(Class<T> cls, Class<?>... parameterClses) {
    try {
      return cls.getDeclaredConstructor(parameterClses);
    } catch (NoSuchMethodException nsme) {
      throw new RuntimeException(cls.getName(), nsme);
    }
  }

  private static <T> Constructor<T> getConstructorForArguments(Constructor<T>[] constructors, Object... arguments) throws NoSuchMethodException {
    Constructor<T> rv = null;
    for (Constructor<T> constructor : constructors) {
      Class<?>[] parameterClses = constructor.getParameterTypes();
      if (parameterClses.length == arguments.length) {
        if (rv != null) {
          throw new RuntimeException("more than one constructor matches arguments");
        } else {
          rv = constructor;
        }
      }
    }
    if (rv != null) {
      return rv;
    } else {
      throw new NoSuchMethodException();
    }
  }

  public static <T> Constructor<T> getConstructorForArguments(Class<T> cls, Object... arguments) {
    try {
      return getConstructorForArguments((Constructor<T>[]) cls.getConstructors(), arguments);
    } catch (NoSuchMethodException nsme) {
      throw new RuntimeException(cls.getName(), nsme);
    }
  }

  public static <T> Constructor<T> getDeclaredConstructorForArguments(Class<T> cls, Object... arguments) {
    try {
      return getConstructorForArguments((Constructor<T>[]) cls.getDeclaredConstructors(), arguments);
    } catch (NoSuchMethodException nsme) {
      throw new RuntimeException(cls.getName(), nsme);
    }
  }

  public static String getDetail(Object instance, Method method, Object[] args) {
    StringBuilder sb = new StringBuilder();
    sb.append("instance=");
    sb.append(instance);
    sb.append(";method=");
    if (method != null) {
      sb.append(method.getName());
    } else {
      sb.append("null");
    }
    sb.append(";args={");
    String separator = " ";
    for (Object arg : args) {
      sb.append(separator);
      sb.append(arg);
      separator = ", ";
    }
    sb.append(" }");
    return sb.toString();
  }

  public static Object invoke(Object instance, Method method, Object... args) {
    try {
      return method.invoke(instance, args);
    } catch (IllegalAccessException iae) {
      throw new RuntimeException(getDetail(instance, method, args), iae);
    } catch (InvocationTargetException ite) {
      throw new RuntimeException(getDetail(instance, method, args), ite);
    } catch (RuntimeException re) {
      throw new RuntimeException(getDetail(instance, method, args), re);
    }
  }

  public static Object get(Field field, Object o) {
    try {
      return field.get(o);
    } catch (IllegalArgumentException iae) {
      throw new RuntimeException(iae);
    } catch (IllegalAccessException iae) {
      throw new RuntimeException(iae);
    }
  }

  public static void set(Field field, Object o, Object value) {
    try {
      field.set(o, value);
    } catch (IllegalArgumentException iae) {
      throw new RuntimeException(iae);
    } catch (IllegalAccessException iae) {
      throw new RuntimeException(iae);
    }
  }

  private static enum InclusionLevel {
    ALL_DECLARED() {
      @Override
      public Field[] getFields(Class<?> cls) {
        return cls.getDeclaredFields();
      }
    }, PUBLIC_INCLUDING_INHERITED {
      @Override
      public Field[] getFields(Class<?> cls) {
        return cls.getFields();
      }
    };

    public abstract Field[] getFields(Class<?> cls);
  }

  private static List<Field> getFields(Class<?> cls, Class<?> clsAssignable, InclusionLevel inclusionLevel, int modifierMask) {
    List<Field> rv = new LinkedList<Field>();
    Field[] fields = inclusionLevel.getFields(cls);
    for (Field field : fields) {
      if (clsAssignable.isAssignableFrom(field.getType())) {
        if ((field.getModifiers() & modifierMask) == modifierMask) {
          rv.add(field);
        }
      }
    }
    return rv;
  }

  //todo
  public static List<Field> getPublicFinalFields(Class<?> cls, Class<?> clsAssignable) {
    return getFields(cls, clsAssignable, InclusionLevel.PUBLIC_INCLUDING_INHERITED, Modifier.PUBLIC | Modifier.FINAL);
  }

  //todo
  public static List<Field> getPublicStaticFinalFields(Class<?> cls, Class<?> clsAssignable) {
    return getFields(cls, clsAssignable, InclusionLevel.PUBLIC_INCLUDING_INHERITED, Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
  }

  //todo
  public static List<Field> getPublicFinalDeclaredFields(Class<?> cls, Class<?> clsAssignable) {
    return getFields(cls, clsAssignable, InclusionLevel.ALL_DECLARED, Modifier.PUBLIC | Modifier.FINAL);
  }

  //todo
  public static List<Field> getPublicStaticFinalDeclaredFields(Class<?> cls, Class<?> clsAssignable) {
    return getFields(cls, clsAssignable, InclusionLevel.ALL_DECLARED, Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
  }

  private static <E> List<E> getInstances(Class<?> cls, Class<E> clsAssignable, int modifierMask) {
    List<E> rv = new LinkedList<E>();
    Field[] fields = cls.getFields();
    for (Field field : fields) {
      if (clsAssignable.isAssignableFrom(field.getType())) {
        if ((field.getModifiers() & modifierMask) == modifierMask) {
          E e = (E) ReflectionUtilities.get(field, null);
          rv.add(e);
        }
      }
    }
    return rv;
  }

  //todo
  public static <E> List<E> getPublicFinalInstances(Class<?> cls, Class<E> clsAssignable) {
    return getInstances(cls, clsAssignable, Modifier.PUBLIC | Modifier.FINAL);
  }

  //todo
  public static <E> List<E> getPublicStaticFinalInstances(Class<?> cls, Class<E> clsAssignable) {
    return getInstances(cls, clsAssignable, Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
  }

  public static <T> T valueOf(Class<T> cls, String s) {
    Method mthd = getMethod(cls, "valueOf", new Class[] {String.class});
    return (T) invoke(null, mthd, new Object[] {s});
  }
}
