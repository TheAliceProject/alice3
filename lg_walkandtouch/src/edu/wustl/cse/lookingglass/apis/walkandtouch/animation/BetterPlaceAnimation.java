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

import edu.cmu.cs.dennisc.animation.DurationBasedAnimation;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel;
import edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation;

//import org.alice.apis.moveandturn.property.SpatialRelationProperty;
//import org.alice.apis.moveandturn.property.NumberProperty;

/**
 * @author caitlin
 */
public class BetterPlaceAnimation extends DurationBasedAnimation {
	
	
	edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target = null; 
	PolygonalModel subject = null;
	edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation spatialRelation = edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF;
	double offset = 2.0;
	
	edu.cmu.cs.dennisc.math.AxisAlignedBox m_subjectBoundingBox = null;
	edu.cmu.cs.dennisc.math.AxisAlignedBox m_targetBoundingBox = null;
	
	edu.cmu.cs.dennisc.math.polynomial.HermiteCubic m_xHermite = null; 
	edu.cmu.cs.dennisc.math.polynomial.HermiteCubic m_yHermite = null; 
	edu.cmu.cs.dennisc.math.polynomial.HermiteCubic m_zHermite = null; 
	
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_transformationBegin;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_transformationEnd;
	
	
	public BetterPlaceAnimation( PolygonalModel subject, edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel target, SpatialRelation spatialRelation, double offset, double duration ) {
		
		super( duration );
		this.subject = subject;
		this.target = target;
		
		this.spatialRelation = spatialRelation;
		this.offset = offset;
	}
	
	@Override
	public void prologue() {
		
		assert( target != null);
		
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 asSeenByTrans = target.getTransformation( org.alice.apis.moveandturn.AsSeenBy.SCENE );		
		target.orientToUprightRightNow( org.alice.apis.moveandturn.AsSeenBy.SCENE ); 
		
		m_transformationBegin = subject.getTransformation( target );	
		
		// find end transformation
		edu.cmu.cs.dennisc.math.Point3 posAbs = getPositionEnd();				
		edu.cmu.cs.dennisc.math.Point3 curPos = subject.getPosition(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		
		subject.setPositionRightNow(posAbs, target);	

		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes = subject.calculatePointAt( target, (edu.cmu.cs.dennisc.math.Point3)null, new edu.cmu.cs.dennisc.math.Vector3(0,1,0) );
		subject.setPositionRightNow(curPos, org.alice.apis.moveandturn.AsSeenBy.SCENE);
		
		//testing
		curPos = subject.getPosition(org.alice.apis.moveandturn.AsSeenBy.SCENE);

		edu.cmu.cs.dennisc.math.AffineMatrix4x4 pov = target.getTransformation( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		
		//just changed	
		pov.orientation.setValue(axes);
		pov.translation.set( posAbs );
		
		m_transformationEnd = new edu.cmu.cs.dennisc.math.AffineMatrix4x4(pov);
	
		double dx = m_transformationBegin.translation.x-m_transformationEnd.translation.x;
		double dy = m_transformationBegin.translation.y-m_transformationEnd.translation.y;
		double dz = m_transformationBegin.translation.z-m_transformationEnd.translation.z;
		double distance = Math.sqrt( dx*dx + dy*dy + dz*dz );
		double s = distance;
		
		//TODO: the structure of the matrix has changed - these need to be updated
		m_xHermite = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( m_transformationBegin.translation.x, m_transformationEnd.translation.x, m_transformationBegin.orientation.createForward().x*s, m_transformationEnd.orientation.createForward().x*s );
		m_yHermite = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( m_transformationBegin.translation.y, m_transformationEnd.translation.y, m_transformationBegin.orientation.createForward().y*s, m_transformationEnd.orientation.createForward().y*s );
		m_zHermite = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( m_transformationBegin.translation.z, m_transformationEnd.translation.z, m_transformationBegin.orientation.createForward().z*s, m_transformationEnd.orientation.createForward().z*s );
					
//			super.prologue(t);
//		getActualStepLength();
		
//			((Model)target).setTransformationRightNow(asSeenByTrans, subject.getWorld());
		target.setTransformationRightNow( asSeenByTrans, org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}
	
	@Override
	protected void setPortion(double portion) {
		if (portion <= 1.0) {
			double x;
			double y;
			double z;
			double dx;
			double dy;
			double dz;

			// get the appropriate position
			x = m_xHermite.evaluate( portion );
			y = m_yHermite.evaluate( portion );
			z = m_zHermite.evaluate( portion );
			
			subject.setPositionRightNow( x, y, z, target );

			// face the direction you are moving
			dx = m_xHermite.evaluateDerivative(portion);
//			dy = m_yHermite.evaluateDerivative(portion);
			dy = 0.0;
			dz = m_zHermite.evaluateDerivative(portion);


			if (!((dx == 0) && (dy==0) && (dz==0))){
				edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orient = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				orient = OrthogonalMatrix3x3.createFromForwardAndUpGuide(new edu.cmu.cs.dennisc.math.Vector3(dx,dy,dz), new edu.cmu.cs.dennisc.math.Vector3(0,1,0));
				subject.setOrientationRightNow(orient, target);
			} 
		
		}
	}
	
	@Override
	protected void epilogue() {
		// TODO Auto-generated method stub
		
	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getPositionEnd() {
		if( m_subjectBoundingBox==null ) {
			m_subjectBoundingBox = subject.getAxisAlignedMinimumBoundingBox(AsSeenBy.SELF, org.alice.apis.moveandturn.HowMuch.THIS_AND_DESCENDANT_PARTS);
	
			if (m_subjectBoundingBox.getMaximum() == null) {
				edu.cmu.cs.dennisc.math.Point3 p = subject.getPosition( subject );
				m_subjectBoundingBox = new edu.cmu.cs.dennisc.math.AxisAlignedBox(p,p);
			}
		}
		if( m_targetBoundingBox ==null ) {
			m_targetBoundingBox = target.getAxisAlignedMinimumBoundingBox(AsSeenBy.SELF, org.alice.apis.moveandturn.HowMuch.THIS_AND_DESCENDANT_PARTS);
	
			if (m_targetBoundingBox.getMaximum() == null) {
				edu.cmu.cs.dennisc.math.Point3 p = subject.getPosition( subject );
				m_targetBoundingBox = new edu.cmu.cs.dennisc.math.AxisAlignedBox(p,p);
			}
		}
		
		edu.cmu.cs.dennisc.math.Point3 v = spatialRelation.getPlaceVector( offset, m_subjectBoundingBox, m_targetBoundingBox );
		
		PrintUtilities.println("end vector: ", v);
		return v;
	}
	
	protected double getValueAtTime(double t) {
		double ft = m_xHermite.evaluateDerivative(t);
		double ht = m_zHermite.evaluateDerivative(t);
		
		return java.lang.Math.sqrt(ft*ft + ht*ht);
	}
	
	protected double getPathLength() {
		double x1s = getValueAtTime(0.0) + getValueAtTime(1.0);
		
		double h = 0.1;
		
		double startT = h;
		double x4s = 0.0;
		
		while (startT < 1.0) {
			x4s += getValueAtTime(startT);
			startT += 2*h;
		}
		
		startT = 2*h;
		double x2s = 0.0;
		
		while (startT < 1.0) {
			x2s += getValueAtTime(startT);
			startT += 2*h;
		}
		
		return (x1s + 4*x4s + 2*x2s) * (h/3.0);
	}

}
	
