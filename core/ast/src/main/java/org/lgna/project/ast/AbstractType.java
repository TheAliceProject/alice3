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

package org.lgna.project.ast;

//todo: add generic capability

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractType<C extends AbstractConstructor, M extends AbstractMethod, F extends AbstractField> extends AbstractAccessibleDeclaration {
  public JavaType getFirstEncounteredJavaType() {
    AbstractType<?, ?, ?> type = this;
    while ((type instanceof JavaType) == false) {
      type = type.getSuperType();
    }
    return (JavaType) type;
  }

  protected abstract boolean isAssignableFromType(AbstractType<?, ?, ?> other);

  public final boolean isAssignableFrom(AbstractType<?, ?, ?> other) {
    if (other != null) {
      return JavaType.getWrapperTypeIfNecessary(this).isAssignableFromType(JavaType.getWrapperTypeIfNecessary(other));
    } else {
      return false;
    }
  }

  public final boolean isAssignableFrom(Class<?> other) {
    return isAssignableFrom(JavaType.getInstance(other));
  }

  public final boolean isAssignableTo(AbstractType<?, ?, ?> other) {
    if (other != null) {
      return other.isAssignableFrom(this);
    } else {
      return false;
    }
  }

  public final boolean isAssignableTo(Class<?> other) {
    return isAssignableTo(JavaType.getInstance(other));
  }

  public abstract boolean isFollowToSuperClassDesired();

  public abstract boolean isConsumptionBySubClassDesired();

  public abstract AbstractType<?, ?, ?> getKeywordFactoryType();

  public abstract AbstractPackage getPackage();

  public abstract AbstractType<?, ?, ?> getSuperType();

  public abstract AbstractType<?, ?, ?>[] getInterfaces();

  public abstract List<C> getDeclaredConstructors();

  public abstract List<M> getDeclaredMethods();

  public abstract List<F> getDeclaredFields();

  public abstract boolean isPrimitive();

  public abstract boolean isInterface();

  public abstract boolean isStatic();

  //cannot be final and abstract
  public abstract boolean isAbstract();

  public abstract boolean isFinal();

  public abstract boolean isStrictFloatingPoint();

  public abstract boolean isArray();

  public abstract boolean isEnum();

  public abstract AbstractType<?, ?, ?> getComponentType();

  @Override
  public void addModifiers(Collection<Modifier> modifiers) {
    super.addModifiers(modifiers);
    if (this.isFinal()) {
      modifiers.add(Modifier.FINAL);
    } else if (this.isAbstract()) {
      modifiers.add(Modifier.ABSTRACT);
    }
    if (this.isStatic()) {
      modifiers.add(Modifier.STATIC);
    }
    if (this.isStrictFloatingPoint()) {
      modifiers.add(Modifier.STRICTFP);
    }
  }

  public C getDeclaredConstructor(AbstractType<?, ?, ?>... parameterTypes) {
    C rv = null;
    for (C constructor : getDeclaredConstructors()) {
      List<? extends AbstractParameter> parameters = constructor.getRequiredParameters();
      if (parameters.size() == parameterTypes.length) {
        rv = constructor;
        for (int i = 0; i < parameterTypes.length; i++) {
          AbstractType<?, ?, ?> parameterType = parameterTypes[i];
          assert parameterType != null;
          if (parameterType.equals(parameters.get(i).getValueType())) {
            //pass
          } else {
            rv = null;
            break;
          }
        }
      }
      if (rv != null) {
        break;
      }
    }
    return rv;
  }

  public C getDeclaredConstructor(Class<?>... parameterClses) {
    return getDeclaredConstructor(JavaType.getInstances(parameterClses));
  }

  public C getDeclaredConstructor() {
    return getDeclaredConstructor(new AbstractType[] {});
  }

  public AbstractConstructor getFirstDeclaredConstructor() {
    List<? extends AbstractConstructor> constructors = getDeclaredConstructors();
    if (constructors.size() > 0) {
      return constructors.get(0);
    }
    return null;
  }

  public M getDeclaredMethod(String name, AbstractType<?, ?, ?>... parameterTypes) {
    M rv = null;
    for (M method : getDeclaredMethods()) {
      if (method.getName().equals(name)) {
        List<? extends AbstractParameter> requiredParameters = method.getRequiredParameters();
        AbstractParameter variableLengthParameter = method.getVariableLengthParameter();
        AbstractParameter keyedParameter = method.getKeyedParameter();
        //todo: check variableLengthParameter and keyedParameter
        final int N = requiredParameters.size();
        if ((N == parameterTypes.length) || (((N + 1) == parameterTypes.length) && ((variableLengthParameter != null) || (keyedParameter != null)))) {
          rv = method;
          for (int i = 0; i < requiredParameters.size(); i++) {
            AbstractType<?, ?, ?> parameterType = parameterTypes[i];
            if (parameterType.equals(requiredParameters.get(i).getValueType())) {
              //pass
            } else {
              rv = null;
              break;
            }
          }
        }
        if (rv != null) {
          break;
        }
      }
    }
    return rv;
  }

  public M getDeclaredMethod(String name, Class<?>... parameterClses) {
    return getDeclaredMethod(name, JavaType.getInstances(parameterClses));
  }

  public M getDeclaredMethod(String name) {
    return getDeclaredMethod(name, new AbstractType[] {});
  }

  public F getDeclaredField(AbstractType<?, ?, ?> valueType, String name) {
    F rv = null;
    for (F field : getDeclaredFields()) {
      if (field.getName().equals(name) && ((valueType == null) || field.getValueType().equals(valueType))) {
        rv = field;
        break;
      }
    }
    return rv;
  }

  public F getDeclaredField(Class<?> valueCls, String name) {
    return getDeclaredField(JavaType.getInstance(valueCls), name);
  }

  public F getDeclaredField(String name) {
    return getDeclaredField((AbstractType<?, ?, ?>) null, name);
  }

  public abstract AbstractType<?, ?, ?> getArrayType();

  public AbstractField findField(AbstractType<?, ?, ?> valueType, String name) {
    AbstractField rv = null;
    AbstractType<?, ?, ?> type = this;
    while (type != null) {
      AbstractField field = type.getDeclaredField(valueType, name);
      if (field != null) {
        rv = field;
        break;
      }
      type = type.getSuperType();
    }
    return rv;
  }

  public AbstractField findField(Class<?> valueCls, String name) {
    return findField(JavaType.getInstance(valueCls), name);

  }

  public AbstractField findField(String name) {
    return findField((AbstractType<?, ?, ?>) null, name);
  }

  public AbstractMethod findMethod(String name, AbstractType<?, ?, ?>... parameterTypes) {
    AbstractMethod rv = null;
    AbstractType<?, ?, ?> type = this;
    while (type != null) {
      AbstractMethod method = type.getDeclaredMethod(name, parameterTypes);
      if (method != null) {
        rv = method;
        break;
      }
      type = type.getSuperType();
    }
    return rv;
  }

  public AbstractMethod findMethod(String name, Class<?>... parameterClses) {
    return findMethod(name, JavaType.getInstances(parameterClses));
  }

  public AbstractMethod findMethod(String name) {
    return findMethod(name, new AbstractType<?, ?, ?>[] {});
  }

  @Override
  public String formatName(BinaryOperator<String> localizer) {
    return isArray() ? getComponentType().formatName(localizer) + "[]" : formatTypeName(localizer);
  }

  protected String formatTypeName(BinaryOperator<String> localizer) {
    return localizer.apply(getName(), getName());
  }
}
