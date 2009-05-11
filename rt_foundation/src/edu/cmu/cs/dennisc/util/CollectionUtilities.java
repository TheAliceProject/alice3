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
package edu.cmu.cs.dennisc.util;

/**
 * @author Dennis Cosgrove
 */
public abstract class CollectionUtilities {
	private CollectionUtilities() {
	}
	public static <E extends Object> E[] createArray( java.util.Collection< E > collection, Class< E > cls ) {
		E[] rv = (E[])java.lang.reflect.Array.newInstance( cls, collection.size() );
		return collection.toArray( rv );
	}
	public static <E extends Object> void set( java.util.Collection< E > collection, E... array ) {
		collection.clear();
		if( collection instanceof java.util.ArrayList ) {
			java.util.ArrayList< E > arrayList = (java.util.ArrayList< E >)collection;
			arrayList.ensureCapacity( array.length );
		}
		if( collection instanceof java.util.Vector ) {
			java.util.Vector< E > vector = (java.util.Vector< E >)collection;
			vector.ensureCapacity( array.length );
		}
		for( E e : array ) {
			collection.add( e );
		}
	}
	public static <E extends Object> java.util.ArrayList<E> createArrayList( E... array ) {
		java.util.ArrayList<E> rv = new java.util.ArrayList< E >();
		set( rv, array );
		return rv;
	}
	public static <E extends Object> java.util.Vector<E> createVector( E... array ) {
		java.util.Vector<E> rv = new java.util.Vector< E >();
		set( rv, array );
		return rv;
	}
}
