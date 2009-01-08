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
class OffscreenLookingGlass extends AbstractLookingGlass implements edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass {
	private javax.media.opengl.GLPbuffer m_glPbuffer;
	private AbstractLookingGlass m_lookingGlassToShareContextWith;
	private java.awt.Dimension m_size = new java.awt.Dimension( -1, -1 );

	/*package-private*/ OffscreenLookingGlass( LookingGlassFactory lookingGlassFactory, AbstractLookingGlass lookingGlassToShareContextWith ) {
		super( lookingGlassFactory );
		m_lookingGlassToShareContextWith = lookingGlassToShareContextWith;
	}
	
	public java.awt.Dimension getSize( java.awt.Dimension rv ) {
		rv.setSize( m_size );
		return rv;
	}
	public void setSize( int width, int height ) {
		if( width != m_size.width || height != m_size.height ) {
			if( m_glPbuffer != null ) {
				m_glPbuffer.destroy();
			}
			m_size.setSize( width, height );
		}
	}
	
	public void clearAndRenderOffscreen() {
		getGLAutoDrawable().display();
	}
	@Override
	protected void actuallyRelease() {
		super.actuallyRelease();
		if( m_glPbuffer != null ) {
			m_glPbuffer.destroy();
		}
	}
	
	@Override
	protected javax.media.opengl.GLAutoDrawable getGLAutoDrawable() {
		if( m_glPbuffer == null ) {
			javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
			if( glDrawableFactory.canCreateGLPbuffer() ) {
				javax.media.opengl.GLCapabilities glCapabilities = new javax.media.opengl.GLCapabilities();
				assert m_size.width > 0;
				assert m_size.height > 0;
				m_glPbuffer = glDrawableFactory.createGLPbuffer( glCapabilities, null, m_size.width, m_size.height, m_lookingGlassToShareContextWith.getGLAutoDrawable().getContext() );
			} else {
				throw new RuntimeException( "cannot create pbuffer" );
			}
		}
		return m_glPbuffer;
	}
}
