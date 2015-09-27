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
package org.lgna.story.implementation.overlay;

/**
 * @author dculyba
 * 
 */
public abstract class OpenUpdateCloseOverlayGraphicAnimation extends edu.cmu.cs.dennisc.animation.AbstractAnimation {
	protected static enum State {
		OPENNING,
		UPDATING,
		CLOSING,
	}

	public OpenUpdateCloseOverlayGraphicAnimation( double openingDuration, double updatingDuration, double closingDuration ) {
		this.openingDuration = openingDuration;
		this.updatingDuration = updatingDuration;
		this.closingDuration = closingDuration;
	}

	protected abstract void updateStateAndPortion( State state, double portion );

	@Override
	protected final void prologue() {
		this.updateStateAndPortion( State.OPENNING, 0.0 );
	}

	@Override
	protected final double update( double deltaSincePrologue, double deltaSinceLastUpdate, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		State state;
		double portion;
		if( ( this.openingDuration > 0.0 ) && ( deltaSincePrologue <= this.openingDuration ) ) {
			state = State.OPENNING;
			portion = deltaSincePrologue / this.openingDuration;
		} else if( ( this.updatingDuration > 0.0 ) && ( deltaSincePrologue <= ( this.openingDuration + this.updatingDuration ) ) ) {
			state = State.UPDATING;
			portion = ( deltaSincePrologue - this.openingDuration ) / this.updatingDuration;
		} else {
			state = State.CLOSING;
			if( this.closingDuration > 0.0 ) {
				portion = Math.min( ( deltaSincePrologue - this.openingDuration - this.updatingDuration ) / this.closingDuration, 1.0 );
			} else {
				portion = 1.0;
			}
		}
		this.updateStateAndPortion( state, portion );
		double toReturn = ( this.openingDuration + this.updatingDuration + this.closingDuration ) - deltaSincePrologue;
		return toReturn;
	}

	@Override
	protected void preEpilogue() {
	}

	@Override
	protected final void epilogue() {
		this.updateStateAndPortion( State.CLOSING, 1.0 );
	}

	private final double openingDuration;
	private final double updatingDuration;
	private final double closingDuration;
}
