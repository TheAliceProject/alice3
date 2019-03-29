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

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class SpringUtilities {
	private SpringUtilities() {
		throw new AssertionError();
	}

	public enum Vertical {
		NORTH( SpringLayout.NORTH ),
		CENTER( null ),
		SOUTH( SpringLayout.SOUTH );
		private String internal;

		private Vertical( String internal ) {
			this.internal = internal;
		}

		public String getInternal() {
			return internal;
		}
	}

	public enum Horizontal {
		WEST( SpringLayout.WEST ),
		CENTER( null ),
		EAST( SpringLayout.EAST );
		private String internal;

		private Horizontal( String internal ) {
			this.internal = internal;
		}

		public String getInternal() {
			return internal;
		}
	}

	private static abstract class CenterSpring extends Spring {
		private Container container;
		private Component component;
		private int offset;

		public CenterSpring( Container container, Component component, int offset ) {
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

		protected abstract int getValue( Dimension dimension );

		@Override
		public int getPreferredValue() {
			int macro = getValue( this.container.getSize() );
			Dimension size;
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
		public HorizontalCenterSpring( Container container, Component component, int offset ) {
			super( container, component, offset );
		}

		@Override
		protected int getValue( Dimension dimension ) {
			return dimension.width;
		}
	}

	private static class VerticalCenterSpring extends CenterSpring {
		public VerticalCenterSpring( Container container, Component component, int offset ) {
			super( container, component, offset );
		}

		@Override
		protected int getValue( Dimension dimension ) {
			return dimension.height;
		}
	}

	protected static void putConstraint( Container container, Component component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		SpringLayout springLayout = (SpringLayout)container.getLayout();
		String horizontalConstraint = horizontal.getInternal();
		String verticalConstraint = vertical.getInternal();
		if( horizontalConstraint != null ) {
			springLayout.putConstraint( horizontalConstraint, component, x, horizontalConstraint, container );
		} else {
			springLayout.putConstraint( SpringLayout.WEST, component, new HorizontalCenterSpring( container, component, x ), SpringLayout.WEST, container );
		}
		if( verticalConstraint != null ) {
			springLayout.putConstraint( verticalConstraint, component, y, verticalConstraint, container );
		} else {
			springLayout.putConstraint( SpringLayout.NORTH, component, new VerticalCenterSpring( container, component, y ), SpringLayout.NORTH, container );
		}
	}

	public static void add( Container container, Component component, Horizontal horizontal, int x, Vertical vertical, int y ) {
		putConstraint( container, component, horizontal, x, vertical, y );
		container.add( component );
	}

	public static void addNorthEast( Container container, Component component, int inset ) {
		add( container, component, Horizontal.EAST, -inset, Vertical.NORTH, inset );
	}

	public static void addNorth( Container container, Component component, int inset ) {
		add( container, component, Horizontal.CENTER, 0, Vertical.NORTH, inset );
	}

	public static void addNorthWest( Container container, Component component, int inset ) {
		add( container, component, Horizontal.WEST, inset, Vertical.NORTH, inset );
	}

	public static void addEast( Container container, Component component, int inset ) {
		add( container, component, Horizontal.EAST, -inset, Vertical.CENTER, 0 );
	}

	public static void addCenter( Container container, Component component, int inset ) {
		add( container, component, Horizontal.CENTER, 0, Vertical.CENTER, 0 );
	}

	public static void addWest( Container container, Component component, int inset ) {
		add( container, component, Horizontal.WEST, inset, Vertical.CENTER, 0 );
	}

	public static void addSouthEast( Container container, Component component, int inset ) {
		add( container, component, Horizontal.EAST, -inset, Vertical.SOUTH, -inset );
	}

	public static void addSouth( Container container, Component component, int inset ) {
		add( container, component, Horizontal.CENTER, 0, Vertical.SOUTH, -inset );
	}

	public static void addSouthWest( Container container, Component component, int inset ) {
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
	public static Component createColumn0Label( String text ) {
		JLabel rv = LabelUtilities.createLabel( text );
		rv.setHorizontalAlignment( SwingConstants.TRAILING );
		return rv;
	}

	public static Component[] createRow( Component... rv ) {
		for( int i = 0; i < rv.length; i++ ) {
			if( rv[ i ] != null ) {
				//pass
			} else {
				Component box = Box.createRigidArea( new Dimension( 0, 0 ) );
				//				box.setBackground( java.awt.Color.BLUE );
				//				if( box instanceof javax.swing.JComponent ) {
				//					((javax.swing.JComponent)box).setOpaque( false );
				//				}
				rv[ i ] = box;
			}
		}
		return rv;
	}

	public static Container springItUpANotch( Container rv, List<Component[]> componentRows, int xPad, int yPad ) {
		assert componentRows != null;
		int rowCount = componentRows.size();
		assert rowCount > 0;
		int columnCount = componentRows.get( 0 ).length;
		for( Component[] componentRow : componentRows ) {
			assert componentRow.length == columnCount;
		}
		SpringLayout layout = new SpringLayout();
		rv.setLayout( layout );

		for( Component[] componentRow : componentRows ) {
			for( Component component : componentRow ) {
				assert component != null;
				rv.add( component );
			}
		}

		Spring xSpring = Spring.constant( 0 );
		Spring xPadSpring = Spring.constant( xPad );
		for( int c = 0; c < columnCount; c++ ) {
			Spring widthSpring = Spring.constant( 0 );
			for( Component[] componentRow : componentRows ) {
				SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				widthSpring = Spring.max( widthSpring, constraints.getWidth() );
			}
			for( Component[] componentRow : componentRows ) {
				SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				constraints.setX( xSpring );
				constraints.setWidth( widthSpring );
			}
			xSpring = Spring.sum( xSpring, widthSpring );
			if( c < ( columnCount - 1 ) ) {
				xSpring = Spring.sum( xSpring, xPadSpring );
			}
		}
		Spring ySpring = Spring.constant( 0 );
		Spring yPadSpring = Spring.constant( yPad );
		for( int r = 0; r < rowCount; r++ ) {
			Component[] componentRow = componentRows.get( r );
			Spring heightSpring = Spring.constant( 0 );
			for( int c = 0; c < columnCount; c++ ) {
				SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				heightSpring = Spring.max( heightSpring, constraints.getHeight() );
			}
			for( int c = 0; c < columnCount; c++ ) {
				SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ] );
				constraints.setY( ySpring );
				constraints.setHeight( heightSpring );
			}
			ySpring = Spring.sum( ySpring, heightSpring );
			if( r < ( rowCount - 1 ) ) {
				ySpring = Spring.sum( ySpring, yPadSpring );
			}
		}
		SpringLayout.Constraints constraints = layout.getConstraints( rv );
		constraints.setConstraint( SpringLayout.EAST, xSpring );
		constraints.setConstraint( SpringLayout.SOUTH, ySpring );
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

	public static Component expandToBounds( Component rv, Container container, int xInset, int yInset ) {
		LayoutManager layout = container.getLayout();
		SpringLayout springLayout;
		if( layout instanceof SpringLayout ) {
			springLayout = (SpringLayout)layout;
		} else {
			springLayout = new SpringLayout();
			container.setLayout( springLayout );
		}
		springLayout.putConstraint( SpringLayout.NORTH, rv, yInset, SpringLayout.NORTH, container );
		springLayout.putConstraint( SpringLayout.WEST, rv, xInset, SpringLayout.WEST, container );
		springLayout.putConstraint( SpringLayout.SOUTH, rv, -yInset, SpringLayout.SOUTH, container );
		springLayout.putConstraint( SpringLayout.EAST, rv, -xInset, SpringLayout.EAST, container );
		return rv;
	}

	public static Component expandToBounds( Component rv, int xInset, int yInset ) {
		return expandToBounds( rv, rv.getParent(), xInset, yInset );
	}

	public static Component expandToBounds( Component rv, Container container ) {
		return expandToBounds( rv, rv.getParent(), 0, 0 );
	}

	public static Component expandToBounds( Component rv ) {
		return expandToBounds( rv, rv.getParent(), 0, 0 );
	}

}
