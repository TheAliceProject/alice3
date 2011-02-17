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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.property.DoubleBufferProperty;
import edu.cmu.cs.dennisc.property.EnumProperty;
import edu.cmu.cs.dennisc.property.FloatBufferProperty;
import edu.cmu.cs.dennisc.property.IntBufferProperty;

public class Mesh extends Geometry
{
    public final edu.cmu.cs.dennisc.property.InstanceProperty< Indices[] > indices = new edu.cmu.cs.dennisc.property.InstanceProperty< Indices[] >( this, null );
    public final edu.cmu.cs.dennisc.property.InstanceProperty< float[] > textureCoordinates = new edu.cmu.cs.dennisc.property.InstanceProperty< float[] >( this, null );
    public final edu.cmu.cs.dennisc.property.InstanceProperty< double[] > vertices = new edu.cmu.cs.dennisc.property.InstanceProperty< double[] >( this, null );
    public final edu.cmu.cs.dennisc.property.InstanceProperty< float[] > normals = new edu.cmu.cs.dennisc.property.InstanceProperty< float[] >( this, null );
    
    public final DoubleBufferProperty vertexBuffer = new DoubleBufferProperty(this, (java.nio.DoubleBuffer)null);
    public final FloatBufferProperty normalBuffer = new FloatBufferProperty(this, (java.nio.FloatBuffer)null);
    public final FloatBufferProperty textCoordBuffer = new FloatBufferProperty(this, (java.nio.FloatBuffer)null);
    public final IntBufferProperty indexBuffer = new IntBufferProperty(this, (java.nio.IntBuffer)null);
    
    public final EnumProperty<MeshType> meshType = new EnumProperty<MeshType>(this, MeshType.ALICE_BASED);
    
    public enum MeshType
    {
        ALICE_BASED,
        COLLADA_BASED;
        
        public static MeshType getMeshType(int intVal)
        {
            return MeshType.values()[intVal];
        }
    }
    
    public Mesh()
    {
    }
    
    public Indices getIndicesAt( int nIndex )
    {
        return this.indices.getValue()[nIndex];
    }

    public int getIndicesCount()
    {
        return this.indices.getValue().length;
    }
    
    @Override
    protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) 
    {
        if (meshType.getValue() == MeshType.ALICE_BASED)
        {
            edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingBox( boundingBox, vertices.getValue() );
        }
        else if (meshType.getValue() == MeshType.COLLADA_BASED)
        {
            edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingBox( boundingBox, vertexBuffer.getValue().array() );
        }
    }
    
    @Override
    protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) 
    {
        if (meshType.getValue() == MeshType.ALICE_BASED)
        {
            edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingSphere( boundingSphere, vertices.getValue() );
        }
        else if (meshType.getValue() == MeshType.COLLADA_BASED)
        {
            edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingSphere( boundingSphere, vertexBuffer.getValue().array() );
        }
    }
    
    @Override
    protected void updatePlane( edu.cmu.cs.dennisc.math.Vector3 forward, edu.cmu.cs.dennisc.math.Vector3 upGuide, edu.cmu.cs.dennisc.math.Point3 translation ) {
        double[] xyzs;
        float[] ijks;
        if (meshType.getValue() == MeshType.ALICE_BASED)
        {
            xyzs = vertices.getValue();
            ijks = normals.getValue();
        }
        else if (meshType.getValue() == MeshType.COLLADA_BASED)
        {
            xyzs = vertexBuffer.getValue().array();
            ijks = normalBuffer.getValue().array();
        }
        else
        {
            xyzs = new double[0];
            ijks = new float[0];
        }
        
        assert xyzs.length >= 6;
        assert ijks.length >= 3;

        forward.set( ijks[ 0 ], ijks[ 1 ], ijks[ 2 ] );
        forward.normalize();
        forward.negate();
        
        translation.set( xyzs[ 0 ], xyzs[ 1 ], xyzs[ 2 ] );
        upGuide.set( translation.x - xyzs[ 3 ], translation.y - xyzs[ 4 ], translation.z - xyzs[ 5 ] );
        upGuide.normalize();
        
    }
    @Override
    public void transform( edu.cmu.cs.dennisc.math.AbstractMatrix4x4 trans ) {
        //todo
    }

}
