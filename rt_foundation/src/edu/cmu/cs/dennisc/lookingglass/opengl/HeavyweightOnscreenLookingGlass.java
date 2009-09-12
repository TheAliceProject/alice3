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
class HeavyweightOnscreenLookingGlass extends OnscreenLookingGlass implements edu.cmu.cs.dennisc.lookingglass.HeavyweightOnscreenLookingGlass {
	private javax.media.opengl.GLCanvas m_glCanvas;

	/*package-private*/ HeavyweightOnscreenLookingGlass( LookingGlassFactory lookingGlassFactory ) {
		super( lookingGlassFactory );
		javax.media.opengl.GLCapabilities glCapabilities = this.createCapabilities();
		m_glCanvas = new javax.media.opengl.GLCanvas( glCapabilities, HeavyweightOnscreenLookingGlass.getGLCapabilitiesChooser(), null, null );
		//m_glCanvas.getChosenGLCapabilities().getDepthBits();
		//m_glCanvas.setAutoSwapBufferMode( false );
		m_glCanvas.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				m_glCanvas.setMinimumSize( new java.awt.Dimension( 0, 0 ) );
				m_glCanvas.repaint();
			}
		} );
	}

	public void repaint() {
		getAWTComponent().repaint();
	}
	public java.awt.Component getAWTComponent() {
		return m_glCanvas;
	}
	public java.awt.Dimension getSize( java.awt.Dimension rv ) {
		return getAWTComponent().getSize( rv );
	}

	@Override
	protected javax.media.opengl.GLAutoDrawable getGLAutoDrawable() {
		return m_glCanvas;
	}
}
