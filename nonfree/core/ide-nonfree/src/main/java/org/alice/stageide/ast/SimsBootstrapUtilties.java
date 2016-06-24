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

/**
 * @author Dennis Cosgrove
 */
public class SimsBootstrapUtilties extends BootstrapUtilties {

	public static org.lgna.project.ast.NamedUserType createProgramType( org.lgna.story.Paint floorAppearance, org.lgna.story.Paint wallAppearance, org.lgna.story.Paint ceilingAppearance, org.lgna.story.Color atmosphereColor, double fogDensity, org.lgna.story.Color aboveLightColor, org.lgna.story.Color belowLightColor ) {

		org.lgna.project.ast.UserField roomField = createPrivateFinalField( org.lgna.story.SRoom.class, "room" );

		roomField.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );

		org.lgna.project.ast.UserField[] modelFields = { roomField
		};
		java.util.ArrayList<org.lgna.project.ast.ExpressionStatement> setupStatements = new java.util.ArrayList<org.lgna.project.ast.ExpressionStatement>();

		org.lgna.project.ast.JavaMethod setFloorPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SRoom.class, "setFloorPaint", org.lgna.story.Paint.class, org.lgna.story.SetFloorPaint.Detail[].class );
		org.lgna.project.ast.Expression floorPaintExpression = null;
		org.lgna.project.ast.Expression wallPaintExpression = null;
		org.lgna.project.ast.Expression ceilingPaintExpression = null;

		try {
			if( floorAppearance instanceof org.lgna.story.SRoom.FloorAppearance ) {
				floorPaintExpression = createFieldAccess( (org.lgna.story.SRoom.FloorAppearance)floorAppearance );
			} else {
				floorPaintExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( floorAppearance );
			}

			if( wallAppearance instanceof org.lgna.story.SRoom.WallAppearance ) {
				wallPaintExpression = createFieldAccess( (org.lgna.story.SRoom.WallAppearance)wallAppearance );
			} else {
				wallPaintExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( wallAppearance );
			}

			if( ceilingAppearance instanceof org.lgna.story.SRoom.CeilingAppearance ) {
				ceilingPaintExpression = createFieldAccess( (org.lgna.story.SRoom.CeilingAppearance)ceilingAppearance );
			} else {
				ceilingPaintExpression = org.alice.stageide.StoryApiConfigurationManager.getInstance().getExpressionCreator().createExpression( ceilingAppearance );
			}
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "cannot create expression", e );
		}

		setupStatements.add( createMethodInvocationStatement( createThisFieldAccess( roomField ), setFloorPaintMethod, floorPaintExpression ) );
		org.lgna.project.ast.JavaMethod setWallPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SRoom.class, "setWallPaint", org.lgna.story.Paint.class, org.lgna.story.SetWallPaint.Detail[].class );
		setupStatements.add( createMethodInvocationStatement( createThisFieldAccess( roomField ), setWallPaintMethod, wallPaintExpression ) );
		org.lgna.project.ast.JavaMethod setCeilingPaintMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SRoom.class, "setCeilingPaint", org.lgna.story.Paint.class, org.lgna.story.SetCeilingPaint.Detail[].class );
		setupStatements.add( createMethodInvocationStatement( createThisFieldAccess( roomField ), setCeilingPaintMethod, ceilingPaintExpression ) );

		return createProgramType( modelFields, setupStatements.toArray( new org.lgna.project.ast.ExpressionStatement[ setupStatements.size() ] ), atmosphereColor, fogDensity, aboveLightColor, belowLightColor );
	}
}
