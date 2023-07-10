package org.lgna.project.io;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipOutputStream;

abstract class DataSourceIo {

  static void writeDataSources(OutputStream os, List<DataSource> dataSources) throws IOException {
    ZipOutputStream zos = new ZipOutputStream(os);
    Set<String> names = new HashSet<>();
    for (DataSource dataSource : dataSources) {
      if (dataSource == null) {
        continue;
      }
      if (names.add(dataSource.getName())) {
        ZipUtilities.write(zos, dataSource);
      } else {
        throw new RuntimeException("Attempt to add duplicate entries under the name " + dataSource.getName());
      }
    }
    zos.flush();
    zos.close();
  }

  static void writeDataSources(File outputDirectory, List<DataSource> dataSources) throws IOException {
    outputDirectory.mkdirs();
    for (DataSource dataSource : dataSources) {
      File outputFile = new File(outputDirectory, dataSource.getName());
      FileUtilities.createParentDirectoriesIfNecessary(outputFile);
      FileOutputStream fos = new FileOutputStream(outputFile);
      dataSource.write(fos);
      fos.close();
    }
  }
}
