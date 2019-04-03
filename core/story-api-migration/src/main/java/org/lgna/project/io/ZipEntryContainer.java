package org.lgna.project.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ZipEntryContainer {
  private final ZipFile zipFile;

  ZipEntryContainer(ZipFile file) {
    zipFile = file;
  }

  public InputStream getInputStream(String name) throws IOException {
    ZipEntry zipEntry = zipFile.getEntry(name);
    return zipEntry == null ? null : zipFile.getInputStream(zipEntry);
  }
}
