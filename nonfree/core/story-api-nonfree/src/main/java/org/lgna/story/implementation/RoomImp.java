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
package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.HorizontalSurface;
import edu.cmu.cs.dennisc.scenegraph.QuadArray;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.TexturedVisual;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.lgna.story.Paint;
import org.lgna.story.SRoom;

/**
 * @author alice
 *
 */
public class RoomImp extends VisualScaleModelImp {

	private static final float BASE_FLOOR_Y = .001f;

	private static class WallSurface extends TexturedVisual {
		private final QuadArray sgGeometry = new QuadArray();
		private final Vertex[] sgVertices = new Vertex[] { Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 0 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 0 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 1 ),
				Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 1 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 0 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 0 ),
				Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 1 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 1 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 0 ),
				Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 0 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 1 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 1 ),
				Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 0 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 0 ), Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 1, 1 ),
				Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, 0, 1 ),
		};

		public WallSurface( float width, float depth, float height, float xTiling, float zTiling ) {
			for( int i = 0; i < 4; i++ ) {
				Vector3f normal;
				switch( i ) {
				case 0: {
					normal = new Vector3f( 1, 0, 0 );
					break;
				}
				case 1: {
					normal = new Vector3f( 0, 0, -1 );
					break;
				}
				case 2: {
					normal = new Vector3f( -1, 0, 0 );
					break;
				}
				case 3: {
					normal = new Vector3f( 0, 0, 1 );
					break;
				}
				default:
					normal = null;
				}
				Vertex topLeft = sgVertices[ ( i * 4 ) + 0 ];
				topLeft.normal.set( normal );
				Vertex topRight = sgVertices[ ( i * 4 ) + 1 ];
				topRight.normal.set( normal );
				Vertex bottomRight = sgVertices[ ( i * 4 ) + 2 ];
				bottomRight.normal.set( normal );
				Vertex bottomLeft = sgVertices[ ( i * 4 ) + 3 ];
				bottomLeft.normal.set( normal );
			}
			this.setTiling( xTiling, zTiling );
			this.setSize( width, depth, height );
			this.sgGeometry.vertices.setValue( this.sgVertices );
			this.geometries.setValue( new Geometry[] { this.sgGeometry } );
		}

		public WallSurface() {
			this( 1, 1, 1, 1, 1 );
		}

		@Override
		public Geometry getGeometry() {
			return this.sgGeometry;
		}

		public void setTiling( float xTiling, float zTiling ) {
			if( ( xTiling == 1 ) && ( zTiling == 1 ) ) {
				this.getAppearance().isDiffuseColorTextureClamped.setValue( true );
			} else {
				this.getAppearance().isDiffuseColorTextureClamped.setValue( false );
			}
			for( int i = 0; i < 4; i++ ) {
				float tiling;
				if( ( i == 0 ) || ( i == 2 ) ) {
					tiling = zTiling;
				} else {
					tiling = xTiling;
				}
				Vertex topLeft = sgVertices[ ( i * 4 ) + 0 ];
				topLeft.textureCoordinate0.u = 0;
				topLeft.textureCoordinate0.v = 1;
				Vertex topRight = sgVertices[ ( i * 4 ) + 1 ];
				topRight.textureCoordinate0.u = tiling;
				topRight.textureCoordinate0.v = 1;
				Vertex bottomRight = sgVertices[ ( i * 4 ) + 2 ];
				bottomRight.textureCoordinate0.u = tiling;
				bottomRight.textureCoordinate0.v = 0;
				Vertex bottomLeft = sgVertices[ ( i * 4 ) + 3 ];
				bottomLeft.textureCoordinate0.u = 0;
				bottomLeft.textureCoordinate0.v = 0;
			}
		}

		public void setSize( float width, float depth, float height ) {
			float x = width / 2;
			float z = depth / 2;
			float y = height;
			Vector3f leftVals = new Vector3f( 0, BASE_FLOOR_Y, 0 );
			Vector3f rightVals = new Vector3f( 0, y, 0 );
			for( int i = 0; i < 4; i++ ) {

				switch( i ) {
				case 0: {
					leftVals.set( -x, BASE_FLOOR_Y, -z );
					rightVals.set( -x, y, z );
				}
					break;
				case 1: {
					leftVals.set( -x, BASE_FLOOR_Y, z );
					rightVals.set( x, y, z );
				}
					break;
				case 2: {
					leftVals.set( x, BASE_FLOOR_Y, z );
					rightVals.set( x, y, -z );
				}
					break;
				case 3: {
					leftVals.set( x, BASE_FLOOR_Y, -z );
					rightVals.set( -x, y, -z );
				}
					break;
				}
				Vertex topLeft = sgVertices[ ( i * 4 ) + 0 ];
				topLeft.position.set( leftVals.x, y, leftVals.z );
				Vertex topRight = sgVertices[ ( i * 4 ) + 1 ];
				topRight.position.set( rightVals.x, y, rightVals.z );
				Vertex bottomRight = sgVertices[ ( i * 4 ) + 2 ];
				bottomRight.position.set( rightVals.x, BASE_FLOOR_Y, rightVals.z );
				Vertex bottomLeft = sgVertices[ ( i * 4 ) + 3 ];
				bottomLeft.position.set( leftVals.x, BASE_FLOOR_Y, leftVals.z );
			}
		}
	}

	public RoomImp( SRoom abstraction ) {
		this( abstraction, 10, 3, 10 );
	}

	public RoomImp( SRoom abstraction, float width, float height, float depth ) {
		this.abstraction = abstraction;
		this.internalSetSize( width, height, depth );
		this.walls.setParent( this.getSgComposite() );
		this.walls.setTiling( (float)( width / 2 ), (float)( depth / 2 ) );
		this.floor.setParent( this.getSgComposite() );
		this.floor.setTiling( width, depth );
		this.ceiling.setParent( this.getSgComposite() );
		this.ceiling.setTiling( width, depth );

		for( SimpleAppearance sgAppearance : this.sgAppearances ) {
			putInstance( sgAppearance );
		}
	}

	private void internalSetSize( float width, float height, float depth ) {
		this.walls.setSize( width, depth, height );
		this.ceiling.setSize( width, depth, height );
		this.floor.setSize( width, depth, BASE_FLOOR_Y );
	}

	@Override
	public void setSize( Dimension3 size ) {
		internalSetSize( (float)size.x, (float)size.y, (float)size.z );
	}

	@Override
	public SRoom getAbstraction() {
		return this.abstraction;
	}

	@Override
	public Visual[] getSgVisuals() {
		return this.sgVisuals;
	}

	@Override
	protected TexturedAppearance[] getSgPaintAppearances() {
		return this.sgAppearances;
	}

	@Override
	protected SimpleAppearance[] getSgOpacityAppearances() {
		return this.sgAppearances;
	}

	private final SRoom abstraction;

	private final WallSurface walls = new WallSurface();
	private final HorizontalSurface floor = new HorizontalSurface( true );
	private final HorizontalSurface ceiling = new HorizontalSurface( false );

	private final Visual[] sgVisuals = { this.walls, this.floor, this.ceiling };

	private final TexturedAppearance[] sgAppearances = { this.walls.getAppearance(), this.floor.getAppearance(), this.ceiling.getAppearance() };

	public final PaintProperty wallPaint = new PaintProperty( RoomImp.this) {
		@Override
		protected void internalSetValue( Paint value ) {
			TexturedPaintUtilities.setPaint( walls, value );
		}
	};

	public final PaintProperty floorPaint = new PaintProperty( RoomImp.this) {
		@Override
		protected void internalSetValue( Paint value ) {
			TexturedPaintUtilities.setPaint( floor, value );
		}
	};

	public final PaintProperty ceilingPaint = new PaintProperty( RoomImp.this) {
		@Override
		protected void internalSetValue( Paint value ) {
			TexturedPaintUtilities.setPaint( ceiling, value );
		}
	};
}
