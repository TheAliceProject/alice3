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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

//import com.jme.animation.SkinNode;

import edu.cmu.cs.dennisc.scenegraph.WeightInfo;

public class WeightedMesh extends Mesh
{
    protected Map< Short, InverseAbsoluteTransformationWeightsPair > mapReferencesToInverseAbsoluteTransformationWeightsPairs;
    
//    protected SkinNode skinNode;
    
    public WeightedMesh()
    {
        super();
        this.mapReferencesToInverseAbsoluteTransformationWeightsPairs = new HashMap< Short, InverseAbsoluteTransformationWeightsPair >();
    }
    
    @Override
    public int getClassID() 
    {
        return Element.WEIGHTED_MESH;
    }
    
    public void addReference( short reference, InverseAbsoluteTransformationWeightsPair poInverseAbsoluteTransformationWeightsPair )
    {
        this.mapReferencesToInverseAbsoluteTransformationWeightsPairs.put(reference, poInverseAbsoluteTransformationWeightsPair);
    }
    
//    public void setSkinNode( SkinNode skinNode )
//    {
//        this.skinNode = skinNode;
//    }
//    
//    public SkinNode getSkinNode()
//    {
//        return skinNode;
//    }
    
    protected VertexMapData getVertexDataForNewIndex(int index)
    {
        for (VertexMapData vmd : this.vertexInfo)
        {
            if (vmd.newIndex == index)
            {
                return vmd;
            }
        }
        return null;
    }
    
    public edu.cmu.cs.dennisc.scenegraph.WeightedMesh createSceneGraphWeightedMesh(Map<Short, String> shortToStringReferenceMap)
    {
    	if (shortToStringReferenceMap == null) {
    		return null;
    	}
        edu.cmu.cs.dennisc.scenegraph.WeightedMesh sgWeightedMesh = new edu.cmu.cs.dennisc.scenegraph.WeightedMesh();
        this.initializeSceneGraphMesh(sgWeightedMesh);
        
        WeightInfo weightInfo = new WeightInfo();

        for (Entry<Short, InverseAbsoluteTransformationWeightsPair> entry : this.mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet())
        {
        	
            String jointID = shortToStringReferenceMap.get(entry.getKey());
            InverseAbsoluteTransformationWeightsPair weightsPair = entry.getValue();
            edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair sgPair = weightsPair.createSceneGraphPair(this.vertexInfo);
            weightInfo.addReference(jointID, sgPair);
        }

        sgWeightedMesh.weightInfo.setValue(weightInfo);
        
        
        return sgWeightedMesh;
    }
    
    @Override
    protected void readInternal(InputStream iStream, int nVersion) throws IOException 
    {
        super.readInternal(iStream, nVersion);
        int count = Utilities.readInt(iStream);
        this.mapReferencesToInverseAbsoluteTransformationWeightsPairs.clear();
        for (int i=0; i<count; i++)
        {
            short reference = Utilities.readShort(iStream);
            Element iatwp = Element.construct(iStream, nVersion);
            assert iatwp instanceof InverseAbsoluteTransformationWeightsPair;
            this.mapReferencesToInverseAbsoluteTransformationWeightsPairs.put(reference, (InverseAbsoluteTransformationWeightsPair)iatwp);
        }
    }
    
    @Override
    protected void writeInternal(OutputStream oStream) throws IOException 
    {
        super.writeInternal(oStream);
        Utilities.writeInt(this.mapReferencesToInverseAbsoluteTransformationWeightsPairs.size(), oStream);
        for (Entry<Short, InverseAbsoluteTransformationWeightsPair> entry : this.mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet())
        {
            Utilities.writeShort(entry.getKey(), oStream);
            entry.getValue().writeWithoutVersion(oStream);
        }
    }
    
    
    
   
        
}
