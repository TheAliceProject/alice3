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

		public void completed( org.lgna.project.Project project );
	}

	private static void invokeOnEventDispatchThread( final GetProjectObserver observer, final org.lgna.project.Project project ) {
		if( java.awt.EventQueue.isDispatchThread() ) {
			observer.completed( project );
		} else {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					observer.completed( project );
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
				org.lgna.project.Project project;
				if( uri != null ) {
					java.io.File file = edu.cmu.cs.dennisc.java.net.UriUtilities.getFile( uri );
					if( file != null ) {
						if( file.exists() ) {
							String lcFilename = file.getName().toLowerCase();
							if( lcFilename.endsWith( ".a2w" ) ) {
								org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "Alice3 does not load Alice2 worlds", "Cannot read file", org.lgna.croquet.MessageType.ERROR );
							} else if( lcFilename.endsWith( org.lgna.project.io.IoUtilities.TYPE_EXTENSION.toLowerCase() ) ) {
								org.lgna.croquet.Application.getActiveInstance().showMessageDialog( file.getAbsolutePath() + " appears to be a class file and not a project file.\n\nLook for files with an " + org.lgna.project.io.IoUtilities.PROJECT_EXTENSION + " extension.", "Incorrect File Type", org.lgna.croquet.MessageType.ERROR );
							} else {
								boolean isWorthyOfException = lcFilename.endsWith( org.lgna.project.io.IoUtilities.PROJECT_EXTENSION.toLowerCase() );
								java.util.zip.ZipFile zipFile;
								try {
									zipFile = new java.util.zip.ZipFile( file );
								} catch( java.io.IOException ioe ) {
									if( isWorthyOfException ) {
										throw new RuntimeException( file.getAbsolutePath(), ioe );
									} else {
										org.alice.ide.ProjectApplication.getActiveInstance().showUnableToOpenProjectMessageDialog( file, false );
										zipFile = null;
									}
								}
								if( zipFile != null ) {
									try {
										project = org.lgna.project.io.IoUtilities.readProject( zipFile );
									} catch( org.lgna.project.VersionNotSupportedException vnse ) {
										org.alice.ide.ProjectApplication.getActiveInstance().handleVersionNotSupported( file, vnse );
									} catch( java.io.IOException ioe ) {
										if( isWorthyOfException ) {
											throw new RuntimeException( file.getAbsolutePath(), ioe );
										} else {
											org.alice.ide.ProjectApplication.getActiveInstance().showUnableToOpenProjectMessageDialog( file, true );
										}
									}
								} else {
									//actionContext.cancel();
								}
							}
						} else {
							org.alice.ide.ProjectApplication.getActiveInstance().showUnableToOpenFileDialog( file, "It does not exist." );
						}
						java.io.InputStream is = new java.io.FileInputStream( new java.io.File( this.uri ) );
						project = org.lgna.project.io.IoUtilities.readProject( is );
					} else if( org.alice.stageide.openprojectpane.models.TemplateUriSelectionState.Template.isValidUri( this.uri ) ) {
						org.alice.stageide.openprojectpane.models.TemplateUriSelectionState.Template template = org.alice.stageide.openprojectpane.models.TemplateUriSelectionState.Template.getSurfaceAppearance( this.uri );
						org.lgna.project.ast.NamedUserType programType;
						if( template.isRoom() ) {
							programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType( template.getFloorAppearance(), template.getWallAppearance(), template.getCeilingAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(), template.getBelowLightColor() );
						} else {
							programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType( template.getSurfaceAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(), template.getBelowLightColor() );
						}
						project = new org.lgna.project.Project( programType );
					} else {
						project = null;
					}
				} else {
					project = null;
				}
				return project;
			} finally {
				if( observer != null ) {
					observer.workEnded();
				}
			}
		}

		public void executeIfNecessary() {
			//note: this check may not be necessary
			if( this.isStarted() ) {
				//pass
			} else {
				this.executorService.execute( this.futureTask );
			}
		}

		public boolean isDone() {
			return this.futureTask.isDone();
		}

		public boolean isStarted() {
			return this.isStarted;
		}

		public org.lgna.project.Project getProject() throws InterruptedException, java.util.concurrent.ExecutionException {
			return this.futureTask.get();
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

	public synchronized void getProjectOnEventDispatchThread( final GetProjectObserver observer ) throws Exception {
		if( this.worker.isDone() ) {
			org.lgna.project.Project project = worker.getProject();
			invokeOnEventDispatchThread( observer, project );
		} else {
			this.worker.addObserver( observer );
			this.worker.executeIfNecessary();
		}
	}

	public org.lgna.project.Project getProjectWaitingIfNecessary() throws InterruptedException, java.util.concurrent.ExecutionException {
		this.worker.executeIfNecessary();
		return this.worker.getProject();
	}

	public static void main( String[] args ) throws Exception {
		java.io.File file = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "Alice3/MyProjects/a.a3p" );
		UriProjectPair uriProjectPair = new UriProjectPair( file.toURI() );

		for( int i = 0; i < 32; i++ ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( i );
			final boolean IS_OBSERVER_DESIRED = true;
			if( IS_OBSERVER_DESIRED ) {
				uriProjectPair.getProjectOnEventDispatchThread( new GetProjectObserver() {
					public void workStarted() {
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "workStarted" );
					}

					public void workEnded() {
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "workEnded" );
					}

					public void completed( org.lgna.project.Project project ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( project );
					}
				} );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( uriProjectPair.getProjectWaitingIfNecessary() );
			}
			Thread.sleep( 100 );
		}
	}
}
