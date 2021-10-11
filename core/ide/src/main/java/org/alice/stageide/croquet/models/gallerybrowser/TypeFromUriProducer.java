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
package org.alice.stageide.croquet.models.gallerybrowser;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import org.alice.ide.IDE;
import org.alice.ide.ProjectApplication;
import org.alice.stageide.StageIDE;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.io.IoUtilities;
import org.lgna.project.io.TypeResourcesPair;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.ZipFile;

/**
 * @author Dennis Cosgrove
 */
public class TypeFromUriProducer extends UriCreator<NamedUserType> {
  private static class SingletonHolder {
    private static TypeFromUriProducer instance = new TypeFromUriProducer();
  }

  public static TypeFromUriProducer getInstance() {
    return SingletonHolder.instance;
  }

  private TypeFromUriProducer() {
    super(UUID.fromString("4ab159a0-7fee-4c0f-8b71-25591fda2b0d"));
  }

  @Override
  protected String getExtension() {
    return IoUtilities.TYPE_EXTENSION;
  }

  private static void showMessageDialog(File file, boolean isValidZip) {
    String applicationName = IDE.getApplicationName();
    StringBuilder sb = new StringBuilder();
    //TODO I18n
    sb.append("Unable to create instance from file ");
    sb.append(FileUtilities.getCanonicalPathIfPossible(file));
    sb.append(".\n\n");
    sb.append(applicationName);
    sb.append(" is able to create instances from class files saved by ");
    sb.append(applicationName);
    sb.append(".\n\nLook for files with an ");
    sb.append(IoUtilities.TYPE_EXTENSION);
    sb.append(" extension.");
    Dialogs.showError("Cannot read file", sb.toString());
  }

  @Override
  protected NamedUserType internalGetValueFrom(File file) {
    final Locale locale = Locale.ENGLISH;
    String lcName = file.getName().toLowerCase(locale);
    if (lcName.endsWith(".a2c")) {
      //TODO I18n
      Dialogs.showError("Incorrect File Type", "Alice3 does not load Alice2 characters");
    } else if (lcName.endsWith(IoUtilities.PROJECT_EXTENSION.toLowerCase(locale))) {
      //TODO I18n
      Dialogs.showError("Incorrect File Type", file.getAbsolutePath() + " appears to be a project file and not a class file.\n\nLook for files with an " + IoUtilities.TYPE_EXTENSION + " extension.");
    } else {
      boolean isWorthyOfException = lcName.endsWith(IoUtilities.TYPE_EXTENSION.toLowerCase(locale));
      ZipFile zipFile;
      try {
        zipFile = new ZipFile(file);
      } catch (IOException ioe) {
        if (isWorthyOfException) {
          throw new RuntimeException(file.getAbsolutePath(), ioe);
        } else {
          showMessageDialog(file, false);
          zipFile = null;
        }
      }
      if (zipFile != null) {
        NamedUserType type;
        try {
          TypeResourcesPair typeResourcesPair = IoUtilities.readType(zipFile);
          type = typeResourcesPair.getType();
          PrintUtilities.println("TODO: add in resources");
        } catch (VersionNotSupportedException vnse) {
          type = null;
          ProjectApplication.getActiveInstance().handleVersionNotSupported(file, vnse);
        } catch (IOException ioe) {
          if (isWorthyOfException) {
            throw new RuntimeException(file.getAbsolutePath(), ioe);
          } else {
            showMessageDialog(file, true);
            type = null;
          }
        }
        return type;
      }
    }
    return null;
  }

  @Override
  protected File getInitialDirectory() {
    return StageIDE.getActiveInstance().getTypesDirectory();
  }
}
