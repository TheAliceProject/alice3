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

/**
 * @author caitlin
 */
public class CloseUpAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
	edu.wustl.cse.lookingglass.apis.walkandtouch.SymmetricPerspectiveCamera m_subject = null;
	edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation spatialRelation = null;
	double amount;
	org.alice.apis.moveandturn.Transformable m_asSeenBy = null;
		
	private edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m_cameraEndOrientation;
	private edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m_cameraBeginOrientation;
	private edu.cmu.cs.dennisc.math.AxisAlignedBox m_subjectBoundingBox;
	private edu.cmu.cs.dennisc.math.AxisAlignedBox m_asSeenByBoundingBox;
	
	edu.cmu.cs.dennisc.math.Point3 m_cameraBeginPosition;
	edu.cmu.cs.dennisc.math.Point3 m_cameraEndPosition;

	private double m_cameraHeight;
	
	private double bubbleSpace = 0.5;

	public CloseUpAnimation(edu.wustl.cse.lookingglass.apis.walkandtouch.SymmetricPerspectiveCamera subject, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation spatialRelation, double amount, org.alice.apis.moveandturn.Transformable asSeenBy, double duration ) {
		this.setDuration( duration );
		this.m_subject = subject;
		this.spatialRelation = spatialRelation;
		this.amount = amount;
		this.m_asSeenBy = asSeenBy;
	}
	
	public CloseUpAnimation(edu.wustl.cse.lookingglass.apis.walkandtouch.SymmetricPerspectiveCamera subject, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation spatialRelation, double amount, org.alice.apis.moveandturn.Transformable asSeenBy, double duration, double bubbleSpace ) {
		this( subject, spatialRelation, amount, asSeenBy, duration);
		
		this.bubbleSpace = bubbleSpace;
	}
	
	@Override
	protected void prologue() {
		
		m_cameraBeginPosition = getPositionBegin();
		m_cameraEndPosition = getPositionEnd();
		
		
	}
	
	@Override
	protected void setPortion( double portion ) {
		// This needs to be moving to the end position; was extending off of abstract position animation before
		UnitQuaternion nextOrient = UnitQuaternion.createInterpolation(m_cameraBeginOrientation.createUnitQuaternion(), m_cameraEndOrientation.createUnitQuaternion(), portion);
		m_subject.setOrientationRightNow(nextOrient.createOrthogonalMatrix3x3().createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE);
		
		Point3 currentPosition = Point3.createInterpolation(m_cameraBeginPosition, m_cameraEndPosition, portion);
		m_subject.setPositionRightNow( currentPosition, org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}
	
	@Override
	protected void epilogue() {
		// TODO Auto-generated method stub
		
	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getPositionBegin() {
		m_cameraBeginOrientation = m_subject.getOrientation(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		return m_subject.getPosition( org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}
	protected edu.cmu.cs.dennisc.math.Point3 getPositionEnd() {
		
		if( m_subjectBoundingBox==null ) {
			m_subjectBoundingBox = m_subject.getAxisAlignedMinimumBoundingBox();		
			if (m_subjectBoundingBox.getMaximum() == null) {
				m_subjectBoundingBox = new edu.cmu.cs.dennisc.math.AxisAlignedBox(m_subject.getPosition( m_subject ), m_subject.getPosition( m_subject ));
			} else if (Double.isNaN( m_subjectBoundingBox.getMinimum().x )) {
				m_subjectBoundingBox = new edu.cmu.cs.dennisc.math.AxisAlignedBox(m_subject.getPosition( m_subject ), m_subject.getPosition( m_subject ));
			}
		}
		if( m_asSeenByBoundingBox ==null ) {
			m_asSeenByBoundingBox = m_asSeenBy.getAxisAlignedMinimumBoundingBox();
		
			if (m_asSeenByBoundingBox.getMaximum() == null) {
				m_asSeenByBoundingBox = new edu.cmu.cs.dennisc.math.AxisAlignedBox(m_asSeenBy.getPosition( m_asSeenBy ), m_asSeenBy.getPosition( m_asSeenBy ));
			}
		}
		
		double actualAmount = amount *  m_asSeenByBoundingBox.getHeight() * 3;
		edu.cmu.cs.dennisc.math.Point3 v = spatialRelation.getPlaceVector( actualAmount, m_subjectBoundingBox, m_asSeenByBoundingBox );
		
		//1.5 is fudge factor to leave space for speech bubbles
		//TODO: there's still something strange going on with getting the full character on screen
		
		double bottomOfFrame = m_asSeenByBoundingBox.getMinimum().y + (1.0 - amount) * m_asSeenByBoundingBox.getHeight();
		double topOfFrame = m_asSeenByBoundingBox.getMinimum().y + m_asSeenByBoundingBox.getHeight() + (bubbleSpace * m_asSeenByBoundingBox.getHeight());		
		double frameSize = topOfFrame - bottomOfFrame;
		
		m_cameraHeight = bottomOfFrame + (frameSize / 2.0);
		
		
//		double halfViewable = m_asSeenByBoundingBox.getHeight() * bubbleSpace * amount / 2.0;
//		
//		double startingHeight = (1.0 - amount) * m_asSeenByBoundingBox.getHeight();
////		m_cameraHeight = startingHeight + halfViewable + m_asSeenByBoundingBox.getMinimum().y - (m_asSeenByBoundingBox.getHeight() * .1);
//		m_cameraHeight = startingHeight + m_asSeenByBoundingBox.getMinimum().y + m_asSeenByBoundingBox.getHeight()/2.0; //- (m_asSeenByBoundingBox.getHeight() * .1);
		v.y = m_cameraHeight;
		
		// now we need to find the orientation
		m_subject.setPositionRightNow( v, m_asSeenBy );
		edu.cmu.cs.dennisc.math.Point3 retPos = m_subject.getPosition( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		
		// questionable...
		m_subject.getSGTransformable().setAxesOnlyToStandUp();
		
		m_cameraEndOrientation = m_subject.calculateTurnToFace( m_asSeenBy, null, new edu.cmu.cs.dennisc.math.Vector3(0,1,0) );
		
		return retPos;
	}
	
	
}
