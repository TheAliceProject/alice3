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
public class Torus extends Shape {
	public final BoundDoubleProperty minorRadius = new BoundDoubleProperty( this, 0.1 ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, Double value) {
			assert value >= 0.0;
			super.setValue( owner, value );
		}
	};
	public final BoundDoubleProperty majorRadius = new BoundDoubleProperty( this, 0.9 ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, Double value) {
			assert value >= 0.0;
			super.setValue( owner, value );
		}
	};

	//todo: specifiy which plane (XZ|XY|YZ)

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		double outerRadius = majorRadius.getValue() + minorRadius.getValue();
		boundingBox.setMinimum( -outerRadius, -minorRadius.getValue(), -outerRadius );
		boundingBox.setMaximum( +outerRadius, +minorRadius.getValue(), +outerRadius );
	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		double outerRadius = majorRadius.getValue() + minorRadius.getValue();
		boundingSphere.center.set( 0, 0, 0 );
		boundingSphere.radius = outerRadius;
	}
}
