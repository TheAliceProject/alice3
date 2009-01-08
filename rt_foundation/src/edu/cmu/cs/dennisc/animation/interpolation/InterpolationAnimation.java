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
package edu.cmu.cs.dennisc.animation.interpolation;

/**
 * @author Dennis Cosgrove
 */
public abstract class InterpolationAnimation< E > extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
	private E m_v0;
	private E m_v1;
	private E m_v;
	public InterpolationAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, E v0, E v1 ) {
		super( duration, style );
		m_v0 = newE( v0 );
		m_v1 = newE( v1 );
		m_v = newE( null );
	}
	protected abstract E newE( E other );
	protected abstract E interpolate( E rv, E v0, E v1, double portion );
	protected abstract void updateValue( E v );
	@Override
	protected final void prologue() {
	}
	@Override
	protected final void setPortion( double portion ) {
		updateValue( interpolate( m_v, m_v0, m_v1, portion ) );
	}
	@Override
	protected final void epilogue() {
		updateValue( m_v1 );
	}
}

