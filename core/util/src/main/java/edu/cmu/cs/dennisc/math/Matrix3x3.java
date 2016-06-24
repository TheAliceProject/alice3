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

//todo: rename
/**
 * @author Dennis Cosgrove
 */
public final class Matrix3x3 extends AbstractMatrix3x3 {
	private static final Matrix3x3 IDENTITY = new Matrix3x3();

	public static Matrix3x3 accessIdentity() {
		IDENTITY.setIdentity();
		return IDENTITY;
	}

	//todo: reduce visibility to private
	public Matrix3x3() {
		//todo: setNaN();
		setIdentity();
	}

	public Matrix3x3( Vector3 right, Vector3 up, Vector3 backward ) {
		set( right, up, backward );
	}

	@Deprecated
	public Matrix3x3( double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22 ) {
		set( m00, m01, m02, m10, m11, m12, m20, m21, m22 );
	}

	public Matrix3x3( AbstractMatrix3x3 other ) {
		setValue( other );
	}

	//accessibility
	@Override
	public void setValue( AbstractMatrix3x3 other ) {
		super.setValue( other );
	}

	public static Matrix3x3 createNaN() {
		return (Matrix3x3)setReturnValueToNaN( new Matrix3x3() );
	}

	public static Matrix3x3 createIdentity() {
		return (Matrix3x3)setReturnValueToIdentity( Matrix3x3.createNaN() );
	}

	//Zero
	public static Matrix3x3 setReturnValueToZero( Matrix3x3 rv ) {
		rv.right.setZero();
		rv.up.setZero();
		rv.backward.setZero();
		return rv;
	}

	public static Matrix3x3 createZero() {
		return setReturnValueToZero( Matrix3x3.createNaN() );
	}

	public void setZero() {
		setReturnValueToZero( this );
	}

	//	public void setToOrientationComponentOf( Matrix4x4 m ) {
	//		this.right.x = m.right.x;
	//		this.up.x = m.up.x;
	//		this.backward.x = m.backward.x;
	//		this.right.y = m.right.y;
	//		this.up.y = m.up.y;
	//		this.backward.y = m.backward.y;
	//		this.right.z = m.right.z;
	//		this.up.z = m.up.z;
	//		this.backward.z = m.backward.z;
	//	}
	@Deprecated
	public void set( double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22 ) {
		this.right.x = m00;
		this.up.x = m01;
		this.backward.x = m02;
		this.right.y = m10;
		this.up.y = m11;
		this.backward.y = m12;
		this.right.z = m20;
		this.up.z = m21;
		this.backward.z = m22;
	}

	//	@Override
	//	public void setToMultiplication( AbstractMatrix3x3 a, AbstractMatrix3x3 b ) {
	//		super.setToMultiplication( a, b );
	//	}
	//	@Override
	//	public void applyMultiplication( AbstractMatrix3x3 b ) {
	//		super.applyMultiplication( b );
	//	}

	//todo: equals, hashCode
}
