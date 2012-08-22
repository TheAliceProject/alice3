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

package edu.cmu.cs.dennisc.lookingglass.opengl;

import static javax.media.opengl.GL.GL_DOUBLE;
import static javax.media.opengl.GL.GL_FLOAT;
import static javax.media.opengl.GL.GL_MAX_ELEMENTS_INDICES;
import static javax.media.opengl.GL.GL_MAX_ELEMENTS_VERTICES;
import static javax.media.opengl.GL.GL_NORMAL_ARRAY;
import static javax.media.opengl.GL.GL_TEXTURE_COORD_ARRAY;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL.GL_UNSIGNED_INT;
import static javax.media.opengl.GL.GL_VERTEX_ARRAY;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.scenegraph.Indices;
import edu.cmu.cs.dennisc.scenegraph.Mesh;

public class MeshAdapter<E extends Mesh> extends GeometryAdapter<E>
{

	protected DoubleBuffer vertexBuffer;
	protected FloatBuffer normalBuffer;
	protected FloatBuffer textCoordBuffer;
	protected IntBuffer indexBuffer;

	protected double[] vertices;
	protected float[] normals;
	protected float[] textureCoordinates;
	protected Indices[] indices;

	@Override
	public void initialize( E element )
	{
		super.initialize( element );
	}

	@Override
	public boolean isAlphaBlended()
	{
		return false;
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.vertexBuffer ) {
			this.vertexBuffer = m_element.vertexBuffer.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.normalBuffer ) {
			this.normalBuffer = m_element.normalBuffer.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.textCoordBuffer ) {
			this.textCoordBuffer = m_element.textCoordBuffer.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.indexBuffer ) {
			this.indexBuffer = m_element.indexBuffer.getValue();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}

	public static void renderMesh( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, DoubleBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer textCoordBuffer, IntBuffer indexBuffer )
	{
		int maxIndices = GetUtilities.getInteger( rc.gl, GL_MAX_ELEMENTS_INDICES );
		int maxVertices = GetUtilities.getInteger( rc.gl, GL_MAX_ELEMENTS_VERTICES );
		if( ( ( vertexBuffer.capacity() / 3 ) >= maxVertices ) || ( indexBuffer.capacity() >= maxIndices ) )
		{
			renderMeshAsArrays( rc, vertexBuffer, normalBuffer, textCoordBuffer, indexBuffer );
		}
		else
		{
			renderMeshWithBuffers( rc, vertexBuffer, normalBuffer, textCoordBuffer, indexBuffer );
		}
	}

	public static void renderMeshWithBuffers( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, DoubleBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer textCoordBuffer, IntBuffer indexBuffer )
	{
		rc.gl.glEnableClientState( GL_VERTEX_ARRAY );
		vertexBuffer.rewind();
		rc.gl.glVertexPointer( 3, GL_DOUBLE, 0, vertexBuffer );
		rc.gl.glEnableClientState( GL_NORMAL_ARRAY );
		normalBuffer.rewind();
		rc.gl.glNormalPointer( GL_FLOAT, 0, normalBuffer );
		rc.gl.glEnableClientState( GL_TEXTURE_COORD_ARRAY );
		textCoordBuffer.rewind();
		rc.gl.glTexCoordPointer( 2, GL_FLOAT, 0, textCoordBuffer );

		indexBuffer.rewind();

		rc.gl.glDrawElements( GL_TRIANGLES, indexBuffer.remaining(), GL_UNSIGNED_INT, indexBuffer );

		rc.gl.glDisableClientState( GL_VERTEX_ARRAY );
		rc.gl.glDisableClientState( GL_NORMAL_ARRAY );
		rc.gl.glDisableClientState( GL_TEXTURE_COORD_ARRAY );
	}

	//This is really slow in debug mode
	public static void renderMeshAsArrays( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, DoubleBuffer vertexBuffer, FloatBuffer normalBuffer, FloatBuffer textCoordBuffer, IntBuffer indexBuffer )
	{
		indexBuffer.rewind();
		rc.gl.glBegin( GL_TRIANGLES );
		while( indexBuffer.hasRemaining() )
		{
			int index = indexBuffer.get();
			int index2x = index * 2;
			int index3x = index * 3;
			textCoordBuffer.position( index2x );
			rc.gl.glTexCoord2f( textCoordBuffer.get(), textCoordBuffer.get() );
			normalBuffer.position( index3x );
			rc.gl.glNormal3f( normalBuffer.get(), normalBuffer.get(), normalBuffer.get() );
			vertexBuffer.position( index3x );
			rc.gl.glVertex3d( vertexBuffer.get(), vertexBuffer.get(), vertexBuffer.get() );
		}
		rc.gl.glEnd();
	}

	public static void pickMesh( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc, DoubleBuffer vertexBuffer, IntBuffer indexBuffer )
	{
		int maxIndices = GetUtilities.getInteger( pc.gl, GL_MAX_ELEMENTS_INDICES );
		int maxVertices = GetUtilities.getInteger( pc.gl, GL_MAX_ELEMENTS_VERTICES );
		if( ( ( vertexBuffer.capacity() / 3 ) >= maxVertices ) || ( indexBuffer.capacity() >= maxIndices ) )
		{
			pickMeshAsArrays( pc, vertexBuffer, indexBuffer );
		}
		else
		{
			pickMeshWithBuffers( pc, vertexBuffer, indexBuffer );
		}
	}

	public static void pickMeshWithBuffers( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc, DoubleBuffer vertexBuffer, IntBuffer indexBuffer )
	{
		pc.gl.glPushName( -1 );
		pc.gl.glEnableClientState( GL_VERTEX_ARRAY );
		vertexBuffer.rewind();
		pc.gl.glVertexPointer( 3, GL_DOUBLE, 0, vertexBuffer );
		indexBuffer.rewind();
		pc.gl.glDrawElements( GL_TRIANGLES, indexBuffer.remaining(), GL_UNSIGNED_INT, indexBuffer );
		pc.gl.glDisableClientState( GL_VERTEX_ARRAY );
		pc.gl.glPopName();
	}

	public static void pickMeshAsArrays( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc, DoubleBuffer vertexBuffer, IntBuffer indexBuffer )
	{
		pc.gl.glPushName( -1 );

		indexBuffer.rewind();
		pc.gl.glBegin( GL_TRIANGLES );
		while( indexBuffer.hasRemaining() )
		{
			int index = indexBuffer.get();
			int index3x = index * 3;
			pc.gl.glVertex3d( vertexBuffer.get( index3x ), vertexBuffer.get( index3x + 1 ), vertexBuffer.get( index3x + 2 ) );
		}
		pc.gl.glEnd();
		pc.gl.glPopName();
	}

	@Override
	protected void renderGeometry( RenderContext rc )
	{
		renderMesh( rc, this.vertexBuffer, this.normalBuffer, this.textCoordBuffer, this.indexBuffer );
	}

	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired )
	{
		pickMesh( pc, vertexBuffer, indexBuffer );
	}

	@Override
	public Point3 getIntersectionInSource( Point3 rv, Ray ray, AffineMatrix4x4 m, int subElement )
	{
		rv.setNaN();
		return rv;
	}

}
