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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
class WaitingRunnable implements Runnable {
	private Runnable runnable;
	private Thread thread;
	private Exception exception;

	public WaitingRunnable( Runnable runnable, Thread thread ) {
		assert runnable != null;
		assert thread != null;
		this.runnable = runnable;
		this.thread = thread;
		this.exception = null;
	}
	public Exception getException() {
		return this.exception;
	}
	public void run() {
		try {
			this.runnable.run();
		} catch( Exception e ) {
			this.exception = e;
		} finally {
			synchronized( this.thread ) {
				this.thread.notifyAll();
			}
		}
	}
}
/**
 * @author Dennis Cosgrove
 */
public class LookingGlassFactory implements edu.cmu.cs.dennisc.lookingglass.LookingGlassFactory, edu.cmu.cs.dennisc.pattern.event.ReleaseListener {
	static {
		try {
			javax.media.opengl.GLDrawableFactory unused = javax.media.opengl.GLDrawableFactory.getFactory();
			//todo: jogl2
//			javax.media.opengl.GLProfile glProfile = javax.media.opengl.GLProfile.getDefault();
//			javax.media.opengl.GLDrawableFactory unused = javax.media.opengl.GLDrawableFactory.getFactory( glProfile );
		} catch( UnsatisfiedLinkError ule ) {
			String platformText = System.getProperty( "os.name" ) + "-" + System.getProperty( "os.arch" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "platform:", platformText );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "library path:", System.getProperty( "java.library.path" ) );
			edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( platformText );
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "UNABLE TO LOAD OPENGL" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "------ -- ---- ------" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "The most common reason for this is an incorrectly set Java library path.  Please update your VM arguments." );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "The text below has been copied to the clipboard for your convenience." );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( platformText );
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "all properties" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "--- ----------" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( edu.cmu.cs.dennisc.java.lang.SystemUtilities.getPropertiesAsXMLString() );
			
			ule.printStackTrace();
			
			System.exit( -1 );
		}
	}

	private static class SingletonHolder {
		private static LookingGlassFactory instance = new LookingGlassFactory();
	}
	public static LookingGlassFactory getInstance() {
		return SingletonHolder.instance;
	}
	
	private final java.util.List<LightweightOnscreenLookingGlass> lightweightOnscreenLookingGlasses = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final java.util.List<HeavyweightOnscreenLookingGlass> heavyweightOnscreenLookingGlasses = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final java.util.List<OffscreenLookingGlass> offscreenLookingGlasses = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	private final java.util.List<edu.cmu.cs.dennisc.pattern.Releasable> toBeReleased = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.Queue<Runnable> runnables = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newConcurrentLinkedQueue();

	private final java.util.concurrent.Semaphore renderingLock = new java.util.concurrent.Semaphore( 1 );

	private static class ReusableAutomaticDisplayEvent extends edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent {
		public ReusableAutomaticDisplayEvent( LookingGlassFactory lookingGlassFactory ) {
			super( lookingGlassFactory );
		}
		@Override
		public boolean isReservedForReuse() {
			return true;
		}
	};
	private final ReusableAutomaticDisplayEvent reusableAutomaticDisplayEvent = new ReusableAutomaticDisplayEvent( this );
	private final java.util.List< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener > automaticDisplayListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	private Animator animator = null;

	private LookingGlassFactory() {
	}
	
	//todo: just force start and stop? or rename methods
	private int automaticDisplayCount = 0;
	//private GLCapabilities glCapabilities;
	/*package-private*/ static javax.media.opengl.GLCapabilities createDesiredGLCapabilities( int desiredSampleCount ) {
		javax.media.opengl.GLCapabilities rv = new javax.media.opengl.GLCapabilities();
		boolean isMultisamplingDesired = desiredSampleCount >= 2;
		rv.setSampleBuffers( isMultisamplingDesired );
		if( isMultisamplingDesired ) {
			rv.setNumSamples( desiredSampleCount );
		}
		//todo: jogl2
//		javax.media.opengl.GLProfile profile = javax.media.opengl.GLProfile.getDefault();
//		javax.media.opengl.GLCapabilities rv = new javax.media.opengl.GLCapabilities( profile );
		
		
		
		
//		javax.media.opengl.GLCapabilitiesChooser chooser = getGLCapabilitiesChooser();
//		if( chooser instanceof edu.cmu.cs.dennisc.javax.media.opengl.HardwareAccellerationEschewingGLCapabilitiesChooser ) {
//			rv.setHardwareAccelerated( false );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: force hardware acceleration off" );
//			rv.setDepthBits( 32 );
//		}
		return rv;
	}
	
	private static javax.media.opengl.GLCapabilitiesChooser glCapabilitiesChooser;
	/*package-private*/ static javax.media.opengl.GLCapabilitiesChooser getGLCapabilitiesChooser() {
		if( glCapabilitiesChooser != null ) {
			//pass
		} else {
			//todo?
			glCapabilitiesChooser = new javax.media.opengl.DefaultGLCapabilitiesChooser();
		}
		return glCapabilitiesChooser;
	}
	
	/*package-private*/ static int getSampleCountForDisabledMultisampling() {
		return 1;
	}
	/*package-private*/ static int getDesiredOnscreenSampleCount() {
		return 1;
	}
	
	
	/*package-private*/ javax.media.opengl.GLCanvas createGLCanvas() {
		return new javax.media.opengl.GLCanvas( createDesiredGLCapabilities(getDesiredOnscreenSampleCount()), getGLCapabilitiesChooser(), null, null );
	}
	/*package-private*/ javax.media.opengl.GLJPanel createGLJPanel() {
		return new javax.media.opengl.GLJPanel( createDesiredGLCapabilities(getDesiredOnscreenSampleCount()), getGLCapabilitiesChooser(), null );
	}
	
	
//	/*package-private*/ boolean canCreateExternalGLDrawable() {
//		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
//		return glDrawableFactory.canCreateExternalGLDrawable();
//	}
//	/*package-private*/ javax.media.opengl.GLDrawable createExternalGLDrawable() {
//		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
//		if( glDrawableFactory.canCreateExternalGLDrawable() ) {
//			return glDrawableFactory.createExternalGLDrawable();
//		} else {
//			return null;
//		}
//	}
	
	/*package-private*/ boolean canCreateGLPbuffer() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "solve pBuffer crash on linux" );
			return false;
		} else {
			javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
			return glDrawableFactory.canCreateGLPbuffer();
		}
	}
	/*package-private*/ javax.media.opengl.GLPbuffer createGLPbuffer( int width, int height, int desiredSampleCount, javax.media.opengl.GLContext share ) {
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
		if (glDrawableFactory.canCreateGLPbuffer()) {
			return glDrawableFactory.createGLPbuffer(createDesiredGLCapabilities( desiredSampleCount ), getGLCapabilitiesChooser(), width, height, share);
		} else {
			return null;
		}
//todo: jogl2
//		javax.media.opengl.GLProfile glProfile = javax.media.opengl.GLProfile.getDefault();
//		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory( glProfile );
//		if (glDrawableFactory.canCreateGLPbuffer( glDrawableFactory.getDefaultDevice() )) {
//			return glDrawableFactory.createGLPbuffer( glDrawableFactory.getDefaultDevice(), createDesiredGLCapabilities(), getGLCapabilitiesChooser(), width, height, share);
//		} else {
//			throw new RuntimeException("cannot create pbuffer");
//		}
	}
	private static boolean isDisplayDesired( OnscreenLookingGlass lg ) {
		if( lg.isRenderingEnabled() ) {
			java.awt.Component component = lg.getAWTComponent();
			if( component.isVisible() && component.isValid() && component.getWidth() > 0 && component.getHeight() > 0 ) {
				if( lg.getCameraCount() > 0 ) {
					return true;
				}
			}
		}
		return false;
	}

	public Animator.ThreadDeferenceAction step() {
		Animator.ThreadDeferenceAction rv = Animator.ThreadDeferenceAction.SLEEP;
		synchronized( this.toBeReleased ) {
			for( edu.cmu.cs.dennisc.pattern.Releasable releasable : this.toBeReleased ) {
				if( releasable instanceof OnscreenLookingGlass ) {
					OnscreenLookingGlass onscreenLookingGlass = (OnscreenLookingGlass)releasable;
					if( onscreenLookingGlass instanceof HeavyweightOnscreenLookingGlass ) {
						this.heavyweightOnscreenLookingGlasses.remove( onscreenLookingGlass );
					} else if( onscreenLookingGlass instanceof LightweightOnscreenLookingGlass ) {
						this.lightweightOnscreenLookingGlasses.remove( onscreenLookingGlass );
					} else {
						assert false;
					}
					//this.animator.remove( onscreenLookingGlass.getGLAutoDrawable() );
				} else if( releasable instanceof OffscreenLookingGlass ) {
					this.offscreenLookingGlasses.remove( releasable );
				} else {
					assert false;
				}
			}
			this.toBeReleased.clear();
		}

		if( this.automaticDisplayCount > 0 ) {
			acquireRenderingLock();
			try {
				ChangeHandler.pushRenderingMode();
				for( OnscreenLookingGlass lg : this.heavyweightOnscreenLookingGlasses ) {
					if( isDisplayDesired( lg ) ) {
						//lg.getGLAutoDrawable().display();
						lg.repaint();
//							edu.cmu.cs.dennisc.print.PrintUtilities.println( lg, System.currentTimeMillis() );
						rv = Animator.ThreadDeferenceAction.YIELD;
					}
				}
				try {
					if( ChangeHandler.getEventCountSinceLastReset() > 0 /*|| isJustCreatedOnscreenLookingGlassAccountedFor == false*/ ) {
						ChangeHandler.resetEventCount();
						//isJustCreatedOnscreenLookingGlassAccountedFor = true;
						for( OnscreenLookingGlass lg : this.lightweightOnscreenLookingGlasses ) {
							if( isDisplayDesired( lg ) ) {
								//lg.getGLAutoDrawable().display();
								lg.repaint();
								//edu.cmu.cs.dennisc.print.PrintUtilities.println( lg );
								rv = Animator.ThreadDeferenceAction.YIELD;
							}
						}
					}
				} finally {
					ChangeHandler.popRenderingMode();
				}
			} finally {
				releaseRenderingLock();
			}
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "this.automaticDisplayCount", this.automaticDisplayCount );
		}
		LookingGlassFactory.this.handleDisplayed();
		return rv;
	}
	
	public void acquireRenderingLock() {
		try {
			this.renderingLock.acquire();
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		}
	}
	public void releaseRenderingLock() {
		this.renderingLock.release();
	}
	

	public int getAutomaticDisplayCount() {
		return this.automaticDisplayCount;
	}
	public synchronized void incrementAutomaticDisplayCount() {
		this.automaticDisplayCount++;
		if( this.animator != null ) {
			//pass
		} else {
			this.animator = new Animator() {
				@Override
				protected ThreadDeferenceAction step() {
					return LookingGlassFactory.this.step();
				}
			};
			this.animator.start();
		}
	}
	public synchronized void decrementAutomaticDisplayCount() {
		this.automaticDisplayCount--;
	}
		
	public void releasing( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent releaseEvent ) {
	}
	public void released( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent releaseEvent ) {
		synchronized( this.toBeReleased ) {
			this.toBeReleased.add( releaseEvent.getTypedSource() );
		}
	}
	
	public edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass createLightweightOnscreenLookingGlass() {
		LightweightOnscreenLookingGlass lolg = new LightweightOnscreenLookingGlass( this );
		lolg.addReleaseListener( this );
		this.lightweightOnscreenLookingGlasses.add( lolg );
		return lolg;
	}
	public edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass createHeavyweightOnscreenLookingGlass() {
		HeavyweightOnscreenLookingGlass holg = new HeavyweightOnscreenLookingGlass( this );
		holg.addReleaseListener( this );
		this.heavyweightOnscreenLookingGlasses.add( holg );
		return holg;
	}
	public edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass createOffscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlassToShareContextWith ) {
		assert lookingGlassToShareContextWith == null || lookingGlassToShareContextWith instanceof AbstractLookingGlass;
		OffscreenLookingGlass olg = new OffscreenLookingGlass( this, (AbstractLookingGlass)lookingGlassToShareContextWith );
		olg.addReleaseListener( this );
		this.offscreenLookingGlasses.add( olg );
		return olg;
	}

	public void addAutomaticDisplayListener( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener ) {
		this.automaticDisplayListeners.add( automaticDisplayListener );
	}
	public void removeAutomaticDisplayListener( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener ) {
		this.automaticDisplayListeners.remove( automaticDisplayListener );
	}
	public Iterable< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener > getAutomaticDisplayListeners() {
		return this.automaticDisplayListeners;
	}

	private void handleDisplayed() {
		for( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener : this.automaticDisplayListeners ) {
			automaticDisplayListener.automaticDisplayCompleted( reusableAutomaticDisplayEvent );
		}
		while( this.runnables.isEmpty() == false ) {
			Runnable runnable = this.runnables.remove();
			runnable.run();
		}
	}

	public void invokeLater( Runnable runnable ) {
		this.runnables.add( runnable );
	}
	public void invokeAndWait( Runnable runnable ) throws InterruptedException, java.lang.reflect.InvocationTargetException {
		Thread currentThread = Thread.currentThread();
		WaitingRunnable waitingRunnable = new WaitingRunnable( runnable, currentThread );
		synchronized( currentThread ) {
			this.runnables.add( waitingRunnable );
			currentThread.wait();
		}
		if( waitingRunnable.getException() != null ) {
			throw new java.lang.reflect.InvocationTargetException( waitingRunnable.getException() );
		}
	}
	public void invokeAndWait_ThrowRuntimeExceptionsIfNecessary( Runnable runnable ) {
		try {
			invokeAndWait( runnable );
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		} catch( java.lang.reflect.InvocationTargetException ie ) {
			throw new RuntimeException( ie );
		}
	}

	public Iterable< ? extends edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass > getLightweightOnscreenLookingGlasses() {
		return this.lightweightOnscreenLookingGlasses;
	}
	public Iterable< ? extends edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass > getHeavyweightOnscreenLookingGlasses() {
		return this.heavyweightOnscreenLookingGlasses;
	}
	public Iterable< ? extends edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass > getOffscreenLookingGlasses() {
		return this.offscreenLookingGlasses;
	}
}
