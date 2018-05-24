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
package edu.cmu.cs.dennisc.java.lang;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

/**
 * @author Dennis Cosgrove
 */
public class ArrayUtilities {
	public static void reverseInPlace( Object[] array ) {
		final int N = array.length;
		for( int i = 0; i < ( N / 2 ); i++ ) {
			int j = N - i - 1;
			if( i != j ) {
				Object temp = array[ i ];
				array[ i ] = array[ j ];
				array[ j ] = temp;
			}
		}
	}

	public static <E> E[] concat( Class<E> cls, E a, E... bs ) {
		E[] rv = (E[])Array.newInstance( cls, 1 + bs.length );
		rv[ 0 ] = a;
		System.arraycopy( bs, 0, rv, 1, bs.length );
		return rv;
	}

	public static <E> E[] concat( Class<E> cls, E[] as, E b ) {
		E[] rv = (E[])Array.newInstance( cls, as.length + 1 );
		System.arraycopy( as, 0, rv, 0, as.length );
		rv[ as.length ] = b;
		return rv;
	}

	public static <E> E[] concatArrays( Class<E> cls, E[]... arrays ) {
		int totalLength = 0;
		for( E[] array : arrays ) {
			totalLength += array.length;
		}
		if( totalLength > 0 ) {
			E[] rv = (E[])Array.newInstance( cls, totalLength );
			int index = 0;
			for( E[] array : arrays ) {
				System.arraycopy( array, 0, rv, index, array.length );
				index += array.length;
			}
			return rv;
		}
		else {
			return null;
		}

	}

	public static <E> E[] createArray( Collection<E> collection, Class<E> cls, boolean isZeroLengthArrayDesiredForNull ) {
		int size;
		if( collection != null ) {
			size = collection.size();
		} else {
			if( isZeroLengthArrayDesiredForNull ) {
				size = 0;
			} else {
				size = -1;
			}
		}
		E[] rv;
		if( size >= 0 ) {
			rv = (E[])Array.newInstance( cls, size );
		} else {
			rv = null;
		}
		if( collection != null ) {
			collection.toArray( rv );
		}
		return rv;
	}

	public static <E> E[] createArray( Collection<E> collection, Class<E> cls ) {
		return createArray( collection, cls, false );
	}

	public static <E> void set( Collection<E> collection, E... array ) {
		collection.clear();
		if( array != null ) {
			if( collection instanceof ArrayList<?> ) {
				ArrayList<?> arrayList = (ArrayList<?>)collection;
				arrayList.ensureCapacity( array.length );
			}
			if( collection instanceof Vector<?> ) {
				Vector<?> vector = (Vector<?>)collection;
				vector.ensureCapacity( array.length );
			}
		}
		for( E e : array ) {
			collection.add( e );
		}
	}

	public static short[] createShortArray( Collection<Short> collection ) {
		final int N = collection.size();
		short[] rv = new short[ N ];
		int i = 0;
		for( Short v : collection ) {
			rv[ i++ ] = v;
		}
		return rv;
	}

	public static int[] createIntArray( Collection<Integer> collection ) {
		final int N = collection.size();
		int[] rv = new int[ N ];
		int i = 0;
		for( Integer v : collection ) {
			rv[ i++ ] = v;
		}
		return rv;
	}

	public static float[] createFloatArray( Collection<Float> collection ) {
		final int N = collection.size();
		float[] rv = new float[ N ];
		int i = 0;
		for( Float v : collection ) {
			rv[ i++ ] = v;
		}
		return rv;
	}

	public static double[] createDoubleArray( Collection<Double> collection ) {
		final int N = collection.size();
		double[] rv = new double[ N ];
		int i = 0;
		for( Double v : collection ) {
			rv[ i++ ] = v;
		}
		return rv;
	}

	public static String toString( Object o ) {
		if( o != null ) {
			if( o instanceof Object[] ) {
				return Arrays.toString( (Object[])o );
			} else if( o instanceof byte[] ) {
				return Arrays.toString( (byte[])o );
			} else if( o instanceof short[] ) {
				return Arrays.toString( (short[])o );
			} else if( o instanceof char[] ) {
				return Arrays.toString( (char[])o );
			} else if( o instanceof int[] ) {
				return Arrays.toString( (int[])o );
			} else if( o instanceof long[] ) {
				return Arrays.toString( (long[])o );
			} else if( o instanceof float[] ) {
				return Arrays.toString( (float[])o );
			} else if( o instanceof double[] ) {
				return Arrays.toString( (double[])o );
			} else {
				return o.toString();
			}
		} else {
			return null;
		}
	}
}
