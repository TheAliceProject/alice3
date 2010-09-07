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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class PopupMenuStep extends AuthoredWaitingStep<edu.cmu.cs.dennisc.croquet.PopupMenuOperation> {
	private PopupMenuOperationCompletor completor;
	private PopupMenuOperationValidator validator;
	public PopupMenuStep( String title, String popupText, final edu.cmu.cs.dennisc.croquet.RuntimeResolver<edu.cmu.cs.dennisc.croquet.PopupMenuOperation> popupMenuOperationResolver, String commitText, PopupMenuOperationCompletor completor, PopupMenuOperationValidator validator ) {
		super( title, popupText, new Hole( new FirstComponentResolver( popupMenuOperationResolver ), Feature.ConnectionPreference.EAST_WEST ), popupMenuOperationResolver );
		this.completor = completor;
		this.validator = validator;

		Note commitNote = new Note( commitText );
		this.addNote( commitNote );
		final int N = this.getNoteCount();
		for( int i=0; i<N; i++ ) {
			this.getNoteAt( i ).setLabel( Integer.toString(i+1) );
		}
	}

	@Override
	public boolean isAlreadyInTheDesiredState() {
		return false;
	}
	@Override
	protected void complete() {
		TutorialStencil.complete( this.completor.createEdit( this.getModel() ) );
	}

	@Override
	public void reset() {
		super.reset();
		this.setActiveNote( 0 );
	}
	private enum State {
		WAITING_ON_POPUP,
		WAITING_ON_COMMIT,
	}
	private State getState() {
		int index = this.getIndexOfFirstActiveNote();
		switch( index ) {
		case 0:
			return State.WAITING_ON_POPUP;
		case 1:
			return State.WAITING_ON_COMMIT;
		default:
			return null;
		}
	}
	
	@Override
	public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
		State state = this.getState();
		boolean rv = false;
		if( child instanceof edu.cmu.cs.dennisc.croquet.CancelEvent ) {
			SoundCache.FAILURE.startIfNotAlreadyActive();
			this.reset();
		} else {
			switch( state ) {
			case WAITING_ON_POPUP:
				if( child instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext ) {
					edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext popupMenuOperationContext = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext)child;
					if( popupMenuOperationContext.getModel() == this.getModel() ) {
						this.setActiveNote( 1 );
					}
				}
				break;
			case WAITING_ON_COMMIT:
				if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent ) {
					edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent completeEvent = (edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent)child;
//					edu.cmu.cs.dennisc.croquet.Model eventModel = completeEvent.getParent().getModel();
//					if( this.getModel() == eventModel ) {
					edu.cmu.cs.dennisc.croquet.Edit<?> edit;
						if (child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent) {
							edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = (edu.cmu.cs.dennisc.croquet.CommitEvent) child;
							edit = commitEvent.getEdit();
						} else {
							edit = null;
						}
						rv = this.validator.checkValidity( this.getModel(), edit ).isProcedeApprorpiate();
						if( rv ) {
							SoundCache.SUCCESS.startIfNotAlreadyActive();
						} else {
							SoundCache.FAILURE.startIfNotAlreadyActive();
						}
//					}
				}
				break;
			}
		}
		return rv;
	}
}
