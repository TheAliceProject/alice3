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
package edu.cmu.cs.dennisc.math.immutable;

/**
 * @author Dennis Cosgrove
 */
public final class MPoint3 extends MTuple3 {
	public static MPoint3 createAddition( MTuple3 a, MTuple3 b ) {
		return new MPoint3(
				a.x + b.x,
				a.y + b.y,
				a.z + b.z );
	}

	public static MPoint3 createSubtraction( MTuple3 a, MTuple3 b ) {
		return new MPoint3(
				a.x - b.x,
				a.y - b.y,
				a.z - b.z );
	}

	public static MPoint3 createMultiplication( MTuple3 a, MTuple3 b ) {
		return new MPoint3(
				a.x * b.x,
				a.y * b.y,
				a.z * b.z );
	}

	public static MPoint3 createMultiplication( MTuple3 v, double d ) {
		return new MPoint3(
				v.x * d,
				v.y * d,
				v.z * d );
	}

	public static MPoint3 createDivision( MTuple3 a, MTuple3 b ) {
		return new MPoint3(
				a.x / b.x,
				a.y / b.y,
				a.z / b.z );
	}

	public static MPoint3 createDivision( MTuple3 v, double d ) {
		return new MPoint3(
				v.x / d,
				v.y / d,
				v.z / d );
	}

	public MPoint3( double x, double y, double z ) {
		super( x, y, z );
	}

	public MPoint3 createTransformed( MAffineMatrix4x4 m ) {
		double m00 = m.orientation.right.x;
		double m10 = m.orientation.right.y;
		double m20 = m.orientation.right.z;
		double m01 = m.orientation.up.x;
		double m11 = m.orientation.up.y;
		double m21 = m.orientation.up.z;
		double m02 = m.orientation.backward.x;
		double m12 = m.orientation.backward.y;
		double m22 = m.orientation.backward.z;
		double m03 = m.translation.x;
		double m13 = m.translation.y;
		double m23 = m.translation.z;
		double x = ( m00 * this.x ) + ( m01 * this.y ) + ( m02 * this.z ) + m03;
		double y = ( m10 * this.x ) + ( m11 * this.y ) + ( m12 * this.z ) + m13;
		double z = ( m20 * this.x ) + ( m21 * this.y ) + ( m22 * this.z ) + m23;
		return new MPoint3( x, y, z );
	}
}
