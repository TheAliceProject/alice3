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
public class CompassPointSpringPane extends CornerSpringPane {
	private java.awt.Component northComponent;
	private java.awt.Component eastComponent;
	private java.awt.Component southComponent;
	private java.awt.Component westComponent;
	
	public java.awt.Component getNorthComponent() {
		return this.northComponent;
	}
	public void setNorthComponent( java.awt.Component northComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.northComponent != null ) {
			springLayout.removeLayoutComponent( this.northComponent );
			this.remove( this.northComponent );
		}
		this.northComponent = northComponent;
		if( this.northComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.northComponent, Horizontal.CENTER, 0, Vertical.NORTH, pad );
			this.add( this.northComponent );
		}
	}
	public java.awt.Component getEastComponent() {
		return this.eastComponent;
	}
	public void setEastComponent( java.awt.Component eastComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.eastComponent != null ) {
			springLayout.removeLayoutComponent( this.eastComponent );
			this.remove( this.eastComponent );
		}
		this.eastComponent = eastComponent;
		if( this.eastComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.eastComponent, Horizontal.EAST, -pad, Vertical.CENTER, 0 );
			this.add( this.eastComponent );
		}
	}
	public java.awt.Component getSouthComponent() {
		return this.southComponent;
	}
	public void setSouthComponent( java.awt.Component southComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.southComponent != null ) {
			springLayout.removeLayoutComponent( this.southComponent );
			this.remove( this.southComponent );
		}
		this.southComponent = southComponent;
		if( this.southComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.southComponent, Horizontal.CENTER, 0, Vertical.SOUTH, -pad );
			this.add( this.southComponent );
		}
	}
	public java.awt.Component getWestComponent() {
		return this.westComponent;
	}
	public void setWestComponent( java.awt.Component westComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.westComponent != null ) {
			springLayout.removeLayoutComponent( this.westComponent );
			this.remove( this.westComponent );
		}
		this.westComponent = westComponent;
		if( this.westComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.westComponent, Horizontal.WEST, -pad, Vertical.CENTER, 0 );
			this.add( this.westComponent );
		}
	}
}
