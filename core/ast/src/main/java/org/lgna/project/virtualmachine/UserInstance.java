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

package org.lgna.project.virtualmachine;

import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.project.ast.*;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class UserInstance {
  public static Object getJavaInstanceIfNecessary(Object instance) {
    if (instance instanceof UserInstance) {
      UserInstance userInstance = (UserInstance) instance;
      return userInstance.getJavaInstance();
    } else if (instance instanceof UserArrayInstance) {
      UserArrayInstance userArrayInstance = (UserArrayInstance) instance;
      int length = userArrayInstance.getLength();
      UserArrayType type = userArrayInstance.getType();
      AbstractType<?, ?, ?> componentType = type.getComponentType();
      Class<?> componentCls = componentType.getFirstEncounteredJavaType().getClassReflectionProxy().getReification();
      Object[] rv = (Object[]) Array.newInstance(componentCls, length);
      for (int i = 0; i < length; i++) {
        rv[i] = getJavaInstanceIfNecessary(userArrayInstance.get(i));
      }
      return rv;
    } else {
      return instance;
    }
  }

  public static Object[] updateArrayWithInstancesInJavaIfNecessary(Object[] rv) {
    for (int i = 0; i < rv.length; i++) {
      rv[i] = getJavaInstanceIfNecessary(rv[i]);
    }
    return rv;
  }

  public static UserInstance createInstance(VirtualMachine vm, NamedUserConstructor constructor, UserType<?> fallbackType, Object[] arguments) {
    return new UserInstance(vm, constructor, arguments, fallbackType, new HashMap<>());
  }

  private final VirtualMachine vm;
  private final Object nextInstance;
  private final UserType<?> type;
  private final Map<UserField, Object> fieldMap;
  // TODO Move this on to the scene, since that is the only user of this map
  private Map<Object, UserField> inverseFieldMap;

  private UserInstance(VirtualMachine vm, NamedUserConstructor constructor, Object[] arguments, UserType<?> fallbackType, Map<UserField, Object> fieldMap) {
    this.vm = vm;
    UserType<?> constType = constructor.getDeclaringType();

    this.type = (constType == null) ? fallbackType : constType;

    assert this.type != null : constructor.getId();

    this.fieldMap = fieldMap;

    ConstructorBlockStatement constructorBlockStatement = constructor.body.getValue();
    ConstructorInvocationStatement constructorInvocationStatement = constructorBlockStatement.constructorInvocationStatement.getValue();
    AbstractConstructor nextConstructor = constructorInvocationStatement.constructor.getValue();
    Map<AbstractParameter, Object> stackMap = new HashMap<AbstractParameter, Object>();
    for (int i = 0; i < arguments.length; i++) {
      stackMap.put(constructor.requiredParameters.get(i), arguments[i]);
    }
    NamedUserType type = (NamedUserType) constructor.getDeclaringType();
    vm.pushConstructorFrame(type, stackMap);
    try {
      Object[] nextArguments = vm.evaluateArguments(nextConstructor, constructorInvocationStatement.requiredArguments, constructorInvocationStatement.variableArguments, constructorInvocationStatement.keyedArguments);
      if (nextConstructor.isUserAuthored()) {
        this.nextInstance = new UserInstance(vm, (NamedUserConstructor) nextConstructor, nextArguments, fallbackType, fieldMap);
      } else {
        JavaConstructor nextConstructorDeclaredInJava = (JavaConstructor) nextConstructor;
        ConstructorReflectionProxy constructorReflectionProxy = nextConstructorDeclaredInJava.getConstructorReflectionProxy();
        Constructor<?> cnstrctr = constructorReflectionProxy.getReification();
        assert cnstrctr != null : constructorReflectionProxy.getDeclaringClassReflectionProxy().getName();
        this.nextInstance = vm.createInstance(this.type, this, cnstrctr, nextArguments);
      }
      vm.setConstructorFrameUserInstance(this);
      for (AbstractField field : this.type.getDeclaredFields()) {
        assert field instanceof UserField;
        UserField userField = (UserField) field;
        vm.createAndSetFieldInstance(this, userField);
      }
      try {
        vm.execute(constructorBlockStatement);
      } catch (ReturnException re) {
        throw new RuntimeException(re);
      }
    } finally {
      vm.popFrame();
    }
  }

  public VirtualMachine getVM() {
    return this.vm;
  }

  public void ensureInverseMapExists() {
    if (inverseFieldMap == null) {
      inverseFieldMap = Maps.newHashMap();
      for (UserField field : fieldMap.keySet()) {
        Object value = fieldMap.get(field);
        addToInverseMapIfManaged(field, value);
      }
    }
  }

  private void addToInverseMapIfManaged(UserField field, Object value) {
    if (field.getManagementLevel() == ManagementLevel.MANAGED) {
      inverseFieldMap.put(getJavaInstanceIfNecessary(value), field);
    }
  }

  public UserType<?> getType() {
    return this.type;
  }

  public Object getJavaInstance() {
    return getJavaInstanceIfNecessary(this.nextInstance);
  }

  public <E> E getJavaInstance(Class<E> cls) {
    return ClassUtilities.getInstance(this.getJavaInstance(), cls);
  }

  public Object getFieldValue(UserField field) {
    return this.fieldMap.get(field);
  }

  public Object getFieldValueInstanceInJava(UserField field) {
    return getJavaInstanceIfNecessary(this.getFieldValue(field));
  }

  public <E> E getFieldValueInstanceInJava(UserField field, Class<E> cls) {
    return ClassUtilities.getInstance(this.getFieldValueInstanceInJava(field), cls);
  }

  void setFieldValue(UserField field, Object value) {
    fieldMap.put(field, value);
    if (inverseFieldMap != null) {
      addToInverseMapIfManaged(field, value);
    }
  }

  public UserField ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava(Object key) {
    assert this.inverseFieldMap != null;
    return this.inverseFieldMap.get(key);
  }

  @Override
  public String toString() {
    if (this.nextInstance != null) {
      return this.nextInstance.toString();
    } else {
      return null;
    }
  }
}
