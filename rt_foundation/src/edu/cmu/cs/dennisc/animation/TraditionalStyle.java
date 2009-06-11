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
package edu.cmu.cs.dennisc.animation;

/**
 * @author Dennis Cosgrove
 */
public enum TraditionalStyle implements Style {
	BEGIN_AND_END_ABRUPTLY( false, false ),
	BEGIN_GENTLY_AND_END_ABRUPTLY( true, false ),
	BEGIN_ABRUPTLY_AND_END_GENTLY( false, true ),
	BEGIN_AND_END_GENTLY( true, true );
	private boolean m_isSlowInDesired;
	private boolean m_isSlowOutDesired;
	TraditionalStyle( boolean isSlowInDesired, boolean isSlowOutDesired ) {
		m_isSlowInDesired = isSlowInDesired;
		m_isSlowOutDesired = isSlowOutDesired;
	}

	private static double gently (double x, double A, double B) {
		double y, a3, b3, c3, m, b2;
		if (x < A) {
			y = ((B - 1)/(A *  (B * B - A * B + A - 1))) * x * x;
		} else if (x > B) {
			a3 = 1 / (B * B - A * B +  A - 1);
			b3 = -2 * a3;
			c3 = 1 + a3;
			y  = a3 * x * x + b3 * x + c3;
		} else {
			m  = 2 * (B - 1) / (B * B - A * B + A - 1);
			b2 = -m * A / 2;
			y  = m * x + b2;
		}
		return y;
	}

	//todo: this method really should account for how long the animation is
	public double calculatePortion( double timeElapsed, double timeTotal ) {
		if( timeTotal!=0 ) {
			double portion = timeElapsed/timeTotal;
			if( m_isSlowInDesired ) {
				if( m_isSlowOutDesired ) {
					return gently( portion, 0.3, 0.8 );
				} else {
					return gently( portion, 0.99, 0.999 );
				}
			} else {
				if( m_isSlowOutDesired ) {
					return gently( portion, 0.001, 0.01 );
				} else {
					return portion;
				}
			}
		}
		else {
			return 1;
		}
	}
}
