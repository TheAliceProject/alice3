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

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.nebulous.Model;
import edu.cmu.cs.dennisc.scenegraph.Indices;
import edu.cmu.cs.dennisc.scenegraph.Mesh;
import edu.cmu.cs.dennisc.scenegraph.Mesh.MeshType;

public class MeshAdapter< E extends Mesh > extends GeometryAdapter<E>
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
    public void initialize(E element)
    {
        super.initialize(element);
    }

    @Override
    public boolean isAlphaBlended()
    {
        return false;
    }
    
    @Override
    protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property ) {
        if( property == m_element.vertices ) {
            this.vertices = m_element.vertices.getValue();
            setIsGeometryChanged( true );
        } else if ( property == m_element.normals ) {
            this.normals = m_element.normals.getValue();
            setIsGeometryChanged( true );
        } else if ( property == m_element.textureCoordinates ) {
            this.textureCoordinates = m_element.textureCoordinates.getValue();
            setIsGeometryChanged( true );
        } else if ( property == m_element.indices ) {
            this.indices = m_element.indices.getValue();
            setIsGeometryChanged( true );
        } else if ( property == m_element.vertexBuffer ) {
            this.vertexBuffer = m_element.vertexBuffer.getValue();
            setIsGeometryChanged( true );
        } else if ( property == m_element.normalBuffer ) {
            this.normalBuffer = m_element.normalBuffer.getValue();
            setIsGeometryChanged( true );
        } else if ( property == m_element.textCoordBuffer ) {
            this.textCoordBuffer = m_element.textCoordBuffer.getValue();
            setIsGeometryChanged( true );
        } else if ( property == m_element.indexBuffer ) {
            this.indexBuffer = m_element.indexBuffer.getValue();
            setIsGeometryChanged( true );
        } else {
            super.propertyChanged( property );
        }
    }
    
    private void renderMeshWithBuffers( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc)
    {
        rc.gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
        this.vertexBuffer.rewind();
        rc.gl.glVertexPointer(3, GL.GL_DOUBLE, 0, this.vertexBuffer);
        rc.gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
        this.normalBuffer.rewind();
        rc.gl.glNormalPointer(GL.GL_FLOAT, 0, this.normalBuffer);
        rc.gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
        this.textCoordBuffer.rewind();
        rc.gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, this.textCoordBuffer);
        
        this.indexBuffer.rewind();

        rc.gl.glDrawElements(GL.GL_TRIANGLES, this.indexBuffer.remaining(), GL.GL_UNSIGNED_INT, this.indexBuffer);
    }
    
    private void renderMeshWithArrays( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc)
    {
        for( Indices ip : this.indices ) 
        {
            ip.adjustIndicesIfNecessary();
            int[] pnIndices = ip.getIndices();

            rc.gl.glBegin( GL.GL_TRIANGLES );
            int nIndexCount = ip.getIndexCount();
            int nIndex;
            
            for( int i=0; i<nIndexCount;  ) {
                nIndex = pnIndices[ i++ ];
                rc.gl.glTexCoord2fv(this.textureCoordinates, nIndex);
                nIndex = pnIndices[ i++ ];
                rc.gl.glNormal3fv(this.normals, nIndex);
                nIndex = pnIndices[ i++ ];
                rc.gl.glVertex3dv( this.vertices, nIndex );
            }
            
            rc.gl.glEnd();
        }
    }
    
    private void pickMeshWithBuffers( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc)
    {
        pc.gl.glPushName( -1 );
        pc.gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
        this.vertexBuffer.rewind();
        pc.gl.glVertexPointer(3, GL.GL_DOUBLE, 0, vertexBuffer);
        this.indexBuffer.rewind();
        pc.gl.glDrawElements(GL.GL_TRIANGLES, this.indexBuffer.remaining(), GL.GL_UNSIGNED_INT, this.indexBuffer);
        pc.gl.glPopName();
    }
    
    private void pickMeshWithArrays( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc)
    {
        pc.gl.glPushName( -1 );
        for( Indices ip : this.indices ) 
        {
            ip.adjustIndicesIfNecessary();
            int[] pnIndices = ip.getIndices();
            pc.gl.glBegin( GL.GL_TRIANGLES );
            int nIndexCount = ip.getIndexCount();
            int nIndex;
            for( int i=0; i<nIndexCount;  ) {
                i++;
                i++;
                nIndex = pnIndices[ i++ ];
                pc.gl.glVertex3dv( this.vertices, nIndex );
            }
            pc.gl.glEnd();
        }
        pc.gl.glPopName();
    }

    @Override
    protected void renderGeometry(RenderContext rc)
    {
        if (m_element.meshType.getValue() == MeshType.COLLADA_BASED)
        {
            renderMeshWithBuffers(rc);
        }
        else if (m_element.meshType.getValue() == MeshType.ALICE_BASED)
        {
            renderMeshWithArrays(rc);
        }
    }

    @Override
    protected void pickGeometry(PickContext pc, boolean isSubElementRequired)
    {
        if (m_element.meshType.getValue() == MeshType.COLLADA_BASED)
        {
            pickMeshWithBuffers(pc);
        }
        else if (m_element.meshType.getValue() == MeshType.ALICE_BASED)
        {
            pickMeshWithArrays(pc);
        }
    }

    @Override
    public Point3 getIntersectionInSource(Point3 rv, Ray ray, AffineMatrix4x4 m, int subElement)
    {
        rv.setNaN();
        return rv;
    }

}
