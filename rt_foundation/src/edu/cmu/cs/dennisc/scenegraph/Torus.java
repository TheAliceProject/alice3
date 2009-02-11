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
	//todo: rename
//	private enum E {
//		YES( true ), NO( true );
//		private boolean is;
//		E( boolean is ) {
//			this.is = is;
//		}
//		public double get( double yesRadius, double noRadius ) {
//			if( this.is ) {
//				return yesRadius;
//			} else {
//				return noRadius;
//			}
//		}
//	};
	public enum CoordinatePlane {
//		XY( E.YES, E.YES, E.NO ),
//		XZ( E.YES, E.NO,  E.YES ),
//		YZ( E.NO,  E.YES, E.YES );
//		private E x;
//		private E y;
//		private E z;
//		CoordinatePlane( E x, E y, E z ) {
//			this.x = x;
//			this.y = y;
//			this.z = z;
//		}
		XY( true,  true,  false ),
		XZ( true,  false, true ),
		YZ( false, true,  true );
		private boolean isX;
		private boolean isY;
		private boolean isZ;
		CoordinatePlane( boolean isX, boolean isY, boolean isZ ) {
			this.isX = isX;
			this.isY = isY;
			this.isZ = isZ;
		}
		public void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox, double yesRadius, double noRadius ) {
			double x;
			double y;
			double z;
			if( this.isX ) {
				x = yesRadius;
			} else {
				x = noRadius;
			}
			if( this.isY ) {
				y = yesRadius;
			} else {
				y= noRadius;
			}
			if( this.isZ ) {
				z = yesRadius;
			} else {
				z = noRadius;
			}
			boundingBox.setMinimum( -x, -y, -z );
			boundingBox.setMaximum( +x, +y, +z );
		}
	}

	public final edu.cmu.cs.dennisc.property.InstanceProperty< CoordinatePlane > coordinatePlane = new edu.cmu.cs.dennisc.property.InstanceProperty< CoordinatePlane >( this, CoordinatePlane.XZ ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.PropertyOwner owner, CoordinatePlane value ) {
			//todo: check isEqual
			super.setValue( owner, value );
			Torus.this.fireBoundChange();
		};
	};
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
			assert value >= 0.0 : value;
			super.setValue( owner, value );
		}
	};

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		double yesRadius = majorRadius.getValue() + minorRadius.getValue();
		double noRadius = minorRadius.getValue();
		this.coordinatePlane.getValue().updateBoundingBox( boundingBox, yesRadius, noRadius );
	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		double outerRadius = majorRadius.getValue() + minorRadius.getValue();
		boundingSphere.center.set( 0, 0, 0 );
		boundingSphere.radius = outerRadius;
	}
}
