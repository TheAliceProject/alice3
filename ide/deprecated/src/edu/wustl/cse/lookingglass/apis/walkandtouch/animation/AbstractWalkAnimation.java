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

import org.alice.apis.moveandturn.Model;

import edu.wustl.cse.lookingglass.apis.walkandtouch.*;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Caitlin Kelleher
 * 
 * TODO: tighten this all up. plenty o room for that.
 */
public abstract class AbstractWalkAnimation extends edu.cmu.cs.dennisc.animation.AbstractAnimation {
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.Person subject = null;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.Amount stepAmount = edu.wustl.cse.lookingglass.apis.walkandtouch.Amount.NORMAL;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.Amount bounceAmount = edu.wustl.cse.lookingglass.apis.walkandtouch.Amount.NORMAL;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.Amount armSwingAmount = edu.wustl.cse.lookingglass.apis.walkandtouch.Amount.NORMAL;
	protected boolean swingArms = true;
	protected double stepSpeed = 1.5;

	protected PolygonalModel rightUpper = null;
	protected PolygonalModel rightLower = null;
	protected PolygonalModel rightFoot = null;

	protected PolygonalModel leftUpper = null;
	protected PolygonalModel leftLower = null;
	protected PolygonalModel leftFoot = null;

	protected PolygonalModel rightUpperArm = null;
	protected PolygonalModel rightLowerArm = null;

	protected PolygonalModel leftUpperArm = null;
	protected PolygonalModel leftLowerArm = null;


	protected static final double normalContactAngle = .46;
	protected static final double normalBackRecoilAngle = 1.3;
	protected static final double normalFrontRecoilAngle = 0.5;

	protected double upperArmAngle = 0.3;
	protected double lowerArmAngle = 0.1;

	protected double portionContact = 1.0 / 3.0;
	protected double portionRecoil = 1.0 / 6.0;
	protected double portionPassing = 1.0 / 3.0;
	protected double portionHighPoint = 1.0 / 6.0;

	protected double contactAngle = 0.2450;

	protected double recoilBackLowerAngle = 0.6;
	protected double recoilFrontUpperAngle = 0.3;

	protected double passingFrontUpperAngle = 0;
	protected double passingFrontLowerAngle = 0;
	protected double passingFrontFootAngle = 0;

	protected double passingBackLowerAngle = 0.2;

	protected double highPointFrontUpperAngle = 0.2;
	protected double highPointBackUpperAngle = 0.7;
	protected double highPointBackLowerAngle = 0;

	protected double heightFromGround = 0.0;
	protected double initialBoundingBoxHeight = 0.0;

	protected boolean firstTimeContact = true;
	protected boolean firstTimeRecoil = true;
	protected boolean firstTimePassing = true;
	protected boolean firstTimeHighPoint = true;

	// leg Lengths
	protected double totalLength = 0.0;
	protected double upperLength = 0.0;
	protected double lowerLength = 0.0;
	protected double footLength = 0.0;
	protected double footHorizLength = 0.0;

	// leg initial orient

	protected OrthogonalMatrix3x3 rightUpperInitialOrient = null;
	protected OrthogonalMatrix3x3 rightLowerInitialOrient = null;
	protected OrthogonalMatrix3x3 rightFootInitialOrient = null;

	protected OrthogonalMatrix3x3 leftUpperInitialOrient = null;
	protected OrthogonalMatrix3x3 leftLowerInitialOrient = null;
	protected OrthogonalMatrix3x3 leftFootInitialOrient = null;

	// arm initial orient
	protected OrthogonalMatrix3x3 rightUpperArmInitialOrient = null;
	protected OrthogonalMatrix3x3 rightLowerArmInitialOrient = null;

	protected OrthogonalMatrix3x3 leftUpperArmInitialOrient = null;
	protected OrthogonalMatrix3x3 leftLowerArmInitialOrient = null;

	protected Point3 initialPos = null;

	protected OrthogonalMatrix3x3 defaultOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	// leg contact quaternions
	protected OrthogonalMatrix3x3 frontUpperContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontLowerContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontFootContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected OrthogonalMatrix3x3 backUpperContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backLowerContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backFootContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected Point3 contactPos = null;
	protected double distanceToMoveContact = 0.0;

	// leg recoil quaternions
	protected OrthogonalMatrix3x3 frontUpperRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontLowerRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontFootRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected OrthogonalMatrix3x3 backUpperRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backLowerRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backFootRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected Point3 recoilPos = null;
	protected double distanceToMoveRecoil = 0.0;

	// leg passing stuff
	protected OrthogonalMatrix3x3 frontUpperPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontLowerPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontFootPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected OrthogonalMatrix3x3 backUpperPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backLowerPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backFootPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected Point3 passingPos = null;
	protected double distanceToMovePassing = 0.0;

	// leg high point stuff
	protected OrthogonalMatrix3x3 frontUpperHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontLowerHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontFootHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected OrthogonalMatrix3x3 backUpperHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backLowerHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backFootHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected Point3 highPointPos = null;
	protected double distanceToMoveHighPoint = 0.0;

	// arm orients
	protected OrthogonalMatrix3x3 frontUpperArmOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 frontLowerArmOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected OrthogonalMatrix3x3 backUpperArmOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 backLowerArmOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	// current orientations
	protected OrthogonalMatrix3x3 rightUpperCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 rightLowerCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 rightFootCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected OrthogonalMatrix3x3 leftUpperCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 leftLowerCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 leftFootCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected OrthogonalMatrix3x3 rightUpperArmCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 rightLowerArmCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	protected OrthogonalMatrix3x3 leftUpperArmCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
	protected OrthogonalMatrix3x3 leftLowerArmCurrentOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

	public AbstractWalkAnimation( Person subject, double stepSpeed, boolean swingArms, Amount stepAmount, Amount bounceAmount, Amount armSwingAmount ) {
		this.subject = subject;
		this.stepAmount = stepAmount;
		this.bounceAmount = bounceAmount;
		this.armSwingAmount = armSwingAmount;
		this.swingArms = swingArms;
		this.stepSpeed = stepSpeed;
	}

	@Override
	public void prologue() {
		
		rightUpper = subject.getBodyPartNamed( Person.RIGHT_UPPER_LEG_PART_NAME );
		rightLower = subject.getBodyPartNamed( Person.RIGHT_LOWER_LEG_PART_NAME );
		rightFoot = subject.getBodyPartNamed( Person.RIGHT_FOOT_PART_NAME );

		leftUpper = subject.getBodyPartNamed( Person.LEFT_UPPER_LEG_PART_NAME );
		leftLower = subject.getBodyPartNamed( Person.LEFT_LOWER_LEG_PART_NAME );
		leftFoot = subject.getBodyPartNamed( Person.LEFT_FOOT_PART_NAME );

		rightUpperArm = subject.getBodyPartNamed( Person.RIGHT_UPPER_ARM_PART_NAME );
		rightLowerArm = subject.getBodyPartNamed( Person.RIGHT_LOWER_ARM_PART_NAME );

		leftUpperArm = subject.getBodyPartNamed( Person.LEFT_UPPER_ARM_PART_NAME );
		leftLowerArm = subject.getBodyPartNamed( Person.LEFT_LOWER_ARM_PART_NAME );

		resetData();

		recoilFrontUpperAngle = normalFrontRecoilAngle;

		if( armSwingAmount == Amount.HUGE ) {
			upperArmAngle = 0.8;
			lowerArmAngle = 1.2;
		} else if( armSwingAmount == Amount.BIG ) {
			upperArmAngle = 0.675;
			lowerArmAngle = 0.925;
		} else if( armSwingAmount == Amount.NORMAL ) {
			upperArmAngle = 0.55;
			lowerArmAngle = 0.65;
		} else if( armSwingAmount == Amount.LITTLE ) {
			upperArmAngle = 0.425;
			lowerArmAngle = 0.375;
		} else if( armSwingAmount == Amount.TINY ) {
			upperArmAngle = 0.3;
			lowerArmAngle = 0.1;
		}

		if( bounceAmount == Amount.HUGE ) {
			recoilFrontUpperAngle = 0.5;
			recoilBackLowerAngle = 2.0;
		} else if( bounceAmount == Amount.BIG ) {
			recoilFrontUpperAngle = 0.37;
			recoilBackLowerAngle = 1.625;
		} else if( bounceAmount == Amount.NORMAL ) {
			recoilFrontUpperAngle = 0.25;
			recoilBackLowerAngle = 1.25;
		} else if( bounceAmount == Amount.LITTLE ) {
			recoilFrontUpperAngle = 0.12;
			recoilBackLowerAngle = 0.875;
		} else if( bounceAmount == Amount.TINY ) {
			recoilFrontUpperAngle = 0.0;
			recoilBackLowerAngle = 0.5;
		}

		if( stepAmount == Amount.HUGE ) {
			contactAngle = normalContactAngle * 1.5;
		} else if( stepAmount == Amount.BIG ) {
			contactAngle = normalContactAngle * 1.25;
		} else if( stepAmount == Amount.NORMAL ) {
			contactAngle = normalContactAngle;
		} else if( stepAmount == Amount.LITTLE ) {
			contactAngle = normalContactAngle * 0.75;
		} else if( stepAmount == Amount.TINY ) {
			contactAngle = normalContactAngle * 0.5;
		}

		setLegLengths();
		setInitialOrientations();
		setContactData();
		setRecoilData();
		setPassingData();
		setHighPointData();
		setArmData();
	}

	// in the step methods, portion is the portion for the current step, not the
	// animation
	// as a whole.

	public void stepRight( double portion, boolean lastStep ) {
		step( rightUpper, portion, lastStep );
	}

	public void stepLeft( double portion, boolean lastStep ) {
		step( leftUpper, portion, lastStep );
	}

	protected void step( Model leg, double portion, boolean lastStep ) {
		// move arms...
		// System.out.println("update: " + portion);
		adjustHeight();

		if( swingArms ) {
			updateArms( leg, portion, lastStep );
		}

		if( portion < portionContact ) {
			if( firstTimeContact ) {
				firstTimeContact = false;
				firstTimeHighPoint = true;
				getCurrentOrientations();
			}
			portion = portion / portionContact;
			updateContact( leg, portion );
		} else if( portion < portionContact + portionRecoil ) {
			if( firstTimeRecoil ) {
				firstTimeRecoil = false;
				firstTimeContact = true;
				getCurrentOrientations();
			}
			portion = (portion - portionContact) / portionRecoil;
			if( subject.isKneeJointPresent() ) {
				updateRecoil( leg, portion );
			}
		} else if( portion < portionContact + portionRecoil + portionPassing ) {
			if( firstTimePassing ) {
				firstTimePassing = false;
				firstTimeRecoil = true;
				getCurrentOrientations();
			}
			portion = (portion - portionContact - portionRecoil) / portionPassing;
			updatePassing( leg, portion );
		} else {
			if( firstTimeHighPoint ) {
				firstTimeHighPoint = false;
				firstTimePassing = true;
				getCurrentOrientations();
			}
			portion = (portion - portionContact - portionRecoil - portionPassing) / portionHighPoint;
			updateHighPoint( leg, portion, lastStep );
		}

		adjustHeight();

	}

	protected void adjustHeight() {
		double distanceAboveGround = 0.0;
		
		if( (rightFoot != null) && (leftFoot != null) ) {
			double rightHeight = rightFoot.getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.AsSeenBy.SCENE ).getCenterOfBottomFace().y;
			double leftHeight = leftFoot.getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.AsSeenBy.SCENE).getCenterOfBottomFace().y;

			distanceAboveGround = java.lang.Math.min( rightHeight, leftHeight );
		} else {
			distanceAboveGround = subject.getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.AsSeenBy.SCENE ).getCenterOfBottomFace().y;
		}
		//subject.moveRightNow( org.alice.apis.moveandturn.MoveDirection.DOWN, distanceAboveGround, subject.getScene() );
		//subject.applyTranslationInScene( 0, -distanceAboveGround, 0 );
		subject.move( org.alice.apis.moveandturn.MoveDirection.DOWN, distanceAboveGround, org.alice.apis.moveandturn.Composite.RIGHT_NOW, org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}

	public void getCurrentOrientations() {
		if( rightUpper != null )

			rightUpperCurrentOrient = rightUpper.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( rightLower != null )
			rightLowerCurrentOrient = rightLower.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( rightFoot != null )
			rightFootCurrentOrient = rightFoot.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );

		if( leftUpper != null )
			leftUpperCurrentOrient = leftUpper.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( leftLower != null )
			leftLowerCurrentOrient = leftLower.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( leftFoot != null )
			leftFootCurrentOrient = leftFoot.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );

		if( rightUpperArm != null )
			rightUpperArmCurrentOrient = rightUpperArm.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( rightLowerArm != null )
			rightLowerArmCurrentOrient = rightLowerArm.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );

		if( leftUpperArm != null )
			leftUpperArmCurrentOrient = leftUpperArm.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( leftLowerArm != null )
			leftLowerArmCurrentOrient = leftLowerArm.getOrientation( org.alice.apis.moveandturn.AsSeenBy.PARENT );

	}

	public void resetData() {
		contactAngle = 0.245;

		recoilBackLowerAngle = 0.2;
		recoilFrontUpperAngle = 0.4;

		passingFrontUpperAngle = 0;
		passingFrontLowerAngle = 0;
		passingFrontFootAngle = 0;

		passingBackLowerAngle = 0.2;

		highPointFrontUpperAngle = 0.2;
		highPointBackUpperAngle = 0.7;
		highPointBackLowerAngle = 0;

		frontUpperContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		frontLowerContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		frontFootContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

		backUpperContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		backLowerContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		backFootContactOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

		contactPos = null;
		distanceToMoveContact = 0.0;

		// leg recoil quaternions
		frontUpperRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		frontLowerRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		frontFootRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

		backUpperRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		backLowerRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		backFootRecoilOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

		recoilPos = null;
		distanceToMoveRecoil = 0.0;

		// leg passing stuff
		frontUpperPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		frontLowerPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		frontFootPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

		backUpperPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		backLowerPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		backFootPassingOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

		passingPos = null;
		distanceToMovePassing = 0.0;

		// leg high point stuff
		frontUpperHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		frontLowerHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		frontFootHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

		backUpperHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		backLowerHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();
		backFootHighPointOrient = OrthogonalMatrix3x3.createIdentity(); //new MatrixD3x3();

		highPointPos = null;
		distanceToMoveHighPoint = 0.0;
	}

	// assuming that legs are the same length
	public void setLegLengths() {
		Point3 top = new Point3();
		Point3 bottom = new Point3();

		footLength = 0.0;
		footHorizLength = 0.0;

		if( rightFoot != null ) {
			top = rightFoot.getPosition( rightFoot );
			bottom = rightFoot.getAxisAlignedMinimumBoundingBox().getCenterOfBottomFace();

			footLength = top.y - bottom.y;
			footHorizLength = bottom.z - top.z;
		}

		lowerLength = 0.0;

		if( rightLower != null ) {
			// this may want to be full distance (assuming that x,z offsets are
			// really small and unimportant)
			top = rightLower.getPosition( rightLower );
			if( rightFoot != null )
				bottom = rightFoot.getPosition( rightLower );
			else
				bottom = rightLower.getAxisAlignedMinimumBoundingBox().getCenterOfBottomFace();
			lowerLength = top.y - bottom.y;
		}

		upperLength = 0.0;

		if( rightUpper != null ) {
			top = rightUpper.getPosition( rightUpper );

			if( rightLower != null )
				bottom = rightLower.getPosition( rightUpper ); //rightUpper
			else if( rightFoot != null )
				bottom = rightFoot.getPosition( rightUpper ); // rightLower
			else
				bottom = rightUpper.getAxisAlignedMinimumBoundingBox().getCenterOfBottomFace();

			upperLength = top.y - bottom.y;
		}

		totalLength = footLength + lowerLength + upperLength;
	}

	public double getStepLength() {
		double stepLength = totalLength * java.lang.Math.sin( contactAngle ) * 1.5;
		if( stepLength == 0.0 )
			stepLength = 1.0;
		return stepLength;
	}

	public void setInitialOrientations() {
		if( rightUpper != null )
			rightUpperInitialOrient = rightUpper.getOrientation( rightUpper );
		if( rightLower != null )
			rightLowerInitialOrient = rightLower.getOrientation( rightLower );
		if( rightFoot != null )
			rightFootInitialOrient = rightFoot.getOrientation( rightFoot );

		if( leftUpper != null )
			leftUpperInitialOrient = leftUpper.getOrientation( leftUpper );
		if( leftLower != null )
			leftLowerInitialOrient = leftLower.getOrientation( leftLower );
		if( leftFoot != null )
			leftFootInitialOrient = leftFoot.getOrientation( leftFoot );

		if( rightUpperArm != null )
			rightUpperArmInitialOrient = rightUpperArm.getOrientation( rightUpperArm );
		if( rightLowerArm != null )
			rightLowerArmInitialOrient = rightLowerArm.getOrientation( rightLowerArm );

		if( leftUpperArm != null )
			leftUpperArmInitialOrient = leftUpperArm.getOrientation( leftUpperArm );
		if( leftLowerArm != null )
			leftLowerArmInitialOrient = leftLowerArm.getOrientation( leftLowerArm );

		if( (rightUpper != null) && (leftUpper != null) ) {

			Point3 top = rightUpper.getPosition( rightUpper );
			assert (rightUpper.getAxisAlignedMinimumBoundingBox() != null);
			Point3 bottom = rightUpper.getAxisAlignedMinimumBoundingBox().getCenterOfBottomFace();
			double offset = (top.y - bottom.y) - totalLength;

			top = leftUpper.getPosition( leftUpper );
			bottom = leftUpper.getAxisAlignedMinimumBoundingBox().getCenterOfBottomFace();
			double offset2 = (top.y - bottom.y) - totalLength;

			if( offset2 > offset )
				offset = offset2;

			initialPos = subject.getPositionInScene( new Point3( 0, offset, 0 ) );
			heightFromGround = initialPos.y;
			initialBoundingBoxHeight = getCurrentLegHeight();
		}
	}

	public double getCurrentLegHeight() {
		if( rightUpper != null ) {
			rightUpper.getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.AsSeenBy.SCENE/*, HowMuch.INSTANCE*/ );

			double boundingBoxHeight = rightUpper.getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.AsSeenBy.SCENE ).getHeight();
			double boundingBoxHeight2 = leftUpper.getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.AsSeenBy.SCENE ).getHeight();
			if( boundingBoxHeight2 > boundingBoxHeight )
				boundingBoxHeight = boundingBoxHeight2;

			return boundingBoxHeight;
		} else
			return 0.0;
	}

	public void setContactData() {
		double rotationLower = 0.0;
		double rotationUpper = 0.0;

		if( (leftLower == null) || (rightLower == null) ) {
			rotationUpper = contactAngle;
		} else {

			double lowerLegEffectiveLength = java.lang.Math.sqrt( footHorizLength * footHorizLength + (lowerLength + footLength) * (lowerLength + footLength) );
			double kneeAngle = (totalLength * totalLength - upperLength * upperLength - lowerLegEffectiveLength * lowerLegEffectiveLength) / (-2.0 * upperLength * lowerLegEffectiveLength);

			kneeAngle = java.lang.Math.acos( kneeAngle );

			rotationLower = (java.lang.Math.PI - kneeAngle) + java.lang.Math.atan( footHorizLength / (footLength + lowerLength) );
			rotationUpper = contactAngle - java.lang.Math.asin( (lowerLegEffectiveLength * java.lang.Math.sin( kneeAngle )) / totalLength );

			recoilBackLowerAngle += rotationLower;
			recoilFrontUpperAngle += contactAngle;

			passingFrontUpperAngle = recoilFrontUpperAngle;
			passingFrontLowerAngle = recoilFrontUpperAngle + 0.2;
			passingFrontFootAngle = 0.2;
			passingBackLowerAngle += recoilBackLowerAngle;

			highPointBackLowerAngle = passingBackLowerAngle / 2.0;
		}
		
		RotationUtilities.rotateAroundX(frontUpperContactOrient, -1.0 * contactAngle);
		RotationUtilities.rotateAroundX(backUpperContactOrient, rotationUpper);
		RotationUtilities.rotateAroundX(backLowerContactOrient, rotationLower);

		distanceToMoveContact = totalLength - (totalLength * java.lang.Math.cos( contactAngle ));
		contactPos = subject.getPositionInScene( new Point3( 0, -1.0 * distanceToMoveContact, 0 ) );
	}

	public void setRecoilData() {
		RotationUtilities.rotateAroundX(frontUpperRecoilOrient, -1.0 * recoilFrontUpperAngle);
		RotationUtilities.rotateAroundX(frontLowerRecoilOrient, recoilFrontUpperAngle);
		RotationUtilities.rotateAroundX(backLowerRecoilOrient, recoilBackLowerAngle);

		double distance = upperLength - (upperLength * java.lang.Math.cos( passingFrontUpperAngle )) + lowerLength - (lowerLength * java.lang.Math.cos( passingFrontLowerAngle - passingFrontUpperAngle ));
		recoilPos = subject.getPositionInScene( new Point3( 0, -1.0 * distance, 0 ) );
	}

	public void setPassingData() {
		RotationUtilities.rotateAroundX(frontUpperPassingOrient, -1.0 * passingFrontUpperAngle);
		RotationUtilities.rotateAroundX(frontLowerPassingOrient, passingFrontLowerAngle);
		RotationUtilities.rotateAroundX(frontFootPassingOrient, -1.0 * passingFrontUpperAngle);

		RotationUtilities.rotateAroundX(backUpperPassingOrient, -1.0 * passingFrontUpperAngle);
		RotationUtilities.rotateAroundX(backLowerPassingOrient, passingBackLowerAngle);

		double distance = upperLength - (upperLength * java.lang.Math.cos( recoilFrontUpperAngle ));
		passingPos = subject.getPositionInScene( new Point3( 0, -1.0 * distance, 0 ) );

	}

	public void setHighPointData() {
		RotationUtilities.rotateAroundX(frontUpperHighPointOrient, highPointFrontUpperAngle);

		RotationUtilities.rotateAroundX(backUpperHighPointOrient, -1.0 * highPointBackUpperAngle);
		RotationUtilities.rotateAroundX(backLowerHighPointOrient, highPointBackLowerAngle);

		double distance = totalLength - (totalLength * java.lang.Math.cos( highPointFrontUpperAngle ));
		highPointPos = subject.getPositionInScene( new Point3( 0, -1.0 * distance, 0 ) );
	}

	public void setArmData() {
		RotationUtilities.rotateAroundX(frontUpperArmOrient, -1.0 * upperArmAngle);
		RotationUtilities.rotateAroundX(frontLowerArmOrient, -1.0 * lowerArmAngle);

		RotationUtilities.rotateAroundX(backUpperArmOrient, 2.0 * upperArmAngle);
	}


	public void updateContact( Model leg, double portion ) {
		if( portion <= 1.0 ) {
			if( leg == null ) {
			} else if( leg.equals( rightUpper ) ) {
				setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, frontUpperContactOrient, portion );
				setUnitQuaternionD( rightLower, rightLowerCurrentOrient, frontLowerContactOrient, portion );

				setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, backUpperContactOrient, portion );
				setUnitQuaternionD( leftLower, leftLowerCurrentOrient, backLowerContactOrient, portion );
			} else {
				setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, frontUpperContactOrient, portion );
				setUnitQuaternionD( leftLower, leftLowerCurrentOrient, frontLowerContactOrient, portion );

				setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, backUpperContactOrient, portion );
				setUnitQuaternionD( rightLower, rightLowerCurrentOrient, backLowerContactOrient, portion );
			}
		}
	}

	public void updateRecoil( Model leg, double portion ) {
		if( leg == null ) {
		} else if( portion <= 1.0 ) {
			if( leg.equals( rightUpper ) ) {
				setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, frontUpperRecoilOrient, portion );
				setUnitQuaternionD( rightLower, rightLowerCurrentOrient, frontLowerRecoilOrient, portion );

				setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, backUpperRecoilOrient, portion );
				setUnitQuaternionD( leftLower, leftLowerCurrentOrient, backLowerRecoilOrient, portion );
			} else {
				setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, frontUpperRecoilOrient, portion );
				setUnitQuaternionD( leftLower, leftLowerCurrentOrient, frontLowerRecoilOrient, portion );

				setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, backUpperRecoilOrient, portion );
				setUnitQuaternionD( rightLower, rightLowerCurrentOrient, backLowerRecoilOrient, portion );
			}

		}
	}

	public void updatePassing( Model leg, double portion ) {
		if( leg == null ) {
		} else if( portion <= 1.0 ) {
			if( leg.equals( rightUpper ) ) {
				setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, frontUpperPassingOrient, portion );
				setUnitQuaternionD( rightLower, rightLowerCurrentOrient, frontLowerPassingOrient, portion );
				setUnitQuaternionD( rightFoot, rightFootCurrentOrient, frontFootPassingOrient, portion );

				setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, backUpperPassingOrient, portion );
				setUnitQuaternionD( leftLower, leftLowerCurrentOrient, backLowerPassingOrient, portion );
				setUnitQuaternionD( leftFoot, leftFootCurrentOrient, backFootPassingOrient, portion );
			} else {
				setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, frontUpperPassingOrient, portion );
				setUnitQuaternionD( leftLower, leftLowerCurrentOrient, frontLowerPassingOrient, portion );
				setUnitQuaternionD( leftFoot, leftFootCurrentOrient, frontFootPassingOrient, portion );

				setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, backUpperPassingOrient, portion );
				setUnitQuaternionD( rightLower, rightLowerCurrentOrient, backLowerPassingOrient, portion );
				setUnitQuaternionD( rightFoot, rightFootCurrentOrient, backFootPassingOrient, portion );
			}
		}
	}

	public void updateHighPoint( Model leg, double portion, boolean lastStep ) {
		if( leg == null ) {
		} else if( portion <= 1.0 ) {
			if( lastStep ) {
				if( leg.equals( rightUpper ) ) {
					setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, defaultOrient, portion );
					setUnitQuaternionD( rightLower, rightLowerCurrentOrient, defaultOrient, portion );
					setUnitQuaternionD( rightFoot, rightFootCurrentOrient, defaultOrient, portion );

					setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, defaultOrient, portion );
					setUnitQuaternionD( leftLower, leftLowerCurrentOrient, defaultOrient, portion );
				} else {
					setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, defaultOrient, portion );
					setUnitQuaternionD( leftLower, leftLowerCurrentOrient, defaultOrient, portion );
					setUnitQuaternionD( leftFoot, leftFootCurrentOrient, defaultOrient, portion );

					setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, defaultOrient, portion );
					setUnitQuaternionD( rightLower, rightLowerCurrentOrient, defaultOrient, portion );
				}
			} else {

				if( leg.equals( rightUpper ) ) {
					setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, frontUpperHighPointOrient, portion );
					setUnitQuaternionD( rightLower, rightLowerCurrentOrient, frontLowerHighPointOrient, portion );
					setUnitQuaternionD( rightFoot, rightFootCurrentOrient, frontFootHighPointOrient, portion );

					setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, backUpperHighPointOrient, portion );
					setUnitQuaternionD( leftLower, leftLowerCurrentOrient, backLowerHighPointOrient, portion );
				} else {
					setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, frontUpperHighPointOrient, portion );
					setUnitQuaternionD( leftLower, leftLowerCurrentOrient, frontLowerHighPointOrient, portion );
					setUnitQuaternionD( leftFoot, leftFootCurrentOrient, frontFootHighPointOrient, portion );

					setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, backUpperHighPointOrient, portion );
					setUnitQuaternionD( rightLower, rightLowerCurrentOrient, backLowerHighPointOrient, portion );
				}
			}

		}
	}

	public void updateArms( Model leg, double portion, boolean lastStep ) {
		if( lastStep && (leg != null) ) {
			setUnitQuaternionD( leftUpperArm, leftUpperArmCurrentOrient, defaultOrient, portion );
			setUnitQuaternionD( leftLowerArm, leftLowerArmCurrentOrient, defaultOrient, portion );

			setUnitQuaternionD( rightUpperArm, rightUpperArmCurrentOrient, defaultOrient, portion );
			setUnitQuaternionD( rightLowerArm, rightLowerArmCurrentOrient, defaultOrient, portion );
		} else {
			if( leg == null ) {
			} else if( leg.equals( leftUpper ) ) {
				setUnitQuaternionD( rightUpperArm, rightUpperArmCurrentOrient, frontUpperArmOrient, portion );
				setUnitQuaternionD( rightLowerArm, rightLowerArmCurrentOrient, frontLowerArmOrient, portion );

				setUnitQuaternionD( leftUpperArm, leftUpperArmCurrentOrient, backUpperArmOrient, portion );
				setUnitQuaternionD( leftLowerArm, leftLowerArmCurrentOrient, backLowerArmOrient, portion );
			} else {
				setUnitQuaternionD( leftUpperArm, leftUpperArmCurrentOrient, frontUpperArmOrient, portion );
				setUnitQuaternionD( leftLowerArm, leftLowerArmCurrentOrient, frontLowerArmOrient, portion );

				setUnitQuaternionD( rightUpperArm, rightUpperArmCurrentOrient, backUpperArmOrient, portion );
				setUnitQuaternionD( rightLowerArm, rightLowerArmCurrentOrient, backLowerArmOrient, portion );
			}
		}
	}

	@Override
	protected void preEpilogue() {
	}
	
	@Override
	protected void epilogue() {
		if( leftUpper != null ) {
			if( swingArms ) {
				setUnitQuaternionD( leftUpperArm, leftUpperArmCurrentOrient, defaultOrient, 1.0 );
				setUnitQuaternionD( leftLowerArm, leftLowerArmCurrentOrient, defaultOrient, 1.0 );

				setUnitQuaternionD( rightUpperArm, rightUpperArmCurrentOrient, defaultOrient, 1.0 );
				setUnitQuaternionD( rightLowerArm, rightLowerArmCurrentOrient, defaultOrient, 1.0 );
			}

			setUnitQuaternionD( rightUpper, rightUpperCurrentOrient, defaultOrient, 1.0 );
			setUnitQuaternionD( rightLower, rightLowerCurrentOrient, defaultOrient, 1.0 );
			setUnitQuaternionD( rightFoot, rightFootCurrentOrient, defaultOrient, 1.0 );

			setUnitQuaternionD( leftUpper, leftUpperCurrentOrient, defaultOrient, 1.0 );
			setUnitQuaternionD( leftLower, leftLowerCurrentOrient, defaultOrient, 1.0 );
			setUnitQuaternionD( leftFoot, leftFootCurrentOrient, defaultOrient, 1.0 );
		}

		adjustHeight();

	}

	private void setUnitQuaternionD( PolygonalModel part, OrthogonalMatrix3x3 initialOrient, OrthogonalMatrix3x3 finalOrient, double portion ) {
		//todo: add style
		//double positionPortion = m_style.getPortion( portion, 1 );
		double positionPortion = portion;
		
		UnitQuaternion currentOrient = UnitQuaternion.createInterpolation( new UnitQuaternion( initialOrient ), new UnitQuaternion( finalOrient ), positionPortion );

		if( part != null ) {
			
			// this will get fixed in the "rightnow" pass
			part.setAxesOnlyAsSeenByParent(new OrthogonalMatrix3x3(currentOrient));
//			part.setOrientationAsSeenByParentRightNow( new MatrixD3x3( currentOrient ) );

		}
	}
}
