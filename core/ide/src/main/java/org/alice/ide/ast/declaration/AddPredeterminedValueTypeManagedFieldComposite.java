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

import org.alice.ide.Theme;
import org.alice.ide.typemanager.TypeManager;
import org.alice.stageide.icons.IconFactoryManager;
import org.lgna.croquet.icon.EmptyIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.views.Dialog;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaType;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddPredeterminedValueTypeManagedFieldComposite extends AddManagedFieldComposite {
  private final JavaType javaValueType;
  private AbstractType<?, ?, ?> type;

  private AddPredeterminedValueTypeManagedFieldComposite(UUID migrationId, JavaType javaValueType) {
    super(migrationId, new FieldDetailsBuilder().valueComponentType(ApplicabilityStatus.DISPLAYED, null).valueIsArrayType(ApplicabilityStatus.APPLICABLE_BUT_NOT_DISPLAYED, false).initializer(ApplicabilityStatus.DISPLAYED, null).build());
    this.javaValueType = javaValueType;
    IconFactory iconFactory = IconFactoryManager.getIconFactoryForType(this.javaValueType);
    if ((iconFactory != null) && (iconFactory != EmptyIconFactory.getInstance())) {
      this.getLaunchOperation().setButtonIcon(iconFactory.getIcon(Theme.SMALL_SQUARE_ICON_SIZE));
    }
  }

  public AddPredeterminedValueTypeManagedFieldComposite(UUID migrationId, Class<?> valueCls) {
    this(migrationId, JavaType.getInstance(valueCls));
  }

  protected boolean isUserTypeDesired() {
    return true;
  }

  @Override
  protected void handlePreShowDialog(Dialog dialog) {
    if (this.isUserTypeDesired()) {
      this.type = TypeManager.getNamedUserTypeFromSuperType(this.javaValueType);
    } else {
      this.type = this.javaValueType;
    }
    super.handlePreShowDialog(dialog);
  }

  @Override
  protected void handlePostHideDialog() {
    super.handlePostHideDialog();
    this.type = null;
  }

  @Override
  protected AbstractType<?, ?, ?> getValueComponentTypeInitialValue() {
    return this.type;
  }

  @Override
  protected Expression getInitializerInitialValue() {
    return AstUtilities.createInstanceCreation(this.type);
  }
}
