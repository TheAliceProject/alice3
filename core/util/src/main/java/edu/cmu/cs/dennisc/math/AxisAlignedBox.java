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
public final class AxisAlignedBox implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private final Point3 minimum = Point3.createNaN();
	private final Point3 maximum = Point3.createNaN();

	public static AxisAlignedBox createNaN() {
		return new AxisAlignedBox( Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN );
	}

	public AxisAlignedBox() {
	}

	public AxisAlignedBox( Point3 minimum, Point3 maximum ) {
		setMinimum( minimum );
		setMaximum( maximum );
	}

	public AxisAlignedBox( double minimumX, double minimumY, double minimumZ, double maximumX, double maximumY, double maximumZ ) {
		setMinimum( minimumX, minimumY, minimumZ );
		setMaximum( maximumX, maximumY, maximumZ );
	}

	public AxisAlignedBox( AxisAlignedBox other ) {
		set( other );
	}

	public AxisAlignedBox( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.minimum.decode( binaryDecoder );
		this.maximum.decode( binaryDecoder );
	}

	//	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
	//		m_minimum.decode( binaryDecoder );
	//		m_maximum.decode( binaryDecoder );
	//	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		this.minimum.encode( binaryEncoder );
		this.maximum.encode( binaryEncoder );
	}

	@Override
	public boolean equals( Object o ) {
		if( o == this ) {
			return true;
		}
		if( ( o != null ) && ( o instanceof AxisAlignedBox ) ) {
			AxisAlignedBox box = (AxisAlignedBox)o;
			return this.minimum.equals( box.minimum ) && this.maximum.equals( box.maximum );
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int rv = 17;
		if( this.minimum != null ) {
			rv = ( 37 * rv ) + this.minimum.hashCode();
		}
		if( this.maximum != null ) {
			rv = ( 37 * rv ) + this.maximum.hashCode();
		}
		return rv;
	}

	public void set( AxisAlignedBox other ) {
		if( other != null ) {
			this.minimum.set( other.minimum );
			this.maximum.set( other.maximum );
		} else {
			setNaN();
		}
	}

	public boolean isNaN() {
		return this.minimum.isNaN() || this.maximum.isNaN();
	}

	public void setNaN() {
		this.minimum.set( Double.NaN, Double.NaN, Double.NaN );
		this.maximum.set( Double.NaN, Double.NaN, Double.NaN );
	}

	public Point3 getMinimum( Point3 rv ) {
		rv.set( this.minimum );
		return rv;
	}

	public Point3 getMinimum() {
		return getMinimum( new Point3() );
	}

	public void setMinimum( Point3 minimum ) {
		if( minimum == null ) {
			this.minimum.set( Double.NaN, Double.NaN, Double.NaN );
		} else {
			this.minimum.set( minimum );
		}
	}

	public void setMinimum( double x, double y, double z ) {
		this.minimum.set( x, y, z );
	}

	public Point3 getMaximum( Point3 rv ) {
		rv.set( this.maximum );
		return rv;
	}

	public Point3 getMaximum() {
		return getMaximum( new Point3() );
	}

	public void setMaximum( Point3 maximum ) {
		if( maximum == null ) {
			this.maximum.set( Double.NaN, Double.NaN, Double.NaN );
		} else {
			this.maximum.set( maximum );
		}
	}

	public void setMaximum( double x, double y, double z ) {
		this.maximum.set( x, y, z );
	}

	public double getXMinimum() {
		return this.minimum.x;
	}

	public void setXMinimum( double v ) {
		this.minimum.x = v;
	}

	public double getYMinimum() {
		return this.minimum.y;
	}

	public void setYMinimum( double v ) {
		this.minimum.y = v;
	}

	public double getZMinimum() {
		return this.minimum.z;
	}

	public void setZMinimum( double v ) {
		this.minimum.z = v;
	}

	public double getXMaximum() {
		return this.maximum.x;
	}

	public void setXMaximum( double v ) {
		this.maximum.x = v;
	}

	public double getYMaximum() {
		return this.maximum.y;
	}

	public void setYMaximum( double v ) {
		this.maximum.y = v;
	}

	public double getZMaximum() {
		return this.maximum.z;
	}

	public void setZMaximum( double v ) {
		this.maximum.z = v;
	}

	public Point3 getCenter( Point3 rv ) {
		rv.set( ( this.minimum.x + this.maximum.x ) / 2, ( this.minimum.y + this.maximum.y ) / 2, ( this.minimum.z + this.maximum.z ) / 2 );
		return rv;
	}

	public Point3 getCenter() {
		return getCenter( new Point3() );
	}

	public Point3 getCenterOfFrontFace( Point3 rv ) {
		rv.set( ( this.minimum.x + this.maximum.x ) / 2, ( this.minimum.y + this.maximum.y ) / 2, ( this.minimum.z ) );
		return rv;
	}

	public Point3 getCenterOfFrontFace() {
		return getCenterOfFrontFace( new Point3() );
	}

	public Point3 getCenterOfBackFace( Point3 rv ) {
		rv.set( ( this.minimum.x + this.maximum.x ) / 2, ( this.minimum.y + this.maximum.y ) / 2, ( this.maximum.z ) );
		return rv;
	}

	public Point3 getCenterOfBackFace() {
		return getCenterOfBackFace( new Point3() );
	}

	public Point3 getCenterOfLeftFace( Point3 rv ) {
		rv.set( ( this.minimum.x ), ( this.minimum.y + this.maximum.y ) / 2, ( this.minimum.z + this.maximum.z ) / 2 );
		return rv;
	}

	public Point3 getCenterOfLeftFace() {
		return getCenterOfLeftFace( new Point3() );
	}

	public Point3 getCenterOfRightFace( Point3 rv ) {
		rv.set( ( this.maximum.x ), ( this.minimum.y + this.maximum.y ) / 2, ( this.minimum.z + this.maximum.z ) / 2 );
		return rv;
	}

	public Point3 getCenterOfRightFace() {
		return getCenterOfRightFace( new Point3() );
	}

	public Point3 getCenterOfTopFace( Point3 rv ) {
		rv.set( ( this.minimum.x + this.maximum.x ) / 2, ( this.maximum.y ), ( this.minimum.z + this.maximum.z ) / 2 );
		return rv;
	}

	public Point3 getCenterOfTopFace() {
		return getCenterOfTopFace( new Point3() );
	}

	public Point3 getCenterOfBottomFace( Point3 rv ) {
		rv.set( ( this.minimum.x + this.maximum.x ) / 2, ( this.minimum.y ), ( this.minimum.z + this.maximum.z ) / 2 );
		return rv;
	}

	public Point3 getCenterOfBottomFace() {
		return getCenterOfBottomFace( new Point3() );
	}

	public double getWidth() {
		return this.maximum.x - this.minimum.x;
	}

	public double getHeight() {
		return this.maximum.y - this.minimum.y;
	}

	public double getDepth() {
		return this.maximum.z - this.minimum.z;
	}

	public Dimension3 getSize() {
		return new Dimension3( this.getWidth(), this.getHeight(), this.getDepth() );
	}

	public double getVolume() {
		return getWidth() * getHeight() * getDepth();
	}

	public double getDiagonal() {
		if( isNaN() )
		{
			return Double.NaN;
		}
		return Point3.calculateDistanceBetween( this.getMinimum(), this.getMaximum() );
	}

	public void union( Point3 p ) {
		if( this.minimum.isNaN() ) {
			this.minimum.set( p );
		}
		else
		{
			this.minimum.x = Math.min( this.minimum.x, p.x );
			this.minimum.y = Math.min( this.minimum.y, p.y );
			this.minimum.z = Math.min( this.minimum.z, p.z );
		}
		if( this.maximum.isNaN() )
		{
			this.maximum.set( p );
		}
		else
		{
			this.maximum.x = Math.max( this.maximum.x, p.x );
			this.maximum.y = Math.max( this.maximum.y, p.y );
			this.maximum.z = Math.max( this.maximum.z, p.z );
		}
	}

	public void union( AxisAlignedBox other ) {
		assert other != null;
		if( isNaN() ) {
			if( other.isNaN() ) {
				//pass
			} else {
				set( other );
			}
		} else {
			if( other.isNaN() ) {
				//pass
			} else {
				this.minimum.x = Math.min( this.minimum.x, other.minimum.x );
				this.minimum.y = Math.min( this.minimum.y, other.minimum.y );
				this.minimum.z = Math.min( this.minimum.z, other.minimum.z );
				this.maximum.x = Math.max( this.maximum.x, other.maximum.x );
				this.maximum.y = Math.max( this.maximum.y, other.maximum.y );
				this.maximum.z = Math.max( this.maximum.z, other.maximum.z );
			}
		}
	}

	public Point3[] getPoints() {
		Point3[] points = {
				new Point3( this.minimum.x, this.minimum.y, this.minimum.z ),
				new Point3( this.maximum.x, this.minimum.y, this.minimum.z ),
				new Point3( this.minimum.x, this.maximum.y, this.minimum.z ),
				new Point3( this.maximum.x, this.maximum.y, this.minimum.z ),
				new Point3( this.minimum.x, this.minimum.y, this.maximum.z ),
				new Point3( this.maximum.x, this.minimum.y, this.maximum.z ),
				new Point3( this.minimum.x, this.maximum.y, this.maximum.z ),
				new Point3( this.maximum.x, this.maximum.y, this.maximum.z )
		};
		return points;
	}

	public Hexahedron getHexahedron( Hexahedron rv ) {
		Point3[] points = this.getPoints();
		Vector3[] normals = {
				new Vector3( +1, 0, 0 ),
				new Vector3( -1, 0, 0 ),
				new Vector3( 0, +1, 0 ),
				new Vector3( 0, -1, 0 ),
				new Vector3( 0, 0, +1 ),
				new Vector3( 0, 0, -1 )
		};
		rv.set( points, normals );
		return rv;
	}

	public Hexahedron getHexahedron() {
		return getHexahedron( Hexahedron.createNaN() );
	}

	public void translate( Vector3 v ) {
		this.minimum.add( v );
		this.maximum.add( v );
	}

	public void scale( Matrix3x3 m ) {
		//		todo?
		//		m.transform( this.minimum );
		//		m.transform( this.maximum );

		double xScale = Tuple3.calculateMagnitude( m.right.x, m.right.y, m.right.z );
		double yScale = Tuple3.calculateMagnitude( m.up.x, m.up.y, m.up.z );
		double zScale = Tuple3.calculateMagnitude( m.backward.x, m.backward.y, m.backward.z );

		this.minimum.x *= xScale;
		this.maximum.x *= xScale;

		this.minimum.y *= yScale;
		this.maximum.y *= yScale;

		this.minimum.z *= zScale;
		this.maximum.z *= zScale;
	}

	@Override
	public String toString() {
		return AxisAlignedBox.class.getName() + "[minimum=" + this.minimum + ",maximum=" + this.maximum + "]";
	}
}
