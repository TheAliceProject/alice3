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

import org.lgna.story.resourceutilities.FindResourcesPanel;

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

	private static java.io.File getDirectoryFromProperty( String propertyName ) {
		String path = System.getProperty( propertyName );
		if( path != null ) {
			java.io.File file = new java.io.File( path );
			if( file.isDirectory() ) {
				return file;
			}
		}
		return null;
	}

	public static java.io.File getInstallDirectory() {
		java.io.File rv = getDirectoryFromProperty( "org.alice.ide.rootDirectory" );
		if( rv != null ) {
			//pass
		} else {
			rv = getDirectoryFromProperty( "user.dir" );
		}
		return rv;
	}

	private static java.io.File getFallbackDirectory() {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory();
	}

	private static java.io.File modelGalleryDirectory;

	public static java.io.File getModelGalleryDirectory() {
		if( StoryApiDirectoryUtilities.modelGalleryDirectory != null ) {
			//pass
		} else {
			java.io.File installDirectory = getInstallDirectory();
			if( installDirectory != null ) {
				java.io.File file = new java.io.File( installDirectory, MODEL_GALLERY_NAME );
				if( file.isDirectory() ) {
					StoryApiDirectoryUtilities.modelGalleryDirectory = file;
					try {
						java.util.prefs.Preferences preferences = java.util.prefs.Preferences.userRoot();
						preferences.put( MODEL_GALLERY_PREFRENCE_KEY, StoryApiDirectoryUtilities.modelGalleryDirectory.getAbsolutePath() );
					} catch( Throwable t ) {
						t.printStackTrace();
					}
				} else {
					try {
						java.util.prefs.Preferences rv = java.util.prefs.Preferences.userRoot();
						String path = rv.get( MODEL_GALLERY_PREFRENCE_KEY, null );
						if( path != null ) {
							java.io.File fileFromPreference = new java.io.File( path );
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

		if( StoryApiDirectoryUtilities.modelGalleryDirectory != null ) {
			//pass
		} else {
			FindResourcesPanel.getInstance().show( null );
			java.io.File fileFromUser = FindResourcesPanel.getInstance().getGalleryDir();
			if( fileFromUser != null ) {
				StoryApiDirectoryUtilities.modelGalleryDirectory = fileFromUser;
			} else {
				throw new RuntimeException();
			}
		}
		return StoryApiDirectoryUtilities.modelGalleryDirectory;
	}

	public static java.io.File getSoundGalleryDirectory() {
		try {
			java.io.File installDirectory = getInstallDirectory();
			if( installDirectory != null ) {
				java.io.File soundGalleryDirectory = new java.io.File( installDirectory, SOUND_GALLERY_NAME );
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

	public static java.io.File getStarterProjectsDirectory() {
		try {
			java.io.File installDirectory = getInstallDirectory();
			if( installDirectory != null ) {
				java.io.File starterProjectsDirectory = new java.io.File( installDirectory, STARTER_PROJECTS_NAME );
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
}
