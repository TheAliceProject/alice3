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

package org.alice.stageide;

import org.alice.nonfree.NebulousIde;

/**
 * @author Dennis Cosgrove
 */
public class StoryApiConfigurationManager extends org.alice.ide.ApiConfigurationManager {
	public static final org.lgna.project.ast.JavaMethod SET_ACTIVE_SCENE_METHOD = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SProgram.class, "setActiveScene", org.lgna.story.SScene.class );

	private static class SingletonHolder {
		private static StoryApiConfigurationManager instance = NebulousIde.nonfree.newStoryApiConfigurationManager();
	}

	public static StoryApiConfigurationManager getInstance() {
		return SingletonHolder.instance;
	}

	private final org.alice.stageide.ast.ExpressionCreator expressionCreator = NebulousIde.nonfree.newExpressionCreator();
	private final java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> categoryProcedureSubComposites;
	private final java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> categoryFunctionSubComposites;
	private final java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> categoryOrAlphabeticalProcedureSubComposites;
	private final java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> categoryOrAlphabeticalFunctionSubComposites;

	private static java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> createUnmodifiableSubCompositeList( org.alice.ide.member.FilteredJavaMethodsSubComposite... subComposites ) {
		return java.util.Collections.unmodifiableList( edu.cmu.cs.dennisc.java.util.Lists.newLinkedList( subComposites ) );
	}

	public StoryApiConfigurationManager() {
		org.alice.ide.common.BeveledShapeForType.addRoundType( org.lgna.story.SThing.class );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SScene.class, org.alice.stageide.icons.SceneIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SCylinder.class, org.alice.stageide.icons.CylinderIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SCone.class, org.alice.stageide.icons.ConeIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SDisc.class, org.alice.stageide.icons.DiscIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SSphere.class, org.alice.stageide.icons.SphereIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.STorus.class, org.alice.stageide.icons.TorusIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SAxes.class, org.alice.stageide.icons.AxesIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.STextModel.class, org.alice.stageide.icons.TextModelIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SBillboard.class, org.alice.stageide.icons.BillboardIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SBox.class, org.alice.stageide.icons.BoxIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SGround.class, org.alice.stageide.icons.GroundIconFactory.getInstance() );

		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SJoint.class, org.alice.stageide.icons.JointIconFactory.getInstance() );
		org.alice.stageide.icons.IconFactoryManager.registerIconFactory( org.lgna.story.SCamera.class, new org.lgna.croquet.icon.ImageIconFactory( org.alice.ide.icons.Icons.class.getResource( "images/160x120/Camera.png" ) ) );

		this.categoryProcedureSubComposites = createUnmodifiableSubCompositeList(
				org.alice.stageide.member.TextProceduresComposite.getInstance(),
				org.alice.stageide.member.AtmosphereProceduresComposite.getInstance(),
				org.alice.stageide.member.SayThinkProceduresComposite.getInstance(),
				org.alice.stageide.member.PositionProceduresComposite.getInstance(),
				org.alice.stageide.member.OrientationProceduresComposite.getInstance(),
				org.alice.stageide.member.PositionAndOrientationProceduresComposite.getInstance(),
				org.alice.stageide.member.SizeProceduresComposite.getInstance(),
				org.alice.stageide.member.AppearanceProceduresComposite.getInstance(),
				org.alice.stageide.member.VehicleProceduresComposite.getInstance(),
				org.alice.stageide.member.AudioProceduresComposite.getInstance(),
				org.alice.stageide.member.TimingProceduresComposite.getInstance() );

		this.categoryFunctionSubComposites = createUnmodifiableSubCompositeList(
				org.alice.stageide.member.AtmosphereFunctionsComposite.getInstance(),
				org.alice.stageide.member.AppearanceFunctionsComposite.getInstance(),
				org.alice.stageide.member.SizeFunctionsComposite.getInstance(),
				org.alice.stageide.member.PromptUserFunctionsComposite.getInstance() );

		this.categoryOrAlphabeticalProcedureSubComposites = createUnmodifiableSubCompositeList(
				org.alice.stageide.member.AddListenerProceduresComposite.getInstance() );

		this.categoryOrAlphabeticalFunctionSubComposites = createUnmodifiableSubCompositeList(
				org.alice.stageide.member.JointFunctionsComposite.getInstance() );
	}

	private static enum TypeComparator implements java.util.Comparator<org.lgna.project.ast.AbstractType<?, ?, ?>> {
		SINGLETON;
		private static final double DEFAULT_VALUE = 50.0;
		private final java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, Double> mapTypeToValue = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

		TypeComparator() {
			mapTypeToValue.put( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE, 1.1 );
			mapTypeToValue.put( org.lgna.project.ast.JavaType.DOUBLE_OBJECT_TYPE, 1.2 );
			mapTypeToValue.put( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE, 1.3 );
			mapTypeToValue.put( org.lgna.project.ast.JavaType.STRING_TYPE, 1.4 );

			mapTypeToValue.put( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThing.class ), 10.1 );

			mapTypeToValue.put( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Color.class ), 20.1 );
			mapTypeToValue.put( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Paint.class ), 20.2 );

			mapTypeToValue.put( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Position.class ), 30.1 );
			mapTypeToValue.put( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Orientation.class ), 30.2 );
			mapTypeToValue.put( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.VantagePoint.class ), 30.3 );

			mapTypeToValue.put( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJoint.class ), 99.9 );
		}

		private double getValue( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
			Double value = mapTypeToValue.get( type );
			if( value != null ) {
				return value;
			} else {
				return DEFAULT_VALUE;
			}
		}

		@Override
		public int compare( org.lgna.project.ast.AbstractType<?, ?, ?> typeA, org.lgna.project.ast.AbstractType<?, ?, ?> typeB ) {
			double valueA = getValue( typeA );
			double valueB = getValue( typeB );
			if( valueA == valueB ) {
				return typeA.getName().compareTo( typeB.getName() );
			} else {
				return Double.compare( valueA, valueB );
			}
		}
	};

	@Override
	public java.util.Comparator<org.lgna.project.ast.AbstractType<?, ?, ?>> getTypeComparator() {
		return TypeComparator.SINGLETON;
	}

	@Override
	protected boolean isNamedUserTypesAcceptableForGallery( org.lgna.project.ast.NamedUserType type ) {
		return type.isAssignableTo( org.lgna.story.SModel.class );
	}

	@Override
	protected boolean isNamedUserTypesAcceptableForSelection( org.lgna.project.ast.NamedUserType type ) {
		return ( type.isAssignableTo( org.lgna.story.SProgram.class ) == false ) || org.alice.ide.croquet.models.ui.preferences.IsIncludingProgramType.getInstance().getValue();
	}

	@Override
	public java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getCategoryProcedureSubComposites() {
		return this.categoryProcedureSubComposites;
	}

	@Override
	public java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getCategoryFunctionSubComposites() {
		return this.categoryFunctionSubComposites;
	}

	@Override
	public java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getCategoryOrAlphabeticalProcedureSubComposites() {
		return this.categoryOrAlphabeticalProcedureSubComposites;
	}

	@Override
	public java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getCategoryOrAlphabeticalFunctionSubComposites() {
		return this.categoryOrAlphabeticalFunctionSubComposites;
	}

	@Override
	public boolean isDeclaringTypeForManagedFields( org.lgna.project.ast.UserType<?> type ) {
		return type.isAssignableTo( org.lgna.story.SScene.class );
	}

	@Override
	public boolean isInstanceFactoryDesiredForType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( type.isAssignableTo( org.lgna.story.SThing.class ) ) {
			if( type.isAssignableTo( org.lgna.story.SMarker.class ) ) {
				return false;
			} else {
				return true;
			}
		} else if( type.isAssignableTo( org.lgna.story.SProgram.class ) ) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public java.util.List<org.lgna.project.ast.JavaType> getTopLevelGalleryTypes() {
		return org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils.INSTANCE.getTopLevelGalleryTypes();
	}

	protected static final org.lgna.project.ast.JavaType BIPED_RESOURCE_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.resources.BipedResource.class );

	@Override
	public org.lgna.project.ast.JavaType getGalleryResourceParentFor( org.lgna.project.ast.JavaType type ) {
		return org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils.INSTANCE.getGalleryResourceParentFor( type );
	}

	@Override
	public java.util.List<org.lgna.project.ast.AbstractDeclaration> getGalleryResourceChildrenFor( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		java.util.List<org.lgna.project.ast.AbstractDeclaration> rv = org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils.INSTANCE.getGalleryResourceChildrenFor( type );
		return rv;
	}

	@Override
	public org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForThis( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.ThisJointedTypeMenuModel.getInstance( type );
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForThisFieldAccess( org.lgna.project.ast.UserField field ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = field.getValueType();
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.ThisFieldAccessJointedTypeMenuModel.getInstance( field );
		} else {
			return null;
		}
		//		return org.alice.stageide.instancefactory.croquet.joint.declaration.ThisFieldAccessJointedTypeMenuModel.getMenuModel( field );
	}

	@Override
	public org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForParameterAccess( org.lgna.project.ast.UserParameter parameter ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = parameter.getValueType();
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.ParameterAccessJointedTypeMenuModel.getInstance( parameter );
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForLocalAccess( org.lgna.project.ast.UserLocal local ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = local.getValueType();
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.LocalAccessJointedTypeMenuModel.getInstance( local );
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> getInstanceFactorySubMenuForParameterAccessMethodInvocation( org.lgna.project.ast.UserParameter parameter, org.lgna.project.ast.AbstractMethod method ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = method.getReturnType();
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.ParameterAccessMethodInvocationJointedTypeMenuModel.getInstance( parameter, method );
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.project.ast.AbstractConstructor getGalleryResourceConstructorFor( org.lgna.project.ast.AbstractType<?, ?, ?> argumentType ) {
		java.util.List<org.lgna.project.ast.NamedUserType> types = org.alice.ide.typemanager.TypeManager.getNamedUserTypesFromSuperTypes( getTopLevelGalleryTypes() );
		for( org.lgna.project.ast.AbstractType<?, ?, ?> type : types ) {
			org.lgna.project.ast.AbstractConstructor constructor = type.getDeclaredConstructors().get( 0 );
			java.util.List<? extends org.lgna.project.ast.AbstractParameter> parameters = constructor.getRequiredParameters();
			if( parameters.size() == 1 ) {
				if( parameters.get( 0 ).getValueType().isAssignableFrom( argumentType ) ) {
					return constructor;
				}
			}
		}
		return null;
	}

	protected org.alice.ide.ast.components.DeclarationNameLabel createDeclarationNameLabel( org.lgna.project.ast.AbstractField field ) {
		//todo: better name
		class ThisFieldAccessNameLabel extends org.alice.ide.ast.components.DeclarationNameLabel {
			public ThisFieldAccessNameLabel( org.lgna.project.ast.AbstractField field ) {
				super( field );
			}

			@Override
			protected String getNameText() {
				if( org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().getValue() ) {
					return "this." + super.getNameText();
				} else {
					return super.getNameText();
				}
			}
		}
		return new ThisFieldAccessNameLabel( field );
	}

	@Override
	public org.lgna.croquet.views.SwingComponentView<?> createReplacementForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess ) {
		org.lgna.project.ast.Expression fieldExpression = fieldAccess.expression.getValue();
		if( ( fieldExpression instanceof org.lgna.project.ast.ThisExpression ) || ( fieldExpression instanceof org.alice.ide.ast.CurrentThisExpression ) ) {
			org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
			org.lgna.project.ast.AbstractType<?, ?, ?> declaringType = field.getDeclaringType();
			if( ( declaringType != null ) && declaringType.isAssignableTo( org.lgna.story.SScene.class ) ) {
				if( field.getValueType().isAssignableTo( org.lgna.story.SThing.class ) ) {
					return this.createDeclarationNameLabel( field );
				}
			}
		}
		return null;
	}

	@Override
	public org.lgna.croquet.CascadeItem<?, ?> getCustomFillInFor( org.lgna.project.annotations.ValueDetails<?> valueDetails ) {
		if( valueDetails instanceof org.lgna.story.annotation.PortionDetails ) {
			return org.alice.ide.custom.PortionCustomExpressionCreatorComposite.getInstance().getValueCreator().getFillIn();
		} else if( valueDetails instanceof org.lgna.story.annotation.VolumeLevelDetails ) {
			//			return org.alice.stageide.croquet.models.custom.CustomVolumeLevelInputDialogOperation.getInstance().getFillIn();
			return org.alice.stageide.custom.VolumeLevelCustomExpressionCreatorComposite.getInstance().getValueCreator().getFillIn();
		} else {
			return null;
		}
	}

	@Override
	public org.alice.ide.ast.ExpressionCreator getExpressionCreator() {
		return this.expressionCreator;
	}

	@Override
	public boolean isSignatureLocked( org.lgna.project.ast.Code code ) {
		//todo: check to see if only referenced from Program and Program type is hidden
		return super.isSignatureLocked( code ) || org.alice.stageide.ast.BootstrapUtilties.MY_FIRST_PROCEDURE_NAME.equalsIgnoreCase( code.getName() );
	}

	@Override
	public boolean isTabClosable( org.lgna.project.ast.AbstractCode code ) {
		return org.alice.stageide.ast.BootstrapUtilties.MY_FIRST_PROCEDURE_NAME.equalsIgnoreCase( code.getName() ) == false;
	}

	@Override
	protected java.util.List<? super org.lgna.project.ast.JavaType> addPrimeTimeJavaTypes( java.util.List<? super org.lgna.project.ast.JavaType> rv ) {
		rv = super.addPrimeTimeJavaTypes( rv );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Model.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Biped.class ) );
		return rv;
	}

	@Override
	protected java.util.List<? super org.lgna.project.ast.JavaType> addSecondaryJavaTypes( java.util.List<? super org.lgna.project.ast.JavaType> rv ) {
		super.addSecondaryJavaTypes( rv );

		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJoint.class ) );
		rv.add( null );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThing.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.STurnable.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SMovableTurnable.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SModel.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJointedModel.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SBillboard.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SAxes.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SShape.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SSphere.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SCone.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SDisc.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SMarker.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThingMarker.class ) );
		//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SCameraMarker.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Paint.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Color.class ) );
		rv.add( null );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.MoveDirection.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.TurnDirection.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.RollDirection.class ) );
		rv.add( null );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.AudioSource.class ) );
		return rv;
	}

	private static final org.lgna.project.ast.JavaType JOINTED_MODEL_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJointedModel.class );

	private static String getFieldMethodNameHint( org.lgna.project.ast.AbstractField field ) {
		if( field instanceof org.lgna.project.ast.JavaField ) {
			java.lang.reflect.Field fld = ( (org.lgna.project.ast.JavaField)field ).getFieldReflectionProxy().getReification();
			if( fld != null ) {
				if( fld.isAnnotationPresent( org.lgna.project.annotations.FieldTemplate.class ) ) {
					org.lgna.project.annotations.FieldTemplate propertyFieldTemplate = fld.getAnnotation( org.lgna.project.annotations.FieldTemplate.class );
					String methodNameHint = propertyFieldTemplate.methodNameHint();
					if( methodNameHint.length() > 0 ) {
						return methodNameHint;
					}
				}
			}
		}
		return null;
	}

	@Override
	public org.lgna.project.ast.UserType<?> augmentTypeIfNecessary( org.lgna.project.ast.UserType<?> rv ) {
		if( JOINTED_MODEL_TYPE.isAssignableFrom( rv ) ) {
			org.lgna.project.ast.AbstractConstructor constructor0 = org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0( rv );
			org.lgna.project.ast.AbstractType<?, ?, ?> resourceType = org.alice.ide.typemanager.ConstructorArgumentUtilities.getParameter0Type( constructor0 );
			if( resourceType != null ) {
				//pass
			} else {
				org.lgna.project.ast.JavaField field = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( constructor0 );
				if( field != null ) {
					resourceType = field.getValueType();
				}
			}
			if( resourceType != null ) {
				org.lgna.project.ast.JavaType ancestorType = rv.getFirstEncounteredJavaType();
				if( resourceType == org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0Parameter0Type( ancestorType ) ) {
					//skip
				} else {
					org.lgna.project.ast.JavaMethod getJointArrayMethod = JOINTED_MODEL_TYPE.getDeclaredMethod( "getJointArray", org.lgna.story.resources.JointId[].class );
					org.lgna.project.ast.JavaMethod getJointArrayIdMethod = JOINTED_MODEL_TYPE.getDeclaredMethod( "getJointArray", org.lgna.story.resources.JointArrayId.class );
					org.lgna.project.ast.JavaMethod getJointMethod = JOINTED_MODEL_TYPE.getDeclaredMethod( "getJoint", org.lgna.story.resources.JointId.class );
					org.lgna.project.ast.JavaMethod getPoseMethod = JOINTED_MODEL_TYPE.getDeclaredMethod( "getPose", org.lgna.story.JointedModelPose.class );

					org.lgna.project.ast.JavaMethod strikePoseMethod = JOINTED_MODEL_TYPE.getDeclaredMethod( "strikePose", org.lgna.story.Pose.class, org.lgna.story.StrikePose.Detail[].class );
					for( org.lgna.project.ast.AbstractField field : resourceType.getDeclaredFields() ) {
						if( field.isStatic() ) {
							if( field.getValueType().isAssignableTo( org.lgna.story.resources.JointId.class ) && ( field.getVisibility() != org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN ) ) {
								String methodName = getFieldMethodNameHint( field );
								if( methodName == null ) {
									methodName = org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( field.getName(), "get" );
								}
								org.lgna.project.ast.UserMethod method = org.lgna.project.ast.AstUtilities.createFunction( methodName, org.lgna.story.SJoint.class );
								method.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.GENERATED );
								org.lgna.project.ast.BlockStatement body = method.body.getValue();
								org.lgna.project.ast.Expression expression = org.lgna.project.ast.AstUtilities.createMethodInvocation(
										new org.lgna.project.ast.ThisExpression(),
										getJointMethod,
										org.lgna.project.ast.AstUtilities.createStaticFieldAccess( field ) );
								body.statements.add( org.lgna.project.ast.AstUtilities.createReturnStatement( org.lgna.story.SJoint.class, expression ) );
								rv.methods.add( method );
							} else if( field.getValueType().isAssignableTo( org.lgna.story.resources.JointId[].class ) && ( field.getVisibility() != org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN ) ) {
								String methodName = getFieldMethodNameHint( field );
								if( methodName == null ) {
									methodName = org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( field.getName(), "get" );
								}
								org.lgna.project.ast.UserMethod method = org.lgna.project.ast.AstUtilities.createFunction( methodName, org.lgna.story.SJoint[].class );
								method.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.GENERATED );
								org.lgna.project.ast.BlockStatement body = method.body.getValue();
								org.lgna.project.ast.Expression expression = org.lgna.project.ast.AstUtilities.createMethodInvocation(
										new org.lgna.project.ast.ThisExpression(),
										getJointArrayMethod,
										org.lgna.project.ast.AstUtilities.createStaticFieldAccess( field ) );
								body.statements.add( org.lgna.project.ast.AstUtilities.createReturnStatement( org.lgna.story.SJoint[].class, expression ) );
								rv.methods.add( method );
							} else if( field.getValueType().isAssignableTo( org.lgna.story.resources.JointArrayId.class ) && ( field.getVisibility() != org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN ) ) {
								String methodName = getFieldMethodNameHint( field );
								if( methodName == null ) {
									methodName = org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( field.getName(), "get" );
								}
								org.lgna.project.ast.UserMethod method = org.lgna.project.ast.AstUtilities.createFunction( methodName, org.lgna.story.SJoint[].class );
								method.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.GENERATED );
								org.lgna.project.ast.BlockStatement body = method.body.getValue();
								org.lgna.project.ast.Expression expression = org.lgna.project.ast.AstUtilities.createMethodInvocation(
										new org.lgna.project.ast.ThisExpression(),
										getJointArrayIdMethod,
										org.lgna.project.ast.AstUtilities.createStaticFieldAccess( field ) );
								body.statements.add( org.lgna.project.ast.AstUtilities.createReturnStatement( org.lgna.story.SJoint[].class, expression ) );
								rv.methods.add( method );
							} else if( field.getValueType().isAssignableTo( org.lgna.story.Pose.class ) && ( field.getVisibility() != org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN ) ) {
								String methodName = getFieldMethodNameHint( field );
								if( methodName == null ) {
									methodName = org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( field.getName() );
								}
								org.lgna.project.ast.UserMethod method = org.lgna.project.ast.AstUtilities.createProcedure( methodName );
								method.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.GENERATED );
								//								UserParameter detailsParameter = new UserParameter( "details", StrikePose.Detail.class );
								//								method.getVariableLengthParameter().
								//								method.requiredParameters.add( detailsParameter );
								org.lgna.project.ast.BlockStatement body = method.body.getValue();
								org.lgna.project.ast.MethodInvocation mi = org.lgna.project.ast.AstUtilities.createMethodInvocation( new org.lgna.project.ast.ThisExpression(), strikePoseMethod, org.lgna.project.ast.AstUtilities.createStaticFieldAccess( field ) );
								//mi.variableArguments.add( new SimpleArgument( strikePoseMethod.getVariableLengthParameter(), new ParameterAccess( detailsParameter ) ) );
								body.statements.add( new org.lgna.project.ast.ExpressionStatement( mi ) );
								rv.methods.add( method );
							}
						}
					}
				}
			}
		}
		return rv;
	}

	@Override
	public boolean isExportTypeDesiredFor( org.lgna.project.ast.NamedUserType type ) {
		if( org.alice.ide.croquet.models.ui.preferences.IsIncludingImportAndExportType.getValue() ) {
			return ( type.isAssignableTo( org.lgna.story.SScene.class ) == false ) && ( type.isAssignableTo( org.lgna.story.SProgram.class ) == false );
		} else {
			return false;
		}
	}

	private org.lgna.project.ast.AbstractType<?, ?, ?> getSpecificPoseBuilderType( org.lgna.project.ast.Expression expression ) {
		if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
			org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
			return getSpecificPoseBuilderType( methodInvocation.expression.getValue() );
		} else if( expression instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)expression;
			return instanceCreation.getType();
		} else {
			return null;
		}
	}

	private org.lgna.project.ast.AbstractType<?, ?, ?> getBuildMethodPoseBuilderType( org.lgna.project.ast.MethodInvocation methodInvocation, boolean isSpecificPoseBuilderTypeRequired ) {
		org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
		if( "build".equals( method.getName() ) ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = methodInvocation.expression.getExpressionType();
			if( type.isAssignableTo( org.lgna.story.PoseBuilder.class ) ) {
				if( isSpecificPoseBuilderTypeRequired ) {
					return getSpecificPoseBuilderType( methodInvocation.expression.getValue() );
				} else {
					return type;
				}
			}
		}
		return null;
	}

	public org.lgna.project.ast.AbstractType<?, ?, ?> getBuildMethodPoseBuilderType( org.lgna.project.ast.MethodInvocation methodInvocation ) {
		return getBuildMethodPoseBuilderType( methodInvocation, true );
	}

	public boolean isBuildMethod( org.lgna.project.ast.MethodInvocation methodInvocation ) {
		return getBuildMethodPoseBuilderType( methodInvocation, false ) != null;
	}

	@Override
	public org.alice.ide.iconfactory.IconFactoryManager createIconFactoryManager() {
		return new org.alice.stageide.iconfactory.StoryIconFactoryManager();
	}
}
