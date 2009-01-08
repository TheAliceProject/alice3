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
public class MapToMapToMap< A, B, C, E > {
	private MapToMap< A, B, java.util.Map< C, E > > m_outerMapToMap = new MapToMap< A, B, java.util.Map< C, E > >();
	//todo: add addtional map methods
	public E get( A a, B b, C c ) {
		 java.util.Map< C, E > innerMap = m_outerMapToMap.get( a, b );
		 if( innerMap != null ) {
			 return innerMap.get( c );
		 } else {
			 return null;
		 }
	}
	public void put( A a, B b, C c, E value ) {
		 java.util.Map< C, E > innerMap = m_outerMapToMap.get( a, b );
		 if( innerMap != null ) {
			 //pass
		 } else {
			 innerMap = new java.util.HashMap< C, E >();
			 m_outerMapToMap.put( a, b, innerMap );
		 }
		 innerMap.put( c, value );
	}
}
