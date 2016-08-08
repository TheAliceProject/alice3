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
package edu.cmu.cs.dennisc.scenegraph.util;

import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.IndexedQuadrilateralArray;
import edu.cmu.cs.dennisc.scenegraph.Vertex;

/**
 * @author Dennis Cosgrove
 */
public class ExtravagantConnection extends Connection {
	private static final int DISC_COUNT = 12;
	private static final int CUBIC_COUNT = 24;

	public ExtravagantConnection() {
		Vertex[] vertices = new Vertex[ DISC_COUNT * CUBIC_COUNT ];
		for( int i = 0; i < vertices.length; i++ ) {
			vertices[ i ] = Vertex.createXYZIJK( 0, 0, 0, 0, 1, 0 );
		}
		int[] quadData = new int[ DISC_COUNT * ( CUBIC_COUNT - 1 ) * 4 ];

		int lcv = 0;
		for( int c = 0; c < ( CUBIC_COUNT - 1 ); c++ ) {
			int c0 = c * DISC_COUNT;
			int c1 = ( c + 1 ) * DISC_COUNT;
			for( int d = 0; d < DISC_COUNT; d++ ) {
				quadData[ lcv++ ] = c1 + d;
				if( d == ( DISC_COUNT - 1 ) ) {
					quadData[ lcv++ ] = c1;
					quadData[ lcv++ ] = c0;
				} else {
					quadData[ lcv++ ] = c1 + d + 1;
					quadData[ lcv++ ] = c0 + d + 1;
				}
				quadData[ lcv++ ] = c0 + d;
			}
		}

		this.sgIQA.vertices.setValue( vertices );
		this.sgIQA.polygonData.setValue( quadData );

		for( int d = 0; d < DISC_COUNT; d++ ) {
			this.points[ d ] = new edu.cmu.cs.dennisc.math.Point3();
			this.normals[ d ] = new edu.cmu.cs.dennisc.math.Vector3f();
		}
		setRadius( 1.0 );
		geometries.setValue( new Geometry[] { this.sgIQA } );
	}

	public void update() {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getTarget().getTransformation( this );
		double s = m.translation.calculateMagnitude();
		s *= 5;
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic xHermiteCubic = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.x, 0, -s * m.orientation.backward.x );
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic yHermiteCubic = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.y, 0, -s * m.orientation.backward.y );
		edu.cmu.cs.dennisc.math.polynomial.HermiteCubic zHermiteCubic = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( 0, m.translation.z, -1, -s * m.orientation.backward.z );

		Vertex[] vertices = this.sgIQA.vertices.getValue();
		synchronized( vertices ) {
			double tDelta = 1.0 / CUBIC_COUNT;
			double t = tDelta;
			int lcv = 0;
			for( int c = 0; c < CUBIC_COUNT; c++ ) {
				double x = xHermiteCubic.evaluate( t );
				double y = yHermiteCubic.evaluate( t );
				double z = zHermiteCubic.evaluate( t );

				this.vBuffer.set( zHermiteCubic.evaluateDerivative( t ), yHermiteCubic.evaluateDerivative( t ), zHermiteCubic.evaluateDerivative( t ) );
				this.vBuffer.normalize();
				//this.vBuffer.negate();

				edu.cmu.cs.dennisc.math.Vector3 upGuide;
				if( edu.cmu.cs.dennisc.math.Vector3.isWithinEpsilonOfPositiveOrNegativeYAxis( this.vBuffer, 0.01 ) ) {
					upGuide = edu.cmu.cs.dennisc.math.Vector3.accessPositiveZAxis();
				} else {
					upGuide = edu.cmu.cs.dennisc.math.Vector3.accessPositiveYAxis();
				}

				//todo
				this.mBuffer.setValue( new edu.cmu.cs.dennisc.math.ForwardAndUpGuide( this.vBuffer, upGuide ) );
				//edu.cmu.cs.dennisc.print.PrintUtilities.printlns( this.mBuffer );        

				for( int d = 0; d < DISC_COUNT; d++ ) {
					vertices[ lcv ].position.set( this.points[ d ] );
					this.mBuffer.transform( vertices[ lcv ].position );
					vertices[ lcv ].position.add( new edu.cmu.cs.dennisc.math.Point3( x, y, z ) );
					vertices[ lcv ].normal.set( this.normals[ d ] );
					this.mBuffer.transform( vertices[ lcv ].normal );
					lcv++;
				}
				t += tDelta;
			}
			this.sgIQA.vertices.touch();
		}
	}

	public double getRadius() {
		return this.radius;
	}

	public void setRadius( double baseRadius ) {
		if( this.radius != baseRadius ) {
			this.radius = baseRadius;
			double theta = 0.0;
			double thetaDelta = ( 2.0 * Math.PI ) / DISC_COUNT;
			for( int d = 0; d < DISC_COUNT; d++ ) {
				double c = Math.cos( theta );
				double s = Math.sin( theta );
				this.points[ d ].set( this.radius * c, this.radius * s, 0 );
				this.normals[ d ].set( (float)s, (float)c, 0 );
				theta += thetaDelta;
			}
		}
	}

	private final IndexedQuadrilateralArray sgIQA = new IndexedQuadrilateralArray();

	private final edu.cmu.cs.dennisc.math.Point3[] points = new edu.cmu.cs.dennisc.math.Point3[ DISC_COUNT ];
	private final edu.cmu.cs.dennisc.math.Vector3f[] normals = new edu.cmu.cs.dennisc.math.Vector3f[ DISC_COUNT ];

	private final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 mBuffer = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createNaN();
	private final edu.cmu.cs.dennisc.math.Vector3 vBuffer = edu.cmu.cs.dennisc.math.Vector3.createNaN();
	private double radius = Double.NaN;
}
