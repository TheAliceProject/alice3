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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
class TypeBorder implements javax.swing.border.Border {
	private static final int X_INSET = 10;
	private static final int Y_INSET = 4;
	private static java.awt.Insets insets = new java.awt.Insets( Y_INSET, X_INSET, Y_INSET, X_INSET );
	private static java.awt.Color FILL_COLOR = org.alice.ide.IDE.getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.TypeExpression.class );
	private static java.awt.Color FILL_BRIGHTER_COLOR = FILL_COLOR.brighter();
	private static java.awt.Color FILL_DARKER_COLOR = FILL_COLOR.darker();

	private static java.awt.Color OUTLINE_COLOR = java.awt.Color.GRAY;
	private static TypeBorder singletonForDeclaredInAlice = new TypeBorder( true );
	private static TypeBorder singletonForDeclaredInJava = new TypeBorder( false );

	public static TypeBorder getSingletonFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			return TypeBorder.singletonForDeclaredInAlice;
		} else {
			return TypeBorder.singletonForDeclaredInJava;
		}
	}
	private boolean isDeclaredInAlice;
	private TypeBorder( boolean isDeclaredInAlice ) {
		this.isDeclaredInAlice = isDeclaredInAlice;
	}

	private int yPrevious = -1;
	private int heightPrevious = -1;
	private java.awt.GradientPaint paintPrevious = null;
	private java.awt.Paint getFillPaint( int x, int y, int width, int height ) {
		if( y==this.yPrevious && height==this.heightPrevious ) {
			//pass
		} else {
			this.yPrevious = y;
			this.heightPrevious = height;
			if( isDeclaredInAlice ) {
				this.paintPrevious = new java.awt.GradientPaint( 0, y, FILL_COLOR, 0, y + height, FILL_BRIGHTER_COLOR );
			} else {
				this.paintPrevious = new java.awt.GradientPaint( 0, y, FILL_COLOR, 0, y + height, FILL_DARKER_COLOR );
			}
		}
		return this.paintPrevious;
	}

	public java.awt.Insets getBorderInsets( java.awt.Component c ) {
		return TypeBorder.insets;
	}
	public boolean isBorderOpaque() {
		return false;
	}
	private static java.awt.Shape createShape( int x, int y, int width, int height ) {
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		int x0 = x;
		int x1 = x0 + width - 1;
		int xA = x0 + X_INSET;
		int xB = x1 - X_INSET;

		int y0 = y;
		int y1 = y0 + height - 1;
		int yC = (y0 + y1) / 2;

		rv.moveTo( xA, y0 );
		rv.lineTo( xB, y0 );
		rv.lineTo( x1, yC );
		rv.lineTo( xB, y1 );
		rv.lineTo( xA, y1 );
		rv.lineTo( x0, yC );
		rv.lineTo( xA, y0 );
		return rv;
	}
	public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
		java.awt.Shape shape = TypeBorder.createShape( x, y, width, height );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( getFillPaint( x, y, width, height ) );
		g2.fill( shape );
		g2.setPaint( OUTLINE_COLOR );
		g2.draw( shape );
	}
}
