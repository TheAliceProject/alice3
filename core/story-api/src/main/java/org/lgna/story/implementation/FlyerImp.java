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
package org.lgna.story.implementation;

/**
 * @author dculyba
 * 
 */
public final class FlyerImp extends JointedModelImp<org.lgna.story.SFlyer, org.lgna.story.resources.FlyerResource> {
	public FlyerImp( org.lgna.story.SFlyer abstraction, JointImplementationAndVisualDataFactory<org.lgna.story.resources.FlyerResource> factory ) {
		super( abstraction, factory );
	}

	@Override
	public org.lgna.story.resources.JointId[] getRootJointIds() {
		return org.lgna.story.resources.FlyerResource.JOINT_ID_ROOTS;
	}

	@Override
	protected edu.cmu.cs.dennisc.math.Vector4 getThoughtBubbleOffset() {
		return this.getTopOffsetForJoint( this.getJointImplementation( org.lgna.story.resources.FlyerResource.HEAD ) );
	}

	@Override
	protected edu.cmu.cs.dennisc.math.Vector4 getSpeechBubbleOffset() {
		return this.getFrontOffsetForJoint( this.getJointImplementation( org.lgna.story.resources.FlyerResource.MOUTH ) );
	}

	//	private static class WingJointData {
	//		private final JointImp jointImp;
	//		private final edu.cmu.cs.dennisc.math.UnitQuaternion q0;
	//		private final edu.cmu.cs.dennisc.math.UnitQuaternion q1;
	//
	//		public WingJointData( JointImp jointImp ) {
	//			this.jointImp = jointImp;
	//			this.q0 = this.jointImp.getLocalOrientation().createUnitQuaternion();
	//			ForwardAndUpGuide faug = new ForwardAndUpGuide( Vector3.accessPositiveXAxis(), Vector3.accessPositiveYAxis() );
	//			//			edu.cmu.cs.dennisc.math.UnitQuaternion q = faug.createUnitQuaternion();
	//			edu.cmu.cs.dennisc.math.UnitQuaternion q = edu.cmu.cs.dennisc.math.UnitQuaternion.createIdentity();
	//			if( q != null ) {
	//				if( this.q0.isWithinReasonableEpsilonOrIsNegativeWithinReasonableEpsilon( q ) ) {
	//					this.q1 = null;
	//				} else {
	//					this.q1 = q;
	//				}
	//			} else {
	//				this.q1 = null;
	//			}
	//		}
	//
	//		public void setPortion( double portion ) {
	//			if( this.q1 != null ) {
	//				this.jointImp.setLocalOrientationOnly( edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( this.q0, this.q1, portion ).createOrthogonalMatrix3x3() );
	//			} else {
	//				//System.err.println( "skipping: " + this.jointImp );
	//			}
	//		}
	//
	//		public void epilogue() {
	//			if( this.q1 != null ) {
	//				this.jointImp.setLocalOrientationOnly( this.q1.createOrthogonalMatrix3x3() );
	//			} else {
	//				//System.err.println( "skipping: " + this.jointImp );
	//			}
	//		}
	//	}
	//
	//	private static class UnfoldWingsTreeWalkObserver implements TreeWalkObserver {
	//		private java.util.List<WingJointData> list = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	//
	//		public void pushJoint( JointImp jointImp ) {
	//			if( ( jointImp != null ) && (
	//					//( jointImp.getJointId() == FlyerResource.LEFT_WING_SHOULDER ) ||
	//					( jointImp.getJointId() == FlyerResource.LEFT_WING_ELBOW ) ||
	//							( jointImp.getJointId() == FlyerResource.LEFT_WING_WRIST ) ||
	//							( jointImp.getJointId() == FlyerResource.LEFT_WING_TIP ) ||
	//							//( jointImp.getJointId() == FlyerResource.RIGHT_WING_SHOULDER ) ||
	//							( jointImp.getJointId() == FlyerResource.RIGHT_WING_ELBOW ) ||
	//							( jointImp.getJointId() == FlyerResource.RIGHT_WING_WRIST ) ||
	//					( jointImp.getJointId() == FlyerResource.RIGHT_WING_TIP )
	//					) ) {
	//				list.add( new WingJointData( jointImp ) );
	//			}
	//		}
	//
	//		public void handleBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
	//		}
	//
	//		public void popJoint( JointImp joint ) {
	//		}
	//	};
	//
	//	public void unfoldWings() {
	//		UnfoldWingsTreeWalkObserver treeWalkObserver = new UnfoldWingsTreeWalkObserver();
	//		this.treeWalk( treeWalkObserver );
	//		for( WingJointData jointData : treeWalkObserver.list ) {
	//			jointData.epilogue();
	//		}
	//	}
	//
	//	public void animateUnfoldWings( double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	//		duration = adjustDurationIfNecessary( duration );
	//		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
	//			this.unfoldWings();
	//		} else {
	//			final UnfoldWingsTreeWalkObserver treeWalkObserver = new UnfoldWingsTreeWalkObserver();
	//			this.treeWalk( treeWalkObserver );
	//			class UnfoldWingsAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
	//				public UnfoldWingsAnimation( double duration, edu.cmu.cs.dennisc.animation.Style style ) {
	//					super( duration, style );
	//				}
	//
	//				@Override
	//				protected void prologue() {
	//				}
	//
	//				@Override
	//				protected void setPortion( double portion ) {
	//					for( WingJointData jointData : treeWalkObserver.list ) {
	//						jointData.setPortion( portion );
	//					}
	//				}
	//
	//				@Override
	//				protected void epilogue() {
	//					for( WingJointData jointData : treeWalkObserver.list ) {
	//						jointData.epilogue();
	//					}
	//				}
	//			}
	//			perform( new UnfoldWingsAnimation( duration, style ) );
	//		}
	//	}
	//
	//	public void animateUnfoldWings( double duration ) {
	//		this.animateUnfoldWings( duration, DEFAULT_STYLE );
	//	}
	//
	//	public void animateUnfoldWings() {
	//		this.animateUnfoldWings( DEFAULT_DURATION );
	//	}
}
