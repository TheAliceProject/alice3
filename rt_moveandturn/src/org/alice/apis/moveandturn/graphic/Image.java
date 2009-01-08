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
public abstract class Image implements Graphic {
	private java.awt.Image m_image;
	private java.awt.Point m_buffer = new java.awt.Point();
	public Image( java.awt.Image image ) {
		setImage( image );
	}
	public java.awt.Image getImage() {
		return m_image;
	}
	public void setImage( java.awt.Image image ) {
		m_image = image;
	}

	protected abstract java.awt.Point getOffset( java.awt.Point rv, edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public void forgetIfNecessary( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2 ) {
		if( g2.isRemembered( m_image ) ) {
			g2.forget( m_image );
		}
	}
	public final void paint( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		if( g2.isRemembered( m_image ) ) {
			//pass
		} else {
			g2.remember( m_image );
		}
		synchronized( m_buffer ) {
			getOffset( m_buffer, g2, lookingGlass, sgCamera );
			g2.drawImage( m_image, m_buffer.x, m_buffer.y, null );
		}
	}
}
