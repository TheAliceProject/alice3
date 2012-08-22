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
package edu.cmu.cs.dennisc.property;

/**
 * @author Dennis Cosgrove
 */
public class GetterSetterProperty<E> implements Property<E> {
	private static Object[] s_getterArgs = new Object[] {};
	private static Object[] s_setterArgs = new Object[] { null };
	private java.lang.reflect.Method m_getter;
	private java.lang.reflect.Method m_setter;
	private String m_name;

	public GetterSetterProperty( java.lang.reflect.Method getter, java.lang.reflect.Method setter ) {
		m_getter = getter;
		m_setter = setter;
		m_name = PropertyUtilities.getPropertyNameForGetter( m_getter );
	}

	public GetterSetterProperty( Class<? extends PropertyOwner> cls, String name ) {
		this( PropertyUtilities.getGetter( cls, name ), PropertyUtilities.getSetter( cls, name ) );
	}

	public String getName() {
		return m_name;
	}

	public E getValue( PropertyOwner owner ) {
		synchronized( s_getterArgs ) {
			return (E)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( owner, m_getter, s_getterArgs );
		}
	}

	public void setValue( PropertyOwner owner, E value ) {
		synchronized( s_setterArgs ) {
			s_setterArgs[ 0 ] = value;
			edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( owner, m_setter, s_setterArgs );
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( this.getClass().getName() );
		sb.append( "[" );
		sb.append( m_name );
		sb.append( "]" );
		return sb.toString();
	}
}
