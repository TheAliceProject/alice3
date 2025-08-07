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

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * @author Dennis Cosgrove
 */
public class KeyedArgumentListProperty extends ArgumentListProperty<JavaKeyedArgument> {
  public KeyedArgumentListProperty(ArgumentOwner owner) {
    super(owner);
  }

  @Override
  public boolean areAllOptionalArgumentsFilled() {
    // This checks if there are any unused optional arguments that can be added to a method call.
    // These are the Detail interfaces defined in per method classes (e.g. org.lgna.story.Say) and implemented by
    // classes representing each such argument (e.g. setting the font on the say method)
    return getOptionalParameters().stream().noneMatch(this::isOptionalArgumentAbsent);
  }

  private List<JavaMethod> getOptionalParameters() {
    // This KeyedArgumentListProperty holds the arguments used to call the owning method (e.g. say(), or move()).
    AbstractCode codeToCall = getOwner().getParameterOwnerProperty().getValue();
    // The Detail interface defined inside the method class
    AbstractType<?, ?, ?> detailType = codeToCall.getKeyedParameter().getValueType().getComponentType();
    // A class representing the method (e.g. Say, or Move)
    AbstractType<?, ?, ?> methodType = detailType.getKeywordFactoryType();
    if (methodType == null) {
      return Collections.emptyList();
    }
    Class<?> cls = ((JavaType) methodType).getClassReflectionProxy().getReification();
    return Arrays.stream(cls.getMethods())
        .filter(method -> isDetailMethod(method, detailType))
        .map(JavaMethod::getInstance)
        .toList();
  }

  // Check for public static methods that return values that implement the Detail marking interface
  private boolean isDetailMethod(java.lang.reflect.Method method, AbstractType<?, ?, ?> detailType) {
    int modifiers = method.getModifiers();
    return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && detailType.isAssignableFrom(method.getReturnType());
  }

  private boolean isOptionalArgumentAbsent(JavaMethod optional) {
    return StreamSupport.stream(this.spliterator(), false)
        .noneMatch(arg -> arg.getKeyMethod() == optional);
  }
}
