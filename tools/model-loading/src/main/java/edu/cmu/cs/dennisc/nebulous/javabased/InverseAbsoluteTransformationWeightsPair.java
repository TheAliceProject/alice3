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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

public abstract class InverseAbsoluteTransformationWeightsPair extends Element 
{
    protected AffineMatrix4x4 oInverseAbsoluteTransformation;
    
    public InverseAbsoluteTransformationWeightsPair()
    {
        this.oInverseAbsoluteTransformation = new AffineMatrix4x4();
    }
    
    public AffineMatrix4x4 getInverseAbsoluteTransformation()
    {
        return this.oInverseAbsoluteTransformation;
    }
    public void setInverseAbsoluteTransformation( AffineMatrix4x4 oInverseAbsoluteTransformation ) 
    {
        this.oInverseAbsoluteTransformation = oInverseAbsoluteTransformation;
    }
    
    protected abstract WeightIterator constructWeightIterator();

    public abstract void setWeights( List<Float> weightsIn );
    
    protected abstract float getWeightForIndex(int index);
    protected abstract boolean hasWeightForIndex(int index);
    
    protected float[] getNewWeightsForVertexData(List<VertexMapData> vertexMapData)
    {
        float[] newWeights = new float[vertexMapData.size()];
        for (int i=0; i<newWeights.length; i++)
        {
            VertexMapData vmd = vertexMapData.get(i);
            assert vmd.newIndex == i;
            int oldIndex = vmd.oldVertexIndex/3;
            if (this.hasWeightForIndex(oldIndex))
            {
                float weight = getWeightForIndex(oldIndex);
                newWeights[i] = weight;
            }
            else
            {
                newWeights[i] = 0;
            }
        }
        return newWeights;
    }
    
    public abstract edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair createSceneGraphPair(List<VertexMapData> vertexMapData);

    public void setWeights( float[] weightsIn )
    {
        this.setWeights(Utilities.convertFloatArrayToList(weightsIn));
    }
    
    @Override
    protected void readInternal(java.io.InputStream iStream, int nVersion) throws java.io.IOException
    {
        AffineMatrix inputMatrix = new AffineMatrix();
        inputMatrix.read(iStream, nVersion);
        this.oInverseAbsoluteTransformation = AffineMatrix.createAliceMatrix(inputMatrix);
    }

    @Override
    protected void writeInternal(OutputStream oStream) throws IOException 
    {
        AffineMatrix outputMatrix = new AffineMatrix(this.oInverseAbsoluteTransformation);
        outputMatrix.write(oStream);
    }
}
