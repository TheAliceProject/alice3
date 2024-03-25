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

package org.alice.ide.croquet.models.declaration;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.croquet.models.cascade.ExpressionFillInWithExpressionBlanks;
import org.alice.ide.typemanager.ConstructorArgumentUtilities;
import org.alice.ide.typemanager.TypeManager;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;

import javax.swing.Icon;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class GalleryResourceTypeFillIn extends ExpressionFillInWithExpressionBlanks<InstanceCreation> {
  private static Map<JavaType, GalleryResourceTypeFillIn> map = Maps.newHashMap();

  public static synchronized GalleryResourceTypeFillIn getInstance(JavaType type) {
    GalleryResourceTypeFillIn rv = map.get(type);
    if (rv == null) {
      rv = new GalleryResourceTypeFillIn(type);
      map.put(type, rv);
    }
    return rv;
  }

  private final JavaType ancestorType;

  private GalleryResourceTypeFillIn(JavaType ancestorType) {
    super(UUID.fromString("281ad60a-090e-4fd8-bb47-da03a2508a4a"), GalleryResourceBlank.getInstance(ConstructorArgumentUtilities.getContructor0Parameter0Type(ancestorType)));
    this.ancestorType = ancestorType;
  }

  @Override
  public String getMenuItemText() {
    return ConstructorArgumentUtilities.getContructor0Parameter0Type(this.ancestorType).getName();
  }

  @Override
  public Icon getMenuItemIcon(ItemNode<? super InstanceCreation, Expression> step) {
    return null;
  }

  @Override
  public InstanceCreation getTransientValue(ItemNode<? super InstanceCreation, Expression> step) {
    return null;
  }

  @Override
  protected InstanceCreation createValue(Expression[] expressions) {
    if (expressions.length == 1) {
      Expression expression = expressions[0];
      if (expression instanceof FieldAccess) {
        FieldAccess fieldAccess = (FieldAccess) expression;
        AbstractField field = fieldAccess.field.getValue();
        if (field.isStatic()) {
          if (field instanceof JavaField) {
            JavaField argumentField = (JavaField) field;
            NamedUserType userType = TypeManager.getNamedUserTypeFromArgumentField(this.ancestorType, argumentField);
            NamedUserConstructor constructor = userType.getDeclaredConstructors().get(0);
            Expression[] argumentExpressions;
            if (constructor.getRequiredParameters().size() == 1) {
              argumentExpressions = new Expression[] {AstUtilities.createStaticFieldAccess(argumentField)};
            } else {
              argumentExpressions = new Expression[] {};
            }
            return AstUtilities.createInstanceCreation(constructor, argumentExpressions);
          }
        }
      } else {
        Logger.severe(expression);
      }
    }
    return null;
  }
}
