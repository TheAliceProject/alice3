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
package edu.cmu.cs.dennisc.java.io;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class FileUtilities {
  private static File s_defaultDirectory = null;

  public static File getDefaultDirectory() {
    if (s_defaultDirectory == null) {
      FileSystemView fileSystemView = FileSystemView.getFileSystemView();
      s_defaultDirectory = fileSystemView.getDefaultDirectory();
    }
    return s_defaultDirectory;
  }

  public static boolean isValidFile(File file) {
    if (file != null) {
      try {
        file.getCanonicalPath();
        return true;
      } catch (IOException ioe) {
        return false;
      }
    } else {
      return false;
    }
  }

  public static boolean isValidPath(String path) {
    return isValidFile(path != null ? new File(path) : null);
  }

  public static String getCanonicalPathIfPossible(File file) {
    if (file != null) {
      try {
        return file.getCanonicalPath();
      } catch (IOException ioe) {
        return file.getAbsolutePath();
      }
    } else {
      return null;
    }
  }

  public static String getExtension(String filename) {
    String extension = null;
    if (filename != null) {
      int index = filename.lastIndexOf('.');
      if (index != -1) {
        extension = filename.substring(index + 1);
      }
    }
    return extension;
  }

  public static String getExtension(File file) {
    if (file != null) {
      return getExtension(file.getName());
    } else {
      return null;
    }
  }

  public static String getBaseName(String filename) {
    if (filename == null) {
      return null;
    }
    String trimmed = trimExtension(filename);
    trimmed = trimBefore(trimmed, '/');
    return trimBefore(trimmed, '\\');
  }

  private static String trimExtension(String filename) {
    int index = filename.lastIndexOf('.');
    return index != -1 ? filename.substring(0, index) : filename;
  }

  private static String trimBefore(String filename, char character) {
    int index = filename.lastIndexOf(character);
    return index != -1 ? filename.substring(index + 1) : filename;
  }

  public static String getBaseName(File file) {
    if (file != null) {
      return getBaseName(file.getName());
    } else {
      return null;
    }
  }

  public static boolean createParentDirectoriesIfNecessary(File file) {
    return file.getParentFile().mkdirs();
  }

  public static File[] listFiles(File root, FileFilter fileFilter) {
    File[] rv = root.listFiles(fileFilter);
    if (rv == null) {
      rv = new File[0];
    }
    return rv;
  }

  public static File[] listDirectories(File root) {
    return listFiles(root, File::isDirectory);
  }

  public static File[] listFiles(File root, String extension) {
    assert extension.charAt(0) != '.';
    final String ext = extension;
    return listFiles(root, file -> file.isFile() && ext.equalsIgnoreCase(getExtension(file)));
  }

  public static File[] listFiles(String rootPath, String extension) {
    return listFiles(new File(rootPath), extension);
  }

  private static void appendDescendants(List<File> descendants, File dir, FileFilter fileFilter, int depth) {
    File[] files = dir.listFiles(fileFilter);
    if (files != null) {
      Collections.addAll(descendants, files);
    }

    if (depth != 0) {
      if (depth != -1) {
        depth--;
      }
      File[] dirs = dir.listFiles(File::isDirectory);
      if (dirs != null) {
        for (File childDir : dirs) {
          appendDescendants(descendants, childDir, fileFilter, depth);
        }
      }
    }
  }

  public static File[] listDescendants(File root, FileFilter fileFilter, int depth) {
    List<File> list = Lists.newLinkedList();
    appendDescendants(list, root, fileFilter, depth);
    return ArrayUtilities.createArray(list, File.class);
  }

  public static File[] listDescendants(File root, FileFilter fileFilter) {
    return listDescendants(root, fileFilter, -1);
  }

  public static File[] listDescendants(File root, final String extension) {
    assert root.exists() : root;
    assert extension != null;
    assert extension.charAt(0) != '.';
    return listDescendants(root,
        file -> file.isFile() && extension.equalsIgnoreCase(getExtension(file)));
  }

  public static FilenameFilter createFilenameFilter(final String extension) {
    return (dir, name) -> name.toUpperCase().endsWith(extension.toUpperCase());
  }

  public static FileFilter createFileWithExtensionFilter(final String extension) {
    return file -> {
      if (file.isFile()) {
        return file.getName().toUpperCase().endsWith(extension.toUpperCase());
      } else {
        return false;
      }
    };
  }

  public static FileFilter createDirectoryFilter() {
    return File::isDirectory;
  }

  public static void copyFile(File in, File out) throws IOException {
    createParentDirectoriesIfNecessary(out);
    FileChannel inChannel = new FileInputStream(in).getChannel();
    FileChannel outChannel = new FileOutputStream(out).getChannel();
    inChannel.transferTo(0, inChannel.size(), outChannel);
    inChannel.close();
    outChannel.close();
  }

  public static LocalDateTime getCreatedDateTime(File f) {
    try {
      BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
      FileTime fileTime = attr.creationTime();
      return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
    } catch (IOException ioe) {
      Logger.throwable(ioe, f);
      return LocalDateTime.MIN;
    }
  }

  public static LocalDateTime getModifiedDateTime(File f) {
    if (f == null) {
      return LocalDateTime.MIN;
    }

    try {
      FileTime fileTime = Files.getLastModifiedTime(f.toPath());
      return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
    } catch (IOException ioe) {
      Logger.throwable(ioe, f);
      return LocalDateTime.MIN;
    }
  }
}
