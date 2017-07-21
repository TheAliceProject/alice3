/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.poser.controllers;

import org.lgna.croquet.BooleanState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.ik.core.IKCore.Limb;
import org.lgna.ik.poser.animation.composites.AbstractPoserControlComposite;
import org.lgna.ik.poser.jselection.JointSelectionSphere;
import org.lgna.story.Color;
import org.lgna.story.SetPaint;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

/**
 * @author Matt May
 */
public class PoserControllerAdapter {

	private final AbstractPoserControlComposite controlComposite;
	private JointId rightLegAnchorJointID;
	private JointId rightArmAnchorJointID;
	private JointId leftLegAnchorJointID;
	private JointId leftArmAnchorJointID;

	public PoserControllerAdapter( AbstractPoserControlComposite controlComposite ) {
		this.controlComposite = controlComposite;
		this.controlComposite.addRightArmAnchorListener( rightArmAnchorJointListener );
		rightLegAnchorJointID = controlComposite.getRightLegAnchor().getValue().getJoint().getJointId();
		this.controlComposite.addRightLegAnchorListener( rightLegAnchorJointListener );
		rightArmAnchorJointID = controlComposite.getRightArmAnchor().getValue().getJoint().getJointId();
		this.controlComposite.addLeftArmAnchorListener( leftArmAnchorJointListener );
		leftLegAnchorJointID = controlComposite.getLeftLegAnchor().getValue().getJoint().getJointId();
		this.controlComposite.addLeftLegAnchorListener( leftLegAnchorJointListener );
		leftArmAnchorJointID = controlComposite.getLeftArmAnchor().getValue().getJoint().getJointId();
	}

	private final ValueListener<JointSelectionSphere> rightArmAnchorJointListener = new ValueListener<JointSelectionSphere>() {

		@Override
		public void changing( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
			prevValue.setPaint( Color.WHITE, SetPaint.duration( 0 ) );
			nextValue.setPaint( Color.GREEN, SetPaint.duration( 0 ) );
			PoserControllerAdapter.this.rightArmAnchorJointID = nextValue.getJoint().getJointId();
		}
	};
	private final ValueListener<JointSelectionSphere> rightLegAnchorJointListener = new ValueListener<JointSelectionSphere>() {

		@Override
		public void changing( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
			prevValue.setPaint( Color.WHITE, SetPaint.duration( 0 ) );
			nextValue.setPaint( Color.GREEN, SetPaint.duration( 0 ) );
			PoserControllerAdapter.this.rightLegAnchorJointID = nextValue.getJoint().getJointId();
		}
	};
	private final ValueListener<JointSelectionSphere> leftArmAnchorJointListener = new ValueListener<JointSelectionSphere>() {

		@Override
		public void changing( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
			prevValue.setPaint( Color.WHITE, SetPaint.duration( 0 ) );
			nextValue.setPaint( Color.GREEN, SetPaint.duration( 0 ) );
			PoserControllerAdapter.this.leftArmAnchorJointID = nextValue.getJoint().getJointId();
		}
	};
	private final ValueListener<JointSelectionSphere> leftLegAnchorJointListener = new ValueListener<JointSelectionSphere>() {

		@Override
		public void changing( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( State<JointSelectionSphere> state, JointSelectionSphere prevValue, JointSelectionSphere nextValue, boolean isAdjusting ) {
			prevValue.setPaint( Color.WHITE, SetPaint.duration( 0 ) );
			nextValue.setPaint( Color.GREEN, SetPaint.duration( 0 ) );
			PoserControllerAdapter.this.leftLegAnchorJointID = nextValue.getJoint().getJointId();
		}
	};

	public JointId getAnchorJointID( Limb limb, JointImp joint ) {
		switch( limb ) {
		case LEFT_ARM:
			return leftArmAnchorJointID;
		case LEFT_LEG:
			return leftLegAnchorJointID;
		case RIGHT_ARM:
			return rightArmAnchorJointID;
		case RIGHT_LEG:
			return rightLegAnchorJointID;
		}
		return null;
	}

	public void updateSphere( Limb limb, JointSelectionSphere sphere ) {
		controlComposite.updateSphere( limb, sphere );
	}

	public BooleanState getJointRotationHandleVisibilityState() {
		return controlComposite.getJointRotationHandleVisibilityState();
	}

	public AbstractPoserControlComposite getControlComposite() {
		return this.controlComposite;
	}
}
