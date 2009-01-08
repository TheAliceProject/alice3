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
public abstract class Context {
	public javax.media.opengl.GL gl;
	public javax.media.opengl.glu.GLU glu;
	public com.sun.opengl.util.GLUT glut;

	private javax.media.opengl.glu.GLUquadric m_quadric;

	public Context() {
		glu = new javax.media.opengl.glu.GLU();
		glut = new com.sun.opengl.util.GLUT();
	}

	//todo: synchronize?
	public javax.media.opengl.glu.GLUquadric getQuadric() {
		if( m_quadric == null ) {
			m_quadric = glu.gluNewQuadric();
		}
		return m_quadric;
	}

	protected abstract void handleGLChange();

	public void setGL( javax.media.opengl.GL gl ) {
		if( this.gl != gl ) {
			this.gl = gl;
			handleGLChange();
		}
	}
	public abstract void setAppearanceIndex( int index );
}
