package org.lgna.project.io;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

public abstract class DataSourceIo {

    protected static void writeDataSources(OutputStream os, List<DataSource> dataSources ) throws IOException {
        ZipOutputStream zos = new ZipOutputStream( os );
        for( DataSource dataSource : dataSources ) {
            ZipUtilities.write( zos, dataSource );
        }
        zos.flush();
        zos.close();
    }

    protected static void writeDataSources(File outputDirectory, List<DataSource> dataSources ) throws IOException {
        outputDirectory.mkdirs();
        for( DataSource dataSource : dataSources ) {
            File outputFile = new File(outputDirectory, dataSource.getName());
            FileUtilities.createParentDirectoriesIfNecessary(outputFile);
            FileOutputStream fos = new FileOutputStream(outputFile);
            dataSource.write(fos);
            fos.close();
        }
    }


    protected static DataSource createDataSource( String name, String content ) {
        return new DataSource() {
            @Override public String getName() { return name; }

            @Override public void write( OutputStream os ) throws IOException {
                os.write( content.getBytes() );
            }
        };
    }
}
