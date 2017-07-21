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
public class GlrDisc extends GlrShape<edu.cmu.cs.dennisc.scenegraph.Disc> {
	//todo: add scenegraph hint
	private static final int SLICE_COUNT = 50;
	private static final int LOOP_COUNT = 1;

	private void glDisc( Context c ) {
		double innerRadius = owner.innerRadius.getValue();
		double outerRadius = owner.outerRadius.getValue();
		c.gl.glPushMatrix();
		try {
			edu.cmu.cs.dennisc.scenegraph.Disc.Axis axis = owner.axis.getValue();
			if( axis == edu.cmu.cs.dennisc.scenegraph.Disc.Axis.X ) {
				c.gl.glRotated( 90.0, 0.0, 1.0, 0.0 );
			} else if( axis == edu.cmu.cs.dennisc.scenegraph.Disc.Axis.Y ) {
				c.gl.glRotated( 90.0, 1.0, 0.0, 0.0 );
			}
			if( owner.isFrontFaceVisible.getValue() ) {
				c.glu.gluDisk( c.getQuadric(), innerRadius, outerRadius, SLICE_COUNT, LOOP_COUNT );
			}
			if( owner.isBackFaceVisible.getValue() ) {
				c.gl.glRotated( 180.0, 0.0, 1.0, 0.0 );
				c.glu.gluDisk( c.getQuadric(), innerRadius, outerRadius, SLICE_COUNT, LOOP_COUNT );
			}
		} finally {
			c.gl.glPopMatrix();
		}
	}

	@Override
	protected void renderGeometry( RenderContext rc, GlrVisual.RenderType renderType ) {
		//Required for quadric shapes like spheres, discs, and cylinders
		boolean isTextureEnabled = rc.isTextureEnabled();
		rc.glu.gluQuadricTexture( rc.getQuadric(), isTextureEnabled );
		glDisc( rc );
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
		glDisc( pc );
		pc.gl.glPopName();
	}

	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement ) {
		return GlrGeometry.getIntersectionInSourceFromPlaneInLocal( rv, ray, m, 0, 0, 0, 0, 1, 0 );
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.innerRadius ) {
			setIsGeometryChanged( true );
		} else if( property == owner.outerRadius ) {
			setIsGeometryChanged( true );
		} else if( property == owner.isFrontFaceVisible ) {
			setIsGeometryChanged( true );
		} else if( property == owner.isBackFaceVisible ) {
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
