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
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.BoundedDoubleState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.ik.poser.AnimatorControlComposite;
import org.lgna.ik.poser.animation.KeyFrameData;
import org.lgna.ik.poser.animation.KeyFrameStyles;
import org.lgna.ik.poser.animation.TimeLineListener;
import org.lgna.ik.poser.animation.views.TimeLineModifierView;

/**
 * @author Matt May
 */
public class TimeLineModifierComposite extends SimpleComposite<TimeLineModifierView> {

	private TimeLineComposite composite;
	private BoundedDoubleState currentTime = createBoundedDoubleState( createKey( "currentTime" ), new BoundedDoubleDetails() );
	private KeyFrameData selectedKeyFrame;
	private BooleanState isEditing = createBooleanState( createKey( "isEditing" ), false );
	private AnimatorControlComposite parent;
	private ListSelectionState<KeyFrameStyles> styleSelectionState = this.createListSelectionStateForEnum( createKey( "styleState" ), KeyFrameStyles.class, KeyFrameStyles.ARRIVE_AND_EXIT_GENTLY );

	public TimeLineModifierComposite( TimeLineComposite composite, AnimatorControlComposite parent ) {
		super( java.util.UUID.fromString( "b2c9fe7b-4566-4368-a5cc-2458b24a2375" ) );
		this.parent = parent;
		this.composite = composite;
		composite.getTimeLine().addListener( listener );
		updateSelectedEvent( null );
	}

	ValueListener<Boolean> isEditingListener = new ValueListener<Boolean>() {

		public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		/** edit/revert **/
		public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			if( nextValue ) {
				composite.setEditEnabled( true );
			} else {
				parent.revertPose( selectedKeyFrame );
				composite.setEditEnabled( false );
			}
		}
	};

	private TimeLineListener listener = new TimeLineListener() {

		public void currentTimeChanged( double currentTime ) {
			TimeLineModifierComposite.this.currentTime.setValueTransactionlessly( new Double( currentTime ) );
		}

		public void keyFrameAdded( KeyFrameData event ) {
			//do nothing don't care here
		}

		public void selectedKeyFrameChanged( KeyFrameData event ) {
			TimeLineModifierComposite.this.updateSelectedEvent( event );
		}

		public void keyFrameDeleted( KeyFrameData event ) {
			if( event.equals( selectedKeyFrame ) ) {
				selectedKeyFrame = null;
				TimeLineModifierComposite.this.updateSelectedEvent( null );
			}
		}

		public void keyFrameModified( KeyFrameData event ) {
			if( event == selectedKeyFrame ) {
				TimeLineModifierComposite.this.updateSelectedEvent( event );
			}
		}

		public void endTimeChanged( double endTime ) {
		}

	};

	protected void updateSelectedEvent( KeyFrameData event ) {
		this.selectedKeyFrame = event;
		boolean activate = event != null;
		deletePoseOperation.setEnabled( activate );
		saveUpdatedPoseOperation.setEnabled( activate );
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

	private ActionOperation deletePoseOperation = createActionOperation( createKey( "deletePose" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			composite.getTimeLine().removeKeyFrameData( selectedKeyFrame );
			return null;
		}
	} );
	private ActionOperation saveUpdatedPoseOperation = createActionOperation( createKey( "savePoseChanges" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			composite.getTimeLine().modifyExistingPose( selectedKeyFrame, parent.getCurrentPose() );
			return null;
		}
	} );

	public ActionOperation getDeletePoseOperation() {
		return this.deletePoseOperation;
	}

	public ActionOperation getSaveUpdatedPoseOperation() {
		return this.saveUpdatedPoseOperation;
	}

	public BooleanState getIsEditing() {
		return this.isEditing;
	}

	public ListSelectionState<KeyFrameStyles> getStyleSelectionState() {
		return this.styleSelectionState;
	}
}
