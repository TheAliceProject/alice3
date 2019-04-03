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
package org.alice.stageide.ast.declaration;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import org.alice.ide.IDE;
import org.alice.ide.ast.declaration.AddManagedFieldComposite;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.cascade.ExpressionCascadeContext;
import org.alice.ide.croquet.models.declaration.InstanceCreationFillInWithGalleryResourceParameter;
import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.alice.stageide.sceneeditor.draganddrop.SceneDropSite;
import org.lgna.croquet.AbstractComposite;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;
import org.lgna.story.SModel;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.implementation.alice.AliceResourceUtilties;

import java.awt.Dimension;
import java.util.List;
import java.util.UUID;

/**
 * @author user
 */
public class AddCopiedManagedFieldComposite extends AddManagedFieldComposite {
  private static class SingletonHolder {
    private static AddCopiedManagedFieldComposite instance = new AddCopiedManagedFieldComposite();
  }

  public static AddCopiedManagedFieldComposite getInstance() {
    return SingletonHolder.instance;
  }

  private static AbstractType<?, ?, ?> getDeclaringTypeFromInitializer(Expression expression) {
    if (expression instanceof InstanceCreation) {
      InstanceCreation instanceCreation = (InstanceCreation) expression;
      return instanceCreation.constructor.getValue().getDeclaringType();
    } else {
      return null;
    }
  }

  private final ValueListener<Expression> initializerListener = new ValueListener<Expression>() {
    @Override
    public void valueChanged(ValueEvent<Expression> e) {
      AddCopiedManagedFieldComposite.this.handleInitializerChanged(e.getNextValue());
    }
  };

  private InstanceCreation initialInstanceCreation;
  private UserField fieldToCopy = null;

  private AddCopiedManagedFieldComposite() {
    super(UUID.fromString("a14a3088-185c-4dfd-983c-af05e1d8dc14"), new FieldDetailsBuilder().valueComponentType(ApplicabilityStatus.DISPLAYED, null).valueIsArrayType(ApplicabilityStatus.APPLICABLE_BUT_NOT_DISPLAYED, false).initializer(ApplicabilityStatus.DISPLAYED, null).build());
    this.getInitializerState().addAndInvokeNewSchoolValueListener(initializerListener);
  }

  @Override
  protected EditCustomization customize(UserActivity userActivity, UserType<?> declaringType, UserField field, EditCustomization rv) {
    AffineMatrix4x4 initialTransform = null;
    DropSite dropSite = userActivity.findDropSite();
    if (dropSite instanceof SceneDropSite) {
      SceneDropSite sceneDropSite = (SceneDropSite) dropSite;
      initialTransform = sceneDropSite.getTransform();
    } else {
      AbstractType<?, ?, ?> type = field.getValueType();
      JavaType javaType = type.getFirstEncounteredJavaType();
      Class<?> cls = javaType.getClassReflectionProxy().getReification();
      if (SModel.class.isAssignableFrom(cls)) {
        initialTransform = AliceResourceUtilties.getDefaultInitialTransform(AliceResourceClassUtilities.getResourceClassForModelClass((Class<? extends SModel>) cls));
      } else {
        initialTransform = null;
      }
    }
    initialTransform = this.updateInitialTransformIfNecessary(initialTransform);
    AbstractSceneEditor sceneEditor = IDE.getActiveInstance().getSceneEditor();
    Statement[] doStatements = sceneEditor.getDoStatementsForCopyField(this.fieldToCopy, field, initialTransform);
    for (Statement s : doStatements) {
      rv.addDoStatement(s);
    }
    Statement[] undoStatements = sceneEditor.getUndoStatementsForAddField(field);
    for (Statement s : undoStatements) {
      rv.addUndoStatement(s);
    }
    return rv;
  }

  private void setInitializerInitialValue(InstanceCreation initialInstanceCreation) {
    this.initialInstanceCreation = initialInstanceCreation;
  }

  @Override
  protected String generateName() {
    String sourceName = fieldToCopy == null ? super.generateName() : fieldToCopy.getName();
    // Remove trailing digits to avoid odd name suggestions
    return sourceName.replaceAll("\\d*$", "");
  }

  public void setFieldToBeCopied(UserField fieldToCopy, Statement... setupStatements) {
    this.fieldToCopy = fieldToCopy;
    Expression newInitializer = AstUtilities.createCopy(fieldToCopy.initializer.getValue(), (NamedUserType) fieldToCopy.getDeclaringType());
    assert fieldToCopy.initializer.getValue() instanceof InstanceCreation;
    setInitializerInitialValue((InstanceCreation) newInitializer);
  }

  @Override
  protected Expression getInitializerInitialValue() {
    return this.initialInstanceCreation;
  }

  @Override
  protected AbstractType<?, ?, ?> getValueComponentTypeInitialValue() {
    return getDeclaringTypeFromInitializer(this.getInitializer());
  }

  private void handleInitializerChanged(Expression nextValue) {
    AbstractType<?, ?, ?> type = getDeclaringTypeFromInitializer(nextValue);
    this.getValueComponentTypeState().setValueTransactionlessly(type);
    this.getNameState().setValueTransactionlessly(this.getNameInitialValue());
    this.refreshStatus();
    final AbstractWindow<?> root = this.getView().getRoot();
    if (root != null) {
      Dimension preferredSize = root.getAwtComponent().getPreferredSize();
      Dimension size = root.getSize();
      if ((preferredSize.width > size.width) || (preferredSize.height > size.height)) {
        root.pack();
      }
    }
  }

  private class InitializerContext implements ExpressionCascadeContext {
    @Override
    public Expression getPreviousExpression() {
      //todo: investigate
      //org.lgna.project.ast.UserField field = getPreviewValue();
      //return field.initializer.getValue();
      return getInitializer();
      //return org.alice.ide.IDE.getActiveInstance().createCopy( getInitializer() );
    }

    @Override
    public BlockStatementIndexPair getBlockStatementIndexPair() {
      return null;
    }
  }

  private class ResourceKeyInitializerCustomizer implements ItemStateCustomizer<Expression> {
    private ExpressionCascadeContext pushedContext;

    @Override
    public CascadeFillIn getFillInFor(Expression value) {
      return null;
    }

    @Override
    public void prologue() {
      this.pushedContext = new InitializerContext();
      IDE.getActiveInstance().getExpressionCascadeManager().pushContext(this.pushedContext);
    }

    @Override
    public void epilogue() {
      IDE.getActiveInstance().getExpressionCascadeManager().popAndCheckContext(this.pushedContext);
      this.pushedContext = null;
    }

    @Override
    public void appendBlankChildren(List<CascadeBlankChild> blankChildren, BlankNode<Expression> blankNode) {
      Expression initializer = getInitializer();
      if (initializer instanceof InstanceCreation) {
        InstanceCreation instanceCreation = (InstanceCreation) initializer;
        AbstractConstructor constructor = instanceCreation.constructor.getValue();
        blankChildren.add(InstanceCreationFillInWithGalleryResourceParameter.getInstance(constructor));
        blankChildren.add(CascadeLineSeparator.getInstance());
      }
    }
  }

  @Override
  protected AbstractComposite.ItemStateCustomizer<Expression> createInitializerCustomizer() {
    return new ResourceKeyInitializerCustomizer();
  }

  @Override
  protected void handlePostHideDialog() {
    super.handlePostHideDialog();
    this.initialInstanceCreation = null;
  }
}
