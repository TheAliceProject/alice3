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
package edu.cmu.cs.dennisc.codec;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author Dennis Cosgrove
 */
public abstract class CodecUtilities {
	public static <T extends BinaryEncodableAndDecodable> Constructor<T> getPublicDecodeConstructor( Class<T> cls, Class<?>[] parameterTypes ) throws NoSuchMethodException {
		return cls.getConstructor( parameterTypes );
	}

	public static <T extends BinaryEncodableAndDecodable> Constructor<T> getPublicDecodeConstructor( String className, Class<?>[] parameterTypes ) throws ClassNotFoundException, NoSuchMethodException {
		Class<T> cls = (Class<T>)ClassUtilities.forName( className );
		return getPublicDecodeConstructor( cls, parameterTypes );
	}

	//	public static <T extends BinaryEncodableAndDecodable > java.lang.reflect.Constructor< T > getPublicDecodeConstructor( T instance ) throws NoSuchMethodException {
	//		return getPublicDecodeConstructor( (Class<T>)instance.getClass() );
	//	}

	@Deprecated public static boolean isDebugDesired = false;
	private static final String DEFAULT_ZIP_ENTRY_NAME = "edu.cmu.cs.dennisc.codec.CodecUtilities.DEFAULT_ZIP_ENTRY_NAME";

	private CodecUtilities() {
		throw new AssertionError();
	}

	public static <E extends BinaryEncodableAndDecodable> E decodeBinary( InputStream is, Class<E> cls ) {
		BinaryDecoder binaryDecoder;
		if( isDebugDesired ) {
			binaryDecoder = new DebugInputStreamBinaryDecoder( is );
		} else {
			binaryDecoder = new InputStreamBinaryDecoder( is );
		}
		return (E)binaryDecoder.decodeBinaryEncodableAndDecodable(/* cls */);
	}

	public static <E extends BinaryEncodableAndDecodable> E decodeBinary( File file, Class<E> cls ) {
		try {
			FileInputStream fis = new FileInputStream( file );
			try {
				return decodeBinary( fis, cls );
			} finally {
				fis.close();
			}
		} catch( FileNotFoundException fnfe ) {
			throw new RuntimeException( fnfe );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static <E extends BinaryEncodableAndDecodable> E decodeBinary( String path, Class<E> cls ) {
		if( FileUtilities.isExtensionAmoung( path, "zip" ) ) {
			return decodeZippedBinary( path, DEFAULT_ZIP_ENTRY_NAME, cls );
		} else {
			return decodeBinary( new File( path ), cls );
		}
	}

	public static <E extends BinaryEncodableAndDecodable> E decodeZippedBinary( ZipFile file, ZipEntry entry, Class<E> cls ) {
		try {
			InputStream is = file.getInputStream( entry );
			try {
				return decodeBinary( is, cls );
			} finally {
				is.close();
			}
		} catch( FileNotFoundException fnfe ) {
			throw new RuntimeException( fnfe );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static <E extends BinaryEncodableAndDecodable> E decodeZippedBinary( String path, String entryName, Class<E> cls ) {
		try {
			ZipFile file = new ZipFile( path );
			ZipEntry entry = file.getEntry( entryName );
			return decodeZippedBinary( file, entry, cls );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	//	public static BinaryEncodableAndDecodable decodeBinary( BinaryEncodableAndDecodable rv, java.io.InputStream is ) {
	//		BinaryDecoder binaryDecoder = new InputStreamBinaryDecoder( is );
	//		return binaryDecoder.decodeBinaryEncodableAndDecodable( rv );
	//	}
	//	public static BinaryEncodableAndDecodable decodeBinary( BinaryEncodableAndDecodable rv, java.io.File file ) {
	//		try {
	//			java.io.FileInputStream fis = new java.io.FileInputStream( file );
	//			try {
	//				return decodeBinary( rv, fis );
	//			} finally {
	//				fis.close();
	//			}
	//		} catch( java.io.FileNotFoundException fnfe ) {
	//			throw new RuntimeException( fnfe );
	//		} catch( java.io.IOException ioe ) {
	//			throw new RuntimeException( ioe );
	//		}
	//	}
	//	
	//	public static BinaryEncodableAndDecodable decodeBinary( BinaryEncodableAndDecodable rv, String path ) {
	//		if( edu.cmu.cs.dennisc.java.io.FileUtilities.isExtensionAmoung( path, edu.cmu.cs.dennisc.equivalence.CaseSensitivityPolicy.INSENSITIVE, "zip" ) ) {
	//			return decodeZippedBinary( rv, path, DEFAULT_ZIP_ENTRY_NAME );
	//		} else {
	//			return decodeBinary( rv, new java.io.File( path ) );
	//		}
	//	}
	//	public static BinaryEncodableAndDecodable decodeBinary( BinaryEncodableAndDecodable rv, byte[] data ) {
	//		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( data );
	//		return decodeBinary(rv, bais);
	//	}
	public static <E extends BinaryEncodableAndDecodable> E decodeBinary( byte[] data, Class<E> cls ) {
		ByteArrayInputStream bais = new ByteArrayInputStream( data );
		return decodeBinary( bais, cls );
	}

	//	public static BinaryEncodableAndDecodable decodeZippedBinary( BinaryEncodableAndDecodable rv, java.util.zip.ZipFile file, java.util.zip.ZipEntry entry ) {
	//		try {
	//			java.io.InputStream is = file.getInputStream( entry );
	//			try {
	//				return decodeBinary( rv, is );
	//			} finally {
	//				is.close();
	//			}
	//		} catch( java.io.FileNotFoundException fnfe ) {
	//			throw new RuntimeException( fnfe );
	//		} catch( java.io.IOException ioe ) {
	//			throw new RuntimeException( ioe );
	//		}
	//	}
	//
	//	public static BinaryEncodableAndDecodable decodeZippedBinary( BinaryEncodableAndDecodable rv, String path, String entryName ) {
	//		try {
	//			java.util.zip.ZipFile file = new java.util.zip.ZipFile( path );
	//			java.util.zip.ZipEntry entry = file.getEntry( entryName );
	//			return decodeZippedBinary( rv, file, entry );
	//		} catch( java.io.IOException ioe ) {
	//			throw new RuntimeException( ioe );
	//		}
	//	}

	public static void encodeBinary( BinaryEncodableAndDecodable binaryEncodableAndDecodable, OutputStream os ) {
		BinaryEncoder binaryEncoder;
		if( isDebugDesired ) {
			binaryEncoder = new DebugOutputStreamBinaryEncoder( os );
		} else {
			binaryEncoder = new OutputStreamBinaryEncoder( os );
		}
		binaryEncoder.encode( binaryEncodableAndDecodable );
		binaryEncoder.flush();
	}

	public static void encodeBinary( BinaryEncodableAndDecodable binaryEncodableAndDecodable, File file ) {
		FileUtilities.createParentDirectoriesIfNecessary( file );
		try {
			FileOutputStream fos = new FileOutputStream( file );
			try {
				encodeBinary( binaryEncodableAndDecodable, fos );
			} finally {
				fos.close();
			}
		} catch( FileNotFoundException fnfe ) {
			throw new RuntimeException( fnfe );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static void encodeBinary( BinaryEncodableAndDecodable binaryEncodableAndDecodable, String path ) {
		if( FileUtilities.isExtensionAmoung( path, "zip" ) ) {
			encodeZippedBinary( binaryEncodableAndDecodable, path, DEFAULT_ZIP_ENTRY_NAME );
		} else {
			encodeBinary( binaryEncodableAndDecodable, new File( path ) );
		}
	}

	public static byte[] encodeBinary( BinaryEncodableAndDecodable binaryEncodableAndDecodable ) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		encodeBinary( binaryEncodableAndDecodable, baos );
		return baos.toByteArray();
	}

	public static void encodeZippedBinary( BinaryEncodableAndDecodable binaryEncodableAndDecodable, ZipOutputStream zos, ZipEntry entry ) {
		try {
			zos.putNextEntry( entry );
			encodeBinary( binaryEncodableAndDecodable, zos );
			zos.closeEntry();
			zos.flush();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static void encodeZippedBinary( BinaryEncodableAndDecodable binaryEncodableAndDecodable, OutputStream os, String entryName ) {
		ZipOutputStream zos = new ZipOutputStream( new BufferedOutputStream( os ) );
		ZipEntry entry = new ZipEntry( entryName );
		encodeZippedBinary( binaryEncodableAndDecodable, zos, entry );
		try {
			//todo?
			zos.close();
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static void encodeZippedBinary( BinaryEncodableAndDecodable binaryEncodableAndDecodable, File file, String entryName ) {
		FileUtilities.createParentDirectoriesIfNecessary( file );
		try {
			encodeZippedBinary( binaryEncodableAndDecodable, new FileOutputStream( file ), entryName );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static void encodeZippedBinary( BinaryEncodableAndDecodable binaryEncodableAndDecodable, String path, String entryName ) {
		encodeZippedBinary( binaryEncodableAndDecodable, new File( path ), entryName );
	}

	public static <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinary( InputStream is, Class<E> cls ) {
		BinaryDecoder binaryDecoder = new InputStreamBinaryDecoder( is );
		Map<Integer, ReferenceableBinaryEncodableAndDecodable> map = new HashMap<Integer, ReferenceableBinaryEncodableAndDecodable>();
		return (E)binaryDecoder.decodeReferenceableBinaryEncodableAndDecodable( /* cls, */map );
	}

	public static <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinary( File file, Class<E> cls ) {
		try {
			FileInputStream fis = new FileInputStream( file );
			try {
				return decodeReferenceableBinary( fis, cls );
			} finally {
				fis.close();
			}
		} catch( FileNotFoundException fnfe ) {
			throw new RuntimeException( fnfe );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinary( String path, Class<E> cls ) {
		if( FileUtilities.isExtensionAmoung( path, "zip" ) ) {
			return decodeZippedReferenceableBinary( path, DEFAULT_ZIP_ENTRY_NAME, cls );
		} else {
			return decodeReferenceableBinary( new File( path ), cls );
		}
	}

	public static <E extends ReferenceableBinaryEncodableAndDecodable> E decodeZippedReferenceableBinary( ZipFile file, ZipEntry entry, Class<E> cls ) {
		try {
			InputStream is = file.getInputStream( entry );
			try {
				return decodeReferenceableBinary( is, cls );
			} finally {
				is.close();
			}
		} catch( FileNotFoundException fnfe ) {
			throw new RuntimeException( fnfe );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static <E extends ReferenceableBinaryEncodableAndDecodable> E decodeZippedReferenceableBinary( String path, String entryName, Class<E> cls ) {
		try {
			ZipFile file = new ZipFile( path );
			ZipEntry entry = file.getEntry( entryName );
			return decodeZippedReferenceableBinary( file, entry, cls );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Deprecated
	public static ReferenceableBinaryEncodableAndDecodable decodeReferenceableBinary( ReferenceableBinaryEncodableAndDecodable rv, InputStream is ) {
		BinaryDecoder binaryDecoder = new InputStreamBinaryDecoder( is );
		Map<Integer, ReferenceableBinaryEncodableAndDecodable> map = new HashMap<Integer, ReferenceableBinaryEncodableAndDecodable>();
		return binaryDecoder.decodeReferenceableBinaryEncodableAndDecodable( rv, map );
	}

	@Deprecated
	public static ReferenceableBinaryEncodableAndDecodable decodeReferenceableBinary( ReferenceableBinaryEncodableAndDecodable rv, File file ) {
		try {
			FileInputStream fis = new FileInputStream( file );
			try {
				return decodeReferenceableBinary( rv, fis );
			} finally {
				fis.close();
			}
		} catch( FileNotFoundException fnfe ) {
			throw new RuntimeException( fnfe );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Deprecated
	public static ReferenceableBinaryEncodableAndDecodable decodeReferenceableBinary( ReferenceableBinaryEncodableAndDecodable rv, String path ) {
		if( FileUtilities.isExtensionAmoung( path, "zip" ) ) {
			return decodeZippedReferenceableBinary( rv, path, DEFAULT_ZIP_ENTRY_NAME );
		} else {
			return decodeReferenceableBinary( rv, new File( path ) );
		}
	}

	@Deprecated
	public static ReferenceableBinaryEncodableAndDecodable decodeZippedReferenceableBinary( ReferenceableBinaryEncodableAndDecodable rv, ZipFile file, ZipEntry entry ) {
		try {
			InputStream is = file.getInputStream( entry );
			try {
				return decodeReferenceableBinary( rv, is );
			} finally {
				is.close();
			}
		} catch( FileNotFoundException fnfe ) {
			throw new RuntimeException( fnfe );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	@Deprecated
	public static ReferenceableBinaryEncodableAndDecodable decodeZippedReferenceableBinary( ReferenceableBinaryEncodableAndDecodable rv, String path, String entryName ) {
		try {
			ZipFile file = new ZipFile( path );
			ZipEntry entry = file.getEntry( entryName );
			return decodeZippedReferenceableBinary( rv, file, entry );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	//	
	//	public static void encodeReferenceableBinary( ReferenceableBinaryEncodableAndDecodable referenceableBinaryEncodableAndDecodable, java.io.OutputStream os ) {
	//		BinaryEncoder binaryEncoder = new OutputStreamBinaryEncoder( os );
	//		java.util.Map< ReferenceableBinaryEncodableAndDecodable, Integer > map = new java.util.HashMap< ReferenceableBinaryEncodableAndDecodable, Integer >();
	//		binaryEncoder.encode( referenceableBinaryEncodableAndDecodable, map );
	//		binaryEncoder.flush();
	//	}
	//	public static void encodeReferenceableBinary( ReferenceableBinaryEncodableAndDecodable referenceableBinaryEncodableAndDecodable, java.io.File file ) {
	//		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
	//		try {
	//			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
	//			try {
	//				encodeReferenceableBinary( referenceableBinaryEncodableAndDecodable, fos );
	//			} finally {
	//				fos.close();
	//			}
	//		} catch( java.io.FileNotFoundException fnfe ) {
	//			throw new RuntimeException( fnfe );
	//		} catch( java.io.IOException ioe ) {
	//			throw new RuntimeException( ioe );
	//		}
	//	}
	//	
	//	public static void encodeReferenceableBinary( ReferenceableBinaryEncodableAndDecodable referenceableBinaryEncodableAndDecodable, String path ) {
	//		if( edu.cmu.cs.dennisc.java.io.FileUtilities.isExtensionAmoung( path, edu.cmu.cs.dennisc.equivalence.CaseSensitivityPolicy.INSENSITIVE, "zip" ) ) {
	//			encodeZippedReferenceableBinary( referenceableBinaryEncodableAndDecodable, path, DEFAULT_ZIP_ENTRY_NAME );
	//		} else {
	//			encodeReferenceableBinary( referenceableBinaryEncodableAndDecodable, new java.io.File( path ) );
	//		}
	//	}
	//
	//	public static void encodeZippedReferenceableBinary( ReferenceableBinaryEncodableAndDecodable referenceableBinaryEncodableAndDecodable, java.util.zip.ZipOutputStream zos, java.util.zip.ZipEntry entry ) {
	//		try {
	//			zos.putNextEntry( entry );
	//			encodeReferenceableBinary( referenceableBinaryEncodableAndDecodable, zos );
	//			zos.closeEntry();
	//		} catch( java.io.IOException ioe ) {
	//			throw new RuntimeException( ioe );
	//		}
	//	}
	//
	//	public static void encodeZippedReferenceableBinary( ReferenceableBinaryEncodableAndDecodable referenceableBinaryEncodableAndDecodable, java.io.OutputStream os, String entryName ) {
	//		java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream( new java.io.BufferedOutputStream( os ) );
	//		java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry( entryName );
	//		encodeZippedReferenceableBinary( referenceableBinaryEncodableAndDecodable, zos, entry );
	//		try {
	//			//todo?
	//			zos.close();
	//		} catch( java.io.IOException ioe ) {
	//			throw new RuntimeException( ioe );
	//		}
	//	}
	//	public static void encodeZippedReferenceableBinary( ReferenceableBinaryEncodableAndDecodable referenceableBinaryEncodableAndDecodable, java.io.File file, String entryName ) {
	//		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
	//		try {
	//			java.io.FileOutputStream fos = new java.io.FileOutputStream( file );
	//			try {
	//				encodeZippedReferenceableBinary( referenceableBinaryEncodableAndDecodable, fos, entryName );
	//			} finally {
	//				fos.flush();
	//				fos.close();
	//			}
	//		} catch( java.io.IOException ioe ) {
	//			throw new RuntimeException( ioe );
	//		}
	//	}
	//	public static void encodeZippedReferenceableBinary( ReferenceableBinaryEncodableAndDecodable referenceableBinaryEncodableAndDecodable, String path, String entryName ) {
	//		encodeZippedReferenceableBinary( referenceableBinaryEncodableAndDecodable, new java.io.File( path ), entryName );
	//	}
}
