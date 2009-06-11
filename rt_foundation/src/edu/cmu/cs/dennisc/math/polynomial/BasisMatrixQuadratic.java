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

package edu.cmu.cs.dennisc.math.polynomial;

/**
 * @author Dennis Cosgrove
 */
public abstract class BasisMatrixQuadratic implements Quadratic {
	private edu.cmu.cs.dennisc.math.Matrix3x3 m_h;
	protected edu.cmu.cs.dennisc.math.Vector3 m_g;  //todo: make private?
	protected BasisMatrixQuadratic( edu.cmu.cs.dennisc.math.Matrix3x3 h, edu.cmu.cs.dennisc.math.Vector3 g ) {
		m_h = h;
		m_g = g;
	}
	public boolean isNaN() {
		return m_h == null || m_h.isNaN() || m_g == null || m_g.isNaN();
	}
	public double evaluate( double t ) {
		double tt = t*t;
		return ( tt*m_h.right.x + t*m_h.right.y + m_h.right.z ) * m_g.x +
			   ( tt*m_h.up.x + t*m_h.up.y + m_h.up.z ) * m_g.y +
			   ( tt*m_h.backward.x + t*m_h.backward.y + m_h.backward.z ) * m_g.z;
	}
    public double evaluateDerivative( double t ) {
        double t2 = t*2;
		return ( t2*m_h.right.x + m_h.right.y ) * m_g.x +
			   ( t2*m_h.up.x + m_h.up.y ) * m_g.y +
			   ( t2*m_h.backward.x + m_h.backward.y ) * m_g.z;
    }
}
