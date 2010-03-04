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
public final class Vector4 extends Tuple4 {
	public Vector4() {
	}
	public Vector4( Vector4 other ) {
		super( other );
	}
	public Vector4( double x, double y, double z, double w ) {
		super( x, y, z, w );
	}

	public static Vector4 createZero() {
		return (Vector4)setReturnValueToZero( new Vector4() );
	}
	public static Vector4 createNaN() {
		return (Vector4)setReturnValueToNaN( new Vector4() );
	}
	public static Vector4 createAddition( Tuple4 a, Tuple4 b ) {
		return (Vector4)setReturnValueToAddition( new Vector4(), a, b );
	}
	public static Vector4 createSubtraction( Tuple4 a, Tuple4 b ) {
		return (Vector4)setReturnValueToSubtraction( new Vector4(), a, b );
	}
	public static Vector4 createNegation( Tuple4 a ) {
		return (Vector4)setReturnValueToNegation( new Vector4(), a );
	}
	public static Vector4 createMultiplication( Tuple4 a, Tuple4 b ) {
		return (Vector4)setReturnValueToMultiplication( new Vector4(), a, b );
	}
	public static Vector4 createMultiplication( Tuple4 a, double b ) {
		return (Vector4)setReturnValueToMultiplication( new Vector4(), a, b );
	}
	public static Vector4 createDivision( Tuple4 a, Tuple4 b ) {
		return (Vector4)setReturnValueToDivision( new Vector4(), a, b );
	}
	public static Vector4 createDivision( Tuple4 a, double b ) {
		return (Vector4)setReturnValueToDivision( new Vector4(), a, b );
	}
	public static Vector4 createInterpolation( Tuple4 a, Tuple4 b, double portion ) {
		return (Vector4)setReturnValueToInterpolation( new Vector4(), a, b, portion );
	}
	public static Vector4 createNormalized( Tuple4 a ) {
		return (Vector4)setReturnValueToNormalized( new Vector4(), a );
	}
}
