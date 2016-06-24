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
public class SpaceChangeUtilities {
	public static Matrix3x3 swapXAndY( Matrix3x3 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		rv.right.x = m11;
		rv.up.x = m10;
		rv.backward.x = m12;
		rv.right.y = m01;
		rv.up.y = m00;
		rv.backward.y = m02;
		rv.right.z = m21;
		rv.up.z = m20;
		rv.backward.z = m22;
		return rv;
	}

	public static Matrix4x4 swapXAndY( Matrix4x4 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		double m23 = rv.translation.z;
		double m30 = rv.right.w;
		double m31 = rv.up.w;
		double m32 = rv.backward.w;
		double m33 = rv.translation.w;
		rv.right.x = m11;
		rv.up.x = m10;
		rv.backward.x = m12;
		rv.translation.x = m13;
		rv.right.y = m01;
		rv.up.y = m00;
		rv.backward.y = m02;
		rv.translation.y = m03;
		rv.right.z = m21;
		rv.up.z = m20;
		rv.backward.z = m22;
		rv.translation.z = m23;
		rv.right.w = m31;
		rv.up.w = m30;
		rv.backward.w = m32;
		rv.translation.w = m33;
		return rv;
	}

	public static AffineMatrix4x4 swapXAndY( AffineMatrix4x4 rv ) {
		double m00 = rv.orientation.right.x;
		double m01 = rv.orientation.up.x;
		double m02 = rv.orientation.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.orientation.right.y;
		double m11 = rv.orientation.up.y;
		double m12 = rv.orientation.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.orientation.right.z;
		double m21 = rv.orientation.up.z;
		double m22 = rv.orientation.backward.z;
		double m23 = rv.translation.z;
		rv.orientation.right.x = m11;
		rv.orientation.up.x = m10;
		rv.orientation.backward.x = m12;
		rv.translation.x = m13;
		rv.orientation.right.y = m01;
		rv.orientation.up.y = m00;
		rv.orientation.backward.y = m02;
		rv.translation.y = m03;
		rv.orientation.right.z = m21;
		rv.orientation.up.z = m20;
		rv.orientation.backward.z = m22;
		rv.translation.z = m23;
		return rv;
	}

	public static Matrix3x3 swapXAndZ( Matrix3x3 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		rv.right.x = m22;
		rv.up.x = m21;
		rv.backward.x = m20;
		rv.right.y = m12;
		rv.up.y = m11;
		rv.backward.y = m10;
		rv.right.z = m02;
		rv.up.z = m01;
		rv.backward.z = m00;
		return rv;
	}

	public static Matrix4x4 swapXAndZ( Matrix4x4 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		double m23 = rv.translation.z;
		double m30 = rv.right.w;
		double m31 = rv.up.w;
		double m32 = rv.backward.w;
		double m33 = rv.translation.w;
		rv.right.x = m22;
		rv.up.x = m21;
		rv.backward.x = m20;
		rv.translation.x = m23;
		rv.right.y = m12;
		rv.up.y = m11;
		rv.backward.y = m10;
		rv.translation.y = m13;
		rv.right.z = m02;
		rv.up.z = m01;
		rv.backward.z = m00;
		rv.translation.z = m03;
		rv.right.w = m32;
		rv.up.w = m31;
		rv.backward.w = m30;
		rv.translation.w = m33;
		return rv;
	}

	public static AffineMatrix4x4 swapXAndZ( AffineMatrix4x4 rv ) {
		double m00 = rv.orientation.right.x;
		double m01 = rv.orientation.up.x;
		double m02 = rv.orientation.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.orientation.right.y;
		double m11 = rv.orientation.up.y;
		double m12 = rv.orientation.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.orientation.right.z;
		double m21 = rv.orientation.up.z;
		double m22 = rv.orientation.backward.z;
		double m23 = rv.translation.z;
		rv.orientation.right.x = m22;
		rv.orientation.up.x = m21;
		rv.orientation.backward.x = m20;
		rv.translation.x = m23;
		rv.orientation.right.y = m12;
		rv.orientation.up.y = m11;
		rv.orientation.backward.y = m10;
		rv.translation.y = m13;
		rv.orientation.right.z = m02;
		rv.orientation.up.z = m01;
		rv.orientation.backward.z = m00;
		rv.translation.z = m03;
		return rv;
	}

	public static Matrix3x3 swapYAndZ( Matrix3x3 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		rv.right.x = m00;
		rv.up.x = m02;
		rv.backward.x = m01;
		rv.right.y = m20;
		rv.up.y = m22;
		rv.backward.y = m21;
		rv.right.z = m10;
		rv.up.z = m12;
		rv.backward.z = m11;
		return rv;
	}

	public static AffineMatrix4x4 swapYAndZ( AffineMatrix4x4 rv ) {
		double m00 = rv.orientation.right.x;
		double m01 = rv.orientation.up.x;
		double m02 = rv.orientation.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.orientation.right.y;
		double m11 = rv.orientation.up.y;
		double m12 = rv.orientation.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.orientation.right.z;
		double m21 = rv.orientation.up.z;
		double m22 = rv.orientation.backward.z;
		double m23 = rv.translation.z;
		rv.orientation.right.x = m00;
		rv.orientation.up.x = m02;
		rv.orientation.backward.x = m01;
		rv.translation.x = m03;
		rv.orientation.right.y = m20;
		rv.orientation.up.y = m22;
		rv.orientation.backward.y = m21;
		rv.translation.y = m23;
		rv.orientation.right.z = m10;
		rv.orientation.up.z = m12;
		rv.orientation.backward.z = m11;
		rv.translation.z = m13;
		return rv;
	}

	public static Matrix4x4 swapYAndZ( Matrix4x4 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		double m23 = rv.translation.z;
		double m30 = rv.right.w;
		double m31 = rv.up.w;
		double m32 = rv.backward.w;
		double m33 = rv.translation.w;
		rv.right.x = m00;
		rv.up.x = m02;
		rv.backward.x = m01;
		rv.translation.x = m03;
		rv.right.y = m20;
		rv.up.y = m22;
		rv.backward.y = m21;
		rv.translation.y = m23;
		rv.right.z = m10;
		rv.up.z = m12;
		rv.backward.z = m11;
		rv.translation.z = m13;
		rv.right.w = m30;
		rv.up.w = m32;
		rv.backward.w = m31;
		rv.translation.w = m33;
		return rv;
	}

	public static Matrix3x3 negateX( Matrix3x3 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		rv.right.x = +m00;
		rv.up.x = -m01;
		rv.backward.x = -m02;
		rv.right.y = -m10;
		rv.up.y = +m11;
		rv.backward.y = +m12;
		rv.right.z = -m20;
		rv.up.z = +m21;
		rv.backward.z = +m22;
		return rv;
	}

	public static Matrix4x4 negateX( Matrix4x4 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		double m23 = rv.translation.z;
		double m30 = rv.right.w;
		double m31 = rv.up.w;
		double m32 = rv.backward.w;
		double m33 = rv.translation.w;
		rv.right.x = +m00;
		rv.up.x = -m01;
		rv.backward.x = -m02;
		rv.translation.x = -m03;
		rv.right.y = -m10;
		rv.up.y = +m11;
		rv.backward.y = +m12;
		rv.translation.y = +m13;
		rv.right.z = -m20;
		rv.up.z = +m21;
		rv.backward.z = +m22;
		rv.translation.z = +m23;
		rv.right.w = -m30;
		rv.up.w = +m31;
		rv.backward.w = +m32;
		rv.translation.w = +m33;
		return rv;
	}

	public static Matrix3x3 negateY( Matrix3x3 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		rv.right.x = +m00;
		rv.up.x = -m01;
		rv.backward.x = -m02;
		rv.right.y = -m10;
		rv.up.y = +m11;
		rv.backward.y = +m12;
		rv.right.z = -m20;
		rv.up.z = +m21;
		rv.backward.z = +m22;
		return rv;
	}

	public static Matrix4x4 negateY( Matrix4x4 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		double m23 = rv.translation.z;
		double m30 = rv.right.w;
		double m31 = rv.up.w;
		double m32 = rv.backward.w;
		double m33 = rv.translation.w;
		rv.right.x = +m00;
		rv.up.x = -m01;
		rv.backward.x = +m02;
		rv.translation.x = +m03;
		rv.right.y = -m10;
		rv.up.y = +m11;
		rv.backward.y = -m12;
		rv.translation.y = -m13;
		rv.right.z = +m20;
		rv.up.z = -m21;
		rv.backward.z = +m22;
		rv.translation.z = +m23;
		rv.right.w = +m30;
		rv.up.w = -m31;
		rv.backward.w = +m32;
		rv.translation.w = +m33;
		return rv;
	}

	public static Matrix3x3 negateZ( Matrix3x3 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		rv.right.x = +m00;
		rv.up.x = +m01;
		rv.backward.x = -m02;
		rv.right.y = +m10;
		rv.up.y = +m11;
		rv.backward.y = -m12;
		rv.right.z = -m20;
		rv.up.z = -m21;
		rv.backward.z = +m22;
		return rv;
	}

	public static Matrix4x4 negateZ( Matrix4x4 rv ) {
		double m00 = rv.right.x;
		double m01 = rv.up.x;
		double m02 = rv.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.right.y;
		double m11 = rv.up.y;
		double m12 = rv.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.right.z;
		double m21 = rv.up.z;
		double m22 = rv.backward.z;
		double m23 = rv.translation.z;
		double m30 = rv.right.w;
		double m31 = rv.up.w;
		double m32 = rv.backward.w;
		double m33 = rv.translation.w;
		rv.right.x = +m00;
		rv.up.x = +m01;
		rv.backward.x = -m02;
		rv.translation.x = +m03;
		rv.right.y = +m10;
		rv.up.y = +m11;
		rv.backward.y = -m12;
		rv.translation.y = +m13;
		rv.right.z = -m20;
		rv.up.z = -m21;
		rv.backward.z = +m22;
		rv.translation.z = -m23;
		rv.right.w = +m30;
		rv.up.w = +m31;
		rv.backward.w = -m32;
		rv.translation.w = +m33;
		return rv;
	}

	public static AffineMatrix4x4 negateZ( AffineMatrix4x4 rv ) {
		double m00 = rv.orientation.right.x;
		double m01 = rv.orientation.up.x;
		double m02 = rv.orientation.backward.x;
		double m03 = rv.translation.x;
		double m10 = rv.orientation.right.y;
		double m11 = rv.orientation.up.y;
		double m12 = rv.orientation.backward.y;
		double m13 = rv.translation.y;
		double m20 = rv.orientation.right.z;
		double m21 = rv.orientation.up.z;
		double m22 = rv.orientation.backward.z;
		double m23 = rv.translation.z;
		rv.orientation.right.x = +m00;
		rv.orientation.up.x = +m01;
		rv.orientation.backward.x = -m02;
		rv.translation.x = +m03;
		rv.orientation.right.y = +m10;
		rv.orientation.up.y = +m11;
		rv.orientation.backward.y = -m12;
		rv.translation.y = +m13;
		rv.orientation.right.z = -m20;
		rv.orientation.up.z = -m21;
		rv.orientation.backward.z = +m22;
		rv.translation.z = -m23;
		return rv;
	}
}
