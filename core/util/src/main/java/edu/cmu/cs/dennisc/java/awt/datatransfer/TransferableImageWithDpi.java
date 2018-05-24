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
package edu.cmu.cs.dennisc.java.awt.datatransfer;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.javax.imageio.PngUtilities;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author Dennis Cosgrove
 */
public class TransferableImageWithDpi implements Transferable {
	private static final DataFlavor PNG_IMAGE_FLAVOR = new DataFlavor( "image/png", "PNG Image" );

	private static final DataFlavor[] DPI_DATA_FLAVORS = {
			//PNG_IMAGE_FLAVOR,
			DataFlavor.javaFileListFlavor,
			DataFlavor.imageFlavor
	};

	private static final DataFlavor[] IMAGE_ONLY_DATA_FLAVORS = {
			DataFlavor.imageFlavor
	};

	private final Image image;
	private final Integer dpi;

	public TransferableImageWithDpi( Image image, Integer dpi ) {
		assert image != null : this;
		this.image = image;
		this.dpi = dpi;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return this.dpi != null ? DPI_DATA_FLAVORS : IMAGE_ONLY_DATA_FLAVORS;
	}

	@Override
	public boolean isDataFlavorSupported( DataFlavor flavor ) {
		return Arrays.asList( this.getTransferDataFlavors() ).contains( flavor );
	}

	private RenderedImage getRenderedImage() {
		if( image instanceof RenderedImage ) {
			return (RenderedImage)image;
		} else {
			int width = ImageUtilities.getWidth( image );
			int height = ImageUtilities.getHeight( image );
			int[] pixels = ImageUtilities.getPixels( image, width, height );
			BufferedImage bufferedImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
			bufferedImage.setRGB( 0, 0, width, height, pixels, 0, width );
			return bufferedImage;
		}
	}

	private File createFile( String ext ) throws IOException {
		RenderedImage renderedImage = this.getRenderedImage();
		File file = File.createTempFile( "clipboard", ext );
		if( ( this.dpi != null ) && "png".equals( ext ) ) {
			PngUtilities.write( renderedImage, this.dpi, file );
		} else {
			ImageIO.write( renderedImage, ext, file );
		}
		return file;
	}

	private InputStream createStream( DataFlavor flavor ) throws IOException {
		RenderedImage renderedImage = this.getRenderedImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( renderedImage, flavor.getSubType(), baos );
		baos.flush();
		return new ByteArrayInputStream( baos.toByteArray() );
	}

	@Override
	public Object getTransferData( DataFlavor flavor ) throws UnsupportedFlavorException, IOException {
		if( DataFlavor.javaFileListFlavor.equals( flavor ) ) {
			String ext = "png";
			return Lists.newArrayList( this.createFile( ext ) );
		} else if( DataFlavor.imageFlavor.equals( flavor ) ) {
			return image;
		} else if( PNG_IMAGE_FLAVOR.equals( flavor ) ) {
			return this.createStream( flavor );
		} else {
			throw new UnsupportedFlavorException( flavor );
		}
	}
}
