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

import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BoundedDoubleState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Group;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.ik.poser.animationTimeLine.models.TimeLineComposite;
import org.lgna.ik.poser.animationTimeLine.models.TimeLineComposite.PoseEvent;
import org.lgna.ik.poser.animationTimeLine.models.TimeLineModifierComposite;
import org.lgna.ik.poser.view.AnimatorControlView;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AnimationStyle;
import org.lgna.story.SetPose;

/**
 * @author Matt May
 */
public class AnimatorControlComposite extends AbstractPoserControlComposite<AnimatorControlView> {

	private StringState nameState = createStringState( createKey( "nameState" ) );

	public AnimatorControlComposite( AbstractPoserInputDialogComposite parent ) {
		super( parent, java.util.UUID.fromString( "09599add-4c1b-4ec6-ab5d-4c35f9053bae" ) );
		currentTime.addValueListener( new ValueListener<Double>() {

			public void changing( State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			}

			public void changed( State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
				timeLine.setCurrentTime( nextValue );
			}
		} );
		currentTime.setValueTransactionlessly( timeLine.getCurrentTime() );
	}

	private TimeLineComposite timeLine = new TimeLineComposite();
	private BoundedDoubleState currentTime = createBoundedDoubleState( createKey( "currentTime" ), new BoundedDoubleDetails() );
	private AppendTimeToAnimationComposite appendTimeComposite = new AppendTimeToAnimationComposite( this );
	private TimeLineModifierComposite editComposite = new TimeLineModifierComposite( timeLine, this );

	private ActionOperation savePoseOperation = createActionOperation( createKey( "savePose" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			Pose pose = parent.getPose();
			timeLine.addEventAtCurrentTime( pose );
			timeLine.setCurrentTime( timeLine.getCurrentTime() + 1 );
			return null;
		}
	} );

	private ActionOperation runAnimationOperation = createActionOperation( createKey( "runAnimation" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			//
			//			posesList.setSelectedIndex( -1 );
			//
			//			//			final org.lgna.story.implementation.ProgramImp programImp = org.lgna.story.ImplementationAccessor.getImplementation( ikPoser );
			//			final org.lgna.story.SBiped ogre = parent.getBiped();
			//			ogre.straightenOutJoints( org.lgna.story.StraightenOutJoints.duration( 0 ) );
			//
			//			//			ComponentThread thread = new ComponentThread( new Runnable() {
			//			//				public void run() {
			//			//					for( PoseAnimation pAnimation : posesList ) {
			//			//						ogre.setPose( pAnimation.getPose(), timeLine.getParametersForPose( pAnimation.getPose() ) );
			//			//					}
			//			//				}
			//			//			}, "noDescription" );
			//			//			thread.start();
			return null;
		}
	} );

	private static final org.lgna.project.ast.JavaMethod SET_POSE_METHOD = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SBiped.class, "setPose", Pose.class, SetPose.Detail[].class );
	private static final Group GROUP = Group.getInstance( java.util.UUID.fromString( "813e60bb-77f3-45b5-a319-aa0bc42faffb" ), "AnimatorOperations" );

	public BlockStatement createMethodBody() {
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = org.alice.stageide.StoryApiConfigurationManager.getInstance();
		org.alice.ide.ast.ExpressionCreator expressionCreator = apiConfigurationManager.getExpressionCreator();

		ListSelectionState<PoseEvent> poseEventList = timeLine.getPoseEventListSelectionState();
		ExpressionStatement[] miArr = new ExpressionStatement[ poseEventList.getItemCount() ];
		int i = 0;
		for( PoseEvent event : poseEventList ) {
			try {
				Expression argumentExpression = expressionCreator.createExpression( event.getPose() );
				double duration = timeLine.getDurationForPose( event.getPose() );
				AnimationStyle style = timeLine.getStyleForPose( event.getPose() );
				MethodInvocation methodInv = AstUtilities.createMethodInvocation( new org.lgna.project.ast.ThisExpression(), SET_POSE_METHOD, argumentExpression );
				//
				org.lgna.project.ast.JavaMethod durationKeyMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
				methodInv.keyedArguments.add(
						new org.lgna.project.ast.JavaKeyedArgument(
								methodInv.method.getValue().getKeyedParameter(),
								durationKeyMethod,
								new org.lgna.project.ast.DoubleLiteral( duration ) ) );

				//animationStyle
				org.lgna.project.ast.JavaMethod styleKeyMethod = org.lgna.project.ast.JavaMethod.getInstance(
						org.lgna.story.DurationAnimationStyleArgumentFactory.class, "animationStyle", AnimationStyle.class );
				methodInv.keyedArguments.add( new org.lgna.project.ast.JavaKeyedArgument( methodInv.method.getValue().getKeyedParameter(),
						styleKeyMethod, expressionCreator.createExpression( style ) ) );
				//
				ExpressionStatement statement = new ExpressionStatement( methodInv );
				miArr[ i ] = statement;
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
				throw new RuntimeException( ccee );
			}
			++i;
		}
		return new BlockStatement( miArr );
	}

	public ActionOperation getSavePoseOperation() {
		return this.savePoseOperation;
	}

	public ActionOperation getRunAnimationOperation() {
		return this.runAnimationOperation;
	}

	public TimeLineComposite getTimeLine() {
		return this.timeLine;
	}

	@Override
	protected AnimatorControlView createView() {
		return new AnimatorControlView( this );
	}

	public BoundedDoubleState getCurrentTime() {
		return this.currentTime;
	}

	public AppendTimeToAnimationComposite getAppendTimeComposite() {
		return this.appendTimeComposite;
	}

	public Group getGroup() {
		return GROUP;
	}

	public void addTime( Double value ) {
		timeLine.setMaxTime( value );
	}

	public TimeLineModifierComposite getEditComposite() {
		return this.editComposite;
	}

	public StringState getNameState() {
		return this.nameState;
	}

	public void revertPose( PoseEvent selectedPose ) {
	}

	public Pose getCurrentPose() {
		return parent.getPose();
	}
}
