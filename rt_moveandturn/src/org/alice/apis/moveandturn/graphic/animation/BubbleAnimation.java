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
public class BubbleAnimation extends OverlayGraphicAnimation {
	private org.alice.apis.moveandturn.graphic.Bubble m_bubble;
	private double m_openingDuration;
	private double m_updatingDuration;
	private double m_closingDuration;
	
	public BubbleAnimation( org.alice.apis.moveandturn.SceneOwner owner, org.alice.apis.moveandturn.graphic.Bubble bubble, double openingDuration, double updatingDuration, double closingDuration ) {
		super( owner );
		m_bubble = bubble;
		m_openingDuration = openingDuration;
		m_updatingDuration = updatingDuration;
		m_closingDuration = closingDuration;
	}
	
	@Override
	protected org.alice.apis.moveandturn.graphic.Graphic getOverlayGraphic() {
		return m_bubble;
	}
	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		org.alice.apis.moveandturn.graphic.Bubble.State state;
		double portion;
		if( m_openingDuration > 0.0 && deltaSincePrologue <= m_openingDuration ) {
			state = org.alice.apis.moveandturn.graphic.Bubble.State.OPENNING;
			portion = deltaSincePrologue / m_openingDuration;
		} else if( m_updatingDuration > 0.0 && deltaSincePrologue <= (m_openingDuration+m_updatingDuration) ) {
			state = org.alice.apis.moveandturn.graphic.Bubble.State.UPDATING;
			portion = ( deltaSincePrologue-m_openingDuration ) / m_updatingDuration;
		} else {
			state = org.alice.apis.moveandturn.graphic.Bubble.State.CLOSING;
			if( m_closingDuration > 0.0 ) {
				portion = Math.min( ( deltaSincePrologue-m_openingDuration-m_updatingDuration ) / m_closingDuration, 1.0 );
			} else {
				portion = 1.0;
			}
		}
		m_bubble.set( state, portion );
		return (m_openingDuration + m_updatingDuration + m_closingDuration) - deltaSincePrologue;
	}
}
