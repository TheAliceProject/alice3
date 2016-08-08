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

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractBinaryEncoder implements BinaryEncoder {
	//todo: handle null arrays
	private void encodeArrayLength( Object array ) {
		//todo: encode -1 for null?
		int arrayLength;
		if( array != null ) {
			arrayLength = java.lang.reflect.Array.getLength( array );
		} else {
			arrayLength = -1;
		}
		this.encode( arrayLength );
	}

	@Override
	public final void encode( boolean[] array ) {
		this.encodeArrayLength( array );
		for( boolean element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( byte[] array ) {
		this.encodeArrayLength( array );
		this.write( array );
	}

	@Override
	public final void encode( char[] array ) {
		this.encodeArrayLength( array );
		for( char element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( double[] array ) {
		this.encodeArrayLength( array );
		for( double element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( float[] array ) {
		this.encodeArrayLength( array );
		for( float element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( int[] array ) {
		this.encodeArrayLength( array );
		for( int element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( long[] array ) {
		this.encodeArrayLength( array );
		for( long element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( short[] array ) {
		this.encodeArrayLength( array );
		for( short element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( String[] array ) {
		this.encodeArrayLength( array );
		for( String element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( Enum<?>[] array ) {
		this.encodeArrayLength( array );
		for( Enum<?> element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( java.util.UUID[] array ) {
		this.encodeArrayLength( array );
		for( UUID element : array ) {
			this.encode( element );
		}
	}

	@Override
	public final void encode( BinaryEncodableAndDecodable[] array ) {
		this.encodeArrayLength( array );
		for( BinaryEncodableAndDecodable element : array ) {
			this.encode( element );
			//array[ i ].encode( this );
		}
	}

	@Override
	public final void encode( ReferenceableBinaryEncodableAndDecodable[] array, java.util.Map<ReferenceableBinaryEncodableAndDecodable, Integer> map ) {
		this.encodeArrayLength( array );
		for( ReferenceableBinaryEncodableAndDecodable element : array ) {
			this.encode( element, map );
			//array[ i ].encode( this, map );
		}
	}

	@Override
	public final void encode( Enum<?> value ) {
		boolean isNotNull = value != null;
		this.encode( isNotNull );
		if( isNotNull ) {
			this.encode( value.getClass().getName() );
			this.encode( value.name() );
		}
	}

	@Override
	public final void encode( java.util.UUID value ) {
		boolean isNotNull = value != null;
		this.encode( isNotNull );
		if( isNotNull ) {
			this.encode( value.getMostSignificantBits() );
			this.encode( value.getLeastSignificantBits() );
		}
	}

	@Override
	public final void encode( BinaryEncodableAndDecodable value ) {
		if( value != null ) {
			this.encode( value.getClass().getName() );
			value.encode( this );
		} else {
			this.encode( "" );
		}
	}

	@Override
	public final void encode( ReferenceableBinaryEncodableAndDecodable value, java.util.Map<ReferenceableBinaryEncodableAndDecodable, Integer> map ) {
		if( value != null ) {
			this.encode( value.getClass().getName() );
			this.encode( value.hashCode() );
			if( map.containsKey( value ) ) {
				//pass
			} else {
				map.put( value, value.hashCode() );
				value.encode( this, map );
			}
		} else {
			encode( "" );
		}
	}
}
