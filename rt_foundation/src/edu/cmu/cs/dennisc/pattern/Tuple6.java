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
package edu.cmu.cs.dennisc.pattern;

/**
 * @author Dennis Cosgrove
 */
public class Tuple6<A, B, C, D, E, F> {
	private A m_a = null;
	private B m_b = null;
	private C m_c = null;
	private D m_d = null;
	private E m_e = null;
	private F m_f = null;
	public Tuple6() {
	}
	public Tuple6( A a, B b, C c, D d, E e, F f ) {
		set( a, b, c, d, e, f );
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
	public E getE() {
		return m_e;
	}
	public void setE( E e ) {
		m_e = e;
	}
	public F getF() {
		return m_f;
	}
	public void setF( F f ) {
		m_f = f;
	}
	public void set( A a, B b, C c, D d, E e, F f ) {
		m_a = a;
		m_b = b;
		m_c = c;
		m_d = d;
		m_e = e;
		m_f = f;
	}
	@Override
	public boolean equals( Object other ) {
		if( super.equals( other ) ) {
			return true;
		} else {
			if( other instanceof Tuple6 ) {
				Tuple6<?,?,?,?,?,?> otherT = (Tuple6<?,?,?,?,?,?>)other;
				return edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_a, otherT.m_a ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_b, otherT.m_b ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_c, otherT.m_c ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_d, otherT.m_d ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_e, otherT.m_e ) && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_f, otherT.m_f );
			} else {
				return false;
			}
		}
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "edu.cmu.cs.dennisc.pattern.Tuple7[ a=" );
		sb.append( m_a );
		sb.append( ", b=" );
		sb.append( m_b );
		sb.append( ", c=" );
		sb.append( m_c );
		sb.append( ", d=" );
		sb.append( m_d );
		sb.append( ", e=" );
		sb.append( m_e );
		sb.append( ", f=" );
		sb.append( m_f );
		sb.append( " ]" );
		return sb.toString();
	}
}
