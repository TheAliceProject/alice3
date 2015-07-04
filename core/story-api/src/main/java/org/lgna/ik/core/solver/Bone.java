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

package org.lgna.ik.core.solver;

import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;

/**
 * @author Dennis Cosgrove
 */
//this is per-chain. related to the calculation, not the structure.
public class Bone {

	public enum Direction {
		DOWNSTREAM,
		UPSTREAM
	}

	// Axis does not know whether the joint is reverse or not. it just is an axis. it knows which bone and which index in joint it is, it contains the corrected axis for this chain.
	public static class Axis {
		private final edu.cmu.cs.dennisc.math.Vector3 axis;

		//these are not local. angular is naturally radian.
		private final edu.cmu.cs.dennisc.math.Vector3 linearContribution = edu.cmu.cs.dennisc.math.Vector3.createZero();
		private final edu.cmu.cs.dennisc.math.Vector3 angularContribution = edu.cmu.cs.dennisc.math.Vector3.createZero();
		private final Bone bone;
		private final int originalIndexInJoint;

		//this is the speed that will be used during simulation
		//TODO this should not be here. someone else should take care of this.
		private double desiredAngleSpeed;

		public Axis( Bone bone, int originalIndexInJoint ) {
			this.bone = bone;
			this.originalIndexInJoint = originalIndexInJoint;
			this.axis = edu.cmu.cs.dennisc.math.Vector3.createZero();
		}

		public edu.cmu.cs.dennisc.math.Vector3 getLinearContribution() {
			return linearContribution;
		}

		public edu.cmu.cs.dennisc.math.Vector3 getAngularContribution() {
			return angularContribution;
		}

		public void updateLinearContributions( edu.cmu.cs.dennisc.math.Vector3 jointEeVector ) {
			edu.cmu.cs.dennisc.math.Vector3.setReturnValueToCrossProduct( this.linearContribution, this.axis, jointEeVector );
		}

		public void updateAngularContributions() {
			this.angularContribution.set( this.axis );
		}

		public edu.cmu.cs.dennisc.math.Vector3 getCurrentValue() {
			return axis;
		}

		public void setCurrentValue( edu.cmu.cs.dennisc.math.Vector3 axis ) {
			this.axis.set( axis );
		}

		public void invertDirection() {
			this.axis.multiply( -1 );
		}

		public Bone getBone() {
			return bone;
		}

		public int getOriginalIndexInJoint() {
			return originalIndexInJoint;
		}

		//so that I can use this in maps and the axes with the same joint and index are equal
		@Override
		public int hashCode() {
			return ( originalIndexInJoint * 19 ) + bone.getA().hashCode();
		}

		@Override
		public boolean equals( java.lang.Object o ) {
			if( o == this ) {
				return true;
			}
			if( o instanceof Axis ) {
				Axis ua = (Axis)o;
				return ( this.originalIndexInJoint == ua.originalIndexInJoint ) &&
						edu.cmu.cs.dennisc.java.util.Objects.equals( this.bone.getA(), ua.bone.getA() );
			} else {
				return false;
			}
		}

		public void applyRotation( double angleInRadians ) {
			//it's nicer to rotate around one axis once using Bone.applyLocalRotation(angle, axis)
			//get the original axis
			edu.cmu.cs.dennisc.math.Vector3 originalAxis = getLocalAxis();
			//turn the joint around the original local vector
			bone.getA().applyRotationInRadians( originalAxis, angleInRadians );
		}

		public edu.cmu.cs.dennisc.math.Vector3 getLocalAxis() {
			return indexToLocalVector( originalIndexInJoint );
		}

		//TODO can never have a 2dof joint that rotates an axis? maybe I can? I just always need to apply rotations in order and I'll be fine?
		//I was using axes to be global x, y, z. ooh, that's right.
		//maybe the axis class should be keeping the original, which is inverted of current?
		private edu.cmu.cs.dennisc.math.Vector3 indexToLocalVector( int originalIndexInJoint ) {
			switch( originalIndexInJoint ) {
			case 0:
				return edu.cmu.cs.dennisc.math.Vector3.accessPositiveXAxis();
			case 1:
				return edu.cmu.cs.dennisc.math.Vector3.accessPositiveYAxis();
			case 2:
				return edu.cmu.cs.dennisc.math.Vector3.accessPositiveZAxis();
			}
			//			if(bone.isABallJoint()) {
			//				//return global x, y, z
			//			} else {
			//				//return current
			//				OrthogonalMatrix3x3 orientation = bone.getA().getTransformation(AsSeenBy.SCENE).orientation;
			//				switch (originalIndexInJoint) {
			//				case 0:
			//					return orientation.right;
			//				case 1:
			//					return orientation.up;
			//				case 2:
			//					return orientation.backward;
			//				}
			//			}
			throw new RuntimeException( "Axis index > 2?" );
		}

	}

	private final Chain chain;
	private final int index;
	private final Axis[] axesByIndex = new Axis[ 3 ];
	private final java.util.List<Axis> axesList = new java.util.ArrayList<Axis>();

	private final edu.cmu.cs.dennisc.math.Point3 anchor = edu.cmu.cs.dennisc.math.Point3.createZero();

	public Bone( Chain chain, int index ) {
		this.chain = chain;
		this.index = index;

		org.lgna.story.implementation.JointImp a = this.getA();

		if( a.isFreeInX() ) {
			Axis newAxis = new Axis( this, 0 );
			this.axesByIndex[ 0 ] = newAxis;
			axesList.add( newAxis );
		}
		if( a.isFreeInY() ) {
			Axis newAxis = new Axis( this, 1 );
			this.axesByIndex[ 1 ] = newAxis;
			axesList.add( newAxis );
		}
		if( a.isFreeInZ() ) {
			Axis newAxis = new Axis( this, 2 );
			this.axesByIndex[ 2 ] = newAxis;
			axesList.add( newAxis );
		}

		//		if(isABallJoint()) {
		//			if(isStraight()) {
		//				this.axesByIndex[ 0 ].setCurrentValue(Vector3.accessPositiveXAxis());
		//				this.axesByIndex[ 1 ].setCurrentValue(Vector3.accessPositiveYAxis());
		//				this.axesByIndex[ 2 ].setCurrentValue(Vector3.accessPositiveZAxis());
		//			} else {
		//				this.axesByIndex[ 0 ].setCurrentValue(Vector3.accessNegativeXAxis());
		//				this.axesByIndex[ 1 ].setCurrentValue(Vector3.accessNegativeYAxis());
		//				this.axesByIndex[ 2 ].setCurrentValue(Vector3.accessNegativeZAxis());
		//			}
		//		}

		//probably unnecessary but wouldn't hurt:
		updateStateFromJoint();
	}

	public org.lgna.story.implementation.JointImp getA() {
		return this.chain.getJointImpAt( this.index );
	}

	public java.util.List<Axis> getAxes() {
		return axesList;
	}

	//	public org.lgna.story.implementation.JointImp getB() {
	//		return this.chain.getJointImpAt( this.index+1 );
	//	}
	private boolean isStraight() {
		return getDirection() == Direction.DOWNSTREAM;
	}

	public Direction getDirection() {
		return this.chain.getDirectionAt( this.index );
	}

	public int getDegreesOfFreedom() {
		int rv = 0;
		for( Axis axis : axesByIndex ) {
			if( axis != null ) {
				rv++;
			}
		}
		return rv;
	}

	public void updateLinearContributions( edu.cmu.cs.dennisc.math.Vector3 jointEeVector ) {
		for( Axis axis : axesByIndex ) {
			if( axis != null ) {
				axis.updateLinearContributions( jointEeVector );
			}
		}
	}

	public void updateAngularContributions() {
		for( Axis axis : axesByIndex ) {
			if( axis != null ) {
				axis.updateAngularContributions();
			}
		}
	}

	@Override
	public String toString() {
		org.lgna.story.implementation.JointImp a = this.getA();
		//		org.lgna.story.implementation.JointImp b = this.getB();
		StringBuilder sb = new StringBuilder();
		sb.append( "Bone[" );
		sb.append( a.getJointId() );
		//		sb.append( "->" );
		//		sb.append( b.getJointId() );
		sb.append( ", " );
		sb.append( getDirection() );
		sb.append( "]" );
		sb.append( " (" );

		int count = 0;
		for( Axis axis : axesList ) {
			sb.append( axis.originalIndexInJoint );
			sb.append( ": " );
			sb.append( String.format( "%.2f", axis.desiredAngleSpeed ) );
			if( count < ( axesList.size() - 1 ) ) {
				sb.append( ", " );
			}
			++count;
		}
		sb.append( ")" );

		return sb.toString();
	}

	public edu.cmu.cs.dennisc.math.Point3 getAnchorPosition() {
		// this returns the curren anchor position that was just fed to this from the world
		return anchor;
	}

	public OrthogonalMatrix3x3 getCurrentOrientationFromJoint() {
		return getA().getLocalOrientation();
	}

	public void updateStateFromJoint() {
		//get anchor
		anchor.set( getA().getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE ).translation );

		//get axes
		//		if( !isABallJoint() ) {
		org.lgna.story.implementation.JointImp a = getA();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 atrans = a.getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE );
		//invert if reverse
		if( a.isFreeInX() ) {
			this.axesByIndex[ 0 ].setCurrentValue( atrans.orientation.right );
			if( !isStraight() ) {
				this.axesByIndex[ 0 ].invertDirection();
			}
		}
		if( a.isFreeInY() ) {
			this.axesByIndex[ 1 ].setCurrentValue( atrans.orientation.up );
			if( !isStraight() ) {
				this.axesByIndex[ 1 ].invertDirection();
			}
		}
		if( a.isFreeInZ() ) {
			this.axesByIndex[ 2 ].setCurrentValue( atrans.orientation.backward );
			if( !isStraight() ) {
				this.axesByIndex[ 2 ].invertDirection();
			}
		}
		//		}

		//the comments below are done above. leaving in for reference.

		//get position and axes from joint
		//get position
		//get axes
		//if joint is ball, then get three orthogonal axes (prolly parallel to world axes)
		//if the chain is using the joint reverse, flip the axis
		//do I need to do this for ball? not sure. possibly. think hard pls.
		//YES! because the same rotation affects two ends of the joint differently!

		//these axes HAVE TO BE INVERTED if joint is not in the right direction (not downstream)
		//		throw new RuntimeException("todo invert axes if chain is reverse");
	}

	public void applyLocalRotation( edu.cmu.cs.dennisc.math.Vector3 axis, double angle ) {
		getA().applyRotationInRadians( axis, angle );
	}

}
