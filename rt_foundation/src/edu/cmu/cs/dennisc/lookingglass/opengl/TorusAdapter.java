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
public class TorusAdapter extends ShapeAdapter< edu.cmu.cs.dennisc.scenegraph.Torus > {
	private double m_majorRadius;
	private double m_minorRadius;

	private void glVertex( Context context, double theta, double phi, boolean isLightingEnabled ) {
		double sinTheta = Math.sin( theta );
		double cosTheta = Math.cos( theta );
		double sinPhi = Math.sin( phi );
		double cosPhi = Math.cos( phi );

		double y = m_minorRadius * sinPhi;
		double r = m_majorRadius + m_minorRadius * cosPhi;
		double x = sinTheta * r;
		double z = cosTheta * r;
		if( isLightingEnabled ) {
			context.gl.glNormal3d( sinTheta * cosPhi, sinPhi, cosTheta * cosPhi );
		}
		context.gl.glVertex3d( x, y, z );
	}

	private void glTorus( Context context, boolean isLightingEnabled ) {
		//todo: add scenegraph hint
		final int N = 32;
		final int M = 16;

		double dTheta = 2 * Math.PI / (N - 1);
		double dPhi = 2 * Math.PI / (M - 1);

		double theta = 0;
		for( int i = 0; i < N - 1; i++ ) {
			double phi = 0;
			context.gl.glBegin( javax.media.opengl.GL.GL_QUAD_STRIP );
			for( int j = 0; j < M; j++ ) {
				glVertex( context, theta, phi, isLightingEnabled );
				glVertex( context, theta + dTheta, phi, isLightingEnabled );
				phi += dPhi;
			}
			context.gl.glEnd();
			theta += dTheta;
		}
	}

	@Override
	protected void renderGeometry( RenderContext rc ) {
		glTorus( rc, true );
	}
	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		int name;
		if( isSubElementRequired ) {
			name = 0;
		} else {
			name = -1;
		}
		pc.gl.glPushName( name );
		glTorus( pc, false );
		pc.gl.glPopName();
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.majorRadius ) {
			m_majorRadius = m_element.majorRadius.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.minorRadius ) {
			m_majorRadius = m_element.minorRadius.getValue();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
