/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.javax.swing;

/**
 * @author Dennis Cosgrove
 */
public class SpringUtilities {
	private SpringUtilities() {
		throw new AssertionError();
	}

	public enum Vertical {
		NORTH( javax.swing.SpringLayout.NORTH ),
		CENTER( null ),
		SOUTH( javax.swing.SpringLayout.SOUTH );
		private String internal;

		private Vertical( String internal ) {
			this.internal = internal;
		}

		public String getInternal() {
			return internal;
		}
	}

	public enum Horizontal {
		WEST( javax.swing.SpringLayout.WEST ),
		CENTER( null ),
		EAST( javax.swing.SpringLayout.EAST );
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
			return this.offset + ( ( macro - micro ) / 2 );
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
		putConstraint( container, component, horizontal, x, vertical, y );
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
	public static java.awt.Component createColumn0Label( String text ) {
		javax.swing.JLabel rv = edu.cmu.cs.dennisc.javax.swing.LabelUtilities.createLabel( text );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}

	public static java.awt.Component[] createRow( java.awt.Component... rv ) {
		for( int i = 0; i < rv.length; i++ ) {
			if( rv[ i ] != null ) {
				//pass
			} else {
				java.awt.Component box = javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 0 ) );
				//				box.setBackground( java.awt.Color.BLUE );
				//				if( box instanceof javax.swing.JComponent ) {
				//					((javax.swing.JComponent)box).setOpaque( false );
				//				}
				rv[ i ] = box;
			}
		}
		return rv;
	}

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
		for( int c = 0; c < columnCount; c++ ) {
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
			if( c < ( columnCount - 1 ) ) {
				xSpring = javax.swing.Spring.sum( xSpring, xPadSpring );
			}
		}
		javax.swing.Spring ySpring = javax.swing.Spring.constant( 0 );
		javax.swing.Spring yPadSpring = javax.swing.Spring.constant( yPad );
		for( int r = 0; r < rowCount; r++ ) {
			java.awt.Component[] componentRow = componentRows.get( r );
			javax.swing.Spring heightSpring = javax.swing.Spring.constant( 0 );
			for( int c = 0; c < columnCount; c++ ) {
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				heightSpring = javax.swing.Spring.max( heightSpring, constraints.getHeight() );
			}
			for( int c = 0; c < columnCount; c++ ) {
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				constraints.setY( ySpring );
				constraints.setHeight( heightSpring );
			}
			ySpring = javax.swing.Spring.sum( ySpring, heightSpring );
			if( r < ( rowCount - 1 ) ) {
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
