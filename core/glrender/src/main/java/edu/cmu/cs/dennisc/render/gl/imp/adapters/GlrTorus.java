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

import static com.jogamp.opengl.GL2.GL_QUAD_STRIP;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual.RenderType;

/**
 * @author Dennis Cosgrove
 */
public class GlrTorus extends GlrShape<edu.cmu.cs.dennisc.scenegraph.Torus> {
	private void glVertex( Context context, edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane coordinatePlane, double majorRadius, double minorRadius, double theta, double phi, boolean isLightingEnabled ) {
		double sinTheta = Math.sin( theta );
		double cosTheta = Math.cos( theta );
		double sinPhi = Math.sin( phi );
		double cosPhi = Math.cos( phi );

		double y = minorRadius * sinPhi;
		double r = majorRadius + ( minorRadius * cosPhi );
		double x = sinTheta * r;
		double z = cosTheta * r;
		if( isLightingEnabled ) {
			double i = sinTheta * cosPhi;
			double j = sinPhi;
			double k = cosTheta * cosPhi;
			if( coordinatePlane == edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane.XY ) {
				//todo
			} else if( coordinatePlane == edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane.YZ ) {
				//todo
			}
			context.gl.glNormal3d( i, j, k );
		}
		if( coordinatePlane == edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane.XY ) {
			//todo
		} else if( coordinatePlane == edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane.YZ ) {
			//todo
		}
		context.gl.glVertex3d( x, y, z );
	}

	private void glTorus( Context context, boolean isLightingEnabled ) {

		edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane coordinatePlane = this.owner.coordinatePlane.getValue();
		double majorRadius = this.owner.majorRadius.getValue();
		double minorRadius = this.owner.minorRadius.getValue();

		//todo: add scenegraph hint
		final int N = 32;
		final int M = 16;
		double dTheta = ( 2 * Math.PI ) / ( N - 1 );
		double dPhi = ( 2 * Math.PI ) / ( M - 1 );

		double theta = 0;
		for( int i = 0; i < ( N - 1 ); i++ ) {
			double phi = 0;
			context.gl.glBegin( GL_QUAD_STRIP );
			for( int j = 0; j < M; j++ ) {
				glVertex( context, coordinatePlane, majorRadius, minorRadius, theta, phi, isLightingEnabled );
				glVertex( context, coordinatePlane, majorRadius, minorRadius, theta + dTheta, phi, isLightingEnabled );
				phi += dPhi;
			}
			context.gl.glEnd();
			theta += dTheta;
		}
	}

	@Override
	protected void renderGeometry( RenderContext rc, GlrVisual.RenderType renderType ) {
		glTorus( rc, true );
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
		glTorus( pc, false );
		pc.gl.glPopName();
	}

	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement ) {
		//todo: solve for intersection with actual torus as opposed to just the plane
		edu.cmu.cs.dennisc.math.Vector3 direction = new edu.cmu.cs.dennisc.math.Vector3( 0, 0, 0 );
		edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane coordinatePlane = this.owner.coordinatePlane.getValue();
		if( coordinatePlane == edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane.XY ) {
			direction.z = 1;
		} else if( coordinatePlane == edu.cmu.cs.dennisc.scenegraph.Torus.CoordinatePlane.YZ ) {
			direction.x = 1;
		} else {
			direction.y = 1;
		}
		return GlrGeometry.getIntersectionInSourceFromPlaneInLocal( rv, ray, m, 0, 0, 0, direction.x, direction.y, direction.z );
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.majorRadius ) {
			setIsGeometryChanged( true );
		} else if( property == owner.minorRadius ) {
			setIsGeometryChanged( true );
		} else if( property == owner.coordinatePlane ) {
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
