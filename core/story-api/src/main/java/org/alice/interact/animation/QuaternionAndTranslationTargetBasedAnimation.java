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

import org.alice.interact.QuaternionAndTranslation;

import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author David Culyba
 */
public abstract class QuaternionAndTranslationTargetBasedAnimation extends TargetBasedFrameObserver<QuaternionAndTranslation> {

	private static final double CUSTOM_SPEED = 5.0d;

	public QuaternionAndTranslationTargetBasedAnimation( QuaternionAndTranslation currentValue ) {
		this( currentValue, currentValue, CUSTOM_SPEED );
	}

	public QuaternionAndTranslationTargetBasedAnimation( QuaternionAndTranslation currentValue, double speed ) {
		this( currentValue, currentValue, speed );
	}

	public QuaternionAndTranslationTargetBasedAnimation( QuaternionAndTranslation currentValue, QuaternionAndTranslation targetValue ) {
		this( currentValue, targetValue, CUSTOM_SPEED );
	}

	public QuaternionAndTranslationTargetBasedAnimation( QuaternionAndTranslation currentValue, QuaternionAndTranslation targetValue, double speed ) {
		super( currentValue, targetValue, speed );
		if( isCloseEnoughToBeDone() ) {
			this.shouldAnimate = true;
		} else {
			this.shouldAnimate = false;
		}
	}

	@Override
	protected boolean isCloseEnoughToBeDone() {
		edu.cmu.cs.dennisc.math.UnitQuaternion currentQ = this.currentValue.getQuaternion();
		edu.cmu.cs.dennisc.math.UnitQuaternion targetQ = this.targetValue.getQuaternion();

		edu.cmu.cs.dennisc.math.UnitQuaternion targetQNegative = new edu.cmu.cs.dennisc.math.UnitQuaternion( targetQ );
		targetQNegative.multiply( -1.0 );
		boolean quaternionDone = currentQ.isWithinEpsilonOrIsNegativeWithinEpsilon( targetQ, MIN_DISTANCE_TO_DONE ) || currentQ.isWithinEpsilonOrIsNegativeWithinEpsilon( targetQNegative, MIN_DISTANCE_TO_DONE );
		double translationDist = Point3.calculateDistanceBetween( this.currentValue.getTranslation(), this.targetValue.getTranslation() );

		boolean translationDone = translationDist < MIN_DISTANCE_TO_DONE;

		return quaternionDone && translationDone;
	}

	//	@Override
	//	public void update( double current ) {
	//		if (this.shouldAnimate)
	//		{
	//			super.update( current );
	//		}
	//		if ()
	//	}

	@Override
	protected QuaternionAndTranslation interpolate( QuaternionAndTranslation v0, QuaternionAndTranslation v1, double deltaSinceLastUpdate ) {
		float portion = (float)( deltaSinceLastUpdate * this.speed );
		portion = Math.min( portion, 1.0f );
		portion = Math.max( -1.0f, portion );

		QuaternionAndTranslation rv = new QuaternionAndTranslation();
		rv.setToInterpolation( v0, v1, portion );
		return rv;
	}

	@Override
	public boolean isDone() {
		return this.isCloseEnoughToBeDone();
	}

	@Override
	protected QuaternionAndTranslation newE( QuaternionAndTranslation other ) {
		return new QuaternionAndTranslation( other );
	}

	private boolean shouldAnimate = false;
}
