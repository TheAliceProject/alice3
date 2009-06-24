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

//todo: implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable
/**
 * @author Dennis Cosgrove
 */
public class Sphere {
	public final edu.cmu.cs.dennisc.math.Point3 center = edu.cmu.cs.dennisc.math.Point3.createNaN();
	public double radius = Double.NaN;
	public Sphere() {
		setNaN();
	}
	public Sphere( edu.cmu.cs.dennisc.math.Point3 center, double radius ) {
		this.center.set( center );
		this.radius = radius;
	}
	public Sphere( Sphere other ) {
		set( other );
	}
	@Override
	public boolean equals( Object o ) {
		if( o==this ) return true;
		if( o!=null && o instanceof Sphere ) {
			Sphere s = (Sphere)o;
			if( this.isNaN() ) {
				return s.isNaN();
			} else {
				return this.center.equals( s.center ) && this.radius==s.radius;
			}
		} else {
			return false;
		}
	}
	
	public void setNaN() {
		center.setNaN();
		radius = Double.NaN;
	}

	public void set( Sphere other ) {
		if( other != null ) {
			this.center.set( other.center );
			this.radius = other.radius;
		} else {
			setNaN();
		}
	}

	public boolean isNaN() {
		return this.center.isNaN() || Double.isNaN( radius ); 
	}

	public double getVolume() {
		return 4.0/3.0 * Math.PI * radius * radius * radius;
	}
	
/*
	public void union( Sphere s ) {
		if( s!=null && s.m_center!=null ) {
			if( m_center!=null ) {
				edu.cmu.cs.dennisc.math.Point3d diagonal = new edu.cmu.cs.dennisc.math.Point3d( m_center );
				diagonal.sub( s.m_center );
				diagonal.normalize();
				edu.cmu.cs.dennisc.math.Point3d[] points = new edu.cmu.cs.dennisc.math.Point3d[4];
				points[0] = MathUtilities.add( m_center, MathUtilities.multiply( diagonal, m_radius ) );
				points[1] = MathUtilities.subtract( m_center, MathUtilities.multiply( diagonal, m_radius ) );
				points[2] = MathUtilities.add( s.m_center, MathUtilities.multiply( diagonal, s.m_radius ) );
				points[3] = MathUtilities.subtract( s.m_center, MathUtilities.multiply( diagonal, s.m_radius ) );
				double maxDistanceSquared = 0;
				int maxDistanceI = 0;
				int maxDistanceJ = 1;
				for( int i=0; i<4; i++ ) {
					for( int j=i+1; j<4; j++ ) {
						double d2 = MathUtilities.subtract( points[i], points[j] ).lengthSquared();
						if( d2>maxDistanceSquared ) {
							maxDistanceSquared = d2;
							maxDistanceI = i;
							maxDistanceJ = j;
						}
					}
				}
				m_center = MathUtilities.divide( MathUtilities.add( points[maxDistanceI], points[maxDistanceJ] ), 2 );
				m_radius = Math.sqrt( maxDistanceSquared )/2.0;
			} else {
				m_center = s.getCenter();
				m_radius = s.getRadius();
			}
		}
	}
	public void transform( edu.cmu.cs.dennisc.math.Matrix4d m ) {
		if( m_center!=null && !Double.isNaN( m_radius ) ) {
			//todo... account for scale
			m_center.add( new edu.cmu.cs.dennisc.math.Point3d( m.right.w, m.up.w, m.backward.w ) );
		}
	}

	public void scale( edu.cmu.cs.dennisc.math.Matrix3d s ) {
		if( s!=null ) {
			if( m_center!=null ) {
				m_center = MathUtilities.multiply( s, m_center );
			}
			//Vector3 v = s.getScaledSpace();
			//double scale = Math.max( Math.max( v.x, v.y ), v.z );
			//m_radius *= scale;
			m_radius *= s.getScale();
		}
	}
*/
//	public void transform( edu.cmu.cs.dennisc.math.Matrix4d m ) {
//		//todo?
//		m_center.x += m.translation.x;
//		m_center.y += m.translation.y;
//		m_center.z += m.translation.z;
//	}

	public void scale( edu.cmu.cs.dennisc.math.Matrix3x3 m ) {
		//todo?
		
		//todo: test
		m.transform( this.center );
		
		double xScaleSquared = Tuple3.calculateMagnitudeSquared( m.right.x, m.right.y, m.right.z );
		double yScaleSquared = Tuple3.calculateMagnitudeSquared( m.up.x, m.up.y, m.up.z );
		double zScaleSquared = Tuple3.calculateMagnitudeSquared( m.backward.x, m.backward.y, m.backward.z );
		
		double scaleSquared = Math.max( xScaleSquared, Math.max( yScaleSquared, zScaleSquared ) );
		
		this.radius *= Math.sqrt( scaleSquared );
	}
	
	@Override
	public String toString() {
		return "edu.cmu.cs.dennisc.math.Sphere[radius="+this.radius+",center="+this.center+"]";
	}
}

//public class SphereD implements java.io.Serializable {
//	protected double m_radius = Double.NaN;
//	protected edu.cmu.cs.dennisc.math.PointD3 m_center = new edu.cmu.cs.dennisc.math.PointD3();
//	public SphereD() {
//		setNaN();
//	}
//	public SphereD( edu.cmu.cs.dennisc.math.PointD3 center, double radius ) {
//		setCenter( center );
//		setRadius( radius );
//	}
//	public SphereD( double x, double y, double z, double radius ) {
//		setCenter( x, y, z );
//		setRadius( radius );
//	}
//	public SphereD( SphereD other ) {
//		set( other );
//	}
//	@Override
//	public boolean equals( Object o ) {
//		if( o==this ) return true;
//		if( o!=null && o instanceof SphereD ) {
//			SphereD s = (SphereD)o;
//			return m_center.equals( s.m_center ) && m_radius==s.m_radius;
//		} else {
//			return false;
//		}
//	}
//	
//	public void setNaN() {
//		setCenter( null );
//		setRadius( Double.NaN );
//	}
//
//	public void set( SphereD other ) {
//		if( other != null ) {
//			setCenter( other.m_center );
//			setRadius( other.m_radius );
//		} else {
//			setCenter( null );
//			setRadius( Double.NaN );
//		}
//	}
//
//	public double getRadius() {
//		return m_radius;
//	}
//	public void setRadius( double radius ) {
//		m_radius = radius;
//	}
//	public edu.cmu.cs.dennisc.math.PointD3 getCenter( edu.cmu.cs.dennisc.math.PointD3 rv ) {
//		rv.set( m_center );
//		return rv;
//	}
//	public edu.cmu.cs.dennisc.math.PointD3 getCenter() {
//		return getCenter( new edu.cmu.cs.dennisc.math.PointD3() );
//	}
//	public void setCenter( edu.cmu.cs.dennisc.math.PointD3 center ) {
//		if( center == null ) {
//			m_center.set( Double.NaN, Double.NaN, Double.NaN );
//		} else {
//			m_center.set( center );
//		}
//	}
//	public void setCenter( double x, double y, double z ) {
//		m_center.set( x, y, z );
//	}
//
//	public boolean isNaN() {
//		return m_center.isNaN() || Double.isNaN( m_radius ); 
//	}
//
//	public double getVolume() {
//		return 4.0/3.0 * Math.PI * m_radius * m_radius * m_radius;
//	}
//	
///*
//	public void union( Sphere s ) {
//		if( s!=null && s.m_center!=null ) {
//			if( m_center!=null ) {
//				edu.cmu.cs.dennisc.math.Point3d diagonal = new edu.cmu.cs.dennisc.math.Point3d( m_center );
//				diagonal.sub( s.m_center );
//				diagonal.normalize();
//				edu.cmu.cs.dennisc.math.Point3d[] points = new edu.cmu.cs.dennisc.math.Point3d[4];
//				points[0] = MathUtilities.add( m_center, MathUtilities.multiply( diagonal, m_radius ) );
//				points[1] = MathUtilities.subtract( m_center, MathUtilities.multiply( diagonal, m_radius ) );
//				points[2] = MathUtilities.add( s.m_center, MathUtilities.multiply( diagonal, s.m_radius ) );
//				points[3] = MathUtilities.subtract( s.m_center, MathUtilities.multiply( diagonal, s.m_radius ) );
//				double maxDistanceSquared = 0;
//				int maxDistanceI = 0;
//				int maxDistanceJ = 1;
//				for( int i=0; i<4; i++ ) {
//					for( int j=i+1; j<4; j++ ) {
//						double d2 = MathUtilities.subtract( points[i], points[j] ).lengthSquared();
//						if( d2>maxDistanceSquared ) {
//							maxDistanceSquared = d2;
//							maxDistanceI = i;
//							maxDistanceJ = j;
//						}
//					}
//				}
//				m_center = MathUtilities.divide( MathUtilities.add( points[maxDistanceI], points[maxDistanceJ] ), 2 );
//				m_radius = Math.sqrt( maxDistanceSquared )/2.0;
//			} else {
//				m_center = s.getCenter();
//				m_radius = s.getRadius();
//			}
//		}
//	}
//	public void transform( edu.cmu.cs.dennisc.math.Matrix4d m ) {
//		if( m_center!=null && !Double.isNaN( m_radius ) ) {
//			//todo... account for scale
//			m_center.add( new edu.cmu.cs.dennisc.math.Point3d( m.right.w, m.up.w, m.backward.w ) );
//		}
//	}
//
//	public void scale( edu.cmu.cs.dennisc.math.Matrix3d s ) {
//		if( s!=null ) {
//			if( m_center!=null ) {
//				m_center = MathUtilities.multiply( s, m_center );
//			}
//			//Vector3 v = s.getScaledSpace();
//			//double scale = Math.max( Math.max( v.x, v.y ), v.z );
//			//m_radius *= scale;
//			m_radius *= s.getScale();
//		}
//	}
//*/
////	public void transform( edu.cmu.cs.dennisc.math.Matrix4d m ) {
////		//todo?
////		m_center.x += m.translation.x;
////		m_center.y += m.translation.y;
////		m_center.z += m.translation.z;
////	}
//
//	public void scale( edu.cmu.cs.dennisc.math.MatrixD3x3 m ) {
//		//todo?
//		
//		//todo: test
//		m.transform( m_center );
//		
//		double xScaleSquared = TupleD3.calculateMagnitudeSquared( m.right.x, m.right.y, m.right.z );
//		double yScaleSquared = TupleD3.calculateMagnitudeSquared( m.up.x, m.up.y, m.up.z );
//		double zScaleSquared = TupleD3.calculateMagnitudeSquared( m.backward.x, m.backward.y, m.backward.z );
//		
//		double scaleSquared = Math.max( xScaleSquared, Math.max( yScaleSquared, zScaleSquared ) );
//		
//		m_radius *= Math.sqrt( scaleSquared );
//	}
//	
//	@Override
//	public String toString() {
//		return "edu.cmu.cs.dennisc.math.Sphere[radius="+m_radius+",center="+m_center+"]";
//	}
//}
