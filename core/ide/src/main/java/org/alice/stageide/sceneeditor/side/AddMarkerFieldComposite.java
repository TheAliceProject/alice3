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
package org.alice.stageide.sceneeditor.side;

import org.alice.ide.ast.ExpressionCreator;
import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.alice.ide.ast.declaration.AddManagedFieldComposite;
import org.alice.ide.ast.declaration.AddPredeterminedValueTypeManagedFieldComposite;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.SetUpMethodGenerator;
import org.lgna.croquet.CustomItemState;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.Dialog;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;
import org.lgna.story.Color;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.SMarker;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddMarkerFieldComposite extends AddPredeterminedValueTypeManagedFieldComposite {
  public AddMarkerFieldComposite(UUID migrationId, Class<? extends SMarker> cls) {
    super(migrationId, cls);
  }

  private final CustomItemState<Expression> colorIdState = this.createInitialPropertyValueExpressionState("colorIdState", Color.RED, SMarker.class, "setColorId", Color.class, null);

  public CustomItemState<Expression> getColorIdState() {
    return this.colorIdState;
  }

  protected abstract Color getInitialMarkerColor();

  @Override
  protected void handlePreShowDialog(Dialog dialog) {
    Color initialMarkerColor = this.getInitialMarkerColor();
    try {
      Expression colorExpresion = StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator().createExpression(initialMarkerColor);
      this.colorIdState.setValueTransactionlessly(colorExpresion);
    } catch (ExpressionCreator.CannotCreateExpressionException ccee) {
      ccee.printStackTrace();
    }
    super.handlePreShowDialog(dialog);
  }

  protected abstract AffineMatrix4x4 getInitialMarkerTransform();

  @Override
  protected boolean isUserTypeDesired() {
    return false;
  }

  @Override
  protected boolean isNameGenerationDesired() {
    //todo?
    return true;
  }

  @Override
  protected boolean isNumberAppendedToNameOfFirstField() {
    return true;
  }

  private static JavaMethod COLOR_ID_SETTER = JavaMethod.getInstance(SMarker.class, "setColorId", Color.class);

  @Override
  protected AddManagedFieldComposite.EditCustomization customize(UserActivity userActivity, UserType<?> declaringType, UserField field, AddManagedFieldComposite.EditCustomization rv) {
    super.customize(userActivity, declaringType, field, rv);
    AffineMatrix4x4 initialMarkerTransform = this.getInitialMarkerTransform();
    rv.addDoStatement(SetUpMethodGenerator.createSetterStatement(false, field, COLOR_ID_SETTER, this.colorIdState.getValue()));
    try {
      Statement orientationStatement = SetUpMethodGenerator.createOrientationStatement(false, field, new Orientation(initialMarkerTransform.orientation));
      rv.addDoStatement(orientationStatement);
    } catch (CannotCreateExpressionException ccee) {
      ccee.printStackTrace();
    }
    try {
      Statement positionStatement = SetUpMethodGenerator.createPositionStatement(false, field, new Position(initialMarkerTransform.translation));
      rv.addDoStatement(positionStatement);
    } catch (CannotCreateExpressionException ccee) {
      ccee.printStackTrace();
    }
    return rv;
  }

}
