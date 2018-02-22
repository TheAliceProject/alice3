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
package org.lgna.project.io;

import org.alice.serialization.xml.XmlEncoderDecoder;

/**
 * @author Dennis Cosgrove
 */
public abstract class IoUtilities {
	private IoUtilities() {
		throw new AssertionError();
	}

	public static final String PROJECT_EXTENSION = "a3p";
	public static final String TYPE_EXTENSION = "a3c";
	public static final String BACKUP_EXTENSION = "bak";

	public static java.io.File[] listProjectFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, PROJECT_EXTENSION );
	}

	public static java.io.File[] listTypeFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, TYPE_EXTENSION );
	}

	private static final String PROPERTIES_ENTRY_NAME = "properties.bin";
	private static final String PROGRAM_TYPE_ENTRY_NAME = "programType.xml";
	private static final String VERSION_ENTRY_NAME = "version.txt";
	private static final String TYPE_ENTRY_NAME = "type.xml";
	private static final String RESOURCES_ENTRY_NAME = "resources.xml";

	private static final String XML_RESOURCE_TAG_NAME = "resource";

	private static final String XML_RESOURCE_CLASSNAME_ATTRIBUTE = "className";
	private static final String XML_RESOURCE_UUID_ATTRIBUTE = "uuid";
	private static final String XML_RESOURCE_ENTRY_NAME_ATTRIBUTE = "entryName";

	private static interface ZipEntryContainer {
		public java.io.InputStream getInputStream( String name ) throws java.io.IOException;
	}

	private static class ZipFileEntryContainer implements ZipEntryContainer {
		private final java.util.zip.ZipFile zipFile;

		public ZipFileEntryContainer( java.util.zip.ZipFile zipFile ) {
			this.zipFile = zipFile;
		}

		@Override
		public java.io.InputStream getInputStream( String name ) throws java.io.IOException {
			java.util.zip.ZipEntry zipEntry = this.zipFile.getEntry( name );
			if( zipEntry != null ) {
				return this.zipFile.getInputStream( zipEntry );
			} else {
				return null;
			}
		}
	}

	private static class ZipInputStreamEntryContainer implements ZipEntryContainer {
		private final java.util.Map<String, byte[]> mapZipEntryToBuffer;

		public ZipInputStreamEntryContainer( java.util.zip.ZipInputStream zipInputStream ) {
			try {
				this.mapZipEntryToBuffer = edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.extract( zipInputStream );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}

		@Override
		public java.io.InputStream getInputStream( String name ) throws java.io.IOException {
			byte[] buffer = this.mapZipEntryToBuffer.get( name );
			if( buffer != null ) {
				return new java.io.ByteArrayInputStream( buffer );
			} else {
				return null;
			}
		}
	}

	private static org.lgna.project.Version readVersion( ZipEntryContainer zipEntryContainer, String entryName ) throws java.io.IOException {
		assert zipEntryContainer != null;
		java.io.InputStream is = zipEntryContainer.getInputStream( entryName );
		if( is != null ) {
			//todo?
			java.util.ArrayList<Byte> buffer = new java.util.ArrayList<Byte>( 32 );
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
			return new org.lgna.project.Version( new String( array ) );
		} else {
			throw new java.io.IOException( zipEntryContainer.toString() + " does not contain entry " + entryName );
		}
	}

	private static org.lgna.project.Version safeReadVersion( ZipEntryContainer zipEntryContainer, String entryName, String absentVersion ) throws java.io.IOException {
		try {
			return readVersion( zipEntryContainer, entryName );
		} catch( java.io.IOException e ) {
			if( absentVersion != null ) {
				return new org.lgna.project.Version( absentVersion );
			} else {
				throw e;
			}
		}
	}

	private static org.lgna.project.Project readProperties( org.lgna.project.Project rv, ZipEntryContainer zipEntryContainer ) throws java.io.IOException {
		assert zipEntryContainer != null;
		java.io.InputStream is = zipEntryContainer.getInputStream( PROPERTIES_ENTRY_NAME );
		if( is != null ) {
			java.io.BufferedInputStream bis = new java.io.BufferedInputStream( is );
			edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder binaryDecoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( bis );
			String version = binaryDecoder.decodeString();
			final int N = binaryDecoder.decodeInt();
			for( int i = 0; i < N; i++ ) {
				org.lgna.project.properties.PropertyKey.decodeIdAndValueAndPut( rv, binaryDecoder, version );
			}
		}
		return rv;
	}

	public static org.w3c.dom.Document readXML( java.io.InputStream is, MigrationManagerDecodedVersionPair[] migrationManagerDecodedVersionPairs ) throws java.io.IOException {
		String modifiedText = null;
		for( MigrationManagerDecodedVersionPair migrationManagerDecodedVersionPair : migrationManagerDecodedVersionPairs ) {
			org.lgna.project.migration.MigrationManager migrationManager = migrationManagerDecodedVersionPair.getMigrationManager();
			org.lgna.project.Version decodedVersion = migrationManagerDecodedVersionPair.getDecodedVersion();
			if( ( migrationManager.getCurrentVersion().compareTo( decodedVersion ) == 0 ) && migrationManager.isDevoidOfVersionIndependentMigrations() ) {
				//pass
			} else {
				if( modifiedText != null ) {
					//pass
				} else {
					modifiedText = edu.cmu.cs.dennisc.java.io.TextFileUtilities.read( new java.io.InputStreamReader( is, "UTF-8" ) );
				}
				modifiedText = migrationManager.migrate( modifiedText, decodedVersion );
			}
		}
		if( modifiedText != null ) {
			is = new java.io.ByteArrayInputStream( modifiedText.getBytes( "UTF-8" ) );
		}
		return edu.cmu.cs.dennisc.xml.XMLUtilities.read( is );
	}

	private static org.w3c.dom.Document readXML( ZipEntryContainer zipEntryContainer, String entryName, MigrationManagerDecodedVersionPair[] migrationManagerDecodedVersionPairs ) throws java.io.IOException {
		assert zipEntryContainer != null;
		java.io.InputStream is = zipEntryContainer.getInputStream( entryName );
		return readXML( is, migrationManagerDecodedVersionPairs );
	}

	private static org.lgna.project.ast.NamedUserType readType( ZipEntryContainer zipEntryContainer, String entryName, String versionIfAbsent ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		org.lgna.project.Version decodedProjectVersion = safeReadVersion( zipEntryContainer, VERSION_ENTRY_NAME, versionIfAbsent );

		MigrationManagerDecodedVersionPair[] migrationManagerDecodedVersionPairs = {
				new MigrationManagerDecodedVersionPair( org.lgna.project.migration.ProjectMigrationManager.getInstance(), decodedProjectVersion )
		};

		org.w3c.dom.Document xmlDocument = readXML( zipEntryContainer, entryName, migrationManagerDecodedVersionPairs );
		org.lgna.project.ast.NamedUserType rv = (org.lgna.project.ast.NamedUserType) (new XmlEncoderDecoder()).decode( xmlDocument );

		org.lgna.project.Project projectIfApplicable = null;
		org.lgna.project.migration.ast.AstMigrationUtilities.migrateNode( rv, projectIfApplicable, migrationManagerDecodedVersionPairs );
		return rv;
	}

	private static org.lgna.project.ast.NamedUserType readType( ZipEntryContainer zipEntryContainer, String entryName ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		String versionIfAbsent = null; // throw exception
		return readType( zipEntryContainer, entryName, versionIfAbsent );
	}

	private static java.util.Set<org.lgna.common.Resource> readResources( ZipEntryContainer zipEntryContainer ) throws java.io.IOException {
		java.util.Set<org.lgna.common.Resource> rv = new java.util.HashSet<org.lgna.common.Resource>();
		java.io.InputStream isResources = zipEntryContainer.getInputStream( RESOURCES_ENTRY_NAME );
		if( isResources != null ) {
			org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.read( isResources );
			java.util.List<org.w3c.dom.Element> xmlElements = edu.cmu.cs.dennisc.xml.XMLUtilities.getChildElementsByTagName( xmlDocument.getDocumentElement(), XML_RESOURCE_TAG_NAME );
			for( org.w3c.dom.Element xmlElement : xmlElements ) {
				String className = xmlElement.getAttribute( XML_RESOURCE_CLASSNAME_ATTRIBUTE );
				String uuidText = xmlElement.getAttribute( XML_RESOURCE_UUID_ATTRIBUTE );
				String entryName = xmlElement.getAttribute( XML_RESOURCE_ENTRY_NAME_ATTRIBUTE );
				if( ( className != null ) && ( uuidText != null ) && ( entryName != null ) ) {
					byte[] data = edu.cmu.cs.dennisc.java.io.InputStreamUtilities.getBytes( zipEntryContainer.getInputStream( entryName ) );
					if( data != null ) {
						try {
							Class<? extends org.lgna.common.Resource> resourceCls = (Class<? extends org.lgna.common.Resource>)edu.cmu.cs.dennisc.java.lang.ClassUtilities.forName( className );
							org.lgna.common.Resource resource = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.valueOf( resourceCls, uuidText );
							resource.decodeAttributes( xmlElement, data );
							rv.add( resource );
						} catch( ClassNotFoundException cnfe ) {
							edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: no class for name:", className );
						}
					} else {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: no data for resource:", entryName );
					}
				}
			}
		}
		return rv;
	}

	private static org.lgna.project.Project readProject( ZipEntryContainer zipEntryContainer ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		assert zipEntryContainer != null;
		org.lgna.project.ast.NamedUserType type = readType( zipEntryContainer, PROGRAM_TYPE_ENTRY_NAME );

		//todo
		java.util.Set<org.lgna.project.ast.NamedUserType> namedUserTypes = java.util.Collections.emptySet();
		java.util.Set<org.lgna.common.Resource> resources = readResources( zipEntryContainer );
		org.lgna.project.Project rv = new org.lgna.project.Project( type, namedUserTypes, resources );
		readProperties( rv, zipEntryContainer );
		return rv;
	}

	public static org.lgna.project.Project readProject( java.util.zip.ZipInputStream zis ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		return readProject( new ZipInputStreamEntryContainer( zis ) );
	}

	public static org.lgna.project.Project readProject( java.io.InputStream is ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		return readProject( new java.util.zip.ZipInputStream( is ) );
	}

	public static org.lgna.project.Project readProject( java.util.zip.ZipFile zipFile ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		return readProject( new ZipFileEntryContainer( zipFile ) );
	}

	public static org.lgna.project.Project readProject( java.io.File file ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		assert file != null;
		assert file.exists();
		return readProject( new java.util.zip.ZipFile( file ) );
	}

	public static org.lgna.project.Project readProject( String path ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		assert path != null;
		return readProject( new java.io.File( path ) );
	}

	private static TypeResourcesPair readType( ZipEntryContainer zipEntryContainer ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		org.lgna.project.ast.NamedUserType type = readType( zipEntryContainer, TYPE_ENTRY_NAME );
		java.util.Set<org.lgna.common.Resource> resources = readResources( zipEntryContainer );
		return new TypeResourcesPair( type, resources );
	}

	public static TypeResourcesPair readType( java.util.zip.ZipInputStream zis ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		return readType( new ZipInputStreamEntryContainer( zis ) );
	}

	public static TypeResourcesPair readType( java.io.InputStream is ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		return readType( new java.util.zip.ZipInputStream( is ) );
	}

	public static TypeResourcesPair readType( java.util.zip.ZipFile zipFile ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		return readType( new ZipFileEntryContainer( zipFile ) );
	}

	public static TypeResourcesPair readType( java.io.File file ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		assert file != null;
		assert file.exists() : file;
		return readType( new java.util.zip.ZipFile( file ) );
	}

	public static TypeResourcesPair readType( String path ) throws java.io.IOException, org.lgna.project.VersionNotSupportedException {
		return readType( new java.io.File( path ) );
	}

	private static void writeVersion( java.util.zip.ZipOutputStream zos, final String entryName, final org.lgna.project.Version version ) throws java.io.IOException {
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.java.util.zip.DataSource() {
			@Override
			public String getName() {
				return entryName;
			}

			@Override
			public void write( java.io.OutputStream os ) throws java.io.IOException {
				os.write( version.toString().getBytes() );
			}
		} );
	}

	private static void writeVersions( java.util.zip.ZipOutputStream zos ) throws java.io.IOException {
		writeVersion( zos, VERSION_ENTRY_NAME, org.lgna.project.ProjectVersion.getCurrentVersion() );
	}

	private static void writeXML( final org.w3c.dom.Document xmlDocument, java.util.zip.ZipOutputStream zos, final String entryName ) throws java.io.IOException {
		edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.java.util.zip.DataSource() {
			@Override
			public String getName() {
				return entryName;
			}

			@Override
			public void write( java.io.OutputStream os ) throws java.io.IOException {
				edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, os );
			}
		} );
	}

	private static void writeType( org.lgna.project.ast.AbstractType<?, ?, ?> type, java.util.zip.ZipOutputStream zos, String entryName ) throws java.io.IOException {
		writeXML( (new XmlEncoderDecoder()).encode(type), zos, entryName );
	}

	private static void writeDataSources( java.util.zip.ZipOutputStream zos, edu.cmu.cs.dennisc.java.util.zip.DataSource... dataSources ) throws java.io.IOException {
		for( edu.cmu.cs.dennisc.java.util.zip.DataSource dataSource : dataSources ) {
			edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.write( zos, dataSource );
		}
	}

	private static String getValidName( String name ) {
		//todo
		return name;
	}

	private static String generateEntryName( org.lgna.common.Resource resource, java.util.Set<String> usedEntryNames ) {
		String validFilename = getValidName( resource.getOriginalFileName() );
		final String DESIRED_DIRECTORY_NAME = "resources";
		int i = 1;
		while( true ) {
			StringBuffer sb = new StringBuffer();
			sb.append( DESIRED_DIRECTORY_NAME );
			if( i > 1 ) {
				sb.append( i );
			}
			sb.append( "/" );
			sb.append( validFilename );
			String potentialEntryName = sb.toString();
			if( usedEntryNames.contains( potentialEntryName ) ) {
				i += 1;
			} else {
				return potentialEntryName;
			}
		}
	}

	private static void writeResources( java.util.zip.ZipOutputStream zos, java.util.Set<org.lgna.common.Resource> resources ) throws java.io.IOException {
		if( resources.isEmpty() ) {
			//pass
		} else {
			org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
			org.w3c.dom.Element xmlRootElement = xmlDocument.createElement( "root" );
			xmlDocument.appendChild( xmlRootElement );
			synchronized( resources ) {
				java.util.Set<String> usedEntryNames = new java.util.HashSet<String>();
				for( org.lgna.common.Resource resource : resources ) {
					org.w3c.dom.Element xmlElement = xmlDocument.createElement( XML_RESOURCE_TAG_NAME );
					resource.encodeAttributes( xmlElement );
					java.util.UUID uuid = resource.getId();
					assert uuid != null;

					xmlElement.setAttribute( XML_RESOURCE_CLASSNAME_ATTRIBUTE, resource.getClass().getName() );
					xmlElement.setAttribute( XML_RESOURCE_UUID_ATTRIBUTE, uuid.toString() );

					String entryName = generateEntryName( resource, usedEntryNames );
					usedEntryNames.add( entryName );
					xmlElement.setAttribute( XML_RESOURCE_ENTRY_NAME_ATTRIBUTE, entryName );
					xmlRootElement.appendChild( xmlElement );
				}
			}
			writeXML( xmlDocument, zos, RESOURCES_ENTRY_NAME );
			synchronized( resources ) {
				java.util.Set<String> usedEntryNames = new java.util.HashSet<String>();
				for( org.lgna.common.Resource resource : resources ) {
					String entryName = generateEntryName( resource, usedEntryNames );
					usedEntryNames.add( entryName );
					edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.java.util.zip.ByteArrayDataSource( entryName, resource.getData() ) );
				}
			}
		}
	}

	public static void writeProject( java.io.OutputStream os, final org.lgna.project.Project project, edu.cmu.cs.dennisc.java.util.zip.DataSource... dataSources ) throws java.io.IOException {
		//	long getStreamStart = System.currentTimeMillis();
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( os );
		//	long getStreamEnd = System.currentTimeMillis();
		//	System.out.println( "Get stream time: " + ( ( getStreamEnd - getStreamStart ) * .001 ) );
		//	long startHeader = System.currentTimeMillis();
		writeVersions( zos );
		org.lgna.project.ast.AbstractType<?, ?, ?> programType = project.getProgramType();
		writeType( programType, zos, PROGRAM_TYPE_ENTRY_NAME );
		final java.util.Set<org.lgna.project.properties.PropertyKey<Object>> propertyKeys = project.getPropertyKeys();
		if( propertyKeys.isEmpty() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.java.util.zip.DataSource() {
				@Override
				public String getName() {
					return PROPERTIES_ENTRY_NAME;
				}

				@Override
				public void write( java.io.OutputStream os ) throws java.io.IOException {
					edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder binaryEncoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( os );
					binaryEncoder.encode( org.lgna.project.ProjectVersion.getCurrentVersionText() );
					binaryEncoder.encode( propertyKeys.size() );
					for( org.lgna.project.properties.PropertyKey<Object> propertyKey : propertyKeys ) {
						propertyKey.encodeIdAndValue( project, binaryEncoder );
					}
					binaryEncoder.flush();
				}
			} );
		}
		//	long endHeader = System.currentTimeMillis();
		//	System.out.println( "Header write time: " + ( ( endHeader - startHeader ) * .001 ) );

		//	long startData = System.currentTimeMillis();
		writeDataSources( zos, dataSources );
		//	long endData = System.currentTimeMillis();
		//	System.out.println( "Data write time: " + ( ( endData - startData ) * .001 ) );

		//	long startGetResources = System.currentTimeMillis();
		java.util.Set<org.lgna.common.Resource> resources = project.getResources();

		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.ResourceExpression> crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.ResourceExpression>( org.lgna.project.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		programType.crawl( crawler, org.lgna.project.ast.CrawlPolicy.COMPLETE );

		for( org.lgna.project.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			org.lgna.common.Resource resource = resourceExpression.resource.getValue();
			if( resources.contains( resource ) ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: adding missing resource", resource );
				resources.add( resource );
			}
		}

		//	long endGetResources = System.currentTimeMillis();
		//	System.out.println( "Get resources time: " + ( ( endGetResources - startGetResources ) * .001 ) );

		//	long startResources = System.currentTimeMillis();
		writeResources( zos, resources );
		//	long endResources = System.currentTimeMillis();
		//	System.out.println( "Resources write time: " + ( ( endResources - startResources ) * .001 ) );

		//	long startFlush = System.currentTimeMillis();
		zos.flush();
		//	long endFlush = System.currentTimeMillis();
		zos.close();
		//	long endClose = System.currentTimeMillis();
		//	System.out.println( "Flush time: " + ( ( endFlush - startFlush ) * .001 ) );
		//	System.out.println( "Close time: " + ( ( endClose - endFlush ) * .001 ) );
	}

	public static void writeProject( java.io.File file, org.lgna.project.Project project, edu.cmu.cs.dennisc.java.util.zip.DataSource... dataSources ) throws java.io.IOException {
		//	long startDir = System.currentTimeMillis();
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		//	long endDir = System.currentTimeMillis();
		//	System.out.println( "Create directories time: " + ( ( endDir - startDir ) * .001 ) );

		writeProject( new java.io.FileOutputStream( file ), project, dataSources );
	}

	public static void writeProject( String path, org.lgna.project.Project project, edu.cmu.cs.dennisc.java.util.zip.DataSource... dataSources ) throws java.io.IOException {
		writeProject( new java.io.File( path ), project, dataSources );
	}

	public static void writeType( java.io.OutputStream os, org.lgna.project.ast.AbstractType<?, ?, ?> type, edu.cmu.cs.dennisc.java.util.zip.DataSource... dataSources ) throws java.io.IOException {
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( os );
		writeVersions( zos );
		writeType( type, zos, TYPE_ENTRY_NAME );
		writeDataSources( zos, dataSources );

		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.ResourceExpression> crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.ResourceExpression>( org.lgna.project.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		type.crawl( crawler, org.lgna.project.ast.CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
		java.util.Set<org.lgna.common.Resource> resources = new java.util.HashSet<org.lgna.common.Resource>();
		for( org.lgna.project.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			resources.add( resourceExpression.resource.getValue() );
		}
		writeResources( zos, resources );

		zos.flush();
		zos.close();
	}

	public static void writeType( java.io.File file, org.lgna.project.ast.AbstractType<?, ?, ?> type, edu.cmu.cs.dennisc.java.util.zip.DataSource... dataSources ) throws java.io.IOException {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		writeType( new java.io.FileOutputStream( file ), type, dataSources );
	}

	public static void writeType( String path, org.lgna.project.ast.AbstractType<?, ?, ?> type, edu.cmu.cs.dennisc.java.util.zip.DataSource... dataSources ) throws java.io.IOException {
		writeType( new java.io.File( path ), type, dataSources );
	}

	public static <N extends org.lgna.project.ast.Node> N decodeNode( org.lgna.project.Project project, edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		java.util.UUID id = binaryDecoder.decodeId();
		return (N)org.lgna.project.ProgramTypeUtilities.lookupNode( project, id );
	}

	public static void encodeNode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, org.lgna.project.ast.Node node ) {
		binaryEncoder.encode( node.getId() );
	}
}
