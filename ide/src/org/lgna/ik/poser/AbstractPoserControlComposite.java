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
package org.lgna.ik.poser;

import java.util.UUID;

import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.ik.poser.view.AbstractPoserControlView;
import org.lgna.ik.walkandtouch.IKMagicWand;
import org.lgna.ik.walkandtouch.IKMagicWand.Limb;
import org.lgna.ik.walkandtouch.PoserScene;
import org.lgna.project.ast.UserType;
import org.lgna.story.Color;

/**
 * @author Matt May
 */
public abstract class AbstractPoserControlComposite<T extends AbstractPoserControlView> extends SimpleComposite<T> {

	protected IkPoser ikPoser;
	protected JointSelectionSphereState rightArmAnchor;
	protected JointSelectionSphereState leftArmAnchor;
	protected JointSelectionSphereState rightLegAnchor;
	protected JointSelectionSphereState leftLegAnchor;
	protected StringValue rightArmLabel = this.createStringValue( createKey( "rightArm" ) );
	protected StringValue leftArmLabel = this.createStringValue( createKey( "leftArm" ) );
	protected StringValue rightLegLabel = this.createStringValue( createKey( "rightLeg" ) );
	protected StringValue leftLegLabel = this.createStringValue( createKey( "leftLeg" ) );
	protected BooleanState isUsingIK = createBooleanState( createKey( "isUsingIK" ), true );
	protected AbstractPoserSplitComposite parent;

	//	public AbstractPoserControlComposite( IkPoser ikPoser, UUID uid ) {
	//		super( uid );
	//		this.ikPoser = ikPoser;
	//		ikPoser.getJointSelectionSheres();
	//		PoserScene scene = ikPoser.scene;
	//		rightArmAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.RIGHT_ARM ), scene.getJointsForLimb( IKMagicWand.Limb.RIGHT_ARM ) );
	//		leftArmAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.LEFT_ARM ), scene.getJointsForLimb( IKMagicWand.Limb.LEFT_ARM ) );
	//		rightLegAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.RIGHT_LEG ), scene.getJointsForLimb( IKMagicWand.Limb.RIGHT_LEG ) );
	//		leftLegAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.LEFT_LEG ), scene.getJointsForLimb( IKMagicWand.Limb.LEFT_LEG ) );
	//		rightArmAnchor.getValue().setPaint( Color.GREEN );
	//		leftArmAnchor.getValue().setPaint( Color.GREEN );
	//		rightLegAnchor.getValue().setPaint( Color.GREEN );
	//		leftLegAnchor.getValue().setPaint( Color.GREEN );
	//		ikPoser.setAdapter( new PoserControllerAdapter( this ) );
	//	}

	public AbstractPoserControlComposite( AbstractPoserSplitComposite parent, UUID uid ) {
		super( uid );
		//		this.ikPoser = ikPoser;
		this.parent = parent;
		parent.getJointSelectionSheres();
		PoserScene scene = parent.scene;
		rightArmAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.RIGHT_ARM ), scene.getJointsForLimb( IKMagicWand.Limb.RIGHT_ARM ) );
		leftArmAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.LEFT_ARM ), scene.getJointsForLimb( IKMagicWand.Limb.LEFT_ARM ) );
		rightLegAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.RIGHT_LEG ), scene.getJointsForLimb( IKMagicWand.Limb.RIGHT_LEG ) );
		leftLegAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.LEFT_LEG ), scene.getJointsForLimb( IKMagicWand.Limb.LEFT_LEG ) );
		rightArmAnchor.getValue().setPaint( Color.GREEN );
		leftArmAnchor.getValue().setPaint( Color.GREEN );
		rightLegAnchor.getValue().setPaint( Color.GREEN );
		leftLegAnchor.getValue().setPaint( Color.GREEN );
		parent.setAdapter( new PoserControllerAdapter( this ) );
	}

	protected ActionOperation straightenJointsOperation = createActionOperation( createKey( "straightenJoints" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			parent.getBiped().straightenOutJoints();
			return null;
		}

	} );

	public BooleanState getIsUsingIK() {
		return this.isUsingIK;
	}

	public ActionOperation getStraightenJointsOperation() {
		return this.straightenJointsOperation;
	}

	public IkPoser getIkPoser() {
		return this.ikPoser;
	}

	public void addRightArmAnchorListener( ValueListener<JointSelectionSphere> listener ) {
		rightArmAnchor.addValueListener( listener );
	}

	public void addRightLegAnchorListener( ValueListener<JointSelectionSphere> listener ) {
		rightLegAnchor.addValueListener( listener );
	}

	public void addLeftArmAnchorListener( ValueListener<JointSelectionSphere> listener ) {
		leftArmAnchor.addValueListener( listener );
	}

	public void addLeftLegAnchorListener( ValueListener<JointSelectionSphere> listener ) {
		leftLegAnchor.addValueListener( listener );
	}

	public StringValue getRightArmLabel() {
		return this.rightArmLabel;
	}

	public StringValue getLeftArmLabel() {
		return this.leftArmLabel;
	}

	public StringValue getRightLegLabel() {
		return this.rightLegLabel;
	}

	public StringValue getLeftLegLabel() {
		return this.leftLegLabel;
	}

	public JointSelectionSphereState getRightArmAnchor() {
		return this.rightArmAnchor;
	}

	public JointSelectionSphereState getRightLegAnchor() {
		return this.rightLegAnchor;
	}

	public JointSelectionSphereState getLeftArmAnchor() {
		return this.leftArmAnchor;
	}

	public JointSelectionSphereState getLeftLegAnchor() {
		return this.leftLegAnchor;
	}

	public void updateSphere( Limb limb, JointSelectionSphere sphere ) {
		switch( limb ) {
		case LEFT_ARM:
			leftArmAnchor.setValueTransactionlessly( sphere );
			break;
		case LEFT_LEG:
			leftLegAnchor.setValueTransactionlessly( sphere );
			break;
		case RIGHT_ARM:
			rightArmAnchor.setValueTransactionlessly( sphere );
			break;
		case RIGHT_LEG:
			rightLegAnchor.setValueTransactionlessly( sphere );
			break;
		}
	}

	public UserType<?> getDeclaringType() {
		return parent.getDeclaringType();
	}

}
