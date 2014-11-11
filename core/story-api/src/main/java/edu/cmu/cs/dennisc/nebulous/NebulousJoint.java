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

package edu.cmu.cs.dennisc.nebulous;

import edu.cmu.cs.dennisc.scenegraph.Composite;

/**
 * @author Dennis Cosgrove
 */
public class NebulousJoint extends edu.cmu.cs.dennisc.scenegraph.AbstractTransformable implements edu.cmu.cs.dennisc.scenegraph.ModelJoint {
	static {
		edu.cmu.cs.dennisc.render.gl.imp.AdapterFactory.register( NebulousJoint.class, NebulousJointAdapter.class );
	}
	private final edu.cmu.cs.dennisc.nebulous.Model nebModel;
	private final org.lgna.story.resources.JointId jointId;
	private edu.cmu.cs.dennisc.math.Point3 actualTranslation;

	public NebulousJoint( edu.cmu.cs.dennisc.nebulous.Model nebModel, org.lgna.story.resources.JointId jointId ) {
		this.nebModel = nebModel;
		this.jointId = jointId;
		this.setName( this.jointId.toString() );
	}

	public org.lgna.story.resources.JointId getJointId() {
		return this.jointId;
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalLocalTransformation() {
		return this.nebModel.getOriginalTransformationForJoint( this.jointId );
	}

	@Override
	protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 accessLocalTransformation() {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 aliceTransform = this.nebModel.getLocalTransformationForJoint( this.jointId );
		if( this.actualTranslation != null ) {
			aliceTransform.translation.set( this.actualTranslation );
		}
		return aliceTransform;
	}

	@Override
	protected void touchLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 current = this.nebModel.getLocalTransformationForJoint( this.jointId );
		current.orientation.setValue( m.orientation );
		if( this.actualTranslation == null ) {
			this.actualTranslation = new edu.cmu.cs.dennisc.math.Point3();
		}
		this.actualTranslation.set( m.translation );
		this.nebModel.setLocalTransformationForJoint( this.jointId, current );
	}

	//	
	//	public void setSgParent( Composite sgParent )
	//    {
	//        this.sgParent = sgParent;
	//    }
	//	
	//	@Override
	//	public void setParent(Composite parent) {
	//		super.setParent(parent);
	//	}
	//    
	//    @Override
	//    public Composite getParent()
	//    {
	//        if (super.getParent() == null)
	//        {
	//        	return this.sgParent;
	//        }
	//        return super.getParent();
	//    }
	//    
	@Override
	protected Composite getVehicle()
	{
		return this.getParent();
	}

	//    
	//    
	//    @Override
	//    public Composite getRoot()
	//    {
	//        if (super.getParent() == null && this.sgParent != null)
	//        {
	//            return this.sgParent.getRoot();
	//        }
	//        return super.getRoot();
	//    }
	//    
	//    @Override
	//    public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
	//        if (super.getParent() == null && this.sgParent != null)
	//        {
	//        	rv = this.sgParent.getAbsoluteTransformation(rv);
	//			rv.setToMultiplication( rv, this.accessLocalTransformation() );
	//            return rv;
	//        }
	//        return super.getAbsoluteTransformation(rv);
	//    }
	//
	//
	//    @Override
	//    public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
	//        if (super.getParent() == null && this.sgParent != null)
	//        {
	//        	rv = this.sgParent.getAbsoluteTransformation(rv);
	//			rv.setToMultiplication( rv, this.accessLocalTransformation() );
	//			rv.invert();
	//			return rv;
	//        }
	//        return super.getInverseAbsoluteTransformation(rv);
	//    }
	//
	//    @Override
	//    public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
	//        if (super.getParent() == null && this.sgParent != null)
	//        {
	//            rv = this.sgParent.getTransformation(rv, asSeenBy);
	//			rv.setToMultiplication( rv, this.accessLocalTransformation() );
	//			return rv;
	//        }
	//        return super.getTransformation(rv, asSeenBy);
	//    }
	//    
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedBoundingBox() {
		return this.nebModel.getAxisAlignedBoundingBoxForJoint( this.jointId );
	}

	//	
	//	@Override
	//	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
	//		edu.cmu.cs.dennisc.scenegraph.Composite vehicle = this.getParent();
	//		//edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.nebModel.getTransformation( this.jointId );
	//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
	//		if( vehicle == null || vehicle.isSceneOf( this ) ) {
	//			rv.set( m );
	//		} else {
	//			rv = vehicle.getAbsoluteTransformation( rv );
	//			rv.setToMultiplication( rv, m );
	//		}
	//		return rv;
	//	}
	//
	//	// todo: cache this information
	//	@Override
	//	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
	//		rv = getAbsoluteTransformation( rv );
	//		rv.invert();
	//		return rv;
	//	}
}
