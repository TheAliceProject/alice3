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
public class Disc extends Shape {
	public final edu.cmu.cs.dennisc.property.DoubleProperty innerRadius = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 0.0 ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, Double value) {
			assert value >= 0.0;
			super.setValue( owner, value );
		}
	};
	public final BoundDoubleProperty outerRadius = new BoundDoubleProperty( this, 1.0 ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, Double value) {
			assert value >= 0.0;
			super.setValue( owner, value );
		}
	};

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		double d = outerRadius.getValue();
		boundingBox.setMinimum( -d, -d, 0 );
		boundingBox.setMaximum( d, d, 0 );
	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		boundingSphere.center.set( 0, 0, 0 );
		boundingSphere.radius = outerRadius.getValue();
	}
}
