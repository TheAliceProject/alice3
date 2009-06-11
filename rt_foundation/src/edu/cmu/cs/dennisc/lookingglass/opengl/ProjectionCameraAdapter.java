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
public class ProjectionCameraAdapter extends AbstractCameraAdapter< edu.cmu.cs.dennisc.scenegraph.ProjectionCamera > {
	private double[] m_projection = new double[ 16 ];
	private java.nio.DoubleBuffer m_projectionBuffer = java.nio.DoubleBuffer.wrap( m_projection );

	@Override
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, java.awt.Rectangle actualViewport ) {
		throw new RuntimeException( "TODO" );
	}
	@Override
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, java.awt.Rectangle actualViewport ) {
		throw new RuntimeException( "todo" );
	}

	@Override
	protected java.awt.Rectangle performLetterboxing( java.awt.Rectangle rv ) {
		//todo
		return rv;
	}
	@Override
	protected void setupProjection( Context context, java.awt.Rectangle actualViewport ) {
		context.gl.glLoadMatrixd( m_projectionBuffer );
	}
	
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.projection ) {
			m_element.projection.getValue().getAsColumnMajorArray16( m_projection );
		} else {
			super.propertyChanged( property );
		}
	}
}
