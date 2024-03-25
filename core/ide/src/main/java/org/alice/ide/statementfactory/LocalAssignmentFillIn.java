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

package org.alice.ide.statementfactory;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ast.IncompleteAstUtilities;
import org.alice.ide.croquet.models.cascade.ExpressionBlank;
import org.alice.ide.croquet.models.cascade.ExpressionFillInWithExpressionBlanks;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.AssignmentExpression;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.LocalAccess;
import org.lgna.project.ast.UserLocal;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class LocalAssignmentFillIn extends ExpressionFillInWithExpressionBlanks<AssignmentExpression> {
  private static Map<UserLocal, LocalAssignmentFillIn> map = Maps.newHashMap();

  public static synchronized LocalAssignmentFillIn getInstance(UserLocal local) {
    LocalAssignmentFillIn rv = map.get(local);
    if (rv == null) {
      rv = new LocalAssignmentFillIn(local);
      map.put(local, rv);
    }
    return rv;
  }

  private final AssignmentExpression transientValue;

  private LocalAssignmentFillIn(UserLocal local) {
    super(UUID.fromString("0a624cbf-fca2-4a89-a6b0-11415b8cc084"), ExpressionBlank.createBlanks(local.valueType.getValue()));
    this.transientValue = IncompleteAstUtilities.createIncompleteLocalAssignment(local);
  }

  private UserLocal getLocal() {
    LocalAccess localAccess = (LocalAccess) this.transientValue.leftHandSide.getValue();
    return localAccess.local.getValue();
  }

  @Override
  protected AssignmentExpression createValue(Expression[] expressions) {
    return AstUtilities.createLocalAssignment(this.getLocal(), expressions[0]);
  }

  @Override
  public AssignmentExpression getTransientValue(ItemNode<? super AssignmentExpression, Expression> node) {
    return this.transientValue;
  }
}
