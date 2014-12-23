/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import edu.cmu.cs.dennisc.render.gl.imp.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrAbstractCamera<E extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> extends GlrLeaf<E> {
	private GlrBackground m_backgroundAdapter = null;
	private GlrLayer[] m_layerAdapters = null;

	private java.awt.Rectangle m_specifiedViewport = null;
	private boolean m_isLetterboxedAsOpposedToDistorted = true;

	public abstract edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, java.awt.Rectangle actualViewport );

	protected abstract java.awt.Rectangle performLetterboxing( java.awt.Rectangle rv );

	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, int surfaceWidth, int surfaceHeight ) {
		if( m_specifiedViewport != null ) {
			rv.setBounds( m_specifiedViewport );
		} else {
			rv.setBounds( 0, 0, surfaceWidth, surfaceHeight );
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

	public void performClearAndRenderOffscreen( RenderContext rc, int surfaceWidth, int surfaceHeight ) {
		GlrScene sceneAdapter = getSceneAdapter();
		if( sceneAdapter != null ) {
			java.awt.Rectangle actualViewport = getActualViewport( new java.awt.Rectangle(), surfaceWidth, surfaceHeight );
			rc.gl.glMatrixMode( GL_PROJECTION );
			rc.gl.glLoadIdentity();
			setupProjection( rc, actualViewport );
			rc.setViewportAndAddToClearRect( actualViewport );
			sceneAdapter.renderScene( rc, this, m_backgroundAdapter );
		}
	}

	public void postRender( RenderContext rc, int surfaceWidth, int surfaceHeight, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, edu.cmu.cs.dennisc.render.Graphics2D g2 ) {
		if( this.m_layerAdapters != null ) {
			java.awt.Rectangle actualViewport = getActualViewport( new java.awt.Rectangle(), surfaceWidth, surfaceHeight );
			for( GlrLayer layerAdapter : this.m_layerAdapters ) {
				layerAdapter.render( g2, renderTarget, actualViewport, this.owner );
			}
		}
	}

	public void performPick( PickContext pc, PickParameters pickParameters, java.awt.Rectangle actualViewport ) {
		GlrScene sceneAdapter = getSceneAdapter();
		if( sceneAdapter != null ) {

			pc.gl.glViewport( actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height );

			pc.gl.glMatrixMode( GL_PROJECTION );
			pc.gl.glLoadIdentity();

			double tx = actualViewport.width - ( 2 * ( pickParameters.getX() - actualViewport.x ) );
			double ty = actualViewport.height - ( 2 * ( pickParameters.getFlippedY( actualViewport ) - actualViewport.y ) );
			pc.gl.glTranslated( tx, ty, 0.0 );
			pc.gl.glScaled( actualViewport.width, actualViewport.height, 1.0 );
			//			int[] vp = { actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height };
			//			java.nio.IntBuffer vpBuffer = java.nio.IntBuffer.wrap( vp );
			//			pc.glu.gluPickMatrix( pickParameters.getX(), pickParameters.getFlippedY( actualViewport ), 1.0, 1.0, vpBuffer );

			setupProjection( pc, actualViewport );

			pc.pickScene( this, sceneAdapter, pickParameters );
		}
	}

	@Override
	public void renderGhost( RenderContext rc, GlrGhost root ) {
	}

	@Override
	public void renderOpaque( RenderContext rc ) {
	}

	@Override
	public void pick( PickContext pc, PickParameters pickParameters ) {
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.background ) {
			m_backgroundAdapter = AdapterFactory.getAdapterFor( owner.background.getValue() );
		} else if( property == owner.postRenderLayers ) {
			m_layerAdapters = AdapterFactory.getAdaptersFor( owner.postRenderLayers.getValue(), GlrLayer.class );
		} else {
			super.propertyChanged( property );
		}
	}
}
