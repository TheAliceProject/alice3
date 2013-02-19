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

package org.alice.stageide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class DeclareFieldFromPersonResourceIteratingOperation extends org.lgna.croquet.SingleThreadIteratingOperation {
	private static class SingletonHolder {
		private static DeclareFieldFromPersonResourceIteratingOperation instance = new DeclareFieldFromPersonResourceIteratingOperation();
	}

	public static DeclareFieldFromPersonResourceIteratingOperation getInstance() {
		return SingletonHolder.instance;
	}

	private DeclareFieldFromPersonResourceIteratingOperation() {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "0ec73a7c-f272-4ff1-87eb-f5f25e480ace" ) );
	}

	@Override
	protected boolean hasNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, java.lang.Object iteratingData ) {
		return subSteps.size() < 2;
	}

	@Override
	protected org.lgna.croquet.Model getNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, java.lang.Object iteratingData ) {
		switch( subSteps.size() ) {
		case 0:
			return org.alice.stageide.personresource.RandomPersonResourceComposite.getInstance().getValueCreator();
		case 1:
			org.lgna.croquet.history.Step<?> prevSubStep = subSteps.get( 0 );
			if( prevSubStep.containsEphemeralDataFor( org.lgna.croquet.ValueCreator.VALUE_KEY ) ) {
				org.lgna.story.resources.sims2.PersonResource personResource = (org.lgna.story.resources.sims2.PersonResource)prevSubStep.getEphemeralDataFor( org.lgna.croquet.ValueCreator.VALUE_KEY );
				org.alice.stageide.ast.declaration.AddPersonResourceManagedFieldComposite addPersonResourceManagedFieldComposite = org.alice.stageide.ast.declaration.AddPersonResourceManagedFieldComposite.getInstance();
				addPersonResourceManagedFieldComposite.setPersonResource( personResource );
				return addPersonResourceManagedFieldComposite.getOperation();
			} else {
				return null;
			}
		default:
			return null;
		}
	}

	@Override
	protected void handleSuccessfulCompletionOfSubModels( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps ) {
		step.finish();
	}

	//	protected abstract org.alice.stageide.person.PersonComposite getPersonComposite();
	//
	//	//	protected abstract org.lgna.story.resources.sims2.PersonResource createInitialPersonResource();
	//	@Override
	//	protected org.lgna.croquet.components.SplitPane prologue( org.lgna.croquet.history.CompletionStep<?> step ) {
	//		//		org.lgna.story.resources.sims2.PersonResource personResource = this.createInitialPersonResource();
	//		//		org.alice.stageide.person.components.PersonViewer personViewer = org.alice.stageide.person.PersonResourceManager.SINGLETON.allocatePersonViewer( personResource );
	//		//		org.alice.stageide.person.components.MainPanel rv = new org.alice.stageide.person.components.MainPanel( personViewer );
	//		org.alice.ide.IDE.getActiveInstance().getPerspectiveState().getValue().disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN ); //todo
	//		return this.getPersonComposite().getView();
	//	}
	//
	//	@Override
	//	protected void modifyPackedDialogSizeIfDesired( org.lgna.croquet.components.Dialog dialog ) {
	//		dialog.setSize( 1000, 700 );
	//	}
	//
	//	protected abstract org.lgna.croquet.edits.Edit<?> createEdit( org.lgna.story.resources.sims2.PersonResource personResource );
	//
	//	@Override
	//	protected void epilogue( org.lgna.croquet.history.CompletionStep<?> step, boolean isCommit ) {
	//		//		org.alice.stageide.person.components.MainPanel mainPanel = (org.alice.stageide.person.components.MainPanel)step.getMainPanel();
	//		//		org.alice.stageide.person.components.PersonViewer personViewer = mainPanel.getPersonViewer();
	//		//		org.alice.stageide.person.PersonResourceManager.SINGLETON.releasePersonViewer( personViewer );
	//		this.getPersonComposite().releaseView();
	//		org.alice.ide.IDE.getActiveInstance().getPerspectiveState().getValue().enableRendering();
	//		if( isCommit ) {
	//			org.lgna.story.resources.sims2.PersonResource personResource = org.alice.stageide.person.PersonResourceManager.SINGLETON.createResourceFromStates();
	//			if( personResource != null ) {
	//				org.lgna.croquet.edits.Edit edit = this.createEdit( personResource );
	//				if( edit != null ) {
	//					step.commitAndInvokeDo( edit );
	//				} else {
	//					step.putEphemeralDataFor( VALUE_KEY, personResource );
	//				}
	//			}
	//		} else {
	//			step.cancel();
	//		}
	//	}
}
