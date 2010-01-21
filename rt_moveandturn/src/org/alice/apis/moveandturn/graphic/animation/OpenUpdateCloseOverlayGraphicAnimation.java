/*
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
package org.alice.apis.moveandturn.graphic.animation;

/**
 * @author Dennis Cosgrove
 */
public abstract class OpenUpdateCloseOverlayGraphicAnimation extends OverlayGraphicAnimation {
	private double m_openingDuration;
	private double m_updatingDuration;
	private double m_closingDuration;

	protected enum State {
		OPENNING,
		UPDATING,
		CLOSING,
	}
	
	public OpenUpdateCloseOverlayGraphicAnimation( org.alice.apis.moveandturn.Composite composite, double openingDuration, double updatingDuration, double closingDuration ) {
		super( composite );
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
		return (m_openingDuration + m_updatingDuration + m_closingDuration) - deltaSincePrologue;
	}
	@Override
	protected void epilogue() {
		this.updateStateAndPortion(State.CLOSING, 1.0);
		super.epilogue();
	}
}
