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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.print.Printable;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractMatrix3x3 implements BinaryEncodableAndDecodable, Printable {
	public final Vector3 right = Vector3.createPositiveXAxis();
	public final Vector3 up = Vector3.createPositiveYAxis();
	public final Vector3 backward = Vector3.createPositiveZAxis();

	@Deprecated
	public void decode( BinaryDecoder binaryDecoder ) {
		right.decode( binaryDecoder );
		up.decode( binaryDecoder );
		backward.decode( binaryDecoder );
	}

	@Override
	public void encode( BinaryEncoder binaryEncoder ) {
		right.encode( binaryEncoder );
		up.encode( binaryEncoder );
		backward.encode( binaryEncoder );
	}

	@Override
	public Appendable append( Appendable rv, DecimalFormat decimalFormat, boolean isLines ) throws IOException {
		if( isLines ) {
			int n = decimalFormat.format( 0.0 ).length() + 1;
			rv.append( "+-" );
			for( int i = 0; i < 3; i++ ) {
				for( int j = 0; j < n; j++ ) {
					rv.append( ' ' );
				}
			}
			rv.append( "-+\n" );
		}

		if( isLines ) {
			rv.append( "| " );
		} else {
			rv.append( "[ " );
		}
		rv.append( decimalFormat.format( this.right.x ) );
		rv.append( ' ' );
		rv.append( decimalFormat.format( this.up.x ) );
		rv.append( ' ' );
		rv.append( decimalFormat.format( this.backward.x ) );
		if( isLines ) {
			rv.append( "  |\n" );
		} else {
			rv.append( "  ] " );
		}

		if( isLines ) {
			rv.append( "| " );
		} else {
			rv.append( "[ " );
		}
		rv.append( decimalFormat.format( this.right.y ) );
		rv.append( ' ' );
		rv.append( decimalFormat.format( this.up.y ) );
		rv.append( ' ' );
		rv.append( decimalFormat.format( this.backward.y ) );
		if( isLines ) {
			rv.append( "  |\n" );
		} else {
			rv.append( "  ] " );
		}

		if( isLines ) {
			rv.append( "| " );
		} else {
			rv.append( "[ " );
		}
		rv.append( decimalFormat.format( this.right.z ) );
		rv.append( ' ' );
		rv.append( decimalFormat.format( this.up.z ) );
		rv.append( ' ' );
		rv.append( decimalFormat.format( this.backward.z ) );
		if( isLines ) {
			rv.append( "  |\n" );
		} else {
			rv.append( "  ] " );
		}

		if( isLines ) {
			int n = decimalFormat.format( 0.0 ).length() + 1;
			rv.append( "+-" );
			for( int i = 0; i < 3; i++ ) {
				for( int j = 0; j < n; j++ ) {
					rv.append( ' ' );
				}
			}
			rv.append( "-+\n" );
		}
		return rv;
	}

	public boolean isWithinEpsilonOfUnitLengthSquared( double epsilon ) {
		return right.isWithinEpsilonOfUnitLengthSquared( epsilon ) && up.isWithinEpsilonOfUnitLengthSquared( epsilon ) && backward.isWithinEpsilonOfUnitLengthSquared( epsilon );
	}

	public boolean isWithinReasonableEpsilonOfUnitLengthSquared() {
		return isWithinEpsilonOfUnitLengthSquared( EpsilonUtilities.REASONABLE_EPSILON );
	}

	protected void setValue( AbstractMatrix3x3 other ) {
		this.right.set( other.right );
		this.up.set( other.up );
		this.backward.set( other.backward );
	}

	//NaN
	public static AbstractMatrix3x3 setReturnValueToNaN( AbstractMatrix3x3 rv ) {
		rv.right.setNaN();
		rv.up.setNaN();
		rv.backward.setNaN();
		return rv;
	}

	public void setNaN() {
		setReturnValueToNaN( this );
	}

	public boolean isNaN() {
		return right.isNaN() || up.isNaN() || backward.isNaN();
	}

	public boolean isZero() {
		return right.isZero() && up.isZero() && backward.isZero();
	}

	public static AbstractMatrix3x3 setReturnValueToIdentity( AbstractMatrix3x3 rv ) {
		rv.right.set( 1, 0, 0 );
		rv.up.set( 0, 1, 0 );
		rv.backward.set( 0, 0, 1 );
		return rv;
	}

	public void setIdentity() {
		setReturnValueToIdentity( this );
	}

	public boolean isIdentity() {
		return right.isPositiveXAxis() && up.isPositiveYAxis() && backward.isPositiveZAxis();
	}

	public double[] getAsColumnMajorArray16( double[] rv ) {
		assert rv.length == 16;
		rv[ 0 ] = right.x;
		rv[ 1 ] = right.y;
		rv[ 2 ] = right.z;
		rv[ 3 ] = 0.0;
		rv[ 4 ] = up.x;
		rv[ 5 ] = up.y;
		rv[ 6 ] = up.z;
		rv[ 7 ] = 0.0;
		rv[ 8 ] = backward.x;
		rv[ 9 ] = backward.y;
		rv[ 10 ] = backward.z;
		rv[ 11 ] = 0.0;
		rv[ 12 ] = 0.0;
		rv[ 13 ] = 0.0;
		rv[ 14 ] = 0.0;
		rv[ 15 ] = 1.0;
		return rv;
	}

	public double[] getAsColumnMajorArray16() {
		return getAsColumnMajorArray16( new double[ 16 ] );
	}

	private void set( double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22 ) {
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

	public void set( Vector3 right, Vector3 up, Vector3 backward ) {
		this.right.set( right );
		this.up.set( up );
		this.backward.set( backward );
	}

	private void scale( double d ) {
		right.multiply( d );
		up.multiply( d );
		backward.multiply( d );
	}

	public double determinate() {
		return ( ( right.x * up.y * backward.z ) + ( right.y * up.z * backward.x ) + ( right.z * up.x * backward.y ) ) - ( right.x * up.z * backward.y ) - ( right.z * up.y * backward.x ) - ( right.y * up.x * backward.z );
	}

	public void invert() {
		double d = determinate();
		set(
				( up.y * backward.z ) - ( backward.y * up.z ), ( backward.x * up.z ) - ( up.x * backward.z ), ( up.x * backward.y ) - ( backward.x * up.y ),
				( backward.y * right.z ) - ( right.y * backward.z ), ( right.x * backward.z ) - ( backward.x * right.z ), ( backward.x * right.y ) - ( right.x * backward.y ),
				( right.y * up.z ) - ( up.y * right.z ), ( up.x * right.z ) - ( right.x * up.z ), ( right.x * up.y ) - ( up.x * right.y ) );
		scale( 1.0 / d );
	}

	protected void setToMultiplication( AbstractMatrix3x3 a, AbstractMatrix3x3 b ) {
		double m00 = ( a.right.x * b.right.x ) + ( a.up.x * b.right.y ) + ( a.backward.x * b.right.z );
		double m01 = ( a.right.x * b.up.x ) + ( a.up.x * b.up.y ) + ( a.backward.x * b.up.z );
		double m02 = ( a.right.x * b.backward.x ) + ( a.up.x * b.backward.y ) + ( a.backward.x * b.backward.z );

		double m10 = ( a.right.y * b.right.x ) + ( a.up.y * b.right.y ) + ( a.backward.y * b.right.z );
		double m11 = ( a.right.y * b.up.x ) + ( a.up.y * b.up.y ) + ( a.backward.y * b.up.z );
		double m12 = ( a.right.y * b.backward.x ) + ( a.up.y * b.backward.y ) + ( a.backward.y * b.backward.z );

		double m20 = ( a.right.z * b.right.x ) + ( a.up.z * b.right.y ) + ( a.backward.z * b.right.z );
		double m21 = ( a.right.z * b.up.x ) + ( a.up.z * b.up.y ) + ( a.backward.z * b.up.z );
		double m22 = ( a.right.z * b.backward.x ) + ( a.up.z * b.backward.y ) + ( a.backward.z * b.backward.z );

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

	protected void applyMultiplication( AbstractMatrix3x3 b ) {
		setToMultiplication( this, b );
	}

	public <E extends Tuple3> E transform( E rv ) {
		double x = ( right.x * rv.x ) + ( up.x * rv.y ) + ( backward.x * rv.z );
		double y = ( right.y * rv.x ) + ( up.y * rv.y ) + ( backward.y * rv.z );
		double z = ( right.z * rv.x ) + ( up.z * rv.y ) + ( backward.z * rv.z );
		rv.x = x;
		rv.y = y;
		rv.z = z;
		return rv;
	}

	public <E extends Tuple3f> E transform( E rv ) {
		float x = (float)( ( right.x * rv.x ) + ( up.x * rv.y ) + ( backward.x * rv.z ) );
		float y = (float)( ( right.y * rv.x ) + ( up.y * rv.y ) + ( backward.y * rv.z ) );
		float z = (float)( ( right.z * rv.x ) + ( up.z * rv.y ) + ( backward.z * rv.z ) );
		rv.x = x;
		rv.y = y;
		rv.z = z;
		return rv;
	}

	public Vector3 createForward() {
		return Vector3.createNegation( backward );
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) {
			return true;
		} else {
			if( o instanceof Matrix3x3 ) {
				Matrix3x3 other = (Matrix3x3)o;
				return Objects.equals( this.right, other.right )
						&& Objects.equals( this.up, other.up )
						&& Objects.equals( this.backward, other.backward );
			} else {
				return false;
			}
		}
	}

	@Override
	public int hashCode() {
		int rv = 17;
		if( this.right != null ) {
			rv = ( 37 * rv ) + this.right.hashCode();
		}
		if( this.up != null ) {
			rv = ( 37 * rv ) + this.up.hashCode();
		}
		if( this.backward != null ) {
			rv = ( 37 * rv ) + this.backward.hashCode();
		}
		return rv;
	}

	public boolean isWithinEpsilonOfIdentity( double epsilon ) {
		return this.right.isWithinEpsilonOf( 1.0, 0.0, 0.0, epsilon ) &&
				this.up.isWithinEpsilonOf( 0.0, 1.0, 0.0, epsilon ) &&
				this.backward.isWithinEpsilonOf( 0.0, 0.0, 1.0, epsilon );
	}

	public boolean isWithinReasonableEpsilonOfIdentity() {
		return this.isWithinEpsilonOfIdentity( EpsilonUtilities.REASONABLE_EPSILON );
	}

	public boolean isWithinEpsilonOf( AbstractMatrix3x3 other, double epsilon ) {
		return this.right.isWithinEpsilonOf( other.right, epsilon ) &&
				this.up.isWithinEpsilonOf( other.up, epsilon ) &&
				this.backward.isWithinEpsilonOf( other.backward, epsilon );
	}

	public boolean isWithinReasonableEpsilonOf( AbstractMatrix3x3 other ) {
		return this.isWithinEpsilonOf( other, EpsilonUtilities.REASONABLE_EPSILON );
	}


	void transformVector(double[] afRV, int offsetDest, double[] afSrc, int offsetSrc) {
		afRV[ offsetDest ] = ( right.x * afSrc[ offsetSrc ] ) + ( up.x * afSrc[ offsetSrc + 1 ] ) + ( backward.x * afSrc[ offsetSrc + 2 ] );
		afRV[ offsetDest + 1 ] = ( right.y * afSrc[ offsetSrc ] ) + ( up.y * afSrc[ offsetSrc + 1 ] ) + ( backward.y * afSrc[ offsetSrc + 2 ] );
		afRV[ offsetDest + 2 ] = ( right.z * afSrc[ offsetSrc ] ) + ( up.z * afSrc[ offsetSrc + 1 ] ) + ( backward.z * afSrc[ offsetSrc + 2 ] );
	}

	public void transformVector(double[] afRV, int offsetDest, float[] afSrc, int offsetSrc ) {
		afRV[ offsetDest ] = ( right.x * afSrc[ offsetSrc ] ) + ( up.x * afSrc[ offsetSrc + 1 ] ) + ( backward.x * afSrc[ offsetSrc + 2 ] );
		afRV[ offsetDest + 1 ] = ( right.y * afSrc[ offsetSrc ] ) + ( up.y * afSrc[ offsetSrc + 1 ] ) + ( backward.y * afSrc[ offsetSrc + 2 ] );
		afRV[ offsetDest + 2 ] = ( right.z * afSrc[ offsetSrc ] ) + ( up.z * afSrc[ offsetSrc + 1 ] ) + ( backward.z * afSrc[ offsetSrc + 2 ] );
	}

	public void transformVector(float[] afRV, int offsetDest, float[] afSrc, int offsetSrc ) {
		afRV[ offsetDest ] = (float) (( right.x * afSrc[ offsetSrc ] ) + ( up.x * afSrc[ offsetSrc + 1 ] ) + ( backward.x * afSrc[ offsetSrc + 2 ] ));
		afRV[ offsetDest + 1 ] = (float) (( right.y * afSrc[ offsetSrc ] ) + ( up.y * afSrc[ offsetSrc + 1 ] ) + ( backward.y * afSrc[ offsetSrc + 2 ] ));
		afRV[ offsetDest + 2 ] = (float) (( right.z * afSrc[ offsetSrc ] ) + ( up.z * afSrc[ offsetSrc + 1 ] ) + ( backward.z * afSrc[ offsetSrc + 2 ] ));
	}
}
