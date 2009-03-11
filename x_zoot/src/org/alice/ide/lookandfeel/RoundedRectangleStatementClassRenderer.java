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
package org.alice.ide.lookandfeel;


/**
 * @author Dennis Cosgrove
 */
public class RoundedRectangleStatementClassRenderer implements StatementClassRenderer {
	private static final java.awt.Color TOP_SELECTED_COLOR = new java.awt.Color( 63, 63, 255, 127 );
	private static final java.awt.Color BOTTOM_SELECTED_COLOR = new java.awt.Color( 0, 0, 0, 127 );
	public void fillBounds( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRoundRect( x, y, width-1, height-1, 8, 8 );
	}
	public void paintPrologue( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height, boolean isActive, boolean isPressed, boolean isSelected ) {
		g2.setPaint( c.getBackground() );
		fillBounds( context, c, g2, x, y, width, height );
	}

	private edu.cmu.cs.dennisc.awt.BevelState getBevelState(boolean isActive, boolean isPressed, boolean isSelected) {
		if (isActive) {
			if (isPressed) {
				return edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
			} else {
				return edu.cmu.cs.dennisc.awt.BevelState.RAISED;
			}
		} else {
			return edu.cmu.cs.dennisc.awt.BevelState.FLUSH;
		}
	}

	public void paintEpilogue( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height, boolean isActive, boolean isPressed, boolean isSelected ) {
		if( isSelected ) {
			java.awt.Paint paint = new java.awt.GradientPaint( 0, 0, TOP_SELECTED_COLOR, 0, (float)height, BOTTOM_SELECTED_COLOR );
			g2.setPaint( paint );
			fillBounds( context, c, g2, x, y, width, height );
		}
		edu.cmu.cs.dennisc.awt.BevelState bevelState;
		if( c instanceof org.alice.ide.ast.EmptyStatementListAfforance ) {
			bevelState = edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
		} else {
			bevelState = this.getBevelState(isActive, isPressed, isSelected);
		}
		if( bevelState != edu.cmu.cs.dennisc.awt.BevelState.FLUSH ) {
			edu.cmu.cs.dennisc.awt.BeveledRoundRectangle beveledRoundRectangle = new edu.cmu.cs.dennisc.awt.BeveledRoundRectangle( x+1, y+1, width-2, height-2, 8.0f, 8.0f );
			beveledRoundRectangle.draw( g2, bevelState, 3.0f, 1.0f, 1.0f );
		} else {
			g2.setColor( java.awt.Color.GRAY );
			g2.drawRoundRect( x, y, width-1, height-1, 8, 8 );
		}
	}
}
