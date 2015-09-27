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

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractBinaryDecoder implements BinaryDecoder {
	//todo: handle null arrays
	private Object createArray( Class<?> componentType ) {
		int length = this.decodeInt();
		if( length != -1 ) {
			return java.lang.reflect.Array.newInstance( componentType, length );
		} else {
			return null;
		}
	}

	@Override
	public final boolean[] decodeBooleanArray() {
		boolean[] rv = (boolean[])createArray( Boolean.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeBoolean();
		}
		return rv;
	}

	@Override
	public final byte[] decodeByteArray() {
		byte[] rv = (byte[])createArray( Byte.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeByte();
		}
		return rv;
	}

	@Override
	public final char[] decodeCharArray() {
		char[] rv = (char[])createArray( Character.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeChar();
		}
		return rv;
	}

	@Override
	public final double[] decodeDoubleArray() {
		double[] rv = (double[])createArray( Double.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeDouble();
		}
		return rv;
	}

	@Override
	public final float[] decodeFloatArray() {
		float[] rv = (float[])createArray( Float.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeFloat();
		}
		return rv;
	}

	@Override
	public final int[] decodeIntArray() {
		int[] rv = (int[])createArray( Integer.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeInt();
		}
		return rv;
	}

	@Override
	public final long[] decodeLongArray() {
		long[] rv = (long[])createArray( Long.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeLong();
		}
		return rv;
	}

	@Override
	public final short[] decodeShortArray() {
		short[] rv = (short[])createArray( Short.TYPE );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeShort();
		}
		return rv;
	}

	@Override
	public final String[] decodeStringArray() {
		String[] rv = (String[])createArray( String.class );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeString();
		}
		return rv;
	}

	@Override
	public final <E extends Enum<E>> E[] decodeEnumArray( Class<E> cls ) {
		E[] rv = (E[])createArray( cls );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeEnum( cls );
		}
		return rv;
	}

	@Override
	public final java.util.UUID[] decodeIdArray() {
		java.util.UUID[] rv = (java.util.UUID[])createArray( java.util.UUID.class );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeId();
		}
		return rv;
	}

	@Override
	public final <E extends BinaryEncodableAndDecodable> E[] decodeBinaryEncodableAndDecodableArray( Class<E> componentCls ) {
		E[] rv = (E[])createArray( componentCls );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = (E)decodeBinaryEncodableAndDecodable();
		}
		return rv;
	}

	@Override
	public final <E extends ReferenceableBinaryEncodableAndDecodable> E[] decodeReferenceableBinaryEncodableAndDecodableArray( Class<E> componentCls, java.util.Map<Integer, ReferenceableBinaryEncodableAndDecodable> map ) {
		E[] rv = (E[])createArray( componentCls );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = decodeReferenceableBinaryEncodableAndDecodable( componentCls, map );
		}
		return rv;
	}

	@Override
	public final <E extends Enum<E>> E decodeEnum() {
		boolean isNotNull = decodeBoolean();
		if( isNotNull ) {
			String clsName = decodeString();
			String name = decodeString();
			Class<E> clsActual = (Class<E>)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( clsName );
			return Enum.valueOf( clsActual, name );
		} else {
			return null;
		}
	}

	@Deprecated
	public final <E extends Enum<E>> E decodeEnum( Class<E> cls ) {
		E rv = decodeEnum();
		assert cls.isInstance( rv );
		return rv;
	}

	@Override
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

	private <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable( Class<?>[] parameterTypes, Object[] args ) {
		String clsName = this.decodeString();
		if( clsName.length() > 0 ) {
			try {
				java.lang.reflect.Constructor<E> cnstrctr = CodecUtilities.getPublicDecodeConstructor( clsName, parameterTypes );
				return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, args );
			} catch( NoSuchMethodException nsme ) {
				try {
					Class<E> cls = (Class<E>)Class.forName( clsName );
					java.lang.reflect.Constructor<E> cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( cls );
					java.lang.reflect.Method mthd = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod( cls, "decode", BinaryDecoder.class );
					E rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr );
					edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( rv, mthd, this );
					//					rv.decode( this );
					return rv;
				} catch( ClassNotFoundException cnfe ) {
					throw new RuntimeException( cnfe );
				}
				//				throw new RuntimeException( nsme );
			} catch( ClassNotFoundException cnfe ) {
				throw new RuntimeException( cnfe );
			}
			//			Class clsActual = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( clsName );
			//			java.lang.reflect.Constructor< E > cnstrctr;
			//			E rv;
			//			try {
			//				cnstrctr = clsActual.getConstructor( new Class[] { BinaryDecoder.class } );
			//				rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, this );
			//			} catch( NoSuchMethodException nsme ) {
			//				cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( clsActual );
			//				rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr );
			//				rv.decode( this );
			//			}
			//			return rv;
		} else {
			return null;
		}
	}

	private static final Class<?>[] EMPTY_PARAMETER_TYPES = { BinaryDecoder.class };
	private static final Class<?>[] OBJECT_PARAMETER_TYPES = { BinaryDecoder.class, Object.class };

	@Override
	public final <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable() {
		return (E)decodeBinaryEncodableAndDecodable( EMPTY_PARAMETER_TYPES, new Object[] { this } );
	}

	@Override
	public final <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable( Object context ) {
		return (E)decodeBinaryEncodableAndDecodable( OBJECT_PARAMETER_TYPES, new Object[] { this, context } );
	}

	//	public final <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable() {
	//		String clsName = this.decodeString();
	//		if( clsName.length() > 0 ) {
	//			try {
	//				java.lang.reflect.Constructor< E > cnstrctr = CodecUtilities.getPublicDecodeConstructor( clsName, EMPTY_PARAMETER_TYPES );			
	//				return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, this );
	//			} catch( NoSuchMethodException nsme ) {
	////				try {
	////					Class<E> cls = (Class<E>)Class.forName( clsName );
	////					java.lang.reflect.Constructor< E > cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( cls );
	////					E rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr );
	////					rv.decode( this );
	////					return rv;
	////				} catch( ClassNotFoundException cnfe ) {
	////					throw new RuntimeException( cnfe );
	////				}
	//				throw new RuntimeException( nsme );
	//			} catch( ClassNotFoundException cnfe ) {
	//				throw new RuntimeException( cnfe );
	//			}
	////			Class clsActual = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( clsName );
	////			java.lang.reflect.Constructor< E > cnstrctr;
	////			E rv;
	////			try {
	////				cnstrctr = clsActual.getConstructor( new Class[] { BinaryDecoder.class } );
	////				rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, this );
	////			} catch( NoSuchMethodException nsme ) {
	////				cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( clsActual );
	////				rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr );
	////				rv.decode( this );
	////			}
	////			return rv;
	//		} else {
	//			return null;
	//		}
	//	}
	@Override
	public <E extends edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable> E[] decodeBinaryEncodableAndDecodableArray( Class<E> componentCls, Object context ) {
		throw new RuntimeException( "todo" );
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
	//			assert edu.cmu.cs.dennisc.java.util.Objects.equals( clsName, rv.getClass().getName() );
	//			rv.decode( this );
	//		} else {
	//			assert clsName.length() == 0;
	//		}
	//		return rv;
	//	}
	@Override
	public final <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable( java.util.Map<Integer, ReferenceableBinaryEncodableAndDecodable> map ) {
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
	public final <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable( Class<E> cls, java.util.Map<Integer, ReferenceableBinaryEncodableAndDecodable> map ) {
		E rv = (E)decodeReferenceableBinaryEncodableAndDecodable( map );
		assert cls.isInstance( rv );
		return rv;
	}

	@Override
	public final ReferenceableBinaryEncodableAndDecodable decodeReferenceableBinaryEncodableAndDecodable( ReferenceableBinaryEncodableAndDecodable rv, java.util.Map<Integer, ReferenceableBinaryEncodableAndDecodable> map ) {
		String clsName = decodeString();
		if( rv != null ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( clsName, rv.getClass().getName() );
			//assert edu.cmu.cs.dennisc.java.util.Objects.equals( clsName, rv.getClass().getName() );
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
