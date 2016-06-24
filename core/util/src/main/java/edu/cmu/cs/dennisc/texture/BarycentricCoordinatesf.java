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
package edu.cmu.cs.dennisc.texture;

/**
 * @author Dennis Cosgrove
 */
public final class BarycentricCoordinatesf implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public final float t0;
	public final float t1;
	public final float t2;

	private BarycentricCoordinatesf( float a, float b, float c, float d, float e, float f, float g, float h, float i ) {
		this.t0 = ( ( b * ( f + i ) ) - ( c * ( e + h ) ) ) / ( ( a * ( e + h ) ) - ( b * ( d + g ) ) );
		this.t1 = ( ( a * ( f + i ) ) - ( c * ( d + g ) ) ) / ( ( b * ( d + g ) ) - ( a * ( e + h ) ) );
		this.t2 = 1 - ( t0 + t1 );
	}

	private BarycentricCoordinatesf( java.awt.Point p0, java.awt.Point p1, java.awt.Point p2, java.awt.Point p ) {
		this( p0.x, p0.y, 0, p1.x, p1.y, 0, p2.x, p2.y, 0, p.x, p.y, 0 );
	}

	//	private BarycentricCoordinatesf( edu.cmu.cs.dennisc.math.Tuple3f p0, edu.cmu.cs.dennisc.math.Tuple3f p1, edu.cmu.cs.dennisc.math.Tuple3f p2, edu.cmu.cs.dennisc.math.Tuple3f p ) {
	//		this( p0.x, p0.y, p0.z, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p.x, p.y, p.z );
	//	}
	private BarycentricCoordinatesf( float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x, float y, float z ) {
		this( x0 - x2, x1 - x2, x2 - x, y0 - y2, y1 - y2, y2 - y, z0 - z2, z1 - z2, z2 - z );
	}

	public static BarycentricCoordinatesf createInstance( java.awt.Point p0, java.awt.Point p1, java.awt.Point p2, java.awt.Point p ) {
		return new BarycentricCoordinatesf( p0, p1, p2, p );
	}

	public BarycentricCoordinatesf( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.t0 = binaryDecoder.decodeFloat();
		this.t1 = binaryDecoder.decodeFloat();
		this.t2 = binaryDecoder.decodeFloat();
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( t0 );
		binaryEncoder.encode( t1 );
		binaryEncoder.encode( t2 );
	}

	@Override
	public final boolean equals( Object o ) {
		if( this == o ) {
			return true;
		} else {
			if( o != null ) {
				if( this.getClass().equals( o.getClass() ) ) {
					BarycentricCoordinatesf other = (BarycentricCoordinatesf)o;
					return ( Float.compare( this.t0, other.t0 ) == 0 ) && ( Float.compare( this.t1, other.t1 ) == 0 ) && ( Float.compare( this.t2, other.t2 ) == 0 );
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public final int hashCode() {
		int rv = 17;
		rv = ( 37 * rv ) + this.getClass().hashCode();
		rv = ( 37 * rv ) + Float.floatToIntBits( this.t0 );
		rv = ( 37 * rv ) + Float.floatToIntBits( this.t1 );
		rv = ( 37 * rv ) + Float.floatToIntBits( this.t2 );
		return rv;
	}

	public edu.cmu.cs.dennisc.texture.TextureCoordinate2f createInterpolation( edu.cmu.cs.dennisc.texture.TextureCoordinate2f uv0, edu.cmu.cs.dennisc.texture.TextureCoordinate2f uv1, edu.cmu.cs.dennisc.texture.TextureCoordinate2f uv2 ) {
		float u = 0.0f;
		float v = 0.0f;

		u += this.t0 * uv0.u;
		v += this.t0 * uv0.v;

		u += this.t1 * uv1.u;
		v += this.t1 * uv1.v;

		u += this.t2 * uv2.u;
		v += this.t2 * uv2.v;

		return new TextureCoordinate2f( u, v );
	}
}
