/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class JointImp extends AbstractTransformableImp {
	public JointImp( JointedModelImp<?, ?> jointedModelImplementation ) {
		this.jointedModelImplementation = jointedModelImplementation;
	}

	@Override
	public org.lgna.story.implementation.SceneImp getScene() {
		return this.jointedModelImplementation.getScene();
	}

	public abstract org.lgna.story.resources.JointId getJointId();

	@Override
	public final org.lgna.story.SJoint getAbstraction() {
		return this.abstraction;
	}

	public void setAbstraction( org.lgna.story.SJoint abstraction ) {
		assert abstraction != null;
		assert this.abstraction == null : this.abstraction;
		this.abstraction = abstraction;
	}

	public abstract boolean isFreeInX();

	public abstract boolean isFreeInY();

	public abstract boolean isFreeInZ();

	@Override
	protected void postCheckSetVehicle( org.lgna.story.implementation.EntityImp vehicle ) {
		//note: do not call super
		this.setSgVehicle( vehicle != null ? vehicle.getSgComposite() : null );
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalOrientation() {
		return this.jointedModelImplementation.getOriginalJointOrientation( this.getJointId() );
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalTransformation() {
		return this.jointedModelImplementation.getOriginalJointTransformation( this.getJointId() );
	}

	private edu.cmu.cs.dennisc.scenegraph.util.ModestAxes getPivot() {
		if( this.axes != null ) {
			//pass
		} else {
			this.axes = new edu.cmu.cs.dennisc.scenegraph.util.ModestAxes( 0.1 );
			putInstance( this.axes );
		}
		return this.axes;
	}

	public boolean isPivotVisible() {
		if( this.axes != null ) {
			return this.axes.getParent() == this.getSgComposite();
		} else {
			return false;
		}
	}

	public void setPivotVisible( boolean isPivotVisible ) {
		if( isPivotVisible ) {
			this.getPivot().setParent( this.getSgComposite() );
		} else {
			if( this.axes != null ) {
				this.axes.setParent( null );
			}
		}
	}

	public JointedModelImp<?, ?> getJointedModelParent() {
		return this.jointedModelImplementation;
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( this.getJointId().toString() );
	}

	public edu.cmu.cs.dennisc.math.Dimension3 getSize() {
		return getAxisAlignedMinimumBoundingBox().getSize();
	}

	public double getWidth() {
		return this.getSize().x;
	}

	public double getHeight() {
		return this.getSize().y;
	}

	public double getDepth() {
		return this.getSize().z;
	}

	private org.lgna.story.SJoint abstraction;
	private final JointedModelImp<?, ?> jointedModelImplementation;
	private edu.cmu.cs.dennisc.scenegraph.util.ModestAxes axes;
}
