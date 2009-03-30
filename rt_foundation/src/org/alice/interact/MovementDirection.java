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

import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author David Culyba
 */
public enum MovementDirection {
	
	FORWARD(0.0d, 0.0d, -1.0d),
	BACKWARD(0.0d, 0.0d, 1.0d),
	LEFT(-1.0d, 0.0d, 0.0d),
	RIGHT(1.0d, 0.0d, 0.0d),
	UP(0.0d, 1.0d, 0.0d),
	DOWN(0.0d, -1.0d, 0.0d),
	RESIZE(-1.0d, 1.0d, 0.0d),
	;
	
	
	private Vector3 directionVector;
	private MovementDirection(double x, double y, double z)
	{
		this.directionVector = new Vector3(x,y,z);
		this.directionVector.normalize();
	}
	
	public Vector3 getVector()
	{
		return this.directionVector;
	}
	
	public MovementDirection getOpposite()
	{
		if (this == FORWARD)
		{
			return BACKWARD;
		}
		else if (this == BACKWARD)
		{
			return FORWARD;
		}
		else if (this == UP)
		{
			return DOWN;
		}
		else if (this == DOWN)
		{
			return UP;
		}
		else if (this == LEFT)
		{
			return RIGHT;
		}
		else if (this == RIGHT)
		{
			return LEFT;
		}
		return this;
	}
	
	public boolean hasDirection( Vector3 vector )
	{
		double dot = Vector3.calculateDotProduct( this.directionVector, vector );
		if (dot > 0.0d)
		{
			return true;
		}
		return false;
	}

}
