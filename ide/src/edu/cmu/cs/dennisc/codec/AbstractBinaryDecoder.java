/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package edu.cmu.cs.dennisc.codec;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractBinaryDecoder implements BinaryDecoder {
	//todo: handle null arrays
	private Object createArray( Class< ? > componentType ) {
		int length = this.decodeInt();
		if( length != -1 ) {
			return java.lang.reflect.Array.newInstance( componentType, length );
		} else {
			return null;
		}
	}
	public final boolean[] decodeBooleanArray() {
		boolean[] rv = (boolean[])createArray( Boolean.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeBoolean();
		}
		return rv;
	}

	private static final boolean DECODE_ONE_BYTE_AT_A_TIME = false;

//	private java.nio.ByteBuffer decodeByteBuffer( int bitsPerPrimative ) {
//		final int N = this.decodeInt() * (bitsPerPrimative/8);
//		boolean isReadOnly = this.decodeBoolean();
//		boolean isDirect = this.decodeBoolean();
//		boolean isBigEndian = this.decodeBoolean();
//		
//		if( isBigEndian ) {
//			//pass
//		} else {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "at the time of this authoring of this print statement, encoding should always be BIG_ENDIAN" );
//		}
//		java.nio.ByteBuffer rv;
////		java.nio.ByteOrder actualByteOrder;
////		if( isBigEndian ) {
////			actualByteOrder = java.nio.ByteOrder.BIG_ENDIAN;
////		} else {
////			actualByteOrder = java.nio.ByteOrder.LITTLE_ENDIAN;
////		}
//		if( isDirect ) {
//			rv = java.nio.ByteBuffer.allocateDirect( N );
//			rv = rv.order( ByteOrder.nativeOrder() );
//		} else {
//			rv = java.nio.ByteBuffer.allocate( N );
//		}
////		java.nio.ByteOrder desiredByteOrder = rv.order();
////		if( actualByteOrder.equals( desiredByteOrder ) ) {
////			//pass
////		} else {
////			rv.order( desiredByteOrder );
////			rv.rewind();
////		}
//		if( DECODE_ONE_BYTE_AT_A_TIME ) {
//			for( int i = 0; i < N; i++ ) {
//				rv.put( this.decodeByte() );
//			}
//		} else {
//			byte[] array = new byte[ N ];
//			this.decodeByteArray( array );
//			rv.put( array );
//		}
//		rv.rewind();
//
////		if( isReadOnly ) {
////			rv = rv.asReadOnlyBuffer();
////		}
//		return rv;
//	}
//		
//	public final java.nio.ByteBuffer decodeByteBuffer() {
//		return decodeByteBuffer( Byte.SIZE );
//	}
//	public final java.nio.CharBuffer decodeCharBuffer() {
//		return decodeByteBuffer( Character.SIZE ).asCharBuffer();
//	}
//	public final java.nio.ShortBuffer decodeShortBuffer() {
//		return decodeByteBuffer( Short.SIZE ).asShortBuffer();
//	}
//	public final java.nio.IntBuffer decodeIntBuffer() {
//		return decodeByteBuffer( Integer.SIZE ).asIntBuffer();
//	}
//	public final java.nio.LongBuffer decodeLongBuffer() {
//		return decodeByteBuffer( Long.SIZE ).asLongBuffer();
//	}
//	public final java.nio.FloatBuffer decodeFloatBuffer() {
//		return decodeByteBuffer( Float.SIZE ).asFloatBuffer();
//	}
//	public final java.nio.DoubleBuffer decodeDoubleBuffer() {
//		return decodeByteBuffer( Double.SIZE ).asDoubleBuffer();
//	}

//	private java.nio.ByteBuffer createByteBufferFromHeader( int bitsPerPrimative ) {
//		final int N = this.decodeInt() * (bitsPerPrimative / 8);
//		boolean isReadOnly = this.decodeBoolean();
//		boolean isDirect = this.decodeBoolean();
//		boolean isBigEndian = this.decodeBoolean();
//		java.nio.ByteBuffer rv;
////		java.nio.ByteOrder actualByteOrder;
////		if( isBigEndian ) {
////			actualByteOrder = java.nio.ByteOrder.BIG_ENDIAN;
////		} else {
////			actualByteOrder = java.nio.ByteOrder.LITTLE_ENDIAN;
////		}
//		if( isDirect ) {
//			rv = java.nio.ByteBuffer.allocateDirect( N );
//		} else {
//			rv = java.nio.ByteBuffer.allocate( N );
//		}
////		if( isReadOnly ) {
////			rv = rv.asReadOnlyBuffer();
////		}
//		return rv;
//	}
//
//	public final java.nio.ByteBuffer decodeByteBuffer() {
//		java.nio.ByteBuffer rv = createByteBufferFromHeader( Byte.SIZE );
//		byte[] array = new byte[ rv.limit() ];
//		this.decodeByteArray( array );
//		rv.put( array );
//		rv.rewind();
//		return rv;
//	}
//	public final java.nio.CharBuffer decodeCharBuffer() {
//		java.nio.CharBuffer rv = createByteBufferFromHeader( Character.SIZE ).asCharBuffer();
//		while( rv.hasRemaining() ) {
//			rv.put( this.decodeChar() );
//		}
//		rv.rewind();
//		return rv;
//	}
//	public final java.nio.ShortBuffer decodeShortBuffer() {
//		java.nio.ShortBuffer rv = createByteBufferFromHeader( Short.SIZE ).asShortBuffer();
//		while( rv.hasRemaining() ) {
//			rv.put( this.decodeShort() );
//		}
//		rv.rewind();
//		return rv;
//	}
//	public final java.nio.IntBuffer decodeIntBuffer() {
//		java.nio.ByteBuffer byteBuffer = createByteBufferFromHeader( Integer.SIZE );
//		byte[] array = new byte[ byteBuffer.limit() ];
//		this.decodeByteArray( array );
//		final int N = array.length;
//		byte temp;
//		for( int i=0; i<N; i+=4 ) {
//			temp = array[ i+0 ]; 
//			array[ i+0 ] = array[ i+3 ];
//			array[ i+3 ] = temp;
//			temp = array[ i+1 ]; 
//			array[ i+1 ] = array[ i+2 ];
//			array[ i+2 ] = temp;
//		}
//		byteBuffer.put( array );
//		byteBuffer.rewind();
////		byteBuffer.order( java.nio.ByteOrder.nativeOrder() );
//
//		java.nio.IntBuffer rv = byteBuffer.order( java.nio.ByteOrder.nativeOrder() ).asIntBuffer();
//		
////		while( rv.hasRemaining() ) {
////			edu.cmu.cs.dennisc.print.PrintUtilities.println( rv.get() );
////		}
////		rv.rewind();
//		
////		while( rv.hasRemaining() ) {
////			rv.put( this.decodeInt() );
////		}
////		rv.rewind();
//		
//		java.nio.IntBuffer srcBuffer = rv;
//
//		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
//		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( baos );
//		BufferUtilities.encode( encoder, srcBuffer, true );
//		byte[] bytes = baos.toByteArray();
//		
//		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( bytes );
//		edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( bais );
//		java.nio.IntBuffer dstBuffer = BufferUtilities.decodeIntBuffer( decoder, true );
//		
//		rv = dstBuffer;
//
//		return rv;
//	}
//	public final java.nio.LongBuffer decodeLongBuffer() {
//		java.nio.LongBuffer rv = createByteBufferFromHeader( Long.SIZE ).asLongBuffer();
//		while( rv.hasRemaining() ) {
//			rv.put( this.decodeLong() );
//		}
//		rv.rewind();
//		return rv;
//	}
//	public final java.nio.FloatBuffer decodeFloatBuffer() {
//		java.nio.FloatBuffer rv = createByteBufferFromHeader( Float.SIZE ).order( java.nio.ByteOrder.nativeOrder() ).asFloatBuffer();
//		while( rv.hasRemaining() ) {
//			rv.put( this.decodeFloat() );
//		}
//		rv.rewind();
//		java.nio.FloatBuffer srcBuffer = rv;
//
//		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
//		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( baos );
//		BufferUtilities.encode( encoder, srcBuffer, true );
//		byte[] bytes = baos.toByteArray();
//		
//		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( bytes );
//		edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( bais );
//		java.nio.FloatBuffer dstBuffer = BufferUtilities.decodeFloatBuffer( decoder, true );
//		
//		rv = dstBuffer;
//		return rv;
//	}
//	public final java.nio.DoubleBuffer decodeDoubleBuffer() {
//		java.nio.DoubleBuffer rv = createByteBufferFromHeader( Double.SIZE ).order( java.nio.ByteOrder.nativeOrder() ).asDoubleBuffer();
//		while( rv.hasRemaining() ) {
//			rv.put( this.decodeDouble() );
//		}
//		rv.rewind();
//		
//		java.nio.DoubleBuffer srcBuffer = rv;
//
//		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
//		edu.cmu.cs.dennisc.codec.BinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder( baos );
//		BufferUtilities.encode( encoder, srcBuffer, true );
//		byte[] bytes = baos.toByteArray();
//		
//		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( bytes );
//		edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = new edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder( bais );
//		java.nio.DoubleBuffer dstBuffer = BufferUtilities.decodeDoubleBuffer( decoder, true );
//		
//		rv = dstBuffer;
//		
//		return rv;
//	}

//	protected abstract byte[] decodeByteArray( byte[] rv );
	//	public final byte[] decodeByteArray() {
	//		byte[] rv = (byte[])createArray( Byte.TYPE );
	//		return decodeByteArray( rv );
	//	}
	//	
	public final byte[] decodeByteArray() {
		byte[] rv = (byte[])createArray( Byte.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeByte();
		}
		return rv;
	}

	public final char[] decodeCharArray() {
		char[] rv = (char[])createArray( Character.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeChar();
		}
		return rv;
	}
	public final double[] decodeDoubleArray() {
		double[] rv = (double[])createArray( Double.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeDouble();
		}
		return rv;
	}
	public final float[] decodeFloatArray() {
		float[] rv = (float[])createArray( Float.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeFloat();
		}
		return rv;
	}
	public final int[] decodeIntArray() {
		int[] rv = (int[])createArray( Integer.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeInt();
		}
		return rv;
	}
	public final long[] decodeLongArray() {
		long[] rv = (long[])createArray( Long.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeLong();
		}
		return rv;
	}
	public final short[] decodeShortArray() {
		short[] rv = (short[])createArray( Short.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeShort();
		}
		return rv;
	}
	public final String[] decodeStringArray() {
		String[] rv = (String[])createArray( String.class );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeString();
		}
		return rv;
	}

	public final <E extends Enum< E >> E[] decodeEnumArray( Class< E > cls ) {
		E[] rv = (E[])createArray( cls );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeEnum( cls );
		}
		return rv;
	}
	public final java.util.UUID[] decodeIdArray() {
		java.util.UUID[] rv = (java.util.UUID[])createArray( java.util.UUID.class );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeId();
		}
		return rv;
	}
	public final <E extends BinaryEncodableAndDecodable> E[] decodeBinaryEncodableAndDecodableArray( Class< E > componentCls ) {
		E[] rv = (E[])createArray( componentCls );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeBinaryEncodableAndDecodable();
		}
		return rv;
	}
	public final <E extends ReferenceableBinaryEncodableAndDecodable> E[] decodeReferenceableBinaryEncodableAndDecodableArray( Class< E > componentCls, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map ) {
		E[] rv = (E[])createArray( componentCls );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeReferenceableBinaryEncodableAndDecodable( componentCls, map );
		}
		return rv;
	}
	public final <E extends Enum< E >> E decodeEnum() {
		boolean isNotNull = decodeBoolean();
		if( isNotNull ) {
			String clsName = decodeString();
			String name = decodeString();
			Class< E > clsActual = (Class< E >)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( clsName );
			return Enum.valueOf( clsActual, name );
		} else {
			return null;
		}
	}
	@Deprecated
	public final <E extends Enum< E >> E decodeEnum( Class< E > cls ) {
		E rv = decodeEnum();
		assert cls.isInstance( rv );
		return rv;
	}

	public final java.util.UUID decodeId() {
		boolean isNotNull = this.decodeBoolean();
		if( isNotNull ) {
			long mostSigBits = this.decodeLong();
			long leastSigBits = this.decodeLong();
			return new java.util.UUID( mostSigBits, leastSigBits );
		} else {
			return null;
		}
	}

	public final <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable() {
		String clsName = this.decodeString();
		if( clsName.length() > 0 ) {
			Class clsActual = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( clsName );
			java.lang.reflect.Constructor< E > cnstrctr;
			E rv;
			try {
				cnstrctr = clsActual.getConstructor( new Class[] { BinaryDecoder.class } );
				rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, this );
			} catch( NoSuchMethodException nsme ) {
				cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( clsActual );
				rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr );
				rv.decode( this );
			}
			return rv;
		} else {
			return null;
		}
	}
	//	@Deprecated
	//	public final <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable( Class< E > cls ) {
	//		E rv = decodeBinaryEncodableAndDecodable();
	//		assert cls.isInstance( rv );
	//		return rv;
	//	}
	//	public final BinaryEncodableAndDecodable decodeBinaryEncodableAndDecodable( BinaryEncodableAndDecodable rv ) {
	//		String clsName = decodeString();
	//		if( rv != null ) {
	//			assert edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( clsName, rv.getClass().getName() );
	//			rv.decode( this );
	//		} else {
	//			assert clsName.length() == 0;
	//		}
	//		return rv;
	//	}
	public final <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable( java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map ) {
		String clsName = decodeString();
		if( clsName.length() > 0 ) {
			int reference = decodeInt();
			E rv;
			if( map.containsKey( reference ) ) {
				rv = (E)map.get( reference );
			} else {
				rv = (E)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( clsName );
				map.put( reference, rv );
				rv.decode( this, map );

				//
				//
				//todo?
				//
				//
				//				Class clsActual = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( clsName );
				//				java.lang.reflect.Constructor< E > cnstrctr;
				//				try {
				//					cnstrctr = clsActual.getConstructor( new Class[] { BinaryDecoder.class, java.util.Map.class } );
				//					rv = (E)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, this, map );
				//				} catch( NoSuchMethodException nsme ) {
				//					cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( clsActual );
				//					rv = (E)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr );
				//					rv.decode( this, map );
				//				}
			}
			return rv;
		} else {
			return null;
		}
	}
	@Deprecated
	public final <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable( Class< E > cls, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map ) {
		E rv = decodeReferenceableBinaryEncodableAndDecodable( map );
		assert cls.isInstance( rv );
		return rv;
	}

	public final ReferenceableBinaryEncodableAndDecodable decodeReferenceableBinaryEncodableAndDecodable( ReferenceableBinaryEncodableAndDecodable rv, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map ) {
		String clsName = decodeString();
		if( rv != null ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( clsName, rv.getClass().getName() );
			//assert edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( clsName, rv.getClass().getName() );
			int reference = decodeInt();
			if( map.containsKey( reference ) ) {
				assert rv == map.get( reference );
			} else {
				map.put( reference, rv );
				rv.decode( this, map );
			}
		} else {
			assert clsName.length() == 0;
		}
		return rv;
	}
}
