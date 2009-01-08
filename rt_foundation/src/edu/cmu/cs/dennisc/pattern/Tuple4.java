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
public class Tuple4<A, B, C, D> {
	private A m_a = null;
	private B m_b = null;
	private C m_c = null;
	private D m_d = null;
	public Tuple4() {
	}
	public Tuple4( A a, B b, C c, D d ) {
		set( a, b, c, d );
	}
	public A getA() {
		return m_a;
	}
	public void setA( A a ) {
		m_a = a;
	}
	public B getB() {
		return m_b;
	}
	public void setB( B b ) {
		m_b = b;
	}
	public C getC() {
		return m_c;
	}
	public void setC( C c ) {
		m_c = c;
	}
	public D getD() {
		return m_d;
	}
	public void setD( D d ) {
		m_d = d;
	}
	public void set( A a, B b, C c, D d ) {
		m_a = a;
		m_b = b;
		m_c = c;
		m_d = d;
	}
	@Override
	public boolean equals( Object other ) {
		if( super.equals( other ) ) {
			return true;
		} else {
			if( other instanceof Tuple4 ) {
				Tuple4<?,?,?,?> otherT = (Tuple4<?,?,?,?>)other;
				return edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_a, otherT.m_a ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_b, otherT.m_b ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_c, otherT.m_c ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_d, otherT.m_d );
			} else {
				return false;
			}
		}
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "edu.cmu.cs.dennisc.pattern.Tuple4[ a=" );
		sb.append( m_a );
		sb.append( ", b=" );
		sb.append( m_b );
		sb.append( ", c=" );
		sb.append( m_c );
		sb.append( ", d=" );
		sb.append( m_d );
		sb.append( " ]" );
		return sb.toString();
	}
}
