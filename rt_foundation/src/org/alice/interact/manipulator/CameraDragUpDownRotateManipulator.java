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

import edu.cmu.cs.dennisc.math.Vector2;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;

/**
 * @author David Culyba
 */
public class CameraDragUpDownRotateManipulator extends Camera2DDragManipulator {

	protected static final Color FORWARD = Color.RED;
	protected static final Color BACKWARD = Color.GREEN;
	
	public CameraDragUpDownRotateManipulator( ImageBasedManipulationHandle2D handle)
	{
		super(handle);
	}
	
	@Override
	protected void initializeEventMessages()
	{
		this.manipulationEvents.clear();
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Rotate, new MovementDescription(MovementDirection.LEFT, MovementType.LOCAL), this.manipulatedTransformable ) );
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Rotate, new MovementDescription(MovementDirection.RIGHT, MovementType.LOCAL), this.manipulatedTransformable ) );
	}
	
	@Override
	protected Vector3 getRotationVectorForColor(Color color) {
		Vector3 initialRotate = new Vector3(0.0d, 0.0d, 0.0d);
		if (color != null)
		{
			if (color.equals( FORWARD ))
			{
				initialRotate.x = -INITIAL_ROTATE_FACTOR;
			}
			else if (color.equals( BACKWARD ))
			{
				initialRotate.x = INITIAL_ROTATE_FACTOR;
			}
		}
		return initialRotate;
	}
	
	@Override
	protected Vector3 getMovementVectorForColor( Color color ) {
		return new Vector3(0.0d, 0.0d, 0.0d);
	}
	
	@Override
	protected Vector3 getRelativeMovementAmount(Vector2 mousePos, double time)
	{
		return new Vector3(0.0d, 0.0d, 0.0d);
	}
	
	@Override
	protected Vector3 getRelativeRotationAmount(Vector2 mousePos, double time)
	{
		Vector2 relativeMousePos = Vector2.createSubtraction( mousePos, this.initialMousePosition );
		double amountToRotateX = relativeMousePos.y * RADIANS_PER_PIXEL_SECONDS * time;
		Vector3 amountToRotateMouse = new Vector3 (amountToRotateX, 0.0d, 0.0d);
		return amountToRotateMouse;
	}
	
	@Override
	protected ReferenceFrame getRotationReferenceFrame()
	{
		return this.getManipulatedTransformable();
	}
	
	@Override
	protected ReferenceFrame getMovementReferenceFrame()
	{
		return this.getManipulatedTransformable();
	}
	

//	@Override
//	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
//		Vector2 toMouse = new Vector2( currentInput.getMouseLocation().x, currentInput.getMouseLocation().y);
//		double awayFromCenter = currentInput.getMouseLocation().y - this.handle.getCenter().y;
//		double amountToMove = awayFromCenter * WORLD_DISTANCE_PER_PIXEL_SECONDS * time;
//		if (Math.abs(amountToMove) > MIN_AMOUNT_TO_MOVE)
//		{
//			Angle rotationDelta = new AngleInRadians(amountToMove);
//			this.manipulatedTransformable.applyRotationAboutXAxis( rotationDelta, this.manipulatedTransformable );
//			
//			// Index 0 : FORWARD
//			// Index 1 : BACKWARD
//			if (amountToMove > 0.0d)
//			{
//				this.dragAdapter.triggerManipulationEvent( this.manipulationEvents.get( 0 ), true );
//				this.dragAdapter.triggerManipulationEvent( this.manipulationEvents.get( 1 ), false );
//			}
//			else
//			{
//				this.dragAdapter.triggerManipulationEvent( this.manipulationEvents.get( 0 ), false );
//				this.dragAdapter.triggerManipulationEvent( this.manipulationEvents.get( 1 ), true );
//			}
//		}
//		else
//		{
//			for (ManipulationEvent event : this.manipulationEvents)
//			{
//				this.dragAdapter.triggerManipulationEvent( event, false );
//			}
//		}
//	}
	
}
