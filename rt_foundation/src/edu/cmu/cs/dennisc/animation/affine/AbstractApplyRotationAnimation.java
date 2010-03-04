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
public abstract class AbstractApplyRotationAnimation extends AffineAnimation {
	private double m_angleInRadians;
	private double m_sumInRadians;
	public AbstractApplyRotationAnimation() {
		m_angleInRadians = Double.NaN;
		m_sumInRadians = Double.NaN;
	}
	public AbstractApplyRotationAnimation( edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgSubject, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy, edu.cmu.cs.dennisc.math.Angle angle ) {
		super( sgSubject, sgAsSeenBy );
		setAngle( angle );
		m_sumInRadians = Double.NaN;
	}

	public edu.cmu.cs.dennisc.math.Angle getAngle() {
		return new edu.cmu.cs.dennisc.math.AngleInRadians( m_angleInRadians );
	}
	public void setAngle( edu.cmu.cs.dennisc.math.Angle angle ) {
		m_angleInRadians = angle.getAsRadians();
	}

	protected abstract void applyRotationInRadians( double angle );
	@Override
	public void prologue() {
		m_sumInRadians = 0.0;
	}
	@Override
	public void setPortion( double portion ) {
		double interpInRadians = m_angleInRadians * portion;
		double deltaInRadians = interpInRadians - m_sumInRadians;
		applyRotationInRadians( deltaInRadians );
		m_sumInRadians = interpInRadians;
	}
	@Override
	public void epilogue() {
		m_sumInRadians = Double.NaN;
	}
}
