/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public class ProgramImp {
	private final org.lgna.story.Program abstraction;
	private edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;

	public static final Double CLOCK_BASED_FRAME_RATE = null;
	private Double frameRate = CLOCK_BASED_FRAME_RATE;
	private edu.cmu.cs.dennisc.animation.ClockBasedAnimator clockBasedAnimator;
	private edu.cmu.cs.dennisc.animation.FrameBasedAnimator frameBasedAnimator;
	
	private double simulationSpeedFactor = 1.0; 
	public ProgramImp( org.lgna.story.Program abstraction ) {
		this.abstraction = abstraction;
	}
	
	private final class ControlPanel extends javax.swing.JPanel {
		private final javax.swing.JLabel label = new javax.swing.JLabel();
		private final javax.swing.BoundedRangeModel boundedRangeModel = new javax.swing.DefaultBoundedRangeModel();
		public ControlPanel() {
			final javax.swing.ButtonModel buttonModel = new javax.swing.JToggleButton.ToggleButtonModel();
			buttonModel.setSelected( true );
			buttonModel.addChangeListener( new javax.swing.event.ChangeListener() {
				public void stateChanged( javax.swing.event.ChangeEvent e ) {
					ProgramImp.this.handlePlayOrPause( buttonModel.isSelected() );
				}
			} );

			javax.swing.JButton playPauseButton = new javax.swing.JButton();
			playPauseButton.setModel( buttonModel );
			playPauseButton.setIcon( new javax.swing.Icon() {
				public int getIconWidth() {
					return 12;
				}
				public int getIconHeight() {
					return 12;
				}
				public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
					javax.swing.AbstractButton toggleButton = (javax.swing.AbstractButton)c;
					javax.swing.ButtonModel buttonModel = toggleButton.getModel();
					if( buttonModel.isSelected() ) {
						g.fillRect( x+1, y+1, 3, 10 );
						g.fillRect( x+7, y+1, 3, 10 );
					} else {
						edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.EAST, x, y, 12, 12 );
					}
				}
			} );
			
			final int PAD_X = 12;
			final int PAD_Y = 8;
			playPauseButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( PAD_Y, PAD_X, PAD_Y, PAD_X ) );
			
			edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( label, edu.cmu.cs.dennisc.java.awt.font.TextFamily.MONOSPACED );
			
			boundedRangeModel.setMinimum( 1 );
			boundedRangeModel.setMaximum( 8 );
			boundedRangeModel.addChangeListener( new javax.swing.event.ChangeListener() {
				public void stateChanged( javax.swing.event.ChangeEvent e ) {
					ControlPanel.this.updateLabel();
					ProgramImp.this.handleSpeedChange( boundedRangeModel.getValue() );
				}
			} );
			ControlPanel.this.updateLabel();
			
			javax.swing.JSlider slider = new javax.swing.JSlider( boundedRangeModel );
			slider.addMouseListener( new java.awt.event.MouseListener() {
				public void mousePressed( java.awt.event.MouseEvent e ) {
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
					if( edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( e ) ) {
						//pass
					} else {
						ControlPanel.this.boundedRangeModel.setValue( 1 );
					}
				}
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
				public void mouseEntered( java.awt.event.MouseEvent e ) {
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
				}
			} );

			javax.swing.JPanel leadingPanel = new javax.swing.JPanel();
			leadingPanel.setLayout( new javax.swing.BoxLayout( leadingPanel, javax.swing.BoxLayout.LINE_AXIS ) );
			leadingPanel.add( playPauseButton );
			leadingPanel.add( javax.swing.Box.createHorizontalStrut( 12 ) );
			leadingPanel.add( this.label );

			this.setLayout( new java.awt.BorderLayout( 8, 0 ) );
			this.add( leadingPanel, java.awt.BorderLayout.LINE_START );
			this.add( slider, java.awt.BorderLayout.CENTER );
			javax.swing.Action restartAction = ProgramImp.this.getRestartAction();
			if( restartAction != null ) {
				this.add( new javax.swing.JButton( restartAction ), java.awt.BorderLayout.LINE_END );
			}
		}
		
		private final void updateLabel() {
			StringBuilder sb = new StringBuilder();
			sb.append( "speed: " );
			sb.append( this.boundedRangeModel.getValue() );
			sb.append( "x" );
			this.label.setText( sb.toString() );
		}
	}
	
	private void handlePlayOrPause( boolean isPlay ) {
		double speedFactor = 0.0;
		if( isPlay ) {
			speedFactor = 1.0;
		} else {
			speedFactor = 0.0;
		}
		this.getAnimator().setSpeedFactor( speedFactor );
	}
	private void handleSpeedChange( double speedFactor ) {
		this.getAnimator().setSpeedFactor( speedFactor );
	}
	
	private javax.swing.Action restartAction;
	public javax.swing.Action getRestartAction() {
		return this.restartAction;
	}
	public void setRestartAction( javax.swing.Action restartAction ) {
		this.restartAction = restartAction;
	}
	
	protected javax.swing.JComponent createControlPanelIfDesired() {
		return new ControlPanel();
	}

	public org.lgna.story.Program getAbstraction() {
		return this.abstraction;
	}
	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		if( this.onscreenLookingGlass != null ) {
			//pass
		} else {
			this.onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().createHeavyweightOnscreenLookingGlass();
			//this.onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().createLightweightOnscreenLookingGlass();
		}
		return this.onscreenLookingGlass;
	}
	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
	}
	
	private edu.cmu.cs.dennisc.animation.ClockBasedAnimator getClockBasedAnimator() {
		if( this.clockBasedAnimator != null ) {
			//pass
		} else {
			this.clockBasedAnimator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
		}
		return this.clockBasedAnimator;
	}
	private edu.cmu.cs.dennisc.animation.FrameBasedAnimator getFrameBasedAnimator() {
		if( this.frameBasedAnimator != null ) {
			//pass
		} else {
			this.frameBasedAnimator = new edu.cmu.cs.dennisc.animation.FrameBasedAnimator();
		}
		return this.frameBasedAnimator;
	}
	public edu.cmu.cs.dennisc.animation.Animator getAnimator() {
		if( this.frameRate == CLOCK_BASED_FRAME_RATE ) {
			return this.getClockBasedAnimator();
		} else {
			return this.getFrameBasedAnimator();
		}
	}
	
	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_setClockBasedAnimator( edu.cmu.cs.dennisc.animation.ClockBasedAnimator clockBasedAnimator ) {
		this.clockBasedAnimator = clockBasedAnimator;
	}
	
	public Double getFrameRate() {
		return this.frameRate;
	}
	public void setFrameRate( Double frameRate ) {
		if( frameRate == CLOCK_BASED_FRAME_RATE ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.animation.FrameBasedAnimator frameBasedAnimator = this.getFrameBasedAnimator();
			frameBasedAnimator.setFramesPerSecond( frameRate );
		}
	}
	
	public double getSimulationSpeedFactor() {
		return this.simulationSpeedFactor;
	}
	public void setSimulationSpeedFactor( double simulationSpeedFactor ) {
		this.simulationSpeedFactor = simulationSpeedFactor;
	}
	
	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			ProgramImp.this.getAnimator().update();
		}
	};
	
	private boolean isAnimatorStarted = false;
	public void startAnimator() {
		edu.cmu.cs.dennisc.lookingglass.LookingGlassFactory lookingGlassFactory = this.getOnscreenLookingGlass().getLookingGlassFactory();
		lookingGlassFactory.addAutomaticDisplayListener( this.automaticDisplayListener );
		lookingGlassFactory.incrementAutomaticDisplayCount();
		this.isAnimatorStarted = true;
	}
	public void stopAnimator() {
		if( this.isAnimatorStarted ) {
			this.getAnimator().completeAll( null );
			edu.cmu.cs.dennisc.lookingglass.LookingGlassFactory lookingGlassFactory = this.getOnscreenLookingGlass().getLookingGlassFactory();
			lookingGlassFactory.decrementAutomaticDisplayCount();
			lookingGlassFactory.removeAutomaticDisplayListener( this.automaticDisplayListener );
			this.isAnimatorStarted = false;
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( this.isAnimatorStarted );
		}
	}
	
	private void addComponents( java.awt.Container container ) {
		synchronized( container.getTreeLock() ) {
			java.awt.Component awtComponent = this.getOnscreenLookingGlass().getAWTComponent();
			container.add( awtComponent );
			javax.swing.JComponent controlPanel = this.createControlPanelIfDesired();
			if( controlPanel != null ) {
				container.add( controlPanel, java.awt.BorderLayout.PAGE_START );
			}
			if (container instanceof javax.swing.JComponent	) {
				((javax.swing.JComponent)container).revalidate();
			}
		}
	}
	
	public void initializeInFrame( final javax.swing.JFrame frame, final Runnable runnable ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				ProgramImp.this.addComponents( frame.getContentPane() );
				frame.setVisible( true );
				runnable.run();
			}
		} );
	}
	public void initializeInAwtContainer( java.awt.Container container ) {
		assert container.getLayout() instanceof java.awt.BorderLayout;
		this.addComponents( container );
		this.startAnimator();
	}
	public void initializeInFrame( javax.swing.JFrame frame ) {
		final java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier( 2 );
		this.initializeInFrame( frame, new Runnable() {
			public void run() {
				try {
					barrier.await();
				} catch( InterruptedException ie ) {
					throw new RuntimeException( ie );
				} catch( java.util.concurrent.BrokenBarrierException bbe ) {
					throw new RuntimeException( bbe );
				}
			}
		} );
		try {
			barrier.await();
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		} catch( java.util.concurrent.BrokenBarrierException bbe ) {
			throw new RuntimeException( bbe );
		}
		this.startAnimator();
	}
	public void initializeInApplet( javax.swing.JApplet applet ) {
		this.addComponents( applet.getContentPane() );
		this.startAnimator();
	}
	private boolean isProgramClosedExceptionDesired = false;
	public void shutDown() {
		this.onscreenLookingGlass.release();
		this.stopAnimator();
		this.isProgramClosedExceptionDesired = true;
	}
	
	/*package-private*/ void perform( edu.cmu.cs.dennisc.animation.Animation animation, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		if( this.isProgramClosedExceptionDesired ) {
			this.stopAnimator();
			throw new org.lgna.project.ProgramClosedException();
		}
		this.getAnimator().invokeAndWait_ThrowRuntimeExceptionsIfNecessary( animation, animationObserver );
	}
}
