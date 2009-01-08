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
public class ClippedPlane implements java.io.Serializable {
	private Point3[] m_points = { 
			new Point3(), 
			new Point3(), 
			new Point3(),
			new Point3() 
	};
	private Vector3 m_normal = new Vector3(); 
	public ClippedPlane() {
		setNaN();
	}

	public ClippedPlane( Point3[] points, Vector3 normal ) {
		set( points, normal );
	}

	public ClippedPlane( ClippedPlane other ) {
		set( other );
	}

	private boolean isEqual( ClippedPlane other ) {
		for( int i = 0; i < m_points.length; i++ ) {
			if( m_points[ i ].equals( other.m_points[ i ] ) ) {
				// pass
			} else {
				return false;
			}
		}
		if( m_normal.equals( other.m_normal ) ) {
			// pass
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean equals( Object other ) {
		if( other == this ) {
			return true;
		}
		if( other instanceof Frustum ) {
			return isEqual( (ClippedPlane)other );
		} else {
			return false;
		}
	}

	public void set( Point3[] points, Vector3 normal ) {
		for( int i = 0; i < m_points.length; i++ ) {
			m_points[ i ].set( points[ i ] );
		}
		m_normal.set( normal );
	}

	public void set( ClippedPlane other ) {
		if( other != null ) {
			set( other.m_points, other.m_normal );
		} else {
			setNaN();
		}
	}

	public void setNaN() {
		for( Point3 point : m_points ) {
			point.setNaN();
		}
		m_normal.setNaN();
	}
	public boolean isNaN() {
		for( Point3 point : m_points ) {
			if( point.isNaN() ) {
				return true;
			}
		}
		if( m_normal.isNaN() ) {
			return true;
		}
		return false;
	}
	public void transform( AbstractMatrix4x4 m ) {
		for( Point3 point : m_points ) {
			m.transform( point );
		}
		m.transform( m_normal );
	}
	
	public Plane getPlane( Plane rv ) {
		rv.set( m_points[ 0 ], m_normal );
		return rv;
	}
	public Plane getPlane() {
		return getPlane( new Plane() );
	}
}
