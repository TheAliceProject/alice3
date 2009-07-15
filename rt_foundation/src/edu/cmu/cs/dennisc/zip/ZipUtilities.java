/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.zip;

/**
 * @author Dennis Cosgrove
 */
public class ZipUtilities {
	public static java.util.HashMap< String, byte[] > extract( java.io.InputStream is ) throws java.io.IOException {
		java.util.zip.ZipInputStream zis;
		if( is instanceof java.util.zip.ZipInputStream ) {
			zis = (java.util.zip.ZipInputStream) is;
		} else {
			zis = new java.util.zip.ZipInputStream( is );
		}
		java.util.HashMap<String, byte[]> filenameToBytesMap = new java.util.HashMap<String, byte[]>();
		java.util.zip.ZipEntry zipEntry;
		while ((zipEntry = zis.getNextEntry()) != null) {
			String name = zipEntry.getName();
			if( zipEntry.isDirectory() ) {
				// pass
			} else {
				final int BUFFER_SIZE = 2048;
				byte[] buffer = new byte[BUFFER_SIZE];
				java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream( BUFFER_SIZE );
				int count;
				while ((count = zis.read( buffer, 0, BUFFER_SIZE )) != -1) {
					baos.write( buffer, 0, count );
				}
				zis.closeEntry();
				filenameToBytesMap.put( name, baos.toByteArray() );
			}
		}
		return filenameToBytesMap;
	}
	
	public static void write( java.util.zip.ZipOutputStream zos, DataSource dataSource ) throws java.io.IOException {
		assert dataSource != null;
		assert dataSource.getName() != null;
		java.util.zip.ZipEntry snapshotEntry = new java.util.zip.ZipEntry( dataSource.getName() );
		zos.putNextEntry( snapshotEntry );
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream( zos );
		dataSource.write( bos );
		bos.flush();
		zos.closeEntry();
	}
}
