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

import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.net.UriUtilities;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.ide.frametitle.IdeFrameTitleGenerator;
import org.alice.ide.project.ProjectDocumentState;
import org.alice.ide.recentprojects.RecentProjectsListData;
import org.alice.ide.uricontent.FileProjectLoader;
import org.alice.ide.uricontent.UriProjectLoader;
import org.lgna.croquet.Application;
import org.lgna.croquet.Group;
import org.lgna.croquet.PerspectiveApplication;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.undo.UndoHistory;
import org.lgna.croquet.undo.event.HistoryClearEvent;
import org.lgna.croquet.undo.event.HistoryInsertionIndexEvent;
import org.lgna.croquet.undo.event.HistoryListener;
import org.lgna.croquet.undo.event.HistoryPushEvent;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.ProjectVersion;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProjectApplication extends PerspectiveApplication<ProjectDocumentFrame> {
	public static final Group HISTORY_GROUP = Group.getInstance( UUID.fromString( "303e94ca-64ef-4e3a-b95c-038468c68438" ), "HISTORY_GROUP" );
	public static final Group URI_GROUP = Group.getInstance( UUID.fromString( "79bf8341-61a4-4395-9469-0448e66d9ac6" ), "URI_GROUP" );

	private UserActivity projectActivity;

	public static ProjectApplication getActiveInstance() {
		return ClassUtilities.getInstance( PerspectiveApplication.getActiveInstance(), ProjectApplication.class );
	}

	private HistoryListener projectHistoryListener;

	public ProjectApplication( IdeConfiguration ideConfiguration, ApiConfigurationManager apiConfigurationManager ) {
		this.projectFileUtilities = new ProjectFileUtilities(this);
		this.projectDocumentFrame = new ProjectDocumentFrame( ideConfiguration, apiConfigurationManager );
		this.projectHistoryListener = new HistoryListener() {
			@Override
			public void operationPushing( HistoryPushEvent e ) {
			}

			@Override
			public void operationPushed( HistoryPushEvent e ) {
			}

			@Override
			public void insertionIndexChanging( HistoryInsertionIndexEvent e ) {
			}

			@Override
			public void insertionIndexChanged( HistoryInsertionIndexEvent e ) {
				ProjectApplication.this.handleInsertionIndexChanged( e );
			}

			@Override
			public void clearing( HistoryClearEvent e ) {
			}

			@Override
			public void cleared( HistoryClearEvent e ) {
			}
		};
		this.updateTitle();
	}

	@Override
	public ProjectDocumentFrame getDocumentFrame() {
		return this.projectDocumentFrame;
	}

	private void updateUndoRedoEnabled() {
		UndoHistory historyManager = this.getProjectHistory( Application.PROJECT_GROUP );
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

	protected void handleInsertionIndexChanged( HistoryInsertionIndexEvent e ) {
		this.updateTitle();
		UndoHistory source = e.getTypedSource();
		if( source.getGroup() == PROJECT_GROUP ) {
			this.updateUndoRedoEnabled();
		}
	}

	public static String getApplicationName() {
		return "Alice";
	}

	public static String getVersionText() {
		return ProjectVersion.getCurrentVersionText();
	}

	public static String getVersionAdornment() {
		return ProjectVersion.getCurrentVersion().getMajorAndMinor();
	}

	@Override
	public String getApplicationSubPath() {
		String rv = getApplicationName();
		if( "Alice".equals( rv ) ) {
			rv = "Alice3";
		}
		return rv.replaceAll( " ", "" );
	}

	public void handleVersionNotSupported( File file, VersionNotSupportedException vnse ) {
		Dialogs.showUnableToOpenFileDialog(
			file, String.format(
				"%s is not backwards compatible with:\n    File Version: %s\n    (Minimum Supported Version: %s)",
				getApplicationName(), vnse.getVersion(), vnse.getMinimumSupportedVersion()));
	}

	private UriProjectLoader uriProjectLoader;

	public final URI getUri() {
		return this.uriProjectLoader != null ? this.uriProjectLoader.getUri() : null;
	}

	@Deprecated
	private final UndoHistory getProjectHistory() {
		return this.getProjectHistory( PROJECT_GROUP );
	}

	@Deprecated
	private final UndoHistory getProjectHistory( Group group ) {
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
		UndoHistory history = this.getProjectHistory();
		if( history == null ) {
			return true;
		} else {
			return this.projectHistoryIndexFile == history.getInsertionIndex();
		}
	}

	protected boolean isProjectUpToDateWithSceneSetUp() {
		UndoHistory history = this.getProjectHistory();
		if( history == null ) {
			return true;
		} else {
			return this.projectHistoryIndexSceneSetUp == history.getInsertionIndex();
		}
	}

	private void updateHistoryIndexFileSync() {
		UndoHistory history = this.getProjectHistory();
		if( history != null ) {
			this.projectHistoryIndexFile = history.getInsertionIndex();
		} else {
			this.projectHistoryIndexFile = PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL;
		}
		this.updateHistoryIndexSceneSetUpSync();
		this.updateTitle();
	}

	protected void updateHistoryIndexSceneSetUpSync() {
		UndoHistory history = this.getProjectHistory();
		if( history != null ) {
			this.projectHistoryIndexSceneSetUp = history.getInsertionIndex();
		} else {
			this.projectHistoryIndexSceneSetUp = PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL;
		}
	}

	private IdeFrameTitleGenerator frameTitleGenerator;

	protected abstract IdeFrameTitleGenerator createFrameTitleGenerator();

	protected final void updateTitle() {
		if( frameTitleGenerator != null ) {
			//pass
		} else {
			this.frameTitleGenerator = this.createFrameTitleGenerator();
		}
		this.getDocumentFrame().getFrame().setTitle( this.frameTitleGenerator.generateTitle( this.getUri(), this.isProjectUpToDateWithFile() ) );
	}

	private ProjectDocument getDocument() {
		return ProjectDocumentState.getInstance().getValue();
	}

	private void setDocument( ProjectDocument document ) {
		ProjectDocumentState.getInstance().setValueTransactionlessly( document );
	}

	public Project getProject() {
		ProjectDocument document = this.getDocument();
		return document != null ? document.getProject() : null;
	}

	public void setProject( Project project ) {
		StringBuilder sb = new StringBuilder();
		Set<NamedUserType> types = project.getNamedUserTypes();
		for( NamedUserType type : types ) {
			boolean wasNullMethodRemoved = false;
			ListIterator<UserMethod> methodIterator = type.getDeclaredMethods().listIterator();
			while( methodIterator.hasNext() ) {
				UserMethod method = methodIterator.next();
				if( method != null ) {
					//pass
				} else {
					methodIterator.remove();
					wasNullMethodRemoved = true;
				}
			}
			boolean wasNullFieldRemoved = false;
			ListIterator<UserField> fieldIterator = type.getDeclaredFields().listIterator();
			while( fieldIterator.hasNext() ) {
				UserField field = fieldIterator.next();
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
			Dialogs.showWarning( "A Problem With Your Project Has Been Fixed", sb.toString() );
		}
		ProgramTypeUtilities.sanityCheckAllTypes( project );
		this.setDocument( new ProjectDocument( project, newProjectActivity() ) );
	}

	private UserActivity newProjectActivity() {
		// This is the activity of opening or creating this project
		getOverallUserActivity().getLatestActivity().finish();
		// If there was a project the new one replaces it.
		if (projectActivity != null) {
			projectActivity.finish();
		}
		// Create a new project activity under the top level user activity
		projectActivity = getOverallUserActivity().newChildActivity();
		return projectActivity;
	}

	public UserActivity getProjectUserActivity() {
		return getDocument().getUserActivity();
	}

	//Look for an open child, if any. Otherwise return null.
	@Override
	public UserActivity getOpenActivity() {
		UserActivity latest = super.getOpenActivity();
		return latest == projectActivity ? null : latest;
	}

	public final void loadProject(UserActivity activity, UriProjectLoader uriProjectLoader ) {
		this.uriProjectLoader = uriProjectLoader;
		if ( uriProjectLoader != null ) {
			uriProjectLoader.deliverContentOnEventDispatchThread(proj -> projectLoaded(activity, proj));
		}
	}

	private void projectLoaded(UserActivity activity, Project project) {
		if( project == null ) {
			uriProjectLoader = null;
			activity.cancel();
		} else {
			updateInterface(project);
		}
	}

	private void updateInterface(Project project) {
		// Remove the old project history listener, so the old project can be cleaned up
		if( ( getProject() != null ) && ( getProjectHistory() != null ) ) {
			getProjectHistory().removeHistoryListener( projectHistoryListener );
		}
		setProject( project );
		getProjectHistory().addHistoryListener( projectHistoryListener );
		URI uri = uriProjectLoader.getUri();
		File file = UriUtilities.getFile(uri );
		try {
			if( ( file != null ) && file.canWrite() ) {
				RecentProjectsListData.getInstance().handleOpen(file );
			}
		} catch( Throwable throwable ) {
			throwable.printStackTrace();
		}

		updateHistoryIndexFileSync();
		updateUndoRedoEnabled();
		projectFileUtilities.startAutoSaving();
	}

	protected abstract BufferedImage createThumbnail() throws Throwable;

	public final void saveProjectTo( File file ) throws IOException {
		RecentProjectsListData.getInstance().handleSave( file );
		uriProjectLoader = new FileProjectLoader( file );

		//		long startTime = System.currentTimeMillis();

		projectFileUtilities.saveProjectTo( file);

		//		long endTime = System.currentTimeMillis();
		//		double saveTime = ( endTime - startTime ) * .001;
		//		System.out.println( "Save time: " + saveTime );

		this.updateHistoryIndexFileSync();
	}

	public final void exportProjectTo( File file ) throws IOException {
		projectFileUtilities.exportCopyOfProjectTo( file );
	}

	public final Project getUpToDateProject() {
		this.ensureProjectCodeUpToDate();
		return this.getProject();
	}

	public abstract void ensureProjectCodeUpToDate();

	private final ProjectDocumentFrame projectDocumentFrame;
	private final ProjectFileUtilities projectFileUtilities;

	public String getAuthorName() {
		return getPreferencesManager().getValue("authorName", System.getProperty( "user.name" ));
	}

	public void setAuthorName(String newName) {
		getPreferencesManager().setValue("authorName", newName);
	}
}
