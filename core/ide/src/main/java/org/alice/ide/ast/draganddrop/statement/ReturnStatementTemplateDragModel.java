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

package org.alice.ide.ast.draganddrop.statement;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ast.IncompleteAstUtilities;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.croquet.models.ast.cascade.statement.ReturnStatementInsertCascade;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.ReturnStatement;
import org.lgna.project.ast.UserMethod;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ReturnStatementTemplateDragModel extends StatementTemplateDragModel {
  private static Map<UserMethod, ReturnStatementTemplateDragModel> map = Maps.newHashMap();

  public static synchronized ReturnStatementTemplateDragModel getInstance(UserMethod method) {
    ReturnStatementTemplateDragModel rv = map.get(method);
    if (rv == null) {
      rv = new ReturnStatementTemplateDragModel(method);
      map.put(method, rv);
    }
    return rv;
  }

  private final UserMethod method;

  private ReturnStatementTemplateDragModel(UserMethod method) {
    super(UUID.fromString("7c9b66c3-9cb9-4fbc-bce1-d8dbf33808b2"), ReturnStatement.class, IncompleteAstUtilities.createIncompleteReturnStatement(
        //todo
        method != null ? method.getReturnType() : JavaType.OBJECT_TYPE));
    this.method = method;
  }

  @Override
  public Triggerable getDropOperation(DragStep step, BlockStatementIndexPair blockStatementIndexPair) {
    return ReturnStatementInsertCascade.getInstance(blockStatementIndexPair);
  }
}
