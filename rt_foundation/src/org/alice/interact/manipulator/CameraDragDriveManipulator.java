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

import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.ImageBasedManipulationHandle2D;
import org.alice.interact.handle.ManipulationHandle2D;

import edu.cmu.cs.dennisc.math.Vector2;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;

/**
 * @author David Culyba
 */
public class CameraDragDriveManipulator extends Camera2DDragManipulator {

	protected static final Color UP = Color.RED;
	protected static final Color LEFT = Color.GREEN;
	protected static final Color RIGHT = Color.BLUE;
	protected static final Color DOWN = Color.WHITE;
	
	public CameraDragDriveManipulator( ImageBasedManipulationHandle2D handle)
	{
		super(handle);
	}
	
	@Override
	protected void initializeEventMessages()
	{
		this.manipulationEvents.clear();
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.BACKWARD, MovementType.STOOD_UP), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.FORWARD, MovementType.STOOD_UP), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Rotate, new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Rotate, new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP), this.manipulatedTransformable ) );
	}
	
	@Override
	protected Vector3 getMovementVectorForColor(Color color) {
		Vector3 initialMove = new Vector3(0.0d, 0.0d, 0.0d);
		if (color != null)
		{
			if (color.equals( UP ))
			{
				initialMove.z = -INITIAL_MOVE_FACTOR;
			}
			else if (color.equals( DOWN ))
			{
				initialMove.z = INITIAL_MOVE_FACTOR;
			}
		}
		return initialMove;
	}
	
	@Override
	protected Vector3 getRotationVectorForColor( Color color ) {
		Vector3 initialRotate = new Vector3(0.0d, 0.0d, 0.0d);
		if (color != null)
		{
			if (color.equals( LEFT ))
			{
				initialRotate.y = INITIAL_ROTATE_FACTOR;
			}
			else if (color.equals( RIGHT ))
			{
				initialRotate.y = -INITIAL_ROTATE_FACTOR;
			}
		}
		return initialRotate;
	}
	

	@Override
	protected Vector3 getRelativeMovementAmount(Vector2 mousePos, double time)
	{
		Vector2 relativeMousePos = Vector2.createSubtraction( mousePos, this.initialMousePosition );
		
		if (this.initialHandleColor.equals( LEFT ) || this.initialHandleColor.equals( RIGHT ))
		{
			if (Math.abs( relativeMousePos.y ) < MIN_PIXEL_MOVE_AMOUNT)
			{
				relativeMousePos.y = 0.0d;
			}
			else
			{
				if (relativeMousePos.y < 0.0d)
				{
					relativeMousePos.y += MIN_PIXEL_MOVE_AMOUNT;
				}
				else
				{
					relativeMousePos.y = MIN_PIXEL_MOVE_AMOUNT;
				}
			}
		}
		
		double amountToMoveZ = relativeMousePos.y * WORLD_DISTANCE_PER_PIXEL_SECONDS * time;
		Vector3 amountToMoveMouse = new Vector3 (0.0d, 0.0d, amountToMoveZ);
		return amountToMoveMouse;
	}
	
	@Override
	protected Vector3 getRelativeRotationAmount(Vector2 mousePos, double time)
	{
		Vector2 relativeMousePos = Vector2.createSubtraction( mousePos, this.initialMousePosition );
		
		if (this.initialHandleColor.equals( UP ) || this.initialHandleColor.equals( DOWN ))
		{
			if (Math.abs( relativeMousePos.x ) < MIN_PIXEL_MOVE_AMOUNT)
			{
				relativeMousePos.x = 0.0d;
			}
			else
			{
				if (relativeMousePos.x < 0.0d)
				{
					relativeMousePos.x += MIN_PIXEL_MOVE_AMOUNT;
				}
				else
				{
					relativeMousePos.x -= MIN_PIXEL_MOVE_AMOUNT;
				}
			}
		}
		
		double amountToRotateY = -relativeMousePos.x * RADIANS_PER_PIXEL_SECONDS * time;
		Vector3 amountToRotateMouse = new Vector3 (0.0d, amountToRotateY, 0.0d);
		return amountToRotateMouse;
	}
	
	@Override
	protected ReferenceFrame getRotationReferenceFrame()
	{
		return this.standUpReference;
	}
	
	@Override
	protected ReferenceFrame getMovementReferenceFrame()
	{
		return this.standUpReference;
	}

}
