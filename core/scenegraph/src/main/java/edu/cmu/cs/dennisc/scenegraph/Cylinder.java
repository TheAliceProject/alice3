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
public class Cylinder extends Shape {
	public static enum OriginAlignment {
		TOP,
		CENTER,
		BOTTOM
	}

	public static enum BottomToTopAxis {
		POSITIVE_X( +1, 0, 0 ),
		POSITIVE_Y( 0, +1, 0 ),
		POSITIVE_Z( 0, 0, +1 ),
		NEGATIVE_X( -1, 0, 0 ),
		NEGATIVE_Y( 0, -1, 0 ),
		NEGATIVE_Z( 0, 0, -1 );

		BottomToTopAxis( double x, double y, double z ) {
			this.axis.set( x, y, z );
		}

		public edu.cmu.cs.dennisc.math.Vector3 accessVector() {
			return this.axis;
		}

		public edu.cmu.cs.dennisc.math.Vector3 getVector( edu.cmu.cs.dennisc.math.Vector3 rv ) {
			rv.set( this.axis );
			return rv;
		}

		public edu.cmu.cs.dennisc.math.Vector3 getVector() {
			return getVector( new edu.cmu.cs.dennisc.math.Vector3() );
		}

		public boolean isPositive() {
			return ( this.axis.x > 0 ) || ( this.axis.y > 0 ) || ( this.axis.z > 0 );
		}

		private final edu.cmu.cs.dennisc.math.Vector3 axis = new edu.cmu.cs.dennisc.math.Vector3();
	}

	public double getActualTopRadius() {
		if( Double.isNaN( topRadius.getValue() ) ) {
			return bottomRadius.getValue();
		} else {
			return topRadius.getValue();
		}
	}

	private double getMaxRadius() {
		if( Double.isNaN( topRadius.getValue() ) ) {
			return bottomRadius.getValue();
		} else {
			return Math.max( bottomRadius.getValue(), topRadius.getValue() );
		}
	}

	private double getTop() {
		OriginAlignment originAlignment = this.originAlignment.getValue();
		if( originAlignment == OriginAlignment.BOTTOM ) {
			return length.getValue();
		} else if( originAlignment == OriginAlignment.CENTER ) {
			return length.getValue() * 0.5;
		} else if( originAlignment == OriginAlignment.TOP ) {
			return 0;
		} else {
			throw new RuntimeException();
		}
	}

	private double getBottom() {
		OriginAlignment originAlignment = this.originAlignment.getValue();
		if( originAlignment == OriginAlignment.BOTTOM ) {
			return 0;
		} else if( originAlignment == OriginAlignment.CENTER ) {
			return -length.getValue() * 0.5;
		} else if( originAlignment == OriginAlignment.TOP ) {
			return -length.getValue();
		} else {
			throw new RuntimeException();
		}
	}

	private double getCenter() {
		OriginAlignment originAlignment = this.originAlignment.getValue();
		if( originAlignment == OriginAlignment.BOTTOM ) {
			return length.getValue() * 0.5;
		} else if( originAlignment == OriginAlignment.CENTER ) {
			return 0;
		} else if( originAlignment == OriginAlignment.TOP ) {
			return -length.getValue() * 0.5;
		} else {
			throw new RuntimeException();
		}
	}

	public edu.cmu.cs.dennisc.math.Point3 getCenterOfTop( edu.cmu.cs.dennisc.math.Point3 rv ) {
		double top = getTop();
		BottomToTopAxis bottomToTopAxis = this.bottomToTopAxis.getValue();
		if( bottomToTopAxis == BottomToTopAxis.POSITIVE_X ) {
			rv.set( top, 0, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.POSITIVE_Y ) {
			rv.set( 0, top, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.POSITIVE_Z ) {
			rv.set( 0, 0, top );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_X ) {
			rv.set( -top, 0, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_Y ) {
			rv.set( 0, -top, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_Z ) {
			rv.set( 0, 0, -top );
		} else {
			throw new RuntimeException();
		}
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Point3 getCenterOfTop() {
		return getCenterOfTop( new edu.cmu.cs.dennisc.math.Point3() );
	}

	public edu.cmu.cs.dennisc.math.Point3 getCenterOfBottom( edu.cmu.cs.dennisc.math.Point3 rv ) {
		double bottom = getBottom();
		BottomToTopAxis bottomToTopAxis = this.bottomToTopAxis.getValue();
		if( bottomToTopAxis == BottomToTopAxis.POSITIVE_X ) {
			rv.set( bottom, 0, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.POSITIVE_Y ) {
			rv.set( 0, bottom, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.POSITIVE_Z ) {
			rv.set( 0, 0, bottom );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_X ) {
			rv.set( -bottom, 0, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_Y ) {
			rv.set( 0, -bottom, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_Z ) {
			rv.set( 0, 0, -bottom );
		} else {
			throw new RuntimeException();
		}
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Point3 getCenterOfBottom() {
		return getCenterOfBottom( new edu.cmu.cs.dennisc.math.Point3() );
	}

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		double top = getTop();
		double bottom = getBottom();
		double maxRadius = getMaxRadius();
		BottomToTopAxis bottomToTopAxis = this.bottomToTopAxis.getValue();
		if( bottomToTopAxis == BottomToTopAxis.POSITIVE_X ) {
			boundingBox.setMinimum( bottom, -maxRadius, -maxRadius );
			boundingBox.setMaximum( top, +maxRadius, +maxRadius );
		} else if( bottomToTopAxis == BottomToTopAxis.POSITIVE_Y ) {
			boundingBox.setMinimum( -maxRadius, bottom, -maxRadius );
			boundingBox.setMaximum( +maxRadius, top, +maxRadius );
		} else if( bottomToTopAxis == BottomToTopAxis.POSITIVE_Z ) {
			boundingBox.setMinimum( -maxRadius, -maxRadius, bottom );
			boundingBox.setMaximum( +maxRadius, +maxRadius, top );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_X ) {
			boundingBox.setMinimum( top, -maxRadius, -maxRadius );
			boundingBox.setMaximum( bottom, +maxRadius, +maxRadius );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_Y ) {
			boundingBox.setMinimum( -maxRadius, top, -maxRadius );
			boundingBox.setMaximum( +maxRadius, bottom, +maxRadius );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_Z ) {
			boundingBox.setMinimum( -maxRadius, -maxRadius, top );
			boundingBox.setMaximum( +maxRadius, +maxRadius, bottom );
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		double center = getCenter();
		BottomToTopAxis bottomToTopAxis = this.bottomToTopAxis.getValue();
		if( bottomToTopAxis == BottomToTopAxis.POSITIVE_X ) {
			boundingSphere.center.set( +center, 0, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.POSITIVE_Y ) {
			boundingSphere.center.set( 0, +center, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.POSITIVE_Z ) {
			boundingSphere.center.set( 0, 0, +center );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_X ) {
			boundingSphere.center.set( -center, 0, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_Y ) {
			boundingSphere.center.set( 0, -center, 0 );
		} else if( bottomToTopAxis == BottomToTopAxis.NEGATIVE_Z ) {
			boundingSphere.center.set( 0, 0, -center );
		} else {
			throw new RuntimeException();
		}
		double halfLength = length.getValue() * 0.5;
		double halfLengthSquared = halfLength * halfLength;
		double maxRadius = getMaxRadius();
		double maxRadiusSquared = maxRadius * maxRadius;
		boundingSphere.radius = Math.sqrt( halfLengthSquared + maxRadiusSquared + maxRadiusSquared );
	}

	public final BoundDoubleProperty length = new BoundDoubleProperty( this, 1.0 ) {
		@Override
		public void setValue( Double value ) {
			assert value >= 0.0 : value;
			super.setValue( value );
		}
	};
	public final BoundDoubleProperty bottomRadius = new BoundDoubleProperty( this, 1.0 ) {
		@Override
		public void setValue( Double value ) {
			assert value >= 0.0 : value;
			super.setValue( value );
		}
	};
	public final BoundDoubleProperty topRadius = new BoundDoubleProperty( this, 1.0 ) {
		@Override
		public void setValue( Double value ) {
			assert value >= 0.0 : value;
			super.setValue( value );
		}
	};
	//todo: change default to CENTER?
	public final edu.cmu.cs.dennisc.property.InstanceProperty<OriginAlignment> originAlignment = new edu.cmu.cs.dennisc.property.InstanceProperty<OriginAlignment>( this, OriginAlignment.BOTTOM ) {
		@Override
		public void setValue( OriginAlignment value ) {
			if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( value, this.getValue() ) ) {
				Cylinder.this.markBoundsDirty();
				super.setValue( value );
				Cylinder.this.fireBoundChanged();
			}
		};
	};

	//todo: change default to POSITIVE_Z? NEGATIVE_Z?
	public final edu.cmu.cs.dennisc.property.InstanceProperty<BottomToTopAxis> bottomToTopAxis = new edu.cmu.cs.dennisc.property.InstanceProperty<BottomToTopAxis>( this, BottomToTopAxis.POSITIVE_Y ) {
		@Override
		public void setValue( BottomToTopAxis value ) {
			if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( value, this.getValue() ) ) {
				Cylinder.this.markBoundsDirty();
				super.setValue( value );
				Cylinder.this.fireBoundChanged();
			}
		};
	};
	public final edu.cmu.cs.dennisc.property.BooleanProperty hasBottomCap = new edu.cmu.cs.dennisc.property.BooleanProperty( this, true );
	public final edu.cmu.cs.dennisc.property.BooleanProperty hasTopCap = new edu.cmu.cs.dennisc.property.BooleanProperty( this, true );
}
