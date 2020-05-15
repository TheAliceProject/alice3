/**
 * Copyright (c) 2019 Carnegie Mellon University. All rights reserved.
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
 */
package org.lgna.project.migration.ast;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Crawlable;
import org.lgna.project.ast.AbstractArgument;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaMethodParameter;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.resourceutilities.ResourceTypeHelper;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;

/*
 * Used in a CompoundMigration to change the invoked class of a java method to a superclass.
 * If the newClass is not an ancestor of the oldClass, the resulting AST may have errors but the migration will
 * continue, but log that risk.
 * The method is found by name, so this will not work for replacing overloaded methods.
 */
public class MethodMovedToSuperclass implements NodeMigration {
  private final Class oldClass;
  private final Class newClass;
  private final String methodName;
  private final AbstractMethod replacementMethod;

  public MethodMovedToSuperclass(Class<?> oldClass, Class<?> newClass, String methodName, Class<?>... parameterTypes) {
    this.oldClass = oldClass;
    this.newClass = newClass;
    this.methodName = methodName;
    replacementMethod = JavaMethod.getInstance(newClass, methodName, parameterTypes);
    if (!newClass.isAssignableFrom(oldClass)) {
      Logger.severe(String.format("Using MethodMovedToSuperclass to move a method invocation from %s to %s, which is not a superclass.", oldClass.getSimpleName(), newClass.getSimpleName()));
    }
  }

  @Override
  public void migrateNode(Crawlable node, ResourceTypeHelper typeHelper, Set<NamedUserType> typeCache) {
    if (node instanceof MethodInvocation) {
      MethodInvocation invocation = (MethodInvocation) node;
      AbstractMethod method = invocation.method.getValue();
      if (isOldMethod(method)) {
        invocation.method.setValue(replacementMethod);
        replaceRequiredParamReferences(method, invocation);
        replaceKeyedParamReferences(method, invocation);
        Logger.outln(String.format("Changed %s.%s call to %s.%s", oldClass.getSimpleName(), methodName, newClass.getSimpleName(), methodName));
      }
    }
  }

  private boolean isOldMethod(AbstractMethod method) {
    return method instanceof JavaMethod && method.getDeclaringType() == JavaType.getInstance(oldClass) && method.getName().equals(methodName);
  }

  private void replaceRequiredParamReferences(AbstractMethod oldMethod, MethodInvocation invocation) {
    replaceParamReferences(oldMethod, invocation.getRequiredArgumentsProperty().getValue(), this::findNewRequiredParam);
  }

  private void replaceKeyedParamReferences(AbstractMethod oldMethod, MethodInvocation invocation) {
    replaceParamReferences(oldMethod, invocation.getKeyedArgumentsProperty().getValue(), this::findNewKeyedParam);

  }

  private void replaceParamReferences(AbstractMethod oldMethod, ArrayList<? extends AbstractArgument> args, Function<JavaMethodParameter, AbstractParameter> filter) {
    for (AbstractArgument argument : args) {
      AbstractParameter param = argument.parameter.getValue();
      if (param instanceof JavaMethodParameter) {
        JavaMethodParameter javaParam = (JavaMethodParameter) param;
        if (oldMethod == javaParam.getCode()) {
          argument.parameter.setValue(filter.apply(javaParam));
        }
      }
    }
  }

  private JavaMethodParameter findNewRequiredParam(JavaMethodParameter oldJavaParam) {
    for (AbstractParameter parameter : replacementMethod.getRequiredParameters()) {
      final JavaMethodParameter newJavaParam = (JavaMethodParameter) parameter;
      if (oldJavaParam.getIndex() == newJavaParam.getIndex()) {
        return newJavaParam;
      }
    }
    return null;
  }

  private AbstractParameter findNewKeyedParam(JavaMethodParameter oldJavaParam) {
    AbstractParameter keyParam = replacementMethod.getKeyedParameter();
    if (keyParam.getValueType() == oldJavaParam.getValueType()) {
      return keyParam;
    }
    return null;
  }
}
