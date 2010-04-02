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

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection;
import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;
import edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities;


/**
 * @author caitlink
 */
public class FallDownAnimation extends AbstractBodyPositionAnimation {
	private edu.cmu.cs.dennisc.math.Point3 m_positionBegin;
	private edu.cmu.cs.dennisc.math.Point3 m_positionEnd;
	
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 initialOrient = null;
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 finalOrient = null;
	
	private FallDirection m_side = null;
	
	public FallDownAnimation( Person subject, FallDirection fallDirection, double duration ) {
		super( subject, duration);
		this.m_side = fallDirection;
	}
	
	@Override
	protected void prologue() {
		m_positionBegin = m_subject.getPosition(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		m_positionEnd = null;
		
		findLegs();
		findArms();
		setInitialOrientations();
		setFinalPartOrientations();
		setSubjectFinalOrientation();
		
	}
	
	@Override
	protected void setPortion( double portion ) {

		if( m_positionEnd==null ) {
			m_positionEnd = getPositionEnd();
		}
		edu.cmu.cs.dennisc.math.Point3 currentPos = Point3.createInterpolation( m_positionBegin, m_positionEnd, portion );		
		m_subject.setPositionRightNow( currentPos, org.alice.apis.moveandturn.AsSeenBy.SCENE );
		
		setOrientation(rightUpperArm, rightUpperArmInitialOrient, rightUpperArmFinalOrient, portion);
		setOrientation(rightLowerArm, rightLowerArmInitialOrient, rightLowerArmFinalOrient, portion);
		setOrientation(leftUpperArm, leftUpperArmInitialOrient, leftUpperArmFinalOrient, portion);
		setOrientation(leftLowerArm, leftLowerArmInitialOrient, leftLowerArmFinalOrient, portion);
		
		UnitQuaternion interpQuat = UnitQuaternion.createInterpolation(initialOrient.createUnitQuaternion(), finalOrient.createUnitQuaternion(), portion);
		m_subject.setOrientationRightNow( interpQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE );
		
		this.adjustHeight();

	}

	@Override
	protected void epilogue() {}

	//ToDo: adjust for bounding box instance only
	protected edu.cmu.cs.dennisc.math.Point3 getPositionEnd() {
		edu.cmu.cs.dennisc.math.Point3 endPos = m_subject.getPosition(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		
		double pivotToGround = m_subject.getAxisAlignedMinimumBoundingBox(org.alice.apis.moveandturn.AsSeenBy.SCENE).getCenterOfBottomFace().y;
		double pivotToFront = 0.0;
		
		if (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.FORWARD) ) {
			pivotToFront = m_subject.getAxisAlignedMinimumBoundingBox().getCenterOfFrontFace().z;
			
			if ((leftUpper != null) && (leftUpper.getAxisAlignedMinimumBoundingBox().getMinimum() != null) ) {
				pivotToFront = leftUpper.getAxisAlignedMinimumBoundingBox().getCenterOfFrontFace().z;
			}
		} else if (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.BACKWARD) ) {
			pivotToFront = m_subject.getAxisAlignedMinimumBoundingBox().getCenterOfBackFace().z;
			
			if ((leftUpper != null) && (leftUpper.getAxisAlignedMinimumBoundingBox().getMinimum() != null) ) {
				pivotToFront = -1 * leftUpper.getAxisAlignedMinimumBoundingBox().getCenterOfBackFace().z;
			}
		} else if (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.LEFT) ) {
			pivotToFront =  -0.8 * m_subject.getAxisAlignedMinimumBoundingBox().getCenterOfLeftFace().x;			
		} else if (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.RIGHT) ) {
			pivotToFront =  -0.8 * m_subject.getAxisAlignedMinimumBoundingBox().getCenterOfLeftFace().x;			
		} 
		
		endPos.y = endPos.y - pivotToGround + (0.9 * pivotToFront);
		
		return endPos;
	}
	
	public void setSubjectFinalOrientation() {
		initialOrient = m_subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		
		m_subject.orientToUprightRightNow( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		finalOrient = m_subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		m_subject.setOrientationRightNow( initialOrient, org.alice.apis.moveandturn.AsSeenBy.SCENE );
		
		if (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.LEFT)) {
			RotationUtilities.rotateAroundZ( finalOrient, -0.5 * Math.PI);
		} else if (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.RIGHT)) {
			RotationUtilities.rotateAroundZ( finalOrient, 0.5 * Math.PI);
		} else if (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.FORWARD)) {
			RotationUtilities.rotateAroundX( finalOrient, 0.5 * Math.PI );
		} else if (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.BACKWARD)) {
			RotationUtilities.rotateAroundX( finalOrient, -0.5 * Math.PI );
		}
	}
	
	public void setFinalPartOrientations() {
		if ((m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.LEFT)) || (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.RIGHT)) ) {
			rightUpperArmFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3(); 
			RotationUtilities.rotateAroundX(rightUpperArmFinalOrient, Math.random() * -0.5 * Math.PI);
			
			leftUpperArmFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			RotationUtilities.rotateAroundX(leftUpperArmFinalOrient, Math.random() * -0.5 * Math.PI);
			
			rightLowerArmFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			RotationUtilities.rotateAroundX(rightLowerArmFinalOrient, Math.random() * -0.5 * Math.PI);
			
			leftLowerArmFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			RotationUtilities.rotateAroundX(leftLowerArmFinalOrient, Math.random() * -0.5 * Math.PI);
		} else if ((m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.FORWARD)) || (m_side.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.FallDirection.BACKWARD)) ) {
			rightUpperArmFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			RotationUtilities.rotateAroundZ(rightUpperArmFinalOrient, Math.random() * -0.5 * Math.PI);
			
			leftUpperArmFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			RotationUtilities.rotateAroundZ(leftUpperArmFinalOrient, Math.random() * 0.5 * Math.PI);
			
			rightLowerArmFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			RotationUtilities.rotateAroundZ(rightLowerArmFinalOrient, Math.random() * -0.5 * Math.PI);
			
			leftLowerArmFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			RotationUtilities.rotateAroundZ(leftLowerArmFinalOrient, Math.random() * 0.5 * Math.PI);
		}
		
	}
}
