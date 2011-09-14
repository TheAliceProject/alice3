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
package org.lgna.project.project;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProjectUtilities {
	private ProjectUtilities() {
	}

	public static java.util.Set< org.alice.virtualmachine.Resource > getReferencedResources( org.lgna.project.Project project ) {
		org.lgna.project.ast.AbstractType<?,?,?> programType = project.getProgramType();
		java.util.Set< org.alice.virtualmachine.Resource > resources = project.getResources();
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.ResourceExpression > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.ResourceExpression >( org.lgna.project.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		programType.crawl( crawler, true );
		
		java.util.Set< org.alice.virtualmachine.Resource > rv = new java.util.HashSet< org.alice.virtualmachine.Resource >();
		for( org.lgna.project.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			org.alice.virtualmachine.Resource resource = resourceExpression.resource.getValue();
			if( resources.contains( resource ) ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: adding missing resource", resource );
				resources.add( resource );
			}
			rv.add( resource );
		}
		return rv;
	}

	public static final String PROJECT_EXTENSION = "a3p";
	public static final String TYPE_EXTENSION = "a3c";
	private static String PROPERTIES_ENTRY_NAME = "properties.bin";
	private static String PROGRAM_TYPE_ENTRY_NAME = "programType.xml";
	private static String VERSION_ENTRY_NAME = "version.txt";
	private static String TYPE_ENTRY_NAME = "type.xml";
	private static String RESOURCES_ENTRY_NAME = "resources.xml";

	private static String XML_RESOURCE_TAG_NAME = "resource";

	private static String XML_RESOURCE_CLASSNAME_ATTRIBUTE = "className";
	private static String XML_RESOURCE_UUID_ATTRIBUTE = "uuid";
	private static String XML_RESOURCE_ENTRY_NAME_ATTRIBUTE = "entryName";

	public static java.io.File[] listProjectFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, PROJECT_EXTENSION );
	}
	public static java.io.File[] listTypeFiles( java.io.File directory ) {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, TYPE_EXTENSION );
	}

	private static String readVersion( java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		assert zipFile != null;
		java.util.zip.ZipEntry entry = zipFile.getEntry( VERSION_ENTRY_NAME );
		if( entry != null ) {
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
		} else {
			throw new java.io.IOException( zipFile.getName() + " does not contain entry " + VERSION_ENTRY_NAME );
		}
	}
	private static org.lgna.project.Project readProperties( org.lgna.project.Project rv, java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		assert zipFile != null;
		java.util.zip.ZipEntry entry = zipFile.getEntry( PROPERTIES_ENTRY_NAME );
		if( entry != null ) {
			java.io.BufferedInputStream bis = new java.io.BufferedInputStream( zipFile.getInputStream( entry ) );
			rv.readProperties( bis );
		}
		return rv;
	}
	private static org.w3c.dom.Document readXML( java.util.zip.ZipFile zipFile, String entryName ) throws java.io.IOException {
		assert zipFile != null;
		java.util.zip.ZipEntry entry = zipFile.getEntry( entryName );
		assert entry != null;
		return edu.cmu.cs.dennisc.xml.XMLUtilities.read( zipFile.getInputStream( entry ) );
	}

	private static org.lgna.project.ast.NamedUserType readType( java.util.zip.ZipFile zipFile, String entryName ) throws java.io.IOException {
		String version = readVersion( zipFile );
		org.w3c.dom.Document xmlDocument = readXML( zipFile, entryName );
		return (org.lgna.project.ast.NamedUserType)org.lgna.project.ast.AbstractNode.decode( xmlDocument, version );
	}
	private static java.util.Set< org.alice.virtualmachine.Resource > readResources( java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		java.util.Set< org.alice.virtualmachine.Resource > rv = new java.util.HashSet< org.alice.virtualmachine.Resource >();
		java.util.zip.ZipEntry zeResources = zipFile.getEntry( RESOURCES_ENTRY_NAME );
		if( zeResources != null ) {
			org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.read( zipFile.getInputStream( zeResources ) );
			java.util.List< org.w3c.dom.Element > xmlElements = edu.cmu.cs.dennisc.xml.XMLUtilities.getChildElementsByTagName( xmlDocument.getDocumentElement(), XML_RESOURCE_TAG_NAME );
			for( org.w3c.dom.Element xmlElement : xmlElements ) {
				String className = xmlElement.getAttribute( XML_RESOURCE_CLASSNAME_ATTRIBUTE );
				String uuidText = xmlElement.getAttribute( XML_RESOURCE_UUID_ATTRIBUTE );
				String entryName = xmlElement.getAttribute( XML_RESOURCE_ENTRY_NAME_ATTRIBUTE );
				if( className != null && uuidText != null && entryName != null ) {
					java.util.zip.ZipEntry zipEntry = zipFile.getEntry( entryName );
					byte[] data = edu.cmu.cs.dennisc.java.io.InputStreamUtilities.getBytes( zipFile.getInputStream( zipEntry ) );
					if( data != null ) {
						try {
							Class< ? extends org.alice.virtualmachine.Resource > resourceCls = (Class< ? extends org.alice.virtualmachine.Resource >)edu.cmu.cs.dennisc.java.lang.ClassUtilities.forName( className );
							org.alice.virtualmachine.Resource resource = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.valueOf( resourceCls, uuidText );
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

	public static org.lgna.project.Project readProject( java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		assert zipFile != null;
		org.lgna.project.ast.NamedUserType type = readType( zipFile, PROGRAM_TYPE_ENTRY_NAME );
		//todo
		java.util.Set< org.lgna.project.ast.NamedUserType > namedUserTypes = java.util.Collections.emptySet();
		java.util.Set< org.alice.virtualmachine.Resource > resources = readResources( zipFile );
		org.lgna.project.Project rv = new org.lgna.project.Project( type, namedUserTypes, resources );
		readProperties( rv, zipFile );
		return rv;
	}
	public static org.lgna.project.Project readProject( java.io.File file ) {
		assert file != null;
		assert file.exists();
		try {
			return readProject( new java.util.zip.ZipFile( file ) );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}
	public static org.lgna.project.Project readProject( String path ) {
		assert path != null;
		return readProject( new java.io.File( path ) );
	}
	public static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends org.lgna.project.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource > > readType( java.util.zip.ZipFile zipFile ) throws java.io.IOException {
		org.lgna.project.ast.AbstractType<?,?,?> type = readType( zipFile, TYPE_ENTRY_NAME );
		java.util.Set< org.alice.virtualmachine.Resource > resources = readResources( zipFile );
		return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( type, resources );
	}
	public static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends org.lgna.project.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource >> readType( java.io.File file ) {
		assert file != null;
		assert file.exists();
		try {
			return readType( new java.util.zip.ZipFile( file ) );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}
	public static edu.cmu.cs.dennisc.pattern.Tuple2< ? extends org.lgna.project.ast.AbstractType<?,?,?>, java.util.Set< org.alice.virtualmachine.Resource >> readType( String path ) {
		return readType( new java.io.File( path ) );
	}

	private static void writeVersion( java.util.zip.ZipOutputStream zos ) throws java.io.IOException {
		edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
			public String getName() {
				return VERSION_ENTRY_NAME;
			}
			public void write( java.io.OutputStream os ) throws java.io.IOException {
				os.write( org.lgna.project.Version.getCurrentVersionText().getBytes() );
			}
		} );
	}
	private static void writeXML( final org.w3c.dom.Document xmlDocument, java.util.zip.ZipOutputStream zos, final String entryName ) throws java.io.IOException {
		edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
			public String getName() {
				return entryName;
			}
			public void write( java.io.OutputStream os ) throws java.io.IOException {
				edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, os );
			}
		} );
	}
	private static void writeType( org.lgna.project.ast.AbstractType<?,?,?> type, java.util.zip.ZipOutputStream zos, String entryName ) throws java.io.IOException {
		writeXML( type.encode(), zos, entryName );
	}
	private static void writeDataSources( java.util.zip.ZipOutputStream zos, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		for( edu.cmu.cs.dennisc.zip.DataSource dataSource : dataSources ) {
			edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, dataSource );
		}
	}

	private static String getValidName( String name ) {
		//todo
		return name;
	}
	private static String generateEntryName( org.alice.virtualmachine.Resource resource, java.util.Set< String > usedEntryNames ) {
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
	private static void writeResources( java.util.zip.ZipOutputStream zos, java.util.Set< org.alice.virtualmachine.Resource > resources ) throws java.io.IOException {
		if( resources.isEmpty() ) {
			//pass
		} else {
			org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
			org.w3c.dom.Element xmlRootElement = xmlDocument.createElement( "root" );
			xmlDocument.appendChild( xmlRootElement );
			synchronized( resources ) {
				java.util.Set< String > usedEntryNames = new java.util.HashSet< String >();
				for( org.alice.virtualmachine.Resource resource : resources ) {
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
				java.util.Set< String > usedEntryNames = new java.util.HashSet< String >();
				for( org.alice.virtualmachine.Resource resource : resources ) {
					String entryName = generateEntryName( resource, usedEntryNames );
					usedEntryNames.add( entryName );
					edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.ByteArrayDataSource( entryName, resource.getData() ) );
				}
			}
		}
	}

	public static void writeProject( java.io.OutputStream os, final org.lgna.project.Project project, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( os );
		writeVersion( zos );
		org.lgna.project.ast.AbstractType<?,?,?> programType = project.getProgramType();
		writeType( programType, zos, PROGRAM_TYPE_ENTRY_NAME );
		if( project.getPropertyKeySet().isEmpty() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.zip.ZipUtilities.write( zos, new edu.cmu.cs.dennisc.zip.DataSource() {
				public String getName() {
					return PROPERTIES_ENTRY_NAME;
				}
				public void write( java.io.OutputStream os ) throws java.io.IOException {
					assert os instanceof java.io.BufferedOutputStream;
					project.writeProperties( (java.io.BufferedOutputStream)os );
				}
			} );
		}
		writeDataSources( zos, dataSources );

		java.util.Set< org.alice.virtualmachine.Resource > resources = project.getResources();

		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.ResourceExpression > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.ResourceExpression >( org.lgna.project.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		programType.crawl( crawler, true );

		for( org.lgna.project.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			org.alice.virtualmachine.Resource resource = resourceExpression.resource.getValue();
			if( resources.contains( resource ) ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: adding missing resource", resource );
				resources.add( resource );
			}
		}

		writeResources( zos, resources );

		zos.flush();
		zos.close();
	}
	public static void writeProject( java.io.File file, org.lgna.project.Project project, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		writeProject( new java.io.FileOutputStream( file ), project, dataSources );
	}

	public static void writeProject( String path, org.lgna.project.Project project, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		writeProject( new java.io.File( path ), project, dataSources );
	}

	public static void writeType( java.io.OutputStream os, org.lgna.project.ast.AbstractType<?,?,?> type, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( os );
		writeVersion( zos );
		writeType( type, zos, TYPE_ENTRY_NAME );
		writeDataSources( zos, dataSources );

		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.ResourceExpression > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.ResourceExpression >( org.lgna.project.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		type.crawl( crawler, false );
		java.util.Set< org.alice.virtualmachine.Resource > resources = new java.util.HashSet< org.alice.virtualmachine.Resource >();
		for( org.lgna.project.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			resources.add( resourceExpression.resource.getValue() );
		}
		writeResources( zos, resources );

		zos.flush();
		zos.close();
	}
	public static void writeType( java.io.File file, org.lgna.project.ast.AbstractType<?,?,?> type, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		writeType( new java.io.FileOutputStream( file ), type, dataSources );
	}
	public static void writeType( String path, org.lgna.project.ast.AbstractType<?,?,?> type, edu.cmu.cs.dennisc.zip.DataSource... dataSources ) throws java.io.IOException {
		writeType( new java.io.File( path ), type, dataSources );
	}

	public static <N extends org.lgna.project.ast.Node > N decodeNode( org.lgna.project.Project project, edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		java.util.UUID id = binaryDecoder.decodeId();
		return (N)lookupNode( project, id );
	}
	public static void encodeNode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, org.lgna.project.ast.Node node ) {
		binaryEncoder.encode( node.getUUID() );
	}
	
	public static <N extends org.lgna.project.ast.Node > N lookupNode( org.lgna.project.Project project, final java.util.UUID id ) {
		final org.lgna.project.ast.Node[] buffer = { null };
		org.lgna.project.ast.NamedUserType programType = project.getProgramType();
		edu.cmu.cs.dennisc.pattern.Crawler crawler = new edu.cmu.cs.dennisc.pattern.Crawler() {
			public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
				if( crawlable instanceof org.lgna.project.ast.Node ) {
					org.lgna.project.ast.Node node = (org.lgna.project.ast.Node)crawlable;
					if( id.equals( node.getUUID() ) ) {
						buffer[ 0 ] = node;
					}
				}
			}
		};
		programType.crawl( crawler, true );
		return (N)buffer[ 0 ];
	}
	public static <R extends org.alice.virtualmachine.Resource > R lookupResource( org.lgna.project.Project project, java.util.UUID id ) {
		for( org.alice.virtualmachine.Resource resource : project.getResources() ) {
			if( resource.getId() == id ) {
				return (R)resource;
			}
		}
		return null;
	}

//	public static java.util.List< org.lgna.project.ast.NamedUserType > getTypes( org.lgna.project.Project project ) {
//		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.NamedUserType > crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.NamedUserType.class );
//		final org.lgna.project.ast.AbstractType<?,?,?> programType = project.getProgramType();
//		programType.crawl( crawler, true );
//		return crawler.getList();
//	}
}
