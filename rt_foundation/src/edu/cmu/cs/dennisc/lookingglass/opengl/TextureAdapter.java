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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public abstract class TextureAdapter<E extends edu.cmu.cs.dennisc.texture.Texture> extends AbstractElementAdapter< E > {
	public static void handleTextureChanged( edu.cmu.cs.dennisc.texture.event.TextureEvent e ) {
		TextureAdapter textureAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		textureAdapter.handleTextureChanged();
	}
	public boolean isPotentiallyAlphaBlended() {
		return m_element.isPotentiallyAlphaBlended();
	}
	public boolean isValid() {
		if( m_element != null ) {
			return m_element.isValid();
		} else {
			return false;
		}
	}

	private java.util.List< RenderContext > m_renderContexts = new java.util.LinkedList< RenderContext >();
	private boolean m_isDirty = true;

	public void addRenderContext( RenderContext rc ) {
		m_renderContexts.add( rc );
	}
	public void removeRenderContext( RenderContext rc ) {
		m_renderContexts.remove( rc );
		setDirty( true );
	}

	@Override
	public void handleReleased() {
		super.handleReleased();
		if( m_renderContexts.size() > 0 ) {
			RenderContext[] renderContexts = new RenderContext[ m_renderContexts.size() ];
			m_renderContexts.toArray( renderContexts );
			for( RenderContext rc : renderContexts ) {
				rc.forgetTextureAdapter( this, true );
			}
		}
	}

	private void handleTextureChanged() {
		setDirty( true );
	}

	protected boolean isDirty() {
		return m_isDirty;
	}
	protected void setDirty( boolean isDirty ) {
		m_isDirty = isDirty;
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "marking texture dirty", this );
//		m_isDirty = true;
	}

	//todo: map u and v for non power of 2 textures?
	public float mapU( float u ) {
		return u;
	}
	public float mapV( float v ) {
		return v;
	}

	private com.sun.opengl.util.texture.Texture m_glTexture;

	protected abstract com.sun.opengl.util.texture.Texture newTexture( com.sun.opengl.util.texture.Texture currentTexture );
	public com.sun.opengl.util.texture.Texture getTexture( RenderContext rc ) {
		if( isDirty() ) {
			com.sun.opengl.util.texture.Texture glTexture = newTexture( m_glTexture );
			if( m_glTexture != glTexture ) {
				m_glTexture = glTexture;
				rc.put( this, m_glTexture );
			}
			m_isDirty = false;
		}
		return m_glTexture;
	}

	public abstract java.awt.Graphics2D createGraphics();
	public abstract void commitGraphics( java.awt.Graphics2D g, int x, int y, int width, int height );
	public abstract java.awt.Image getImage();

//	@Override
//	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
//		if( property == m_texture.isPotentiallyAlphaBlended ) {
//			setDirty( true );
//		} else if( property == m_texture.isMipMappingDesired ) {
//			setDirty( true );
//		} else {
//			super.propertyChanged( property );
//		}
//	}
}
