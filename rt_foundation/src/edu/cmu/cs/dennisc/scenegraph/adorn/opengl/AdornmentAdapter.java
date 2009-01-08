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
package edu.cmu.cs.dennisc.scenegraph.adorn.opengl;

/**
 * @author Dennis Cosgrove
 */
public abstract class AdornmentAdapter extends edu.cmu.cs.dennisc.lookingglass.opengl.ComponentAdapter<edu.cmu.cs.dennisc.scenegraph.adorn.Adornment> {
	protected edu.cmu.cs.dennisc.lookingglass.opengl.CompositeAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Composite > m_adornmentRootAdapter = null;
	
	protected abstract void actuallyRender( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, edu.cmu.cs.dennisc.lookingglass.opengl.CompositeAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Composite > adornmentRootAdapter );

	@Override
	public void setup( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		//pass
	}

	@Override
	public void renderOpaque( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		if( m_adornmentRootAdapter != null ) {
			rc.gl.glPushMatrix();
			rc.gl.glMultMatrixd( accessInverseAbsoluteTransformationAsBuffer() );
			actuallyRender( rc, m_adornmentRootAdapter );
			rc.gl.glPopMatrix();
		}
	}
	
	@Override
	public void renderGhost( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, edu.cmu.cs.dennisc.lookingglass.opengl.GhostAdapter root ) {
		//todo?
		//pass
	}

	@Override
	public void pick( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc, edu.cmu.cs.dennisc.lookingglass.opengl.PickParameters pickParameters ) {
		//todo?
		//pass
	}
	
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.adorningRoot ) {
			m_adornmentRootAdapter = edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory.getAdapterFor( m_element.adorningRoot.getValue() );
		} else {
			super.propertyChanged( property );
		}
	}
}
