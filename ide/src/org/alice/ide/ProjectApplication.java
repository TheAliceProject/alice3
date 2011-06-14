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

package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProjectApplication extends org.lgna.croquet.Application {
	public static final org.lgna.croquet.Group HISTORY_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "303e94ca-64ef-4e3a-b95c-038468c68438" ), "HISTORY_GROUP" );
	public static final org.lgna.croquet.Group URI_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "79bf8341-61a4-4395-9469-0448e66d9ac6" ), "URI_GROUP" );

	private static ProjectApplication singleton;
	public static ProjectApplication getSingleton() {
		return ProjectApplication.singleton;
	}
	public ProjectApplication() {
		assert ProjectApplication.singleton == null;
		ProjectApplication.singleton = this;
		this.getProjectHistoryManager().addHistoryListener( new edu.cmu.cs.dennisc.history.event.HistoryListener() {
			public void operationPushing( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
			}
			public void operationPushed( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
			}
			public void insertionIndexChanging( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
			}
			public void insertionIndexChanged( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
				updateTitle();
			}
			public void clearing( edu.cmu.cs.dennisc.history.event.HistoryClearEvent e ) {
			}
			public void cleared( edu.cmu.cs.dennisc.history.event.HistoryClearEvent e ) {
			}
		} );
	}

	protected abstract org.alice.ide.openprojectpane.TabContentPanel createTemplatesTabContentPane();

	public abstract String getApplicationName();
	protected abstract String getVersionText();
	protected abstract String getVersionAdornment();

	private void showUnableToOpenProjectMessageDialog( java.io.File file, boolean isValidZip ) {
		StringBuffer sb = new StringBuffer();
		sb.append( "Unable to open project from file " );
		sb.append( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file ) );
		sb.append( ".\n\n" );
		sb.append( this.getApplicationName() );
		sb.append( " is able to open projects from files saved by " );
		sb.append( this.getApplicationName() );
		sb.append( ".\n\nLook for files with an " );
		sb.append( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION );
		sb.append( " extension." );
		this.showMessageDialog( sb.toString(), "Cannot read file", org.lgna.croquet.MessageType.ERROR );
	}

	private java.net.URI uri = null;
	public java.net.URI getUri() {
		return this.uri;
	}
	@Deprecated
	public java.io.File getFile() {
		if( this.uri != null ) {
			return new java.io.File( this.uri );
		} else {
			return null;
		}
	}
	public void setUri( java.net.URI uri ) {
		java.io.File file = new java.io.File( uri );
		if( file.exists() ) {
			String lcFilename = file.getName().toLowerCase();
			if( lcFilename.endsWith( ".a2w" ) ) {
				this.showMessageDialog( "Alice3 does not load Alice2 worlds", "Cannot read file", org.lgna.croquet.MessageType.ERROR );
			} else if( lcFilename.endsWith( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.TYPE_EXTENSION.toLowerCase() ) ) {
				this.showMessageDialog( file.getAbsolutePath() + " appears to be a class file and not a project file.\n\nLook for files with an " + edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION + " extension.", "Incorrect File Type", org.lgna.croquet.MessageType.ERROR );
			} else {
				boolean isWorthyOfException = lcFilename.endsWith( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION.toLowerCase() );
				java.util.zip.ZipFile zipFile;
				try {
					zipFile = new java.util.zip.ZipFile( file );
				} catch( java.io.IOException ioe ) {
					if( isWorthyOfException ) {
						throw new RuntimeException( file.getAbsolutePath(), ioe );
					} else {
						this.showUnableToOpenProjectMessageDialog( file, false );
						zipFile = null;
					}
				}
				if( zipFile != null ) {
					edu.cmu.cs.dennisc.alice.Project project;
					try {
						project = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.readProject( zipFile );
					} catch( java.io.IOException ioe ) {
						if( isWorthyOfException ) {
							throw new RuntimeException( file.getAbsolutePath(), ioe );
						} else {
							this.showUnableToOpenProjectMessageDialog( file, true );
							project = null;
						}
					}
					if( project != null ) {
						this.setProject( project );
						this.uri = uri;
						try {
//							long t0 = System.currentTimeMillis();
							if( file != null && file.canWrite() ) {
								int desiredRecentProjectCount = org.alice.ide.preferences.GeneralPreferences.getSingleton().desiredRecentProjectCount.getValue();
								org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.add( file, desiredRecentProjectCount );
							}
//							long tDelta = System.currentTimeMillis() - t0;
//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "time to store preference (msec):", tDelta );
						} catch( Throwable throwable ) {
							throwable.printStackTrace();
						}
						this.updateTitle();
					} else {
						//actionContext.cancel();
					}
				} else {
					//actionContext.cancel();
				}
			}
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append( "Cannot read project from file:\n\t" );
			sb.append( file.getAbsolutePath() );
			sb.append( "\nIt does not exist." );
			this.showMessageDialog( sb.toString(), "Cannot read file", org.lgna.croquet.MessageType.ERROR );
		}
	}
	
	private edu.cmu.cs.dennisc.alice.Project project = null;
	
	public final edu.cmu.cs.dennisc.history.HistoryManager getProjectHistoryManager() {
		return edu.cmu.cs.dennisc.history.HistoryManager.getInstance( edu.cmu.cs.dennisc.alice.Project.GROUP );
	}
	
	private int projectHistoryInsertionIndexOfCurrentFile = 0;

	private boolean isProjectChanged() {
		edu.cmu.cs.dennisc.history.HistoryManager projectHistoryManager = this.getProjectHistoryManager();
		return this.projectHistoryInsertionIndexOfCurrentFile != projectHistoryManager.getInsertionIndex();
	}
	public boolean isProjectUpToDateWithFile() {
		if( this.uri != null ) {
			return isProjectChanged() == false;
		} else {
			return true;
		}
	}
	protected StringBuffer updateTitlePrefix( StringBuffer rv ) {
		rv.append( this.getApplicationName() );
		rv.append( " " );
//		rv.append( this.getVersionText() );
//		rv.append( " " );
		rv.append( this.getVersionAdornment() ); 
		rv.append( " " );
		return rv;
	}
	protected StringBuffer updateTitle( StringBuffer rv ) {
		this.updateTitlePrefix( rv );
		if( this.uri != null ) {
			java.io.File file = new java.io.File( this.uri );
			rv.append( file.getAbsolutePath() );
			rv.append( " " );
		}
		if( this.isProjectChanged() ) {
			rv.append( "*" );
		}
		return rv;
	}

	protected final void updateTitle() {
		StringBuffer sb = new StringBuffer();
		this.updateTitle( sb );
		this.getFrame().setTitle( sb.toString() );
	}
	
	private void updateHistoryLengthAtLastFileOperation() {
		edu.cmu.cs.dennisc.history.HistoryManager projectHistoryManager = this.getProjectHistoryManager();
		this.projectHistoryInsertionIndexOfCurrentFile = projectHistoryManager.getInsertionIndex();
		this.updateTitle();
	}
	protected void preserveProjectProperties() {
	}
	protected void restoreProjectProperties() {
	}

	public static interface ProjectObserver {
		public void projectOpening( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject );
		public void projectOpened( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject );
	}
	
	private java.util.List< ProjectObserver > projectObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	
	public void addProjectObserver( ProjectObserver projectObserver ) {
		this.projectObservers.add( projectObserver );
	}
	public void removeProjectObserver( ProjectObserver projectObserver ) {
		this.projectObservers.remove( projectObserver );
	}
	
	
	public edu.cmu.cs.dennisc.alice.Project getProject() {
		return this.project;
	}
	public void setProject( edu.cmu.cs.dennisc.alice.Project project ) {
		edu.cmu.cs.dennisc.alice.Project previousProject = this.project;
		for( ProjectObserver projectObserver : this.projectObservers ) {
			projectObserver.projectOpening( previousProject, project );
		}
		this.project = project;
		for( ProjectObserver projectObserver : this.projectObservers ) {
			projectObserver.projectOpened( previousProject, project );
		}
	}
	
	public void loadProjectFrom( java.net.URI uri ) {
		edu.cmu.cs.dennisc.history.HistoryManager projectHistoryManager = this.getProjectHistoryManager();
		projectHistoryManager.performClear();
		this.updateHistoryLengthAtLastFileOperation();
		this.restoreProjectProperties();
		setUri( uri );
	}
	public final void loadProjectFrom( java.io.File file ) {
		loadProjectFrom( file.toURI() );
	}
	public final void loadProjectFrom( String path ) {
		loadProjectFrom( new java.io.File( path ) );
	}
	public void createProjectFromBootstrap() {
		throw new RuntimeException( "todo" );
	}
	protected abstract java.awt.image.BufferedImage createThumbnail() throws Throwable;

	public void saveProjectTo( java.io.File file ) throws java.io.IOException {
		edu.cmu.cs.dennisc.alice.Project project = getProject();
		this.ensureProjectCodeUpToDate();
		this.preserveProjectProperties();

		edu.cmu.cs.dennisc.zip.DataSource[] dataSources;
		try {
			final java.awt.image.BufferedImage thumbnailImage = createThumbnail();
			if( thumbnailImage != null ) {
				if( thumbnailImage.getWidth() > 0 && thumbnailImage.getHeight() > 0 ) {
					//pass
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new NullPointerException();
			}
			dataSources = new edu.cmu.cs.dennisc.zip.DataSource[] { new edu.cmu.cs.dennisc.zip.DataSource() {
				public String getName() {
					return "thumbnail.png";
				}
				public void write( java.io.OutputStream os ) throws java.io.IOException {
					edu.cmu.cs.dennisc.image.ImageUtilities.write( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, os, thumbnailImage );
				}
			} };
		} catch( Throwable t ) {
			dataSources = new edu.cmu.cs.dennisc.zip.DataSource[] {};
		}
		edu.cmu.cs.dennisc.alice.project.ProjectUtilities.writeProject( file, project, dataSources );
		this.uri = file.toURI();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "project saved to: ", file.getAbsolutePath() );
		this.updateHistoryLengthAtLastFileOperation();
	}
	public void saveProjectTo( String path ) throws java.io.IOException {
		saveProjectTo( new java.io.File( path ) );
	}
	

	//todo: remove
	private String getSubPath() {
		String rv = getApplicationName();
		if( "Alice".equals( rv ) ) {
			rv = "Alice3";
		}
		return rv.replaceAll( " ", "" );
	}
	public java.io.File getMyProjectsDirectory() {
		return edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getMyProjectsDirectory( this.getSubPath() );
	}

	public abstract void ensureProjectCodeUpToDate();
	public abstract java.io.File getApplicationRootDirectory();
}
