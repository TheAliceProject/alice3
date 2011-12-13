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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static javax.media.opengl.GL.*;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Mesh;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;


public class SkeletonVisualAdapter extends edu.cmu.cs.dennisc.lookingglass.opengl.VisualAdapter< SkeletonVisual > implements PropertyListener{
    
    private boolean skeletonIsDirty = true;
    private Joint currentSkeleton = null;
    protected Map<TexturedAppearanceAdapter, WeightedMeshControl[]> appearanceToMeshControllersMap = new HashMap<TexturedAppearanceAdapter, WeightedMeshControl[]>();
    protected Map<TexturedAppearanceAdapter, MeshAdapter<Mesh>[]> appearanceToGeometryAdapaters = new HashMap<TexturedAppearanceAdapter, MeshAdapter<Mesh>[]>();
    private boolean isDataDirty = true;
    
    public SkeletonVisualAdapter()
    {
        super();
    }
    
    @Override
    public void initialize(SkeletonVisual element)
    {
        super.initialize(element);
        initializeDataIfNecessary();
    }
    
    private void initializeDataIfNecessary()
    {
        if (this.isDataDirty)
        {
            initializeData();
        }
    }
    
    private void initializeData()
    {
        if (this.m_element != null)
        {
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
            appearanceToGeometryAdapaters.clear();
            appearanceToMeshControllersMap.clear();
            for (TexturedAppearance ta : this.m_element.textures.getValue())
            {
            	List<WeightedMeshControl> controls = new LinkedList<WeightedMeshControl>();
	            for (WeightedMesh weightedMesh : this.m_element.weightedMeshes.getValue())
	            {
	            	if (weightedMesh.textureId.getValue() == ta.textureId.getValue())
	            	{
	            		WeightedMeshControl control = new WeightedMeshControl();
	            		control.initialize(weightedMesh);
	            		controls.add(control);
	            	}
	            }
	            appearanceToMeshControllersMap.put(AdapterFactory.getAdapterFor(ta), controls.toArray(new WeightedMeshControl[controls.size()]));
	            List<MeshAdapter<Mesh>> meshAdapters = new LinkedList<MeshAdapter<Mesh>>();
	            for (GeometryAdapter adapter : this.m_geometryAdapters)
	            {
	            	if (adapter instanceof MeshAdapter<?>)
	            	{
	            		MeshAdapter<Mesh> ma = (MeshAdapter<Mesh>)adapter;
		            	if (((Mesh)ma.m_element).textureId.getValue() == ta.textureId.getValue())
		            	{
		            		meshAdapters.add(ma);
		            	}
	            	}
	            }
	            appearanceToGeometryAdapaters.put(AdapterFactory.getAdapterFor(ta), meshAdapters.toArray(new MeshAdapter[meshAdapters.size()]));
            }
        }
        this.isDataDirty = false;
    }
    
    @Override
    protected void pickGeometry(PickContext pc, boolean isSubElementActuallyRequired)
    {
        initializeDataIfNecessary();
        if (this.skeletonIsDirty)
        {
            this.processWeightedMesh();
        }
        super.pickGeometry(pc, isSubElementActuallyRequired);
        
        int i = this.m_element.geometries.getLength();
        for (Entry<TexturedAppearanceAdapter, WeightedMeshControl[]> controlEntry : this.appearanceToMeshControllersMap.entrySet())
        {
        	for (WeightedMeshControl wmc : controlEntry.getValue())
            {
                pc.gl.glPushName( i++ );
                wmc.pickGeometry(pc, isSubElementActuallyRequired);
                pc.gl.glPopName();
            }
        }
        
    }
    
    @Override
    protected void renderGeometry(RenderContext rc)
    {
        initializeDataIfNecessary();
        if (this.skeletonIsDirty)
        {
            this.processWeightedMesh();
        }
        
        //DEBUG RENDERING
        if (edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_DRAW_PROPERTY_KEY))
        {
            edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox = this.m_element.getAxisAlignedMinimumBoundingBox();
            
            if (boundingBox != null)
            {
                rc.gl.glColor3f( 1.0f, 1.0f, 1.0f );
                edu.cmu.cs.dennisc.math.Point3 min = boundingBox.getMinimum();
                edu.cmu.cs.dennisc.math.Point3 max = boundingBox.getMaximum();
                
                //Bottom
                rc.gl.glBegin( GL_LINE_LOOP );
                rc.gl.glVertex3d( min.x, min.y, min.z );
                rc.gl.glVertex3d( min.x, min.y, max.z );
                rc.gl.glVertex3d( max.x, min.y, max.z );
                rc.gl.glVertex3d( max.x, min.y, min.z );
                rc.gl.glEnd();
                
                //Top
                rc.gl.glBegin( GL_LINE_LOOP );
                rc.gl.glVertex3d( min.x, max.y, min.z );
                rc.gl.glVertex3d( min.x, max.y, max.z );
                rc.gl.glVertex3d( max.x, max.y, max.z );
                rc.gl.glVertex3d( max.x, max.y, min.z );
                rc.gl.glEnd();
                
                //Sides
                rc.gl.glBegin( GL_LINES );
                rc.gl.glVertex3d( min.x, min.y, min.z );
                rc.gl.glVertex3d( min.x, max.y, min.z );
                rc.gl.glEnd();
                
                rc.gl.glBegin( GL_LINES );
                rc.gl.glVertex3d( max.x, min.y, min.z );
                rc.gl.glVertex3d( max.x, max.y, min.z );
                rc.gl.glEnd();
                
                rc.gl.glBegin( GL_LINES );
                rc.gl.glVertex3d( min.x, min.y, max.z );
                rc.gl.glVertex3d( min.x, max.y, max.z );
                rc.gl.glEnd();
                
                rc.gl.glBegin( GL_LINES );
                rc.gl.glVertex3d( max.x, min.y, max.z );
                rc.gl.glVertex3d( max.x, max.y, max.z );
                rc.gl.glEnd();
            }
     
            if( this.currentSkeleton != null) 
            {
                this.currentSkeleton.renderVisualization(rc);
            }
        }
        //END DEBUG RENDERING
        
//        super.renderGeometry(rc);
        for (Entry<TexturedAppearanceAdapter, WeightedMeshControl[]> controlEntry : this.appearanceToMeshControllersMap.entrySet())
        {
        	controlEntry.getKey().setTexturePipelineState(rc);
        	for (WeightedMeshControl wmc : controlEntry.getValue())
            {
        		if (!wmc.weightedMesh.cullBackfaces.getValue())
        		{
        			rc.gl.glDisable( GL_CULL_FACE );
        		}
        		else
        		{
        			rc.gl.glEnable( GL_CULL_FACE );
        			rc.gl.glCullFace( GL_BACK );
        		}
                wmc.renderGeometry(rc);
                rc.gl.glEnable( GL_CULL_FACE );
            }
        	MeshAdapter[] meshAdapters = this.appearanceToGeometryAdapaters.get(controlEntry.getKey());
        	if (meshAdapters != null)
        	{
        		for (MeshAdapter ma : meshAdapters)
        		{
        			if (!((Mesh)ma.m_element).cullBackfaces.getValue())
            		{
            			rc.gl.glDisable( GL_CULL_FACE );
            		}
            		else
            		{
            			rc.gl.glEnable( GL_CULL_FACE );
            			rc.gl.glCullFace( GL_BACK );
            		}
        			ma.render(rc);
        			rc.gl.glEnable( GL_CULL_FACE );
        		}
        	}
        }
    }
    
    @Override
    protected boolean isActuallyShowing() {
        initializeDataIfNecessary();
        if (super.isActuallyShowing())
        {
            return true;
        }
        if (m_isShowing && appearanceToMeshControllersMap != null && appearanceToMeshControllersMap.size() > 0)
        {
            if( m_frontFacingAppearanceAdapter != null ) {
                if( m_frontFacingAppearanceAdapter.isActuallyShowing() ) {
                    return true;
                }
            }
            if( m_backFacingAppearanceAdapter != null ) {
                if( m_backFacingAppearanceAdapter.isActuallyShowing() ) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isAlphaBlended() 
    {
        initializeDataIfNecessary();
        if (super.isAlphaBlended())
        {
            return true;
        }
        
        if (appearanceToMeshControllersMap != null && appearanceToMeshControllersMap.size() > 0)
        {
            if( m_frontFacingAppearanceAdapter != null ) {
                if( m_frontFacingAppearanceAdapter.isAlphaBlended() ) {
                    return true;
                }
            }
            if( m_backFacingAppearanceAdapter != null ) {
                if( m_backFacingAppearanceAdapter.isAlphaBlended() ) {
                    return true;
                }
            }
            for (Entry<TexturedAppearanceAdapter, WeightedMeshControl[]> controlEntry : this.appearanceToMeshControllersMap.entrySet())
            {
            	if (controlEntry.getKey().isActuallyShowing())
            	{
            		return true;
            	}
            }
        }
        return false;
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
    
    public void processWeightedMesh()
    {
        if (this.currentSkeleton != null)
        {
            synchronized( appearanceToMeshControllersMap ) {
                for (Entry<TexturedAppearanceAdapter, WeightedMeshControl[]> controlEntry : this.appearanceToMeshControllersMap.entrySet())
                {
                	for (WeightedMeshControl wmc : controlEntry.getValue())
                    {
                		wmc.preProcess();
                    }
                }
                
                AffineMatrix4x4 oTransformationPre = new AffineMatrix4x4();
                processWeightedMesh(this.currentSkeleton, oTransformationPre);
                for (Entry<TexturedAppearanceAdapter, WeightedMeshControl[]> controlEntry : this.appearanceToMeshControllersMap.entrySet())
                {
                	for (WeightedMeshControl wmc : controlEntry.getValue())
                    {
                		wmc.postProcess();
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
            	for (Entry<TexturedAppearanceAdapter, WeightedMeshControl[]> controlEntry : this.appearanceToMeshControllersMap.entrySet())
                {
                	for (WeightedMeshControl wmc : controlEntry.getValue())
                    {
                		wmc.process( (Joint)currentNode, oTransformationPost );
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
        if( property == m_element.skeleton ||
            property == m_element.weightedMeshes ||
            property == m_element.textures ) {
            this.isDataDirty = true;
        }
        else {
            super.propertyChanged( property );
        }
    }
}
