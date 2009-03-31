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
public class TypeExpressionPane extends swing.LineAxisPane  {
	private edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression;
	private static final int X_INSET = 10;
	public TypeExpressionPane( edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression ) {
		this.typeExpression = typeExpression;
		String text = this.typeExpression.value.getValue().getName();
//		if( isClassPrefixDesired() ) {
//			if( "java".equals( alice.ide.IDE.getSingleton().getLocale().getVariant() ) ) {
//				//pass
//			} else {
//				text = "class: " + text;
//			}
//		}

		this.setToolTipText( "class: " + text );
		this.add( new zoot.ZLabel( text ) );
		java.awt.Color color = org.alice.ide.IDE.getColorForASTInstance( this.typeExpression );
		this.setBackground( color );
		int yInset = 4;
		this.setBorder( javax.swing.BorderFactory.createMatteBorder( yInset, X_INSET, yInset, X_INSET, color ) );
	}
	protected boolean isClassPrefixDesired() {
		return false;
	}
	protected java.awt.Shape createShape() {
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		int x0 = 0;
		int x1 = getWidth()-1;
		int xA = x0 + X_INSET;
		int xB = x1 - X_INSET;
		
		int y0 = 0;
		int y1 = getHeight()-1;
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
	protected void paintBorder( java.awt.Graphics g ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( java.awt.Color.GRAY );
		g2.draw( this.createShape() );
	}
	@Override
	public void paint( java.awt.Graphics g ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( this.getBackground() );
		g2.fill( this.createShape() );
		super.paint( g );
	}
}
