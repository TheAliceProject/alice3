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
package edu.cmu.cs.dennisc.map;

/**
 * @author Dennis Cosgrove
 */
public class MapToMap< A, B, E > {
	private java.util.Map< A, java.util.Map< B, E > > m_outerMap = new java.util.HashMap< A, java.util.Map< B, E > >();
	//todo: add addtional map methods
	public E get( A a, B b ) {
		 java.util.Map< B, E > innerMap = m_outerMap.get( a );
		 if( innerMap != null ) {
			 return innerMap.get( b );
		 } else {
			 return null;
		 }
	}
	public void put( A a, B b, E value ) {
		 java.util.Map< B, E > innerMap = m_outerMap.get( a );
		 if( innerMap != null ) {
			 //pass
		 } else {
			 innerMap = new java.util.HashMap< B, E >();
			 m_outerMap.put( a, innerMap );
		 }
		 innerMap.put( b, value );
	}
	
	public java.util.Collection< E > values() {
		java.util.List< E > rv = new java.util.LinkedList< E >();
		for( java.util.Map< B, E > innerMap : m_outerMap.values() ) {
			rv.addAll( innerMap.values() );
		}
		return rv;
	}
}
