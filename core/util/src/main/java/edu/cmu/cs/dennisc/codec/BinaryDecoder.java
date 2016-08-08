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
public interface BinaryDecoder {
	public byte[] readFully( byte[] rv );

	public byte[] readFully( byte[] rv, int offset, int length );

	public boolean decodeBoolean();

	public byte decodeByte();

	public char decodeChar();

	public double decodeDouble();

	public float decodeFloat();

	public int decodeInt();

	public long decodeLong();

	public short decodeShort();

	public String decodeString();

	public <E extends Enum<E>> E decodeEnum();

	public java.util.UUID decodeId();

	public <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable();

	public <E extends BinaryEncodableAndDecodable> E decodeBinaryEncodableAndDecodable( Object context );

	@Deprecated
	public ReferenceableBinaryEncodableAndDecodable decodeReferenceableBinaryEncodableAndDecodable( ReferenceableBinaryEncodableAndDecodable rv, java.util.Map<Integer, ReferenceableBinaryEncodableAndDecodable> map );

	public <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable( java.util.Map<Integer, ReferenceableBinaryEncodableAndDecodable> map );

	//	public <E extends ReferenceableBinaryEncodableAndDecodable> E decodeReferenceableBinaryEncodableAndDecodable( java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map, Object context );

	public boolean[] decodeBooleanArray();

	public byte[] decodeByteArray();

	public char[] decodeCharArray();

	public double[] decodeDoubleArray();

	public float[] decodeFloatArray();

	public int[] decodeIntArray();

	public long[] decodeLongArray();

	public short[] decodeShortArray();

	public String[] decodeStringArray();

	public <E extends Enum<E>> E[] decodeEnumArray( Class<E> cls );

	public java.util.UUID[] decodeIdArray();

	public <E extends BinaryEncodableAndDecodable> E[] decodeBinaryEncodableAndDecodableArray( Class<E> componentCls );

	public <E extends BinaryEncodableAndDecodable> E[] decodeBinaryEncodableAndDecodableArray( Class<E> componentCls, Object context );

	public <E extends ReferenceableBinaryEncodableAndDecodable> E[] decodeReferenceableBinaryEncodableAndDecodableArray( Class<E> componentCls, java.util.Map<Integer, ReferenceableBinaryEncodableAndDecodable> map );
	//	public <E extends ReferenceableBinaryEncodableAndDecodable> E[] decodeReferenceableBinaryEncodableAndDecodableArray( Class< E > componentCls, java.util.Map< Integer, ReferenceableBinaryEncodableAndDecodable > map, Object context );
}
