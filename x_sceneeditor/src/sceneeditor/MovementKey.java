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
package sceneeditor;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Tuple3;

/**
 * @author David Culyba
 */
public class MovementKey {

	public Tuple3 direction;
	public double directionMultiplier;
	public int keyValue;
	public MovementType movementType;
	
	public MovementKey( int keyValue, Tuple3 direction )
	{
		this(keyValue, direction, MovementType.STOOD_UP);
	}
	
	public MovementKey( int keyValue, Tuple3 direction, MovementType movementType )
	{
		this(keyValue, direction, movementType, 1.0d);
	}
	
	public MovementKey( int keyValue, Tuple3 direction, double directionMultiplier )
	{
		this(keyValue, direction, MovementType.STOOD_UP, directionMultiplier);
	}
	
	public MovementKey( int keyValue, Tuple3 direction, MovementType movementType, double directionMultiplier )
	{
		this.keyValue = keyValue;
		this.direction = direction;
		this.movementType = movementType;
		this.directionMultiplier = directionMultiplier;
	}
}
