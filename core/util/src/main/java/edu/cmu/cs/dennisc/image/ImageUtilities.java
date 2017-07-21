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
public class ImageUtilities {
	public static final String PNG_CODEC_NAME = "png";
	public static final String JPEG_CODEC_NAME = "jpeg";
	public static final String BMP_CODEC_NAME = "bmp";
	public static final String GIF_CODEC_NAME = "gif";
	//	public static final String TIFF_CODEC_NAME = "tiff";
	public static final String TGA_CODEC_NAME = "tga";

	private static final String[] s_codecNames = { PNG_CODEC_NAME, JPEG_CODEC_NAME, BMP_CODEC_NAME, GIF_CODEC_NAME, /* TIFF_CODEC_NAME, */TGA_CODEC_NAME };
	private static final java.util.Map<String, String[]> s_codecNameToExtensionsMap;
	private static final java.util.Map<String, String> s_extensionToCodecNameMap;

	static {
		String[] pngExtensions = { "png" };
		String[] jpegExtensions = { "jpeg", "jpg" };
		String[] bmpExtensions = { "bmp" };
		String[] gifExtensions = { "gif" };
		// String[] tiffExtensions = { "tiff", "tif" };
		String[] tgaExtensions = { "tga" };
		s_codecNameToExtensionsMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		s_codecNameToExtensionsMap.put( PNG_CODEC_NAME, pngExtensions );
		s_codecNameToExtensionsMap.put( JPEG_CODEC_NAME, jpegExtensions );
		s_codecNameToExtensionsMap.put( BMP_CODEC_NAME, bmpExtensions );
		s_codecNameToExtensionsMap.put( GIF_CODEC_NAME, gifExtensions );
		//s_codecNameToExtensionsMap.put(TIFF_CODEC_NAME, tiffExtensions);
		s_codecNameToExtensionsMap.put( TGA_CODEC_NAME, tgaExtensions );

		s_extensionToCodecNameMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		for( String key : s_codecNameToExtensionsMap.keySet() ) {
			String[] value = s_codecNameToExtensionsMap.get( key );
			for( String element : value ) {
				s_extensionToCodecNameMap.put( element, key );
			}
		}
	}

	public static String[] accessCodecNames() {
		return s_codecNames;
	}

	private static javax.swing.filechooser.FileFilter s_fileFilter = new javax.swing.filechooser.FileFilter() {
		@Override
		public boolean accept( java.io.File file ) {
			return file.isDirectory() || isAcceptable( file );
		}

		@Override
		public String getDescription() {
			StringBuffer sb = new StringBuffer();
			sb.append( "Image (" );
			for( String key : s_codecNameToExtensionsMap.keySet() ) {
				String[] value = s_codecNameToExtensionsMap.get( key );
				for( String element : value ) {
					sb.append( "*." );
					sb.append( element );
					sb.append( ";" );
				}
			}
			sb.append( ")" );
			return sb.toString();
		}
	};

	public static javax.swing.filechooser.FileFilter accessFileFilter() {
		return s_fileFilter;
	}

	public static boolean isAcceptable( String path ) {
		String codecName = getCodecNameForExtension( edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( path ) );
		return codecName != null;
	}

	public static boolean isAcceptable( java.io.File file ) {
		return isAcceptable( file.getName() );
	}

	public static String[] getExtensionsForCodecName( String codecName ) {
		return s_codecNameToExtensionsMap.get( codecName );
	}

	public static String getCodecNameForExtension( String extension ) {
		if( extension != null ) {
			return s_extensionToCodecNameMap.get( extension.toLowerCase( java.util.Locale.ENGLISH ) );
		} else {
			return null;
		}
	}

	public static java.awt.image.BufferedImage read( String path ) throws java.io.IOException {
		return read( new java.io.File( path ) );
	}

	public static java.awt.image.BufferedImage read( String path, javax.imageio.ImageReadParam imageReadParam ) throws java.io.IOException {
		return read( new java.io.File( path ), imageReadParam );
	}

	public static java.awt.image.BufferedImage read( java.io.File file ) throws java.io.IOException {
		return read( file, null );
	}

	public static java.awt.image.BufferedImage read( java.io.File file, javax.imageio.ImageReadParam imageReadParam ) throws java.io.IOException {
		String extension = edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( file );
		String codecName = getCodecNameForExtension( extension );
		if( codecName != null ) {
			try {
				java.awt.image.BufferedImage rv;
				java.io.FileInputStream fis = new java.io.FileInputStream( file );
				try {
					rv = read( codecName, fis, imageReadParam );
				} finally {
					fis.close();
				}
				return rv;
			} catch( RuntimeException re ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file ) );
				edu.cmu.cs.dennisc.print.PrintUtilities.accessPrintStream().flush();
				throw re;
			}
		} else {
			throw new RuntimeException( "Could not find codec for extension: " + extension );
		}
	}

	public static java.awt.image.BufferedImage read( java.net.URL url ) throws java.io.IOException {
		return read( url, null );
	}

	public static java.awt.image.BufferedImage read( java.net.URL url, javax.imageio.ImageReadParam imageReadParam ) throws java.io.IOException {
		//todo: net.URLUtilities?
		String extension = edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( url.getPath() );
		String codecName = getCodecNameForExtension( extension );
		if( codecName != null ) {
			java.awt.image.BufferedImage rv;
			java.io.InputStream is = url.openStream();
			try {
				rv = read( codecName, is, imageReadParam );
			} catch( NoClassDefFoundError ncdfe ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( url );
				rv = null;
			} finally {
				is.close();
			}
			return rv;
		} else {
			throw new RuntimeException( "Could not find codec for extension: " + extension );
		}
	}

	public static java.awt.image.BufferedImage read( String codecName, java.io.InputStream inputStream ) throws java.io.IOException {
		return read( codecName, inputStream, null );
	}

	public static java.awt.image.BufferedImage read( String codecName, java.io.InputStream inputStream, javax.imageio.ImageReadParam imageReadParam ) throws java.io.IOException {
		java.io.BufferedInputStream bufferedInputStream;
		if( inputStream instanceof java.io.BufferedInputStream ) {
			bufferedInputStream = (java.io.BufferedInputStream)inputStream;
		} else {
			bufferedInputStream = new java.io.BufferedInputStream( inputStream );
		}
		if( codecName.equals( TGA_CODEC_NAME ) ) {
			return TgaUtilities.readTGA( bufferedInputStream );
			//		} else if (codecName.equals(TIFF_CODEC_NAME)) {
			//			return readTIFF(bufferedInputStream, null);
		} else {
			return javax.imageio.ImageIO.read( bufferedInputStream );
		}
	}

	public static void write( String path, java.awt.Image image ) throws java.io.IOException {
		write( path, image, null );
	}

	public static void write( String path, java.awt.Image image, javax.imageio.ImageReadParam imageWriteParam ) throws java.io.IOException {
		write( new java.io.File( path ), image, imageWriteParam );
	}

	public static void write( java.io.File file, java.awt.Image image ) throws java.io.IOException {
		write( file, image, null );
	}

	public static void write( java.io.File file, java.awt.Image image, javax.imageio.ImageReadParam imageWriteParam ) throws java.io.IOException {
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		String extension = edu.cmu.cs.dennisc.java.io.FileUtilities.getExtension( file );
		String codecName = getCodecNameForExtension( extension );
		if( codecName != null ) {
			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
			try {
				write( codecName, fos, image, imageWriteParam );
			} finally {
				fos.close();
			}
		} else {
			throw new RuntimeException( "Could not find codec for extension: " + extension );
		}
	}

	public static void write( String codecName, java.io.OutputStream outputStream, java.awt.Image image ) throws java.io.IOException {
		write( codecName, outputStream, image, null );
	}

	public static void write( String codecName, java.io.OutputStream outputStream, java.awt.Image image, javax.imageio.ImageReadParam imageWriteParam ) throws java.io.IOException {
		int width = ImageUtilities.getWidth( image );
		int height = ImageUtilities.getHeight( image );

		java.awt.image.RenderedImage renderedImage;

		if( codecName.equals( JPEG_CODEC_NAME ) ) {
			java.awt.image.BufferedImage bufferedImageBGR = null;
			if( image instanceof java.awt.image.BufferedImage ) {
				java.awt.image.BufferedImage bufferedImage = (java.awt.image.BufferedImage)image;
				if( bufferedImage.getType() == java.awt.image.BufferedImage.TYPE_3BYTE_BGR ) {
					bufferedImageBGR = bufferedImage;
				}
			}

			if( bufferedImageBGR != null ) {
				// pass
			} else {
				java.awt.Image originalImage = image;
				bufferedImageBGR = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_3BYTE_BGR );
				java.awt.Graphics g = image.getGraphics();
				g.drawImage( originalImage, 0, 0, accessImageObserver() );
				// todo: investigate - does dispose ensure the image is finished
				// drawing?
				g.dispose();
			}

			image = bufferedImageBGR;
		}
		if( image instanceof java.awt.image.RenderedImage ) {
			renderedImage = (java.awt.image.RenderedImage)image;
		} else {
			int[] pixels = ImageUtilities.getPixels( image, width, height );
			java.awt.image.BufferedImage bufferedImage = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			bufferedImage.setRGB( 0, 0, width, height, pixels, 0, width );
			renderedImage = bufferedImage;
		}
		//try {
		javax.imageio.ImageIO.write( renderedImage, codecName, outputStream );
		outputStream.flush();
		//} catch( IndexOutOfBoundsException ioobe ) {
		//todo: throw new IoException???
		//}

		// if( imageEncodeParam == null ) {
		// if( codecName.equals( PNG_CODEC_NAME ) ) {
		// imageEncodeParam =
		// edu.cmu.cs.dennisc.image.codec.PNGEncodeParam.getDefaultEncodeParam(
		// renderedImage );
		// }
		// }
		// java.io.BufferedOutputStream bufferedOutputStream;
		// if( outputStream instanceof java.io.BufferedOutputStream ) {
		// bufferedOutputStream = (java.io.BufferedOutputStream)outputStream;
		// } else {
		// bufferedOutputStream = new java.io.BufferedOutputStream( outputStream
		// );
		// }
		//
		// edu.cmu.cs.dennisc.image.codec.ImageEncoder imageEncoder =
		// edu.cmu.cs.dennisc.image.codec.ImageCodec.createImageEncoder(
		// codecName, bufferedOutputStream, imageEncodeParam );
		// try {
		// imageEncoder.encode( renderedImage );
		// bufferedOutputStream.flush();
		// } catch( java.io.IOException ioe ) {
		// throw new RuntimeException( ioe );
		// }
	}

	public static byte[] writeToByteArray( String codecName, java.awt.Image image, javax.imageio.ImageReadParam imageWriteParam ) throws java.io.IOException {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		write( codecName, baos, image, imageWriteParam );
		return baos.toByteArray();
	}

	public static byte[] writeToByteArray( String codecName, java.awt.Image image ) throws java.io.IOException {
		return writeToByteArray( codecName, image, null );
	}

	private static java.awt.MediaTracker s_mediaTracker = new java.awt.MediaTracker( new java.awt.Panel() );

	private static java.awt.image.ImageObserver s_imageObserver = new java.awt.image.ImageObserver() {
		@Override
		public boolean imageUpdate( java.awt.Image image, int infoflags, int x, int y, int width, int height ) {
			return true;
		}
	};

	public static java.awt.image.ImageObserver accessImageObserver() {
		return s_imageObserver;
	}

	private static void waitForImage( java.awt.Image image ) {
		s_mediaTracker.addImage( image, 0 );
		try {
			s_mediaTracker.waitForID( 0 );
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		} finally {
			s_mediaTracker.removeImage( image );
		}
	}

	public static int getWidth( java.awt.Image image ) {
		waitForImage( image );
		return image.getWidth( s_imageObserver );
	}

	public static int getHeight( java.awt.Image image ) {
		waitForImage( image );
		return image.getHeight( s_imageObserver );
	}

	public static int[] getPixels( java.awt.Image image, int width, int height ) {
		int[] pixels = new int[ width * height ];
		java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber( image, 0, 0, width, height, pixels, 0, width );
		try {
			pg.grabPixels();
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		}
		if( ( pg.getStatus() & java.awt.image.ImageObserver.ABORT ) != 0 ) {
			throw new RuntimeException( "image fetch aborted or errored" );
		}
		return pixels;
	}

	public static java.awt.image.BufferedImage createBufferedImage( java.awt.Image image, int imageType ) {
		int width = getWidth( image );
		int height = getHeight( image );
		// int type;
		// if( isAlphaBlended ) {
		// type = java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
		// } else {
		// type = java.awt.image.BufferedImage.TYPE_INT_ARGB;
		// }
		java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage( width, height, imageType );
		java.awt.Graphics g = bi.getGraphics();
		g.drawImage( image, 0, 0, accessImageObserver() );
		g.dispose();
		return bi;
	}

	public static java.awt.image.BufferedImage createAlphaMaskedImage( java.awt.Image image, edu.cmu.cs.dennisc.java.awt.Painter<Void> painter, float alpha ) {
		int width = getWidth( image );
		int height = getHeight( image );
		java.awt.image.BufferedImage rv = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
		java.awt.Graphics2D g2 = rv.createGraphics();
		g2.drawImage( image, 0, 0, null );

		java.awt.image.BufferedImage alphaImage = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
		java.awt.Graphics2D ag2 = alphaImage.createGraphics();
		painter.paint( ag2, null, width, height );
		ag2.dispose();

		g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.DST_IN, alpha ) );
		g2.drawImage( alphaImage, 0, 0, null );
		g2.dispose();
		return rv;
	}

	public static java.awt.image.BufferedImage createAlphaMaskedImage( java.awt.Image image, edu.cmu.cs.dennisc.java.awt.Painter<Void> painter ) {
		return createAlphaMaskedImage( image, painter, 1.0f );
	}
}
