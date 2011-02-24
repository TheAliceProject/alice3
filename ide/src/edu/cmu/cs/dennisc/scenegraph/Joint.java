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

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.EulerAngles;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.math.property.EulerAnglesProperty;
import edu.cmu.cs.dennisc.math.property.Vector3fProperty;

public class Joint extends Transformable
{
    public final edu.cmu.cs.dennisc.property.StringProperty jointID = new edu.cmu.cs.dennisc.property.StringProperty( this, null );
    public final edu.cmu.cs.dennisc.property.BooleanProperty isFreeInX = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );
    public final edu.cmu.cs.dennisc.property.BooleanProperty isFreeInY = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );
    public final edu.cmu.cs.dennisc.property.BooleanProperty isFreeInZ = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );
    
    public final edu.cmu.cs.dennisc.property.DoubleProperty boundRadius = new edu.cmu.cs.dennisc.property.DoubleProperty( this, .5d );
    
    public final edu.cmu.cs.dennisc.property.InstanceProperty<AxisAlignedBox> boundingBox = new edu.cmu.cs.dennisc.property.InstanceProperty<AxisAlignedBox>(this, new AxisAlignedBox());
    
    public final Vector3fProperty oStiffness = new Vector3fProperty(this, new Vector3f());
    public final EulerAnglesProperty oBoneOrientation = new EulerAnglesProperty(this, EulerAngles.createIdentity());
    public final EulerAnglesProperty oPreferedAngles = new EulerAnglesProperty(this, EulerAngles.createIdentity());
    public final EulerAnglesProperty oLocalRotationAxis = new EulerAnglesProperty(this, EulerAngles.createIdentity());
    public final Vector3fProperty oMinimumDampRange = new Vector3fProperty(this, new Vector3f());
    public final Vector3fProperty oMaximumDampRange = new Vector3fProperty(this, new Vector3f());
    public final Vector3fProperty oMinimumDampStrength = new Vector3fProperty(this, new Vector3f());
    public final Vector3fProperty oMaximumDampStrength = new Vector3fProperty(this, new Vector3f());
    
    private Composite sgParent = null;
    
    public Joint()
    {
        super();
    }
    
    private Joint getJoint(Composite c, String jointID)
    {
        if (c == null)
        {
            return null;
        }
        if (c instanceof Joint)
        {
            Joint j = (Joint)c;
            if (j.jointID.getValue().equals(jointID))
            {
                return j;
            }
        }
        for (int i=0; i<c.getComponentCount(); i++)
        {
            Component comp = c.getComponentAt(i);
            if (comp instanceof Composite)
            {
                Joint foundJoint = getJoint((Composite)comp, jointID);
                if (foundJoint != null)
                {
                    return foundJoint;
                }
            }
        }
        return null;
    }
    
    public AxisAlignedBox getBoundingBox(Composite c, AxisAlignedBox rv, AffineMatrix4x4 transform)
    {
        if (c == null)
        {
            return null;
        }
        
        if (c instanceof AbstractTransformable)
        {
            transform = AffineMatrix4x4.createMultiplication(transform, ((AbstractTransformable)c).accessLocalTransformation());
        }
        if (c instanceof Joint)
        {
            Joint j = (Joint)c;
            Point3 localMin = j.boundingBox.getValue().getMinimum();
            Point3 localMax = j.boundingBox.getValue().getMaximum();
            Point3 transformedMin = transform.createTransformed(localMin);
            Point3 transformedMax = transform.createTransformed(localMax);
            AxisAlignedBox transformedBBox = new AxisAlignedBox(transformedMin, transformedMax);
            rv.union(transformedBBox);
        }
        for (int i=0; i<c.getComponentCount(); i++)
        {
            Component comp = c.getComponentAt(i);
            if (comp instanceof Composite)
            {
                getBoundingBox((Composite)comp, rv, transform);
            }
        }
        return rv;
    }
    
    public AxisAlignedBox getBoundingBox(AxisAlignedBox rv, AffineMatrix4x4 transform)
    {
        if (rv == null)
        {
            rv = new AxisAlignedBox();
        }
        getBoundingBox(this, rv, transform);
        return rv;
    }
    
    public AxisAlignedBox getBoundingBox(AxisAlignedBox rv)
    {
        if (rv == null)
        {
            rv = new AxisAlignedBox();
        }
        getBoundingBox(this, rv, AffineMatrix4x4.createIdentity());
        return rv;
    }
    
    public Joint getJoint(String jointID)
    {
        return getJoint(this, jointID);
    }
    
    public void setSGParent( Composite sgParent )
    {
        this.sgParent = sgParent;
    }
    
    @Override
    public Composite getParent()
    {
        if (super.getParent() == null && this.sgParent != null)
        {
            return this.sgParent;
        }
        return super.getParent();
    }
    
    @Override
    protected Composite getVehicle()
    {
        if (super.getParent() == null && this.sgParent != null)
        {
            return this.sgParent;
        }
        return super.getVehicle();
    }
    
    
    @Override
    public Composite getRoot()
    {
        if (super.getParent() == null && this.sgParent != null)
        {
            return this.sgParent.getRoot();
        }
        return super.getRoot();
    }
    
    @Override
    public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
        if (super.getParent() == null && this.sgParent != null)
        {
            return this.sgParent.getAbsoluteTransformation(rv);
        }
        return super.getAbsoluteTransformation(rv);
    }


    @Override
    public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
        if (super.getParent() == null && this.sgParent != null)
        {
            return this.sgParent.getInverseAbsoluteTransformation(rv);
        }
        return super.getInverseAbsoluteTransformation(rv);
    }

    @Override
    public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
        if (super.getParent() == null && this.sgParent != null)
        {
            return this.sgParent.getTransformation(rv, asSeenBy);
        }
        return super.getTransformation(rv, asSeenBy);
    }
    
   
    
    public void renderVisualization(edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc)
    {
        if ( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_DRAW_PROPERTY_KEY))
        {
            rc.gl.glPushMatrix();
            renderSelfVisualization(rc);
            renderChildrenVisualization(rc);
            rc.gl.glPopMatrix();
        }
    }
    
    protected void renderChildrenVisualization(edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc)
    {
        if ( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_DRAW_PROPERTY_KEY))
        {
            for (Component child : this.getComponents())
            {
                if (child instanceof Joint)
                {
                    ((Joint)child).renderVisualization(rc);
                }
            }
        }
    }
    
    private double[] m_local = new double[ 16 ];
    private java.nio.DoubleBuffer m_localBuffer = java.nio.DoubleBuffer.wrap( m_local );
    
    protected void renderSelfVisualization(edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc) 
    {
        if (edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue(org.alice.ide.IDE.DEBUG_PROPERTY_KEY))
        {
            final double UNIT_LENGTH = .5;
            this.localTransformation.getValue().getAsColumnMajorArray16(m_local);
            rc.gl.glMultMatrixd(m_localBuffer);
            {
//                rc.gl.glDisable( javax.media.opengl.GL.GL_LIGHTING );
        
//                rc.gl.glBegin( javax.media.opengl.GL.GL_LINES );
//        
//                rc.gl.glColor3f( 1.0f, 0.0f, 0.0f );
//                rc.gl.glVertex3d( 0, 0, 0 );
//                rc.gl.glVertex3d( UNIT_LENGTH, 0, 0 );
//        
//                rc.gl.glColor3f( 0.0f, 1.0f, 0.0f );
//                rc.gl.glVertex3d( 0, 0, 0 );
//                rc.gl.glVertex3d( 0, UNIT_LENGTH, 0 );
//        
//                rc.gl.glColor3f( 0.0f, 0.0f, 1.0f );
//                rc.gl.glVertex3d( 0, 0, 0 );
//                rc.gl.glVertex3d( 0, 0, UNIT_LENGTH );
//        
//                rc.gl.glColor3f( 1.0f, 1.0f, 1.0f );
//                rc.gl.glVertex3d( 0, 0, 0 );
//                rc.gl.glVertex3d( 0, 0, -2*UNIT_LENGTH );
//                rc.gl.glEnd();
                
                if (this.boundingBox.getValue() != null)
                {
                    rc.gl.glColor3f( 1.0f, 1.0f, 1.0f );
                    Point3 min = this.boundingBox.getValue().getMinimum();
                    Point3 max = this.boundingBox.getValue().getMaximum();
                    
                    //Bottom
                    rc.gl.glBegin( javax.media.opengl.GL.GL_LINE_LOOP );
                    rc.gl.glVertex3d( min.x, min.y, min.z );
                    rc.gl.glVertex3d( min.x, min.y, max.z );
                    rc.gl.glVertex3d( max.x, min.y, max.z );
                    rc.gl.glVertex3d( max.x, min.y, min.z );
                    rc.gl.glEnd();
                    
                    //Top
                    rc.gl.glBegin( javax.media.opengl.GL.GL_LINE_LOOP );
                    rc.gl.glVertex3d( min.x, max.y, min.z );
                    rc.gl.glVertex3d( min.x, max.y, max.z );
                    rc.gl.glVertex3d( max.x, max.y, max.z );
                    rc.gl.glVertex3d( max.x, max.y, min.z );
                    rc.gl.glEnd();
                    
                    //Sides
                    rc.gl.glBegin( javax.media.opengl.GL.GL_LINES );
                    rc.gl.glVertex3d( min.x, min.y, min.z );
                    rc.gl.glVertex3d( min.x, max.y, min.z );
                    rc.gl.glEnd();
                    
                    rc.gl.glBegin( javax.media.opengl.GL.GL_LINES );
                    rc.gl.glVertex3d( max.x, min.y, min.z );
                    rc.gl.glVertex3d( max.x, max.y, min.z );
                    rc.gl.glEnd();
                    
                    rc.gl.glBegin( javax.media.opengl.GL.GL_LINES );
                    rc.gl.glVertex3d( min.x, min.y, max.z );
                    rc.gl.glVertex3d( min.x, max.y, max.z );
                    rc.gl.glEnd();
                    
                    rc.gl.glBegin( javax.media.opengl.GL.GL_LINES );
                    rc.gl.glVertex3d( max.x, min.y, max.z );
                    rc.gl.glVertex3d( max.x, max.y, max.z );
                    rc.gl.glEnd();
                }
                
        
//                rc.gl.glEnable( javax.media.opengl.GL.GL_LIGHTING );
            }
        }
    }
    
}
