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
public interface BinaryEncoder {
	public void encode( boolean value );
	public void encode( byte value );
	public void encode( char value );
	public void encode( double value );
	public void encode( float value );
	public void encode( int value );
	public void encode( long value );
	public void encode( short value );
	public void encode( String value );
	public void encode( Enum<?> value );
	public void encode( BinaryEncodableAndDecodable value );
	public void encode( ReferenceableBinaryEncodableAndDecodable value, java.util.Map< ReferenceableBinaryEncodableAndDecodable, Integer > map );

	public void encode( boolean[] array );
	public void encode( byte[] array );
	public void encode( char[] array );
	public void encode( double[] array );
	public void encode( float[] array );
	public void encode( int[] array );
	public void encode( long[] array );
	public void encode( short[] array );
	public void encode( String[] array );
	public void encode( Enum<?>[] array );
	public void encode( BinaryEncodableAndDecodable[] array );
	public void encode( ReferenceableBinaryEncodableAndDecodable[] array, java.util.Map< ReferenceableBinaryEncodableAndDecodable, Integer > map );
	
	public void flush();
}
