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
public abstract class Program extends edu.cmu.cs.dennisc.lookingglass.DefaultProgram {
	class ControlPanel extends javax.swing.JPanel {
		private static final String PAUSE_TEXT = "pause";
		private static final String RESUME_TEXT = "resume";

		private javax.swing.JButton m_pauseResume = new javax.swing.JButton( "pause" );
		private javax.swing.JLabel m_feedback = new javax.swing.JLabel( "speed: 1x" );
		private javax.swing.JSlider m_speed = new javax.swing.JSlider();
		private javax.swing.JButton m_restart = new javax.swing.JButton( "restart" );

		public ControlPanel() {

			m_speed.setMinimum( 1 );
			m_speed.setValue( 1 );
			m_speed.setMaximum( 10 );

			m_restart.setEnabled( Program.this.isRestartSupported() );

			m_pauseResume.addActionListener( new java.awt.event.ActionListener() {
				public void actionPerformed( java.awt.event.ActionEvent e ) {
					boolean isPaused = ControlPanel.this.isPaused();
					if( isPaused ) {
						m_pauseResume.setText( PAUSE_TEXT );
						Program.this.handleSpeedChange( m_speed.getValue() );
					} else {
						m_pauseResume.setText( RESUME_TEXT );
						Program.this.handleSpeedChange( 0 );
					}
					m_speed.setEnabled( isPaused );
					m_feedback.setEnabled( isPaused );
					updateFeedback();
				}
			} );
			m_restart.addActionListener( new java.awt.event.ActionListener() {
				public void actionPerformed( java.awt.event.ActionEvent e ) {
					Program.this.restart( e );
				}
			} );

			m_speed.addChangeListener( new javax.swing.event.ChangeListener() {
				public void stateChanged( javax.swing.event.ChangeEvent e ) {
					ControlPanel.this.updateFeedback();
					Program.this.handleSpeedChange( m_speed.getValue() );
				}
			} );
			m_speed.addMouseListener( new java.awt.event.MouseListener() {
				public void mousePressed( java.awt.event.MouseEvent e ) {
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
					if( edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( e ) ) {
						//pass
					} else {
						m_speed.setValue( 1 );
					}
				}
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
				public void mouseEntered( java.awt.event.MouseEvent e ) {
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
				}
			} );

			setLayout( new java.awt.GridBagLayout() );
			java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
			gbc.fill = java.awt.GridBagConstraints.BOTH;
			add( m_pauseResume, gbc );
			add( m_feedback, gbc );
			gbc.weightx = 1.0;
			add( m_speed, gbc );
			gbc.weightx = 0.0;
			add( m_restart, gbc );
		}

		private boolean isPaused() {
			return m_pauseResume.getText().equals( RESUME_TEXT );
		}

		private void updateFeedback() {
			if( isPaused() ) {
				m_feedback.setText( "speed: 0x" );
			} else {
				m_feedback.setText( "speed: " + m_speed.getValue() + "x" );
			}
		}
	}

	private ControlPanel m_controlPanel = new ControlPanel();
	private Animator m_animator;
	private edu.cmu.cs.dennisc.movie.MovieEncoder m_movieEncoder = null;

	private void handleSpeedChange( double speed ) {
		if( m_animator != null ) {
			m_animator.setSpeedFactor( speed );
		}
	}

	public Animator getAnimator() {
		return m_animator;
	}
	protected void setAnimator( Animator animator ) {
		m_animator = animator;
	}
	protected Animator createAnimator() {
		if( m_movieEncoder != null ) {
			return new FrameBasedAnimator();
		} else {
			return new ClockBasedAnimator();
		}
	}
	
	
	protected boolean isRestartSupported() {
		return false;
	}
	protected void restart( java.util.EventObject e ) {
		throw new RuntimeException();
	}

	public edu.cmu.cs.dennisc.movie.MovieEncoder getMovieEncoder() {
		return m_movieEncoder;
	}
	public void setMovieEncoder( edu.cmu.cs.dennisc.movie.MovieEncoder movieEncoder ) {
		if( m_movieEncoder != null ) {
			m_movieEncoder.stop();
		}
		m_movieEncoder = movieEncoder;
		if( m_movieEncoder != null ) {
			m_movieEncoder.start();
		}
	}

	protected void updateAnimator() {
		if( m_animator != null ) {
			if( m_movieEncoder != null ) {
				m_movieEncoder.addBufferedImage( getOnscreenLookingGlass().getColorBuffer() );
			}
			m_animator.update();
		}
	}

	
	protected boolean isSpeedMultiplierControlPanelDesired() {
		return true;
	}

	@Override
	protected void initializeAWT( java.awt.Container container, edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		super.initializeAWT( container, onscreenLookingGlass );
		if( isSpeedMultiplierControlPanelDesired() ) {
			add( m_controlPanel, java.awt.BorderLayout.NORTH );
		}
	}

	@Override
	protected void preInitialize() {
		super.preInitialize();
		getLookingGlassFactory().addAutomaticDisplayListener( new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
			public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
				Program.this.updateAnimator();
			}
		} );
	}

	@Override
	protected void preRun() {
		super.preRun();
		String movieEncoderDirectoryPath = System.getProperty( Program.class.getName() + ".movieEncoderDirectoryPath" );
		if( movieEncoderDirectoryPath != null ) {
			setMovieEncoder( new edu.cmu.cs.dennisc.movie.seriesofimages.SeriesOfImagesMovieEncoder( movieEncoderDirectoryPath, "capture", "000", "bmp" ) );
		}
		if( m_animator != null ) {
			//pass
		} else {
			setAnimator( createAnimator() );
		}
	}
	@Override
	protected void postRun() {
		super.postRun();
		setMovieEncoder( null );
	}

	public void perform( Animation animation, AnimationObserver animationObserver ) {
		if( this.isClosed() ) {
			throw new edu.cmu.cs.dennisc.program.ProgramClosedException();
		}
		if( m_animator != null ) {
			m_animator.invokeAndWait_ThrowRuntimeExceptionsIfNecessary( animation, animationObserver );
		} else {
			//todo: issue warning?
			animation.complete( animationObserver );
		}
	}
}
