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

import java.awt.Color;
import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.ImageBasedManipulationHandle2D;
import org.alice.interact.handle.ManipulationHandle2D;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.Vector2;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public abstract class Camera2DDragManipulator extends CameraManipulator2D {

	protected static final double MIN_AMOUNT_TO_MOVE = .005d;
	protected static final double WORLD_DISTANCE_PER_PIXEL_SECONDS = .1d;
	protected static final double RADIANS_PER_PIXEL_SECONDS = .02d;
	
	protected Vector3 initialMoveFactor = new Vector3(0.0d, 0.0d, 0.0d);
	protected Vector3 initialRotateFactor = new Vector3(0.0d, 0.0d, 0.0d);
	protected static final double INITIAL_MOVE_FACTOR = 10.0d;
	protected static final double INITIAL_ROTATE_FACTOR = 8.0d;
	
	protected Transformable standUpReference = new Transformable();
	protected Color initialHandleColor = null;
	protected Vector2 initialMousePosition = new Vector2();
	
	public Camera2DDragManipulator( ImageBasedManipulationHandle2D handle)
	{
		super(handle);
	}
	
	@Override
	protected abstract void initializeEventMessages();
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {

	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if (this.getCamera() != null)
		{
			this.initializeEventMessages();
			this.standUpReference.setParent( this.getCamera().getParent() );
			this.standUpReference.localTransformation.setValue( AffineMatrix4x4.createIdentity() );
			this.standUpReference.setAxesOnlyToStandUp();
			this.initialMousePosition.x = startInput.getMouseLocation().x;
			this.initialMousePosition.y = startInput.getMouseLocation().y;
			if (this.handle instanceof ImageBasedManipulationHandle2D)
			{
				this.initialHandleColor = ((ImageBasedManipulationHandle2D)this.handle).getColor( (int)this.initialMousePosition.x, (int)this.initialMousePosition.y );
			}
			this.initialMoveFactor = this.getMovementVectorForColor( this.initialHandleColor );
			this.initialRotateFactor = this.getRotationVectorForColor( this.initialHandleColor );
			return true;
		}
		return false;
	}

	protected abstract Vector3 getRotationVectorForColor(Color color);
	protected abstract Vector3 getMovementVectorForColor(Color color);
	protected abstract Vector3 getRelativeMovementAmount(Vector2 mousePos, double time);
	protected abstract Vector3 getRelativeRotationAmount(Vector2 mousePos, double time);
	protected abstract ReferenceFrame getRotationReferenceFrame();
	protected abstract ReferenceFrame getMovementReferenceFrame();
	
	protected Vector3 getTotalMovementAmount(Vector2 mousePos, double time)
	{
		Vector3 relativeMovementAmount = this.getRelativeMovementAmount( mousePos, time );
		Vector3 amountToMoveInitial = Vector3.createMultiplication( this.initialMoveFactor, WORLD_DISTANCE_PER_PIXEL_SECONDS * time );
		Vector3 amountToMove = Vector3.createAddition(relativeMovementAmount, amountToMoveInitial);
		
		return amountToMove;
	}
	
	protected Vector3 getTotalRotationAmount(Vector2 mousePos, double time)
	{
		Vector3 relativeRotationAmount = this.getRelativeRotationAmount( mousePos, time );
		Vector3 amountToRotateInitial = Vector3.createMultiplication( this.initialRotateFactor, RADIANS_PER_PIXEL_SECONDS * time );
		Vector3 amountToRotate = Vector3.createAddition(relativeRotationAmount, amountToRotateInitial);
		
		return amountToRotate;
	}
	
	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		Vector2 mousePos = new Vector2( currentInput.getMouseLocation().x, currentInput.getMouseLocation().y);
//		Vector2 handleCenter = this.handle.getCenter();
//		toMouse.subtract( handleCenter );
		Vector3 moveVector = this.getTotalMovementAmount( mousePos, time );
		Vector3 rotateVector = this.getTotalRotationAmount( mousePos, time );
		this.manipulatedTransformable.applyTranslation( moveVector, this.getMovementReferenceFrame() );
		if (rotateVector.x != 0.0d)
			this.manipulatedTransformable.applyRotationAboutXAxis( new AngleInRadians(rotateVector.x), getRotationReferenceFrame() );
		if (rotateVector.y != 0.0d)
			this.manipulatedTransformable.applyRotationAboutYAxis( new AngleInRadians(rotateVector.y), getRotationReferenceFrame() );
		if (rotateVector.z != 0.0d)
			this.manipulatedTransformable.applyRotationAboutZAxis( new AngleInRadians(rotateVector.z), getRotationReferenceFrame() );
		for (ManipulationEvent event : this.manipulationEvents)
		{
			Vector3 dotVector = null;
			if (event.getType() == ManipulationEvent.EventType.Rotate)
			{
				dotVector = rotateVector;
			}
			else if (event.getType() == ManipulationEvent.EventType.Translate)
			{
				dotVector = moveVector;
			}
			if (dotVector != null)
			{
				Vector3 normalizedDotVector = new Vector3(dotVector);
				normalizedDotVector.normalize();
				double dot = Vector3.calculateDotProduct( event.getMovementDescription().direction.getVector(), normalizedDotVector );
				if (dot > 0.0d)
				{
					this.dragAdapter.triggerManipulationEvent( event, true );
				}
				else if ( dot <= 0.0d)
				{
					this.dragAdapter.triggerManipulationEvent( event, false );
				}
			}
		}
	}


}
