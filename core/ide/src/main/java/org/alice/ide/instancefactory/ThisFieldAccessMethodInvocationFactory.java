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

package org.alice.ide.instancefactory;

import edu.cmu.cs.dennisc.map.MapToMap;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.UserField;

/**
 * @author Dennis Cosgrove
 */
public class ThisFieldAccessMethodInvocationFactory extends MethodInvocationFactory {
  private static MapToMap<UserField, AbstractMethod, ThisFieldAccessMethodInvocationFactory> mapToMap = MapToMap.newInstance();

  public static synchronized ThisFieldAccessMethodInvocationFactory getInstance(UserField field, AbstractMethod method) {
    assert field != null;
    return mapToMap.getInitializingIfAbsent(field, method, new MapToMap.Initializer<UserField, AbstractMethod, ThisFieldAccessMethodInvocationFactory>() {
      @Override
      public ThisFieldAccessMethodInvocationFactory initialize(UserField field, AbstractMethod method) {
        return new ThisFieldAccessMethodInvocationFactory(field, method);
      }
    });
  }

  private final UserField field;

  private ThisFieldAccessMethodInvocationFactory(UserField field, AbstractMethod method) {
    super(method, field.name);
    this.field = field;
  }

  @Override
  protected AbstractType<?, ?, ?> getValidInstanceType(AbstractType<?, ?, ?> type, AbstractCode code) {
    AbstractType<?, ?, ?> fieldDeclarationType = this.field.getDeclaringType();
    if ((fieldDeclarationType != null) && fieldDeclarationType.isAssignableFrom(type)) {
      return this.field.getValueType();
    } else {
      return null;
    }
  }

  public UserField getField() {
    return this.field;
  }

  private FieldAccess createFieldAccess(Expression expression) {
    return new FieldAccess(expression, this.field);
  }

  @Override
  protected Expression createTransientExpressionForMethodInvocation() {
    return this.createFieldAccess(createTransientThisExpression());
  }

  @Override
  protected Expression createExpressionForMethodInvocation() {
    return this.createFieldAccess(createThisExpression());
  }

  @Override
  protected StringBuilder addAccessRepr(StringBuilder rv) {
    rv.append("this.");
    rv.append(this.field.getName());
    return rv;
  }
}
