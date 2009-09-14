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
public class TransformableAdapter< E extends edu.cmu.cs.dennisc.scenegraph.Transformable > extends CompositeAdapter< E > {
	private double[] m_localTransformation = new double[ 16 ];
	private java.nio.DoubleBuffer m_localTransformationBuffer = java.nio.DoubleBuffer.wrap( m_localTransformation );
	
	public double[] accessLocalTransformation() {
		return m_localTransformation;
	}
	public java.nio.DoubleBuffer accessLocalTransformationAsBuffer() {
		return m_localTransformationBuffer;
	}
		
	@Override
	public void renderOpaque( RenderContext rc ) {
		rc.gl.glPushMatrix();
		try {
			rc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
			super.renderOpaque( rc );
		} finally {
			rc.gl.glPopMatrix();
		}
	}
	
	@Override
	public void renderGhost( RenderContext rc, GhostAdapter root ) {
		rc.gl.glPushMatrix();
		try {
			if( this == root ) {
				rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
			} else {
				rc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
			}
			super.renderGhost( rc, root );
		} finally {
			rc.gl.glPopMatrix();
		}
	}
	
	
	@Override
	public void pick( PickContext pc, PickParameters pickParameters, ConformanceTestResults conformanceTestResults ) {
		pc.gl.glPushMatrix();
		try {
			pc.gl.glMultMatrixd( m_localTransformationBuffer );
			super.pick( pc, pickParameters, conformanceTestResults );
		} finally {
			pc.gl.glPopMatrix();
		}
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.localTransformation ) {
			m_element.localTransformation.getValue().getAsColumnMajorArray16( m_localTransformation );
		} else {
			super.propertyChanged( property );
		}
	}
}
