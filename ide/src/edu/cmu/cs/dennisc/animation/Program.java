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
package edu.cmu.cs.dennisc.animation;

/**
 * @author Dennis Cosgrove
 */
public abstract class Program extends edu.cmu.cs.dennisc.lookingglass.DefaultProgram {
	private Animator animator;
	private edu.cmu.cs.dennisc.movie.MovieEncoder movieEncoder = null;

	protected void handleSpeedChange( double speed ) {
		if( this.animator != null ) {
			this.animator.setSpeedFactor( speed );
		}
	}

	public Animator getAnimator() {
		return this.animator;
	}
	protected void setAnimator( Animator animator ) {
		this.animator = animator;
	}
	protected Animator createAnimator() {
		if( this.movieEncoder != null ) {
			return new FrameBasedAnimator();
		} else {
			return new ClockBasedAnimator();
		}
	}
	
	
	protected abstract boolean isRestartSupported();
	protected void restart( java.util.EventObject e ) {
		this.animator.completeAll( null );
	}

	private Object monitor = new Object();
	
	public edu.cmu.cs.dennisc.movie.MovieEncoder getMovieEncoder() {
		return this.movieEncoder;
	}
	public void setMovieEncoder( edu.cmu.cs.dennisc.movie.MovieEncoder movieEncoder ) {
		synchronized( this.monitor ) {
			if( this.movieEncoder != null ) {
				this.movieEncoder.stop();
			}
			this.movieEncoder = movieEncoder;
			if( this.movieEncoder != null ) {
				this.movieEncoder.start();
			}
		}
	}

	private java.awt.image.BufferedImage reusableImage = null;
	protected void updateAnimator() {
		if( this.animator != null ) {
			synchronized( this.monitor ) {
				if( this.movieEncoder != null ) {
					edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = this.getOnscreenLookingGlass();
					if( lg.getWidth() > 0 && lg.getHeight() > 0 ) {
						if( reusableImage == null || reusableImage.getWidth() != lg.getWidth() || reusableImage.getHeight() != lg.getHeight() ) {
							reusableImage = lg.createBufferedImageForUseAsColorBuffer();
						}
						if( this.reusableImage != null ) {
							lg.getColorBuffer( this.reusableImage );
							this.movieEncoder.addBufferedImage( this.reusableImage );
						}
					}
				}
			}
			this.animator.update();
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
		if( this.animator != null ) {
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

	@Override
	protected void handleWindowClosed( java.awt.event.WindowEvent e ) {
		super.handleWindowClosed( e );
		this.animator.completeAll( null );
	}
	public void perform( Animation animation, AnimationObserver animationObserver ) {
		if( this.isClosed() ) {
			throw new edu.cmu.cs.dennisc.alice.ProgramClosedException();
		}
		if( this.animator != null ) {
			this.animator.invokeAndWait_ThrowRuntimeExceptionsIfNecessary( animation, animationObserver );
		} else {
			//todo: issue warning?
			animation.complete( animationObserver );
		}
	}
}
