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
import org.alice.apis.moveandturn.Composite;
import org.alice.apis.moveandturn.HowMuch;
import org.alice.apis.moveandturn.StandIn;

import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author caitlin
 */
public class BetterStandUpAnimation extends StraightenAnimation {
	boolean scootForward = true;
	
//	java.util.Vector bodyPartInitialOrientations = null;
//	java.util.Vector bodyParts = null;
	
	edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 normalOrientation = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
	
	protected edu.cmu.cs.dennisc.math.Point3 m_positionBegin;
	protected edu.cmu.cs.dennisc.math.Point3 m_positionEnd;
	
	protected OrthogonalMatrix3x3 m_orientationBegin;
	protected OrthogonalMatrix3x3 m_orientationEnd;
	
	public BetterStandUpAnimation(edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel subject, boolean scootForward, double duration) {
		super( subject, duration );
		this.scootForward = scootForward;	
	}
	
	@Override
	public void prologue() {
		super.prologue();
		
		m_positionBegin = subject.getPosition( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		m_orientationBegin = subject.getOrientation(AsSeenBy.SCENE);
		
	}
	
	@Override
	protected void setPortion( double portion ) {
		super.setPortion( portion );
		
		if (m_positionEnd == null) {
			m_positionEnd = getPositionEnd();
		}
		
		if (m_orientationEnd == null) {
			m_orientationEnd = getOrientationEnd();
		}
		
		// set body position
//		subject.setPositionRightNow( Point3.createInterpolation(m_positionBegin, m_positionEnd, portion), org.alice.apis.moveandturn.AsSeenBy.SCENE);
		
		// set current orientation for the whole body
		UnitQuaternion currentQuat = UnitQuaternion.createInterpolation(m_orientationBegin.createUnitQuaternion(), m_orientationEnd.createUnitQuaternion(), portion);
		subject.setOrientationRightNow( currentQuat.createOrthogonalMatrix3x3(), AsSeenBy.SCENE );
		
		this.adjustHeight(Point3.createInterpolation(m_positionBegin, m_positionEnd, portion));
		
	}

	
	@Override
	public void epilogue () {
		super.epilogue();		
		m_positionEnd = null;
	
	}
	
	protected OrthogonalMatrix3x3 getOrientationEnd() {

		StandIn standIn = subject.acquireStandIn(subject);
		standIn.orientToUpright(Composite.RIGHT_NOW);
		
		return standIn.getOrientation(AsSeenBy.SCENE);

	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getPositionEnd() {
		if (subject != null) {
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orient = subject.getOrientation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
			
			edu.cmu.cs.dennisc.math.Vector3 forward = orient.backward;
			forward.negate();
			
			double moveAmount = 0.0;
			
			edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel rightUpper = subject.findFirstMatch( HowMuch.DESCENDANT_PARTS_ONLY, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion(
					"rightUpperLeg", true ) );
			
			if( rightUpper != null) {
				moveAmount = rightUpper.getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF, HowMuch.THIS_AND_DESCENDANT_PARTS ).getHeight();
			}

			if (scootForward) {
				m_positionEnd = new edu.cmu.cs.dennisc.math.Point3(m_positionBegin.x + (forward.x * moveAmount), 0, m_positionBegin.z + (forward.z * moveAmount));
			} else {
				m_positionEnd = new edu.cmu.cs.dennisc.math.Point3(m_positionBegin.x, 0, m_positionBegin.z);
			}
			
			return m_positionEnd;
		} else {
			return m_positionBegin;
		}
	}

}
