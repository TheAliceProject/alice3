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
package edu.cmu.cs.dennisc.scenegraph.util;

import edu.cmu.cs.dennisc.scenegraph.TriangleFan;
import edu.cmu.cs.dennisc.scenegraph.Vertex;

/**
 * @author Dennis Cosgrove
 */
public class Square extends TriangleFan {
	private Vertex[] m_vertices = new Vertex[ 4 ];
	public Square() {
        m_vertices[ 0 ] = Vertex.createXYZIJKUV( 0,0,0, 0,0,1, 0,1 );
        m_vertices[ 1 ] = Vertex.createXYZIJKUV( 0,0,0, 0,0,1, 1,1 );
        m_vertices[ 2 ] = Vertex.createXYZIJKUV( 0,0,0, 0,0,1, 1,0 );
        m_vertices[ 3 ] = Vertex.createXYZIJKUV( 0,0,0, 0,0,1, 0,0 );
        setHalfSize( 0.5 );
	}
	public void setHalfSize( double halfSize ) {
		m_vertices[ 0 ].position.x = -halfSize; 
		m_vertices[ 0 ].position.y = -halfSize;  

		m_vertices[ 1 ].position.x = +halfSize; 
		m_vertices[ 1 ].position.y = -halfSize;  

		m_vertices[ 2 ].position.x = +halfSize; 
		m_vertices[ 2 ].position.y = +halfSize;  

		m_vertices[ 3 ].position.x = -halfSize; 
		m_vertices[ 3 ].position.y = +halfSize;  
		vertices.setValue( m_vertices );
	}
}
