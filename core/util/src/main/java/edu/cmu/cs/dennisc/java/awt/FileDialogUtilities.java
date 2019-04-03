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
package edu.cmu.cs.dennisc.java.awt;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.map.MapToMap;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class FileDialogUtilities {
  private interface FileDialog {
    String getFile();

    void setFile(String filename);

    String getDirectory();

    void setDirectory(String path);

    void setFilenameFilter(FilenameFilter filenameFilter);

    void show();
  }

  private static class AwtFileDialog implements FileDialog {
    private final java.awt.FileDialog awtFileDialog;

    AwtFileDialog(Component root, String title, int mode) {
      if (root instanceof Frame) {
        awtFileDialog = new java.awt.FileDialog((Frame) root, title, mode);
      } else if (root instanceof Dialog) {
        awtFileDialog = new java.awt.FileDialog((Dialog) root, title, mode);
      } else {
        awtFileDialog = new java.awt.FileDialog((Dialog) null, title, mode);
      }
    }

    @Override
    public String getFile() {
      return this.awtFileDialog.getFile();
    }

    @Override
    public void setFile(String filename) {
      this.awtFileDialog.setFile(filename);
    }

    @Override
    public String getDirectory() {
      return this.awtFileDialog.getDirectory();
    }

    @Override
    public void setDirectory(String path) {
      this.awtFileDialog.setDirectory(path);
    }

    @Override
    public void setFilenameFilter(FilenameFilter filenameFilter) {
      this.awtFileDialog.setFilenameFilter(filenameFilter);
    }

    @Override
    public void show() {
      this.awtFileDialog.setVisible(true);
    }
  }

  private static class SwingFileDialog implements FileDialog {
    private final JFileChooser jFileChooser = new JFileChooser();
    private final Component root;
    private final String title;
    private final int mode;
    private int result = JFileChooser.CANCEL_OPTION;

    SwingFileDialog(Component root, String title, int mode) {
      this.root = root;
      this.title = title;
      this.mode = mode;
    }

    @Override
    public String getFile() {
      if (this.result != JFileChooser.CANCEL_OPTION) {
        File file = this.jFileChooser.getSelectedFile();
        if (file != null) {
          return file.getName();
        } else {
          return null;
        }
      } else {
        return null;
      }
    }

    @Override
    public void setFile(String filename) {
      this.jFileChooser.setSelectedFile(new File(filename));
    }

    @Override
    public String getDirectory() {
      if (this.result != JFileChooser.CANCEL_OPTION) {
        File file = this.jFileChooser.getCurrentDirectory();
        if (file != null) {
          return file.getAbsolutePath();
        } else {
          return null;
        }
      } else {
        return null;
      }
    }

    @Override
    public void setDirectory(String path) {
      if (path != null) {
        this.jFileChooser.setCurrentDirectory(new File(path));
      }
    }

    @Override
    public void setFilenameFilter(FilenameFilter filenameFilter) {
      //todo Wrap FilenameFilter in a FileFilter
    }

    @Override
    public void show() {
      //todo: use this.title
      this.result = JFileChooser.CANCEL_OPTION;
      if (mode == java.awt.FileDialog.LOAD) {
        this.result = this.jFileChooser.showOpenDialog(this.root);
      } else {
        this.result = this.jFileChooser.showSaveDialog(this.root);
      }

    }
  }

  private static MapToMap<Component, String, FileDialog> mapPathToSaveFileDialog = MapToMap.newInstance();
  private static Map<String, String> mapSecondaryKeyToPath = Maps.newHashMap();

  private static FileDialog createFileDialog(Component root, String title, int mode) {
    if (SystemUtilities.isLinux()) {
      return new SwingFileDialog(root, title, mode);
    } else {
      return new AwtFileDialog(root, title, mode);
    }
  }

  private static final String NULL_KEY = "null";

  public static File showSaveFileDialog(Component component, File directory, String filename, String extension, boolean isSharingDesired) {
    String directoryPath = directory != null ? directory.getAbsolutePath() : null;
    FileDialog fileDialog;
    Component root = SwingUtilities.getRoot(component);
    String secondaryKey;
    if (directoryPath != null) {
      secondaryKey = directoryPath;
    } else {
      secondaryKey = NULL_KEY;
    }
    MapToMap<Component, String, FileDialog> mapPathToFileDialog;
    mapPathToFileDialog = FileDialogUtilities.mapPathToSaveFileDialog;
    if (isSharingDesired) {
      fileDialog = mapPathToFileDialog.get(component, secondaryKey);
    } else {
      fileDialog = null;
    }
    if (fileDialog == null) {
      fileDialog = createFileDialog(root, "Save...", java.awt.FileDialog.SAVE);
      if (isSharingDesired) {
        mapPathToFileDialog.put(component, secondaryKey, fileDialog);
      }
    }
    if (filename != null) {
      fileDialog.setFile(filename);
    }

    String path;
    if (isSharingDesired) {
      path = FileDialogUtilities.mapSecondaryKeyToPath.get(secondaryKey);
    } else {
      path = null;
    }
    if (path == null) {
      path = directoryPath;
    }
    if (path != null) {
      fileDialog.setDirectory(path);
    }

    fileDialog.show();
    String fileName = fileDialog.getFile();
    if (fileName != null) {
      String requestedDirectoryPath = fileDialog.getDirectory();
      if (isSharingDesired) {
        FileDialogUtilities.mapSecondaryKeyToPath.put(secondaryKey, requestedDirectoryPath);
      }
      File directory1 = new File(requestedDirectoryPath);
      if (!fileName.endsWith("." + extension)) {
        fileName += "." + extension;
      }
      return new File(directory1, fileName);
    } else {
      return null;
    }
  }

  private static File showFileDialog(int mode, Component component, String title, File directory, String filename, FilenameFilter filenameFilter, String extensionToAddIfMissing) {
    if ((extensionToAddIfMissing != null) && (extensionToAddIfMissing.charAt(0) == '.')) {
      Logger.severe("removing leading . from", extensionToAddIfMissing);
      extensionToAddIfMissing = extensionToAddIfMissing.substring(1);
    }
    Component root = SwingUtilities.getRoot(component);
    FileDialog fileDialog = createFileDialog(root, title, mode);
    if (filename != null) {
      fileDialog.setFile(filename);
    }
    if ((directory != null) && directory.exists()) {
      fileDialog.setDirectory(directory.toString());
    }
    if (filenameFilter != null) {
      fileDialog.setFilenameFilter(filenameFilter);
    }
    fileDialog.show();
    String fileName = fileDialog.getFile();
    File rv;
    if (fileName != null) {
      String requestedDirectoryPath = fileDialog.getDirectory();
      File rvDirectory = new File(requestedDirectoryPath);
      if (mode == java.awt.FileDialog.SAVE) {
        if (!fileName.endsWith("." + extensionToAddIfMissing)) {
          fileName += "." + extensionToAddIfMissing;
        }
      }
      rv = new File(rvDirectory, fileName);
    } else {
      rv = null;
    }
    return rv;
  }

  public static File showOpenFileDialog(Component component, String title, File initialDirectory, String initialFilename, FilenameFilter filenameFilter) {
    return showFileDialog(java.awt.FileDialog.LOAD, component, title, initialDirectory, initialFilename, filenameFilter, null);
  }

  public static File showSaveFileDialog(Component component, String title, File initialDirectory, String initialFilename, FilenameFilter filenameFilter, String extensionToAddIfMissing) {
    return showFileDialog(java.awt.FileDialog.SAVE, component, title, initialDirectory, initialFilename, filenameFilter, extensionToAddIfMissing);
  }
}
