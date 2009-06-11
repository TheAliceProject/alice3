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
public class Point3 extends Tuple3 {
	public Point3() {
	}
	public Point3( Tuple3 other ) {
		super( other );
	}
	public Point3( double x, double y, double z ) {
		super( x, y, z );
	}

	public static Point3 createZero() {
		return (Point3)setReturnValueToZero( new Point3() );
	}
	public static Point3 createNaN() {
		return (Point3)setReturnValueToNaN( new Point3() );
	}
	public static Point3 createAddition( Tuple3 a, Tuple3 b ) {
		return (Point3)setReturnValueToAddition( new Point3(), a, b );
	}
	public static Point3 createSubtraction( Tuple3 a, Tuple3 b ) {
		return (Point3)setReturnValueToSubtraction( new Point3(), a, b );
	}
	public static Point3 createNegation( Tuple3 a ) {
		return (Point3)setReturnValueToNegation( new Point3(), a );
	}
	public static Point3 createMultiplication( Tuple3 a, Tuple3 b ) {
		return (Point3)setReturnValueToMultiplication( new Point3(), a, b );
	}
	public static Point3 createMultiplication( Tuple3 a, double b ) {
		return (Point3)setReturnValueToMultiplication( new Point3(), a, b );
	}
	public static Point3 createDivision( Tuple3 a, Tuple3 b ) {
		return (Point3)setReturnValueToDivision( new Point3(), a, b );
	}
	public static Point3 createDivision( Tuple3 a, double b ) {
		return (Point3)setReturnValueToDivision( new Point3(), a, b );
	}
	public static Point3 createInterpolation( Tuple3 a, Tuple3 b, double portion ) {
		return (Point3)setReturnValueToInterpolation( new Point3(), a, b, portion );
	}
	public static Point3 createNormalized( Tuple3 a ) {
		return (Point3)setReturnValueToNormalized( new Point3(), a );
	}

	public static Point3 createFromXYZW( Vector4 xyzw ) {
		return new Point3( xyzw.x / xyzw.w, xyzw.y / xyzw.w, xyzw.z / xyzw.w );
	}

	public void setToTranslationComponentOf( Matrix4x4 m ) {
		x = m.translation.x;
		y = m.translation.y;
		z = m.translation.z;
	}
	public static double calculateDistanceSquaredBetween( Point3 a, Point3 b ) {
		double xDelta = b.x - a.x;
		double yDelta = b.y - a.y;
		double zDelta = b.z - a.z;
		return xDelta * xDelta + yDelta * yDelta + zDelta * zDelta;
	}
	public static double calculateDistanceBetween( Point3 a, Point3 b ) {
		return Math.sqrt( calculateDistanceSquaredBetween( a, b ) );
	}
}
