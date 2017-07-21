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
public final class Point2f extends Tuple2f {
	public Point2f() {
	}

	public Point2f( Tuple2f other ) {
		super( other );
	}

	public Point2f( float x, float y ) {
		super( x, y );
	}

	public static Point2f createZero() {
		return (Point2f)setReturnValueToZero( new Point2f() );
	}

	public static Point2f createNaN() {
		return (Point2f)setReturnValueToNaN( new Point2f() );
	}

	public static Point2f createAddition( Tuple2f a, Tuple2f b ) {
		return (Point2f)setReturnValueToAddition( new Point2f(), a, b );
	}

	public static Point2f createSubtraction( Tuple2f a, Tuple2f b ) {
		return (Point2f)setReturnValueToSubtraction( new Point2f(), a, b );
	}

	public static Point2f createNegation( Tuple2f a ) {
		return (Point2f)setReturnValueToNegation( new Point2f(), a );
	}

	public static Point2f createMultiplication( Tuple2f a, Tuple2f b ) {
		return (Point2f)setReturnValueToMultiplication( new Point2f(), a, b );
	}

	public static Point2f createMultiplication( Tuple2f a, float b ) {
		return (Point2f)setReturnValueToMultiplication( new Point2f(), a, b );
	}

	public static Point2f createDivision( Tuple2f a, Tuple2f b ) {
		return (Point2f)setReturnValueToDivision( new Point2f(), a, b );
	}

	public static Point2f createDivision( Tuple2f a, float b ) {
		return (Point2f)setReturnValueToDivision( new Point2f(), a, b );
	}

	public static Point2f createInterpolation( Tuple2f a, Tuple2f b, float portion ) {
		return (Point2f)setReturnValueToInterpolation( new Point2f(), a, b, portion );
	}

	public static Point2f createNormalized( Tuple2f a ) {
		return (Point2f)setReturnValueToNormalized( new Point2f(), a );
	}

	public static float calculateDistanceSquaredBetween( Point2f a, Point2f b ) {
		float xDelta = b.x - a.x;
		float yDelta = b.y - a.y;
		return ( xDelta * xDelta ) + ( yDelta * yDelta );
	}

	public static float calculateDistanceBetween( Point2f a, Point2f b ) {
		return (float)Math.sqrt( calculateDistanceSquaredBetween( a, b ) );
	}
}
