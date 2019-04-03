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
package org.alice.ide.members.components.templates;

import edu.cmu.cs.dennisc.property.event.ListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ListPropertyListener;
import edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter;
import org.alice.ide.ast.IncompleteAstUtilities;
import org.alice.ide.ast.draganddrop.expression.FunctionInvocationDragModel;
import org.alice.ide.templates.ExpressionTemplate;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;

/**
 * @author Dennis Cosgrove
 */
public class FunctionInvocationTemplate extends ExpressionTemplate {
  private AbstractMethod method;
  private ListPropertyListener<UserParameter> parameterAdapter = new SimplifiedListPropertyAdapter<UserParameter>() {
    @Override
    protected void changing(ListPropertyEvent<UserParameter> e) {
    }

    @Override
    protected void changed(ListPropertyEvent<UserParameter> e) {
      FunctionInvocationTemplate.this.refresh();
    }
  };

  public FunctionInvocationTemplate(AbstractMethod method) {
    super(FunctionInvocationDragModel.getInstance(method));
    this.method = method;
    if (method instanceof UserMethod) {
      UserMethod userMethod = (UserMethod) method;
      this.setPopupPrepModel(new MethodPopupMenuModel(userMethod).getPopupPrepModel());
    }
  }

  @Override
  protected Expression createIncompleteExpression() {
    return IncompleteAstUtilities.createIncompleteMethodInvocation(this.method);
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    if (this.method instanceof UserMethod) {
      this.refresh();
      ((UserMethod) this.method).requiredParameters.addListPropertyListener(this.parameterAdapter);
    }
  }

  @Override
  protected void handleUndisplayable() {
    if (this.method instanceof UserMethod) {
      ((UserMethod) this.method).requiredParameters.removeListPropertyListener(this.parameterAdapter);
    }
    super.handleUndisplayable();
  }
}
