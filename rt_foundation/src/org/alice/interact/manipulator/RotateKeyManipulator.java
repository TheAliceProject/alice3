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
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementKey;
import org.alice.interact.MovementType;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author David Culyba
 */
public class RotateKeyManipulator extends AbstractManipulator {
	
	protected static double TURN_RATE = 5.0d;
	protected static double CLICK_TIME = .1d;
	protected static double CLICK_MOVE_AMOUNT = .2d;
	
	protected Point3 initialPoint = new Point3();
	protected double startTime = 0.0d;
	MovementKey[] rotationKeys;
	java.util.Vector< MovementDirection > rotateAxes = new java.util.Vector< MovementDirection >();
	
	public RotateKeyManipulator()
	{
		setKeys(new MovementKey[0]);
	}
	
	public RotateKeyManipulator( MovementKey[] directionKeys )
	{
		setKeys(directionKeys);
	}
	
	@Override
	public String getUndoRedoDescription() {
		return "Object Rotate";
	}
	
	public void setKeys(MovementKey[] directionKeys)
	{
		this.rotationKeys = directionKeys;
		for (MovementKey direction : directionKeys)
		{
			if ( !this.rotateAxes.contains( direction.movementDescription.direction ) )
			{
				this.rotateAxes.add( direction.movementDescription.direction );
			}
		}
	}
	
	protected double[][] getRotateAmount( InputState input )
	{
		MovementType[] movementTypes = MovementType.values();
		double[][] rotateDirs = new double[this.rotateAxes.size()][movementTypes.length];
		for (int i=0; i<rotateDirs.length; i++ )
		{
			for (int j=0; j<rotateDirs[i].length; j++)
			{
				rotateDirs[i][j] = 0.0d;
			}
		}
		for (int i=0; i<this.rotationKeys.length; i++)
		{
			if (input.isKeyDown( this.rotationKeys[i].keyValue ))
			{
				int typeIndex = this.rotationKeys[i].movementDescription.type.getIndex();
				int axisIndex = this.rotateAxes.indexOf( this.rotationKeys[i].movementDescription.direction );
				
				rotateDirs[axisIndex][typeIndex] += this.rotationKeys[i].directionMultiplier;
			}	
		}
		return rotateDirs;
	}
	
	
	private void rotateTransformable( double[][] rotateAmounts )
	{
		for (int axisIndex=0; axisIndex<rotateAmounts.length; axisIndex++)
		{
			for (int typeIndex=0; typeIndex<rotateAmounts[axisIndex].length; typeIndex++)
			{
				if (rotateAmounts[axisIndex][typeIndex] != 0.0d)
				{
					Vector3 rotateAxis = this.rotateAxes.get( axisIndex ).getVector();
					Angle rotateAngle = new AngleInDegrees( rotateAmounts[axisIndex][typeIndex] );
					MovementType movementType = MovementType.getMovementTypeForIndex( typeIndex );
					if (movementType != null)
					{
						movementType.applyRotation( this.manipulatedTransformable, rotateAxis, rotateAngle );
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
			double amountToRotate = CLICK_TIME * TURN_RATE;
			double[][] rotateAmounts = getRotateAmount(previousInput);
			for (int i=0; i<rotateAmounts.length; i++ )
			{
				for (int j=0; j<rotateAmounts[i].length; j++)
				{
					rotateAmounts[i][j] *= amountToRotate;
				}
			}
			this.manipulatedTransformable.setTranslationOnly( initialPoint, this.manipulatedTransformable.getRoot() );
			rotateTransformable( rotateAmounts );
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
	public void doTimeUpdateManipulator( double dTime, InputState currentInput ) {
		if (this.manipulatedTransformable != null)
		{
			double[][] rotateAmounts = getRotateAmount(currentInput);
			for (int i=0; i<rotateAmounts.length; i++ )
			{
				for (int j=0; j<rotateAmounts[i].length; j++)
				{
					rotateAmounts[i][j] *= TURN_RATE * dTime ;
				}
			}
			
			rotateTransformable( rotateAmounts );
		}
		
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

}
