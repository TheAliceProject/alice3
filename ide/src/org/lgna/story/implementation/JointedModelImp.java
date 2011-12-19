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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class JointedModelImp< A extends org.lgna.story.JointedModel, R extends org.lgna.story.resources.JointedModelResource > extends ModelImp {
	public static interface VisualData { 
		public edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals();
		public edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgAppearances();
		public double getBoundingSphereRadius();
		public void setSGParent(edu.cmu.cs.dennisc.scenegraph.Composite parent);
	}
	public static interface JointImplementationAndVisualDataFactory< R extends org.lgna.story.resources.JointedModelResource > {
		public R getResource();
		public JointImp createJointImplementation( org.lgna.story.implementation.JointedModelImp<?,?> jointedModelImplementation, org.lgna.story.resources.JointId jointId );
		public VisualData createVisualData( org.lgna.story.implementation.JointedModelImp<?,?> jointedModelImplementation );
		public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointId jointId );
	}
	private final JointImplementationAndVisualDataFactory<R> factory;
	private final A abstraction;
	private final VisualData visualData;

	private final java.util.Map< org.lgna.story.resources.JointId, org.lgna.story.implementation.JointImp > mapIdToJoint = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public JointedModelImp( A abstraction, JointImplementationAndVisualDataFactory< R > factory ) {
		this.abstraction = abstraction;
		this.factory = factory;
		this.visualData = this.factory.createVisualData( this );
		this.visualData.setSGParent(this.getSgComposite());
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.visualData.getSgVisuals() ) {
			sgVisual.setParent( this.getSgComposite() );
		}
		for( org.lgna.story.resources.JointId root : this.getRootJointIds() ) {
			this.createJointTree( root, this );
		}
	}
	
	private void createJointTree( org.lgna.story.resources.JointId jointId, EntityImp parent ) {
		//System.err.println( "createJointTree " + jointId );
		JointImp joint = this.createJointImplementation( jointId );
		if (joint == null) {
			joint = this.createJointImplementation(jointId);
		}
		if( joint != null && parent instanceof JointedModelImp ) {
			joint.setCustomJointSgParent( parent.getSgComposite() );
		} else {
			if (joint != null && joint.getSgVehicle() == null && parent != null) {
				joint.setVehicle( parent );
			}
		}
		this.mapIdToJoint.put( jointId, joint );
		for( org.lgna.story.resources.JointId childId : jointId.getChildren( this.factory.getResource() ) ) {
			this.createJointTree( childId, joint );
		}
	}
	
	public void setJointAxisVisibility(boolean jointAxisIsVisible) {
		for (java.util.Map.Entry< org.lgna.story.resources.JointId, org.lgna.story.implementation.JointImp > jointEntry : this.mapIdToJoint.entrySet()) {
			jointEntry.getValue().setJointAxisVisible(jointAxisIsVisible);
		}
	}
	
	@Override
	public A getAbstraction() {
		return this.abstraction;
	}
	public R getResource() {
		return this.factory.getResource();
	}
	public VisualData getVisualData() {
		return this.visualData;
	}
	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals() {
		return this.visualData.getSgVisuals();
	}
	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgPaintAppearances() {
		return this.visualData.getSgAppearances();
	}
	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgOpacityAppearances() {
		return this.getSgPaintAppearances();
	}
	
	public org.lgna.story.implementation.JointImp getJointImplementation( org.lgna.story.resources.JointId jointId ) {
		return this.mapIdToJoint.get( jointId );
//		synchronized( this.mapIdToJoint ) {
//			org.lgna.story.implementation.JointImp rv = this.mapIdToJoint.get( jointId );
//			if( rv != null || this.mapIdToJoint.containsKey( jointId ) ) {
//				//pass
//			} else {
//				rv = this.createJointImplementation( jointId );
//				this.mapIdToJoint.put( jointId, rv );
//				if (rv.getVehicle() == null && jointId.getParent() != null ) {
//					org.lgna.story.implementation.JointImp parentJoint = getJointImplementation(jointId.getParent());
//					rv.setVehicle(parentJoint);
//				}
//				else if ( jointId.getParent() == null ) {
//					rv.setCustomJointSgParent(this.getSgComposite());
//				}
//			}
//			return rv;
//		}
	}
	
	protected edu.cmu.cs.dennisc.math.Vector4 getOffsetForJoint(org.lgna.story.implementation.JointImp jointImp) {
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 jointTransform = jointImp.getTransformation(this);
		offsetAsSeenBySubject.x = jointTransform.translation.x;
		offsetAsSeenBySubject.y = jointTransform.translation.y;
		offsetAsSeenBySubject.z = jointTransform.translation.z;
		offsetAsSeenBySubject.w = 1;
		return offsetAsSeenBySubject;
	}
	
	public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.getOriginalJointOrientation( jointId );
	}
	
	public abstract org.lgna.story.resources.JointId[] getRootJointIds();
	
	public edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getSgSkeletonVisual() {
		if (this.getSgVisuals()[ 0 ] instanceof edu.cmu.cs.dennisc.scenegraph.SkeletonVisual)
		{
			return (edu.cmu.cs.dennisc.scenegraph.SkeletonVisual)this.getSgVisuals()[ 0 ];
		}
		return null;
	}
	
	protected final org.lgna.story.implementation.JointImp createJointImplementation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.createJointImplementation( this, jointId );
	}
	@Override
	protected final double getBoundingSphereRadius() {
		return this.visualData.getBoundingSphereRadius();
	}
	
	private org.lgna.story.implementation.visualization.JointedModelVisualization visualization;
	private org.lgna.story.implementation.visualization.JointedModelVisualization getVisualization() {
		if( this.visualization != null ) {
			//pass
		} else {
			this.visualization = new org.lgna.story.implementation.visualization.JointedModelVisualization( this );
		}
		return this.visualization;
	}
	public void showVisualization() {
		this.getVisualization().setParent( this.getSgComposite() );
	}
	public void hideVisualization() {
		if( this.visualization != null ) {
			this.visualization.setParent( null );
		}
	}
	
	public static interface TreeWalkObserver {
		public void pushJoint( JointImp joint );
		public void handleBone( JointImp parent, JointImp child );
		public void popJoint( JointImp joint );
	}
	
	private void treeWalk( org.lgna.story.resources.JointId parentId, TreeWalkObserver observer ) {
		JointImp parentImp = this.getJointImplementation( parentId );
		if( parentImp != null ) {
			observer.pushJoint( parentImp );
			R resource = this.getResource();
			for( org.lgna.story.resources.JointId childId : parentId.getChildren( resource ) ) {
				JointImp childImp = this.getJointImplementation( childId );
				if( childImp != null ) {
					observer.handleBone( parentImp, childImp );
				}
			}
			observer.popJoint( parentImp );
			for( org.lgna.story.resources.JointId childId : parentId.getChildren( resource ) ) {
				treeWalk( childId, observer );
			}
		}
	}
	public void treeWalk( TreeWalkObserver observer ) {
		for( org.lgna.story.resources.JointId root : this.getRootJointIds() ) {
			this.treeWalk( root, observer );
		}
	}
	
	private static enum AddOp {
		PREPEND {
			@Override
			public java.util.List< JointImp > add( java.util.List< JointImp > rv, JointImp joint ) {
				rv.add( 0, joint );
				return rv;
			}
		},
		APPEND {
			@Override
			public java.util.List< JointImp > add( java.util.List< JointImp > rv, JointImp joint ) {
				rv.add( joint );
				return rv;
			}
		};
		public abstract java.util.List< JointImp > add( java.util.List< JointImp > rv, JointImp joint );
	}
	private java.util.List< JointImp > updateJointsBetween( java.util.List< JointImp > rv, JointImp joint, EntityImp ancestor, AddOp addOp ) {
		if( joint == ancestor ) {
			//pass
		} else {
			org.lgna.story.resources.JointId parentId = joint.getJointId().getParent();
			if( parentId != null ) {
				JointImp parent = this.getJointImplementation( parentId );
				this.updateJointsBetween( rv, parent, ancestor, addOp );
			}
		}
		addOp.add( rv, joint );
		return rv;
	}
	public java.util.List< JointImp > getInclusiveListOfJointsBetween( JointImp jointA, JointImp jointB ) {
		assert jointA != null : this;
		assert jointB != null : this;
		java.util.List< JointImp > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		if( jointA == jointB ) {
			//?
			rv.add( jointA );
		} else {
			if( jointA.isDescendantOf( jointB ) ) {
				this.updateJointsBetween( rv, jointA, jointB, AddOp.PREPEND );
			} else if( jointB.isDescendantOf( jointA ) ) {
				this.updateJointsBetween( rv, jointB, jointA, AddOp.APPEND );
			} else {
				this.updateJointsBetween( rv, jointB, this, AddOp.APPEND );
				this.updateJointsBetween( rv, jointA, this, AddOp.PREPEND );
			}
		}
		return rv;
	}
	public java.util.List< JointImp > getInclusiveListOfJointsBetween( org.lgna.story.resources.JointId idA, org.lgna.story.resources.JointId idB ) {
		return this.getInclusiveListOfJointsBetween( this.getJointImplementation( idA ), this.getJointImplementation( idB ) );
	}
	
	private static class JointData {
		private final JointImp jointImp;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q0;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q1;
		public JointData( JointImp jointImp ) {
			this.jointImp = jointImp;
			this.q0 = this.jointImp.getLocalOrientation().createUnitQuaternion();
			edu.cmu.cs.dennisc.math.UnitQuaternion q = this.jointImp.getOriginalOrientation();
			if( q != null ) {
				if( this.q0.isWithinReasonableEpsilonOrIsNegativeWithinReasonableEpsilon( q ) ) {
					this.q1 = null;
				} else {
					this.q1 = q;
				}
			} else {
				this.q1 = null;
			}
		}
//		public JointImp getJointImp() {
//			return this.jointImp;
//		}
//		public edu.cmu.cs.dennisc.math.UnitQuaternion getQ0() {
//			return this.q0;
//		}
//		public edu.cmu.cs.dennisc.math.UnitQuaternion getQ1() {
//			return this.q1;
//		}
		public void setPortion( double portion ) {
			if( this.q1 != null ) {
				this.jointImp.setLocalOrientationOnly( edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( this.q0, this.q1, portion ).createOrthogonalMatrix3x3() );
			} else {
				//System.err.println( "skipping: " + this.jointImp );
			}
		}
		public void epilogue() {
			if( this.q1 != null ) {
				this.jointImp.setLocalOrientationOnly( this.q1.createOrthogonalMatrix3x3() );
			} else {
				//System.err.println( "skipping: " + this.jointImp );
			}
		}
	}
	private static class StraightenTreeWalkObserver implements TreeWalkObserver {
		private java.util.List< JointData > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		public void pushJoint(JointImp jointImp) {
			if( jointImp != null ) {
				list.add( new JointData( jointImp ) );
			}
		}
		public void handleBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
		}	
		public void popJoint(JointImp joint) {
		}
	};
	public void straightenOutJoints() {
		StraightenTreeWalkObserver treeWalkObserver = new StraightenTreeWalkObserver();
		this.treeWalk( treeWalkObserver );
		for( JointData jointData : treeWalkObserver.list ) {
			jointData.epilogue();
		}
	}
	public void animateStraightenOutJoints( double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.straightenOutJoints();
		} else {
			final StraightenTreeWalkObserver treeWalkObserver = new StraightenTreeWalkObserver();
			this.treeWalk( treeWalkObserver );
			class StraightenOutJointsAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
				public StraightenOutJointsAnimation( double duration, edu.cmu.cs.dennisc.animation.Style style ) {
					super( duration, style );
				}
				@Override
				protected void prologue() {
				}
				@Override
				protected void setPortion( double portion ) {
					for( JointData jointData : treeWalkObserver.list ) {
						jointData.setPortion( portion );
					}
				}
				@Override
				protected void epilogue() {
					for( JointData jointData : treeWalkObserver.list ) {
						jointData.epilogue();
					}
				}
			}
			perform( new StraightenOutJointsAnimation( duration, style ) );
		}
	}
	public void animateStraightenOutJoints( double duration ) {
		this.animateStraightenOutJoints( duration, DEFAULT_STYLE );
	}
	public void animateStraightenOutJoints() {
		this.animateStraightenOutJoints( DEFAULT_DURATION );
	}
}
