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


/**
 * @author David Culyba
 */
public abstract class DoubleTargetBasedAnimation extends TargetBasedFrameObserver<Double> {
	public DoubleTargetBasedAnimation( Double currentValue ) {
		super( currentValue );
	}

	public DoubleTargetBasedAnimation( Double currentValue, double speed ) {
		super( currentValue, speed );
	}

	public DoubleTargetBasedAnimation( Double currentValue, Double targetValue ) {
		super( currentValue, targetValue );
	}

	public DoubleTargetBasedAnimation( Double currentValue, Double targetValue, double speed ) {
		super( currentValue, targetValue, speed );
		if( this.currentValue.isNaN() ) {
			this.currentValue = new Double( 0.0d );
		}
		if( this.targetValue.isNaN() ) {
			this.targetValue = new Double( 0.0d );
		}
	}

	@Override
	protected boolean isCloseEnoughToBeDone() {
		return ( Math.abs( this.currentValue - this.targetValue ) < MIN_DISTANCE_TO_DONE );
	}

	@Override
	public boolean isDone() {
		return this.currentValue.equals( this.targetValue );
	}

	@Override
	protected Double interpolate( Double v0, Double v1, double deltaSinceLastUpdate ) {
		double newValue = v0 + ( ( v1 - v0 ) * this.speed * deltaSinceLastUpdate );
		return new Double( newValue );
	}

	@Override
	protected Double newE( Double other ) {
		return new Double( other );
	}

}
