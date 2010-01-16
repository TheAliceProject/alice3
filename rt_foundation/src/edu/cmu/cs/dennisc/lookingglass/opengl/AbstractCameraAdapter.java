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

import javax.media.opengl.GL;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCameraAdapter< E extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > extends LeafAdapter< E > {
	private BackgroundAdapter m_backgroundAdapter = null;

	private java.awt.Rectangle m_specifiedViewport = null;
	private boolean m_isLetterboxedAsOpposedToDistorted = true;

	public abstract edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, java.awt.Rectangle actualViewport );

	protected abstract java.awt.Rectangle performLetterboxing( java.awt.Rectangle rv );
	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, int width, int height ) {
		if( m_specifiedViewport != null ) {
			rv.setBounds( m_specifiedViewport );
		} else {
			rv.setBounds( 0, 0, width, height );
		}
		if( m_isLetterboxedAsOpposedToDistorted ) {
			performLetterboxing( rv );
		}
		return rv;
	}

	public abstract edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, java.awt.Rectangle actualViewport );

	public java.awt.Rectangle getSpecifiedViewport() {
		if( m_specifiedViewport != null ) {
			return new java.awt.Rectangle( m_specifiedViewport );
		} else {
			return null;
		}
	}
	public void setSpecifiedViewport( java.awt.Rectangle specifiedViewport ) {
		if( specifiedViewport != null ) {
			m_specifiedViewport = new java.awt.Rectangle( specifiedViewport );
		} else {
			m_specifiedViewport = null;
		}
	}

	public boolean isLetterboxedAsOpposedToDistorted() {
		return m_isLetterboxedAsOpposedToDistorted;
	}
	public void setIsLetterboxedAsOpposedToDistorted( boolean isLetterboxedAsOpposedToDistorted ) {
		m_isLetterboxedAsOpposedToDistorted = isLetterboxedAsOpposedToDistorted;
	}

	@Override
	public void setup( RenderContext rc ) {
		//pass
	}
	protected abstract void setupProjection( Context context, java.awt.Rectangle actualViewport );

	public void performClearAndRenderOffscreen( RenderContext rc, int width, int height ) {
		SceneAdapter sceneAdapter = getSceneAdapter();
		if( sceneAdapter != null ) {
			java.awt.Rectangle actualViewport = getActualViewport( new java.awt.Rectangle(), width, height );
			rc.gl.glMatrixMode( GL.GL_PROJECTION );
			rc.gl.glLoadIdentity();
			setupProjection( rc, actualViewport );
			rc.setViewportAndAddToClearRect( actualViewport );
			sceneAdapter.renderScene( rc, this, m_backgroundAdapter );
		}
		for( edu.cmu.cs.dennisc.layer.Layer layer : m_element.postRenderLayers ) {
			for( edu.cmu.cs.dennisc.layer.Graphic graphic : layer.getGraphics() ) {
				
			}
		}
	}
	public void performPick( PickContext pc, PickParameters pickParameters, java.awt.Rectangle actualViewport, ConformanceTestResults conformanceTestResults ) {
		SceneAdapter sceneAdapter = getSceneAdapter();
		if( sceneAdapter != null ) {

			pc.gl.glViewport( actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height );
			
			pc.gl.glMatrixMode( GL.GL_PROJECTION );
			pc.gl.glLoadIdentity();

			double tx = actualViewport.width  - 2 * ( pickParameters.getX()                        - actualViewport.x );
			double ty = actualViewport.height - 2 * ( pickParameters.getFlippedY( actualViewport ) - actualViewport.y );
			pc.gl.glTranslated( tx, ty, 0.0 );
			pc.gl.glScaled( actualViewport.width, actualViewport.height, 1.0 );
//			int[] vp = { actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height };
//			java.nio.IntBuffer vpBuffer = java.nio.IntBuffer.wrap( vp );
//			pc.glu.gluPickMatrix( pickParameters.getX(), pickParameters.getFlippedY( actualViewport ), 1.0, 1.0, vpBuffer );
			
			setupProjection( pc, actualViewport );

			pc.pickScene( this, sceneAdapter, pickParameters, conformanceTestResults );
		}
	}
	@Override
	public void renderGhost( RenderContext rc, GhostAdapter root ) {
	}
	@Override
	public void renderOpaque( RenderContext rc ) {
	}
	@Override
	public void pick( PickContext pc, PickParameters pickParameters, ConformanceTestResults conformanceTestResults ) {
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.background ) {
			m_backgroundAdapter = AdapterFactory.getAdapterFor( m_element.background.getValue() );
		} else if( property == m_element.postRenderLayers ) {
			//pass
		} else {
			super.propertyChanged( property );
		}
	}
}
