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
		Element rv = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( this.getClass() );
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
