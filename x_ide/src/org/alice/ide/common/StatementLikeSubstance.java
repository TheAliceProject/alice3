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
public abstract class StatementLikeSubstance extends NodeLikeSubstance {
	private Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > statementCls;
	
	protected static Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getClassFor( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		if( statement != null ) {
			return statement.getClass();
		} else {
			return edu.cmu.cs.dennisc.alice.ast.Statement.class;
		}
	}
	public StatementLikeSubstance( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > statementCls, int axis ) {
		this.statementCls = statementCls;
		this.setLayout( new javax.swing.BoxLayout( this, axis ) );
	}
	
	public Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getStatementCls() {
		return this.statementCls;
	}
	private static final int INSET = 2;
	@Override
	protected int getInsetTop() {
		return StatementLikeSubstance.INSET;
	}
	
	@Override
	protected int getDockInsetLeft() {
		return 2;
	}
	@Override
	protected int getInternalInsetLeft() {
		return StatementLikeSubstance.INSET;
	}
	
	
	@Override
	protected int getInsetBottom() {
		return StatementLikeSubstance.INSET + 2;
	}
	@Override
	protected int getInsetRight() {
		return StatementLikeSubstance.INSET + 4;
	}
	@Override
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		return getIDE().getPaintFor( this.statementCls, x, y, width, height );
	}

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRoundRect( x, y, width - 1, height - 1, 8, 8 );
	}
	
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		java.awt.geom.RoundRectangle2D rr = new java.awt.geom.RoundRectangle2D.Float( x+1, y+1, width-3, height-3, 8, 8 );
		g2.fill( rr );
	}
	
	protected void paintOutline( java.awt.Graphics2D g2, java.awt.geom.RoundRectangle2D.Float rr ) {
		java.awt.Stroke prevStroke = g2.getStroke();
		if( this.isActive() ) {
			g2.setPaint( java.awt.Color.BLUE );
			g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
		} else {
			g2.setPaint( java.awt.Color.GRAY );
		}
		g2.draw( rr );
		g2.setStroke( prevStroke );
	}
	@Override
	protected final void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		super.paintEpilogue( g2, x, y, width, height );
		this.paintOutline( g2, new java.awt.geom.RoundRectangle2D.Float( x+1, y+1, width-3, height-3, 8, 8 ) );
	}
	
//	@Override
//	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
//		return new edu.cmu.cs.dennisc.awt.BeveledRoundRectangle( new java.awt.geom.RoundRectangle2D.Float( 1.5f, 1.5f, (float)getWidth()-3, (float)getHeight()-3, 8.0f, 8.0f ) );
//	}

////	//todo: remove
//	@Override
//	protected void paintBorder( java.awt.Graphics g ) {
//		super.paintBorder( g );
//		if( this.isKnurlDesired() ) {
//			this.getBorder().paintBorder( this, g, 0, 0, getWidth(), getHeight() );
//		}
////		super.paintBorder( g );
////		if( this.isKnurlDesired() ) {
////			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
////			edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl5( g2, 3, 2, 8, getHeight()-2 );
////		}
//	}
}
