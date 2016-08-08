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
package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author alice
 */
public class HorizontalSurface extends edu.cmu.cs.dennisc.scenegraph.TexturedVisual {
	public HorizontalSurface( boolean faceUp, float size, float height, float tiling ) {
		this.faceUp = faceUp;
		float k = this.getK();
		for( edu.cmu.cs.dennisc.scenegraph.Vertex vertex : sgVertices ) {
			vertex.normal.set( 0, k, 0 );
		}
		this.setTiling( tiling, tiling );
		this.setSize( size, size, height );
		this.sgGeometry.vertices.setValue( this.sgVertices );
		this.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgGeometry } );
	}

	public HorizontalSurface( boolean faceUp ) {
		this( faceUp, 1, 1, 1 );
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.Geometry getGeometry()
	{
		return this.sgGeometry;
	}

	public void setTiling( float xTiling, float yTiling ) {
		if( ( xTiling == 1 ) && ( yTiling == 1 ) ) {
			this.getAppearance().isDiffuseColorTextureClamped.setValue( true );
		}
		else {
			this.getAppearance().isDiffuseColorTextureClamped.setValue( false );
		}
		edu.cmu.cs.dennisc.scenegraph.Vertex v0 = sgVertices[ this.getIndex( 0 ) ];
		v0.textureCoordinate0.u = xTiling;
		v0.textureCoordinate0.v = 0;

		edu.cmu.cs.dennisc.scenegraph.Vertex v1 = sgVertices[ this.getIndex( 1 ) ];
		v1.textureCoordinate0.u = xTiling;
		v1.textureCoordinate0.v = yTiling;

		edu.cmu.cs.dennisc.scenegraph.Vertex v2 = sgVertices[ this.getIndex( 2 ) ];
		v2.textureCoordinate0.u = 0;
		v2.textureCoordinate0.v = yTiling;

		edu.cmu.cs.dennisc.scenegraph.Vertex v3 = sgVertices[ this.getIndex( 3 ) ];
		v3.textureCoordinate0.u = 0;
		v3.textureCoordinate0.v = 0;
	}

	public void setSize( float width, float depth, float height ) {
		float x = width / 2;
		float z = depth / 2;
		edu.cmu.cs.dennisc.scenegraph.Vertex v0 = sgVertices[ this.getIndex( 0 ) ];
		v0.position.x = -x;
		v0.position.y = height;
		v0.position.z = +z;

		edu.cmu.cs.dennisc.scenegraph.Vertex v1 = sgVertices[ this.getIndex( 1 ) ];
		v1.position.x = -x;
		v1.position.y = height;
		v1.position.z = -z;

		edu.cmu.cs.dennisc.scenegraph.Vertex v2 = sgVertices[ this.getIndex( 2 ) ];
		v2.position.x = +x;
		v2.position.y = height;
		v2.position.z = -z;

		edu.cmu.cs.dennisc.scenegraph.Vertex v3 = sgVertices[ this.getIndex( 3 ) ];
		v3.position.x = +x;
		v3.position.y = height;
		v3.position.z = +z;
	}

	private int getIndex( int i ) {
		if( this.faceUp ) {
			return sgVertices.length - 1 - i;
		} else {
			return i;
		}
	}

	private float getK() {
		if( this.faceUp ) {
			return 1.0f;
		} else {
			return -1.0f;
		}
	}

	private final edu.cmu.cs.dennisc.scenegraph.QuadArray sgGeometry = new edu.cmu.cs.dennisc.scenegraph.QuadArray();
	private final edu.cmu.cs.dennisc.scenegraph.Vertex[] sgVertices = new edu.cmu.cs.dennisc.scenegraph.Vertex[] {
			edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 0 ),
			edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 0 ),
			edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 1 ),
			edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 1 )
	};
	private final boolean faceUp;
}
