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
package org.alice.ide.croquet.edits.ast;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import org.alice.ide.IDE;
import org.lgna.croquet.Application;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractNode;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionProperty;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.NodeUtilities;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPropertyEdit extends AbstractEdit {
  private final ExpressionProperty expressionProperty;
  private final Expression nextExpression;
  private final Expression prevExpression;

  public ExpressionPropertyEdit(UserActivity userActivity, ExpressionProperty expressionProperty, Expression prevExpression, Expression nextExpression) {
    super(userActivity);
    this.expressionProperty = expressionProperty;
    this.prevExpression = prevExpression;
    this.nextExpression = nextExpression;
  }

  public ExpressionPropertyEdit(BinaryDecoder binaryDecoder, Object step) {
    super(binaryDecoder, step);
    UUID expressionPropertyNodeId = binaryDecoder.decodeId();
    String propertyName = binaryDecoder.decodeString();
    UUID prevExpressionId = binaryDecoder.decodeId();
    UUID nextExpressionId = binaryDecoder.decodeId();

    IDE ide = IDE.getActiveInstance();
    Project project = ide.getProject();
    AbstractNode node = ProgramTypeUtilities.lookupNode(project, expressionPropertyNodeId);
    this.expressionProperty = (ExpressionProperty) node.getPropertyNamed(propertyName);
    this.prevExpression = ProgramTypeUtilities.lookupNode(project, prevExpressionId);
    this.nextExpression = ProgramTypeUtilities.lookupNode(project, nextExpressionId);
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    super.encode(binaryEncoder);
    Node node = (Node) this.expressionProperty.getOwner();
    binaryEncoder.encode(node.getId());
    binaryEncoder.encode(this.expressionProperty.getName());

    binaryEncoder.encode(this.prevExpression.getId());
    binaryEncoder.encode(this.nextExpression.getId());
  }

  protected void setValue(Expression expression) {
    this.expressionProperty.setValue(expression);
  }

  @Override
  protected final void doOrRedoInternal(boolean isDo) {
    this.setValue(this.nextExpression);
  }

  @Override
  protected final void undoInternal() {
    this.setValue(this.prevExpression);
  }

  @Override
  protected void appendDescription(StringBuilder rv, DescriptionStyle descriptionStyle) {
    rv.append("set: ");
    NodeUtilities.safeAppendRepr(rv, this.prevExpression, Application.getLocale());
    rv.append(" ===> ");
    NodeUtilities.safeAppendRepr(rv, this.nextExpression, Application.getLocale());
  }
}
