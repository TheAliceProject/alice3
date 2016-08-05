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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class TypeBorder implements javax.swing.border.Border {
	private static final int X_INSET = 8;
	private static final int Y_INSET = 2;
	private static java.awt.Insets insets = new java.awt.Insets( Y_INSET, X_INSET, Y_INSET, X_INSET );
	private static java.awt.Color FILL_COLOR = org.alice.ide.ThemeUtilities.getActiveTheme().getColorFor( org.lgna.project.ast.TypeExpression.class );
	private static java.awt.Color FILL_BRIGHTER_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( FILL_COLOR, 1.0, 0.5, 1.4 );
	private static java.awt.Color FILL_DARKER_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( FILL_COLOR, 1.0, 1.0, 0.8 );

	//private static java.awt.Color NULL_COLOR = java.awt.Color.RED.darker();
	//private static java.awt.Color NULL_DARKER_COLOR = NULL_COLOR.darker();

	private static java.awt.Color OUTLINE_COLOR = java.awt.Color.GRAY;
	private static TypeBorder singletonForUser = new TypeBorder( true );
	private static TypeBorder singletonForJava = new TypeBorder( false );
	private static TypeBorder singletonForNull = new TypeBorder( null );

	public static TypeBorder getSingletonFor( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			if( type instanceof org.lgna.project.ast.NamedUserType ) {
				return TypeBorder.singletonForUser;
			} else {
				return TypeBorder.singletonForJava;
			}
		} else {
			return TypeBorder.singletonForNull;
		}
	}

	public static TypeBorder getSingletonForUserType() {
		return singletonForUser;
	}

	public static TypeBorder getSingletonForJavaType() {
		return singletonForJava;
	}

	public static TypeBorder getSingletonForNull() {
		return singletonForNull;
	}

	private Boolean isDeclaredByUser;

	private TypeBorder( Boolean isDeclaredByUser ) {
		this.isDeclaredByUser = isDeclaredByUser;
	}

	private int yPrevious = -1;
	private int heightPrevious = -1;
	private java.awt.Paint paintPrevious = null;

	private java.awt.Paint getFillPaint( java.awt.Component c, int x, int y, int width, int height ) {
		if( c.isEnabled() ) {
			if( ( y == this.yPrevious ) && ( height == this.heightPrevious ) ) {
				//pass
			} else {
				this.yPrevious = y;
				this.heightPrevious = height;
				if( isDeclaredByUser != null ) {
					if( isDeclaredByUser ) {
						this.paintPrevious = new java.awt.GradientPaint( 0, y, FILL_COLOR, 0, y + height, FILL_BRIGHTER_COLOR );
					} else {
						this.paintPrevious = new java.awt.GradientPaint( 0, y, FILL_COLOR, 0, y + height, FILL_DARKER_COLOR );
					}
				} else {
					//this.paintPrevious = new java.awt.GradientPaint( 0, y, NULL_COLOR, 0, y + height, NULL_DARKER_COLOR );;
					//this.paintPrevious = java.awt.Color.GRAY;
					//this.paintPrevious = java.awt.Color.RED.darker();
					this.paintPrevious = java.awt.Color.RED;
				}
			}
			return this.paintPrevious;
		} else {
			return java.awt.Color.RED;
		}
	}

	@Override
	public java.awt.Insets getBorderInsets( java.awt.Component c ) {
		return TypeBorder.insets;
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	private static java.awt.Shape createShape( int x, int y, int width, int height ) {
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		int x0 = x + 0;
		int x1 = ( x0 + width ) - 1;
		int xA = x0 + ( X_INSET / 2 );
		int xB = x1 - ( X_INSET / 2 );

		int y0 = y + 0;
		int y1 = ( y0 + height ) - 1;
		int yC = ( y0 + y1 ) / 2;

		rv.moveTo( xA, y0 );
		rv.lineTo( xB, y0 );
		rv.lineTo( x1, yC );
		rv.lineTo( xB, y1 );
		rv.lineTo( xA, y1 );
		rv.lineTo( x0, yC );
		rv.lineTo( xA, y0 );
		return rv;
	}

	@Override
	public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
		java.awt.Shape shape = TypeBorder.createShape( x, y, width, height );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( getFillPaint( c, x, y, width, height ) );
		g2.fill( shape );
		g2.setPaint( OUTLINE_COLOR );
		g2.draw( shape );
	}
}
