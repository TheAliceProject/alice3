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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public abstract class ElementAdapter< E extends edu.cmu.cs.dennisc.scenegraph.Element > extends AbstractElementAdapter< E > {
	// @Override
	// protected void finalize() throws Throwable {
	// super.finalize();
	// System.err.println( "finalize: " + this );
	// }

	@Override
	public void initialize( E element ) {
		super.initialize( element );
		for( edu.cmu.cs.dennisc.property.Property<?> property : m_element.getProperties() ) {
			edu.cmu.cs.dennisc.property.InstanceProperty<?> instanceProperty = (edu.cmu.cs.dennisc.property.InstanceProperty<?>)property;
			propertyChanged( instanceProperty );
		}
	}

	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		// todo:
		System.err.println( "unhandled property: " + property );
	}

	public static void handlePropertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> instanceProperty ) {
		edu.cmu.cs.dennisc.scenegraph.Element sgElement = (edu.cmu.cs.dennisc.scenegraph.Element)instanceProperty.getOwner();
		ElementAdapter elementAdapter = (ElementAdapter)AdapterFactory.getAdapterForElement( sgElement );
		elementAdapter.propertyChanged( instanceProperty );
	}

	@Override
	public String toString() {
		if( m_element != null ) {
			return getClass().getName() + " " + m_element.toString();
		} else {
			return super.toString();
		}
	}
}
