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
public class OrthographicCameraAdapter extends AbstractNearPlaneAndFarPlaneCameraAdapter< edu.cmu.cs.dennisc.scenegraph.OrthographicCamera > {
	private static edu.cmu.cs.dennisc.math.ClippedZPlane s_actualPicturePlaneBufferForReuse = new edu.cmu.cs.dennisc.math.ClippedZPlane( Double.NaN, Double.NaN, Double.NaN, Double.NaN );
//	private edu.cmu.cs.dennisc.scenegraph.ClippedPlane m_picturePlane = new edu.cmu.cs.dennisc.scenegraph.ClippedPlane( Double.NaN, Double.NaN, Double.NaN, Double.NaN );

	@Override
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, java.awt.Rectangle actualViewport ) {
		throw new RuntimeException( "TODO" );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, java.awt.Rectangle actualViewport ) {
		synchronized( s_actualPicturePlaneBufferForReuse ) {
			edu.cmu.cs.dennisc.math.ClippedZPlane actualPicturePlane = getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), actualViewport );
			double left = actualPicturePlane.getXMinimum();
			double right = actualPicturePlane.getXMaximum();
			double bottom = actualPicturePlane.getYMinimum();
			double top = actualPicturePlane.getYMaximum();
			double near = m_element.nearClippingPlaneDistance.getValue();
			double far = m_element.farClippingPlaneDistance.getValue();

			rv.setIdentity();

			rv.right.x = 2 / (right - left);
			rv.up.y = 2 / (top - bottom);
			rv.backward.z = 2 / (far - near);

			rv.translation.x = (right + left) / (right - left);
			rv.translation.y = (top + bottom) / (top - bottom);
			rv.translation.z = (far + near) / (far - near);
		}
		return rv;
	}

	@Override
	protected java.awt.Rectangle performLetterboxing( java.awt.Rectangle rv ) {
		//todo: handle NaN
		return rv;
	}

	protected edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, java.awt.Rectangle actualViewport ) {
//		rv.set( m_picturePlane, actualViewport );
		rv.set( m_element.picturePlane.getValue(), actualViewport );
		return rv;
	}

	@Override
	protected void setupProjection( Context context, java.awt.Rectangle actualViewport, float near, float far ) {
		synchronized( s_actualPicturePlaneBufferForReuse ) {
			getActualPicturePlane( s_actualPicturePlaneBufferForReuse, actualViewport );
			context.gl.glOrtho( s_actualPicturePlaneBufferForReuse.getXMinimum(), s_actualPicturePlaneBufferForReuse.getXMaximum(), s_actualPicturePlaneBufferForReuse.getYMinimum(), s_actualPicturePlaneBufferForReuse.getYMaximum(), near, far );
		}
	}	

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.picturePlane ) {
		} else {
			super.propertyChanged( property );
		}
	}
}
