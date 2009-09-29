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
package edu.cmu.cs.dennisc.zoot;


/**
 * @author Dennis Cosgrove
 */
class DropReceptorInfo {
	private DropReceptor dropReceptor;
	private java.awt.Rectangle bounds;
	public DropReceptorInfo( DropReceptor dropReceptor, java.awt.Rectangle bounds ) {
		this.dropReceptor = dropReceptor;
		this.bounds = bounds;
	}
	public boolean contains( int x, int y ) {
		return this.bounds.contains( x, y );
	}
	public boolean intersects( java.awt.Rectangle rectangle ) {
		return this.bounds.intersects( rectangle );
	}
	public DropReceptor getDropReceptor() {
		return this.dropReceptor;
	}
	public void setDropReceptor( DropReceptor dropReceptor ) {
		this.dropReceptor = dropReceptor;
	}
	public java.awt.Rectangle getBounds() {
		return this.bounds;
	}
	public void setBounds( java.awt.Rectangle bounds ) {
		this.bounds = bounds;
	}
}
