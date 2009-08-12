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

package edu.cmu.cs.dennisc.lookingglass.opengl;

import edu.cmu.cs.dennisc.lookingglass.opengl.Animator.ThreadDeferenceAction;

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
	private static LookingGlassFactory s_singleton;

	static {
		try {
			javax.media.opengl.GLDrawableFactory unused = javax.media.opengl.GLDrawableFactory.getFactory();
		} catch( UnsatisfiedLinkError ule ) {
			//final String JAVA_LIBRARY_PATH = "java.library.path";
			//String pathOriginal = System.getProperty( JAVA_LIBRARY_PATH );
			StringBuffer sb = new StringBuffer();
			String libraryPath;
			if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
				libraryPath = "\"/Applications/Alice3Beta/ext/jogl/lib/macosx-universal\"";
			} else if( edu.cmu.cs.dennisc.lang.SystemUtilities.isWindows() ) {
				libraryPath = "\"/Program Files/Alice3Beta/ext/jogl/lib/windows-i586\"";
			} else {
				libraryPath = "?/Alice3Beta/ext/jogl/lib/?";
			}
			sb.append( "-ea -Xmx1024m -Djava.library.path=" + libraryPath );
			
			String javaLibraryPath = sb.toString();
			
			edu.cmu.cs.dennisc.clipboard.ClipboardUtilities.setClipboardContents( javaLibraryPath );
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "UNABLE TO LOAD OPENGL" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "------ -- ---- ------" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "The most common reason for this is an incorrectly set Java library path.  Please update your VM arguments." );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "The text below has been copied to the clipboard for your convenience." );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( javaLibraryPath );
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			System.exit( 0 );
		}
	}
	public static LookingGlassFactory getSingleton() {
		if( s_singleton != null ) {
			//pass
		} else {
			s_singleton = new LookingGlassFactory();
		}
		return s_singleton;
	}


	
	private Animator animator = null;

	private java.util.Queue< Runnable > runnables = new java.util.LinkedList< Runnable >();
	private java.util.concurrent.Semaphore renderingLock = new java.util.concurrent.Semaphore( 1 );

	private java.util.List<edu.cmu.cs.dennisc.pattern.Releasable> toBeReleased = new java.util.LinkedList<edu.cmu.cs.dennisc.pattern.Releasable>();
	private java.util.List<LightweightOnscreenLookingGlass> lightweightOnscreenLookingGlasses = new java.util.LinkedList<LightweightOnscreenLookingGlass>();
	private java.util.List<HeavyweightOnscreenLookingGlass> heavyweightOnscreenLookingGlasses = new java.util.LinkedList<HeavyweightOnscreenLookingGlass>();
	private java.util.List<OffscreenLookingGlass> offscreenLookingGlasses = new java.util.LinkedList<OffscreenLookingGlass>();
	
	//todo: just force start and stop? or rename methods
	private int automaticDisplayCount = 0;
	
	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent automaticDisplayEvent = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent( this ) {
		@Override
		public boolean isReservedForReuse() {
			return true;
		}
	};
	private java.util.List< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener > automaticDisplayListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener >();

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

	private LookingGlassFactory() {
		s_singleton = this;
	}

	public ThreadDeferenceAction step() {
		ThreadDeferenceAction rv = ThreadDeferenceAction.SLEEP;
		synchronized( this.toBeReleased ) {
			for( edu.cmu.cs.dennisc.pattern.Releasable releasable : this.toBeReleased ) {
				if( releasable instanceof OnscreenLookingGlass ) {
					OnscreenLookingGlass onscreenLookingGlass = (OnscreenLookingGlass)releasable;
					if( onscreenLookingGlass instanceof HeavyweightOnscreenLookingGlass ) {
						synchronized( this.heavyweightOnscreenLookingGlasses ) {
							this.heavyweightOnscreenLookingGlasses.remove( onscreenLookingGlass );
						}
					} else if( onscreenLookingGlass instanceof LightweightOnscreenLookingGlass ) {
						synchronized( this.lightweightOnscreenLookingGlasses ) {
							this.lightweightOnscreenLookingGlasses.remove( onscreenLookingGlass );
						}
					} else {
						assert false;
					}
					//this.animator.remove( onscreenLookingGlass.getGLAutoDrawable() );
				} else if( releasable instanceof OffscreenLookingGlass ) {
					synchronized( this.offscreenLookingGlasses ) {
						this.offscreenLookingGlasses.remove( releasable );
					}
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
				try {
					synchronized( this.heavyweightOnscreenLookingGlasses ) {
						for( OnscreenLookingGlass lg : this.heavyweightOnscreenLookingGlasses ) {
							if( isDisplayDesired( lg ) ) {
								//lg.getGLAutoDrawable().display();
								lg.repaint();
//								edu.cmu.cs.dennisc.print.PrintUtilities.println( lg, System.currentTimeMillis() );
								rv = ThreadDeferenceAction.YIELD;
							}
						}
					}
					if( ChangeHandler.getEventCountSinceLastReset() > 0 ) {
						ChangeHandler.resetEventCount();
						synchronized( this.lightweightOnscreenLookingGlasses ) {
							for( OnscreenLookingGlass lg : this.lightweightOnscreenLookingGlasses ) {
								if( isDisplayDesired( lg ) ) {
									//lg.getGLAutoDrawable().display();
									lg.repaint();
									//edu.cmu.cs.dennisc.print.PrintUtilities.println( lg );
									rv = ThreadDeferenceAction.YIELD;
								}
							}
						}
					}
				} finally {
					ChangeHandler.popRenderingMode();
				}
			} finally {
				releaseRenderingLock();
			}
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
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "lightweight count:", this.lightweightOnscreenLookingGlasses.size() );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "heavyweight count:", this.heavyweightOnscreenLookingGlasses.size() );
//			javax.swing.SwingUtilities.invokeLater( new Runnable() {
//				public void run() {
					LookingGlassFactory.this.animator.start();
//				}
//			} );
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
		synchronized( this.lightweightOnscreenLookingGlasses ) {
			this.lightweightOnscreenLookingGlasses.add( lolg );
		}
		return lolg;
	}
	public edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass createHeavyweightOnscreenLookingGlass() {
		HeavyweightOnscreenLookingGlass holg = new HeavyweightOnscreenLookingGlass( this );
		holg.addReleaseListener( this );
		synchronized( this.heavyweightOnscreenLookingGlasses ) {
			this.heavyweightOnscreenLookingGlasses.add( holg );
		}
		return holg;
	}
	public edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass createOffscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlassToShareContextWith ) {
		assert lookingGlassToShareContextWith == null || lookingGlassToShareContextWith instanceof AbstractLookingGlass;
		OffscreenLookingGlass olg = new OffscreenLookingGlass( this, (AbstractLookingGlass)lookingGlassToShareContextWith );
		olg.addReleaseListener( this );
		synchronized( this.offscreenLookingGlasses ) {
			this.offscreenLookingGlasses.add( olg );
		}
		return olg;
	}

	public void addAutomaticDisplayListener( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener ) {
		synchronized( this.automaticDisplayListeners ) {
			this.automaticDisplayListeners.add( automaticDisplayListener );
		}
	}
	public void removeAutomaticDisplayListener( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener ) {
		synchronized( this.automaticDisplayListeners ) {
			this.automaticDisplayListeners.remove( automaticDisplayListener );
		}
	}
	public Iterable< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener > accessAutomaticDisplayListeners() {
		return this.automaticDisplayListeners;
	}

	private void handleDisplayed() {
		synchronized( this.automaticDisplayListeners ) {
			for( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener : this.automaticDisplayListeners ) {
				automaticDisplayListener.automaticDisplayCompleted( this.automaticDisplayEvent );
			}
		}
		synchronized( this.runnables ) {
			while( this.runnables.isEmpty() == false ) {
				Runnable runnable = this.runnables.remove();
				runnable.run();
			}
		}
	}

	public void invokeLater( Runnable runnable ) {
		synchronized( this.runnables ) {
			this.runnables.add( runnable );
		}
	}
	public void invokeAndWait( Runnable runnable ) throws InterruptedException, java.lang.reflect.InvocationTargetException {
		Thread currentThread = Thread.currentThread();
		//assert currentThread != this.animator.getThread(); 
		WaitingRunnable waitingRunnable = new WaitingRunnable( runnable, currentThread );
		synchronized( currentThread ) {
			synchronized( this.runnables ) {
				this.runnables.add( waitingRunnable );
			}
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

	public Iterable< ? extends edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass > accessLightweightOnscreenLookingGlasses() {
		return this.lightweightOnscreenLookingGlasses;
	}
	public Iterable< ? extends edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass > accessHeavyweightOnscreenLookingGlasses() {
		return this.heavyweightOnscreenLookingGlasses;
	}
	public Iterable< ? extends edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass > accessOffscreenLookingGlasses() {
		return this.offscreenLookingGlasses;
	}
}
