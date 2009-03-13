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
public class SpringUtilities {

//	public static javax.swing.SpringLayout springItUpANotch( java.awt.Container pane, java.awt.Component[][] componentRows, int xPad, int yPad ) {
//		assert componentRows != null;
//		int rowCount = componentRows.length;
//		assert componentRows.length > 0;
//		int columnCount = componentRows[ 0 ].length;
//		for( java.awt.Component[] componentRow : componentRows ) {
//			assert componentRow.length == columnCount;
//		}
//		javax.swing.SpringLayout layout = new javax.swing.SpringLayout();
//		pane.setLayout( layout );
//
//		for( java.awt.Component[] componentRow : componentRows ) {
//			for( java.awt.Component component : componentRow ) {
//				pane.add( component );
//			}
//		}
//
//		int r;
//		int c;
//		javax.swing.Spring xSpring = javax.swing.Spring.constant( 0 );
//		javax.swing.Spring xPadSpring = javax.swing.Spring.constant( xPad );	
//		for( c=0; c<columnCount; c++ ) { 
//			javax.swing.Spring widthSpring = javax.swing.Spring.constant( 0 );
//			for( r=0; r<rowCount; r++ ) { 
//				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRows[ r ][ c ] );
//				widthSpring = javax.swing.Spring.max( widthSpring, constraints.getWidth() );
//			}
//			for( r=0; r<rowCount; r++ ) { 
//				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRows[ r ][ c ] );
//				constraints.setX( xSpring );
//				constraints.setWidth( widthSpring );
//			}
//			xSpring = javax.swing.Spring.sum( xSpring, widthSpring );
//			if( c<columnCount-1 ) {
//				xSpring = javax.swing.Spring.sum( xSpring, xPadSpring );
//			}
//		}
//		javax.swing.Spring ySpring = javax.swing.Spring.constant( 0 );
//		javax.swing.Spring yPadSpring = javax.swing.Spring.constant( yPad );	
//		for( r=0; r<rowCount; r++ ) { 
//			javax.swing.Spring heightSpring = javax.swing.Spring.constant( 0 );
//			for( c=0; c<columnCount; c++ ) { 
//				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRows[ r ][ c ] );
//				heightSpring = javax.swing.Spring.max( heightSpring, constraints.getHeight() );
//			}
//			for( c=0; c<columnCount; c++ ) { 
//				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRows[ r ][ c ] );
//				constraints.setY( ySpring );
//				constraints.setHeight( heightSpring );
//			}
//			ySpring = javax.swing.Spring.sum( ySpring, heightSpring );
//			if( r<rowCount-1 ) {
//				ySpring = javax.swing.Spring.sum( ySpring, yPadSpring );
//			}
//		}
//		javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( pane );
//		constraints.setConstraint( javax.swing.SpringLayout.EAST, xSpring );
//		constraints.setConstraint( javax.swing.SpringLayout.SOUTH, ySpring );
//		return layout;
//	}
	public static java.awt.Container springItUpANotch( java.awt.Container rv, java.util.List<java.awt.Component[]> componentRows, int xPad, int yPad ) {
		assert componentRows != null;
		int rowCount = componentRows.size();
		assert rowCount > 0;
		int columnCount = componentRows.get( 0 ).length;
		for( java.awt.Component[] componentRow : componentRows ) {
			assert componentRow.length == columnCount;
		}
		javax.swing.SpringLayout layout = new javax.swing.SpringLayout();
		rv.setLayout( layout );

		for( java.awt.Component[] componentRow : componentRows ) {
			for( java.awt.Component component : componentRow ) {
				assert component != null;
				rv.add( component );
			}
		}

		javax.swing.Spring xSpring = javax.swing.Spring.constant( 0 );
		javax.swing.Spring xPadSpring = javax.swing.Spring.constant( xPad );	
		for( int c=0; c<columnCount; c++ ) { 
			javax.swing.Spring widthSpring = javax.swing.Spring.constant( 0 );
			for( java.awt.Component[] componentRow : componentRows ) { 
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				widthSpring = javax.swing.Spring.max( widthSpring, constraints.getWidth() );
			}
			for( java.awt.Component[] componentRow : componentRows ) { 
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				constraints.setX( xSpring );
				constraints.setWidth( widthSpring );
			}
			xSpring = javax.swing.Spring.sum( xSpring, widthSpring );
			if( c<columnCount-1 ) {
				xSpring = javax.swing.Spring.sum( xSpring, xPadSpring );
			}
		}
		javax.swing.Spring ySpring = javax.swing.Spring.constant( 0 );
		javax.swing.Spring yPadSpring = javax.swing.Spring.constant( yPad );	
		for( int r=0; r<rowCount; r++ ) { 
			java.awt.Component[] componentRow = componentRows.get( r );
			javax.swing.Spring heightSpring = javax.swing.Spring.constant( 0 );
			for( int c=0; c<columnCount; c++ ) { 
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				heightSpring = javax.swing.Spring.max( heightSpring, constraints.getHeight() );
			}
			for( int c=0; c<columnCount; c++ ) { 
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				constraints.setY( ySpring );
				constraints.setHeight( heightSpring );
			}
			ySpring = javax.swing.Spring.sum( ySpring, heightSpring );
			if( r<rowCount-1 ) {
				ySpring = javax.swing.Spring.sum( ySpring, yPadSpring );
			}
		}
		javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( rv );
		constraints.setConstraint( javax.swing.SpringLayout.EAST, xSpring );
		constraints.setConstraint( javax.swing.SpringLayout.SOUTH, ySpring );
		return rv;
	}
//	public static java.awt.Container springItUpANotch( java.awt.Container rv, java.awt.Component[][] array, int xPad, int yPad ) {
//		java.util.ArrayList<java.awt.Component[]> list = new java.util.ArrayList< java.awt.Component[] >();
//		list.ensureCapacity( array.length );
//		for( java.awt.Component[] row : array ) {
//			list.add( row );
//		}
//		return springItUpANotch( rv, list, xPad, yPad );
//	}
	
	public static java.awt.Component expandToBounds( java.awt.Component rv, java.awt.Container container, int xInset, int yInset ) {
		java.awt.LayoutManager layout = container.getLayout();
		javax.swing.SpringLayout springLayout;
		if( layout instanceof javax.swing.SpringLayout ) {
			springLayout = (javax.swing.SpringLayout)layout;
		} else {
			springLayout = new javax.swing.SpringLayout();
			container.setLayout( springLayout );
		}
		springLayout.putConstraint( javax.swing.SpringLayout.NORTH, rv, yInset, javax.swing.SpringLayout.NORTH, container );
		springLayout.putConstraint( javax.swing.SpringLayout.WEST, rv, xInset, javax.swing.SpringLayout.WEST, container );
		springLayout.putConstraint( javax.swing.SpringLayout.SOUTH, rv, -yInset, javax.swing.SpringLayout.SOUTH, container );
		springLayout.putConstraint( javax.swing.SpringLayout.EAST, rv, -xInset, javax.swing.SpringLayout.EAST, container );
		return rv;
	}
	public static java.awt.Component expandToBounds( java.awt.Component rv, int xInset, int yInset ) {
		return expandToBounds( rv, rv.getParent(), xInset, yInset );
	}
	public static java.awt.Component expandToBounds( java.awt.Component rv, java.awt.Container container ) {
		return expandToBounds( rv, rv.getParent(), 0, 0 );
	}
	public static java.awt.Component expandToBounds( java.awt.Component rv ) {
		return expandToBounds( rv, rv.getParent(), 0, 0 );
	}
}
