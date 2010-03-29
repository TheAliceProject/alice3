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
import org.alice.apis.moveandturn.Model;

import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author caitlin
 */
public class CharacterViewAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {//AbstractPositionAnimation {
	edu.wustl.cse.lookingglass.apis.walkandtouch.SymmetricPerspectiveCamera subject = null; // does this want to be a symmetric perspective camera?
	org.alice.apis.moveandturn.Transformable asSeenBy = null;

	private org.alice.apis.moveandturn.Model m_characterHead = null;
	private OrthogonalMatrix3x3 m_orientationBegin = null;
	private OrthogonalMatrix3x3 m_orientationEnd = null;
	
	edu.cmu.cs.dennisc.math.Point3 m_positionBegin = null;
	edu.cmu.cs.dennisc.math.Point3 m_positionEnd = null;
	
	public CharacterViewAnimation ( edu.wustl.cse.lookingglass.apis.walkandtouch.SymmetricPerspectiveCamera subject, org.alice.apis.moveandturn.Transformable asSeenBy, double duration ) {
		this.subject = subject;
		this.asSeenBy = asSeenBy;
		this.setDuration( duration );
	}
	
	@Override
	protected void prologue() {
		m_orientationBegin = getOrientationBegin();
		m_orientationEnd = null;
		
		m_positionBegin = getPositionBegin();
		m_positionEnd = getPositionEnd();
		
	}
	
	@Override
	protected void setPortion( double portion ) {
		if( m_orientationEnd==null ) {
			m_orientationEnd = getOrientationEnd();
		}
		UnitQuaternion interpQuat = UnitQuaternion.createInterpolation(m_orientationBegin.createUnitQuaternion(), m_orientationEnd.createUnitQuaternion(), portion);
		subject.setOrientationRightNow( interpQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE );
			
		edu.cmu.cs.dennisc.math.Point3 currentPoint = Point3.createInterpolation( m_positionBegin, m_positionEnd, portion );
		subject.setPositionRightNow( currentPoint, org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}
	
	@Override
	protected void epilogue() {	
	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getPositionBegin() {
		
		return subject.getPosition( org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}
		
	protected edu.cmu.cs.dennisc.math.Point3 getPositionEnd() {
		edu.cmu.cs.dennisc.math.Point3 v = new edu.cmu.cs.dennisc.math.Point3(0,0,0); 
		
		//TODO: update these with the new offset magic when it goes in.
		if (asSeenBy  instanceof Model) {
			Model character = (Model) asSeenBy;
			
			Model m_characterHead = character.findFirstMatch( Model.class, new edu.wustl.cse.lookingglass.apis.walkandtouch.pattern.NameContainsCriterion("head", false ));
			if (m_characterHead != null) {
				v = m_characterHead.getAxisAlignedMinimumBoundingBox().getCenterOfFrontFace();
				return m_characterHead.createOffsetStandIn(v).getPosition(AsSeenBy.SCENE);
//				return m_characterHead.getPosition(v, org.alice.apis.moveandturn.AsSeenBy.SCENE);
			} else {
				v = character.getAxisAlignedMinimumBoundingBox().getCenterOfFrontFace();
				v.y *= 1.8;
				return character.createOffsetStandIn(v).getPosition(AsSeenBy.SCENE);
//				return character.getPosition(v, org.alice.apis.moveandturn.AsSeenBy.SCENE);
			}
		} else {
		}
		return asSeenBy.createOffsetStandIn(v).getPosition(AsSeenBy.SCENE);
//		return asSeenBy.getPosition( v, org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}
	
	protected OrthogonalMatrix3x3 getOrientationBegin() {
		return subject.getOrientation(org.alice.apis.moveandturn.AsSeenBy.SCENE);
	}
	
	protected OrthogonalMatrix3x3 getOrientationEnd() {
		if (m_characterHead != null) {
			return m_characterHead.getOrientation(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		} else {
			return asSeenBy.getOrientation(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		}
	}

}

