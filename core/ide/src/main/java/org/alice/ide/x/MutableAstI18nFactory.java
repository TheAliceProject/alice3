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
package org.alice.ide.x;

import org.alice.ide.ast.IdeExpression;
import org.alice.ide.ast.declaration.DeclarationNameState;
import org.alice.ide.codeeditor.ArgumentListPropertyPane;
import org.alice.ide.codeeditor.ExpressionPropertyDropDownPane;
import org.alice.ide.common.AbstractStatementPane;
import org.alice.ide.croquet.models.ast.DefaultExpressionPropertyCascade;
import org.alice.ide.croquet.models.ast.StatementContextMenu;
import org.alice.ide.x.components.ExpressionPropertyView;
import org.alice.ide.x.components.KeyedArgumentListPropertyView;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.Group;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ArgumentListProperty;
import org.lgna.project.ast.ArgumentOwner;
import org.lgna.project.ast.DeclarationProperty;
import org.lgna.project.ast.ExpressionProperty;
import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.KeyedArgumentListProperty;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;

/**
 * @author Dennis Cosgrove
 */
public abstract class MutableAstI18nFactory extends AstI18nFactory {
  private final Group group;

  public MutableAstI18nFactory(Group group) {
    this.group = group;
  }

  @Override
  public SwingComponentView<?> createNameView(AbstractDeclaration declaration) {
    final boolean IS_DECLARATION_NAME_STATE_READY_FOR_PRIME_TIME = false;
    if (IS_DECLARATION_NAME_STATE_READY_FOR_PRIME_TIME) {
      //      if( declaration instanceof org.lgna.project.ast.Code ) {
      //        org.lgna.project.ast.Code code = (org.lgna.project.ast.Code)declaration;
      //        //todo
      //        if( this.isSignatureLocked( code ) ) {
      //          return super.createNameView( declaration );
      //        }
      //      }
      return DeclarationNameState.getInstance(declaration).createSubduedTextField();
    } else {
      return super.createNameView(declaration);
    }
  }

  @Override
  protected AbstractType<?, ?, ?> getFallBackTypeForThisExpression() {
    return null;
  }

  public boolean isStatementListPropertyMutable(StatementListProperty statementListProperty) {
    return true;
  }

  public boolean isKeyedArgumentListMutable(ArgumentListProperty<JavaKeyedArgument> argumentListProperty) {
    return true;
  }

  @Override
  protected SwingComponentView<?> createKeyedArgumentListPropertyPane(KeyedArgumentListProperty argumentListProperty) {
    ArgumentOwner owner = argumentListProperty.getOwner();
    DeclarationProperty<? extends AbstractCode> codeProperty = owner.getParameterOwnerProperty();
    AbstractCode code = codeProperty.getValue();
    if (code.getKeyedParameter() != null) {
      return new KeyedArgumentListPropertyView(this, argumentListProperty);
    } else {
      return new Label();
    }

  }

  @Override
  protected SwingComponentView<?> createIdeExpressionPane(IdeExpression ideExpression) {
    throw new RuntimeException(ideExpression.toString());
  }

  @Override
  protected SwingComponentView<?> createSimpleArgumentListPropertyPane(SimpleArgumentListProperty argumentListProperty) {
    return new ArgumentListPropertyPane(this, argumentListProperty);
  }

  @Override
  public SwingComponentView<?> createExpressionPropertyPane(ExpressionProperty expressionProperty, AbstractType<?, ?, ?> desiredValueType) {
    SwingComponentView<?> rv = new ExpressionPropertyView(this, expressionProperty);
    if (this.isDropDownDesiredFor(expressionProperty)) {
      DefaultExpressionPropertyCascade model = DefaultExpressionPropertyCascade.getInstance(group, expressionProperty, desiredValueType);
      ExpressionPropertyDropDownPane expressionPropertyDropDownPane = new ExpressionPropertyDropDownPane(model.getRoot().getPopupPrepModel(), null, rv, expressionProperty);
      rv = expressionPropertyDropDownPane;
    }
    return rv;
  }

  protected boolean isStatementContextMenuDesiredFor(Statement statement) {
    return true;
  }

  @Override
  public AbstractStatementPane createStatementPane(DragModel dragModel, Statement statement, StatementListProperty statementListProperty) {
    AbstractStatementPane abstractStatementPane = super.createStatementPane(dragModel, statement, statementListProperty);
    if (this.isStatementContextMenuDesiredFor(statement)) {
      abstractStatementPane.setPopupPrepModel(StatementContextMenu.getInstance(statement).getPopupPrepModel());
    }
    return abstractStatementPane;
  }

}
