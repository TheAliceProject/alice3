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
public class SphereAdapter extends ShapeAdapter< edu.cmu.cs.dennisc.scenegraph.Sphere > {
	private double m_radius;
	//todo: add scenegraph hint
	private int m_stacks = 50;
	private int m_slices = 50;

	private void glSphere( Context c ) {
		c.glu.gluSphere( c.getQuadric(), m_radius, m_slices, m_stacks );
	}
	@Override
	protected void renderGeometry( RenderContext rc ) {
		glSphere( rc );
	}
	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource(edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement) {
		//assuming ray unit length
		
		edu.cmu.cs.dennisc.math.Point3 origin = new edu.cmu.cs.dennisc.math.Point3( 0,0,0 );
		edu.cmu.cs.dennisc.math.Vector3 dst = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( ray.accessOrigin(), origin );
		double b = edu.cmu.cs.dennisc.math.Vector3.calculateDotProduct( dst, ray.accessDirection() );
		double c = edu.cmu.cs.dennisc.math.Vector3.calculateDotProduct( dst, dst ) - m_radius;
		double d = b*b -c;
		if( d > 0 ) {
			double t = -b - Math.sqrt( d );
			ray.getPointAlong(rv, t);
		} else {
			rv.setNaN();
		}
		return rv;
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
		glSphere( pc );
		pc.gl.glPopName();
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.radius ) {
			m_radius = m_element.radius.getValue();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
