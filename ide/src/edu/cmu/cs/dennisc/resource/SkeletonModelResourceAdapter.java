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

package edu.cmu.cs.dennisc.resource;

import org.alice.ide.IDE;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.lookingglass.opengl.GeometryAdapter;
import edu.cmu.cs.dennisc.lookingglass.opengl.PickContext;
import edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext;
import edu.cmu.cs.dennisc.lookingglass.opengl.WeightedMeshAdapter;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class SkeletonModelResourceAdapter extends edu.cmu.cs.dennisc.lookingglass.opengl.VisualAdapter< SkeletonModelResource >  implements PropertyListener{
    
    private boolean skeletonIsDirty = true;
    private Joint currentSkeleton = null;
    
    
    public SkeletonModelResourceAdapter()
    {
        super();
    }
    
    @Override
    protected void pickGeometry(PickContext pc, boolean isSubElementActuallyRequired)
    {
        super.pickGeometry(pc, isSubElementActuallyRequired);
    }
    
    @Override
    protected void renderGeometry(RenderContext rc)
    {
        if (this.skeletonIsDirty)
        {
            this.processWeightedMesh();
        }
        
        //DEBUG RENDERING
        if (SystemUtilities.isPropertyTrue(IDE.DEBUG_DRAW_PROPERTY_KEY))
        {
            if( this.currentSkeleton != null) 
            {
                this.currentSkeleton.renderVisualization(rc);
            }
        }
        //END DEBUG RENDERING
        
        super.renderGeometry(rc);
    }
    
    //PropertyListener methods for listening to changes on skeleton transforms
    public void propertyChanging(PropertyEvent e)
    {
        //Do Nothing
    }

    private void setListeningOnSkeleton(Composite c, boolean shouldListen)
    {
        if (c == null)
        {
            return;
        }
        if (c instanceof Joint)
        {
            if (shouldListen)
            {
                ((Joint)c).localTransformation.addPropertyListener(this);
            }
            else
            {
                ((Joint)c).localTransformation.removePropertyListener(this);
            }
        }
        for (int i=0; i<c.getComponentCount(); i++)
        {
            if (c.getComponentAt(i) instanceof Composite)
            {
                setListeningOnSkeleton((Composite)c.getComponentAt(i), shouldListen);
            }
        }
    }
    
    public void propertyChanged(PropertyEvent e)
    {
        handleSkeletonTransformationChange();
    }
    
    private void handleSkeletonTransformationChange()
    {
        this.skeletonIsDirty = true;
    }
    
    private void processWeightedMesh()
    {
        if (this.currentSkeleton != null)
        {
            synchronized( m_geometryAdapters ) {
                for (GeometryAdapter adapter : this.m_geometryAdapters)
                {
                    if (adapter instanceof WeightedMeshAdapter)
                    {
                        ((WeightedMeshAdapter)adapter).preProcess();
                    }
                }
                AffineMatrix4x4 oTransformationPre = new AffineMatrix4x4();
                processWeightedMesh(this.currentSkeleton, oTransformationPre);
                for (GeometryAdapter adapter : this.m_geometryAdapters)
                {
                    if (adapter instanceof WeightedMeshAdapter)
                    {
                        ((WeightedMeshAdapter)adapter).postProcess();
                    }
                }
            }
        }
        this.skeletonIsDirty = false;
    }
    
    private void processWeightedMesh( Composite currentNode, AffineMatrix4x4 oTransformationPre)
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
                for (GeometryAdapter adapter : this.m_geometryAdapters)
                {
                    if (adapter instanceof WeightedMeshAdapter)
                    {
                        ((WeightedMeshAdapter)adapter).process( (Joint)currentNode, oTransformationPost );
                    }
                }
            }
        }
        for (int i=0; i<currentNode.getComponentCount(); i++)
        {
            Component comp = currentNode.getComponentAt(i);
            if (comp instanceof Composite)
            {
                Composite jointChild = (Composite)comp;
                processWeightedMesh( jointChild, oTransformationPost );
            }
        }
    }
    
    @Override
    protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
        if( property == m_element.skeleton ) {
            if (this.currentSkeleton != null)
            {
                this.setListeningOnSkeleton(this.currentSkeleton, false);
            }
            this.currentSkeleton = m_element.skeleton.getValue();
            if (this.currentSkeleton != null)
            {
                this.setListeningOnSkeleton(this.currentSkeleton, true);
                this.skeletonIsDirty = true;
            }
        } else {
            super.propertyChanged( property );
        }
    }
}
