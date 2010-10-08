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

package edu.cmu.cs.dennisc.alan;

/**
 * @author Dennis Cosgrove
 */
public class TextLabel extends View {
	private String m_text;
	private boolean m_isMuted = false;
	private java.awt.Paint m_foreground = Look.TEXT_PAINT;

	public TextLabel() {
	}
	public TextLabel( String text ) {
		m_text = text;
	}
	public TextLabel( String text, boolean isMuted ) {
		this( text );
		m_isMuted = isMuted;
	}

	private java.awt.Font getFont() {
		if( m_isMuted ) {
			return Look.getMutedFont();
		} else {
			return Look.getBaseFont();
		}
	} 
	public java.awt.Paint getForeground() {
		return m_foreground;
	}
	public void setForeground( java.awt.Paint foreground ) {
		m_foreground = foreground;
	}
	
	public String getText() {
		return m_text;
	}
	public void setText( String text ) {
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_text, text ) ) {
			//pass
		} else {
			m_text = text;
			this.setLayoutRequired( true );
		}
	}

	@Override
	protected void calculateLocalBounds( java.awt.Graphics g ) {
		g.setFont( getFont() );
		m_localBounds.setRect( g.getFontMetrics().getStringBounds( m_text, g ) );
	}
	
	@Override
	protected void paintForeground( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		super.paintForeground( g2, x, y, width, height );
		g2.setFont( getFont() );
		g2.setPaint( m_foreground );
		g2.drawString( m_text, 0, 0 );
	}
}
