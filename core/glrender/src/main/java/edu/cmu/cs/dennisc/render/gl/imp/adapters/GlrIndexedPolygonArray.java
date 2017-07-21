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

import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrIndexedPolygonArray<T extends edu.cmu.cs.dennisc.scenegraph.IndexedPolygonArray> extends GlrVertexGeometry<T> {
	public GlrIndexedPolygonArray( int mode, int indicesPerPolygon ) {
		this.mode = mode;
		this.indicesPerPolygon = indicesPerPolygon;
	}

	protected abstract void renderPolygon( RenderContext rc, int[] polygonData, int i );

	protected abstract void pickPolygon( PickContext pc, int[] polygonData, int i );

	@Override
	protected boolean isDisplayListInNeedOfRefresh( RenderContext rc ) {
		float uRatio = rc.getURatio();
		float vRatio = rc.getVRatio();
		return super.isDisplayListInNeedOfRefresh( rc ) || ( Float.compare( uRatio, this.uRatioPrev ) != 0 ) || ( Float.compare( vRatio, this.vRatioPrev ) != 0 );
	}

	@Override
	protected void renderGeometry( RenderContext rc, GlrVisual.RenderType renderType ) {
		float uRatio = rc.getURatio();
		float vRatio = rc.getVRatio();
		int[] polygonData = owner.polygonData.getValueAsArray();
		rc.gl.glBegin( mode );
		try {
			for( int i = 0; i < polygonData.length; i += indicesPerPolygon ) {
				renderPolygon( rc, polygonData, i );
			}
		} finally {
			rc.gl.glEnd();
		}
		this.uRatioPrev = uRatio;
		this.vRatioPrev = vRatio;
	}

	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		int[] polygonData = owner.polygonData.getValueAsArray();
		//todo: add try/finally pairs
		pc.gl.glPushName( -1 );
		if( isSubElementRequired ) {
			int id = 0;
			for( int i = 0; i < polygonData.length; i += indicesPerPolygon ) {
				pc.gl.glLoadName( id++ );
				pc.gl.glBegin( mode );
				try {
					pickPolygon( pc, polygonData, i );
				} finally {
					pc.gl.glEnd();
				}
			}
		} else {
			pc.gl.glBegin( mode );
			try {
				for( int i = 0; i < polygonData.length; i += indicesPerPolygon ) {
					pickPolygon( pc, polygonData, i );
				}
			} finally {
				pc.gl.glEnd();
			}
		}
		pc.gl.glPopName();
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.polygonData ) {
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}

	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement ) {
		if( subElement != -1 ) {
			int[] polygonData = owner.polygonData.getValueAsArray();
			int index = subElement * indicesPerPolygon;
			if( ( 0 <= index ) && ( index < polygonData.length ) ) {
				edu.cmu.cs.dennisc.scenegraph.Vertex v = accessVertexAt( polygonData[ index ] );
				GlrGeometry.getIntersectionInSourceFromPlaneInLocal( rv, ray, m, v.position.x, v.position.y, v.position.z, v.normal.x, v.normal.y, v.normal.z );
			} else {
				rv.setNaN();
			}
		} else {
			rv.setNaN();
		}
		return rv;
	}

	private final int mode;
	private final int indicesPerPolygon;
	private float uRatioPrev = Float.NaN;
	private float vRatioPrev = Float.NaN;
}
