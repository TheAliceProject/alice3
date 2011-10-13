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

import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;


/**
 * @author caitlin
 */
public class KneelAnimation extends AbstractBodyPositionAnimation {
	boolean oneKnee = true;

	public KneelAnimation( Person subject, boolean oneKnee, double duration ) {
		super( subject, duration);
		this.oneKnee = oneKnee;
	}
	
	@Override
	public void prologue(  ) {		
		findLegs();
		setInitialOrientations();
		setFinalOrientations();
	}
	
	@Override
	protected void setPortion( double portion ) {
		
		setOrientation(rightUpper, rightUpperInitialOrient, rightUpperFinalOrient, portion);
		setOrientation(rightLower, rightLowerInitialOrient, rightLowerFinalOrient, portion);
		if (rightFoot != null) setOrientation(rightFoot, rightFootInitialOrient, rightFootFinalOrient, portion);
		
		setOrientation(leftUpper, leftUpperInitialOrient, leftUpperFinalOrient, portion);
		setOrientation(leftLower, leftLowerInitialOrient, leftLowerFinalOrient, portion);
		if (leftFoot != null) setOrientation(leftFoot, leftFootInitialOrient, leftFootFinalOrient, portion);
		
		adjustHeight();
	}
	
	
	public void setFinalOrientations() {
		rightUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		rightLowerFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightLowerFinalOrient, 0.5 * Math.PI);
		rightFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightFootFinalOrient, 0.5 * Math.PI);
		
		if (oneKnee) {
			double lengthSupportLeg = 0.0;
			
			if ( (rightLower == null) ) {
				leftUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX(leftUpperFinalOrient, 0.25*Math.PI );
				leftFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftFootFinalOrient, -0.25 * Math.PI);
				
				rightUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightUpperFinalOrient, -0.25*Math.PI);
				rightFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightFootFinalOrient, 0.25 * Math.PI);
				
			}
			
			if (( rightUpper != null) && (rightLower != null)) {
				edu.cmu.cs.dennisc.math.Point3 posLower = rightLower.getPosition(rightUpper);
				edu.cmu.cs.dennisc.math.AxisAlignedBox boxLower = rightLower.getAxisAlignedMinimumBoundingBox();
				
				lengthSupportLeg = Math.abs(posLower.y) + Math.abs(boxLower.getMinimum().z);
				
				double lengthLowerLeg =  0.0; 
				if (leftFoot != null) lengthLowerLeg = Math.abs(leftFoot.getPosition(leftLower).y) + Math.abs(leftFoot.getAxisAlignedMinimumBoundingBox().getMinimum().z);
				else lengthLowerLeg = Math.abs(leftLower.getAxisAlignedMinimumBoundingBox().getMinimum().y);
				
				double diff = lengthSupportLeg - lengthLowerLeg;
				double angle = Math.asin(Math.abs(diff)/Math.abs(posLower.y));
								
				if (lengthSupportLeg*2.0 < lengthLowerLeg) {
					leftUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					leftLowerFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftLowerFinalOrient, 0.25*Math.PI );
					leftFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftFootFinalOrient, -0.25 * Math.PI);
					
					rightUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					rightLowerFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightLowerFinalOrient, -0.25*Math.PI);
					rightFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightFootFinalOrient, 0.25 * Math.PI);
				} else if (diff < 0) {
					leftUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftUpperFinalOrient, -0.5*Math.PI - angle);
					leftLowerFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftLowerFinalOrient, 0.5 * Math.PI + angle);
					leftFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					
				} else {
					leftUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftUpperFinalOrient, -0.5*Math.PI + angle);
					leftLowerFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftLowerFinalOrient, 0.5 * Math.PI - angle);
					leftFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
					
				}
			}
		} else {	
			if ( (rightLower == null) ) {
				leftUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftUpperFinalOrient, 0.5 * Math.PI);
				leftFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftFootFinalOrient, 0.5 * Math.PI);

				rightUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightUpperFinalOrient, 0.5 * Math.PI);
				rightFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( rightFootFinalOrient, 0.5 * Math.PI);
				
			} else {
				leftUpperFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				leftLowerFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftLowerFinalOrient, 0.5 * Math.PI);
				leftFootFinalOrient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundX( leftFootFinalOrient, 0.5 * Math.PI);
			}
		}
	}
	

}
