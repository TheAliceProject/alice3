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
public class Point2 extends Tuple2 {
	public Point2() {
	}
	public Point2( Tuple2 other ) {
		super( other );
	}
	public Point2( double x, double y ) {
		super( x, y );
	}

	public static Point2 createZero() {
		return (Point2)setReturnValueToZero( new Point2() );
	}
	public static Point2 createNaN() {
		return (Point2)setReturnValueToNaN( new Point2() );
	}
	public static Point2 createAddition( Tuple2 a, Tuple2 b ) {
		return (Point2)setReturnValueToAddition( new Point2(), a, b );
	}
	public static Point2 createSubtraction( Tuple2 a, Tuple2 b ) {
		return (Point2)setReturnValueToSubtraction( new Point2(), a, b );
	}
	public static Point2 createNegation( Tuple2 a ) {
		return (Point2)setReturnValueToNegation( new Point2(), a );
	}
	public static Point2 createMultiplication( Tuple2 a, Tuple2 b ) {
		return (Point2)setReturnValueToMultiplication( new Point2(), a, b );
	}
	public static Point2 createMultiplication( Tuple2 a, double b ) {
		return (Point2)setReturnValueToMultiplication( new Point2(), a, b );
	}
	public static Point2 createDivision( Tuple2 a, Tuple2 b ) {
		return (Point2)setReturnValueToDivision( new Point2(), a, b );
	}
	public static Point2 createDivision( Tuple2 a, double b ) {
		return (Point2)setReturnValueToDivision( new Point2(), a, b );
	}
	public static Point2 createInterpolation( Tuple2 a, Tuple2 b, double portion ) {
		return (Point2)setReturnValueToInterpolation( new Point2(), a, b, portion );
	}
	public static Point2 createNormalized( Tuple2 a ) {
		return (Point2)setReturnValueToNormalized( new Point2(), a );
	}

	public static double calculateDistanceSquaredBetween( Point2 a, Point2 b ) {
		double xDelta = b.x - a.x;
		double yDelta = b.y - a.y;
		return xDelta * xDelta + yDelta * yDelta;
	}
	public static double calculateDistanceBetween( Point2 a, Point2 b ) {
		return Math.sqrt( calculateDistanceSquaredBetween( a, b ) );
	}
}
