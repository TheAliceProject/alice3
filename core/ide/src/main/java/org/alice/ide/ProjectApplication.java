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

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.net.UriUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult;
import org.alice.ide.frametitle.IdeFrameTitleGenerator;
import org.alice.ide.project.ProjectDocumentState;
import org.alice.ide.recentprojects.RecentProjectsListData;
import org.alice.ide.uricontent.FileProjectLoader;
import org.alice.ide.uricontent.StarterProjectFileLoader;
import org.alice.ide.uricontent.UriProjectLoader;
import org.apache.commons.io.FileUtils;
import org.lgna.croquet.Application;
import org.lgna.croquet.CancelException;
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

import javax.swing.RootPaneContainer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;

import static edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles;
import static org.alice.ide.ProjectFileUtilities.BACKUP_AUTO;
import static org.lgna.project.io.IoUtilities.PROJECT_EXTENSION;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProjectApplication extends PerspectiveApplication<ProjectDocumentFrame> {
  public static final Group HISTORY_GROUP = Group.getInstance(UUID.fromString("303e94ca-64ef-4e3a-b95c-038468c68438"), "HISTORY_GROUP");
  public static final Group URI_GROUP = Group.getInstance(UUID.fromString("79bf8341-61a4-4395-9469-0448e66d9ac6"), "URI_GROUP");

  private UserActivity projectActivity;

  public static ProjectApplication getActiveInstance() {
    return ClassUtilities.getInstance(PerspectiveApplication.getActiveInstance(), ProjectApplication.class);
  }

  private HistoryListener projectHistoryListener;

  public ProjectApplication(ApiConfigurationManager apiConfigurationManager) {
    this.projectFileUtilities = new ProjectFileUtilities(this);
    this.projectDocumentFrame = new ProjectDocumentFrame(apiConfigurationManager);
    this.projectHistoryListener = new HistoryListener() {
      @Override
      public void operationPushing(HistoryPushEvent e) {
      }

      @Override
      public void operationPushed(HistoryPushEvent e) {
      }

      @Override
      public void insertionIndexChanging(HistoryInsertionIndexEvent e) {
      }

      @Override
      public void insertionIndexChanged(HistoryInsertionIndexEvent e) {
        ProjectApplication.this.handleInsertionIndexChanged(e);
      }

      @Override
      public void clearing(HistoryClearEvent e) {
      }

      @Override
      public void cleared(HistoryClearEvent e) {
      }
    };
    this.updateTitle();
  }

  @Override
  public ProjectDocumentFrame getDocumentFrame() {
    return this.projectDocumentFrame;
  }

  private void updateUndoRedoEnabled() {
    UndoHistory historyManager = this.getProjectHistory(Application.PROJECT_GROUP);
    boolean isUndoEnabled;
    boolean isRedoEnabled;
    if (historyManager != null) {
      int index = historyManager.getInsertionIndex();
      int size = historyManager.getStack().size();
      isUndoEnabled = index > 0;
      isRedoEnabled = index < size;
    } else {
      isUndoEnabled = false;
      isRedoEnabled = false;
    }

    ProjectDocumentFrame documentFrame = this.getDocumentFrame();
    documentFrame.getUndoOperation().setEnabled(isUndoEnabled);
    documentFrame.getRedoOperation().setEnabled(isRedoEnabled);
  }

  protected void handleInsertionIndexChanged(HistoryInsertionIndexEvent e) {
    this.updateTitle();
    UndoHistory source = e.getTypedSource();
    if (source.getGroup() == PROJECT_GROUP) {
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
    return String.valueOf(ProjectVersion.getCurrentVersion().getAliceIdentifier());
  }

  @Override
  public String getApplicationSubPath() {
    String rv = getApplicationName();
    if ("Alice".equals(rv)) {
      rv = "Alice3";
    }
    return rv.replaceAll(" ", "");
  }

  public void handleVersionNotSupported(File file, VersionNotSupportedException vnse) {
    Dialogs.showUnableToOpenFileDialog(file, String.format("%s is not backwards compatible with:\n    File Version: %s\n    (Minimum Supported Version: %s)", getApplicationName(), vnse.getVersion(), vnse.getMinimumSupportedVersion()));
  }

  public UriProjectLoader uriProjectLoader;

  public final URI getUri() {
    return this.uriProjectLoader != null ? this.uriProjectLoader.getUri() : null;
  }

  @Deprecated
  private final UndoHistory getProjectHistory() {
    return this.getProjectHistory(PROJECT_GROUP);
  }

  @Deprecated
  private final UndoHistory getProjectHistory(Group group) {
    if (this.getDocument() == null) {
      return null;
    } else {
      return this.getDocument().getUndoHistory(group);
    }
  }

  //todo: investigate
  private static final int PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL = 0;

  private int projectHistoryIndexFile = 0;
  private int projectHistoryIndexSceneSetUp = 0;
  private int projectHistoryIndexBackups = 0;

  public boolean isProjectUpToDateWithFile() {
    UndoHistory history = this.getProjectHistory();
    if (uriProjectLoader != null && uriProjectLoader.shouldBeSaved()) {
      return false;
    }
    if (history == null) {
      return true;
    } else {
      return this.projectHistoryIndexFile == history.getInsertionIndex();
    }
  }

  protected boolean isProjectUpToDateWithSceneSetUp() {
    UndoHistory history = this.getProjectHistory();
    if (history == null) {
      return true;
    } else {
      return this.projectHistoryIndexSceneSetUp == history.getInsertionIndex();
    }
  }

  protected boolean isProjectUpToDateWithBackups() {
    UndoHistory history = this.getProjectHistory();
    if (history == null) {
      return true;
    } else {
      return this.projectHistoryIndexBackups == history.getInsertionIndex();
    }
  }

  private void updateHistoryIndexFileSync() {
    UndoHistory history = this.getProjectHistory();
    if (history != null) {
      this.projectHistoryIndexFile = history.getInsertionIndex();
    } else {
      this.projectHistoryIndexFile = PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL;
    }
    this.updateHistoryIndexSceneSetUpSync();
    this.updateTitle();
  }

  protected void updateHistoryIndexSceneSetUpSync() {
    UndoHistory history = this.getProjectHistory();
    if (history != null) {
      this.projectHistoryIndexSceneSetUp = history.getInsertionIndex();
    } else {
      this.projectHistoryIndexSceneSetUp = PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL;
    }
  }

  private void updateHistoryIndexBackupSync() {
    UndoHistory history = this.getProjectHistory();
    this.projectHistoryIndexBackups = history != null
            ? history.getInsertionIndex()
            : PROJECT_HISTORY_INDEX_IF_PROJECT_HISTORY_IS_NULL;
  }

  private IdeFrameTitleGenerator frameTitleGenerator;

  protected abstract IdeFrameTitleGenerator createFrameTitleGenerator();

  protected final void updateTitle() {
    if (frameTitleGenerator == null) {
      this.frameTitleGenerator = this.createFrameTitleGenerator();
    }
    this.getDocumentFrame().getFrame().setTitle(this.frameTitleGenerator.generateTitle(uriProjectLoader, isProjectUpToDateWithFile()));
  }

  private ProjectDocument getDocument() {
    return ProjectDocumentState.getInstance().getValue();
  }

  private void setDocument(ProjectDocument document) {
    ProjectDocumentState.getInstance().setValueTransactionlessly(document);
  }

  public Project getProject() {
    ProjectDocument document = this.getDocument();
    return document != null ? document.getProject() : null;
  }

  public void setProject(Project project) {
    // TODO I18N
    StringBuilder sb = new StringBuilder();
    Set<NamedUserType> types = project.getNamedUserTypes();
    for (NamedUserType type : types) {
      boolean wasNullMethodRemoved = false;
      ListIterator<UserMethod> methodIterator = type.getDeclaredMethods().listIterator();
      while (methodIterator.hasNext()) {
        UserMethod method = methodIterator.next();
        if (method == null) {
          methodIterator.remove();
          wasNullMethodRemoved = true;
        }
      }
      boolean wasNullFieldRemoved = false;
      ListIterator<UserField> fieldIterator = type.getDeclaredFields().listIterator();
      while (fieldIterator.hasNext()) {
        UserField field = fieldIterator.next();
        if (field == null) {
          fieldIterator.remove();
          wasNullFieldRemoved = true;
        }
      }
      if (wasNullMethodRemoved) {
        if (sb.length() > 0) {
          sb.append("\n");
        }
        sb.append("null method was removed from ");
        sb.append(type.getName());
        sb.append(".");
      }
      if (wasNullFieldRemoved) {
        if (sb.length() > 0) {
          sb.append("\n");
        }
        sb.append("null field was removed from ");
        sb.append(type.getName());
        sb.append(".");
      }
    }
    if (sb.length() > 0) {
      Dialogs.showWarning("A Problem With Your Project Has Been Fixed", sb.toString());
    }
    String typeCheck = ProgramTypeUtilities.sanityCheckAllTypes(project);
    if (typeCheck.length() > 0) {
      Dialogs.showError("Problems With Your Project Were Not Fixed", "These may cause errors when editing or running.\nProceed with caution.\n\n" + typeCheck);
    }
    this.setDocument(new ProjectDocument(project, newProjectActivity()));
  }

  private UserActivity newProjectActivity() {
    // If present, this is the activity of opening or creating a project
    UserActivity openChild = getOpenActivity();
    if (openChild != null) {
      openChild.finish();
    }
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

  //Look for an open child, if any. Otherwise, return null.
  @Override
  public UserActivity getOpenActivity() {
    UserActivity latest = super.getOpenActivity();
    return latest == projectActivity ? null : latest;
  }

  public final void loadProject(UserActivity activity, UriProjectLoader uriProjectLoader) {
    loadProject(activity, uriProjectLoader, false, false, new HashSet<>());
  }

  private void loadProject(UserActivity activity, UriProjectLoader uriProjectLoader, boolean isLoadingBackups,
                                 boolean isMainProjectCorrupted, Set<String> unloadableFiles) {
    this.uriProjectLoader = uriProjectLoader;
    if (uriProjectLoader != null) {
      showWaitCursor();
      uriProjectLoader.deliverContentOnEventDispatchThread(proj -> {
        try {
          projectLoaded(activity, proj, isLoadingBackups, isMainProjectCorrupted, unloadableFiles);
        } catch (RuntimeException re) {
          handleProjectLoadException(re, activity);
        } finally {
          hideWaitCursor();
        }
      });
    }
  }

  protected boolean loadNewProjectBackup() {
    File backupDir = projectFileUtilities.defaultBackupDirectory().toFile();
    File backup = getNextBackup(null, backupDir, false, new HashSet<>());

    if (backup != null) {
      YesNoCancelResult result = Dialogs.showCustomConfirm("Load Backup of New Project",
              "WARNING: backups of an unsaved new project were detected.\n"
              + "Would you like to load the latest one, or discard all backups?",
              new String[] {
                      "Load Backup", "Discard Backups"
              });

      return switch (result) {
        case YES -> {
          // load a backup
          loadProject(newProjectActivity(), new FileProjectLoader(backup, false), true, true, new HashSet<>());

          yield true;
        }
        case NO -> {
          // discard the backups
          try {
            FileUtils.deleteDirectory(backupDir);
          } catch (IOException e) {
            Logger.throwable(e, "Unable to delete default backup directory.");
          }

          yield false;
        }
        case CANCEL ->
          // unreachable path
          false;
      };
    }

    return false;
  }

  private void projectLoaded(UserActivity activity, Project project, boolean isLoadingBackups,
                             boolean isMainProjectCorrupted, Set<String> unloadableFiles) {
    File saved = UriUtilities.getFile(getUri());

    if (saved != null && !projectFileUtilities.isProject(saved)) {
      return;
    }

    boolean isBackup = uriProjectLoader.isBackup(saved);

    if (project == null) {
      handleProjectLoadError(saved, activity, isBackup, isLoadingBackups, isMainProjectCorrupted, unloadableFiles);
    } else {
      handleProjectLoadSuccess(project, saved, activity, isBackup, isLoadingBackups, isMainProjectCorrupted, unloadableFiles);
    }
  }

  private void handleProjectLoadError(File projectFile, UserActivity activity, boolean isBackup,
                                      boolean isLoadingBackups, boolean isMainProjectCorrupted,
                                      Set<String> unloadableFiles) {
    File backupDir = projectFileUtilities.appropriateBackupDirectory(projectFile, isBackup).toFile();
    boolean makeVrReady = uriProjectLoader.shouldMakeVrReady();

    unloadableFiles.add(projectFile.getName());

    // If this failed attempt was already a load of a backup, keep the existing value
    // Otherwise, this is the main project, so set this to true
    if (!isLoadingBackups) {
      isMainProjectCorrupted = true;
    }

    File mainProject = getMainProjectFile(projectFile);
    LocalDateTime projectModifiedTime = FileUtilities.getModifiedDateTime(mainProject);
    File backup = getNextBackup(projectModifiedTime, backupDir, isMainProjectCorrupted, unloadableFiles);

    uriProjectLoader = null;
    activity.cancel();

    // A corrupted backup was manually loaded
    if (isBackup && !isLoadingBackups) {
      Dialogs.showError("Unable to Load Backup", "This backup was corrupted and none could be loaded");

      return;
    }

    if (backup != null) {
      String message = isBackup ? "an earlier backup" : "a backup"; // BOTH CONFIRMED

      if (Dialogs.confirmWithWarning("Load Backup",
              "WARNING: " + projectFile.getName() + " could not be loaded.\nWould you like to try loading " + message + "?")) {
        loadProject(newProjectActivity(), new FileProjectLoader(backup, makeVrReady), true, isMainProjectCorrupted, unloadableFiles);
      }
    } else {
      if (isMainProjectCorrupted) {
        Dialogs.showError("Unable to Load Backup", "The original project (" + mainProject.getName() + ") and all backups were corrupted, and none could be loaded");
      } else {
        if (Dialogs.confirmWithWarning("Reload Original",
                "WARNING: all backups more recent than the project were corrupted.\nWould you like to reload the original project file (" + projectFile.getName() + ")?")) {
          loadProject(newProjectActivity(), new FileProjectLoader(mainProject, makeVrReady), false, isMainProjectCorrupted, unloadableFiles);
        }
      }
    }
  }

  private void handleProjectLoadSuccess(Project project, File projectFile, UserActivity activity, boolean isBackup,
                                        boolean isLoadingBackups, boolean isMainProjectCorrupted,
                                        Set<String> unloadableFiles) {
    if (isBackup && !isLoadingBackups) {
      // User manually opened a backup, don't do anything special
    } else if (unloadableFiles.isEmpty() && !uriProjectLoader.isNewProject()) {
      // check for backups newer than the project

      File backupDir = projectFileUtilities.appropriateBackupDirectory(projectFile, isBackup).toFile();

      LocalDateTime projectModifiedTime = FileUtilities.getModifiedDateTime(projectFile);

      File backup = getNextBackup(projectModifiedTime, backupDir, false, unloadableFiles);

      if (backup != null && Dialogs.confirmWithWarning("Load backup?",
              "WARNING: There is a backup with more recent changes.\nWould you like to load it instead?")) {
        // restart load with backup
        loadProject(newProjectActivity(), new FileProjectLoader(backup, uriProjectLoader.shouldMakeVrReady()), true, isMainProjectCorrupted, unloadableFiles);

        return;
      }
    }

    boolean defaultBackup = uriProjectLoader.isDefaultBackup(projectFile);

    if (defaultBackup) {
      try {
        uriProjectLoader = new StarterProjectFileLoader(new URI("starterfile:/"), uriProjectLoader.shouldMakeVrReady());
      } catch (URISyntaxException e) {
        Logger.throwable(e, uriProjectLoader);
      }
    }

    updateInterface(project);

    if (!isLoadingBackups || defaultBackup) {
      return;
    }

    // If a backup of a saved project was successfully loaded, prompt the user for what to do next
    createProjectFromBackup(projectFile, getMainProjectFile(projectFile));
  }

  private void handleProjectLoadException(RuntimeException re, UserActivity activity) {
    var message = new StringBuilder("Errors reported in " + getUri());
    Throwable cause = re;
    Logger.throwable(re, getUri());
    do {
      var causeMessage = cause.getLocalizedMessage();
      if (causeMessage != null) {
        message.append("\n\n  ").append(causeMessage);
      }
      cause = cause.getCause();
    } while (cause != null);
    // TODO clear project remnants from system
    uriProjectLoader = null;
    activity.cancel(new CancelException(re));
    Dialogs.showError("Unable to Load Project", message.toString());
    setPerspective(getDocumentFrame().getNoProjectPerspective());
    UserActivity newActivity = getOverallUserActivity().getLatestActivity().newChildActivity();
    getDocumentFrame().getNewProjectOperation().fire(newActivity);
  }

  private void createProjectFromBackup(File backup, File original) {
    YesNoCancelResult result = Dialogs.showCustomConfirmOrCancel("Replace Project With Backup?",
            "A backup has been opened successfully: " + backup.getName() + ".\n"
                    + "Would you like to replace the original project, or create a new project from the backup?\n"
                    + "Cancel to do neither and just continue opening the backup.",
                    new String[] {
                            "Replace Original Project", "Save Copy Under New Name", "Cancel"
                    });

    switch (result) {
      case YES -> {
        // replace the main project file

        try {
          saveProjectTo(original);
        } catch (IOException ioe) {
          Dialogs.showError("Unable to save file", ioe.getMessage());
        }
      }
      case NO -> {
        // when the user saves it, create a new project from this one

        try {
          uriProjectLoader = new StarterProjectFileLoader(new URI("starterfile:/"), uriProjectLoader.shouldMakeVrReady());
        } catch (URISyntaxException e) {
          Logger.throwable(e, uriProjectLoader);
        }
      }
      case CANCEL -> {
        // load the main project file

        loadProject(newProjectActivity(), new FileProjectLoader(original, uriProjectLoader.shouldMakeVrReady()));
      }
    };
  }

  private File getMainProjectFile(File f) {
    if (!uriProjectLoader.isBackup(f)) {
      return f;
    }

    File backupDir = f.getParentFile();
    String originalFileName = FileUtilities.getBaseName(backupDir) + "." + PROJECT_EXTENSION;

    return f.toPath().getParent().resolveSibling(originalFileName).toFile();
  }

  private File getNextBackup(LocalDateTime modifiedTime, File backupDir, boolean isMainProjectCorrupted, Set<String> unloadableFiles) {
    if (backupDir == null) {
      return null;
    }

    File[] backups = getSortedBackups(BACKUP_AUTO, backupDir);

    for (File backup : backups) {
      if (!unloadableFiles.contains(backup.getName())) {
        // if the main project is corrupted, return the latest backup
        if (isMainProjectCorrupted || modifiedTime == null || modifiedTime == LocalDateTime.MIN) {
          return backup;
        }

        // otherwise, return the latest backup, as long as it is newer than the main project

        LocalDateTime backupCreatedTime = FileUtilities.getCreatedDateTime(backup);

        if (backupCreatedTime.isAfter(modifiedTime)) {
          return backup;
        } else { // don't bother checking any backups older than the original project
          return null;
        }
      }
    }

    return null;
  }

  protected File[] getSortedBackups(final String type, File backupDir) {
    File[] backups = listFiles(backupDir, file -> file.isFile() && file.getName().startsWith(type));

    Arrays.sort(backups, new Comparator<File>() {
      @Override
      public int compare(final File f1, final File f2) {
        LocalDateTime f1Creation = FileUtilities.getCreatedDateTime(f1);
        LocalDateTime f2Creation = FileUtilities.getCreatedDateTime(f2);
        return f1Creation.compareTo(f2Creation);
      }
    });

    // reverse the array to read the latest entries first
    Collections.reverse(Arrays.asList(backups));

    return backups;
  }

  private void updateInterface(Project project) {
    // Remove the old project history listener, so the old project can be cleaned up
    if ((getProject() != null) && (getProjectHistory() != null)) {
      getProjectHistory().removeHistoryListener(projectHistoryListener);
    }
    setProject(project);
    // Normally, the menu bar sub-menus are populated when the user is about to open one. However,
    // the popupMenuWillBecomeVisible/popupMenuWillBecomeInvisible events don't fire on Mac specifically
    // for the top-level menu bar. As a workaround, all submenus are initialized at launch on Mac,
    // and re-initialized every time a new project is opened, so the recent projects list remains correct
    if (SystemUtilities.isMac()) {
      getDocumentFrame().getFrame().rebuildMenuBar();
    }
    getProjectHistory().addHistoryListener(projectHistoryListener);
    URI uri = getUri();
    File file = UriUtilities.getFile(uri);
    try {
      if ((file != null) && file.canWrite()) {
        RecentProjectsListData.getInstance().handleOpen(file);
      }
    } catch (Throwable throwable) {
      Logger.throwable(throwable, file);
    }

    updateHistoryIndexFileSync();
    updateUndoRedoEnabled();
    projectFileUtilities.startAutoSaving();
  }

  protected abstract BufferedImage createThumbnail() throws Throwable;

  public final void saveProjectTo(File file) throws IOException {
    File originalFile = UriUtilities.getFile(getUri());

    boolean savingNewProject = uriProjectLoader.isNewProject()
            || (uriProjectLoader.isDefaultBackup(originalFile) && !uriProjectLoader.isDefaultBackup(file));

    if (savingNewProject) {
      projectFileUtilities.renameDefaultBackupDirectory(file);
    }

    uriProjectLoader = new FileProjectLoader(file);

    //    long startTime = System.currentTimeMillis();

    projectFileUtilities.saveProjectTo(file);

    if (savingNewProject) {
      updateInterface(getUpToDateProject());
    }

    //    long endTime = System.currentTimeMillis();
    //    double saveTime = ( endTime - startTime ) * .001;
    //    System.out.println( "Save time: " + saveTime );
    RecentProjectsListData.getInstance().handleSave(file);

    this.updateHistoryIndexFileSync();
  }

  public final void updateBackupIndexAndSaveProjectTo(File file) throws IOException {
    projectFileUtilities.saveCopyOfProjectTo(file);

    updateHistoryIndexBackupSync();
  }

  public final void exportProjectTo(File file) throws IOException {
    projectFileUtilities.exportCopyOfProjectTo(file);
  }

  final Project getForcedUpToDateProject() {
    forceProjectCodeUpToDate();
    return getProject();
  }

  public abstract void forceProjectCodeUpToDate();

  public final Project getUpToDateProject() {
    ensureProjectCodeUpToDate();
    return getProject();
  }

  public abstract void ensureProjectCodeUpToDate();

  public void showWaitCursor() {
    CursorUtilities.pushAndSetRootWait(getRootComponent());
  }

  public void hideWaitCursor() {
    CursorUtilities.popAndSetRoot(getRootComponent());
  }

  private RootPaneContainer getRootComponent() {
    return getDocumentFrame().getFrame().getAwtComponent();
  }

  private final ProjectDocumentFrame projectDocumentFrame;
  private final ProjectFileUtilities projectFileUtilities;

  public String getAuthorName() {
    return getPreferencesManager().getValue("authorName", System.getProperty("user.name"));
  }

  public void setAuthorName(String newName) {
    getPreferencesManager().setValue("authorName", newName);
  }
}
