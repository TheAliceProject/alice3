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

package edu.cmu.cs.dennisc.lookingglass.overlay;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractLayoutManager implements LayoutManager {
	private java.util.List< Composite > m_composites = new java.util.LinkedList< Composite >();
	public void addComposite( Composite composite ) {
		assert composite != null;
		m_composites.add( composite );
		composite.setLayoutRequired( true );
	}
	public void removeComposite( Composite composite ) {
		assert composite != null;
		m_composites.remove( composite );
	}
	public Iterable< Composite > getComposites() {
		return m_composites;
	}

	protected void fireLayoutRequired() {
		for( Composite composite : m_composites ) {
			composite.setLayoutRequired( true );
		}
	}
	public void computePreferredSizes( Composite composite, java.awt.Graphics g, int width, int height ) {
		for( Component component : composite.getComponents() ) {
			if( component.isVisible() ) {
				component.computePreferredSize( g, width, height );
			}
		}
	}
}
