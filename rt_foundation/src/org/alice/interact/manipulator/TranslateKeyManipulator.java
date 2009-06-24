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

import org.alice.interact.InputState;
import org.alice.interact.MovementKey;
import org.alice.interact.MovementType;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;

/**
 * @author David Culyba
 */
public class TranslateKeyManipulator extends AbstractManipulator {
	
	protected static double MOVEMENT_RATE = 5.0d;
	protected static double CLICK_TIME = .1d;
	protected static double CLICK_MOVE_AMOUNT = .2d;
	
	protected Point3 initialPoint = new Point3();
	protected double startTime = 0.0d;
	MovementKey[] directionKeys;
	
	protected AxisAlignedBox bounds;
	
	public TranslateKeyManipulator()
	{
		directionKeys = new MovementKey[0];
	}
	
	public TranslateKeyManipulator( MovementKey[] directionKeys )
	{
		setKeys(directionKeys);
	}
	
	public void setBounds(AxisAlignedBox bounds)
	{
		this.bounds = bounds;
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
		for (int i=0; i<moveDirs.length; i++ )
		{
			moveDirs[i] = new Point3(0.0d, 0.0d, 0.0d);
		}
		for (int i=0; i<this.directionKeys.length; i++)
		{
			if (input.isKeyDown( this.directionKeys[i].keyValue ))
			{				
				Point3 multipliedPoint = Point3.createMultiplication( this.directionKeys[i].movementDescription.direction.getVector(), this.directionKeys[i].directionMultiplier );
				moveDirs[this.directionKeys[i].movementDescription.type.getIndex()].add( multipliedPoint );
			}	
		}
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
					if (this.bounds != null)
					{
						Point3 currentPos = this.manipulatedTransformable.getTranslation( AsSeenBy.SCENE );
						if (currentPos.x > this.bounds.getXMaximum())
						{
							currentPos.x = this.bounds.getXMaximum();
						}
						if (currentPos.x < this.bounds.getXMinimum())
						{
							currentPos.x = this.bounds.getXMinimum();
						}
						if (currentPos.y > this.bounds.getYMaximum())
						{
							currentPos.y = this.bounds.getYMaximum();
						}
						if (currentPos.y < this.bounds.getYMinimum())
						{
							currentPos.y = this.bounds.getYMinimum();
						}
						if (currentPos.z > this.bounds.getZMaximum())
						{
							currentPos.z = this.bounds.getZMaximum();
						}
						if (currentPos.z < this.bounds.getZMinimum())
						{
							currentPos.z = this.bounds.getZMinimum();
						}
						
						this.manipulatedTransformable.setTranslationOnly( currentPos, AsSeenBy.SCENE );
					}
				}
			}
		}
	}
	
	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
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
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if (this.manipulatedTransformable != null)
		{
			this.startTime = System.currentTimeMillis() * .001d;
			this.initialPoint.set(this.manipulatedTransformable.getAbsoluteTransformation().translation);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput  ) {
	}

	@Override
	public void doTimeUpdateManipulator( double dTime, InputState currentInput ) {
		if (this.manipulatedTransformable != null)
		{
			Point3[] translateDirections = getMoveDirection(currentInput);
			for (Point3 direction : translateDirections) {
				direction.multiply( MOVEMENT_RATE * dTime  );
			}
			moveTransformable( translateDirections );
		}
		
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

}
