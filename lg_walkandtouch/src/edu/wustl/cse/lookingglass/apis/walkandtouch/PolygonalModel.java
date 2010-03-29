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

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.ReferenceFrame;
import org.alice.apis.moveandturn.StandIn;
import org.alice.apis.moveandturn.gallery.environments.Ground;
import org.alice.apis.moveandturn.gallery.environments.grounds.*;

import edu.cmu.cs.dennisc.alice.annotations.*;

public class PolygonalModel extends org.alice.apis.moveandturn.PolygonalModel {
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setAxesOnlyAsSeenByParent( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes ) {
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getLocalTransformation();
//		m.orientation.setValue( axes );
//		setLocalTransformation( m );
//	}
//
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void standUpRightNow( ReferenceFrame asSeenBy) {	
//		this.getSGTransformable().setAxesOnlyToStandUp( asSeenBy );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void standUpRightNow( org.alice.apis.moveandturn.Transformable trans) {
//		this.getSGTransformable().setAxesOnlyToStandUp( trans.getSGReferenceFrame() );
//	}
//	
//	//(edu.wustl.cse.ckelleher.apis.walkandtouch.PolygonalModel subject, double duration) {

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void orientToUprightRightNow( org.alice.apis.moveandturn.ReferenceFrame asSeenBy) {
		super.orientToUpright( RIGHT_NOW );
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void straightenUp(Number duration) {
		perform(new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.StraightenAnimation(this, duration.doubleValue()));
	}
	
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void straightenUp() {
		straightenUp(1.0);
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void moveTo(PolygonalModel target, SpatialRelation spatialRelation, Number offset, Number duration) {
		perform( new edu.wustl.cse.lookingglass.apis.walkandtouch.animation.BetterPlaceAnimation(this, target, spatialRelation, offset.doubleValue(), duration.doubleValue()));
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void moveTo(PolygonalModel target, SpatialRelation spatialRelation, Number offset) {
		moveTo(target, spatialRelation, offset, 1.0);
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void moveTo(PolygonalModel target, SpatialRelation spatialRelation) {
		moveTo(target, spatialRelation, 1.0);
	}
	@MethodTemplate( visibility=Visibility.CHAINED)
	public void moveTo(PolygonalModel target) {
		moveTo(target, SpatialRelation.IN_FRONT_OF);
	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculatePointAt(org.alice.apis.moveandturn.Transformable target, edu.cmu.cs.dennisc.math.Point3 offset, edu.cmu.cs.dennisc.math.Vector3 upguide ) {
//		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 currentOrientation = this.getSGTransformable().getAxes( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		this.getSGTransformable().setAxesOnlyToPointAt( target.getSGTransformable(), offset, upguide );
//		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 pointAtOrientation = this.getSGTransformable().getAxes( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		this.getSGTransformable().setAxesOnly( currentOrientation, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//		
//		return pointAtOrientation;
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void moveRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
//		getSGTransformable().applyTranslation( direction.getAxis(amount), asSeenBy  );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void moveRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount, org.alice.apis.moveandturn.Transformable asSeenBy) {
//		getSGTransformable().applyTranslation( direction.getAxis(amount), asSeenBy.getSGReferenceFrame()  );
//	}
//
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void moveRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount) {
//		getSGTransformable().applyTranslation( direction.getAxis(amount) );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void turnRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount){
//		
//	}
//	
////	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
////	public void turnRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount, org.alice.apis.moveandturn.Transformable asSeenBy) {
////		
////	}
////	
////	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
////	public void turnRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
////		
////	}
//	
////	turnRightNow(org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.25);
//	
//	//positions
//	
//	//I've swapped -z on all of these. is that right?
////	public void setPositionRightNow(org.alice.apis.moveandturn.Position posAbs, org.alice.apis.moveandturn.Transformable asSeenBy) {
////		getSGTransformable().setTranslationOnly( getVectorForPosition(posAbs), asSeenBy.getSGReferenceFrame() );
////	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setPositionRightNow(edu.cmu.cs.dennisc.math.Point3 posAbs, org.alice.apis.moveandturn.Transformable asSeenBy) {
//		getSGTransformable().setTranslationOnly(  new edu.cmu.cs.dennisc.math.Point3(posAbs.x,posAbs.y,posAbs.z), asSeenBy.getSGReferenceFrame() );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setPositionRightNow(edu.cmu.cs.dennisc.math.Point3 posAbs, AsSeenBy asSeenBy) {
//		getSGTransformable().setTranslationOnly(  new edu.cmu.cs.dennisc.math.Point3(posAbs.x,posAbs.y,posAbs.z), asSeenBy.getSGReferenceFrame() );
//	}
//	
////	public void setPositionRightNow(org.alice.apis.moveandturn.Position posAbs, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
////		getSGTransformable().setTranslationOnly( getVectorForPosition(posAbs), asSeenBy );
////	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setPositionRightNow(edu.cmu.cs.dennisc.math.Point3 posAbs, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
//		getSGTransformable().setTranslationOnly(  new edu.cmu.cs.dennisc.math.Point3(posAbs.x,posAbs.y,posAbs.z), asSeenBy );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setPositionRightNow(double x, double y, double z, org.alice.apis.moveandturn.Transformable asSeenBy) {
//		getSGTransformable().setTranslationOnly( new edu.cmu.cs.dennisc.math.Point3(x,y,z), asSeenBy.getSGReferenceFrame() );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setPositionRightNow(double x, double y, double z, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
//		getSGTransformable().setTranslationOnly( new edu.cmu.cs.dennisc.math.Point3(x,y,z), asSeenBy );
//	}
////	
////	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
////	public void setPositionRightNow(double x, double y, double z, AsSeenBy asSeenBy) {
////		getSGTransformable().setTranslationOnly( new edu.cmu.cs.dennisc.math.PointD3(x,y,z), asSeenBy );
////	}
//	
//	//orientations
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getOrientationAsAxes(org.alice.apis.moveandturn.ReferenceFrame asSeenBy){
//		return this.getOrientation( asSeenBy );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setOrientationRightNow( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
//		getSGTransformable().setAxesOnly( axes, asSeenBy );		
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setOrientationRightNow( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes, edu.cmu.cs.dennisc.scenegraph.AsSeenBy asSeenBy) {
//		getSGTransformable().setAxesOnly( axes, asSeenBy );	
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setOrientationRightNow( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes, org.alice.apis.moveandturn.Transformable asSeenBy) {
//		getSGTransformable().setAxesOnly( axes, asSeenBy.getSGReferenceFrame() );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( org.alice.apis.moveandturn.ReferenceFrame asSeenBy ) {
//		return this.getPointOfView( asSeenBy );
//	}
//	
//	// transformable
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setTransformationRightNow(edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation, org.alice.apis.moveandturn.Transformable asSeenBy) {
//		this.getSGTransformable().setTransformation( transformation, asSeenBy.getSGReferenceFrame() );
//	}
//	
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void setTransformationRightNow(edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
//		this.getSGTransformable().setTransformation( transformation, asSeenBy );
//	}
//	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
	@Deprecated
	public PolygonalModel getBodyPartNamed( String name ) {
		return this.findFirstDescendantNamed( PolygonalModel.class, name );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public org.alice.apis.moveandturn.PolygonalModel getGround() {
		Class[] clses = { Ground.class, GrassyGround.class, DirtGround.class, MoonSurface.class, SandyGround.class, SeaSurface.class, SnowyGround.class };
		for( Class<? extends org.alice.apis.moveandturn.PolygonalModel> cls : clses ) {
			org.alice.apis.moveandturn.PolygonalModel rv = this.getScene().findFirstMatch( cls );
			if( rv != null ) {
				return rv;
			}
		}
		return null;
//		Ground ground = this.getScene().findFirstMatch(Ground.class);
//		return ground;
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public SymmetricPerspectiveCamera getCamera() {
		SymmetricPerspectiveCamera camera = this.getScene().findFirstMatch(SymmetricPerspectiveCamera.class);
		return camera;
	}
//	
//
//	
////	public static edu.cmu.cs.dennisc.linearalgebra.VectorD3 getVectorForPosition(org.alice.apis.moveandturn.Position pos) {
////		edu.cmu.cs.dennisc.linearalgebra.VectorD3 vecPos = new edu.cmu.cs.dennisc.linearalgebra.VectorD3(pos.getDistanceRight(), pos.getDistanceUp(), pos.getDistanceBackward());
////		return vecPos;
////	}
////	
////	public static edu.cmu.cs.dennisc.linearalgebra.PointD3 getPointForPosition(org.alice.apis.moveandturn.Position pos) {
////		edu.cmu.cs.dennisc.linearalgebra.PointD3 point = new edu.cmu.cs.dennisc.linearalgebra.PointD3(pos.getDistanceRight(), pos.getDistanceUp(), pos.getDistanceBackward());
////		return point;
////	}
//	
//
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculatePointAt(org.alice.apis.moveandturn.Transformable target, edu.cmu.cs.dennisc.math.Point3 offset, edu.cmu.cs.dennisc.math.Vector3 upguide ) {
		return super.calculatePointAtAxes( target.createOffsetStandInIfNecessary( offset ) );
	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getOrientationAsAxes(org.alice.apis.moveandturn.ReferenceFrame asSeenBy){
		return this.getOrientation( asSeenBy );
	}

//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
//	public void moveRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
//		getSGTransformable().applyTranslation( direction.getAxis(amount), asSeenBy  );
//	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void moveRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount, org.alice.apis.moveandturn.ReferenceFrame asSeenBy) {
		super.move( direction, amount, RIGHT_NOW, asSeenBy );
	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void moveRightNow(org.alice.apis.moveandturn.MoveDirection direction, double amount) {
		moveRightNow( direction, amount, AsSeenBy.SELF );
	}

	public void setPositionRightNow( edu.cmu.cs.dennisc.math.Point3 offset, org.alice.apis.moveandturn.ReferenceFrame asSeenBy ) {
		org.alice.apis.moveandturn.StandIn standIn = acquireStandIn( asSeenBy, offset );
		try {
			super.setTranslationOnly( standIn, RIGHT_NOW, null);
		} finally {
			releaseStandIn( standIn );
		}
	}
	public void setPositionRightNow( double x, double y, double z, org.alice.apis.moveandturn.ReferenceFrame asSeenBy ) {
		setPositionRightNow( new edu.cmu.cs.dennisc.math.Point3( x, y, z ), asSeenBy );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void setOrientationRightNow( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 offset, org.alice.apis.moveandturn.ReferenceFrame asSeenBy) {
		org.alice.apis.moveandturn.StandIn standIn = acquireStandIn( asSeenBy, offset );
		try {
			super.orientTo( standIn, RIGHT_NOW );
		} finally {
			releaseStandIn( standIn );
		}
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void setTransformationRightNow( edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset, org.alice.apis.moveandturn.ReferenceFrame asSeenBy) {
		org.alice.apis.moveandturn.StandIn standIn = acquireStandIn( asSeenBy, offset );
		try {
			super.moveAndOrientTo( standIn, RIGHT_NOW );
		} finally {
			releaseStandIn( standIn );
		}
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void setAxesOnlyAsSeenByParent( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes ) {
		setOrientationRightNow( axes, AsSeenBy.PARENT );
	}


	//
//	//todo: push to ReferenceFrame?
//	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
//	public void applyTranslationInScene( double xDelta, double yDelta, double zDelta ) {
//		getSGTransformable().applyTranslation( xDelta, yDelta, zDelta, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
//	}
//	
//	//todo: push to ReferenceFrame?
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.math.Point3 getPositionInScene( edu.cmu.cs.dennisc.math.Point3 offset ) {
		org.alice.apis.moveandturn.StandIn standIn = acquireStandIn( this, offset );
		try {
			return standIn.getPosition( AsSeenBy.SCENE );
		} finally {
			releaseStandIn( standIn );
		}
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.math.Point3 getPositionInScene( double x, double y, double z ) {
		return getPositionInScene( new edu.cmu.cs.dennisc.math.Point3( x, y, z ) );
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN)
	@Override
	public StandIn acquireStandIn(org.alice.apis.moveandturn.ReferenceFrame referenceFrame) {
		return super.acquireStandIn(referenceFrame);
	}
//	@Override
//	public StandIn acquireStandIn(ReferenceFrame referenceFrame) {
//		return super.acquireStandIn(referenceFrame);
//	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME)
	public org.alice.apis.moveandturn.PolygonalModel getSceneCharacter(String characterName) {
//		getScene().find
		return getScene().findFirstDescendantNamed(org.alice.apis.moveandturn.PolygonalModel.class, characterName);
		
	}
}
