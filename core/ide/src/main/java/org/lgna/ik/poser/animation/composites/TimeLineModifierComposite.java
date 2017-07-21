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

import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.BoundedDoubleState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.SingleSelectListState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.ik.poser.animation.KeyFrameData;
import org.lgna.ik.poser.animation.KeyFrameStyles;
import org.lgna.ik.poser.animation.TimeLineListener;
import org.lgna.ik.poser.animation.edits.DeleteKeyFrameFromTimeLineEdit;
import org.lgna.ik.poser.animation.edits.ModifyTimeOfExistingKeyFrameInTimeLineEdit;
import org.lgna.ik.poser.animation.views.TimeLineModifierView;
import org.lgna.story.Pose;

/**
 * @author Matt May
 */
public class TimeLineModifierComposite extends SimpleComposite<TimeLineModifierView> {

	private TimeLineComposite composite;
	private KeyFrameData selectedKeyFrame;
	private final BoundedDoubleState currentTime;
	private final SingleSelectListState<KeyFrameStyles, ?> styleSelectionState = this.createImmutableListStateForEnum( "styleState", KeyFrameStyles.class, KeyFrameStyles.ARRIVE_AND_EXIT_GENTLY );

	public TimeLineModifierComposite( TimeLineComposite composite ) {
		super( java.util.UUID.fromString( "b2c9fe7b-4566-4368-a5cc-2458b24a2375" ) );
		this.composite = composite;
		currentTime = createBoundedDoubleState( "currentTime",
				new BoundedDoubleDetails().initialValue( 0 ).minimum( 0 ).maximum( composite.getTimeLine().getEndTime() ).stepSize( .1 ) );

		currentTime.addValueListener( timeListener );
		composite.getTimeLine().addListener( listener );
		styleSelectionState.addValueListener( styleListener );
		updateSelectedEvent( null );
	}

	private final TimeLineListener listener = new TimeLineListener() {

		@Override
		public void currentTimeChanged( double currentTime, Pose pose ) {
			//			TimeLineModifierComposite.this.currentTime.setValueTransactionlessly( new Double( currentTime ) );
		}

		@Override
		public void keyFrameAdded( KeyFrameData event ) {
			//do nothing don't care here
		}

		@Override
		public void selectedKeyFrameChanged( KeyFrameData event ) {
			TimeLineModifierComposite.this.updateSelectedEvent( event );
		}

		@Override
		public void keyFrameDeleted( KeyFrameData event ) {
			if( event.equals( selectedKeyFrame ) ) {
				selectedKeyFrame = null;
				TimeLineModifierComposite.this.updateSelectedEvent( null );
			}
		}

		@Override
		public void keyFrameModified( KeyFrameData event ) {
			if( event == selectedKeyFrame ) {
				TimeLineModifierComposite.this.updateSelectedEvent( event );
			}
		}

		@Override
		public void endTimeChanged( double endTime ) {
		}

	};
	private final ValueListener<KeyFrameStyles> styleListener = new ValueListener<KeyFrameStyles>() {

		@Override
		public void changing( State<KeyFrameStyles> state, KeyFrameStyles prevValue, KeyFrameStyles nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( State<KeyFrameStyles> state, KeyFrameStyles prevValue, KeyFrameStyles nextValue, boolean isAdjusting ) {
			selectedKeyFrame.setStyle( nextValue );
		}
	};

	private final ValueListener<Double> timeListener = new ValueListener<Double>() {

		@Override
		public void changing( State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			//			assert isAdjusting == ( prevValue == nextValue );
			//			if( isAdjusting ) {
			//			System.out.println( "time changed" );
			if( ( selectedKeyFrame != null ) ) {
				org.lgna.croquet.history.TransactionHistory history = Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
				org.lgna.croquet.history.Transaction transaction = history.acquireActiveTransaction();
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( null, NullTrigger.createUserInstance() );

				step.commitAndInvokeDo( new ModifyTimeOfExistingKeyFrameInTimeLineEdit( step, composite.getTimeLine(), selectedKeyFrame, nextValue, prevValue ) );
			}
		}
	};

	protected void updateSelectedEvent( KeyFrameData event ) {
		this.selectedKeyFrame = event;
		boolean activate = event != null;
		deletePoseOperation.setEnabled( activate );
		currentTime.setEnabled( activate );
		getView().enableOperations( activate );
		if( activate ) {
			currentTime.setValueTransactionlessly( event.getEventTime() );
			styleSelectionState.setValueTransactionlessly( event.getEventStyle() );
		}
	}

	@Override
	protected TimeLineModifierView createView() {
		return new TimeLineModifierView( this );
	}

	private ActionOperation deletePoseOperation = createActionOperation( "deletePose", new Action() {

		@Override
		public AbstractEdit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			//			composite.getTimeLine().removeKeyFrameData( selectedKeyFrame );
			new DeleteKeyFrameFromTimeLineEdit( step, composite.getTimeLine(), selectedKeyFrame ).doOrRedo( true );
			return null;
		}
	} );

	public ActionOperation getDeletePoseOperation() {
		return this.deletePoseOperation;
	}

	public SingleSelectListState<KeyFrameStyles, ?> getStyleSelectionState() {
		return this.styleSelectionState;
	}

	public BoundedDoubleState getCurrentTime() {
		return this.currentTime;
	}
}
