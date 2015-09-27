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
public class Torus extends Shape {
	public static enum CoordinatePlane {
		XY( true, true, false ),
		XZ( true, false, true ),
		YZ( false, true, true );
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
				y = noRadius;
			}
			if( this.isZ ) {
				z = yesRadius;
			} else {
				z = noRadius;
			}
			boundingBox.setMinimum( -x, -y, -z );
			boundingBox.setMaximum( +x, +y, +z );
		}

		private final boolean isX;
		private final boolean isY;
		private final boolean isZ;
	}

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

	public final edu.cmu.cs.dennisc.property.InstanceProperty<CoordinatePlane> coordinatePlane = new edu.cmu.cs.dennisc.property.InstanceProperty<CoordinatePlane>( this, CoordinatePlane.XZ ) {
		@Override
		public void setValue( CoordinatePlane value ) {
			//todo: check isEqual
			Torus.this.markBoundsDirty();
			super.setValue( value );
			Torus.this.fireBoundChanged();
		};
	};
	public final BoundDoubleProperty minorRadius = new BoundDoubleProperty( this, 0.1 ) {
		@Override
		public void setValue( Double value ) {
			assert value >= 0.0 : value;
			super.setValue( value );
		}
	};
	public final BoundDoubleProperty majorRadius = new BoundDoubleProperty( this, 0.9 ) {
		@Override
		public void setValue( Double value ) {
			assert value >= 0.0 : value;
			super.setValue( value );
		}
	};
}
