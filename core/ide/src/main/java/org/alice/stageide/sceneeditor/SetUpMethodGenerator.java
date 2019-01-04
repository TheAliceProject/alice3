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
package org.alice.stageide.sceneeditor;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import org.alice.ide.ast.ExpressionCreator;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.StageIDE;
import org.alice.stageide.ast.JointMethodArrayAccessInfo;
import org.alice.stageide.ast.JointedTypeInfo;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ArrayAccess;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.DoubleLiteral;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.IntegerLiteral;
import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.Color;
import org.lgna.story.DurationAnimationStyleArgumentFactory;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.MutableRider;
import org.lgna.story.Orientation;
import org.lgna.story.Paint;
import org.lgna.story.Place;
import org.lgna.story.Position;
import org.lgna.story.Resizable;
import org.lgna.story.SBox;
import org.lgna.story.SJoint;
import org.lgna.story.SJointedModel;
import org.lgna.story.SMarker;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.STurnable;
import org.lgna.story.Scale;
import org.lgna.story.SetOrientationRelativeToVehicle;
import org.lgna.story.SetPaint;
import org.lgna.story.SetPositionRelativeToVehicle;
import org.lgna.story.SetScale;
import org.lgna.story.SetSize;
import org.lgna.story.Size;
import org.lgna.story.SpatialRelation;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class SetUpMethodGenerator {

	private static org.alice.stageide.ast.ExpressionCreator expressionCreator = null;

	protected static ExpressionCreator getExpressionCreator() {
		if( ( StageIDE.getActiveInstance() != null )
				&& ( StageIDE.getActiveInstance().getApiConfigurationManager() != null )
				&& ( StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator() != null ) ) {
			return StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator();
		} else {
			if( expressionCreator == null ) {
				expressionCreator = NebulousIde.nonfree.newExpressionCreator();
			}
			return expressionCreator;
		}
	}

	private static Expression createInstanceExpression( boolean isThis, AbstractField field ) {
		return isThis ? new ThisExpression() : new FieldAccess(field);
	}

	private static ExpressionStatement createStatement( Class<?> declarationCls, String methodName, Class<?>[] parameterClses, Expression instanceExpression,
			Expression... argumentExpressions ) {
		AbstractMethod method = AstUtilities.lookupMethod( declarationCls, methodName, parameterClses );
		return AstUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpressions );
	}

	static ExpressionStatement createSetVehicleStatement(AbstractField field, AbstractField vehicleField,
														 boolean isVehicleScene) {
		AbstractMethod setVehicleMethod = AstUtilities.lookupMethod(MutableRider.class, "setVehicle", (Class<?>) SThing.class);
		Expression vehicle;
		if( ( vehicleField != null ) || isVehicleScene ) {
			vehicle = SetUpMethodGenerator.createInstanceExpression(isVehicleScene, vehicleField);
		} else {
			vehicle = new NullLiteral();
		}
		return AstUtilities.createMethodInvocationStatement(new FieldAccess(field), setVehicleMethod, vehicle);
	}

	public static ExpressionStatement createSetPaintStatement( AbstractField field, Paint paint ) {
		if( paint != null ) {
			Expression paintExpression = null;
			try {
				paintExpression = getExpressionCreator().createExpression( paint );
			} catch( Exception e ) {
				paintExpression = null;
			}
			if( paintExpression != null ) {
				if( field.getValueType().isAssignableFrom( SModel.class ) ) {
					return createStatement( SModel.class, "setPaint", new Class<?>[] { Paint.class, SetPaint.Detail[].class }, new FieldAccess( field ), paintExpression );
				}
			}
		}
		return null;
	}

	public static ExpressionStatement createSetColorIdStatement( AbstractField field, Color colorId ) {
		if( colorId != null ) {
			Expression colorIdExpression = null;
			try {
				colorIdExpression = getExpressionCreator().createExpression( colorId );
			} catch( Exception e ) {
				colorIdExpression = null;
			}
			if( colorIdExpression != null ) {
				if( field.getValueType().isAssignableTo( SMarker.class ) ) {
					return createStatement( SMarker.class, "setColorId", new Class<?>[] { Color.class }, new FieldAccess( field ), colorIdExpression );
				}
			}
		}
		return null;
	}

	public static Statement createOrientationStatement( boolean isThis, AbstractField field, Orientation orientation ) throws ExpressionCreator.CannotCreateExpressionException {
		return createOrientationStatement( isThis, field, orientation, -1 );
	}

	public static Statement createOrientationStatement( boolean isThis, AbstractField field, Orientation orientation, double duration )
			throws ExpressionCreator.CannotCreateExpressionException {
		ExpressionStatement orientationStatement = createStatement(
				STurnable.class, "setOrientationRelativeToVehicle", new Class<?>[] { Orientation.class, SetOrientationRelativeToVehicle.Detail[].class },
				SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( orientation ) );
		if( duration != -1 ) {
			MethodInvocation methodInvocation = (MethodInvocation)orientationStatement.expression.getValue();
			JavaMethod durationKeyMethod = JavaMethod.getInstance( DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
			methodInvocation.keyedArguments.add( new JavaKeyedArgument( methodInvocation.method.getValue().getKeyedParameter(), durationKeyMethod, new DoubleLiteral( duration ) ) );
		}
		return orientationStatement;
	}

	public static Statement createPositionStatement( boolean isThis, AbstractField field, Position position ) throws ExpressionCreator.CannotCreateExpressionException {
		return createPositionStatement( isThis, field, position, -1 );
	}

	public static Statement createPositionStatement( boolean isThis, AbstractField field, Position position, double duration )
			throws ExpressionCreator.CannotCreateExpressionException {
		ExpressionStatement positionStatement = createStatement(
				SMovableTurnable.class, "setPositionRelativeToVehicle", new Class<?>[] { Position.class, SetPositionRelativeToVehicle.Detail[].class },
				SetUpMethodGenerator.createInstanceExpression( isThis, field ), getExpressionCreator().createExpression( position ) );
		if( duration != -1 ) {
			MethodInvocation methodInvocation = (MethodInvocation)positionStatement.expression.getValue();
			JavaMethod durationKeyMethod = JavaMethod.getInstance( DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
			methodInvocation.keyedArguments.add( new JavaKeyedArgument( methodInvocation.method.getValue().getKeyedParameter(), durationKeyMethod, new DoubleLiteral( duration ) ) );
		}
		return positionStatement;
	}

	public static MethodInvocation createSetterInvocation( boolean isThis, AbstractField field, AbstractMethod setter, Expression expression ) {
		return AstUtilities.createMethodInvocation(
				SetUpMethodGenerator.createInstanceExpression( isThis, field ),
				setter,
				expression );
	}

	public static ExpressionStatement createSetterStatement( boolean isThis, AbstractField field, AbstractMethod setter, Expression expression ) {
		return new ExpressionStatement( createSetterInvocation( isThis, field, setter, expression ) );
	}

	private static boolean shouldPlaceModelAboveGround( AbstractType<?, ?, ?> type ) {
		return false;
	}

	public static Statement[] getSetupStatementsForField( boolean isThis, AbstractField field, UserInstance sceneInstance, AbstractField initialVehicle,
			AffineMatrix4x4 initialTransform ) {
		List<Statement> statements = Lists.newLinkedList();

		AbstractType<?, ?, ?> abstractType = field.getValueType();
		JavaType javaType = abstractType.getFirstEncounteredJavaType();
		if( javaType.isAssignableTo( MutableRider.class ) ) {
			statements.add( createSetVehicleStatement( field, initialVehicle, ( initialVehicle == null ) ) );
		}
		if( initialTransform != null ) {
			if( javaType.isAssignableTo( STurnable.class ) ) {
				try {
					statements.add( createOrientationStatement( isThis, field, EmployeesOnly.createOrientation( initialTransform.orientation ), 0 ) );
				} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
					throw new RuntimeException( ccee );
				}
			}
			if( javaType.isAssignableTo( SMovableTurnable.class ) ) {
				try {
					statements.add( createPositionStatement( isThis, field, EmployeesOnly.createPosition( initialTransform.translation ), 0 ) );

					//todo
					if( ( initialTransform.translation.y == 0.0 ) && shouldPlaceModelAboveGround( abstractType ) ) {
						//place above ground
						Expression targetExpression = new NullLiteral();
						ExpressionStatement placeStatement = createStatement(
								SMovableTurnable.class,
								"place",
								new Class[] { SpatialRelation.class, SThing.class, Place.Detail[].class },
								SetUpMethodGenerator.createInstanceExpression( isThis, field ),
								getExpressionCreator().createExpression( SpatialRelation.ABOVE ), targetExpression );
						MethodInvocation methodInvocation = (MethodInvocation)placeStatement.expression.getValue();
						JavaMethod durationKeyMethod = JavaMethod.getInstance( DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
						methodInvocation.keyedArguments.add( new JavaKeyedArgument( methodInvocation.method.getValue().getKeyedParameter(), durationKeyMethod, new DoubleLiteral( 0.0 ) ) );
						statements.add( placeStatement );
					}

				} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
					throw new RuntimeException( ccee );
				}
			}
		}
		return ArrayUtilities.createArray( statements, Statement.class );
	}

	private static SJointedModel getJointedModelForJointImp( JointImp jointImp ) {
		if( jointImp.getVehicle() instanceof JointedModelImp<?, ?> ) {
			JointedModelImp<?, ?> parent = (JointedModelImp<?, ?>)jointImp.getVehicle();
			return parent.getAbstraction();
		} else if( jointImp.getVehicle() instanceof JointImp ) {
			return getJointedModelForJointImp( (JointImp)jointImp.getVehicle() );
		}
		return null;
	}

	public static Expression getGetterExpressionForJoint( SJoint joint, UserInstance sceneInstance ) {
		AbstractMethod getJointMethod = getJointGetterForJoint( joint, sceneInstance );
		JointImp jointImp = EmployeesOnly.getImplementation( joint );
		SJointedModel jointedModel = getJointedModelForJointImp( jointImp );
		AbstractField entityField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( jointedModel );
		assert getJointMethod != null;
		return new MethodInvocation( new FieldAccess(entityField), getJointMethod );
	}

	private static AbstractMethod getJointGetterForJoint( SJoint joint, UserInstance sceneInstance ) {
		JointImp jointImp = EmployeesOnly.getImplementation( joint );
		SJointedModel jointedModel = getJointedModelForJointImp( jointImp );
		AbstractField entityField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( jointedModel );
		AbstractMethod getJointMethod = null;
		AbstractType<?, ?, ?> fieldType = entityField.getValueType();
		List<JointedTypeInfo> jointedTypeInfos = JointedTypeInfo.getInstances( fieldType );
		//Loop through all the get<joint>() methods and find the one that resolves to the joint we're seeing
		for( JointedTypeInfo jti : jointedTypeInfos ) {
			for( AbstractMethod jointGetter : jti.getJointGetters() ) {
				Object[] values = sceneInstance.getVM().ENTRY_POINT_evaluate(
						sceneInstance,
						new Expression[] { new MethodInvocation( new FieldAccess(entityField), jointGetter ) } );
				for( Object o : values ) {
					if( o instanceof SJoint ) {
						JointImp gottenJoint = EmployeesOnly.getImplementation( (SJoint)o );
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

	public static Statement[] getSetupStatementsForInstance( boolean isThis, Object instance, UserInstance sceneInstance, boolean captureFullState ) {
		List<Statement> statements = Lists.newLinkedList();

		if( instance != null ) {
			AbstractField field = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( instance );
			if( ( field != null ) || isThis ) {
				JavaType javaType = JavaType.getInstance( instance.getClass() );
				for( JavaMethod getter : AstUtilities.getPersistentPropertyGetters( javaType ) ) {
					Method gttr = getter.getMethodReflectionProxy().getReification();
					Object value = ReflectionUtilities.invoke( instance, gttr );
					JavaMethod setter = AstUtilities.getSetterForGetter( getter, javaType );
					if( setter != null ) {
						try {
							Expression expression;
							if( value instanceof SThing ) {
								SThing entity = (SThing)value;
								boolean isEntityScene = ( entity instanceof SScene );
								if( entity instanceof SJoint ) {
									SJoint joint = (SJoint)entity;
									expression = getGetterExpressionForJoint( joint, sceneInstance );
								} else {
									AbstractField entityField = sceneInstance.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( entity );
									expression = SetUpMethodGenerator.createInstanceExpression( isEntityScene, entityField );
								}
							} else {
								expression = getExpressionCreator().createExpression( value );
							}
							statements.add(
									AstUtilities.createMethodInvocationStatement(
											SetUpMethodGenerator.createInstanceExpression( isThis, field ),
											setter,
											expression ) );
						} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
							Logger.severe( "cannot create expression for: " + value );
						}
					} else {
						Logger.warning( "setter is null for: " + getter );
					}
				}
				if( instance instanceof STurnable ) {
					STurnable turnable = (STurnable)instance;
					Orientation orientation = turnable.getOrientationRelativeToVehicle();
					try {
						statements.add(
								createOrientationStatement( isThis, field, orientation ) );
					} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
						throw new RuntimeException( ccee );
					}
					if( turnable instanceof SMovableTurnable ) {
						SMovableTurnable movableTurnable = (SMovableTurnable)turnable;
						Position position = movableTurnable.getPositionRelativeToVehicle();
						try {
							statements.add(
									createPositionStatement( isThis, field, position ) );
						} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
							throw new RuntimeException( ccee );
						}
					}
				}
				if( instance instanceof Resizable ) {
					Resizable resizable = (Resizable)instance;
					if (instance instanceof SBox) {
						try {
							statements.add(createStatement(Resizable.class, "setSize",
											new Class<?>[] { Size.class, SetSize.Detail[].class },
											createInstanceExpression(isThis, field),
											getExpressionCreator().createExpression(resizable.getSize())));
						} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
							throw new RuntimeException( ccee );
						}
					} else {
						Scale scale = resizable.getScale();
						if (!Scale.IDENTITY.equals(scale)) {
							try {
								statements.add(createStatement(Resizable.class, "setScale",
												new Class<?>[] { Scale.class, SetScale.Detail[].class },
												createInstanceExpression(isThis, field),
												getExpressionCreator().createExpression(scale)));
							} catch (ExpressionCreator.CannotCreateExpressionException ccee) {
								throw new RuntimeException(ccee);
							}
						}
					}
				}
				if( instance instanceof SJointedModel ) {
					JointedModelImp<?, ?> jointedModelImp = EmployeesOnly.getImplementation( (SJointedModel)instance );
					List<JointedTypeInfo> jointedTypeInfos = JointedTypeInfo.getInstances( field.getValueType() );
					for( JointedTypeInfo jointInfo : jointedTypeInfos ) {
						List<Expression> jointAccessExpressions = Lists.newLinkedList();
						for( AbstractMethod jointGetter : jointInfo.getJointGetters() ) {
							Expression getJointExpression = new MethodInvocation( new FieldAccess(field), jointGetter );
							jointAccessExpressions.add( getJointExpression );
						}
						for( JointMethodArrayAccessInfo jointArrayGetter : jointInfo.getJointArrayAccessGetters() ) {
							Expression getJointExpression = new MethodInvocation( new FieldAccess(field), jointArrayGetter.getMethod() );
							Expression arrayAccessExpression = new ArrayAccess( jointArrayGetter.getMethod().getReturnType(), getJointExpression, new IntegerLiteral( jointArrayGetter.getIndex() ) );
							jointAccessExpressions.add( arrayAccessExpression );
						}
						for( Expression getJointExpression : jointAccessExpressions ) {
							Object[] values;
							try {
								values = sceneInstance.getVM().ENTRY_POINT_evaluate(
										sceneInstance,
										new Expression[] { getJointExpression } );
							} catch( Throwable t ) {
								Logger.errln( "set up method generator failed:", getJointExpression );
								values = new Object[ 0 ];
							}
							for( Object o : values ) {
								if( o instanceof SJoint ) {
									SJoint jointEntity = (SJoint)o;
									JointImp gottenJoint = EmployeesOnly.getImplementation( jointEntity );
									AffineMatrix4x4 currentTransform = gottenJoint.getLocalTransformation();
									AffineMatrix4x4 originalTransform = gottenJoint.getOriginalTransformation();
									if( captureFullState || !currentTransform.orientation.isWithinReasonableEpsilonOf( originalTransform.orientation ) ) {
										try {
											Orientation orientation = jointEntity.getOrientationRelativeToVehicle();
											ExpressionStatement orientationStatement = createStatement(
													STurnable.class, "setOrientationRelativeToVehicle", new Class<?>[] { Orientation.class, SetOrientationRelativeToVehicle.Detail[].class },
													getJointExpression, getExpressionCreator().createExpression( orientation ) );
											statements.add( orientationStatement );
										} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
											throw new RuntimeException( ccee );
										}
									}
									if( captureFullState || !currentTransform.translation.isWithinReasonableEpsilonOf( originalTransform.translation ) ) {
										try {
											Position position = jointEntity.getPositionRelativeToVehicle();
											ExpressionStatement positionStatement = createStatement(
													SMovableTurnable.class, "setPositionRelativeToVehicle", new Class<?>[] { Position.class, SetPositionRelativeToVehicle.Detail[].class },
													getJointExpression, getExpressionCreator().createExpression( position ) );
											statements.add( positionStatement );
										} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
											throw new RuntimeException( ccee );
										}
									}

								}
							}
						}
					}
				}

				if( instance instanceof SMarker ) {
					SMarker marker = (SMarker)instance;
					Statement colorIdStatement = createSetColorIdStatement( field, marker.getColorId() );
					if( colorIdStatement != null ) {
						statements.add( colorIdStatement );
					}
				}
			}
		}

		return ArrayUtilities.createArray( statements, Statement.class );
	}

	public static void fillInAutomaticSetUpMethod( StatementListProperty bodyStatementsProperty, boolean isThis, AbstractField field, Object instance, UserInstance sceneInstance,
			boolean getFullState ) {
		if( instance != null ) {
			if( instance instanceof SThing ) {
				SThing entity = (SThing)instance;
				entity.setName( field.getName() );
			}

			Statement[] setupStatements = getSetupStatementsForInstance( isThis, instance, sceneInstance, getFullState );
			bodyStatementsProperty.add( setupStatements );
		}
	}
}
