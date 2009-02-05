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
public class SpringPane extends javax.swing.JPanel {
	public enum Vertical {
		NORTH(javax.swing.SpringLayout.NORTH), 
		//CENTER(null), 
		SOUTH(javax.swing.SpringLayout.SOUTH);
		private String internal;
		private Vertical( String internal ) {
			this.internal = internal;
		}
		public String getInternal() {
			return internal;
		}
	}
	public enum Horizontal {
		WEST(javax.swing.SpringLayout.WEST), 
		//CENTER(null), 
		EAST(javax.swing.SpringLayout.EAST);
		private String internal;
		private Horizontal( String internal ) {
			this.internal = internal;
		}
		public String getInternal() {
			return internal;
		}
	}
	private javax.swing.SpringLayout springLayout;
	public SpringPane() {
		this.springLayout = new javax.swing.SpringLayout();
		setLayout( this.springLayout );
	}
	protected javax.swing.SpringLayout getSpringLayout() {
		return this.springLayout;
	}
	
	protected void putConstraint( java.awt.Component component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		this.springLayout.putConstraint( horizontal.getInternal(), component, x, horizontal.getInternal(), this );
		this.springLayout.putConstraint( vertical.getInternal(), component, y, vertical.getInternal(), this );
	}
	
	public void add( java.awt.Component component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		this.putConstraint(component, horizontal, x, vertical, y);
		this.add( component );
	}

	//todo?
//	@Override
//	public void remove( java.awt.Component component ) {
//		super.remove( component );
//		this.springLayout.removeLayoutComponent( component );
//	}
	
}
