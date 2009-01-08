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
package edu.cmu.cs.dennisc.codec;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractBinaryDecoder implements BinaryDecoder {
	private Object createArray( Class<?> componentType ) {
		int length = decodeInt();
		return java.lang.reflect.Array.newInstance( componentType, length );
	}
	public final boolean[] decodeBooleanArray() {
		boolean[] rv = (boolean[])createArray( Boolean.TYPE );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeBoolean();
		}
		return rv;
	}

	protected abstract byte[] decodeByteArray( byte[] rv );
	public final byte[] decodeByteArray() {
		byte[] rv = (byte[])createArray( Byte.TYPE );
		return decodeByteArray( rv );
	}
	public final char[] decodeCharArray() {
		char[] rv = (char[])createArray( Character.TYPE );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeChar();
		}
		return rv;
	}
	public final double[] decodeDoubleArray() {
		double[] rv = (double[])createArray( Double.TYPE );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeDouble();
		}
		return rv;
	}
	public final float[] decodeFloatArray() {
		float[] rv = (float[])createArray( Float.TYPE );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeFloat();
		}
		return rv;
	}
	public final int[] decodeIntArray() {
		int[] rv = (int[])createArray( Integer.TYPE );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeInt();
		}
		return rv;
	}
	public final long[] decodeLongArray() {
		long[] rv = (long[])createArray( Long.TYPE );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeLong();
		}
		return rv;
	}
	public final short[] decodeShortArray() {
		short[] rv = (short[])createArray( Short.TYPE );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeShort();
		}
		return rv;
	}
	public final String[] decodeStringArray() {
		String[] rv = (String[])createArray( String.class );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeString();
		}
		return rv;
	}
	public final < E extends Enum< E > > E[] decodeEnumArray( Class< E > cls ) {
		E[] rv = (E[])createArray( cls );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeEnum( cls );
		}
		return rv;
	}
	public final <E extends BinaryEncodableAndDecodable> E[] decodeBinaryEncodableAndDecodableArray( Class< E > cls ) {
		E[] rv = (E[])createArray( cls );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeBinaryEncodableAndDecodable( cls );
		}
		return rv;
	}
	public final <E extends ReferenceableBinaryEncodableAndDecodable> E[] decodeReferenceableBinaryEncodableAndDecodableArray( Class< E > cls, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map ) {
		E[] rv = (E[])createArray( cls );
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = decodeReferenceableBinaryEncodableAndDecodable( cls, map );
		}
		return rv;
	}
	public final <E extends Enum< E >> E decodeEnum( Class< E > cls ) {
		boolean isNotNull = decodeBoolean();
		if( isNotNull ) {
			String clsName = decodeString();
			assert edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( clsName, cls.getName() );
			String name = decodeString();
			return Enum.valueOf( cls, name );
		} else {
			return null;
		}
	}

	public final <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable( Class< E > cls ) {
		String clsName = decodeString();
		if( clsName.length() > 0 ) {
			Class clsActual = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( clsName );
			java.lang.reflect.Constructor cnstrctr;
			E rv;
			try {
				cnstrctr = clsActual.getConstructor( new Class[] { BinaryDecoder.class } );
				rv = (E)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, this );
			} catch( NoSuchMethodException nsme ) {
				cnstrctr = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( clsActual );
				rv = (E)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( cnstrctr );
				rv.decode( this );
			}
			return rv;
		} else {
			return null;
		}
	}
	public final BinaryEncodableAndDecodable decodeBinaryEncodableAndDecodable( BinaryEncodableAndDecodable rv ) {
		String clsName = decodeString();
		if( rv != null ) {
			assert edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( clsName, rv.getClass().getName() );
			rv.decode( this );
		} else {
			assert clsName.length() == 0;
		}
		return rv;
	}
	public final <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable( Class< E > cls, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map ) {
		String clsName = decodeString();
		if( clsName.length() > 0 ) {
			int reference = decodeInt();
			E rv;
			if( map.containsKey( reference ) ) {
				rv = (E)map.get( reference );
			} else {
				rv = (E)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( clsName );
				map.put( reference, rv );
				rv.decode( this, map );
			}
			return rv;
		} else {
			return null;
		}
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
