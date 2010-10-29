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
@Deprecated
public class DefaultButtonRenderer implements Renderer {
	public DefaultButtonRenderer() {
	}
	public DefaultButtonRenderer( String text ) {
		setText( text );
	}
	//todo
	private java.awt.Paint getNormalPaint() {
		return new java.awt.GradientPaint( 0, 0, new java.awt.Color( 201, 213, 230 ), 0, getHeight(), new java.awt.Color( 91, 104, 121 ) ); 
	}
	private java.awt.Paint getHighlightedPaint() {
		return new java.awt.GradientPaint( 0, 0, new java.awt.Color( 198, 212, 212 ), 0, getHeight(), new java.awt.Color( 100, 125, 125 ) ); 
	}
	private java.awt.Paint getPressedPaint() {
		return new java.awt.Color( 113, 135, 156 ); 
	}

	public static final java.awt.Font DEFAULT_FONT = new java.awt.Font( null, java.awt.Font.PLAIN, 24 );
	private java.awt.Font m_font = DefaultButtonRenderer.DEFAULT_FONT;
	public java.awt.Font getFont() {
		return m_font;
	}
	public void setFont( java.awt.Font font ) {
		if( m_font != font ) {
			m_font = font;
			//setTextureDirty( true );
		}
	}

	private String m_text;
	public String getText() {
		return m_text;
	}
	public void setText( String text ) {
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areNotEquivalent( m_text, text ) ) {
			m_text = text;
			//setTextureDirty( true );
		}
	}

	private java.awt.geom.Rectangle2D m_bounds = null;
	private static final int PAD = 12;
	
	public float getWidth() {
		assert m_bounds != null;
		return (float)( m_bounds.getWidth() + PAD + PAD );
	}
	public float getHeight() {
		assert m_bounds != null;
		return (float)( m_bounds.getHeight() + PAD + PAD );
	}
	
	public void layoutIfNecessary( java.awt.Graphics g, boolean isHighlighted, boolean isPressed, boolean isSelected ) {
		java.awt.FontMetrics fm = g.getFontMetrics( m_font );
		m_bounds = fm.getStringBounds( m_text, g );
	}
	public void paint( java.awt.Graphics2D g2, boolean isHighlighted, boolean isPressed, boolean isSelected ) {
		int width = (int)(getWidth()+0.999f);
		int height = (int)(getHeight()+0.999f);
		final int DROP_SHADOW_OFFSET = 4;
		int arcSize = Math.min( width, height );

		java.awt.Composite composite = g2.getComposite();
		g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC, 0.75f ) );

		g2.setPaint( java.awt.Color.DARK_GRAY );
		g2.fillRoundRect( DROP_SHADOW_OFFSET, DROP_SHADOW_OFFSET, width-DROP_SHADOW_OFFSET, height-DROP_SHADOW_OFFSET, arcSize, arcSize );

		g2.setComposite( composite );

		java.awt.Paint paint;
		if( isPressed ) {
			paint = getPressedPaint();
		} else {
			if( isHighlighted ) {
				paint = getHighlightedPaint();
			} else {
				paint = getNormalPaint();
			}
		}
		g2.setPaint( paint );
		g2.fillRoundRect( 0, 0, width-DROP_SHADOW_OFFSET, height-DROP_SHADOW_OFFSET, arcSize, arcSize );

		g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		g2.setFont( getFont() );
		String text =  getText();
		if( text != null ) {
			g2.setColor( java.awt.Color.BLACK );
			g2.drawString( text, (int)( -m_bounds.getX() ) + PAD, (int)-m_bounds.getY() + PAD );
		}
	}
}
