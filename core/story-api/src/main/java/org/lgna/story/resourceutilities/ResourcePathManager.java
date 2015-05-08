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
