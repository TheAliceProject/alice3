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

package org.alice.ide.instancefactory.croquet;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.ApiConfigurationManager;
import org.alice.ide.IDE;
import org.alice.ide.MetaDeclarationFauxState;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.ast.fieldtree.FieldNode;
import org.alice.ide.ast.fieldtree.FieldTree;
import org.alice.ide.ast.fieldtree.RootNode;
import org.alice.ide.ast.fieldtree.TypeNode;
import org.alice.ide.croquet.models.cascade.MethodNameSeparator;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.LocalAccessFactory;
import org.alice.ide.instancefactory.ParameterAccessFactory;
import org.alice.ide.instancefactory.ParameterAccessMethodInvocationFactory;
import org.alice.ide.instancefactory.ThisFieldAccessFactory;
import org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory;
import org.alice.ide.instancefactory.ThisInstanceFactory;
import org.alice.ide.instancefactory.croquet.codecs.InstanceFactoryCodec;
import org.alice.ide.meta.DeclarationMeta;
import org.alice.ide.project.ProjectChangeOfInterestManager;
import org.alice.ide.project.events.ProjectChangeOfInterestListener;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.Application;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.CascadeItemMenuCombo;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.CustomItemStateWithInternalBlank;
import org.lgna.croquet.PrepModel;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.edits.StateEdit;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.Lambda;
import org.lgna.project.ast.LambdaExpression;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.NodeUtilities;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserLambda;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SProp;
import org.lgna.story.SQuadruped;
import org.lgna.story.SShape;
import org.lgna.story.SSlitherer;
import org.lgna.story.SSwimmer;
import org.lgna.story.SThing;
import org.lgna.story.STransport;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryState extends CustomItemStateWithInternalBlank<InstanceFactory> {
  public InstanceFactoryState(ProjectDocumentFrame projectDocumentFrame) {
    super(Application.DOCUMENT_UI_GROUP, UUID.fromString("f4e26c9c-0c3d-4221-95b3-c25df0744a97"), null, InstanceFactoryCodec.SINGLETON);
    projectDocumentFrame.getMetaDeclarationFauxState().addValueListener(declarationListener);
    ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener(this.projectChangeOfInterestListener);
  }

  private void fallBackToDefaultFactory() {
    this.setValueTransactionlessly(ThisInstanceFactory.getInstance());
  }

  private void handleDeclarationChanged(AbstractDeclaration prevValue, AbstractDeclaration nextValue) {
    if (this.ignoreCount == 0) {
      if (nextValue instanceof AbstractMethod) {
        AbstractMethod method = (AbstractMethod) nextValue;
        if (method.isStatic()) {
          this.setValueTransactionlessly(null);
          return;
        }
      }
      InstanceFactory instanceFactory = this.getValue();
      if (instanceFactory != null) {
        if (instanceFactory.isValid()) {
          //pass
        } else {
          this.fallBackToDefaultFactory();
        }
      } else {
        this.fallBackToDefaultFactory();
      }
      //      org.lgna.project.ast.AbstractType< ?,?,? > prevType = getDeclaringType( prevValue );
      //      org.lgna.project.ast.AbstractType< ?,?,? > nextType = getDeclaringType( nextValue );
      //      if( prevType != nextType ) {
      //        InstanceFactory prevValue = this.getValue();
      //        if( prevType != null ) {
      //          if( prevValue != null ) {
      //            map.put( prevType, prevValue );
      //          } else {
      //            map.remove( prevType );
      //          }
      //        }
      //        InstanceFactory nextValue;
      //        if( nextType != null ) {
      //          nextValue = map.get( nextType );
      //          if( nextValue != null ) {
      //            //pass
      //          } else {
      //            nextValue = org.alice.ide.instancefactory.ThisInstanceFactory.getInstance();
      //          }
      //        } else {
      //          nextValue = null;
      //        }
      //        this.setValueTransactionlessly( nextValue );
      //      }
    }
  }

  private void handleAstChangeThatCouldBeOfInterest() {
    InstanceFactory instanceFactory = this.getValue();
    if (instanceFactory != null) {
      if (instanceFactory.isValid()) {
        //pass
      } else {
        this.fallBackToDefaultFactory();
      }
    } else {
      this.fallBackToDefaultFactory();
    }
  }

  private static CascadeBlankChild<InstanceFactory> createFillInMenuComboIfNecessary(CascadeFillIn<InstanceFactory, Void> item, CascadeMenuModel<InstanceFactory> subMenu) {
    if (subMenu != null) {
      return new CascadeItemMenuCombo<InstanceFactory>(item, subMenu);
    } else {
      return item;
    }
  }

  /* package-private */
  static CascadeBlankChild<InstanceFactory> createFillInMenuComboIfNecessaryForField(ApiConfigurationManager apiConfigurationManager, UserField field) {
    return createFillInMenuComboIfNecessary(InstanceFactoryFillIn.getInstance(ThisFieldAccessFactory.getInstance(field)), apiConfigurationManager.getInstanceFactorySubMenuForThisFieldAccess(field));
  }

  @Override
  protected void appendPrepModelsToCascadeRootPath(List<PrepModel> cascadeRootPath, Edit edit) {
    super.appendPrepModelsToCascadeRootPath(cascadeRootPath, edit);
    if (edit instanceof StateEdit) {
      StateEdit<InstanceFactory> stateEdit = (StateEdit<InstanceFactory>) edit;
      InstanceFactory nextValue = stateEdit.getNextValue();
      if (nextValue instanceof ThisFieldAccessMethodInvocationFactory) {
        ThisFieldAccessMethodInvocationFactory thisFieldAccessMethodInvocationFactory = (ThisFieldAccessMethodInvocationFactory) nextValue;
        UserField field = thisFieldAccessMethodInvocationFactory.getField();
        IDE ide = IDE.getActiveInstance();
        ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
        cascadeRootPath.add(apiConfigurationManager.getInstanceFactorySubMenuForThisFieldAccess(field));
      }
    } else {
      throw new RuntimeException(edit != null ? edit.toString() : null);
    }
  }

  //todo
  private final ParametersVariablesAndConstantsSeparator parametersVariablesConstantsSeparator = new ParametersVariablesAndConstantsSeparator();

  @Override
  protected void updateBlankChildren(List<CascadeBlankChild> blankChildren, BlankNode<InstanceFactory> blankNode) {
    IDE ide = IDE.getActiveInstance();
    ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
    AbstractDeclaration declaration = DeclarationMeta.getDeclaration();
    boolean isStaticMethod;
    if (declaration instanceof AbstractMethod) {
      AbstractMethod method = (AbstractMethod) declaration;
      isStaticMethod = method.isStatic();
    } else {
      isStaticMethod = false;
    }
    AbstractType<?, ?, ?> type = DeclarationMeta.getType();

    if (isStaticMethod) {
      //pass
    } else {
      blankChildren.add(createFillInMenuComboIfNecessary(InstanceFactoryFillIn.getInstance(ThisInstanceFactory.getInstance()), apiConfigurationManager.getInstanceFactorySubMenuForThis(type)));
    }
    if (type instanceof NamedUserType) {
      NamedUserType namedUserType = (NamedUserType) type;
      if (isStaticMethod) {
        //pass
      } else {
        List<UserField> fields = namedUserType.getDeclaredFields();
        List<UserField> filteredFields = Lists.newLinkedList();
        for (UserField field : fields) {
          if (apiConfigurationManager.isInstanceFactoryDesiredForType(field.getValueType())) {
            filteredFields.add(field);
          }
        }
        final boolean IS_COLLAPSE_DESIRED = true;
        if (IS_COLLAPSE_DESIRED && (filteredFields.size() > 16)) {
          RootNode root = FieldTree.createTreeFor(filteredFields, FieldTree.createFirstClassThreshold(SBiped.class), FieldTree.createFirstClassThreshold(SQuadruped.class), FieldTree.createFirstClassThreshold(SSwimmer.class), FieldTree.createFirstClassThreshold(SFlyer.class), FieldTree.createFirstClassThreshold(SSlitherer.class),

                                                  FieldTree.createSecondClassThreshold(SProp.class), FieldTree.createSecondClassThreshold(STransport.class), FieldTree.createSecondClassThreshold(SShape.class), FieldTree.createSecondClassThreshold(SThing.class), FieldTree.createSecondClassThreshold(Object.class));
          for (FieldNode fieldNode : root.getFieldNodes()) {
            blankChildren.add(createFillInMenuComboIfNecessaryForField(apiConfigurationManager, fieldNode.getDeclaration()));
          }
          for (TypeNode typeNode : root.getTypeNodes()) {
            blankChildren.add(new TypeCascadeMenuModel(typeNode, apiConfigurationManager));
          }
        } else {
          for (UserField field : filteredFields) {
            blankChildren.add(createFillInMenuComboIfNecessaryForField(apiConfigurationManager, field));
          }
        }
      }

      AbstractCode code = ide.getDocumentFrame().getFocusedCode();
      if (code instanceof UserCode) {

        List<CascadeBlankChild> parameters = Lists.newLinkedList();
        List<CascadeBlankChild> locals = Lists.newLinkedList();
        boolean containsVariable = false;
        boolean containsConstant = false;
        UserCode userCode = (UserCode) code;
        for (UserParameter parameter : userCode.getRequiredParamtersProperty()) {
          if (apiConfigurationManager.isInstanceFactoryDesiredForType(parameter.getValueType())) {
            parameters.add(createFillInMenuComboIfNecessary(InstanceFactoryFillIn.getInstance(ParameterAccessFactory.getInstance(parameter)), apiConfigurationManager.getInstanceFactorySubMenuForParameterAccess(parameter)));
          }
        }

        for (UserLocal local : ProgramTypeUtilities.getLocals(userCode)) {
          if (apiConfigurationManager.isInstanceFactoryDesiredForType(local.getValueType())) {
            if (local.isFinal.getValue()) {
              containsConstant = true;
            } else {
              containsVariable = true;
            }
            locals.add(createFillInMenuComboIfNecessary(InstanceFactoryFillIn.getInstance(LocalAccessFactory.getInstance(local)), apiConfigurationManager.getInstanceFactorySubMenuForLocalAccess(local)));
          }
        }
        if ((parameters.size() > 0) || (locals.size() > 0)) {
          blankChildren.add(CascadeLineSeparator.getInstance());
          blankChildren.add(this.parametersVariablesConstantsSeparator);
          StringBuilder sb = new StringBuilder();
          NodeUtilities.safeAppendRepr(sb, code);
          sb.append(" ");
          String prefix = "";
          if (parameters.size() > 0) {
            sb.append("parameters");
            blankChildren.addAll(parameters);
            prefix = ", ";
          }
          if (locals.size() > 0) {
            if (containsVariable) {
              sb.append(prefix);
              sb.append("variables");
              prefix = ", ";
            }
            if (containsConstant) {
              sb.append(prefix);
              sb.append("constants");
              prefix = ", ";
            }
            blankChildren.addAll(locals);
          }
          this.parametersVariablesConstantsSeparator.setMenuItemText(sb.toString());
        }

        if (userCode instanceof UserMethod) {
          UserMethod userMethod = (UserMethod) userCode;
          if (StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME.equals(userMethod.getName())) {
            for (Statement statement : userMethod.body.getValue().statements) {
              if (statement instanceof ExpressionStatement) {
                ExpressionStatement expressionStatement = (ExpressionStatement) statement;
                Expression expression = expressionStatement.expression.getValue();
                if (expression instanceof MethodInvocation) {
                  MethodInvocation methodInvocation = (MethodInvocation) expression;
                  List<CascadeBlankChild> methodInvocationBlankChildren = Lists.newLinkedList();

                  for (SimpleArgument argument : methodInvocation.requiredArguments) {
                    Expression argumentExpression = argument.expression.getValue();
                    if (argumentExpression instanceof LambdaExpression) {
                      LambdaExpression lambdaExpression = (LambdaExpression) argumentExpression;
                      Lambda lambda = lambdaExpression.value.getValue();
                      if (lambda instanceof UserLambda) {
                        UserLambda userLambda = (UserLambda) lambda;
                        for (UserParameter parameter : userLambda.getRequiredParameters()) {
                          AbstractType<?, ?, ?> parameterType = parameter.getValueType();
                          for (AbstractMethod parameterMethod : AstUtilities.getAllMethods(parameterType)) {
                            AbstractType<?, ?, ?> parameterMethodReturnType = parameterMethod.getReturnType();
                            if (parameterMethodReturnType.isAssignableTo(SThing.class)) {
                              methodInvocationBlankChildren.add(createFillInMenuComboIfNecessary(InstanceFactoryFillIn.getInstance(ParameterAccessMethodInvocationFactory.getInstance(parameter, parameterMethod)), apiConfigurationManager.getInstanceFactorySubMenuForParameterAccessMethodInvocation(parameter, parameterMethod)));
                            }
                          }
                        }
                      }
                    }
                  }

                  if (methodInvocationBlankChildren.size() > 0) {
                    AbstractMethod method = methodInvocation.method.getValue();
                    blankChildren.add(MethodNameSeparator.getInstance(method));
                    blankChildren.addAll(methodInvocationBlankChildren);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  protected InstanceFactory getSwingValue() {
    return this.value;
  }

  @Override
  protected void setSwingValue(InstanceFactory value) {
    this.value = value;
  }

  private int ignoreCount = 0;

  public void pushIgnoreAstChanges() {
    ignoreCount++;
  }

  public void popIgnoreAstChanges() {
    ignoreCount--;
    if (ignoreCount == 0) {
      this.handleAstChangeThatCouldBeOfInterest();
    }
  }

  private final MetaDeclarationFauxState.ValueListener declarationListener = new MetaDeclarationFauxState.ValueListener() {
    @Override
    public void changed(AbstractDeclaration prevValue, AbstractDeclaration nextValue) {
      InstanceFactoryState.this.handleDeclarationChanged(prevValue, nextValue);
    }
  };
  //todo: map AbstractCode to Stack< InstanceFactory >
  //private java.util.Map< org.lgna.project.ast.AbstractDeclaration, InstanceFactory > map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
  private InstanceFactory value;

  private final ProjectChangeOfInterestListener projectChangeOfInterestListener = new ProjectChangeOfInterestListener() {
    @Override
    public void projectChanged() {
      handleAstChangeThatCouldBeOfInterest();
    }
  };
}
