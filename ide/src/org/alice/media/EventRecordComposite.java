/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.alice.media;

import org.alice.media.components.EventRecordView;
import org.alice.stageide.program.RunProgramContext;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.matt.EventScript;

/**
 * @author Matt May
 */
public class EventRecordComposite extends WizardPageComposite<EventRecordView> {

	private final ExportToYouTubeWizardDialogComposite owner;
	private RunProgramContext programContext;
	private EventScript script;
	org.lgna.croquet.components.BorderPanel lookingGlassContainer;
	private final ErrorStatus cannotAdvanceBecauseRecording = this.createErrorStatus( this.createKey( "cannotAdvanceBecauseRecording" ) );

	private BooleanState isRecordingState = this.createBooleanState( this.createKey( "isRecordingState" ), false );

	private final ValueListener<Boolean> isRecordingListener = new ValueListener<Boolean>() {
		public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			if( isRecordingState.getValue() ) {
				programContext.getProgramImp().startAnimator();
			} else {
				programContext.getProgramImp().stopAnimator();
			}
		}
	};

	public EventRecordComposite( ExportToYouTubeWizardDialogComposite owner ) {
		super( java.util.UUID.fromString( "35d34417-8c0c-4f06-b919-5945b336b596" ) );
		assert owner != null;
		this.owner = owner;
		isRecordingState.addValueListener( isRecordingListener );
	}

	private final ActionOperation restartRecording = this.createActionOperation( this.createKey( "restart" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			lookingGlassContainer.removeAllComponents();
			lookingGlassContainer = getView().getLookingGlassContainer();
			programContext = new RunProgramContext( owner.getProject().getProgramType() );
			programContext.initializeInContainer( lookingGlassContainer.getAwtComponent(), 640, 360 );
			programContext.getProgramImp().stopAnimator();
			programContext.setActiveScene();
			script = ( (SceneImp)ImplementationAccessor.getImplementation( programContext.getProgram().getActiveScene() ) ).getTranscript();
			owner.setScript( script );
			return null;
		}
	} );

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		lookingGlassContainer = getView().getLookingGlassContainer();
		if( programContext == null ) {
			programContext = new RunProgramContext( owner.getProject().getProgramType() );
			programContext.initializeInContainer( lookingGlassContainer.getAwtComponent(), 640, 360 );
			programContext.getProgramImp().stopAnimator();
			programContext.setActiveScene();
		}
		script = ( (SceneImp)ImplementationAccessor.getImplementation( programContext.getProgram().getActiveScene() ) ).getTranscript();
		owner.setScript( script );
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
	}

	public BooleanState getPlayRecordedOperation() {
		return this.isRecordingState;
	}

	public ActionOperation getRestartRecording() {
		return this.restartRecording;
	}

	public EventScript getScript() {
		return this.script;
	}

	@Override
	protected EventRecordView createView() {
		return new EventRecordView( this );
	}

	@Override
	public Status getPageStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		return isRecordingState.getValue() ? cannotAdvanceBecauseRecording : IS_GOOD_TO_GO_STATUS;
	}
}
