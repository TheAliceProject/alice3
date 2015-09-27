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
package org.lgna.story.resourceutilities;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.alice.nonfree.NebulousStoryApi;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;

import edu.cmu.cs.dennisc.java.io.FileUtilities;

public enum StorytellingResources {
	INSTANCE;

	private static final String ALICE_RESOURCE_DIRECTORY_PREF_KEY = "ALICE_RESOURCE_DIRECTORY_PREF_KEY";
	static final String GALLERY_DIRECTORY_PREF_KEY = "GALLERY_DIRECTORY_PREF_KEY";

	private static final String ALICE_RESOURCE_INSTALL_PATH = "assets/alice";

	private List<Class<? extends org.lgna.story.resources.ModelResource>> aliceClassesLoaded = null;

	private List<URLClassLoader> resourceClassLoaders;
	private static final java.io.FileFilter DIR_FILE_FILTER = new java.io.FileFilter() {
		@Override
		public boolean accept( java.io.File file ) {
			return file.isDirectory();
		}
	};

	public static File getGalleryDirectory( java.io.File dir ) {
		if( dir.exists() && dir.isDirectory() ) {
			File[] dirs = FileUtilities.listDescendants( dir, DIR_FILE_FILTER, 4 ); //only search a limited depth to avoid massive spidering
			for( File subDir : dirs ) {
				String galleryDir = getGalleryPathFromResourcePath( subDir.getAbsolutePath() );
				if( galleryDir != null ) {
					return new File( galleryDir );
				}
			}
		}
		return null;
	}

	public static File getGalleryRootDirectory() {
		//		File rootGallery = getPathFromProperties( new String[] { "org.alice.ide.rootDirectory", "user.dir" }, new String[] { "application/gallery" } );
		//		if( ( rootGallery != null ) && rootGallery.exists() ) {
		//			return rootGallery;
		//		}
		//		return null;
		return org.lgna.story.implementation.StoryApiDirectoryUtilities.getModelGalleryDirectory();
	}

	//	private static java.io.File getPathFromProperties( String[] propertyKeys, String[] subPaths ) {
	//		for( String propertyKey : propertyKeys ) {
	//			for( String subPath : subPaths ) {
	//				java.io.File rv = new java.io.File( System.getProperty( propertyKey ), subPath );
	//				if( rv.exists() ) {
	//					return rv;
	//				}
	//			}
	//		}
	//		return null;
	//	}

	static File findResourcePath( String relativePath ) {
		File rootGallery = getGalleryRootDirectory();
		if( ( rootGallery != null ) && rootGallery.exists() ) {
			File path = new File( rootGallery, relativePath );
			if( path.exists() ) {
				return path;
			}
		}
		return null;
	}

	private static String getGalleryPathFromResourcePath( String resourcePath ) {
		if( resourcePath != null ) {
			int resourceIndex = -1;
			if( NebulousStoryApi.nonfree.getNebulousResourceInstallPath() != null ) {
				resourceIndex = resourcePath.lastIndexOf( NebulousStoryApi.nonfree.getNebulousResourceInstallPath() );
			}
			if( resourceIndex == -1 ) {
				resourceIndex = resourcePath.lastIndexOf( ALICE_RESOURCE_INSTALL_PATH );
			}
			if( resourceIndex != -1 ) {
				resourcePath = resourcePath.substring( 0, resourceIndex );
				while( resourcePath.endsWith( "/" ) ) {
					resourcePath = resourcePath.substring( 0, resourcePath.length() - 1 );
				}
				java.io.File galleryDir = new java.io.File( resourcePath );
				if( galleryDir.exists() ) {
					return resourcePath;
				}
			}
		}
		return null;
	}

	private static String[] getGalleryPathsFromResourcePath( String resourcePath ) {
		if( resourcePath != null ) {

			resourcePath = resourcePath.replace( '\\', '/' );
			String[] resourcePaths = resourcePath.split( PATH_SEPARATOR );
			List<String> galleryPaths = new ArrayList<String>( resourcePaths.length );
			for( String path : resourcePaths ) {
				String galleryPath = getGalleryPathFromResourcePath( path );
				if( ( galleryPath != null ) && !galleryPaths.contains( galleryPath ) ) {
					galleryPaths.add( galleryPath );
				}
			}

			return galleryPaths.toArray( new String[ galleryPaths.size() ] );
		}
		return null;
	}

	private static String getPreference( String key, String def ) {
		final boolean IS_IGNORING_PREFERENCES = false;
		if( IS_IGNORING_PREFERENCES ) {
			return null;
		} else {
			java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
			return rv.get( key, def );
		}
	}

	static File[] getDirsFromPref( String key, String relativeDir ) {
		String dir = getPreference( key, "" );
		if( ( dir != null ) && ( dir.length() > 0 ) ) {
			String[] splitDir = dir.split( PATH_SEPARATOR );
			File[] fileDirs = new File[ splitDir.length ];
			for( int i = 0; i < splitDir.length; i++ ) {
				fileDirs[ i ] = new File( splitDir[ i ] + relativeDir );
			}
			return fileDirs;
		}
		return null;
	}

	static String makeDirectoryPreferenceString( String[] dirs ) {
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < dirs.length; i++ ) {
			if( i != 0 ) {
				sb.append( PATH_SEPARATOR );
			}
			sb.append( dirs[ i ] );
		}
		return sb.toString();
	}

	private File[] getAliceDirsFromGalleryPref() {
		return getDirsFromPref( GALLERY_DIRECTORY_PREF_KEY, ALICE_RESOURCE_INSTALL_PATH );
	}

	public void setAliceResourceDirs( String[] dirs ) {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		String dirsString = makeDirectoryPreferenceString( dirs );
		rv.put( ALICE_RESOURCE_DIRECTORY_PREF_KEY, dirsString );
		String[] galleryDir = getGalleryPathsFromResourcePath( dirsString );
		if( galleryDir != null ) {
			setGalleryResourceDirs( galleryDir );
		}
	}

	public File[] getAliceDirsFromPref() {
		File[] dirs = getDirsFromPref( ALICE_RESOURCE_DIRECTORY_PREF_KEY, "" );
		if( dirs != null ) {
			return dirs;
		} else {
			return getAliceDirsFromGalleryPref();
		}
	}

	public void setGalleryResourceDirs( String[] dirs ) {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( GALLERY_DIRECTORY_PREF_KEY, makeDirectoryPreferenceString( dirs ) );
	}

	private static final String PATH_SEPARATOR = System.getProperty( "path.separator" );

	public File[] getGalleryDirsFromPref() {
		return getDirsFromPref( GALLERY_DIRECTORY_PREF_KEY, "" );
	}

	private List<File> findAliceResources() {
		File alicePath = findResourcePath( ALICE_RESOURCE_INSTALL_PATH );
		if( alicePath != null ) {
			ResourcePathManager.addPath( ResourcePathManager.MODEL_RESOURCE_KEY, alicePath );
			return ResourcePathManager.getPaths( ResourcePathManager.MODEL_RESOURCE_KEY );
		} else {
			LinkedList<File> directoryFromSavedPreference = new LinkedList<File>();
			File[] resourceDirs = getAliceDirsFromPref();

			if( resourceDirs != null ) {
				for( File resourceDir : resourceDirs ) {
					directoryFromSavedPreference.add( resourceDir );
				}
			}
			return directoryFromSavedPreference;
		}
	}

	private StorytellingResources() {
	}

	private static String getAliceResourceClassName( String resourcePath ) {
		String className = resourcePath.replace( '/', '.' );
		className = className.replace( '\\', '.' );
		int lastDot = className.lastIndexOf( "." );
		String baseName = className.substring( 0, lastDot );
		if( baseName.startsWith( "." ) ) {
			baseName = baseName.substring( 1 );
		}
		baseName += AliceResourceClassUtilities.RESOURCE_SUFFIX;
		return baseName;
	}

	public static java.util.Map<File, List<String>> getClassNamesFromResources( File... resourceFiles ) {
		java.util.HashMap<File, List<String>> rv = new java.util.HashMap<File, List<String>>();
		for( File resourceFile : resourceFiles ) {
			try {
				if( resourceFile.isDirectory() ) {
					File[] xmlFiles = FileUtilities.listDescendants( resourceFile, "xml" );
					for( File xmlFile : xmlFiles ) {
						if( !xmlFile.getName().contains( "$" ) ) {
							String relativePath = xmlFile.getAbsolutePath().substring( resourceFile.getAbsolutePath().length() );
							String baseName = getAliceResourceClassName( relativePath );
							if( !rv.containsKey( resourceFile ) ) {
								rv.put( resourceFile, new LinkedList<String>() );
							}
							rv.get( resourceFile ).add( baseName );
						}
					}
				} else {
					ZipFile zip = new ZipFile( resourceFile );
					Enumeration<? extends ZipEntry> entries = zip.entries();
					while( entries.hasMoreElements() ) {
						ZipEntry entry = entries.nextElement();
						if( entry.getName().endsWith( ".xml" ) && !entry.getName().contains( "$" ) ) {
							String baseName = getAliceResourceClassName( entry.getName() );

							if( !rv.containsKey( resourceFile ) ) {
								rv.put( resourceFile, new LinkedList<String>() );
							}
							rv.get( resourceFile ).add( baseName );
						} else {
							if( entry.getName().endsWith( ".xml" ) ) {
								System.out.println( "NOT ADDING CLASS: " + entry.getName() );
							}
						}
					}
				}
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
		return rv;
	}

	public List<String> getClassNamesFromResourceFiles( File... resourceFiles ) {
		List<String> classNames = new LinkedList<String>();
		java.util.Map<File, List<String>> classNameMap = getClassNamesFromResources( resourceFiles );
		for( java.util.Map.Entry<File, List<String>> entry : classNameMap.entrySet() ) {
			for( String className : entry.getValue() ) {
				classNames.add( className );
			}
		}
		return classNames;
	}

	public List<Class<? extends org.lgna.story.resources.ModelResource>> loadClassesFromResourceFiles( List<String> classNames, File... resourceFiles ) {
		List<Class<? extends org.lgna.story.resources.ModelResource>> classes = new LinkedList<Class<? extends org.lgna.story.resources.ModelResource>>();
		try {
			URL[] urlArray = new URL[ resourceFiles.length ];
			for( int i = 0; i < resourceFiles.length; i++ ) {
				urlArray[ i ] = resourceFiles[ i ].toURI().toURL();
			}
			URLClassLoader cl = new URLClassLoader( urlArray, null );
			for( String className : classNames ) {
				try {
					Class<?> cls = cl.loadClass( className );
					if( org.lgna.story.resources.ModelResource.class.isAssignableFrom( cls ) ) {
						//TEST
						Field[] fields = cls.getDeclaredFields();
						Method[] methods = cls.getDeclaredMethods();

						Field[] fields2 = cls.getFields();
						Method[] methods2 = cls.getMethods();

						classes.add( (Class<? extends org.lgna.story.resources.ModelResource>)cls );
					}
				} catch( Throwable cnfe ) {

					try {
						Class<?> cls = ClassLoader.getSystemClassLoader().loadClass( className );
						if( org.lgna.story.resources.ModelResource.class.isAssignableFrom( cls ) ) {
							classes.add( (Class<? extends org.lgna.story.resources.ModelResource>)cls );
						}
					} catch( ClassNotFoundException cnfe2 ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "FAILED TO LOAD GALLERY CLASS: " + className );
					}
				}
			}
			if( this.resourceClassLoaders == null ) {
				this.resourceClassLoaders = new LinkedList<URLClassLoader>();
			}
			this.resourceClassLoaders.add( cl );

		} catch( Exception e ) {
			e.printStackTrace();
		}
		return classes;
	}

	public List<Class<? extends org.lgna.story.resources.ModelResource>> loadResourcesFromFiles( File... resourceFiles ) {
		List<Class<? extends org.lgna.story.resources.ModelResource>> classes = new LinkedList<Class<? extends org.lgna.story.resources.ModelResource>>();

		List<String> classNames = new LinkedList<String>();
		LinkedList<URL> urls = new LinkedList<URL>();

		java.util.Map<File, List<String>> classNameMap = getClassNamesFromResources( resourceFiles );

		for( java.util.Map.Entry<File, List<String>> entry : classNameMap.entrySet() ) {
			try {
				urls.add( entry.getKey().toURI().toURL() );
				for( String className : entry.getValue() ) {
					classNames.add( className );
				}
			} catch( java.net.MalformedURLException e ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "Failed to load resources from jar: " + entry.getKey() );
			}
		}
		try {
			URL[] urlArray = urls.toArray( new URL[ urls.size() ] );
			URLClassLoader cl = new URLClassLoader( urlArray );

			for( String className : classNames ) {
				try {
					Class<?> cls = cl.loadClass( className );
					if( org.lgna.story.resources.ModelResource.class.isAssignableFrom( cls ) ) {
						classes.add( (Class<? extends org.lgna.story.resources.ModelResource>)cls );
					}
				} catch( ClassNotFoundException cnfe ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "FAILED TO LOAD GALLERY CLASS: " + className );
				}
			}
			if( this.resourceClassLoaders == null ) {
				this.resourceClassLoaders = new LinkedList<URLClassLoader>();
			}
			this.resourceClassLoaders.add( cl );

		} catch( Exception e ) {
			e.printStackTrace();
		}
		return classes;
	}

	public List<Class<? extends org.lgna.story.resources.ModelResource>> getAndLoadModelResourceClasses( List<File> resourcePaths ) {
		List<File> resourceFiles = new ArrayList<File>();
		List<Class<? extends org.lgna.story.resources.ModelResource>> galleryClasses = new LinkedList<Class<? extends org.lgna.story.resources.ModelResource>>();
		for( File modelPath : resourcePaths ) {
			if( modelPath.exists() ) {
				if( modelPath.isDirectory() ) {
					java.util.Collections.addAll( resourceFiles, FileUtilities.listFiles( modelPath, "jar" ) );
					java.util.Collections.addAll( resourceFiles, FileUtilities.listDirectories( modelPath ) );
				} else {
					resourceFiles.add( modelPath );
				}
			}
		}
		File[] resourceFileArray = resourceFiles.toArray( new File[ resourceFiles.size() ] );
		List<String> classNames = this.getClassNamesFromResourceFiles( resourceFileArray );
		galleryClasses = this.loadClassesFromResourceFiles( classNames, resourceFileArray );
		return galleryClasses;
	}

	public void getGalleryLocationFromUser() {
		FindResourcesPanel.getInstance().show( null );
		if( FindResourcesPanel.getInstance().getGalleryDir() != null ) {
			String userSpecifiedGalleryDir = FindResourcesPanel.getInstance().getGalleryDir().getAbsolutePath();
			String[] dirArray = { userSpecifiedGalleryDir };
			setGalleryResourceDirs( dirArray );
		}
	}

	//	//DEBUG
	//	static
	//	{
	////		//DEBUG ONLY
	////		//CLEAR DIR PREFS
	//		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
	//		rv.put( ALICE_RESOURCE_DIRECTORY_PREF_KEY, "" );
	//		rv.put( GALLERY_DIRECTORY_PREF_KEY, "" );
	//	}

	private void clearAliceResourceInfo() {
		ResourcePathManager.clearPaths( ResourcePathManager.MODEL_RESOURCE_KEY );
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( ALICE_RESOURCE_DIRECTORY_PREF_KEY, "" );
		rv.put( GALLERY_DIRECTORY_PREF_KEY, "" );

	}

	/*package-private*/List<Class<? extends org.lgna.story.resources.ModelResource>> findAndLoadAliceResourcesIfNecessary() {
		if( this.aliceClassesLoaded == null ) {
			List<File> resourcePaths = ResourcePathManager.getPaths( ResourcePathManager.MODEL_RESOURCE_KEY );
			if( resourcePaths.size() == 0 ) {
				resourcePaths = findAliceResources();
			}
			this.aliceClassesLoaded = this.getAndLoadModelResourceClasses( resourcePaths );
			if( aliceClassesLoaded.size() == 0 ) {
				//Clear previously cached info
				clearAliceResourceInfo();
				File galleryDir = FindResourcesPanel.getInstance().getGalleryDir();
				if( galleryDir == null ) {
					FindResourcesPanel.getInstance().show( null );
					galleryDir = FindResourcesPanel.getInstance().getGalleryDir();
				}
				if( galleryDir != null ) {
					//Save the directory to the preference
					String[] dirArray = { galleryDir.getAbsolutePath() };
					setGalleryResourceDirs( dirArray );
					//Try finding the resources again
					resourcePaths = findAliceResources();
					this.aliceClassesLoaded = this.getAndLoadModelResourceClasses( resourcePaths );
				}
			}
			if( this.aliceClassesLoaded.size() == 0 ) {
				//No resources were found
				//Clear the cached data and display an error
				clearAliceResourceInfo();
				StringBuilder sb = new StringBuilder();
				sb.append( "Cannot find the Alice gallery resources." );
				if( ( resourcePaths == null ) || ( resourcePaths.size() == 0 ) ) {
					sb.append( "\nNo gallery directories were detected. Make sure Alice is properly installed and has been run at least once." );
				} else {
					sb.append( "\nFailed to locate the resources in:" );
					String separator = "\n   ";
					for( File path : resourcePaths ) {
						sb.append( separator + "'" + path + "'" );
					}
					String phrase = resourcePaths.size() > 1 ? "these directories exist" : "this directory exists";
					sb.append( "\nVerify that " + phrase + " and verify that Alice is properly installed." );
				}
				javax.swing.JOptionPane.showMessageDialog( null, sb.toString() );
			} else {
				String[] galleryDirs = new String[ resourcePaths.size() ];
				for( int i = 0; i < resourcePaths.size(); i++ ) {
					File galleryFile = resourcePaths.get( i );
					if( galleryFile.isDirectory() ) {
						galleryDirs[ i ] = galleryFile.getAbsolutePath();
					} else {
						galleryDirs[ i ] = galleryFile.getParentFile().getAbsolutePath();
					}
				}
				setAliceResourceDirs( galleryDirs );
			}
		}
		return this.aliceClassesLoaded;
	}

	public URL getAliceResource( String resourceString ) {
		if( resourceString.contains( "ı" ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( resourceString );
			resourceString = resourceString.replaceAll( "ı", "i" );
		}
		this.findAndLoadAliceResourcesIfNecessary();
		assert this.resourceClassLoaders != null;
		URL foundResource = null;
		for( URLClassLoader cl : this.resourceClassLoaders ) {
			foundResource = cl.findResource( resourceString );
			if( foundResource != null ) {
				break;
			} else {
				//edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "cannot find resource for:", resourceString );
			}
		}
		return foundResource;
	}

	public InputStream getAliceResourceAsStream( String resourceString ) {
		this.findAndLoadAliceResourcesIfNecessary();
		assert this.resourceClassLoaders != null;
		InputStream foundResource = null;
		for( URLClassLoader cl : this.resourceClassLoaders ) {
			foundResource = cl.getResourceAsStream( resourceString );
			if( foundResource != null ) {
				break;
			}
		}
		return foundResource;
	}
}
