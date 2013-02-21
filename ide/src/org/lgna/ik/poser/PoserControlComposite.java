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

import java.util.List;
import java.util.Map;

import org.lgna.common.ComponentThread;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.ik.poser.view.PoserControlView;
import org.lgna.ik.walkandtouch.IKMagicWand;
import org.lgna.ik.walkandtouch.IKMagicWand.Limb;
import org.lgna.ik.walkandtouch.PoserScene;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.AnimateToPose.Detail;
import org.lgna.story.Color;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class PoserControlComposite extends SimpleComposite<PoserControlView> {

	private IkPoser ikPoser;
	private JointSelectionSphereState rightArmAnchor;
	private JointSelectionSphereState leftArmAnchor;
	private JointSelectionSphereState rightLegAnchor;
	private JointSelectionSphereState leftLegAnchor;
	private StringValue rightArmLabel = this.createStringValue( createKey( "rightArm" ) );
	private StringValue leftArmLabel = this.createStringValue( createKey( "leftArm" ) );
	private StringValue rightLegLabel = this.createStringValue( createKey( "rightLeg" ) );
	private StringValue leftLegLabel = this.createStringValue( createKey( "leftLeg" ) );
	private ListSelectionState<PoseAnimation> posesList = createListSelectionState( createKey( "listOfPoses" ), PoseAnimation.class, PoseAnimationList.getCodec(), -1 );
	private NameAndExportAnimationCompositeInHonorOfJenLapp nameAndExportAnimationComposite = new NameAndExportAnimationCompositeInHonorOfJenLapp( this );
	private Map<PoseAnimation, Detail[]> animationToDetailMap = Collections.newHashMap();
	private ValueListener<PoseAnimation> poseAnimationListener = new ValueListener<PoseAnimation>() {

		public void changing( State<PoseAnimation> state, PoseAnimation prevValue, PoseAnimation nextValue, boolean isAdjusting ) {
		}

		public void changed( State<PoseAnimation> state, PoseAnimation prevValue, PoseAnimation nextValue, boolean isAdjusting ) {
			if(nextValue != null) {
			final org.lgna.story.implementation.ProgramImp programImp = org.lgna.story.ImplementationAccessor.getImplementation( ikPoser );
			final org.lgna.story.SBiped ogre = ikPoser.getBiped();
			nextValue.animate( programImp, ogre );
			}
		}
	};

	private ActionOperation savePoseOperation = createActionOperation( createKey( "savePose" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			PoserControlComposite.this.getView().enableExport();
			Pose pose = ikPoser.getPose();
			PoseAnimation pAnimation = new PoseAnimation( pose );
			posesList.addItem( pAnimation );
			return null;
		}
	} );

	private ActionOperation runAnimationOperation = createActionOperation( createKey( "runAnimation" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {

			posesList.setSelectedIndex( -1 );

			final org.lgna.story.implementation.ProgramImp programImp = org.lgna.story.ImplementationAccessor.getImplementation( ikPoser );
			final org.lgna.story.SBiped ogre = ikPoser.getBiped();
			ogre.straightenOutJoints( org.lgna.story.StraightenOutJoints.duration( 0 ) );

			ComponentThread thread = new ComponentThread( new Runnable() {
				public void run() {
					for( PoseAnimation pAnimation : posesList ) {
						pAnimation.animate( programImp, ogre );
					}
				}
			}, "noDescription" );
			thread.start();
			return null;
		}
	} );

	private ActionOperation deletePoseOperation = createActionOperation( createKey( "deletePose" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			posesList.removeItem( posesList.getValue() );
			if( posesList.getItemCount() == 0 ) {
				PoserControlComposite.this.getView().disableExport();
			}
			return null;
		}
	} );
	private ActionOperation deselectPoseOperation = createActionOperation( createKey( "deselectPose" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			posesList.setSelectedIndex( -1 );
			return null;
		}
	} );
	private ActionOperation saveUpdatedPoseOperation = createActionOperation( createKey( "savePoseChanges" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			int index = posesList.indexOf( posesList.getValue() );
			List<PoseAnimation> newValues = Collections.newArrayList();
			for( int i = 0; i != posesList.getItemCount(); ++i ) {
				if( i == index ) {
					newValues.add( new PoseAnimation( ikPoser.getPose() ) );
				} else {
					newValues.add( posesList.getItemAt( i ) );
				}
			}
			posesList.setItems( newValues );
			return null;
		}
	} );

	public UserMethod createUserMethod( String name ) {
		ExpressionStatement[] miArr = new ExpressionStatement[ posesList.getItemCount() ];
		int i = 0;
		for( PoseAnimation animation : posesList ) {
			miArr[ i ] = new ExpressionStatement( animation.getPose().createAliceMethod( getParametersForAnimation( animation ) ) );
			++i;
		}
		BlockStatement body = new BlockStatement( miArr );
		UserMethod rv = new UserMethod( name, Void.class, new UserParameter[ 0 ], body );
		return rv;
	}

	private Detail[] getParametersForAnimation( PoseAnimation animation ) {
		if( animationToDetailMap.get( animation ) != null ) {
			return animationToDetailMap.get( animation );
		}
		return new Detail[ 0 ];
	}

	public PoserControlComposite( IkPoser ikPoser ) {
		super( java.util.UUID.fromString( "67c1692b-8fca-406a-8be3-267b1796ceb8" ) );
		this.ikPoser = ikPoser;
		ikPoser.getJointSelectionSheres();
		PoserScene scene = ikPoser.scene;
		rightArmAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.RIGHT_ARM ), scene.getJointsForLimb( IKMagicWand.Limb.RIGHT_ARM ) );
		leftArmAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.LEFT_ARM ), scene.getJointsForLimb( IKMagicWand.Limb.LEFT_ARM ) );
		rightLegAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.RIGHT_LEG ), scene.getJointsForLimb( IKMagicWand.Limb.RIGHT_LEG ) );
		leftLegAnchor = new JointSelectionSphereState( scene.getDefaultAnchorJoint( IKMagicWand.Limb.LEFT_LEG ), scene.getJointsForLimb( IKMagicWand.Limb.LEFT_LEG ) );
		rightArmAnchor.getValue().setPaint( Color.GREEN );
		leftArmAnchor.getValue().setPaint( Color.GREEN );
		rightLegAnchor.getValue().setPaint( Color.GREEN );
		leftLegAnchor.getValue().setPaint( Color.GREEN );
		ikPoser.setAdapter( new PoserControllerAdapter( this ) );
		posesList.addValueListener( poseAnimationListener );
	}

	public IkPoser getIkPoser() {
		return this.ikPoser;
	}

	@Override
	protected PoserControlView createView() {
		return new PoserControlView( this );
	}

	public ActionOperation getSavePoseOperation() {
		return this.savePoseOperation;
	}

	public ActionOperation getRunAnimationOperation() {
		return this.runAnimationOperation;
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

	public ListSelectionState<PoseAnimation> getPosesList() {
		return this.posesList;
	}

	public ActionOperation getDeletePoseOperation() {
		return this.deletePoseOperation;
	}

	public ActionOperation getDeselectPoseOperation() {
		return this.deselectPoseOperation;
	}

	public ActionOperation getSaveUpdatedPoseOperation() {
		return this.saveUpdatedPoseOperation;
	}

	public NameAndExportAnimationCompositeInHonorOfJenLapp getExportAnimation() {
		return this.nameAndExportAnimationComposite;
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
}
