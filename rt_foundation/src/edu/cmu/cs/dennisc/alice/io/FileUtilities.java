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

	private static String subPath = "Alice3";

	public static String getSubPath() {
		return FileUtilities.subPath;
	}
	public static void setSubPath( String subPath ) {
		FileUtilities.subPath = subPath;
	}

	public static java.io.File getMyAliceDirectory() {
		java.io.File rv = new java.io.File( edu.cmu.cs.dennisc.io.FileUtilities.getDefaultDirectory(), FileUtilities.getSubPath() );
		rv.mkdirs();
		return rv;
	}
	public static java.io.File getMyProjectsDirectory() {
		java.io.File rv = new java.io.File( getMyAliceDirectory(), "MyProjects" );
		rv.mkdirs();
		return rv;
	}
	public static java.io.File getMyTypesDirectory() {
		java.io.File rv = new java.io.File( getMyAliceDirectory(), "MyClasses" );
		rv.mkdirs();
		return rv;
	}

	public static java.io.File[] listProjectFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.io.FileUtilities.listFiles( directory, PROJECT_EXTENSION );
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
	
	private static edu.cmu.cs.dennisc.alice.Project.Properties readProperties( java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		return null;
	}
	public static edu.cmu.cs.dennisc.alice.Project readProject( java.io.File file ) {
		assert file != null;
		assert file.exists();
		try {
			java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile( file );
			return new edu.cmu.cs.dennisc.alice.Project( readType( zipFile, PROGRAM_TYPE_ENTRY_NAME ), readProperties( zipFile ) );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	public static edu.cmu.cs.dennisc.alice.Project readProject( String path ) {
		return readProject( new java.io.File( path ) );
	}

	private static void writeXML( org.w3c.dom.Document xmlDocument, java.io.File file, String entryName ) {
		edu.cmu.cs.dennisc.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		try {
			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
			java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( fos );
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
			zos.close();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}
	private static void writeType( edu.cmu.cs.dennisc.alice.ast.AbstractType type, java.io.File file, String entryName ) {
		writeXML( type.encode(), file, entryName );
	}
	public static void writeProject( edu.cmu.cs.dennisc.alice.Project project, java.io.File file ) {
		writeType( project.getProgramType(), file, PROGRAM_TYPE_ENTRY_NAME );
	}
	public static void writeProject( edu.cmu.cs.dennisc.alice.Project project, String path ) {
		writeProject( project, new java.io.File( path ) );
	}
	public static java.io.File[] listTypeFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.io.FileUtilities.listFiles( directory, TYPE_EXTENSION );
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

	public static void writeType( edu.cmu.cs.dennisc.alice.ast.AbstractType type, java.io.File file ) {
		writeType( type, file, TYPE_ENTRY_NAME );
	}
	public static void writeType( edu.cmu.cs.dennisc.alice.ast.AbstractType type, String path ) {
		writeType( type, new java.io.File( path ) );
	}
}
