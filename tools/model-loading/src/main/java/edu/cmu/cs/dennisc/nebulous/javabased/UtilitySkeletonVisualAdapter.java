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

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSkeletonVisual;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;

public class UtilitySkeletonVisualAdapter extends GlrSkeletonVisual
{

    @Override
    protected void updateAppearanceToMeshControllersMap() {
		synchronized( appearanceIdToMeshControllersMap ) {
			appearanceIdToMeshControllersMap.clear();
			for( TexturedAppearance ta : this.owner.textures.getValue() )
			{
				List<edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSkeletonVisual.WeightedMeshControl> controls = new LinkedList<edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSkeletonVisual.WeightedMeshControl>();
				for( WeightedMesh weightedMesh : this.owner.weightedMeshes.getValue() )
				{
					if( weightedMesh.textureId.getValue() == ta.textureId.getValue() )
					{
						edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSkeletonVisual.WeightedMeshControl control = new UtilityWeightedMeshControl();
						control.initialize( weightedMesh );
						controls.add( control );
					}
				}
				appearanceIdToMeshControllersMap.put( ta.textureId.getValue(), controls.toArray( new UtilityWeightedMeshControl[ controls.size() ] ) );
			}
		}
	}
    
    protected List<UtilityWeightedMeshControl> getUtilityWeightedMeshControls()
    {
    	List<UtilityWeightedMeshControl> controlList = new LinkedList<UtilityWeightedMeshControl>();
    	for (Entry<Integer, edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSkeletonVisual.WeightedMeshControl[]> entry : this.appearanceIdToMeshControllersMap.entrySet())
    	{
    		for (edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSkeletonVisual.WeightedMeshControl w : entry.getValue()) {
    			if (!controlList.contains(w)) {
    				assert w instanceof UtilityWeightedMeshControl;
    				controlList.add((UtilityWeightedMeshControl)w);
    			}
    		}
    	}
    	return controlList;
    }
    
    public AxisAlignedBox getAbsoluteBoundingBox()
    {
    	AxisAlignedBox box = new AxisAlignedBox();
        for (UtilityWeightedMeshControl control : this.getUtilityWeightedMeshControls())
        {
            AxisAlignedBox subBox = control.getAbsoluteBoundingBox();
            box.union(subBox);
        }
        return box;
    }
    
    public void initializeJointBoundingBoxes(Joint joint)
    {
        initializeJointBoundingBoxes(joint, AffineMatrix4x4.createIdentity());
    }
    
    private void initializeJointBoundingBoxes(Composite currentNode, AffineMatrix4x4 oTransformationPre)
    {
        if (currentNode == null)
        {
            return;
        }
        AffineMatrix4x4 oTransformationPost = oTransformationPre;
        if (currentNode instanceof Transformable)
        {
            oTransformationPost = AffineMatrix4x4.createMultiplication(oTransformationPre, ((Transformable)currentNode).localTransformation.getValue());
            if (currentNode instanceof Joint)
            {
                AxisAlignedBox box = new AxisAlignedBox();
                for (UtilityWeightedMeshControl control : this.getUtilityWeightedMeshControls())
                {
                    AxisAlignedBox subBox = control.getBoundingBoxForJoint((Joint)currentNode);
                    box.union(subBox);
                }
                ((Joint)currentNode).boundingBox.setValue(box);
                //Now that the bounding boxes are set we can set the radii (they use the bounding box for their calculations)
//                double boundingRadius = Double.NaN;
//                for (WeightedMeshControl control : this.meshControls)
//                {
//                    if (control instanceof UtilityWeightedMeshControl)
//                    {
//                        double radius = ((UtilityWeightedMeshControl)control).getBoundingRadiusForJoint((Joint)currentNode);
//                        if (Double.isNaN(boundingRadius))
//                        {
//                            boundingRadius = radius;
//                        }
//                        else if (!Double.isNaN(radius))
//                        {
//                            boundingRadius = Math.max(boundingRadius, radius);
//                        }
//                    }
//                }
//                ((Joint)currentNode).boundingRadius.setValue(boundingRadius);
                
                PrintUtilities.println("Set bounding box on "+currentNode.getName()+" to: "+box/*+", and the bounding radius to "+boundingRadius*/);
            }
        }
        for (int i=0; i<currentNode.getComponentCount(); i++)
        {
            Component comp = currentNode.getComponentAt(i);
            if (comp instanceof Composite)
            {
                Composite jointChild = (Composite)comp;
                initializeJointBoundingBoxes( jointChild, oTransformationPost );
            }
        }
    }
    
}
