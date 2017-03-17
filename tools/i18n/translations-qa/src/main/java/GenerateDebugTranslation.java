import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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

/**
 * @author dculyba
 */
public class GenerateDebugTranslation {

	private static PathMatcher PROPERTIES_MATCHER = FileSystems.getDefault().getPathMatcher( "glob:**.properties" );

	private static boolean isSrcFolder( Path path ) {
		if( Files.exists( path.resolve( "main" ) ) && Files.exists( path.resolve( "l10n-alice" ) ) ) {
			return true;
		}
		return false;
	}

	private static boolean isPropertiesFile( Path path ) {
		boolean matches = PROPERTIES_MATCHER.matches( path.getFileName() );
		return matches;
	}

	private static boolean shouldSkipValue( String value ) {
		if( value.contains( "VK_" ) ) {
			return true;
		}
		return false;
	}

	private static void copyPropertiesFile( Path srcPath, Path localizePath, Path propertiesPath, String suffix, String customKey, boolean deleteOverride ) {
		Path relativePropertyPath = srcPath.relativize( propertiesPath );
		Path localizePropertyPath = localizePath.resolve( relativePropertyPath );
		Path newLocalizePropertyPath = localizePropertyPath.resolveSibling( relativePropertyPath.getFileName().toString().replace( ".properties", suffix + ".properties" ) );

		if( deleteOverride ) {
			if( Files.exists( newLocalizePropertyPath ) ) {
				try {
					Files.delete( newLocalizePropertyPath );
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
			return;
		}

		try {

			ResourceBundle rb = new PropertyResourceBundle( Files.newInputStream( propertiesPath ) );
			//			ResourceBundle rb = ResourceBundle.getBundle( relativePropertyPath.toString(), Locale.getDefault(), loader );
			if( !Files.exists( newLocalizePropertyPath ) ) {
				newLocalizePropertyPath.toFile().getParentFile().mkdirs();
				Files.createFile( newLocalizePropertyPath );
			}
			BufferedWriter writer = Files.newBufferedWriter( newLocalizePropertyPath );
			Enumeration<String> keys = rb.getKeys();
			while( keys.hasMoreElements() ) {
				String key = keys.nextElement();
				String value = rb.getString( key );
				if( !shouldSkipValue( value ) ) {
					writer.write( key + " = " + customKey + value + customKey + "\r\n" );
				}
			}
			writer.close();
		} catch( Exception e ) {
			e.printStackTrace();
		}

		System.out.println( "Wrote new file: " + newLocalizePropertyPath );
	}

	public static String stripQuotes( String toStrip ) {
		if( ( toStrip.startsWith( "\"" ) && toStrip.endsWith( "\"" ) ) || ( toStrip.startsWith( "\'" ) && toStrip.endsWith( "\'" ) ) ) {
			toStrip = toStrip.substring( 1, toStrip.length() - 1 );
		}
		return toStrip;
	}

	public static void main( String[] args ) throws Exception {
		boolean deleteOverride = false;
		String exportPath = null;
		String generatedFileSuffix = "_en_CA";
		String customKey = "#";
		int i = 0;
		while( i < args.length ) {
			if( args[ i ].equalsIgnoreCase( "-d" ) ) {
				deleteOverride = true;
				i++;
			} else if( args[ i ].equalsIgnoreCase( "-x" ) ) {
				if( ( i + 1 ) < args.length ) {
					exportPath = args[ i + 1 ];
					i += 2;
				}
			} else if( args[ i ].equalsIgnoreCase( "-s" ) ) {
				if( ( i + 1 ) < args.length ) {
					generatedFileSuffix = args[ i + 1 ];
					generatedFileSuffix = stripQuotes( generatedFileSuffix );
					i += 2;
				} else {
					generatedFileSuffix = "";
					i += 1;
				}
			} else if( args[ i ].equalsIgnoreCase( "-k" ) ) {
				if( ( i + 1 ) < args.length ) {
					customKey = args[ i + 1 ];
					customKey = stripQuotes( customKey );
					i += 2;
				} else {
					customKey = "";
					i += 1;
				}
			}
		}

		File repoRoot = new File( System.getProperty( "user.dir" ), "../../../" );
		repoRoot = repoRoot.getCanonicalFile();
		List<Path> srcPaths = new ArrayList<Path>();

		try ( Stream<Path> stream = Files.walk( Paths.get( repoRoot.getAbsolutePath() ) ) ) {
			stream.filter( path -> isSrcFolder( path ) ).forEach( path -> srcPaths.add( path ) );
		}

		int fileCount = 0;
		for( Path rootPath : srcPaths ) {
			List<Path> propertyPaths = new ArrayList<Path>();
			Path srcPath = rootPath.resolve( "main" );
			Path localizePath;
			if( exportPath != null ) {
				localizePath = Paths.get( exportPath );
			} else {
				localizePath = rootPath.resolve( "l10n-alice" );
			}
			try ( Stream<Path> stream = Files.walk( srcPath ) ) {
				stream.filter( path -> isPropertiesFile( path ) ).forEach( path -> propertyPaths.add( path ) );
			}

			for( Path propertyPath : propertyPaths ) {
				copyPropertiesFile( srcPath, localizePath, propertyPath, generatedFileSuffix, customKey, deleteOverride );
				fileCount++;
			}
		}

		if( deleteOverride ) {
			System.out.println( "Deleted generated translation files" );
		} else {
			System.out.println();
			System.out.println( "Wrote " + fileCount + " properties files" );
		}

	}
}
