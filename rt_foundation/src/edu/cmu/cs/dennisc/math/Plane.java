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
public class Plane {
	private double m_a;
	private double m_b;
	private double m_c;
	private double m_d;

	public Plane() {
		set( 0, 1, 0, 0 );
	}
	public Plane( double a, double b, double c, double d ) {
		set( a, b, c, d );
	}
	public Plane( double[] array ) {
		set( array );
	}
	public Plane( edu.cmu.cs.dennisc.math.Point3 position, edu.cmu.cs.dennisc.math.Vector3 normal ) {
		set( position, normal );
	}
	public Plane( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		set( m );
	}
	public Plane( edu.cmu.cs.dennisc.math.Point3 a, edu.cmu.cs.dennisc.math.Point3 b, edu.cmu.cs.dennisc.math.Point3 c ) {
		set( a, b, c );
	}

	public void set( double a, double b, double c, double d ) {
		m_a = a;
		m_b = b;
		m_c = c;
		m_d = d;
	}
	public void set( double[] array ) {
		set( array[ 0 ], array[ 1 ], array[ 2 ], array[ 3 ] );
	}
	
	//Kept private to avoid confusion on order
	private void set( double xPosition, double yPosition, double zPosition, double xNormal, double yNormal, double zNormal ) {
		final double EPSILON = 0.01;
		assert EpsilonUtilities.isWithinEpsilonOf1InSquaredSpace( Tuple3.calculateMagnitudeSquared( xNormal, yNormal, zNormal ), EPSILON );
		//assert EpsilonUtilities.isWithinEpsilon( Tuple3.calculateMagnitudeSquared( xNormal, yNormal, zNormal ), 1.0, EPSILON );
		set( xNormal, yNormal, zNormal, -(xNormal*xPosition + yNormal*yPosition + zNormal*zPosition) );
	}
	public void set( edu.cmu.cs.dennisc.math.Point3 position, edu.cmu.cs.dennisc.math.Vector3 normal ) {
		assert position.isNaN() == false;
		assert normal.isNaN() == false;
		set( position.x, position.y, position.z, normal.x, normal.y, normal.z );
	}
	public void set( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		assert m.isNaN() == false;
		set( m.translation.x, m.translation.y, m.translation.z, -m.orientation.backward.x, -m.orientation.backward.y, -m.orientation.backward.z );
	}
	public void set( edu.cmu.cs.dennisc.math.Point3 a, edu.cmu.cs.dennisc.math.Point3 b, edu.cmu.cs.dennisc.math.Point3 c ) {
		assert a.isNaN() == false;
		assert b.isNaN() == false;
		assert c.isNaN() == false;
		edu.cmu.cs.dennisc.math.Vector3 ac = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( c, a );
		edu.cmu.cs.dennisc.math.Vector3 ab = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( b, a );
		ac.normalize();
		ab.normalize();
		edu.cmu.cs.dennisc.math.Vector3 normal = edu.cmu.cs.dennisc.math.Vector3.createCrossProduct( ac, ab );
		set( a, normal );
	}
	public boolean isNaN() {
		return Double.isNaN( m_a ) || Double.isNaN( m_b ) || Double.isNaN( m_c ) || Double.isNaN( m_d );
	}
	public void setNaN() {
		set( Double.NaN, Double.NaN, Double.NaN, Double.NaN );
	}
	
	public double[] getEquation( double[] rv ) {
		rv[ 0 ] = m_a;
		rv[ 1 ] = m_b;
		rv[ 2 ] = m_c;
		rv[ 3 ] = m_d;
		return rv;
	}
	public double[] getEquation() {
		return getEquation( new double[ 4 ] );
	}
//	@Override
//	public synchronized Object clone() {
//		try {
//			return super.clone();
//		} catch( CloneNotSupportedException e ) {
//			throw new InternalError();
//		}
//	}
	@Override
	public boolean equals( Object o ) {
		if( o == this )
			return true;
		if( o != null && o instanceof Plane ) {
			Plane plane = (Plane)o;
			return m_a == plane.m_a && m_b == plane.m_b && m_c == plane.m_c && m_d == plane.m_d;
		} else {
			return false;
		}
	}

	public double intersect( Ray ray ) {
		edu.cmu.cs.dennisc.math.Point3 p = ray.getOrigin();
		edu.cmu.cs.dennisc.math.Vector3 d = ray.getDirection();
		double denom = m_a * d.x + m_b * d.y + m_c * d.z;
		if( denom == 0 ) {
			return Double.NaN;
		} else {
			double numer = m_a * p.x + m_b * p.y + m_c * p.z + m_d;
			return -numer / denom;
		}
	}
	
	public double evaluate( Point3 p ) {
		return m_a*p.x + m_b*p.y + m_c*p.z + m_d;
	}
	
//	public LineD intersect( LineD rv, PlaneD other ) {
//		throw new RuntimException( "todo" );
//	}
//	public LineD intersect( PlaneD other ) {
//		return intersect( new LineD(), other );
//	}

//	public edu.cmu.cs.dennisc.math.Matrix4d getReflection( edu.cmu.cs.dennisc.math.Matrix4d rv ) {
//		rv.setRow( 0, -2 * m_a * m_a + 1,   -2 * m_b * m_a,       -2 * m_c * m_a,       0.0 );
//		rv.setRow( 1, -2 * m_a * m_b,       -2 * m_b * m_b + 1,   -2 * m_c * m_b,       0.0 );
//		rv.setRow( 2, -2 * m_a * m_c,       -2 * m_b * m_c,       -2 * m_c * m_c + 1,   0.0 );
//		rv.setRow( 3, -2 * m_a * m_d,       -2 * m_b * m_d,       -2 * m_c * m_d,       1.0 );
//		return rv;
//	}
//	public edu.cmu.cs.dennisc.math.Matrix4d getReflection() {
//		return getReflection( new edu.cmu.cs.dennisc.math.Matrix4d() );
//	}
	@Override
	public String toString() {
		return "edu.cmu.cs.dennisc.math.Plane[a=" + m_a + ",b=" + m_b + ",c=" + m_c + ",d=" + m_d + "]";
	}
	public static Plane valueOf( String s ) {
		String[] markers = { "edu.cmu.cs.dennisc.math.Plane[a=", ",b=", ",c=", ",d=", "]" };
		double[] values = new double[ markers.length - 1 ];
		for( int i = 0; i < values.length; i++ ) {
			int begin = s.indexOf( markers[ i ] ) + markers[ i ].length();
			int end = s.indexOf( markers[ i + 1 ] );
			values[ i ] = Double.valueOf( s.substring( begin, end ) ).doubleValue();
		}
		return new Plane( values );
	}
}
