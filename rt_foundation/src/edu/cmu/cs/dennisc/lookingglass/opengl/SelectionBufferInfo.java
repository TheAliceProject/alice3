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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
class SelectionBufferInfo {
	private float zFront;
	private float zBack;
	private VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> visualAdapter;
//	private edu.cmu.cs.dennisc.scenegraph.Visual sgVisual;
	private boolean isFrontFacing;
//	private edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry;
	private int geometryIndex;
	private int subElement;

	private edu.cmu.cs.dennisc.math.Point3 pointInSource = edu.cmu.cs.dennisc.math.Point3.createNaN();

	public SelectionBufferInfo( PickContext pc, java.nio.IntBuffer intBuffer, int offset ) {
		int nameCount = intBuffer.get( offset + 0 );
		int zFrontAsInt = intBuffer.get( offset + 1 );
		int zBackAsInt = intBuffer.get( offset + 2 );

		long zFrontAsLong = zFrontAsInt;
		zFrontAsLong &= RenderContext.MAX_UNSIGNED_INTEGER;

		this.zFront = (float)zFrontAsLong;
		this.zFront /= (float)RenderContext.MAX_UNSIGNED_INTEGER;

		long zBackAsLong = zBackAsInt;
		zBackAsLong &= RenderContext.MAX_UNSIGNED_INTEGER;
		
//		int[] atDepth = { -1 };
//		pc.gl.glGetIntegerv( GL.GL_DEPTH_BITS, atDepth, 0 );
//		int[] atClearValue = { -1 };
//		pc.gl.glGetIntegerv( GL.GL_DEPTH_CLEAR_VALUE, atClearValue, 0 );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "SelectionBufferInfo:", atDepth[ 0 ], Long.toHexString( atClearValue[ 0 ] ), Long.toHexString( RenderContext.MAX_UNSIGNED_INTEGER ), Integer.toHexString( zFrontAsInt ), Long.toHexString( zFrontAsLong ), Integer.toHexString( zBackAsInt ), Long.toHexString( zBackAsLong )  );

		this.zBack = (float)zBackAsLong;
		this.zBack /= (float)RenderContext.MAX_UNSIGNED_INTEGER;

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
		}
	}

	public float getZFront() {
		return this.zFront;
	}
	public float getZBack() {
		return this.zBack;
	}

	public edu.cmu.cs.dennisc.scenegraph.Visual getSGVisual() {
		if( this.visualAdapter != null ) {
			return this.visualAdapter.m_element;
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
		edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = this.getSGVisual();
		if( sgVisual != null ) {
			edu.cmu.cs.dennisc.scenegraph.Geometry[] gs = sgVisual.geometries.getValue();
			if( 0 <= this.geometryIndex && this.geometryIndex < gs.length ) {
				return gs[ this.geometryIndex ];
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

		this.pointInSource.set( v.x/v.w, v.y/v.w, v.z/v.w );
	}
	public edu.cmu.cs.dennisc.math.Point3 getPointInSource() {
		return this.pointInSource;
	}
}
