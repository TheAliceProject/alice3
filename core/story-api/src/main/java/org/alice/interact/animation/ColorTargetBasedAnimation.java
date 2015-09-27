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

import edu.cmu.cs.dennisc.color.Color4f;

/**
 * @author David Culyba
 */
public abstract class ColorTargetBasedAnimation extends TargetBasedFrameObserver<Color4f> {

	public ColorTargetBasedAnimation( Color4f currentValue ) {
		super( currentValue );
	}

	public ColorTargetBasedAnimation( Color4f currentValue, double speed ) {
		super( currentValue, speed );
	}

	public ColorTargetBasedAnimation( Color4f currentValue, Color4f targetValue ) {
		super( currentValue, targetValue );
	}

	public ColorTargetBasedAnimation( Color4f currentValue, Color4f targetValue, double speed ) {
		super( currentValue, targetValue, speed );
	}

	@Override
	protected boolean isCloseEnoughToBeDone() {
		double rDif = Math.abs( this.currentValue.red - this.targetValue.red );
		double gDif = Math.abs( this.currentValue.green - this.targetValue.green );
		double bDif = Math.abs( this.currentValue.blue - this.targetValue.blue );
		double aDif = Math.abs( this.currentValue.alpha - this.targetValue.alpha );

		return ( Math.sqrt( ( rDif * rDif ) + ( gDif * gDif ) + ( bDif * bDif ) + ( aDif * aDif ) ) < MIN_DISTANCE_TO_DONE );
	}

	@Override
	protected Color4f interpolate( Color4f v0, Color4f v1, double deltaSinceLastUpdate ) {
		return Color4f.createInterpolation( v0, v1, (float)( deltaSinceLastUpdate * this.speed ) );
	}

	@Override
	public boolean isDone() {
		return this.currentValue.equals( this.targetValue );
	}

	@Override
	protected Color4f newE( Color4f other ) {
		return new Color4f( other );
	}

}
