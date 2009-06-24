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
public class SceneAdapter extends CompositeAdapter< edu.cmu.cs.dennisc.scenegraph.Scene > {
	private BackgroundAdapter m_backgroundAdapter = null;
	private float m_globalBrightness;
	private java.util.Vector< GhostAdapter > m_ghostAdapters = new java.util.Vector< GhostAdapter >();
	//todo: reduce visibility
	protected java.util.Vector< VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> > m_visualAdapters = new java.util.Vector< VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> >();
	private java.util.Vector< PlanarReflectorAdapter > m_planarReflectorAdapters = new java.util.Vector< PlanarReflectorAdapter >();

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

	public void addDescendant( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > componentAdapter ) {
		if( componentAdapter instanceof GhostAdapter ) {
			synchronized( m_ghostAdapters ) {
				m_ghostAdapters.add( (GhostAdapter)componentAdapter );
			}
		} else if( componentAdapter instanceof VisualAdapter ) {
			synchronized( m_visualAdapters ) {
				m_visualAdapters.add( (VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual>)componentAdapter );
			}
			if( componentAdapter instanceof PlanarReflectorAdapter ) {
				synchronized( m_planarReflectorAdapters ) {
					m_planarReflectorAdapters.add( (PlanarReflectorAdapter)componentAdapter );
				}
			}
		}
	}
	public void removeDescendant( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > componentAdapter ) {
		if( componentAdapter instanceof GhostAdapter ) {
			synchronized( m_ghostAdapters ) {
				m_ghostAdapters.remove( (GhostAdapter)componentAdapter );
			}
		} else if( componentAdapter instanceof VisualAdapter ) {
			synchronized( m_visualAdapters ) {
				m_visualAdapters.remove( (VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual>)componentAdapter );
			}
			if( componentAdapter instanceof PlanarReflectorAdapter ) {
				synchronized( m_planarReflectorAdapters ) {
					m_planarReflectorAdapters.remove( (PlanarReflectorAdapter)componentAdapter );
				}
			}
		}
	}

	public void renderAlphaBlended( RenderContext rc ) {
		// todo depth sort
		//rc.gl.glDisable( GL.GL_DEPTH_TEST );
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
					visualAdapter.renderAlphaBlended( rc );
				}
			}
		}
		//		} finally {
		//			rc.gl.glDepthMask( true );
		//		}
		//rc.gl.glEnable( GL.GL_DEPTH_TEST );
	}

	@Override
	public void setup( RenderContext rc ) {
		rc.setGlobalBrightness( m_globalBrightness );
		rc.beginAffectorSetup();
		super.setup( rc );
		rc.endAffectorSetup();
	}

	private void renderScene( RenderContext rc ) {
		rc.gl.glDisable( GL.GL_BLEND );
		renderOpaque( rc );
		rc.gl.glEnable( GL.GL_BLEND );
		rc.gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
		renderAlphaBlended( rc );
		rc.gl.glDisable( GL.GL_BLEND );
	}

	public void renderScene( RenderContext rc, AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter, BackgroundAdapter backgroundAdapter ) {
		rc.gl.glMatrixMode( GL.GL_MODELVIEW );
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
				rc.gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT | GL.GL_STENCIL_BUFFER_BIT );
				rc.gl.glColorMask( false, false, false, false );
				rc.gl.glEnable( GL.GL_STENCIL_TEST );
				rc.gl.glStencilFunc( GL.GL_ALWAYS, 1, 1 );
				rc.gl.glStencilOp( GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE );
				rc.gl.glDisable( GL.GL_DEPTH_TEST );
				planarReflectorAdapter.renderStencil( rc );
				rc.gl.glEnable( GL.GL_DEPTH_TEST );
				rc.gl.glColorMask( true, true, true, true );
				rc.gl.glStencilFunc( GL.GL_EQUAL, 1, 1 );
				rc.gl.glStencilOp( GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP );
				rc.gl.glEnable( GL.GL_CLIP_PLANE0 );
				rc.gl.glPushMatrix();
				planarReflectorAdapter.applyReflection( rc );
				rc.gl.glFrontFace( GL.GL_CW );
				setup( rc );
				renderScene( rc );
				rc.gl.glFrontFace( GL.GL_CCW );
				rc.gl.glPopMatrix();
				rc.gl.glDisable( GL.GL_CLIP_PLANE0 );
				rc.gl.glDisable( GL.GL_STENCIL_TEST );
				setup( rc );
				rc.gl.glEnable( GL.GL_BLEND );
				rc.gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
				planarReflectorAdapter.renderStencil( rc );
				rc.gl.glDisable( GL.GL_BLEND );
			} else {
				rc.gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
				setup( rc );
			}
		} else {
			rc.gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
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
