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
package org.alice.ide.ast;

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
	public StatementLikeSubstance( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > statementCls ) {
		this.statementCls = statementCls;
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
		this.setBackground( getIDE().getColorForASTClass( this.statementCls ) );
	}
	
	public Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getStatementCls() {
		return this.statementCls;
	}
	private static final int INSET = 4;
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
		return StatementLikeSubstance.INSET;
	}
	@Override
	protected int getInsetRight() {
		return StatementLikeSubstance.INSET;
	}
	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRoundRect( x, y, width - 1, height - 1, 8, 8 );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		edu.cmu.cs.dennisc.awt.BeveledRoundRectangle beveledRoundRectangle = new edu.cmu.cs.dennisc.awt.BeveledRoundRectangle( x+1, y+1, width-3, height-3, 8, 8 );
		beveledRoundRectangle.paint( g2, this.getBevelState(), 3.0f, 1.0f, 1.0f );
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
