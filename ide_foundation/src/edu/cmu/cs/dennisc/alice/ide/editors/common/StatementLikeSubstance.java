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
public abstract class StatementLikeSubstance extends PotentiallyDraggablePane {
//	private static final int KNURL_WIDTH = 12;
	private static edu.cmu.cs.dennisc.alice.ide.ast.BorderFactory borderFactory = null;
	public static void setBorderFactory( edu.cmu.cs.dennisc.alice.ide.ast.BorderFactory borderFactory ) {
		StatementLikeSubstance.borderFactory = borderFactory;
	}
	public StatementLikeSubstance() {
		super( javax.swing.BoxLayout.LINE_AXIS );
		if( StatementLikeSubstance.borderFactory != null ) {
			this.setBorder( StatementLikeSubstance.borderFactory.createBorder( getStatementClass() ) );
		} else {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		}
//		this.setBorder( javax.swing.BorderFactory.createMatteBorder( getBorderTop(), getBorderLeft(), getBorderBottom(), getBorderRight(), edu.cmu.cs.dennisc.awt.ColorUtilities.GARISH_COLOR ) );
	}
	
	protected abstract Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getStatementClass();
//	protected int getBorderTop() {
//		return 4;
//	}
//	protected int getBorderLeft() {
//		int rv = 4;
//		if( isKnurlDesired() ) {
//			rv += KNURL_WIDTH;
//		}
//		return rv;
//	}
//	protected int getBorderBottom() {
//		return 4;
//	}
//	protected int getBorderRight() {
//		return 4;
//	}

	@Override
	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
		return new edu.cmu.cs.dennisc.awt.BeveledRoundRectangle( new java.awt.geom.RoundRectangle2D.Float( 1.5f, 1.5f, (float)getWidth()-3, (float)getHeight()-3, 8.0f, 8.0f ) );
	}
	//todo: remove
	@Deprecated
	protected boolean isKnurlDesired() {
		return true;
	}

//	//todo: remove
	@Override
	protected void paintBorder( java.awt.Graphics g ) {
		super.paintBorder( g );
		if( this.isKnurlDesired() ) {
			this.getBorder().paintBorder( this, g, 0, 0, getWidth(), getHeight() );
		}
//		super.paintBorder( g );
//		if( this.isKnurlDesired() ) {
//			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//			edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl5( g2, 3, 2, 8, getHeight()-2 );
//		}
	}
}
