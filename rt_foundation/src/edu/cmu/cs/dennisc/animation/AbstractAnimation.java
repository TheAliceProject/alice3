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
public abstract class AbstractAnimation implements Animation {
	private enum State {
		PROLOGUE_IS_REQUIRED,
		UPDATE_IS_REQUIRED,
		EPILOGUE_IS_REQUIRED,
		COMPLETED
	}
	private State m_state = State.PROLOGUE_IS_REQUIRED;
	private double m_t0 = Double.NaN;
	private double m_tPrevious = Double.NaN;
	
	public AbstractAnimation() {
		initialize();
	}

	public void initialize() {
		m_state = State.PROLOGUE_IS_REQUIRED;
		m_t0 = Double.NaN;
		m_tPrevious = Double.NaN;
	}
	
	public double update( double tCurrent, AnimationObserver animationObserver ) {
		double tRemaining = Double.NaN;
		if( m_state != State.COMPLETED ) {
			if( m_state == State.PROLOGUE_IS_REQUIRED ) {
				m_t0 = tCurrent;
				prologue();
				if( animationObserver != null ) {
					animationObserver.started( this );
				}
				m_state = State.UPDATE_IS_REQUIRED;
			}
			if( m_state == State.UPDATE_IS_REQUIRED ) {
				tRemaining = update( tCurrent - m_t0, tCurrent - m_tPrevious, animationObserver );
				if( tRemaining <= 0.0 ) {
					m_state = State.EPILOGUE_IS_REQUIRED;
				}
				m_tPrevious = tCurrent;
			}
			if( m_state == State.EPILOGUE_IS_REQUIRED ) {
				epilogue();
				if( animationObserver != null ) {
					animationObserver.finished( this );
				}
				m_state = State.COMPLETED;
			}
		} else {
			//todo
			tRemaining = 0.0;
		}
		assert Double.isNaN( tRemaining ) == false;
		return tRemaining;
	}
	
	public void complete( AnimationObserver animationObserver ) {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "complete: ", m_state );
		if( m_state == State.PROLOGUE_IS_REQUIRED ) {
			prologue();
			if( animationObserver != null ) {
				animationObserver.started( this );
			}
			m_state = State.UPDATE_IS_REQUIRED;
		}
		if( m_state == State.UPDATE_IS_REQUIRED ) {
			update( 0, 0, animationObserver );
			m_state = State.EPILOGUE_IS_REQUIRED;
		}
		if( m_state == State.EPILOGUE_IS_REQUIRED ) {
			epilogue();
			if( animationObserver != null ) {
				animationObserver.finished( this );
			}
			m_state = State.PROLOGUE_IS_REQUIRED;
		}
	}
	
	protected abstract void prologue();
	protected abstract double update( double tDeltaSincePrologue, double tDeltaSinceLastUpdate, AnimationObserver animationObserver );
	protected abstract void epilogue();
}
