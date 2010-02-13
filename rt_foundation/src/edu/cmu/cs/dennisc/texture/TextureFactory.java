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
	private static java.util.Map< org.alice.virtualmachine.Resource, Texture > resourceToTextureMap = new java.util.HashMap< org.alice.virtualmachine.Resource, Texture >();
	private static java.util.Map< String, String > extensionToContentTypeMap;
	
	private static final String PNG_MIME_TYPE = "image/png";
	private static final String JPEG_MIME_TYPE = "image/jpeg";
	private static final String BMP_MIME_TYPE = "image/bmp";

	static {
		System.out.print( "Attempting to register mp3 capability... " );
		com.sun.media.codec.audio.mp3.JavaDecoder.main( new String[] {} );
		TextureFactory.extensionToContentTypeMap = new java.util.HashMap< String, String >();
		TextureFactory.extensionToContentTypeMap.put( "png", PNG_MIME_TYPE );
		TextureFactory.extensionToContentTypeMap.put( "jpg", JPEG_MIME_TYPE );
		TextureFactory.extensionToContentTypeMap.put( "jpeg", JPEG_MIME_TYPE );
		TextureFactory.extensionToContentTypeMap.put( "bmp", BMP_MIME_TYPE );
	}

	public static String getContentType( String path ) {
		String extension = edu.cmu.cs.dennisc.io.FileUtilities.getExtension( path );
		return TextureFactory.extensionToContentTypeMap.get( extension.toLowerCase() );
	}
	public static String getContentType( java.io.File file ) {
		return getContentType( file.getName() );
	}
	
	public static boolean isAcceptableContentType( String contentType ) {
		return TextureFactory.extensionToContentTypeMap.containsValue( contentType );
	}
	
	public static java.io.FilenameFilter createFilenameFilter( final boolean areDirectoriesAccepted ) {
		return new java.io.FilenameFilter() {
			public boolean accept( java.io.File dir, String name ) {
				java.io.File file = new java.io.File( dir, name );
				if( file.isDirectory() ) {
					return areDirectoriesAccepted;
				} else {
					return getContentType( name ) != null;
				}
			}
		};
	}
	
	public static Texture getTexture( org.alice.virtualmachine.Resource resource, boolean isMipMappingDesired ) {
		assert resource != null;
		Texture texture = TextureFactory.resourceToTextureMap.get( resource );
		if( texture != null ) {
			//pass
		} else {
			try {
				java.awt.image.BufferedImage bufferedImage = javax.imageio.ImageIO.read( new java.io.ByteArrayInputStream( resource.getData() ) );
				BufferedImageTexture bufferedImageTexture = new BufferedImageTexture();
				bufferedImageTexture.setBufferedImage( bufferedImage );
				bufferedImageTexture.setMipMappingDesired( isMipMappingDesired );
				
				//todo: handle java.awt.image.BufferedImage.BITMASK? 
				boolean isPotenentiallyAlphaBlended = bufferedImage.getTransparency()==java.awt.image.BufferedImage.TRANSLUCENT;
				bufferedImageTexture.setPotentiallyAlphaBlended( isPotenentiallyAlphaBlended );

			
				texture = bufferedImageTexture;
				TextureFactory.resourceToTextureMap.put( resource, texture );
			} catch( java.io.IOException ioe ) {
				//todo: return warning texture
			}
		}
		return texture;
	}
	
	public static org.alice.virtualmachine.resources.ImageResource createImageResource( java.io.File file ) throws java.io.IOException {
		String contentType = getContentType( file );
		if( contentType != null ) {
			return new org.alice.virtualmachine.resources.ImageResource( file, contentType );
		} else {
			return null;
		}
	}
	
}
