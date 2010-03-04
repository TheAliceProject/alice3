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
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;

/**
 * @author David Culyba
 */
public class OmniDirectionalDragManipulator extends AbstractManipulator implements CameraInformedManipulator {

	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	
	protected Plane pickPlane = new edu.cmu.cs.dennisc.math.Plane( 0.0d, 1.0d, 0.0d, 0.0d );
	protected Point3 offsetToOrigin = null;
	protected Boolean hasMoved = false;
	
	public AbstractCamera getCamera()
	{
		if( this.onscreenLookingGlass != null )
		{
			return onscreenLookingGlass.getCameraAt( 0 );
		} 
		return null;
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Move";
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
	
	protected Plane createCameraPickPlane( Point3 clickPoint )
	{
		Vector3 clickPlaneNormal = this.getCamera().getAxes( AsSeenBy.SCENE ).backward;
//		clickPlaneNormal.y += 2d;  //Make the bad plane slightly tilted so moving the mouse will always move the object in the plane
		clickPlaneNormal.normalize();
		return new Plane( clickPoint, clickPlaneNormal );
	}
	
	protected Plane createLevelPickPlane( Point3 clickPoint )
	{
		Vector3 levelPlaneNormal = MovementDirection.UP.getVector();
//		clickPlaneNormal.y += 2d;  //Make the bad plane slightly tilted so moving the mouse will always move the object in the plane
		levelPlaneNormal.normalize();
		return new Plane( clickPoint, levelPlaneNormal );
	}
	
	private Vector3 getMouseMovementFromVector(Point mouseVector)
	{
		if (mouseVector.x == 0 && mouseVector.y == 0)
		{
			return new Vector3(0,0,0);
		}
		
		Vector3 mouseRelativeMovement = new Vector3(mouseVector.x, 0d, mouseVector.y);
		getCamera().getRoot().transformFrom_AffectReturnValuePassedIn( mouseRelativeMovement, getCamera());
		mouseRelativeMovement.y = 0d;
		mouseRelativeMovement.normalize();
		
		double MOVEMENT_SCALE = .02d;
		double movementAmount = mouseVector.distance( 0f, 0f ) * MOVEMENT_SCALE;
		mouseRelativeMovement.multiply( movementAmount );
		
		if (mouseRelativeMovement.isNaN())
		{
			System.out.println("NaN!");
		}
		
		return mouseRelativeMovement;
	}
	
	private Point3 getPickBasedVector(InputState currentInput, InputState previousInput)
	{
		boolean usePickForVertical = true;
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.getCamera(), currentInput.getMouseLocation().x, currentInput.getMouseLocation().y );
		if (pickRay == null)
		{
			return new Point3();
		}
		
		Vector3 upDownVector = new Vector3(this.getCamera().getAxes( AsSeenBy.SCENE ).backward);
		upDownVector.negate();
		upDownVector.y = 0d;
		upDownVector.normalize();
		
		Point mouseMovement = new Point(0, currentInput.getMouseLocation().y - previousInput.getMouseLocation().y);
		Vector3 mouseVertical = getMouseMovementFromVector(mouseMovement);
		
		Point3 currentObjectOffsetPoint = Point3.createSubtraction( this.manipulatedTransformable.getAbsoluteTransformation().translation , this.offsetToOrigin);
		
		Plane cameraPickPlane = this.createCameraPickPlane( currentObjectOffsetPoint );
		Point3 cameraMovementVector = PlaneUtilities.getPointInPlane( cameraPickPlane, pickRay );
		cameraMovementVector.subtract( currentObjectOffsetPoint );
		cameraMovementVector.y = 0f;
		Vector3 cameraMovementProjection = VectorUtilities.projectOntoVector( new Vector3(cameraMovementVector), upDownVector );
		double cameraMovementDotProd = Vector3.calculateDotProduct( cameraMovementProjection, mouseVertical );
		boolean cameraMovementIsValid = true;
		if (cameraMovementDotProd < 0)
		{
			cameraMovementIsValid = false;
		}
		
		Plane levelPickPlane = this.createLevelPickPlane( currentObjectOffsetPoint );
		Point3 levelMovementVector = PlaneUtilities.getPointInPlane( levelPickPlane, pickRay );
		boolean levelMovementIsValid = true;
		Vector3 levelMovementProjection = null;
		if (levelMovementVector != null)
		{
			levelMovementVector.subtract( currentObjectOffsetPoint );
			levelMovementVector.y = 0f;
			
			levelMovementProjection = VectorUtilities.projectOntoVector( new Vector3(levelMovementVector), upDownVector );
			double levelMovementDotProd = Vector3.calculateDotProduct( levelMovementProjection, mouseVertical );
			if (levelMovementDotProd < 0)
			{
				levelMovementIsValid = false;
			}
		}
		else
		{
			levelMovementIsValid = false;
		}
		
		Point3 movementVector = new Point3(0,0,0);
		Vector3 movementProjection = new Vector3(0,0,0);
		if (cameraMovementIsValid && !levelMovementIsValid)
		{
			movementVector = cameraMovementVector;
			movementProjection = cameraMovementProjection;
		} 
		else if (!cameraMovementIsValid && levelMovementIsValid)
		{
			movementVector = levelMovementVector;
			movementProjection = levelMovementProjection;
		}
		else if (cameraMovementIsValid && levelMovementIsValid)
		{
			double cameraMag = cameraMovementProjection.calculateMagnitude();
			double levelMag = levelMovementProjection.calculateMagnitude();
			if (levelMag > cameraMag && Math.abs( levelMag - cameraMag ) < 1)
			{
				movementVector = levelMovementVector;
				movementProjection = levelMovementProjection;
			}
			else
			{
//				movementVector = cameraMovementVector;
//				movementProjection = cameraMovementProjection;
				movementVector = levelMovementVector;
				movementProjection = levelMovementProjection;
			}
		}
		
		
		
		
		double cameraAngleApproximation = Math.abs( this.getCamera().getAxes( AsSeenBy.SCENE ).backward.y );
		if (cameraAngleApproximation < .1d)
		{
			usePickForVertical = false;
		}
		
		if (!usePickForVertical)
		{
			movementVector.subtract( movementProjection );
			movementVector.add( mouseVertical );
		}
		return movementVector;
		
	}
	
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) && this.manipulatedTransformable != null)
		{
			if (!this.hasMoved)
			{
				this.hasMoved = true;
			}
				
			Point3 movementVector = getPickBasedVector( currentInput, previousInput );
			Point3 currentPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation;
			Point3 newPosition = Point3.createAddition( currentPosition, movementVector );
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
			this.hasMoved = false;
			Point3 initialClickPoint = new Point3();
			startInput.getClickPickResult().getPositionInSource(initialClickPoint);
			startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
			this.pickPlane = createCameraPickPlane(initialClickPoint);
			
			Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.getCamera(), startInput.getMouseLocation().x, startInput.getMouseLocation().y );
			if (pickRay != null)
			{
				this.offsetToOrigin = Point3.createSubtraction( this.manipulatedTransformable.getAbsoluteTransformation().translation, initialClickPoint );
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
