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
package edu.cmu.cs.dennisc.alice.io;

/**
 * @author Dennis Cosgrove
 */
public class FileUtilities {
	public static final String PROJECT_EXTENSION = "a3p";
	public static final String TYPE_EXTENSION = "a3c";
	private static String PROPERTIES_ENTRY_NAME = "properties.bin";
	private static String PROGRAM_TYPE_ENTRY_NAME = "programType.xml";
	private static String VERSION_ENTRY_NAME = "version.txt";
	private static String TYPE_ENTRY_NAME = "type.xml";

	public static java.io.File getMyAliceDirectory( String applicationName ) {
		java.io.File rv = new java.io.File( edu.cmu.cs.dennisc.io.FileUtilities.getDefaultDirectory(), applicationName );
		rv.mkdirs();
		return rv;
	}
	public static java.io.File getMyProjectsDirectory( String applicationName ) {
		java.io.File rv = new java.io.File( getMyAliceDirectory( applicationName ), "MyProjects" );
		rv.mkdirs();
		return rv;
	}
	public static java.io.File getMyTypesDirectory( String applicationName ) {
		java.io.File rv = new java.io.File( getMyAliceDirectory( applicationName ), "MyClasses" );
		rv.mkdirs();
		return rv;
	}
	@Deprecated
	public static java.io.File getMyAliceDirectory() {
		return getMyAliceDirectory( "Alice3" );
	}
	@Deprecated
	public static java.io.File getMyProjectsDirectory() {
		return getMyProjectsDirectory( "Alice3" );
	}
	@Deprecated
	public static java.io.File getMyTypesDirectory() {
		return getMyTypesDirectory( "Alice3" );
	}

	public static java.io.File[] listProjectFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.io.FileUtilities.listFiles( directory, PROJECT_EXTENSION );
	}
	public static java.io.File[] listTypeFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.io.FileUtilities.listFiles( directory, TYPE_EXTENSION );
	}

	private static String readVersion( java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		assert zipFile != null;
		java.util.zip.ZipEntry entry = zipFile.getEntry( VERSION_ENTRY_NAME );
		assert entry != null;
		java.io.InputStream is = zipFile.getInputStream( entry );
		java.util.ArrayList< Byte > buffer = new java.util.ArrayList< Byte >( 32 );
		while( true ) {
			int b = is.read();
			if( b != -1 ) {
				buffer.add( (byte)b );
			} else {
				break;
			}
		}
		byte[] array = new byte[ buffer.size() ];
		int i = 0;
		for( Byte b : buffer ) {
			array[ i++ ] = b;
		}
		return new String( array );
	}
	private static edu.cmu.cs.dennisc.alice.Project.Properties readProperties( edu.cmu.cs.dennisc.alice.Project.Properties rv, java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		assert zipFile != null;
		java.util.zip.ZipEntry entry = zipFile.getEntry( PROPERTIES_ENTRY_NAME );
		if( entry != null ) {
			java.io.BufferedInputStream bis = new java.io.BufferedInputStream( zipFile.getInputStream( entry ) );
			rv.read( bis );
		}
		return rv;
	}
	private static org.w3c.dom.Document readXML( java.util.zip.ZipFile zipFile, String entryName ) throws java.io.IOException {
		assert zipFile != null;
		java.util.zip.ZipEntry entry = zipFile.getEntry( entryName );
		assert entry != null;
		return edu.cmu.cs.dennisc.xml.XMLUtilities.read( zipFile.getInputStream( entry ) );
	}

	private static edu.cmu.cs.dennisc.alice.ast.AbstractType readType( java.util.zip.ZipFile zipFile, String entryName ) throws java.io.IOException {
		String version = readVersion( zipFile );
		org.w3c.dom.Document xmlDocument = readXML( zipFile, entryName );
		return (edu.cmu.cs.dennisc.alice.ast.AbstractType)edu.cmu.cs.dennisc.alice.ast.Node.decode( xmlDocument, version );
	}

	public static edu.cmu.cs.dennisc.alice.Project readProject( java.io.File file ) {
		assert file != null;
		assert file.exists();
		try {
			java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile( file );
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = readType( zipFile, PROGRAM_TYPE_ENTRY_NAME );
			edu.cmu.cs.dennisc.alice.Project rv = new edu.cmu.cs.dennisc.alice.Project( type );
			readProperties( rv.getProperties(), zipFile );
			return rv;
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public static edu.cmu.cs.dennisc.alice.Project readProject( String path ) {
		return readProject( new java.io.File( path ) );
	}
	public static edu.cmu.cs.dennisc.alice.ast.AbstractType readType( java.io.File file ) {
		assert file != null;
		assert file.exists();
		try {
			java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile( file );
			return readType( zipFile, TYPE_ENTRY_NAME );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public static edu.cmu.cs.dennisc.alice.ast.AbstractType readType( String path ) {
		return readType( new java.io.File( path ) );
	}

	private static void writeProperties( java.util.zip.ZipOutputStream zos, edu.cmu.cs.dennisc.alice.Project.Properties properties ) throws java.io.IOException {
		java.util.zip.ZipEntry propertiesEntry = new java.util.zip.ZipEntry( PROPERTIES_ENTRY_NAME );
		zos.putNextEntry( propertiesEntry );
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream( zos );
		properties.write( bos );
		bos.flush();
		zos.closeEntry();
		zos.flush();
	}
	private static void writeXML( org.w3c.dom.Document xmlDocument, java.util.zip.ZipOutputStream zos, String entryName ) throws java.io.IOException {
		java.util.zip.ZipEntry programTypeEntry = new java.util.zip.ZipEntry( entryName );
		zos.putNextEntry( programTypeEntry );
		edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, zos );
		zos.closeEntry();
		zos.flush();

		java.util.zip.ZipEntry versionEntry = new java.util.zip.ZipEntry( VERSION_ENTRY_NAME );
		zos.putNextEntry( versionEntry );
		zos.write( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText().getBytes() );
		zos.closeEntry();
		zos.flush();
	}
	private static void writeType( edu.cmu.cs.dennisc.alice.ast.AbstractType type, java.util.zip.ZipOutputStream zos, String entryName ) throws java.io.IOException {
		writeXML( type.encode(), zos, entryName );
	}
	public static void writeProject( edu.cmu.cs.dennisc.alice.Project project, java.io.File file ) {
		edu.cmu.cs.dennisc.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		try {
			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
			java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( fos );
			writeType( project.getProgramType(), zos, PROGRAM_TYPE_ENTRY_NAME );
			edu.cmu.cs.dennisc.alice.Project.Properties properties = project.getProperties();
			if( properties != null ) {
				writeProperties( zos, properties );
			}
			zos.flush();
			zos.close();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}

	private static String SNAPSHOT_ENTRY_NAME = "snapshot.png";

	//todo
	public static void writeProject( edu.cmu.cs.dennisc.alice.Project project, java.io.File file, java.awt.Image snapshotImage ) {
		edu.cmu.cs.dennisc.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		try {
			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
			java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( fos );
			writeType( project.getProgramType(), zos, PROGRAM_TYPE_ENTRY_NAME );
			edu.cmu.cs.dennisc.alice.Project.Properties properties = project.getProperties();
			if( properties != null ) {
				writeProperties( zos, properties );
			}
			if( snapshotImage != null ) {
				writeSnapshotImage( zos, snapshotImage );
			}
			zos.flush();
			zos.close();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}

	private static void writeSnapshotImage( java.util.zip.ZipOutputStream zos, java.awt.Image image ) throws java.io.IOException {
		java.util.zip.ZipEntry snapshotEntry = new java.util.zip.ZipEntry( SNAPSHOT_ENTRY_NAME );

		zos.putNextEntry( snapshotEntry );
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream( zos );

		edu.cmu.cs.dennisc.image.ImageUtilities.write( edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, bos, image );

		bos.flush();
		zos.closeEntry();
	}

	public static void writeProject( edu.cmu.cs.dennisc.alice.Project project, String path ) {
		writeProject( project, new java.io.File( path ) );
	}
	public static void writeType( edu.cmu.cs.dennisc.alice.ast.AbstractType type, java.io.File file ) {
		edu.cmu.cs.dennisc.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		try {
			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
			java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( fos );
			writeType( type, zos, TYPE_ENTRY_NAME );
			zos.flush();
			zos.close();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}
	public static void writeType( edu.cmu.cs.dennisc.alice.ast.AbstractType type, String path ) {
		writeType( type, new java.io.File( path ) );
	}
}
