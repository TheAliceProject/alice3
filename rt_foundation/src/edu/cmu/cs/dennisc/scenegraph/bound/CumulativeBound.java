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
public class CumulativeBound {
	private java.util.Vector< edu.cmu.cs.dennisc.math.Point3 > m_transformedPoints = new java.util.Vector< edu.cmu.cs.dennisc.math.Point3 >();
//	public CumulativeBound() {
//	}
//	public CumulativeBound( edu.cmu.cs.dennisc.scenegraph.Composite sgRoot, final edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
//		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : edu.cmu.cs.dennisc.pattern.VisitUtilities.getAll( sgRoot, edu.cmu.cs.dennisc.scenegraph.Visual.class ) ) {
//			if( sgVisual.isShowing.getValue() ) {
//				add( sgVisual, sgVisual.getTransformation( asSeenBy ) );
//				//add( sgVisual, asSeenBy.getTransformation( sgVisual ) );
//			}
//		}
//	}
	private void addPoint( edu.cmu.cs.dennisc.math.Point3 p, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		assert p.isNaN() == false;
		assert trans.isNaN() == false;
		trans.transform( p );
		m_transformedPoints.addElement( p );
	}

	public void add( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		edu.cmu.cs.dennisc.math.AxisAlignedBox box = sgVisual.getAxisAlignedMinimumBoundingBox();
		if( box.isNaN() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.math.Hexahedron hexahedron = box.getHexahedron();
			for( int i=0; i<8; i++ ) {
				addPoint( hexahedron.getPointAt( i ), trans );
			}
		}
	}
	public void addOrigin( edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		addPoint( edu.cmu.cs.dennisc.math.Point3.createZero(), trans );
	}

	
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere rv ) {
		return BoundUtilities.getBoundingSphere( rv, m_transformedPoints );
	}
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere() {
		return getBoundingSphere( new edu.cmu.cs.dennisc.math.Sphere() );
	}
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv ) {
		return BoundUtilities.getBoundingBox( rv, m_transformedPoints );
	}
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox() {
		return getBoundingBox( new edu.cmu.cs.dennisc.math.AxisAlignedBox() );
	}
}
