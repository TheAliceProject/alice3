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
public class ClockBasedAnimator extends AbstractAnimator {
	private double m_tPreviousClock = Double.NaN;
	private double m_tPreviousSimulation = Double.NaN;

	@Override
	protected void updateCurrentTime( boolean isPaused ) {
		//todo: optimize for speed factor == 0
		double tCurrentClock = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
		double tCurrentSimulation;
		if( Double.isNaN( m_tPreviousClock ) ) {
			tCurrentSimulation = 0.0;
		} else {
			//todo: optimize for speed factor == 0
			tCurrentSimulation = m_tPreviousSimulation + (tCurrentClock - m_tPreviousClock) * getSpeedFactor();
		}
		setCurrentTime( tCurrentSimulation );
		m_tPreviousClock = tCurrentClock;
		m_tPreviousSimulation = tCurrentSimulation;
	}
}
