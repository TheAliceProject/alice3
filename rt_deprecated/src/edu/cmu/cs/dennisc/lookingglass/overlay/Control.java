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
public abstract class Control extends Widget {
	private boolean m_isEnabled = true;
	
	@Override
	void setOverlay( Overlay overlay ) {
		super.setOverlay( overlay );
		m_isHighlighted = false;
		m_isPressed = false;
	}
	
	public boolean isEnabled() {
		return m_isEnabled;
	}
	public void setEnabled( boolean isEnabled ) {
		if( m_isEnabled != isEnabled ) {
			m_isEnabled = isEnabled;
			setTextureDirty( true );
		}
	}

	private boolean m_isHighlighted = false;
	@Override
	public boolean isHighlighted() {
		return m_isHighlighted;
	}
	public void setHighlighted( boolean isHighlighted ) {
		if( m_isHighlighted != isHighlighted ) {
			m_isHighlighted = isHighlighted;
			setTextureDirty( true );
		}
	}

	private boolean m_isPressed = false;
	@Override
	public boolean isPressed() {
		return m_isPressed;
	}
	public void setPressed( boolean isPressed ) {
		if( m_isPressed != isPressed ) {
			m_isPressed = isPressed;
			setTextureDirty( true );
		}
	}
	
	private java.util.List< java.awt.event.ActionListener > m_popupListeners = new java.util.LinkedList< java.awt.event.ActionListener >();
	public void addPopupListener( java.awt.event.ActionListener l ) {
		synchronized( m_popupListeners ) {
			m_popupListeners.add( l );
		}
	}
	public void removePopupListener( java.awt.event.ActionListener l ) {
		synchronized( m_popupListeners ) {
			m_popupListeners.remove( l );
		}
	}
	public Iterable< java.awt.event.ActionListener > accessPopupListeners() {
		synchronized( m_popupListeners ) {
			return m_popupListeners;
		}
	}
	public void firePopupListeners( java.awt.event.MouseEvent mouseEvent ) {
		//todo
		java.awt.event.ActionEvent e = new edu.cmu.cs.dennisc.alan.event.ActionEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "popup", mouseEvent );
		synchronized( m_popupListeners ) {
			for( java.awt.event.ActionListener l : m_popupListeners ) {
				l.actionPerformed( e );
			}
		}
	}
	
	
//	protected abstract void paintTexture( java.awt.Graphics2D g, int width, int height, boolean isEnabled, boolean isHighlighted, boolean isPressed );
//	@Override
//	protected void paintTexture( java.awt.Graphics2D g, int width, int height ) {
//		clearTexture( g, width, height );
//		paintTexture( g, width, height, m_isEnabled, m_isHighlighted, m_isPressed );
//	}
}
