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
public abstract class AbstractBinaryEncoder implements BinaryEncoder {
	private void encodeArrayLength( Object array ) {
		encode( java.lang.reflect.Array.getLength( array ) );
	}

	public final void encode( boolean[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}

	protected abstract void encodeBuffer( byte[] buffer );
	
	public final void encode( byte[] array ) {
		encodeArrayLength( array );
		encodeBuffer( array );
	}
	public final void encode( char[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}
	public final void encode( double[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}
	public final void encode( float[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}
	public final void encode( int[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}
	public final void encode( long[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}
	public final void encode( short[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}
	public final void encode( String[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}
	public final void encode( Enum< ? >[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}
	public final void encode( BinaryEncodableAndDecodable[] array ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ] );
		}
	}

	public final void encode( ReferenceableBinaryEncodableAndDecodable[] array, java.util.Map< ReferenceableBinaryEncodableAndDecodable, Integer > map ) {
		encodeArrayLength( array );
		for( int i=0; i<array.length; i++ ) {
			encode( array[ i ], map );
		}
	}
	
	public final void encode( Enum< ? > value ) {
		boolean isNotNull = value != null;
		encode( isNotNull );
		if( isNotNull ) {
			encode( value.getClass().getName() );
			encode( value.name() );
		}
	}

	public final void encode( BinaryEncodableAndDecodable value ) {
		if( value != null ) {
			encode( value.getClass().getName() );
			value.encode( this );
		} else {
			encode( "" );
		}
	}

	public final void encode( ReferenceableBinaryEncodableAndDecodable value, java.util.Map< ReferenceableBinaryEncodableAndDecodable, Integer > map ) {
		if( value != null ) {
			encode( value.getClass().getName() );
			encode( value.hashCode() );
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
	

//	@Override
//	public final void encode( Collection< ? extends BinaryEncodableAndDecodable > collection ) {
//		synchronized( collection ) {
//			encode( collection.size() );
//			for( BinaryEncodableAndDecodable binaryEncodableAndDecodable : collection ) {
//				encode( binaryEncodableAndDecodable );
//			}
//		}
//		
//	}
}
