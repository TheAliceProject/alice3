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
package org.alice.interact.manipulator;

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.VectorUtilities;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;

/**
 * @author David Culyba
 */
public class ObjectTranslateDragManipulator extends AbstractManipulator implements CameraInformedManipulator {

	protected static final double BAD_ANGLE_THRESHOLD = 2.0d*Math.PI * (8.0d/360.0d);
	protected static final double MIN_BAD_ANGLE_THRESHOLD = 0.0d;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	protected Point3 initialClickPoint = new Point3();
	protected Point3 initialObjectPosition = new Point3();
	protected Plane movementPlane = new edu.cmu.cs.dennisc.math.Plane( 0.0d, 1.0d, 0.0d, 0.0d );
	protected Plane badAnglePlane = null; 
	protected Point3 offsetToOrigin = null;
	protected Point initialMouseLocation = new Point();
	protected Boolean hasMoved = false;
	
	public AbstractCamera getCamera()
	{
		if( this.onscreenLookingGlass != null )
		{
			return onscreenLookingGlass.getCameraAt( 0 );
		} 
		return null;
	}

	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
	}
	
	@Override
	protected void initializeEventMessages()
	{
		this.manipulationEvents.clear();
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.LEFT, MovementType.ABSOLUTE), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.RIGHT, MovementType.ABSOLUTE), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.FORWARD, MovementType.ABSOLUTE), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.BACKWARD, MovementType.ABSOLUTE), this.manipulatedTransformable ) );
	}
	
	protected Point3 getPositionForPlane( Plane movementPlane, Ray pickRay )
	{
		if (pickRay != null)
		{
			Point3 pointInPlane = PlaneUtilities.getPointInPlane( movementPlane, pickRay );
			Point3 newPosition = Point3.createAddition( this.offsetToOrigin, pointInPlane );
			newPosition.y = this.initialObjectPosition.y;
			return newPosition;
		}
		else
		{
			return null;
		}
	}
	
	protected Point3 getPositionBasedonOnMouseLocation( Point mouseLocation )
	{
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.getCamera(), mouseLocation.x, mouseLocation.y );
		if (pickRay != null)
		{
			Plane toMoveIn = this.movementPlane;
			double badAngleAmount = this.getBadAngleAmount( this.movementPlane, pickRay );
			if (badAngleAmount > 0.0d)
			{
				Vector3 newNormal = Vector3.createInterpolation( PlaneUtilities.getPlaneNormal( this.movementPlane ), PlaneUtilities.getPlaneNormal( this.badAnglePlane ), badAngleAmount );
				newNormal.normalize();
				toMoveIn = new Plane(this.initialClickPoint, newNormal);
			}
			Point3 newPosition = this.getPositionForPlane( toMoveIn, pickRay );
			return newPosition;
		}
		else
		{
			return null;
		}
	}
	
	protected double getBadAngleAmount( Plane plane, Ray pickRay )
	{
		Vector3 cameraDirection = this.getCamera().getAbsoluteTransformation().orientation.backward;
		double cameraDistanceFactor = PlaneUtilities.distanceToPlane( plane, this.getCamera().getAbsoluteTransformation().translation );
		Vector3 planeNormal = PlaneUtilities.getPlaneNormal( plane );
		AngleInRadians angleBetweenVector = VectorUtilities.getAngleBetweenVectors(cameraDirection, planeNormal);
		double distanceToRightAngle = Math.abs(Math.PI*.5d - angleBetweenVector.getAsRadians());
		
		double scaledBadAngleThreshold = BAD_ANGLE_THRESHOLD / cameraDistanceFactor;
		double scaledMinBadAngleThreshold = MIN_BAD_ANGLE_THRESHOLD / cameraDistanceFactor;
		
		if (distanceToRightAngle < scaledBadAngleThreshold)
		{
			if (distanceToRightAngle < scaledMinBadAngleThreshold)
			{
				return 1.0d;
			}
			distanceToRightAngle -= scaledMinBadAngleThreshold;
			double thresholdDif = scaledBadAngleThreshold - scaledMinBadAngleThreshold;
			return (thresholdDif - distanceToRightAngle) / thresholdDif;
		}
		else
		{
			return 0.0d;
		}
	}
	
	protected Plane createCameraFacingStoodUpPlane( Point3 clickPoint )
	{
		return new Plane(clickPoint, createCameraFacingStoodUpVector());
	}
	
	protected Vector3 createCameraFacingStoodUpVector()
	{
		Vector3 cameraBackward = this.getCamera().getAxes( AsSeenBy.SCENE ).backward;
		if (EpsilonUtilities.isWithinReasonableEpsilon( cameraBackward.y, 1.0d )) //handle case where camera is pointing down or up
		{
			return Vector3.createMultiplication( this.getCamera().getAbsoluteTransformation().orientation.up, -1.0d );
		}
		else
		{
			cameraBackward.y = 0.0f;
			cameraBackward.normalize();
			return cameraBackward;
		}
	}
	
	protected Plane createBadAnglePlane( Point3 clickPoint )
	{
		Vector3 badPlaneNormal = createCameraFacingStoodUpVector();
		badPlaneNormal.y += .5d;  //Make the bad plane slightly tilted so moving the mouse will always move the object in the plane
		badPlaneNormal.normalize();
		return new Plane( clickPoint, badPlaneNormal );
	}
	
	protected Plane createPickPlane( Point3 clickPoint )
	{
		return new Plane(clickPoint, Vector3.createPositiveYAxis());
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) && this.manipulatedTransformable != null)
		{
			if (!this.hasMoved)
			{
				this.hasMoved = true;
			}
				
			Point3 newPosition = getPositionBasedonOnMouseLocation( currentInput.getMouseLocation() );
			//Send manipulation events
			Vector3 movementDif = Vector3.createSubtraction( newPosition, this.manipulatedTransformable.getAbsoluteTransformation().translation);
			movementDif.normalize();
			for (ManipulationEvent event : this.manipulationEvents)
			{
				double dot = Vector3.calculateDotProduct( event.getMovementDescription().direction.getVector(), movementDif );
				if (dot > 0.1d)
				{
					this.dragAdapter.triggerManipulationEvent( event, true );
				}
				else if ( dot < -.07d)
				{
					this.dragAdapter.triggerManipulationEvent( event, false );
				}
			}
			
			if (newPosition != null)
			{
				this.manipulatedTransformable.setTranslationOnly( newPosition, AsSeenBy.SCENE );
			}
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput  ) 
	{
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.manipulatedTransformable = startInput.getClickPickTransformable();	
		if (this.manipulatedTransformable != null)
		{
			this.initializeEventMessages();
			this.initialMouseLocation.setLocation( startInput.getMouseLocation() ); 
			this.hasMoved = false;
			this.initialObjectPosition.set( this.manipulatedTransformable.getAbsoluteTransformation().translation );
			startInput.getClickPickResult().getPositionInSource(this.initialClickPoint);
			startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
			this.movementPlane = createPickPlane(this.initialClickPoint);
			this.badAnglePlane = createBadAnglePlane(this.initialClickPoint);
			
			Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			if (pickRay != null)
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.movementPlane, pickRay );
				this.offsetToOrigin = Point3.createSubtraction( this.manipulatedTransformable.getAbsoluteTransformation().translation, pointInPlane );
			}
			else 
			{
				this.manipulatedTransformable = null;
			}
			if (this.manipulatedTransformable != null)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return HandleSet.GROUND_TRANSLATION_VISUALIZATION;
	}


}
