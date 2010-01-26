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
public class EpsilonUtilities {
	public final static double REASONABLE_EPSILON = 0.001;
	public final static double MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE = (1-REASONABLE_EPSILON) * (1-REASONABLE_EPSILON);
	public final static double MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE = (1+REASONABLE_EPSILON) * (1+REASONABLE_EPSILON);
	public final static double MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE = REASONABLE_EPSILON * REASONABLE_EPSILON;
	
	public final static float REASONABLE_EPSILON_FLOAT = (float)REASONABLE_EPSILON;
	public final static float MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE_FLOAT = (float)MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE;
	public final static float MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE_FLOAT = (float)MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE;
	public final static float MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE_FLOAT = (float)MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE;

	public static boolean isWithinEpsilon( Number a, double b, double epsilon ) {
		return Math.abs( a.doubleValue()-b ) < epsilon;
	}
	public static boolean isWithinEpsilon( Number a, float b, float epsilon ) {
		return Math.abs( a.floatValue()-b ) < epsilon;
	}
	public static boolean isWithinReasonableEpsilon( Number a, double b ) {
		return isWithinEpsilon( a, b, REASONABLE_EPSILON );
	}
	public static boolean isWithinReasonableEpsilon( Number a, float b ) {
		return isWithinEpsilon( a, b, REASONABLE_EPSILON_FLOAT );
	}
	
	public static boolean isWithinEpsilonOf1InSquaredSpace( double a, double epsilon ) {
		final double min = 1.0-epsilon;
		final double max = 1.0+epsilon;
		return (min*min) < a && a < (max*max);
	}
	public static boolean isWithinEpsilonOf0InSquaredSpace( double a, double epsilon ) {
		return a < epsilon*epsilon;
	}
	public static boolean isWithinEpsilonOf1InSquaredSpace( float a, float epsilon ) {
		final float min = 1.0f-epsilon;
		final float max = 1.0f+epsilon;
		return (min*min) < a && a < (max*max);
	}
	public static boolean isWithinEpsilonOf0InSquaredSpace( float a, float epsilon ) {
		return a < epsilon*epsilon;
	}

	public static boolean isWithinReasonableEpsilonOf1InSquaredSpace( double a ) {
		return MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE < a && a < MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE;
	}
	public static boolean isWithinReasonableEpsilonOf0InSquaredSpace( double a ) {
		return a < MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE;
	}
	public static boolean isWithinReasonableEpsilonOf1InSquaredSpace( float a ) {
		return MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE_FLOAT < a && a < MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE_FLOAT;
	}
	public static boolean isWithinReasonableEpsilonOf0InSquaredSpace( float a ) {
		return a < MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE_FLOAT;
	}
}
