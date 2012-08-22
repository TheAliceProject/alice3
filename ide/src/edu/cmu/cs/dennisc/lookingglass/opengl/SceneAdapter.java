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

package edu.cmu.cs.dennisc.lookingglass.opengl;

import static javax.media.opengl.GL.GL_ALWAYS;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_CCW;
import static javax.media.opengl.GL.GL_CLIP_PLANE0;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_CW;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_EQUAL;
import static javax.media.opengl.GL.GL_KEEP;
import static javax.media.opengl.GL.GL_MODELVIEW;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_REPLACE;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_STENCIL_BUFFER_BIT;
import static javax.media.opengl.GL.GL_STENCIL_TEST;

/**
 * @author Dennis Cosgrove
 */
public class SceneAdapter extends CompositeAdapter<edu.cmu.cs.dennisc.scenegraph.Scene> {
	private BackgroundAdapter m_backgroundAdapter = null;
	private float m_globalBrightness;
	private java.util.Vector<GhostAdapter> m_ghostAdapters = new java.util.Vector<GhostAdapter>();
	//todo: reduce visibility
	protected java.util.Vector<VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual>> m_visualAdapters = new java.util.Vector<VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual>>();
	private java.util.Vector<PlanarReflectorAdapter> m_planarReflectorAdapters = new java.util.Vector<PlanarReflectorAdapter>();

	@Override
	public void initialize( edu.cmu.cs.dennisc.scenegraph.Scene sgElement ) {
		super.initialize( sgElement );
		for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : edu.cmu.cs.dennisc.pattern.VisitUtilities.getAll( m_element, edu.cmu.cs.dennisc.scenegraph.Component.class ) ) {
			addDescendant( AdapterFactory.getAdapterFor( sgComponent ) );
		}
		//		m_sgE.accept( new edu.cmu.cs.dennisc.pattern.FilteredVisitor< edu.cmu.cs.dennisc.scenegraph.Component >() {
		//			@Override
		//			protected void filteredVisit( edu.cmu.cs.dennisc.scenegraph.Component sgComponent ) {
		//				addDescendant( AdapterFactory.getAdapterFor( sgComponent ) );
		//			}
		//		} );
	}

	public BackgroundAdapter getBackgroundAdapter() {
		return m_backgroundAdapter;
	}

	public void addDescendant( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> componentAdapter ) {
		if( componentAdapter instanceof GhostAdapter ) {
			synchronized( m_ghostAdapters ) {
				m_ghostAdapters.add( (GhostAdapter)componentAdapter );
			}
		} else if( componentAdapter instanceof VisualAdapter<?> ) {
			synchronized( m_visualAdapters ) {
				m_visualAdapters.add( (VisualAdapter<?>)componentAdapter );
			}
			if( componentAdapter instanceof PlanarReflectorAdapter ) {
				synchronized( m_planarReflectorAdapters ) {
					m_planarReflectorAdapters.add( (PlanarReflectorAdapter)componentAdapter );
				}
			}
		}
	}

	public void removeDescendant( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> componentAdapter ) {
		if( componentAdapter instanceof GhostAdapter ) {
			synchronized( m_ghostAdapters ) {
				m_ghostAdapters.remove( componentAdapter );
			}
		} else if( componentAdapter instanceof VisualAdapter<?> ) {
			synchronized( m_visualAdapters ) {
				m_visualAdapters.remove( componentAdapter );
			}
			if( componentAdapter instanceof PlanarReflectorAdapter ) {
				synchronized( m_planarReflectorAdapters ) {
					m_planarReflectorAdapters.remove( componentAdapter );
				}
			}
		}
	}

	public void renderAlphaBlended( RenderContext rc ) {
		// todo depth sort
		//rc.gl.glDisable( GL_DEPTH_TEST );
		//		rc.gl.glDepthMask( false );
		//		try {
		synchronized( m_ghostAdapters ) {
			for( GhostAdapter ghostAdapter : m_ghostAdapters ) {
				ghostAdapter.renderGhost( rc, ghostAdapter );
			}
		}
		synchronized( m_visualAdapters ) {
			for( VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> visualAdapter : m_visualAdapters ) {
				if( visualAdapter.isAlphaBlended() ) {
					//todo: adapters should be removed 
					if( ( visualAdapter.m_element != null ) && ( visualAdapter.m_element.getRoot() instanceof edu.cmu.cs.dennisc.scenegraph.Scene ) ) {
						visualAdapter.renderAlphaBlended( rc );
					}
				}
			}
		}
		//		} finally {
		//			rc.gl.glDepthMask( true );
		//		}
		//rc.gl.glEnable( GL_DEPTH_TEST );
	}

	@Override
	public void setup( RenderContext rc ) {
		rc.setGlobalBrightness( m_globalBrightness );
		rc.beginAffectorSetup();
		super.setup( rc );
		rc.endAffectorSetup();
	}

	private void renderScene( RenderContext rc ) {
		rc.gl.glDisable( GL_BLEND );
		renderOpaque( rc );
		rc.gl.glEnable( GL_BLEND );
		rc.gl.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		renderAlphaBlended( rc );
		rc.gl.glDisable( GL_BLEND );
	}

	public void renderScene( RenderContext rc, AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter, BackgroundAdapter backgroundAdapter ) {
		rc.gl.glMatrixMode( GL_MODELVIEW );
		synchronized( cameraAdapter ) {
			rc.gl.glLoadMatrixd( cameraAdapter.accessInverseAbsoluteTransformationAsBuffer() );
		}

		if( backgroundAdapter != null ) {
			//pass
		} else {
			backgroundAdapter = m_backgroundAdapter;
		}
		if( backgroundAdapter != null ) {
			backgroundAdapter.setup( rc );
		}

		if( m_planarReflectorAdapters.size() > 0 ) {
			PlanarReflectorAdapter planarReflectorAdapter = m_planarReflectorAdapters.get( 0 );
			if( planarReflectorAdapter.isFacing( cameraAdapter ) ) {
				rc.gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT );
				rc.gl.glColorMask( false, false, false, false );
				rc.gl.glEnable( GL_STENCIL_TEST );
				rc.gl.glStencilFunc( GL_ALWAYS, 1, 1 );
				rc.gl.glStencilOp( GL_KEEP, GL_KEEP, GL_REPLACE );
				rc.gl.glDisable( GL_DEPTH_TEST );
				planarReflectorAdapter.renderStencil( rc );
				rc.gl.glEnable( GL_DEPTH_TEST );
				rc.gl.glColorMask( true, true, true, true );
				rc.gl.glStencilFunc( GL_EQUAL, 1, 1 );
				rc.gl.glStencilOp( GL_KEEP, GL_KEEP, GL_KEEP );
				rc.gl.glEnable( GL_CLIP_PLANE0 );
				rc.gl.glPushMatrix();
				planarReflectorAdapter.applyReflection( rc );
				rc.gl.glFrontFace( GL_CW );
				setup( rc );
				renderScene( rc );
				rc.gl.glFrontFace( GL_CCW );
				rc.gl.glPopMatrix();
				rc.gl.glDisable( GL_CLIP_PLANE0 );
				rc.gl.glDisable( GL_STENCIL_TEST );
				setup( rc );
				rc.gl.glEnable( GL_BLEND );
				rc.gl.glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
				planarReflectorAdapter.renderStencil( rc );
				rc.gl.glDisable( GL_BLEND );
			} else {
				rc.gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
				setup( rc );
			}
		} else {
			rc.gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
			setup( rc );
		}
		renderScene( rc );
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.background ) {
			m_backgroundAdapter = AdapterFactory.getAdapterFor( m_element.background.getValue() );
		} else if( property == m_element.globalBrightness ) {
			m_globalBrightness = m_element.globalBrightness.getValue();
		} else {
			super.propertyChanged( property );
		}
	}
}
