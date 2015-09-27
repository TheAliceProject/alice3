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
package org.alice.interact.handle;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.util.BoundingBoxDecorator;

/**
 * @author Dave Culyba
 */
public class SelectionIndicator extends ManipulationHandle3D {
	public SelectionIndicator() {
		this.sgBoundingBoxOffsetTransformable.setParent( this );
		AffineMatrix4x4 offsetTransform = AffineMatrix4x4.createIdentity();
		offsetTransform.translation.y = .01;
		this.sgBoundingBoxOffsetTransformable.setLocalTransformation( offsetTransform );
		this.sgBoundingBoxDecorator.setParent( this.sgBoundingBoxOffsetTransformable );
	}

	@Override
	public ManipulationHandle3D clone() {
		return new SelectionIndicator();
	}

	@Override
	protected void setScale( double scale ) {
		//Do nothing
	}

	@Override
	public ReferenceFrame getSnapReferenceFrame() {
		return null;
	}

	@Override
	public void positionRelativeToObject() {
		//Do nothing
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public void setVisualsShowing( boolean showing ) {
		super.setVisualsShowing( showing );
		if( this.sgBoundingBoxDecorator != null ) {
			this.sgBoundingBoxDecorator.isShowing.setValue( showing );
		}
	}

	@Override
	protected float getOpacity() {
		return this.sgBoundingBoxDecorator.getSgFrontAppearance().opacity.getValue();
	}

	@Override
	protected void setOpacity( float opacity ) {
		super.setOpacity( opacity );
		this.sgBoundingBoxDecorator.getSgFrontAppearance().opacity.setValue( opacity );
		this.sgBoundingBoxDecorator.getSgBackAppearance().opacity.setValue( opacity );
	}

	@Override
	protected double getDesiredOpacity( HandleRenderState renderState ) {
		switch( renderState ) {
		case NOT_VISIBLE:
			return 0.0d;
		default:
			return 0.6d * this.cameraRelativeOpacity;
		}
	}

	@Override
	public void resizeToObject() {
		if( ( this.getParentTransformable() != null ) && ( this.manipulatedObject != null ) ) {
			AxisAlignedBox boundingBox = this.getManipulatedObjectBox();
			this.sgBoundingBoxDecorator.setBox( boundingBox );
		}
	}

	private final BoundingBoxDecorator sgBoundingBoxDecorator = new BoundingBoxDecorator( false );
	private final Transformable sgBoundingBoxOffsetTransformable = new Transformable();
}
