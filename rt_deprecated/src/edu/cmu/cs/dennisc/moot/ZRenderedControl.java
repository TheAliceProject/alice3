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
package edu.cmu.cs.dennisc.moot;

/**
 * @author Dennis Cosgrove
 */
public abstract class ZRenderedControl<E> extends ZControl {
	public ZRenderedControl( int axis ) {
		super( axis );
	}
	protected abstract Renderer< E > getRenderer();
	protected abstract E getContext();
	
	@Override
	public void paint( java.awt.Graphics g ) {
		Renderer< E > renderer = this.getRenderer();
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		if( renderer != null ) {
			renderer.paintPrologue( getContext(), this, g2, 0, 0, getWidth(), getHeight(), isActive(), isPressed(), isSelected() );
		}
		super.paint( g );
		if( renderer != null ) {
			renderer.paintEpilogue( getContext(), this, g2, 0, 0, getWidth(), getHeight(), isActive(), isPressed(), isSelected() );
		}
	}
}
