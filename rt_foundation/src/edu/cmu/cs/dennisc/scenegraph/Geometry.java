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

import edu.cmu.cs.dennisc.scenegraph.event.BoundListener;

/**
 * @author Dennis Cosgrove
 */
public abstract class Geometry extends Element {
	private edu.cmu.cs.dennisc.math.AxisAlignedBox m_boundingBox = new edu.cmu.cs.dennisc.math.AxisAlignedBox();
	private edu.cmu.cs.dennisc.math.Sphere m_boundingSphere = new edu.cmu.cs.dennisc.math.Sphere();
	private java.util.Vector<BoundListener> m_boundObservers = new java.util.Vector<BoundListener>();

	protected abstract void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox );
	protected abstract void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere );
	protected abstract void updatePlane( edu.cmu.cs.dennisc.math.Vector3 forward, edu.cmu.cs.dennisc.math.Vector3 upGuide, edu.cmu.cs.dennisc.math.Point3 translation );

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getPlane( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		edu.cmu.cs.dennisc.math.Vector3 forward = new edu.cmu.cs.dennisc.math.Vector3();
		edu.cmu.cs.dennisc.math.Vector3 upGuide = new edu.cmu.cs.dennisc.math.Vector3();
		updatePlane( forward, upGuide, rv.translation );
		rv.orientation.setValue( new edu.cmu.cs.dennisc.math.ForwardAndUpGuide( forward, upGuide ) );
		return rv;
	}
	
	public abstract void transform( edu.cmu.cs.dennisc.math.AbstractMatrix4x4 trans );
	
	//todo: better name
	public class BoundDoubleProperty extends edu.cmu.cs.dennisc.property.DoubleProperty {
		public BoundDoubleProperty( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner, Double value ) {
			super( owner, value );
		}
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.PropertyOwner owner, Double value ) {
			//todo: check isEqual
			super.setValue( owner, value );
			Geometry.this.fireBoundChange();
		};
	}

	public final edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		if( m_boundingBox.isNaN() ) {
			updateBoundingBox( m_boundingBox );
		}
		boundingBox.set( m_boundingBox );
		return boundingBox;
	}
	public final edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( new edu.cmu.cs.dennisc.math.AxisAlignedBox() );
	}
	public final edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		if( m_boundingSphere.isNaN() ) {
			updateBoundingSphere( m_boundingSphere );
		}
		boundingSphere.set( m_boundingSphere );
		return boundingSphere;
	}
	public final edu.cmu.cs.dennisc.math.Sphere getBoundingSphere() {
		return getBoundingSphere( new edu.cmu.cs.dennisc.math.Sphere() );
	}

	public void addBoundObserver( BoundListener boundObserver ) {
		m_boundObservers.addElement( boundObserver );
	}
	public void removeBoundObserver( BoundListener boundObserver ) {
		m_boundObservers.removeElement( boundObserver );
	}
	public Iterable< BoundListener > accessBoundObservers() {
		return m_boundObservers;
	}
	protected void fireBoundChange() {
		m_boundingBox.setNaN();
		m_boundingSphere.setNaN();
		edu.cmu.cs.dennisc.scenegraph.event.BoundEvent e = new edu.cmu.cs.dennisc.scenegraph.event.BoundEvent( this );
		for( BoundListener boundObserver : m_boundObservers ) {
			boundObserver.boundChanged( e );
		}
	}
}
