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
public abstract class Bubble extends Text {
	private Originator m_originator;
	private edu.cmu.cs.dennisc.scenegraph.AbstractCamera m_sgCamera;
	private edu.cmu.cs.dennisc.lookingglass.LookingGlass m_lookingGlass;
	private java.awt.Color m_fillColor;
	private java.awt.Color m_outlineColor;
	private java.awt.geom.Point2D m_originOfTail;
	private java.awt.geom.Point2D m_bodyConnectionLocationOfTail;
	private java.awt.geom.Point2D m_textBoundsOffset;
	
	public enum State {
		OPENNING,
		UPDATING,
		CLOSING,
	}
	private State m_state = null;
	private double m_portion = Double.NaN;

	
	public Bubble( String text, java.awt.Font font, java.awt.Color textColor, java.awt.Color fillColor, java.awt.Color outlineColor, Originator originator ) {
		super( text, font, textColor );
		m_originator = originator;
		m_fillColor = fillColor;
		m_outlineColor = outlineColor;
	}

	public java.awt.geom.Point2D accessOriginOfTail() {
		return m_originOfTail;
	}
	
	private Object o = new Object();

	protected abstract void paint( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.lang.String text, java.awt.Font font, java.awt.Color textColor, java.awt.geom.Rectangle2D textBounds, State state, double portion, java.awt.geom.Point2D originOfTail, java.awt.geom.Point2D bodyConnectionLocationOfTail, java.awt.geom.Point2D textBoundsOffset, java.awt.Color fillColor, java.awt.Color outlineColor  );
	
	@Override
	protected final void paint( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.lang.String text, java.awt.Font font, java.awt.Color textColor, java.awt.geom.Rectangle2D textBounds ) {
		m_sgCamera = sgCamera;
		m_lookingGlass = lookingGlass;
		if( m_state != null ) {
			synchronized( o ) {
				if( m_originOfTail != null ) {
					//pass
				} else {
					m_originOfTail = m_originator.calculateOriginOfTail( this, m_sgCamera, m_lookingGlass );
				}
				assert m_originOfTail != null;
	
				if( m_bodyConnectionLocationOfTail != null ) {
					//pass
				} else {
					m_bodyConnectionLocationOfTail = m_originator.calculateBodyConnectionLocationOfTail( this, m_sgCamera, m_lookingGlass );
				}
				assert m_bodyConnectionLocationOfTail != null;
	
				if( m_textBoundsOffset != null ) {
					//pass
				} else {
					m_textBoundsOffset = m_originator.calculateTextBoundsOffset( this, m_sgCamera, m_lookingGlass );
				}
				assert m_textBoundsOffset != null;
				
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
				paint( g2, lookingGlass, sgCamera, text, font, textColor, textBounds, m_state, m_portion, m_originOfTail, m_bodyConnectionLocationOfTail, m_textBoundsOffset, m_fillColor, m_outlineColor );
			}
		}
	}

	//todo: update origin based on absolute transformation listeners 
	protected void updateOrigin() {
		synchronized( o ) {
			m_originOfTail = null;
		}
	}

	public void set( State state, double portion ) {
		m_state = state;
		m_portion = portion;
		
		//
		//todo: remove
		//
		updateOrigin();
	}
}
