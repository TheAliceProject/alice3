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

import org.alice.apis.moveandturn.HowMuch;

import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel;

/**
 * @author caitlin
 */
public class LookAtAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
	boolean onlyAffectYaw = false;
	edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel subject = null;
	org.alice.apis.moveandturn.Transformable target = null;
	edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel head = null;
	double turnAmount = 0;
	org.alice.apis.moveandturn.TurnDirection m_direction = null;
	
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 subjectInitialRotation = null;
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 subjectFinalRotation = null;
	
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 headInitialRotation = null;
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 headFinalRotation = null;
	
	
	public LookAtAnimation(edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel subject, org.alice.apis.moveandturn.Transformable target, boolean onlyAffectYaw, double duration) {
		super( duration );
		this.subject = subject;
		this.target = target;
		this.onlyAffectYaw = onlyAffectYaw;			
	}
	
	@Override
	protected void prologue() {
		
		head = getHead( subject );
		
		if (head != null) {
		
			//figure out how far we need to turn the whole character
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 targetMatrix = subject.calculatePointAt( target, new edu.cmu.cs.dennisc.math.Point3(0,0,0), new edu.cmu.cs.dennisc.math.Vector3(0,1,0) );
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 subjectMatrix = subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			
			edu.cmu.cs.dennisc.math.Vector3 targetForward = targetMatrix.createForward();
			edu.cmu.cs.dennisc.math.Vector3 subjectForward = subjectMatrix.createForward();
			
			double cosAngle = edu.cmu.cs.dennisc.math.Vector3.calculateDotProduct( subjectForward, targetForward ) / (targetForward.calculateMagnitude() * subjectForward.calculateMagnitude());
			cosAngle = java.lang.Math.acos(cosAngle);
			
			// convert to revolutions
			cosAngle /= (2 * java.lang.Math.PI);
			
			// if the character has to turn more than 1/4, then 
			if (cosAngle > 0.25) {
				turnAmount = cosAngle - 0.25;
				
				// set the direction to turn
				edu.cmu.cs.dennisc.math.Point3 targetPos = target.getPosition(subject);
				if (targetPos.x < 0) {
					m_direction = org.alice.apis.moveandturn.TurnDirection.LEFT;
				} else {
					m_direction = org.alice.apis.moveandturn.TurnDirection.LEFT;
				}
				
			} else {
				turnAmount = 0;
			}
			
			subjectInitialRotation = subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			subjectFinalRotation = subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundY( subjectInitialRotation, turnAmount );		
				
	//		calculate the rotation matrices for the head
			
			subject.setOrientationRightNow( subjectFinalRotation, org.alice.apis.moveandturn.AsSeenBy.SCENE );
			
			headInitialRotation = head.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
	
			headFinalRotation = head.calculatePointAt( target, getOffset(), new edu.cmu.cs.dennisc.math.Vector3(0,1,0) );
			
			subject.setOrientationRightNow( subjectInitialRotation, org.alice.apis.moveandturn.AsSeenBy.SCENE );
		} else {
			headInitialRotation = subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			
			headFinalRotation = subject.calculatePointAt( target, getOffset(), new edu.cmu.cs.dennisc.math.Vector3(0,1,0) );
		}
		
	}
	
	@Override
	protected void setPortion( double portion ) {
		
		if (head != null) {
			
			if (turnAmount > 0) {			
				UnitQuaternion currentQuat = UnitQuaternion.createInterpolation(subjectInitialRotation.createUnitQuaternion(), subjectFinalRotation.createUnitQuaternion(), portion);
				subject.setOrientationRightNow( currentQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE );
			}
			
			UnitQuaternion currentHeadQuat = UnitQuaternion.createInterpolation(headInitialRotation.createUnitQuaternion(), headFinalRotation.createUnitQuaternion(), portion);
			head.setOrientationRightNow( currentHeadQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE );
		} else {
			UnitQuaternion currentSubjectQuat = UnitQuaternion.createInterpolation(headInitialRotation.createUnitQuaternion(), headFinalRotation.createUnitQuaternion(), portion);
			subject.setOrientationRightNow( currentSubjectQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE );
		}
	}
	
	@Override
	protected void epilogue() {
	}
	
	protected boolean onlyAffectYaw() {
		org.alice.apis.moveandturn.ReferenceFrame head = getHead(subject);
		if (head != null) return false;
		else return true;
	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getOffset() {
		
		org.alice.apis.moveandturn.Transformable head = getHead(target);
		if ((head != null) && (head instanceof edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel) ) {
			return ((edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel)head).getAxisAlignedMinimumBoundingBox(target).getCenter();
		}
		else if (target instanceof edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel) return ((edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel)target).getAxisAlignedMinimumBoundingBox().getCenter();
		else return new edu.cmu.cs.dennisc.math.Point3(0,0,0);
	}
	
	private edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel getHead(org.alice.apis.moveandturn.Transformable subject) {
		edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel modelHead = subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion(
				"head", false ) );
		
		return modelHead;
	}
}
