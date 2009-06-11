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
public class GraphicsContext extends TransformContext {
	private java.awt.Graphics2D m_g2;

	public void initialize( java.awt.Graphics2D g2 ) {
		super.initialize();
		m_g2 = g2;
	}

	public java.awt.Graphics2D getAWTGraphics2D() {
		return m_g2;
	}

	@Override
	protected void update( java.awt.geom.AffineTransform affineTransform ) {
		m_g2.setTransform( affineTransform );
	}
}
