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
package org.alice.ide.projecturi;

/**
 * @author Dennis Cosgrove
 */
public class UriProjectPair {
	//todo: make more thread safe and more sophisticated
	public static interface GetProjectObserver {
		public void workStarted();

		public void workEnded();

		public void done( org.lgna.project.Project project );
	}

	private static void invokeOnEventDispatchThread( final GetProjectObserver observer, final org.lgna.project.Project project ) {
		if( java.awt.EventQueue.isDispatchThread() ) {
			observer.done( project );
		} else {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					observer.done( project );
				}
			} );
		}
	}

	private static class Worker {
		private final java.net.URI uri;
		private final java.util.List<GetProjectObserver> observers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

		private final java.util.concurrent.FutureTask<org.lgna.project.Project> futureTask;
		private boolean isStarted;

		private final java.util.concurrent.ExecutorService executorService = java.util.concurrent.Executors.newSingleThreadExecutor();

		public Worker( java.net.URI uri ) {
			this.uri = uri;

			this.futureTask = new java.util.concurrent.FutureTask<org.lgna.project.Project>( new java.util.concurrent.Callable<org.lgna.project.Project>() {
				public org.lgna.project.Project call() throws java.lang.Exception {
					return readProject();
				}
			} ) {
				@Override
				protected void done() {
					super.done();
					try {
						invokeObserversOnEventDispatchThread( this.get() );
					} catch( InterruptedException ie ) {
						throw new Error( "done", ie );
					} catch( java.util.concurrent.ExecutionException ee ) {
						throw new Error( "done", ee );
					}
				}
			};
		}

		public java.net.URI getUri() {
			return this.uri;
		}

		public void addObserver( GetProjectObserver observer ) {
			this.observers.add( observer );
		}

		private org.lgna.project.Project readProject() throws Exception {
			this.isStarted = true;
			GetProjectObserver observer;
			synchronized( this.observers ) {
				if( this.observers.size() > 0 ) {
					observer = this.observers.get( 0 );
				} else {
					observer = null;
				}
			}
			if( observer != null ) {
				observer.workStarted();
			}
			try {
				java.io.InputStream is = new java.io.FileInputStream( new java.io.File( uri ) );
				return org.lgna.project.io.IoUtilities.readProject( is );
			} finally {
				if( observer != null ) {
					observer.workEnded();
				}
			}
		}

		public void execute() {
			this.executorService.execute( this.futureTask );
		}

		public boolean isDone() {
			return this.futureTask.isDone();
		}

		public boolean isStarted() {
			return this.isStarted;
		}

		public org.lgna.project.Project getProject() {
			try {
				return this.futureTask.get();
			} catch( InterruptedException ie ) {
				throw new RuntimeException( "should only be called when isDone", ie );
			} catch( java.util.concurrent.ExecutionException ee ) {
				throw new RuntimeException( "should only be called when isDone", ee );
			}
		}

		private void invokeObserversOnEventDispatchThread( org.lgna.project.Project project ) {
			for( GetProjectObserver observer : this.observers ) {
				invokeOnEventDispatchThread( observer, project );
			}
		}
	};

	private final Worker worker;

	public UriProjectPair( java.net.URI uri ) {
		this.worker = new Worker( uri );
	}

	public java.net.URI getUri() {
		return this.worker.getUri();
	}

	public synchronized void getProject( final GetProjectObserver observer ) throws Exception {
		if( this.worker.isDone() ) {
			org.lgna.project.Project project = worker.getProject();
			invokeOnEventDispatchThread( observer, project );
		} else {
			this.worker.addObserver( observer );
			//note: this check may not be necessary
			if( this.worker.isStarted() ) {
				//pass
			} else {
				worker.execute();
			}
		}
	}

	public static void main( String[] args ) throws Exception {
		java.io.File file = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "Alice3/MyProjects/a.a3p" );
		UriProjectPair uriProjectPair = new UriProjectPair( file.toURI() );

		for( int i = 0; i < 32; i++ ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( i );
			uriProjectPair.getProject( new GetProjectObserver() {
				public void workStarted() {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "workStarted" );
				}

				public void workEnded() {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "workEnded" );
				}

				public void done( org.lgna.project.Project project ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( project );
				}
			} );
			Thread.sleep( 100 );
		}
	}
}
