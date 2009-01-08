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

package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */

public class Polygons extends Element {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< int[] > POLYGON_DATA_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< int[] >( Polygons.class, "PolygonData" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< edu.cmu.cs.dennisc.scenegraph.Vertex[] > VERTICES_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< edu.cmu.cs.dennisc.scenegraph.Vertex[] >( Polygons.class, "Vertices" );

	private edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray m_sgITA = new edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray();

	public edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray getSGITA() {
		return m_sgITA;
	}
	
	public int[] getPolygonData() {
		return m_sgITA.polygonData.getValue();
	}
	public void setPolygonData( int[] polygonData ) {
		m_sgITA.polygonData.setValue( polygonData );
	}

	public edu.cmu.cs.dennisc.scenegraph.Vertex[] getVertices() {
		return m_sgITA.vertices.getValue();
	}
	public void setVertices( edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices ) {
		m_sgITA.vertices.setValue( vertices );
	}
}
