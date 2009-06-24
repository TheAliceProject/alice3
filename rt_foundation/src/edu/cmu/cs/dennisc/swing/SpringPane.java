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
		CENTER(null), 
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
		CENTER(null), 
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
	
	abstract class CenterSpring extends javax.swing.Spring {
		private java.awt.Component component;
		private int offset;
		public CenterSpring( java.awt.Component component, int offset ) {
			this.component = component;
			this.offset = offset;
		}
		@Override
		public int getValue() {
			return this.getPreferredValue();
		}
		@Override
		public void setValue( int value ) {
		}
		protected abstract int getValue( java.awt.Dimension dimension );
		@Override
		public int getPreferredValue() {
			int macro = getValue( SpringPane.this.getSize() );
			java.awt.Dimension size;
			if( this.component.isValid() ) {
				size = this.component.getSize();
			} else {
				size = this.component.getPreferredSize();
			}
			int micro = getValue( size );
			return this.offset + (macro - micro) / 2;
		}
		@Override
		public int getMinimumValue() {
			return this.getPreferredValue();
		}
		@Override
		public int getMaximumValue() {
			return this.getPreferredValue();
		}
	}
	class HorizontalCenterSpring extends CenterSpring {
		public HorizontalCenterSpring( java.awt.Component component, int offset ) {
			super( component, offset );
		}
		@Override
		protected int getValue( java.awt.Dimension dimension ) {
			return dimension.width;
		}
	}
	class VerticalCenterSpring extends CenterSpring {
		public VerticalCenterSpring( java.awt.Component component, int offset ) {
			super( component, offset );
		}
		@Override
		protected int getValue( java.awt.Dimension dimension ) {
			return dimension.height;
		}
	}
	protected void putConstraint( java.awt.Component component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		String horizontalConstraint = horizontal.getInternal();
		String verticalConstraint = vertical.getInternal();
		if( horizontalConstraint != null ) {
			this.springLayout.putConstraint( horizontalConstraint, component, x, horizontalConstraint, this );
		} else {
			this.springLayout.putConstraint( javax.swing.SpringLayout.WEST, component, new HorizontalCenterSpring( component, x ), javax.swing.SpringLayout.WEST, this );
		}
		if( verticalConstraint != null ) {
			this.springLayout.putConstraint( verticalConstraint, component, y, verticalConstraint, this );
		} else {
			this.springLayout.putConstraint( javax.swing.SpringLayout.NORTH, component, new VerticalCenterSpring( component, y ), javax.swing.SpringLayout.NORTH, this );
		}
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
