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
package edu.cmu.cs.dennisc.java.util;

/**
 * @author Dennis Cosgrove
 */
public class Collections {
	private Collections() {
		throw new AssertionError();
	}

	public static <C extends java.util.Collection<E>, E> C addAll( C rv, E... values ) {
		for( E value : values ) {
			rv.add( value );
		}
		return rv;
	}

	public static <E> java.util.LinkedList<E> newLinkedList() {
		return new java.util.LinkedList<E>();
	}

	public static <E> java.util.LinkedList<E> newLinkedList( java.util.Collection<E> other ) {
		java.util.LinkedList<E> rv = new java.util.LinkedList<E>();
		rv.addAll( other );
		return rv;
	}

	public static <E> java.util.LinkedList<E> newLinkedList( E... array ) {
		java.util.LinkedList<E> rv = new java.util.LinkedList<E>();
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( rv, array );
		return rv;
	}

	public static <E> java.util.ArrayList<E> newArrayListWithMinimumCapacity( int minCapacity ) {
		java.util.ArrayList<E> rv = new java.util.ArrayList<E>();
		rv.ensureCapacity( minCapacity );
		return rv;
	}

	public static <E> java.util.ArrayList<E> newArrayList() {
		return new java.util.ArrayList<E>();
	}

	public static <E> java.util.ArrayList<E> newArrayList( E... array ) {
		java.util.ArrayList<E> rv = new java.util.ArrayList<E>();
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( rv, array );
		return rv;
	}

	public static <E> java.util.ArrayList<E> newArrayList( java.util.Collection<E> other ) {
		java.util.ArrayList<E> rv = new java.util.ArrayList<E>();
		rv.addAll( other );
		return rv;
	}

	public static <E> java.util.Vector<E> newVector() {
		return new java.util.Vector<E>();
	}

	public static <E> java.util.Vector<E> newVector( E... array ) {
		java.util.Vector<E> rv = new java.util.Vector<E>();
		if( array != null ) {
			edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( rv, array );
		}
		return rv;
	}

	public static <E> java.util.Vector<E> newVector( java.util.Collection<E> other ) {
		java.util.Vector<E> rv = new java.util.Vector<E>();
		rv.addAll( other );
		return rv;
	}

	public static <E> java.util.Stack<E> newStack() {
		return new java.util.Stack<E>();
	}

	public static <E> java.util.Stack<E> newStack( E... array ) {
		java.util.Stack<E> rv = new java.util.Stack<E>();
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( rv, array );
		return rv;
	}

	public static <E> java.util.Stack<E> newStack( java.util.Collection<E> other ) {
		java.util.Stack<E> rv = new java.util.Stack<E>();
		rv.addAll( other );
		return rv;
	}

	public static <E> java.util.HashSet<E> newHashSet() {
		return new java.util.HashSet<E>();
	}

	public static <E> java.util.HashSet<E> newHashSet( E... values ) {
		java.util.HashSet<E> rv = new java.util.HashSet<E>();
		if( values != null ) {
			for( E value : values ) {
				rv.add( value );
			}
		}
		return rv;
	}

	public static <E> java.util.HashSet<E> newHashSet( java.util.Collection<E> other ) {
		java.util.HashSet<E> rv = new java.util.HashSet<E>();
		rv.addAll( other );
		return rv;
	}

	public static <K, V> java.util.HashMap<K, V> newHashMap() {
		return new java.util.HashMap<K, V>();
	}

	public static <K, V> java.util.WeakHashMap<K, V> newWeakHashMap() {
		return new java.util.WeakHashMap<K, V>();
	}

	public static <A, B> java.util.HashMap<B, A> newInverseHashMap( java.util.Map<A, B> map ) {
		java.util.HashMap<B, A> rv = newHashMap();
		for( A a : map.keySet() ) {
			B b = map.get( a );
			rv.put( b, a );
		}
		return rv;
	}
}
