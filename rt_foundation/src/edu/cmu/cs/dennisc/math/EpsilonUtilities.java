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
	public final static double REASONABLE_EPSILON_FOR_SQUARED_VALUES = Math.sqrt( REASONABLE_EPSILON );
	public final static float REASONABLE_EPSILON_FLOAT = (float)REASONABLE_EPSILON;
	public final static float REASONABLE_EPSILON_FOR_SQUARED_VALUES_FLOAT = (float)REASONABLE_EPSILON_FOR_SQUARED_VALUES;
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
}
