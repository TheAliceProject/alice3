package org.lgna.story.resources.sims2;

public class SimsResourceUtilities {
	
	private SimsResourceUtilities() {
	}
	
	public static String getTextureName(Object resource)
	{
		String textureName =  resource.toString();
		int splitPoint = textureName.indexOf('_');
		if (splitPoint != -1)
		{
			textureName = textureName.substring(splitPoint+1);
		}
		return textureName;
	}

	public static String getModelName(Object resource)
	{
		return resource.getClass().getSimpleName();
	}

}
