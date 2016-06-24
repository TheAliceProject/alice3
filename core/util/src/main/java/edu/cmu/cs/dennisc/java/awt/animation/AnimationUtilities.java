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
package edu.cmu.cs.dennisc.java.awt.animation;

/**
 * @author Dennis Cosgrove
 */
public class AnimationUtilities {
	private static final int DEFAULT_FRAME_DELAY = 5;

	private AnimationUtilities() {
		throw new AssertionError();
	}

	private static abstract class AnimationActionListener implements java.awt.event.ActionListener {
		private javax.swing.Timer timer;
		private final double duration;
		private final java.awt.Component component;

		private Long t0 = null;

		public AnimationActionListener( double duration, java.awt.Component component ) {
			this.duration = duration;
			this.component = component;
		}

		public double getDuration() {
			return this.duration;
		}

		public java.awt.Component getComponent() {
			return this.component;
		}

		protected abstract void update( double portion );

		@Override
		public final void actionPerformed( java.awt.event.ActionEvent e ) {
			if( t0 != null ) {
				//pass
			} else {
				t0 = e.getWhen();
			}
			long tDelta = e.getWhen() - t0;
			double portion = ( tDelta * 0.001 ) / this.duration;
			portion = Math.min( portion, 1.0 );
			this.update( portion );
			if( portion >= 1.0 ) {
				timer.stop();
			}
		}

		public void startTimer( double initialDelay ) {
			this.timer = new javax.swing.Timer( DEFAULT_FRAME_DELAY, this );
			timer.setInitialDelay( (int)( initialDelay * 1000 ) );
			timer.start();
		}
	}

	public static void animatePosition( java.awt.Component component, int x, int y, double duration, double initialDelay ) {

		class PositionAnimationActionListener extends AnimationActionListener {
			private final int x0;
			private final int y0;
			private final int x1;
			private final int y1;

			public PositionAnimationActionListener( double duration, java.awt.Component component, int x, int y ) {
				super( duration, component );
				this.x0 = component.getX();
				this.y0 = component.getY();
				this.x1 = x;
				this.y1 = y;
			}

			@Override
			protected void update( double portion ) {
				int x;
				int y;
				if( portion == 0.0 ) {
					x = this.x0;
					y = this.y0;
				} else if( portion == 1.0 ) {
					x = this.x1;
					y = this.y1;
				} else {
					x = (int)Math.round( ( x0 * ( 1 - portion ) ) + ( x1 * portion ) );
					y = (int)Math.round( ( y0 * ( 1 - portion ) ) + ( y1 * portion ) );
				}
				this.getComponent().setLocation( x, y );
			}
		}
		PositionAnimationActionListener animationActionListener = new PositionAnimationActionListener( duration, component, x, y );
		animationActionListener.startTimer( initialDelay );
	}

	public static void animateScale( java.awt.Component component, double scale0, double scale1, double duration, double initialDelay ) {
		class ScaleAnimationActionListener extends AnimationActionListener {
			private final double scale0;
			private final double scale1;

			public ScaleAnimationActionListener( double duration, java.awt.Component component, double scale0, double scale1 ) {
				super( duration, component );
				this.scale0 = scale0;
				this.scale1 = scale1;
			}

			@Override
			protected void update( double portion ) {
				double scale;
				if( portion == 0.0 ) {
					scale = this.scale0;
				} else if( portion == 1.0 ) {
					scale = this.scale1;
				} else {
					scale = ( scale0 * ( 1 - portion ) ) + ( scale1 * portion );
				}
				java.awt.Component awtComponent = this.getComponent();
				java.awt.Dimension preferredSize = awtComponent.getPreferredSize();
				int width = (int)Math.round( preferredSize.width * scale );
				int height = (int)Math.round( preferredSize.height * scale );
				awtComponent.setSize( width, height );
			}
		}
		ScaleAnimationActionListener animationActionListener = new ScaleAnimationActionListener( duration, component, scale0, scale1 );
		animationActionListener.startTimer( initialDelay );
	}

	public static void main( String[] args ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				javax.swing.JFrame frame = new javax.swing.JFrame();
				frame.setLocation( 0, 0 );
				frame.setPreferredSize( new java.awt.Dimension( 400, 200 ) );
				frame.pack();
				frame.setVisible( true );
				animatePosition( frame, 1000, 500, 1.0, 2.0 );
				animateScale( frame, 1.0, 4.0, 1.0, 3.0 );
			}
		} );
	}
}
