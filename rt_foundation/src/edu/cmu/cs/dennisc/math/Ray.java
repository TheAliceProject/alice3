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

package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class Ray implements java.io.Serializable {
	protected edu.cmu.cs.dennisc.math.Point3 m_origin = new edu.cmu.cs.dennisc.math.Point3( 0, 0, 0 );
	protected edu.cmu.cs.dennisc.math.Vector3 m_direction = new edu.cmu.cs.dennisc.math.Vector3( 0, 0, 1 );

	public Ray() {
	}
	public Ray( edu.cmu.cs.dennisc.math.Point3 origin, edu.cmu.cs.dennisc.math.Vector3 direction ) {
		m_origin.set( origin );
		m_direction.set( direction );
	}
	public Ray( Ray other ) {
		m_origin = other.getOrigin();
		m_direction = other.getDirection();
	}

	@Override
	public boolean equals( Object o ) {
		if( o == this )
			return true;
		if( o instanceof Ray ) {
			Ray ray = (Ray) o;
			return m_origin.equals( ray.m_origin ) && m_direction.equals( ray.m_direction );
		} else {
			return false;
		}
	}

	public boolean isNaN() {
		return m_origin.isNaN() || m_direction.isNaN();
	}
	public void setNaN() {
		m_origin.setNaN();
		m_direction.setNaN();
	}
	
	public edu.cmu.cs.dennisc.math.Point3 accessOrigin() {
		return m_origin;
	}
	public edu.cmu.cs.dennisc.math.Point3 getOrigin( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( m_origin );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Point3 getOrigin() {
		return getOrigin( new edu.cmu.cs.dennisc.math.Point3() );
	}
	public void setOrigin( edu.cmu.cs.dennisc.math.Point3 origin ) {
		if( origin != null ) {
			m_origin.set( origin );
		} else {
			m_origin.set( Double.NaN, Double.NaN, Double.NaN );
		}
	}
	public edu.cmu.cs.dennisc.math.Vector3 accessDirection() {
		return m_direction;
	}
	public edu.cmu.cs.dennisc.math.Vector3 getDirection( edu.cmu.cs.dennisc.math.Vector3 rv ) {
		rv.set( m_direction );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Vector3 getDirection() {
		return getDirection( new edu.cmu.cs.dennisc.math.Vector3() );
	}
	public void setDirection( edu.cmu.cs.dennisc.math.Vector3 direction ) {
		if( direction != null ) {
			m_direction.set( direction );
		} else {
			m_direction.set( Double.NaN, Double.NaN, Double.NaN );
		}
	}

	public edu.cmu.cs.dennisc.math.Point3 getPointAlong( edu.cmu.cs.dennisc.math.Point3 rv, double t ) {
		rv.set( m_direction );
		rv.multiply( t );
		rv.add( m_origin );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Point3 getPointAlong( double t ) {
		return getPointAlong( new edu.cmu.cs.dennisc.math.Point3(), t );
	}

	public void transform( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		m.transform( m_origin );
		m.transform( m_direction );
//		edu.cmu.cs.dennisc.math.Vector4d transformedOrigin = LinearAlgebra.multiply( m_origin.x, m_origin.y, m_origin.z, 1, m );
//		m_origin = new edu.cmu.cs.dennisc.math.Point3d( transformedOrigin.x/transformedOrigin.w, transformedOrigin.y/transformedOrigin.w, transformedOrigin.z/transformedOrigin.w );
//		edu.cmu.cs.dennisc.math.Vector4d transformedDirection = LinearAlgebra.multiply( m_direction, 0, m );
//		transformedDirection.w = 1;
//		m_direction = LinearAlgebra.newVector3d( transformedDirection );
	}
	@Override
	public String toString() {
		return "edu.cmu.cs.dennisc.math.Ray[origin=" + m_origin + ",direction=" + m_direction + "]";
	}
	//public static Ray valueOf( String s ) {
	//}
}
