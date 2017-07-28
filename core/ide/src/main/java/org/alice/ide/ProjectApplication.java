/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProjectApplication extends org.lgna.croquet.PerspectiveApplication<ProjectDocumentFrame> {
	public static final org.lgna.croquet.Group HISTORY_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "303e94ca-64ef-4e3a-b95c-038468c68438" ), "HISTORY_GROUP" );
	public static final org.lgna.croquet.Group URI_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "79bf8341-61a4-4395-9469-0448e66d9ac6" ), "URI_GROUP" );

	public static ProjectApplication getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( org.lgna.croquet.PerspectiveApplication.getActiveInstance(), ProjectApplication.class );
	}

	private org.lgna.croquet.undo.event.HistoryListener projectHistoryListener;

	public ProjectApplication( IdeConfiguration ideConfiguration, ApiConfigurationManager apiConfigurationManager ) {
		this.projectFileUtilities = new ProjectFileUtilities(this);
		this.projectDocumentFrame = new ProjectDocumentFrame( ideConfiguration, apiConfigurationManager );
		this.projectHistoryListener = new org.lgna.croquet.undo.event.HistoryListener() {
			@Override
			public void operationPushing( org.lgna.croquet.undo.event.HistoryPushEvent e ) {
			}

			@Override
			public void operationPushed( org.lgna.croquet.undo.event.HistoryPushEvent e ) {
			}

			@Override
			public void insertionIndexChanging( org.lgna.croquet.undo.event.HistoryInsertionIndexEvent e ) {
			}

			@Override
			public void insertionIndexChanged( org.lgna.croquet.undo.event.HistoryInsertionIndexEvent e ) {
				ProjectApplication.this.handleInsertionIndexChanged( e );
			}

			@Override
			public void clearing( org.lgna.croquet.undo.event.HistoryClearEvent e ) {
			}

			@Override
			public void cleared( org.lgna.croquet.undo.event.HistoryClearEvent e ) {
			}
		};
		this.updateTitle();
	}

	@Override
	public org.alice.ide.ProjectDocumentFrame getDocumentFrame() {
		return this.projectDocumentFrame;
	}

	private void updateUndoRedoEnabled() {
		org.lgna.croquet.undo.UndoHistory historyManager = this.getProjectHistory( org.lgna.croquet.Application.PROJECT_GROUP );
		boolean isUndoEnabled;
		boolean isRedoEnabled;
		if( historyManager != null ) {
			int index = historyManager.getInsertionIndex();
			int size = historyManager.getStack().size();
			isUndoEnabled = index > 0;
			isRedoEnabled = index < size;
		} else {
			isUndoEnabled = false;
			isRedoEnabled = false;
		}

		ProjectDocumentFrame documentFrame = this.getDocumentFrame();
		documentFrame.getUndoOperation().setEnabled( isUndoEnabled );
		documentFrame.getRedoOperation().setEnabled( isRedoEnabled );
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
		return org.lgna.project.ProjectVersion.getCurrentVersionText();
	}

	public static final String getVersionAdornment() {
		return " 3.3";
	}

	public static final String getApplicationSubPath() {
		String rv = getApplicationName();
		if( "Alice".equals( rv ) ) {
			rv = "Alice3";
		}
		return rv.replaceAll( " ", "" );
	}

	public void showUnableToOpenFileDialog( java.io.File file, String message ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Unable to open file " );
		sb.append( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file ) );
		sb.append( ".\n\n" );
		sb.append( message );
		new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( sb.toString() )
				.title( "Cannot read file" )
				.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.ERROR )
				.buildAndShow();
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

	public void showUnableToOpenProjectMessageDialog( java.io.File file, boolean isValidZip ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Look for files with an " );
		sb.append( org.lgna.project.io.IoUtilities.PROJECT_EXTENSION );
		sb.append( " extension." );
		this.showUnableToOpenFileDialog( file, sb.toString() );
	}

	private org.alice.ide.uricontent.UriProjectLoader uriProjectLoader;

	public org.alice.ide.uricontent.UriProjectLoader getUriProjectLoader() {
		return this.uriProjectLoader;
	}

	public final java.net.URI getUri() {
		return this.uriProjectLoader != null ? this.uriProjectLoader.getUri() : null;
	}

	private void setUriProjectPair( org.alice.ide.uricontent.UriProjectLoader uriProjectLoader ) {
		this.uriProjectLoader = null;
		org.lgna.project.Project project;
		if( uriProjectLoader != null ) {
			try {
				project = uriProjectLoader.getContentWaitingIfNecessary( org.alice.ide.uricontent.UriContentLoader.MutationPlan.WILL_MUTATE );
			} catch( InterruptedException ie ) {
				throw new RuntimeException( ie );
			} catch( java.util.concurrent.ExecutionException ee ) {
				throw new RuntimeException( ee );
			}
		} else {
			project = null;
		}
		if( project != null ) {
			// Remove the old project history listener, so the old project can be cleaned up
			if( ( this.getProject() != null ) && ( this.getProjectHistory() != null ) ) {
				this.getProjectHistory().removeHistoryListener( this.projectHistoryListener );
			}
			this.setProject( project );
			this.uriProjectLoader = uriProjectLoader;
			this.getProjectHistory().addHistoryListener( this.projectHistoryListener );
			java.net.URI uri = this.uriProjectLoader.getUri();
			java.io.File file = edu.cmu.cs.dennisc.java.net.UriUtilities.getFile( uri );
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
		return this.getProjectHistory( PROJECT_GROUP );
	}

	@Deprecated
	private final org.lgna.croquet.undo.UndoHistory getProjectHistory( org.lgna.croquet.Group group ) {
		if( this.getDocument() == null ) {
			return null;
		} else {
			return this.getDocument().getUndoHistory( group );
		}
	}

	//todo: investigate
	private static final int PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL = 0;

	private int projectHistoryIndexFile = 0;
	private int projectHistoryIndexSceneSetUp = 0;

	public boolean isProjectUpToDateWithFile() {
		org.lgna.croquet.undo.UndoHistory history = this.getProjectHistory();
		if( history == null ) {
			return true;
		} else {
			return this.projectHistoryIndexFile == history.getInsertionIndex();
		}
	}

	protected boolean isProjectUpToDateWithSceneSetUp() {
		org.lgna.croquet.undo.UndoHistory history = this.getProjectHistory();
		if( history == null ) {
			return true;
		} else {
			return this.projectHistoryIndexSceneSetUp == history.getInsertionIndex();
		}
	}

	private void updateHistoryIndexFileSync() {
		org.lgna.croquet.undo.UndoHistory history = this.getProjectHistory();
		if( history != null ) {
			this.projectHistoryIndexFile = history.getInsertionIndex();
		} else {
			this.projectHistoryIndexFile = PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL;
		}
		this.updateHistoryIndexSceneSetUpSync();
		this.updateTitle();
	}

	protected void updateHistoryIndexSceneSetUpSync() {
		org.lgna.croquet.undo.UndoHistory history = this.getProjectHistory();
		if( history != null ) {
			this.projectHistoryIndexSceneSetUp = history.getInsertionIndex();
		} else {
			this.projectHistoryIndexSceneSetUp = PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL;
		}
	}

	private org.alice.ide.frametitle.IdeFrameTitleGenerator frameTitleGenerator;

	protected abstract org.alice.ide.frametitle.IdeFrameTitleGenerator createFrameTitleGenerator();

	protected final void updateTitle() {
		if( frameTitleGenerator != null ) {
			//pass
		} else {
			this.frameTitleGenerator = this.createFrameTitleGenerator();
		}
		this.getDocumentFrame().getFrame().setTitle( this.frameTitleGenerator.generateTitle( this.getUri(), this.isProjectUpToDateWithFile() ) );
	}

	private ProjectDocument getDocument() {
		return org.alice.ide.project.ProjectDocumentState.getInstance().getValue();
	}

	private void setDocument( ProjectDocument document ) {
		org.alice.ide.project.ProjectDocumentState.getInstance().setValueTransactionlessly( document );
	}

	public org.lgna.project.Project getProject() {
		ProjectDocument document = this.getDocument();
		return document != null ? document.getProject() : null;
	}

	public void setProject( org.lgna.project.Project project ) {
		StringBuilder sb = new StringBuilder();
		java.util.Set<org.lgna.project.ast.NamedUserType> types = project.getNamedUserTypes();
		for( org.lgna.project.ast.NamedUserType type : types ) {
			boolean wasNullMethodRemoved = false;
			java.util.ListIterator<org.lgna.project.ast.UserMethod> methodIterator = type.getDeclaredMethods().listIterator();
			while( methodIterator.hasNext() ) {
				org.lgna.project.ast.UserMethod method = methodIterator.next();
				if( method != null ) {
					//pass
				} else {
					methodIterator.remove();
					wasNullMethodRemoved = true;
				}
			}
			boolean wasNullFieldRemoved = false;
			java.util.ListIterator<org.lgna.project.ast.UserField> fieldIterator = type.getDeclaredFields().listIterator();
			while( fieldIterator.hasNext() ) {
				org.lgna.project.ast.UserField field = fieldIterator.next();
				if( field != null ) {
					//pass
				} else {
					fieldIterator.remove();
					wasNullFieldRemoved = true;
				}
			}
			if( wasNullMethodRemoved ) {
				if( sb.length() > 0 ) {
					sb.append( "\n" );
				}
				sb.append( "null method was removed from " );
				sb.append( type.getName() );
				sb.append( "." );
			}
			if( wasNullFieldRemoved ) {
				if( sb.length() > 0 ) {
					sb.append( "\n" );
				}
				sb.append( "null field was removed from " );
				sb.append( type.getName() );
				sb.append( "." );
			}
		}
		if( sb.length() > 0 ) {
			new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( sb.toString() )
					.title( "A Problem With Your Project Has Been Fixed" )
					.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.WARNING )
					.buildAndShow();
		}
		org.lgna.project.ProgramTypeUtilities.sanityCheckAllTypes( project );
		this.setDocument( new ProjectDocument( project ) );
	}

	public org.lgna.croquet.history.TransactionHistory getProjectTransactionHistory() {
		return this.getDocument().getRootTransactionHistory();
	}

	public final void loadProjectFrom( org.alice.ide.uricontent.UriProjectLoader uriProjectLoader ) {
		this.setUriProjectPair( uriProjectLoader );
		this.updateHistoryIndexFileSync();
		this.updateUndoRedoEnabled();
		projectFileUtilities.startAutoSaving();
	}

	public final void loadProjectFrom( java.io.File file ) {
		this.loadProjectFrom( new org.alice.ide.uricontent.FileProjectLoader( file ) );
	}

	public final void loadProjectFrom( String path ) {
		loadProjectFrom( new java.io.File( path ) );
	}

	protected abstract java.awt.image.BufferedImage createThumbnail() throws Throwable;

	public final void saveProjectTo( java.io.File file ) throws java.io.IOException {
		//		long startTime = System.currentTimeMillis();

		projectFileUtilities.saveProjectTo( file);

		//		long endTime = System.currentTimeMillis();
		//		double saveTime = ( endTime - startTime ) * .001;
		//		System.out.println( "Save time: " + saveTime );

		org.alice.ide.recentprojects.RecentProjectsListData.getInstance().handleSave( file );

		edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: better handling of file project loader", file );

		this.uriProjectLoader = new org.alice.ide.uricontent.FileProjectLoader( file );
		this.updateHistoryIndexFileSync();
	}

	public java.io.File getMyProjectsDirectory() {
		return org.alice.ide.croquet.models.ui.preferences.UserProjectsDirectoryState.getInstance().getDirectoryEnsuringExistance();
	}

	public final org.lgna.project.Project getUpToDateProject() {
		this.ensureProjectCodeUpToDate();
		return this.getProject();
	}

	public abstract void ensureProjectCodeUpToDate();

	private final ProjectDocumentFrame projectDocumentFrame;
	private final ProjectFileUtilities projectFileUtilities;
}
