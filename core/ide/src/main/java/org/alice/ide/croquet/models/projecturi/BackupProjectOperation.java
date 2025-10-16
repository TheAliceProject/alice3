package org.alice.ide.croquet.models.projecturi;

import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult;

import java.time.LocalDateTime;
import java.time.format.*;
import java.util.Locale;
import java.util.UUID;

import static org.alice.ide.ProjectFileUtilities.ORDER_FORMAT;

/**
 * @author Dmitry Portnoy
 */
public class BackupProjectOperation extends PotentialClearanceUriCreatorIteratingOperation {
    private static final DateTimeFormatter READABLE_DATETIME_FORMAT =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault());

    public BackupProjectOperation() {
        super(UUID.fromString("89b65a9c-f36a-44ba-8aed-c2922d40f298"), false);
    }

    public YesNoCancelResult showBackupProjectOpenedDialog(String mainProjectName, String backupName, boolean isMainProjectCorrupted) {
        String title = findLocalizedText("BackupOpenedDialog.title");
        String message = "";

        String backupDateString = getDateStringFromBackupName(backupName);

        if (backupDateString != null) {
            message = findLocalizedText("BackupOpenedDialog.messageWithBackupDate")
                    .replaceAll("</projectName/>", mainProjectName)
                    .replaceAll("</backupDate/>", backupDateString);
        } else { // handle the unexpected case when the date can't be read from the backup filename
            message = findLocalizedText("BackupOpenedDialog.messageWithBackupName")
                    .replaceAll("</projectName/>", mainProjectName)
                    .replaceAll("</backupName/>", backupName);
        }

        String option1 = findLocalizedText("BackupOpenedDialog.option1");
        String option2 = findLocalizedText("BackupOpenedDialog.option2");
        String option3 = findLocalizedText("BackupOpenedDialog.option3");

        return Dialogs.showCustomConfirmOrCancel(title, message,
                isMainProjectCorrupted
                ? new String[] {
                        option1, option2
                    }
                : new String[] {
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

    public boolean showProjectLoadErrorAndLoadBackupDialog(String mainProjectName, String backupName, boolean isCurrentProjectBackup) {
        String title = findLocalizedText("ProjectLoadErrorAndLoadBackupDialog.title");
        String message = "";

        if (isCurrentProjectBackup) {
            String backupDateString = getDateStringFromBackupName(backupName);

            if (backupDateString != null) {
                message = findLocalizedText("ProjectLoadErrorAndLoadBackupDialog.messageWithBackupDate")
                        .replaceAll("</projectName/>", mainProjectName)
                        .replaceAll("</backupDate/>", backupDateString);
            } else {  // handle the unexpected case when the date can't be read from the backup filename
                message = findLocalizedText("ProjectLoadErrorAndLoadBackupDialog.messageWithBackupName")
                        .replaceAll("</projectName/>", mainProjectName)
                        .replaceAll("</backupName/>", backupName);
            }
        } else {
            message = findLocalizedText("ProjectLoadErrorAndLoadBackupDialog.message")
                    .replaceAll("</projectName/>", mainProjectName);
        }

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

    public void showUnsavedBackupsLoadErrorDialog() {
        String title = findLocalizedText("UnsavedBackupsLoadErrorDialog.title");
        String message = findLocalizedText("UnsavedBackupsLoadErrorDialog.message");

        Dialogs.showError(title, message);
    }

    public boolean showMoreRecentBackupsDialog() {
        String title = findLocalizedText("MoreRecentBackupsDialog.title");
        String message = findLocalizedText("MoreRecentBackupsDialog.message");

        return Dialogs.confirmWithWarning(title, message);
    }

    private String getDateStringFromBackupName(String name) {
        if (name.length() < 8) {
            return null;
        }

        try {
            String datetime = name.substring(4, name.length() - 4);
            LocalDateTime date = LocalDateTime.parse(datetime, ORDER_FORMAT);

            return date.format(READABLE_DATETIME_FORMAT);
        } catch (DateTimeParseException pe) {
            return null;
        }
    }
}
