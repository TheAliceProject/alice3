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
package edu.cmu.cs.dennisc.animation.affine;

/**
 * @author Dennis Cosgrove
 */
public class OrientationAnimation extends AffineAnimation {
	public static final edu.cmu.cs.dennisc.math.UnitQuaternion USE_EXISTING_VALUE_AT_RUN_TIME = null;

	private edu.cmu.cs.dennisc.math.UnitQuaternion m_quatBegin = edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN();
	private edu.cmu.cs.dennisc.math.UnitQuaternion m_quatEnd = edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN();

	private edu.cmu.cs.dennisc.math.UnitQuaternion m_quatBeginUsedAtRuntime = edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN();

	public OrientationAnimation() {
		m_quatBeginUsedAtRuntime.setNaN();
		m_quatBegin.setNaN();
		m_quatEnd.setNaN();
	}

	public OrientationAnimation( edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgSubject, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy, edu.cmu.cs.dennisc.math.UnitQuaternion quatBegin, edu.cmu.cs.dennisc.math.UnitQuaternion quatEnd ) {
		super( sgSubject, sgAsSeenBy );
		m_quatBeginUsedAtRuntime.setNaN();
		setOrientationBegin( quatBegin );
		setOrientationEnd( quatEnd );
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion accessOrientationBeginUsedAtRuntime() {
		return m_quatBeginUsedAtRuntime;
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getOrientationBeginUsedAtRuntime( edu.cmu.cs.dennisc.math.UnitQuaternion rv ) {
		rv.setValue( m_quatBeginUsedAtRuntime );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getOrientationBeginUsedAtRuntime() {
		return getOrientationBeginUsedAtRuntime( edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN() );
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion accessOrientationBegin() {
		return m_quatBegin;
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getOrientationBegin( edu.cmu.cs.dennisc.math.UnitQuaternion rv ) {
		rv.setValue( m_quatBegin );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getOrientationBegin() {
		return getOrientationBegin( edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN() );
	}

	public void setOrientationBegin( edu.cmu.cs.dennisc.math.UnitQuaternion quatBegin ) {
		if( quatBegin != USE_EXISTING_VALUE_AT_RUN_TIME ) {
			m_quatBegin.setValue( quatBegin );
		} else {
			m_quatBegin.setNaN();
		}
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion accessOrientationEnd() {
		return m_quatEnd;
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getOrientationEnd( edu.cmu.cs.dennisc.math.UnitQuaternion rv ) {
		rv.setValue( m_quatEnd );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getOrientationEnd() {
		return getOrientationEnd( edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN() );
	}

	public void setOrientationEnd( edu.cmu.cs.dennisc.math.UnitQuaternion quatEnd ) {
		m_quatEnd.setValue( quatEnd );
	}

	@Override
	public void prologue() {
		if( m_quatBegin.isNaN() ) {
			m_quatBeginUsedAtRuntime.setValue( getSubject().getAxes( getAsSeenBy() ) );
		} else {
			m_quatBeginUsedAtRuntime.setValue( m_quatBegin );
		}
	}

	@Override
	public void setPortion( double portion ) {
		getSubject().setAxesOnly( edu.cmu.cs.dennisc.math.InterpolationUtilities.interpolate( m_quatBeginUsedAtRuntime, m_quatEnd, portion ), getAsSeenBy() );
	}

	@Override
	public void epilogue() {
		getSubject().setAxesOnly( m_quatEnd, getAsSeenBy() );
		m_quatBeginUsedAtRuntime.setNaN();
	}
}
