/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.interact.animation;

import edu.cmu.cs.dennisc.animation.FrameObserver;

/**
 * @author David Culyba
 */
public abstract class TargetBasedFrameObserver<E> implements FrameObserver {

	private static final double DEFAULT_SPEED = 12.0d;
	protected static final double MIN_DISTANCE_TO_DONE = .001d;
	private static final double MAX_FRAME_LENGTH = .1d;

	public TargetBasedFrameObserver() {
		speed = DEFAULT_SPEED;
	}

	public TargetBasedFrameObserver( E currentValue ) {
		this( currentValue, currentValue, DEFAULT_SPEED );
	}

	public TargetBasedFrameObserver( E currentValue, double speed ) {
		this( currentValue, currentValue, speed );
	}

	public TargetBasedFrameObserver( E currentValue, E targetValue ) {
		this( currentValue, targetValue, DEFAULT_SPEED );
	}

	public TargetBasedFrameObserver( E currentValue, E targetValue, double speed ) {
		synchronized( syncLock ) {
			this.currentValue = this.newE( currentValue );
			this.targetValue = this.newE( targetValue );
			this.speed = speed;
		}
	}

	public void setTarget( E target ) {
		synchronized( syncLock ) {
			this.targetValue = this.newE( target );
			this.isDone = this.isDone();
		}
	}

	public E getCurrentValue() {
		return this.currentValue;
	}

	public void setCurrentValue( E value ) {
		synchronized( syncLock ) {
			this.currentValue = this.newE( value );
			this.isDone = this.isDone();
		}
	}

	public void setSpeed( double speed ) {
		synchronized( syncLock ) {
			this.speed = speed;
		}
	}

	public double getSpeed() {
		return this.speed;
	}

	protected abstract void updateValue( E value );

	protected abstract boolean isCloseEnoughToBeDone();

	public abstract boolean isDone();

	protected abstract E interpolate( E v0, E v1, double deltaSinceLastUpdate );

	protected abstract E newE( E other );

	public void forceValueUpdate() {
		synchronized( syncLock ) {
			this.updateValue( this.currentValue );
		}
	}

	@Override
	public void complete() {
		synchronized( syncLock ) {
			this.currentValue = newE( targetValue );
			this.updateValue( this.currentValue );
		}
	}

	@Override
	public void update( double tCurrent ) {
		if( Double.isNaN( timeOfLastFrame ) ) {
			deltaSinceLastFrame = 0.0d;
		} else {
			deltaSinceLastFrame = tCurrent - timeOfLastFrame;
		}
		timeOfLastFrame = tCurrent;
		if( !this.isDone ) {
			synchronized( syncLock ) {
				if( !Double.isNaN( deltaSinceLastFrame ) ) {
					if( deltaSinceLastFrame > MAX_FRAME_LENGTH ) {
						deltaSinceLastFrame = MAX_FRAME_LENGTH;
					}
					this.currentValue = this.interpolate( this.currentValue, this.targetValue, deltaSinceLastFrame );
				}
				if( this.isCloseEnoughToBeDone() ) {
					this.currentValue = newE( this.targetValue );
				}
				this.updateValue( this.currentValue );
				this.isDone = this.isDone();
			}
		}
	}

	protected E targetValue;
	protected E currentValue;
	protected double speed;
	private double timeOfLastFrame = Double.NaN;
	private double deltaSinceLastFrame = Double.NaN;
	private boolean isDone = true;

	private final Object syncLock = new Object();
}
