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
public final class Point3f extends Tuple3f {
	public final static edu.cmu.cs.dennisc.math.Point3f ORIGIN = edu.cmu.cs.dennisc.math.Point3f.createZero();

	public Point3f() {
	}

	public Point3f( Tuple3f other ) {
		super( other );
	}

	public Point3f( float x, float y, float z ) {
		super( x, y, z );
	}

	public static Point3f createZero() {
		return (Point3f)setReturnValueToZero( new Point3f() );
	}

	public static Point3f createNaN() {
		return (Point3f)setReturnValueToNaN( new Point3f() );
	}

	public static Point3f createAddition( Tuple3f a, Tuple3f b ) {
		return (Point3f)setReturnValueToAddition( new Point3f(), a, b );
	}

	public static Point3f createSubtraction( Tuple3f a, Tuple3f b ) {
		return (Point3f)setReturnValueToSubtraction( new Point3f(), a, b );
	}

	public static Point3f createNegation( Tuple3f a ) {
		return (Point3f)setReturnValueToNegation( new Point3f(), a );
	}

	public static Point3f createMultiplication( Tuple3f a, Tuple3f b ) {
		return (Point3f)setReturnValueToMultiplication( new Point3f(), a, b );
	}

	public static Point3f createMultiplication( Tuple3f a, float b ) {
		return (Point3f)setReturnValueToMultiplication( new Point3f(), a, b );
	}

	public static Point3f createDivision( Tuple3f a, Tuple3f b ) {
		return (Point3f)setReturnValueToDivision( new Point3f(), a, b );
	}

	public static Point3f createDivision( Tuple3f a, float b ) {
		return (Point3f)setReturnValueToDivision( new Point3f(), a, b );
	}

	public static Point3f createInterpolation( Tuple3f a, Tuple3f b, float portion ) {
		return (Point3f)setReturnValueToInterpolation( new Point3f(), a, b, portion );
	}

	public static Point3f createNormalized( Tuple3f a ) {
		return (Point3f)setReturnValueToNormalized( new Point3f(), a );
	}

	public static Point3f createFromXYZW( Vector4f xyzw ) {
		return new Point3f( xyzw.x / xyzw.w, xyzw.y / xyzw.w, xyzw.z / xyzw.w );
	}

	//	public void setToTranslationComponentOf( MatrixF4x4 m ) {
	//		x = m.translation.x;
	//		y = m.translation.y;
	//		z = m.translation.z;
	//	}
	public static float calculateDistanceSquaredBetween( Point3f a, Point3f b ) {
		float xDelta = b.x - a.x;
		float yDelta = b.y - a.y;
		float zDelta = b.z - a.z;
		return ( xDelta * xDelta ) + ( yDelta * yDelta ) + ( zDelta * zDelta );
	}

	public static float calculateDistanceBetween( Point3f a, Point3f b ) {
		return (float)Math.sqrt( calculateDistanceSquaredBetween( a, b ) );
	}
}
