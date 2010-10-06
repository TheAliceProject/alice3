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

import org.alice.apis.moveandturn.AngleInRevolutions;
import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.MoveDirection;
import org.alice.apis.moveandturn.ReferenceFrame;
import org.alice.apis.moveandturn.Transformable;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;
import edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel;
import edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart;


/**
 * @author caitlink
 */
public class TouchAnimation extends AbstractBodyPositionAnimation {
	protected PolygonalModel target;
	protected TouchPart limb;
	protected MoveDirection sideToTouch;
	protected double offset;
	
	protected double upperLimbLength = -1.0;
	protected double lowerLimbLength = -1.0;
	protected double limbAngle = 0;
	
	protected PolygonalModel upperLimb = null;
	protected PolygonalModel lowerLimb = null;
	
	protected UnitQuaternion lowerInitialQuat = null;
	protected UnitQuaternion upperInitialQuat = null;
	protected UnitQuaternion upperTargetQuat = null;
	protected UnitQuaternion lowerTargetQuat = null;
	
	protected Vector3 initialVector = null;
	protected Vector3 targetVector = null;
	
	public TouchAnimation( Person subject, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, TouchPart limb, MoveDirection direction, double offset, double duration ) {
		super(subject, duration);
		
		this.target = target;
		this.limb = limb;
		this.sideToTouch = direction;
		this.offset = offset;
	}
	
	@Override
	protected void prologue() {
		
		if ( (limb == TouchPart.LEFT_HAND) || (limb == TouchPart.RIGHT_HAND )) {
			this.findArms();
			if (limb == TouchPart.LEFT_HAND) {
				upperLimb = leftUpperArm;
				lowerLimb = leftLowerArm;
			} else {
				upperLimb = rightUpperArm;
				lowerLimb = rightLowerArm;					
			}
		} else {
			this.findLegs();
			if (limb == TouchPart.RIGHT_HAND) {
				upperLimb = leftUpper;
				lowerLimb = leftLower;
			} else {
				upperLimb = rightUpper;
				lowerLimb = rightLower;					
			}
		}
		
		upperInitialQuat = upperLimb.getOrientation(AsSeenBy.PARENT).createUnitQuaternion();
		lowerInitialQuat = null;
		if (lowerLimb != null) lowerInitialQuat = lowerLimb.getOrientation(AsSeenBy.PARENT).createUnitQuaternion();
		upperTargetQuat = null;
		lowerTargetQuat = null;
		
		limbAngle = 0.0;
		upperLimbLength = -1.0;
		lowerLimbLength = -1.0;
		
		initialVector = null;
		targetVector = null;
		
	}
	@Override
	protected void setPortion( double portion ) {
		if (portion <= 1.0) {
			if (lowerTargetQuat == null) {
				lowerTargetQuat = getLowerTargetUnitQuaternionD();		
			}
			if (upperTargetQuat == null) {
				upperTargetQuat = getUpperTargetUnitQuaternionD();
			}
		
			edu.cmu.cs.dennisc.math.UnitQuaternion q = edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( upperInitialQuat, upperTargetQuat, portion );
			q.normalize();
			upperLimb.setAxesOnlyAsSeenByParent(q.createOrthogonalMatrix3x3());
//			upperLimb.setOrientationAsSeenByParentRightNow(q.createOrthogonalMatrix3x3());
			
			
			if (lowerLimb != null) {				
				edu.cmu.cs.dennisc.math.UnitQuaternion r = edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( lowerInitialQuat, lowerTargetQuat, portion );
				r.normalize();
				
				lowerLimb.setAxesOnlyAsSeenByParent(r.createOrthogonalMatrix3x3());
//				lowerLimb.setOrientationAsSeenByParentRightNow(r.createOrthogonalMatrix3x3());
			}
		}
		
	}
	@Override
	protected void epilogue() {
		// TODO Auto-generated method stub
		
	}
	
	protected void setTargetUnitQuaternionDs() {
		// save upper and lower current transformations 
		AffineMatrix4x4 initialTrans = null;
		if (lowerLimb != null) initialTrans = lowerLimb.getLocalTransformation();
		AffineMatrix4x4 initialUpperTrans = upperLimb.getLocalTransformation();
		
		// set upper and lower limbs to their default orientations as a starting point.
		if (lowerLimb != null) {
			lowerLimb.setAxesOnlyAsSeenByParent(OrthogonalMatrix3x3.createIdentity());
//			lowerLimb.setOrientationAsSeenByParentRightNow(MatrixD3x3.createIdentity());
		}
		upperLimb.setAxesOnlyAsSeenByParent(OrthogonalMatrix3x3.createIdentity());
//		upperLimb.setOrientationAsSeenByParentRightNow(MatrixD3x3.createIdentity());
		
		// set initial data
		setArmLengths();
		setVectors();	
		
		// calculate rotation of lower limb
		double targetDistance = targetVector.calculateMagnitude();
		limbAngle = 0;
		
		// if the target is within reach figure out target angle between upper and lower arm to 
		// make the effective length of the arm right
		if (targetDistance < upperLimbLength + lowerLimbLength) {
		
			// calculate appropriate turn angle
			double cosAngle = ((targetDistance * targetDistance) - (upperLimbLength * upperLimbLength) - (lowerLimbLength * lowerLimbLength))/(-2.0 * upperLimbLength * lowerLimbLength);
			//System.out.println("cosAngle: " + cosAngle);
			if (cosAngle > 1.0) cosAngle = 1.0;
			limbAngle = (java.lang.Math.PI - java.lang.Math.acos(cosAngle))/ (2.0 * java.lang.Math.PI);		
			//System.out.println("limbAngle: " + limbAngle);
			
//				arms turn backwards, legs turn forwards so as not to break people
			// upper limbs turn 1/2 limbAngle so that effective arm is still along y axis
			if ( (limb == TouchPart.LEFT_HAND) || (limb == TouchPart.RIGHT_HAND) ) {
				if (lowerLimb != null) rotateAroundXAxis(lowerLimb, -limbAngle); //lowerLimb.turnRightNow(TurnDirection.BACKWARD, limbAngle);	
				rotateAroundXAxis(upperLimb, limbAngle/2.0); //upperLimb.turnRightNow(TurnDirection.FORWARD, (limbAngle / 2.0));
			} else {
				if (lowerLimb != null) rotateAroundXAxis(lowerLimb, limbAngle); //lowerLimb.turnRightNow(TurnDirection.FORWARD, limbAngle);
				rotateAroundXAxis(upperLimb, -limbAngle/2.0); //upperLimb.turnRightNow(TurnDirection.BACKWARD, (limbAngle / 2.0));
			}
		}
		
//			calculate rotation and rotate

		
		double angle = getAngle(initialVector, targetVector);
		Vector3 cross = new Vector3();
		Vector3.setReturnValueToCrossProduct(cross, initialVector, targetVector);
		cross.normalize();	
		//upperLimb.rotateRightNow(cross, angle /(2.0 * java.lang.Math.PI), (org.alice.apis.moveandturn.ReferenceFrame)upperLimb.getParent());
		rotateAroundArbitraryAxis(upperLimb, cross, angle / (2.0 * java.lang.Math.PI), AsSeenBy.PARENT);
		
		
		//nudge rotations to make arms look a tab more reasonable
		targetVector.normalize();
		double turnAmt = 0;

		if (limb == TouchPart.RIGHT_HAND) {
			
			if ((targetVector.x < 0) ) { 
				turnAmt = .25 + (.1 * -targetVector.x);
			}	
			if (-targetVector.z < .3) { 
				turnAmt += 1 * (.3 + targetVector.z);
			}
			
			if (turnAmt > 0) {
				//upperLimb.rotateRightNow(targetVector, turnAmt, (org.alice.apis.moveandturn.ReferenceFrame)upperLimb.getParent());
				rotateAroundArbitraryAxis(upperLimb, targetVector, -turnAmt, AsSeenBy.PARENT);
			}
		} else if (limb == TouchPart.LEFT_HAND) {
			if ((targetVector.x > 0) ) { turnAmt = .25 + (-.1 * targetVector.x);}	
			if (-targetVector.z < .5) { turnAmt += 1 * (.3 + targetVector.z);}
			if (Math.abs(turnAmt) > 0) {
				//upperLimb.rotateRightNow(targetVector, -1.0 * turnAmt, (org.alice.apis.moveandturn.ReferenceFrame)upperLimb.getParent());
				rotateAroundArbitraryAxis(upperLimb, targetVector, turnAmt, AsSeenBy.PARENT);
			}
		}
		
		// save target quaternions
		lowerTargetQuat = null;
		if (lowerLimb != null) lowerTargetQuat = lowerLimb.getOrientation(AsSeenBy.PARENT).createUnitQuaternion();
		upperTargetQuat = upperLimb.getOrientation(AsSeenBy.PARENT).createUnitQuaternion(); 
		
		// set limbs back to initial transformations
		if (lowerLimb != null) lowerLimb.setLocalTransformation( initialTrans );//owerLimb.setOrientationAsSeenByParentRightNow(lowerInitialQuat.createOrthogonalMatrix3x3());//lowerLimb.setTransformationRightNow(initialTrans, (org.alice.apis.moveandturn.ReferenceFrame)lowerLimb.getParent());
		upperLimb.setLocalTransformation( initialUpperTrans );//upperLimb.setOrientationAsSeenByParentRightNow(upperInitialQuat.createOrthogonalMatrix3x3()); //.setTransformationRightNow(initialUpperTrans, (org.alice.apis.moveandturn.ReferenceFrame)upperLimb.getParent());
		
	}
	
	protected UnitQuaternion getLowerTargetUnitQuaternionD() {
		if  ((lowerLimb != null) && (lowerTargetQuat == null)) {
			setTargetUnitQuaternionDs();
		}
		return lowerTargetQuat;
	}
	
	protected edu.cmu.cs.dennisc.math.UnitQuaternion getUpperTargetUnitQuaternionD() {		
		if (upperTargetQuat == null) {
			setTargetUnitQuaternionDs();
		}
		return upperTargetQuat;
	}
	
	protected void setArmLengths() {
		if ((upperLimb != null) && (lowerLimb != null)) {
			
			Point3 tmp = lowerLimb.getPosition(AsSeenBy.SELF);
			tmp.subtract(lowerLimb.getAxisAlignedMinimumBoundingBox(AsSeenBy.SELF, org.alice.apis.moveandturn.HowMuch.THIS_AND_DESCENDANT_PARTS).getCenterOfBottomFace());
			lowerLimbLength = tmp.calculateMagnitude();

			upperLimbLength = upperLimb.getPosition(upperLimb).y - lowerLimb.getPosition(upperLimb).y;
		} else {
			upperLimbLength = upperLimb.getPosition(upperLimb).y - upperLimb.getAxisAlignedMinimumBoundingBox().getCenterOfBottomFace().y;
			lowerLimbLength = 0.0;
		}
	}
	
	protected void setVectors() {
		Point3 rightUpperPos = upperLimb.getPosition(upperLimb);
		Point3 targetPos = getTargetPosition(); 
		targetPos.subtract(rightUpperPos); // targetVector
		
		targetVector = new Vector3( targetPos.x, targetPos.y, targetPos.z );
		
		Point3 currentDir = null;
		if (lowerLimb == null) {
			currentDir = upperLimb.getAxisAlignedMinimumBoundingBox().getCenterOfBottomFace(); 
		} else {
			currentDir = lowerLimb.getAxisAlignedMinimumBoundingBox().getCenterOfBottomFace();
			currentDir = lowerLimb.createOffsetStandIn(currentDir).getPosition(upperLimb);//lowerLimb.getPosition(currentDir, upperLimb);
		}

		initialVector = new Vector3(currentDir.x, currentDir.y, currentDir.z);
		
	}
	
	Point3 getTargetPosition() {	

		org.alice.apis.moveandturn.ReferenceFrame asb = null; //target; //m_asSeenBy; maybe?
		
		if (asb == null) asb = upperLimb;
		
		Point3 targPos = target.getAxisAlignedMinimumBoundingBox(asb).getCenter();
		Vector3 offsetDir = null;
		
		org.alice.apis.moveandturn.HowMuch howMuch = org.alice.apis.moveandturn.HowMuch.THIS_AND_DESCENDANT_PARTS;
		// if target is not a top-level object, just get the bounding box for that one part
		

//		if ( !(target.getOwner().equals(target.getScene() )) ){
//			howMuch = org.alice.apis.moveandturn.HowMuch.THIS_ONLY;
//		}
		
		//calculate offset from the center of the bounding box
		if (sideToTouch == MoveDirection.BACKWARD) {
			targPos = target.getAxisAlignedMinimumBoundingBox(target, howMuch).getCenterOfBackFace();
//			targPos = target.getAxisAlignedMinimumBoundingBox(howMuch, target).getCenterOfFrontFace();
			offsetDir = target.getOrientationAsAxes(asb).createForward();
			offsetDir.negate();
		} else if (sideToTouch == MoveDirection.UP) {
			targPos = target.getAxisAlignedMinimumBoundingBox(target, howMuch).getCenterOfTopFace();
			offsetDir = target.getOrientationAsAxes(asb).up;
		} else if (sideToTouch == MoveDirection.DOWN) {
			targPos = target.getAxisAlignedMinimumBoundingBox(target, howMuch).getCenterOfBottomFace();
			offsetDir = target.getOrientationAsAxes(asb).up;
			offsetDir.negate();
		} else if (sideToTouch == MoveDirection.LEFT) {
			targPos = target.getAxisAlignedMinimumBoundingBox(target, howMuch).getCenterOfLeftFace();
			offsetDir = target.getOrientationAsAxes(asb).right;
		} else if (sideToTouch == MoveDirection.RIGHT) {
			targPos = target.getAxisAlignedMinimumBoundingBox(target, howMuch).getCenterOfRightFace();
			offsetDir = target.getOrientationAsAxes(asb).right;
			offsetDir.negate();
		} else {
			targPos = target.getAxisAlignedMinimumBoundingBox(target, howMuch).getCenterOfFrontFace();
//			targPos = target.getAxisAlignedMinimumBoundingBox(howMuch, target).getCenterOfBackFace();
			offsetDir = target.getOrientationAsAxes(asb).createForward();
		}
		
//		targPos.z = -targPos.z; // z is reversed somehow.
		
		targPos = target.createOffsetStandIn(targPos).getPosition(upperLimb); //target.getPosition(targPos, upperLimb);
		
		if (offset != 0) {
			offsetDir.multiply(offset);
//			offsetDir.scale(offset);
			targPos.add(offsetDir);
		}

		
		return targPos;			
	}
	
	double getAngle(Vector3 a, Vector3 b) {	
		// calculate angle
		Vector3 c = new Vector3();
		Vector3.setReturnValueToSubtraction(c, a, b);
		double cLength = c.calculateMagnitude(); ;
		double aLength = a.calculateMagnitude();
		double bLength = b.calculateMagnitude(); 
		double cosC = ((cLength * cLength) - (aLength * aLength) - (bLength * bLength)) / (-2.0 * aLength * bLength);
		
		return java.lang.Math.acos(cosC);
	}
	
	// I assume I want rotation in radians
	public void rotateAroundXAxis(Transformable trans, double amount) {
		trans.getSGTransformable().applyRotationAboutXAxis(new AngleInRevolutions(-amount));
	}
	
	// I assume I want rotation in radians
	public void rotateAroundArbitraryAxis(Transformable trans, Vector3 axis, double amount, ReferenceFrame asSeenBy) {
		trans.getSGTransformable().applyRotationAboutArbitraryAxis(axis, new AngleInRevolutions(amount), asSeenBy.getSGReferenceFrame());
	}
	
}
