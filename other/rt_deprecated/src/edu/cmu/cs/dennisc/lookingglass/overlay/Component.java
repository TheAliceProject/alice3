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
package edu.cmu.cs.dennisc.lookingglass.overlay;

/**
 * @author Dennis Cosgrove
 */
public abstract class Component {
	private Overlay m_overlay = null;

	/*package-private*/Overlay getOverlay() {
		return m_overlay;
	}
	/*package-private*/void setOverlay( Overlay overlay ) {
		if( m_overlay != overlay ) {
			m_overlay = overlay;
			repaint();
		}
	}
	

	public void repaint() {
		if( m_overlay != null ) {
			m_overlay.repaint();
		}
	}

	private float m_x = Float.NaN;
	private float m_y = Float.NaN;

	public final float getX() {
		return m_x;
	}
	public final float getY() {
		return m_y;
	}
	public void setLocation( float x, float y ) {
		if( m_x != x || m_y != y ) {
			m_x = x;
			m_y = y;
			repaint();
		}
	}

	public abstract void computePreferredSize( java.awt.Graphics g, int width, int height );
	public abstract float getWidth();
	public abstract float getHeight();

	protected java.awt.Rectangle getBounds() {
		return new java.awt.Rectangle( (int)m_x, (int)m_y, (int)getWidth(), (int)getHeight() );
	}
	
	public boolean isPointContainedWithin( int x, int y ) {
		return getBounds().contains( x, y );
	}
	private boolean m_isVisible = true;

	public boolean isVisible() {
		return m_isVisible;
	}
	public void setVisible( boolean isVisible ) {
		if( m_isVisible != isVisible ) {
			m_isVisible = isVisible;
			repaint();
		}
	}

	private float m_opacity = 1.0f;

	public float getOpacity() {
		return m_opacity;
	}
	public void setOpacity( float opacity ) {
		if( m_opacity != opacity ) {
			m_opacity = opacity;
			repaint();
		}
	}

	protected boolean isHighlighted() {
		return false;
	}
	protected boolean isPressed() {
		return false;
	}
	protected boolean isSelected() {
		return false;
	}
	
	public abstract void paint( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e, java.awt.Graphics2D g );
}
