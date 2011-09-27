/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.stageide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public class SetUpMethodGenerator {
	private static org.alice.ide.ast.ExpressionCreator getExpressionCreator() {
		return org.alice.stageide.StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator();
	}

	private static org.lgna.project.ast.Expression createInstanceExpression( boolean isThis, org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.Expression thisExpression = new org.lgna.project.ast.ThisExpression();
		if( isThis ) {
			return thisExpression;
		} else {
			return new org.lgna.project.ast.FieldAccess( thisExpression, field );
		}
	}

	private static org.lgna.project.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? > parameterCls, org.lgna.project.ast.Expression instanceExpression, org.lgna.project.ast.Expression argumentExpression ) {
		org.lgna.project.ast.AbstractMethod method = org.alice.ide.ast.AstUtilities.lookupMethod( declarationCls, methodName, parameterCls );
		return org.alice.ide.ast.AstUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpression );
	}
	private static org.lgna.project.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? >[] parameterClses, org.lgna.project.ast.Expression instanceExpression, org.lgna.project.ast.Expression... argumentExpressions ) {
		org.lgna.project.ast.AbstractMethod method = org.alice.ide.ast.AstUtilities.lookupMethod( declarationCls, methodName, parameterClses );
		return org.alice.ide.ast.AstUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpressions );
	}

	public static org.lgna.project.ast.InstanceCreation createExpression( org.lgna.story.resources.sims2.PersonResource personResource ) {
		Class<?>[] parameterClses = { 
				org.lgna.story.resources.sims2.Gender.class, 
				org.lgna.story.resources.sims2.SkinTone.class, 
				org.lgna.story.resources.sims2.EyeColor.class, 
				org.lgna.story.resources.sims2.Hair.class, 
				Number.class, 
				org.lgna.story.resources.sims2.Outfit.class 
		};
		org.lgna.project.ast.Expression[] arguments = {
				getExpressionCreator().createExpression( personResource.getGender() ),
				getExpressionCreator().createExpression( personResource.getSkinTone() ),
				getExpressionCreator().createExpression( personResource.getEyeColor() ),
				getExpressionCreator().createExpression( personResource.getHair() ),
				getExpressionCreator().createExpression( personResource.getObesityLevel() ),
				getExpressionCreator().createExpression( personResource.getOutfit() )
		};
		return org.alice.ide.ast.AstUtilities.createInstanceCreation( personResource.getClass(), parameterClses, arguments );
	}
	
	
	public static org.lgna.project.ast.InstanceCreation createInstanceCreation( org.lgna.story.resources.sims2.PersonResource personResource ) {
		org.lgna.project.ast.AbstractType< ?,?,? > type = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Biped.class );
		org.lgna.project.ast.InstanceCreation rv = org.alice.ide.ast.AstUtilities.createInstanceCreation( type );
		return rv;
	}
	
//	public static void fillInAutomaticPointOfViewAssignment(org.lgna.project.ast.StatementListProperty bodyStatementsProperty, org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractField pointOfViewField)
//	{
//		if (pointOfViewField != null)
//		{
//			bodyStatementsProperty.add( createStatement( org.lgna.story.Turnable.class, "moveAndOrientTo", new Class< ? >[] {org.lgna.story.Entity.class, Number.class}, SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( false, pointOfViewField ), getExpressionCreator().createExpression( 0.0 ) ) );
//		}
//	}
	
	public static void fillInAutomaticVehicleAssignment(org.lgna.project.ast.StatementListProperty bodyStatementsProperty, org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractField vehicleField, boolean isVehicleScene)
	{
		if (vehicleField != null)
		{
			bodyStatementsProperty.add( createStatement( org.lgna.story.Turnable.class, "setVehicle", org.lgna.story.Entity.class, SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( isVehicleScene, vehicleField ) ) );
		}
	}
	
	public static org.lgna.project.ast.ExpressionStatement createSetVehicleStatement(org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractField vehicleField, boolean isVehicleScene)
	{
		if (vehicleField != null || isVehicleScene)
		{
			return createStatement( org.lgna.story.Turnable.class, "setVehicle", org.lgna.story.Entity.class, SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( isVehicleScene, vehicleField ) );
		}
		return null;
	}

	public static org.lgna.project.ast.Statement createOrientationStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.story.Orientation orientation ) {
		return createStatement( 
				org.lgna.story.Turnable.class, "setOrientationRelativeToVehicle", org.lgna.story.Orientation.class, 
				SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( orientation ) 
		);
	}
	public static org.lgna.project.ast.Statement createPositionStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.story.Position position ) {
		return createStatement( 
				org.lgna.story.MovableTurnable.class, "setPositionRelativeToVehicle", org.lgna.story.Position.class, 
				SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( position ) 
		);
	}
	
	public static void fillInAutomaticSetUpMethod( org.lgna.project.ast.StatementListProperty bodyStatementsProperty, boolean isThis, org.lgna.project.ast.AbstractField field, Object instance, org.lgna.project.virtualmachine.UserInstance sceneInstance ) {
		if( instance instanceof org.lgna.story.Entity ) {
			org.lgna.story.Entity entity = (org.lgna.story.Entity)instance;
			String name = field.getName();
			bodyStatementsProperty.add( 
					createStatement( 
							org.lgna.story.Entity.class, "setName", String.class, 
							SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( field.getName() ) 
					) 
			);

			if( instance instanceof org.lgna.story.Scene ) {
				org.lgna.story.Scene scene = (org.lgna.story.Scene)entity;
				bodyStatementsProperty.add( createStatement( org.lgna.story.Scene.class, "setAtmosphereColor", org.lgna.story.Color.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( scene.getAtmosphereColor() ) ) );
			} else {
				if ( instance instanceof org.lgna.story.MutableRider )
				{
					org.lgna.story.Entity vehicle = entity.getVehicle();
					boolean isVehicleScene = (vehicle instanceof org.lgna.story.Scene);
		
					org.lgna.project.ast.AbstractField vehicleField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava(vehicle);
					bodyStatementsProperty.add( 
							createStatement( 
									org.lgna.story.MutableRider.class, "setVehicle", org.lgna.story.Entity.class, 
									SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( isVehicleScene, vehicleField ) 
							) 
					);
				}
				if( instance instanceof org.lgna.story.Turnable ) {
					org.lgna.story.Turnable turnable = (org.lgna.story.Turnable)instance;
					org.lgna.story.Orientation orientation = turnable.getOrientationRelativeToVehicle();
					bodyStatementsProperty.add( 
							createStatement( 
									org.lgna.story.Turnable.class, "setOrientationRelativeToVehicle", org.lgna.story.Orientation.class, 
									SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( orientation ) 
							) 
					);
					if( turnable instanceof org.lgna.story.MovableTurnable ) {
						org.lgna.story.MovableTurnable movableTurnable = (org.lgna.story.MovableTurnable)turnable;
						org.lgna.story.Position position = movableTurnable.getPositionRelativeToVehicle();
						bodyStatementsProperty.add( 
								createStatement( 
										org.lgna.story.MovableTurnable.class, "setPositionRelativeToVehicle", org.lgna.story.Position.class, 
										SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( position ) 
								) 
						);
						if( instance instanceof org.lgna.story.Billboard ) {
							org.lgna.story.Billboard billboard = (org.lgna.story.Billboard)movableTurnable;
							bodyStatementsProperty.add( createStatement( org.lgna.story.Billboard.class, "setFrontPaint", org.lgna.story.Paint.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( billboard.getFrontPaint() ) ) );
							bodyStatementsProperty.add( createStatement( org.lgna.story.Billboard.class, "setBackPaint", org.lgna.story.Paint.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( billboard.getBackPaint() ) ) );
						}
					}
				}
			}
		}
		if( instance instanceof org.lgna.story.Resizable ) {
			org.lgna.story.Resizable resizable = (org.lgna.story.Resizable)instance;
			org.lgna.story.Scale scale = resizable.getScale();
			bodyStatementsProperty.add( createStatement( org.lgna.story.Resizable.class, "setScale", new Class< ? >[] { org.lgna.story.Scale.class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( scale ) ) );
		}
		
		if( instance != null ) {
			org.lgna.project.ast.JavaType javaType = org.lgna.project.ast.JavaType.getInstance( instance.getClass() );
			for( org.lgna.project.ast.JavaMethod getter : org.alice.ide.ast.AstUtilities.getDeclaredPersistentPropertyGetters( javaType ) ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle:", getter );
			}
		}
//		
//		
//				if (transformable instanceof CameraMarker)
//				{
//					CameraMarker marker = (CameraMarker)transformable;
//					//CameraMarkers are all fields of the Scene but may be parented to the camera while being edited in the scene editor. Because of this, make sure to get their LocalPointOfView as seen by the Scene
//					bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Turnable.class, "setLocalPointOfView", org.lookingglassandalice.storytelling.VantagePoint.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( transformable.getPointOfView(AsSeenBy.SCENE) ) ) );
//					org.lookingglassandalice.storytelling.Color markerColor = marker.getColor();
//					bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Marker.class, "setColor", new Class< ? >[] { org.lookingglassandalice.storytelling.Color.class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), createExpression( markerColor ) ) );
//				}
//				else
//				{
//					bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Turnable.class, "setLocalPointOfView", org.lookingglassandalice.storytelling.VantagePoint.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( transformable.getLocalPointOfView() ) ) );
//				}
//
//					if( instance instanceof org.lookingglassandalice.storytelling.Model ) {
//						org.lookingglassandalice.storytelling.Model model = (org.lookingglassandalice.storytelling.Model)transformable;
//
//						org.lookingglassandalice.storytelling.Color modelColor = model.getColor();
//						bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Model.class, "setColor", new Class< ? >[] { org.lookingglassandalice.storytelling.Color.class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), createExpression( modelColor ) ) );
//						
//						Double modelOpacity = model.getOpacity();
//						bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Model.class, "setOpacity", new Class< ? >[] { Number.class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), createExpression( modelOpacity ) ) );
//						
//						if( model instanceof org.lookingglassandalice.storytelling.Text ) {
//							org.lookingglassandalice.storytelling.Text text = (org.lookingglassandalice.storytelling.Text)model;
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Text.class, "setValue", String.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( text.getValue() ) ) );
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Text.class, "setFont", org.lookingglassandalice.storytelling.Font.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( text.getFont() ) ) );
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Text.class, "setLetterHeight", Number.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( text.getLetterHeight() ) ) );
//						} else if( model instanceof org.lookingglassandalice.storytelling.Person ) {
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Person.class, "setGender", org.lookingglassandalice.storytelling.Gender.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( person.getGender() ) ) );
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Person.class, "setHair", org.lookingglassandalice.storytelling.Hair.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( (Enum)person.getHair() ) ) );
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Person.class, "setEyeColor", org.lookingglassandalice.storytelling.EyeColor.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( (Enum)person.getEyeColor() ) ) );
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Person.class, "setSkinTone", org.lookingglassandalice.storytelling.SkinTone.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( (Enum)person.getSkinTone() ) ) );
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Person.class, "setOutfit", org.lookingglassandalice.storytelling.Outfit.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( (Enum)person.getOutfit() ) ) );
//							bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Person.class, "setFitnessLevel", Number.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( person.getFitnessLevel() ) ) );
//
//						}
//				}
////				bodyStatementsProperty.add( createStatement( org.lookingglassandalice.storytelling.Entity.class, "addComponent", org.lookingglassandalice.storytelling.Transformable.class, new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), SetUpMethodGenerator.createInstanceExpression( isThis, field ) ) );
//		}
	}
}
