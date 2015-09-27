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
package org.alice.stageide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionCascadeManager extends org.alice.ide.cascade.ExpressionCascadeManager {
	public ExpressionCascadeManager() {
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.ImagePaintFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.ImageSourceFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.AudioSourceFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.ColorFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.KeyFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.ArrowKeyListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.NumberKeyListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.MouseClickedOnScreenFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.MouseClickOnObjectFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.TransformationListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.ComesIntoViewEventListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.LeavesViewEventListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.StartCollisionListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.EndCollisionListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.EnterProximityEventListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.ExitProximityEventListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.StartOcclusionEventListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.EndOcclusionEventListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.SceneActivationEventFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.TimerEventListenerFillerInner() );
		this.addExpressionFillerInner( new org.alice.stageide.cascade.fillerinners.KeyListenerFillerInner() );

		this.addSimsExpressionFillerInners();
		//this.addExpressionFillerInner( org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner.getInstance( org.lgna.story.Color.class ) );

		this.addRelationalTypeToBooleanFillerInner( org.lgna.story.SThing.class );
	}

	@Override
	protected org.lgna.project.ast.AbstractType<?, ?, ?> getEnumTypeForInterfaceType( org.lgna.project.ast.AbstractType<?, ?, ?> interfaceType ) {
		if( interfaceType == org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Style.class ) ) {
			return org.lgna.project.ast.JavaType.getInstance( org.lgna.story.AnimationStyle.class );
		} else {
			return super.getEnumTypeForInterfaceType( interfaceType );
		}
	}

	@Override
	protected boolean areEnumConstantsDesired( org.lgna.project.ast.AbstractType enumType ) {
		if( enumType == org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Key.class ) ) {
			return false;
		} else {
			return super.areEnumConstantsDesired( enumType );
		}
	}

	@Override
	protected org.lgna.croquet.CascadeMenuModel<org.lgna.project.ast.Expression> createPartMenuModel( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractType<?, ?, ?> desiredType,
			org.lgna.project.ast.AbstractType<?, ?, ?> expressionType, boolean isOwnedByCascadeItemMenuCombo ) {
		if( expressionType.isAssignableTo( org.lgna.story.SJointedModel.class ) ) {
			if( desiredType.isAssignableFrom( org.lgna.story.SJoint.class ) ) {
				if( org.alice.stageide.ast.JointedTypeInfo.isJointed( expressionType ) ) {
					java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos = org.alice.stageide.ast.JointedTypeInfo.getInstances( expressionType );
					return new JointExpressionMenuModel( expression, jointedTypeInfos, 0, isOwnedByCascadeItemMenuCombo );
				}
			}
		}
		return null;
	}

	@Override
	protected boolean isApplicableForPartFillIn( org.lgna.project.ast.AbstractType<?, ?, ?> desiredType, org.lgna.project.ast.AbstractType<?, ?, ?> expressionType ) {
		return desiredType.isAssignableFrom( org.lgna.story.SJoint.class ) && expressionType.isAssignableTo( org.lgna.story.SJointedModel.class );
	}

	@Override
	protected void appendOtherTypes( java.util.List<org.lgna.project.ast.AbstractType<?, ?, ?>> otherTypes ) {
		super.appendOtherTypes( otherTypes );
		otherTypes.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThing.class ) );

	}

	protected void addSimsExpressionFillerInners() {
	}
}
