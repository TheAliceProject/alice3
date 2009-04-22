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
package zoot;

public abstract class ZComponent extends swing.Pane {
	protected abstract int getInsetTop();
	protected abstract int getInsetLeft();
	protected abstract int getInsetBottom();
	protected abstract int getInsetRight();

	protected abstract void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height );
	protected abstract void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height );
	protected abstract void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height );

	public ZComponent() {
//		this.setBorder( null );
		this.setDoubleBuffered( false );
	}
	
	private void updateBorderIfNecessary() {
		if( this.getBorder() == null ) {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( this.getInsetTop(), this.getInsetLeft(), this.getInsetBottom(), this.getInsetRight() ) );
		}
	}
	
	protected java.awt.Paint getForegroundPaint( int x, int y, int width, int height ) {
		return this.getForeground();
	}
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		return this.getBackground();
	}
	
	@Override
	public void doLayout() {
		this.updateBorderIfNecessary();
		super.doLayout();
	}
	
	@Override
	public void addNotify() {
		this.updateBorderIfNecessary();
		super.addNotify();
	}

	@Override
	public void paint( java.awt.Graphics g ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//		java.awt.Color prev = g2.getColor();
		int x = 0;
		int y = 0;
		int width = getWidth();
		int height = getHeight();
		try {
			g2.setPaint( this.getBackgroundPaint( x, y, width, height ) );
			//g2.setColor( this.getBackground() );
			this.paintPrologue( g2, x, y, width, height );
		} finally {
//			g2.setPaint( prev );
		}
		super.paint( g );
//		prev = g2.getColor();
		g2.setPaint( this.getForegroundPaint( x, y, width, height ) );
		//g2.setColor( this.getForeground() );
		try {
			this.paintEpilogue( g2, x, y, width, height );
		} finally {
//			g2.setPaint( prev );
		}
	}
}