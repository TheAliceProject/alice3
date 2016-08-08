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

import org.alice.interact.MovementDirection;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;

/**
 * @author dculyba
 * 
 */
public class JointRotationRingHandle extends RotationRingHandle {

	private static final double JOINT_MIN_RADIUS = .2d;

	public JointRotationRingHandle() {
		super();
	}

	public JointRotationRingHandle( MovementDirection rotationAxisDirection ) {
		super( rotationAxisDirection );
	}

	public JointRotationRingHandle( MovementDirection rotationAxisDirection, Color4f color ) {
		super( rotationAxisDirection, color );
	}

	public JointRotationRingHandle( MovementDirection rotationAxisDirection, HandlePosition handlePosition ) {
		super( rotationAxisDirection, handlePosition );
	}

	public JointRotationRingHandle( MovementDirection rotationAxisDirection, HandlePosition handlePosition, Color4f color ) {
		super( rotationAxisDirection, handlePosition, color );
	}

	public JointRotationRingHandle( MovementDirection rotationAxisDirection, HandlePosition handlePosition, Color4f baseColor, Color4f activeColor, Color4f rolloverColor, Color4f mutedColor ) {
		super( rotationAxisDirection, handlePosition, baseColor, activeColor, rolloverColor, mutedColor );
	}

	public JointRotationRingHandle( JointRotationRingHandle handle ) {
		super( handle.rotationAxisDirection, handle.handlePosition, handle.baseColor, handle.activeColor, handle.rolloverColor, handle.mutedColor );
	}

	@Override
	public JointRotationRingHandle clone() {
		return new JointRotationRingHandle( this );
	}

	@Override
	protected double getMinTorusRadius() {
		return super.getMinTorusRadius() * .8;
	}

	@Override
	protected double getMaxTorusRadius() {
		return super.getMaxTorusRadius() * .8;
	}

	@Override
	protected double getMajorAxisRadius() {
		if( this.getParentTransformable() != null ) {
			AxisAlignedBox boundingBox = this.getManipulatedObjectBox();
			double radius = boundingBox.getDiagonal() * .5;
			if( Double.isNaN( radius ) || ( radius < JOINT_MIN_RADIUS ) ) {
				radius = JOINT_MIN_RADIUS;
			}
			return radius;
		}
		return JOINT_MIN_RADIUS;
	}

}
