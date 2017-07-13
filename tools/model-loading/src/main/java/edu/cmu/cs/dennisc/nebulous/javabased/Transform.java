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
import java.util.List;

import org.alice.ide.IDE;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Scene;
import edu.cmu.cs.dennisc.scenegraph.TransformationAffect;

public class Transform extends Node
{
    private AffineMatrix localTransformation;
    
    public Transform()
    {
        this.localTransformation = new AffineMatrix();
    }
    
    @Override
    public int getClassID() 
    {
        return Element.TRANSFORM;
    }
    
    public AffineMatrix getLocalTransformation()
    {
        return this.localTransformation;
    }
    
    public void setLocalTransformation( AffineMatrix localTransformation )
    {
        this.localTransformation = localTransformation;
    }
    
    public void applyTransformation( AffineMatrix transformation )
    {
        localTransformation.multiply(transformation);
    }
    
    public void applyTranslation(double x, double y, double z)
    {
        this.localTransformation.addTranslation(x, y, z);
    }
    
    
    public AffineMatrix getLocalTransformation( AffineMatrix rv ) {
        rv.set( localTransformation );
        return rv;
    }
    
    public final AffineMatrix getAbsoluteTransformation() {
        return getAbsoluteTransformation( new AffineMatrix() );
    }
    
    public AffineMatrix getAbsoluteTransformation( AffineMatrix rv ) 
    {
        Transform vehicle = (Transform)this.parent;
        if( !(this.parent instanceof Transform) || this.parent.isSceneOf( this ) ) 
        {
            rv = getLocalTransformation( rv );
        } else 
        {
            rv = vehicle.getAbsoluteTransformation( rv );
            rv.setToProduct( rv, this.localTransformation);
        }
        return rv;
    }
    
    public final AffineMatrix getTransformation( ReferenceFrame other ) {
        return getTransformation( new AffineMatrix(), other );
    }
    
    public AffineMatrix getInverseAbsoluteTransformation( AffineMatrix rv ) {
        rv = getAbsoluteTransformation( rv );
        rv.invert();
        return rv;
    }
    
    public AffineMatrix getTransformation( AffineMatrix rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
        if( asSeenBy == AsSeenBy.PARENT ) {
            rv = getLocalTransformation( rv );
        } else if( asSeenBy == AsSeenBy.SCENE  ) {
            rv = getAbsoluteTransformation( rv );
        } else if( asSeenBy == AsSeenBy.SELF ) {
            rv.setIdentity();
        } else {
            throw new IllegalArgumentException("Can't handle asSeenBy = "+asSeenBy+" yet.");
        }
        return rv;
    }
    
    public void setTransformation( AffineMatrix transformation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) 
    {
        if( asSeenBy == AsSeenBy.PARENT ) {
            setLocalTransformation( transformation );
        } else if( asSeenBy == AsSeenBy.SELF ) {
            applyTransformation( transformation, asSeenBy );
            //      } else if( asSeenBy.isSceneOf( this ) ) { 
            //          applyTransformation( transformation, asSeenBy, affectMask );
        } else {
            Transform vehicle = (Transform)this.parent;
            assert vehicle != null;

            //todo: optimize
            AffineMatrix m = new AffineMatrix( vehicle.getInverseAbsoluteTransformation(new AffineMatrix()) );
            if( asSeenBy == AsSeenBy.SCENE ) 
            {
                //pass
            } else {
                m.multiply( asSeenBy.getAbsoluteTransformation() );
            }
            m.multiply( transformation );

            setLocalTransformation( m );
            //return LinearAlgebra.multiply( transformation, LinearAlgebra.multiply( asSeenBy.getAbsoluteTransformation(), vehicleInverse ) );
            //          throw new RuntimeException( "todo.  this: " + this + "; vehicle: " + getVehicle() + "; asSeenBy: " + asSeenBy );
        }
    }
    
    public void applyTransformation( AffineMatrix transformation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy) {
        //todo: handle affect
        if( asSeenBy == AsSeenBy.SELF ) {
            AffineMatrix m = getLocalTransformation();
            m.setToProduct( m, transformation );
            setLocalTransformation( m );
        } else if( asSeenBy == AsSeenBy.PARENT ) {
            AffineMatrix m = getLocalTransformation();
            m.setToProduct( transformation, m );
            setLocalTransformation( m );
        } else {
            AffineMatrix m = getTransformation( asSeenBy );
            m.setToProduct( transformation, m );
            setTransformation( m, asSeenBy );
        }
    }
  

    @Override
    protected void readInternal(InputStream iStream, int nVersion) throws IOException 
    {
        super.readInternal(iStream, nVersion);
        this.localTransformation = new AffineMatrix();
        this.localTransformation.read(iStream, nVersion);
//        double xMag = Tuple3.calculateMagnitude( this.localTransformation.getXAxis()[0], this.localTransformation.getXAxis()[1], this.localTransformation.getXAxis()[2]);
//        double yMag = Tuple3.calculateMagnitude( this.localTransformation.getYAxis()[0], this.localTransformation.getYAxis()[1], this.localTransformation.getYAxis()[2]);
//        double zMag = Tuple3.calculateMagnitude( this.localTransformation.getZAxis()[0], this.localTransformation.getZAxis()[1], this.localTransformation.getZAxis()[2]);
//        
//        if (xMag < .5 && yMag < .5 && zMag < .5)
//        {
//        	System.out.println("ERROR");
//        }
    }
    
    @Override
    protected void writeInternal(OutputStream oStream) throws IOException 
    {
        super.writeInternal(oStream);
        this.localTransformation.write(oStream);
    }
    
    @Override
    protected void renderSelfVisualization(RenderContext rc) 
    {
        //Do Nothing
    }

//    public void processWeightedMesh( AffineMatrix oTransformationPre, List< WeightedMeshControl > vpoWeightedMeshControls )
//    {
//        AffineMatrix oTransformationPost = AffineMatrix.createFromProduct(oTransformationPre, this.localTransformation);
//        for(WeightedMeshControl wmc : vpoWeightedMeshControls) 
//        {
//            wmc.process( this, oTransformationPost );
//        }
//        for( Node child : this.children ) 
//        {
//            if ( child != null && child instanceof Transform ) 
//            {
//                Transform poTransformChild = (Transform)child;
//                poTransformChild.processWeightedMesh( oTransformationPost, vpoWeightedMeshControls );
//            }
//        }
//    }

}
