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

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.w3c.dom.NodeList;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class PngUtilities {
	private static IIOMetadataNode getFirstPhysNodeCreatingAndAddingIfNecessary( IIOMetadataNode parentNode ) {
		final String NAME = "pHYs";
		IIOMetadataNode rv;
		NodeList nodeList = parentNode.getElementsByTagName( NAME );
		switch( nodeList.getLength() ) {
		case 0:
			rv = new IIOMetadataNode( NAME );
			parentNode.appendChild( rv );
			break;
		default:
			rv = (IIOMetadataNode)nodeList.item( 0 );
		}
		return rv;
	}

	public static void write( RenderedImage image, int dpi, ImageOutputStream ios ) throws IOException {
		ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromRenderedImage( image );

		int pixelsPerMeter = (int)Math.round( dpi / 0.0254 );
		String pixelsPerMeterText = Integer.toString( pixelsPerMeter );
		Iterator<ImageWriter> imageWriterIter = ImageIO.getImageWritersByFormatName( "png" );
		//todo: convert to while loop?
		if( imageWriterIter.hasNext() ) {
			ImageWriter imageWriter = imageWriterIter.next();
			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

			//seems to create new metadata
			IIOMetadata metadata = imageWriter.getDefaultImageMetadata( imageTypeSpecifier, imageWriteParam );

			//access restricted
			//if( metadata instanceof com.sun.imageio.plugins.png.PNGMetadata ) {
			//	com.sun.imageio.plugins.png.PNGMetadata pngMetadata = (com.sun.imageio.plugins.png.PNGMetadata)metadata;
			//	pngMetadata.pHYs_present = true;
			//	pngMetadata.pHYs_pixelsPerUnitXAxis = dotsPerMeter;
			//	pngMetadata.pHYs_pixelsPerUnitYAxis = dotsPerMeter;
			//	pngMetadata.pHYs_unitSpecifier = 1; //meter
			//}

			String formatName = metadata.getNativeMetadataFormatName();
			IIOMetadataNode root = (IIOMetadataNode)metadata.getAsTree( formatName );
			IIOMetadataNode physNode = getFirstPhysNodeCreatingAndAddingIfNecessary( root );
			physNode.setAttribute( "pixelsPerUnitXAxis", pixelsPerMeterText );
			physNode.setAttribute( "pixelsPerUnitYAxis", pixelsPerMeterText );
			physNode.setAttribute( "unitSpecifier", "meter" );
			metadata.setFromTree( formatName, root );

			List<BufferedImage> thumbnails = null;
			IIOImage iioImage = new IIOImage( image, thumbnails, metadata );
			imageWriter.setOutput( ios );
			imageWriter.write( iioImage );
			ios.flush();
			imageWriter.setOutput( null );
		} else {
			throw new RuntimeException( "no image writers for png" );
		}
	}

	public static void write( RenderedImage image, int dpi, File file ) throws IOException {
		ImageOutputStream ios = ImageIO.createImageOutputStream( file );
		try {
			write( image, dpi, ios );
		} finally {
			ios.close();
		}
	}

	public static void main( String[] args ) throws IOException {
		File normalFile = new File( FileUtilities.getDefaultDirectory(), "test.png" );
		File dpiFile = new File( FileUtilities.getDefaultDirectory(), "test300.png" );
		BufferedImage image = new BufferedImage( 1200, 600, BufferedImage.TYPE_INT_RGB );

		//test to make sure not messing up future image io writes
		write( image, 300, dpiFile );
		ImageIO.write( image, "png", normalFile );

	}
}
