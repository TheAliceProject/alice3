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
package edu.cmu.cs.dennisc.math.animation;

/**
 * @author Dennis Cosgrove
 */
public abstract class AffineMatrix4x4Animation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
	private edu.cmu.cs.dennisc.math.UnitQuaternion m_q0;
	private edu.cmu.cs.dennisc.math.UnitQuaternion m_q1;
	private edu.cmu.cs.dennisc.math.UnitQuaternion m_qBuffer;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_m0;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_m1;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_mBuffer;
	public AffineMatrix4x4Animation( Number duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1 ) {
		super( duration, style );
		m_q0 = m0.orientation.createUnitQuaternion();
		m_q1 = m1.orientation.createUnitQuaternion();
		m_qBuffer = edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN();

		m_m0 = m0;
		m_m1 = m1;
		m_mBuffer = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN();
	}
	
	protected abstract void updateValue( edu.cmu.cs.dennisc.math.AffineMatrix4x4 value );
	@Override
	protected void prologue() {
		updateValue( m_m0 );
	}
	@Override
	protected void setPortion( double portion ) {
		m_mBuffer.translation.setToInterpolation( m_m0.translation, m_m1.translation, portion );
		m_qBuffer.setToInterpolation( m_q0, m_q1, portion );
		m_mBuffer.orientation.setValue( m_qBuffer );
		updateValue( m_mBuffer );
	}
	@Override
	protected void epilogue() {
		updateValue( m_m1 );
	}
}
