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
public abstract class FrameBasedAnimation extends AbstractAnimation {
	private int m_iPrevious;
	private int m_iCurrent;

	public abstract int getFrameCount();

	public abstract double getFramesPerSecond();

	protected double getDurationPerFrame() {
		return 1.0 / getFramesPerSecond();
	}

	protected double getDuration() {
		return getFrameCount() * getDurationPerFrame();
	}

	protected abstract boolean isSkippingFramesAcceptable();

	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, AnimationObserver animationObserver ) {
		double tDuration = getDuration();
		double tRemaining = tDuration - deltaSincePrologue;
		double tDurationPerFrame = getDurationPerFrame();
		m_iCurrent = (int)( ( deltaSincePrologue + ( 0.5 * tDurationPerFrame ) ) / tDurationPerFrame );
		m_iCurrent = Math.min( m_iCurrent, getFrameCount() - 1 );
		if( m_iPrevious == m_iCurrent ) {
			//pass
		} else {
			int init;
			if( m_iCurrent < m_iPrevious ) {
				init = 0;
			} else {
				if( isSkippingFramesAcceptable() ) {
					init = m_iCurrent;
				} else {
					init = m_iPrevious + 1;
				}
			}
			for( int i = init; i <= m_iCurrent; i++ ) {
				setFrameIndex( i );
			}
			if( animationObserver instanceof FrameBasedAnimationObserver ) {
				try {
					( (FrameBasedAnimationObserver)animationObserver ).updated( this, m_iCurrent );
				} catch( BreakException be ) {
					tRemaining = 0.0;
				}
			}
			m_iPrevious = m_iCurrent;
		}
		return tRemaining;
	}

	@Override
	protected final void preEpilogue() {
		this.setFrameIndex( getFrameCount() - 1 );
	}

	protected abstract void setFrameIndex( int frameIndex );
}
//public abstract class FrameBasedAnimation implements Animation {
//	private enum State {
//		PROLOGUE_IS_REQUIRED, 
//		SET_FRAME_INDEX_IS_REQUIRED, 
//		EPILOGUE_IS_REQUIRED, 
//		COMPLETED
//	}
//
//	private State m_state;
//	private double m_t0;
//
//	private int m_iPrevious;
//	private int m_iCurrent;
//
//	public FrameBasedAnimation() {
//		initialize();
//	}
//
//	public abstract int getFrameCount();
//	public abstract double getFramesPerSecond();
//	protected double getDurationPerFrame() {
//		return 1.0 / getFramesPerSecond();
//	}
//	protected double getDuration() {
//		return getFrameCount() * getDurationPerFrame();
//	}
//
//	protected abstract boolean isSkippingFramesAcceptable();
//
//	public void initialize() {
//		m_state = State.PROLOGUE_IS_REQUIRED;
//		m_t0 = Double.NaN;
//	}
//
//	public double update( double tCurrent, AnimationObserver animationObserver ) {
//		double tRemaining = Double.NaN;
//		if( m_state != State.COMPLETED ) {
//			if( m_state == State.PROLOGUE_IS_REQUIRED ) {
//				prologue();
//				if( animationObserver != null ) {
//					animationObserver.started( this );
//				}
//				m_iPrevious = -1;
//				m_t0 = tCurrent;
//				m_state = State.SET_FRAME_INDEX_IS_REQUIRED;
//			}
//			if( m_state == State.SET_FRAME_INDEX_IS_REQUIRED ) {
//				double tDuration = getDuration();
//				double tDelta = tCurrent - m_t0;
//				tRemaining = tDuration - tDelta;
//				double tDurationPerFrame = getDurationPerFrame();
//				m_iCurrent = (int)((tDelta + 0.5 * tDurationPerFrame) / tDurationPerFrame);
//				if( tRemaining > 0.0 ) {
//					//pass
//				} else {
//					m_state = State.EPILOGUE_IS_REQUIRED;
//				}
//				m_iCurrent = Math.min( m_iCurrent, getFrameCount() - 1 );
//				if( m_iPrevious == m_iCurrent ) {
//					//pass
//				} else {
//					int init;
//					if( m_iCurrent < m_iPrevious ) {
//						init = 0;
//					} else {
//						if( isSkippingFramesAcceptable() ) {
//							init = m_iCurrent;
//						} else {
//							init = m_iPrevious + 1;
//						}
//					}
//					for( int i = init; i <= m_iCurrent; i++ ) {
//						setFrameIndex( i );
//					}
//					if( animationObserver instanceof FrameBasedAnimationObserver ) {
//						boolean isContinueDesired = ((FrameBasedAnimationObserver)animationObserver).updated( this, m_iCurrent );
//						if( isContinueDesired ) {
//							//pass
//						} else {
//							tRemaining = 0.0;
//							m_state = State.EPILOGUE_IS_REQUIRED;
//						}
//					}
//					m_iPrevious = m_iCurrent;
//				}
//			}
//			if( m_state == State.EPILOGUE_IS_REQUIRED ) {
//				epilogue();
//				if( animationObserver != null ) {
//					animationObserver.finished( this );
//				}
//				m_state = State.COMPLETED;
//			}
//		} else {
//			//todo
//			tRemaining = 0.0;
//		}
//		assert Double.isNaN( tRemaining ) == false;
//		return tRemaining;
//	}
//	public void updateFrame( int index, AnimationObserver animationObserver ) {
//		if( m_state == State.PROLOGUE_IS_REQUIRED ) {
//			update( 0, null );
//		}
//		update( m_t0 + getDurationPerFrame() * index, animationObserver );
//	}
//	public void complete( AnimationObserver animationObserver ) {
//		if( m_state == State.PROLOGUE_IS_REQUIRED ) {
//			prologue();
//			if( animationObserver != null ) {
//				animationObserver.started( this );
//			}
//			setFrameIndex( 0 );
//			m_state = State.SET_FRAME_INDEX_IS_REQUIRED;
//		}
//		if( m_state == State.SET_FRAME_INDEX_IS_REQUIRED ) {
//			int finalFrameIndex = getFrameCount() - 1;
//			int init;
//			if( isSkippingFramesAcceptable() ) {
//				init = finalFrameIndex;
//			} else {
//				init = 0;
//			}
//			for( int i = init; i <= finalFrameIndex; i++ ) {
//				setFrameIndex( i );
//			}
//			if( animationObserver instanceof FrameBasedAnimationObserver ) {
//				((FrameBasedAnimationObserver)animationObserver).updated( this, finalFrameIndex );
//			}
//			m_state = State.EPILOGUE_IS_REQUIRED;
//		}
//		if( m_state == State.EPILOGUE_IS_REQUIRED ) {
//			epilogue();
//			if( animationObserver != null ) {
//				animationObserver.finished( this );
//			}
//			m_state = State.PROLOGUE_IS_REQUIRED;
//		}
//	}
//
//	protected abstract void prologue();
//	protected abstract void setFrameIndex( int frameIndex );
//	protected abstract void epilogue();
//}
