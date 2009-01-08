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
	public OrientationAnimation( edu.cmu.cs.dennisc.scenegraph.Transformable sgSubject, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy, edu.cmu.cs.dennisc.math.UnitQuaternion quatBegin, edu.cmu.cs.dennisc.math.UnitQuaternion quatEnd ) {
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
