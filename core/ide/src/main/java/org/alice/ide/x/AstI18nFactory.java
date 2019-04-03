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

import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import edu.cmu.cs.dennisc.property.ListProperty;
import org.alice.ide.IDE;
import org.alice.ide.ast.IdeExpression;
import org.alice.ide.ast.components.DeclarationNameLabel;
import org.alice.ide.ast.draganddrop.statement.StatementDragModel;
import org.alice.ide.codeeditor.CommentPane;
import org.alice.ide.codeeditor.ExpressionPropertyDropDownPane;
import org.alice.ide.codeeditor.ParametersPane;
import org.alice.ide.common.AbstractStatementPane;
import org.alice.ide.common.AnonymousConstructorPane;
import org.alice.ide.common.AssignmentExpressionPane;
import org.alice.ide.common.DefaultNodeListPropertyPane;
import org.alice.ide.common.DefaultStatementPane;
import org.alice.ide.common.ExpressionStatementPane;
import org.alice.ide.common.GetsPane;
import org.alice.ide.common.LocalDeclarationPane;
import org.alice.ide.common.LocalPane;
import org.alice.ide.common.TypeComponent;
import org.alice.ide.croquet.models.ast.cascade.ArgumentCascade;
import org.alice.ide.croquet.models.ast.cascade.ExpressionPropertyCascade;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState;
import org.alice.ide.i18n.MethodInvocationChunk;
import org.alice.ide.x.components.ExpressionListPropertyPane;
import org.alice.ide.x.components.ExpressionPropertyView;
import org.alice.ide.x.components.ExpressionView;
import org.alice.ide.x.components.FieldAccessView;
import org.alice.ide.x.components.InfixExpressionView;
import org.alice.ide.x.components.InstanceCreationView;
import org.alice.ide.x.components.InstancePropertyLabelView;
import org.alice.ide.x.components.KeyedArgumentView;
import org.alice.ide.x.components.ListPropertyLabelsView;
import org.alice.ide.x.components.NodePropertyView;
import org.alice.ide.x.components.ResourcePropertyView;
import org.alice.ide.x.components.StatementListPropertyView;
import org.alice.stageide.StoryApiConfigurationManager;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractArgument;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractNode;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AnonymousUserConstructor;
import org.lgna.project.ast.AssignmentExpression;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Code;
import org.lgna.project.ast.Comment;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionListProperty;
import org.lgna.project.ast.ExpressionProperty;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.InfixExpression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.KeyedArgumentListProperty;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.NodeProperty;
import org.lgna.project.ast.ResourceProperty;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.TypeExpression;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserMethod;

import java.awt.Paint;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public abstract class AstI18nFactory extends I18nFactory {
  protected abstract AbstractType<?, ?, ?> getFallBackTypeForThisExpression();

  protected SwingComponentView<?> EPIC_HACK_createWrapperIfNecessaryForExpressionPanelessComponent(SwingComponentView<?> component) {
    return component;
  }

  public Paint getInvalidExpressionPaint(Paint paint, int x, int y, int width, int height) {
    return paint;
  }

  public boolean isCommentMutable(Comment comment) {
    return false;
  }

  public boolean isSignatureLocked(Code code) {
    return true;
  }

  public boolean isLocalDraggableAndMutable(UserLocal local) {
    return true;
  }

  public SwingComponentView<?> createNameView(AbstractDeclaration declaration) {
    return new DeclarationNameLabel(declaration);
  }

  protected float getDeclarationNameFontScale() {
    return 1.1f;
  }

  @Override
  protected SwingComponentView<?> createComponent(MethodInvocationChunk methodInvocationChunk, InstancePropertyOwner owner) {
    String methodName = methodInvocationChunk.getMethodName();
    SwingComponentView<?> rv;
    if ((owner instanceof AbstractDeclaration) && methodName.equals("getName")) {
      AbstractDeclaration declaration = (AbstractDeclaration) owner;
      DeclarationNameLabel label = new DeclarationNameLabel(declaration);
      if (declaration instanceof AbstractMethod) {
        AbstractMethod method = (AbstractMethod) declaration;
        if (method.getReturnType() == JavaType.VOID_TYPE) {
          label.scaleFont(this.getDeclarationNameFontScale());
          label.changeFont(TextWeight.BOLD);
        }
      }
      rv = label;
    } else if ((owner instanceof SimpleArgument) && methodName.equals("getParameterNameText")) {
      SimpleArgument argument = (SimpleArgument) owner;
      rv = new DeclarationNameLabel(argument.parameter.getValue());
    } else if ((owner instanceof AbstractConstructor) && methodName.equals("getDeclaringType")) {
      AbstractConstructor constructor = (AbstractConstructor) owner;
      rv = this.createTypeComponent(constructor.getDeclaringType());
    } else if ((owner instanceof UserMethod) && methodName.equals("getReturnType")) {
      UserMethod method = (UserMethod) owner;
      rv = this.createTypeComponent(method.getReturnType());
    } else if ((owner instanceof UserCode) && methodName.equals("getParameters")) {
      UserCode code = (UserCode) owner;
      rv = new ParametersPane(this, code);
    } else {
      Method mthd = ReflectionUtilities.getMethod(owner.getClass(), methodName);
      Object o = ReflectionUtilities.invoke(owner, mthd);
      String s;
      if (o != null) {
        if (o instanceof AbstractType<?, ?, ?>) {
          s = ((AbstractType<?, ?, ?>) o).getName();
        } else {
          s = o.toString();
        }
      } else {
        s = null;
      }
      rv = new Label(s);
    }
    return rv;
  }

  protected ExpressionPropertyCascade getArgumentCascade(SimpleArgument simpleArgument) {
    return ArgumentCascade.getInstance(simpleArgument);
  }

  protected boolean isDropDownDesiredFor(ExpressionProperty expressionProperty) {
    Expression expression = expressionProperty.getValue();
    return IDE.getActiveInstance().isDropDownDesiredFor(expression);
  }

  public SwingComponentView<?> createArgumentPane(AbstractArgument argument, SwingComponentView<?> prefixPane) {
    if (argument instanceof SimpleArgument) {
      SimpleArgument simpleArgument = (SimpleArgument) argument;
      ExpressionProperty expressionProperty = simpleArgument.expression;
      SwingComponentView<?> rv = new ExpressionPropertyView(this, expressionProperty);
      if (this.isDropDownDesiredFor(expressionProperty)) {
        ExpressionPropertyCascade model = this.getArgumentCascade(simpleArgument);
        ExpressionPropertyDropDownPane expressionPropertyDropDownPane = new ExpressionPropertyDropDownPane(model.getRoot().getPopupPrepModel(), prefixPane, rv, expressionProperty);
        rv = expressionPropertyDropDownPane;
      }
      return rv;
    } else if (argument instanceof JavaKeyedArgument) {
      JavaKeyedArgument keyedArgument = (JavaKeyedArgument) argument;
      return new KeyedArgumentView(this, keyedArgument);
    } else {
      throw new RuntimeException("todo: " + argument);
    }
  }

  protected SwingComponentView<?> createInstanceCreationPane(InstanceCreation instanceCreation) {
    AbstractConstructor constructor = instanceCreation.constructor.getValue();
    if (constructor instanceof AnonymousUserConstructor) {
      return new AnonymousConstructorPane(this, (AnonymousUserConstructor) constructor);
    } else {
      return new InstanceCreationView(this, instanceCreation);
    }
  }

  protected SwingComponentView<?> createFieldAccessPane(FieldAccess fieldAccess) {
    return new FieldAccessView(this, fieldAccess);
  }

  @Override
  protected SwingComponentView<?> createGetsComponent(boolean isTowardLeadingEdge) {
    return new GetsPane(isTowardLeadingEdge);
  }

  private SwingComponentView<?> createTypeComponent(AbstractType<?, ?, ?> type) {
    return TypeComponent.createInstance(type);
  }

  protected SwingComponentView<?> createLocalDeclarationPane(UserLocal userLocal) {
    return new LocalDeclarationPane(userLocal, this.createLocalPane(userLocal));
  }

  protected SwingComponentView<?> createLocalPane(UserLocal userLocal) {
    return new LocalPane(userLocal, this.isLocalDraggableAndMutable(userLocal));
  }

  protected SwingComponentView<?> createGenericNodePropertyPane(NodeProperty<?> nodeProperty) {
    return new NodePropertyView(this, nodeProperty);
  }

  public abstract SwingComponentView<?> createExpressionPropertyPane(ExpressionProperty expressionProperty, AbstractType<?, ?, ?> type);

  public final SwingComponentView<?> createExpressionPropertyPane(ExpressionProperty expressionProperty) {
    return this.createExpressionPropertyPane(expressionProperty, null);
  }

  protected SwingComponentView<?> createResourcePropertyPane(ResourceProperty resourceProperty) {
    return new ResourcePropertyView(this, resourceProperty);
  }

  protected SwingComponentView<?> createStatementListPropertyPane(StatementListProperty statementListProperty) {
    return new StatementListPropertyView(this, statementListProperty);
  }

  protected abstract SwingComponentView<?> createSimpleArgumentListPropertyPane(SimpleArgumentListProperty argumentListProperty);

  protected abstract SwingComponentView<?> createKeyedArgumentListPropertyPane(KeyedArgumentListProperty argumentListProperty);

  protected SwingComponentView<?> createExpressionListPropertyPane(ExpressionListProperty expressionListProperty) {
    return new ExpressionListPropertyPane(this, expressionListProperty);
  }

  protected SwingComponentView<?> createGenericNodeListPropertyPane(NodeListProperty<AbstractNode> nodeListProperty) {
    return new DefaultNodeListPropertyPane(this, nodeListProperty);
  }

  protected SwingComponentView<?> createGenericListPropertyPane(ListProperty<Object> listProperty) {
    return new ListPropertyLabelsView(this, listProperty);
  }

  protected SwingComponentView<?> createGenericInstancePropertyPane(InstanceProperty property) {
    return new InstancePropertyLabelView(this, property);
  }

  public AbstractStatementPane createStatementPane(DragModel dragModel, Statement statement, StatementListProperty statementListProperty) {
    AbstractStatementPane rv;
    if (statement instanceof ExpressionStatement) {
      rv = new ExpressionStatementPane(dragModel, this, (ExpressionStatement) statement, statementListProperty);
    } else if (statement instanceof Comment) {
      rv = new CommentPane(dragModel, this, (Comment) statement, statementListProperty);
    } else {
      rv = new DefaultStatementPane(dragModel, this, statement, statementListProperty);
    }
    return rv;
  }

  public AbstractStatementPane createStatementPane(Statement statement) {
    return this.createStatementPane(StatementDragModel.getInstance(statement), statement, null);
  }

  protected abstract SwingComponentView<?> createIdeExpressionPane(IdeExpression ideExpression);

  public SwingComponentView<?> createExpressionPane(Expression expression) {
    if (expression instanceof IdeExpression) {
      IdeExpression ideExpression = (IdeExpression) expression;
      return this.createIdeExpressionPane(ideExpression);
    } else {
      SwingComponentView<?> rv = null;
      if (expression instanceof InfixExpression) {
        rv = new InfixExpressionView(this, (InfixExpression<? extends Enum<?>>) expression);
      } else if (expression instanceof AssignmentExpression) {
        rv = new AssignmentExpressionPane(this, (AssignmentExpression) expression);
      } else if (expression instanceof FieldAccess) {
        rv = this.createFieldAccessPane((FieldAccess) expression);
      } else if (expression instanceof TypeExpression) {
        if (FormatterState.getInstance().getValue().isTypeExpressionDesired()) {
          TypeExpression typeExpression = (TypeExpression) expression;
          Node parent = typeExpression.getParent();
          if (parent instanceof MethodInvocation) {
            MethodInvocation methodInvocation = (MethodInvocation) parent;
            Node grandparent = methodInvocation.getParent();
            if (grandparent instanceof JavaKeyedArgument) {
              JavaKeyedArgument javaKeyedArgument = (JavaKeyedArgument) grandparent;
              AbstractType<?, ?, ?> type = AstUtilities.getKeywordFactoryType(javaKeyedArgument);
              if (type != null) {
                rv = new Label(type.getName() + ".");
                //rv.makeStandOut();
              }
            }
          }
          if (rv != null) {
            //pass
          } else {
            AbstractType<?, ?, ?> type = typeExpression.value.getValue();
            rv = new LineAxisPanel(new DeclarationNameLabel(type), new Label("."));
          }
        } else {
          rv = new Label();
        }
      } else if (expression instanceof InstanceCreation) {
        rv = this.createInstanceCreationPane((InstanceCreation) expression);
        //        } else if( expression instanceof org.lgna.project.ast.AbstractLiteral ) {
        //          rv = this.createComponent( expression );
      } else {
        SwingComponentView<?> component = null;
        if (expression != null) {
          Node parent = expression.getParent();
          if (parent instanceof MethodInvocation) {
            MethodInvocation methodInvocation = (MethodInvocation) parent;
            if (expression == methodInvocation.expression.getValue()) {
              AbstractType<?, ?, ?> type = StoryApiConfigurationManager.getInstance().getBuildMethodPoseBuilderType(methodInvocation);
              if (type != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("new ");
                sb.append(type.getName());
                sb.append("(...).");
                component = new Label(sb.toString());
              }
            }
          }
        }
        if (component != null) {
          rv = this.EPIC_HACK_createWrapperIfNecessaryForExpressionPanelessComponent(component);
        } else {
          if (IsIncludingTypeFeedbackForExpressionsState.getInstance().getValue()) {
            rv = new ExpressionView(this, expression);
          } else {
            if (IsIncludingTypeFeedbackForExpressionsState.getInstance().getValue()) {
              rv = new ExpressionView(this, expression);
            } else {
              component = this.createComponent(expression);
              rv = this.EPIC_HACK_createWrapperIfNecessaryForExpressionPanelessComponent(component);
            }
          }
        }
      }
      return rv;
    }
  }

  private static final Set<String> LOCAL_PROPERTY_NAMES = Sets.newHashSet("local", "item", "variable", "constant");

  @Override
  protected SwingComponentView<?> createPropertyComponent(InstanceProperty<?> property, int underscoreCount) {
    //todo:
    String propertyName = property.getName();
    //

    SwingComponentView<?> rv;
    if (underscoreCount == 2) {
      if (LOCAL_PROPERTY_NAMES.contains(propertyName)) {
        rv = this.createLocalDeclarationPane((UserLocal) property.getValue());
      } else {
        rv = new Label("TODO: handle underscore count 2: " + propertyName);
      }
    } else if (underscoreCount == 1) {
      if (LOCAL_PROPERTY_NAMES.contains(propertyName)) {
        rv = this.createLocalPane((UserLocal) property.getValue());
      } else {
        rv = new Label("TODO: handle underscore count 1: " + propertyName);
      }
    } else {
      rv = null;
      if (rv != null) {
        //pass
      } else {
        if (property instanceof NodeProperty<?>) {
          if (property instanceof ExpressionProperty) {
            rv = this.createExpressionPropertyPane((ExpressionProperty) property);
          } else {
            rv = this.createGenericNodePropertyPane((NodeProperty<?>) property);
          }
        } else if (property instanceof ResourceProperty) {
          rv = this.createResourcePropertyPane((ResourceProperty) property);
        } else if (property instanceof ListProperty<?>) {
          if (property instanceof NodeListProperty<?>) {
            if (property instanceof StatementListProperty) {
              rv = this.createStatementListPropertyPane((StatementListProperty) property);
            } else if (property instanceof SimpleArgumentListProperty) {
              rv = this.createSimpleArgumentListPropertyPane((SimpleArgumentListProperty) property);
            } else if (property instanceof KeyedArgumentListProperty) {
              rv = this.createKeyedArgumentListPropertyPane((KeyedArgumentListProperty) property);
            } else if (property instanceof ExpressionListProperty) {
              rv = this.createExpressionListPropertyPane((ExpressionListProperty) property);
            } else {
              rv = this.createGenericNodeListPropertyPane((NodeListProperty<AbstractNode>) property);
            }
          } else {
            rv = this.createGenericListPropertyPane((ListProperty<Object>) property);
          }
        } else {
          rv = this.createGenericInstancePropertyPane(property);
        }
      }
    }
    assert rv != null : property;
    return rv;
  }
}
