/**
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.interact;

import java.awt.Point;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class LinearDragManipulator extends DragManipulator implements CameraInformedManipulator {

	protected AbstractCamera camera = null;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	protected LinearDragHandle linearHandle;
	protected Vector3 absoluteDragAxis;
	protected Point3 initialClickPoint = new Point3();
	protected Point3 originalOrigin;
	protected Plane cameraFacingPlane;
	protected Plane handleAlignedPlane;
	
	public void setCamera( AbstractCamera camera ) {
		this.camera = camera;
	}

	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
	}
	
	protected double getDistanceAlongAxisBasedOnMouse( Point mouseLocation )
	{
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.camera, mouseLocation.x, mouseLocation.y );
		if (pickRay != null)
		{
			double axisCameraDot = Vector3.calculateDotProduct( this.absoluteDragAxis, this.camera.getAbsoluteTransformation().orientation.backward );
			if (Math.abs( axisCameraDot ) > .98d )
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.cameraFacingPlane, pickRay );
				Vector3 fromOriginalMouseToCurrentMouse = Vector3.createSubtraction( pointInPlane, this.initialClickPoint );
				Vector3 dragRightAxis = this.camera.getAbsoluteTransformation().orientation.right;
				dragRightAxis.normalize();
				Vector3 dragUpAxis = this.camera.getAbsoluteTransformation().orientation.up;
				dragUpAxis.normalize();
				
				double leftRightSign = 1.0d;
				if (Vector3.calculateDotProduct( this.absoluteDragAxis, dragRightAxis ) < 0.0d)
				{
					leftRightSign = -1.0d;
				}
				double upDownSign = 1.0d;
				if (Vector3.calculateDotProduct( this.absoluteDragAxis, dragUpAxis ) < 0.0d)
				{
					upDownSign = -1.0d;
				}
				
				double mouseYDistance = upDownSign * Vector3.calculateDotProduct( fromOriginalMouseToCurrentMouse, dragUpAxis );
				double mouseXDistance = leftRightSign * Vector3.calculateDotProduct( fromOriginalMouseToCurrentMouse, dragRightAxis );
				return this.linearHandle.getCurrentHandleLength() + mouseYDistance + mouseXDistance;
			}
			else
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.handleAlignedPlane, pickRay );
				if (pointInPlane != null)
				{
					Vector3 pointVector = Vector3.createSubtraction( pointInPlane, this.originalOrigin );
					double dragAmount = Vector3.calculateDotProduct( pointVector, this.absoluteDragAxis );
					return dragAmount;
				}
			}
		}
		return 0;
	}
	
	protected void updateBasedOnHandlePull( double previousPull, double newPull )
	{
		Vector3 translation = Vector3.createMultiplication( this.linearHandle.getDragAxis(), (newPull - previousPull));
		this.manipulatedTransformable.applyTranslation( translation, this.linearHandle.getReferenceFrame() );
	}
	
	@Override
	public void dataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) )
		{
			double currentDistance = getDistanceAlongAxisBasedOnMouse( currentInput.getMouseLocation() );
			double previousDistance  = getDistanceAlongAxisBasedOnMouse( previousInput.getMouseLocation() );
			updateBasedOnHandlePull(previousDistance, currentDistance);
		}

	}

	@Override
	public void endManipulator( InputState endInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startManipulator( InputState startInput ) {
		Transformable clickedHandle = PickHint.HANDLES.getMatchingTransformable( startInput.getClickPickedTransformable(true) );
		if (clickedHandle instanceof LinearDragHandle)
		{
			this.linearHandle = (LinearDragHandle)clickedHandle;
			this.manipulatedTransformable = this.linearHandle.getManipulatedObject();
			this.absoluteDragAxis = this.linearHandle.getReferenceFrame().getAbsoluteTransformation().createTransformed( this.linearHandle.getDragAxis() );
			
			startInput.getClickPickResult().getPositionInSource(this.initialClickPoint);
			startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
			
			Vector3 toCamera = Vector3.createSubtraction( this.camera.getAbsoluteTransformation().translation, this.manipulatedTransformable.getAbsoluteTransformation().translation );
			toCamera.normalize();
			Vector3 axisAlignedNormal = null;
			if (Math.abs(Vector3.calculateDotProduct( toCamera, this.absoluteDragAxis )) > .99d)
			{
				axisAlignedNormal = toCamera;
			}
			else
			{
				Vector3 axisAlignedCameraVector = VectorUtilities.projectOntoVector( toCamera, this.absoluteDragAxis );
				Vector3 awayFromAxis = Vector3.createSubtraction( toCamera, axisAlignedCameraVector );
				awayFromAxis.normalize();
				axisAlignedNormal = awayFromAxis;
			}
			this.handleAlignedPlane = new Plane(this.linearHandle.getAbsoluteTransformation().translation, axisAlignedNormal);
			this.cameraFacingPlane = new Plane( this.initialClickPoint, this.camera.getAbsoluteTransformation().orientation.backward);
			this.originalOrigin = this.manipulatedTransformable.getAbsoluteTransformation().translation; 
		}
	}

	@Override
	public void timeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub

	}

}
