/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class EpsilonUtilities {
	public final static double REASONABLE_EPSILON = 0.001;
	public final static double MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE = ( 1 - REASONABLE_EPSILON ) * ( 1 - REASONABLE_EPSILON );
	public final static double MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE = ( 1 + REASONABLE_EPSILON ) * ( 1 + REASONABLE_EPSILON );
	public final static double MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE = REASONABLE_EPSILON * REASONABLE_EPSILON;

	public final static float REASONABLE_EPSILON_FLOAT = (float)REASONABLE_EPSILON;
	public final static float MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE_FLOAT = (float)MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE;
	public final static float MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE_FLOAT = (float)MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE;
	public final static float MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE_FLOAT = (float)MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE;

	public static boolean isWithinEpsilon( Number a, double b, double epsilon ) {
		return Math.abs( a.doubleValue() - b ) < epsilon;
	}

	public static boolean isWithinEpsilon( Number a, float b, float epsilon ) {
		return Math.abs( a.floatValue() - b ) < epsilon;
	}

	public static boolean isWithinReasonableEpsilon( Number a, double b ) {
		return isWithinEpsilon( a, b, REASONABLE_EPSILON );
	}

	public static boolean isWithinReasonableEpsilon( Number a, float b ) {
		return isWithinEpsilon( a, b, REASONABLE_EPSILON_FLOAT );
	}

	public static boolean isWithinEpsilonOf1InSquaredSpace( double a, double epsilon ) {
		final double min = 1.0 - epsilon;
		final double max = 1.0 + epsilon;
		return ( ( min * min ) < a ) && ( a < ( max * max ) );
	}

	public static boolean isWithinEpsilonOf0InSquaredSpace( double a, double epsilon ) {
		return a < ( epsilon * epsilon );
	}

	public static boolean isWithinEpsilonOf1InSquaredSpace( float a, float epsilon ) {
		final float min = 1.0f - epsilon;
		final float max = 1.0f + epsilon;
		return ( ( min * min ) < a ) && ( a < ( max * max ) );
	}

	public static boolean isWithinEpsilonOf0InSquaredSpace( float a, float epsilon ) {
		return a < ( epsilon * epsilon );
	}

	public static boolean isWithinReasonableEpsilonOf1InSquaredSpace( double a ) {
		return ( MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE < a ) && ( a < MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE );
	}

	public static boolean isWithinReasonableEpsilonOf0InSquaredSpace( double a ) {
		return a < MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE;
	}

	public static boolean isWithinReasonableEpsilonOf1InSquaredSpace( float a ) {
		return ( MINIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_1_IN_SQUARED_SPACE_FLOAT < a ) && ( a < MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE_FLOAT );
	}

	public static boolean isWithinReasonableEpsilonOf0InSquaredSpace( float a ) {
		return a < MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE_FLOAT;
	}
}
