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
package org.alice.apis.moveandturn.graphic;

/**
 * @author Dennis Cosgrove
 */
public abstract class Text implements Graphic {
	private String m_text;
	private java.awt.Font m_font;
	private java.awt.Color m_textColor;
	private java.awt.geom.Rectangle2D m_textBounds = null;
	private boolean m_isForgetFontRequired = false;
	
	public Text( String text, java.awt.Font font, java.awt.Color textColor ) {
		m_text = text;
		m_font = font;
		m_textColor = textColor;
	}

//	protected java.awt.Font getFont() {
//		return m_font;
//	}
//	protected String getText() {
//		return m_text;
//	}
//	protected java.awt.Color getTextColor() {
//		return m_textColor;
//	}
	public java.awt.geom.Rectangle2D accessTextBounds() {
		return m_textBounds;
	}
	
	protected abstract void paint( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, String text, java.awt.Font font, java.awt.Color textColor, java.awt.geom.Rectangle2D textBounds );

	public void forgetIfNecessary( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2 ) {
		if( m_isForgetFontRequired ) {
			g2.forget( m_font );
		}
	}
	public final void paint( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		if( m_textBounds != null ) {
			//pass
		} else {
			g2.remember( m_font );
			m_isForgetFontRequired = true;
			m_textBounds = g2.getBounds( m_text, m_font );
		}
		assert m_textBounds != null;
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		paint( g2, lookingGlass, sgCamera, m_text, m_font, m_textColor, m_textBounds );
	}
}
