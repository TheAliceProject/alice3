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
package org.alice.ide.croquet.models.ast.cascade.statement;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ast.EmptyExpression;
import org.alice.ide.croquet.models.cascade.ExpressionBlank;
import org.alice.ide.croquet.models.cascade.ExpressionFillInWithExpressionBlanks;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.annotations.ArrayIndexDetails;
import org.lgna.project.ast.ArrayAccess;
import org.lgna.project.ast.AssignmentExpression;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.ast.UserField;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class FieldArrayAtIndexAssignmentFillIn extends ExpressionFillInWithExpressionBlanks<AssignmentExpression> {
  private static Map<UserField, FieldArrayAtIndexAssignmentFillIn> map = Maps.newHashMap();

  public static synchronized FieldArrayAtIndexAssignmentFillIn getInstance(UserField field) {
    FieldArrayAtIndexAssignmentFillIn rv = map.get(field);
    if (rv == null) {
      rv = new FieldArrayAtIndexAssignmentFillIn(field);
      map.put(field, rv);
    }
    return rv;
  }

  private final AssignmentExpression transientValue;

  private FieldArrayAtIndexAssignmentFillIn(UserField field) {
    super(UUID.fromString("3385e94e-3299-42d2-941f-435af105dee4"), ExpressionBlank.getBlankForType(Integer.class, ArrayIndexDetails.SINGLETON), ExpressionBlank.getBlankForType(field.valueType.getValue().getComponentType()));
    this.transientValue = AstUtilities.createFieldArrayAssignment(new ThisExpression(), field, new EmptyExpression(JavaType.INTEGER_OBJECT_TYPE), new EmptyExpression(field.valueType.getValue().getComponentType()));
  }

  private UserField getField() {
    ArrayAccess arrayAccess = (ArrayAccess) this.transientValue.leftHandSide.getValue();
    FieldAccess fieldAccess = (FieldAccess) arrayAccess.array.getValue();
    return (UserField) fieldAccess.field.getValue();
  }

  @Override
  protected AssignmentExpression createValue(Expression[] expressions) {
    return AstUtilities.createFieldArrayAssignment(this.getField(), expressions[0], expressions[1]);
  }

  @Override
  public AssignmentExpression getTransientValue(ItemNode<? super AssignmentExpression, Expression> node) {
    return this.transientValue;
  }
}
