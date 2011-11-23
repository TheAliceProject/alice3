package org.alice.ide;



public class ResourcePathManager {
	
	public static String AUDIO_RESOURCE_KEY = "org.alice.ide.audioResourcePath";
	public static String IMAGE_RESOURCE_KEY = "org.alice.ide.imageResourcePath";
	public static String MODEL_RESOURCE_KEY = "org.alice.ide.modelResourcePath";
	public static String SIMS_RESOURCE_KEY = "org.alice.ide.simsResourcePath";
	
	private final static java.util.Map<String, java.util.List<java.io.File>> resourcePathMap = new java.util.HashMap<String, java.util.List<java.io.File>>();
	
	
	static
	{
		ResourcePathManager.initializePath(AUDIO_RESOURCE_KEY);
		ResourcePathManager.initializePath(IMAGE_RESOURCE_KEY);
		ResourcePathManager.initializePath(MODEL_RESOURCE_KEY);
		ResourcePathManager.initializePath(SIMS_RESOURCE_KEY);
		
	}

	private static java.io.File getValidPath(String path)
	{
		try
		{
			java.io.File f = new java.io.File(path);
			if (f.exists() && f.isDirectory())
			{
				return f;
			}
		}
		catch (Exception e)
		{
		}
		return null;
	}

	private static void initializePath(String pathKey)
	{
		java.util.List<java.io.File> validPaths = new java.util.LinkedList<java.io.File>();
		String pathValue = System.getProperty(pathKey);
		if (pathValue != null && pathValue.length() > 0)
		{
			String[] paths = pathValue.split(java.io.File.pathSeparator);
			if (paths.length > 0)
			{
				for (String path : paths)
				{
					java.io.File resourcePath = getValidPath(path);
					if (resourcePath != null)
					{
						validPaths.add(resourcePath);
					}
					else
					{
						System.err.println("Failed to add path to "+pathKey+": "+path);
					}
				}
			}
			else
			{
				java.io.File resourcePath = getValidPath(pathValue);
				if (resourcePath != null)
				{
					validPaths.add(resourcePath);
				}
				else
				{
					System.err.println("Failed to add path to "+pathKey+": "+pathValue);
				}
			}
		}
		resourcePathMap.put(pathKey, validPaths);
	}
	
	public static boolean addPath(String key, java.io.File path)
	{
		if (!resourcePathMap.containsKey(key))
		{
			return false;
		}
		java.io.File validPath = getValidPath(path.getAbsolutePath());
		if (validPath != null)
		{
			java.util.List<java.io.File> existingPaths = resourcePathMap.get(key);
			if (!existingPaths.contains(validPath))
			{
				existingPaths.add(validPath);
				return true;
			}
		}
		return false;
	}
	
	public static java.util.List<java.io.File> getPaths(String key)
	{
		if (resourcePathMap.containsKey(key))
		{
			return resourcePathMap.get(key);
		}
		return new java.util.LinkedList<java.io.File>();
	}
}
