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
public final class ClippedPlane {
	private final Point3[] points = {
			new Point3(),
			new Point3(),
			new Point3(),
			new Point3()
	};
	private final Vector3 normal = new Vector3();

	public ClippedPlane( Point3[] points, Vector3 normal ) {
		set( points, normal );
	}

	private boolean isEqual( ClippedPlane other ) {
		for( int i = 0; i < this.points.length; i++ ) {
			if( this.points[ i ].equals( other.points[ i ] ) ) {
				// pass
			} else {
				return false;
			}
		}
		if( this.normal.equals( other.normal ) ) {
			// pass
		} else {
			return false;
		}
		return true;
	}

	@Override
	public final int hashCode() {
		int rv = 17;
		for( Point3 point : this.points ) {
			rv = ( 37 * rv ) + point.hashCode();
		}
		rv = ( 37 * rv ) + this.normal.hashCode();
		return rv;
	}

	@Override
	public boolean equals( Object other ) {
		if( other == this ) {
			return true;
		}
		if( other instanceof ClippedPlane ) {
			return isEqual( (ClippedPlane)other );
		} else {
			return false;
		}
	}

	public void set( Point3[] points, Vector3 normal ) {
		for( int i = 0; i < this.points.length; i++ ) {
			this.points[ i ].set( points[ i ] );
		}
		this.normal.set( normal );
	}

	public void setNaN() {
		for( Point3 point : this.points ) {
			point.setNaN();
		}
		this.normal.setNaN();
	}

	public boolean isNaN() {
		for( Point3 point : this.points ) {
			if( point.isNaN() ) {
				return true;
			}
		}
		if( this.normal.isNaN() ) {
			return true;
		}
		return false;
	}

	public void transform( AbstractMatrix4x4 m ) {
		for( Point3 point : this.points ) {
			m.transform( point );
		}
		m.transform( this.normal );
	}

	public Plane getPlane() {
		return Plane.createInstance( this.points[ 0 ], this.normal );
	}
}
