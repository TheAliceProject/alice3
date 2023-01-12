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

package org.alice.ide.croquet.models.ast.keyed;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class KeyedBlank extends CascadeBlank<JavaKeyedArgument> {
  private static Map<ArgumentListProperty<JavaKeyedArgument>, KeyedBlank> map = Maps.newHashMap();

  public static synchronized KeyedBlank getInstance(ArgumentListProperty<JavaKeyedArgument> argumentListProperty) {
    KeyedBlank rv = map.get(argumentListProperty);
    if (rv != null) {
      //pass
    } else {
      rv = new KeyedBlank(argumentListProperty);
      map.put(argumentListProperty, rv);
    }
    return rv;
  }

  private static boolean isValidMethod(Method mthd, AbstractType<?, ?, ?> valueType) {
    int modifiers = mthd.getModifiers();
    if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
      return valueType.isAssignableFrom(mthd.getReturnType());
    } else {
      return false;
    }
  }

  private final ArgumentListProperty<JavaKeyedArgument> argumentListProperty;

  private KeyedBlank(ArgumentListProperty<JavaKeyedArgument> argumentListProperty) {
    this.argumentListProperty = argumentListProperty;
  }

  @Override
  protected void updateChildren(List<CascadeBlankChild> children, BlankNode<JavaKeyedArgument> blankNode) {
    // For clever reasons, this code has no idea what is in the scene, or what any of the methods can do.
    // For this method (e.g. move or say), find the arguments it could have (e.g. duration, text, animation style)
    // Once we know what the options are, we can check if it already has a value (and thus is handled elsewhere)
    // or if it is part of this cascade moment we're having.

    AbstractType<?, ?, ?> valueType = this.argumentListProperty.getOwner().getParameterOwnerProperty().getValue().getKeyedParameter().getValueType().getComponentType();
    AbstractType<?, ?, ?> keywordFactoryType = valueType.getKeywordFactoryType();
    if (keywordFactoryType != null) {
      Class<?> cls = ((JavaType) keywordFactoryType).getClassReflectionProxy().getReification();
      for (Method mthd : cls.getMethods()) {
        if (isValidMethod(mthd, valueType)) {
          JavaMethod keyMethod = JavaMethod.getInstance(mthd);
          boolean isAlreadyFilledIn = false;
          for (JavaKeyedArgument keyedArgument : this.argumentListProperty) {
            if (keyedArgument.getKeyMethod() == keyMethod) {
              isAlreadyFilledIn = true;
              break;
            }
          }
          if (!isAlreadyFilledIn) {
            children.add(JavaKeyedArgumentFillIn.getInstance(keyMethod));
          }
        }
      }
    }
  }
}
