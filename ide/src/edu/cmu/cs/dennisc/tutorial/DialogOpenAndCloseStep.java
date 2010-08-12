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
/*package-private*/ class DialogOpenAndCloseStep extends WaitingStep<edu.cmu.cs.dennisc.croquet.DialogOperation> {
	public DialogOpenAndCloseStep( String title, String openText, String closeText, final edu.cmu.cs.dennisc.croquet.Resolver<edu.cmu.cs.dennisc.croquet.DialogOperation> dialogOperationResolver ) {
		super( title, openText, new Hole( new FirstComponentResolver( dialogOperationResolver ), Feature.ConnectionPreference.EAST_WEST ), dialogOperationResolver );

		Note closeNote = new Note( closeText );
		closeNote.addFeature( new DialogCloseButtonFeature( new edu.cmu.cs.dennisc.croquet.Resolver< edu.cmu.cs.dennisc.croquet.TrackableShape >() {
			public edu.cmu.cs.dennisc.croquet.TrackableShape getResolved() {
				edu.cmu.cs.dennisc.croquet.DialogOperation dialogOperation = dialogOperationResolver.getResolved();
				if( dialogOperation != null ) {
					edu.cmu.cs.dennisc.croquet.Dialog activeDialog = dialogOperation.getActiveDialog();
					if( activeDialog != null ) {
						return activeDialog.getCloseButtonTrackableShape();
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
		} ) );
		this.addNote( closeNote );
		final int N = this.getNoteCount();
		for( int i=0; i<N; i++ ) {
			this.getNoteAt( i ).setLabel( Integer.toString(i+1) );
		}
	}
	@Override
	public void reset() {
		super.reset();
		this.setActiveNote( 0 );
	}
	@Override
	protected boolean isAlreadyInTheDesiredState() {
		return false;
	}
	@Override
	protected void complete() {
	}
	
	private enum State {
		WAITING_ON_OPEN,
		WAITING_ON_CLOSE,
	}
	private State getState() {
		int index = this.getIndexOfFirstActiveNote();
		switch( index ) {
		case 0:
			return State.WAITING_ON_OPEN;
		case 1:
			return State.WAITING_ON_CLOSE;
		default:
			return null;
		}
	}
	@Override
	public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
		boolean rv = false;
		State state = this.getState();
		switch( state ) {
		case WAITING_ON_OPEN:
			if( child instanceof edu.cmu.cs.dennisc.croquet.DialogOperationContext.WindowOpenedEvent ) {
				edu.cmu.cs.dennisc.croquet.DialogOperationContext.WindowOpenedEvent windowOpenedEvent = (edu.cmu.cs.dennisc.croquet.DialogOperationContext.WindowOpenedEvent)child;
				if( windowOpenedEvent.getParent().getModel() == this.getModel() ) {
					this.setActiveNote( 1 );
					
					edu.cmu.cs.dennisc.croquet.Dialog dialog = this.getModel().getActiveDialog();
					if( dialog != null ) {
						java.awt.Rectangle dialogLocalBounds = dialog.getLocalBounds();
						Note note1 = this.getNoteAt( 1 );
						java.awt.Rectangle bounds = note1.getBounds( dialog );
						if( bounds.intersects( dialogLocalBounds ) ) {
							note1.setLocation( dialog.getWidth()+100, dialog.getHeight()/2, dialog );
						}
					}
					
				}
			}
			break;
		case WAITING_ON_CLOSE:
			if( child instanceof edu.cmu.cs.dennisc.croquet.DialogOperationContext.WindowClosedEvent ) {
				edu.cmu.cs.dennisc.croquet.DialogOperationContext.WindowClosedEvent windowClosedEvent = (edu.cmu.cs.dennisc.croquet.DialogOperationContext.WindowClosedEvent)child;
				rv = windowClosedEvent.getParent().getModel() == this.getModel();
			}
			break;
		}
		return rv;
	}
}
