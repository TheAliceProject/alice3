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
public interface BinaryDecoder {
	public boolean decodeBoolean();
	public byte decodeByte();
	public char decodeChar();
	public double decodeDouble();
	public float decodeFloat();
	public int decodeInt();
	public long decodeLong();
	public short decodeShort();
	public String decodeString();
	public <E extends Enum< E >> E decodeEnum( Class< E > cls );
	public <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable( Class< E > cls );
	public BinaryEncodableAndDecodable decodeBinaryEncodableAndDecodable( BinaryEncodableAndDecodable rv );
	public <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable( Class< E > cls, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map );
	public ReferenceableBinaryEncodableAndDecodable decodeReferenceableBinaryEncodableAndDecodable( ReferenceableBinaryEncodableAndDecodable rv, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map );

	public boolean[] decodeBooleanArray();
	public byte[] decodeByteArray();
	public char[] decodeCharArray();
	public double[] decodeDoubleArray();
	public float[] decodeFloatArray();
	public int[] decodeIntArray();
	public long[] decodeLongArray();
	public short[] decodeShortArray();
	public String[] decodeStringArray();
	public <E extends Enum< E >> E[] decodeEnumArray( Class< E > cls );
	public <E extends BinaryEncodableAndDecodable> E[] decodeBinaryEncodableAndDecodableArray( Class< E > cls );
	public <E extends ReferenceableBinaryEncodableAndDecodable> E[] decodeReferenceableBinaryEncodableAndDecodableArray( Class< E > cls, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map );
}
