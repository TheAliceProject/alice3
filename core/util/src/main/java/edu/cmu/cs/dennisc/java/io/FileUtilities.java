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
package edu.cmu.cs.dennisc.java.io;

/**
 * @author Dennis Cosgrove
 */
public class FileUtilities {
	private static java.io.File s_defaultDirectory = null;

	public static java.util.Comparator<java.io.File> createComparator() {
		return new java.util.Comparator<java.io.File>() {
			private int compareFileNames( java.io.File fileA, java.io.File fileB ) {
				return fileA.getName().compareToIgnoreCase( fileB.getName() );
			}

			@Override
			public int compare( java.io.File fileA, java.io.File fileB ) {
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

	public static java.io.File getDefaultDirectory() {
		if( s_defaultDirectory != null ) {
			//pass
		} else {
			javax.swing.filechooser.FileSystemView fileSystemView = javax.swing.filechooser.FileSystemView.getFileSystemView();
			s_defaultDirectory = fileSystemView.getDefaultDirectory();
		}
		return s_defaultDirectory;
	}

	public static java.io.File getUserDirectory() {
		java.io.File defaultDirectory = getDefaultDirectory();
		java.io.File rv;
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			rv = defaultDirectory.getParentFile();
		} else {
			rv = defaultDirectory;
		}
		return rv;
	}

	public static boolean isValidFile( java.io.File file ) {
		if( file != null ) {
			try {
				file.getCanonicalPath();
				return true;
			} catch( java.io.IOException ioe ) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isValidPath( String path ) {
		return isValidFile( path != null ? new java.io.File( path ) : null );
	}

	public static String getCanonicalPathIfPossible( java.io.File file ) {
		if( file != null ) {
			try {
				return file.getCanonicalPath();
			} catch( java.io.IOException ioe ) {
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
			if( edu.cmu.cs.dennisc.java.util.Strings.equalsIgnoreCase( extension, extensionCandidate ) ) {
				return true;
			}
		}
		return false;
	}

	public static boolean isExtensionAmoung( java.io.File file, String... extensionCandidates ) {
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

	public static String getExtension( java.io.File file ) {
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

	public static String getBaseName( java.io.File file ) {
		if( file != null ) {
			return getBaseName( file.getName() );
		} else {
			return null;
		}
	}

	public static boolean exists( String path ) {
		java.io.File file = new java.io.File( path );
		return file.exists();
	}

	public static boolean existsAndHasLengthGreaterThanZero( java.io.File file ) {
		return file.exists() && ( file.length() > 0 );
	}

	public static boolean existsAndHasLengthGreaterThanZero( String path ) {
		return existsAndHasLengthGreaterThanZero( new java.io.File( path ) );
	}

	public static boolean createParentDirectoriesIfNecessary( java.io.File file ) {
		return file.getParentFile().mkdirs();
	}

	public static boolean createParentDirectoriesIfNecessary( String path ) {
		return createParentDirectoriesIfNecessary( new java.io.File( path ) );
	}

	public static java.io.File getRelativeFile( java.io.File srcFile, int upCount, String... childPaths ) {
		java.io.File rv = srcFile;
		for( int i = 0; i < upCount; i++ ) {
			rv = rv.getParentFile();
		}
		for( String childPath : childPaths ) {
			rv = new java.io.File( rv, childPath );
		}
		return rv;
	}

	public static java.io.File getRelativeFile( java.io.File src, String... childPaths ) {
		return getRelativeFile( src, 0, childPaths );
	}

	public static java.io.File getRelativeFile( String srcPath, int upCount, String... childPaths ) {
		return getRelativeFile( new java.io.File( srcPath ), upCount, childPaths );
	}

	public static java.io.File getRelativeFile( String srcPath, String... childPaths ) {
		return getRelativeFile( srcPath, 0, childPaths );
	}

	public static java.io.File getAnalogousFile( java.io.File src, java.io.File srcRoot, java.io.File dstRoot ) {
		String srcPath = src.getAbsolutePath();
		String srcRootPath = srcRoot.getAbsolutePath();

		assert srcPath.startsWith( srcRootPath );
		String srcSubPath = srcPath.substring( srcRootPath.length() );

		return new java.io.File( dstRoot, srcSubPath );
	}

	public static java.io.File getAnalogousFile( java.io.File src, java.io.File srcRoot, java.io.File dstRoot, String srcExtension, String dstExtension ) {
		java.io.File f = getAnalogousFile( src, srcRoot, dstRoot );
		java.io.File directory = f.getParentFile();
		String name = f.getName();
		assert name.endsWith( srcExtension );
		return new java.io.File( directory, name.substring( 0, name.length() - srcExtension.length() ) + dstExtension );
	}

	public static java.io.File[] listFiles( java.io.File root, java.io.FileFilter fileFilter ) {
		java.io.File[] rv = root.listFiles( fileFilter );
		if( rv != null ) {
			//pass
		} else {
			rv = new java.io.File[ 0 ];
		}
		return rv;
	}

	public static java.io.File[] listDirectories( java.io.File root ) {
		return listFiles( root, new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
				return file.isDirectory();
			}
		} );
	}

	public static java.io.File[] listDirectories( String rootPath ) {
		return listDirectories( new java.io.File( rootPath ) );
	}

	public static java.io.File[] listFiles( java.io.File root, String extension ) {
		assert extension.charAt( 0 ) != '.';
		final String ext = extension;
		return listFiles( root, new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
				return file.isFile() && ext.equalsIgnoreCase( getExtension( file ) );
			}
		} );
	}

	public static java.io.File[] listFiles( String rootPath, String extension ) {
		return listFiles( new java.io.File( rootPath ), extension );
	}

	private static void appendDescendants( java.util.List<java.io.File> descendants, java.io.File dir, java.io.FileFilter fileFilter, int depth ) {
		java.io.File[] files = dir.listFiles( fileFilter );
		if( files != null ) {
			for( java.io.File childFile : files ) {
				descendants.add( childFile );
			}
		}

		if( depth != 0 ) {
			if( depth != -1 ) {
				depth--;
			}
			java.io.File[] dirs = dir.listFiles( new java.io.FileFilter() {
				@Override
				public boolean accept( java.io.File file ) {
					return file.isDirectory();
				}
			} );
			if( dirs != null ) {
				for( java.io.File childDir : dirs ) {
					appendDescendants( descendants, childDir, fileFilter, depth );
				}
			}
		}
	}

	public static java.io.File[] listDescendants( java.io.File root, java.io.FileFilter fileFilter, int depth ) {
		java.util.List<java.io.File> list = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		appendDescendants( list, root, fileFilter, depth );
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( list, java.io.File.class );
	}

	public static java.io.File[] listDescendants( java.io.File root, java.io.FileFilter fileFilter ) {
		return listDescendants( root, fileFilter, -1 );
	}

	public static java.io.File[] listDescendants( String rootPath, java.io.FileFilter fileFilter ) {
		return listDescendants( new java.io.File( rootPath ), fileFilter );
	}

	public static java.io.File[] listDescendants( String rootPath, java.io.FileFilter fileFilter, int depth ) {
		return listDescendants( new java.io.File( rootPath ), fileFilter, depth );
	}

	public static java.io.File[] listDescendants( java.io.File root, final String extension ) {
		assert root.exists() : root;
		assert extension != null;
		assert extension.charAt( 0 ) != '.';
		return listDescendants( root, new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
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

	public static java.io.File[] listDescendants( String rootPath, String extension ) {
		return listDescendants( new java.io.File( rootPath ), extension );
	}

	public static boolean isDescendantOf( java.io.File file, java.io.File possibleAncestor ) {
		while( true ) {
			java.io.File parent = file.getParentFile();
			if( parent == null ) {
				return false;
			} else if( parent.equals( possibleAncestor ) ) {
				return true;
			} else {
				file = parent;
			}
		}
	}

	public static boolean isAncestorOf( java.io.File file, java.io.File possibleDescendant ) {
		return isDescendantOf( possibleDescendant, file );
	}

	public static java.io.FilenameFilter createFilenameFilter( final String extension ) {
		return new java.io.FilenameFilter() {
			@Override
			public boolean accept( java.io.File dir, String name ) {
				return name.endsWith( extension );
			}
		};
	}

	public static java.io.FileFilter createFileWithExtensionFilter( final String extension ) {
		return new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
				if( file.isFile() ) {
					return file.getName().endsWith( extension );
				} else {
					return false;
				}
			}
		};
	}

	public static java.io.FileFilter createDirectoryFilter() {
		return new java.io.FileFilter() {
			@Override
			public boolean accept( java.io.File file ) {
				return file.isDirectory();
			}
		};
	}

	public static void copyFile( java.io.File in, java.io.File out ) throws java.io.IOException {
		createParentDirectoriesIfNecessary( out );
		java.nio.channels.FileChannel inChannel = new java.io.FileInputStream( in ).getChannel();
		java.nio.channels.FileChannel outChannel = new java.io.FileOutputStream( out ).getChannel();
		inChannel.transferTo( 0, inChannel.size(), outChannel );
		inChannel.close();
		outChannel.close();
	}

	public static void copyFile( String inPath, String outPath ) throws java.io.IOException {
		copyFile( new java.io.File( inPath ), new java.io.File( outPath ) );
	}

	private static void _copyDirectory( java.io.File in, java.io.File out, edu.cmu.cs.dennisc.pattern.Criterion<java.io.File> criterion ) throws java.io.IOException {
		if( ( criterion == null ) || criterion.accept( in ) ) {
			if( in.isDirectory() ) {
				for( String filename : in.list() ) {
					_copyDirectory( new java.io.File( in, filename ), new java.io.File( out, filename ), criterion );
				}
			} else {
				copyFile( in, out );
			}
		}
	}

	public static void copyDirectory( java.io.File in, java.io.File out, edu.cmu.cs.dennisc.pattern.Criterion<java.io.File> criterion ) throws java.io.IOException {
		createParentDirectoriesIfNecessary( out );
		_copyDirectory( in, out, criterion );
	}

	public static void copyDirectory( java.io.File in, java.io.File out ) throws java.io.IOException {
		copyDirectory( in, out, null );
	}

	public static void copyDirectory( String inPath, String outPath ) throws java.io.IOException {
		copyDirectory( new java.io.File( inPath ), new java.io.File( outPath ) );
	}

	public static void delete( java.io.File file ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "deleting", file );
		if( file.exists() ) {
			if( file.isDirectory() ) {
				for( java.io.File f : file.listFiles() ) {
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
		delete( new java.io.File( path ) );
	}

	public static java.io.File toFile( java.net.URL url ) {
		if( url != null ) {
			try {
				return toFile( url.toURI() );
			} catch( java.net.URISyntaxException urise ) {
				throw new RuntimeException( urise );
			}
		} else {
			return null;
		}
	}

	public static java.io.File toFile( java.net.URI uri ) {
		if( uri != null ) {
			return new java.io.File( uri );
		} else {
			return null;
		}
	}

	public static java.net.URI toUri( java.io.File file ) {
		if( file != null ) {
			return file.toURI();
		} else {
			return null;
		}
	}

	public static java.net.URL toUrl( java.io.File file ) {
		if( file != null ) {
			try {
				return file.toURL();
			} catch( java.net.MalformedURLException murle ) {
				throw new RuntimeException( murle );
			}
		} else {
			return null;
		}
	}
}
