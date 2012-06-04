package org.lgna.story.resourceutilities;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.alice.ide.ResourcePathManager;
import org.lgna.project.ast.JavaType;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.javax.swing.models.TreeNode;

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
	private final List< File > simsPathsLoaded = new LinkedList< File >();
	private List< Class< ? extends org.lgna.story.resources.ModelResource >> aliceClassesLoaded = null;
	
	private URLClassLoader classLoader;
	private static final java.io.FileFilter DIR_FILE_FILTER = new java.io.FileFilter() {
		public boolean accept( java.io.File file ) {
			return file.isDirectory();
		}
	};
	
	public static File getGalleryDirectory(java.io.File dir) {
		if (dir.exists() && dir.isDirectory()) {
			File[] dirs = FileUtilities.listDescendants(dir, DIR_FILE_FILTER, 4 ); //only search a limited depth to avoid massive spidering
			for (File subDir : dirs) {
				String galleryDir = getGalleryPathFromResourcePath(subDir.getAbsolutePath());
				if (galleryDir != null) {
					return new File(galleryDir);
				}
			}
		}
		return null;
	}
	
	private static java.io.File getPathFromProperties( String[] propertyKeys, String[] subPaths ) {
		for( String propertyKey : propertyKeys ) {
			for( String subPath : subPaths ) {
				java.io.File rv = new java.io.File( System.getProperty( propertyKey ), subPath );
				if( rv.exists() ) {
					return rv;
				}
			}
		}
		return null;
	} 

	private File findResourcePath( String relativePath ) {
		File rootGallery = getPathFromProperties( new String[] { "org.alice.ide.IDE.install.dir", "user.dir" }, new String[] { "gallery" } );
		if( rootGallery != null && rootGallery.exists() ) {
			File path = new File( rootGallery, relativePath );
			if( path.exists() ) {
				return path;
			}
		}
		return null;
	}
	
	private static String getGalleryPathFromResourcePath(String resourcePath) {
		if (resourcePath != null) {
			resourcePath = resourcePath.replace('\\', '/');
			int resourceIndex = -1;
			resourceIndex = resourcePath.lastIndexOf(NEBULOUS_RESOURCE_INSTALL_PATH);
			if (resourceIndex == -1) {
				resourceIndex = resourcePath.lastIndexOf(ALICE_RESOURCE_INSTALL_PATH);
			}
			if (resourceIndex != -1) {
				resourcePath = resourcePath.substring(0, resourceIndex);
				while (resourcePath.endsWith("/")) {
					resourcePath = resourcePath.substring(0, resourcePath.length()-1);
				}
				java.io.File galleryDir = new java.io.File(resourcePath);
				if (galleryDir.exists()) {
					return resourcePath;
				}
			}
		}
		return null;
	}
	
	private File getNebulousDirFromGalleryPref() {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		String dir = rv.get( GALLERY_DIRECTORY_PREF_KEY, "" );
		if( dir != null && dir.length() > 0 ) {
			return new File( dir, NEBULOUS_RESOURCE_INSTALL_PATH );
		}
		return null;
	}
	
	private File getAliceDirFromGalleryPref() {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		String dir = rv.get( GALLERY_DIRECTORY_PREF_KEY, "" );
		if( dir != null && dir.length() > 0 ) {
			return new File( dir, ALICE_RESOURCE_INSTALL_PATH );
		}
		return null;
	}
	
	public void setNebulousResourceDir( String dir ) {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, dir );
	}

	public File getNebulousDirFromPref() {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		String dir = rv.get( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, "" );
		if( dir != null && dir.length() > 0 ) {
			return new File( dir );
		}
		else {
			return getNebulousDirFromGalleryPref();
		}
	}
	
	public void setAliceResourceDir( String dir ) {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( ALICE_RESOURCE_DIRECTORY_PREF_KEY, dir );
		String galleryDir = getGalleryPathFromResourcePath(dir);
		if (galleryDir != null) {
			setGalleryResourceDir(galleryDir);
		}
	}

	public File getAliceDirFromPref() {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		String dir = rv.get( ALICE_RESOURCE_DIRECTORY_PREF_KEY, "" );
		if( dir != null && dir.length() > 0 ) {
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
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		String dir = rv.get( GALLERY_DIRECTORY_PREF_KEY, "" );
		if( dir != null && dir.length() > 0 ) {
			return new File( dir );
		}
		return null;
	}

	private List< File > findSimsBundles() {
		File simsPath = findResourcePath( NEBULOUS_RESOURCE_INSTALL_PATH );
		if( simsPath != null ) {
			ResourcePathManager.addPath( ResourcePathManager.SIMS_RESOURCE_KEY, simsPath );
			return ResourcePathManager.getPaths( ResourcePathManager.SIMS_RESOURCE_KEY );
		} else {
			LinkedList< File > directoryFromSavedPreference = new LinkedList< File >();
			File resourceDir = getNebulousDirFromPref();
			if( resourceDir != null ) {
				directoryFromSavedPreference.add( resourceDir );
			}
			return directoryFromSavedPreference;
		}
	}

	private List< File > findAliceResources() {
		File alicePath = findResourcePath( ALICE_RESOURCE_INSTALL_PATH );
		if( alicePath != null ) {
			ResourcePathManager.addPath( ResourcePathManager.MODEL_RESOURCE_KEY, alicePath );
			return ResourcePathManager.getPaths( ResourcePathManager.MODEL_RESOURCE_KEY );
		} 
		else {
			LinkedList< File > directoryFromSavedPreference = new LinkedList< File >();
			File resourceDir = getAliceDirFromPref();
			
			if( resourceDir != null ) {
				directoryFromSavedPreference.add( resourceDir );
			}
			return directoryFromSavedPreference;
		}
	}

	private StorytellingResources() {
	}

	
	public static java.util.Map<File, List<String>> getClassNamesFromResources(File... resourceJars) {
		java.util.HashMap<File, List<String>> rv = new java.util.HashMap<File, List<String>>();
		for (File resourceJar : resourceJars) {
			try
			{
				ZipFile zip = new ZipFile(resourceJar);
				Enumeration<? extends ZipEntry> entries = zip.entries();
				while (entries.hasMoreElements())
				{
					ZipEntry entry = entries.nextElement();
					if (entry.getName().endsWith(".xml") && !entry.getName().contains("$"))
					{
						String className = entry.getName().replace('/', '.');
						int lastDot = className.lastIndexOf(".");
						String baseName = className.substring(0, lastDot);
						if (baseName.startsWith(".")) {
							baseName = baseName.substring(1);
						}
						if (!rv.containsKey(resourceJar)) {
							rv.put(resourceJar, new LinkedList<String>());
						}
						rv.get(resourceJar).add(baseName);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return rv;
	}
	
	public List<Class<? extends org.lgna.story.resources.ModelResource>> loadResourceJarFile(File... resourceJars)
	{
		List<Class<? extends org.lgna.story.resources.ModelResource>> classes = new LinkedList<Class<? extends org.lgna.story.resources.ModelResource>>();
		
		List<String> classNames = new LinkedList<String>();
		LinkedList<URL> urls = new LinkedList<URL>();
		
		java.util.Map<File, List<String>> classNameMap = getClassNamesFromResources(resourceJars);
		
		for (java.util.Map.Entry<File, List<String>> entry : classNameMap.entrySet()) {
			try {
				urls.add(entry.getKey().toURI().toURL());
				for (String className : entry.getValue()) {
					classNames.add(className);
				}
			}
			catch (java.net.MalformedURLException e) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe("Failed to load resources from jar: "+entry.getKey());
			}
		}
		try
		{
			URL[] urlArray = urls.toArray(new URL[urls.size()]);
			URLClassLoader cl = new URLClassLoader(urlArray);
			
			for (String className : classNames)
			{
				try {
					Class<?> cls = cl.loadClass(className);
					if (org.lgna.story.resources.ModelResource.class.isAssignableFrom(cls))
					{
						classes.add((Class<? extends org.lgna.story.resources.ModelResource>)cls);
					}
				}
				catch (ClassNotFoundException cnfe) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe("FAILED TO LOAD GALLERY CLASS: "+className);
				}
			}
			this.classLoader = cl;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return classes;
	}
	
	
	public List<Class<? extends org.lgna.story.resources.ModelResource>> getAndLoadModelResourceClasses(List<File> resourcePaths)
	{
		List<Class<? extends org.lgna.story.resources.ModelResource>> galleryClasses = new LinkedList<Class<? extends org.lgna.story.resources.ModelResource>>();
		for (File modelPath : resourcePaths)
		{
			if (modelPath.exists()) {
				try {
					if (modelPath.isDirectory()) {
						File[] jarFiles = FileUtilities.listDescendants(modelPath, "jar");
						galleryClasses.addAll( this.loadResourceJarFile(jarFiles) );
					}
					else {
						galleryClasses.addAll( this.loadResourceJarFile(modelPath));
					}
				}
				catch (Exception e)
				{
					System.err.println("Failed to load resources on path: '"+modelPath+"'");
				}
			}
		}
		return galleryClasses;
	}
	
	public void getGalleryLocationFromUser() {
		FindResourcesPanel.getInstance().show(null);
		if (FindResourcesPanel.getInstance().getGalleryDir() != null) {
			setGalleryResourceDir(FindResourcesPanel.getInstance().getGalleryDir().getAbsolutePath());
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
	
	private void buildGalleryTreeWithJars(File...resourceJars) {
		java.util.ArrayList<File> jarFiles = new java.util.ArrayList<File>();
		for (int i=0; i<resourceJars.length; i++) {
			jarFiles.add(resourceJars[i]);
		}
		List< Class< ? extends org.lgna.story.resources.ModelResource >> modelResourceClasses = this.getAndLoadModelResourceClasses( jarFiles );
		this.galleryTree = new ModelResourceTree( modelResourceClasses );
	}
	
	public void findAndLoadAliceResourcesIfNecessary() {
		if (this.aliceClassesLoaded == null) {
			List< File > resourcePaths = ResourcePathManager.getPaths( ResourcePathManager.MODEL_RESOURCE_KEY );
			if( resourcePaths.size() == 0 ) {
				resourcePaths = findAliceResources();
			}
			this.aliceClassesLoaded = this.getAndLoadModelResourceClasses( resourcePaths );
			if( aliceClassesLoaded.size() == 0) {
				if (FindResourcesPanel.getInstance().getGalleryDir() != null) {
					setGalleryResourceDir(FindResourcesPanel.getInstance().getGalleryDir().getAbsolutePath());
				}
				else 
				{
					getGalleryLocationFromUser();
				}
				//Try again
				resourcePaths = findAliceResources();
				this.aliceClassesLoaded = this.getAndLoadModelResourceClasses( resourcePaths );
			}
			if( this.aliceClassesLoaded.size() == 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("Cannot find the Alice gallery resources.");
				if (resourcePaths == null || resourcePaths.size() == 0) {
					sb.append("\nNo gallery directories were detected. Make sure Alice is properly installed and has been run at least once.");
				} else {
					sb.append("\nFailed to locate the resources in:");
					String separator = "\n   ";
					for (File path : resourcePaths) {
						sb.append(separator+"'"+path+"'");
					}
					String phrase = resourcePaths.size() > 1 ? "these directories exist" : "this directory exists";
					sb.append("\nVerify that "+phrase+" and verify that Alice is properly installed.");
				}
				javax.swing.JOptionPane.showMessageDialog( null, sb.toString() );
			}
			else {
				File galleryFile = resourcePaths.get( 0 );
				if (galleryFile.isDirectory()) {
					setAliceResourceDir( galleryFile.getAbsolutePath() );
				}
				else
				{
					setAliceResourceDir( galleryFile.getParentFile().getAbsolutePath() );
				}
			}
		}
	}
	
	private int loadSimsBundlesFromPaths(List< File > resourcePaths) {
		int count = 0;
		for( File path : resourcePaths ) {
			if (path.exists()) {
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
		List< File > resourcePaths = ResourcePathManager.getPaths( ResourcePathManager.SIMS_RESOURCE_KEY );
		if( resourcePaths.size() == 0 ) {
			resourcePaths = findSimsBundles();
		}
		int loaded = loadSimsBundlesFromPaths(resourcePaths);
		if ( loaded == 0 && simsPathsLoaded.size() == 0) {
			if (FindResourcesPanel.getInstance().getGalleryDir() != null) {
				setGalleryResourceDir(FindResourcesPanel.getInstance().getGalleryDir().getAbsolutePath());
			}
			else 
			{
				getGalleryLocationFromUser();
			}
			//Try again
			resourcePaths = findSimsBundles();
			loaded = loadSimsBundlesFromPaths(resourcePaths);
		}
		if( loaded == 0 && simsPathsLoaded.size() == 0 ) {
			StringBuilder sb = new StringBuilder();
			sb.append("Cannot find The Sims (TM) 2 Art Assets.");
			if (resourcePaths == null || resourcePaths.size() == 0) {
				sb.append("\nNo gallery directories were detected. Make sure Alice is properly installed and has been run at least once.");
			} else {
				sb.append("\nSearched in ");
				String separator = "";
				for (File path : resourcePaths) {
					sb.append(separator+"'"+path+"'");
					if (separator.length() == 0){
						separator = ", ";
					}
				}
				String phrase = resourcePaths.size() > 1 ? "these directories exist" : "this directory exists";
				sb.append("\nVerify that "+phrase+" and verify that Alice is properly installed.");
			}
			javax.swing.JOptionPane.showMessageDialog( null, sb.toString() );
			
		} else {
			File galleryFile = simsPathsLoaded.get( 0 );
			if (galleryFile.isDirectory()) {
				setNebulousResourceDir( galleryFile.getAbsolutePath() );
			}
			else
			{
				setNebulousResourceDir( galleryFile.getParentFile().getAbsolutePath() );
			}
		}
	}

	public List< org.lgna.project.ast.JavaType > getTopLevelGalleryTypes() {
		List< ModelResourceTreeNode > rootNodes = this.getGalleryTreeInternal().getRootNodes();
		List< org.lgna.project.ast.JavaType > rootTypes = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		for( ModelResourceTreeNode node : rootNodes ) {
			rootTypes.add( node.getUserType().getFirstEncounteredJavaType() );
		}
		return rootTypes;
	}

	public org.lgna.project.ast.AbstractType< ?, ?, ? > getGalleryResourceParentFor( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		TreeNode< JavaType > child = this.getGalleryResourceTreeNodeForJavaType( type );
		if( child != null ) {
			ModelResourceTreeNode parent = (ModelResourceTreeNode)child.getParent();
			//Go up an extra level if the node we're returning is a node with a single child (this mirrors what is happening in getResourceChildren)
			if( parent != null && hasSingleLeafChild( parent ) ) {
				parent = (ModelResourceTreeNode)parent.getParent();
			}
			return parent.getResourceJavaType();
		} else {
			return null;
		}
	}

	private boolean hasSingleLeafChild( TreeNode< ? > node ) {
		return (node.getChildCount() == 1 && node.getChildAt( 0 ).getChildCount() == 0);
	}
	
	public URL getAliceResource(String resourceString) {
		this.findAndLoadAliceResourcesIfNecessary();
		assert this.classLoader != null;
		return this.classLoader.findResource(resourceString);
	}
	
	public InputStream getAliceResourceAsStream(String resourceString) {
		this.findAndLoadAliceResourcesIfNecessary();
		assert this.classLoader != null;
		return this.classLoader.getResourceAsStream(resourceString);
	}

	public List< org.lgna.project.ast.AbstractDeclaration > getGalleryResourceChildrenFor( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		//System.out.println( "Getting children for type: " + type );
		java.util.List< org.lgna.project.ast.AbstractDeclaration > toReturn = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		TreeNode< JavaType > typeNode = this.getGalleryResourceTreeNodeForJavaType( type );
		if( typeNode != null ) {
			for( int i = 0; i < typeNode.getChildCount(); i++ ) {
				TreeNode< JavaType > child = typeNode.getChildAt( i );
				//If the child has a single leaf child, go down a level and return that
				if( hasSingleLeafChild( child ) ) {
					child = child.getChildAt( 0 );
				}
				ModelResourceTreeNode node = (ModelResourceTreeNode)child;
				if( node.isLeaf() && node.getJavaField() != null ) {
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
	
	public void initializeGalleryTreeWithJars(File... resourceJars) {
		this.buildGalleryTreeWithJars(resourceJars);
	}
	
	private ModelResourceTree getGalleryTreeInternal() {
		if (this.galleryTree == null) {
			findAndLoadAliceResourcesIfNecessary();
			this.galleryTree = new ModelResourceTree( this.aliceClassesLoaded );
		}
		return this.galleryTree;
	}

	private ModelResourceTreeNode getGalleryResourceTreeNodeForJavaType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		return this.getGalleryTreeInternal().getGalleryResourceTreeNodeForJavaType( type );
	}

	public ModelResourceTreeNode getGalleryResourceTreeNodeForUserType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		return getGalleryTreeInternal().getGalleryResourceTreeNodeForUserType( type );
	}

	public ModelResourceTreeNode getGalleryTree() {
		return getGalleryTreeInternal().getTree();
	}

}
