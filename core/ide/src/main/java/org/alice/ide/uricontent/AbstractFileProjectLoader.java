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
package org.alice.ide.uricontent;

import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.ide.ProjectApplication;
import org.lgna.project.Project;
import org.lgna.project.ProjectVersion;
import org.lgna.project.Version;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.io.IoUtilities;
import org.lgna.project.io.ProjectIo;
import org.lgna.story.resourceutilities.StorytellingResourcesTreeUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public abstract class AbstractFileProjectLoader extends UriProjectLoader {

  AbstractFileProjectLoader(File file, boolean makeVrReady) {
    super(makeVrReady);
    this.file = file;
  }

  protected File getFile() {
    return this.file;
  }

  @Override
  protected Project load() {
    if (!isAlice3ProjectFile()) {
      return null;
    }
    try {
      ProjectIo.ProjectReader reader = IoUtilities.projectReader(file);
      if (isFromFutureVersion(reader)) {
        return null;
      }
      reader.setResourceTypeHelper(StorytellingResourcesTreeUtils.INSTANCE);
      return reader.readProject(makeVrReady);
    } catch (VersionNotSupportedException vnse) {
      ProjectApplication.getActiveInstance().handleVersionNotSupported(file, vnse);
    } catch (IOException ioe) {
      Dialogs.showUnableToOpenFileDialog(file, "");
    }
    return null;
  }

  private boolean isAlice3ProjectFile() {
    if (!file.exists()) {
      // TODO I18n
      Dialogs.showUnableToOpenFileDialog(file, "It does not exist.");
      return false;
    }
    final Locale locale = Locale.ENGLISH;
    String lcFilename = file.getName().toLowerCase(locale);
    if (lcFilename.endsWith(".a2w")) {
      // TODO I18n
      Dialogs.showError("Cannot read file", "Alice3 does not load Alice2 worlds");
      return false;
    }
    if (lcFilename.endsWith(IoUtilities.TYPE_EXTENSION.toLowerCase(locale))) {
      // TODO I18n
      Dialogs.showError("Incorrect File Type", file.getAbsolutePath() + " appears to be a class file and not a project file.\n\nLook for files with the extension " + IoUtilities.PROJECT_EXTENSION);
      return false;
    }
    return true;
  }

  private boolean isFromFutureVersion(ProjectIo.ProjectReader reader) throws IOException {
    Version fromFutureVersion = reader.checkForFutureVersion();
    if (fromFutureVersion != null) {
      // TODO I18n
      return !Dialogs.confirmWithWarning("From later Alice version", "WARNING: This project was produced by a newer version of Alice:" + fromFutureVersion + "\n" + "You are running " + ProjectVersion.getCurrentVersion() + " and should consider upgrading. Visit alice.org.\n\n" + "Would you like to try to load this anyway?");
    }
    return false;
  }

  private final File file;
}
