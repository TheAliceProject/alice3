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
package edu.cmu.cs.dennisc.java.lang;

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

	public static <E extends Object> E[] createArray( java.util.Collection<E> collection, Class<E> cls, boolean isZeroLengthArrayDesiredForNull ) {
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
			rv = (E[])java.lang.reflect.Array.newInstance( cls, size );
		} else {
			rv = null;
		}
		if( collection != null ) {
			collection.toArray( rv );
		}
		return rv;
	}

	public static <E extends Object> E[] createArray( java.util.Collection<E> collection, Class<E> cls ) {
		return createArray( collection, cls, false );
	}

	public static <E extends Object> void set( java.util.Collection<E> collection, E... array ) {
		collection.clear();
		if( array != null ) {
			if( collection instanceof java.util.ArrayList<?> ) {
				java.util.ArrayList<?> arrayList = (java.util.ArrayList<?>)collection;
				arrayList.ensureCapacity( array.length );
			}
			if( collection instanceof java.util.Vector<?> ) {
				java.util.Vector<?> vector = (java.util.Vector<?>)collection;
				vector.ensureCapacity( array.length );
			}
		}
		for( E e : array ) {
			collection.add( e );
		}
	}

	public static short[] createShortArray( java.util.Collection<Short> collection ) {
		final int N = collection.size();
		short[] rv = new short[ N ];
		int i = 0;
		for( Short v : collection ) {
			rv[ i++ ] = v;
		}
		return rv;
	}

	public static int[] createIntArray( java.util.Collection<Integer> collection ) {
		final int N = collection.size();
		int[] rv = new int[ N ];
		int i = 0;
		for( Integer v : collection ) {
			rv[ i++ ] = v;
		}
		return rv;
	}

	public static float[] createFloatArray( java.util.Collection<Float> collection ) {
		final int N = collection.size();
		float[] rv = new float[ N ];
		int i = 0;
		for( Float v : collection ) {
			rv[ i++ ] = v;
		}
		return rv;
	}

	public static double[] createDoubleArray( java.util.Collection<Double> collection ) {
		final int N = collection.size();
		double[] rv = new double[ N ];
		int i = 0;
		for( Double v : collection ) {
			rv[ i++ ] = v;
		}
		return rv;
	}
}
