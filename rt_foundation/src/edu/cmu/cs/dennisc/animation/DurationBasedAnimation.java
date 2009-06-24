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
package edu.cmu.cs.dennisc.animation;

/**
 * @author Dennis Cosgrove
 */
public abstract class DurationBasedAnimation extends AbstractAnimation {
	public static final double DEFAULT_DURATION = 1.0;
	public static final Style DEFAULT_STYLE = TraditionalStyle.BEGIN_AND_END_GENTLY;

	private double m_duration;
	private Style m_style;

	public DurationBasedAnimation() {
		this( DEFAULT_DURATION );
	}

	public DurationBasedAnimation( Number duration ) {
		this( duration, DEFAULT_STYLE );
	}

	public DurationBasedAnimation( Number duration, Style style ) {
		setDuration( duration );
		setStyle( style );
	}

	public double getDuration() {
		return m_duration;
	}

	public void setDuration( Number duration ) {
		m_duration = duration.doubleValue();
	}

	public Style getStyle() {
		return m_style;
	}

	public void setStyle( Style style ) {
		assert style != null;
		m_style = style;
	}

	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, AnimationObserver animationObserver ) {
		double portion;
		if( m_duration > 0.0 ) {
			//portion = Math.min( deltaSincePrologue / m_duration, 1.0 );
			portion = m_style.calculatePortion( deltaSincePrologue, m_duration );
		} else {
			portion = 1.0;
		}
		double tRemaining = m_duration - deltaSincePrologue;
		setPortion( portion );
		if( animationObserver instanceof DurationBasedAnimationObserver ) {
			try {
				((DurationBasedAnimationObserver)animationObserver).updated( this, portion );
			} catch( BreakException be ) {
				tRemaining = 0.0;
			}
		}
		return tRemaining;
	}

	protected abstract void setPortion( double portion );
}
