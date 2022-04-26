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

package org.lgna.project;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Crawlable;
import edu.cmu.cs.dennisc.pattern.Crawler;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import edu.cmu.cs.dennisc.tree.DefaultNode;
import org.lgna.common.Resource;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.Declaration;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserMethod;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ProgramTypeUtilities {
  private ProgramTypeUtilities() {
    throw new AssertionError();
  }

  public static List<UserLocal> getLocals(UserCode code) {
    IsInstanceCrawler<UserLocal> crawler = IsInstanceCrawler.createInstance(UserLocal.class);
    code.getBodyProperty().getValue().crawl(crawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY);
    return crawler.getList();
  }

  public static List<FieldAccess> getFieldAccesses(NamedUserType programType, final AbstractField field, Criterion<Declaration> declarationFilter) {
    assert programType != null;
    IsInstanceCrawler<FieldAccess> crawler = new IsInstanceCrawler<FieldAccess>(FieldAccess.class) {
      @Override
      protected boolean isAcceptable(FieldAccess fieldAccess) {
        return fieldAccess.field.getValue() == field;
      }
    };
    programType.crawl(crawler, CrawlPolicy.COMPLETE, declarationFilter);
    return crawler.getList();
  }

  public static List<MethodInvocation> getMethodInvocations(NamedUserType programType, final AbstractMethod method, Criterion<Declaration> declarationFilter) {
    assert programType != null;
    IsInstanceCrawler<MethodInvocation> crawler = new IsInstanceCrawler<MethodInvocation>(MethodInvocation.class) {
      @Override
      protected boolean isAcceptable(MethodInvocation methodInvocation) {
        return methodInvocation.method.getValue() == method;
      }
    };
    programType.crawl(crawler, CrawlPolicy.COMPLETE, declarationFilter);
    return crawler.getList();
  }

  public static List<SimpleArgumentListProperty> getArgumentLists(NamedUserType programType, final UserCode code, Criterion<Declaration> declarationFilter) {
    assert programType != null;
    class ArgumentListCrawler implements Crawler {
      private final List<SimpleArgumentListProperty> list = Lists.newLinkedList();

      @Override
      public void visit(Crawlable crawlable) {
        if (crawlable instanceof MethodInvocation) {
          MethodInvocation methodInvocation = (MethodInvocation) crawlable;
          if (methodInvocation.method.getValue() == code) {
            this.list.add(methodInvocation.requiredArguments);
          }
        } else if (crawlable instanceof InstanceCreation) {
          InstanceCreation instanceCreation = (InstanceCreation) crawlable;
          if (instanceCreation.constructor.getValue() == code) {
            this.list.add(instanceCreation.requiredArguments);
          }
        }
      }

      public List<SimpleArgumentListProperty> getList() {
        return this.list;
      }
    }
    ArgumentListCrawler crawler = new ArgumentListCrawler();
    programType.crawl(crawler, CrawlPolicy.COMPLETE, declarationFilter);
    return crawler.getList();
  }

  public static <N extends Node> N lookupNode(Project project, final UUID id) {
    final Node[] buffer = {null};
    NamedUserType programType = project.getProgramType();
    Crawler crawler = new Crawler() {
      @Override
      public void visit(Crawlable crawlable) {
        if (crawlable instanceof Node) {
          Node node = (Node) crawlable;
          if (id.equals(node.getId())) {
            buffer[0] = node;
          }
        }
      }
    };
    programType.crawl(crawler, CrawlPolicy.COMPLETE);
    return (N) buffer[0];
  }

  public static <R extends Resource> R lookupResource(Project project, UUID id) {
    for (Resource resource : project.getResources()) {
      if (resource.getId() == id) {
        return (R) resource;
      }
    }
    return null;
  }

  private static DefaultNode<NamedUserType> getNode(NamedUserType type, DefaultNode<NamedUserType> root) {
    DefaultNode<NamedUserType> rv = root.get(type);
    if (rv != null) {
      //pass
    } else {
      rv = DefaultNode.createSafeInstance(type, NamedUserType.class);
      AbstractType<?, ?, ?> superType = type.getSuperType();
      if (superType instanceof NamedUserType) {
        DefaultNode<NamedUserType> superNode = getNode((NamedUserType) superType, root);
        superNode.addChild(rv);
      } else {
        root.addChild(rv);
      }
    }
    return rv;
  }

  public static DefaultNode<NamedUserType> getNamedUserTypesAsTree(Project project) {
    DefaultNode<NamedUserType> root = DefaultNode.createSafeInstance(null, NamedUserType.class);
    Iterable<NamedUserType> types = project.getNamedUserTypes();
    for (NamedUserType type : types) {
      getNode(type, root);
    }
    return root;
  }

  public static UserMethod getMainMethod(NamedUserType programType) {
    UserMethod rv = programType.getDeclaredMethod("main", String[].class);
    if (rv != null) {
      if (rv.isStatic()) {
        //pass
      } else {
        Logger.warning("main method is not static", rv);
      }
    }
    return rv;
  }

  public static UserMethod getMainMethod(Project project) {
    return getMainMethod(project.getProgramType());
  }

  public static String sanityCheckAllTypes(Project project) {
    // TODO I18N
    StringBuilder sb = new StringBuilder();
    for (NamedUserType type : project.getNamedUserTypes()) {
      for (NamedUserConstructor constructor : type.constructors) {
        if (constructor.getDeclaringType() != type) {
          sb.append("Constructor for ").append(constructor.getDeclaringType()).append(" is found on a different class ").append(type);
        }
      }
      for (UserMethod method : type.methods) {
        if (method.getDeclaringType() != type) {
          sb.append("Method ").append(method.getDeclaringType()).append('.').append(method.getName()).append(" is found on a different class ").append(type);
        }
      }
      for (UserField field : type.fields) {
        if (field.getDeclaringType() != type) {
          sb.append("Field ").append(field.getDeclaringType()).append('.').append(field.getName()).append(" is found on a different class ").append(type);
        }
      }
    }
    return sb.toString();
  }
}
