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

import edu.cmu.cs.dennisc.math.InterpolationUtilities;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;

/**
 * @author Dennis Cosgrove
 */
public class OrientationAnimation extends AffineAnimation {
	public static final UnitQuaternion USE_EXISTING_VALUE_AT_RUN_TIME = null;

	private UnitQuaternion m_quatBegin = UnitQuaternion.createNaN();
	private UnitQuaternion m_quatEnd = UnitQuaternion.createNaN();

	private UnitQuaternion m_quatBeginUsedAtRuntime = UnitQuaternion.createNaN();

	public OrientationAnimation() {
		m_quatBeginUsedAtRuntime.setNaN();
		m_quatBegin.setNaN();
		m_quatEnd.setNaN();
	}

	public OrientationAnimation( AbstractTransformable sgSubject, ReferenceFrame sgAsSeenBy, UnitQuaternion quatBegin, UnitQuaternion quatEnd ) {
		super( sgSubject, sgAsSeenBy );
		m_quatBeginUsedAtRuntime.setNaN();
		setOrientationBegin( quatBegin );
		setOrientationEnd( quatEnd );
	}

	public UnitQuaternion accessOrientationBeginUsedAtRuntime() {
		return m_quatBeginUsedAtRuntime;
	}

	public UnitQuaternion getOrientationBeginUsedAtRuntime( UnitQuaternion rv ) {
		rv.setValue( m_quatBeginUsedAtRuntime );
		return rv;
	}

	public UnitQuaternion getOrientationBeginUsedAtRuntime() {
		return getOrientationBeginUsedAtRuntime( UnitQuaternion.createNaN() );
	}

	public UnitQuaternion accessOrientationBegin() {
		return m_quatBegin;
	}

	public UnitQuaternion getOrientationBegin( UnitQuaternion rv ) {
		rv.setValue( m_quatBegin );
		return rv;
	}

	public UnitQuaternion getOrientationBegin() {
		return getOrientationBegin( UnitQuaternion.createNaN() );
	}

	public void setOrientationBegin( UnitQuaternion quatBegin ) {
		if( quatBegin != USE_EXISTING_VALUE_AT_RUN_TIME ) {
			m_quatBegin.setValue( quatBegin );
		} else {
			m_quatBegin.setNaN();
		}
	}

	public UnitQuaternion accessOrientationEnd() {
		return m_quatEnd;
	}

	public UnitQuaternion getOrientationEnd( UnitQuaternion rv ) {
		rv.setValue( m_quatEnd );
		return rv;
	}

	public UnitQuaternion getOrientationEnd() {
		return getOrientationEnd( UnitQuaternion.createNaN() );
	}

	public void setOrientationEnd( UnitQuaternion quatEnd ) {
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
		getSubject().setAxesOnly( InterpolationUtilities.interpolate( m_quatBeginUsedAtRuntime, m_quatEnd, portion ), getAsSeenBy() );
	}

	@Override
	public void epilogue() {
		getSubject().setAxesOnly( m_quatEnd, getAsSeenBy() );
		m_quatBeginUsedAtRuntime.setNaN();
	}
}
