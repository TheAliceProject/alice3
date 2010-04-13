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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public abstract class Element extends edu.cmu.cs.dennisc.pattern.DefaultInstancePropertyOwner {
	//	@Override
	//	protected void finalize() throws Throwable {
	//		super.finalize();
	//		System.out.println( "finalize: " + this );
	//	}

	private java.util.HashMap< Object, Object > m_runtimeDataMap = new java.util.HashMap< Object, Object >();

	@Override
	public boolean isComposedOfGetterAndSetterProperties() {
		return false;
	}
	public boolean containsBonusDataFor( Object key ) {
		return m_runtimeDataMap.containsKey( key );
	}
	public Object getBonusDataFor( Object key ) {
		return m_runtimeDataMap.get( key );
	}
	public void putBonusDataFor( Object key, Object value ) {
		m_runtimeDataMap.put( key, value );
	}
	public void removeBonusDataFor( Object key ) {
		m_runtimeDataMap.remove( key );
	}

	//todo: investigate typing return value with generics
	//todo: support copying referenced elements?
	public Element newCopy() {
		Element rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( this.getClass() );
		rv.setName( this.getName() );
		for( edu.cmu.cs.dennisc.property.Property property : this.getProperties() ) {
			Object value;
			if( property instanceof edu.cmu.cs.dennisc.property.CopyableProperty ) {
				value = ((edu.cmu.cs.dennisc.property.CopyableProperty)property).getCopy( this );
			} else {
				value = property.getValue( this );
			}
			edu.cmu.cs.dennisc.property.Property rvProperty = rv.getPropertyNamed( property.getName() );
			rvProperty.setValue( rv, value );
		}
		return rv;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[name=\"" + getName() + "\"]";
	}
}
