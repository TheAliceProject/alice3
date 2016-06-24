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

package edu.cmu.cs.dennisc.render.gl.imp;

import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class SelectionBufferInfo {
	public SelectionBufferInfo( PickContext pc, java.nio.IntBuffer intBuffer, int offset ) {
		int nameCount = intBuffer.get( offset + 0 );
		int zFrontAsInt = intBuffer.get( offset + 1 );
		int zBackAsInt = intBuffer.get( offset + 2 );

		long zFrontAsLong = zFrontAsInt;
		zFrontAsLong &= PickContext.MAX_UNSIGNED_INTEGER;

		float zFront = (float)zFrontAsLong;
		zFront /= (float)PickContext.MAX_UNSIGNED_INTEGER;
		this.zFront = zFront;

		long zBackAsLong = zBackAsInt;
		zBackAsLong &= PickContext.MAX_UNSIGNED_INTEGER;

		//		int[] atDepth = { -1 };
		//		pc.gl.glGetIntegerv( GL_DEPTH_BITS, atDepth, 0 );
		//		int[] atClearValue = { -1 };
		//		pc.gl.glGetIntegerv( GL_DEPTH_CLEAR_VALUE, atClearValue, 0 );
		//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "SelectionBufferInfo:", atDepth[ 0 ], Long.toHexString( atClearValue[ 0 ] ), Long.toHexString( RenderContext.MAX_UNSIGNED_INTEGER ), Integer.toHexString( zFrontAsInt ), Long.toHexString( zFrontAsLong ), Integer.toHexString( zBackAsInt ), Long.toHexString( zBackAsLong )  );

		float zBack = (float)zBackAsLong;
		zBack /= (float)PickContext.MAX_UNSIGNED_INTEGER;
		this.zBack = zBack;

		if( nameCount == 4 ) {
			int key = intBuffer.get( offset + 3 );
			this.visualAdapter = pc.getPickVisualAdapterForName( key );
			//			if( visualAdapter != null ) {
			//				this.sgVisual = visualAdapter.m_element;
			this.isFrontFacing = intBuffer.get( offset + 4 ) == 1;
			//				this.sgGeometry = this.sgVisual.geometries.getValue()[ intBuffer.get( offset + 5 ) ];
			this.geometryIndex = intBuffer.get( offset + 5 );
			this.subElement = intBuffer.get( offset + 6 );
			//			}
		} else {
			this.visualAdapter = null;
			this.isFrontFacing = false;
			this.geometryIndex = -1;
			this.subElement = -1;
		}
	}

	public float getZFront() {
		return this.zFront;
	}

	public float getZBack() {
		return this.zBack;
	}

	public edu.cmu.cs.dennisc.scenegraph.Visual getSgVisual() {
		if( this.visualAdapter != null ) {
			return this.visualAdapter.getOwner();
		} else {
			return null;
		}
	}

	public boolean isFrontFacing() {
		return this.isFrontFacing;
	}

	public int getGeometryIndex() {
		return this.geometryIndex;
	}

	public edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = this.getSgVisual();
		if( sgVisual != null ) {
			if( ( 0 <= this.geometryIndex ) && ( this.geometryIndex < sgVisual.getGeometryCount() ) ) {
				return sgVisual.getGeometryAt( this.geometryIndex );
			} else {
				return null;
			}
		} else {
			return null;
		}
		//return this.sgGeometry;
	}

	public int getSubElement() {
		return this.subElement;
	}

	public void updatePointInSource( edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseAbsoluteTransformationOfSource ) {
		if( this.visualAdapter != null ) {
			this.visualAdapter.getIntersectionInSource( this.pointInSource, ray, inverseAbsoluteTransformationOfSource, this.geometryIndex, this.subElement );
		} else {
			this.pointInSource.setNaN();
		}
	}

	public void updatePointInSource( edu.cmu.cs.dennisc.math.Matrix4x4 m ) {
		double z = this.zFront;
		z *= 2;
		z -= 1;

		edu.cmu.cs.dennisc.math.Vector4 v = new edu.cmu.cs.dennisc.math.Vector4( 0, 0, z, 1 );
		m.transform( v );

		this.pointInSource.set( v.x / v.w, v.y / v.w, v.z / v.w );
	}

	public edu.cmu.cs.dennisc.math.Point3 getPointInSource() {
		return this.pointInSource;
	}

	private final float zFront;
	private final float zBack;
	private final GlrVisual<? extends edu.cmu.cs.dennisc.scenegraph.Visual> visualAdapter;
	private final boolean isFrontFacing;
	private final int geometryIndex;
	private final int subElement;

	private edu.cmu.cs.dennisc.math.Point3 pointInSource = edu.cmu.cs.dennisc.math.Point3.createNaN();
}
