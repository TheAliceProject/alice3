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
package edu.cmu.cs.dennisc.alice.ide.editors.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class StatementLikeSubstance extends PotentiallyDraggablePane< Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement> > {
	private static edu.cmu.cs.dennisc.alice.ide.lookandfeel.StatementClassBorderFactory borderFactory = null;
	private static edu.cmu.cs.dennisc.alice.ide.lookandfeel.StatementClassRenderer renderer = null;

	private Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > statementCls;
	
	protected static Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getClassFor( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		if( statement != null ) {
			return statement.getClass();
		} else {
			return edu.cmu.cs.dennisc.alice.ast.Statement.class;
		}
	}
	
	public static void setBorderFactory( edu.cmu.cs.dennisc.alice.ide.lookandfeel.StatementClassBorderFactory borderFactory ) {
		StatementLikeSubstance.borderFactory = borderFactory;
	}
	public static void setRenderer( edu.cmu.cs.dennisc.alice.ide.lookandfeel.StatementClassRenderer renderer ) {
		StatementLikeSubstance.renderer = renderer;
	}
	
	public StatementLikeSubstance( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > statementCls ) {
		super( javax.swing.BoxLayout.LINE_AXIS );
		this.statementCls = statementCls;
		if( StatementLikeSubstance.borderFactory != null ) {
			this.setBorder( StatementLikeSubstance.borderFactory.createBorder( this.statementCls, this ) );
		} else {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		}
	}
	
	@Override
	protected edu.cmu.cs.dennisc.alice.ide.lookandfeel.StatementClassRenderer getRenderer() {
		return StatementLikeSubstance.renderer;
	}
	
	
//	@Override
//	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
//		return new edu.cmu.cs.dennisc.awt.BeveledRoundRectangle( new java.awt.geom.RoundRectangle2D.Float( 1.5f, 1.5f, (float)getWidth()-3, (float)getHeight()-3, 8.0f, 8.0f ) );
//	}

	//todo: remove
	@Deprecated
	protected boolean isKnurlDesired() {
		return true;
	}

	
	@Override
	protected Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getContext() {
		return this.statementCls;
	}
	
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
