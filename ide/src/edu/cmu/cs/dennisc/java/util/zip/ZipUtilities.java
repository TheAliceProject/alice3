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
package edu.cmu.cs.dennisc.java.util.zip;

/**
 * @author Dennis Cosgrove
 */
public class ZipUtilities {
	public static byte[] extractBytes( java.util.zip.ZipInputStream zis, java.util.zip.ZipEntry zipEntry ) throws java.io.IOException {
		final int BUFFER_SIZE = 2048;
		byte[] buffer = new byte[ BUFFER_SIZE ];
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream( BUFFER_SIZE );
		int count;
		while( ( count = zis.read( buffer, 0, BUFFER_SIZE ) ) != -1 ) {
			baos.write( buffer, 0, count );
		}
		zis.closeEntry();
		return baos.toByteArray();
	}

	public static java.util.HashMap<String, byte[]> extract( java.util.zip.ZipInputStream zis ) throws java.io.IOException {
		java.util.HashMap<String, byte[]> filenameToBytesMap = new java.util.HashMap<String, byte[]>();
		java.util.zip.ZipEntry zipEntry;
		while( ( zipEntry = zis.getNextEntry() ) != null ) {
			String name = zipEntry.getName();
			if( zipEntry.isDirectory() ) {
				// pass
			} else {
				filenameToBytesMap.put( name, extractBytes( zis, zipEntry ) );
			}
		}
		return filenameToBytesMap;
	}

	public static java.util.HashMap<String, byte[]> extract( java.io.InputStream is ) throws java.io.IOException {
		java.util.zip.ZipInputStream zis;
		if( is instanceof java.util.zip.ZipInputStream ) {
			zis = (java.util.zip.ZipInputStream)is;
		} else {
			zis = new java.util.zip.ZipInputStream( is );
		}
		return extract( zis );
	}

	public static java.util.HashMap<String, byte[]> extract( java.io.File file ) throws java.io.IOException {
		return extract( new java.io.FileInputStream( file ) );
	}

	public static java.util.HashMap<String, byte[]> extract( String path ) throws java.io.IOException {
		return extract( new java.io.File( path ) );
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
	public static void zip( java.io.File srcDirectory, java.io.File dstZip ) throws java.io.IOException {

		assert srcDirectory.isDirectory();

		java.io.FileOutputStream fos = new java.io.FileOutputStream( dstZip );
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( fos );

		String rootPath = srcDirectory.getAbsolutePath();
		byte[] buffer = new byte[ 1024 ];
		java.io.FileFilter filter = null;
		java.io.File[] files = edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( srcDirectory, filter );
		for( java.io.File file : files ) {
			if( file.isDirectory() ) {
				//pass
			} else {
				String path = file.getAbsolutePath();
				assert path.startsWith( rootPath );
				String subPath = path.substring( rootPath.length() + 1 );
				subPath = subPath.replace( '\\', '/' );
				java.io.FileInputStream fis = new java.io.FileInputStream( file );
				java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry( subPath );
				zos.putNextEntry( zipEntry );
				while( true ) {
					int bytesRead = fis.read( buffer );
					if( bytesRead != -1 ) {
						zos.write( buffer, 0, bytesRead );
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

	public static void zip( java.io.File srcDirectory, String dstZipPath ) throws java.io.IOException {
		zip( srcDirectory, new java.io.File( dstZipPath ) );
	}

	public static void zip( String srcDirectoryPath, java.io.File dstZip ) throws java.io.IOException {
		zip( new java.io.File( srcDirectoryPath ), dstZip );
	}

	public static void zip( String srcDirectoryPath, String dstZipPath ) throws java.io.IOException {
		zip( new java.io.File( srcDirectoryPath ), new java.io.File( dstZipPath ) );
	}

	public static void unzip( java.io.File srcFile, java.io.File dstDirectory ) throws java.io.IOException {
		java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream( new java.io.FileInputStream( srcFile ) );
		java.util.zip.ZipEntry zipEntry;
		while( ( zipEntry = zis.getNextEntry() ) != null ) {
			if( zipEntry.isDirectory() ) {
				// pass
			} else {
				java.io.File outFile = new java.io.File( dstDirectory, zipEntry.getName() );
				edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( outFile );
				java.io.FileOutputStream fos = new java.io.FileOutputStream( outFile );
				byte[] data = extractBytes( zis, zipEntry );
				fos.write( data, 0, data.length );
				fos.close();
			}
		}
	}

	public static void unzip( java.io.File srcZip, String dstDirectoryPath ) throws java.io.IOException {
		unzip( srcZip, new java.io.File( dstDirectoryPath ) );
	}

	public static void unzip( String srcZipPath, java.io.File dstDirectory ) throws java.io.IOException {
		unzip( new java.io.File( srcZipPath ), dstDirectory );
	}

	public static void unzip( String srcZipPath, String dstDirectoryPath ) throws java.io.IOException {
		unzip( new java.io.File( srcZipPath ), new java.io.File( dstDirectoryPath ) );
	}
}
