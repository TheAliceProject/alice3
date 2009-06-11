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

/**
 * @author David Culyba
 */
public abstract class DoubleTargetBasedAnimation extends TargetBasedAnimation<Double> 
{
	public DoubleTargetBasedAnimation( Double currentValue )
	{
		super(currentValue);
	}
	
	public DoubleTargetBasedAnimation( Double currentValue, double speed )
	{
		super(currentValue, speed);
	}
	
	public DoubleTargetBasedAnimation( Double currentValue, Double targetValue )
	{
		super(currentValue, targetValue);
	}
	
	public DoubleTargetBasedAnimation( Double currentValue, Double targetValue, double speed )
	{
		super(currentValue, targetValue, speed);
		if (this.currentValue.isNaN())
		{
			this.currentValue = new Double(0.0d);
		}
		if (this.targetValue.isNaN())
		{
			this.targetValue = new Double(0.0d);
		}
	}
	
	@Override
	protected double getDistanceToDone()
	{
		return Math.abs(this.currentValue - this.targetValue);
	}
	
	@Override
	public boolean isDone()
	{
		return this.currentValue.equals( this.targetValue );
	}
	
	@Override
	protected Double interpolate( Double v0, Double v1, double deltaSinceLastUpdate )
	{
		double newValue = v0 + (v1 - v0)*this.speed*deltaSinceLastUpdate;
		return new Double(newValue);
	}
	
	@Override
	protected Double newE( Double other )
	{
		return new Double(other);
	}
	
}
