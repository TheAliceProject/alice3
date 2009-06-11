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
	private Animator m_animator;
	private edu.cmu.cs.dennisc.movie.MovieEncoder m_movieEncoder = null;

	protected void handleSpeedChange( double speed ) {
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

	private java.awt.image.BufferedImage reusableImage = null;
	protected void updateAnimator() {
		if( m_animator != null ) {
			if( m_movieEncoder != null ) {
				edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = this.getOnscreenLookingGlass();
				if( lg.getWidth() > 0 && lg.getHeight() > 0 ) {
					if( reusableImage == null || reusableImage.getWidth() != lg.getWidth() || reusableImage.getHeight() != lg.getHeight() ) {
						reusableImage = lg.createBufferedImageForUseAsColorBuffer();
					}
					if( this.reusableImage != null ) {
						lg.getColorBuffer( this.reusableImage );
						m_movieEncoder.addBufferedImage( this.reusableImage );
					}
				}
			}
			m_animator.update();
		}
	}

	
	protected java.awt.Component createSpeedMultiplierControlPanel() {
		return new ControlPanel( this );
	}
	
	@Override
	protected void initializeAWT( java.awt.Container container, edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		super.initializeAWT( container, onscreenLookingGlass );
		java.awt.Component speedMultiplierControlPanel = this.createSpeedMultiplierControlPanel();
		if( speedMultiplierControlPanel != null ) {
			add( speedMultiplierControlPanel, java.awt.BorderLayout.NORTH );
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
