package org.lgna.story.resourceutilities;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.lgna.project.ast.JavaType;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.javax.swing.models.TreeNode;
import edu.cmu.cs.dennisc.nebulous.Manager;

public class StorytellingResources {
	private static class SingletonHolder {
		private static StorytellingResources instance = new StorytellingResources();
	}

	public static StorytellingResources getInstance() {
		return SingletonHolder.instance;
	}

	private static final String NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY = "NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY";
	private static final String ALICE_RESOURCE_DIRECTORY_PREF_KEY = "ALICE_RESOURCE_DIRECTORY_PREF_KEY";
	private static final String GALLERY_DIRECTORY_PREF_KEY = "GALLERY_DIRECTORY_PREF_KEY";

	private static final String NEBULOUS_RESOURCE_INSTALL_PATH = "assets/sims";
	private static final String ALICE_RESOURCE_INSTALL_PATH = "assets/alice";

	private ModelResourceTree galleryTree;
	private final List<File> simsPathsLoaded = new LinkedList<File>();
	private List<Class<? extends org.lgna.story.resources.ModelResource>> aliceClassesLoaded = null;

	private URLClassLoader resourceClassLoader;
	private static final java.io.FileFilter DIR_FILE_FILTER = new java.io.FileFilter() {
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

	private static File findResourcePath( String relativePath ) {
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
			resourcePath = resourcePath.replace( '\\', '/' );
			int resourceIndex = -1;
			resourceIndex = resourcePath.lastIndexOf( NEBULOUS_RESOURCE_INSTALL_PATH );
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

	private static String getPreference( String key, String def ) {
		final boolean IS_IGNORING_PREFERENCES = false;
		if( IS_IGNORING_PREFERENCES ) {
			return null;
		} else {
			java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
			return rv.get( key, def );
		}
	}

	private File getNebulousDirFromGalleryPref() {
		String dir = getPreference( GALLERY_DIRECTORY_PREF_KEY, "" );
		if( ( dir != null ) && ( dir.length() > 0 ) ) {
			return new File( dir, NEBULOUS_RESOURCE_INSTALL_PATH );
		}
		return null;
	}

	private File getAliceDirFromGalleryPref() {
		String dir = getPreference( GALLERY_DIRECTORY_PREF_KEY, "" );
		if( ( dir != null ) && ( dir.length() > 0 ) ) {
			return new File( dir, ALICE_RESOURCE_INSTALL_PATH );
		}
		return null;
	}

	public void setNebulousResourceDir( String dir ) {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, dir );
	}

	public File getNebulousDirFromPref() {
		String dir = getPreference( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, "" );
		if( ( dir != null ) && ( dir.length() > 0 ) ) {
			return new File( dir );
		}
		else {
			return getNebulousDirFromGalleryPref();
		}
	}

	public void setAliceResourceDir( String dir ) {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( ALICE_RESOURCE_DIRECTORY_PREF_KEY, dir );
		String galleryDir = getGalleryPathFromResourcePath( dir );
		if( galleryDir != null ) {
			setGalleryResourceDir( galleryDir );
		}
	}

	public File getAliceDirFromPref() {
		String dir = getPreference( ALICE_RESOURCE_DIRECTORY_PREF_KEY, "" );
		if( ( dir != null ) && ( dir.length() > 0 ) ) {
			return new File( dir );
		}
		else {
			return getAliceDirFromGalleryPref();
		}
	}

	public void setGalleryResourceDir( String dir ) {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( GALLERY_DIRECTORY_PREF_KEY, dir );
	}

	public File getGalleryDirFromPref() {
		String dir = getPreference( GALLERY_DIRECTORY_PREF_KEY, "" );
		if( ( dir != null ) && ( dir.length() > 0 ) ) {
			return new File( dir );
		}
		return null;
	}

	private List<File> findSimsBundles() {
		File simsPath = findResourcePath( NEBULOUS_RESOURCE_INSTALL_PATH );
		if( simsPath != null ) {
			ResourcePathManager.addPath( ResourcePathManager.SIMS_RESOURCE_KEY, simsPath );
			return ResourcePathManager.getPaths( ResourcePathManager.SIMS_RESOURCE_KEY );
		} else {
			LinkedList<File> directoryFromSavedPreference = new LinkedList<File>();
			File resourceDir = getNebulousDirFromPref();
			if( resourceDir != null ) {
				directoryFromSavedPreference.add( resourceDir );
			}
			return directoryFromSavedPreference;
		}
	}

	private List<File> findAliceResources() {
		File alicePath = findResourcePath( ALICE_RESOURCE_INSTALL_PATH );
		if( alicePath != null ) {
			ResourcePathManager.addPath( ResourcePathManager.MODEL_RESOURCE_KEY, alicePath );
			return ResourcePathManager.getPaths( ResourcePathManager.MODEL_RESOURCE_KEY );
		}
		else {
			LinkedList<File> directoryFromSavedPreference = new LinkedList<File>();
			File resourceDir = getAliceDirFromPref();

			if( resourceDir != null ) {
				directoryFromSavedPreference.add( resourceDir );
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
			try
			{
				if( resourceFile.isDirectory() ) {
					File[] xmlFiles = FileUtilities.listDescendants( resourceFile, "xml" );
					for( File xmlFile : xmlFiles ) {
						if( !xmlFile.getName().contains( "$" ) )
						{
							String relativePath = xmlFile.getAbsolutePath().substring( resourceFile.getAbsolutePath().length() );
							String baseName = getAliceResourceClassName( relativePath );
							if( !rv.containsKey( resourceFile ) ) {
								rv.put( resourceFile, new LinkedList<String>() );
							}
							rv.get( resourceFile ).add( baseName );
						}
					}
				}
				else {
					ZipFile zip = new ZipFile( resourceFile );
					Enumeration<? extends ZipEntry> entries = zip.entries();
					while( entries.hasMoreElements() )
					{
						ZipEntry entry = entries.nextElement();
						if( entry.getName().endsWith( ".xml" ) && !entry.getName().contains( "$" ) )
						{
							String baseName = getAliceResourceClassName( entry.getName() );

							if( !rv.containsKey( resourceFile ) ) {
								rv.put( resourceFile, new LinkedList<String>() );
							}
							rv.get( resourceFile ).add( baseName );
						}
						else {
							if( entry.getName().endsWith( ".xml" ) )
							{
								System.out.println( "NOT ADDING CLASS: " + entry.getName() );
							}
						}
					}
				}
			} catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		return rv;
	}

	public List<Class<? extends org.lgna.story.resources.ModelResource>> loadResourcesFromFiles( File... resourceFiles )
	{
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
		try
		{
			URL[] urlArray = urls.toArray( new URL[ urls.size() ] );
			URLClassLoader cl = new URLClassLoader( urlArray );

			for( String className : classNames )
			{
				try {
					Class<?> cls = cl.loadClass( className );
					if( org.lgna.story.resources.ModelResource.class.isAssignableFrom( cls ) )
					{
						classes.add( (Class<? extends org.lgna.story.resources.ModelResource>)cls );
					}
				} catch( ClassNotFoundException cnfe ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "FAILED TO LOAD GALLERY CLASS: " + className );
				}
			}
			this.resourceClassLoader = cl;

		} catch( Exception e )
		{
			e.printStackTrace();
		}
		return classes;
	}

	public List<Class<? extends org.lgna.story.resources.ModelResource>> getAndLoadModelResourceClasses( List<File> resourcePaths )
	{
		List<Class<? extends org.lgna.story.resources.ModelResource>> galleryClasses = new LinkedList<Class<? extends org.lgna.story.resources.ModelResource>>();
		for( File modelPath : resourcePaths )
		{
			if( modelPath.exists() ) {
				try {
					if( modelPath.isDirectory() ) {
						File[] jarFiles = FileUtilities.listFiles( modelPath, "jar" );
						File[] dirs = FileUtilities.listDirectories( modelPath );
						File[] resourceFiles = ArrayUtilities.concatArrays( File.class, dirs, jarFiles );
						galleryClasses.addAll( this.loadResourcesFromFiles( resourceFiles ) );
					}
					else {
						galleryClasses.addAll( this.loadResourcesFromFiles( modelPath ) );
					}
				} catch( Exception e )
				{
					System.err.println( "Failed to load resources on path: '" + modelPath + "'" );
				}
			}
		}
		return galleryClasses;
	}

	public void getGalleryLocationFromUser() {
		FindResourcesPanel.getInstance().show( null );
		if( FindResourcesPanel.getInstance().getGalleryDir() != null ) {
			setGalleryResourceDir( FindResourcesPanel.getInstance().getGalleryDir().getAbsolutePath() );
		}
	}

	//	//DEBUG
	//	static 
	//	{
	////		//DEBUG ONLY
	////		//CLEAR DIR PREFS
	//		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
	//		rv.put( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, "" );
	//		rv.put( ALICE_RESOURCE_DIRECTORY_PREF_KEY, "" );
	//		rv.put( GALLERY_DIRECTORY_PREF_KEY, "" );
	//	}

	private void buildGalleryTreeWithJars( File... resourceJars ) {
		java.util.ArrayList<File> jarFiles = new java.util.ArrayList<File>();
		for( File resourceJar : resourceJars ) {
			jarFiles.add( resourceJar );
		}
		List<Class<? extends org.lgna.story.resources.ModelResource>> modelResourceClasses = this.getAndLoadModelResourceClasses( jarFiles );
		this.galleryTree = new ModelResourceTree( modelResourceClasses );
	}

	private void clearAliceResourceInfo()
	{
		ResourcePathManager.clearPaths( ResourcePathManager.MODEL_RESOURCE_KEY );
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( ALICE_RESOURCE_DIRECTORY_PREF_KEY, "" );
		rv.put( GALLERY_DIRECTORY_PREF_KEY, "" );

	}

	private void clearSimsResourceInfo()
	{
		ResourcePathManager.clearPaths( ResourcePathManager.SIMS_RESOURCE_KEY );
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, "" );
		rv.put( GALLERY_DIRECTORY_PREF_KEY, "" );

	}

	public void findAndLoadAliceResourcesIfNecessary() {
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
				if( galleryDir == null )
				{
					FindResourcesPanel.getInstance().show( null );
					galleryDir = FindResourcesPanel.getInstance().getGalleryDir();
				}
				if( galleryDir != null ) {
					//Save the directory to the preference
					setGalleryResourceDir( galleryDir.getAbsolutePath() );
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
			}
			else {
				File galleryFile = resourcePaths.get( 0 );
				if( galleryFile.isDirectory() ) {
					setAliceResourceDir( galleryFile.getAbsolutePath() );
				}
				else
				{
					setAliceResourceDir( galleryFile.getParentFile().getAbsolutePath() );
				}
			}
		}
	}

	private int loadSimsBundlesFromPaths( List<File> resourcePaths ) {
		int count = 0;
		for( File path : resourcePaths ) {
			if( path.exists() ) {
				for( java.io.File file : path.listFiles() ) {
					if( !simsPathsLoaded.contains( file ) ) {
						try {
							if( file.getName().endsWith( "txt" ) ) {
								//pass
							} else {
								edu.cmu.cs.dennisc.nebulous.Manager.addBundle( file );
								simsPathsLoaded.add( file );
								count++;
							}
						} catch( Throwable t ) {
							t.printStackTrace();
						}
					}
				}
			}
		}
		return count;
	}

	public void loadSimsBundles() {

		//DEBUG

		String DEBUG_rawPathValue = System.getProperty( "org.alice.ide.simsDebugResourcePath" );
		if( DEBUG_rawPathValue != null ) {
			java.io.File rawResourcePath = new File( DEBUG_rawPathValue );
			if( rawResourcePath.exists() ) {
				Manager.setRawResourcePath( rawResourcePath );
			}
		}

		List<File> resourcePaths = ResourcePathManager.getPaths( ResourcePathManager.SIMS_RESOURCE_KEY );
		if( resourcePaths.size() == 0 ) {
			resourcePaths = findSimsBundles();
		}
		int loaded = loadSimsBundlesFromPaths( resourcePaths );
		if( ( loaded == 0 ) && ( simsPathsLoaded.size() == 0 ) ) {
			//Clear previously cached info
			clearSimsResourceInfo();
			File galleryDir = FindResourcesPanel.getInstance().getGalleryDir();
			if( galleryDir == null )
			{
				FindResourcesPanel.getInstance().show( null );
				galleryDir = FindResourcesPanel.getInstance().getGalleryDir();
			}
			if( galleryDir != null ) {
				//Save the directory to the preference
				setGalleryResourceDir( galleryDir.getAbsolutePath() );
				//Try finding the resources again
				resourcePaths = findSimsBundles();
				loaded = loadSimsBundlesFromPaths( resourcePaths );
			}
		}
		if( ( loaded == 0 ) && ( simsPathsLoaded.size() == 0 ) ) {
			clearSimsResourceInfo();
			StringBuilder sb = new StringBuilder();
			sb.append( "Cannot find The Sims (TM) 2 Art Assets." );
			if( ( resourcePaths == null ) || ( resourcePaths.size() == 0 ) ) {
				sb.append( "\nNo gallery directories were detected. Make sure Alice is properly installed and has been run at least once." );
			} else {
				sb.append( "\nSearched in " );
				String separator = "";
				for( File path : resourcePaths ) {
					sb.append( separator + "'" + path + "'" );
					if( separator.length() == 0 ) {
						separator = ", ";
					}
				}
				String phrase = resourcePaths.size() > 1 ? "these directories exist" : "this directory exists";
				sb.append( "\nVerify that " + phrase + " and verify that Alice is properly installed." );
			}
			javax.swing.JOptionPane.showMessageDialog( null, sb.toString() );

		} else {
			File galleryFile = simsPathsLoaded.get( 0 );
			if( galleryFile.isDirectory() ) {
				setNebulousResourceDir( galleryFile.getAbsolutePath() );
			}
			else
			{
				setNebulousResourceDir( galleryFile.getParentFile().getAbsolutePath() );
			}
		}
	}

	public List<org.lgna.project.ast.JavaType> getTopLevelGalleryTypes() {
		List<ModelResourceTreeNode> rootNodes = this.getGalleryTreeInternal().getRootNodes();
		List<org.lgna.project.ast.JavaType> rootTypes = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
		for( ModelResourceTreeNode node : rootNodes ) {
			rootTypes.add( node.getUserType().getFirstEncounteredJavaType() );
		}
		return rootTypes;
	}

	public org.lgna.project.ast.JavaType getGalleryResourceParentFor( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		TreeNode<JavaType> child = this.getGalleryResourceTreeNodeForJavaType( type );
		if( child != null ) {
			ModelResourceTreeNode parent = (ModelResourceTreeNode)child.getParent();
			//Go up an extra level if the node we're returning is a node with a single child (this mirrors what is happening in getResourceChildren)
			if( ( parent != null ) && hasSingleLeafChild( parent ) ) {
				parent = (ModelResourceTreeNode)parent.getParent();
			}
			return parent.getResourceJavaType();
		} else {
			return null;
		}
	}

	private boolean hasSingleLeafChild( TreeNode<?> node ) {
		return ( ( node.getChildCount() == 1 ) && ( node.getChildAt( 0 ).getChildCount() == 0 ) );
	}

	public URL getAliceResource( String resourceString ) {
		this.findAndLoadAliceResourcesIfNecessary();
		assert this.resourceClassLoader != null;
		return this.resourceClassLoader.findResource( resourceString );
	}

	public ResourceBundle getLocalizationBundle( String bundleKey, Locale locale )
	{
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( bundleKey, locale, this.resourceClassLoader );
		if( resourceBundle != null ) {
			return resourceBundle;
		}
		return java.util.ResourceBundle.getBundle( bundleKey, locale );
	}

	public InputStream getAliceResourceAsStream( String resourceString ) {
		this.findAndLoadAliceResourcesIfNecessary();
		assert this.resourceClassLoader != null;
		return this.resourceClassLoader.getResourceAsStream( resourceString );
	}

	public List<org.lgna.project.ast.AbstractDeclaration> getGalleryResourceChildrenFor( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		//System.out.println( "Getting children for type: " + type );
		java.util.List<org.lgna.project.ast.AbstractDeclaration> toReturn = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
		TreeNode<JavaType> typeNode = this.getGalleryResourceTreeNodeForJavaType( type );
		if( typeNode != null ) {
			for( int i = 0; i < typeNode.getChildCount(); i++ ) {
				TreeNode<JavaType> child = typeNode.getChildAt( i );
				//If the child has a single leaf child, go down a level and return that
				if( hasSingleLeafChild( child ) ) {
					child = child.getChildAt( 0 );
				}
				ModelResourceTreeNode node = (ModelResourceTreeNode)child;
				if( node.isLeaf() && ( node.getJavaField() != null ) ) {
					//System.out.println( "  Returning field: " + node.getJavaField() );
					toReturn.add( node.getJavaField() );
				} else {
					//System.out.println( "  Returning type: " + node.getResourceJavaType() );
					toReturn.add( node.getResourceJavaType() );
				}
			}
		}
		return toReturn;
	}

	public void initializeGalleryTreeWithJars( File... resourceJars ) {
		this.buildGalleryTreeWithJars( resourceJars );
	}

	private ModelResourceTree getGalleryTreeInternal() {
		if( this.galleryTree == null ) {
			findAndLoadAliceResourcesIfNecessary();
			this.galleryTree = new ModelResourceTree( this.aliceClassesLoaded );
		}
		return this.galleryTree;
	}

	private ModelResourceTreeNode getGalleryResourceTreeNodeForJavaType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return this.getGalleryTreeInternal().getGalleryResourceTreeNodeForJavaType( type );
	}

	public ModelResourceTreeNode getGalleryResourceTreeNodeForUserType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return getGalleryTreeInternal().getGalleryResourceTreeNodeForUserType( type );
	}

	public ModelResourceTreeNode getGalleryTree() {
		return getGalleryTreeInternal().getTree();
	}

}
