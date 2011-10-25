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

import org.lgna.story.ImplementationAccessor;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

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
		org.lgna.project.ast.AbstractMethod method = org.lgna.project.ast.AstUtilities.lookupMethod( declarationCls, methodName, parameterCls );
		return org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpression );
	}
	private static org.lgna.project.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? >[] parameterClses, org.lgna.project.ast.Expression instanceExpression, org.lgna.project.ast.Expression... argumentExpressions ) {
		org.lgna.project.ast.AbstractMethod method = org.lgna.project.ast.AstUtilities.lookupMethod( declarationCls, methodName, parameterClses );
		return org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpressions );
	}

	public static org.lgna.project.ast.InstanceCreation createSims2PersonRecourseInstanceCreation( org.lgna.story.resources.sims2.PersonResource personResource ) throws org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException {
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
		return org.lgna.project.ast.AstUtilities.createInstanceCreation( personResource.getClass(), parameterClses, arguments );
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
			bodyStatementsProperty.add( createStatement( org.lgna.story.MutableRider.class, "setVehicle", org.lgna.story.Entity.class, SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( isVehicleScene, vehicleField ) ) );
		}
	}
	
	public static org.lgna.project.ast.ExpressionStatement createSetVehicleStatement(org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractField vehicleField, boolean isVehicleScene)
	{
		if (vehicleField != null || isVehicleScene)
		{
			return createStatement( org.lgna.story.MutableRider.class, "setVehicle", org.lgna.story.Entity.class, SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( isVehicleScene, vehicleField ) );
		}
		else if (vehicleField == null)
		{
			return createStatement( org.lgna.story.MutableRider.class, "setVehicle", org.lgna.story.Entity.class, SetUpMethodGenerator.createInstanceExpression( false, field ), new org.lgna.project.ast.NullLiteral());
		}
		return null;
	}

	public static org.lgna.project.ast.Statement createOrientationStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.story.Orientation orientation ) throws org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException {
		return createStatement( 
				org.lgna.story.Turnable.class, "setOrientationRelativeToVehicle", org.lgna.story.Orientation.class, 
				SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( orientation ) 
		);
	}
	public static org.lgna.project.ast.Statement createPositionStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.story.Position position ) throws org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException {
		return createStatement( 
				org.lgna.story.MovableTurnable.class, "setPositionRelativeToVehicle", org.lgna.story.Position.class, 
				SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( position ) 
		);
	}
	
	public static org.lgna.project.ast.Statement createSetterStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractMethod setter, org.lgna.project.ast.Expression expression ) {
		return org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( 
				SetUpMethodGenerator.createInstanceExpression( isThis, field ),
				setter, 
				expression 
		);
	}
	
	public static org.lgna.project.ast.Statement[] getSetupStatementsForField(boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.project.virtualmachine.UserInstance sceneInstance, org.lgna.project.ast.AbstractField initialVehicle, AffineMatrix4x4 initialTransform)
	{
		java.util.List< org.lgna.project.ast.Statement > statements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		org.lgna.project.ast.AbstractType<?,?,?> abstractType = field.getValueType();
		org.lgna.project.ast.JavaType javaType = abstractType.getFirstTypeEncounteredDeclaredInJava();
		if (javaType.isAssignableTo(org.lgna.story.MutableRider.class))
		{
			statements.add(createSetVehicleStatement(field, initialVehicle, (initialVehicle == null) ));
		}
		if (javaType.isAssignableTo(org.lgna.story.Turnable.class))
		{
			try {
				statements.add(createOrientationStatement(isThis, field, ImplementationAccessor.createOrientation(initialTransform.orientation)));
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
				throw new RuntimeException( ccee );
			}
		}
		if (javaType.isAssignableTo(org.lgna.story.MovableTurnable.class))
		{
			try {
				statements.add(createPositionStatement(isThis, field, ImplementationAccessor.createPosition(initialTransform.translation)));
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
				throw new RuntimeException( ccee );
			}
		}
		
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( statements, org.lgna.project.ast.Statement.class );
	}
	
	public static org.lgna.project.ast.Statement[] getSetupStatementsForInstance(boolean isThis, Object instance, org.lgna.project.virtualmachine.UserInstance sceneInstance)
	{
		java.util.List< org.lgna.project.ast.Statement > statements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		if (instance != null)
		{
			org.lgna.project.ast.AbstractField field = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava(instance);
			if( field != null ) {
				org.lgna.project.ast.AbstractType<?,?,?> abstractType = field.getValueType();
				org.lgna.project.ast.JavaType javaType = abstractType.getFirstTypeEncounteredDeclaredInJava();
				
				for( org.lgna.project.ast.JavaMethod getter : org.lgna.project.ast.AstUtilities.getPersistentPropertyGetters( javaType ) ) {
					java.lang.reflect.Method gttr = getter.getMethodReflectionProxy().getReification();
					Object value = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( instance, gttr );
					org.lgna.project.ast.JavaMethod setter = org.lgna.project.ast.AstUtilities.getSetterForGetter( getter, javaType );
					if( setter != null ) {
						try {
							org.lgna.project.ast.Expression expression;
							if( value instanceof org.lgna.story.Entity ) {
								org.lgna.story.Entity entity = (org.lgna.story.Entity)value;
								boolean isEntityScene = (entity instanceof org.lgna.story.Scene);
								org.lgna.project.ast.AbstractField entityField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava(entity);
								expression = SetUpMethodGenerator.createInstanceExpression( isEntityScene, entityField );
							} else {
								expression = getExpressionCreator().createExpression( value );
							}
							statements.add( 
									org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( 
											SetUpMethodGenerator.createInstanceExpression( isThis, field ), 
											setter, 
											expression 
									)
							);
						} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
							System.err.println( "cannot create expression for: " + value );
						}
					} else {
						System.err.println( "setter is null for: " + getter );
					}
				}
				if( instance instanceof org.lgna.story.Turnable ) {
					org.lgna.story.Turnable turnable = (org.lgna.story.Turnable)instance;
					org.lgna.story.Orientation orientation = turnable.getOrientationRelativeToVehicle();
					try {
						statements.add( 
								createOrientationStatement( isThis, field, orientation )
						);
					} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
						throw new RuntimeException( ccee );
					}
					if( turnable instanceof org.lgna.story.MovableTurnable ) {
						org.lgna.story.MovableTurnable movableTurnable = (org.lgna.story.MovableTurnable)turnable;
						org.lgna.story.Position position = movableTurnable.getPositionRelativeToVehicle();
						try {
							statements.add( 
									createPositionStatement( isThis, field, position )
							);
						} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
							throw new RuntimeException( ccee );
						}
					}
				}
				if( instance instanceof org.lgna.story.Resizable ) {
					org.lgna.story.Resizable resizable = (org.lgna.story.Resizable)instance;
					org.lgna.story.Scale scale = resizable.getScale();
					try {
						statements.add( 
								createStatement( 
										org.lgna.story.Resizable.class, "setScale", new Class< ? >[] { org.lgna.story.Scale.class, org.lgna.story.SetScale.Detail[].class }, 
										SetUpMethodGenerator.createInstanceExpression( isThis, field ), 
										getExpressionCreator().createExpression( scale ) 
								)
						);
					} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
						throw new RuntimeException( ccee );
					}
				}
			} else {
				if( instance instanceof org.lgna.story.Scene ) {
					org.lgna.story.Scene scene = (org.lgna.story.Scene)instance;
					System.err.println( "todo: handle scene: " + scene );
				} else {
					System.err.println( "todo: handle unknown: " + instance );
				}
			}
		}
		
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( statements, org.lgna.project.ast.Statement.class );
	}
	
	public static void fillInAutomaticSetUpMethod( org.lgna.project.ast.StatementListProperty bodyStatementsProperty, boolean isThis, org.lgna.project.ast.AbstractField field, Object instance, org.lgna.project.virtualmachine.UserInstance sceneInstance ) {
		if( instance != null ) {
			if( instance instanceof org.lgna.story.Entity ) {
				org.lgna.story.Entity entity = (org.lgna.story.Entity)instance;
				entity.setName( field.getName() );
			}
			
			org.lgna.project.ast.Statement[] setupStatements = getSetupStatementsForInstance(isThis, instance, sceneInstance);
			bodyStatementsProperty.add(setupStatements);
		}
	}
}