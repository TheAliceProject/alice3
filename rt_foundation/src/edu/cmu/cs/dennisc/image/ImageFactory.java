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
package edu.cmu.cs.dennisc.image;

/**
 * @author Dennis Cosgrove
 */
public class ImageFactory {
	private static java.util.Map< org.alice.virtualmachine.resources.ImageResource, java.awt.image.BufferedImage > resourceToBufferedImageMap = new java.util.HashMap< org.alice.virtualmachine.resources.ImageResource, java.awt.image.BufferedImage >();

	public static java.awt.image.BufferedImage getBufferedImage( org.alice.virtualmachine.resources.ImageResource imageResource ) {
		assert imageResource != null;
		java.awt.image.BufferedImage rv = ImageFactory.resourceToBufferedImageMap.get( imageResource );
		if( rv != null ) {
			//pass
		} else {
			try {
				rv = javax.imageio.ImageIO.read( new java.io.ByteArrayInputStream( imageResource.getData() ) );
				//				if( imageResource.getWidth() < 0 || imageResource.getHeight() < 0 ) {
				imageResource.setWidth( rv.getWidth() );
				imageResource.setHeight( rv.getHeight() );
				//				}
				ImageFactory.resourceToBufferedImageMap.put( imageResource, rv );
			} catch( java.io.IOException ioe ) {
				//todo: return warning texture
			}
		}
		return rv;
	}

	public static org.alice.virtualmachine.resources.ImageResource createImageResource( java.io.File file ) throws java.io.IOException {
		String contentType = org.alice.virtualmachine.resources.ImageResource.getContentType( file );
		if( contentType != null ) {
			org.alice.virtualmachine.resources.ImageResource rv = new org.alice.virtualmachine.resources.ImageResource( file, contentType );

			//update width and height details
			getBufferedImage( rv );

			return rv;
		} else {
			throw new RuntimeException( "content type not found for " + file );
		}
	}

}
