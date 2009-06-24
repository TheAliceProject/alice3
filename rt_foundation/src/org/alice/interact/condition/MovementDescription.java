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
package org.alice.interact.condition;

import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;

/**
 * @author David Culyba
 */
public class MovementDescription {
	public MovementType type;
	public MovementDirection direction;
	
	public MovementDescription( MovementDirection direction )
	{
		this(direction, MovementType.STOOD_UP);
	}
	
	public MovementDescription( MovementDirection direction, MovementType type )
	{
		this.type = type;
		this.direction = direction;
	}
	
	@Override
	public boolean equals( Object o )
	{
		if (o instanceof MovementDescription)
		{
			return (((MovementDescription)o).direction == this.direction) && (((MovementDescription)o).type == this.type);
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "Movement Type: "+this.type+", Direction: "+this.direction;
	}
	
}
