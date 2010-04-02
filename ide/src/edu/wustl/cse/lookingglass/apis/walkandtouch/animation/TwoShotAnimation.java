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

import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation;

/**
 * @author caitlin
 */
public class TwoShotAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
	
	private double angleToRotate = java.lang.Math.PI/6; // 5 degrees, for now
	
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.SymmetricPerspectiveCamera m_subject = null;
	protected org.alice.apis.moveandturn.Transformable m_asSeenBy2 = null;
	protected org.alice.apis.moveandturn.Transformable m_asSeenBy = null;
	protected edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation spatialRelation = null;

	private edu.cmu.cs.dennisc.math.AxisAlignedBox m_subjectBoundingBox;
	private edu.cmu.cs.dennisc.math.AxisAlignedBox m_asSeenByBoundingBox;
	private edu.cmu.cs.dennisc.math.AxisAlignedBox m_asSeenBy2BoundingBox;
	private double m_amount;
	
	private edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m_cameraEndOrientation;
	private edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m_cameraBeginOrientation;
	
	edu.cmu.cs.dennisc.math.Point3 m_cameraBeginPosition;
	edu.cmu.cs.dennisc.math.Point3 m_cameraEndPosition;
	
	private double m_cameraHeight;
	
	public TwoShotAnimation(edu.wustl.cse.lookingglass.apis.walkandtouch.SymmetricPerspectiveCamera subject, org.alice.apis.moveandturn.Transformable asSeenBy, org.alice.apis.moveandturn.Transformable asSeenBy2, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation spatialRelation, double amount, double duration) {
		this.setDuration( duration );
		this.m_subject = subject;
		this.m_asSeenBy = asSeenBy;
		this.m_asSeenBy2 = asSeenBy2;
		this.spatialRelation = spatialRelation;
		this.m_amount = amount;
		
	}
	
	@Override
	protected void prologue() {
//		if( m_asSeenBy == null ) {
//			  throw new org.alice.apis.moveandturn.SimulationPropertyException( "first character value must not be null.", null, TwoShotAnimation.this.asSeenBy );
//		  }
//
//		  if( m_asSeenBy2 == null ) {
//			  throw new org.alice.apis.moveandturn.SimulationPropertyException( "second character value must not be null.", getCurrentStack(), TwoShotAnimation.this.asSeenBy2 );            
//		  }
//		  if( m_subject == null ) {
//			  throw new org.alice.apis.moveandturn.SimulationPropertyException( "subject value must not be null.", getCurrentStack(), TwoShotAnimation.this.subject );            
//		  }
//		  if( m_asSeenBy2 == m_asSeenBy ) {
//			  throw new org.alice.apis.moveandturn.SimulationPropertyException( "first and second characters must be different", getCurrentStack(), TwoShotAnimation.this.asSeenBy2 );            
//		  }
//		  if ((m_subject == m_asSeenBy) || ( m_subject == m_asSeenBy2 )) {
//			  throw new org.alice.apis.moveandturn.SimulationPropertyException( "subject value cannot be the same as either character", getCurrentStack(), TwoShotAnimation.this.subject );            
//		  }
//		  if (m_amount < 0) {
//			  throw new org.alice.apis.moveandturn.SimulationPropertyException( "amount must be greater than 0", getCurrentStack(), TwoShotAnimation.this.amount );            
//		  }
		
		m_cameraBeginPosition = getPositionBegin();
		m_cameraEndPosition = getPositionEnd();
		
	}
	@Override
	protected void setPortion( double portion ) {
		
		// need to interpolate the orientation as well.
		UnitQuaternion nextQuat = UnitQuaternion.createInterpolation(m_cameraBeginOrientation.createUnitQuaternion(), m_cameraEndOrientation.createUnitQuaternion(), portion);
		m_subject.setOrientationRightNow(nextQuat.createOrthogonalMatrix3x3(), org.alice.apis.moveandturn.AsSeenBy.SCENE);
		
		
		edu.cmu.cs.dennisc.math.Point3 currentPos = Point3.createInterpolation(m_cameraBeginPosition, m_cameraEndPosition, portion);
		m_subject.setPositionRightNow( currentPos, org.alice.apis.moveandturn.AsSeenBy.SCENE);

	}
	@Override
	protected void epilogue() {
		// TODO Auto-generated method stub
		
	}
	
	protected edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox(org.alice.apis.moveandturn.Transformable ref, org.alice.apis.moveandturn.Transformable asSeenBy) {
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = ref.getAxisAlignedMinimumBoundingBox(asSeenBy);
		if (bbox.getMaximum() == null) {
			bbox = new edu.cmu.cs.dennisc.math.AxisAlignedBox(ref.getPosition( asSeenBy ), ref.getPosition( asSeenBy ));
		} else if (Double.isNaN( bbox.getMinimum().x )) {
			bbox = new edu.cmu.cs.dennisc.math.AxisAlignedBox(ref.getPosition( asSeenBy ), ref.getPosition( asSeenBy ));
		}
		return bbox;							
	}
	
	// b as in y = mx + b (although z is what matters in this case)
	protected double calculateB(double x0, double z0, double dx, double dz) {
		return (z0 - ((dz/dx) * x0));
	}
	
	// calculate x intersection of camera left and forward lines (b's based on intersection with
	// a bounding box min or max)
	protected double calculateXIntersect(double dxLeft, double dzLeft, double bLeft, double bForward) {
		return (bForward - bLeft) / ((dzLeft/dxLeft) + (dxLeft/dzLeft));
	}
	
	// calculate z intersection of camera left and forward lines (b's based on intersection with
	// a bounding box min or max)
	protected double calculateZIntersect(double dxLeft, double dzLeft, double bLeft, double bForward) {
		return (((dxLeft/dzLeft) * bLeft) + ((dzLeft/dxLeft) * bForward))/((dxLeft/dzLeft) + (dzLeft/dxLeft));
	}
	
	protected double calculateDistance(double x1, double z1, double x2, double z2) {				
		return java.lang.Math.sqrt((x2-x1)*(x2-x1) + (z2-z1)*(z2-z1));
	}
	
	protected OrthogonalMatrix3x3 getCameraOrientation(SpatialRelation spatialRelation) {
	// get the initial position and orientation for the camera
		OrthogonalMatrix3x3 cameraEndOrientation = new OrthogonalMatrix3x3();
		
		if (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND_RIGHT_OF)) {
			cameraEndOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide(new edu.cmu.cs.dennisc.math.Vector3(0, 0, 1), cameraEndOrientation.up);
			edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundY( cameraEndOrientation, -1.0 * angleToRotate);
		} else if (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND_LEFT_OF)) {
			cameraEndOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide(new edu.cmu.cs.dennisc.math.Vector3(0, 0, 1), cameraEndOrientation.up);
			edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundY(cameraEndOrientation, angleToRotate);
		} else if (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.FRONT_RIGHT_OF)) {
			cameraEndOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide(new edu.cmu.cs.dennisc.math.Vector3(0, 0, -1), cameraEndOrientation.up);
			edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundY(cameraEndOrientation, angleToRotate);
		} else if (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.FRONT_LEFT_OF)) {
			cameraEndOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide(new edu.cmu.cs.dennisc.math.Vector3(0, 0, -1), cameraEndOrientation.up);
			edu.wustl.cse.lookingglass.apis.walkandtouch.RotationUtilities.rotateAroundY(cameraEndOrientation, -1.0 * angleToRotate);
		}
		
		return cameraEndOrientation;
	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getPositionBegin() {
		m_cameraBeginOrientation = m_subject.getOrientation(org.alice.apis.moveandturn.AsSeenBy.SCENE);
		return m_subject.getPosition( org.alice.apis.moveandturn.AsSeenBy.SCENE );
	}
					
	protected edu.cmu.cs.dennisc.math.Point3 getPositionEnd() {

		// get the bounding boxes to set the spatial relations
		if( m_subjectBoundingBox==null ) m_subjectBoundingBox = getBoundingBox(m_subject, m_subject);
		if( m_asSeenByBoundingBox ==null ) m_asSeenByBoundingBox = getBoundingBox(m_asSeenBy, m_asSeenBy);
		if( m_asSeenBy2BoundingBox ==null ) m_asSeenBy2BoundingBox = getBoundingBox(m_asSeenBy2, m_asSeenBy);
		m_asSeenByBoundingBox.union(m_asSeenBy2BoundingBox);
		
		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 cameraEndOrientation = null;
		edu.cmu.cs.dennisc.math.Point3 cameraEndPos = spatialRelation.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );		
		
//		// THIS IS COMPLETELY HORRIBLE		
//		edu.cmu.cs.dennisc.math.AffineMatrixD4x4 m_transformationBegin = m_subject.getPointOfView( org.alice.apis.moveandturn.AsSeenBy.SCENE );
//		m_subject.setPositionRightNow( cameraEndPos, m_asSeenBy );
//		m_subject.getSGTransformable().setAxesOnlyToStandUp();	
//		cameraEndOrientation = m_subject.calculateTurnToFace( m_asSeenBy, null, new edu.cmu.cs.dennisc.math.VectorD3(0,1,0) );
//		
//		m_subject.getSGTransformable().setTransformation( m_transformationBegin, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
		
		
		// if the camera is supposed to end up above or below, this doesn't all hold anymore, so 
		// I'll exclude those cases for now.
		if ((spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND_LEFT_OF)) || (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND_RIGHT_OF)) || (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.FRONT_LEFT_OF)) || (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.FRONT_RIGHT_OF)) ) {
		//if (!spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.ABOVE) && !spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.BELOW)) {
			cameraEndOrientation = getCameraOrientation(spatialRelation);
			edu.cmu.cs.dennisc.math.Vector3 rightVector = cameraEndOrientation.right;
			edu.cmu.cs.dennisc.math.Vector3 forwardVector = cameraEndOrientation.createForward();
			
			PrintUtilities.println("left: ", rightVector);
			PrintUtilities.println("forward: ", forwardVector);
			
			double bLeft = this.calculateB(cameraEndPos.x, cameraEndPos.z, rightVector.x, rightVector.z);			
			
			// min bounds for bounding box
			edu.cmu.cs.dennisc.math.Point3 boxMin = m_asSeenByBoundingBox.getMinimum();
			double bForward = this.calculateB(boxMin.x, boxMin.z, forwardVector.x, forwardVector.z);
			double minX = this.calculateXIntersect(rightVector.x, rightVector.z, bLeft, bForward);
			double minZ = this.calculateZIntersect(rightVector.x, rightVector.z, bLeft, bForward);
			
			// max bounds for bounding box			
			edu.cmu.cs.dennisc.math.Point3 boxMax = m_asSeenByBoundingBox.getMaximum();
			bForward = this.calculateB(boxMax.x, boxMax.z, forwardVector.x, forwardVector.z);
			double maxX = this.calculateXIntersect(rightVector.x, rightVector.z, bLeft, bForward);
			double maxZ = this.calculateZIntersect(rightVector.x, rightVector.z, bLeft, bForward);
			

			// update camera end position - NOTE: camera height is not set yet
			cameraEndPos.x = minX + (maxX - minX)/2.0;
			cameraEndPos.z = minZ + (maxZ - minZ)/2.0;
							
			// figure out how far the camera should be away and place it
			double distance = this.calculateDistance(minX, minZ, maxX, maxZ);
			double largestDimSize = 0.0;
			largestDimSize =  m_amount * m_asSeenByBoundingBox.getHeight();
			if (distance > largestDimSize) largestDimSize = distance;
			
			cameraEndPos.x += (-2.5 * largestDimSize) * forwardVector.x;
			cameraEndPos.z += (-2.5 * largestDimSize) * forwardVector.z;
			
			// set the camera to the appropriate height
			double halfViewable = (m_asSeenByBoundingBox.getHeight() * m_amount * 1.5)/ 2.0;
			double startingHeight = (1.0 - m_amount) * m_asSeenByBoundingBox.getHeight();
			m_cameraHeight = startingHeight + halfViewable + m_asSeenByBoundingBox.getMinimum().y;
			cameraEndPos.y = m_cameraHeight;
			
//			cameraEndPos.z = -cameraEndPos.z;
			
		} else if ( (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.LEFT_OF)) || (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.RIGHT_OF)) ){
			
			double largestDimSize = m_asSeenByBoundingBox.getDepth();
			if ( m_amount * m_asSeenByBoundingBox.getHeight() > largestDimSize) {
				largestDimSize = m_amount * m_asSeenByBoundingBox.getHeight();
			}
			cameraEndPos = spatialRelation.getPlaceVector(largestDimSize * 2.5, m_subjectBoundingBox, m_asSeenByBoundingBox);
			
			double halfViewable = (m_asSeenByBoundingBox.getHeight() * m_amount * 1.5)/ 2.0;
			double startingHeight = (1.0 - m_amount) * m_asSeenByBoundingBox.getHeight();
			m_cameraHeight = startingHeight + halfViewable + m_asSeenByBoundingBox.getMinimum().y;
			
			cameraEndPos.y = m_cameraHeight;
			cameraEndPos.z = m_asSeenByBoundingBox.getCenter().z;
		} else if ( (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF)) || (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BEHIND)) ){
			
			double largestDimSize = m_asSeenByBoundingBox.getWidth();
			if ( m_amount * m_asSeenByBoundingBox.getHeight() > largestDimSize) {
				largestDimSize =  m_amount * m_asSeenByBoundingBox.getHeight();
			}
			cameraEndPos = spatialRelation.getPlaceVector(largestDimSize * 3.0, m_subjectBoundingBox, m_asSeenByBoundingBox);
				
			double halfViewable = (m_asSeenByBoundingBox.getHeight() * 1.5 * m_amount)/ 2.0;
			double startingHeight = (1.0 - m_amount) * m_asSeenByBoundingBox.getHeight();
			m_cameraHeight = startingHeight + halfViewable + m_asSeenByBoundingBox.getMinimum().y;
				
			cameraEndPos.y = m_cameraHeight;
			cameraEndPos.x = m_asSeenByBoundingBox.getCenter().x;
		} else if ( (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.ABOVE)) || (spatialRelation.equals(edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.BELOW)) ){
			
			double largestDimSize = m_asSeenByBoundingBox.getDepth();
			if (m_asSeenByBoundingBox.getWidth() > largestDimSize) {
				largestDimSize = m_asSeenByBoundingBox.getWidth();
			}
			cameraEndPos = spatialRelation.getPlaceVector(largestDimSize * 2.2, m_subjectBoundingBox, m_asSeenByBoundingBox);
					
			double halfViewable = (m_asSeenByBoundingBox.getDepth() / 2.0);
			m_cameraHeight = halfViewable + m_asSeenByBoundingBox.getMinimum().y;
					
			cameraEndPos.z = m_cameraHeight;
			cameraEndPos.x = m_asSeenByBoundingBox.getCenter().x;
		}
		
		// now we need to find the orientation
//		edu.cmu.cs.dennisc.math.AffineMatrixD4x4 m_transformationBegin = m_subject.getPointOfView( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		m_subject.setPositionRightNow( cameraEndPos, m_asSeenBy );
		edu.cmu.cs.dennisc.math.Point3 retPos = m_subject.getPosition( org.alice.apis.moveandturn.AsSeenBy.SCENE );
		
		// questionable...
		m_subject.getSGTransformable().setAxesOnlyToStandUp();
		
		m_cameraEndOrientation = m_subject.calculateTurnToFace( m_asSeenBy, null, new edu.cmu.cs.dennisc.math.Vector3(0,1,0) );
		
		return retPos;
		
	}

}

//// get the initial position and orientation for the camera
// originally from getPositionEnd()
//if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.LEFT_OF)) {
//	cameraEndPos = spatialRelation.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(1, 0, 0), cameraEndOrientation.getColumn(1));
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.RIGHT_OF)) {
//	cameraEndPos = spatialRelation.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(-1, 0, 0), cameraEndOrientation.getColumn(1));
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.BEHIND_RIGHT_OF)) {
//	cameraEndPos = edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.BEHIND.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(0, 0, 1), cameraEndOrientation.getColumn(1));
//	edu.wustl.cse.ckelleher.apis.walkandtouch.RotationUtilities.rotateAroundY( cameraEndOrientation, -1.0 * angleToRotate);
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.BEHIND_LEFT_OF)) {
//	cameraEndPos = edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.BEHIND.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(0, 0, 1), cameraEndOrientation.getColumn(1));
//	edu.wustl.cse.ckelleher.apis.walkandtouch.RotationUtilities.rotateAroundY(cameraEndOrientation, angleToRotate);
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.FRONT_RIGHT_OF)) {
//	cameraEndPos = edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.IN_FRONT_OF.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(0, 0, -1), cameraEndOrientation.getColumn(1));
//	edu.wustl.cse.ckelleher.apis.walkandtouch.RotationUtilities.rotateAroundY(cameraEndOrientation, angleToRotate);
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.FRONT_LEFT_OF)) {
//	cameraEndPos = edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.IN_FRONT_OF.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(0, 0, -1), cameraEndOrientation.getColumn(1));
//	edu.wustl.cse.ckelleher.apis.walkandtouch.RotationUtilities.rotateAroundY(cameraEndOrientation, -1.0 * angleToRotate);
//
//// these are the cases that I don't think are useful for this animation, but shouldn't break	
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.IN_FRONT_OF)) {
//	edu.cmu.cs.dennisc.print.PrintUtilities.println("in front");
//	cameraEndPos = spatialRelation.getPlaceVector( 1, m_asSeenByBoundingBox, m_asSeenBy2BoundingBox );
//	edu.cmu.cs.dennisc.print.PrintUtilities.println("cameraEndPos ", cameraEndPos);
//	edu.cmu.cs.dennisc.print.PrintUtilities.println("asSeenBy forward ", cameraEndOrientation);
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(0, 0, 1), cameraEndOrientation.getColumn(1)); //
//	edu.cmu.cs.dennisc.print.PrintUtilities.println("asSeenBy forward post", cameraEndOrientation);
//	edu.cmu.cs.dennisc.print.PrintUtilities.println("cameraEndOrientation ", cameraEndOrientation);
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.BEHIND)) {
//	cameraEndPos = spatialRelation.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(0, 0, 1), cameraEndOrientation.getColumn(1));
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.ABOVE)) {
//	cameraEndPos = spatialRelation.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(0, -1, 0), new edu.cmu.cs.dennisc.math.VectorD3(-1,0,0));
//} else if (spatialRelation.equals(edu.wustl.cse.ckelleher.apis.walkandtouch.SpatialRelation.BELOW)) {
//	cameraEndPos = spatialRelation.getPlaceVector( 1, m_subjectBoundingBox, m_asSeenByBoundingBox );
//	cameraEndOrientation.set(new edu.cmu.cs.dennisc.math.VectorD3(0, 1, 0), new edu.cmu.cs.dennisc.math.VectorD3(-1,0,0));
//} 
