/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class AxisAlignedBox implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	protected Point3 m_minimum = new Point3();
	protected Point3 m_maximum = new Point3();

	public AxisAlignedBox() {
		setNaN();
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
	
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		m_minimum.decode( binaryDecoder );
		m_maximum.decode( binaryDecoder );
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		m_minimum.encode( binaryEncoder );
		m_maximum.encode( binaryEncoder );
	}
	
	@Override
	public boolean equals( Object o ) {
		if( o == this )
			return true;
		if( o != null && o instanceof AxisAlignedBox ) {
			AxisAlignedBox box = (AxisAlignedBox)o;
			return m_minimum.equals( box.m_minimum ) && m_maximum.equals( box.m_maximum );
		} else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		int rv = 17;
		if( this.m_minimum != null ) {
			rv = 37*rv + this.m_minimum.hashCode();
		}
		if( this.m_maximum != null ) {
			rv = 37*rv + this.m_maximum.hashCode();
		}
		return rv;
	}

	public void set( AxisAlignedBox other ) {
		if( other != null ) {
			m_minimum.set( other.m_minimum );
			m_maximum.set( other.m_maximum );
		} else {
			setNaN();
		}
	}

	public boolean isNaN() {
		return m_minimum.isNaN() || m_maximum.isNaN();
	}
	public void setNaN() {
		m_minimum.set( Double.NaN, Double.NaN, Double.NaN );
		m_maximum.set( Double.NaN, Double.NaN, Double.NaN );
	}
	
	public Point3 getMinimum( Point3 rv ) {
		rv.set( m_minimum );
		return rv;
	}
	public Point3 getMinimum() {
		return getMinimum( new Point3() );
	}
	public void setMinimum( Point3 minimum ) {
		if( minimum == null ) {
			m_minimum.set( Double.NaN, Double.NaN, Double.NaN );
		} else {
			m_minimum.set( minimum );
		}
	}
	public void setMinimum( double x, double y, double z ) {
		m_minimum.set( x, y, z );
	}

	public Point3 getMaximum( Point3 rv ) {
		rv.set( m_maximum );
		return rv;
	}
	public Point3 getMaximum() {
		return getMaximum( new Point3() );
	}
	public void setMaximum( Point3 maximum ) {
		if( maximum == null ) {
			m_maximum.set( Double.NaN, Double.NaN, Double.NaN );
		} else {
			m_maximum.set( maximum );
		}
	}
	public void setMaximum( double x, double y, double z ) {
		m_maximum.set( x, y, z );
	}

	public double getXMinimum() {
		return m_minimum.x;
	}
	public void setXMinimum( double v ) {
		m_minimum.x = v;
	}
	public double getYMinimum() {
		return m_minimum.y;
	}
	public void setYMinimum( double v ) {
		m_minimum.y = v;
	}
	public double getZMinimum() {
		return m_minimum.z;
	}
	public void setZMinimum( double v ) {
		m_minimum.z = v;
	}
	public double getXMaximum() {
		return m_maximum.x;
	}
	public void setXMaximum( double v ) {
		m_maximum.x = v;
	}
	public double getYMaximum() {
		return m_maximum.y;
	}
	public void setYMaximum( double v ) {
		m_maximum.y = v;
	}
	public double getZMaximum() {
		return m_maximum.z;
	}
	public void setZMaximum( double v ) {
		m_maximum.z = v;
	}

	public Point3 getCenter( Point3 rv ) {
		rv.set( (m_minimum.x + m_maximum.x) / 2, (m_minimum.y + m_maximum.y) / 2, (m_minimum.z + m_maximum.z) / 2 );
		return rv;
	}
	public Point3 getCenter() {
		return getCenter( new Point3() );
	}

	public Point3 getCenterOfFrontFace( Point3 rv ) {
		rv.set( (m_minimum.x + m_maximum.x) / 2, (m_minimum.y + m_maximum.y) / 2, (m_minimum.z) );
		return rv;
	}
	public Point3 getCenterOfFrontFace() {
		return getCenterOfFrontFace( new Point3() );
	}

	public Point3 getCenterOfBackFace( Point3 rv ) {
		rv.set( (m_minimum.x + m_maximum.x) / 2, (m_minimum.y + m_maximum.y) / 2, (m_maximum.z) );
		return rv;
	}
	public Point3 getCenterOfBackFace() {
		return getCenterOfBackFace( new Point3() );
	}

	public Point3 getCenterOfLeftFace( Point3 rv ) {
		rv.set( (m_minimum.x), (m_minimum.y + m_maximum.y) / 2, (m_minimum.z + m_maximum.z) / 2 );
		return rv;
	}
	public Point3 getCenterOfLeftFace() {
		return getCenterOfLeftFace( new Point3() );
	}

	public Point3 getCenterOfRightFace( Point3 rv ) {
		rv.set( (m_maximum.x), (m_minimum.y + m_maximum.y) / 2, (m_minimum.z + m_maximum.z) / 2 );
		return rv;
	}
	public Point3 getCenterOfRightFace() {
		return getCenterOfRightFace( new Point3() );
	}

	public Point3 getCenterOfTopFace( Point3 rv ) {
		rv.set( (m_minimum.x + m_maximum.x) / 2, (m_maximum.y), (m_minimum.z + m_maximum.z) / 2 );
		return rv;
	}
	public Point3 getCenterOfTopFace() {
		return getCenterOfTopFace( new Point3() );
	}

	public Point3 getCenterOfBottomFace( Point3 rv ) {
		rv.set( (m_minimum.x + m_maximum.x) / 2, (m_minimum.y), (m_minimum.z + m_maximum.z) / 2 );
		return rv;
	}
	public Point3 getCenterOfBottomFace() {
		return getCenterOfBottomFace( new Point3() );
	}

	public double getWidth() {
		return m_maximum.x - m_minimum.x;
	}
	public double getHeight() {
		return m_maximum.y - m_minimum.y;
	}
	public double getDepth() {
		return m_maximum.z - m_minimum.z;
	}

	public double getVolume() {
		return getWidth() * getHeight() * getDepth();
	}

	public void union( Point3 p ) {
	    if( m_minimum.isNaN() ) {
	        m_minimum.set( p );
	    }
	    else
	    {
    		m_minimum.x = Math.min( m_minimum.x, p.x );
    		m_minimum.y = Math.min( m_minimum.y, p.y );
    		m_minimum.z = Math.min( m_minimum.z, p.z );
	    }
	    if ( m_maximum.isNaN() )
	    {
	        m_maximum.set( p );
	    }
	    else
	    {
    		m_maximum.x = Math.max( m_maximum.x, p.x );
    		m_maximum.y = Math.max( m_maximum.y, p.y );
    		m_maximum.z = Math.max( m_maximum.z, p.z );
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
				m_minimum.x = Math.min( m_minimum.x, other.m_minimum.x );
				m_minimum.y = Math.min( m_minimum.y, other.m_minimum.y );
				m_minimum.z = Math.min( m_minimum.z, other.m_minimum.z );
				m_maximum.x = Math.max( m_maximum.x, other.m_maximum.x );
				m_maximum.y = Math.max( m_maximum.y, other.m_maximum.y );
				m_maximum.z = Math.max( m_maximum.z, other.m_maximum.z );
			}
		}
	}

	public Hexahedron getHexahedron( Hexahedron rv ) {
		Point3[] points = { 
				new Point3( m_minimum.x, m_minimum.y, m_minimum.z ), 
				new Point3( m_maximum.x, m_minimum.y, m_minimum.z ), 
				new Point3( m_minimum.x, m_maximum.y, m_minimum.z ),
				new Point3( m_maximum.x, m_maximum.y, m_minimum.z ), 
				new Point3( m_minimum.x, m_minimum.y, m_maximum.z ), 
				new Point3( m_maximum.x, m_minimum.y, m_maximum.z ), 
				new Point3( m_minimum.x, m_maximum.y, m_maximum.z ), 
				new Point3( m_maximum.x, m_maximum.y, m_maximum.z ) 
		};
		Vector3[] normals = { 
				new Vector3( +1, 0, 0 ), 
				new Vector3( -1, 0, 0 ), 
				new Vector3( 0, +1, 0 ),
				new Vector3( 0, -1, 0 ), 
				new Vector3( 0, 0, +1 ), 
				new Vector3( 0, 0, -1 ) 
		};
		rv.set(points, normals);
		return rv;
	}
	public Hexahedron getHexahedron() {
		return getHexahedron( new Hexahedron() );
	}
	
	public void translate( Vector3 v ) {
		m_minimum.add( v );
		m_maximum.add( v );
	}
	public void scale( Matrix3x3 m ) {
		//		todo?
		//		m.transform( m_minimum );
		//		m.transform( m_maximum );

		double xScale = Tuple3.calculateMagnitude( m.right.x, m.right.y, m.right.z );
		double yScale = Tuple3.calculateMagnitude( m.up.x, m.up.y, m.up.z );
		double zScale = Tuple3.calculateMagnitude( m.backward.x, m.backward.y, m.backward.z );

		m_minimum.x *= xScale;
		m_maximum.x *= xScale;

		m_minimum.y *= yScale;
		m_maximum.y *= yScale;

		m_minimum.z *= zScale;
		m_maximum.z *= zScale;
	}

	@Override
	public String toString() {
		return AxisAlignedBox.class.getName() + "[minimum=" + m_minimum + ",maximum=" + m_maximum + "]";
	}
}
