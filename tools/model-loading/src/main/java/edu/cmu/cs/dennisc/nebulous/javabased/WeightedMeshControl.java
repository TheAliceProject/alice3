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

package edu.cmu.cs.dennisc.nebulous.javabased;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.alice.ide.IDE;
import org.alice.interact.debug.DebugInteractUtilities;

//import com.jme.animation.SkinNode;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.nebulous.javabased.Mesh.MeshType;
import edu.cmu.cs.dennisc.print.PrintUtilities;

public class WeightedMeshControl 
{
    private static final boolean USE_SKIN_NODE = false;
    
    protected WeightedMesh weightedMesh;
    protected edu.cmu.cs.dennisc.scenegraph.Joint skeleton;

    protected Map< edu.cmu.cs.dennisc.scenegraph.Joint, InverseAbsoluteTransformationWeightsPair> mapTransformsToInverseAbsoluteTransformationWeightsPairs;
    protected AffineMatrix4x4[] affineMatrices;
    protected float[] weights;
    protected List<Integer>[] normalIndices;
    protected VerticesAndNormals transformedVerticesAndNormals;
    
    protected FloatBuffer transformedVertices;
    protected FloatBuffer transformedNormals;
    protected IntBuffer indexBuffer;
    
//    protected SkinNode skinNode;
    
    public WeightedMeshControl( WeightedMesh poWeightedMesh, edu.cmu.cs.dennisc.scenegraph.Joint poSkeleton ) 
    {
        this.weightedMesh = poWeightedMesh;
        this.skeleton = poSkeleton;
        this.mapTransformsToInverseAbsoluteTransformationWeightsPairs = new HashMap<edu.cmu.cs.dennisc.scenegraph.Joint, InverseAbsoluteTransformationWeightsPair>();
//        for (Entry<Short, InverseAbsoluteTransformationWeightsPair> entry : this.weightedMesh.mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet())
//        {
//            edu.cmu.cs.dennisc.scenegraph.Joint joint = this.skeleton.getJoint(entry.getKey());
//            if (joint != null)
//            {
//                this.mapTransformsToInverseAbsoluteTransformationWeightsPairs.put(joint, entry.getValue());
//            }
//            else
//            {
//                PrintUtilities.println("WARNING: could not resolve reference "+entry.getKey());
//            } 
//        }
        
        if (this.weightedMesh.getMeshType() == MeshType.MAYA_BASED && this.weightedMesh.getVerticesAndNormals() != null)
        {
            VerticesAndNormals poVerticesAndNormals = this.weightedMesh.getVerticesAndNormals();
    
            this.transformedVerticesAndNormals = new VerticesAndNormals();
            this.transformedVerticesAndNormals.resize(poVerticesAndNormals);
    
            int nVertexCount = poVerticesAndNormals.getVertexCount();
            this.affineMatrices = new AffineMatrix4x4[ nVertexCount ];
            this.weights = new float[ nVertexCount ];
            this.normalIndices = (List<Integer>[])new LinkedList[ nVertexCount ];
            for (int i=0; i<nVertexCount; i++)
            {
                this.affineMatrices[i] = new AffineMatrix4x4();
                this.weights[i] = 0f;
                this.normalIndices[i] = new LinkedList<Integer>();
            }
            
            for(ShaderIndicesPair sip : this.weightedMesh.getShaderIndicesPairs()) 
            {
                for( int j=0; j<sip.getTriangleCount(); j++ ) 
                {
                    for( int k=0; k<3; k++ ) 
                    {
                        int nVertexIndex = sip.getVertexIndex( j, k );
                        int nNormalIndex = sip.getNormalIndex( j, k );
                        this.addNormalIndex( nVertexIndex, nNormalIndex );
                    }
                }
            }
        }
        else if ( this.weightedMesh.getMeshType() == MeshType.COLLADA_BASED )
        {
            this.transformedVerticesAndNormals = null;
            
//            this.skinNode = this.weightedMesh.getSkinNode();
            
            this.transformedNormals = Utilities.copyFloatBuffer(this.weightedMesh.getNormalBuffer());
            this.transformedVertices = Utilities.copyFloatBuffer(this.weightedMesh.getVertexBuffer());
            int nVertexCount = this.transformedVertices.limit() / 3;
            
            this.affineMatrices = new AffineMatrix4x4[ nVertexCount ];
            this.weights = new float[ nVertexCount ];
            this.normalIndices = null;
            for (int i=0; i<nVertexCount; i++)
            {
                this.affineMatrices[i] = new AffineMatrix4x4();
                this.weights[i] = 0f;
            }
            
        }

        if (DebugInteractUtilities.isDebugEnabled())
        {
            for( int i=0; i<this.normalIndices.length; i++ ) {
                if( this.normalIndices[ i ].size() == 0 ) 
                {
                    PrintUtilities.println( "WARNING: no normals on index "+i );
                }
            }
        }
    }
    
    public Texture getTexture()
    {
        return this.weightedMesh.getTexture();
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
        InverseAbsoluteTransformationWeightsPair iatwp = this.mapTransformsToInverseAbsoluteTransformationWeightsPairs.get(joint);
        if (iatwp != null)
        {
            AffineMatrix4x4 oDelta = AffineMatrix4x4.createMultiplication( oTransformation, iatwp.getInverseAbsoluteTransformation());
            WeightIterator wi = iatwp.constructWeightIterator();
            while (!wi.isDone())
            {
                int vertexIndex = wi.getIndex();
                float weight = wi.getWeight();
                AffineMatrix4x4 transform = AffineMatrix4x4.createMultiplication(oDelta, weight);
                this.affineMatrices[ vertexIndex ].add( transform );
                this.weights[ vertexIndex ] += weight;
                wi.advance();
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
        if (this.transformedVerticesAndNormals != null)
        {
            
            this.transformedVerticesAndNormals.transform( this.affineMatrices, this.normalIndices, this.weightedMesh.getVerticesAndNormals() );
        }
        else
        {
//            if (USE_SKIN_NODE)
//            {
//                if (this.skinNode != null )
//                {
//                    this.skinNode.updateSkin();
//                }
//            }
//            else
            {
                this.transform(this.affineMatrices, this.transformedVertices, this.transformedNormals, this.weightedMesh.getVertexBuffer(), this.weightedMesh.getNormalBuffer());
            }
        }
    }
    
    private void transform( AffineMatrix4x4[] voAffineMatrices, FloatBuffer vertices, FloatBuffer normals, FloatBuffer verticesSrc, FloatBuffer normalsSrc ) 
    {   
        float[] vertexSrc = new float[3];
        float[] normalSrc = new float[3];
        float[] vertexDst = new float[3];
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

    public void render(edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc) 
    {
//        if (this.transformedVerticesAndNormals != null)
//        {
//            this.weightedMesh.renderMesh( rc, this.transformedVerticesAndNormals );
//        }
//        else
//        {
//            if (USE_SKIN_NODE)
//            {
//                this.weightedMesh.renderMesh(rc, this.weightedMesh.getVertexBuffer(), this.weightedMesh.getNormalBuffer());
//            }
//            else
//            {
//                this.weightedMesh.renderMesh(rc, this.transformedVertices, this.transformedNormals);
//            }
//        }
    }
    
    public void pick(edu.cmu.cs.dennisc.render.gl.imp.PickContext pc) 
    {
//        if (this.transformedVerticesAndNormals != null)
//        {
//            this.weightedMesh.pickMesh(pc, this.transformedVerticesAndNormals );
//        }
//        else
//        {
//            if (USE_SKIN_NODE)
//            {  
//                this.weightedMesh.pickMesh(pc, this.weightedMesh.getVertexBuffer());
//            }
//            else
//            {
//                this.weightedMesh.pickMesh(pc, this.transformedVertices);
//            }
//        }
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
}
