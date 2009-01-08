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
public class Visual extends Leaf {
	public final edu.cmu.cs.dennisc.property.InstanceProperty< Appearance > frontFacingAppearance = new edu.cmu.cs.dennisc.property.InstanceProperty< Appearance >( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty< Appearance > backFacingAppearance = new edu.cmu.cs.dennisc.property.InstanceProperty< Appearance >( this, null );
	public final edu.cmu.cs.dennisc.math.property.Matrix3x3Property scale = new edu.cmu.cs.dennisc.math.property.Matrix3x3Property( this, edu.cmu.cs.dennisc.math.Matrix3x3.createIdentity() );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isShowing = new edu.cmu.cs.dennisc.property.BooleanProperty( this, true );
	public final edu.cmu.cs.dennisc.property.CopyableArrayProperty< Geometry > geometries = new edu.cmu.cs.dennisc.property.CopyableArrayProperty< Geometry >( this, new Geometry[ 0 ] ) {
		@Override
		protected Geometry[] createArray( int length ) {
			return new Geometry[ length ];
		}
		@Override
		protected Geometry createCopy( Geometry src ) {
			//todo?
			return src;
		}
	};

	@Override
	protected void actuallyRelease() {
		super.actuallyRelease();
		if( frontFacingAppearance.getValue() != null ) {
			frontFacingAppearance.getValue().release();
		}
		if( backFacingAppearance.getValue() != null ) {
			backFacingAppearance.getValue().release();
		}
		for( Geometry geometry : geometries.getValue() ) {
			geometry.release();
		}
	}

	
	@Deprecated
	public Geometry getGeometry() {
		if( geometries.getValue().length > 0 ) {
			return geometries.getValue()[ 0 ];
		} else {
			return null;
		}
	}
	@Deprecated
	public void setGeometry( Geometry geometry ) {
		geometries.setValue( new Geometry[] { geometry } );
	}

	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv ) {
		if( getGeometry() != null  ) {
			//todo
			getGeometry().getAxisAlignedMinimumBoundingBox( rv );
			rv.scale( scale.getValue() );
		} else {
			rv.setNaN();
		}
		return rv;
	}
	public final edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( new edu.cmu.cs.dennisc.math.AxisAlignedBox() );
	}
	
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere rv ) {
		if( getGeometry() != null  ) {
			//todo
			getGeometry().getBoundingSphere( rv );
			rv.scale( scale.getValue() );
		} else {
			rv.setNaN();
		}
		return rv;
	}
	public final edu.cmu.cs.dennisc.math.Sphere getBoundingSphere() {
		return getBoundingSphere( new edu.cmu.cs.dennisc.math.Sphere() );
	}
	

	//	public void transform( edu.cmu.cs.dennisc.math.Matrix4d trans ) {
	//		Geometry geometry = getGeometry();
	//		if( geometry!=null ) {
	//			geometry.transform( trans );
	//		}
	//	}
}
