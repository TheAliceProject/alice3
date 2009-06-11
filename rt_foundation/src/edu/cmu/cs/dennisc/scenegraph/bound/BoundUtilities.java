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
package edu.cmu.cs.dennisc.scenegraph.bound;

/**
 * @author Dennis Cosgrove
 */
public class BoundUtilities {

	//TODO: remove duplicate code, if possible

	public static edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv, edu.cmu.cs.dennisc.scenegraph.Vertex[] va ) {
		edu.cmu.cs.dennisc.math.Point3 min = new edu.cmu.cs.dennisc.math.Point3( +Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE );
		edu.cmu.cs.dennisc.math.Point3 max = new edu.cmu.cs.dennisc.math.Point3( -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE );
		for( edu.cmu.cs.dennisc.scenegraph.Vertex v : va ) {
			min.x = Math.min( min.x, v.position.x );
			min.y = Math.min( min.y, v.position.y );
			min.z = Math.min( min.z, v.position.z );
			max.x = Math.max( max.x, v.position.x );
			max.y = Math.max( max.y, v.position.y );
			max.z = Math.max( max.z, v.position.z );
		}
		if( min.x == +Double.MAX_VALUE ) {
			rv.setNaN();
		} else {
			rv.setMinimum( min );
			rv.setMaximum( max );
		}
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox(edu.cmu.cs.dennisc.math.AxisAlignedBox rv, Iterable<edu.cmu.cs.dennisc.math.Point3> pi) {
		edu.cmu.cs.dennisc.math.Point3 min = new edu.cmu.cs.dennisc.math.Point3( +Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE );
		edu.cmu.cs.dennisc.math.Point3 max = new edu.cmu.cs.dennisc.math.Point3( -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE );
		for (edu.cmu.cs.dennisc.math.Point3 p : pi) {
			min.x = Math.min(min.x, p.x);
			min.y = Math.min(min.y, p.y);
			min.z = Math.min(min.z, p.z);
			max.x = Math.max(max.x, p.x);
			max.y = Math.max(max.y, p.y);
			max.z = Math.max(max.z, p.z);
		}
		if (min.x == +Double.MAX_VALUE) {
			rv.setNaN();
		} else {
			rv.setMinimum(min);
			rv.setMaximum(max);
		}
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv, edu.cmu.cs.dennisc.math.Point3[] pa ) {
		edu.cmu.cs.dennisc.math.Point3 min = new edu.cmu.cs.dennisc.math.Point3( +Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE );
		edu.cmu.cs.dennisc.math.Point3 max = new edu.cmu.cs.dennisc.math.Point3( -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE );
		for( edu.cmu.cs.dennisc.math.Point3 p : pa ) {
			min.x = Math.min( min.x, p.x );
			min.y = Math.min( min.y, p.y );
			min.z = Math.min( min.z, p.z );
			max.x = Math.max( max.x, p.x );
			max.y = Math.max( max.y, p.y );
			max.z = Math.max( max.z, p.z );
		}
		if( min.x == +Double.MAX_VALUE ) {
			rv.setNaN();
		} else {
			rv.setMinimum( min );
			rv.setMaximum( max );
		}
		return rv;
	}

	public static edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere rv, edu.cmu.cs.dennisc.math.Point3[] pa ) {
		final int N = pa.length;
		if( N == 0 ) {
			rv.setNaN();
		} else {
			int indexOfCornerI = -1;
			int indexOfCornerJ = -1;
			double maxDistanceSquared = -Double.MAX_VALUE; // any negative number would do
			for( int i = 0; i < N; i++ ) {
				edu.cmu.cs.dennisc.math.Point3 cornerI = pa[ i ];
				for( int j = i + 1; j < N; j++ ) {
					edu.cmu.cs.dennisc.math.Point3 cornerJ = pa[ j ];
					double distanceSquared = edu.cmu.cs.dennisc.math.Point3.calculateDistanceSquaredBetween( cornerI, cornerJ );
					if( distanceSquared > maxDistanceSquared ) {
						indexOfCornerI = i;
						indexOfCornerJ = j;
					}
				}
			}

			edu.cmu.cs.dennisc.math.Point3 center = new edu.cmu.cs.dennisc.math.Point3( 0, 0, 0 );
			center.add( pa[ indexOfCornerI ] );
			center.add( pa[ indexOfCornerJ ] );
			center.multiply( 0.5 );

			double maxDistanceFromCenterSquared = -Double.MAX_VALUE; // any negative number would do
			for( edu.cmu.cs.dennisc.math.Point3 p : pa ) {
				double distanceSquared = edu.cmu.cs.dennisc.math.Point3.calculateDistanceSquaredBetween( p, center );
				maxDistanceFromCenterSquared = Math.max( maxDistanceFromCenterSquared, distanceSquared );
			}

			double radius = Math.sqrt( maxDistanceFromCenterSquared );
			rv.center.set( center );
			rv.radius = radius;
		}
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere rv, edu.cmu.cs.dennisc.scenegraph.Vertex[] va ) {
		final int N = va.length;
		if( N == 0 ) {
			rv.setNaN();
		} else {
			int indexOfCornerI = -1;
			int indexOfCornerJ = -1;
			double maxDistanceSquared = -Double.MAX_VALUE; // any negative number would do
			for( int i = 0; i < N; i++ ) {
				edu.cmu.cs.dennisc.math.Point3 cornerI = va[ i ].position;
				for( int j = i + 1; j < N; j++ ) {
					edu.cmu.cs.dennisc.math.Point3 cornerJ = va[ j ].position;
					double distanceSquared = edu.cmu.cs.dennisc.math.Point3.calculateDistanceSquaredBetween( cornerI, cornerJ );
					if( distanceSquared > maxDistanceSquared ) {
						indexOfCornerI = i;
						indexOfCornerJ = j;
					}
				}
			}

			edu.cmu.cs.dennisc.math.Point3 center = new edu.cmu.cs.dennisc.math.Point3( 0, 0, 0 );
			center.add( va[ indexOfCornerI ].position );
			center.add( va[ indexOfCornerJ ].position );
			center.multiply( 0.5 );

			double maxDistanceFromCenterSquared = -Double.MAX_VALUE; // any negative number would do
			for( edu.cmu.cs.dennisc.scenegraph.Vertex v : va ) {
				double distanceSquared = edu.cmu.cs.dennisc.math.Point3.calculateDistanceSquaredBetween( v.position, center );
				maxDistanceFromCenterSquared = Math.max( maxDistanceFromCenterSquared, distanceSquared );
			}

			double radius = Math.sqrt( maxDistanceFromCenterSquared );
			rv.center.set( center );
			rv.radius = radius;
		}
		return rv;
	}

	public static edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere rv, java.util.Vector< edu.cmu.cs.dennisc.math.Point3 > pv ) {
		final int N = pv.size();
		if( N == 0 ) {
			rv.setNaN();
		} else {
//			int indexOfCornerI = -1;
//			int indexOfCornerJ = -1;
//			double maxDistanceSquared = -Double.MAX_VALUE; // any negative number would do
//			for( int i = 0; i < N; i++ ) {
//				edu.cmu.cs.dennisc.math.Point3 cornerI = pv.elementAt( i );
//				for( int j = i + 1; j < N; j++ ) {
//					edu.cmu.cs.dennisc.math.Point3 cornerJ = pv.elementAt( j );
//					double distanceSquared = edu.cmu.cs.dennisc.math.Point3.calculateDistanceSquaredBetween( cornerI, cornerJ );
//					if( distanceSquared > maxDistanceSquared ) {
//						indexOfCornerI = i;
//						indexOfCornerJ = j;
//						maxDistanceSquared = distanceSquared;
//					}
//				}
//			}

			//rv.center.setToAddition( pv.elementAt( indexOfCornerI ), pv.elementAt( indexOfCornerJ ) );
			//rv.center.multiply( 0.5 );
			//rv.radius = Math.sqrt( maxDistanceSquared ) * 0.5;

			edu.cmu.cs.dennisc.math.AxisAlignedBox bb = new edu.cmu.cs.dennisc.math.AxisAlignedBox();
			getBoundingBox( bb, pv );
			bb.getCenter( rv.center );
			
			
			double maxDistanceFromCenterSquared = -1.0; // any negative number would do
			for( edu.cmu.cs.dennisc.math.Point3 p : pv ) {
				double distanceSquared = edu.cmu.cs.dennisc.math.Point3.calculateDistanceSquaredBetween( p, rv.center );
				maxDistanceFromCenterSquared = Math.max( maxDistanceFromCenterSquared, distanceSquared );
			}

			rv.radius = Math.sqrt( maxDistanceFromCenterSquared );
		}
		return rv;
	}
}
