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

	private static org.alice.stageide.ast.ExpressionCreator expressionCreator = null;

	private static org.alice.ide.ast.ExpressionCreator getExpressionCreator() {
		if( ( org.alice.stageide.StageIDE.getActiveInstance() != null ) && ( org.alice.stageide.StageIDE.getActiveInstance().getApiConfigurationManager() != null ) && ( org.alice.stageide.StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator() != null ) ) {
			return org.alice.stageide.StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator();
		} else {
			if( expressionCreator == null ) {
				expressionCreator = new org.alice.stageide.ast.ExpressionCreator();
			}
			return expressionCreator;
		}
	}

	private static org.lgna.project.ast.Expression createInstanceExpression( boolean isThis, org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.Expression thisExpression = new org.lgna.project.ast.ThisExpression();
		if( isThis ) {
			return thisExpression;
		} else {
			return new org.lgna.project.ast.FieldAccess( thisExpression, field );
		}
	}

	private static org.lgna.project.ast.ExpressionStatement createStatement( Class<?> declarationCls, String methodName, Class<?> parameterCls, org.lgna.project.ast.Expression instanceExpression, org.lgna.project.ast.Expression argumentExpression ) {
		org.lgna.project.ast.AbstractMethod method = org.lgna.project.ast.AstUtilities.lookupMethod( declarationCls, methodName, parameterCls );
		return org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpression );
	}

	private static org.lgna.project.ast.ExpressionStatement createStatement( Class<?> declarationCls, String methodName, Class<?>[] parameterClses, org.lgna.project.ast.Expression instanceExpression, org.lgna.project.ast.Expression... argumentExpressions ) {
		org.lgna.project.ast.AbstractMethod method = org.lgna.project.ast.AstUtilities.lookupMethod( declarationCls, methodName, parameterClses );
		return org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpressions );
	}

	public static org.lgna.project.ast.InstanceCreation createSims2PersonRecourseInstanceCreation( org.lgna.story.resources.sims2.PersonResource personResource ) throws org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException {
		Class<?>[] parameterClses = { org.lgna.story.resources.sims2.Gender.class, org.lgna.story.Color.class, org.lgna.story.resources.sims2.EyeColor.class, org.lgna.story.resources.sims2.Hair.class, Number.class, org.lgna.story.resources.sims2.Outfit.class, org.lgna.story.resources.sims2.Face.class,
		};
		org.lgna.project.ast.Expression[] arguments = { getExpressionCreator().createExpression( personResource.getGender() ), getExpressionCreator().createExpression( personResource.getSkinColor() ), getExpressionCreator().createExpression( personResource.getEyeColor() ), getExpressionCreator().createExpression( personResource.getHair() ), getExpressionCreator().createExpression( personResource.getObesityLevel() ), getExpressionCreator().createExpression( personResource.getOutfit() ), getExpressionCreator().createExpression( personResource.getFace() ),
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

	public static void fillInAutomaticVehicleAssignment( org.lgna.project.ast.StatementListProperty bodyStatementsProperty, org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractField vehicleField, boolean isVehicleScene ) {
		if( vehicleField != null ) {
			bodyStatementsProperty.add( createStatement( org.lgna.story.MutableRider.class, "setVehicle", org.lgna.story.SThing.class, SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( isVehicleScene, vehicleField ) ) );
		}
	}

	public static org.lgna.project.ast.ExpressionStatement createSetVehicleStatement( org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractField vehicleField, boolean isVehicleScene ) {
		if( ( vehicleField != null ) || isVehicleScene ) {
			return createStatement( org.lgna.story.MutableRider.class, "setVehicle", org.lgna.story.SThing.class, SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( isVehicleScene, vehicleField ) );
		} else {
			return createStatement( org.lgna.story.MutableRider.class, "setVehicle", org.lgna.story.SThing.class, SetUpMethodGenerator.createInstanceExpression( false, field ), new org.lgna.project.ast.NullLiteral() );
		}
	}

	public static org.lgna.project.ast.ExpressionStatement createSetPaintStatement( org.lgna.project.ast.AbstractField field, org.lgna.story.Paint paint ) {
		if( paint != null ) {
			org.lgna.project.ast.Expression paintExpression = null;
			try {
				paintExpression = getExpressionCreator().createExpression( paint );
			} catch( Exception e ) {
				paintExpression = null;
			}
			if( paintExpression != null ) {
				if( field.getValueType().isAssignableFrom( org.lgna.story.SModel.class ) ) {
					return createStatement( org.lgna.story.SModel.class, "setPaint", new Class<?>[] { org.lgna.story.Paint.class, org.lgna.story.SetPaint.Detail[].class }, SetUpMethodGenerator.createInstanceExpression( false, field ), paintExpression );
				}
			}
		}
		return null;
	}

	public static org.lgna.project.ast.ExpressionStatement createSetColorIdStatement( org.lgna.project.ast.AbstractField field, org.lgna.story.Color colorId ) {
		if( colorId != null ) {
			org.lgna.project.ast.Expression colorIdExpression = null;
			try {
				colorIdExpression = getExpressionCreator().createExpression( colorId );
			} catch( Exception e ) {
				colorIdExpression = null;
			}
			if( colorIdExpression != null ) {
				if( field.getValueType().isAssignableTo( org.lgna.story.SMarker.class ) ) {
					return createStatement( org.lgna.story.SMarker.class, "setColorId", new Class<?>[] { org.lgna.story.Color.class }, SetUpMethodGenerator.createInstanceExpression( false, field ), colorIdExpression );
				}
			}
		}
		return null;
	}

	public static org.lgna.project.ast.Statement createOrientationStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.story.Orientation orientation ) throws org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException {
		return createOrientationStatement( isThis, field, orientation, -1 );
	}

	public static org.lgna.project.ast.Statement createOrientationStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.story.Orientation orientation, double duration ) throws org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException {
		org.lgna.project.ast.ExpressionStatement orientationStatement = createStatement( org.lgna.story.STurnable.class, "setOrientationRelativeToVehicle", new Class<?>[] { org.lgna.story.Orientation.class, org.lgna.story.SetOrientationRelativeToVehicle.Detail[].class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( orientation ) );
		if( duration != -1 ) {
			org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)orientationStatement.expression.getValue();
			org.lgna.project.ast.JavaMethod durationKeyMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
			methodInvocation.keyedArguments.add( new org.lgna.project.ast.JavaKeyedArgument( methodInvocation.method.getValue().getKeyedParameter(), durationKeyMethod, new org.lgna.project.ast.DoubleLiteral( duration ) ) );
		}
		return orientationStatement;
	}

	public static org.lgna.project.ast.Statement createPositionStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.story.Position position ) throws org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException {
		return createPositionStatement( isThis, field, position, -1 );
	}

	public static org.lgna.project.ast.Statement createPositionStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.story.Position position, double duration ) throws org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException {
		org.lgna.project.ast.ExpressionStatement positionStatement = createStatement( org.lgna.story.SMovableTurnable.class, "setPositionRelativeToVehicle", new Class<?>[] { org.lgna.story.Position.class, org.lgna.story.SetPositionRelativeToVehicle.Detail[].class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( position ) );
		if( duration != -1 ) {
			org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)positionStatement.expression.getValue();
			org.lgna.project.ast.JavaMethod durationKeyMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
			methodInvocation.keyedArguments.add( new org.lgna.project.ast.JavaKeyedArgument( methodInvocation.method.getValue().getKeyedParameter(), durationKeyMethod, new org.lgna.project.ast.DoubleLiteral( duration ) ) );
		}
		return positionStatement;
	}

	public static org.lgna.project.ast.MethodInvocation createSetterInvocation( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractMethod setter, org.lgna.project.ast.Expression expression ) {
		return org.lgna.project.ast.AstUtilities.createMethodInvocation( SetUpMethodGenerator.createInstanceExpression( isThis, field ), setter, expression );
	}

	public static org.lgna.project.ast.ExpressionStatement createSetterStatement( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.project.ast.AbstractMethod setter, org.lgna.project.ast.Expression expression ) {
		return new org.lgna.project.ast.ExpressionStatement( createSetterInvocation( isThis, field, setter, expression ) );
	}

	private static boolean shouldPlaceModelAboveGround( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return false;
	}

	public static org.lgna.project.ast.Statement[] getSetupStatementsForField( boolean isThis, org.lgna.project.ast.AbstractField field, org.lgna.project.virtualmachine.UserInstance sceneInstance, org.lgna.project.ast.AbstractField initialVehicle, edu.cmu.cs.dennisc.math.AffineMatrix4x4 initialTransform ) {
		java.util.List<org.lgna.project.ast.Statement> statements = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		org.lgna.project.ast.AbstractType<?, ?, ?> abstractType = field.getValueType();
		org.lgna.project.ast.JavaType javaType = abstractType.getFirstEncounteredJavaType();
		if( javaType.isAssignableTo( org.lgna.story.MutableRider.class ) ) {
			statements.add( createSetVehicleStatement( field, initialVehicle, ( initialVehicle == null ) ) );
		}
		if( initialTransform != null ) {
			if( javaType.isAssignableTo( org.lgna.story.STurnable.class ) ) {
				try {
					statements.add( createOrientationStatement( isThis, field, org.lgna.story.EmployeesOnly.createOrientation( initialTransform.orientation ), 0 ) );
				} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
					throw new RuntimeException( ccee );
				}
			}
			if( javaType.isAssignableTo( org.lgna.story.SMovableTurnable.class ) ) {
				try {
					statements.add( createPositionStatement( isThis, field, org.lgna.story.EmployeesOnly.createPosition( initialTransform.translation ), 0 ) );

					//todo
					if( ( initialTransform.translation.y == 0.0 ) && shouldPlaceModelAboveGround( abstractType ) ) {
						//place above ground
						org.lgna.project.ast.Expression targetExpression = new org.lgna.project.ast.NullLiteral();
						org.lgna.project.ast.ExpressionStatement placeStatement = createStatement( org.lgna.story.SMovableTurnable.class, "place", new Class[] { org.lgna.story.SpatialRelation.class, org.lgna.story.SThing.class, org.lgna.story.Place.Detail[].class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( org.lgna.story.SpatialRelation.ABOVE ), targetExpression );
						org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)placeStatement.expression.getValue();
						org.lgna.project.ast.JavaMethod durationKeyMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
						methodInvocation.keyedArguments.add( new org.lgna.project.ast.JavaKeyedArgument( methodInvocation.method.getValue().getKeyedParameter(), durationKeyMethod, new org.lgna.project.ast.DoubleLiteral( 0.0 ) ) );
						statements.add( placeStatement );
					}

				} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
					throw new RuntimeException( ccee );
				}
			}
		}
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( statements, org.lgna.project.ast.Statement.class );
	}

	private static org.lgna.story.SJointedModel getJointedModelForJointImp( org.lgna.story.implementation.JointImp jointImp ) {
		if( jointImp.getVehicle() instanceof org.lgna.story.implementation.JointedModelImp<?, ?> ) {
			org.lgna.story.implementation.JointedModelImp<?, ?> parent = (org.lgna.story.implementation.JointedModelImp<?, ?>)jointImp.getVehicle();
			return parent.getAbstraction();
		} else if( jointImp.getVehicle() instanceof org.lgna.story.implementation.JointImp ) {
			return getJointedModelForJointImp( (org.lgna.story.implementation.JointImp)jointImp.getVehicle() );
		}
		return null;
	}

	public static org.lgna.project.ast.Expression getGetterExpressionForJoint( org.lgna.story.SJoint joint, org.lgna.project.virtualmachine.UserInstance sceneInstance ) {
		org.lgna.project.ast.AbstractMethod getJointMethod = getJointGetterForJoint( joint, sceneInstance );
		org.lgna.story.implementation.JointImp jointImp = org.lgna.story.EmployeesOnly.getImplementation( joint );
		org.lgna.story.SJointedModel jointedModel = getJointedModelForJointImp( jointImp );
		org.lgna.project.ast.AbstractField entityField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( jointedModel );
		assert getJointMethod != null;
		org.lgna.project.ast.Expression expression = new org.lgna.project.ast.MethodInvocation( new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), entityField ), getJointMethod );
		return expression;
	}

	private static org.lgna.project.ast.AbstractMethod getJointGetterForJoint( org.lgna.story.SJoint joint, org.lgna.project.virtualmachine.UserInstance sceneInstance ) {
		org.lgna.story.implementation.JointImp jointImp = org.lgna.story.EmployeesOnly.getImplementation( joint );
		org.lgna.story.SJointedModel jointedModel = getJointedModelForJointImp( jointImp );
		org.lgna.project.ast.AbstractField entityField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( jointedModel );
		org.lgna.project.ast.AbstractMethod getJointMethod = null;
		org.lgna.project.ast.AbstractType<?, ?, ?> fieldType = entityField.getValueType();
		java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos = org.alice.stageide.ast.JointedTypeInfo.getInstances( fieldType );
		//Loop through all the get<joint>() methods and find the one that resolves to the joint we're seeing
		for( org.alice.stageide.ast.JointedTypeInfo jti : jointedTypeInfos ) {
			for( org.lgna.project.ast.AbstractMethod jointGetter : jti.getJointGetters() ) {
				Object[] values = sceneInstance.getVM().ENTRY_POINT_evaluate( sceneInstance, new org.lgna.project.ast.Expression[] { new org.lgna.project.ast.MethodInvocation( new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), entityField ), jointGetter ) } );
				for( Object o : values ) {
					if( o instanceof org.lgna.story.SJoint ) {
						org.lgna.story.implementation.JointImp gottenJoint = org.lgna.story.EmployeesOnly.getImplementation( (org.lgna.story.SJoint)o );
						if( gottenJoint.getJointId() == jointImp.getJointId() ) {
							getJointMethod = jointGetter;
							break;
						}
					}
				}
			}
			if( getJointMethod != null ) {
				break;
			}
		}
		assert getJointMethod != null;
		return getJointMethod;
	}

	public static org.lgna.project.ast.Statement[] getSetupStatementsForInstance( boolean isThis, Object instance, org.lgna.project.virtualmachine.UserInstance sceneInstance, boolean captureFullState ) {
		java.util.List<org.lgna.project.ast.Statement> statements = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		if( instance != null ) {
			org.lgna.project.ast.AbstractField field = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( instance );
			if( ( field != null ) || isThis ) {
				org.lgna.project.ast.JavaType javaType = org.lgna.project.ast.JavaType.getInstance( instance.getClass() );
				for( org.lgna.project.ast.JavaMethod getter : org.lgna.project.ast.AstUtilities.getPersistentPropertyGetters( javaType ) ) {
					java.lang.reflect.Method gttr = getter.getMethodReflectionProxy().getReification();
					Object value = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( instance, gttr );
					org.lgna.project.ast.JavaMethod setter = org.lgna.project.ast.AstUtilities.getSetterForGetter( getter, javaType );
					if( setter != null ) {
						try {
							org.lgna.project.ast.Expression expression;
							if( value instanceof org.lgna.story.SThing ) {
								org.lgna.story.SThing entity = (org.lgna.story.SThing)value;
								boolean isEntityScene = ( entity instanceof org.lgna.story.SScene );
								if( entity instanceof org.lgna.story.SJoint ) {
									org.lgna.story.SJoint joint = (org.lgna.story.SJoint)entity;
									expression = getGetterExpressionForJoint( joint, sceneInstance );
								} else {
									org.lgna.project.ast.AbstractField entityField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( entity );
									expression = SetUpMethodGenerator.createInstanceExpression( isEntityScene, entityField );
								}
							} else {
								expression = getExpressionCreator().createExpression( value );
							}
							statements.add( org.lgna.project.ast.AstUtilities.createMethodInvocationStatement( SetUpMethodGenerator.createInstanceExpression( isThis, field ), setter, expression ) );
						} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
							edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "cannot create expression for: " + value );
						}
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "setter is null for: " + getter );
					}
				}
				if( instance instanceof org.lgna.story.STurnable ) {
					org.lgna.story.STurnable turnable = (org.lgna.story.STurnable)instance;
					org.lgna.story.Orientation orientation = turnable.getOrientationRelativeToVehicle();
					try {
						statements.add( createOrientationStatement( isThis, field, orientation ) );
					} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
						throw new RuntimeException( ccee );
					}
					if( turnable instanceof org.lgna.story.SMovableTurnable ) {
						org.lgna.story.SMovableTurnable movableTurnable = (org.lgna.story.SMovableTurnable)turnable;
						org.lgna.story.Position position = movableTurnable.getPositionRelativeToVehicle();
						try {
							statements.add( createPositionStatement( isThis, field, position ) );
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
										org.lgna.story.Resizable.class, "setScale", new Class<?>[] { org.lgna.story.Scale.class, org.lgna.story.SetScale.Detail[].class },
										SetUpMethodGenerator.createInstanceExpression( isThis, field ),
										getExpressionCreator().createExpression( scale ) ) );
					} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
						throw new RuntimeException( ccee );
					}
				}
				if( instance instanceof org.lgna.story.SJointedModel ) {
					org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImp = org.lgna.story.EmployeesOnly.getImplementation( (org.lgna.story.SJointedModel)instance );
					java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos = org.alice.stageide.ast.JointedTypeInfo.getInstances( field.getValueType() );
					for( org.alice.stageide.ast.JointedTypeInfo jointInfo : jointedTypeInfos ) {
						java.util.List<org.lgna.project.ast.Expression> jointAccessExpressions = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
						for( org.lgna.project.ast.AbstractMethod jointGetter : jointInfo.getJointGetters() ) {
							org.lgna.project.ast.Expression getJointExpression = new org.lgna.project.ast.MethodInvocation( new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field ), jointGetter );
							jointAccessExpressions.add( getJointExpression );
						}
						for( org.alice.stageide.ast.JointMethodArrayAccessInfo jointArrayGetter : jointInfo.getJointArrayAccessGetters() ) {
							org.lgna.project.ast.Expression getJointExpression = new org.lgna.project.ast.MethodInvocation( new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field ), jointArrayGetter.getMethod() );
							org.lgna.project.ast.Expression arrayAccessExpression = new org.lgna.project.ast.ArrayAccess( jointArrayGetter.getMethod().getReturnType(), getJointExpression, new org.lgna.project.ast.IntegerLiteral( jointArrayGetter.getIndex() ) );
							jointAccessExpressions.add( arrayAccessExpression );
						}
						for( org.lgna.project.ast.Expression getJointExpression : jointAccessExpressions ) {
							Object[] values;
							try {
								values = sceneInstance.getVM().ENTRY_POINT_evaluate( sceneInstance, new org.lgna.project.ast.Expression[] { getJointExpression } );
							} catch( Throwable t ) {
								edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "set up method generator failed:", getJointExpression );
								values = new Object[ 0 ];
							}
							for( Object o : values ) {
								if( o instanceof org.lgna.story.SJoint ) {
									org.lgna.story.SJoint jointEntity = (org.lgna.story.SJoint)o;
									org.lgna.story.implementation.JointImp gottenJoint = org.lgna.story.EmployeesOnly.getImplementation( jointEntity );
									edu.cmu.cs.dennisc.math.AffineMatrix4x4 currentTransform = gottenJoint.getLocalTransformation();
									edu.cmu.cs.dennisc.math.AffineMatrix4x4 originalTransform = gottenJoint.getOriginalTransformation();
									if( captureFullState || !currentTransform.orientation.isWithinReasonableEpsilonOf( originalTransform.orientation ) ) {
										try {
											org.lgna.story.Orientation orientation = jointEntity.getOrientationRelativeToVehicle();
											org.lgna.project.ast.ExpressionStatement orientationStatement = createStatement( org.lgna.story.STurnable.class, "setOrientationRelativeToVehicle", new Class<?>[] { org.lgna.story.Orientation.class, org.lgna.story.SetOrientationRelativeToVehicle.Detail[].class }, getJointExpression, getExpressionCreator().createExpression( orientation ) );
											statements.add( orientationStatement );
										} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
											throw new RuntimeException( ccee );
										}
									}
									if( captureFullState || !currentTransform.translation.isWithinReasonableEpsilonOf( originalTransform.translation ) ) {
										try {
											org.lgna.story.Position position = jointEntity.getPositionRelativeToVehicle();
											org.lgna.project.ast.ExpressionStatement positionStatement = createStatement(
													org.lgna.story.SMovableTurnable.class, "setPositionRelativeToVehicle", new Class<?>[] { org.lgna.story.Position.class, org.lgna.story.SetPositionRelativeToVehicle.Detail[].class },
													getJointExpression, getExpressionCreator().createExpression( position ) );
											statements.add( positionStatement );
										} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
											throw new RuntimeException( ccee );
										}
									}

								}
							}
						}
					}
				}

				if( instance instanceof org.lgna.story.SMarker ) {
					org.lgna.story.SMarker marker = (org.lgna.story.SMarker)instance;
					org.lgna.project.ast.Statement colorIdStatement = createSetColorIdStatement( field, marker.getColorId() );
					if( colorIdStatement != null ) {
						statements.add( colorIdStatement );
					}
				}
			}
		}

		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( statements, org.lgna.project.ast.Statement.class );
	}

	public static void fillInAutomaticSetUpMethod( org.lgna.project.ast.StatementListProperty bodyStatementsProperty, boolean isThis, org.lgna.project.ast.AbstractField field, Object instance, org.lgna.project.virtualmachine.UserInstance sceneInstance, boolean getFullState ) {
		if( instance != null ) {
			if( instance instanceof org.lgna.story.SThing ) {
				org.lgna.story.SThing entity = (org.lgna.story.SThing)instance;
				entity.setName( field.getName() );
			}

			org.lgna.project.ast.Statement[] setupStatements = getSetupStatementsForInstance( isThis, instance, sceneInstance, getFullState );
			bodyStatementsProperty.add( setupStatements );
		}
	}
}
