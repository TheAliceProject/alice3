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
package edu.cmu.cs.dennisc.javax.imageio;

/**
 * @author Dennis Cosgrove
 */
public class PngUtilities {
	private static javax.imageio.metadata.IIOMetadataNode getFirstPhysNodeCreatingAndAddingIfNecessary( javax.imageio.metadata.IIOMetadataNode parentNode ) {
		final String NAME = "pHYs";
		javax.imageio.metadata.IIOMetadataNode rv;
		org.w3c.dom.NodeList nodeList = parentNode.getElementsByTagName( NAME );
		switch( nodeList.getLength() ) {
		case 0:
			rv = new javax.imageio.metadata.IIOMetadataNode( NAME );
			parentNode.appendChild( rv );
			break;
		default:
			rv = (javax.imageio.metadata.IIOMetadataNode)nodeList.item( 0 );
		}
		return rv;
	}

	public static void write( java.awt.image.RenderedImage image, int dpi, javax.imageio.stream.ImageOutputStream ios ) throws java.io.IOException {
		javax.imageio.ImageTypeSpecifier imageTypeSpecifier = javax.imageio.ImageTypeSpecifier.createFromRenderedImage( image );

		int pixelsPerMeter = (int)Math.round( dpi / 0.0254 );
		String pixelsPerMeterText = Integer.toString( pixelsPerMeter );
		java.util.Iterator<javax.imageio.ImageWriter> imageWriterIter = javax.imageio.ImageIO.getImageWritersByFormatName( "png" );
		//todo: convert to while loop?
		if( imageWriterIter.hasNext() ) {
			javax.imageio.ImageWriter imageWriter = imageWriterIter.next();
			javax.imageio.ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

			//seems to create new metadata
			javax.imageio.metadata.IIOMetadata metadata = imageWriter.getDefaultImageMetadata( imageTypeSpecifier, imageWriteParam );

			//access restricted
			//if( metadata instanceof com.sun.imageio.plugins.png.PNGMetadata ) {
			//	com.sun.imageio.plugins.png.PNGMetadata pngMetadata = (com.sun.imageio.plugins.png.PNGMetadata)metadata;
			//	pngMetadata.pHYs_present = true;
			//	pngMetadata.pHYs_pixelsPerUnitXAxis = dotsPerMeter;
			//	pngMetadata.pHYs_pixelsPerUnitYAxis = dotsPerMeter;
			//	pngMetadata.pHYs_unitSpecifier = 1; //meter
			//}

			String formatName = metadata.getNativeMetadataFormatName();
			javax.imageio.metadata.IIOMetadataNode root = (javax.imageio.metadata.IIOMetadataNode)metadata.getAsTree( formatName );
			javax.imageio.metadata.IIOMetadataNode physNode = getFirstPhysNodeCreatingAndAddingIfNecessary( root );
			physNode.setAttribute( "pixelsPerUnitXAxis", pixelsPerMeterText );
			physNode.setAttribute( "pixelsPerUnitYAxis", pixelsPerMeterText );
			physNode.setAttribute( "unitSpecifier", "meter" );
			metadata.setFromTree( formatName, root );

			java.util.List<java.awt.image.BufferedImage> thumbnails = null;
			javax.imageio.IIOImage iioImage = new javax.imageio.IIOImage( image, thumbnails, metadata );
			imageWriter.setOutput( ios );
			imageWriter.write( iioImage );
			ios.flush();
			imageWriter.setOutput( null );
		} else {
			throw new RuntimeException( "no image writers for png" );
		}
	}

	public static void write( java.awt.image.RenderedImage image, int dpi, java.io.File file ) throws java.io.IOException {
		javax.imageio.stream.ImageOutputStream ios = javax.imageio.ImageIO.createImageOutputStream( file );
		try {
			write( image, dpi, ios );
		} finally {
			ios.close();
		}
	}

	public static void main( String[] args ) throws java.io.IOException {
		java.io.File normalFile = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "test.png" );
		java.io.File dpiFile = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "test300.png" );
		java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( 1200, 600, java.awt.image.BufferedImage.TYPE_INT_RGB );

		//test to make sure not messing up future image io writes
		write( image, 300, dpiFile );
		javax.imageio.ImageIO.write( image, "png", normalFile );

	}
}
