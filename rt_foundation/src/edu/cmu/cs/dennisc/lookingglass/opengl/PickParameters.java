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
public class PickParameters {
	private java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > m_pickResults = new java.util.LinkedList< edu.cmu.cs.dennisc.lookingglass.PickResult >();
	private javax.media.opengl.GLAutoDrawable m_glAutoDrawable;
	private edu.cmu.cs.dennisc.scenegraph.AbstractCamera m_sgCamera;
	private int m_x;
	private int m_y;
	private boolean m_isSubElementRequired;
	private edu.cmu.cs.dennisc.lookingglass.PickObserver m_pickObserver;

	public PickParameters( javax.media.opengl.GLAutoDrawable glAutoDrawable, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int x, int y, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		m_glAutoDrawable = glAutoDrawable;
		m_sgCamera = sgCamera;
		m_x = x;
		m_y = y;
		m_isSubElementRequired = isSubElementRequired;
		m_pickObserver = pickObserver;
	}

	
	public void addPickResult( edu.cmu.cs.dennisc.scenegraph.Component source, edu.cmu.cs.dennisc.scenegraph.Visual sgVisual, boolean isFrontFacing, edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry, int subElement, edu.cmu.cs.dennisc.math.Point3 xyzInSource ) {
		m_pickResults.add(  new edu.cmu.cs.dennisc.lookingglass.PickResult( source, sgVisual, isFrontFacing, sgGeometry, subElement, xyzInSource ) );
	}
	
	public java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > accessAllPickResults() {
		return m_pickResults;
	}
	public edu.cmu.cs.dennisc.lookingglass.PickResult accessFrontMostPickResult() {
		edu.cmu.cs.dennisc.lookingglass.PickResult rv;
		if( m_pickResults.isEmpty() ) {
			rv = new edu.cmu.cs.dennisc.lookingglass.PickResult( m_sgCamera );
		} else {
			rv = m_pickResults.get( 0 );
		}
		return rv;
	}
	
	public javax.media.opengl.GLAutoDrawable getGLAutoDrawable() {
		return m_glAutoDrawable;

	}
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
		return m_sgCamera;
	}
	public int getX() {
		return m_x;
	}
	public int getY() {
		return m_y;
	}
	public int getFlippedY( java.awt.Rectangle actualViewport ) {
		return actualViewport.height - m_y;
	}
	public boolean isSubElementRequired() {
		return m_isSubElementRequired;
	}
	public edu.cmu.cs.dennisc.lookingglass.PickObserver getPickObserver() {
		return m_pickObserver;
	}
}
