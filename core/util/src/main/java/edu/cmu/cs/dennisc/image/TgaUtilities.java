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
package edu.cmu.cs.dennisc.image;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class TgaUtilities {
	private TgaUtilities() {
		throw new Error();
	}

	private static short getShort( byte[] array, int offset ) {
		int low = 0xFF & array[ offset + 0 ];
		int high = 0xFF & array[ offset + 1 ];
		high <<= 8;
		return (short)( low | high );
	}

	private static int getPixel( byte[] array, int offset, int bytesPerPixel ) {
		int r;
		int g;
		int b;
		int a = 0xFF;

		if( bytesPerPixel == 1 ) {
			b = g = r = 0xFF & array[ offset ];
		} else if( bytesPerPixel == 2 ) {
			throw new RuntimeException();
		} else {
			b = 0xFF & array[ offset++ ];
			g = 0xFF & array[ offset++ ];
			r = 0xFF & array[ offset++ ];
			if( bytesPerPixel == 4 ) {
				a = 0xFF & array[ offset++ ];
			}
		}
		a <<= 24;
		r <<= 16;
		g <<= 8;
		b <<= 0;
		return a | r | g | b;
	}

	/*package-private*/static java.awt.image.BufferedImage readTGA( java.io.BufferedInputStream bufferedInputStream ) throws java.io.IOException {
		byte[] header = new byte[ 18 ];
		bufferedInputStream.read( header );

		byte idEntrySize = header[ 0 ];
		byte tgaColorMapType = header[ 1 ];
		byte tgaImageType = header[ 2 ];
		short width = getShort( header, 12 );
		short height = getShort( header, 14 );
		byte bitsPerPixel = header[ 16 ];
		byte descriptor = header[ 17 ];

		final byte HORIZONTAL_FLIP_MASK = 0x1 << 4;
		final byte VERTICAL_FLIP_MASK = 0x1 << 5;
		// final byte ALPHA_BITS_MASK = 0xF;

		boolean isHorizontalFlipped = ( descriptor & HORIZONTAL_FLIP_MASK ) != 0;
		boolean isVerticalFlipped = ( descriptor & VERTICAL_FLIP_MASK ) != 0;

		if( isHorizontalFlipped ) {
			throw new RuntimeException( "TODO: isHorizontalFlipped" );
		}
		if( isVerticalFlipped ) {
			throw new RuntimeException( "TODO: isVerticalFlipped" );
		}

		int bytesPerPixel;
		switch( bitsPerPixel ) {
		case 8:
			bytesPerPixel = 1;
			break;
		case 16:
			bytesPerPixel = 2;
			break;
		case 24:
			bytesPerPixel = 3;
			break;
		case 32:
			bytesPerPixel = 4;
			break;
		default:
			throw new RuntimeException( "TODO: handle bitsPerPixel = " + bitsPerPixel );
		}

		if( tgaColorMapType != 0 ) {
			throw new RuntimeException( "TODO: handle tgaColorMapType = " + tgaColorMapType );
		}

		java.awt.image.BufferedImage bufferedImage;
		switch( tgaImageType ) {
		case 2:
			bufferedImage = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			break;
		case 3:
			if( bytesPerPixel != 1 ) {
				throw new RuntimeException( "TODO: handle grey image with bytesPerPixel = " + bytesPerPixel );
			}
			bufferedImage = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_BYTE_GRAY );
			break;
		default:
			throw new RuntimeException( "TODO: handle tgaImageType = " + tgaImageType );
		}

		if( idEntrySize > 0 ) {
			byte[] idEntry = new byte[ idEntrySize ];
			bufferedInputStream.read( idEntry );
		}

		byte[] pixels = new byte[ width * height * bytesPerPixel ];
		bufferedInputStream.read( pixels );

		int offset = 0;
		for( int y = 0; y < height; y++ ) {
			for( int x = 0; x < width; x++ ) {
				int pixel = getPixel( pixels, offset, bytesPerPixel );
				bufferedImage.setRGB( x, ( height - 1 ) - y, pixel );
				offset += bytesPerPixel;
			}
		}
		return bufferedImage;
	}
}
