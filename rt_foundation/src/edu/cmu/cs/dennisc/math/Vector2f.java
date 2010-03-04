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
public final class Vector2f extends Tuple2f {
	public Vector2f() {
	}
	public Vector2f( Tuple2f other ) {
		super( other );
	}
	public Vector2f( float x, float y ) {
		super( x, y );
	}

	public static Vector2f createZero() {
		return (Vector2f)setReturnValueToZero( new Vector2f() );
	}
	public static Vector2f createNaN() {
		return (Vector2f)setReturnValueToNaN( new Vector2f() );
	}
	public static Vector2f createAddition( Tuple2f a, Tuple2f b ) {
		return (Vector2f)setReturnValueToAddition( new Vector2f(), a, b );
	}
	public static Vector2f createSubtraction( Tuple2f a, Tuple2f b ) {
		return (Vector2f)setReturnValueToSubtraction( new Vector2f(), a, b );
	}
	public static Vector2f createNegation( Tuple2f a ) {
		return (Vector2f)setReturnValueToNegation( new Vector2f(), a );
	}
	public static Vector2f createMultiplication( Tuple2f a, Tuple2f b ) {
		return (Vector2f)setReturnValueToMultiplication( new Vector2f(), a, b );
	}
	public static Vector2f createMultiplication( Tuple2f a, float b ) {
		return (Vector2f)setReturnValueToMultiplication( new Vector2f(), a, b );
	}
	public static Vector2f createDivision( Tuple2f a, Tuple2f b ) {
		return (Vector2f)setReturnValueToDivision( new Vector2f(), a, b );
	}
	public static Vector2f createDivision( Tuple2f a, float b ) {
		return (Vector2f)setReturnValueToDivision( new Vector2f(), a, b );
	}
	public static Vector2f createInterpolation( Tuple2f a, Tuple2f b, float portion ) {
		return (Vector2f)setReturnValueToInterpolation( new Vector2f(), a, b, portion );
	}
	public static Vector2f createNormalized( Tuple2f a ) {
		return (Vector2f)setReturnValueToNormalized( new Vector2f(), a );
	}
	public static float calculateDotProduct( Vector2f a, Vector2f b ) {
		return a.x * b.x + a.y * b.y;
	}
}
