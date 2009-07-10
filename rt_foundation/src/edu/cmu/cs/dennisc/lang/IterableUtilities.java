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
package edu.cmu.cs.dennisc.lang;

/**
 * @author Dennis Cosgrove
 */
public class IterableUtilities {
	public static <E> E[] toArray( Iterable<E> iterable, Class<E> cls ) {
		java.util.Collection< Object > collection;
		if( iterable instanceof java.util.Collection< ? > ) {
			collection = (java.util.Collection< Object >)iterable;
		} else {
			collection = new java.util.Vector< Object >();
			for( Object item : iterable ) {
				collection.add( item );
			}
		}
		E[] rv = (E[])java.lang.reflect.Array.newInstance( cls, collection.size() );
		return collection.toArray( rv );
	}
	public static Object[] toArray( Iterable iterable ) {
		return toArray( iterable, Object.class );
	}
}
