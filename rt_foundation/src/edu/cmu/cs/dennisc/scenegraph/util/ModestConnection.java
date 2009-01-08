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
public class ModestConnection extends Connection {
	private LineStrip m_sgLineStrip = new LineStrip();
	
	public ModestConnection() {
		Vertex[] vertices = new Vertex[ 32 ];
		for( int i=0; i<vertices.length; i++ ) {
			vertices[ i ] = Vertex.createXYZ( 0,0,0 );
		}
		m_sgLineStrip.vertices.setValue( vertices );		
		geometries.setValue( new Geometry[] { m_sgLineStrip } );
		getSGFrontFacingAppearance().setShadingStyle( ShadingStyle.NONE );
	}
	
	public void update() {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getTarget().getTransformation( this );
		double s = m.translation.calculateMagnitude();
		s *= 2;
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic x = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.x, 0, s*m.orientation.backward.x );
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic y = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.y, 0, s*m.orientation.backward.y );
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic z = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.z, -s, -s*m.orientation.backward.z );
		Vertex[] vertices = m_sgLineStrip.vertices.getValue();
		synchronized( vertices ) {
			double tDelta = 1.0/(vertices.length-1);
			double t = tDelta;
			for( int i=1; i<vertices.length; i++ ) {
				vertices[ i ].position.set( x.evaluate( t ), y.evaluate( t ), z.evaluate( t ) );
				t += tDelta;
			}
			m_sgLineStrip.vertices.touch();
		}
	}
}
