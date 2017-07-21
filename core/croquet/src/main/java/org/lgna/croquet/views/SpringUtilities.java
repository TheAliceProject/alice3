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
package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class SpringUtilities {
	private SpringUtilities() {
		throw new AssertionError();
	}

	public static AwtComponentView<?> createTrailingLabel( String text ) {
		Label rv = new Label( text );
		rv.setHorizontalAlignment( HorizontalAlignment.TRAILING );
		return rv;
	}

	public static AwtComponentView<?> createTrailingTopLabel( String text ) {
		Label rv = new Label( text );
		rv.setHorizontalAlignment( HorizontalAlignment.TRAILING );
		rv.setVerticalAlignment( VerticalAlignment.TOP );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 6, 0, 0, 0 ) );
		return rv;
	}

	public static AwtComponentView<?>[] createRow( AwtComponentView<?>... rv ) {
		for( int i = 0; i < rv.length; i++ ) {
			if( rv[ i ] != null ) {
				//pass
			} else {
				AwtComponentView<?> box = org.lgna.croquet.views.BoxUtilities.createRigidArea( 0, 0 );
				//				box.setBackground( java.awt.Color.BLUE );
				//				if( box instanceof javax.swing.JComponent ) {
				//					((javax.swing.JComponent)box).setOpaque( false );
				//				}
				rv[ i ] = box;
			}
		}
		return rv;
	}

	public static AwtComponentView<?>[] createLabeledRow( String labelText, AwtComponentView<?>... components ) {
		AwtComponentView<?>[] rv = new AwtComponentView<?>[ components.length + 1 ];
		rv[ 0 ] = createTrailingLabel( labelText );
		System.arraycopy( components, 0, rv, 1, components.length );
		return createRow( rv );
	}

	public static AwtComponentView<?>[] createTopLabeledRow( String labelText, AwtComponentView<?>... components ) {
		AwtComponentView<?>[] rv = new AwtComponentView<?>[ components.length + 1 ];
		rv[ 0 ] = createTrailingTopLabel( labelText );
		System.arraycopy( components, 0, rv, 1, components.length );
		return createRow( rv );
	}

	public static SpringPanel springItUpANotch( SpringPanel rv, java.util.List<AwtComponentView<?>[]> componentRows, int xPad, int yPad ) {
		assert componentRows != null;
		int rowCount = componentRows.size();
		assert rowCount > 0;
		int columnCount = componentRows.get( 0 ).length;
		for( AwtComponentView<?>[] componentRow : componentRows ) {
			assert componentRow.length == columnCount;
		}
		for( AwtComponentView<?>[] componentRow : componentRows ) {
			for( AwtComponentView<?> component : componentRow ) {
				//assert component != null;
				if( component != null ) {
					rv.internalAddComponent( component );
				} else {
					rv.internalAddComponent( org.lgna.croquet.views.BoxUtilities.createRigidArea( 0, 0 ) );
				}
			}
		}

		javax.swing.SpringLayout layout = (javax.swing.SpringLayout)rv.getAwtComponent().getLayout();

		javax.swing.Spring xSpring = javax.swing.Spring.constant( 0 );
		javax.swing.Spring xPadSpring = javax.swing.Spring.constant( xPad );
		for( int c = 0; c < columnCount; c++ ) {
			javax.swing.Spring widthSpring = javax.swing.Spring.constant( 0 );
			for( AwtComponentView<?>[] componentRow : componentRows ) {
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ].getAwtComponent() );
				widthSpring = javax.swing.Spring.max( widthSpring, constraints.getWidth() );
			}
			for( AwtComponentView<?>[] componentRow : componentRows ) {
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ].getAwtComponent() );
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
			AwtComponentView<?>[] componentRow = componentRows.get( r );
			javax.swing.Spring heightSpring = javax.swing.Spring.constant( 0 );
			for( int c = 0; c < columnCount; c++ ) {
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ].getAwtComponent() );
				heightSpring = javax.swing.Spring.max( heightSpring, constraints.getHeight() );
			}
			for( int c = 0; c < columnCount; c++ ) {
				javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( componentRow[ c ].getAwtComponent() );
				constraints.setY( ySpring );
				constraints.setHeight( heightSpring );
			}
			ySpring = javax.swing.Spring.sum( ySpring, heightSpring );
			if( r < ( rowCount - 1 ) ) {
				ySpring = javax.swing.Spring.sum( ySpring, yPadSpring );
			}
		}
		javax.swing.SpringLayout.Constraints constraints = layout.getConstraints( rv.getAwtComponent() );
		constraints.setConstraint( javax.swing.SpringLayout.EAST, xSpring );
		constraints.setConstraint( javax.swing.SpringLayout.SOUTH, ySpring );
		return rv;
	}

}
