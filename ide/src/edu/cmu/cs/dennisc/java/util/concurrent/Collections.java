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
package edu.cmu.cs.dennisc.java.util.concurrent;

/**
 * @author Dennis Cosgrove
 */
public class Collections {
	private Collections() {
		throw new AssertionError();
	}

	public static <E> java.util.concurrent.CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
		return new java.util.concurrent.CopyOnWriteArrayList<E>();
	}

	public static <E> java.util.concurrent.CopyOnWriteArrayList<E> newCopyOnWriteArrayList( E... array ) {
		java.util.concurrent.CopyOnWriteArrayList<E> rv = new java.util.concurrent.CopyOnWriteArrayList<E>();
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( rv, array );
		return rv;
	}

	public static <E> java.util.concurrent.CopyOnWriteArrayList<E> newCopyOnWriteArrayList( java.util.Collection<E> other ) {
		java.util.concurrent.CopyOnWriteArrayList<E> rv = new java.util.concurrent.CopyOnWriteArrayList<E>();
		rv.addAll( other );
		return rv;
	}

	public static <E> java.util.concurrent.CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
		return new java.util.concurrent.CopyOnWriteArraySet<E>();
	}

	public static <E> java.util.concurrent.CopyOnWriteArraySet<E> newCopyOnWriteArraySet( E... array ) {
		java.util.concurrent.CopyOnWriteArraySet<E> rv = new java.util.concurrent.CopyOnWriteArraySet<E>();
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( rv, array );
		return rv;
	}

	public static <E> java.util.concurrent.CopyOnWriteArraySet<E> newCopyOnWriteArraySet( java.util.Collection<E> other ) {
		java.util.concurrent.CopyOnWriteArraySet<E> rv = new java.util.concurrent.CopyOnWriteArraySet<E>();
		rv.addAll( other );
		return rv;
	}

	public static <E> java.util.concurrent.ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
		return new java.util.concurrent.ConcurrentLinkedQueue<E>();
	}

	public static <E> java.util.concurrent.ConcurrentLinkedQueue<E> newConcurrentLinkedQueue( E... array ) {
		java.util.concurrent.ConcurrentLinkedQueue<E> rv = new java.util.concurrent.ConcurrentLinkedQueue<E>();
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( rv, array );
		return rv;
	}

	public static <E> java.util.concurrent.ConcurrentLinkedQueue<E> newConcurrentLinkedQueue( java.util.Collection<E> other ) {
		java.util.concurrent.ConcurrentLinkedQueue<E> rv = new java.util.concurrent.ConcurrentLinkedQueue<E>();
		rv.addAll( other );
		return rv;
	}

	public static <K, V> java.util.concurrent.ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new java.util.concurrent.ConcurrentHashMap<K, V>();
	}
}
