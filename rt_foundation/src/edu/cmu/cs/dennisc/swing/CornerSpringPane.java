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
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class CornerSpringPane extends SpringPane {
	private java.awt.Component northWestComponent;
	private java.awt.Component northEastComponent;
	private java.awt.Component southWestComponent;
	private java.awt.Component southEastComponent;
	
	protected int getPad() {
		return 8;
	}
	public java.awt.Component getNorthWestComponent() {
		return this.northWestComponent;
	}
	public void setNorthWestComponent( java.awt.Component northWestComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.northWestComponent != null ) {
			springLayout.removeLayoutComponent( this.northWestComponent );
			this.remove( this.northWestComponent );
		}
		this.northWestComponent = northWestComponent;
		if( this.northWestComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.northWestComponent, Horizontal.WEST, pad, Vertical.NORTH, pad );
			//springLayout.putConstraint( javax.swing.SpringLayout.NORTH, this.northWestComponent, pad, javax.swing.SpringLayout.NORTH, this );
			//springLayout.putConstraint( javax.swing.SpringLayout.WEST, this.northWestComponent, pad, javax.swing.SpringLayout.WEST, this );
			this.add( this.northWestComponent );
		}
	}
	public java.awt.Component getNorthEastComponent() {
		return this.northEastComponent;
	}
	public void setNorthEastComponent( java.awt.Component northEastComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.northEastComponent != null ) {
			springLayout.removeLayoutComponent( this.northEastComponent );
			this.remove( this.northEastComponent );
		}
		this.northEastComponent = northEastComponent;
		if( this.northEastComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.northEastComponent, Horizontal.EAST, -pad, Vertical.NORTH, pad );
//			springLayout.putConstraint( javax.swing.SpringLayout.NORTH, this.northEastComponent, pad, javax.swing.SpringLayout.NORTH, this );
//			springLayout.putConstraint( javax.swing.SpringLayout.EAST, this.northEastComponent, -pad, javax.swing.SpringLayout.EAST, this );
			this.add( this.northEastComponent );
		}
	}
	public java.awt.Component getSouthWestComponent() {
		return this.southWestComponent;
	}
	public void setSouthWestComponent( java.awt.Component southWestComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.southWestComponent != null ) {
			springLayout.removeLayoutComponent( this.southWestComponent );
			this.remove( this.southWestComponent );
		}
		this.southWestComponent = southWestComponent;
		if( this.southWestComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.southWestComponent, Horizontal.WEST, pad, Vertical.SOUTH, -pad );
//			springLayout.putConstraint( javax.swing.SpringLayout.SOUTH, this.southWestComponent, -pad, javax.swing.SpringLayout.SOUTH, this );
//			springLayout.putConstraint( javax.swing.SpringLayout.WEST, this.southWestComponent, pad, javax.swing.SpringLayout.WEST, this );
			this.add( this.southWestComponent );
		}
	}
	public java.awt.Component getSouthEastComponent() {
		return this.southEastComponent;
	}
	public void setSouthEastComponent( java.awt.Component southEastComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.southEastComponent != null ) {
			springLayout.removeLayoutComponent( this.southEastComponent );
			this.remove( this.southEastComponent );
		}
		this.southEastComponent = southEastComponent;
		if( this.southEastComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.southEastComponent, Horizontal.EAST, -pad, Vertical.SOUTH, -pad );
//			springLayout.putConstraint( javax.swing.SpringLayout.SOUTH, this.southEastComponent, -pad, javax.swing.SpringLayout.SOUTH, this );
//			springLayout.putConstraint( javax.swing.SpringLayout.EAST, this.southEastComponent, -pad, javax.swing.SpringLayout.EAST, this );
			this.add( this.southEastComponent );
		}
	}
}
