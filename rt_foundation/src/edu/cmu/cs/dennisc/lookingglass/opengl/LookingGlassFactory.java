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

/**
 * @author Dennis Cosgrove
 */
class WaitingRunnable implements Runnable {
	private Runnable m_runnable;
	private Thread m_thread;
	private Exception m_exception;
	public WaitingRunnable( Runnable runnable, Thread thread ) {
		assert runnable != null;
		assert thread != null;
		m_runnable = runnable;
		m_thread = thread;
		m_exception = null;
	}
	public Exception getException() {
		return m_exception;
	}
	public void run() {
		try {
			m_runnable.run();
		} catch( Exception e ) {
			m_exception = e;
		} finally {
			synchronized( m_thread ) {
				m_thread.notifyAll();
			}
		}
	}
}

//abstract class Animator implements Runnable {
//	public static final long DEFAULT_SLEEP_MILLIS = 5;
//	private boolean isActive = false;
//	private long tStart;
//	private int frameCount;
//	private long sleepMillis = DEFAULT_SLEEP_MILLIS;
//	public enum ThreadDeferenceAction {
//		DO_NOTHING,
//		SLEEP,
//		YIELD
//	}
//	public void start() {
//		this.isActive = true;
//		this.frameCount = 0;
//		this.tStart = System.currentTimeMillis();
////		javax.swing.SwingUtilities.invokeLater( this );
//		new Thread( this ).start();
//	}
//	public void stop() {
//		this.isActive = false;
//		long tDelta = System.currentTimeMillis() - this.tStart;
//		//edu.cmu.cs.dennisc.print.PrintUtilities.println( this.frameCount, tDelta, this.frameCount/(tDelta*0.001) );
//	}
//	public int getFrameCount() {
//		return this.frameCount;
//	}
//	public long getStartTimeMillis() {
//		return this.tStart;
//	}
//	public long getSleepMillis() {
//		return this.sleepMillis;
//	}
//	public void setSleepMillis( long sleepMillis ) {
//		this.sleepMillis = sleepMillis;
//	}
//	
//	protected abstract ThreadDeferenceAction step();
//	public void run() {
//		while( this.isActive ) {
////		if( this.isActive ) {
////			try {
//				ThreadDeferenceAction threadAction = this.step();
//				if( threadAction == ThreadDeferenceAction.SLEEP ) {
//					edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( this.sleepMillis );
//				} else if( threadAction == ThreadDeferenceAction.YIELD ) {
//					Thread.yield();
//				}
//				this.frameCount ++;
////			} finally {
////				javax.swing.SwingUtilities.invokeLater( this );
////			}
//		}
//	}
//}
//
/**
 * @author Dennis Cosgrove
 */
public class LookingGlassFactory implements edu.cmu.cs.dennisc.lookingglass.LookingGlassFactory, edu.cmu.cs.dennisc.pattern.event.ReleaseListener {
//	private static boolean isDisplayDesired( OnscreenLookingGlass lg ) {
//		java.awt.Component component = lg.getAWTComponent();
//		if( component.isVisible() && component.isValid() && component.getWidth() > 0 && component.getHeight() > 0 ) {
//			if( lg.getCameraCount() > 0 ) {
//				return true;
//			}
//		}
//		return false;
//	}
//	class LookingGlassAnimator extends Animator {
//		private java.util.concurrent.Semaphore m_renderingLock = new java.util.concurrent.Semaphore( 1 );
//		@Override
//		protected ThreadDeferenceAction step() {
//			ThreadDeferenceAction rv = ThreadDeferenceAction.SLEEP;
//			synchronized( m_toBeReleased ) {
//				for( edu.cmu.cs.dennisc.pattern.Releasable releasable : m_toBeReleased ) {
//					if( releasable instanceof OnscreenLookingGlass ) {
//						OnscreenLookingGlass onscreenLookingGlass = (OnscreenLookingGlass)releasable;
//						if( onscreenLookingGlass instanceof HeavyweightOnscreenLookingGlass ) {
//							synchronized( m_heavyweightOnscreenLookingGlasses ) {
//								m_heavyweightOnscreenLookingGlasses.remove( onscreenLookingGlass );
//							}
//						} else if( onscreenLookingGlass instanceof LightweightOnscreenLookingGlass ) {
//							synchronized( m_lightweightOnscreenLookingGlasses ) {
//								m_lightweightOnscreenLookingGlasses.remove( onscreenLookingGlass );
//							}
//						} else {
//							assert false;
//						}
//						//m_animator.remove( onscreenLookingGlass.getGLAutoDrawable() );
//					} else if( releasable instanceof OffscreenLookingGlass ) {
//						synchronized( m_offscreenLookingGlasses ) {
//							m_offscreenLookingGlasses.remove( releasable );
//						}
//					} else {
//						assert false;
//					}
//				}
//				m_toBeReleased.clear();
//			}
//
//			if( ChangeHandler.getEventCountSinceLastReset() > 0 ) {
//				ChangeHandler.resetEventCount();
//				acquireRenderingLock();
//				try {
//					ChangeHandler.pushRenderingMode();
//					try {
//						synchronized( m_heavyweightOnscreenLookingGlasses ) {
//							for( OnscreenLookingGlass lg : m_heavyweightOnscreenLookingGlasses ) {
//								if( isDisplayDesired( lg ) ) {
//									lg.getGLAutoDrawable().display();
//									//lg.getAWTComponent().repaint();
//									//edu.cmu.cs.dennisc.print.PrintUtilities.println( lg );
//									rv = ThreadDeferenceAction.YIELD; 
//								}
//							}
//						}
//						synchronized( m_lightweightOnscreenLookingGlasses ) {
//							for( OnscreenLookingGlass lg : m_lightweightOnscreenLookingGlasses ) {
//								if( isDisplayDesired( lg ) ) {
//									lg.getGLAutoDrawable().display();
//									//lg.getAWTComponent().repaint();
//									//edu.cmu.cs.dennisc.print.PrintUtilities.println( lg );
//									rv = ThreadDeferenceAction.YIELD; 
//								}
//							}
//						}
//					} finally {
//						ChangeHandler.popRenderingMode();
//					}
//				} finally {
//					releaseRenderingLock();
//				}
//			}
//			LookingGlassFactory.this.handleDisplayed();
//			return rv;
//		}
//		public void acquireRenderingLock() {
//			try {
//				m_renderingLock.acquire();
//			} catch( InterruptedException ie ) {
//				throw new RuntimeException( ie );
//			}
//		}
//		public void releaseRenderingLock() {
//			m_renderingLock.release();
//		}
//	}
//	private LookingGlassAnimator m_animator = new LookingGlassAnimator();
	class Animator extends com.sun.opengl.util.Animator {
		private java.util.concurrent.Semaphore m_renderingLock = new java.util.concurrent.Semaphore( 1 );

		public void acquireRenderingLock() {
			try {
				m_renderingLock.acquire();
			} catch( InterruptedException ie ) {
				throw new RuntimeException( ie );
			}
		}
		public void releaseRenderingLock() {
			m_renderingLock.release();
		}

		@Override
		protected void display() {
			acquireRenderingLock();
			try {
				ChangeHandler.pushRenderingMode();
				try {
					super.display();
				} finally {
					ChangeHandler.popRenderingMode();
				}
			} finally {
				releaseRenderingLock();
			}
			LookingGlassFactory.this.handleDisplayed();
		}
	}

	private Animator m_animator = new Animator();

	private java.util.Queue< Runnable > m_runnables = new java.util.LinkedList< Runnable >();

	private java.util.List<edu.cmu.cs.dennisc.pattern.Releasable> m_toBeReleased = new java.util.LinkedList<edu.cmu.cs.dennisc.pattern.Releasable>();
	private java.util.List<LightweightOnscreenLookingGlass> m_lightweightOnscreenLookingGlasses = new java.util.LinkedList<LightweightOnscreenLookingGlass>();
	private java.util.List<HeavyweightOnscreenLookingGlass> m_heavyweightOnscreenLookingGlasses = new java.util.LinkedList<HeavyweightOnscreenLookingGlass>();
	private java.util.List<OffscreenLookingGlass> m_offscreenLookingGlasses = new java.util.LinkedList<OffscreenLookingGlass>();
	
	private static LookingGlassFactory s_singleton;
	
	static {
		try {
			javax.media.opengl.GLDrawableFactory unused = javax.media.opengl.GLDrawableFactory.getFactory();
		} catch( UnsatisfiedLinkError ule ) {
			final String JAVA_LIBRARY_PATH = "java.library.path";
			//String pathOriginal = System.getProperty( JAVA_LIBRARY_PATH );
			StringBuffer sb = new StringBuffer();
			String libraryPath;
			if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
				libraryPath = "\"/Applications/Programming/Alice/3.beta.0000/tools/jogl/lib/macosx-universal\"";
			} else {
				libraryPath = "\"/Program Files/Alice/3.beta.0000/tools/jogl/lib/windows-i586\"";
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

	private LookingGlassFactory() {
		s_singleton = this;
	}
	
	
	public void acquireRenderingLock() {
		m_animator.acquireRenderingLock();
	}
	public void releaseRenderingLock() {
		m_animator.releaseRenderingLock();
	}
	

	//todo: just force start and stop? or rename methods
	private int m_automaticDisplayCount = 0;
	public int getAutomaticDisplayCount() {
		return m_automaticDisplayCount;
	}
	public synchronized void incrementAutomaticDisplayCount() {
		m_automaticDisplayCount++;
		if( m_automaticDisplayCount == 1 ) {
			while( m_animator.isAnimating() ) {
				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "waiting for animator to stop" );
			}
			m_animator.start();
		}
	}
	public synchronized void decrementAutomaticDisplayCount() {
		if( m_automaticDisplayCount == 1 ) {
			m_animator.stop();
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "animator.stop()" );
		}
		m_automaticDisplayCount--;
	}
		
	public void releasing( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent releaseEvent ) {
	}
	public void released( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent releaseEvent ) {
		synchronized( this.m_toBeReleased ) {
			this.m_toBeReleased.add( releaseEvent.getTypedSource() );
		}
	}
	
	public edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass createLightweightOnscreenLookingGlass() {
		LightweightOnscreenLookingGlass lolg = new LightweightOnscreenLookingGlass( this );
		lolg.addReleaseListener( this );
		synchronized( m_lightweightOnscreenLookingGlasses ) {
			m_lightweightOnscreenLookingGlasses.add( lolg );
		}
		m_animator.add( lolg.getGLAutoDrawable() );
		return lolg;
	}
	public edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass createHeavyweightOnscreenLookingGlass() {
		HeavyweightOnscreenLookingGlass holg = new HeavyweightOnscreenLookingGlass( this );
		holg.addReleaseListener( this );
		synchronized( m_heavyweightOnscreenLookingGlasses ) {
			m_heavyweightOnscreenLookingGlasses.add( holg );
		}
		m_animator.add( holg.getGLAutoDrawable() );
		return holg;
	}
	public edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass createOffscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlassToShareContextWith ) {
		assert lookingGlassToShareContextWith instanceof AbstractLookingGlass;
		OffscreenLookingGlass olg = new OffscreenLookingGlass( this, (AbstractLookingGlass)lookingGlassToShareContextWith );
		olg.addReleaseListener( this );
		synchronized( m_offscreenLookingGlasses ) {
			m_offscreenLookingGlasses.add( olg );
		}
		return olg;
	}

	private java.util.List< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener > m_automaticDisplayListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener >();
	public void addAutomaticDisplayListener( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener ) {
		synchronized( m_automaticDisplayListeners ) {
			m_automaticDisplayListeners.add( automaticDisplayListener );
		}
	}
	public void removeAutomaticDisplayListener( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener ) {
		synchronized( m_automaticDisplayListeners ) {
			m_automaticDisplayListeners.remove( automaticDisplayListener );
		}
	}
	public Iterable< edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener > accessAutomaticDisplayListeners() {
		return m_automaticDisplayListeners;
	}

	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent m_automaticDisplayEvent = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent( this ) {
		@Override
		public boolean isReservedForReuse() {
			return true;
		}
	};
	private void handleDisplayed() {
		synchronized( m_automaticDisplayListeners ) {
			for( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener : m_automaticDisplayListeners ) {
				automaticDisplayListener.automaticDisplayCompleted( m_automaticDisplayEvent );
			}
		}
		synchronized( m_runnables ) {
			while( m_runnables.isEmpty() == false ) {
				Runnable runnable = m_runnables.remove();
				runnable.run();
			}
		}
	}

	public void invokeLater( Runnable runnable ) {
		synchronized( m_runnables ) {
			m_runnables.add( runnable );
		}
	}
	public void invokeAndWait( Runnable runnable ) throws InterruptedException, java.lang.reflect.InvocationTargetException {
		Thread currentThread = Thread.currentThread();
		//assert currentThread != m_animator.getThread(); 
		WaitingRunnable waitingRunnable = new WaitingRunnable( runnable, currentThread );
		synchronized( currentThread ) {
			synchronized( m_runnables ) {
				m_runnables.add( waitingRunnable );
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
		return m_lightweightOnscreenLookingGlasses;
	}
	public Iterable< ? extends edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass > accessHeavyweightOnscreenLookingGlasses() {
		return m_heavyweightOnscreenLookingGlasses;
	}
	public Iterable< ? extends edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass > accessOffscreenLookingGlasses() {
		return m_offscreenLookingGlasses;
	}
}
