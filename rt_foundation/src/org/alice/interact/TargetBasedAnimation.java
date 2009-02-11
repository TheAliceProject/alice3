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

import edu.cmu.cs.dennisc.animation.AbstractAnimation;
import edu.cmu.cs.dennisc.animation.AnimationObserver;

/**
 * @author David Culyba
 */
public abstract class TargetBasedAnimation< E > extends AbstractAnimation {

	protected static final double DEFAULT_SPEED = 12.0d;
	private static final double DISTANCE_TO_DONE = .001d;
	
	protected E targetValue;
	protected E currentValue;
	protected double speed;
	
	public TargetBasedAnimation()
	{
		speed = DEFAULT_SPEED;
	}
	
	public TargetBasedAnimation( E currentValue )
	{
		this(currentValue, currentValue, DEFAULT_SPEED);
	}
	
	public TargetBasedAnimation( E currentValue, double speed )
	{
		this(currentValue, currentValue, speed);
	}

	public TargetBasedAnimation( E currentValue, E targetValue )
	{
		this(currentValue, targetValue, DEFAULT_SPEED);
	}
	
	public TargetBasedAnimation( E currentValue, E targetValue, double speed )
	{
		this.currentValue = this.newE( currentValue );
		this.targetValue = this.newE( targetValue );
		this.speed = speed;
	}
	
	public void setTarget( E target )
	{
		this.targetValue = this.newE( target );
	}
	
	public void setCurrentValue( E value )
	{
		this.currentValue = this.newE( value );
	}
	
	public void setSpeed( double speed )
	{
		this.speed = speed;
	}
	
	public double getSpeed()
	{
		return this.speed;
	}
	
	@Override
	protected void epilogue() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void prologue() {
		// TODO Auto-generated method stub

	}

	protected abstract void updateValue( E value );
	protected abstract double getDistanceToDone();
	public abstract boolean isDone();
	protected abstract E interpolate( E v0, E v1, double deltaSinceLastUpdate );
	protected abstract E newE( E other );
	
	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, AnimationObserver animationObserver ) {
		
		if (!this.isDone())
		{
			if (!Double.isNaN( deltaSinceLastUpdate ))
			{
				this.currentValue = this.interpolate( this.currentValue, this.targetValue, deltaSinceLastUpdate);
			}
			if ( this.getDistanceToDone() < DISTANCE_TO_DONE)
			{
				this.currentValue = newE(this.targetValue);
			}
			this.updateValue( this.currentValue );
		}
		return 1.0d;
	}
	
	
	
	

}
