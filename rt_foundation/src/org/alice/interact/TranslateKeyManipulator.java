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

import java.awt.event.KeyEvent;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class TranslateKeyManipulator extends DragManipulator {
	
	protected static double MOVEMENT_RATE = 5.0d;
	protected static double CLICK_TIME = .1d;
	protected static double CLICK_MOVE_AMOUNT = .2d;
	
	protected Point3 initialPoint = new Point3();
	protected double startTime = 0.0d;
	MovementKey[] directionKeys;
	
	public TranslateKeyManipulator()
	{
		directionKeys = new MovementKey[0];
	}
	
	public TranslateKeyManipulator( MovementKey[] directionKeys )
	{
		setKeys(directionKeys);
	}
	
	public void setKeys(MovementKey[] directionKeys)
	{
		this.directionKeys = directionKeys;
	}
	
	public void addKeys( MovementKey[] directionKeys )
	{
		if (this.directionKeys != null && directionKeys != null)
		{
			MovementKey[] combinedKeys = new MovementKey[this.directionKeys.length + directionKeys.length];
			for (int i=0; i<this.directionKeys.length; i++)
			{
				combinedKeys[i] = this.directionKeys[i];
			}
			for (int i=0; i<directionKeys.length; i++)
			{
				combinedKeys[this.directionKeys.length+i] = directionKeys[i];
			}
			setKeys(combinedKeys);
			
		}
		else
		{
			setKeys(directionKeys);
		}
	}
	
	protected Point3[] getMoveDirection( InputState input )
	{
		MovementType[] movementTypes = MovementType.values();
		Point3[] moveDirs = new Point3[movementTypes.length];
		double[] multiplierTotals = new double[movementTypes.length];
		for (int i=0; i<moveDirs.length; i++ )
		{
			moveDirs[i] = new Point3(0.0d, 0.0d, 0.0d);
			multiplierTotals[i] = 0.0d;
		}
		for (int i=0; i<this.directionKeys.length; i++)
		{
			if (input.isKeyDown( this.directionKeys[i].keyValue ))
			{
				Point3 multipliedPoint = Point3.createMultiplication( this.directionKeys[i].direction, this.directionKeys[i].directionMultiplier );
				multiplierTotals[this.directionKeys[i].movementType.getIndex()] += this.directionKeys[i].directionMultiplier;
				moveDirs[this.directionKeys[i].movementType.getIndex()].add( multipliedPoint );
			}	
		}
//		for (int i=0; i<moveDirs.length; i++)
//		{
//			if (!(moveDirs[i].x == 0.0d && moveDirs[i].y == 0.0d && moveDirs[i].z == 0.0d))
//			{
//				moveDirs[i].multiply( 1.0d / multiplierTotals[i] );
//			}
//		}
		return moveDirs;
	}
	
	private void moveTransformable( Point3[] movementAmounts )
	{
		for (int i=0; i<movementAmounts.length; i++)
		{
			if (!(movementAmounts[i].x == 0.0d && movementAmounts[i].y == 0.0d && movementAmounts[i].z == 0.0d))
			{
				MovementType movementType = MovementType.getMovementTypeForIndex( i );
				if (movementType != null)
				{
					movementType.applyTranslation( this.manipulatedTransformable, movementAmounts[i] );
				}
			}
		}
	}
	
	@Override
	public void endManipulator( InputState endInput, InputState previousInput ) {
		double currentTime = System.currentTimeMillis() * .001d;
		if (currentTime - this.startTime < CLICK_TIME)
		{
			double distanceToMove = CLICK_TIME * MOVEMENT_RATE;
			Point3 positionDif = Point3.createSubtraction( this.manipulatedTransformable.getAbsoluteTransformation().translation, initialPoint );
			double distanceAlreadyMoved = positionDif.calculateMagnitude();
			if (distanceToMove > distanceAlreadyMoved)
			{
				Point3[] translateDirections = getMoveDirection(previousInput);
				for (Point3 direction : translateDirections) {
					direction.multiply( distanceToMove );
				}
				this.manipulatedTransformable.setTranslationOnly( initialPoint, this.manipulatedTransformable.getRoot() );
				moveTransformable( translateDirections );
			}
		}
		this.hasStarted = false;
	}

	@Override
	public void startManipulator( InputState startInput ) {
		if (this.manipulatedTransformable != null)
		{
			this.hasStarted = true;
			this.startTime = System.currentTimeMillis() * .001d;
			this.initialPoint.set(this.manipulatedTransformable.getAbsoluteTransformation().translation);
		}
	}

	@Override
	public void dataUpdateManipulator( InputState currentInput, InputState previousInput  ) {
//		if (this.manipulatedTransformable != null)
//		{
//			edu.cmu.cs.dennisc.math.AffineMatrix4x4 transform = this.manipulatedTransformable.localTransformation.getValue();
//			int xDelta = currentInput.getMouseLocation().x - previousInput.getMouseLocation().x;
//			int yDelta = currentInput.getMouseLocation().y - previousInput.getMouseLocation().y;
//			transform.applyTranslation( xDelta*.05d, yDelta*-.05d, 0.0d);
//			this.manipulatedTransformable.setLocalTransformation( transform );
//		}
	}

	@Override
	public void timeUpdateManipulator( double dTime, InputState currentInput ) {
		if (this.manipulatedTransformable != null && this.hasStarted)
		{
			Point3[] translateDirections = getMoveDirection(currentInput);
			for (Point3 direction : translateDirections) {
				direction.multiply( MOVEMENT_RATE * dTime  );
			}
			moveTransformable( translateDirections );
		}
		
	}

}
