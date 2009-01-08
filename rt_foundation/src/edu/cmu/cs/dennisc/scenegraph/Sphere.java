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
public class Sphere extends Shape {
	public final BoundDoubleProperty radius = new BoundDoubleProperty( this, 0.5 ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, Double value) {
			assert value >= 0.0;
			super.setValue( owner, value );
		}
	};
	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		double d = radius.getValue();
		boundingBox.setMinimum( -d, -d, -d );
		boundingBox.setMaximum( d, d, d );
	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		boundingSphere.center.set( 0, 0, 0 );
		boundingSphere.radius = radius.getValue();
	}
}
