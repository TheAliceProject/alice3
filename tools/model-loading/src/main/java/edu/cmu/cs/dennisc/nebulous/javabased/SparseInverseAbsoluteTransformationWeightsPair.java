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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SparseInverseAbsoluteTransformationWeightsPair extends InverseAbsoluteTransformationWeightsPair 
{
    private List<Float> vfWeights;
    private List<Integer> vnIndices;
    
    @Override
    public int getClassID() 
    {
        return Element.SPARSE_INVERSE_ABSOLUTE_TRANSFORMATION_WEIGHTS_PAIR;
    }
    
    @Override
    protected WeightIterator constructWeightIterator() 
    {
        return new SparseWeightIterator(this.vfWeights, this.vnIndices);
    }
    
    @Override
    public void setWeights(float[] weightsIn) 
    {
        super.setWeights(weightsIn);
    }
    
    @Override
    public void setWeights(List<Float> weightsIn) 
    {
        this.vfWeights = new LinkedList<Float>();
        this.vnIndices = new LinkedList<Integer>();
        for (int i=0; i<weightsIn.size(); i++)
        {
            if (weightsIn.get(i) != 0)
            {
                this.vfWeights.add(new Float(weightsIn.get(i)));
                this.vnIndices.add(new Integer(i));
            }
        }
    }
    
    @Override
    protected void readInternal(InputStream iStream, int nVersion) throws IOException 
    {
        super.readInternal(iStream, nVersion);
        float[] weightArray = Utilities.readFloatArray(iStream);
        
        this.vfWeights = Utilities.convertFloatArrayToList(weightArray);
        if (nVersion < 3)
        {
        	short[] indicesArray = Utilities.readShortArray(iStream);
        	this.vnIndices = new LinkedList<Integer>();
            for (short i : indicesArray)
            {
            	this.vnIndices.add((int)i);
            }
        }
        else
        {
        	int[] indicesArray = Utilities.readIntArray(iStream);
        	this.vnIndices = Utilities.convertIntArrayToList(indicesArray);
        }
    }
    
    @Override
    protected void writeInternal(OutputStream oStream) throws IOException 
    {
        super.writeInternal(oStream);
        float[] weightArray = Utilities.convertListToFloatArray(this.vfWeights);
        int[] indicesArray = Utilities.convertListToIntArray(this.vnIndices);
        Utilities.writeFloatArray(oStream, weightArray);
        Utilities.writeIntArray(oStream, indicesArray);
    }
    
    @Override
    protected boolean hasWeightForIndex(int index)
    {
        for (int i=0; i<this.vnIndices.size(); i++)
        {
            if (this.vnIndices.get(i) == index)
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected float getWeightForIndex(int index)
    {
        for (int i=0; i<this.vnIndices.size(); i++)
        {
            if (this.vnIndices.get(i) == index)
            {
                return this.vfWeights.get(i);
            }
        }
        assert false;
        return 0;
    }

    @Override
    public edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair createSceneGraphPair(List<VertexMapData> vertexMapData)
    {
        edu.cmu.cs.dennisc.scenegraph.SparseInverseAbsoluteTransformationWeightsPair sgPair = new edu.cmu.cs.dennisc.scenegraph.SparseInverseAbsoluteTransformationWeightsPair();
        float[] newWeights = getNewWeightsForVertexData(vertexMapData);
        sgPair.setWeights(newWeights);
        sgPair.setInverseAbsoluteTransformation(this.getInverseAbsoluteTransformation());
        return sgPair;
    }
}
