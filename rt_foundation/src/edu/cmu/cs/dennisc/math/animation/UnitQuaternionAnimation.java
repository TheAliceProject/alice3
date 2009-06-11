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

import edu.cmu.cs.dennisc.animation.interpolation.InterpolationAnimation;

/**
 * @author Dennis Cosgrove
 */
public abstract class UnitQuaternionAnimation extends InterpolationAnimation<edu.cmu.cs.dennisc.math.UnitQuaternion> {
	public UnitQuaternionAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.UnitQuaternion q0, edu.cmu.cs.dennisc.math.UnitQuaternion q1 ) {
		super( duration, style, q0, q1 );
	}
	@Override
	protected edu.cmu.cs.dennisc.math.UnitQuaternion newE( edu.cmu.cs.dennisc.math.UnitQuaternion other ) {
		edu.cmu.cs.dennisc.math.UnitQuaternion rv;
		if( other != null ) {
			rv = new edu.cmu.cs.dennisc.math.UnitQuaternion( other );
		} else {
			rv = new edu.cmu.cs.dennisc.math.UnitQuaternion( Double.NaN, Double.NaN, Double.NaN, Double.NaN );
		}
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.math.UnitQuaternion interpolate( edu.cmu.cs.dennisc.math.UnitQuaternion rv, edu.cmu.cs.dennisc.math.UnitQuaternion v0, edu.cmu.cs.dennisc.math.UnitQuaternion v1, double portion ) {
		assert v0.isNaN() == false;
		assert v1.isNaN() == false;
		rv.setToInterpolation( v0, v1, portion );
		return rv;
	}
}
