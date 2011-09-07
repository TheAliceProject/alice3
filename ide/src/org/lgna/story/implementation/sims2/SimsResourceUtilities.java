package org.lgna.story.implementation.sims2;

public class SimsResourceUtilities {
	
	private SimsResourceUtilities() {
	}
	
	public static String getTextureName(Object resource)
	{
		String textureName =  resource.toString();
		return textureName;
	}

	public static String getModelName(Object resource)
	{
		return resource.getClass().getSimpleName();
	}

}
