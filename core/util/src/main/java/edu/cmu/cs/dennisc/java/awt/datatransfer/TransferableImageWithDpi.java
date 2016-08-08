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

/**
 * @author Dennis Cosgrove
 */
public class TransferableImageWithDpi implements java.awt.datatransfer.Transferable {
	private static final java.awt.datatransfer.DataFlavor PNG_IMAGE_FLAVOR = new java.awt.datatransfer.DataFlavor( "image/png", "PNG Image" );

	private static final java.awt.datatransfer.DataFlavor[] DPI_DATA_FLAVORS = {
			//PNG_IMAGE_FLAVOR,
			java.awt.datatransfer.DataFlavor.javaFileListFlavor,
			java.awt.datatransfer.DataFlavor.imageFlavor
	};

	private static final java.awt.datatransfer.DataFlavor[] IMAGE_ONLY_DATA_FLAVORS = {
			java.awt.datatransfer.DataFlavor.imageFlavor
	};

	private final java.awt.Image image;
	private final Integer dpi;

	public TransferableImageWithDpi( java.awt.Image image, Integer dpi ) {
		assert image != null : this;
		this.image = image;
		this.dpi = dpi;
	}

	@Override
	public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
		return this.dpi != null ? DPI_DATA_FLAVORS : IMAGE_ONLY_DATA_FLAVORS;
	}

	@Override
	public boolean isDataFlavorSupported( java.awt.datatransfer.DataFlavor flavor ) {
		return java.util.Arrays.asList( this.getTransferDataFlavors() ).contains( flavor );
	}

	private java.awt.image.RenderedImage getRenderedImage() {
		if( image instanceof java.awt.image.RenderedImage ) {
			return (java.awt.image.RenderedImage)image;
		} else {
			int width = edu.cmu.cs.dennisc.image.ImageUtilities.getWidth( image );
			int height = edu.cmu.cs.dennisc.image.ImageUtilities.getHeight( image );
			int[] pixels = edu.cmu.cs.dennisc.image.ImageUtilities.getPixels( image, width, height );
			java.awt.image.BufferedImage bufferedImage = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			bufferedImage.setRGB( 0, 0, width, height, pixels, 0, width );
			return bufferedImage;
		}
	}

	private java.io.File createFile( String ext ) throws java.io.IOException {
		java.awt.image.RenderedImage renderedImage = this.getRenderedImage();
		java.io.File file = java.io.File.createTempFile( "clipboard", ext );
		if( ( this.dpi != null ) && "png".equals( ext ) ) {
			edu.cmu.cs.dennisc.javax.imageio.PngUtilities.write( renderedImage, this.dpi, file );
		} else {
			javax.imageio.ImageIO.write( renderedImage, ext, file );
		}
		return file;
	}

	private java.io.InputStream createStream( java.awt.datatransfer.DataFlavor flavor ) throws java.io.IOException {
		java.awt.image.RenderedImage renderedImage = this.getRenderedImage();
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		javax.imageio.ImageIO.write( renderedImage, flavor.getSubType(), baos );
		baos.flush();
		return new java.io.ByteArrayInputStream( baos.toByteArray() );
	}

	@Override
	public Object getTransferData( java.awt.datatransfer.DataFlavor flavor ) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
		if( java.awt.datatransfer.DataFlavor.javaFileListFlavor.equals( flavor ) ) {
			String ext = "png";
			return edu.cmu.cs.dennisc.java.util.Lists.newArrayList( this.createFile( ext ) );
		} else if( java.awt.datatransfer.DataFlavor.imageFlavor.equals( flavor ) ) {
			return image;
		} else if( PNG_IMAGE_FLAVOR.equals( flavor ) ) {
			return this.createStream( flavor );
		} else {
			throw new java.awt.datatransfer.UnsupportedFlavorException( flavor );
		}
	}
}
