/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.renderer.gl;

abstract class Animator implements Runnable {
	public static final long DEFAULT_SLEEP_MILLIS = 5;
	private boolean isActive = false;
	private long tStart;
	private int frameCount;
	private long sleepMillis = DEFAULT_SLEEP_MILLIS;

	public enum ThreadDeferenceAction {
		DO_NOTHING,
		SLEEP,
		YIELD
	}

	public void start() {
		this.isActive = true;
		this.frameCount = 0;
		this.tStart = System.currentTimeMillis();
		//		javax.swing.SwingUtilities.invokeLater( this );
		new Thread( this ).start();
	}

	public void stop() {
		this.isActive = false;
		//long tDelta = System.currentTimeMillis() - this.tStart;
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( this.frameCount, tDelta, this.frameCount/(tDelta*0.001) );
	}

	public int getFrameCount() {
		return this.frameCount;
	}

	public long getStartTimeMillis() {
		return this.tStart;
	}

	public long getSleepMillis() {
		return this.sleepMillis;
	}

	public void setSleepMillis( long sleepMillis ) {
		this.sleepMillis = sleepMillis;
	}

	protected abstract ThreadDeferenceAction step();

	public void run() {
		final long THRESHOLD = 5;
		long tPrev = System.currentTimeMillis() - THRESHOLD;
		while( this.isActive ) {
			//			int i = 0;
			while( true ) {
				long tCurrent = System.currentTimeMillis();
				if( ( tCurrent - tPrev ) < THRESHOLD ) {
					edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 5 );
					//					i++;
				} else {
					tPrev = tCurrent;
					break;
				}
			}
			//			if( i>3 ) {
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "sleep count:", i );
			//			}
			//		if( this.isActive ) {
			//			try {
			ThreadDeferenceAction threadAction = this.step();
			if( threadAction == ThreadDeferenceAction.SLEEP ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "sleep", this.sleepMillis );
				edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( this.sleepMillis );
			} else if( threadAction == ThreadDeferenceAction.YIELD ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "yield" );
				Thread.yield();
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "threadAction", threadAction );
			}
			this.frameCount++;
			//			} finally {
			//				javax.swing.SwingUtilities.invokeLater( this );
			//			}
		}
	}
}

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
public enum GlRenderFactory implements edu.cmu.cs.dennisc.renderer.RenderFactory {
	INSTANCE;
	private static boolean isDisplayDesired( edu.cmu.cs.dennisc.renderer.OnscreenRenderTarget<?> rt ) {
		if( rt.isRenderingEnabled() ) {
			java.awt.Component component = rt.getAwtComponent();
			if( component.isVisible() && component.isValid() && ( component.getWidth() > 0 ) && ( component.getHeight() > 0 ) ) {
				if( rt.getSgCameraCount() > 0 ) {
					return true;
				}
			}
		}
		return false;
	}

	private static class ReusableAutomaticDisplayEvent extends edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayEvent {
		public ReusableAutomaticDisplayEvent( GlRenderFactory renderFactory ) {
			super( renderFactory );
		}

		@Override
		public boolean isReservedForReuse() {
			return true;
		}
	};

	public edu.cmu.cs.dennisc.renderer.ColorBuffer createColorBuffer() {
		return new edu.cmu.cs.dennisc.renderer.gl.GlColorBuffer();
	}

	public edu.cmu.cs.dennisc.renderer.ColorAndDepthBuffers createColorAndDepthBuffers() {
		return new edu.cmu.cs.dennisc.renderer.gl.GlColorAndDepthBuffers();
	}

	public edu.cmu.cs.dennisc.renderer.HeavyweightOnscreenRenderTarget createHeavyweightOnscreenRenderTarget() {
		edu.cmu.cs.dennisc.renderer.HeavyweightOnscreenRenderTarget rv = new edu.cmu.cs.dennisc.renderer.gl.GlHeavyweightOnscreenRenderTarget();
		this.heavyweightOnscreenRenderTargets.add( rv );
		return rv;
	}

	public edu.cmu.cs.dennisc.renderer.LightweightOnscreenRenderTarget createLightweightOnscreenRenderTarget() {
		edu.cmu.cs.dennisc.renderer.LightweightOnscreenRenderTarget rv = new edu.cmu.cs.dennisc.renderer.gl.GlLightweightOnscreenRenderTarget();
		this.lightweightOnscreenRenderTargets.add( rv );
		return rv;
	}

	public edu.cmu.cs.dennisc.renderer.OffscreenRenderTarget createOffscreenRenderTarget( int width, int height ) {
		edu.cmu.cs.dennisc.renderer.OffscreenRenderTarget rv = new edu.cmu.cs.dennisc.renderer.gl.GlOffscreenRenderTarget( width, height );
		this.offscreenRenderTargets.add( rv );
		return rv;
	}

	public Iterable<edu.cmu.cs.dennisc.renderer.HeavyweightOnscreenRenderTarget> getHeavyweightOnscreenLookingGlasses() {
		return java.util.Collections.unmodifiableList( this.heavyweightOnscreenRenderTargets );
	}

	public Iterable<edu.cmu.cs.dennisc.renderer.LightweightOnscreenRenderTarget> getLightweightOnscreenLookingGlasses() {
		return java.util.Collections.unmodifiableList( this.lightweightOnscreenRenderTargets );
	}

	public Iterable<edu.cmu.cs.dennisc.renderer.OffscreenRenderTarget> getOffscreenLookingGlasses() {
		return java.util.Collections.unmodifiableList( this.offscreenRenderTargets );
	}

	public Animator.ThreadDeferenceAction step() {
		Animator.ThreadDeferenceAction rv = Animator.ThreadDeferenceAction.SLEEP;
		//		synchronized( this.toBeReleased ) {
		//			for( edu.cmu.cs.dennisc.pattern.Releasable releasable : this.toBeReleased ) {
		//				if( releasable instanceof OnscreenLookingGlass ) {
		//					OnscreenLookingGlass onscreenLookingGlass = (OnscreenLookingGlass)releasable;
		//					if( onscreenLookingGlass instanceof HeavyweightOnscreenLookingGlass ) {
		//						this.heavyweightOnscreenRenderTargets.remove( onscreenLookingGlass );
		//					} else if( onscreenLookingGlass instanceof LightweightOnscreenLookingGlass ) {
		//						this.lightweightOnscreenRenderTargets.remove( onscreenLookingGlass );
		//					} else {
		//						assert false;
		//					}
		//					//this.animator.remove( onscreenLookingGlass.getGLAutoDrawable() );
		//				} else if( releasable instanceof OffscreenLookingGlass ) {
		//					this.offscreenRenderTargets.remove( releasable );
		//				} else {
		//					assert false;
		//				}
		//			}
		//			this.toBeReleased.clear();
		//		}

		if( this.automaticDisplayCount > 0 ) {
			acquireRenderingLock();
			try {
				edu.cmu.cs.dennisc.renderer.gl.ChangeHandler.pushRenderingMode();
				for( edu.cmu.cs.dennisc.renderer.HeavyweightOnscreenRenderTarget rt : this.heavyweightOnscreenRenderTargets ) {
					if( isDisplayDesired( rt ) ) {
						//lg.getGLAutoDrawable().display();
						rt.repaint();
						//							edu.cmu.cs.dennisc.print.PrintUtilities.println( lg, System.currentTimeMillis() );
						rv = Animator.ThreadDeferenceAction.YIELD;
					}
				}
				try {
					if( edu.cmu.cs.dennisc.renderer.gl.ChangeHandler.getEventCountSinceLastReset() > 0 /* || isJustCreatedOnscreenLookingGlassAccountedFor == false */) {
						edu.cmu.cs.dennisc.renderer.gl.ChangeHandler.resetEventCount();
						//isJustCreatedOnscreenLookingGlassAccountedFor = true;
						for( edu.cmu.cs.dennisc.renderer.OnscreenRenderTarget<?> rt : this.lightweightOnscreenRenderTargets ) {
							if( isDisplayDesired( rt ) ) {
								//lg.getGLAutoDrawable().display();
								rt.repaint();
								//edu.cmu.cs.dennisc.print.PrintUtilities.println( lg );
								rv = Animator.ThreadDeferenceAction.YIELD;
							}
						}
					}
				} finally {
					edu.cmu.cs.dennisc.renderer.gl.ChangeHandler.popRenderingMode();
				}
			} finally {
				releaseRenderingLock();
			}
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "this.automaticDisplayCount", this.automaticDisplayCount );
		}
		GlRenderFactory.this.handleDisplayed();
		return rv;
	}

	private void handleDisplayed() {
		for( edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener automaticDisplayListener : this.automaticDisplayListeners ) {
			automaticDisplayListener.automaticDisplayCompleted( reusableAutomaticDisplayEvent );
		}
		while( this.runnables.isEmpty() == false ) {
			Runnable runnable = this.runnables.remove();
			runnable.run();
		}
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

	public void addAutomaticDisplayListener( edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener automaticDisplayListener ) {
		this.automaticDisplayListeners.add( automaticDisplayListener );
	}

	public void removeAutomaticDisplayListener( edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener automaticDisplayListener ) {
		this.automaticDisplayListeners.remove( automaticDisplayListener );
	}

	public Iterable<edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener> getAutomaticDisplayListeners() {
		return java.util.Collections.unmodifiableList( this.automaticDisplayListeners );
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
					return GlRenderFactory.this.step();
				}
			};
			this.animator.start();
		}
	}

	public synchronized void decrementAutomaticDisplayCount() {
		this.automaticDisplayCount--;
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

	private final java.util.List<edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener> automaticDisplayListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<edu.cmu.cs.dennisc.renderer.LightweightOnscreenRenderTarget> lightweightOnscreenRenderTargets = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<edu.cmu.cs.dennisc.renderer.HeavyweightOnscreenRenderTarget> heavyweightOnscreenRenderTargets = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<edu.cmu.cs.dennisc.renderer.OffscreenRenderTarget> offscreenRenderTargets = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.Queue<Runnable> runnables = edu.cmu.cs.dennisc.java.util.Queues.newConcurrentLinkedQueue();
	private final java.util.concurrent.Semaphore renderingLock = new java.util.concurrent.Semaphore( 1 );
	private final ReusableAutomaticDisplayEvent reusableAutomaticDisplayEvent = new ReusableAutomaticDisplayEvent( this );

	private Animator animator;
	//todo: just force start and stop? or rename methods
	private int automaticDisplayCount;

	public static void main( String[] args ) throws Exception {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				edu.cmu.cs.dennisc.scenegraph.util.World sgWorld = new edu.cmu.cs.dennisc.scenegraph.util.World();
				edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes sgAxes = new edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes( 1.0 );
				sgAxes.setParent( sgWorld );
				sgWorld.getSGCameraVehicle().setTranslationOnly( 4, 4, 4, sgAxes );
				sgWorld.getSGCameraVehicle().setAxesOnlyToPointAt( sgAxes );
				edu.cmu.cs.dennisc.renderer.OnscreenRenderTarget<?> renderTarget = INSTANCE.createHeavyweightOnscreenRenderTarget();
				renderTarget.addSgCamera( sgWorld.getSGCamera() );

				javax.swing.JFrame frame = new javax.swing.JFrame();
				frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
				frame.getContentPane().add( renderTarget.getAwtComponent(), java.awt.BorderLayout.CENTER );
				frame.setPreferredSize( edu.cmu.cs.dennisc.math.GoldenRatio.createWiderSizeFromWidth( 640 ) );
				frame.pack();
				frame.setVisible( true );
			}
		} );
	}
}
