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
public class Hexahedron implements java.io.Serializable {
	public enum RightOrLeft {
		RIGHT(0x0), LEFT(0x1);
		private int m_mask;

		RightOrLeft( int mask ) {
			m_mask = mask;
		}

		public int getMask() {
			return m_mask;
		}
	}

	public enum TopOrBottom {
		TOP(0x0), BOTTOM(0x2);
		private int m_mask;

		TopOrBottom( int mask ) {
			m_mask = mask;
		}

		public int getMask() {
			return m_mask;
		}
	}

	public enum BackOrFront {
		BACK(0x0), FRONT(0x4);
		private int m_mask;

		BackOrFront( int mask ) {
			m_mask = mask;
		}

		public int getMask() {
			return m_mask;
		}
	}

	public enum Side {
		RIGHT(0, 0x0, 0x1, 0x2, 0x3), LEFT(1, 0x0, 0x1, 0x2, 0x3), 
		TOP(2, 0x0, 0x1, 0x2, 0x3), BOTTOM(3, 0x0, 0x1, 0x2, 0x3), 
		BACK(4, 0x0, 0x1, 0x2, 0x3), FRONT(5, 0x0, 0x1, 0x2, 0x3);
		private int m_normalIndex;
		private int[] m_pointIndices;

		Side( int normalIndex, int... pointIndices ) {
			assert 0 <= normalIndex && normalIndex < 6;
			assert pointIndices.length == 4;
			for( int pointIndex : pointIndices ) {
				assert 0 <= pointIndex && pointIndex < 8;
			}
			m_normalIndex = normalIndex;
			m_pointIndices = pointIndices;
		}
		public int getNormalIndex() {
			return m_normalIndex;
		}
		public int[] getPointIndices() {
			return m_pointIndices;
		}
		public int getPointIndexAt( int i ) {
			return m_pointIndices[ i ];
		}
	}
	private Point3[] m_points = { 
			new Point3(), 
			new Point3(), 
			new Point3(),
			new Point3(), 
			new Point3(), 
			new Point3(), 
			new Point3(), 
			new Point3() 
	};
	private Vector3[] m_normals = { 
			new Vector3(), 
			new Vector3(), 
			new Vector3(),
			new Vector3(), 
			new Vector3(), 
			new Vector3() 
	};

	public Hexahedron() {
		setNaN();
	}

	public Hexahedron( Point3[] points, Vector3[] normals ) {
		set( points, normals );
	}

	public Hexahedron( Hexahedron other ) {
		set( other );
	}

	protected boolean isEqual( Hexahedron other ) {
		for( int i = 0; i < m_points.length; i++ ) {
			if( m_points[ i ].equals( other.m_points[ i ] ) ) {
				// pass
			} else {
				return false;
			}
		}
		for( int i = 0; i < m_normals.length; i++ ) {
			if( m_normals[ i ].equals( other.m_normals[ i ] ) ) {
				// pass
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals( Object other ) {
		if( other == this ) {
			return true;
		}
		if( other instanceof Hexahedron ) {
			return isEqual( (Hexahedron)other );
		} else {
			return false;
		}
	}


	public void set( Point3[] points, Vector3[] normals ) {
		for( int i = 0; i < m_points.length; i++ ) {
			m_points[ i ].set( points[ i ] );
		}
		for( int i = 0; i < m_normals.length; i++ ) {
			m_normals[ i ].set( normals[ i ] );
		}
	}

	public void set( Hexahedron other ) {
		if( other != null ) {
			set( other.m_points, other.m_normals );
		} else {
			setNaN();
		}
	}

	public void setNaN() {
		for( Point3 point : m_points ) {
			point.setNaN();
		}
		for( Vector3 normal : m_normals ) {
			normal.setNaN();
		}
	}
	public boolean isNaN() {
		for( Point3 point : m_points ) {
			if( point.isNaN() ) {
				return true;
			}
		}
		for( Vector3 normal : m_normals ) {
			if( normal.isNaN() ) {
				return true;
			}
		}
		return false;
	}
	public void transform( AbstractMatrix4x4 m ) {
		for( Point3 point : m_points ) {
			m.transform( point );
		}
		for( Vector3 normal : m_normals ) {
			m.transform( normal );
		}
	}

	@Deprecated
	public Point3 getPointAt( int index ) {
		return new Point3( m_points[ index ] );
	}
	@Deprecated
	public Point3[] getPoints() {
		return m_points;
	}
	@Deprecated
	public Vector3[] getNormals() {
		return m_normals;
	}
	
	private int getPointIndex( RightOrLeft rightOrLeft, TopOrBottom topOrBottom, BackOrFront backOrFront ) {
		return rightOrLeft.getMask() | topOrBottom.getMask() | backOrFront.getMask(); 
	}
	public Point3 getPoint( Point3 rv, RightOrLeft rightOrLeft, TopOrBottom topOrBottom, BackOrFront backOrFront ) {
		rv.set( m_points[ getPointIndex( rightOrLeft, topOrBottom, backOrFront ) ] );
		return rv;
	}
	public Point3 getPoint( RightOrLeft rightOrLeft, TopOrBottom topOrBottom, BackOrFront backOrFront ) {
		return getPoint( new Point3(), rightOrLeft, topOrBottom, backOrFront );
	}

	private Point3[] getPoints( Side side ) {
		Point3[] rv = new Point3[ 4 ];
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = m_points[ side.getPointIndexAt( i ) ];
		}
		return rv;
	}
	public ClippedPlane getClippedPlane( ClippedPlane rv, Side side ) {
		Point3[] points = getPoints( side );
		Vector3 normal = m_normals[ side.getNormalIndex() ];
		rv.set( points, normal );
		return rv;
	}

	public ClippedPlane getClippedPlane( Side side ) {
		return getClippedPlane( new ClippedPlane(), side );
	}
	
	public AxisAlignedBox getAxisAlignedMinimumBoundingBox( AxisAlignedBox rv ) {
		Point3 minimum = new Point3( m_points[ 0 ] );
		Point3 maximum = new Point3( m_points[ 0 ] );
		for( int i=1; i<m_points.length; i++ ) {
			Point3 p = m_points[ i ];
			minimum.x = Math.min( minimum.x, p.x ); 
			minimum.y = Math.min( minimum.y, p.y ); 
			minimum.z = Math.min( minimum.z, p.z ); 
			maximum.x = Math.max( maximum.x, p.x ); 
			maximum.y = Math.max( maximum.y, p.y ); 
			maximum.z = Math.max( maximum.z, p.z ); 
		}
		rv.setMinimum( minimum );
		rv.setMaximum( maximum );
		return rv;
	}
	public AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( new AxisAlignedBox() );
	}
}
