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
package org.alice.ide.croquet.models.ast;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.croquet.models.ast.cascade.ExpressionPropertyCascade;
import org.alice.ide.croquet.models.cascade.ExpressionBlank;
import org.lgna.croquet.Application;
import org.lgna.croquet.Group;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionProperty;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class DefaultExpressionPropertyCascade extends ExpressionPropertyCascade {
  private static Map<ExpressionProperty, DefaultExpressionPropertyCascade> projectGroupMap = Maps.newHashMap();
  private static Map<ExpressionProperty, DefaultExpressionPropertyCascade> inheritGroupMap = Maps.newHashMap();

  private static AbstractType<?, ?, ?> getDesiredType(ExpressionProperty expressionProperty, AbstractType<?, ?, ?> desiredType) {
    if (desiredType == null) {
      desiredType = expressionProperty.getExpressionType();
    }
    return desiredType;
  }

  public static synchronized DefaultExpressionPropertyCascade getInstance(Group group, ExpressionProperty expressionProperty, AbstractType<?, ?, ?> desiredType) {
    desiredType = getDesiredType(expressionProperty, desiredType);
    Map<ExpressionProperty, DefaultExpressionPropertyCascade> map;
    if (group == Application.PROJECT_GROUP) {
      map = projectGroupMap;
    } else if (group == Application.INHERIT_GROUP) {
      map = inheritGroupMap;
    } else {
      throw new RuntimeException(group.toString());
    }
    DefaultExpressionPropertyCascade rv = map.get(expressionProperty);
    if (rv == null) {
      rv = new DefaultExpressionPropertyCascade(group, expressionProperty, desiredType);
      map.put(expressionProperty, rv);
    }
    return rv;
  }

  private DefaultExpressionPropertyCascade(Group group, ExpressionProperty expressionProperty, AbstractType<?, ?, ?> desiredType) {
    super(group, UUID.fromString("77532795-0674-4ba4-ad18-989ee9ca0507"), expressionProperty, ExpressionBlank.createBlanks(desiredType));
  }

  @Override
  protected Expression createExpression(Expression[] expressions) {
    assert expressions.length == 1;
    return expressions[0];
  }
}
