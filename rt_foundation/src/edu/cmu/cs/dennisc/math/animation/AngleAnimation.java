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
public abstract class AngleAnimation extends InterpolationAnimation<edu.cmu.cs.dennisc.math.Angle> {
	public AngleAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.Angle a0, edu.cmu.cs.dennisc.math.Angle a1 ) {
		super( duration, style, a0, a1 );
	}
	@Override
	protected edu.cmu.cs.dennisc.math.Angle newE( edu.cmu.cs.dennisc.math.Angle other ) {
		edu.cmu.cs.dennisc.math.Angle rv;
		if( other != null ) {
			rv = new edu.cmu.cs.dennisc.math.AngleInRadians( other );
		} else {
			rv = new edu.cmu.cs.dennisc.math.AngleInRadians( Double.NaN );
		}
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.math.Angle interpolate( edu.cmu.cs.dennisc.math.Angle rv, edu.cmu.cs.dennisc.math.Angle v0, edu.cmu.cs.dennisc.math.Angle v1, double portion ) {
		assert v0.isNaN() == false;
		assert v1.isNaN() == false;
		rv.setToInterpolation( v0, v1, portion );
		return rv;
	}
}
