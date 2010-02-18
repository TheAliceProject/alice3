/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.texture;

/**
 * @author Dennis Cosgrove
 */
public class TextureFactory {
	private static java.util.Map< org.alice.virtualmachine.resources.ImageResource, Texture > resourceToTextureMap = new java.util.HashMap< org.alice.virtualmachine.resources.ImageResource, Texture >();
	
	public static Texture getTexture( org.alice.virtualmachine.resources.ImageResource imageResource, boolean isMipMappingDesired ) {
		assert imageResource != null;
		Texture rv = TextureFactory.resourceToTextureMap.get( imageResource );
		if( rv != null ) {
			//pass
		} else {
			java.awt.image.BufferedImage bufferedImage = edu.cmu.cs.dennisc.image.ImageFactory.getBufferedImage( imageResource );

			if( bufferedImage != null ) {
				BufferedImageTexture bufferedImageTexture = new BufferedImageTexture();
				bufferedImageTexture.setBufferedImage( bufferedImage );
				bufferedImageTexture.setMipMappingDesired( isMipMappingDesired );
				
				//todo: handle java.awt.image.BufferedImage.BITMASK? 
				boolean isPotenentiallyAlphaBlended = bufferedImage.getTransparency()==java.awt.image.BufferedImage.TRANSLUCENT;
				bufferedImageTexture.setPotentiallyAlphaBlended( isPotenentiallyAlphaBlended );

			
				rv = bufferedImageTexture;
				TextureFactory.resourceToTextureMap.put( imageResource, rv );
			} else {
				//todo: warning texture
				rv = null;
			}
		}
		return rv;
	}
}
