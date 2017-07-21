/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public abstract class Geometry extends Element {
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
		public void setValue( Double value ) {
			//todo: check isEqual
			Geometry.this.markBoundsDirty();
			super.setValue( value );
			Geometry.this.fireBoundChanged();
		};
	}

	public final edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		if( this.boundingBox.isNaN() )
		{
			updateBoundingBox( this.boundingBox );
		}
		boundingBox.set( this.boundingBox );
		return boundingBox;
	}

	public final edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( new edu.cmu.cs.dennisc.math.AxisAlignedBox() );
	}

	public final edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		if( this.boundingSphere.isNaN() ) {
			updateBoundingSphere( this.boundingSphere );
		}
		boundingSphere.set( this.boundingSphere );
		return boundingSphere;
	}

	public final edu.cmu.cs.dennisc.math.Sphere getBoundingSphere() {
		return getBoundingSphere( new edu.cmu.cs.dennisc.math.Sphere() );
	}

	public void addBoundListener( edu.cmu.cs.dennisc.scenegraph.event.BoundListener boundListener ) {
		this.boundListeners.add( boundListener );
	}

	public void removeBoundListener( edu.cmu.cs.dennisc.scenegraph.event.BoundListener boundListener ) {
		this.boundListeners.remove( boundListener );
	}

	public java.util.Collection<edu.cmu.cs.dennisc.scenegraph.event.BoundListener> getBoundListeners() {
		return java.util.Collections.unmodifiableCollection( this.boundListeners );
	}

	protected void markBoundsDirty() {
		this.boundingBox.setNaN();
		this.boundingSphere.setNaN();
	}

	protected void fireBoundChanged() {
		edu.cmu.cs.dennisc.scenegraph.event.BoundEvent e = new edu.cmu.cs.dennisc.scenegraph.event.BoundEvent( this );
		for( edu.cmu.cs.dennisc.scenegraph.event.BoundListener boundListener : this.boundListeners ) {
			boundListener.boundChanged( e );
		}
	}

	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.event.BoundListener> boundListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox = new edu.cmu.cs.dennisc.math.AxisAlignedBox();
	private final edu.cmu.cs.dennisc.math.Sphere boundingSphere = new edu.cmu.cs.dennisc.math.Sphere();

}
