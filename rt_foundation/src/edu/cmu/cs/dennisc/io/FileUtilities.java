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
package edu.cmu.cs.dennisc.io;

/**
 * @author Dennis Cosgrove
 */
public class FileUtilities {
	private static java.io.File s_defaultDirectory = null;
	public static java.io.File getDefaultDirectory() {
		if( s_defaultDirectory != null ) {
			//pass
		} else {
			javax.swing.filechooser.FileSystemView fileSystemView = javax.swing.filechooser.FileSystemView.getFileSystemView();
			s_defaultDirectory = fileSystemView.getDefaultDirectory();
		}
		return s_defaultDirectory;
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
	
	public static boolean isExtensionAmoung( String filename, edu.cmu.cs.dennisc.equivalence.CaseSensitivityPolicy caseSensitivityPolicy, String... extensionCandidates ) {
		String extension = getExtension( filename );
		for( String extensionCandidate : extensionCandidates ) {
			assert extensionCandidate.charAt( 0 ) != '.';
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( extension, extensionCandidate, caseSensitivityPolicy ) ) {
				return true;
			}
		}
		return false;
	}
	public static boolean isExtensionAmoung( java.io.File file, edu.cmu.cs.dennisc.equivalence.CaseSensitivityPolicy caseSensitivityPolicy, String... extensionCandidates ) {
		return isExtensionAmoung( file.getName(), caseSensitivityPolicy, extensionCandidates );
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
		return file.exists() && file.length() > 0;
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
		 for( int i=0; i<upCount; i++ ) {
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
		return new java.io.File( directory, name.substring( 0, name.length()-srcExtension.length() ) + dstExtension ); 
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
			public boolean accept( java.io.File file ) {
				return file.isFile() && ext.equalsIgnoreCase( getExtension( file ) );
			}
		} );
	}
	public static java.io.File[] listFiles( String rootPath, String extension ) {
		return listFiles( new java.io.File( rootPath ), extension );
	}

	private static java.util.Vector<java.io.File> updateDescendants( java.util.Vector<java.io.File> rv, java.io.File dir, java.io.FileFilter fileFilter ) {
		java.io.File[] files = dir.listFiles( fileFilter );
		if( files != null ) {
			for( java.io.File childFile : files ) {
				rv.add( childFile );
			}
		}

		java.io.File[] dirs = dir.listFiles( new java.io.FileFilter() {
			public boolean accept( java.io.File file ) {
				return file.isDirectory();
			}
		} );
		if( files != dirs ) {
			for( java.io.File childDir : dirs ) {
				updateDescendants( rv, childDir, fileFilter );
			}
		}
		return rv;
	}

	public static java.io.File[] listDescendants( java.io.File root, java.io.FileFilter fileFilter ) {
		java.util.Vector<java.io.File> v = updateDescendants( new java.util.Vector<java.io.File>(), root, fileFilter );
		java.io.File[] rv = new java.io.File[ v.size() ];
		v.copyInto( rv );
		return rv;
	}
	public static java.io.File[] listDescendants( String rootPath, java.io.FileFilter fileFilter ) {
		return listDescendants( new java.io.File( rootPath ), fileFilter );
	}
	public static java.io.File[] listDescendants( java.io.File root, final String extension ) {
		assert root.exists() : root;
		assert extension.charAt( 0 ) != '.';
		return listDescendants( root, new java.io.FileFilter() {
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
			public boolean accept( java.io.File dir, java.lang.String name ) {
				return name.endsWith( extension );
			}
		};
	}
	public static java.io.FileFilter createFileFilter( final String extension ) {
		return new java.io.FileFilter() {
			public boolean accept( java.io.File file ) {
				return file.getName().endsWith( extension );
			}
		};
	}
}
