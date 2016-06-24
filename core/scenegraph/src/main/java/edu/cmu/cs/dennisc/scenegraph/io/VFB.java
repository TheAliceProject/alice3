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

package edu.cmu.cs.dennisc.scenegraph.io;

import edu.cmu.cs.dennisc.scenegraph.Vertex;

/**
 * @author Dennis Cosgrove
 */
public class VFB {
	public static Vertex[] loadVertices( java.io.InputStream is ) throws java.io.IOException, java.io.FileNotFoundException {
		return (Vertex[])load( new java.io.BufferedInputStream( is ) )[ 0 ];
	}

	public static int[] loadIndices( java.io.InputStream is ) throws java.io.IOException, java.io.FileNotFoundException {
		return (int[])load( new java.io.BufferedInputStream( is ) )[ 1 ];
	}

	public static Object[] load( java.io.BufferedInputStream bis ) throws java.io.IOException, java.io.FileNotFoundException {
		int nByteCount = bis.available();
		byte[] byteArray = new byte[ nByteCount ];
		bis.read( byteArray );
		int nByteIndex;
		for( nByteIndex = 0; nByteIndex < nByteCount; nByteIndex += 4 ) {
			byte b;
			b = byteArray[ nByteIndex ];
			byteArray[ nByteIndex ] = byteArray[ nByteIndex + 3 ];
			byteArray[ nByteIndex + 3 ] = b;
			b = byteArray[ nByteIndex + 1 ];
			byteArray[ nByteIndex + 1 ] = byteArray[ nByteIndex + 2 ];
			byteArray[ nByteIndex + 2 ] = b;
		}
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream( byteArray );
		java.io.DataInputStream dis = new java.io.DataInputStream( bais );
		Object[] verticesAndIndices = { null, null };
		int nVersion = dis.readInt();
		if( nVersion == 1 ) {
			int vertexCount = dis.readInt();
			Vertex[] vertices = new Vertex[ vertexCount ];
			for( int index = 0; index < vertices.length; index++ ) {
				float x = -dis.readFloat();
				float y = dis.readFloat();
				float z = dis.readFloat();
				float i = -dis.readFloat();
				float j = dis.readFloat();
				float k = dis.readFloat();
				float u = dis.readFloat();
				float v = dis.readFloat();
				vertices[ index ] = Vertex.createXYZIJKUV( x, y, z, i, j, k, u, v );
			}

			int faceCount = dis.readInt();
			/* unused int faceDataCount = */dis.readInt();
			int verticesPerFace = dis.readInt();
			int[] indices = new int[ faceCount * 3 ];
			int i = 0;
			for( int f = 0; f < faceCount; f++ ) {
				int length;
				if( verticesPerFace == 0 ) {
					length = dis.readInt();
				} else {
					length = verticesPerFace;
				}
				indices[ i + 0 ] = dis.readInt();
				indices[ i + 1 ] = dis.readInt();
				indices[ i + 2 ] = dis.readInt();
				i += 3;
				for( int lcv = 3; lcv < length; lcv++ ) {
					dis.readInt();
				}
			}
			verticesAndIndices[ 0 ] = vertices;
			verticesAndIndices[ 1 ] = indices;
		}
		return verticesAndIndices;
	}

	private static void store( java.io.BufferedOutputStream bos, int i ) throws java.io.IOException {
		bos.write( (byte)( i & 0x000000FF ) );
		bos.write( (byte)( ( i >> 8 ) & 0x000000FF ) );
		bos.write( (byte)( ( i >> 16 ) & 0x000000FF ) );
		bos.write( (byte)( ( i >> 24 ) & 0x000000FF ) );
	}

	private static void store( java.io.BufferedOutputStream bos, float f ) throws java.io.IOException {
		store( bos, Float.floatToIntBits( f ) );
	}

	public static void store( java.io.OutputStream os, Vertex[] vertices, int[] indices ) throws java.io.IOException {
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream( os );
		store( bos, 1 );
		if( vertices != null ) {
			store( bos, vertices.length );
			for( Vertex vertice : vertices ) {
				store( bos, (float)vertice.position.x );
				store( bos, (float)vertice.position.y );
				store( bos, (float)vertice.position.z );
				store( bos, vertice.normal.x );
				store( bos, vertice.normal.y );
				store( bos, vertice.normal.z );
				store( bos, vertice.textureCoordinate0.u );
				store( bos, vertice.textureCoordinate0.v );
			}
		} else {
			store( bos, 0 );
		}
		if( indices != null ) {
			store( bos, indices.length / 3 );
			store( bos, indices.length );
			store( bos, 3 );
			for( int i = 0; i < indices.length; i += 3 ) {
				store( bos, indices[ i + 2 ] );
				store( bos, indices[ i + 1 ] );
				store( bos, indices[ i ] );
			}
		} else {
			store( bos, 0 );
		}
		bos.flush();
	}
}
