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

import edu.cmu.cs.dennisc.scenegraph.*;

/**
 * @author Dennis Cosgrove
 */
public class ConsolidateMeshes extends edu.cmu.cs.dennisc.batch.Batch {
	private static edu.cmu.cs.dennisc.math.Point3[] convertToPositions( double[] xyzs ) {
		java.util.ArrayList< edu.cmu.cs.dennisc.math.Point3 > points = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		final int N = xyzs.length;
		points.ensureCapacity( N / 3 );
		for( int i = 0; i < N; i += 3 ) {
			points.add( new edu.cmu.cs.dennisc.math.Point3( xyzs[ i + 0 ], xyzs[ i + 1 ], xyzs[ i + 2 ] ) );
		}
		return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( points, edu.cmu.cs.dennisc.math.Point3.class );
	}
	private static edu.cmu.cs.dennisc.math.Vector3f[] convertToNormals( float[] ijks ) {
		java.util.ArrayList< edu.cmu.cs.dennisc.math.Vector3f > normals = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		final int N = ijks.length;
		normals.ensureCapacity( N / 3 );
		for( int i = 0; i < N; i += 3 ) {
			normals.add( new edu.cmu.cs.dennisc.math.Vector3f( ijks[ i + 0 ], ijks[ i + 1 ], ijks[ i + 2 ] ) );
		}
		return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( normals, edu.cmu.cs.dennisc.math.Vector3f.class );
	}
	private static edu.cmu.cs.dennisc.texture.TextureCoordinate2f[] convertToTextureCoordinates( float[] uvs ) {
		java.util.ArrayList< edu.cmu.cs.dennisc.texture.TextureCoordinate2f > normals = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		final int N = uvs.length;
		normals.ensureCapacity( N / 2 );
		for( int i = 0; i < N; i += 2 ) {
			normals.add( new edu.cmu.cs.dennisc.texture.TextureCoordinate2f( uvs[ i + 0 ], uvs[ i + 1 ] ) );
		}
		return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( normals, edu.cmu.cs.dennisc.texture.TextureCoordinate2f.class );
	}

	static class Vertex {
		private short positionIndex;
		private short normalIndex;
		private short textureCoordIndex;

		public Vertex( short positionIndex, short normalIndex, short textureCoordIndex ) {
			this.positionIndex = positionIndex;
			this.normalIndex = normalIndex;
			this.textureCoordIndex = textureCoordIndex;
		}

		@Override
		public boolean equals( Object o ) {
			if( this == o ) {
				return true;
			} else {
				if( o instanceof Vertex ) {
					Vertex other = (Vertex)o;
					return this.positionIndex == other.positionIndex && this.normalIndex == other.normalIndex && this.textureCoordIndex == other.textureCoordIndex;
				} else {
					return false;
				}
			}
		}
		@Override
		public int hashCode() {
			int rv = 17;
			rv = 37*rv + this.positionIndex;
			rv = 37*rv + this.normalIndex;
			rv = 37*rv + this.textureCoordIndex;
			return rv;
		}
	}

	private static void check( int positionsLength, int normalsLength, int textureCoordinatesLength, short[] positionIndices, short[] normalIndices, short[] textureCoordIndices ) {
		final int N = positionIndices.length;
		for( int i = 0; i < N; i++ ) {
			short positionIndex = positionIndices[ i ];
			short normalIndex = normalIndices[ i ];
			short textureCoordIndex = textureCoordIndices[ i ];
			assert positionIndex < positionsLength;
			assert normalIndex < normalsLength;
			assert textureCoordIndex < textureCoordinatesLength;
		}
	}

	private static java.util.Set< Vertex > updateVertexSet( java.util.Set< Vertex > rv, short[] positionIndices, short[] normalIndices, short[] textureCoordIndices ) {
		final int N = positionIndices.length;
		for( int i = 0; i < N; i++ ) {
			short positionIndex = positionIndices[ i ];
			short normalIndex = normalIndices[ i ];
			short textureCoordIndex = textureCoordIndices[ i ];
			Vertex v = new Vertex( positionIndex, normalIndex, textureCoordIndex );
			rv.add( v );
		}
		return rv;
	}
	private static short[] createIndices( short[] positionIndices, short[] normalIndices, short[] textureCoordIndices, java.util.Map< Vertex, Short > map ) {
		final int N = positionIndices.length;
		short[] rv = new short[ N ];
		for( int i=0; i<N; i++ ) {
			short positionIndex = positionIndices[ i ];
			short normalIndex = normalIndices[ i ];
			short textureCoordIndex = textureCoordIndices[ i ];
			Vertex v = new Vertex( positionIndex, normalIndex, textureCoordIndex );
			assert map.containsKey( v );
			rv[ i ] = map.get( v ); 
		}
		return rv;
	}

	private static Mesh arrayize( Mesh rv ) {
		short[] xyzTriangleIndices = rv.xyzTriangleIndices.getValue();
		short[] ijkTriangleIndices = rv.ijkTriangleIndices.getValue();
		short[] uvTriangleIndices = rv.uvTriangleIndices.getValue();

		short[] xyzQuadrangleIndices = rv.xyzQuadrangleIndices.getValue();
		short[] ijkQuadrangleIndices = rv.ijkQuadrangleIndices.getValue();
		short[] uvQuadrangleIndices = rv.uvQuadrangleIndices.getValue();

//		java.util.Map< Short, Short > mapIJKIndexToXYZIndex = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
//		java.util.Map< Short, Short > mapUVIndexToXYZIndex = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

		assert xyzTriangleIndices.length == ijkTriangleIndices.length;
		assert xyzTriangleIndices.length == uvTriangleIndices.length;
		assert xyzQuadrangleIndices.length == ijkQuadrangleIndices.length;
		assert xyzQuadrangleIndices.length == uvQuadrangleIndices.length;

		edu.cmu.cs.dennisc.math.Point3[] prevPositions = convertToPositions( rv.xyzs.getValue() );
		edu.cmu.cs.dennisc.math.Vector3f[] prevNormals = convertToNormals( rv.ijks.getValue() );
		edu.cmu.cs.dennisc.texture.TextureCoordinate2f[] prevTextureCoordinates = convertToTextureCoordinates( rv.uvs.getValue() );

		check( prevPositions.length, prevNormals.length, prevTextureCoordinates.length, xyzTriangleIndices, ijkTriangleIndices, uvTriangleIndices );
		check( prevPositions.length, prevNormals.length, prevTextureCoordinates.length, xyzQuadrangleIndices, ijkQuadrangleIndices, uvQuadrangleIndices );

		java.util.Set< Vertex > vertices = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
		updateVertexSet( vertices, xyzTriangleIndices, ijkTriangleIndices, uvTriangleIndices );
		updateVertexSet( vertices, xyzQuadrangleIndices, ijkQuadrangleIndices, uvQuadrangleIndices );


		int min = edu.cmu.cs.dennisc.java.lang.MathUtilities.minInt( prevPositions.length, prevNormals.length, prevTextureCoordinates.length, vertices.size() );
		int max = edu.cmu.cs.dennisc.java.lang.MathUtilities.maxInt( prevPositions.length, prevNormals.length, prevTextureCoordinates.length, vertices.size() );

		final int N = vertices.size();
		if( N < Short.MAX_VALUE ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( points.length, normals.length, textureCoordinates.length, vertices.size() );
			double factor = max / (double)min;
			if( factor > 2.0 ) {
				System.err.println( "WARNING: over threshold: " +  factor);
				edu.cmu.cs.dennisc.print.PrintUtilities.println( System.err, prevPositions.length, prevNormals.length, prevTextureCoordinates.length, vertices.size() );
				//assert false;
			}
			double[] nextXYZs = new double[ N*3 ];
			float[] nextIJKs = new float[ N*3 ];
			float[] nextUVs = new float[ N*2 ];
			int iXYZ = 0;
			int iIJK = 0;
			int iUV = 0;
			
			short iVertex = 0;
			java.util.Map< Vertex, Short > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			for( Vertex vertex : vertices ) {
				edu.cmu.cs.dennisc.math.Point3 position = prevPositions[ vertex.positionIndex ];
				nextXYZs[ iXYZ++ ] = position.x;
				nextXYZs[ iXYZ++ ] = position.y;
				nextXYZs[ iXYZ++ ] = position.z;
				edu.cmu.cs.dennisc.math.Vector3f normal = prevNormals[ vertex.normalIndex ];
				nextIJKs[ iIJK++ ] = normal.x;
				nextIJKs[ iIJK++ ] = normal.y;
				nextIJKs[ iIJK++ ] = normal.z;
				edu.cmu.cs.dennisc.texture.TextureCoordinate2f textureCoordinate = prevTextureCoordinates[ vertex.textureCoordIndex ];
				nextUVs[ iUV++ ] = textureCoordinate.u;
				nextUVs[ iUV++ ] = textureCoordinate.v;
				
				map.put( vertex, iVertex++ );
			}
			
			rv.xyzs.setValue( nextXYZs );
			rv.ijks.setValue( nextIJKs );
			rv.uvs.setValue( nextUVs );
			
			rv.xyzTriangleIndices.setValue( createIndices( xyzTriangleIndices, ijkTriangleIndices, uvTriangleIndices, map ) );
			rv.ijkTriangleIndices.setValue( null );
			rv.uvTriangleIndices.setValue( null );
			
			rv.xyzQuadrangleIndices.setValue( createIndices( xyzQuadrangleIndices, ijkQuadrangleIndices, uvQuadrangleIndices, map ) );
			rv.ijkQuadrangleIndices.setValue( null );
			rv.uvQuadrangleIndices.setValue( null );
		} else {
			System.err.println( "vertex count greater than max short: " + vertices.size() );
			//assert false;
		}

		return rv;
	}

	@Override
	protected boolean isSkipExistingOutFilesDesirable() {
		return edu.cmu.cs.dennisc.lang.SystemUtilities.isPropertyTrue( "isSkipExistingOutFilesDesirable" );
	}
	@Override
	protected void handle( java.io.File inFile, java.io.File outFile ) {
		try {
			edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder modelBuilder = edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.getInstance( inFile );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( inFile );
			for( Geometry geometry : modelBuilder.getGeometries() ) {
				if( geometry instanceof Mesh ) {
					Mesh mesh = (Mesh)geometry;
					arrayize( mesh );
				} else {
					assert false;
				}
			}
			modelBuilder.encode( outFile );
			edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.forget( inFile );
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}

	public static void main( String[] args ) throws Exception {
		final String ROOT = System.getProperty( "user.home" ) + "/Desktop/gallery_src/";
		String subsetOrFull;
		if( args.length > 0 ) {
			subsetOrFull = args[ 0 ];
		} else {
			subsetOrFull = "full";
		}
		ConsolidateMeshes batch = new ConsolidateMeshes();
		for( int i=0; i<3; i++ ) {
			batch.process( ROOT + subsetOrFull + "/smoothed_" + i + "/", ROOT + subsetOrFull + "/consolidated_" + i + "/", "zip", "zip" );
		}
	}
}
