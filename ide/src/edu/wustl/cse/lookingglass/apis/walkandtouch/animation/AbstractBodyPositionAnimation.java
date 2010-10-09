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
/*
 * Created on Feb 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.animation;

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.HowMuch;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.wustl.cse.lookingglass.apis.walkandtouch.Person;
/**
 * @author caitlin
 */
public abstract class AbstractBodyPositionAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
//	protected edu.wustl.cse.ckelleher.apis.walkandtouch.Person subject = null;

	//		legs
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel rightUpper = null;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel rightLower = null;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel rightFoot = null;

	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel leftUpper = null;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel leftLower = null;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel leftFoot = null;

	//		arms
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel rightUpperArm = null;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel rightLowerArm = null;

	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel leftUpperArm = null;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel leftLowerArm = null;

	//leg orientations
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightUpperInitialOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightLowerInitialOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightFootInitialOrient = null;

	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftUpperInitialOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftLowerInitialOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftFootInitialOrient = null;

	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightUpperFinalOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightLowerFinalOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightFootFinalOrient = null;

	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftUpperFinalOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftLowerFinalOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftFootFinalOrient = null;

	//arm orientations
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightUpperArmInitialOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightLowerArmInitialOrient = null;

	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftUpperArmInitialOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftLowerArmInitialOrient = null;

	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightUpperArmFinalOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rightLowerArmFinalOrient = null;

	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftUpperArmFinalOrient = null;
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 leftLowerArmFinalOrient = null;

	protected Person m_subject = null;

	public AbstractBodyPositionAnimation( Person subject, double duration ) {
		this.m_subject = subject;
		this.setDuration( duration );
	}
	
	@Override
	protected void epilogue() {
		// TODO Auto-generated method stub
	}
	
	protected void adjustHeight(Point3 calculatedTargetPosition) {
		double distanceAboveGround = 0.0;
		if( m_subject != null ) {
			
			distanceAboveGround = m_subject.getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.AsSeenBy.SCENE ).getCenterOfBottomFace().y;
			
			Point3 currentPos = m_subject.getPosition(AsSeenBy.SCENE);
			calculatedTargetPosition.y = currentPos.y- distanceAboveGround;
			m_subject.setPositionRightNow( calculatedTargetPosition, org.alice.apis.moveandturn.AsSeenBy.SCENE );
		}
	}
	
	protected void adjustHeight() {
		double distanceAboveGround = 0.0;
		if( m_subject != null ) {
			distanceAboveGround = m_subject.getAxisAlignedMinimumBoundingBox( org.alice.apis.moveandturn.AsSeenBy.SCENE ).getCenterOfBottomFace().y;
			m_subject.moveRightNow( org.alice.apis.moveandturn.MoveDirection.DOWN, distanceAboveGround, org.alice.apis.moveandturn.AsSeenBy.SCENE );
		}
	}

	protected void setOrientation( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel part, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 initialOrient, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 finalOrient, double portion ) {
		
		if( part != null ) {
			assert (initialOrient != null);
			assert (finalOrient != null);
			UnitQuaternion interpQuat = UnitQuaternion.createInterpolation(initialOrient.createUnitQuaternion(), finalOrient.createUnitQuaternion(), portion);
			part.setOrientationRightNow( interpQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.PARENT );
		}
	}

	public edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel getPolygonalModelChild( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel parent ) {
		if( parent == null )
			return null;

		java.util.List< edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel > kids = parent.findAllMatches(HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class );

		//		if this leg has more than one part, we've got a problem
		if( kids.size() == 1 ) {
			return kids.get( 0 );
		} else
			return null;
	}

	public void setInitialOrientations() {

		if( rightUpper != null )
			rightUpperInitialOrient = rightUpper.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( rightLower != null )
			rightLowerInitialOrient = rightLower.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( rightFoot != null )
			rightFootInitialOrient = rightFoot.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );

		if( leftUpper != null )
			leftUpperInitialOrient = leftUpper.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( leftLower != null )
			leftLowerInitialOrient = leftLower.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( leftFoot != null )
			leftFootInitialOrient = leftFoot.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );

		if( rightUpperArm != null )
			rightUpperArmInitialOrient = rightUpperArm.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( rightLowerArm != null )
			rightLowerArmInitialOrient = rightLowerArm.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );

		if( leftUpperArm != null )
			leftUpperArmInitialOrient = leftUpperArm.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );
		if( leftLowerArm != null )
			leftLowerArmInitialOrient = leftLowerArm.getOrientationAsAxes( org.alice.apis.moveandturn.AsSeenBy.PARENT );
	}

	public void findArms() {
		Criterion<?> c = new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion("rightUpperArm", true );
		rightUpperArm = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion("rightUpperArm", true ) );
		rightLowerArm = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion(
				"rightLowerArm", true ) );
		leftUpperArm = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion(
				"leftUpperArm", true ) );
		leftLowerArm = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion(
				"leftLowerArm", true ) );
	}

	//		search model to find the legs
	public void findLegs() {
		rightUpper = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion(
				"rightUpperLeg", true ) );
		rightLower = m_subject.findFirstMatch(HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion(
				"rightLowerLeg", true ) );
		rightFoot = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion( "rightFoot",
				true ) );
		leftUpper = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion( "leftUpperLeg",
				true ) );
		leftLower = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion( "leftLowerLeg",
				true ) );
		leftFoot = m_subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class,
				new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion( "leftFoot", true ) );
	}

}
