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

import edu.cmu.cs.dennisc.scenegraph.*;

/**
 * @author Dennis Cosgrove
 */
public class ModestAxes extends Visual {
	private static SingleAppearance s_sgFrontFacingAppearance = new SingleAppearance();
	static {
		s_sgFrontFacingAppearance.setShadingStyle( ShadingStyle.NONE );
	}
	
	public ModestAxes( double unitLength, double forwardFactor ) {
	    Vertex[] vertices = new Vertex[ 8 ];
	    vertices[ 0 ] = Vertex.createXYZRGB( 0,          0,          0,                         1,0,0 );
	    vertices[ 1 ] = Vertex.createXYZRGB( unitLength, 0,          0,                         1,0,0 );
	    vertices[ 2 ] = Vertex.createXYZRGB( 0,          0,          0,                         0,1,0 );
	    vertices[ 3 ] = Vertex.createXYZRGB( 0,          unitLength, 0,                         0,1,0 );
	    vertices[ 4 ] = Vertex.createXYZRGB( 0,          0,          0,                         0,0,1 );
	    vertices[ 5 ] = Vertex.createXYZRGB( 0,          0,          unitLength,                0,0,1 );
	    vertices[ 6 ] = Vertex.createXYZRGB( 0,          0,          0,                         1,1,1 );
	    vertices[ 7 ] = Vertex.createXYZRGB( 0,          0,          -unitLength*forwardFactor, 1,1,1 );
	    
	    LineArray sgLineArray = new LineArray();
	    sgLineArray.vertices.setValue( vertices );
	    geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgLineArray } );
		frontFacingAppearance.setValue( s_sgFrontFacingAppearance );
	}
	public ModestAxes( double unitLength ) {
		this( unitLength, 2.0 );
	}
}
