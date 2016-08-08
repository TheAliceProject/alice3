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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public class GlrCylinder extends GlrShape<edu.cmu.cs.dennisc.scenegraph.Cylinder> {
	//todo: add scenegraph hint
	private static final int SLICE_COUNT = 50;
	private static final int STACK_COUNT = 1;
	private static final int LOOP_COUNT = 1;

	private void glCylinder( Context c ) {
		double topRadius;
		if( Double.isNaN( this.topRadius ) ) {
			topRadius = this.bottomRadius;
		} else {
			topRadius = this.topRadius;
		}
		c.gl.glPushMatrix();
		try {
			if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_X ) {
				c.gl.glRotated( +90, 0, 1, 0 );
			} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Y ) {
				c.gl.glRotated( -90, 1, 0, 0 );
			} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Z ) {
				//pass
			} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_X ) {
				c.gl.glRotated( -90, 0, 1, 0 );
			} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_Y ) {
				c.gl.glRotated( +90, 1, 0, 0 );
			} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_Z ) {
				c.gl.glRotated( 180, 1, 0, 0 );
			} else {
				//todo?
				throw new RuntimeException();
			}

			double z;
			if( this.originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.BOTTOM ) {
				z = 0;
			} else if( this.originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.CENTER ) {
				z = -this.length * 0.5;
			} else if( this.originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.TOP ) {
				z = -this.length;
			} else {
				//todo?
				throw new RuntimeException();
			}
			c.gl.glTranslated( 0, 0, z );

			c.glu.gluCylinder( c.getQuadric(), this.bottomRadius, topRadius, this.length, SLICE_COUNT, STACK_COUNT );
			if( this.hasBottomCap && ( this.bottomRadius > 0 ) ) {
				c.gl.glRotated( 180, 1, 0, 0 );
				c.glu.gluDisk( c.getQuadric(), 0, this.bottomRadius, SLICE_COUNT, STACK_COUNT );
				c.gl.glRotated( 180, 1, 0, 0 );
			}
			if( this.hasTopCap && ( topRadius > 0 ) ) {
				c.gl.glTranslated( 0, 0, +this.length );
				c.glu.gluDisk( c.getQuadric(), 0, topRadius, SLICE_COUNT, LOOP_COUNT );
				c.gl.glTranslated( 0, 0, -this.length );
			}
		} finally {
			c.gl.glPopMatrix();
		}
	}

	@Override
	protected void renderGeometry( RenderContext rc, GlrVisual.RenderType renderType ) {
		glCylinder( rc );
	}

	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		int name;
		if( isSubElementRequired ) {
			name = 0;
		} else {
			name = -1;
		}
		pc.gl.glPushName( name );
		glCylinder( pc );
		pc.gl.glPopName();
	}

	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement ) {
		double bottomValue;
		double topValue;
		if( this.originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.BOTTOM ) {
			bottomValue = 0;
			topValue = this.length;
		} else if( this.originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.CENTER ) {
			bottomValue = -this.length * 0.5;
			topValue = +this.length * 0.5;
		} else if( this.originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.TOP ) {
			bottomValue = this.length;
			topValue = 0;
		} else {
			//todo?
			throw new RuntimeException();
		}

		edu.cmu.cs.dennisc.math.Point3 cylinderPosition = new edu.cmu.cs.dennisc.math.Point3( 0, 0, 0 );
		edu.cmu.cs.dennisc.math.Vector3 cylinderDirection = new edu.cmu.cs.dennisc.math.Vector3( 0, 0, 0 );

		edu.cmu.cs.dennisc.math.Point3 cylinderTopPosition = new edu.cmu.cs.dennisc.math.Point3( 0, 0, 0 );
		if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_X ) {
			cylinderDirection.x = 1;
			cylinderPosition.x = bottomValue;
			cylinderTopPosition.x = topValue;
		} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Y ) {
			cylinderDirection.y = 1;
			cylinderPosition.y = bottomValue;
			cylinderTopPosition.y = topValue;
		} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Z ) {
			cylinderDirection.z = 1;
			cylinderPosition.z = bottomValue;
			cylinderTopPosition.z = topValue;
		} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_X ) {
			cylinderDirection.x = -1;
			cylinderPosition.x = -bottomValue;
			cylinderTopPosition.x = -topValue;
		} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_Y ) {
			cylinderDirection.y = -1;
			cylinderPosition.y = -bottomValue;
			cylinderTopPosition.y = -topValue;
		} else if( this.bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_Z ) {
			cylinderDirection.z = -1;
			cylinderPosition.z = -bottomValue;
			cylinderTopPosition.z = -topValue;
		} else {
			//todo?
			throw new RuntimeException();
		}
		double maxRadius = Math.max( this.bottomRadius, this.topRadius );
		m.transform( cylinderPosition );
		m.transform( cylinderDirection );

		final boolean HANDLE_CONES_SEPARATELY = false;
		double t = Double.NaN;
		final double THRESHOLD = 0.01;
		if( HANDLE_CONES_SEPARATELY && ( Math.abs( this.bottomRadius - this.topRadius ) < THRESHOLD ) ) {
			edu.cmu.cs.dennisc.math.Vector3 originToOrigin = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( ray.accessOrigin(), cylinderPosition );
			edu.cmu.cs.dennisc.math.Vector3 rayDirection_X_cylinderDirection = edu.cmu.cs.dennisc.math.Vector3.createCrossProduct( ray.accessDirection(), cylinderDirection );

			double magnitude = rayDirection_X_cylinderDirection.calculateMagnitude();

			if( magnitude > edu.cmu.cs.dennisc.math.EpsilonUtilities.REASONABLE_EPSILON ) {
				rayDirection_X_cylinderDirection.normalize();
				double d = Math.abs( edu.cmu.cs.dennisc.math.Vector3.calculateDotProduct( originToOrigin, rayDirection_X_cylinderDirection ) );
				if( d <= maxRadius ) {
					edu.cmu.cs.dennisc.math.Vector3 originToOrigin_X_cylinderDirection = edu.cmu.cs.dennisc.math.Vector3.createCrossProduct( originToOrigin, cylinderDirection );
					double a = -edu.cmu.cs.dennisc.math.Vector3.calculateDotProduct( originToOrigin_X_cylinderDirection, rayDirection_X_cylinderDirection ) / magnitude;

					edu.cmu.cs.dennisc.math.Vector3 rayDirection_X_CylinderDirection___X_cylinderDirection = edu.cmu.cs.dennisc.math.Vector3.createCrossProduct( rayDirection_X_cylinderDirection, cylinderDirection );
					rayDirection_X_CylinderDirection___X_cylinderDirection.normalize();
					double b = Math.abs( Math.sqrt( ( maxRadius * maxRadius ) - ( d * d ) ) / edu.cmu.cs.dennisc.math.Vector3.calculateDotProduct( ray.accessDirection(), rayDirection_X_CylinderDirection___X_cylinderDirection ) );

					t = a - b;
				}
			}
		} else {
			//todo handle cones
		}
		if( Double.isNaN( t ) ) {
			rv.setNaN();
		} else {
			ray.getPointAlong( rv, t );
		}

		if( rv.isNaN() ) {
			//todo: check to see if hit cap
			edu.cmu.cs.dennisc.math.Point3 pTopCap;
			edu.cmu.cs.dennisc.math.Point3 pBottomCap;
			if( this.hasBottomCap && ( this.bottomRadius > 0 ) ) {
				pBottomCap = new edu.cmu.cs.dennisc.math.Point3();
				GlrGeometry.getIntersectionInSourceFromPlaneInLocal( pBottomCap, ray, m, cylinderPosition, cylinderDirection );
			} else {
				pBottomCap = null;
			}
			if( this.hasTopCap && ( this.topRadius > 0 ) ) {
				pTopCap = new edu.cmu.cs.dennisc.math.Point3();
				GlrGeometry.getIntersectionInSourceFromPlaneInLocal( pTopCap, ray, m, cylinderTopPosition, cylinderDirection );
			} else {
				pTopCap = null;
			}
			if( pBottomCap != null ) {
				if( rv.isNaN() || ( rv.z > pBottomCap.z ) ) {
					rv.set( pBottomCap );
				}
			}
			if( pTopCap != null ) {
				if( rv.isNaN() || ( rv.z > pTopCap.z ) ) {
					rv.set( pTopCap );
				}
			}
		}

		return rv;
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.length ) {
			this.length = owner.length.getValue();
			setIsGeometryChanged( true );
		} else if( property == owner.bottomRadius ) {
			this.bottomRadius = owner.bottomRadius.getValue();
			setIsGeometryChanged( true );
		} else if( property == owner.topRadius ) {
			this.topRadius = owner.topRadius.getValue();
			setIsGeometryChanged( true );
		} else if( property == owner.hasBottomCap ) {
			this.hasBottomCap = owner.hasBottomCap.getValue();
			setIsGeometryChanged( true );
		} else if( property == owner.hasTopCap ) {
			this.hasTopCap = owner.hasTopCap.getValue();
			setIsGeometryChanged( true );
		} else if( property == owner.originAlignment ) {
			this.originAlignment = owner.originAlignment.getValue();
			setIsGeometryChanged( true );
		} else if( property == owner.bottomToTopAxis ) {
			this.bottomToTopAxis = owner.bottomToTopAxis.getValue();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}

	private double length;
	private double bottomRadius;
	private double topRadius;
	private boolean hasBottomCap;
	private boolean hasTopCap;
	private edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment originAlignment;
	private edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis bottomToTopAxis;
}
