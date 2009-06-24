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
package edu.cmu.cs.dennisc.stencil;

/**
 * @author Dennis Cosgrove
 */
public class ShapedHole implements Hole {
	private java.awt.Shape m_shape = null;
	private java.awt.geom.Area m_area = null;
	public ShapedHole() {
	}
	public ShapedHole( java.awt.Shape shape ) {
		setShape( shape );
	}
	//todo: handle root
	public java.awt.geom.Area getArea( java.awt.Container root ) {
		if( m_area != null ) {
			//pass
		} else {
			if( m_shape != null ) {
				m_area = new java.awt.geom.Area( m_shape );
			} else {
				//pass
			}
		}
		return m_area;
	}
	public java.awt.Shape getShape() {
		return m_shape;
	}
	public void setShape( java.awt.Shape shape ) {
		m_shape = shape;
		m_area = null;
	}
}
