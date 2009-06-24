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

package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class AngleUtilities {
	public static final double REVOLUTIONS_TO_DEGREES_FACTOR = 360;
	public static final double REVOLUTIONS_TO_RADIANS_FACTOR = 2 * Math.PI;
	
	public static final double DEGREES_TO_REVOLUTIONS_FACTOR = 1 / REVOLUTIONS_TO_DEGREES_FACTOR;
	public static final double RADIANS_TO_REVOLUTIONS_FACTOR = 1 / REVOLUTIONS_TO_RADIANS_FACTOR;

	public static final double RADIANS_TO_DEGREES_FACTOR = RADIANS_TO_REVOLUTIONS_FACTOR * REVOLUTIONS_TO_DEGREES_FACTOR;
	public static final double DEGREES_TO_RADIANS_FACTOR = DEGREES_TO_REVOLUTIONS_FACTOR * REVOLUTIONS_TO_RADIANS_FACTOR;

	public static double radiansToRevolutions( double radians ) { 
        return radians * RADIANS_TO_REVOLUTIONS_FACTOR;
    }
	public static double radiansToDegrees( double radians ) { 
        return radians * RADIANS_TO_DEGREES_FACTOR;
    }
    public static double degreesToRevolutions( double degrees ) { 
        return degrees * DEGREES_TO_REVOLUTIONS_FACTOR;
    }
    public static double degreesToRadians( double degrees ) { 
        return degrees * DEGREES_TO_RADIANS_FACTOR;
    }
	public static double revolutionsToRadians( double revolutions ) { 
        return revolutions * REVOLUTIONS_TO_RADIANS_FACTOR;
    }
	public static double revolutionsToDegrees( double revolutions ) { 
        return revolutions * REVOLUTIONS_TO_DEGREES_FACTOR;
    }
	
	public static Angle createNaN() {
		return new AngleInRadians( Double.NaN );
	}
	public static Angle createAddition( Angle a, Angle b ) {
		return setReturnValueToAddition( createNaN(), a, b );
	}
	public static Angle createSubtraction( Angle a, Angle b ) {
		return setReturnValueToSubtraction( createNaN(), a, b );
	}
	public static Angle createNegation( Angle a ) {
		return setReturnValueToNegation( createNaN(), a );
	}
	public static Angle createInterpolation( Angle a, Angle b, double portion ) {
		return setReturnValueToInterpolation( createNaN(), a, b, portion );
	}

	public static Angle setReturnValueToAddition( Angle rv, Angle a, Angle b ) {
		rv.setAsRadians( a.getAsRadians() + b.getAsRadians() );
		return rv;
	}
	public static Angle setReturnValueToSubtraction( Angle rv, Angle a, Angle b ) {
		rv.setAsRadians( a.getAsRadians() - b.getAsRadians() );
		return rv;
	}
	public static Angle setReturnValueToNegation( Angle rv, Angle a ) {
		rv.setAsRadians( -a.getAsRadians() );
		return rv;
	}
	public static Angle setReturnValueToInterpolation( Angle rv, Angle a, Angle b, double portion ) {
		double aInRadians = a.getAsRadians();
		double bInRadians = b.getAsRadians();
		rv.setAsRadians( aInRadians + (bInRadians-aInRadians)*portion );
		return rv;
	}
}
