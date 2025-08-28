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

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.net.UriUtilities;
import org.alice.ide.projecturi.ProjectSnapshot;
import org.alice.stageide.openprojectpane.models.TemplateUriState;
import org.lgna.project.Project;

import java.io.File;
import java.net.URI;

import static edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension;
import static org.alice.ide.ProjectFileUtilities.BACKUP_EXTENSION;
import static org.alice.ide.ProjectFileUtilities.DEFAULT_BACKUP_DIR;
import static org.lgna.project.io.IoUtilities.PROJECT_EXTENSION;

/**
 * @author Dennis Cosgrove
 */
public abstract class UriProjectLoader extends UriContentLoader<Project> {
  protected final boolean makeVrReady;

  public UriProjectLoader(boolean makeVrReady) {
    this.makeVrReady = makeVrReady;
  }

  public static UriProjectLoader createInstance(ProjectSnapshot proj, boolean makeVrReady) {
    if (proj != null) {
      URI uri = proj.getUri();
      String scheme = uri.getScheme();
      if ("file".equalsIgnoreCase(scheme)) {
        File file = UriUtilities.getFile(uri);
        return new FileProjectLoader(file, makeVrReady);
      } else if (TemplateUriState.STARTER_SCHEME.equalsIgnoreCase(scheme)) {
        return new StarterProjectFileLoader(uri, makeVrReady);
      } else if (proj.hasValidUri()) {
        TemplateUriState.Template template = TemplateUriState.Template.getSurfaceAppearance(proj);
        return new BlankSlateProjectLoader(template, makeVrReady);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public abstract boolean isNewProject();

  // If true the project expects to be saved but has not yet.
  // Defaults to false.
  public boolean shouldBeSaved() {
    return false;
  }

  public boolean shouldMakeVrReady() {
    return makeVrReady;
  }

  public URI getMainProjectUri() {
    URI uri = getUri();
    File projectFile = UriUtilities.getFile(getUri());

    if (projectFile != null) {
      uri = getMainProjectFile(projectFile).toURI();
    }

    return uri;
  }

  public boolean isBackup(File f) {
    if (f == null) {
      return false;
    }

    String parentDirExtension = getParentDirExtension(f);

    return BACKUP_EXTENSION.equals(parentDirExtension) || DEFAULT_BACKUP_DIR.equals(parentDirExtension);
  }

  public boolean isDefaultBackup(File f) {
    if (f == null) {
      return false;
    }

    String parentDirExtension = getParentDirExtension(f);

    return DEFAULT_BACKUP_DIR.equals(parentDirExtension);
  }

  protected File getMainProjectFile(File f) {
    if (!isBackup(f)) {
      return f;
    }

    File backupDir = f.getParentFile();
    String originalFileName = FileUtilities.getBaseName(backupDir) + "." + PROJECT_EXTENSION;

    return f.toPath().getParent().resolveSibling(originalFileName).toFile();
  }

  protected String getParentDirExtension(File f) {
    if (isNewProject()) {
      return "";
    }

    File parentDir = f.getParentFile();

    if (parentDir == null) {
      return "";
    }

    return getExtension(parentDir.getName());
  }
}
