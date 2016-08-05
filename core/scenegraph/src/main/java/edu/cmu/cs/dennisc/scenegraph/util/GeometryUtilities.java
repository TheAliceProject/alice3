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

import edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray;
import edu.cmu.cs.dennisc.scenegraph.Vertex;

/**
 * @author Dennis Cosgrove
 */
public class GeometryUtilities {
	public GeometryUtilities() {
		throw new AssertionError();
	}

	private static boolean isSharingVerticesNecessary( edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices ) {
		final int N = vertices.length;
		for( int i = 0; i < N; i++ ) {
			edu.cmu.cs.dennisc.scenegraph.Vertex vI = vertices[ i ];
			for( int j = i + 1; j < N; j++ ) {
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
			java.util.Map<Integer, Integer> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			java.util.List<edu.cmu.cs.dennisc.scenegraph.Vertex> sharedVertices = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			final int N = vertices.length;
			for( int i = 0; i < N; i++ ) {
				if( map.keySet().contains( i ) ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.scenegraph.Vertex vI = vertices[ i ];
					//assert vI.equals( vI );
					int sharedIndex = sharedVertices.size();
					sharedVertices.add( vI );
					map.put( i, sharedIndex );
					for( int j = i + 1; j < N; j++ ) {
						edu.cmu.cs.dennisc.scenegraph.Vertex vJ = vertices[ j ];
						if( vI.equals( vJ ) ) {
							map.put( j, sharedIndex );
						}
					}
				}
			}

			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "sharing", rv.getName(), vertices.length, "--->", sharedVertices.size() );
			rv.vertices.setValue( edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( sharedVertices, edu.cmu.cs.dennisc.scenegraph.Vertex.class ) );
			int[] array = rv.polygonData.getValueAsArray();
			for( int i = 0; i < array.length; i++ ) {
				array[ i ] = map.get( array[ i ] );
			}

		}
		return rv;
	}

	private static boolean isRemovingExcessTrianglesNecessary( IndexedTriangleArray ita ) {
		int[] polygonData = ita.polygonData.getValueAsArray();
		final int N = polygonData.length;
		for( int i = 0; i < N; i += 3 ) {
			int a1 = polygonData[ i + 0 ];
			int b1 = polygonData[ i + 1 ];
			int c1 = polygonData[ i + 2 ];
			if( a1 == b1 ) {
				return true;
			}
			if( a1 == c1 ) {
				return true;
			}
			if( b1 == c1 ) {
				return true;
			}
			for( int j = i + 3; j < N; j += 3 ) {
				int a2 = polygonData[ j + 0 ];
				int b2 = polygonData[ j + 1 ];
				int c2 = polygonData[ j + 2 ];
				if( ( a1 != a2 ) || ( b1 != b2 ) || ( c1 != c2 ) ) {
					//pass
				} else {
					return true;
				}
			}
		}
		return false;
	}

	private static IndexedTriangleArray removeExcessTriangles( IndexedTriangleArray rv ) {
		if( isRemovingExcessTrianglesNecessary( rv ) ) {
			class Triangle {
				private int a;
				private int b;
				private int c;
				private boolean isToBeIncluded;

				public Triangle( int a, int b, int c ) {
					this.a = a;
					this.b = b;
					this.c = c;
					this.isToBeIncluded = this.isLineOrPoint() == false;
					if( this.isToBeIncluded ) {
						assert this.a != this.b;
						assert this.a != this.c;
						assert this.b != this.c;
					}
				}

				private boolean isLineOrPoint() {
					if( this.a == this.b ) {
						return true;
					}
					if( this.a == this.c ) {
						return true;
					}
					if( this.b == this.c ) {
						return true;
					}
					return false;
				}

				@Override
				public boolean equals( Object o ) {
					if( this == o ) {
						return true;
					} else {
						if( o instanceof Triangle ) {
							Triangle other = (Triangle)o;
							return ( this.a == other.a ) && ( this.b == other.b ) && ( this.c == other.c );
						} else {
							return false;
						}
					}
				}

				@Override
				public String toString() {
					return "Triangle[" + this.a + "," + this.b + "," + this.c + "]";
				}
			}
			Vertex[] vertices = rv.vertices.getValue();

			int[] polygonData = rv.polygonData.getValueAsArray();

			java.util.ArrayList<Triangle> triangles = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
			final int N_POLYGON_DATA = polygonData.length;
			for( int i = 0; i < N_POLYGON_DATA; i += 3 ) {
				int a = polygonData[ i + 0 ];
				int b = polygonData[ i + 1 ];
				int c = polygonData[ i + 2 ];
				assert a < vertices.length;
				assert b < vertices.length;
				assert c < vertices.length;
				triangles.add( new Triangle( a, b, c ) );
			}

			final int N_TRIANGLES = triangles.size();
			for( int i = 0; i < N_TRIANGLES; i++ ) {
				Triangle triangleI = triangles.get( i );
				if( triangleI.isToBeIncluded ) {
					for( int j = i + 1; j < N_TRIANGLES; j++ ) {
						Triangle triangleJ = triangles.get( j );
						if( triangleI.equals( triangleJ ) ) {
							triangleJ.isToBeIncluded = false;
						}
					}
				}
			}

			//			java.util.ListIterator< Triangle > triangleIterator = triangles.listIterator();
			//			while( triangleIterator.hasNext() ) {
			//				Triangle triangle = triangleIterator.next();
			//				if( triangle.isToBeIncluded ) {
			//					//pass
			//				} else {
			//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "removing triangle", triangle );
			//					triangleIterator.remove();
			//				}
			//			}
			//			
			//			int[] trimmedPolygonData = new int[ triangles.size()*3 ];
			//			int i = 0;
			//			for( Triangle triangle : triangles ) {
			//				polygonData[ i++ ] = triangle.a;
			//				polygonData[ i++ ] = triangle.b;
			//				polygonData[ i++ ] = triangle.c;
			//			}
			//			rv.polygonData.setValue( trimmedPolygonData );

			java.util.LinkedList<Integer> trimmedPolygonData = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( Triangle triangle : triangles ) {
				if( triangle.isToBeIncluded ) {
					trimmedPolygonData.add( triangle.a );
					trimmedPolygonData.add( triangle.b );
					trimmedPolygonData.add( triangle.c );
					assert triangle.a < vertices.length;
					assert triangle.b < vertices.length;
					assert triangle.c < vertices.length;
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.info( "removing triangle" );
				}
			}
			rv.polygonData.setValue( edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createIntArray( trimmedPolygonData ) );
		}
		return rv;
	}

	private static IndexedTriangleArray removeUnreferencedVertices( IndexedTriangleArray rv ) {
		Vertex[] vertices = rv.vertices.getValue();
		final int N = vertices.length;
		boolean[] isReferencedArray = new boolean[ N ];
		int[] polygonData = rv.polygonData.getValueAsArray();

		for( int i : polygonData ) {
			isReferencedArray[ i ] = true;
		}

		boolean isRequiringTrimming = false;
		for( boolean isReferenced : isReferencedArray ) {
			if( isReferenced ) {
				//pass
			} else {
				isRequiringTrimming = true;
				break;
			}
		}

		if( isRequiringTrimming ) {
			java.util.List<Vertex> trimmedVertices = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			java.util.Map<Integer, Integer> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			for( int i = 0; i < N; i++ ) {
				if( isReferencedArray[ i ] ) {
					map.put( i, trimmedVertices.size() );
					trimmedVertices.add( vertices[ i ] );
				}
			}

			//not necessary at the moment, but might as well create new array
			int[] reassignedPolygonData = new int[ polygonData.length ];
			for( int i = 0; i < polygonData.length; i++ ) {
				reassignedPolygonData[ i ] = map.get( polygonData[ i ] );
			}

			rv.vertices.setValue( edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( trimmedVertices, Vertex.class ) );
			rv.polygonData.setValue( reassignedPolygonData );
		}
		return rv;
	}

	public static IndexedTriangleArray clean( IndexedTriangleArray rv ) {
		shareVertices( rv );
		removeExcessTriangles( rv );
		removeUnreferencedVertices( rv );
		return rv;
	}
}
