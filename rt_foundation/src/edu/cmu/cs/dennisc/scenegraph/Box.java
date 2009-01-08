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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public class Box extends Shape {
	public final BoundDoubleProperty xMinimum = new BoundDoubleProperty( this, -0.5 );
	public final BoundDoubleProperty xMaximum = new BoundDoubleProperty( this, +0.5 );
	public final BoundDoubleProperty yMinimum = new BoundDoubleProperty( this, -0.5 );
	public final BoundDoubleProperty yMaximum = new BoundDoubleProperty( this, +0.5 );
	public final BoundDoubleProperty zMinimum = new BoundDoubleProperty( this, -0.5 );
	public final BoundDoubleProperty zMaximum = new BoundDoubleProperty( this, +0.5 );

	public edu.cmu.cs.dennisc.math.Point3 getMinimum( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( xMinimum.getValue(), yMinimum.getValue(), zMinimum.getValue() );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Point3 getMinimum() {
		return getMinimum( new edu.cmu.cs.dennisc.math.Point3() );
	}
	public void setMinimum( double x, double y, double z ) {
		xMinimum.setValue( x );
		yMinimum.setValue( y );
		zMinimum.setValue( z );
	}
	public void setMinimum( edu.cmu.cs.dennisc.math.Point3 minimum ) {
		setMinimum( minimum.x, minimum.y, minimum.z );
	}

	public edu.cmu.cs.dennisc.math.Point3 getMaximum( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( xMaximum.getValue(), yMaximum.getValue(), zMaximum.getValue() );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Point3 getMaximum() {
		return getMaximum( new edu.cmu.cs.dennisc.math.Point3() );
	}
	public void setMaximum( double x, double y, double z ) {
		xMaximum.setValue( x );
		yMaximum.setValue( y );
		zMaximum.setValue( z );
	}
	public void setMaximum( edu.cmu.cs.dennisc.math.Point3 maximum ) {
		setMaximum( maximum.x, maximum.y, maximum.z );
	}

	public void set( edu.cmu.cs.dennisc.math.Point3 minimum, edu.cmu.cs.dennisc.math.Point3 maximum ) {
		setMinimum( minimum );
		setMaximum( maximum );
	}
	public void set( edu.cmu.cs.dennisc.math.AxisAlignedBox box ) {
		setMinimum( box.getXMinimum(), box.getYMinimum(), box.getZMinimum() );
		setMaximum( box.getXMaximum(), box.getYMaximum(), box.getZMaximum() );
	}

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		boundingBox.setMinimum( xMinimum.getValue(), yMinimum.getValue(), zMinimum.getValue() );
		boundingBox.setMaximum( xMaximum.getValue(), yMaximum.getValue(), zMaximum.getValue() );
	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		double xCenter = (xMinimum.getValue() + xMaximum.getValue()) * 0.5;
		double yCenter = (yMinimum.getValue() + yMaximum.getValue()) * 0.5;
		double zCenter = (zMinimum.getValue() + zMaximum.getValue()) * 0.5;
		boundingSphere.center.set( xCenter, yCenter, zCenter );
		double width = xMaximum.getValue() - xMinimum.getValue();
		double height = yMaximum.getValue() - yMinimum.getValue();
		double depth = zMaximum.getValue() - zMinimum.getValue();
		boundingSphere.radius = Math.max( Math.max( width, height ), depth ) * 0.5;
	}
}
