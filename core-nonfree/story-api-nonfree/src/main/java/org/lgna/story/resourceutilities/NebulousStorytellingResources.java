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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import edu.cmu.cs.dennisc.nebulous.Manager;

import javax.swing.JOptionPane;

public enum NebulousStorytellingResources {
	INSTANCE;

	private static final String NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY = "NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY";

	public static final String NEBULOUS_RESOURCE_INSTALL_PATH = "assets/sims";

	private final List<File> simsPathsLoaded = new LinkedList<File>();

	private File[] getNebulousDirsFromGalleryPref() {
		return StorytellingResources.getDirsFromPref( StorytellingResources.GALLERY_DIRECTORY_PREF_KEY, NEBULOUS_RESOURCE_INSTALL_PATH );
	}

	public void setNebulousResourceDir( String[] dirs ) {
		Preferences rv = Preferences.userRoot();
		rv.put( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, StorytellingResources.makeDirectoryPreferenceString( dirs ) );
	}

	public File[] getNebulousDirsFromPref() {
		File[] dirs = StorytellingResources.getDirsFromPref( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, "" );
		if( dirs != null ) {
			return dirs;
		} else {
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
				Collections.addAll( directoryFromSavedPreference, resourceDirs );
			}
			return directoryFromSavedPreference;
		}
	}

	private NebulousStorytellingResources() {
	}

	private void clearSimsResourceInfo() {
		ResourcePathManager.clearPaths( ResourcePathManager.SIMS_RESOURCE_KEY );
		Preferences rv = Preferences.userRoot();
		rv.put( NEBULOUS_RESOURCE_DIRECTORY_PREF_KEY, "" );
		rv.put( StorytellingResources.GALLERY_DIRECTORY_PREF_KEY, "" );
	}

	private int loadSimsBundlesFromPaths( List<File> resourcePaths ) {
		int count = 0;
		for( File path : resourcePaths ) {
			if( path.exists() ) {
				for( File file : path.listFiles() ) {
					if( !simsPathsLoaded.contains( file ) ) {
						try {
							if( file.getName().endsWith( "txt" ) ) {
								//pass
							} else {
								Manager.addBundle( file );
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
			File rawResourcePath = new File( DEBUG_rawPathValue );
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
			if( galleryDir == null ) {
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
			JOptionPane.showMessageDialog( null, sb.toString() );

		} else {
			String[] galleryDirs = new String[ simsPathsLoaded.size() ];
			for( int i = 0; i < simsPathsLoaded.size(); i++ ) {
				File galleryFile = simsPathsLoaded.get( i );
				if( galleryFile.isDirectory() ) {
					galleryDirs[ i ] = galleryFile.getAbsolutePath();
				} else {
					galleryDirs[ i ] = galleryFile.getParentFile().getAbsolutePath();
				}
			}
			setNebulousResourceDir( galleryDirs );
		}
	}
}
