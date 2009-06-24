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
public class LinearFogAdapter extends FogAdapter< edu.cmu.cs.dennisc.scenegraph.LinearFog > {
	private float m_near;
	private float m_far;

	@Override
	public void setup( RenderContext rc ) {
		super.setup( rc );
		rc.gl.glFogi( GL.GL_FOG_MODE, GL.GL_LINEAR );
		rc.gl.glFogf( GL.GL_FOG_START, m_near );
		rc.gl.glFogf( GL.GL_FOG_END, m_far );
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.nearDistance ) {
			m_near = m_element.nearDistance.getValue().floatValue();
		} else if( property == m_element.farDistance ) {
			m_far = m_element.farDistance.getValue().floatValue();
		} else {
			super.propertyChanged( property );
		}
	}
}
