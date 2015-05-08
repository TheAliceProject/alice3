/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public class Visual extends Leaf {
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

	public Geometry getGeometryAt( int index ) {
		return this.geometries.getValue()[ index ];
	}

	public int getGeometryCount() {
		return this.geometries.getValue().length;
	}

	@Deprecated
	public Geometry getGeometry() {
		Geometry[] array = this.geometries.getValue();
		if( ( array != null ) && ( array.length > 0 ) ) {
			return array[ 0 ];
		} else {
			return null;
		}
	}

	@Deprecated
	public void setGeometry( Geometry geometry ) {
		Geometry[] geometries;
		if( geometry != null ) {
			geometries = new Geometry[] { geometry };
		} else {
			geometries = new Geometry[] {};
		}
		this.geometries.setValue( geometries );
	}

	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv ) {
		if( getGeometry() != null ) {
			//todo
			getGeometry().getAxisAlignedMinimumBoundingBox( rv );
			rv.scale( scale.getValue() );
		} else {
			rv.setNaN();
		}
		if( rv.isNaN() || rv.getMaximum().isNaN() || rv.getMaximum().isNaN() ) {
			System.err.append( "NaN" );
		}
		return rv;
	}

	public final edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( new edu.cmu.cs.dennisc.math.AxisAlignedBox() );
	}

	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere rv ) {
		if( getGeometry() != null ) {
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

	public final edu.cmu.cs.dennisc.property.InstanceProperty<Appearance> frontFacingAppearance = new edu.cmu.cs.dennisc.property.InstanceProperty<Appearance>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<Appearance> backFacingAppearance = new edu.cmu.cs.dennisc.property.InstanceProperty<Appearance>( this, null );
	public final edu.cmu.cs.dennisc.math.property.Matrix3x3Property scale = new edu.cmu.cs.dennisc.math.property.Matrix3x3Property( this, edu.cmu.cs.dennisc.math.Matrix3x3.createIdentity() );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isShowing = new edu.cmu.cs.dennisc.property.BooleanProperty( this, true );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isPickable = new edu.cmu.cs.dennisc.property.BooleanProperty( this, true );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<Silhouette> silouette = new edu.cmu.cs.dennisc.property.InstanceProperty<Silhouette>( this, null );
	public final edu.cmu.cs.dennisc.property.CopyableArrayProperty<Geometry> geometries = new edu.cmu.cs.dennisc.property.CopyableArrayProperty<Geometry>( this, new Geometry[ 0 ] ) {
		@Override
		protected Geometry[] createArray( int length ) {
			return new Geometry[ length ];
		}

		@Override
		protected Geometry createCopy( Geometry src ) {
			//todo?
			return src;
		}

		@Override
		public void setValue( edu.cmu.cs.dennisc.scenegraph.Geometry[] value ) {
			for( Geometry geometry : value ) {
				assert geometry != null : this;
			}
			super.setValue( value );
		}
	};
}
