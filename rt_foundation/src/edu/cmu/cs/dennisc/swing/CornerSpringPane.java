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
public class CornerSpringPane extends javax.swing.JPanel {
	private javax.swing.SpringLayout springLayout;
	private java.awt.Component northWestComponent;
	private java.awt.Component northEastComponent;
	private java.awt.Component southWestComponent;
	private java.awt.Component southEastComponent;
	
	public CornerSpringPane() {
		this.springLayout = new javax.swing.SpringLayout();
		setLayout( this.springLayout );
	}
	protected javax.swing.SpringLayout getSpringLayout() {
		return this.springLayout;
	}
	protected int getPad() {
		return 8;
	}
	public java.awt.Component getNorthWestComponent() {
		return this.northWestComponent;
	}
	public void setNorthWestComponent( java.awt.Component northWestComponent ) {
		if( this.northWestComponent != null ) {
			this.springLayout.removeLayoutComponent( this.northWestComponent );
			this.remove( this.northWestComponent );
		}
		this.northWestComponent = northWestComponent;
		if( this.northWestComponent != null ) {
			int pad = getPad();
			this.springLayout.putConstraint( javax.swing.SpringLayout.NORTH, this.northWestComponent, pad, javax.swing.SpringLayout.NORTH, this );
			this.springLayout.putConstraint( javax.swing.SpringLayout.WEST, this.northWestComponent, pad, javax.swing.SpringLayout.WEST, this );
			this.add( this.northWestComponent );
		}
	}
	public java.awt.Component getNorthEastComponent() {
		return this.northEastComponent;
	}
	public void setNorthEastComponent( java.awt.Component northEastComponent ) {
		if( this.northEastComponent != null ) {
			this.springLayout.removeLayoutComponent( this.northEastComponent );
			this.remove( this.northEastComponent );
		}
		this.northEastComponent = northEastComponent;
		if( this.northEastComponent != null ) {
			int pad = getPad();
			this.springLayout.putConstraint( javax.swing.SpringLayout.NORTH, this.northEastComponent, pad, javax.swing.SpringLayout.NORTH, this );
			this.springLayout.putConstraint( javax.swing.SpringLayout.EAST, this.northEastComponent, -pad, javax.swing.SpringLayout.EAST, this );
			this.add( this.northEastComponent );
		}
	}
	public java.awt.Component getSouthWestComponent() {
		return this.southWestComponent;
	}
	public void setSouthWestComponent( java.awt.Component southWestComponent ) {
		if( this.southWestComponent != null ) {
			this.springLayout.removeLayoutComponent( this.southWestComponent );
			this.remove( this.southWestComponent );
		}
		this.southWestComponent = southWestComponent;
		if( this.southWestComponent != null ) {
			int pad = getPad();
			this.springLayout.putConstraint( javax.swing.SpringLayout.SOUTH, this.southWestComponent, -pad, javax.swing.SpringLayout.SOUTH, this );
			this.springLayout.putConstraint( javax.swing.SpringLayout.WEST, this.southWestComponent, pad, javax.swing.SpringLayout.WEST, this );
			this.add( this.southWestComponent );
		}
	}
	public java.awt.Component getSouthEastComponent() {
		return this.southEastComponent;
	}
	public void setSouthEastComponent( java.awt.Component southEastComponent ) {
		if( this.southEastComponent != null ) {
			this.springLayout.removeLayoutComponent( this.southEastComponent );
			this.remove( this.southEastComponent );
		}
		this.southEastComponent = southEastComponent;
		if( this.southEastComponent != null ) {
			int pad = getPad();
			this.springLayout.putConstraint( javax.swing.SpringLayout.SOUTH, this.southEastComponent, -pad, javax.swing.SpringLayout.SOUTH, this );
			this.springLayout.putConstraint( javax.swing.SpringLayout.EAST, this.southEastComponent, -pad, javax.swing.SpringLayout.EAST, this );
			this.add( this.southEastComponent );
		}
	}
}
