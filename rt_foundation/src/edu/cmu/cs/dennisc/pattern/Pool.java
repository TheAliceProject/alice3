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
package edu.cmu.cs.dennisc.pattern;

/**
 * @author Dennis Cosgrove
 */
public class Pool< E extends Reusable > {
	private Class<E> m_cls;
	private java.util.Stack< E > m_available = new java.util.Stack< E >();
	
	public Pool( Class<E> cls ) {
		m_cls = cls;
	}
	
	public E acquire() {
		E rv;
		synchronized( m_available ) {
			if( m_available.size() > 0 ) {
				rv = m_available.pop(); 
			} else {
				rv = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( m_cls );
			}
		}
		return rv;
	}
	public void release( E e ) {
		synchronized( m_available ) {
			m_available.push( e );
		}
	}
}
