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
public abstract class ProjectApplication extends org.lgna.croquet.PerspectiveApplication {

	public static final org.lgna.croquet.Group PROJECT_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "a89d2513-6d9a-4378-a08b-4d773618244d" ), "PROJECT_GROUP" );
	public static final org.lgna.croquet.Group HISTORY_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "303e94ca-64ef-4e3a-b95c-038468c68438" ), "HISTORY_GROUP" );
	public static final org.lgna.croquet.Group URI_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "79bf8341-61a4-4395-9469-0448e66d9ac6" ), "URI_GROUP" );

	public static ProjectApplication getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( org.lgna.croquet.Application.getActiveInstance(), ProjectApplication.class );
	}

	private org.lgna.croquet.undo.event.HistoryListener projectHistoryListener;

	public ProjectApplication() {
		this.projectHistoryListener = new org.lgna.croquet.undo.event.HistoryListener() {
			public void operationPushing( org.lgna.croquet.undo.event.HistoryPushEvent e ) {
			}

			public void operationPushed( org.lgna.croquet.undo.event.HistoryPushEvent e ) {
			}

			public void insertionIndexChanging( org.lgna.croquet.undo.event.HistoryInsertionIndexEvent e ) {
			}

			public void insertionIndexChanged( org.lgna.croquet.undo.event.HistoryInsertionIndexEvent e ) {
				ProjectApplication.this.handleInsertionIndexChanged( e );
			}

			public void clearing( org.lgna.croquet.undo.event.HistoryClearEvent e ) {
			}

			public void cleared( org.lgna.croquet.undo.event.HistoryClearEvent e ) {
			}
		};
		this.updateTitle();
	}

	private void updateUndoRedoEnabled() {
		org.lgna.croquet.undo.UndoHistory historyManager = this.getProjectHistory( org.alice.ide.IDE.PROJECT_GROUP );
		int index = historyManager.getInsertionIndex();
		int size = historyManager.getStack().size();
		org.alice.ide.croquet.models.history.UndoOperation.getInstance().setEnabled( index > 0 );
		org.alice.ide.croquet.models.history.RedoOperation.getInstance().setEnabled( index < size );
	}

	protected void handleInsertionIndexChanged( org.lgna.croquet.undo.event.HistoryInsertionIndexEvent e ) {
		this.updateTitle();
		org.lgna.croquet.undo.UndoHistory source = e.getTypedSource();
		if( source.getGroup() == PROJECT_GROUP ) {
			this.updateUndoRedoEnabled();
		}
	}

	public static final String getApplicationName() {
		return "Alice";
	}

	public static final String getVersionText() {
		return org.lgna.project.Version.getCurrentVersionText();
	}

	public static final String getVersionAdornment() {
		return " 3.1";
	}

	public static final String getApplicationSubPath() {
		String rv = getApplicationName();
		if( "Alice".equals( rv ) ) {
			rv = "Alice3";
		}
		return rv.replaceAll( " ", "" );
	}

	private void showUnableToOpenFileDialog( java.io.File file, String message ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Unable to open file " );
		sb.append( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file ) );
		sb.append( ".\n\n" );
		sb.append( message );
		this.showMessageDialog( sb.toString(), "Cannot read file", org.lgna.croquet.MessageType.ERROR );
	}

	public void handleVersionNotSupported( java.io.File file, org.lgna.project.VersionNotSupportedException vnse ) {
		StringBuilder sb = new StringBuilder();
		sb.append( getApplicationName() );
		sb.append( " is not backwards compatible with:" );
		sb.append( "\n    File Version: " );
		sb.append( vnse.getVersion() );
		sb.append( "\n    (Minimum Supported Version: " );
		sb.append( vnse.getMinimumSupportedVersion() );
		sb.append( ")" );
		this.showUnableToOpenFileDialog( file, sb.toString() );
	}

	private void showUnableToOpenProjectMessageDialog( java.io.File file, boolean isValidZip ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Look for files with an " );
		sb.append( org.lgna.project.io.IoUtilities.PROJECT_EXTENSION );
		sb.append( " extension." );
		this.showUnableToOpenFileDialog( file, sb.toString() );
	}

	private java.net.URI uri = null;

	public java.net.URI getUri() {
		return this.uri;
	}

	public void setUri( java.net.URI uri ) {
		org.lgna.project.Project project = null;
		java.io.File file = null;
		if( uri != null ) {
			file = edu.cmu.cs.dennisc.java.net.UriUtilities.getFile( uri );
			if( file != null ) {
				if( file.exists() ) {
					String lcFilename = file.getName().toLowerCase();
					if( lcFilename.endsWith( ".a2w" ) ) {
						this.showMessageDialog( "Alice3 does not load Alice2 worlds", "Cannot read file", org.lgna.croquet.MessageType.ERROR );
					} else if( lcFilename.endsWith( org.lgna.project.io.IoUtilities.TYPE_EXTENSION.toLowerCase() ) ) {
						this.showMessageDialog( file.getAbsolutePath() + " appears to be a class file and not a project file.\n\nLook for files with an " + org.lgna.project.io.IoUtilities.PROJECT_EXTENSION + " extension.", "Incorrect File Type", org.lgna.croquet.MessageType.ERROR );
					} else {
						boolean isWorthyOfException = lcFilename.endsWith( org.lgna.project.io.IoUtilities.PROJECT_EXTENSION.toLowerCase() );
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
							try {
								project = org.lgna.project.io.IoUtilities.readProject( zipFile );
							} catch( org.lgna.project.VersionNotSupportedException vnse ) {
								this.handleVersionNotSupported( file, vnse );
							} catch( java.io.IOException ioe ) {
								if( isWorthyOfException ) {
									throw new RuntimeException( file.getAbsolutePath(), ioe );
								} else {
									this.showUnableToOpenProjectMessageDialog( file, true );
								}
							}
						} else {
							//actionContext.cancel();
						}
					}
				} else {
					this.showUnableToOpenFileDialog( file, "It does not exist." );
				}
			} else {
				org.alice.stageide.openprojectpane.models.TemplateUriSelectionState.Template template = org.alice.stageide.openprojectpane.models.TemplateUriSelectionState.getSurfaceAppearance( uri );
				org.lgna.project.ast.NamedUserType programType;
				if( template.isRoom() ) {
					programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType( template.getFloorAppearance(), template.getWallAppearance(), template.getCeilingAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(), template.getBelowLightColor() );
				}
				else {
					programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType( template.getSurfaceAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(), template.getBelowLightColor() );
				}
				project = new org.lgna.project.Project( programType );
			}
		}
		if( project != null ) {
			// Remove the old project history listener, so the old project can be cleaned up
			if( ( this.getProject() != null ) && ( this.getProjectHistory() != null ) ) {
				this.getProjectHistory().removeHistoryListener( this.projectHistoryListener );
			}
			this.setProject( project );
			this.uri = uri;
			this.getProjectHistory().addHistoryListener( this.projectHistoryListener );
			try {
				if( ( file != null ) && file.canWrite() ) {
					//org.alice.ide.croquet.models.openproject.RecentProjectsUriSelectionState.getInstance().handleOpen( file );
					org.alice.ide.recentprojects.RecentProjectsListData.getInstance().handleOpen( file );
				}
			} catch( Throwable throwable ) {
				throwable.printStackTrace();
			}
			this.updateTitle();
		} else {
			//actionContext.cancel();
		}
	}

	@Deprecated
	private final org.lgna.croquet.undo.UndoHistory getProjectHistory() {
		return this.getProjectHistory( IDE.PROJECT_GROUP );
	}

	@Deprecated
	private final org.lgna.croquet.undo.UndoHistory getProjectHistory( org.lgna.croquet.Group group ) {
		if( this.getDocument() == null ) {
			return null;
		} else {
			return this.getDocument().getUndoHistory( group );
		}
	}

	private int projectHistoryInsertionIndexOfCurrentFile = 0;

	private boolean isProjectChanged() {
		if( this.getProjectHistory() == null ) {
			return false;
		} else {
			return this.projectHistoryInsertionIndexOfCurrentFile != this.getProjectHistory().getInsertionIndex();
		}
	}

	public boolean isProjectUpToDateWithFile() {
		return isProjectChanged() == false;
	}

	protected StringBuffer updateTitlePrefix( StringBuffer rv ) {
		rv.append( getApplicationName() );
		rv.append( " " );
		rv.append( getVersionAdornment() );
		rv.append( " " );
		return rv;
	}

	protected StringBuffer updateTitle( StringBuffer rv ) {
		this.updateTitlePrefix( rv );
		if( this.uri != null ) {
			String scheme = this.uri.getScheme();
			if( "file".equalsIgnoreCase( scheme ) ) {
				rv.append( new java.io.File( this.uri.getPath() ) );
			}
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
		this.projectHistoryInsertionIndexOfCurrentFile = this.getProjectHistory().getInsertionIndex();
		this.updateTitle();
	}

	@Override
	public ProjectDocument getDocument() {
		return org.alice.ide.project.ProjectDocumentState.getInstance().getValue();
	}

	private void setDocument( ProjectDocument document ) {
		org.alice.ide.project.ProjectDocumentState.getInstance().setValue( document );
	}

	public org.lgna.project.Project getProject() {
		ProjectDocument document = this.getDocument();
		return document != null ? document.getProject() : null;
	}

	public void setProject( org.lgna.project.Project project ) {
		this.setDocument( new ProjectDocument( project ) );
	}

	public org.lgna.croquet.history.TransactionHistory getProjectTransactionHistory() {
		return this.getDocument().getRootTransactionHistory();
	}

	public void loadProjectFrom( java.net.URI uri ) {
		setUri( uri );
		this.updateHistoryLengthAtLastFileOperation();
		this.updateUndoRedoEnabled();
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
		org.lgna.project.Project project = this.getUpToDateProject();
		edu.cmu.cs.dennisc.java.util.zip.DataSource[] dataSources;
		try {
			final java.awt.image.BufferedImage thumbnailImage = createThumbnail();
			if( thumbnailImage != null ) {
				if( ( thumbnailImage.getWidth() > 0 ) && ( thumbnailImage.getHeight() > 0 ) ) {
					//pass
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new NullPointerException();
			}
			dataSources = new edu.cmu.cs.dennisc.java.util.zip.DataSource[] { new edu.cmu.cs.dennisc.java.util.zip.DataSource() {
				public String getName() {
					return "thumbnail.png";
				}

				public void write( java.io.OutputStream os ) throws java.io.IOException {
					edu.cmu.cs.dennisc.image.ImageUtilities.write( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, os, thumbnailImage );
				}
			} };
		} catch( Throwable t ) {
			dataSources = new edu.cmu.cs.dennisc.java.util.zip.DataSource[] {};
		}
		org.lgna.project.io.IoUtilities.writeProject( file, project, dataSources );
		this.uri = file.toURI();
		this.updateHistoryLengthAtLastFileOperation();
	}

	public void saveProjectTo( String path ) throws java.io.IOException {
		saveProjectTo( new java.io.File( path ) );
	}

	public java.io.File getMyProjectsDirectory() {
		return org.alice.ide.croquet.models.ui.preferences.UserProjectsDirectoryState.getInstance().getDirectoryEnsuringExistance();
	}

	public final org.lgna.project.Project getUpToDateProject() {
		this.ensureProjectCodeUpToDate();
		return this.getProject();
	}

	public abstract void ensureProjectCodeUpToDate();
}
