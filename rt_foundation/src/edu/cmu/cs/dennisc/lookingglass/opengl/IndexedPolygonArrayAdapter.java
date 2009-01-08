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
public abstract class IndexedPolygonArrayAdapter< E extends edu.cmu.cs.dennisc.scenegraph.IndexedPolygonArray > extends VertexGeometryAdapter< E > {
	private float m_uRatio = Float.NaN;
	private float m_vRatio = Float.NaN;

	protected abstract int getMode();
	protected abstract int getIndicesPerPolygon();
	protected abstract void renderPolygon( RenderContext rc, int[] polygonData, int i );
	protected abstract void pickPolygon( PickContext pc, int[] polygonData, int i );
	
	@Override
    protected boolean isDisplayListInNeedOfRefresh( RenderContext rc ) {
		float uRatio = rc.getURatio();
		float vRatio = rc.getVRatio();
    	return super.isDisplayListInNeedOfRefresh( rc ) || (Float.compare( uRatio, m_uRatio ) != 0) || (Float.compare( vRatio, m_vRatio ) != 0);
    }
	@Override
	protected void renderGeometry( RenderContext rc ) {
		float uRatio = rc.getURatio();
		float vRatio = rc.getVRatio();
		int[] polygonData = m_element.polygonData.getValue();
		int mode = getMode();
		int indicesPerPolygon = getIndicesPerPolygon();
		rc.gl.glBegin( mode );
		try {
			for( int i = 0; i < polygonData.length; i += indicesPerPolygon ) {
				renderPolygon( rc, polygonData, i );
			}
		} finally {
			rc.gl.glEnd();
		}
		m_uRatio = uRatio;
		m_vRatio = vRatio;
	}
	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		int mode = getMode();
		int indicesPerPolygon = getIndicesPerPolygon();
		int[] polygonData = m_element.polygonData.getValue();
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
			pc.gl.glBegin(mode );
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
		if( property == m_element.polygonData ) {
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
