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
public final class Vector2 extends Tuple2 {
	public Vector2() {
	}
	public Vector2( Tuple2 other ) {
		super( other );
	}
	public Vector2( double x, double y ) {
		super( x, y );
	}

	public static Vector2 createZero() {
		return (Vector2)setReturnValueToZero( new Vector2() );
	}
	public static Vector2 createNaN() {
		return (Vector2)setReturnValueToNaN( new Vector2() );
	}
	public static Vector2 createAddition( Tuple2 a, Tuple2 b ) {
		return (Vector2)setReturnValueToAddition( new Vector2(), a, b );
	}
	public static Vector2 createSubtraction( Tuple2 a, Tuple2 b ) {
		return (Vector2)setReturnValueToSubtraction( new Vector2(), a, b );
	}
	public static Vector2 createNegation( Tuple2 a ) {
		return (Vector2)setReturnValueToNegation( new Vector2(), a );
	}
	public static Vector2 createMultiplication( Tuple2 a, Tuple2 b ) {
		return (Vector2)setReturnValueToMultiplication( new Vector2(), a, b );
	}
	public static Vector2 createMultiplication( Tuple2 a, double b ) {
		return (Vector2)setReturnValueToMultiplication( new Vector2(), a, b );
	}
	public static Vector2 createDivision( Tuple2 a, Tuple2 b ) {
		return (Vector2)setReturnValueToDivision( new Vector2(), a, b );
	}
	public static Vector2 createDivision( Tuple2 a, double b ) {
		return (Vector2)setReturnValueToDivision( new Vector2(), a, b );
	}
	public static Vector2 createInterpolation( Tuple2 a, Tuple2 b, double portion ) {
		return (Vector2)setReturnValueToInterpolation( new Vector2(), a, b, portion );
	}
	public static Vector2 createNormalized( Tuple2 a ) {
		return (Vector2)setReturnValueToNormalized( new Vector2(), a );
	}
	public static double calculateDotProduct( Vector2 a, Vector2 b ) {
		return a.x * b.x + a.y * b.y;
	}
}
