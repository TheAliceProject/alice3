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
public class IndexedTriangleArrayAdapter extends IndexedPolygonArrayAdapter< edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray > {
	@Override
	protected int getMode() {
		return javax.media.opengl.GL.GL_TRIANGLES;
	}
	@Override
	protected int getIndicesPerPolygon() {
		return 3;
	}
	@Override
	protected void renderPolygon( RenderContext rc, int[] polygonData, int i ) {
		rc.renderVertex( accessVertexAt( polygonData[ i ] ) );
		rc.renderVertex( accessVertexAt( polygonData[ i + 1 ] ) );
		rc.renderVertex( accessVertexAt( polygonData[ i + 2 ] ) );
	}
	@Override
	protected void pickPolygon( PickContext pc, int[] polygonData, int i ) {
		pc.pickVertex( accessVertexAt( polygonData[ i ] ) );
		pc.pickVertex( accessVertexAt( polygonData[ i + 1 ] ) );
		pc.pickVertex( accessVertexAt( polygonData[ i + 2 ] ) );
	}
}
