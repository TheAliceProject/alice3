/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.java.awt.animation;

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
