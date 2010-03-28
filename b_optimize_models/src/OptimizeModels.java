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
public class OptimizeModels extends edu.cmu.cs.dennisc.batch.Batch {
	private static boolean isSharingVerticesNecessary( edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices ) {
		final int N = vertices.length;
		for( int i=0; i<N; i++ ) {
			edu.cmu.cs.dennisc.scenegraph.Vertex vI = vertices[ i ];
			for( int j=i+1; j<N; j++ ) {
				edu.cmu.cs.dennisc.scenegraph.Vertex vJ = vertices[ j ];
				if( vI.equals( vJ ) ) {
					return true;
				}
			}
		}
		return false;
	}
	private static IndexedTriangleArray shareVertices( IndexedTriangleArray rv ) {
		edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices = rv.vertices.getValue();
		if( isSharingVerticesNecessary( vertices ) ) {
			java.util.Map< Integer, Integer > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			java.util.List< edu.cmu.cs.dennisc.scenegraph.Vertex > sharedVertices = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			final int N = vertices.length;
			for( int i=0; i<N; i++ ) {
				if( map.keySet().contains( i ) ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.scenegraph.Vertex vI = vertices[ i ];
					//assert vI.equals( vI );
					int sharedIndex = sharedVertices.size();
					sharedVertices.add( vI );
					map.put( i, sharedIndex );
					for( int j=i+1; j<N; j++ ) {
						edu.cmu.cs.dennisc.scenegraph.Vertex vJ = vertices[ j ];
						if( vI.equals( vJ ) ) {
							map.put( j, sharedIndex );
						}
					}
				}
			}
			
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "sharing", rv.getName(), vertices.length, "--->", sharedVertices.size() );
			rv.vertices.setValue( edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( sharedVertices, edu.cmu.cs.dennisc.scenegraph.Vertex.class ) );
			int[] array = rv.polygonData.getValue();
			for( int i=0; i<array.length; i++ ) {
				array[ i ] = map.get( array[ i ] );
			}
			
		}
		return rv;
	}

	private static Mesh meshify( IndexedTriangleArray ita ) {
		Vertex[] vertices = ita.vertices.getValue();
		final int N = vertices.length;
		double[] xyzs = new double[ N*3 ];
		float[] ijks = new float[ N*3 ];
		float[] uvs = new float[ N*2 ];
		
		for( int i=0; i<N; i++ ) {
			Vertex v = vertices[ i ];
			xyzs[ i*3+0 ] = v.position.x;
			xyzs[ i*3+1 ] = v.position.y;
			xyzs[ i*3+2 ] = v.position.z;
			ijks[ i*3+0 ] = v.normal.x;
			ijks[ i*3+1 ] = v.normal.y;
			ijks[ i*3+2 ] = v.normal.z;
			uvs[ i*2+0 ] = v.textureCoordinate0.u;
			uvs[ i*2+1 ] = v.textureCoordinate0.v;
		}
		
		int[] polygonData = ita.polygonData.getValue();
		final int M = polygonData.length;
		short[] xyzTriangleIndices = new short[ M ];
		for( int i=0; i<M; i++ ) {
			assert polygonData[ i ] <= Short.MAX_VALUE;
			xyzTriangleIndices[ i ] = (short)polygonData[ i ];
		}

		Mesh rv = new Mesh();
		rv.xyzs.setValue( xyzs );
		rv.ijks.setValue( ijks );
		rv.uvs.setValue( uvs );
		rv.xyzTriangleIndices.setValue( xyzTriangleIndices );
		return rv;
	}
	
	
	@Override
	protected boolean isSkipExistingOutFilesDesirable() {
		return edu.cmu.cs.dennisc.lang.SystemUtilities.isPropertyTrue( "isSkipExistingOutFilesDesirable" );
	}
	@Override
	protected void handle(java.io.File inFile, java.io.File outFile) {
		try {
			edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder modelBuilder = edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.getInstance( inFile );
			java.util.Map< Geometry, Geometry > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			for( Geometry geometry : modelBuilder.getGeometries() ) {
				if( geometry instanceof IndexedTriangleArray ) {
					IndexedTriangleArray ita = (IndexedTriangleArray)geometry;
					shareVertices( ita );
					Mesh mesh = meshify( ita );
					map.put( ita, mesh );
				} else {
					assert false;
				}
			}
			modelBuilder.replaceGeometries( map );
			modelBuilder.encode( outFile );
			edu.cmu.cs.dennisc.scenegraph.builder.ModelBuilder.forget( inFile );
		} catch( Throwable t ) {
			t.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		final String ROOT = System.getProperty( "user.home" ) + "/Desktop/gallery_src/";
		OptimizeModels batch = new OptimizeModels();
		String subsetOrFull = "subset/";
		//String subsetOrFull = "full/";
		batch.process( ROOT + subsetOrFull + "convertedTo3Gallery/", ROOT + subsetOrFull + "shared3Gallery/", "zip", "zip");
	}
}
