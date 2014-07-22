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

	@Override
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
		edu.cmu.cs.dennisc.renderer.RendererNativeLibraryLoader.initializeIfNecessary();
	}

	private static class SingletonHolder {
		private static LookingGlassFactory instance = new LookingGlassFactory();
	}

	public static LookingGlassFactory getInstance() {
		return SingletonHolder.instance;
	}

	private final java.util.List<LightweightOnscreenLookingGlass> lightweightOnscreenLookingGlasses = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<HeavyweightOnscreenLookingGlass> heavyweightOnscreenLookingGlasses = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<OffscreenLookingGlass> offscreenLookingGlasses = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private final java.util.List<edu.cmu.cs.dennisc.pattern.Releasable> toBeReleased = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	private final java.util.Queue<Runnable> runnables = edu.cmu.cs.dennisc.java.util.Queues.newConcurrentLinkedQueue();

	private final java.util.concurrent.Semaphore renderingLock = new java.util.concurrent.Semaphore( 1 );

	private static class ReusableAutomaticDisplayEvent extends edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayEvent {
		public ReusableAutomaticDisplayEvent( LookingGlassFactory lookingGlassFactory ) {
			super( lookingGlassFactory );
		}

		@Override
		public boolean isReservedForReuse() {
			return true;
		}
	};

	private final ReusableAutomaticDisplayEvent reusableAutomaticDisplayEvent = new ReusableAutomaticDisplayEvent( this );
	private final java.util.List<edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener> automaticDisplayListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private Animator animator = null;

	private LookingGlassFactory() {
	}

	//todo: just force start and stop? or rename methods
	private int automaticDisplayCount = 0;

	private static boolean isDisplayDesired( OnscreenLookingGlass lg ) {
		if( lg.isRenderingEnabled() ) {
			java.awt.Component component = lg.getAwtComponent();
			if( component.isVisible() && component.isValid() && ( component.getWidth() > 0 ) && ( component.getHeight() > 0 ) ) {
				if( lg.getSgCameraCount() > 0 ) {
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
					if( ChangeHandler.getEventCountSinceLastReset() > 0 /* || isJustCreatedOnscreenLookingGlassAccountedFor == false */) {
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

	@Override
	public void acquireRenderingLock() {
		try {
			this.renderingLock.acquire();
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		}
	}

	@Override
	public void releaseRenderingLock() {
		this.renderingLock.release();
	}

	@Override
	public int getAutomaticDisplayCount() {
		return this.automaticDisplayCount;
	}

	@Override
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

	@Override
	public synchronized void decrementAutomaticDisplayCount() {
		this.automaticDisplayCount--;
	}

	@Override
	public void releasing( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent releaseEvent ) {
	}

	@Override
	public void released( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent releaseEvent ) {
		synchronized( this.toBeReleased ) {
			this.toBeReleased.add( releaseEvent.getTypedSource() );
		}
	}

	@Override
	public edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass createLightweightOnscreenLookingGlass() {
		LightweightOnscreenLookingGlass lolg = new LightweightOnscreenLookingGlass( this );
		lolg.addReleaseListener( this );
		this.lightweightOnscreenLookingGlasses.add( lolg );
		return lolg;
	}

	@Override
	public edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass createHeavyweightOnscreenLookingGlass() {
		HeavyweightOnscreenLookingGlass holg = new HeavyweightOnscreenLookingGlass( this );
		holg.addReleaseListener( this );
		this.heavyweightOnscreenLookingGlasses.add( holg );
		return holg;
	}

	@Override
	public edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass createOffscreenLookingGlass( int width, int height, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlassToShareContextWith ) {
		assert ( lookingGlassToShareContextWith == null ) || ( lookingGlassToShareContextWith instanceof AbstractLookingGlass );
		OffscreenLookingGlass olg = new OffscreenLookingGlass( this, width, height, (AbstractLookingGlass)lookingGlassToShareContextWith );
		olg.addReleaseListener( this );
		this.offscreenLookingGlasses.add( olg );
		return olg;
	}

	@Override
	public void addAutomaticDisplayListener( edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener automaticDisplayListener ) {
		this.automaticDisplayListeners.add( automaticDisplayListener );
	}

	@Override
	public void removeAutomaticDisplayListener( edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener automaticDisplayListener ) {
		this.automaticDisplayListeners.remove( automaticDisplayListener );
	}

	@Override
	public Iterable<edu.cmu.cs.dennisc.renderer.event.AutomaticDisplayListener> getAutomaticDisplayListeners() {
		return this.automaticDisplayListeners;
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

	@Override
	public void invokeLater( Runnable runnable ) {
		this.runnables.add( runnable );
	}

	@Override
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

	@Override
	public void invokeAndWait_ThrowRuntimeExceptionsIfNecessary( Runnable runnable ) {
		try {
			invokeAndWait( runnable );
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		} catch( java.lang.reflect.InvocationTargetException ie ) {
			throw new RuntimeException( ie );
		}
	}

	@Override
	public Iterable<? extends edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass> getLightweightOnscreenLookingGlasses() {
		return this.lightweightOnscreenLookingGlasses;
	}

	@Override
	public Iterable<? extends edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass> getHeavyweightOnscreenLookingGlasses() {
		return this.heavyweightOnscreenLookingGlasses;
	}

	@Override
	public Iterable<? extends edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass> getOffscreenLookingGlasses() {
		return this.offscreenLookingGlasses;
	}
}
