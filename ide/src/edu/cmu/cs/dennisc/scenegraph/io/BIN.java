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
package edu.cmu.cs.dennisc.scenegraph.io;

import edu.cmu.cs.dennisc.scenegraph.Vertex;

/**
 * @author Dennis Cosgrove
 */
public class BIN {
	public static Vertex[] loadVertices( java.io.InputStream is ) throws java.io.IOException {
		Vertex[] vertices = null;
		java.io.BufferedInputStream bis = new java.io.BufferedInputStream ( is );
		java.io.DataInputStream dis = new java.io.DataInputStream ( bis );
		int version = dis.readInt ();
		if( version==1 ) {
			int vertexCount = dis.readInt();
			vertices = new Vertex[vertexCount];
			for (int i=0; i<vertices.length; i++) {
				vertices[i] = new Vertex();
				vertices[i].position.x = dis.readDouble();
				vertices[i].position.y = dis.readDouble();
				vertices[i].position.z = dis.readDouble();
				vertices[i].normal.x = (float)dis.readDouble();
				vertices[i].normal.y = (float)dis.readDouble();
				vertices[i].normal.z = (float)dis.readDouble();
				vertices[i].textureCoordinate0.u = (float)dis.readDouble();
				vertices[i].textureCoordinate0.v = (float)dis.readDouble();
			}
		} else if( version==2 ) {
			int vertexCount = dis.readInt();
			vertices = new Vertex[vertexCount];
			for (int i=0; i<vertices.length; i++) {
				int format = dis.readInt();
				vertices[i] = new Vertex();
				if( (format&Vertex.FORMAT_POSITION)!=0 ) {
					vertices[i].position.x = dis.readDouble();
					vertices[i].position.y = dis.readDouble();
					vertices[i].position.z = dis.readDouble();
				}
				if( (format&Vertex.FORMAT_NORMAL)!=0 ) {
					vertices[i].normal.x = (float)dis.readDouble();
					vertices[i].normal.y = (float)dis.readDouble();
					vertices[i].normal.z = (float)dis.readDouble();
				}
				if( (format&Vertex.FORMAT_DIFFUSE_COLOR)!=0 ) {
					vertices[i].diffuseColor.red = (float)dis.readDouble();
					vertices[i].diffuseColor.green = (float)dis.readDouble();
					vertices[i].diffuseColor.blue = (float)dis.readDouble();
					vertices[i].diffuseColor.alpha = (float)dis.readDouble();
				}
				if( (format&Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR)!=0 ) {
					//vertices[i].specularHighlightColor.x = (float)dis.readDouble();
					//vertices[i].specularHighlightColor.y = (float)dis.readDouble();
					//vertices[i].specularHighlightColor.z = (float)dis.readDouble();
					//vertices[i].specularHighlightColor.w = (float)dis.readDouble();
				}
				if( (format&Vertex.FORMAT_TEXTURE_COORDINATE_0)!=0 ) {
					vertices[i].textureCoordinate0.u = (float)dis.readDouble();
					vertices[i].textureCoordinate0.v = (float)dis.readDouble();
				}
			}
		} else if( version==3 ) {
			int vertexCount = dis.readInt();
			vertices = new Vertex[vertexCount];
			for (int i=0; i<vertices.length; i++) {
				int format = dis.readInt();
				vertices[i] = new Vertex();
				if( (format&Vertex.FORMAT_POSITION)!=0 ) {
					vertices[i].position.x = dis.readDouble();
					vertices[i].position.y = dis.readDouble();
					vertices[i].position.z = dis.readDouble();
				}
				if( (format&Vertex.FORMAT_NORMAL)!=0 ) {
					vertices[i].normal.x = (float)dis.readDouble();
					vertices[i].normal.y = (float)dis.readDouble();
					vertices[i].normal.z = (float)dis.readDouble();
				}
				if( (format&Vertex.FORMAT_DIFFUSE_COLOR)!=0 ) {
					vertices[i].diffuseColor.red = dis.readFloat();
					vertices[i].diffuseColor.green = dis.readFloat();
					vertices[i].diffuseColor.blue = dis.readFloat();
					vertices[i].diffuseColor.alpha = dis.readFloat();
				}
				if( (format&Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR)!=0 ) {
					vertices[i].specularHighlightColor.red = dis.readFloat();
					vertices[i].specularHighlightColor.green = dis.readFloat();
					vertices[i].specularHighlightColor.blue = dis.readFloat();
					vertices[i].specularHighlightColor.alpha = dis.readFloat();
				}
				if( (format&Vertex.FORMAT_TEXTURE_COORDINATE_0)!=0 ) {
					vertices[i].textureCoordinate0.u = dis.readFloat();
					vertices[i].textureCoordinate0.v = dis.readFloat();
				}
			}
		} else {
			throw new RuntimeException( "invalid file version: " + version );
		}
		return vertices;
	}
	public static void storeVertices( Vertex[] vertices, java.io.OutputStream os ) throws java.io.IOException {
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream ( os );
		java.io.DataOutputStream dos = new java.io.DataOutputStream ( bos );
		dos.writeInt( 3 );
		dos.writeInt( vertices.length );
		for (int i=0; i<vertices.length; i++) {
			int format = vertices[i].getFormat();
			dos.writeInt( format );
			if( (format&Vertex.FORMAT_POSITION)!=0 ) {
				dos.writeDouble( vertices[i].position.x );
				dos.writeDouble( vertices[i].position.y );
				dos.writeDouble( vertices[i].position.z );
			}
			if( (format&Vertex.FORMAT_NORMAL)!=0 ) {
				dos.writeDouble( vertices[i].normal.x );
				dos.writeDouble( vertices[i].normal.y );
				dos.writeDouble( vertices[i].normal.z );
			}
			if( (format&Vertex.FORMAT_DIFFUSE_COLOR)!=0 ) {
				dos.writeFloat( vertices[i].diffuseColor.red );
				dos.writeFloat( vertices[i].diffuseColor.green );
				dos.writeFloat( vertices[i].diffuseColor.blue );
				dos.writeFloat( vertices[i].diffuseColor.alpha );
			}
			if( (format&Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR)!=0 ) {
				dos.writeFloat( vertices[i].specularHighlightColor.red );
				dos.writeFloat( vertices[i].specularHighlightColor.green );
				dos.writeFloat( vertices[i].specularHighlightColor.blue );
				dos.writeFloat( vertices[i].specularHighlightColor.alpha );
			}
			if( (format&Vertex.FORMAT_TEXTURE_COORDINATE_0)!=0 ) {
				dos.writeFloat( vertices[i].textureCoordinate0.u );
				dos.writeFloat( vertices[i].textureCoordinate0.v );
			}
		}
		dos.flush();
	}

	public static int[] loadTriangleData( java.io.InputStream is ) throws java.io.IOException {
		int[] indices = null;
		java.io.BufferedInputStream bis = new java.io.BufferedInputStream ( is );
		java.io.DataInputStream dis = new java.io.DataInputStream ( bis );
		int version = dis.readInt ();
		if( version==1 ) {
			int faceCount = dis.readInt();
			int verticesPerFace = dis.readInt();
			indices = new int[faceCount*verticesPerFace];
			for( int i=0; i<indices.length; i++ ) {
				indices[i] = dis.readInt();
			}
		} else if( version==2 ) {
			int indicesCount = dis.readInt();
			indices = new int[indicesCount];
			for( int i=0; i<indices.length; i++ ) {
				indices[i] = dis.readInt();
			}
		} else {
			throw new RuntimeException( "invalid file version: " + version );
		}
		return indices;
	}
	public static void storeTriangleData( int[] indices, java.io.OutputStream os ) throws java.io.IOException {
		java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream ( os );
		java.io.DataOutputStream dos = new java.io.DataOutputStream ( bos );
		dos.writeInt( 2 );
		dos.writeInt( indices.length );
		for (int i=0; i<indices.length; i++) {
			dos.writeInt( indices[i] );
		}
		dos.flush();
	}
}
