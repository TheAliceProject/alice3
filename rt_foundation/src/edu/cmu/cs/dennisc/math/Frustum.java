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
public class Frustum extends Hexahedron {
	public Frustum() {
		super();
	}
	public Frustum( Point3[] points, Vector3[] normals ) {
		super( points, normals );
	}
	public Frustum( Frustum other ) {
		super( other );
	}
	public Frustum( Matrix4x4 projection ) {
		double xMin = -1.0;
		double xMax = +1.0;
		double yMin = -1.0;
		double yMax = +1.0;
		double zMin = -1.0;
		double zMax = +1.0;

		Point3[] points = { 
				new Point3( xMin, yMin, zMin ), 
				new Point3( xMax, yMin, zMin ), 
				new Point3( xMin, yMax, zMin ),
				new Point3( xMax, yMax, zMin ), 
				new Point3( xMin, yMin, zMax ), 
				new Point3( xMax, yMin, zMax ), 
				new Point3( xMin, yMax, zMax ), 
				new Point3( xMax, yMax, zMax ) 
		};
		Vector3[] normals = { 
				new Vector3( +1, 0, 0 ), 
				new Vector3( -1, 0, 0 ), 
				new Vector3( 0, +1, 0 ),
				new Vector3( 0, -1, 0 ), 
				new Vector3( 0, 0, +1 ), 
				new Vector3( 0, 0, -1 ) 
		};
		
		set( points, normals );
		transform( projection );
	}
}
