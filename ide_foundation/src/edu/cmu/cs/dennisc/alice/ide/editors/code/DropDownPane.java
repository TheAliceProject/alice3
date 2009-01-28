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
package edu.cmu.cs.dennisc.alice.ide.editors.code;

/**
 * @author Dennis Cosgrove
 */
public abstract class DropDownPane extends edu.cmu.cs.dennisc.alice.ide.editors.common.Control< Object > {
//	private static final int AFFORDANCE_SIZE = 9; 
//	private static final int INSET = 2; 
//	private static final int LEFT = INSET; 
//	private static final int TOP = INSET; 
//	private static final int RIGHT = INSET + AFFORDANCE_SIZE; 
//	private static final int BOTTOM = INSET;
	private static edu.cmu.cs.dennisc.alice.ide.lookandfeel.DropDownBorderFactory borderFactory = null;
	private static edu.cmu.cs.dennisc.alice.ide.lookandfeel.DropDownRenderer renderer = null;
	public static void setBorderFactory( edu.cmu.cs.dennisc.alice.ide.lookandfeel.DropDownBorderFactory borderFactory ) {
		DropDownPane.borderFactory = borderFactory;
	}
	public static void setRenderer( edu.cmu.cs.dennisc.alice.ide.lookandfeel.DropDownRenderer renderer ) {
		DropDownPane.renderer = renderer;
	}
	public DropDownPane( javax.swing.JComponent prefixPane, javax.swing.JComponent mainComponent, javax.swing.JComponent postfixPane ) {
		super( javax.swing.BoxLayout.LINE_AXIS );
		if( DropDownPane.borderFactory != null ) {
			this.setBorder( DropDownPane.borderFactory.createBorder( null ) );
		} else {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		}
		//this.setBorder( javax.swing.BorderFactory.createMatteBorder( TOP, LEFT, BOTTOM, RIGHT, edu.cmu.cs.dennisc.awt.ColorUtilities.GARISH_COLOR ) );
		//this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 230 ) );
		//this.setOpaque( true );
		if( prefixPane != null ) {
			this.add( prefixPane );
		}
		this.mainComponent = mainComponent;
		this.add( this.mainComponent );
		if( postfixPane != null ) {
			this.add( postfixPane );
		}
	}
	
	@Override
	protected edu.cmu.cs.dennisc.zoot.Renderer<Object> getRenderer() {
		return DropDownPane.renderer;
	}
	@Override
	protected Object getContext() {
		return null;
	}

	private javax.swing.JComponent mainComponent;
	
	@Override
	protected boolean isActuallyPotentiallySelectable() {
		return false;
	}
//	@Override
//	public boolean isSelected() {
//		return false;
//	}

	public javax.swing.JComponent getMainComponent() {
		return this.mainComponent;
	}
//	@Override
//	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
//		return new edu.cmu.cs.dennisc.awt.BeveledRectangle( new java.awt.geom.Rectangle2D.Float( 0, 0, getWidth()-1.0f, getHeight()-1.0f ) );
//	}
//	@Override
//	protected void paintBorder( java.awt.Graphics g ) {
//		super.paintBorder( g );
//		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//		java.awt.Color prev = g.getColor();
//		try {
//			final int INSET = 4;
//			int size = AFFORDANCE_SIZE;
//
//			int x0 = getWidth() - INSET/2 - AFFORDANCE_SIZE;
//			int x1 = x0 + size;
//			int xC = (x0 + x1) / 2;
//
//			
//			int y0 = INSET + 2;
//			int y1 = y0 + size;
//
//			java.awt.Color triangleFill;
//			java.awt.Color triangleOutline;
//			if( this.isActive() ) {
//				triangleFill = java.awt.Color.YELLOW;
//				triangleOutline = java.awt.Color.BLACK;
//			} else {
//				triangleFill = edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 192 );
//				triangleOutline = null;
//			}
//
//			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//
//			int[] xs = { x0, xC, x1 };
//			int[] ys = { y0, y1, y0 };
//			g.setColor( triangleFill );
//			g.fillPolygon( xs, ys, 3 );
//			if( triangleOutline != null ) {
//				g.setColor( triangleOutline );
//				g.drawPolygon( xs, ys, 3 );
//			}
//		} finally {
//			g.setColor( prev );
//		}
//	}
	
//	@Override
//	protected void paintActiveBorder( java.awt.Graphics2D g2 ) {
//		//super.paintActiveBorder( g2 );
//		g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
//		g2.setColor( java.awt.Color.BLUE );
//		g2.draw( new java.awt.geom.Rectangle2D.Float( 1.5f, 1.5f, getWidth()-3.0f, getHeight()-3.0f ) );
//	}
}
