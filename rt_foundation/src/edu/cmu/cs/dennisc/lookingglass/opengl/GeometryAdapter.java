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
public abstract class GeometryAdapter< E extends edu.cmu.cs.dennisc.scenegraph.Geometry > extends ElementAdapter< E > {
	private java.util.List< RenderContext > m_renderContexts = new java.util.LinkedList< RenderContext >();
	public void addRenderContext( RenderContext rc ) {
		m_renderContexts.add( rc );
	}
	public void removeRenderContext( RenderContext rc ) {
		m_renderContexts.add( rc );
	}
	@Override
	public void handleReleased() {
		super.handleReleased();
		if( m_renderContexts.size() > 0 ) {
			RenderContext[] renderContexts = new RenderContext[ m_renderContexts.size() ];
			m_renderContexts.toArray( renderContexts );
			for( RenderContext rc : renderContexts ) {
				rc.forgetGeometryAdapter( this, true );
			}
		}
	}

	private boolean m_isGeometryChanged;
    protected boolean isGeometryChanged() {
        return m_isGeometryChanged;
    }
    protected void setIsGeometryChanged( boolean isGeometryChanged ) {
        m_isGeometryChanged = isGeometryChanged;
    }
    public abstract boolean isAlphaBlended();


    protected boolean isDisplayListDesired() {
    	return true;
    }
    protected boolean isDisplayListInNeedOfRefresh( RenderContext rc ) {
    	return isGeometryChanged();
    }

    //todo: better name
    protected abstract void renderGeometry( RenderContext rc );
    protected abstract void pickGeometry( PickContext pc, boolean isSubElementRequired );
    
    public final void render( RenderContext rc ) {
    	if( isDisplayListDesired() ) {
    		Integer id = rc.getDisplayListID( this );
    		if( id == null ) {
    			id = rc.generateDisplayListID( this );
    			setIsGeometryChanged( true );
    		}
    		if( isDisplayListInNeedOfRefresh( rc )  ) {
    			rc.gl.glNewList( id, GL.GL_COMPILE_AND_EXECUTE );
    			try {
            		renderGeometry( rc );
    			} finally {
    				rc.gl.glEndList();
    			}
    			setIsGeometryChanged( false );
    		} else {
    			rc.gl.glCallList( id );
    		}
    	} else {
    		renderGeometry( rc );
    	}
    }
	public final void pick( PickContext pc, boolean isSubElementRequired ) {
		//todo: display lists?
		pickGeometry( pc, isSubElementRequired );
	}
}
