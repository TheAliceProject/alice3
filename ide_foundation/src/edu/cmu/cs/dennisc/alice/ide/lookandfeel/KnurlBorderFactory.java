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
package edu.cmu.cs.dennisc.alice.ide.lookandfeel;

/**
 * @author Dennis Cosgrove
 */
class KnurlBorder implements javax.swing.border.Border {
	private static final int KNURL_WIDTH = 8;
	private static final int INSET = 4;
	private static final int TOP = INSET;
	private static final int LEFT = INSET + KNURL_WIDTH + 2;
	private static final int BOTTOM = INSET;
	private static final int RIGHT = INSET;
	private java.awt.Insets insets = new java.awt.Insets( TOP, LEFT, BOTTOM, RIGHT );
	public java.awt.Insets getBorderInsets( java.awt.Component c ) {
		return this.insets;
	}
	public boolean isBorderOpaque() {
		return true;
	}
	public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g.setColor( java.awt.Color.BLACK );
		edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl5( g2, x+2, y+2, KNURL_WIDTH, height-2 );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class KnurlBorderFactory implements StatementClassBorderFactory {
	private KnurlBorder sharedInstance = new KnurlBorder();
	public javax.swing.border.Border createBorder( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > statementCls, java.awt.Component component ) {
		assert statementCls != null;
		if( component instanceof edu.cmu.cs.dennisc.alice.ide.editors.code.EmptyStatementListAfforance ) {
			return javax.swing.BorderFactory.createEmptyBorder( 8, 16, 8, 48 );
		} else {
			return this.sharedInstance;
		}
	}
}
