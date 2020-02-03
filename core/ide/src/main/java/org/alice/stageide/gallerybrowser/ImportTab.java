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
package org.alice.stageide.gallerybrowser;

import org.alice.ide.icons.Icons;
import org.alice.stageide.StageIDE;
import org.alice.stageide.gallerybrowser.views.ImportTabView;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.FileDialogValueCreator;
import org.lgna.croquet.Operation;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.StringState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.io.IoUtilities;

import java.io.File;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class ImportTab extends GalleryTab {
  private final PlainStringValue notDirectoryText = this.createStringValue("notDirectoryText");
  private final PlainStringValue noFilesText = this.createStringValue("noFilesText");
  private final StringState directoryState = this.createStringState("directoryState");
  private final Operation browseOperation = this.createActionOperation("browseOperation", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      File directory = new File(directoryState.getValue());
      FileDialogValueCreator fileOp = new FileDialogValueCreator(null, directory, IoUtilities.TYPE_EXTENSION);

      final UserActivity fileActivity = userActivity.newChildActivity();
      fileOp.fire(fileActivity);

      if (fileActivity.isCanceled() || fileActivity.getProducedValue() == null) {
        throw new CancelException();
      }

      File file = (File) fileActivity.getProducedValue();
      if (file.isFile()) {
        file = file.getParentFile();
      }
      directoryState.setValueTransactionlessly(file.getAbsolutePath());
      return null;
    }
  });
  private final Operation restoreToDefaultOperation = this.createActionOperation("restoreToDefaultOperation", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      restoreToDefault();
      return null;
    }
  });

  public ImportTab() {
    super(UUID.fromString("89ae8138-80a3-40e8-a8e6-e2f9b47ac452"));
    this.restoreToDefault();
    this.browseOperation.setButtonIcon(Icons.FOLDER_ICON_SMALL);
  }

  public boolean isDirectoryStateSetToDefault() {
    return StageIDE.getActiveInstance().getTypesDirectory().getAbsolutePath().contentEquals(this.directoryState.getValue());
  }

  private void restoreToDefault() {
    this.directoryState.setValueTransactionlessly(StageIDE.getActiveInstance().getTypesDirectory().getAbsolutePath());
  }

  public PlainStringValue getNotDirectoryText() {
    return this.notDirectoryText;
  }

  public PlainStringValue getNoFilesText() {
    return this.noFilesText;
  }

  public StringState getDirectoryState() {
    return this.directoryState;
  }

  public Operation getBrowseOperation() {
    return this.browseOperation;
  }

  public Operation getRestoreToDefaultOperation() {
    return this.restoreToDefaultOperation;
  }

  @Override
  protected ImportTabView createView() {
    return new ImportTabView(this);
  }
}
