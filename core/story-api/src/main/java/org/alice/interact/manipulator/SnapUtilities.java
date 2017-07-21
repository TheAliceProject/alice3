/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package org.alice.interact.manipulator;

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.VectorUtilities;
import org.alice.interact.handle.RotationRingHandle;
import org.alice.interact.manipulator.scenegraph.SnapLine;
import org.alice.interact.manipulator.scenegraph.SnapSphere;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities;

public class SnapUtilities {
	public static final double SNAP_LINE_VISUAL_HEIGHT = .01d;

	public static final double SNAP_TO_GROUND_DISTANCE = .05d;
	public static final double SNAP_TO_GRID_DISTANCE = .1d;
	public static final double DEFAULT_GRID_SPACING = .5d;
	public static final double MIN_SNAP_RETURN_VALUE = .1d;
	public static final double ANGLE_SNAP_DISTANCE_IN_RADIANS = ( 2 * Math.PI ) / 30d;

	private static final SnapLine X_AXIS_LINE = new SnapLine( Vector3.accessPositiveXAxis() );
	private static final SnapLine Y_AXIS_LINE = new SnapLine( Vector3.accessPositiveYAxis() );
	private static final SnapLine Z_AXIS_LINE = new SnapLine( Vector3.accessPositiveZAxis() );
	private static final SnapLine ARBITRARY_AXIS_LINE = new SnapLine( Vector3.accessPositiveXAxis() );
	private static final SnapSphere ANGLE_SNAP_SPHERE = new SnapSphere();

	private static final double MIN_SNAP_DELTA = .000001d;

	public static void showXAxis( Point3 position, Composite parent ) {
		X_AXIS_LINE.setParent( parent );
		X_AXIS_LINE.setTranslationOnly( position, AsSeenBy.SCENE );
	}

	public static void showXAxis( Point3 position, Vector3 newDirection, Composite parent ) {
		X_AXIS_LINE.setLine( newDirection );
		X_AXIS_LINE.setParent( parent );
		X_AXIS_LINE.setTranslationOnly( position, AsSeenBy.SCENE );
	}

	public static void hideXAxis() {
		X_AXIS_LINE.setLine( Vector3.accessPositiveXAxis() ); //Restore the axis line to the absolute direction
		X_AXIS_LINE.setParent( null );
	}

	public static void showYAxis( Point3 position, Composite parent ) {
		Y_AXIS_LINE.setParent( parent );
		Y_AXIS_LINE.setTranslationOnly( position, AsSeenBy.SCENE );
	}

	public static void showYAxis( Point3 position, Vector3 newDirection, Composite parent ) {
		Y_AXIS_LINE.setLine( newDirection );
		Y_AXIS_LINE.setParent( parent );
		Y_AXIS_LINE.setTranslationOnly( position, AsSeenBy.SCENE );
	}

	public static void hideYAxis() {
		Y_AXIS_LINE.setLine( Vector3.accessPositiveYAxis() ); //Restore the axis line to the absolute direction
		Y_AXIS_LINE.setParent( null );
	}

	public static void showZAxis( Point3 position, Composite parent ) {
		Z_AXIS_LINE.setParent( parent );
		Z_AXIS_LINE.setTranslationOnly( position, AsSeenBy.SCENE );
	}

	public static void showZAxis( Point3 position, Vector3 newDirection, Composite parent ) {
		Z_AXIS_LINE.setLine( newDirection );
		Z_AXIS_LINE.setParent( parent );
		Z_AXIS_LINE.setTranslationOnly( position, AsSeenBy.SCENE );
	}

	public static void hideZAxis() {
		Z_AXIS_LINE.setLine( Vector3.accessPositiveZAxis() ); //Restore the axis line to the absolute direction
		Z_AXIS_LINE.setParent( null );
	}

	public static void showArbitraryAxis( Point3 position, Vector3 direction, Composite parent ) {
		ARBITRARY_AXIS_LINE.setLine( direction );
		ARBITRARY_AXIS_LINE.setParent( parent );
		ARBITRARY_AXIS_LINE.setTranslationOnly( position, AsSeenBy.SCENE );
	}

	public static void hideArbitraryAxis() {
		ARBITRARY_AXIS_LINE.setParent( null );
	}

	public static void hideMovementSnapVisualization() {
		hideZAxis();
		hideYAxis();
		hideXAxis();
		hideArbitraryAxis();
	}

	public static void hideRotationSnapVisualization() {
		hideSnapSphere();
	}

	public static void showSnapSphere( Point3 location, Point3 ringCenter, Composite parent ) {
		ANGLE_SNAP_SPHERE.setParent( parent );
		ANGLE_SNAP_SPHERE.setTranslationOnly( location, AsSeenBy.SCENE );
		ANGLE_SNAP_SPHERE.setLineDirection( ringCenter, location );
	}

	public static void hideSnapSphere() {
		ANGLE_SNAP_SPHERE.setParent( null );
	}

	public static Visual getSGVisualForTransformable( AbstractTransformable t ) {
		if( t != null ) {
			for( int i = 0; i < t.getComponentCount(); i++ )
			{
				Component c = t.getComponentAt( i );
				if( c instanceof Visual )
				{
					return (Visual)c;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	public static edu.cmu.cs.dennisc.math.Matrix3x3 getTransformableScale( AbstractTransformable t ) {
		edu.cmu.cs.dennisc.math.Matrix3x3 returnScale;
		Visual objectVisual = getSGVisualForTransformable( t );
		if( objectVisual != null ) {
			returnScale = new edu.cmu.cs.dennisc.math.Matrix3x3();
			returnScale.setValue( objectVisual.scale.getValue() );
		} else {
			returnScale = edu.cmu.cs.dennisc.math.ScaleUtilities.newScaleMatrix3d( 1.0d, 1.0d, 1.0d );
		}
		return returnScale;

	}

	public static AxisAlignedBox getBoundingBox( AbstractTransformable t ) {
		AxisAlignedBox boundingBox = null;
		if( t != null ) {
			Object bbox = t.getBonusDataFor( AbstractDragAdapter.BOUNDING_BOX_KEY );
			if( bbox instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox ) {
				boundingBox = new AxisAlignedBox( (edu.cmu.cs.dennisc.math.AxisAlignedBox)bbox );
				if( boundingBox.isNaN() ) {
					boundingBox = null;
				}
			}
		}
		if( boundingBox == null ) {
			boundingBox = new AxisAlignedBox( new Point3( -1, 0, -1 ), new Point3( 1, 1, 1 ) );
		}
		if( boundingBox != null ) {
			boundingBox.scale( getTransformableScale( t ) );
		}
		Point3 boxMin = boundingBox.getMinimum();
		Point3 boxMax = boundingBox.getMaximum();
		TransformationUtilities.transformToAbsolute( boxMin, boxMin, t );
		TransformationUtilities.transformToAbsolute( boxMax, boxMax, t );
		boundingBox.setMaximum( boxMax );
		boundingBox.setMinimum( boxMin );
		return boundingBox;
	}

	public static Point3 snapObjectToGround( AbstractTransformable toSnap, Point3 newPosition, ReferenceFrame referenceFrame ) {

		Point3 returnSnapPosition = new Point3( newPosition );

		boolean didSnap = false;
		Vector3 movementDelta = Vector3.createSubtraction( newPosition, toSnap.getAbsoluteTransformation().translation );
		if( movementDelta.y != 0 ) {
			AxisAlignedBox bbox = getBoundingBox( toSnap );
			bbox.translate( movementDelta ); //move the bounding box to where the newPosition would place it
			double boxBottom = bbox.getYMinimum();
			if( Math.abs( boxBottom ) <= SNAP_TO_GROUND_DISTANCE ) {
				didSnap = true;
				returnSnapPosition.y -= boxBottom; //translate the object point the amount the bottom of the bounding box is awau from y
			}
			//			if (Math.abs(rv.y) <= SNAP_TO_GROUND_DISTANCE)
			//			{
			//				rv.y = 0.0d;
			//			}
		}
		return returnSnapPosition;

	}

	public static boolean isEdgeOn( AbstractCamera camera, Vector3 upVector ) {
		return isEdgeOn( camera, upVector, 0 );
	}

	public static boolean isEdgeOn( AbstractCamera camera, Vector3 upVector, double epsilon ) {
		double dotProd = Vector3.calculateDotProduct( camera.getAbsoluteTransformation().orientation.backward, upVector );
		return ( Math.abs( dotProd ) ) <= epsilon;
	}

	public static void showHorizontalSnap( AbstractCamera camera, Point3 currentPosition, Point3 snapPosition, ReferenceFrame referenceFrame ) {
		Vector3 snapVector = Vector3.createSubtraction( currentPosition, snapPosition );
		Point3 linePosition = new Point3( snapPosition );
		AffineMatrix4x4 snapTransform = referenceFrame.getAbsoluteTransformation();
		Vector3 xSnapLine = snapTransform.orientation.right;
		Vector3 zSnapLine = snapTransform.orientation.backward;
		Vector3 ySnapLine = snapTransform.orientation.up;

		referenceFrame.getInverseAbsoluteTransformation().transform( snapVector );

		//If we're looking edge on, use a vertical line to draw the snap
		if( isEdgeOn( camera, snapTransform.orientation.up ) ) {
			if( ( Math.abs( snapVector.x ) > MIN_SNAP_DELTA ) || ( Math.abs( snapVector.z ) > MIN_SNAP_DELTA ) ) {
				SnapUtilities.showYAxis( linePosition, ySnapLine, camera.getRoot() );
			} else {
				SnapUtilities.hideYAxis();
			}
		} else {
			linePosition.y = snapPosition.y;
			//If the position is too close to the ground to show up well, bump it up a little bit
			if( Math.abs( linePosition.y ) < SNAP_LINE_VISUAL_HEIGHT ) {
				linePosition.y = SNAP_LINE_VISUAL_HEIGHT;
			}
			if( Math.abs( snapVector.x ) > MIN_SNAP_DELTA ) {
				SnapUtilities.showZAxis( linePosition, zSnapLine, camera.getRoot() );
			} else {
				SnapUtilities.hideZAxis();
			}
			if( Math.abs( snapVector.z ) > MIN_SNAP_DELTA ) {
				SnapUtilities.showXAxis( linePosition, xSnapLine, camera.getRoot() );
			} else {
				SnapUtilities.hideXAxis();
			}
		}
	}

	public static void showVerticalSnap( AbstractCamera camera, Point3 currentPosition, Point3 snapPosition, ReferenceFrame referenceFrame ) {
		Vector3 snapVector = Vector3.createSubtraction( currentPosition, snapPosition );
		AffineMatrix4x4 snapTransform = referenceFrame.getAbsoluteTransformation();
		referenceFrame.getInverseAbsoluteTransformation().transform( snapVector );

		//		if (isEdgeOn(camera, snapTransform.orientation.up, .9)) {
		if( Math.abs( snapVector.y ) > MIN_SNAP_DELTA ) {
			AffineMatrix4x4 cameraTransform = camera.getAbsoluteTransformation();
			double dotRight = Vector3.calculateDotProduct( snapTransform.orientation.right, cameraTransform.orientation.backward );
			double dotBackward = Vector3.calculateDotProduct( snapTransform.orientation.backward, cameraTransform.orientation.backward );
			Vector3 lineToUse = ( Math.abs( dotRight ) < Math.abs( dotBackward ) ) ? snapTransform.orientation.right : snapTransform.orientation.backward;
			SnapUtilities.showArbitraryAxis( snapPosition, lineToUse, camera.getRoot() );
		} else {
			SnapUtilities.hideArbitraryAxis();
		}
		//}
	}

	public static void showSnapLines( AbstractCamera camera, Point3 currentPosition, Point3 snapPosition, ReferenceFrame referenceFrame ) {
		showHorizontalSnap( camera, currentPosition, snapPosition, referenceFrame );
		showVerticalSnap( camera, currentPosition, snapPosition, referenceFrame );
	}

	public static Point3 snapObjectToAbsoluteGrid( Transformable toSnap, Point3 newPosition ) {
		return snapObjectToGrid( toSnap, newPosition, DEFAULT_GRID_SPACING, toSnap.getRoot() );
	}

	public static Point3 snapObjectToGrid( AbstractTransformable toSnap, Point3 originalPositionIn, double gridSpacing, ReferenceFrame referenceFrame ) {
		AffineMatrix4x4 toReferenceFrame = referenceFrame.getInverseAbsoluteTransformation();
		AffineMatrix4x4 backToScene = referenceFrame.getAbsoluteTransformation();
		Point3 originalPosition = new Point3( originalPositionIn );
		toReferenceFrame.transform( originalPosition );
		Point3 returnSnapPosition = new Point3( originalPosition );
		boolean didSnap = false;
		Point3 currentObjectTranslation = toSnap.getAbsoluteTransformation().translation;
		toReferenceFrame.transform( currentObjectTranslation );
		Vector3 movementDelta = Vector3.createSubtraction( originalPosition, currentObjectTranslation );
		if( movementDelta.x != 0 ) {
			double currentPos = originalPosition.x;

			int lowerMultiplier = (int)( currentPos / gridSpacing );
			int upperMultiplier = ( currentPos < 0 ) ? lowerMultiplier - 1 : lowerMultiplier + 1;
			double lowerSnap = gridSpacing * lowerMultiplier;
			double upperSnap = gridSpacing * upperMultiplier;
			if( Math.abs( lowerSnap - currentPos ) <= SNAP_TO_GRID_DISTANCE ) {
				didSnap = true;
				currentPos = gridSpacing * lowerMultiplier;
			} else if( Math.abs( upperSnap - currentPos ) <= SNAP_TO_GRID_DISTANCE ) {
				didSnap = true;
				currentPos = gridSpacing * upperMultiplier;
			}

			returnSnapPosition.x = currentPos;
		}
		//Don't snap in Y
		//		if( movementDelta.y != 0 )
		//		{
		//			double currentPos = originalPosition.y;
		//
		//			int lowerMultiplier = (int)( currentPos / gridSpacing );
		//			int upperMultiplier = ( currentPos < 0 ) ? lowerMultiplier - 1 : lowerMultiplier + 1;
		//			double lowerSnap = gridSpacing * lowerMultiplier;
		//			double upperSnap = gridSpacing * upperMultiplier;
		//			if( Math.abs( lowerSnap - currentPos ) <= SNAP_TO_GRID_DISTANCE )
		//			{
		//				didSnap = true;
		//				currentPos = gridSpacing * lowerMultiplier;
		//			}
		//			else if( Math.abs( upperSnap - currentPos ) <= SNAP_TO_GRID_DISTANCE )
		//			{
		//				didSnap = true;
		//				currentPos = gridSpacing * upperMultiplier;
		//			}
		//
		//			returnSnapPosition.y = currentPos;
		//		}
		if( movementDelta.z != 0 ) {
			double currentPos = originalPosition.z;

			int lowerMultiplier = (int)( currentPos / gridSpacing );
			int upperMultiplier = ( currentPos < 0 ) ? lowerMultiplier - 1 : lowerMultiplier + 1;
			double lowerSnap = gridSpacing * lowerMultiplier;
			double upperSnap = gridSpacing * upperMultiplier;
			if( Math.abs( lowerSnap - currentPos ) <= SNAP_TO_GRID_DISTANCE ) {
				didSnap = true;
				currentPos = gridSpacing * lowerMultiplier;
			} else if( Math.abs( upperSnap - currentPos ) <= SNAP_TO_GRID_DISTANCE ) {
				didSnap = true;
				currentPos = gridSpacing * upperMultiplier;
			}

			returnSnapPosition.z = currentPos;
		}
		backToScene.transform( returnSnapPosition );
		return returnSnapPosition;

	}

	public static Point3 doMovementSnapping( AbstractTransformable t, Point3 currentPosition, AbstractDragAdapter dragAdapter, ReferenceFrame referenceFrame, AbstractCamera camera ) {
		Point3 snapPosition = new Point3( currentPosition );
		if( dragAdapter != null ) {
			//Try snapping to various snaps
			if( dragAdapter.shouldSnapToGround() ) {
				snapPosition = SnapUtilities.snapObjectToGround( t, currentPosition, referenceFrame );
			}
			if( dragAdapter.shouldSnapToGrid() ) {
				snapPosition = SnapUtilities.snapObjectToGrid( t, snapPosition, dragAdapter.getGridSpacing(), referenceFrame );
			}
			//Visualize any snapping that happened
			if( camera != null ) {
				SnapUtilities.showSnapLines( camera, currentPosition, snapPosition, referenceFrame );
			}
		}
		//Apply the new snap position
		return snapPosition;
	}

	private static java.util.List<Vector3> getSnapVectors( Vector3 guideForwardAxis, Vector3 guideUpAxis, Angle snapAmount ) {
		java.util.List<Vector3> snapVectors = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		OrthogonalMatrix3x3 rotationMatrix = OrthogonalMatrix3x3.createFromForwardAndUpGuide( guideForwardAxis, guideUpAxis );
		double currentAngle = 0;
		while( currentAngle < 360d ) {
			snapVectors.add( Vector3.createMultiplication( rotationMatrix.backward, -1 ) );
			rotationMatrix.applyRotationAboutYAxis( snapAmount );
			currentAngle += snapAmount.getAsDegrees();
		}
		return snapVectors;
	}

	private static Vector3 snapAxis( Vector3 inputAxis, Vector3 guideForwardAxis, Vector3 guideUpAxis, Angle snapDegrees ) {
		java.util.List<Vector3> snapVectors = getSnapVectors( guideForwardAxis, guideUpAxis, snapDegrees );
		for( Vector3 snapVector : snapVectors )
		{
			AngleInRadians angleBetween = VectorUtilities.getAngleBetweenVectors( inputAxis, snapVector );
			if( Math.abs( angleBetween.getAsRadians() ) <= ANGLE_SNAP_DISTANCE_IN_RADIANS )
			{
				return snapVector;
			}
		}
		return inputAxis;
	}

	//	public static Vector3 snapObjectRotation(Vector3 preRotateForward, Vector3 currentForward, Vector3 rotationAxis, Angle snapAngle, ReferenceFrame referenceFrame)
	//	{
	//		AffineMatrix4x4 toReferenceFrame = referenceFrame.getInverseAbsoluteTransformation();
	//		AffineMatrix4x4 referenceFrameTransform = referenceFrame.getAbsoluteTransformation();
	//		OrthogonalMatrix3x3 currentOrientationInReferenceFrame = new OrthogonalMatrix3x3(currentOrientation);
	//		toReferenceFrame.applyOrientation(currentOrientationInReferenceFrame);
	//		boolean didSnap = false;
	//		OrthogonalMatrix3x3 originalOrientationInReferenceFrame = preRotateTransform.orientation;
	//		toReferenceFrame.applyOrientation(originalOrientationInReferenceFrame);
	//
	//		Vector3 snapRightAxis = currentOrientation.right;
	//		Vector3 snapUpAxis = currentOrientation.up;
	//		Vector3 snapBackwardAxis = currentOrientation.backward;
	//		if (!originalOrientationInReferenceFrame.right.isWithinEpsilonOf(currentOrientationInReferenceFrame.right, MIN_SNAP_DELTA))
	//		{
	//			snapRightAxis = snapAxis(currentOrientation.right, referenceFrameTransform.orientation.right, referenceFrameTransform.orientation.up, snapAngle);
	//		}
	//		if (!originalOrientationInReferenceFrame.up.isWithinEpsilonOf(currentOrientationInReferenceFrame.up, MIN_SNAP_DELTA))
	//		{
	//			snapUpAxis = snapAxis(currentOrientation.up, referenceFrameTransform.orientation.up, referenceFrameTransform.orientation.backward, snapAngle);
	//		}
	//		if (!originalOrientationInReferenceFrame.backward.isWithinEpsilonOf(currentOrientationInReferenceFrame.backward, MIN_SNAP_DELTA))
	//		{
	//			snapBackwardAxis = snapAxis(currentOrientation.backward, referenceFrameTransform.orientation.backward, referenceFrameTransform.orientation.up, snapAngle);
	//		}
	//		return new OrthogonalMatrix3x3(snapRightAxis, snapUpAxis, snapBackwardAxis);
	//	}

	//	public static OrthogonalMatrix3x3 doRotationSnapping(AffineMatrix4x4 preRotateTransform, OrthogonalMatrix3x3 currentOrientation, AbstractDragAdapter dragAdapter, ReferenceFrame referenceFrame, AbstractCamera camera)
	//	{
	//		OrthogonalMatrix3x3 snapOrientation = new OrthogonalMatrix3x3(currentOrientation);
	//		
	//		//Try snapping to various snaps
	//		if (dragAdapter.getSnapState().shouldSnapToRotation())
	//		{
	//			snapOrientation = SnapUtilities.snapObjectRotation(preRotateTransform, currentOrientation, dragAdapter.getSnapState().getRotationSnapAngle(), referenceFrame);
	//		}
	//		//Visualize any snapping that happened
	////		if (camera != null)
	////		{
	////			SnapUtilities.showSnaprotation(camera, currentOrientation, snapOrientation, referenceFrame);
	////		}
	//		//Apply the new snap position
	//		return snapOrientation;
	//	}

	public static Angle snapObjectToAngle( Angle currentAngle, Angle snapAngleAmount ) {
		double currentAngleInRadians = currentAngle.getAsRadians();
		double snapAmountInRadians = snapAngleAmount.getAsRadians();

		int lowerMultiplier = (int)( currentAngleInRadians / snapAmountInRadians );
		int upperMultiplier = ( currentAngleInRadians < 0 ) ? lowerMultiplier - 1 : lowerMultiplier + 1;
		double lowerSnap = snapAmountInRadians * lowerMultiplier;
		double upperSnap = snapAmountInRadians * upperMultiplier;
		if( Math.abs( lowerSnap - currentAngleInRadians ) <= ANGLE_SNAP_DISTANCE_IN_RADIANS ) {
			return new AngleInRadians( lowerSnap );
		}
		if( Math.abs( upperSnap - currentAngleInRadians ) <= ANGLE_SNAP_DISTANCE_IN_RADIANS ) {
			return new AngleInRadians( upperSnap );
		}
		return currentAngle;
	}

	public static void showSnapRotation( RotationRingHandle rotationHandle ) {
		AffineMatrix4x4 handleTransform = rotationHandle.getAbsoluteTransformation();
		Vector3 snapDirection = Vector3.createMultiplication( handleTransform.orientation.backward, rotationHandle.getRadius() * -1 );
		Point3 snapSphereLocation = Point3.createAddition( handleTransform.translation, snapDirection );
		showSnapSphere( snapSphereLocation, handleTransform.translation, rotationHandle.getRoot() );
	}

	public static Angle doRotationSnapping( Angle currentAngle, AbstractDragAdapter dragAdapter ) {
		Angle snapAngle = new AngleInRadians( currentAngle );
		//Try snapping to various snaps
		if( dragAdapter != null ) {
			if( dragAdapter.shouldSnapToRotation() ) {
				snapAngle = snapObjectToAngle( currentAngle, dragAdapter.getRotationSnapAngle() );
			}
		}
		return snapAngle;
	}

}
