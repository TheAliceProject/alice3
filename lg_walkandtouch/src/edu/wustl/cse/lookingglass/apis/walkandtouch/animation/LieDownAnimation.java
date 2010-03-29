/*
 * Copyright (c) 2008-2010, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor 
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL 
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, 
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, 
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.animation;

import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author caitlin
 */
public class LieDownAnimation extends StraightenAnimation {
	org.alice.apis.moveandturn.MoveDirection feetFaceDirection = org.alice.apis.moveandturn.MoveDirection.FORWARD;	
	org.alice.apis.moveandturn.Transformable target = null;
	
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 initialOrient;
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 finalOrient;
	
	protected edu.cmu.cs.dennisc.math.Point3 m_positionBegin;
	protected edu.cmu.cs.dennisc.math.Point3 m_positionEnd;
	

	public LieDownAnimation(edu.wustl.cse.lookingglass.apis.walkandtouch.Person subject, org.alice.apis.moveandturn.Transformable target, org.alice.apis.moveandturn.MoveDirection feetFaceDirection, double duration) {
		super(subject, duration);
		this.subject = subject;
		this.target = target;
		this.feetFaceDirection = feetFaceDirection;
	}	
	
	@Override
	public void prologue() {
		super.prologue();
		m_positionBegin = subject.getPosition(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		
		initialOrient = subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		finalOrient = this.getGoalOrientation( target.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE ), new edu.cmu.cs.dennisc.math.Vector3(0,1,0) );
	}
	
	@Override
	protected void setPortion( double portion ) {
		super.setPortion(portion);
		
		if( m_positionEnd==null ) {
			m_positionEnd = getPositionEnd();
		}
		subject.setPositionRightNow( Point3.createInterpolation( m_positionBegin, m_positionEnd, portion ), org.alice.apis.moveandturn.AsSeenBy.SCENE );

		UnitQuaternion currentQuat = UnitQuaternion.createInterpolation(initialOrient.createUnitQuaternion(), finalOrient.createUnitQuaternion(), portion);
		subject.setOrientationRightNow( currentQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}
	
	@Override
	public void epilogue() {
		super.epilogue();
		m_positionEnd = null;
	}
	
	
	
	protected edu.cmu.cs.dennisc.math.Point3 getPositionEnd() {
		if (target != null) {
			
			edu.cmu.cs.dennisc.math.Point3 endPos = null;
			
			if (target.getName().equals("ground")){
				endPos = subject.getAxisAlignedMinimumBoundingBox(org.alice.apis.moveandturn.AsSeenBy.SCENE).getCenterOfBottomFace();
			} else {
				endPos = target.getAxisAlignedMinimumBoundingBox(org.alice.apis.moveandturn.AsSeenBy.SCENE).getCenterOfTopFace();
			}
			
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 targetMatrix = target.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			edu.cmu.cs.dennisc.math.Vector3 forward = targetMatrix.createForward(); 
			edu.cmu.cs.dennisc.math.Vector3 right = targetMatrix.up; 
			
			double subjectHeight = subject.getHeight();
			double subjectDepth = subject.getDepth();
			
			double zOffset = java.lang.Math.abs(subject.getAxisAlignedMinimumBoundingBox(subject).getCenterOfBackFace().z) - subject.getAxisAlignedMinimumBoundingBox(subject).getDepth()*0.2;
			
			// TODO: deal with pivots in the floor vs in the center of the character.
			if (feetFaceDirection.equals(org.alice.apis.moveandturn.MoveDirection.BACKWARD)) {
				endPos = new edu.cmu.cs.dennisc.math.Point3(endPos.x - (forward.x * subjectHeight/2.0), endPos.y + zOffset + (forward.y * subjectHeight/2.0), endPos.z - (forward.z * subjectHeight/2.0));
			} else if (feetFaceDirection.equals(org.alice.apis.moveandturn.MoveDirection.FORWARD)) {
				endPos = new edu.cmu.cs.dennisc.math.Point3(endPos.x + (forward.x * subjectHeight/2.0), endPos.y + zOffset + (forward.y * subjectHeight/2.0), endPos.z + (forward.z * subjectHeight/2.0));
			} else if (feetFaceDirection.equals(org.alice.apis.moveandturn.MoveDirection.LEFT)) {
				endPos = new edu.cmu.cs.dennisc.math.Point3(endPos.x + (right.x * subjectHeight/2.0), endPos.y + zOffset +(forward.y * subjectHeight/2.0), endPos.z - (right.z * subjectHeight/2.0));
			} else if (feetFaceDirection.equals(org.alice.apis.moveandturn.MoveDirection.RIGHT)) {
				endPos = new edu.cmu.cs.dennisc.math.Point3(endPos.x - (right.x * subjectHeight/2.0), endPos.y + zOffset + (forward.y * subjectHeight/2.0), endPos.z + (right.z * subjectHeight/2.0));
			}
			
			return endPos;
		} else {
			return m_positionBegin;
		}
	}
	
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getGoalOrientation(edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 targetsOrient, edu.cmu.cs.dennisc.math.Vector3 goalForward) {
		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		edu.cmu.cs.dennisc.math.Vector3 goalUp = null;
		// this is going to change based on how we want to align to target model
		if ( (feetFaceDirection.equals(org.alice.apis.moveandturn.MoveDirection.FORWARD)) || (feetFaceDirection.equals(org.alice.apis.moveandturn.MoveDirection.BACKWARD)) ) {
			goalUp = targetsOrient.createForward(); //targetsOrient.getColumn(2);
			if (feetFaceDirection.equals(org.alice.apis.moveandturn.MoveDirection.FORWARD)) {
				goalUp = targetsOrient.backward;
			}
		} else {
			goalUp = targetsOrient.right; //targetsOrient.getColumn(0);
			if ( feetFaceDirection.equals(org.alice.apis.moveandturn.MoveDirection.LEFT )) {
				goalUp.negate();
			}
		}
		orient = OrthogonalMatrix3x3.createFromForwardAndUpGuide(goalForward, goalUp);	
		return orient;
	}
	
	@Override
	protected void adjustHeight() {
		if (target == null) {
			super.adjustHeight();
		} else {
			double distanceAboveTarget = 0.0;
			if (subject != null) {
				distanceAboveTarget = subject.getAxisAlignedMinimumBoundingBox(org.alice.apis.moveandturn.AsSeenBy.SCENE).getCenterOfBottomFace().y;
				distanceAboveTarget -= target.getAxisAlignedMinimumBoundingBox(org.alice.apis.moveandturn.AsSeenBy.SCENE).getCenterOfTopFace().y;				

				subject.moveRightNow(org.alice.apis.moveandturn.MoveDirection.DOWN, distanceAboveTarget , org.alice.apis.moveandturn.AsSeenBy.SCENE );
			}
		}
	}
}
