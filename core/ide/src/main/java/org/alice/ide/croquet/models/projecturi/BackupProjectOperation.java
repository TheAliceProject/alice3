package org.alice.ide.croquet.models.projecturi;

import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult;

import java.util.UUID;

/**
 * @author Dmitry Portnoy
 */
public class BackupProjectOperation extends PotentialClearanceUriCreatorIteratingOperation {
    public BackupProjectOperation() {
        super(UUID.fromString("89b65a9c-f36a-44ba-8aed-c2922d40f298"), false);
    }

    public YesNoCancelResult showBackupProjectOpenedDialog(String backupName) {
        String title = findLocalizedText("BackupOpenedDialog.title");
        String message = findLocalizedText("BackupOpenedDialog.message")
                .replaceAll("</backupName/>", backupName);

        String option1 = findLocalizedText("BackupOpenedDialog.option1");
        String option2 = findLocalizedText("BackupOpenedDialog.option2");
        String option3 = findLocalizedText("BackupOpenedDialog.option3");

        return Dialogs.showCustomConfirmOrCancel(title, message,
                new String[] {
                        option1, option2, option3
                });
    }

    public YesNoCancelResult showUnsavedBackupProjectOpenedDialog() {
        String title = findLocalizedText("UnsavedBackupOpenedDialog.title");
        String message = findLocalizedText("UnsavedBackupOpenedDialog.message");

        String option1 = findLocalizedText("UnsavedBackupOpenedDialog.option1");
        String option2 = findLocalizedText("UnsavedBackupOpenedDialog.option2");

        return Dialogs.showCustomConfirm(title, message,
                new String[] {
                        option1, option2
                });
    }

    public void showBackupLoadErrorDialog() {
        String title = findLocalizedText("BackupLoadErrorDialog.title");
        String message = findLocalizedText("BackupLoadErrorDialog.message");

        Dialogs.showError(title, message);
    }

    public boolean showProjectLoadErrorAndLoadBackupDialog(String projectName, boolean isCurrentProjectBackup) {
        String title = findLocalizedText("ProjectLoadErrorAndLoadBackupDialog.title");
        String message = findLocalizedText("ProjectLoadErrorAndLoadBackupDialog.message")
                .replaceAll("</projectName/>", projectName)
                .replaceAll("</backupType/>", isCurrentProjectBackup
                        ? "an earlier backup"
                        : "a backup");

        return Dialogs.confirmWithWarning(title, message);
    }

    public boolean showProjectLoadRecentBackupsErrorAndLoadMainDialog(String projectName) {
        String title = findLocalizedText("BackupLoadErrorAndLoadOriginalDialog.title");
        String message = findLocalizedText("BackupLoadErrorAndLoadOriginalDialog.message")
                .replaceAll("</projectName/>", projectName);

        return Dialogs.confirmWithWarning(title, message);
    }

    public void showProjectAndAllBackupsLoadErrorDialog(String mainProjectName) {
        String title = findLocalizedText("BackupsAndOriginalLoadErrorDialog.title");
        String message = findLocalizedText("BackupsAndOriginalLoadErrorDialog.message")
                .replaceAll("</mainProjectName/>", mainProjectName);

        Dialogs.showError(title, message);
    }

    public boolean showMoreRecentBackupsDialog() {
        String title = findLocalizedText("MoreRecentBackupsDialog.title");
        String message = findLocalizedText("MoreRecentBackupsDialog.message");

        return Dialogs.confirmWithWarning(title, message);
    }
}
