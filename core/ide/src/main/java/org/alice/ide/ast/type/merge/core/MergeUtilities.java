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
package org.alice.ide.ast.type.merge.core;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import org.alice.ide.ProjectStack;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaCodeGenerator;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.code.CodeGenerator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class MergeUtilities {
  private MergeUtilities() {
    throw new AssertionError();
  }

  public static NamedUserType findMatchingTypeInExistingTypes(NamedUserType srcType, Collection<NamedUserType> dstTypes) {
    for (NamedUserType dstType : dstTypes) {
      //todo
      if (dstType.getName().contentEquals(srcType.getName())) {
        return dstType;
      }
    }
    return null;
  }

  public static NamedUserType findMatchingTypeInExistingTypes(NamedUserType type) {
    Project project = ProjectStack.peekProject();
    if (project != null) {
      Set<NamedUserType> dstTypes = project.getNamedUserTypes();
      return findMatchingTypeInExistingTypes(type, dstTypes);
    }
    return null;
  }

  public static UserMethod findMethodWithMatchingName(UserMethod srcMethod, NamedUserType dstType) {
    for (UserMethod dstMethod : dstType.methods) {
      if (dstMethod.getName().contentEquals(srcMethod.getName())) {
        return dstMethod;
      }
    }
    return null;
  }

  private static JavaCodeGenerator createJavaCodeGeneratorForEquivalence() {
    return new JavaCodeGenerator.Builder().isLambdaSupported(true) //don't care
                                          .build();
  }

  public static boolean isHeaderEquivalent(UserMethod a, UserMethod b) {
    String aText = a.generateHeaderJavaCode(createJavaCodeGeneratorForEquivalence());
    String bText = b.generateHeaderJavaCode(createJavaCodeGeneratorForEquivalence());
    return aText.contentEquals(bText);
  }

  public static boolean isEquivalent(CodeGenerator a, CodeGenerator b) {
    String aText = a.generateCode(createJavaCodeGeneratorForEquivalence());
    String bText = b.generateCode(createJavaCodeGeneratorForEquivalence());
    return aText.contentEquals(bText);
  }

  public static boolean isValueTypeEquivalent(UserField a, UserField b) {
    return a.getValueType().getName().contentEquals(b.getValueType().getName()); //todo
  }

  private static AbstractMethod findMatch(AbstractType<?, ?, ?> type, AbstractMethod original) {
    if (type != null) {
      for (AbstractMethod candidate : type.getDeclaredMethods()) {
        if (candidate.getName().contentEquals(original.getName())) { //todo
          return candidate;
        }
      }
      return findMatch(type.getSuperType(), original);
    } else {
      return null;
    }
  }

  private static AbstractField findMatch(AbstractType<?, ?, ?> type, AbstractField original) {
    if (type != null) {
      for (AbstractField candidate : type.getDeclaredFields()) {
        if (candidate.getName().contentEquals(original.getName())) { //todo
          return candidate;
        }
      }
      return findMatch(type.getSuperType(), original);
    } else {
      return null;
    }
  }

  private static boolean isAcceptableType(AbstractType<?, ?, ?> declaringType, List<NamedUserType> types) {
    if (declaringType != null) {
      if (declaringType instanceof NamedUserType) {
        NamedUserType namedUserType = (NamedUserType) declaringType;
        return types.contains(namedUserType);
      } else {
        return true;
      }
    } else {
      return false;
    }

  }

  private static void mendMethodInvocations(Node node, List<NamedUserType> types) {
    IsInstanceCrawler<MethodInvocation> crawler = IsInstanceCrawler.createInstance(MethodInvocation.class);
    node.crawl(crawler, CrawlPolicy.COMPLETE, null);

    Map<AbstractMethod, AbstractMethod> map = Maps.newHashMap();
    for (MethodInvocation methodInvocation : crawler.getList()) {
      AbstractMethod method = methodInvocation.method.getValue();
      if (isAcceptableType(method.getDeclaringType(), types)) {
        //pass
      } else {
        AbstractMethod replacement;
        if (map.containsKey(method)) {
          replacement = map.get(method);
        } else {
          Expression expression = methodInvocation.expression.getValue();
          AbstractType<?, ?, ?> type = expression.getType();
          replacement = findMatch(type, method);
          if (replacement != null) {
            map.put(method, replacement);
          }
        }
        if (replacement != null) {
          Logger.outln("mending", methodInvocation, method);
          methodInvocation.method.setValue(replacement);
          AstUtilities.fixRequiredArgumentsIfNecessary(methodInvocation);
        } else {
          Logger.severe("cannot find replacement:", method);
        }
      }
    }
  }

  private static void mendFieldAccesses(Node node, List<NamedUserType> types) {
    IsInstanceCrawler<FieldAccess> crawler = IsInstanceCrawler.createInstance(FieldAccess.class);
    node.crawl(crawler, CrawlPolicy.COMPLETE, null);

    Map<AbstractField, AbstractField> map = Maps.newHashMap();
    for (FieldAccess fieldAccess : crawler.getList()) {
      AbstractField field = fieldAccess.field.getValue();
      if (isAcceptableType(field.getDeclaringType(), types)) {
        //pass
      } else {
        AbstractField replacement;
        if (map.containsKey(field)) {
          replacement = map.get(field);
        } else {
          Expression expression = fieldAccess.expression.getValue();
          AbstractType<?, ?, ?> type = expression.getType();
          replacement = findMatch(type, field);
          if (replacement != null) {
            map.put(field, replacement);
          }
        }
        if (replacement != null) {
          Logger.outln("mending", fieldAccess, field);
          fieldAccess.field.setValue(replacement);
        } else {
          Logger.severe("cannot find replacement:", field);
        }
      }
    }
  }

  public static void mendMethodInvocationsAndFieldAccesses(Node node) {
    IsInstanceCrawler<NamedUserType> crawler = IsInstanceCrawler.createInstance(NamedUserType.class);
    node.crawl(crawler, CrawlPolicy.COMPLETE, null);
    for (NamedUserType namedUserType : crawler.getList()) {
      Logger.outln(namedUserType);
    }
    mendMethodInvocations(node, crawler.getList());
    mendFieldAccesses(node, crawler.getList());
  }
}
