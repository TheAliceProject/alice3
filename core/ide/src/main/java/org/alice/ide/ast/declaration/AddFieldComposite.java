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

package org.alice.ide.ast.declaration;

import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import org.alice.ide.croquet.edits.ast.DeclareFieldEdit;
import org.alice.ide.identifier.IdentifierNameGenerator;
import org.alice.ide.typemanager.ConstructorArgumentUtilities;
import org.alice.stageide.croquet.models.gallerybrowser.preferences.IsPromptProvidingInitialFieldNamesState;
import org.alice.stageide.icons.PlusIconFactory;
import org.lgna.croquet.CustomItemState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AccessLevel;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldModifierFinalVolatileOrNeither;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.StaticAnalysisUtilities;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;

import java.awt.Dimension;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddFieldComposite extends FieldComposite {
  public static class FieldDetailsBuilder {
    private ApplicabilityStatus isFinalStatus = ApplicabilityStatus.NOT_APPLICABLE;
    private boolean inFinalInitialValue;
    private ApplicabilityStatus valueComponentTypeStatus;
    private AbstractType<?, ?, ?> valueComponentTypeInitialValue;
    private ApplicabilityStatus valueIsArrayTypeStatus;
    private boolean valueIsArrayTypeInitialValue;
    private ApplicabilityStatus initializerStatus;
    private Expression initializerInitialValue;

    public FieldDetailsBuilder isFinal(ApplicabilityStatus status, boolean initialValue) {
      this.isFinalStatus = status;
      this.inFinalInitialValue = initialValue;
      return this;
    }

    public FieldDetailsBuilder valueComponentType(ApplicabilityStatus status, AbstractType<?, ?, ?> initialValue) {
      this.valueComponentTypeStatus = status;
      this.valueComponentTypeInitialValue = initialValue;
      return this;
    }

    public FieldDetailsBuilder valueIsArrayType(ApplicabilityStatus status, boolean initialValue) {
      this.valueIsArrayTypeStatus = status;
      this.valueIsArrayTypeInitialValue = initialValue;
      return this;
    }

    public FieldDetailsBuilder initializer(ApplicabilityStatus status, Expression initialValue) {
      this.initializerStatus = status;
      this.initializerInitialValue = initialValue;
      return this;
    }

    public Details build() {
      assert this.valueComponentTypeStatus != null : this;
      assert this.valueIsArrayTypeStatus != null : this;
      assert this.initializerStatus != null : this;
      return new Details().isFinal(this.isFinalStatus, this.inFinalInitialValue).valueComponentType(this.valueComponentTypeStatus, this.valueComponentTypeInitialValue).valueIsArrayType(this.valueIsArrayTypeStatus, this.valueIsArrayTypeInitialValue).name(ApplicabilityStatus.EDITABLE).initializer(this.initializerStatus, this.initializerInitialValue);
    }
  }

  public AddFieldComposite(UUID migrationId, Details details) {
    super(migrationId, details);
  }

  protected abstract ManagementLevel getManagementLevel();

  protected abstract boolean isFieldFinal();

  private UserField createField() {
    UserField field = new UserField();
    if (this.isFieldFinal()) {
      field.finalVolatileOrNeither.setValue(FieldModifierFinalVolatileOrNeither.FINAL);
    }
    field.accessLevel.setValue(AccessLevel.PRIVATE);
    field.managementLevel.setValue(this.getManagementLevel());
    field.valueType.setValue(this.getValueType());
    field.name.setValue(this.getDeclarationLikeSubstanceName());
    field.initializer.setValue(this.getInitializer());
    return field;
  }

  @Override
  protected void localize() {
    super.localize();
    int size = UIManagerUtilities.getDefaultFontSize() + 4;
    this.getLaunchOperation().setSmallIcon(PlusIconFactory.getInstance().getIcon(new Dimension(size, size)));
  }

  @Override
  public String modifyNameIfNecessary(String text) {
    text = super.modifyNameIfNecessary(text);
    if (text != null) {
      String declaringTypeName;
      if (this.getDeclaringType() != null) {
        declaringTypeName = this.getDeclaringType().getName();
      } else {
        declaringTypeName = "";
      }
      text = text.replace("</declaringType/>", declaringTypeName);
    }
    return text;
  }

  @Override
  public UserField getPreviewValue() {
    return this.createField();
  }

  protected abstract DeclareFieldEdit createEdit(UserActivity userActivity, UserType<?> declaringType, UserField field);

  @Override
  protected final Edit createEdit(UserActivity userActivity) {
    return this.createEdit(userActivity, this.getDeclaringType(), this.createField());
  }

  @Override
  protected boolean isNameAvailable(String name) {
    return StaticAnalysisUtilities.isAvailableFieldName(name, this.getDeclaringType());
  }

  protected InstanceCreation getInstanceCreationFromInitializer() {
    CustomItemState<Expression> initializerState = this.getInitializerState();
    if (initializerState != null) {
      Expression expression = initializerState.getValue();
      if (expression instanceof InstanceCreation) {
        return (InstanceCreation) expression;
      }
    }
    return null;
  }

  protected Field getFldFromInstanceCreationInitializer(InstanceCreation instanceCreation) {
    if (instanceCreation != null) {
      JavaField argumentField = ConstructorArgumentUtilities.getArgumentField(instanceCreation);
      if (argumentField != null) {
        Field fld = argumentField.getFieldReflectionProxy().getReification();
        return fld;
      }
    }
    return null;
  }

  protected String generateName() {
    InstanceCreation instanceCreation = this.getInstanceCreationFromInitializer();
    return IdentifierNameGenerator.SINGLETON.createIdentifierNameFromInstanceCreation(instanceCreation);
  }

  protected boolean isNumberAppendedToNameOfFirstField() {
    return false;
  }

  protected boolean isNameGenerationDesired() {
    return IsPromptProvidingInitialFieldNamesState.getInstance().getValue();
  }

  @Override
  protected String getNameInitialValue() {
    if (this.isNameGenerationDesired()) {
      String baseName = generateName();
      boolean isNumberAppendedToNameOfFirstField = this.isNumberAppendedToNameOfFirstField();
      final boolean IS_GENERATING_AVAILABLE_NAME_ENABLED = true;
      if (IS_GENERATING_AVAILABLE_NAME_ENABLED) {
        boolean isSearchFrom2Desired;
        if (isNumberAppendedToNameOfFirstField || this.isNameAvailable(baseName)) {
          if (this.isNameAvailable(baseName + 1)) {
            isSearchFrom2Desired = false;
          } else {
            isSearchFrom2Desired = true;
          }
        } else {
          isSearchFrom2Desired = true;
        }
        if (isSearchFrom2Desired) {
          int i = 2;
          while (true) {
            if (this.isNameAvailable(baseName + i)) {
              break;
            }
            i++;
          }
          return baseName + i;
        }
      }
      if (isNumberAppendedToNameOfFirstField) {
        return baseName + 1;
      } else {
        return baseName;
      }
    } else {
      return super.getNameInitialValue();
    }
  }
}
