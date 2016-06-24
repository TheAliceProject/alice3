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

import org.lgna.story.implementation.alice.AliceResourceClassUtilities;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.nebulous.Manager;

public enum NebulousStorytellingResources {
	INSTANCE;

	private static final String NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY = "NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY";

	public static final String NEBULOUS_RESOURCE_INSTALL_PATH = "assets/sims";

	private final List<File> simsPathsLoaded = new LinkedList<File>();

	private File[] getNebulousDirsFromGalleryPref() {
		return StorytellingResources.getDirsFromPref( StorytellingResources.GALLERY_DIRECTORY_PREF_KEY, NEBULOUS_RESOURCE_INSTALL_PATH );
	}

	public void setNebulousResourceDir( String[] dirs ) {
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, StorytellingResources.makeDirectoryPreferenceString( dirs ) );
	}

	public File[] getNebulousDirsFromPref() {
		File[] dirs = StorytellingResources.getDirsFromPref( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, "" );
		if( dirs != null ) {
			return dirs;
		}
		else {
			return getNebulousDirsFromGalleryPref();
		}
	}

	private List<File> findSimsBundles() {
		File simsPath = StorytellingResources.findResourcePath( NEBULOUS_RESOURCE_INSTALL_PATH );
		if( simsPath != null ) {
			ResourcePathManager.addPath( ResourcePathManager.SIMS_RESOURCE_KEY, simsPath );
			return ResourcePathManager.getPaths( ResourcePathManager.SIMS_RESOURCE_KEY );
		} else {
			LinkedList<File> directoryFromSavedPreference = new LinkedList<File>();
			File[] resourceDirs = getNebulousDirsFromPref();
			if( resourceDirs != null ) {
				for( File resourceDir : resourceDirs ) {
					directoryFromSavedPreference.add( resourceDir );
				}
			}
			return directoryFromSavedPreference;
		}
	}

	private NebulousStorytellingResources() {
	}

	private void clearSimsResourceInfo()
	{
		ResourcePathManager.clearPaths( ResourcePathManager.SIMS_RESOURCE_KEY );
		java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
		rv.put( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, "" );
		rv.put( StorytellingResources.GALLERY_DIRECTORY_PREF_KEY, "" );
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
				String[] dirArray = { galleryDir.getAbsolutePath() };
				StorytellingResources.INSTANCE.setGalleryResourceDirs( dirArray );
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
			String[] galleryDirs = new String[ simsPathsLoaded.size() ];
			for( int i = 0; i < simsPathsLoaded.size(); i++ ) {
				File galleryFile = simsPathsLoaded.get( i );
				if( galleryFile.isDirectory() ) {
					galleryDirs[ i ] = galleryFile.getAbsolutePath();
				}
				else
				{
					galleryDirs[ i ] = galleryFile.getParentFile().getAbsolutePath();
				}
			}
			setNebulousResourceDir( galleryDirs );
		}
	}
}
