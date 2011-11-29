/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.animation;

import edu.cmu.cs.dennisc.scenegraph.Graphic;

/**
 * @author dculyba
 *
 */
public abstract class OpenUpdateCloseOverlayGraphicAnimation extends OverlayGraphicAnimation {

	protected double m_openingDuration;
	protected double m_updatingDuration;
	protected double m_closingDuration;

	protected enum State {
		OPENNING,
		UPDATING,
		CLOSING,
	}
	
	public OpenUpdateCloseOverlayGraphicAnimation( org.lgna.story.implementation.EntityImp entityImp, double openingDuration, double updatingDuration, double closingDuration ) {
		super( entityImp );
		m_openingDuration = openingDuration;
		m_updatingDuration = updatingDuration;
		m_closingDuration = closingDuration;
	}
	protected double getOpeningDuration() {
		return m_openingDuration;
	}
	protected double getUpdatingDuration() {
		return m_updatingDuration;
	}
	protected double getClosingDuration() {
		return m_closingDuration;
	}
	protected abstract void updateStateAndPortion( State state, double portion );
	@Override
	protected void prologue() {
		this.updateStateAndPortion(State.OPENNING, 0.0);
		super.prologue();
	}
	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		State state;
		double portion;
		if( m_openingDuration > 0.0 && deltaSincePrologue <= m_openingDuration ) {
			state = State.OPENNING;
			portion = deltaSincePrologue / m_openingDuration;
		} else if( m_updatingDuration > 0.0 && deltaSincePrologue <= (m_openingDuration+m_updatingDuration) ) {
			state = State.UPDATING;
			portion = ( deltaSincePrologue-m_openingDuration ) / m_updatingDuration;
		} else {
			state = State.CLOSING;
			if( m_closingDuration > 0.0 ) {
				portion = Math.min( ( deltaSincePrologue-m_openingDuration-m_updatingDuration ) / m_closingDuration, 1.0 );
			} else {
				portion = 1.0;
			}
		}
		this.updateStateAndPortion(state, portion);
		double toReturn = (m_openingDuration + m_updatingDuration + m_closingDuration) - deltaSincePrologue;
		return toReturn;
	}
	@Override
	protected void epilogue() {
		this.updateStateAndPortion(State.CLOSING, 1.0);
		super.epilogue();
	}

}
