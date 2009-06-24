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
package edu.cmu.cs.dennisc.awt.geom;

/**
 * @author Dennis Cosgrove
 */
public class Arc extends Shape {
	private java.awt.geom.Arc2D m_arc;
	public Arc( double xHalfLength, double yHalfLength, edu.cmu.cs.dennisc.math.Angle angleStart, edu.cmu.cs.dennisc.math.Angle angleExtent ) {
		m_arc = new java.awt.geom.Arc2D.Double( -xHalfLength, -yHalfLength, xHalfLength*2, yHalfLength*2, angleStart.getAsDegrees(), angleExtent.getAsDegrees(), java.awt.geom.Arc2D.OPEN );
	}
	@Override
	protected java.awt.Shape getDrawShape() {
		return m_arc;
	}
	@Override
	protected java.awt.Shape getFillShape() {
		return m_arc;
	}
}
