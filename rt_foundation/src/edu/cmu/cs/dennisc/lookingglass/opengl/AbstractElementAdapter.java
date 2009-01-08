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
public abstract class AbstractElementAdapter< E extends edu.cmu.cs.dennisc.pattern.AbstractElement > {
	protected E m_element;

	// @Override
	// protected void finalize() throws Throwable {
	// super.finalize();
	// System.err.println( "finalize: " + this );
	// }

	public void handleReleased() {
		AdapterFactory.forget( m_element );
		ChangeHandler.removeListenersAndObservers( m_element );
		m_element = null;
	}

	public static void handleReleased( edu.cmu.cs.dennisc.pattern.event.ReleaseEvent e ) {
		edu.cmu.cs.dennisc.pattern.Releasable releasable = e.getTypedSource();
		if( releasable instanceof edu.cmu.cs.dennisc.pattern.AbstractElement ) {
			edu.cmu.cs.dennisc.pattern.AbstractElement element = (edu.cmu.cs.dennisc.pattern.AbstractElement)releasable;
			AbstractElementAdapter elementAdapter = AdapterFactory.getAdapterForElement( element );
			if( elementAdapter != null ) {
				elementAdapter.handleReleased();
			}
		} else {
			throw new Error( "this should never occur" );
		}
	}

	public void initialize( E element ) {
		m_element = element;
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
