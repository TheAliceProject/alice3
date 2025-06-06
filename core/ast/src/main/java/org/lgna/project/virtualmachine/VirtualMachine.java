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

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.IterableUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import org.lgna.common.EachInTogetherRunnable;
import org.lgna.common.ThreadUtilities;
import org.lgna.project.ast.*;
import org.lgna.project.virtualmachine.events.CountLoopIterationEvent;
import org.lgna.project.virtualmachine.events.EachInTogetherItemEvent;
import org.lgna.project.virtualmachine.events.ExpressionEvaluationEvent;
import org.lgna.project.virtualmachine.events.ForEachLoopIterationEvent;
import org.lgna.project.virtualmachine.events.StatementExecutionEvent;
import org.lgna.project.virtualmachine.events.VirtualMachineListener;
import org.lgna.project.virtualmachine.events.WhileLoopIterationEvent;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class VirtualMachine {
  public abstract LgnaStackTraceElement[] getStackTrace(Thread thread);

  protected abstract UserInstance getThis();

  protected abstract void pushBogusFrame(UserInstance instance);

  protected abstract void pushConstructorFrame(NamedUserType type, Map<AbstractParameter, Object> map);

  protected abstract void setConstructorFrameUserInstance(UserInstance instance);

  protected abstract void pushMethodFrame(UserInstance instance, UserMethod method, Map<AbstractParameter, Object> map);

  protected abstract void pushLambdaFrame(UserInstance instance, UserLambda lambda, AbstractMethod singleAbstractMethod, Map<AbstractParameter, Object> map);

  protected abstract void popFrame();

  protected abstract Object lookup(UserParameter parameter);

  protected abstract void pushLocal(UserLocal local, Object value);

  protected abstract Object getLocal(UserLocal local);

  protected abstract void setLocal(UserLocal local, Object value);

  protected abstract void popLocal(UserLocal local);

  //  protected abstract Frame createCopyOfCurrentFrame();
  protected abstract Frame getFrameForThread(Thread thread);

  protected abstract void pushCurrentThread(Frame frame);

  protected abstract void popCurrentThread();

  public Object[] ENTRY_POINT_evaluate(UserInstance instance, Expression[] expressions) {
    this.pushBogusFrame(instance);
    try {
      Object[] rv = new Object[expressions.length];
      for (int i = 0; i < expressions.length; i++) {
        rv[i] = this.evaluate(expressions[i]);
      }
      return rv;
    } finally {
      this.popFrame();
    }
  }

  public Object ENTRY_POINT_invoke(UserInstance target, AbstractMethod method, Object... arguments) {
    return invoke(target, method, arguments);
  }

  private NamedUserConstructor getConstructor(NamedUserType entryPointType, Object[] arguments) {
    for (NamedUserConstructor constructor : entryPointType.constructors) {
      List<? extends AbstractParameter> parameters = constructor.getRequiredParameters();
      if (parameters.size() == arguments.length) {
        //todo: check types
        return constructor;
      }
    }
    return null;
  }

  public UserInstance ENTRY_POINT_createInstance(NamedUserType entryPointType, Object... arguments) {
    return getConstructor(entryPointType, arguments).evaluate(this, null, arguments);
  }

  public Object createAndSetFieldInstance(UserInstance userInstance, UserField field) {
    Expression expression = field.initializer.getValue();
    assert expression != null;
    Object rv = this.evaluate(expression);
    userInstance.setFieldValue(field, rv);
    return rv;
  }

  public Object ACCEPTABLE_HACK_FOR_SCENE_EDITOR_initializeField(UserInstance instance, UserField field) {
    //pushCurrentThread( null );
    //try {
    this.pushBogusFrame(instance);
    try {
      return this.createAndSetFieldInstance(instance, field);
    } finally {
      this.popFrame();
    }
    //} finally {
    //  popCurrentThread();
    //}
  }

  public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_executeStatement(UserInstance instance, Statement statement) {
    assert (statement instanceof ReturnStatement) == false;
    //pushCurrentThread( null );
    //try {
    this.pushBogusFrame(instance);
    try {
      try {
        this.execute(statement);
      } catch (ReturnException re) {
        throw new AssertionError();
      }
    } finally {
      this.popFrame();
    }
    //} finally {
    //  popCurrentThread();
    //}
  }

  private final Map<Class<?>, Class<?>> mapAbstractClsToAdapterCls = Maps.newHashMap();

  public void registerAbstractClassAdapter(Class<?> abstractCls, Class<?> adapterCls) {
    if (ReflectionUtilities.isAbstract(abstractCls)) {
      //pass
    } else {
      Logger.severe(abstractCls);
    }
    this.mapAbstractClsToAdapterCls.put(abstractCls, adapterCls);
  }

  /* package-private */Object createInstance(UserType<?> type, final UserInstance userInstance, Constructor<?> cnstrctr, Object... arguments) {
    Class<?> cls = cnstrctr.getDeclaringClass();
    Class<?> adapterCls = this.mapAbstractClsToAdapterCls.get(cls);
    if (adapterCls != null) {
      MethodContext context = new MethodContext() {
        @Override
        public void invokeEntryPoint(AbstractMethod method, final Object... arguments) {
          VirtualMachine.this.ENTRY_POINT_invoke(userInstance, method, arguments);
        }
      };
      Class<?>[] parameterTypes = {MethodContext.class, UserType.class, Object[].class};
      Object[] args = {context, type, arguments};
      return ReflectionUtilities.newInstance(adapterCls, parameterTypes, args);
    } else {
      return ReflectionUtilities.newInstance(cnstrctr, arguments);
    }
  }

  private UserArrayInstance createUserArrayInstance(UserArrayType type, int[] lengths, Object[] values) {
    return new UserArrayInstance(type, lengths, values);
  }

  private Object createJavaArrayInstance(JavaType type, int[] lengths, Object[] values) {
    Class<?> cls = type.getClassReflectionProxy().getReification();
    assert cls != null;
    Class<?> componentCls = cls.getComponentType();
    assert componentCls != null;
    Object rv = Array.newInstance(componentCls, lengths);
    for (int i = 0; i < values.length; i++) {
      if (values[i] instanceof UserInstance) {
        UserInstance userValue = (UserInstance) values[i];
        values[i] = userValue.getJavaInstance();
      }
      Array.set(rv, i, values[i]);
    }
    return rv;
  }

  protected Object createArrayInstance(AbstractType<?, ?, ?> type, int[] lengths, Object... values) {
    assert type != null;
    if (type instanceof UserArrayType) {
      return this.createUserArrayInstance((UserArrayType) type, lengths, values);
    } else if (type instanceof JavaType) {
      return this.createJavaArrayInstance((JavaType) type, lengths, values);
    } else {
      throw new RuntimeException();
    }
  }

  private Object evaluateArgument(AbstractArgument argument) {
    assert argument != null;
    Expression expression = argument.expression.getValue();
    assert expression != null;
    if (expression instanceof LambdaExpression) {
      return this.EPIC_HACK_evaluateLambdaExpression((LambdaExpression) expression, argument);
    } else {
      return this.evaluate(expression);
    }
  }

  public Object[] evaluateArguments(AbstractCode code, NodeListProperty<SimpleArgument> arguments, NodeListProperty<SimpleArgument> variableArguments, NodeListProperty<JavaKeyedArgument> keyedArguments) {
    //todo: when variable length and keyed parameters are offered in the IDE (User) this code will need to be updated
    List<? extends AbstractParameter> requiredParameters = code.getRequiredParameters();
    AbstractParameter variableParameter = code.getVariableLengthParameter();
    AbstractParameter keyedParameter = code.getKeyedParameter();

    final int REQUIRED_N = arguments.size();
    assert requiredParameters.size() == REQUIRED_N : code.getName() + " " + requiredParameters.size() + " " + arguments.size();

    int length = REQUIRED_N;
    if (variableParameter != null) {
      length += 1;
    }
    if (keyedParameter != null) {
      length += 1;
    }
    Object[] rv = new Object[length];
    int rvIndex;
    for (rvIndex = 0; rvIndex < REQUIRED_N; rvIndex++) {
      rv[rvIndex] = this.evaluateArgument(arguments.get(rvIndex));
    }
    if (variableParameter != null) {
      final int VARIABLE_N = variableArguments.size();
      JavaType variableArrayType = variableParameter.getValueType().getFirstEncounteredJavaType();
      assert variableArrayType.isArray();
      Class<?> componentCls = variableArrayType.getComponentType().getClassReflectionProxy().getReification();
      Object array = Array.newInstance(componentCls, VARIABLE_N);
      for (int i = 0; i < VARIABLE_N; i++) {
        //todo: support primitive types
        Array.set(array, i, this.evaluateArgument(variableArguments.get(rvIndex)));
      }
      rv[rvIndex] = array;
    }
    if (keyedParameter != null) {
      final int KEYED_N = keyedArguments.size();
      JavaType keyedArrayType = keyedParameter.getValueType().getFirstEncounteredJavaType();
      assert keyedArrayType.isArray();
      Class<?> componentCls = keyedArrayType.getComponentType().getClassReflectionProxy().getReification();
      Object array = Array.newInstance(componentCls, KEYED_N);
      for (int i = 0; i < KEYED_N; i++) {
        //todo: support primitive types
        Array.set(array, i, this.evaluateArgument(keyedArguments.get(i)));
      }
      rv[rvIndex] = array;
      //
      //
      //      org.lgna.project.ast.AbstractParameter paramLast = requiredParameters.get( N-1 );
      //      if( paramLast.isVariableLength() ) {
      //        Class<?> arrayCls =  paramLast.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification();
      //        assert arrayCls != null;
      //        Class<?> componentCls = arrayCls.getComponentType();
      //        assert componentCls != null;
      //        rv[ N-1 ] = java.lang.reflect.Array.newInstance( componentCls, M );
      //        for( int j=0; j<( M - (N-1) ); j++ ) {
      //          org.lgna.project.ast.AbstractArgument argumentJ = arguments.get( (N-1) + j );
      //          assert argumentJ != null;
      //          Object valueJ = this.evaluate( argumentJ );
      //          assert valueJ != null;
      //          java.lang.reflect.Array.set( rv[ N-1 ], j, valueJ );
      //        }
      //      } else {
      //        rv[ N-1 ] = this.evaluate( arguments.get( N-1 ) );
      //      }
    }
    return rv;
  }

  protected Integer getArrayLength(Object array) {
    if (array != null) {
      if (array instanceof UserArrayInstance) {
        UserArrayInstance userArrayInstance = (UserArrayInstance) array;
        return userArrayInstance.getLength();
      } else {
        return Array.getLength(array);
      }
    } else {
      throw new NullPointerException();
    }
  }

  protected Object getUserField(UserField field, Object instance) {
    assert instance != null : field.getName();
    assert instance instanceof UserInstance;
    UserInstance userInstance = (UserInstance) instance;
    return userInstance.getFieldValue(field);
  }

  protected void setUserField(UserField field, Object instance, Object value) {
    assert instance instanceof UserInstance;
    UserInstance userInstance = (UserInstance) instance;
    userInstance.setFieldValue(field, value);
  }

  protected Object getFieldDeclaredInJavaWithField(JavaField field, Object instance) {
    instance = UserInstance.getJavaInstanceIfNecessary(instance);
    Field fld = field.getFieldReflectionProxy().getReification();
    assert fld != null : field.getFieldReflectionProxy();
    return ReflectionUtilities.get(fld, instance);
  }

  protected void setFieldDeclaredInJavaWithField(JavaField field, Object instance, Object value) {
    instance = UserInstance.getJavaInstanceIfNecessary(instance);
    Field fld = field.getFieldReflectionProxy().getReification();
    assert fld != null : field;
    ReflectionUtilities.set(fld, instance, value);
  }

  public Object get(AbstractField field, Object instance) {
    assert field != null;
    assert (instance != null) || field.isStatic() : field;
    if (field instanceof UserField) {
      return this.getUserField((UserField) field, instance);
    } else if (field instanceof JavaField) {
      return this.getFieldDeclaredInJavaWithField((JavaField) field, instance);
    } else {
      throw new RuntimeException();
    }
  }

  public void set(AbstractField field, Object instance, Object value) {
    assert field != null;
    if (field instanceof UserField) {
      this.setUserField((UserField) field, instance, value);
    } else if (field instanceof JavaField) {
      this.setFieldDeclaredInJavaWithField((JavaField) field, instance, value);
    } else {
      throw new RuntimeException();
    }
  }

  private void checkIndex(int index, int length) {
    if ((index < 0) || (length <= index)) {
      throw new LgnaVmArrayIndexOutOfBoundsException(this, index, length);
    }
  }

  private void checkNotNull(Object value, String message) {
    if (value == null) {
      throw new LgnaVmNullPointerException(message, this);
    }
  }

  public Object getItemAtIndex(AbstractType<?, ?, ?> arrayType, Object array, Integer index) {
    assert arrayType != null;
    assert arrayType.isArray();
    if (array instanceof UserArrayInstance) {
      UserArrayInstance userArrayInstance = (UserArrayInstance) array;
      this.checkIndex(index, userArrayInstance.getLength());
      return userArrayInstance.get(index);
    } else {
      this.checkIndex(index, Array.getLength(array));
      return Array.get(array, index);
    }
  }

  public void setItemAtIndex(AbstractType<?, ?, ?> arrayType, Object array, Integer index, Object value) {
    value = UserInstance.getJavaInstanceIfNecessary(value);
    assert arrayType != null;
    assert arrayType.isArray() : arrayType;
    if (array instanceof UserArrayInstance) {
      UserArrayInstance userArrayInstance = (UserArrayInstance) array;
      this.checkIndex(index, userArrayInstance.getLength());
      userArrayInstance.set(index, value);
    } else {
      this.checkIndex(index, Array.getLength(array));
      Array.set(array, index, value);
    }
  }

  public Object invokeUserMethod(Object instance, UserMethod method, Object... arguments) {
    if (method.isStatic()) {
      assert instance == null;
    } else {
      assert instance != null : method;
      assert instance instanceof UserInstance : instance;
    }
    UserInstance userInstance = (UserInstance) instance;
    Map<AbstractParameter, Object> map = Maps.newHashMap();
    for (int i = 0; i < arguments.length; i++) {
      map.put(method.requiredParameters.get(i), arguments[i]);
    }
    this.pushMethodFrame(userInstance, method, map);
    try {
      this.execute(method.body.getValue());
      if (method.isProcedure() || isStopped) {
        return null;
      } else {
        throw new LgnaVmNoReturnException(this);
      }
    } catch (ReturnException re) {
      return re.getValue();
    } finally {
      this.popFrame();
    }
  }

  private static void checkArguments(Class<?>[] parameterTypes, Object[] arguments, IllegalArgumentException iae, String text) {
    if (parameterTypes.length != arguments.length) {
      throw new RuntimeException("wrong number of arguments.  expected: " + parameterTypes.length + "; received: " + arguments.length + ". " + text, iae);
    }
    int i = 0;
    for (Class<?> parameterType : parameterTypes) {
      Object argument = arguments[i];
      if (argument != null) {
        if (parameterType.isPrimitive()) {
          //todo
        } else {
          if (parameterType.isAssignableFrom(argument.getClass())) {
            //pass
          } else {
            throw new RuntimeException("parameterType[" + i + "] " + parameterType.getName() + " is not assignable from argument[" + i + "]: " + argument + ". " + text, iae);
          }
        }
      }
      i++;
    }
  }

  public Object invokeMethodDeclaredInJava(Object instance, JavaMethod method, Object... arguments) {
    instance = UserInstance.getJavaInstanceIfNecessary(instance);
    UserInstance.updateArrayWithInstancesInJavaIfNecessary(arguments);
    Method mthd = method.getMethodReflectionProxy().getReification();

    Class<?>[] parameterTypes = mthd.getParameterTypes();
    int lastParameterIndex = parameterTypes.length - 1;
    if (lastParameterIndex == arguments.length) {
      if (mthd.isVarArgs()) {
        Object[] fixedArguments = new Object[parameterTypes.length];
        System.arraycopy(arguments, 0, fixedArguments, 0, arguments.length);
        assert parameterTypes[lastParameterIndex].isArray() : parameterTypes[lastParameterIndex];
        fixedArguments[lastParameterIndex] = Array.newInstance(parameterTypes[lastParameterIndex].getComponentType(), 0);
        arguments = fixedArguments;
      }
    }

    if (ReflectionUtilities.isProtected(mthd)) {
      Class<?> adapterCls = mapAbstractClsToAdapterCls.get(mthd.getDeclaringClass());
      if (adapterCls != null) {
        mthd = ReflectionUtilities.getMethod(adapterCls, mthd.getName(), mthd.getParameterTypes());
      }
    }
    assert ReflectionUtilities.isPublic(mthd) : mthd;

    try {
      return mthd.invoke(instance, arguments);
    } catch (IllegalArgumentException illegalArgumentException) {
      checkArguments(mthd.getParameterTypes(), arguments, illegalArgumentException, ReflectionUtilities.getDetail(instance, mthd, arguments));
      throw illegalArgumentException;
    } catch (IllegalAccessException illegalAccessException) {
      throw new RuntimeException(ReflectionUtilities.getDetail(instance, mthd, arguments), illegalAccessException);
    } catch (InvocationTargetException ite) {
      Throwable throwable = ite.getTargetException();
      if (throwable instanceof RuntimeException) {
        RuntimeException re = (RuntimeException) throwable;
        throw re;
      } else {
        throw new RuntimeException(ReflectionUtilities.getDetail(instance, mthd, arguments), throwable);
      }
    }
  }

  protected Object invoke(Object instance, AbstractMethod method, Object... arguments) {
    assert method != null;

    if (!method.isStatic()) {
      checkNotNull(instance, "Instance method target is null");
    }

    return method.invoke(this, instance, arguments);
  }

  protected Object evaluateAssignmentExpression(AssignmentExpression assignmentExpression) {
    Expression leftHandExpression = assignmentExpression.leftHandSide.getValue();
    Expression rightHandExpression = assignmentExpression.rightHandSide.getValue();
    Object rightHandValue = this.evaluate(rightHandExpression);
    if (assignmentExpression.operator.getValue() == AssignmentExpression.Operator.ASSIGN) {
      if (leftHandExpression instanceof FieldAccess) {
        FieldAccess fieldAccess = (FieldAccess) leftHandExpression;
        this.set(fieldAccess.field.getValue(), this.evaluate(fieldAccess.expression.getValue()), rightHandValue);
      } else if (leftHandExpression instanceof LocalAccess) {
        LocalAccess localAccess = (LocalAccess) leftHandExpression;
        this.setLocal(localAccess.local.getValue(), rightHandValue);
      } else if (leftHandExpression instanceof ArrayAccess) {
        ArrayAccess arrayAccess = (ArrayAccess) leftHandExpression;
        this.setItemAtIndex(arrayAccess.arrayType.getValue(), this.evaluate(arrayAccess.array.getValue()), this.evaluateInt(arrayAccess.index.getValue(), "array index is null"), rightHandValue);
      } else {
        PrintUtilities.println("todo: evaluateActual", assignmentExpression.leftHandSide.getValue(), rightHandValue);
      }
    } else {
      PrintUtilities.println("todo: evaluateActual", assignmentExpression);
    }
    return null;
  }

  protected Object evaluateBooleanLiteral(BooleanLiteral booleanLiteral) {
    return booleanLiteral.value.getValue();
  }

  protected Object evaluateArrayInstanceCreation(ArrayInstanceCreation arrayInstanceCreation) {
    Object[] values = new Object[arrayInstanceCreation.expressions.size()];
    for (int i = 0; i < values.length; i++) {
      values[i] = this.evaluate(arrayInstanceCreation.expressions.get(i));
    }
    int[] lengths = new int[arrayInstanceCreation.lengths.size()];
    for (int i = 0; i < lengths.length; i++) {
      lengths[i] = arrayInstanceCreation.lengths.get(i);
    }
    return this.createArrayInstance(arrayInstanceCreation.arrayType.getValue(), lengths, values);
  }

  protected Object evaluateArrayAccess(ArrayAccess arrayAccess) {
    return this.getItemAtIndex(arrayAccess.arrayType.getValue(), this.evaluate(arrayAccess.array.getValue()), this.evaluateInt(arrayAccess.index.getValue(), "array index is null"));
  }

  protected Integer evaluateArrayLength(ArrayLength arrayLength) {
    return this.getArrayLength(this.evaluate(arrayLength.array.getValue()));
  }

  protected Object evaluateFieldAccess(FieldAccess fieldAccess) {
    Object o = fieldAccess.field.getValue();
    if (o instanceof AbstractField) {
      AbstractField field = fieldAccess.field.getValue();
      Expression expression = fieldAccess.expression.getValue();
      Object value = this.evaluate(expression);
      return this.get(field, value);
    } else {
      Logger.errln("field access field is not a field", o);
      Node node = fieldAccess;
      while (node != null) {
        Logger.errln("   ", node);
        node = node.getParent();
      }
      return null;
    }
  }

  protected Object evaluateLocalAccess(LocalAccess localAccess) {
    return this.getLocal(localAccess.local.getValue());
  }

  protected Object evaluateArithmeticInfixExpression(ArithmeticInfixExpression arithmeticInfixExpression) {
    Number leftOperand = (Number) this.evaluate(arithmeticInfixExpression.leftOperand.getValue());
    Number rightOperand = (Number) this.evaluate(arithmeticInfixExpression.rightOperand.getValue());
    return arithmeticInfixExpression.operator.getValue().operate(leftOperand, rightOperand);
  }

  protected Object evaluateBitwiseInfixExpression(BitwiseInfixExpression bitwiseInfixExpression) {
    Object leftOperand = this.evaluate(bitwiseInfixExpression.leftOperand.getValue());
    Object rightOperand = this.evaluate(bitwiseInfixExpression.rightOperand.getValue());
    return bitwiseInfixExpression.operator.getValue().operate(leftOperand, rightOperand);
  }

  protected Boolean evaluateConditionalInfixExpression(ConditionalInfixExpression conditionalInfixExpression) {
    ConditionalInfixExpression.Operator operator = conditionalInfixExpression.operator.getValue();
    Boolean leftOperand = (Boolean) this.evaluate(conditionalInfixExpression.leftOperand.getValue());
    if (operator == ConditionalInfixExpression.Operator.AND) {
      if (leftOperand) {
        return (Boolean) this.evaluate(conditionalInfixExpression.rightOperand.getValue());
      } else {
        return false;
      }
    } else if (operator == ConditionalInfixExpression.Operator.OR) {
      if (leftOperand) {
        return true;
      } else {
        return (Boolean) this.evaluate(conditionalInfixExpression.rightOperand.getValue());
      }
    } else {

      return false;
    }
  }

  protected Boolean evaluateRelationalInfixExpression(RelationalInfixExpression relationalInfixExpression) {
    Object leftOperand = UserInstance.getJavaInstanceIfNecessary(this.evaluate(relationalInfixExpression.leftOperand.getValue()));
    Object rightOperand = UserInstance.getJavaInstanceIfNecessary(this.evaluate(relationalInfixExpression.rightOperand.getValue()));
    if (leftOperand != null) {
      if (rightOperand != null) {
        return relationalInfixExpression.operator.getValue().operate(leftOperand, rightOperand);
      } else {
        throw new LgnaVmNullPointerException("right operand is null.", this);
      }
    } else {
      if (rightOperand != null) {
        throw new LgnaVmNullPointerException("left operand is null.", this);
      } else {
        throw new LgnaVmNullPointerException("left and right operands are both null.", this);
      }
    }
  }

  protected Object evaluateShiftInfixExpression(ShiftInfixExpression shiftInfixExpression) {
    Object leftOperand = this.evaluate(shiftInfixExpression.leftOperand.getValue());
    Object rightOperand = this.evaluate(shiftInfixExpression.rightOperand.getValue());
    return shiftInfixExpression.operator.getValue().operate(leftOperand, rightOperand);
  }

  protected Object evaluateLogicalComplement(LogicalComplement logicalComplement) {
    Boolean operand = this.evaluateBoolean(logicalComplement.operand.getValue(), "logical complement expression is null");
    return !operand;
  }

  protected String evaluateStringConcatenation(StringConcatenation stringConcatenation) {
    Object leftOperand = this.evaluate(stringConcatenation.leftOperand.getValue());
    Object rightOperand = this.evaluate(stringConcatenation.rightOperand.getValue());
    return String.valueOf(leftOperand) + rightOperand;
  }

  protected Object evaluateMethodInvocation(MethodInvocation methodInvocation) {
    if (methodInvocation.isValid()) {
      Object[] allArguments = this.evaluateArguments(methodInvocation.method.getValue(), methodInvocation.requiredArguments, methodInvocation.variableArguments, methodInvocation.keyedArguments);
      int parameterCount = methodInvocation.method.getValue().getRequiredParameters().size();
      if (methodInvocation.method.getValue().getVariableLengthParameter() != null) {
        parameterCount += 1;
      }
      if (methodInvocation.method.getValue().getKeyedParameter() != null) {
        parameterCount += 1;
      }
      assert parameterCount == allArguments.length : methodInvocation.method.getValue().getName();
      Expression targetExpression = methodInvocation.expression.getValue();
      Object target = this.evaluate(targetExpression);

      try {
        return invoke(target, methodInvocation.method.getValue(), allArguments);
      } catch (Throwable e) {
        if (!isStopped) {
          Logger.severe("The method invocation threw an error. Continuing past.", methodInvocation.method.getValue(), e);
        }
        return null;
      }
    } else {
      Logger.severe("The method invocation is not valid. Continuing past.", methodInvocation.method.getValue());
      return null;
    }
  }

  protected Object evaluateNullLiteral(NullLiteral nullLiteral) {
    return null;
  }

  protected Object evaluateDoubleLiteral(DoubleLiteral doubleLiteral) {
    return doubleLiteral.value.getValue();
  }

  protected Object evaluateFloatLiteral(FloatLiteral floatLiteral) {
    return floatLiteral.value.getValue();
  }

  protected Object evaluateIntegerLiteral(IntegerLiteral integerLiteral) {
    return integerLiteral.value.getValue();
  }

  protected Object evaluateParameterAccess(ParameterAccess parameterAccess) {
    return this.lookup(parameterAccess.parameter.getValue());
  }

  protected Object evaluateStringLiteral(StringLiteral stringLiteral) {
    return stringLiteral.value.getValue();
  }

  protected Object evaluateThisExpression(ThisExpression thisExpression) {
    Object rv = this.getThis();
    assert rv != null;
    return rv;
  }

  protected Object evaluateTypeExpression(TypeExpression typeExpression) {
    return typeExpression.value.getValue();
  }

  protected Object evaluateTypeLiteral(TypeLiteral typeLiteral) {
    return typeLiteral.value.getValue();
  }

  protected Object evaluateResourceExpression(ResourceExpression resourceExpression) {
    return resourceExpression.resource.getValue();
  }

  protected Object EPIC_HACK_evaluateLambdaExpression(LambdaExpression lambdaExpression, AbstractArgument argument) {
    Lambda lambda = lambdaExpression.value.getValue();

    AbstractType<?, ?, ?> type = argument.parameter.getValue().getValueType();
    if (type instanceof JavaType) {

      UserInstance thisInstance = this.getThis();
      assert thisInstance != null;

      JavaType javaType = (JavaType) type;
      Class<?> interfaceCls = javaType.getClassReflectionProxy().getReification();
      Class<?> adapterCls = this.mapAbstractClsToAdapterCls.get(interfaceCls);
      assert adapterCls != null : interfaceCls;
      Class<?>[] parameterTypes = {LambdaContext.class, Lambda.class, UserInstance.class};
      Object[] arguments = {new LambdaContext() {
        @Override
        public void invokeEntryPoint(Lambda lambda, AbstractMethod singleAbstractMethod, UserInstance thisInstance, Object... arguments) {
          assert thisInstance != null;
          if (lambda instanceof UserLambda) {
            UserLambda userLambda = (UserLambda) lambda;
            Map<AbstractParameter, Object> map = Maps.newHashMap();
            for (int i = 0; i < arguments.length; i++) {
              map.put(userLambda.requiredParameters.get(i), arguments[i]);
            }
            pushLambdaFrame(thisInstance, userLambda, singleAbstractMethod, map);
            try {
              execute(userLambda.body.getValue());
            } catch (ReturnException re) {
              Logger.todo("handle return");
              assert false : re;
            } finally {
              popFrame();
            }
          }
        }
      }, lambda, thisInstance};
      try {
        Constructor<?> cnstrctr = adapterCls.getDeclaredConstructor(parameterTypes);
        return cnstrctr.newInstance(arguments);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new RuntimeException("todo");
    }
  }

  protected Object evaluateLambdaExpression(LambdaExpression lambdaExpression) {
    throw new RuntimeException("todo");
  }

  protected Object evaluate(Expression expression) {
    if (expression != null) {
      Object rv;
      if (expression instanceof AssignmentExpression) {
        rv = this.evaluateAssignmentExpression((AssignmentExpression) expression);
      } else if (expression instanceof BooleanLiteral) {
        rv = this.evaluateBooleanLiteral((BooleanLiteral) expression);
      } else if (expression instanceof InstanceCreation) {
        rv = ((InstanceCreation) expression).evaluate(this);
      } else if (expression instanceof ArrayInstanceCreation) {
        rv = this.evaluateArrayInstanceCreation((ArrayInstanceCreation) expression);
      } else if (expression instanceof ArrayLength) {
        rv = this.evaluateArrayLength((ArrayLength) expression);
      } else if (expression instanceof ArrayAccess) {
        rv = this.evaluateArrayAccess((ArrayAccess) expression);
      } else if (expression instanceof FieldAccess) {
        rv = this.evaluateFieldAccess((FieldAccess) expression);
      } else if (expression instanceof LocalAccess) {
        rv = this.evaluateLocalAccess((LocalAccess) expression);
      } else if (expression instanceof ArithmeticInfixExpression) {
        rv = this.evaluateArithmeticInfixExpression((ArithmeticInfixExpression) expression);
      } else if (expression instanceof BitwiseInfixExpression) {
        rv = this.evaluateBitwiseInfixExpression((BitwiseInfixExpression) expression);
      } else if (expression instanceof ConditionalInfixExpression) {
        rv = this.evaluateConditionalInfixExpression((ConditionalInfixExpression) expression);
      } else if (expression instanceof RelationalInfixExpression) {
        rv = this.evaluateRelationalInfixExpression((RelationalInfixExpression) expression);
      } else if (expression instanceof ShiftInfixExpression) {
        rv = this.evaluateShiftInfixExpression((ShiftInfixExpression) expression);
      } else if (expression instanceof LogicalComplement) {
        rv = this.evaluateLogicalComplement((LogicalComplement) expression);
      } else if (expression instanceof MethodInvocation) {
        rv = this.evaluateMethodInvocation((MethodInvocation) expression);
      } else if (expression instanceof NullLiteral) {
        rv = this.evaluateNullLiteral((NullLiteral) expression);
      } else if (expression instanceof StringConcatenation) {
        rv = this.evaluateStringConcatenation((StringConcatenation) expression);
      } else if (expression instanceof DoubleLiteral) {
        rv = this.evaluateDoubleLiteral((DoubleLiteral) expression);
      } else if (expression instanceof FloatLiteral) {
        rv = this.evaluateFloatLiteral((FloatLiteral) expression);
      } else if (expression instanceof IntegerLiteral) {
        rv = this.evaluateIntegerLiteral((IntegerLiteral) expression);
      } else if (expression instanceof ParameterAccess) {
        rv = this.evaluateParameterAccess((ParameterAccess) expression);
      } else if (expression instanceof StringLiteral) {
        rv = this.evaluateStringLiteral((StringLiteral) expression);
      } else if (expression instanceof ThisExpression) {
        rv = this.evaluateThisExpression((ThisExpression) expression);
      } else if (expression instanceof TypeExpression) {
        rv = this.evaluateTypeExpression((TypeExpression) expression);
      } else if (expression instanceof TypeLiteral) {
        rv = this.evaluateTypeLiteral((TypeLiteral) expression);
      } else if (expression instanceof ResourceExpression) {
        rv = this.evaluateResourceExpression((ResourceExpression) expression);
      } else if (expression instanceof LambdaExpression) {
        rv = this.evaluateLambdaExpression((LambdaExpression) expression);
      } else {
        throw new RuntimeException(expression.getClass().getName());
      }
      synchronized (this.virtualMachineListeners) {
        if (this.virtualMachineListeners.size() > 0) {
          ExpressionEvaluationEvent expressionEvaluationEvent = new ExpressionEvaluationEvent(this, expression, rv);
          for (VirtualMachineListener virtualMachineListener : this.virtualMachineListeners) {
            virtualMachineListener.expressionEvaluated(expressionEvaluationEvent);
          }
        }
      }
      return rv;
    } else {
      throw new NullPointerException();
    }
  }

  protected final <E> E evaluate(Expression expression, Class<E> cls) {
    //in order to support python...
    //if( result instanceof Integer ) {
    //  condition = ((Integer)result) != 0;
    //} else {
    //  condition = (Boolean)result;
    //}
    Object value = this.evaluate(expression);
    if (cls.isArray()) {
      if (value instanceof UserArrayInstance) {
        UserArrayInstance userArrayInstance = (UserArrayInstance) value;
        //todo
        value = userArrayInstance.getValues();
      }
    }
    return cls.cast(value);
  }

  private boolean evaluateBoolean(Expression expression, String nullExceptionMessage) {
    Object value = this.evaluate(expression);
    this.checkNotNull(value, nullExceptionMessage);
    if (value instanceof Boolean) {
      return (Boolean) value;
    } else {
      throw new LgnaVmClassCastException(this, Boolean.class, value.getClass());
    }
  }

  private int evaluateInt(Expression expression, String nullExceptionMessage) {
    Object value = this.evaluate(expression);
    this.checkNotNull(value, nullExceptionMessage);
    if (value instanceof Integer) {
      return (Integer) value;
    } else {
      throw new LgnaVmClassCastException(this, Integer.class, value.getClass());
    }
  }

  protected void executeBlockStatement(BlockStatement blockStatement, VirtualMachineListener[] listeners) throws ReturnException {
    //todo?
    Statement[] array = new Statement[blockStatement.statements.size()];
    blockStatement.statements.toArray(array);
    for (Statement statement : array) {
      this.execute(statement);
    }
  }

  protected void executeConditionalStatement(ConditionalStatement conditionalStatement, VirtualMachineListener[] listeners) throws ReturnException {
    for (BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs) {
      if (this.evaluateBoolean(booleanExpressionBodyPair.expression.getValue(), "if condition is null")) {
        this.execute(booleanExpressionBodyPair.body.getValue());
        return;
      }
    }
    this.execute(conditionalStatement.elseBody.getValue());
  }

  protected void executeComment(Comment comment, VirtualMachineListener[] listeners) {
  }

  protected void executeCountLoop(CountLoop countLoop, VirtualMachineListener[] listeners) throws ReturnException {
    UserLocal variable = countLoop.variable.getValue();
    UserLocal constant = countLoop.constant.getValue();
    this.pushLocal(variable, -1);
    try {
      final int n = this.evaluateInt(countLoop.count.getValue(), "count expression is null");
      this.pushLocal(constant, n);
      try {
        for (int i = 0; i < n; i++) {
          if (isStopped) {
            return;
          }
          CountLoopIterationEvent countLoopIterationEvent;
          if (listeners != null) {
            countLoopIterationEvent = new CountLoopIterationEvent(this, countLoop, i, n);
            for (VirtualMachineListener virtualMachineListener : listeners) {
              virtualMachineListener.countLoopIterating(countLoopIterationEvent);
            }
          } else {
            countLoopIterationEvent = null;
          }
          this.setLocal(variable, i);
          this.execute(countLoop.body.getValue());
          if (listeners != null) {
            for (VirtualMachineListener virtualMachineListener : listeners) {
              virtualMachineListener.countLoopIterated(countLoopIterationEvent);
            }
          }
        }
      } finally {
        this.popLocal(constant);
      }
    } finally {
      this.popLocal(variable);
    }
  }

  protected void executeDoInOrder(DoInOrder doInOrder, VirtualMachineListener[] listeners) throws ReturnException {
    execute(doInOrder.body.getValue());
  }

  protected void executeDoTogether(DoTogether doTogether, VirtualMachineListener[] listeners) throws ReturnException {
    BlockStatement blockStatement = doTogether.body.getValue();
    //todo?
    switch (blockStatement.statements.size()) {
    case 0:
      break;
    case 1:
      execute(blockStatement.statements.get(0));
      break;
    default:
      final Frame owner = this.getFrameForThread(Thread.currentThread());
      Runnable[] runnables = new Runnable[blockStatement.statements.size()];
      for (int i = 0; i < runnables.length; i++) {
        final Statement statementI = blockStatement.statements.get(i);
        runnables[i] = new Runnable() {
          @Override
          public void run() {
            //edu.cmu.cs.dennisc.print.PrintUtilities.println( statementI );
            pushCurrentThread(owner);
            try {
              execute(statementI);
            } catch (ReturnException re) {
              //todo
            } finally {
              popCurrentThread();
            }
          }
        };
      }
      ThreadUtilities.doTogether(runnables);
    }
  }

  protected void executeExpressionStatement(ExpressionStatement expressionStatement, VirtualMachineListener[] listeners) {
    @SuppressWarnings("unused") Object unused = this.evaluate(expressionStatement.expression.getValue());
  }

  protected void excecuteForEachLoop(AbstractForEachLoop forEachInLoop, Object[] array, VirtualMachineListener[] listeners) throws ReturnException {
    UserLocal item = forEachInLoop.item.getValue();
    BlockStatement blockStatement = forEachInLoop.body.getValue();
    this.pushLocal(item, -1);
    try {
      int index = 0;
      for (Object o : array) {
        if (isStopped) {
          return;
        }
        ForEachLoopIterationEvent forEachLoopIterationEvent;
        if (listeners != null) {
          forEachLoopIterationEvent = new ForEachLoopIterationEvent(this, forEachInLoop, o, array, index);
          for (VirtualMachineListener virtualMachineListener : listeners) {
            virtualMachineListener.forEachLoopIterating(forEachLoopIterationEvent);
          }
        } else {
          forEachLoopIterationEvent = null;
        }

        this.setLocal(item, o);
        this.execute(blockStatement);
        if (listeners != null) {
          for (VirtualMachineListener virtualMachineListener : listeners) {
            virtualMachineListener.forEachLoopIterated(forEachLoopIterationEvent);
          }
        }
        index++;
      }
    } finally {
      this.popLocal(item);
    }
  }

  protected final void executeForEachInArrayLoop(ForEachInArrayLoop forEachInArrayLoop, VirtualMachineListener[] listeners) throws ReturnException {
    Object[] array = this.evaluate(forEachInArrayLoop.array.getValue(), Object[].class);
    this.checkNotNull(array, "for each array is null");
    excecuteForEachLoop(forEachInArrayLoop, array, listeners);
  }

  protected final void executeForEachInIterableLoop(ForEachInIterableLoop forEachInIterableLoop, VirtualMachineListener[] listeners) throws ReturnException {
    Iterable<?> iterable = this.evaluate(forEachInIterableLoop.iterable.getValue(), Iterable.class);
    this.checkNotNull(iterable, "for each iterable is null");
    excecuteForEachLoop(forEachInIterableLoop, IterableUtilities.toArray(iterable), listeners);
  }

  protected void excecuteEachInTogether(final AbstractEachInTogether eachInTogether, final Object[] array, final VirtualMachineListener[] listeners) throws ReturnException {
    final UserLocal item = eachInTogether.item.getValue();
    final BlockStatement blockStatement = eachInTogether.body.getValue();

    switch (array.length) {
    case 0:
      break;
    case 1:
      Object value = array[0];
      VirtualMachine.this.pushLocal(item, value);
      try {
        EachInTogetherItemEvent eachInTogetherEvent;
        if (listeners != null) {
          eachInTogetherEvent = new EachInTogetherItemEvent(this, eachInTogether, value, array);
          for (VirtualMachineListener virtualMachineListener : listeners) {
            virtualMachineListener.eachInTogetherItemExecuting(eachInTogetherEvent);
          }
        } else {
          eachInTogetherEvent = null;
        }
        VirtualMachine.this.execute(blockStatement);
        if (listeners != null) {
          for (VirtualMachineListener virtualMachineListener : listeners) {
            virtualMachineListener.eachInTogetherItemExecuted(eachInTogetherEvent);
          }
        }
      } finally {
        VirtualMachine.this.popLocal(item);
      }
      break;
    default:
      final Frame owner = this.getFrameForThread(Thread.currentThread());
      ThreadUtilities.eachInTogether(new EachInTogetherRunnable<Object>() {
        @Override
        public void run(Object value) {
          pushCurrentThread(owner);
          try {
            VirtualMachine.this.pushLocal(item, value);
            try {
              EachInTogetherItemEvent eachInTogetherEvent;
              if (listeners != null) {
                eachInTogetherEvent = new EachInTogetherItemEvent(VirtualMachine.this, eachInTogether, value, array);
                for (VirtualMachineListener virtualMachineListener : listeners) {
                  virtualMachineListener.eachInTogetherItemExecuting(eachInTogetherEvent);
                }
              } else {
                eachInTogetherEvent = null;
              }
              VirtualMachine.this.execute(blockStatement);
              if (listeners != null) {
                for (VirtualMachineListener virtualMachineListener : listeners) {
                  virtualMachineListener.eachInTogetherItemExecuted(eachInTogetherEvent);
                }
              }
            } catch (ReturnException re) {
              //todo
            } finally {
              VirtualMachine.this.popLocal(item);
            }
          } finally {
            popCurrentThread();
          }
        }
      }, array);
    }
  }

  protected final void executeEachInArrayTogether(EachInArrayTogether eachInArrayTogether, VirtualMachineListener[] listeners) throws ReturnException {
    Object[] array = this.evaluate(eachInArrayTogether.array.getValue(), Object[].class);
    this.checkNotNull(array, "each in together array is null");
    excecuteEachInTogether(eachInArrayTogether, array, listeners);
  }

  protected final void executeEachInIterableTogether(EachInIterableTogether eachInIterableTogether, VirtualMachineListener[] listeners) throws ReturnException {
    Iterable<?> iterable = this.evaluate(eachInIterableTogether.iterable.getValue(), Iterable.class);
    this.checkNotNull(iterable, "each in together iterable is null");
    excecuteEachInTogether(eachInIterableTogether, IterableUtilities.toArray(iterable), listeners);
  }

  protected void executeReturnStatement(ReturnStatement returnStatement, VirtualMachineListener[] listeners) throws ReturnException {
    Object returnValue = this.evaluate(returnStatement.expression.getValue());
    //setReturnValue( returnValue );
    throw new ReturnException(returnValue);
  }

  protected void executeWhileLoop(WhileLoop whileLoop, VirtualMachineListener[] listeners) throws ReturnException {
    int i = 0;
    while (!isStopped && evaluateBoolean(whileLoop.conditional.getValue(), "while condition is null")) {
      WhileLoopIterationEvent whileLoopIterationEvent;
      if (listeners != null) {
        whileLoopIterationEvent = new WhileLoopIterationEvent(this, whileLoop, i);
        for (VirtualMachineListener virtualMachineListener : listeners) {
          virtualMachineListener.whileLoopIterating(whileLoopIterationEvent);
        }
      } else {
        whileLoopIterationEvent = null;
      }
      this.execute(whileLoop.body.getValue());
      if (listeners != null) {
        for (VirtualMachineListener virtualMachineListener : listeners) {
          virtualMachineListener.whileLoopIterated(whileLoopIterationEvent);
        }
      }
      i++;
    }
  }

  protected void executeLocalDeclarationStatement(LocalDeclarationStatement localDeclarationStatement, VirtualMachineListener[] listeners) {
    this.pushLocal(localDeclarationStatement.local.getValue(), this.evaluate(localDeclarationStatement.initializer.getValue()));
    //handle pop on exit of owning block statement
  }

  protected void execute(Statement statement) throws ReturnException {
    if (isStopped) {
      return;
    }
    assert statement != null : this;
    if (statement.isEnabled.getValue()) {
      StatementExecutionEvent statementEvent;
      VirtualMachineListener[] listeners;
      synchronized (this.virtualMachineListeners) {
        if (this.virtualMachineListeners.size() > 0) {
          statementEvent = new StatementExecutionEvent(this, statement);
          listeners = ArrayUtilities.createArray(this.virtualMachineListeners, VirtualMachineListener.class);
        } else {
          statementEvent = null;
          listeners = null;
        }
      }
      if ((statementEvent != null) && (listeners != null)) {
        for (VirtualMachineListener virtualMachineListener : listeners) {
          virtualMachineListener.statementExecuting(statementEvent);
        }
      }

      try {
        if (statement instanceof BlockStatement) {
          this.executeBlockStatement((BlockStatement) statement, listeners);
        } else if (statement instanceof ConditionalStatement) {
          this.executeConditionalStatement((ConditionalStatement) statement, listeners);
        } else if (statement instanceof Comment) {
          this.executeComment((Comment) statement, listeners);
        } else if (statement instanceof CountLoop) {
          this.executeCountLoop((CountLoop) statement, listeners);
        } else if (statement instanceof DoTogether) {
          this.executeDoTogether((DoTogether) statement, listeners);
        } else if (statement instanceof DoInOrder) {
          this.executeDoInOrder((DoInOrder) statement, listeners);
        } else if (statement instanceof ExpressionStatement) {
          this.executeExpressionStatement((ExpressionStatement) statement, listeners);
        } else if (statement instanceof ForEachInArrayLoop) {
          this.executeForEachInArrayLoop((ForEachInArrayLoop) statement, listeners);
        } else if (statement instanceof ForEachInIterableLoop) {
          this.executeForEachInIterableLoop((ForEachInIterableLoop) statement, listeners);
        } else if (statement instanceof EachInArrayTogether) {
          this.executeEachInArrayTogether((EachInArrayTogether) statement, listeners);
        } else if (statement instanceof EachInIterableTogether) {
          this.executeEachInIterableTogether((EachInIterableTogether) statement, listeners);
        } else if (statement instanceof WhileLoop) {
          this.executeWhileLoop((WhileLoop) statement, listeners);
        } else if (statement instanceof LocalDeclarationStatement) {
          this.executeLocalDeclarationStatement((LocalDeclarationStatement) statement, listeners);
        } else if (statement instanceof ReturnStatement) {
          this.executeReturnStatement((ReturnStatement) statement, listeners);
          // note: does not return.  throws ReturnException.
        } else {
          throw new RuntimeException();
        }
      } finally {
        if ((statementEvent != null) && (listeners != null)) {
          for (VirtualMachineListener virtualMachineListener : listeners) {
            virtualMachineListener.statementExecuted(statementEvent);
          }
        }
      }
    }
  }

  public void stopExecution() {
    isStopped = true;
  }

  public void addVirtualMachineListener(VirtualMachineListener virtualMachineListener) {
    synchronized (this.virtualMachineListeners) {
      this.virtualMachineListeners.add(virtualMachineListener);
    }
  }

  public void removeVirtualMachineListener(VirtualMachineListener virtualMachineListener) {
    synchronized (this.virtualMachineListeners) {
      this.virtualMachineListeners.remove(virtualMachineListener);
    }
  }

  public List<VirtualMachineListener> getVirtualMachineListeners() {
    synchronized (this.virtualMachineListeners) {
      return Collections.unmodifiableList(this.virtualMachineListeners);
    }
  }

  private final List<VirtualMachineListener> virtualMachineListeners = Lists.newLinkedList();
  private boolean isStopped = false;
}
