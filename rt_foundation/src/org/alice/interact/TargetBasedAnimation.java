/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
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
	private static final double MAX_FRAME_LENGTH = .1d;
	
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
	
	public void forceValueUpdate()
	{
		this.updateValue( this.currentValue );
	}
	
	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, AnimationObserver animationObserver ) {
		
		if (!this.isDone())
		{
			if (!Double.isNaN( deltaSinceLastUpdate ))
			{
				if (deltaSinceLastUpdate > MAX_FRAME_LENGTH)
					deltaSinceLastUpdate = MAX_FRAME_LENGTH;
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
