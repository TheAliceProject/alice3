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

public class ResourcePathManager {

	public static final String AUDIO_RESOURCE_KEY = "org.alice.ide.audioResourcePath";
	public static final String IMAGE_RESOURCE_KEY = "org.alice.ide.imageResourcePath";
	public static final String MODEL_RESOURCE_KEY = "org.alice.ide.modelResourcePath";
	public static final String SIMS_RESOURCE_KEY = "org.alice.ide.simsResourcePath";

	private static final java.util.Map<String, java.util.List<java.io.File>> resourcePathMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	static {
		ResourcePathManager.initializePath( AUDIO_RESOURCE_KEY );
		ResourcePathManager.initializePath( IMAGE_RESOURCE_KEY );
		ResourcePathManager.initializePath( MODEL_RESOURCE_KEY );
		ResourcePathManager.initializePath( SIMS_RESOURCE_KEY );
	}

	private ResourcePathManager() {
		throw new AssertionError();
	}

	private static java.io.File getValidPath( String path ) {
		try {
			java.io.File f = new java.io.File( path );
			if( f.exists() && f.isDirectory() ) {
				return f;
			}
		} catch( Exception e ) {
			//todo?
			//e.printStackTrace();
		}
		return null;
	}

	private static void initializePath( String pathKey ) {
		java.util.List<java.io.File> validPaths = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		String pathValue = System.getProperty( pathKey );
		if( ( pathValue != null ) && ( pathValue.length() > 0 ) ) {
			String[] paths = pathValue.split( java.io.File.pathSeparator );
			if( paths.length > 0 ) {
				for( String path : paths ) {
					java.io.File resourcePath = getValidPath( path );
					if( resourcePath != null ) {
						validPaths.add( resourcePath );
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "Failed to add path to", pathKey + ":", path );
					}
				}
			} else {
				java.io.File resourcePath = getValidPath( pathValue );
				if( resourcePath != null ) {
					validPaths.add( resourcePath );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "Failed to add path to", pathKey + ":", pathValue );
				}
			}
		}
		resourcePathMap.put( pathKey, validPaths );
	}

	public static boolean addPath( String key, java.io.File path ) {
		if( !resourcePathMap.containsKey( key ) ) {
			return false;
		}
		java.io.File validPath = getValidPath( path.getAbsolutePath() );
		if( validPath != null ) {
			java.util.List<java.io.File> existingPaths = resourcePathMap.get( key );
			if( !existingPaths.contains( validPath ) ) {
				existingPaths.add( validPath );
				return true;
			}
		}
		return false;
	}

	public static void clearPaths( String key ) {
		resourcePathMap.remove( key );
	}

	public static java.util.List<java.io.File> getPaths( String key ) {
		if( resourcePathMap.containsKey( key ) ) {
			return resourcePathMap.get( key );
		} else {
			return new java.util.LinkedList<java.io.File>();
		}
	}
}
