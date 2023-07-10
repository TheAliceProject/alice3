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
package org.lgna.project.io;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import org.lgna.project.Project;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.NamedUserType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipFile;

/**
 * @author Dennis Cosgrove
 */
public abstract class IoUtilities {
  private IoUtilities() {
    throw new AssertionError();
  }

  public static final String EXPORT_EXTENSION = "a3w";
  public static final String PROJECT_EXTENSION = "a3p";
  public static final String TYPE_EXTENSION = "a3c";
  public static final String BACKUP_EXTENSION = "bak";

  public static File[] listProjectFiles(File directory) {
    return FileUtilities.listFiles(directory, PROJECT_EXTENSION);
  }

  public static File[] listTypeFiles(File directory) {
    return FileUtilities.listFiles(directory, TYPE_EXTENSION);
  }

  public static Project readProject(File file) throws IOException, VersionNotSupportedException {
    return projectReader(file).readProject(false);
  }

  public static Project readProject(String path) throws IOException, VersionNotSupportedException {
    return readProject(new File(path));
  }

  public static TypeResourcesPair readType(ZipFile zipFile) throws IOException, VersionNotSupportedException {
    return projectReader(zipFile).readType();
  }

  public static TypeResourcesPair readType(File file) throws IOException, VersionNotSupportedException {
    return projectReader(file).readType();
  }

  public static void writeProject(OutputStream os, final Project project, DataSource... dataSources) throws IOException {
    latestReadbleWriter().writeProject(os, project, dataSources);
  }

  public static void writeProject(File file, Project project, DataSource... dataSources) throws IOException {
    FileUtilities.createParentDirectoriesIfNecessary(file);
    writeProject(new FileOutputStream(file), project, dataSources);
  }

  public static void exportProject(File file, Project project, DataSource... dataSources) throws IOException {
    FileUtilities.createParentDirectoriesIfNecessary(file);
    playerWriter().writeProject(new FileOutputStream(file), project, dataSources);
  }

  public static void writeType(File file, NamedUserType type, DataSource... dataSources) throws IOException {
    FileUtilities.createParentDirectoriesIfNecessary(file);
    latestReadbleWriter().writeType(new FileOutputStream(file), type, dataSources);
  }

  public static ProjectIo.ProjectReader projectReader(File file) throws IOException {
    return projectReader(new ZipFile(file));
  }

  private static ProjectIo.ProjectReader projectReader(ZipFile zipFile) throws IOException {
    return readerForContainer(new ZipEntryContainer(zipFile));
  }

  private static ProjectIo.ProjectReader readerForContainer(ZipEntryContainer container) throws IOException {
    // TODO read manifest to identify file type and use JsonProjectIo for a3w files
    // Old format a3p files
    return XmlProjectIo.reader(container);
  }

  private static ProjectIo.ProjectWriter latestReadbleWriter() {
    //TODO replace with JSON variant
    return XmlProjectIo.writer();
  }

  private static ProjectIo.ProjectWriter playerWriter() {
    return JsonProjectIo.writer();
  }
}
