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
package edu.cmu.cs.dennisc.java.io;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Strings;
import edu.cmu.cs.dennisc.pattern.Criterion;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class FileUtilities {
	private static File s_defaultDirectory = null;

	public static Comparator<File> createComparator() {
		return new Comparator<File>() {
			private int compareFileNames( File fileA, File fileB ) {
				return fileA.getName().compareToIgnoreCase( fileB.getName() );
			}

			@Override
			public int compare( File fileA, File fileB ) {
				if( fileA != null ) {
					if( fileB != null ) {
						if( fileA.isDirectory() ) {
							if( fileB.isDirectory() ) {
								return this.compareFileNames( fileA, fileB );
							} else {
								return -1;
							}
						} else {
							if( fileB.isDirectory() ) {
								return -1;
							} else {
								return this.compareFileNames( fileA, fileB );
							}
						}
					} else {
						return 1;
					}
				} else {
					if( fileB != null ) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		};
	}

	public static File getDefaultDirectory() {
		if( s_defaultDirectory != null ) {
			//pass
		} else {
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
			s_defaultDirectory = fileSystemView.getDefaultDirectory();
		}
		return s_defaultDirectory;
	}

	public static File getUserDirectory() {
		File defaultDirectory = getDefaultDirectory();
		File rv;
		if( SystemUtilities.isWindows() ) {
			rv = defaultDirectory.getParentFile();
		} else {
			rv = defaultDirectory;
		}
		return rv;
	}

	public static boolean isValidFile( File file ) {
		if( file != null ) {
			try {
				file.getCanonicalPath();
				return true;
			} catch( IOException ioe ) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isValidPath( String path ) {
		return isValidFile( path != null ? new File( path ) : null );
	}

	public static String getCanonicalPathIfPossible( File file ) {
		if( file != null ) {
			try {
				return file.getCanonicalPath();
			} catch( IOException ioe ) {
				return file.getAbsolutePath();
			}
		} else {
			return null;
		}
	}

	public static String fixFilenameIfNecessary( String filename ) {
		StringBuffer sb = new StringBuffer( filename.length() );
		for( int i = 0; i < filename.length(); i++ ) {
			char c = filename.charAt( i );
			switch( c ) {
			case '|':
				sb.append( '_' );
				break;
			default:
				sb.append( c );
			}
		}
		return sb.toString();
	}

	public static boolean isExtensionAmoung( String filename, String... extensionCandidates ) {
		String extension = getExtension( filename );
		for( String extensionCandidate : extensionCandidates ) {
			assert extensionCandidate.charAt( 0 ) != '.';
			if( Strings.equalsIgnoreCase( extension, extensionCandidate ) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean isExtensionAmoung( File file, String... extensionCandidates ) {
		return isExtensionAmoung( file.getName(), extensionCandidates );
	}

	public static String getExtension( String filename ) {
		String extension = null;
		if( filename != null ) {
			int index = filename.lastIndexOf( '.' );
			if( index != -1 ) {
				extension = filename.substring( index + 1 );
			}
		}
		return extension;
	}

	public static String getExtension( File file ) {
		if( file != null ) {
			return getExtension( file.getName() );
		} else {
			return null;
		}
	}

	public static String getBaseName( String filename ) {
		String basename = null;
		if( filename != null ) {
			int index = filename.lastIndexOf( '.' );
			if( index != -1 ) {
				basename = filename.substring( 0, index );
			} else {
				basename = filename;
			}

			index = basename.lastIndexOf( '/' );
			if( index != -1 ) {
				basename = basename.substring( index + 1 );
			}
			index = basename.lastIndexOf( '\\' );
			if( index != -1 ) {
				basename = basename.substring( index + 1 );
			}

		}
		return basename;
	}

	public static String getBaseName( File file ) {
		if( file != null ) {
			return getBaseName( file.getName() );
		} else {
			return null;
		}
	}

	public static boolean exists( String path ) {
		File file = new File( path );
		return file.exists();
	}

	public static boolean existsAndHasLengthGreaterThanZero( File file ) {
		return file.exists() && ( file.length() > 0 );
	}

	public static boolean existsAndHasLengthGreaterThanZero( String path ) {
		return existsAndHasLengthGreaterThanZero( new File( path ) );
	}

	public static boolean createParentDirectoriesIfNecessary( File file ) {
		return file.getParentFile().mkdirs();
	}

	public static boolean createParentDirectoriesIfNecessary( String path ) {
		return createParentDirectoriesIfNecessary( new File( path ) );
	}

	public static File getRelativeFile( File srcFile, int upCount, String... childPaths ) {
		File rv = srcFile;
		for( int i = 0; i < upCount; i++ ) {
			rv = rv.getParentFile();
		}
		for( String childPath : childPaths ) {
			rv = new File( rv, childPath );
		}
		return rv;
	}

	public static File getRelativeFile( File src, String... childPaths ) {
		return getRelativeFile( src, 0, childPaths );
	}

	public static File getRelativeFile( String srcPath, int upCount, String... childPaths ) {
		return getRelativeFile( new File( srcPath ), upCount, childPaths );
	}

	public static File getRelativeFile( String srcPath, String... childPaths ) {
		return getRelativeFile( srcPath, 0, childPaths );
	}

	public static File getAnalogousFile( File src, File srcRoot, File dstRoot ) {
		String srcPath = src.getAbsolutePath();
		String srcRootPath = srcRoot.getAbsolutePath();

		assert srcPath.startsWith( srcRootPath );
		String srcSubPath = srcPath.substring( srcRootPath.length() );

		return new File( dstRoot, srcSubPath );
	}

	public static File getAnalogousFile( File src, File srcRoot, File dstRoot, String srcExtension, String dstExtension ) {
		File f = getAnalogousFile( src, srcRoot, dstRoot );
		File directory = f.getParentFile();
		String name = f.getName();
		assert name.endsWith( srcExtension );
		return new File( directory, name.substring( 0, name.length() - srcExtension.length() ) + dstExtension );
	}

	public static File[] listFiles( File root, FileFilter fileFilter ) {
		File[] rv = root.listFiles( fileFilter );
		if( rv != null ) {
			//pass
		} else {
			rv = new File[ 0 ];
		}
		return rv;
	}

	public static File[] listDirectories( File root ) {
		return listFiles( root, new FileFilter() {
			@Override
			public boolean accept( File file ) {
				return file.isDirectory();
			}
		} );
	}

	public static File[] listDirectories( String rootPath ) {
		return listDirectories( new File( rootPath ) );
	}

	public static File[] listFiles( File root, String extension ) {
		assert extension.charAt( 0 ) != '.';
		final String ext = extension;
		return listFiles( root, new FileFilter() {
			@Override
			public boolean accept( File file ) {
				return file.isFile() && ext.equalsIgnoreCase( getExtension( file ) );
			}
		} );
	}

	public static File[] listFiles( String rootPath, String extension ) {
		return listFiles( new File( rootPath ), extension );
	}

	private static void appendDescendants( List<File> descendants, File dir, FileFilter fileFilter, int depth ) {
		File[] files = dir.listFiles( fileFilter );
		if( files != null ) {
			Collections.addAll( descendants, files );
		}

		if( depth != 0 ) {
			if( depth != -1 ) {
				depth--;
			}
			File[] dirs = dir.listFiles( new FileFilter() {
				@Override
				public boolean accept( File file ) {
					return file.isDirectory();
				}
			} );
			if( dirs != null ) {
				for( File childDir : dirs ) {
					appendDescendants( descendants, childDir, fileFilter, depth );
				}
			}
		}
	}

	public static File[] listDescendants( File root, FileFilter fileFilter, int depth ) {
		List<File> list = Lists.newLinkedList();
		appendDescendants( list, root, fileFilter, depth );
		return ArrayUtilities.createArray( list, File.class );
	}

	public static File[] listDescendants( File root, FileFilter fileFilter ) {
		return listDescendants( root, fileFilter, -1 );
	}

	public static File[] listDescendants( String rootPath, FileFilter fileFilter ) {
		return listDescendants( new File( rootPath ), fileFilter );
	}

	public static File[] listDescendants( String rootPath, FileFilter fileFilter, int depth ) {
		return listDescendants( new File( rootPath ), fileFilter, depth );
	}

	public static File[] listDescendants( File root, final String extension ) {
		assert root.exists() : root;
		assert extension != null;
		assert extension.charAt( 0 ) != '.';
		return listDescendants( root, new FileFilter() {
			@Override
			public boolean accept( File file ) {
				if( file.isFile() ) {
					if( extension != null ) {
						return extension.equalsIgnoreCase( getExtension( file ) );
					} else {
						return true;
					}
				} else {
					return false;
				}
			}
		} );
	}

	public static File[] listDescendants( String rootPath, String extension ) {
		return listDescendants( new File( rootPath ), extension );
	}

	public static boolean isDescendantOf( File file, File possibleAncestor ) {
		while( true ) {
			File parent = file.getParentFile();
			if( parent == null ) {
				return false;
			} else if( parent.equals( possibleAncestor ) ) {
				return true;
			} else {
				file = parent;
			}
		}
	}

	public static boolean isAncestorOf( File file, File possibleDescendant ) {
		return isDescendantOf( possibleDescendant, file );
	}

	public static FilenameFilter createFilenameFilter( final String extension ) {
		return new FilenameFilter() {
			@Override
			public boolean accept( File dir, String name ) {
				return name.toUpperCase().endsWith( extension.toUpperCase() );
			}
		};
	}

	public static FileFilter createFileWithExtensionFilter( final String extension ) {
		return new FileFilter() {
			@Override
			public boolean accept( File file ) {
				if( file.isFile() ) {
					return file.getName().toUpperCase().endsWith( extension.toUpperCase() );
				} else {
					return false;
				}
			}
		};
	}

	public static FileFilter createDirectoryFilter() {
		return new FileFilter() {
			@Override
			public boolean accept( File file ) {
				return file.isDirectory();
			}
		};
	}

	public static void copyFile( File in, File out ) throws IOException {
		createParentDirectoriesIfNecessary( out );
		FileChannel inChannel = new FileInputStream( in ).getChannel();
		FileChannel outChannel = new FileOutputStream( out ).getChannel();
		inChannel.transferTo( 0, inChannel.size(), outChannel );
		inChannel.close();
		outChannel.close();
	}

	public static void copyFile( String inPath, String outPath ) throws IOException {
		copyFile( new File( inPath ), new File( outPath ) );
	}

	private static void _copyDirectory( File in, File out, Criterion<File> criterion ) throws IOException {
		if( ( criterion == null ) || criterion.accept( in ) ) {
			if( in.isDirectory() ) {
				for( String filename : in.list() ) {
					_copyDirectory( new File( in, filename ), new File( out, filename ), criterion );
				}
			} else {
				copyFile( in, out );
			}
		}
	}

	public static void copyDirectory( File in, File out, Criterion<File> criterion ) throws IOException {
		createParentDirectoriesIfNecessary( out );
		_copyDirectory( in, out, criterion );
	}

	public static void copyDirectory( File in, File out ) throws IOException {
		copyDirectory( in, out, null );
	}

	public static void copyDirectory( String inPath, String outPath ) throws IOException {
		copyDirectory( new File( inPath ), new File( outPath ) );
	}

	public static void delete( File file ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "deleting", file );
		if( file.exists() ) {
			if( file.isDirectory() ) {
				for( File f : file.listFiles() ) {
					delete( f );
				}
			}
			boolean isSuccessful = file.delete();
			assert isSuccessful : file;
			//			if( isSuccessful ) {
			//				//pass 
			//			} else {
			//				throw new RuntimeException( file.getAbsolutePath() );
			//			}
			assert file.exists() == false : file;
			//			if( file.exists() ) {
			//				throw new RuntimeException( file.getAbsolutePath() );
			//			}
		}
	}

	public static void delete( String path ) {
		delete( new File( path ) );
	}

	public static File toFile( URL url ) {
		if( url != null ) {
			try {
				return toFile( url.toURI() );
			} catch( URISyntaxException urise ) {
				throw new RuntimeException( urise );
			}
		} else {
			return null;
		}
	}

	public static File toFile( URI uri ) {
		if( uri != null ) {
			return new File( uri );
		} else {
			return null;
		}
	}

	public static URI toUri( File file ) {
		if( file != null ) {
			return file.toURI();
		} else {
			return null;
		}
	}

	public static URL toUrl( File file ) {
		if( file != null ) {
			try {
				return file.toURL();
			} catch( MalformedURLException murle ) {
				throw new RuntimeException( murle );
			}
		} else {
			return null;
		}
	}
}
