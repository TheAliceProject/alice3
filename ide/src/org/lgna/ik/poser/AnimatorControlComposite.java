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
import org.lgna.croquet.BoundedDoubleState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Group;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.ik.poser.events.TimeLineListener;
import org.lgna.ik.poser.view.AnimatorControlView;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.AnimationStyle;
import org.lgna.story.SetPose;
import org.lgna.story.SetPose.Detail;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class AnimatorControlComposite extends AbstractPoserControlComposite<AnimatorControlView> {

	public AnimatorControlComposite( AbstractPoserSplitComposite parent ) {
		super( parent, java.util.UUID.fromString( "09599add-4c1b-4ec6-ab5d-4c35f9053bae" ) );
		System.out.println( "parent: " + parent );
		posesList.addValueListener( poseAnimationListener );
		timeLine.addTimeLineListener( listener );
		currentTime.addValueListener( new ValueListener<Double>() {

			public void changing( State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			}

			public void changed( State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
				timeLine.setCurrentTime( nextValue );
			}
		} );
		currentTime.setValueTransactionlessly( timeLine.getCurrentTime() );
	}

	protected ListSelectionState<PoseAnimation> posesList = createListSelectionState( createKey( "listOfPoses" ), PoseAnimation.class,
			new ItemCodec<PoseAnimation>() {

				public Class<PoseAnimation> getValueClass() {
					return PoseAnimation.class;
				}

				public PoseAnimation decodeValue( BinaryDecoder binaryDecoder ) {
					throw new RuntimeException( "todo" );
				}

				public void encodeValue( BinaryEncoder binaryEncoder, PoseAnimation value ) {
					throw new RuntimeException( "todo" );
				}

				public void appendRepresentation( StringBuilder sb, PoseAnimation value ) {
					sb.append( value );
				}

			}, -1 );
	protected Map<PoseAnimation, Detail[]> animationToDetailMap = Collections.newHashMap();
	protected TimeLineComposite timeLine = new TimeLineComposite();
	private BoundedDoubleState currentTime = createBoundedDoubleState( createKey( "currentTime" ), new BoundedDoubleDetails() );
	private AppendTimeToAnimationComposite appendTimeComposite = new AppendTimeToAnimationComposite( this );
	private TimeLineListener listener = new TimeLineListener() {

		public void changed() {
			currentTime.setValueTransactionlessly( timeLine.getCurrentTime() );
		}
	};

	private ValueListener<PoseAnimation> poseAnimationListener = new ValueListener<PoseAnimation>() {

		public void changing( State<PoseAnimation> state, PoseAnimation prevValue, PoseAnimation nextValue, boolean isAdjusting ) {
		}

		public void changed( State<PoseAnimation> state, PoseAnimation prevValue, PoseAnimation nextValue, boolean isAdjusting ) {
			if( nextValue != null ) {
				timeLine.addTimeLineListener( listener );
				final org.lgna.story.implementation.ProgramImp programImp = org.lgna.story.ImplementationAccessor.getImplementation( ikPoser );
				final org.lgna.story.SBiped ogre = parent.getBiped();
				nextValue.animate( programImp, ogre );
			}
		}
	};

	private ActionOperation savePoseOperation = createActionOperation( createKey( "savePose" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			( (AnimatorControlView)AnimatorControlComposite.this.getView() ).enableExport();
			Pose pose = parent.getPose();
			PoseAnimation pAnimation = new PoseAnimation( pose );
			posesList.addItem( pAnimation );
			timeLine.addEventAtCurrentTime( pose );
			timeLine.setCurrentTime( timeLine.getCurrentTime() + 1 );
			return null;
		}
	} );

	private ActionOperation runAnimationOperation = createActionOperation( createKey( "runAnimation" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {

			posesList.setSelectedIndex( -1 );

			//			final org.lgna.story.implementation.ProgramImp programImp = org.lgna.story.ImplementationAccessor.getImplementation( ikPoser );
			final org.lgna.story.SBiped ogre = parent.getBiped();
			ogre.straightenOutJoints( org.lgna.story.StraightenOutJoints.duration( 0 ) );

			ComponentThread thread = new ComponentThread( new Runnable() {
				public void run() {
					for( PoseAnimation pAnimation : posesList ) {
						System.out.println( "run Animation!!!" );
						ogre.setPose( pAnimation.getPose(), timeLine.getParametersForPose( pAnimation.getPose() ) );
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
				( (AnimatorControlView)AnimatorControlComposite.this.getView() ).disableExport();
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
					newValues.add( new PoseAnimation( parent.getPose() ) );
				} else {
					newValues.add( posesList.getItemAt( i ) );
				}
			}
			posesList.setItems( newValues );
			return null;
		}
	} );
	protected NameAndExportAnimationCompositeInHonorOfJenLapp nameAndExportAnimationComposite = new NameAndExportAnimationCompositeInHonorOfJenLapp( this );

	public NameAndExportAnimationCompositeInHonorOfJenLapp getExportAnimation() {
		return this.nameAndExportAnimationComposite;
	}

	private static final org.lgna.project.ast.JavaMethod SET_POSE_METHOD = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SBiped.class, "setPose", Pose.class, SetPose.Detail[].class );
	private static final Group GROUP = Group.getInstance( java.util.UUID.fromString( "813e60bb-77f3-45b5-a319-aa0bc42faffb" ), "AnimatorOperations" );

	public UserMethod createUserMethod( CompletionStep<?> completionStep, String name ) {
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = org.alice.stageide.StoryApiConfigurationManager.getInstance();
		org.alice.ide.ast.ExpressionCreator expressionCreator = apiConfigurationManager.getExpressionCreator();

		ExpressionStatement[] miArr = new ExpressionStatement[ posesList.getItemCount() ];
		int i = 0;
		for( PoseAnimation animation : posesList ) {
			try {
				Detail[] details = timeLine.getParametersForPose( animation.getPose() );
				Expression argumentExpression = expressionCreator.createExpression( animation.getPose() );
				MethodInvocation methodInv = AstUtilities.createMethodInvocation( new org.lgna.project.ast.ThisExpression(), SET_POSE_METHOD, argumentExpression );
				//
				org.lgna.project.ast.JavaMethod durationKeyMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.DurationAnimationStyleArgumentFactory.class, "duration", Number.class );
				methodInv.keyedArguments.add( new org.lgna.project.ast.JavaKeyedArgument(
						methodInv.method.getValue().getKeyedParameter(), durationKeyMethod, new org.lgna.project.ast.DoubleLiteral( SetPose.getDuration( details ) ) ) );

				//animationStyle
				org.lgna.project.ast.JavaMethod styleKeyMethod = org.lgna.project.ast.JavaMethod.getInstance(
						org.lgna.story.DurationAnimationStyleArgumentFactory.class, "animationStyle", AnimationStyle.class );
				methodInv.keyedArguments.add( new org.lgna.project.ast.JavaKeyedArgument( methodInv.method.getValue().getKeyedParameter(),
						styleKeyMethod, expressionCreator.createExpression( SetPose.getStyle( details ) ) ) );
				//
				ExpressionStatement statement = new ExpressionStatement( methodInv );
				miArr[ i ] = statement;
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
				throw new RuntimeException( ccee );
			}
			++i;
		}
		BlockStatement body = new BlockStatement( miArr );
		UserMethod rv = new UserMethod( name, Void.TYPE, new UserParameter[ 0 ], body );

		return rv;
	}

	private Detail[] getParametersForAnimation( PoseAnimation animation ) {
		if( animationToDetailMap.get( animation ) != null ) {
			return animationToDetailMap.get( animation );
		}
		//timeline.getParametersForAnimation(Pose)
		return new Detail[ 0 ];
	}

	public ActionOperation getSavePoseOperation() {
		return this.savePoseOperation;
	}

	public ActionOperation getRunAnimationOperation() {
		return this.runAnimationOperation;
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

	public ListSelectionState<PoseAnimation> getPosesList() {
		return this.posesList;
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

}
