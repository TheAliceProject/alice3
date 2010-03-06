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
package edu.cmu.cs.dennisc.javax.swing;

/**
 * @author Dennis Cosgrove
 */
public class SpringUtilities {
	private SpringUtilities() {
		throw new AssertionError();
	}
	
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
	private static abstract class CenterSpring extends javax.swing.Spring {
		private java.awt.Container container;
		private java.awt.Component component;
		private int offset;
		public CenterSpring( java.awt.Container container, java.awt.Component component, int offset ) {
			this.container = container;
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
			int macro = getValue( this.container.getSize() );
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
	private static class HorizontalCenterSpring extends CenterSpring {
		public HorizontalCenterSpring( java.awt.Container container, java.awt.Component component, int offset ) {
			super( container, component, offset );
		}
		@Override
		protected int getValue( java.awt.Dimension dimension ) {
			return dimension.width;
		}
	}
	private static class VerticalCenterSpring extends CenterSpring {
		public VerticalCenterSpring( java.awt.Container container, java.awt.Component component, int offset ) {
			super( container, component, offset );
		}
		@Override
		protected int getValue( java.awt.Dimension dimension ) {
			return dimension.height;
		}
	}
	protected static void putConstraint( java.awt.Container container, java.awt.Component component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		javax.swing.SpringLayout springLayout = (javax.swing.SpringLayout)container.getLayout();
		String horizontalConstraint = horizontal.getInternal();
		String verticalConstraint = vertical.getInternal();
		if( horizontalConstraint != null ) {
			springLayout.putConstraint( horizontalConstraint, component, x, horizontalConstraint, container );
		} else {
			springLayout.putConstraint( javax.swing.SpringLayout.WEST, component, new HorizontalCenterSpring( container, component, x ), javax.swing.SpringLayout.WEST, container );
		}
		if( verticalConstraint != null ) {
			springLayout.putConstraint( verticalConstraint, component, y, verticalConstraint, container );
		} else {
			springLayout.putConstraint( javax.swing.SpringLayout.NORTH, component, new VerticalCenterSpring( container, component, y ), javax.swing.SpringLayout.NORTH, container );
		}
	}
	
	public static void add( java.awt.Container container, java.awt.Component component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		putConstraint(container, component, horizontal, x, vertical, y);
		container.add( component );
	}
	
	public static void addNorthEast( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.EAST, -inset, Vertical.NORTH, inset );
	}
	public static void addNorth( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.CENTER, 0, Vertical.NORTH, inset );
	}
	public static void addNorthWest( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.WEST, inset, Vertical.NORTH, inset );
	}

	public static void addEast( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.EAST, -inset, Vertical.CENTER, 0 );
	}
	public static void addCenter( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.CENTER, 0, Vertical.CENTER, 0 );
	}
	public static void addWest( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.WEST, inset, Vertical.CENTER, 0 );
	}

	public static void addSouthEast( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.EAST, -inset, Vertical.SOUTH, -inset );
	}
	public static void addSouth( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.CENTER, 0, Vertical.SOUTH, -inset );
	}
	public static void addSouthWest( java.awt.Container container, java.awt.Component component, int inset ) {
		add( container, component, Horizontal.WEST, inset, Vertical.SOUTH, -inset );
	}
	
}
