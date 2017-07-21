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
package org.lgna.ik.poser.animation.composites;

import java.util.ArrayList;
import java.util.List;

import org.lgna.common.ComponentThread;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.BoundedDoubleState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Group;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.CompositeView;
import org.lgna.ik.poser.PoserSphereManipulatorListener;
import org.lgna.ik.poser.animation.KeyFrameData;
import org.lgna.ik.poser.animation.TimeLine;
import org.lgna.ik.poser.animation.TimeLineListener;
import org.lgna.ik.poser.animation.edits.AddKeyFrameToTimeLineEdit;
import org.lgna.ik.poser.animation.edits.ModifyPoseOnExistingKeyFrameInTimeLineEdit;
import org.lgna.ik.poser.controllers.PoserEvent;
import org.lgna.ik.poser.croquet.AbstractPoserOrAnimatorComposite;
import org.lgna.ik.poser.croquet.AnimatorComposite;
import org.lgna.ik.poser.croquet.views.AnimatorControlView;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;
import org.lgna.story.AnimationStyle;
import org.lgna.story.Duration;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.Pose;
import org.lgna.story.SJointedModel;
import org.lgna.story.StrikePose;
import org.lgna.story.implementation.PoseUtilities;

/**
 * @author Matt May
 */
public class AnimatorControlComposite<M extends SJointedModel> extends AbstractPoserControlComposite<AnimatorControlView> {

	public static final Group GROUP = Group.getInstance( java.util.UUID.fromString( "813e60bb-77f3-45b5-a319-aa0bc42faffb" ), "AnimatorOperations" );
	private final StringState nameState = createStringState( "nameState" );
	private final TimeLineComposite tlComposite = new TimeLineComposite();
	private final BoundedDoubleState currentTime = createBoundedDoubleState( "currentTime", new BoundedDoubleDetails() );
	private final TimeLineModifierComposite tlModifierComposite = new TimeLineModifierComposite( tlComposite );

	public AnimatorControlComposite( AbstractPoserOrAnimatorComposite parent ) {
		super( parent, java.util.UUID.fromString( "09599add-4c1b-4ec6-ab5d-4c35f9053bae" ) );
		currentTime.addValueListener( new ValueListener<Double>() {

			@Override
			public void changing( State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			}

			@Override
			public void changed( State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
				tlComposite.getTimeLine().setCurrentTime( nextValue );
			}
		} );
		parent.getScene().addDragListener( sphereDragListener );
		currentTime.setValueTransactionlessly( tlComposite.getTimeLine().getCurrentTime() );

		tlComposite.getTimeLine().addListener( timeLineListener );
		tlComposite.setInitialPose( PoseUtilities.createPoseFromT( parent.getModel() ) );
	}

	private final TimeLineListener timeLineListener = new TimeLineListener() {

		@Override
		public void selectedKeyFrameChanged( KeyFrameData event ) {
		}

		@Override
		public void keyFrameAdded( KeyFrameData event ) {
			if( tlComposite.getTimeLine().getCurrentTime() == event.getEventTime() ) {
				tlComposite.selectKeyFrame( event );
			}
		}

		@Override
		public void keyFrameDeleted( KeyFrameData event ) {
			tlComposite.getTimeLine().setCurrentTime( tlComposite.getTimeLine().getCurrentTime() );//to refire to get interpolation for pose
		}

		@Override
		public void keyFrameModified( KeyFrameData event ) {
			tlComposite.getTimeLine().setCurrentTime( event.getEventTime() );
		}

		@Override
		public void endTimeChanged( double endTime ) {
		}

		@Override
		public void currentTimeChanged( double currentTime, Pose pose ) {
			parent.strikePose( pose );
		}
	};

	private final PoserSphereManipulatorListener sphereDragListener = new PoserSphereManipulatorListener() {

		@Override
		public void fireStart( PoserEvent poserEvent ) {
		}

		@Override
		public void fireFinish( PoserEvent poserEvent ) {
			TimeLine timeLine = tlComposite.getTimeLine();
			KeyFrameData currentFrame = timeLine.getFrameForCurrentTime();
			org.lgna.croquet.history.TransactionHistory history = Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
			org.lgna.croquet.history.Transaction transaction = history.acquireActiveTransaction();
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( null, NullTrigger.createUserInstance() );
			if( currentFrame != null ) {
				step.commitAndInvokeDo( new ModifyPoseOnExistingKeyFrameInTimeLineEdit( step, timeLine, currentFrame, parent.getPose(), currentFrame.getPose() ) );
			} else {
				step.commitAndInvokeDo( new AddKeyFrameToTimeLineEdit( step, timeLine, new KeyFrameData( timeLine.getCurrentTime(), parent.getPose() ) ) );
			}
		}

		@Override
		public void fireAnchorUpdate( PoserEvent poserEvent ) {
		}
	};
	private final ActionOperation runAnimationOperation = createActionOperation( "runAnimation", new Action() {

		boolean stillRunning = true;

		@Override
		public AbstractEdit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			final M model = (M)parent.getModel();
			final TimeLine timeLine = tlComposite.getTimeLine();
			final List<KeyFrameData> keyFrames = timeLine.getKeyFrames();
			final ComponentThread timerThread = new ComponentThread( new Runnable() {

				@Override
				public void run() {
					timeLine.disableListeners();
					timeLine.setCurrentTime( 0 );
					while( stillRunning ) {
						try {
							Thread.sleep( 10 );
							timeLine.setCurrentTime( timeLine.getCurrentTime() + .01 );
						} catch( InterruptedException e ) {
							e.printStackTrace();
						}
					}
					timeLine.setCurrentTime( timeLine.getKeyFrames().get( timeLine.getKeyFrames().size() - 1 ).getEventTime() );
					timeLine.enableListeners();
				}

			}, "runAnimation" );
			ComponentThread thread = new ComponentThread( new Runnable() {

				@Override
				public void run() {
					if( keyFrames.size() > 0 ) {
						model.straightenOutJoints();
						stillRunning = true;
						timerThread.start();
					}
					for( KeyFrameData data : keyFrames ) {
						double duration = timeLine.getDurationForKeyFrame( data );
						AnimationStyle styleForKeyFramePose = timeLine.getStyleForKeyFramePose( data );
						org.lgna.story.implementation.JointedModelImp imp = EmployeesOnly.getImplementation( model );
						imp.strikePose( data.getPoseActual(), duration, EmployeesOnly.getInternal( styleForKeyFramePose ) );
						//						tlComposite.selectKeyFrame( data );
					}
					stillRunning = false;
				}

			}, "runAnimation" );
			thread.start();
			return null;
		}
	} );

	public BlockStatement createMethodBody() {
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = org.alice.stageide.StoryApiConfigurationManager.getInstance();
		org.alice.ide.ast.ExpressionCreator expressionCreator = apiConfigurationManager.getExpressionCreator();

		List<KeyFrameData> keyFrameList = tlComposite.getTimeLine().getKeyFrames();
		ExpressionStatement[] miArr = new ExpressionStatement[ keyFrameList.size() ];
		int i = 0;
		for( KeyFrameData event : keyFrameList ) {
			try {
				Expression argumentExpression = expressionCreator.createExpression( event.getPose() );
				double duration = tlComposite.getTimeLine().getDurationForKeyFrame( event );
				AnimationStyle style = tlComposite.getTimeLine().getStyleForKeyFramePose( event );

				Class<? extends Pose> poseCls = PoseUtilities.getPoseClassForModelClass( parent.getModelClass() );
				org.lgna.project.ast.JavaMethod STRIKE_POSE_METHOD = org.lgna.project.ast.JavaMethod.getInstance( parent.getModelClass(), org.lgna.ik.poser.PoseAstUtilities.STRIKE_POSE_METHOD_NAME, poseCls, StrikePose.Detail[].class );
				MethodInvocation methodInv = AstUtilities.createMethodInvocation( new org.lgna.project.ast.ThisExpression(), STRIKE_POSE_METHOD, argumentExpression );
				org.lgna.project.ast.JavaMethod durationKeyMethod = org.lgna.project.ast.JavaMethod.getInstance(
						org.lgna.story.DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
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

	public ActionOperation getRunAnimationOperation() {
		return this.runAnimationOperation;
	}

	public TimeLineComposite getTimeLine() {
		return this.tlComposite;
	}

	@Override
	protected AnimatorControlView createView() {
		return new AnimatorControlView( this );
	}

	public BoundedDoubleState getCurrentTime() {
		return this.currentTime;
	}

	public Group getGroup() {
		return GROUP;
	}

	public TimeLineModifierComposite getEditComposite() {
		return this.tlModifierComposite;
	}

	public StringState getNameState() {
		return this.nameState;
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		UserMethod method = ( (AnimatorComposite)parent ).getMethod();
		if( method == null ) {
			tlComposite.getTimeLine().refresh();
			parent.getModel().straightenOutJoints( new Duration( 0 ) );
		} else {
			parseMethod( method );
			nameState.setValueTransactionlessly( method.getName() );
			nameState.setEnabled( false );
		}
	}

	public CompositeView getSouthViewForDialog() {
		BorderPanel borderPanel = new BorderPanel();
		borderPanel.addCenterComponent( tlComposite.getView() );
		borderPanel.addLineStartComponent( runAnimationOperation.createButton() );
		return borderPanel;
	}

	public void parseMethod( UserMethod method ) {
		ArrayList<KeyFrameData> frames = AnimationParser.initializeAndParse( method );
		for( KeyFrameData frame : frames ) {
			tlComposite.getTimeLine().addKeyFrameData( frame );
		}
	}

	public boolean isEmpty() {
		return tlComposite.getTimeLine().getKeyFrames().size() == 0;
	}
}
