/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
	

	//todo: support recursion
	public static void zipContentsOfDirectory( String zipPath, String directoryPath ) throws java.io.IOException {
		java.io.FileOutputStream fos = new java.io.FileOutputStream( zipPath );
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( fos );
		
		java.io.File directory = new java.io.File( directoryPath );
		
		byte[] buffer = new byte[ 1024 ];
		java.io.File[] files = directory.listFiles();
		for( java.io.File file : files ) {
			if( file.isDirectory() ) {
				//pass
			} else {
				java.io.FileInputStream fis = new java.io.FileInputStream( file );
				java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry( file.getName() );
				zos.putNextEntry( zipEntry );
				while( true ) {
					int bytesRead = fis.read( buffer );
					if( bytesRead != -1 ) {
						zos.write( buffer, 0, bytesRead );
					} else {
						break;
					}
				}
				fis.close();
			}
		}
		zos.flush();
		zos.close();
	}
	
}
