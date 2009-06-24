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
public class SymmetricPerspectiveCameraAdapter extends AbstractPerspectiveCameraAdapter< edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera > {
	private double m_verticalInDegrees;
	private double m_horizontalInDegrees;
	
	@Override
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, java.awt.Rectangle actualViewport ) {
		double vertical = getActualVerticalViewingAngle( actualViewport ).getAsRadians();
		double near = m_element.nearClippingPlaneDistance.getValue();
		double far = m_element.farClippingPlaneDistance.getValue();

		//todo: investigate (especially x)
		xPixel = actualViewport.width - xPixel;
		yPixel = actualViewport.height - yPixel;

		double tanHalfVertical = Math.tan( vertical * 0.5 );
		double aspect = actualViewport.width / (double)actualViewport.height;
		double halfWidth = actualViewport.width * 0.5;
		double halfHeight = actualViewport.height * 0.5;
		double dx = tanHalfVertical * (xPixel / halfWidth - 1.0) * aspect;
		double dy = tanHalfVertical * (1.0 - yPixel / halfHeight);

		//todo: optimize?
		edu.cmu.cs.dennisc.math.Point3 pNear = new edu.cmu.cs.dennisc.math.Point3( dx * near, dy * near, near );
		edu.cmu.cs.dennisc.math.Point3 pFar = new edu.cmu.cs.dennisc.math.Point3( dx * far, dy * far, far );

		rv.setOrigin( pNear );
		edu.cmu.cs.dennisc.math.Vector3 direction = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( pFar, pNear );
		//todo: remove?
		direction.normalize();
		rv.setDirection( direction );

		return rv;
	}

	@Override
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, java.awt.Rectangle actualViewport ) {
		double zNear = m_element.nearClippingPlaneDistance.getValue();
		double zFar = m_element.farClippingPlaneDistance.getValue();
		double fovx = getActualHorizontalViewingAngle( actualViewport ).getAsRadians();
		double fovy = getActualVerticalViewingAngle( actualViewport ).getAsRadians();
		double aspect = fovx / fovy;
		double f = 1 / Math.tan( fovy / 2 );
		
		rv.right.set      ( f/aspect, 0, 0,                                    0 );
		rv.up.set         ( 0,        f, 0,                                    0 );
		rv.backward.set   ( 0,        0, (zFar + zNear) / (zNear - zFar),     -1 );
		rv.translation.set( 0,        0, (2 * zFar * zNear) / (zNear - zFar),  0 );
		
//		rv.setRow( 0, f / aspect, 0, 0, 0 );
//		rv.setRow( 1, 0, f, 0, 0 );
//		rv.setRow( 2, 0, 0, (zFar + zNear) / (zNear - zFar), (2 * zFar * zNear) / (zNear - zFar) );
//		rv.setRow( 3, 0, 0, -1, 0 );
		return rv;
	}

	@Override
	protected java.awt.Rectangle performLetterboxing( java.awt.Rectangle rv ) {
		if( Double.isNaN( m_horizontalInDegrees ) || Double.isNaN( m_verticalInDegrees ) ) {
			//pass
		} else {
			double aspect = m_horizontalInDegrees / m_verticalInDegrees;
			double pixelAspect = rv.width / (double)rv.height;
			if( aspect > pixelAspect ) {
				int letterBoxedHeight = (int)((rv.width / aspect) + 0.5);
				rv.setBounds( 0, (rv.height - letterBoxedHeight) / 2, rv.width, letterBoxedHeight );
			} else if( aspect < pixelAspect ) {
				int letterBoxedWidth = (int)((rv.height * aspect) + 0.5);
				rv.setBounds( (rv.width - letterBoxedWidth) / 2, 0, letterBoxedWidth, rv.height );
			} else {
				//pass
			}
		}
		return rv;
	}

	private static final double DEFAULT_ACTUAL_VERTICAL_IN_DEGREES = new edu.cmu.cs.dennisc.math.AngleInRadians( 0.5 ).getAsDegrees();

	public edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle( java.awt.Rectangle actualViewport ) {
		double horizontalInDegrees;
		if( Double.isNaN( m_horizontalInDegrees ) ) {
			double aspect = actualViewport.width / (double)actualViewport.height;
			if( Double.isNaN( m_verticalInDegrees ) ) {
				horizontalInDegrees = DEFAULT_ACTUAL_VERTICAL_IN_DEGREES * aspect;
			} else {
				horizontalInDegrees = m_verticalInDegrees * aspect;
			}
		} else {
			horizontalInDegrees = m_horizontalInDegrees;
		}
		return new edu.cmu.cs.dennisc.math.AngleInDegrees( horizontalInDegrees );
	}
	public edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle( java.awt.Rectangle actualViewport ) {
		double verticalInDegrees;
		if( Double.isNaN( m_verticalInDegrees ) ) {
			double aspect = actualViewport.width / (double)actualViewport.height;
			if( Double.isNaN( m_horizontalInDegrees ) ) {
				verticalInDegrees = DEFAULT_ACTUAL_VERTICAL_IN_DEGREES;
			} else {
				verticalInDegrees = m_horizontalInDegrees / aspect;
			}
		} else {
			verticalInDegrees = m_verticalInDegrees;
		}
		return new edu.cmu.cs.dennisc.math.AngleInDegrees( verticalInDegrees );
	}

//	@Override
//	protected void setupViewportAndProjection( PickContext pc, int x, int y, java.awt.Rectangle actualViewport, float zNear, float zFar ) {
//		int yFlipped = actualViewport.height - y;
//
////		double vertical = getActualVerticalViewingAngle( actualViewport, edu.cmu.cs.dennisc.math.UnitOfAngle.RADIANS );
////		
////		double xInWindow = x;
////		//todo: account for actualViewport.x
////		xInWindow /= actualViewport.width; 
////		xInWindow *= 2; 
////		xInWindow -= 1; 
////
////		double yInWindow = actualViewport.height - y;
////		//todo: account for actualViewport.y
////		yInWindow /= actualViewport.height; 
////		yInWindow *= 2; 
////		yInWindow -= 1;
////		
//////		xInWindow = 0.0;
//////		yInWindow = 0.0;
////		
////		edu.cmu.cs.dennisc.math.Matrix4d actualProjection = new edu.cmu.cs.dennisc.math.Matrix4d();
////		getActualProjectionMatrix( actualProjection, actualViewport );
////		
////		edu.cmu.cs.dennisc.math.Vector4d xyzwNear = new edu.cmu.cs.dennisc.math.Vector4d();
////		xyzwNear.x = xInWindow;
////		xyzwNear.y = yInWindow;
////		xyzwNear.z = 0.0;
////		xyzwNear.w = 1.0;
////		
////		actualProjection.invert();
////		actualProjection.transform( xyzwNear );
////		
//////		
//////		actualProjection.transform( xyzwNear );
//////		xyzwNear.scale( 1/xyzwNear.w );
////		
//////		edu.cmu.cs.dennisc.math.Vector4d xyzwFar = new edu.cmu.cs.dennisc.math.Vector4d();
//////		xyzwFar.x = xInWindow;
//////		xyzwFar.y = yInWindow;
//////		xyzwFar.z = zFar;
//////		xyzwFar.w = 1.0;
//////		
//////		actualProjection.transform( xyzwFar );
////		
////		double aspect = actualViewport.width / (double)actualViewport.height;
////		
////		double tanHalfVertical = Math.tan( vertical * 0.5 );
////		double halfHeightNear = tanHalfVertical * zNear;
////		double halfHeightPixelNear = halfHeightNear / actualViewport.height;
////		
////		//double halfWidthNear = halfHeightNear * aspect;
////		double halfWidthPixelNear = halfHeightPixelNear * aspect;
////
////		double _x = xyzwNear.x / xyzwNear.w;
////		double _y = xyzwNear.y / xyzwNear.w;
////		
////		System.err.println( _x + ", " + _y );
////		System.err.println( halfWidthPixelNear + ", " + halfHeightPixelNear );
////		System.err.println( zNear + " " + ( xyzwNear.z / xyzwNear.w ) );
////
////		
//
//		double halfVertical = getActualVerticalViewingAngle( actualViewport, edu.cmu.cs.dennisc.math.UnitOfAngle.RADIANS ) * 0.5;
//		double aspect = actualViewport.width / (double)actualViewport.height;
//		
//		double halfVerticalTangent = Math.tan( halfVertical );
//		double halfHeightPlaneNear = zNear * halfVerticalTangent;
//		double halfHeightPixelNear = halfHeightPlaneNear / actualViewport.height;
//
//		double halfWidthPlaneNear = halfHeightPlaneNear * aspect;
//		double halfWidthPixelNear = halfHeightPixelNear * aspect;
//		
//		double left = -halfWidthPlaneNear  + ( x        * halfWidthPixelNear );
//		double top  = -halfHeightPlaneNear + ( yFlipped * halfHeightPixelNear );
//		
//		double right  = left + halfWidthPixelNear  + halfWidthPixelNear;
//		double bottom = top  + halfHeightPixelNear + halfHeightPixelNear;
//		
//		pc.gl.glFrustum( left, right, top, bottom, zNear, zFar );
//		pc.gl.glViewport( x, yFlipped, 1, 1 );
//	}

	
	@Override
	protected void setupProjection( Context context, java.awt.Rectangle actualViewport, float zNear, float zFar ) {
//		double actualVerticalInDegrees = getActualVerticalViewingAngle( actualViewport, edu.cmu.cs.dennisc.math.UnitOfAngle.DEGREES );
//		context.glu.gluPerspective( actualVerticalInDegrees, actualViewport.width / (double)actualViewport.height, zNear, zFar );

//		double halfVertical = getActualVerticalViewingAngle( actualViewport, edu.cmu.cs.dennisc.math.UnitOfAngle.RADIANS ) * 0.5;
//		double aspect = actualViewport.width / (double)actualViewport.height;
//		
//		double halfVerticalTangent = Math.tan( halfVertical );
//		double yNear = zNear * halfVerticalTangent;
//		double xNear = yNear * aspect;
//
//		context.gl.glFrustum( -xNear, +xNear, -yNear, +yNear, zNear, zFar );
		
		edu.cmu.cs.dennisc.math.Matrix4x4 projection = new edu.cmu.cs.dennisc.math.Matrix4x4();
		double[] projectionArray = new double[ 16 ];
		java.nio.DoubleBuffer projectionBuffer = java.nio.DoubleBuffer.wrap( projectionArray );
		getActualProjectionMatrix( projection, actualViewport );
		projection.getAsColumnMajorArray16( projectionArray );
		context.gl.glMultMatrixd( projectionBuffer );
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.verticalViewingAngle ) {
			m_verticalInDegrees = m_element.verticalViewingAngle.getValue().getAsDegrees();
		} else if( property == m_element.horizontalViewingAngle ) {
			m_horizontalInDegrees = m_element.horizontalViewingAngle.getValue().getAsDegrees();
		} else {
			super.propertyChanged( property );
		}
	}
}
