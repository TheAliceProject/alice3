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

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alice.ide.IDE;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.lookingglass.opengl.MeshAdapter;
import edu.cmu.cs.dennisc.lookingglass.opengl.PickContext;
import edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.Mesh.MeshType;

public class WeightedMeshAdapter< E extends WeightedMesh > extends MeshAdapter<E>
{
//    protected WeightedMesh weightedMesh;
//    protected Joint skeleton;

    protected AffineMatrix4x4[] affineMatrices;
    protected float[] weights;
    protected List<Integer>[] normalIndices;
    protected boolean needsInitialization = true;
    
    @Override
    public void initialize(E element)
    {
        super.initialize(element);
        internalInitialize();
    }
    
    @Override
    protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property ) {
        if( property == m_element.vertices ||
            property == m_element.normals ||
            property == m_element.textureCoordinates ||
            property == m_element.indices ||
            property == m_element.vertexBuffer ||
            property == m_element.normalBuffer ||
            property == m_element.textCoordBuffer ||
            property == m_element.indexBuffer ||
            property == m_element.weightInfo ||
            property == m_element.skeleton )
        {
            this.needsInitialization = true;
            setIsGeometryChanged( true );
        } 
        else {
            super.propertyChanged( property );
        }
    }
    
    
    
    private void internalInitialize()
    {
        if (m_element != null)
        {
            if (this.m_element.meshType.getValue() == MeshType.ALICE_BASED)
            {
                initializeAliceBased();
            }
            else if ( this.m_element.meshType.getValue() == MeshType.COLLADA_BASED )
            {
                initializeColladaBased();
            }
            needsInitialization = false;
            //DEBUG
            if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
            {
                for( int i=0; i<this.normalIndices.length; i++ ) {
                    if( this.normalIndices[ i ].size() == 0 ) 
                    {
                        PrintUtilities.println( "WARNING: no normals on index "+i );
                    }
                }
            }
            //END DEBUG
        }        
    }
    
    private void initializeAliceBased()
    {
        this.vertices = new double[this.m_element.vertices.getValue().length];
        this.normals = new float[this.m_element.normals.getValue().length];
        
        int nVertexCount = this.m_element.vertices.getValue().length / 3;
        this.affineMatrices = new AffineMatrix4x4[ nVertexCount ];
        this.weights = new float[ nVertexCount ];
        this.normalIndices = (List<Integer>[])new LinkedList[ nVertexCount ];
        for (int i=0; i<nVertexCount; i++)
        {
            this.affineMatrices[i] = new AffineMatrix4x4();
            this.weights[i] = 0f;
            this.normalIndices[i] = new LinkedList<Integer>();
        }
        for(Indices indices : this.m_element.indices.getValue()) 
        {
            for( int j=0; j<indices.getTriangleCount(); j++ ) 
            {
                for( int k=0; k<3; k++ ) 
                {
                    int nVertexIndex = indices.getVertexIndex( j, k );
                    int nNormalIndex = indices.getNormalIndex( j, k );
                    this.addNormalIndex( nVertexIndex, nNormalIndex );
                }
            }
        }
    }
    
    private void initializeColladaBased()
    {
        this.normalBuffer = BufferUtilities.copyFloatBuffer(this.m_element.normalBuffer.getValue());
        this.vertexBuffer = BufferUtilities.copyDoubleBuffer(this.m_element.vertexBuffer.getValue());
        int nVertexCount = this.vertexBuffer.limit() / 3;
        
        this.affineMatrices = new AffineMatrix4x4[ nVertexCount ];
        this.weights = new float[ nVertexCount ];
        for (int i=0; i<nVertexCount; i++)
        {
            this.affineMatrices[i] = new AffineMatrix4x4();
            this.weights[i] = 0f;
        }
    }
    
    private void addNormalIndex( int nIndex, int nNormalIndex ) 
    {
        for( int i=0; i<this.normalIndices[ nIndex ].size(); i++ ) 
        {
            if( this.normalIndices[ nIndex ].get(i) == nNormalIndex ) 
            {
                return;
            }
        }
        this.normalIndices[nIndex].add( nNormalIndex );
    }
    
    public void preProcess() 
    {
        for( int i=0; i<this.affineMatrices.length; i++ ) {
            this.affineMatrices[ i ].setZero();
            this.weights[ i ] = 0f;
        }
    }
    
    public void process( edu.cmu.cs.dennisc.scenegraph.Joint joint, AffineMatrix4x4 oTransformation ) 
    {
        InverseAbsoluteTransformationWeightsPair iatwp = this.m_element.weightInfo.getValue().getMap().get(joint.jointID.getValue());;
        if (iatwp != null)
        {
            AffineMatrix4x4 oDelta = AffineMatrix4x4.createMultiplication( oTransformation, iatwp.getInverseAbsoluteTransformation());
            iatwp.reset();
            while (!iatwp.isDone())
            {
                int vertexIndex = iatwp.getIndex();
                float weight = iatwp.getWeight();
                AffineMatrix4x4 transform = AffineMatrix4x4.createMultiplication(oDelta, weight);
                this.affineMatrices[ vertexIndex ].add( transform );
                this.weights[ vertexIndex ] += weight;
                iatwp.advance();
            }
        }
    }
    
    public void postProcess() 
    {
        for( int i=0; i<this.affineMatrices.length; i++ ) 
        {
            float weight = this.weights[ i ];
            if( 0.999f < weight && weight < 1.001f ) {
                //pass
            } else {
                this.affineMatrices[ i ].multiply(weight);
            }
        }
        if (this.m_element.meshType.getValue() == MeshType.ALICE_BASED)
        {
            this.transformArrays( this.affineMatrices, this.normalIndices, this.vertices, this.normals, this.m_element.vertices.getValue(), this.m_element.normals.getValue() );
        }
        else
        {
            this.transformBuffers(this.affineMatrices, this.vertexBuffer, this.normalBuffer, this.m_element.vertexBuffer.getValue(), this.m_element.normalBuffer.getValue());
        }
        setIsGeometryChanged(true);
    }
    
    private void transformArrays(AffineMatrix4x4[] voAffineMatrices, List< Integer >[] vnNormalIndices, double[] verticesDst, float[] normalsDst, double[] verticesSrc, float[] normalsSrc )
    {
        int iTimes3 = 0;
        for( int i=0; i<voAffineMatrices.length; i++, iTimes3+=3 ) {
            voAffineMatrices[i].transformVertex( verticesDst, iTimes3, verticesSrc, iTimes3 );
            for( int j=0; j<vnNormalIndices[i].size(); j++ ) {
                int kTimes3 = vnNormalIndices[i].get(j)*3;
                voAffineMatrices[i].transformNormal( normalsDst, kTimes3, normalsSrc, kTimes3 );
            }
        }
    }
    
    private void transformBuffers( AffineMatrix4x4[] voAffineMatrices, DoubleBuffer vertices, FloatBuffer normals, DoubleBuffer verticesSrc, FloatBuffer normalsSrc ) 
    {   
        double[] vertexSrc = new double[3];
        float[] normalSrc = new float[3];
        double[] vertexDst = new double[3];
        float[] normalDst = new float[3];
        vertices.rewind();
        normals.rewind();
        verticesSrc.rewind();
        normalsSrc.rewind();
        
        for( int i=0; i<voAffineMatrices.length; i++ ) 
        {
            vertexSrc[0] = verticesSrc.get();
            vertexSrc[1] = verticesSrc.get();
            vertexSrc[2] = verticesSrc.get();
            voAffineMatrices[i].transformVertex(vertexDst, 0, vertexSrc, 0);
            vertices.put(vertexDst);
            
            normalSrc[0] = normalsSrc.get();
            normalSrc[1] = normalsSrc.get();
            normalSrc[2] = normalsSrc.get();
            voAffineMatrices[i].transformNormal(normalDst, 0, normalSrc, 0);
            normals.put(normalDst);
        }
    }
    
    @Override
    protected void renderGeometry(RenderContext rc)
    {
        if (this.needsInitialization)
        {
            this.internalInitialize();
        }
        super.renderGeometry(rc);
    }
    
    @Override
    protected void pickGeometry(PickContext pc, boolean isSubElementRequired)
    {
        if (this.needsInitialization)
        {
            this.internalInitialize();
        }
        super.pickGeometry(pc, isSubElementRequired);
    }
    
    
    
}
