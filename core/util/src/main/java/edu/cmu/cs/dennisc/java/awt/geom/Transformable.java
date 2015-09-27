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
package edu.cmu.cs.dennisc.java.awt.geom;

/**
 * @author Dennis Cosgrove
 */
public abstract class Transformable {
	private java.awt.geom.AffineTransform m_affineTransform = new java.awt.geom.AffineTransform();
	private java.awt.geom.AffineTransform m_inverseAffineTransform = null;

	public java.awt.geom.AffineTransform getInverseAffineTransform( java.awt.geom.AffineTransform rv ) {
		if( m_inverseAffineTransform == null ) {
			try {
				m_inverseAffineTransform = m_affineTransform.createInverse();
			} catch( java.awt.geom.NoninvertibleTransformException nte ) {
				throw new RuntimeException( nte );
			}
		}
		rv.setTransform( m_inverseAffineTransform );
		return rv;
	}

	public java.awt.geom.AffineTransform getInverseAffineTransform() {
		return getInverseAffineTransform( new java.awt.geom.AffineTransform() );
	}

	public java.awt.geom.AffineTransform getAffineTransform( java.awt.geom.AffineTransform rv ) {
		rv.setTransform( m_affineTransform );
		return rv;
	}

	public java.awt.geom.AffineTransform getAffineTransform() {
		return getAffineTransform( new java.awt.geom.AffineTransform() );
	}

	public void setAffineTransform( java.awt.geom.AffineTransform affineTransform ) {
		m_affineTransform.setTransform( affineTransform );
		m_inverseAffineTransform = null;

	}

	public void applyTranslation( double x, double y ) {
		m_affineTransform.translate( x, y );
		m_inverseAffineTransform = null;
	}

	public void applyRotation( edu.cmu.cs.dennisc.math.Angle theta ) {
		m_affineTransform.rotate( theta.getAsRadians() );
		m_inverseAffineTransform = null;
	}

	public void applyScale( double x, double y ) {
		m_affineTransform.scale( x, y );
		m_inverseAffineTransform = null;
	}

	protected abstract void paintComponent( GraphicsContext gc );

	public final void paint( GraphicsContext gc ) {
		gc.pushAffineTransform();
		gc.multiplyAffineTransform( m_affineTransform );

		paintComponent( gc );

		gc.popAffineTransform();
	}

	protected abstract java.awt.geom.Area update( java.awt.geom.Area rv, TransformContext tc );

	public final java.awt.geom.Area getArea( java.awt.geom.Area rv, TransformContext tc ) {
		tc.pushAffineTransform();
		tc.multiplyAffineTransform( m_affineTransform );
		update( rv, tc );
		tc.popAffineTransform();
		return rv;
	}

	public final java.awt.geom.Area getArea( TransformContext tc ) {
		return getArea( new java.awt.geom.Area(), tc );
	}
}
