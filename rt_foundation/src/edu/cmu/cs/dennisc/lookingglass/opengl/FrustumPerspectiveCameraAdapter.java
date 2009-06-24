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
public class FrustumPerspectiveCameraAdapter extends AbstractPerspectiveCameraAdapter< edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera > {
	private static edu.cmu.cs.dennisc.math.ClippedZPlane s_actualPicturePlaneBufferForReuse = new edu.cmu.cs.dennisc.math.ClippedZPlane( Double.NaN, Double.NaN, Double.NaN, Double.NaN );
//	private edu.cmu.cs.dennisc.scenegraph.ClippedPlane m_picturePlane = new edu.cmu.cs.dennisc.scenegraph.ClippedPlane( Double.NaN, Double.NaN, Double.NaN, Double.NaN );

	@Override
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, java.awt.Rectangle actualViewport ) {
		throw new RuntimeException( "TODO" );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, java.awt.Rectangle actualViewport ) {
		edu.cmu.cs.dennisc.math.ClippedZPlane actualPicturePlane = getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), actualViewport );
		double left = actualPicturePlane.getXMinimum();
		double right = actualPicturePlane.getXMaximum();
		double bottom = actualPicturePlane.getYMinimum();
		double top = actualPicturePlane.getYMaximum();
		double zNear = m_element.nearClippingPlaneDistance.getValue();
		double zFar = m_element.farClippingPlaneDistance.getValue();

		rv.right.set      ( 2*zNear,                         0,                               0,                                    0  );
		rv.up.set         ( 0,                               (2 * zNear) / (top - bottom),    0,                                    0  );
		rv.backward.set   ( (right + left) / (right - left), (top + bottom) / (top - bottom), -(zFar + zNear) / (zFar + zNear),     -1 );
		rv.translation.set( 0,                               0,                               -(2 * zFar * zNear) / (zFar - zNear), 0  );

//		rv.setRow( 0, 2 * zNear, 0, (right + left) / (right - left), 0 );
//		rv.setRow( 1, 0, (2 * zNear) / (top - bottom), (top + bottom) / (top - bottom), 0 );
//		rv.setRow( 2, 0, 0, -(zFar + zNear) / (zFar + zNear), -(2 * zFar * zNear) / (zFar - zNear) );
//		rv.setRow( 3, 0, 0, -1, 0 );

		throw new RuntimeException( "todo" );
	}

	@Override
	protected java.awt.Rectangle performLetterboxing( java.awt.Rectangle rv ) {
		//todo: handle NaN
		return rv;
	}

	protected edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, java.awt.Rectangle actualViewport ) {
		rv.set( m_element.picturePlane.getValue(), actualViewport );
		return rv;
	}

	@Override
	protected void setupProjection( Context context, java.awt.Rectangle actualViewport, float near, float far ) {
		synchronized( s_actualPicturePlaneBufferForReuse ) {
			getActualPicturePlane( s_actualPicturePlaneBufferForReuse, actualViewport );
			context.gl.glFrustum( s_actualPicturePlaneBufferForReuse.getXMinimum(), s_actualPicturePlaneBufferForReuse.getXMaximum(), s_actualPicturePlaneBufferForReuse.getYMinimum(), s_actualPicturePlaneBufferForReuse.getYMaximum(), near, far );
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
