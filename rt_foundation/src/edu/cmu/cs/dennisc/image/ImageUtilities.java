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
public class ImageUtilities {
	public static final String PNG_CODEC_NAME = "png";
	public static final String JPEG_CODEC_NAME = "jpeg";
	public static final String BMP_CODEC_NAME = "bmp";
	public static final String GIF_CODEC_NAME = "gif";
//	public static final String TIFF_CODEC_NAME = "tiff";
	public static final String TGA_CODEC_NAME = "tga";

	private static final String[] s_codecNames = { PNG_CODEC_NAME, JPEG_CODEC_NAME, BMP_CODEC_NAME, GIF_CODEC_NAME, /*TIFF_CODEC_NAME,*/ TGA_CODEC_NAME };
	private static java.util.HashMap< String, String[] > s_codecNameToExtensionsMap = null;
	private static java.util.HashMap< String, String > s_extensionToCodecNameMap = null;

	static {
		String[] pngExtensions = { "png" };
		String[] jpegExtensions = { "jpeg", "jpg" };
		String[] bmpExtensions = { "bmp" };
		String[] gifExtensions = { "gif" };
		// String[] tiffExtensions = { "tiff", "tif" };
		String[] tgaExtensions = { "tga" };
		s_codecNameToExtensionsMap = new java.util.HashMap< String, String[] >();
		s_codecNameToExtensionsMap.put( PNG_CODEC_NAME, pngExtensions );
		s_codecNameToExtensionsMap.put( JPEG_CODEC_NAME, jpegExtensions );
		s_codecNameToExtensionsMap.put( BMP_CODEC_NAME, bmpExtensions );
		s_codecNameToExtensionsMap.put( GIF_CODEC_NAME, gifExtensions );
		//s_codecNameToExtensionsMap.put(TIFF_CODEC_NAME, tiffExtensions);
		s_codecNameToExtensionsMap.put( TGA_CODEC_NAME, tgaExtensions );

		s_extensionToCodecNameMap = new java.util.HashMap< String, String >();
		for( String key : s_codecNameToExtensionsMap.keySet() ) {
			String[] value = s_codecNameToExtensionsMap.get( key );
			for( int i = 0; i < value.length; i++ ) {
				s_extensionToCodecNameMap.put( value[ i ], key );
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
				for( int i = 0; i < value.length; i++ ) {
					sb.append( "*." );
					sb.append( value[ i ] );
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
		String codecName = getCodecNameForExtension( edu.cmu.cs.dennisc.io.FileUtilities.getExtension( path ) );
		return codecName != null;
	}

	public static boolean isAcceptable( java.io.File file ) {
		return isAcceptable( file.getName() );
	}

	public static String[] getExtensionsForCodecName( String codecName ) {
		return s_codecNameToExtensionsMap.get( codecName );
	}

	public static String getCodecNameForExtension( String extension ) {
		return s_extensionToCodecNameMap.get( extension.toLowerCase() );
	}

	public static java.awt.image.BufferedImage read( String path ) {
		return read( new java.io.File( path ) );
	}

	public static java.awt.image.BufferedImage read( String path, javax.imageio.ImageReadParam imageReadParam ) {
		return read( new java.io.File( path ), imageReadParam );
	}

	public static java.awt.image.BufferedImage read( java.io.File file ) {
		return read( file, null );
	}

	public static java.awt.image.BufferedImage read( java.io.File file, javax.imageio.ImageReadParam imageReadParam ) {
		String extension = edu.cmu.cs.dennisc.io.FileUtilities.getExtension( file );
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
			} catch( java.io.FileNotFoundException fnfe ) {
				throw new RuntimeException( fnfe );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		} else {
			throw new RuntimeException( "Could not find codec for extension: " + extension );
		}
	}
	public static java.awt.image.BufferedImage read( java.net.URL url ) {
		return read( url, null );
	}

	public static java.awt.image.BufferedImage read( java.net.URL url, javax.imageio.ImageReadParam imageReadParam ) {
		//todo: net.URLUtilities?
		String extension = edu.cmu.cs.dennisc.io.FileUtilities.getExtension( url.getPath() );
		String codecName = getCodecNameForExtension( extension );
		if( codecName != null ) {
			try {
				java.awt.image.BufferedImage rv;
				java.io.InputStream is = url.openStream();
				try {
					rv = read( codecName, is, imageReadParam );
				} finally {
					is.close();
				}
				return rv;
			} catch( java.io.FileNotFoundException fnfe ) {
				throw new RuntimeException( fnfe );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		} else {
			throw new RuntimeException( "Could not find codec for extension: " + extension );
		}
	}

	public static java.awt.image.BufferedImage read( String codecName, java.io.InputStream inputStream ) {
		return read( codecName, inputStream, null );
	}

	// private static java.awt.image.BufferedImage readTIFF(
	// java.io.BufferedInputStream bufferedInputStream,
	// edu.cmu.cs.dennisc.image.codec.ImageDecodeParam imageDecodeParam ) throws
	// java.io.IOException {
	// edu.cmu.cs.dennisc.image.codec.ImageDecoder imageDecoder =
	// edu.cmu.cs.dennisc.image.codec.ImageCodec.createImageDecoder( "tiff",
	// bufferedInputStream, imageDecodeParam );
	// java.awt.image.RenderedImage renderedImage =
	// imageDecoder.decodeAsRenderedImage();
	//
	// if( renderedImage instanceof java.awt.image.BufferedImage ) {
	// return (java.awt.image.BufferedImage)renderedImage;
	// } else {
	// java.awt.image.Raster raster = renderedImage.getData();
	// java.awt.image.ColorModel colorModel = renderedImage.getColorModel();
	// java.util.Hashtable< Object, Object > properties = null;
	// String[] propertyNames = renderedImage.getPropertyNames();
	// if( propertyNames != null ) {
	// properties = new java.util.Hashtable< Object, Object >();
	// for( int i = 0; i < propertyNames.length; i++ ) {
	// String propertyName = propertyNames[ i ];
	// properties.put( propertyName, renderedImage.getProperty( propertyName )
	// );
	// }
	// }
	// java.awt.image.WritableRaster writableRaster;
	// if( raster instanceof java.awt.image.WritableRaster ) {
	// writableRaster = (java.awt.image.WritableRaster)raster;
	// } else {
	// writableRaster = raster.createCompatibleWritableRaster();
	// }
	// return new java.awt.image.BufferedImage( renderedImage.getColorModel(),
	// writableRaster, colorModel.isAlphaPremultiplied(), properties );
	// }
	// }

	private static short getShort( byte[] array, int offset ) {
		int low = 0xFF & array[ offset + 0 ];
		;
		int high = 0xFF & array[ offset + 1 ];
		high <<= 8;
		return (short)(low | high);
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
			;
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

	private static java.awt.image.BufferedImage readTGA( java.io.BufferedInputStream bufferedInputStream ) throws java.io.IOException {
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

		boolean isHorizontalFlipped = (descriptor & HORIZONTAL_FLIP_MASK) != 0;
		boolean isVerticalFlipped = (descriptor & VERTICAL_FLIP_MASK) != 0;

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
				bufferedImage.setRGB( x, (height - 1) - y, pixel );
				offset += bytesPerPixel;
			}
		}
		return bufferedImage;
	}

	public static java.awt.image.BufferedImage read( String codecName, java.io.InputStream inputStream, javax.imageio.ImageReadParam imageReadParam ) {
		try {
			java.io.BufferedInputStream bufferedInputStream;
			if( inputStream instanceof java.io.BufferedInputStream ) {
				bufferedInputStream = (java.io.BufferedInputStream)inputStream;
			} else {
				bufferedInputStream = new java.io.BufferedInputStream( inputStream );
			}
			if( codecName.equals( TGA_CODEC_NAME ) ) {
				return readTGA( bufferedInputStream );
				//			} else if (codecName.equals(TIFF_CODEC_NAME)) {
				//				return readTIFF(bufferedInputStream, null);
			} else {
				return javax.imageio.ImageIO.read( bufferedInputStream );
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static void write( String path, java.awt.Image image ) {
		write( path, image, null );
	}

	public static void write( String path, java.awt.Image image, javax.imageio.ImageReadParam imageWriteParam ) {
		write( new java.io.File( path ), image, imageWriteParam );
	}

	public static void write( java.io.File file, java.awt.Image image ) {
		write( file, image, null );
	}

	public static void write( java.io.File file, java.awt.Image image, javax.imageio.ImageReadParam imageWriteParam ) {
		edu.cmu.cs.dennisc.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		String extension = edu.cmu.cs.dennisc.io.FileUtilities.getExtension( file );
		String codecName = getCodecNameForExtension( extension );
		if( codecName != null ) {
			try {
				java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
				try {
					write( codecName, fos, image, imageWriteParam );
				} finally {
					fos.close();
				}
			} catch( java.io.FileNotFoundException fnfe ) {
				throw new RuntimeException( fnfe );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		} else {
			throw new RuntimeException( "Could not find codec for extension: " + extension );
		}
	}

	public static void write( String codecName, java.io.OutputStream outputStream, java.awt.Image image ) {
		write( codecName, outputStream, image, null );
	}

	public static void write( String codecName, java.io.OutputStream outputStream, java.awt.Image image, javax.imageio.ImageReadParam imageWriteParam ) {
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
		try {
			javax.imageio.ImageIO.write( renderedImage, codecName, outputStream );
			outputStream.flush();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}

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

	private static java.awt.MediaTracker s_mediaTracker = new java.awt.MediaTracker( new java.awt.Panel() );

	private static java.awt.image.ImageObserver s_imageObserver = new java.awt.image.ImageObserver() {
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
		if( (pg.getStatus() & java.awt.image.ImageObserver.ABORT) != 0 ) {
			throw new RuntimeException( "image fetch aborted or errored" );
		}
		return pixels;
	}

	public static java.awt.image.BufferedImage constructBufferedImage( java.awt.Image image, int imageType ) {
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
}
