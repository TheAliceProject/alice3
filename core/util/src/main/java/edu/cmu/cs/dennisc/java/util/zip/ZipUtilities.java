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
package edu.cmu.cs.dennisc.java.util.zip;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Dennis Cosgrove
 */
public class ZipUtilities {
  public static byte[] extractBytes(ZipInputStream zis, ZipEntry zipEntry) throws IOException {
    final int BUFFER_SIZE = 2048;
    byte[] buffer = new byte[BUFFER_SIZE];
    ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
    int count;
    while ((count = zis.read(buffer, 0, BUFFER_SIZE)) != -1) {
      baos.write(buffer, 0, count);
    }
    zis.closeEntry();
    return baos.toByteArray();
  }

  public static Map<String, byte[]> extractZipInputStream(ZipInputStream zis, Collection<String> entryNameFilter) throws IOException {
    Map<String, byte[]> filenameToBytesMap = Maps.newHashMap();
    ZipEntry zipEntry;
    while ((zipEntry = zis.getNextEntry()) != null) {
      String name = zipEntry.getName();
      if (zipEntry.isDirectory()) {
        // pass
      } else {
        if ((entryNameFilter == null) || entryNameFilter.contains(zipEntry.getName())) {
          filenameToBytesMap.put(name, extractBytes(zis, zipEntry));
        }
      }
    }
    return filenameToBytesMap;
  }

  public static Map<String, byte[]> extractZipInputStream(ZipInputStream zis) throws IOException {
    return extractZipInputStream(zis, null);
  }

  public static Map<String, byte[]> extract(InputStream is, Collection<String> entryNameFilter) throws IOException {
    ZipInputStream zis;
    if (is instanceof ZipInputStream) {
      zis = (ZipInputStream) is;
    } else {
      zis = new ZipInputStream(is);
    }
    return extractZipInputStream(zis, entryNameFilter);
  }

  public static Map<String, byte[]> extract(InputStream is) throws IOException {
    return extract(is, null);
  }

  public static Map<String, byte[]> extract(File file, Collection<String> entryNameFilter) throws IOException {
    return extract(new FileInputStream(file), entryNameFilter);
  }

  public static Map<String, byte[]> extract(File file) throws IOException {
    return extract(file, null);
  }

  public static Map<String, byte[]> extract(String path, Collection<String> entryNameFilter) throws IOException {
    return extract(new File(path), entryNameFilter);
  }

  public static Map<String, byte[]> extract(String path) throws IOException {
    return extract(path, null);
  }

  public static void write(ZipOutputStream zos, DataSource dataSource) throws IOException {
    assert dataSource != null;
    assert dataSource.getName() != null;
    ZipEntry snapshotEntry = new ZipEntry(dataSource.getName());
    zos.putNextEntry(snapshotEntry);
    BufferedOutputStream bos = new BufferedOutputStream(zos);
    dataSource.write(bos);
    bos.flush();
    zos.closeEntry();
  }

  public static void addFileToZipStream(File fileToAdd, ZipOutputStream zos, String pathPrefix, byte[] buffer) throws IOException {
    assert !fileToAdd.isDirectory();
    pathPrefix = pathPrefix.replace('\\', '/');
    if (!pathPrefix.endsWith("/")) {
      pathPrefix += "/";
    }
    FileInputStream fis = new FileInputStream(fileToAdd);
    ZipEntry zipEntry = new ZipEntry(pathPrefix + fileToAdd.getName());
    zos.putNextEntry(zipEntry);
    while (true) {
      int bytesRead = fis.read(buffer);
      if (bytesRead != -1) {
        zos.write(buffer, 0, bytesRead);
      } else {
        break;
      }
    }
    zos.closeEntry();
    fis.close();
  }

  public static void addFileToZipStream(File fileToAdd, ZipOutputStream zos, String pathPrefix) throws IOException {
    addFileToZipStream(fileToAdd, zos, pathPrefix, new byte[1024]);
  }

  public static void addFileToZipStream(File rootDir, File fileToAdd, ZipOutputStream zos, String pathPrefix) throws IOException {
    assert rootDir.isDirectory();
    byte[] buffer = new byte[1024];

    String rootPath = rootDir.getAbsolutePath();
    pathPrefix = pathPrefix.replace('\\', '/');
    if ((pathPrefix.length() > 0) && !pathPrefix.endsWith("/")) {
      pathPrefix += "/";
    }
    String path = fileToAdd.getAbsolutePath();
    assert path.startsWith(rootPath);
    String subPath = path.substring(rootPath.length() + 1, path.length() - fileToAdd.getName().length());
    subPath = subPath.replace('\\', '/');
    addFileToZipStream(fileToAdd, zos, pathPrefix + subPath, buffer);
  }

  public static void addFileToZipStream(File rootDir, File fileToAdd, ZipOutputStream zos) throws IOException {
    addFileToZipStream(rootDir, fileToAdd, zos, "");
  }

  private static void addDirToZipStream(File dirToAdd, ZipOutputStream zos, String pathPrefix, boolean includeDirName) throws IOException {
    assert dirToAdd.isDirectory();
    byte[] buffer = new byte[1024];
    FileFilter filter = null;
    File[] files = FileUtilities.listDescendants(dirToAdd, filter);

    String rootPath = includeDirName ? dirToAdd.getParentFile().getAbsolutePath() : dirToAdd.getAbsolutePath();
    pathPrefix = pathPrefix.replace('\\', '/');
    if ((pathPrefix.length() > 0) && !pathPrefix.endsWith("/")) {
      pathPrefix += "/";
    }
    for (File file : files) {
      if (file.isDirectory()) {
        //pass
      } else {
        String path = file.getAbsolutePath();
        assert path.startsWith(rootPath);
        String subPath = path.substring(rootPath.length() + 1, path.length() - file.getName().length());
        subPath = subPath.replace('\\', '/');
        addFileToZipStream(file, zos, pathPrefix + subPath, buffer);
      }
    }
  }

  public static void addDirToZipStream(File dirToAdd, ZipOutputStream zos, String pathPrefix) throws IOException {
    addDirToZipStream(dirToAdd, zos, pathPrefix, true);
  }

  public static void addDirContentsToZipStream(File dirToAdd, ZipOutputStream zos, String pathPrefix) throws IOException {
    addDirToZipStream(dirToAdd, zos, pathPrefix, false);
  }

  private static void _zip(File srcDirectory, File dstZip, boolean isRecursive, FileFilter filter) throws IOException {
    assert srcDirectory.isDirectory() : srcDirectory;

    FileOutputStream fos = new FileOutputStream(dstZip);
    ZipOutputStream zos = new ZipOutputStream(fos);

    String rootPath = srcDirectory.getAbsolutePath();
    byte[] buffer = new byte[1024];
    File[] files = isRecursive ? FileUtilities.listDescendants(srcDirectory, filter) : FileUtilities.listFiles(srcDirectory, filter);

    for (File file : files) {
      if (file.isDirectory()) {
        //pass
      } else {
        String path = file.getAbsolutePath();
        assert path.startsWith(rootPath);
        String subPath = path.substring(rootPath.length() + 1);
        subPath = subPath.replace('\\', '/');
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(subPath);
        zos.putNextEntry(zipEntry);
        while (true) {
          int bytesRead = fis.read(buffer);
          if (bytesRead != -1) {
            zos.write(buffer, 0, bytesRead);
          } else {
            break;
          }
        }
        zos.closeEntry();
        fis.close();
      }
    }
    zos.flush();
    zos.close();
  }

  public static void zipFilesInDirectory(File srcDirectory, File dstZip, FileFilter filter) throws IOException {
    _zip(srcDirectory, dstZip, false, filter);
  }

  public static void zipFilesInDirectory(File srcDirectory, File dstZip) throws IOException {
    zipFilesInDirectory(srcDirectory, dstZip, null);
  }

  public static void zip(File srcDirectory, File dstZip, FileFilter filter) throws IOException {
    _zip(srcDirectory, dstZip, true, filter);
  }

  public static void zip(File srcDirectory, File dstZip) throws IOException {
    zip(srcDirectory, dstZip, null);
  }

  public static void zip(File srcDirectory, String dstZipPath) throws IOException {
    zip(srcDirectory, new File(dstZipPath));
  }

  public static void zip(String srcDirectoryPath, File dstZip) throws IOException {
    zip(new File(srcDirectoryPath), dstZip);
  }

  public static void zip(String srcDirectoryPath, String dstZipPath) throws IOException {
    zip(new File(srcDirectoryPath), new File(dstZipPath));
  }

  public static void unzip(File srcFile, File dstDirectory) throws IOException {
    ZipInputStream zis = new ZipInputStream(new FileInputStream(srcFile));
    ZipEntry zipEntry;
    while ((zipEntry = zis.getNextEntry()) != null) {
      if (zipEntry.isDirectory()) {
        // pass
      } else {
        File outFile = new File(dstDirectory, zipEntry.getName());
        FileUtilities.createParentDirectoriesIfNecessary(outFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        byte[] data = extractBytes(zis, zipEntry);
        //        final int BUFFER_SIZE = 8192;
        //        for( int i = 0; i < data.length; i += BUFFER_SIZE ) {
        //          fos.write( data, i, Math.min( data.length - i, BUFFER_SIZE ) );
        //        }
        fos.write(data, 0, data.length);
        fos.close();
      }
    }
  }

  public static void unzip(File srcZip, String dstDirectoryPath) throws IOException {
    unzip(srcZip, new File(dstDirectoryPath));
  }

  public static void unzip(String srcZipPath, File dstDirectory) throws IOException {
    unzip(new File(srcZipPath), dstDirectory);
  }

  public static void unzip(String srcZipPath, String dstDirectoryPath) throws IOException {
    unzip(new File(srcZipPath), new File(dstDirectoryPath));
  }
}
