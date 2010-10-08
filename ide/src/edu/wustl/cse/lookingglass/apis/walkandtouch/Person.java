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
package edu.wustl.cse.lookingglass.apis.walkandtouch;

import org.alice.apis.moveandturn.Model;
//import org.alice.apis.moveandturn.MoveDirection;

import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;
import edu.wustl.cse.lookingglass.apis.walkandtouch.animation.KeepTouchingAnimation;
import edu.wustl.cse.lookingglass.apis.walkandtouch.animation.TouchAnimation;

public class Person extends Character {
	public static final String LEFT_UPPER_LEG_PART_NAME = "leftUpperLeg";
	public static final String LEFT_LOWER_LEG_PART_NAME = "leftLowerLeg";
	public static final String LEFT_FOOT_PART_NAME = "leftFoot";
	public static final String RIGHT_UPPER_LEG_PART_NAME = "rightUpperLeg";
	public static final String RIGHT_LOWER_LEG_PART_NAME = "rightLowerLeg";
	public static final String RIGHT_FOOT_PART_NAME = "rightFoot";

	public static final String LEFT_UPPER_ARM_PART_NAME = "leftUpperArm";
	public static final String LEFT_LOWER_ARM_PART_NAME = "leftLowerArm";
	public static final String RIGHT_UPPER_ARM_PART_NAME = "rightUpperArm";
	public static final String RIGHT_LOWER_ARM_PART_NAME = "rightLowerArm";
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void keepTouching(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection direction, TouchPart touchpart, Number offset, Number duration) {
		perform( new KeepTouchingAnimation(this, target, touchpart, direction, offset.doubleValue(), duration.doubleValue()));
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void keepTouching(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection direction, TouchPart touchpart, Number offset) {
		keepTouching(target, direction, touchpart, offset, 1.0);
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void keepTouching(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection direction, TouchPart touchpart) {
		keepTouching(target, direction, touchpart, 0.1);
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void keepTouching(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection direction) {
		keepTouching(target, direction, TouchPart.RIGHT_HAND);
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void keepTouching(PolygonalModel target) {
		keepTouching(target, org.alice.apis.moveandturn.MoveDirection.FORWARD);
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void touch(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection direction, TouchPart touchpart, Number offset, Number duration) {
		perform( new TouchAnimation(this, target, touchpart, direction, offset.doubleValue(), duration.doubleValue()));
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void touch(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection direction, TouchPart touchpart, Number offset) {
		touch(target, direction, touchpart, offset, 1.0);
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void touch(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection direction, TouchPart touchpart) {
		touch(target, direction, touchpart, 0.1);
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void touch(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection direction) {
		assert direction != null;
		touch(target, direction, TouchPart.RIGHT_HAND);
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void touch(PolygonalModel target) {
		touch(target, org.alice.apis.moveandturn.MoveDirection.FORWARD);
	}
	
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void walk( Number distance, Number stepSpeed, Boolean swingArms, Amount stepAmount, Amount bounceAmount, Amount armSwingAmount ) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.WalkAnimation( this, distance.doubleValue(), stepSpeed.doubleValue(), swingArms, stepAmount, bounceAmount, armSwingAmount ) );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walk( Number distance, Number stepSpeed, Boolean swingArms, Amount stepAmount, Amount bounceAmount ) {
		walk( distance, stepSpeed, swingArms, stepAmount, bounceAmount, Amount.NORMAL );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walk( Number distance, Number stepSpeed, Boolean swingArms, Amount stepAmount ) {
		walk( distance, stepSpeed, swingArms, stepAmount, Amount.NORMAL );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walk( Number distance, Number stepSpeed, Boolean swingArms ) {
		walk( distance, stepSpeed, swingArms, Amount.NORMAL );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walk( Number distance, Number stepSpeed ) {
		walk( distance, stepSpeed, Boolean.TRUE );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walk( Number distance ) {
		walk( distance, 1.5 );
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void walkOffscreen( ExitDirection exitDirection, Number stepSpeed, Boolean swingArms, Amount stepAmount, Amount bounceAmount, Amount armSwingAmount ) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.WalkOffscreenAnimation( this, exitDirection, stepSpeed.doubleValue(), swingArms, stepAmount, bounceAmount, armSwingAmount ) );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkOffscreen( ExitDirection exitDirection, Number stepSpeed, Boolean swingArms, Amount stepAmount, Amount bounceAmount ) {
		walkOffscreen( exitDirection, stepSpeed, swingArms, stepAmount, bounceAmount, Amount.NORMAL );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkOffscreen( ExitDirection exitDirection, Number stepSpeed, Boolean swingArms, Amount stepAmount ) {
		walkOffscreen( exitDirection, stepSpeed, swingArms, stepAmount, Amount.NORMAL );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkOffscreen( ExitDirection exitDirection, Number stepSpeed, Boolean swingArms ) {
		walkOffscreen( exitDirection, stepSpeed, swingArms, Amount.NORMAL );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkOffscreen( ExitDirection exitDirection, Number stepSpeed ) {
		walkOffscreen( exitDirection, stepSpeed, Boolean.TRUE );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkOffscreen( ExitDirection exitDirection ) {
		walkOffscreen( exitDirection, 1.5 );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkOffscreen( ) {
		walkOffscreen( ExitDirection.RIGHT );
	}
	
	//public WalkToAnimation( Person subject, edu.wustl.cse.ckelleher.apis.walkandtouch.PolygonalModel target, double stepSpeed, boolean swingArms, Amount stepAmount, Amount bounceAmount, Amount armSwingAmount ) {
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void walkTo( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SpatialRelation spatialRelation, Number offset, Number stepSpeed, Boolean swingArms, Amount stepAmount, Amount bounceAmount, Amount armSwingAmount ) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.WalkToAnimation( this, target, spatialRelation, offset.doubleValue(), stepSpeed.doubleValue(), swingArms, stepAmount, bounceAmount, armSwingAmount ) );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkTo( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SpatialRelation spatialRelation, Number offset, Number stepSpeed, Boolean swingArms, Amount stepAmount, Amount bounceAmount) {
		walkTo(target, spatialRelation, offset, stepSpeed, swingArms, stepAmount, bounceAmount, Amount.NORMAL);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkTo( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SpatialRelation spatialRelation, Number offset, Number stepSpeed, Boolean swingArms, Amount stepAmount) {
		walkTo(target, spatialRelation, offset, stepSpeed, swingArms, stepAmount, Amount.NORMAL);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkTo( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SpatialRelation spatialRelation, Number offset, Number stepSpeed, Boolean swingArms) {
		walkTo(target, spatialRelation, offset, stepSpeed, swingArms, Amount.NORMAL);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkTo( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SpatialRelation spatialRelation, Number offset, Number stepSpeed) {
		walkTo(target, spatialRelation, offset, stepSpeed, Boolean.TRUE );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkTo( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SpatialRelation spatialRelation, Number offset) {
		walkTo(target, spatialRelation, offset, 1.5 );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkTo( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SpatialRelation spatialRelation) {
		walkTo(target, spatialRelation, 1.0 );
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void walkTo( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target) {
		walkTo(target, SpatialRelation.IN_FRONT_OF );
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void fallDown(FallDirection direction, Number duration) {
		perform(new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.FallDownAnimation(this, direction, duration.doubleValue()));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void fallDown(FallDirection direction) {
		fallDown(direction, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void fallDown() {
		fallDown(FallDirection.BACKWARD);
	}
	
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void kneel(boolean oneKnee, Number duration) {
		perform(new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.KneelAnimation(this, oneKnee, duration.doubleValue()));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void kneel(boolean oneKnee) {
		kneel(oneKnee, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void kneel() {
		kneel(true);
	}
	
	
	
//	public SitAnimation( Person subject, edu.wustl.cse.ckelleher.apis.walkandtouch.PolygonalModel target, org.alice.apis.moveandturn.MoveDirection side, double duration ) 
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void sitOn(PolygonalModel target, SitDirection side, Number duration) {
		perform(new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.SitAnimation(this, target, side, duration.doubleValue()));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void sitOn(PolygonalModel target, SitDirection side) {
		sitOn(target, side, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void sitOn(PolygonalModel target ) {
		sitOn(target, SitDirection.FORWARD);
	}
	
//	public LieDownAnimation(edu.wustl.cse.ckelleher.apis.walkandtouch.PolygonalModel subject, org.alice.apis.moveandturn.Transformable target, org.alice.apis.moveandturn.MoveDirection feetFaceDirection, double duration) {
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public void lieDownOn(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection feetFaceDirection, Number duration) {
		perform(new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.LieDownAnimation(this, target, feetFaceDirection, duration.doubleValue()));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void lieDownOn(PolygonalModel target, org.alice.apis.moveandturn.MoveDirection feetFaceDirection) {
		lieDownOn(target, feetFaceDirection, 1.0);
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void lieDownOn(PolygonalModel target ) {
		lieDownOn(target, org.alice.apis.moveandturn.MoveDirection.FORWARD);
	}
	
//	@MethodTemplate(visibility = Visibility.PRIME_TIME)
//	public void touch( Person other, TouchPart touchPart ) {
//		printBodyParts();
//		turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new edu.cmu.cs.dennisc.math.AngleInRevolutionsD( 1.0 ) );
//	}
//	@MethodTemplate(visibility = Visibility.CHAINED)
//	public void touch( Person other ) {
//		touch( other, TouchPart.RIGHT_HAND );
//	}

	// Finding Body Parts ___________________________________________________
	
	
	protected boolean isKneeJointPresent_AlreadyInvoked = false;
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
	public boolean isKneeJointPresent() {
		Model rightLowerLeg = null;
		if( isKneeJointPresent_AlreadyInvoked ) {
			
		} else {
			rightLowerLeg = getBodyPartNamed( RIGHT_LOWER_LEG_PART_NAME );
			isKneeJointPresent_AlreadyInvoked = true;
		}
		return rightLowerLeg != null;
	}
//
//	//todo: push to ReferenceFrame?
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public void applyTranslationInScene( double xDelta, double yDelta, double zDelta ) {
//		getSGTransformable().applyTranslation( xDelta, yDelta, zDelta, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//	}
//	
//	//todo: push to ReferenceFrame?
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public edu.cmu.cs.dennisc.math.Point3 getPositionInScene( edu.cmu.cs.dennisc.math.Point3 offset ) {
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 t = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( offset );
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getSGTransformable().getAbsoluteTransformation();
//		m.multiply( t );
//		return new edu.cmu.cs.dennisc.math.Point3( m.translation );
//	}
}
