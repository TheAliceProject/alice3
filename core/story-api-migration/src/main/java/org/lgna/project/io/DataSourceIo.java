package org.lgna.project.io;

import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;

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

    protected static DataSource createDataSource( String name, String content ) {
        return new DataSource() {
            @Override public String getName() { return name; }

            @Override public void write( OutputStream os ) throws IOException {
                os.write( content.getBytes() );
            }
        };
    }
}
