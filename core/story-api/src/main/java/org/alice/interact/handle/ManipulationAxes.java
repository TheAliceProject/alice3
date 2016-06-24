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

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;

/**
 * @author dculyba
 * 
 */
public class ManipulationAxes extends ManipulationHandle3D {
	private static final double MIN_SIZE = .6;

	public ManipulationAxes() {
		this.axis = new edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes( 1, 1.5 );
		this.axis.setParent( this );
	}

	@Override
	public ManipulationHandle3D clone() {
		return new ManipulationAxes();
	}

	@Override
	protected void setScale( double scale ) {
		if( scale < 1.0 ) {
			scale = 1;
		}
		this.diameterScale = scale * 1.4;
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
		if( this.axis != null ) {
			this.axis.setIsShowing( showing );
		}
	}

	@Override
	protected float getOpacity() {
		if( this.axis != null ) {
			return this.axis.getOpacity();
		} else {
			return super.getOpacity();
		}
	}

	@Override
	protected void setOpacity( float opacity ) {
		super.setOpacity( opacity );
		if( this.axis != null ) {
			this.axis.setOpacity( opacity );
		}

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
			double diagonal = boundingBox.getDiagonal();
			if( Double.isNaN( diagonal ) || ( diagonal < MIN_SIZE ) ) {
				diagonal = MIN_SIZE;
			}
			if( this.axis != null ) {
				this.axis.resize( diagonal * .5, 1.5, this.diameterScale );
			}
		}
	}

	private final edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes axis;
	private double diameterScale = 1;
}
