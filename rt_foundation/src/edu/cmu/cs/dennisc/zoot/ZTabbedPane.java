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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */

class ZTabbedPaneUI extends javax.swing.plaf.basic.BasicTabbedPaneUI {
	private static final int NORTH_AREA_PAD = 4;
	private static final int EAST_TAB_PAD = 16;
	
	private java.awt.Font selectedFont;
	private java.awt.FontMetrics selectedFontMetrics;
	@Override
	protected void installDefaults() {
		super.installDefaults();
		java.awt.Font normalFont = this.tabPane.getFont();
		this.selectedFont = normalFont.deriveFont( normalFont.getSize() * 1.25f );
		this.selectedFontMetrics = this.tabPane.getFontMetrics( this.selectedFont );
		this.tabAreaInsets.set( 0, 0, 0, 0 );
		this.tabInsets.set( 2, 2, 2, 2 );
		this.selectedTabPadInsets.set( 0, 0, 0, 0 );
	}
	
	private java.awt.geom.GeneralPath createPath( int width, int height ) {
		float x0 = width - EAST_TAB_PAD;
		float x1 = width + EAST_TAB_PAD;
		//x0 += EAST_TAB_PAD;
		float cx0 = x0 + 15;
		float cx1 = x1 - 20;

		float y0 = NORTH_AREA_PAD;
		float y1 = height;
		float cy0 = y0;
		float cy1 = y1;

		float xA = height * 0.3f;
		float yA = xA;
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();

		path.moveTo( x1, y1 );
		path.curveTo( cx1, cy1, cx0, cy0, x0, y0 );
		path.lineTo( xA, y0 );
		path.quadTo( 0, y0, 0, yA );
		path.lineTo( 0, y1 );
		return path;
	}
	@Override
	protected void paintTabBorder( java.awt.Graphics g, int tabPlacement, int tabIndex, int x, int y, int width, int height, boolean isSelected ) {
		if( isSelected ) {
			g.setColor( java.awt.Color.RED );
		} else {
			g.setColor( java.awt.Color.LIGHT_GRAY );
		}

		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		java.awt.geom.GeneralPath path = createPath( width, height );
		g2.translate( x, y );
		g2.draw( path );
		g2.translate( -x, -y );
		//edu.cmu.cs.dennisc.awt.ShapeUtilties.paint( g2, path, edu.cmu.cs.dennisc.awt.BevelState.RAISED );
//		g.drawRect( x, y+4, width, height-TOP_AREA_PAD );
	}
	@Override
	protected void paintTabBackground( java.awt.Graphics g, int tabPlacement, int tabIndex, int x, int y, int width, int height, boolean isSelected ) {
		if( isSelected ) {
			g.setColor( java.awt.Color.RED.darker() );
		} else {
			g.setColor( java.awt.Color.GRAY );
		}
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		java.awt.geom.GeneralPath path = createPath( width, height );
		g2.translate( x, y );
		g2.fill( path );
		g2.translate( -x, -y );
	}
	@Override
	protected void paintTabArea( java.awt.Graphics g, int tabPlacement, int selectedIndex ) {
		g.setColor( java.awt.Color.DARK_GRAY );
		java.awt.Rectangle bounds = g.getClipBounds();
		g.fillRect( bounds.x, bounds.y, bounds.width, bounds.height );
		super.paintTabArea( g, tabPlacement, selectedIndex );
	}
	@Override
	protected void paintFocusIndicator( java.awt.Graphics g, int tabPlacement, java.awt.Rectangle[] rects, int tabIndex, java.awt.Rectangle iconRect, java.awt.Rectangle textRect, boolean isSelected ) {
	}
	
	@Override
	protected int calculateTabHeight( int tabPlacement, int tabIndex, int fontHeight ) {
		return fontHeight + NORTH_AREA_PAD + 6;
	}
	@Override
	protected int calculateTabWidth( int tabPlacement, int tabIndex, java.awt.FontMetrics metrics ) {
		int rv = super.calculateTabWidth( tabPlacement, tabIndex, metrics );
		rv += EAST_TAB_PAD;
		return rv;
	}
	@Override
	protected void paintText( java.awt.Graphics g, int tabPlacement, java.awt.Font font, java.awt.FontMetrics metrics, int tabIndex, String title, java.awt.Rectangle textRect, boolean isSelected ) {
		if( isSelected ) {
			g.setFont( this.selectedFont );
		} else {
			g.setFont( font );
		}
		g.setColor( java.awt.Color.BLACK );
		int x = textRect.x;
		x -= EAST_TAB_PAD/2;
		g.drawString( title, x, textRect.y + textRect.height );
		//super.paintText( g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected );
	}
}

public class ZTabbedPane extends javax.swing.JTabbedPane {
	public ZTabbedPane() {
		this.setTabLayoutPolicy( javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT );
		this.setUI( new ZTabbedPaneUI() );
	}

	public static void main( String[] args ) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		ZTabbedPane tabbedPane = new ZTabbedPane();
		String[] tabTitles = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		for( String tabTitle : tabTitles ) {
			tabbedPane.add( tabTitle, new ZPane() );
		}
		frame.getContentPane().add( tabbedPane );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
