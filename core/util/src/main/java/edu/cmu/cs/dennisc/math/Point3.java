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
public final class Point3 extends Tuple3 {
	public final static edu.cmu.cs.dennisc.math.Point3 ORIGIN = edu.cmu.cs.dennisc.math.Point3.createZero();

	public Point3() {
	}

	public Point3( Tuple3 other ) {
		super( other );
	}

	public Point3( double x, double y, double z ) {
		super( x, y, z );
	}

	public Point3( edu.cmu.cs.dennisc.math.immutable.MPoint3 other ) {
		this( other.x, other.y, other.z );
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

	public static double calculateDistanceSquaredBetween( Tuple3 a, Tuple3 b ) {
		double xDelta = b.x - a.x;
		double yDelta = b.y - a.y;
		double zDelta = b.z - a.z;
		return ( xDelta * xDelta ) + ( yDelta * yDelta ) + ( zDelta * zDelta );
	}

	public static double calculateDistanceBetween( Tuple3 a, Tuple3 b ) {
		return Math.sqrt( calculateDistanceSquaredBetween( a, b ) );
	}

	public edu.cmu.cs.dennisc.math.immutable.MPoint3 createImmutable() {
		return new edu.cmu.cs.dennisc.math.immutable.MPoint3( this.x, this.y, this.z );
	}
}
