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

package org.alice.stageide.ast;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.stageide.StoryApiConfigurationManager;
import org.lgna.project.ast.DoubleLiteral;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.story.Color;
import org.lgna.story.Paint;
import org.lgna.story.SGround;
import org.lgna.story.SRoom;
import org.lgna.story.SetCeilingPaint;
import org.lgna.story.SetFloorPaint;
import org.lgna.story.SetOpacity;
import org.lgna.story.SetPaint;
import org.lgna.story.SetWallPaint;

import java.util.ArrayList;

/**
 * @author Dennis Cosgrove
 */
public class SimsBootstrapUtilities extends BootstrapUtilities {
  //groundAppearance is optional and will cause the program to generate a ground in addition to a room
  public static NamedUserType createProgramType(SGround.SurfaceAppearance groundAppearance, Paint floorAppearance, Paint wallAppearance, Paint ceilingAppearance, Color atmosphereColor, double fogDensity, Color aboveLightColor, Color belowLightColor, double groundOpacity) {

    UserField roomField = createPrivateFinalField(SRoom.class, "room");
    roomField.managementLevel.setValue(ManagementLevel.MANAGED);

    UserField groundField = groundAppearance != null ? createPrivateFinalField(SGround.class, "ground") : null;
    if (groundField != null) {
      groundField.managementLevel.setValue(ManagementLevel.MANAGED);
    }

    UserField[] modelFields;
    if (groundField != null) {
      modelFields = new UserField[] {roomField, groundField};
    } else {
      modelFields = new UserField[] {roomField};
    }

    ArrayList<ExpressionStatement> setupStatements = new ArrayList<ExpressionStatement>();

    JavaMethod setFloorPaintMethod = JavaMethod.getInstance(SRoom.class, "setFloorPaint", Paint.class, SetFloorPaint.Detail[].class);
    Expression floorPaintExpression = null;
    Expression wallPaintExpression = null;
    Expression ceilingPaintExpression = null;

    try {
      if (floorAppearance instanceof SRoom.FloorAppearance) {
        floorPaintExpression = createFieldAccess((SRoom.FloorAppearance) floorAppearance);
      } else {
        floorPaintExpression = StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression(floorAppearance);
      }

      if (wallAppearance instanceof SRoom.WallAppearance) {
        wallPaintExpression = createFieldAccess((SRoom.WallAppearance) wallAppearance);
      } else {
        wallPaintExpression = StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression(wallAppearance);
      }

      if (ceilingAppearance instanceof SRoom.CeilingAppearance) {
        ceilingPaintExpression = createFieldAccess((SRoom.CeilingAppearance) ceilingAppearance);
      } else {
        ceilingPaintExpression = StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression(ceilingAppearance);
      }
    } catch (org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e) {
      Logger.errln("cannot create expression", e);
    }

    setupStatements.add(createMethodInvocationStatement(new FieldAccess(roomField), setFloorPaintMethod, floorPaintExpression));
    JavaMethod setWallPaintMethod = JavaMethod.getInstance(SRoom.class, "setWallPaint", Paint.class, SetWallPaint.Detail[].class);
    setupStatements.add(createMethodInvocationStatement(new FieldAccess(roomField), setWallPaintMethod, wallPaintExpression));
    JavaMethod setCeilingPaintMethod = JavaMethod.getInstance(SRoom.class, "setCeilingPaint", Paint.class, SetCeilingPaint.Detail[].class);
    setupStatements.add(createMethodInvocationStatement(new FieldAccess(roomField), setCeilingPaintMethod, ceilingPaintExpression));

    if (groundAppearance != null) {
      JavaMethod setGroundPaintMethod = JavaMethod.getInstance(SGround.class, "setPaint", Paint.class, SetPaint.Detail[].class);
      setupStatements.add(createMethodInvocationStatement(new FieldAccess(groundField), setGroundPaintMethod, createFieldAccess(groundAppearance)));
      if (groundOpacity != 1) {
        JavaMethod setGroundOpacityMethod = JavaMethod.getInstance(SGround.class, "setOpacity", Number.class, SetOpacity.Detail[].class);
        setupStatements.add(createMethodInvocationStatement(new FieldAccess(groundField), setGroundOpacityMethod, new DoubleLiteral(groundOpacity)));
      }
    }

    return createProgramType(modelFields, setupStatements.toArray(new ExpressionStatement[setupStatements.size()]), atmosphereColor, fogDensity, aboveLightColor, belowLightColor);
  }
}
