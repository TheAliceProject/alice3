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
package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.lgna.story.resourceutilities.FindResourcesPanel;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * @author Dennis Cosgrove
 */
public class StoryApiDirectoryUtilities {
	private static final String MODEL_GALLERY_PREFRENCE_KEY = "MODEL_GALLERY_PREFRENCE_KEY";
	private static final String MODEL_GALLERY_NAME = "application/gallery";
	private static final String SOUND_GALLERY_NAME = "application/sound-gallery";
	private static final String STARTER_PROJECTS_NAME = "application/starter-projects";

	private StoryApiDirectoryUtilities() {
		throw new AssertionError();
	}

	private static File getDirectoryFromProperty( String propertyName ) {
		String path = System.getProperty( propertyName );
		if( path != null ) {
			File file = new File( path );
			if( file.isDirectory() ) {
				return file;
			}
		}
		return null;
	}

	private static File getInstallDirectory() {
		File rootDir = getDirectoryFromProperty( "org.alice.ide.rootDirectory" );
		if (rootDir == null) {
			return getDirectoryFromProperty( "user.dir" );
		}
		return rootDir;
	}

	private static File getFallbackDirectory() {
		return FileUtilities.getDefaultDirectory();
	}

	private static File modelGalleryDirectory;

	public static File getModelGalleryDirectory() {
		if (StoryApiDirectoryUtilities.modelGalleryDirectory == null) {
			initializeModelGallery();
		}
		if (StoryApiDirectoryUtilities.modelGalleryDirectory == null) {
			askUserForModelGallery();
		}
		return StoryApiDirectoryUtilities.modelGalleryDirectory;
	}

	private static void initializeModelGallery() {
		File installDirectory = getInstallDirectory();
		if( installDirectory != null ) {
			File file = new File( installDirectory, MODEL_GALLERY_NAME );
			if( file.isDirectory() ) {
				StoryApiDirectoryUtilities.modelGalleryDirectory = file;
				try {
					Preferences preferences = Preferences.userRoot();
					preferences.put( MODEL_GALLERY_PREFRENCE_KEY, StoryApiDirectoryUtilities.modelGalleryDirectory.getAbsolutePath() );
				} catch( Throwable t ) {
					t.printStackTrace();
				}
			} else {
				try {
					Preferences rv = Preferences.userRoot();
					String path = rv.get( MODEL_GALLERY_PREFRENCE_KEY, null );
					if( path != null ) {
						File fileFromPreference = new File( path );
						if( fileFromPreference.isDirectory() ) {
							StoryApiDirectoryUtilities.modelGalleryDirectory = fileFromPreference;
						}
					}
				} catch( Throwable t ) {
					t.printStackTrace();
				}
			}
		}
	}

	private static void askUserForModelGallery() {
		FindResourcesPanel.getInstance().show(null );
		StoryApiDirectoryUtilities.modelGalleryDirectory = FindResourcesPanel.getInstance().getGalleryDir();
	}

	public static File getSoundGalleryDirectory() {
		try {
			File installDirectory = getInstallDirectory();
			if( installDirectory != null ) {
				File soundGalleryDirectory = new File( installDirectory, SOUND_GALLERY_NAME );
				if( soundGalleryDirectory.isDirectory() ) {
					return soundGalleryDirectory;
				} else {
					throw new RuntimeException(); //fallback
				}
			} else {
				throw new NullPointerException(); //fallback
			}
		} catch( Throwable t ) {
			return getFallbackDirectory();
		}
	}

	public static File getStarterProjectsDirectory() {
		try {
			File installDirectory = getInstallDirectory();
			if( installDirectory != null ) {
				File starterProjectsDirectory = new File( installDirectory, STARTER_PROJECTS_NAME );
				if( starterProjectsDirectory.isDirectory() ) {
					return starterProjectsDirectory;
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new NullPointerException();
			}
		} catch( Throwable t ) {
			return getFallbackDirectory();
		}
	}

	private static File userGalleryDirectory = null;

	public static void setUserGalleryDirectory( File userGalleryDirectory ) {
		StoryApiDirectoryUtilities.userGalleryDirectory = userGalleryDirectory;
	}

	public static File getUserGalleryDirectory() {
		if (StoryApiDirectoryUtilities.userGalleryDirectory != null) {
			return StoryApiDirectoryUtilities.userGalleryDirectory;
		}
		else {
			File userHome = getDirectoryFromProperty( "user.home" );
			return new File(userHome,  "Alice3/MyGallery");
		}
	}
}
