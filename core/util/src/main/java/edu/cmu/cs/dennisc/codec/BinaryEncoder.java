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
public interface BinaryEncoder {
	public void write( byte[] data );

	public void write( byte[] data, int offset, int length );

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

	public void encode( java.util.UUID value );

	public void encode( BinaryEncodableAndDecodable value );

	public void encode( ReferenceableBinaryEncodableAndDecodable value, java.util.Map<ReferenceableBinaryEncodableAndDecodable, Integer> map );

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

	public void encode( java.util.UUID[] array );

	public void encode( BinaryEncodableAndDecodable[] array );

	public void encode( ReferenceableBinaryEncodableAndDecodable[] array, java.util.Map<ReferenceableBinaryEncodableAndDecodable, Integer> map );

	public void flush();
}
