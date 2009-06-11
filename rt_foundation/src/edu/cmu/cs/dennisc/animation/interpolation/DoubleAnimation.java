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

//todo: better handling of immutables?
/**
 * @author Dennis Cosgrove
 */
public abstract class DoubleAnimation extends InterpolationAnimation< Double > {
	public DoubleAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, Double d0, Double d1 ) {
		super( duration, style, d0, d1 );
	}
	@Override
	protected Double newE( Double other ) {
		return other;
	}
	@Override
	protected Double interpolate( Double rv, Double v0, Double v1, double portion ) {
		return v0 + (v1-v0) * portion;
	}
}
