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
package edu.cmu.cs.dennisc.animation;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractAnimation implements Animation {
	private enum State {
		INITIALIZE_IS_REQUIRED,
		PROLOGUE_IS_REQUIRED,
		UPDATE_IS_REQUIRED,
		EPILOGUE_IS_REQUIRED,
		COMPLETED
	}

	private State m_state = State.PROLOGUE_IS_REQUIRED;
	private double m_t0 = Double.NaN;
	private double m_tPrevious = Double.NaN;

	//	public AbstractAnimation() {
	//		initialize();
	//	}

	@Override
	public synchronized final void reset() {
		m_state = State.INITIALIZE_IS_REQUIRED;
	}

	@Override
	public synchronized final double update( double tCurrent, AnimationObserver animationObserver ) {
		double tRemaining = Double.NaN;
		if( m_state != State.COMPLETED ) {
			if( m_state == State.INITIALIZE_IS_REQUIRED ) {
				m_t0 = Double.NaN;
				m_tPrevious = Double.NaN;
				m_state = State.PROLOGUE_IS_REQUIRED;
			}
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
				this.preEpilogue();
				epilogue();
				if( animationObserver != null ) {
					animationObserver.finished( this );
				}
				m_state = State.COMPLETED;
				tRemaining = 0.0;
			}
		} else {
			//todo
			tRemaining = 0.0;
		}
		assert Double.isNaN( tRemaining ) == false;
		return tRemaining;
	}

	@Override
	public synchronized final void complete( AnimationObserver animationObserver ) {
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
			this.preEpilogue();
			epilogue();
			if( animationObserver != null ) {
				animationObserver.finished( this );
			}
			m_state = State.PROLOGUE_IS_REQUIRED;
		}
	}

	protected abstract void prologue();

	protected abstract double update( double tDeltaSincePrologue, double tDeltaSinceLastUpdate, AnimationObserver animationObserver );

	//todo: clean
	protected abstract void preEpilogue();

	protected abstract void epilogue();
}
