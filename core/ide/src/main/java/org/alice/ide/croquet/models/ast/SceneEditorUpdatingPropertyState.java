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

package org.alice.ide.croquet.models.ast;

import edu.cmu.cs.dennisc.map.MapToMap;
import org.alice.ide.ast.PropertyState;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.croquet.Application;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.ast.UserField;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class SceneEditorUpdatingPropertyState extends PropertyState {
  private static MapToMap<UserField, JavaMethod, SceneEditorUpdatingPropertyState> mapToMap = MapToMap.newInstance();

  public static synchronized SceneEditorUpdatingPropertyState getInstanceForSetter(UserField field, JavaMethod setter) {
    return mapToMap.getInitializingIfAbsent(field, setter, new MapToMap.Initializer<UserField, JavaMethod, SceneEditorUpdatingPropertyState>() {
      @Override
      public SceneEditorUpdatingPropertyState initialize(UserField field, JavaMethod setter) {
        return new SceneEditorUpdatingPropertyState(field, setter);
      }
    });
  }

  public static synchronized SceneEditorUpdatingPropertyState getInstanceForGetter(UserField field, JavaMethod getter) {
    return getInstanceForSetter(field, AstUtilities.getSetterForGetter(getter));
  }

  private final UserField field;

  private SceneEditorUpdatingPropertyState(UserField field, JavaMethod setter) {
    super(Application.PROJECT_GROUP, UUID.fromString("f38ed248-1d68-43eb-b2c0-09ac62bd748e"), setter);
    this.field = field;
  }

  public UserField getField() {
    return this.field;
  }

  @Override
  protected void handleTruthAndBeautyValueChange(Expression nextValue) {
    super.handleTruthAndBeautyValueChange(nextValue);
    if (nextValue instanceof NullLiteral) {
      //do nothing for null literals
    } else {
      Expression e;
      if (this.field == null) {
        e = ThisExpression.createInstanceThatCanExistWithoutAnAncestorType(StorytellingSceneEditor.getInstance().getActiveSceneType());
      } else {
        e = new FieldAccess(field);
      }
      AbstractParameter parameter = this.getSetter().getRequiredParameters().get(0);
      SimpleArgument argument = new SimpleArgument(parameter, nextValue);
      MethodInvocation methodInvocation = new MethodInvocation(e, this.getSetter(), argument);
      Statement s = new ExpressionStatement(methodInvocation);
      StorytellingSceneEditor.getInstance().executeStatements(s);
    }
  }
}
