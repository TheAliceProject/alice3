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

import edu.cmu.cs.dennisc.color.Color4f;

/**
 * @author David Culyba
 */
public abstract class ColorTargetBasedAnimation extends TargetBasedAnimation< Color4f > {

	public ColorTargetBasedAnimation( Color4f currentValue )
	{
		super(currentValue);
	}
	
	public ColorTargetBasedAnimation( Color4f currentValue, double speed )
	{
		super(currentValue, speed);
	}
	
	public ColorTargetBasedAnimation( Color4f currentValue, Color4f targetValue )
	{
		super(currentValue, targetValue);
	}
	
	public ColorTargetBasedAnimation( Color4f currentValue, Color4f targetValue, double speed )
	{
		super(currentValue, targetValue, speed);
	}

	@Override
	protected double getDistanceToDone() {
		double rDif = Math.abs( this.currentValue.red - this.targetValue.red);
		double gDif = Math.abs( this.currentValue.green - this.targetValue.green);
		double bDif = Math.abs( this.currentValue.blue - this.targetValue.blue);
		double aDif = Math.abs( this.currentValue.alpha - this.targetValue.alpha);
		
		return Math.sqrt( rDif*rDif + gDif*gDif + bDif*bDif + aDif*aDif );
	}

	@Override
	protected Color4f interpolate( Color4f v0, Color4f v1, double deltaSinceLastUpdate ) {
		Color4f toReturn = new Color4f(v0);
		toReturn.interpolate( v1, (float)(deltaSinceLastUpdate*this.speed) );
		return toReturn;
	}

	@Override
	public boolean isDone() {
		return this.currentValue.equals( this.targetValue );
	}

	@Override
	protected Color4f newE( Color4f other ) {
		return new Color4f(other);
	}


}
