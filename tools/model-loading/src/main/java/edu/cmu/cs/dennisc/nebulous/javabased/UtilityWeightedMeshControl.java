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

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point2;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair;


public class UtilityWeightedMeshControl extends edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrSkeletonVisual.WeightedMeshControl
{
	
	public AxisAlignedBox getAbsoluteBoundingBox()
    { 
		AxisAlignedBox box = new AxisAlignedBox(); 
    	this.indexBuffer.rewind();
    	while(this.indexBuffer.hasRemaining())
    	{
    		int index = this.indexBuffer.get()*3;
        	Point3 vertex = new Point3(this.vertexBuffer.get(index), this.vertexBuffer.get(index+1), this.vertexBuffer.get(index+2));
    		box.union(vertex);
    	}
    	return box;
    }
	
	public AxisAlignedBox getSrcAbsoluteBoundingBox()
    { 
		AxisAlignedBox box = new AxisAlignedBox(); 
    	this.indexBuffer.rewind();
    	while(this.indexBuffer.hasRemaining())
    	{
    		int index = this.indexBuffer.get()*3;
        	Point3 vertex = new Point3(this.weightedMesh.vertexBuffer.getValue().get(index), this.weightedMesh.vertexBuffer.getValue().get(index+1), this.weightedMesh.vertexBuffer.getValue().get(index+2));
    		box.union(vertex);
    	}
    	return box;
    }
	
	public DoubleBuffer getTransformedVertices()
	{
		return this.vertexBuffer;
	}
	
	public FloatBuffer getTransformedNormals()
	{
		return this.normalBuffer;
	}
	
	public IntBuffer getIndexBuffer()
	{
		return this.indexBuffer;
	}
	
	public FloatBuffer getTextCoordBuffer()
	{
		return this.textCoordBuffer;
	}
	
	public edu.cmu.cs.dennisc.scenegraph.WeightedMesh getSgWeightedMesh()
	{
		return this.weightedMesh;
	}
	
	private static final float WEIGHT_THRESHOLD = .2f;
	
    public AxisAlignedBox getBoundingBoxForJoint(edu.cmu.cs.dennisc.scenegraph.Joint joint)
    { 	
        InverseAbsoluteTransformationWeightsPair iatwp = this.weightedMesh.weightInfo.getValue().getMap().get(joint.jointID.getValue());
        AffineMatrix4x4 inverseJoint = joint.getInverseAbsoluteTransformation();
        AxisAlignedBox box = new AxisAlignedBox(); 
        if (iatwp != null)
        {
            iatwp.reset();
            while (!iatwp.isDone())
            {
                int vertexIndex = iatwp.getIndex()*3;
                float weight = iatwp.getWeight();
                if (weight > WEIGHT_THRESHOLD)
                {
                    weight = 1;
                }
                Point3 vertex = new Point3(this.vertexBuffer.get(vertexIndex), this.vertexBuffer.get(vertexIndex+1), this.vertexBuffer.get(vertexIndex+2));
                Point3 localVertex = inverseJoint.createTransformed(vertex);
                localVertex.multiply(weight);
                box.union(localVertex);
                iatwp.advance();
            }
        }
        return box;
    }
    
    private double getRadiusCalculationForJoint(edu.cmu.cs.dennisc.scenegraph.Joint joint)
    {
        Vector3 directionToBoxCenter = Vector3.createNormalized(joint.boundingBox.getValue().getCenter());
        Point3 min3 = joint.boundingBox.getValue().getMinimum();
        Point3 max3 = joint.boundingBox.getValue().getMaximum();
        double[] absVals = {Math.abs(directionToBoxCenter.x), Math.abs(directionToBoxCenter.y), Math.abs(directionToBoxCenter.z)};
        int maxIndex = 1;
        for (int i=0; i<absVals.length; i++)
        {
            if (absVals[i] > absVals[maxIndex])
            {
                maxIndex = i;
            }
        }
        if (maxIndex == 0) //X-Axis
        {
            Point2 min = new Point2(min3.y, min3.z);
            Point2 max = new Point2(max3.y, max3.z);
            double radius = Point2.calculateDistanceBetween(min, max);
            return radius;
        }
        if (maxIndex == 1) //Y-Axis
        {
            Point2 min = new Point2(min3.x, min3.z);
            Point2 max = new Point2(max3.x, max3.z);
            double radius = Point2.calculateDistanceBetween(min, max);
            return radius;
        }
        if (maxIndex == 2) //Z-Axis
        {
            Point2 min = new Point2(min3.x, min3.y);
            Point2 max = new Point2(max3.x, max3.y);
            double radius = Point2.calculateDistanceBetween(min, max);
            return radius;
        }
        return Double.NaN;
    }
    
    public double getBoundingRadiusForJoint(edu.cmu.cs.dennisc.scenegraph.Joint joint)
    {
        double thisRadius = getRadiusCalculationForJoint(joint);
        double parentRadius = Double.NaN;
        if (joint.getParent() instanceof edu.cmu.cs.dennisc.scenegraph.Joint)
        {
            parentRadius = getRadiusCalculationForJoint((edu.cmu.cs.dennisc.scenegraph.Joint)joint.getParent());
        }
        
        if (Double.isNaN(parentRadius)|| Double.isNaN(thisRadius))
        {
            return thisRadius;
        }
        return (thisRadius + parentRadius) / 2;
    }
    
    
    
}
