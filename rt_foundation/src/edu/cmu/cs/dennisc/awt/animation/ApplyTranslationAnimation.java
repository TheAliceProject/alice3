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
package edu.cmu.cs.dennisc.awt.animation;

/**
 * @author Dennis Cosgrove
 */
public class ApplyTranslationAnimation extends SubjectAnimation {
	private edu.cmu.cs.dennisc.math.Point2f m_translation = new edu.cmu.cs.dennisc.math.Point2f();
	private edu.cmu.cs.dennisc.math.Point2f m_sum = new edu.cmu.cs.dennisc.math.Point2f();
	private edu.cmu.cs.dennisc.math.Point2f m_interp = new edu.cmu.cs.dennisc.math.Point2f();
	private java.awt.Point m_runtime = new java.awt.Point();
	public ApplyTranslationAnimation() {
		m_translation.setNaN();
		m_sum.setNaN();
	}
	public ApplyTranslationAnimation( java.awt.Component awtSubject, edu.cmu.cs.dennisc.math.Point2f translation ) {
		super( awtSubject );
		setTranslation( translation );
		m_sum.setNaN();
	}

	public edu.cmu.cs.dennisc.math.Point2f accessTranslation() {
		return m_translation;
	}
	public edu.cmu.cs.dennisc.math.Point2f getTranslation( edu.cmu.cs.dennisc.math.Point2f rv ) {
		rv.set( m_translation );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Point2f getTranslation() {
		return getTranslation( new edu.cmu.cs.dennisc.math.Point2f() );
	}
	public void setTranslation( edu.cmu.cs.dennisc.math.Point2f translation ) {
		m_translation.set( translation );
	}

	@Override
	public void prologue() {
		m_sum.set( 0, 0 );
	}
	@Override
	public void setPortion( double portion ) {
		edu.cmu.cs.dennisc.math.InterpolationUtilities.interpolate( m_interp, m_translation, portion );
		
		getSubject().getLocation( m_runtime );
		
		m_runtime.x += m_interp.x - m_sum.x;
		m_runtime.y += m_interp.y - m_sum.y;
		
		getSubject().setLocation( m_runtime );

		m_sum.set( m_interp );
	}
	@Override
	public void epilogue() {
		m_sum.setNaN();
	}
}
