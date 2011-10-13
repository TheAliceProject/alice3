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

import org.alice.apis.moveandturn.AsSeenBy;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;
import edu.wustl.cse.lookingglass.apis.walkandtouch.SitDirection;

/**
 * @author caitlin
 */
public class SitAnimation extends AbstractBodyPositionAnimation {
	edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel m_target = null;
	SitDirection m_side = SitDirection.FORWARD;
	
		
	private edu.cmu.cs.dennisc.math.Point3 m_positionBegin;
	private edu.cmu.cs.dennisc.math.Point3 m_positionEnd;
	
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 initialOrient;
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 finalOrient;
	
	public SitAnimation( Person subject, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SitDirection side, double duration ) {
		super( subject, duration);
		m_target = target;
		m_side = side;
	}
	
	@Override
	public void prologue(  ) {

		m_positionBegin = m_subject.getPosition(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		m_positionEnd = null;

		findLegs();
		setInitialOrientations();
		setFinalOrientations();
		
		initialOrient = m_subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		finalOrient = getTargetMatrix();
		
	}
	
	@Override
	protected void epilogue() {}
	
	@Override
	protected void setPortion( double portion ) {
		
		if( m_positionEnd==null ) {
			m_positionEnd = getPositionEnd();
		}
		edu.cmu.cs.dennisc.math.Point3 currentPos = Point3.createInterpolation( m_positionBegin, m_positionEnd, portion );
		m_subject.setPositionRightNow( currentPos, org.alice.apis.moveandturn.AsSeenBy.SCENE );

		setOrientation(rightUpper, rightUpperInitialOrient, rightUpperFinalOrient, portion);
		if ( !(m_target.getName().equals("ground")) )
			setOrientation(rightLower, rightLowerInitialOrient, rightLowerFinalOrient, portion);

		setOrientation(leftUpper, leftUpperInitialOrient, leftUpperFinalOrient, portion);
		if ( !(m_target.getName().equals("ground")) )
			setOrientation(leftLower, leftLowerInitialOrient, leftLowerFinalOrient, portion);
		
		UnitQuaternion currentQuat = UnitQuaternion.createInterpolation(initialOrient.createUnitQuaternion(), finalOrient.createUnitQuaternion(), portion);
		m_subject.setOrientationRightNow( currentQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE );

		adjustHeight(); 
	}
	
	
	@Override
	protected void adjustHeight() {	
		if ((m_target != null) && !(m_target.getName().equals("ground")) ){
			
		} else {
			super.adjustHeight();
		}
	}
	
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getTargetMatrix() {
		
		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orient = m_target.getOrientationAsAxes(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		
		if (m_side.equals(SitDirection.BACKWARD)) {
			edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundY( orient, Math.PI);				
		} else if (m_side.equals(SitDirection.LEFT)) {
			edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundY( orient, -Math.PI/2.0);
		} else if (m_side.equals(SitDirection.RIGHT)) {
			edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundY( orient, Math.PI/2.0);
		}
		
		if (m_target.getName().equals("ground")) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 currentTrans = m_subject.getTransformation(org.alice.apis.moveandturn.AsSeenBy.SCENE);
			m_subject.orientToUprightRightNow(org.alice.apis.moveandturn.AsSeenBy.SCENE);
			orient = m_subject.getOrientationAsAxes(org.alice.apis.moveandturn.AsSeenBy.SCENE);
			m_subject.setTransformationRightNow(currentTrans, org.alice.apis.moveandturn.AsSeenBy.SCENE);				
		}
//		return orient.getUnitQuaternionD();
		return orient;
	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getPositionEnd() {
		if (m_target != null) {

			edu.cmu.cs.dennisc.math.Point3 centerTopFace = m_target.getAxisAlignedMinimumBoundingBox(AsSeenBy.SELF, org.alice.apis.moveandturn.HowMuch.THIS_AND_DESCENDANT_PARTS).getCenterOfTopFace();
			edu.cmu.cs.dennisc.math.Point3 endPos = null;
			
			//			edu.cmu.cs.dennisc.math.PointD3[] forwardAndUp = m_target.getOrientationAsForwardAndUpGuide(org.alice.apis.moveandturn.AsSeenBy.SCENE);
			
			if ( ((leftUpper != null)  && (leftLower == null) ) || m_target.getName().equals("ground")){
				double xOffset = m_subject.getAxisAlignedMinimumBoundingBox(AsSeenBy.SELF, org.alice.apis.moveandturn.HowMuch.THIS_AND_DESCENDANT_PARTS).getCenter().x;
				
				double yOffset = leftUpper.getPosition(m_subject).y;
				double zStart = 0.0;
				
				double depthSeat = leftUpper.getAxisAlignedMinimumBoundingBox(leftUpper).getHeight() * 2.0 / 3.0;
								
				if ((m_side.equals(SitDirection.BACKWARD)) || (m_side.equals(SitDirection.FORWARD)) ) {
					
					if (depthSeat > m_target.getAxisAlignedMinimumBoundingBox(m_target).getDepth())
						depthSeat = m_target.getAxisAlignedMinimumBoundingBox(m_target).getDepth();
					
					if (m_side.equals(SitDirection.BACKWARD)) {
						zStart = m_target.getAxisAlignedMinimumBoundingBox(m_target).getCenterOfBackFace().z;
						depthSeat *= -1.0;
					} else {
						zStart = m_target.getAxisAlignedMinimumBoundingBox(m_target).getCenterOfFrontFace().z;
					}
					
					endPos = new edu.cmu.cs.dennisc.math.Point3(centerTopFace.x + xOffset, centerTopFace.y - yOffset, zStart + depthSeat );
					endPos = m_target.createOffsetStandIn(endPos).getPosition(AsSeenBy.SCENE);
						//m_target.getPosition(endPos, org.alice.apis.moveandturn.AsSeenBy.SCENE);
				} else if ((m_side.equals(SitDirection.LEFT)) || (m_side.equals(SitDirection.RIGHT)) ) {
					
					if (depthSeat > m_target.getAxisAlignedMinimumBoundingBox(m_target).getWidth())
						depthSeat = m_target.getAxisAlignedMinimumBoundingBox(m_target).getWidth();
											
					if (m_side.equals(SitDirection.RIGHT)) {
						zStart = m_target.getAxisAlignedMinimumBoundingBox(m_target).getCenterOfRightFace().x;
					} else {
						zStart = m_target.getAxisAlignedMinimumBoundingBox(m_target).getCenterOfLeftFace().x;
						depthSeat *= -1.0;
					}
				
					endPos = new edu.cmu.cs.dennisc.math.Point3(zStart - depthSeat, centerTopFace.y - yOffset, centerTopFace.z - xOffset );
					endPos = m_target.createOffsetStandIn(endPos).getPosition(AsSeenBy.SCENE);//m_target.getPosition(endPos, org.alice.apis.moveandturn.AsSeenBy.SCENE);
					
				}	
				if (m_target.getName().equals("ground")){
					endPos = m_subject.getPosition(org.alice.apis.moveandturn.AsSeenBy.SCENE);
					endPos.y -= depthSeat;
				}
				return endPos;			
			} else if ((leftUpper != null) && (leftLower != null)) {
				double depthSeat = leftUpper.getAxisAlignedMinimumBoundingBox(leftUpper, org.alice.apis.moveandturn.HowMuch.THIS_ONLY).getHeight() - leftLower.getAxisAlignedMinimumBoundingBox(leftLower, org.alice.apis.moveandturn.HowMuch.THIS_ONLY).getDepth()/2.0;
				depthSeat *= 2.0/3.0;
				
				double xOffset = m_subject.getAxisAlignedMinimumBoundingBox().getCenter().x;
				double yOffset = leftUpper.getPosition(m_subject).y;
				double zStart = 0.0;
				
				if ((m_side.equals(SitDirection.BACKWARD)) || (m_side.equals(SitDirection.FORWARD)) ) {
					
					
					if (depthSeat > m_target.getAxisAlignedMinimumBoundingBox(m_target).getDepth())
						depthSeat = m_target.getAxisAlignedMinimumBoundingBox(m_target).getDepth();
										
					if (m_side.equals(SitDirection.BACKWARD)) {
						zStart = m_target.getAxisAlignedMinimumBoundingBox(m_target).getCenterOfBackFace().z;
						depthSeat *= -1.0;
					} else {
						zStart = m_target.getAxisAlignedMinimumBoundingBox(m_target).getCenterOfFrontFace().z;
					}
					
					endPos = new edu.cmu.cs.dennisc.math.Point3(centerTopFace.x + xOffset, centerTopFace.y - yOffset, zStart + depthSeat );
					endPos = m_target.createOffsetStandIn(endPos).getPosition(AsSeenBy.SCENE);//m_target.getPosition(endPos, org.alice.apis.moveandturn.AsSeenBy.SCENE);
				} else if ((m_side.equals(SitDirection.LEFT)) || (m_side.equals(SitDirection.RIGHT)) ) {
					
					if (depthSeat > m_target.getAxisAlignedMinimumBoundingBox(m_target).getWidth())
						depthSeat = m_target.getAxisAlignedMinimumBoundingBox(m_target).getWidth();
											
					if (m_side.equals(SitDirection.RIGHT)) {
						zStart = m_target.getAxisAlignedMinimumBoundingBox(m_target).getCenterOfRightFace().x;
					} else {
						zStart = m_target.getAxisAlignedMinimumBoundingBox(m_target).getCenterOfLeftFace().x;
						depthSeat *= -1.0;
					}
					
					endPos = new edu.cmu.cs.dennisc.math.Point3(zStart - depthSeat, centerTopFace.y - yOffset, centerTopFace.z - xOffset );
					endPos = m_target.createOffsetStandIn(endPos).getPosition(AsSeenBy.SCENE); //m_target.getPosition(endPos, org.alice.apis.moveandturn.AsSeenBy.SCENE);
				}
				
				return endPos;
				
			} 
			return endPos;
		} else {
			return m_positionBegin;
		}
	}
	
	public void setFinalOrientations() {
		rightUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		leftUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		rightLowerFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		leftLowerFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		
		edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightUpperFinalOrient, -0.5 * Math.PI);
		edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftUpperFinalOrient, -0.5 * Math.PI);
		
		edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightLowerFinalOrient, 0.5 * Math.PI);
		edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftLowerFinalOrient, 0.5 * Math.PI);
	}


}
