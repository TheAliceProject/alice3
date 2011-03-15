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
/*package-private*/ class DragAndDropStep extends AuthoredWaitingStep<edu.cmu.cs.dennisc.croquet.DragAndDropModel> {
	private enum State {
		WAITING_ON_DRAG,
		WAITING_ON_DROP,
		WAITING_ON_POPUP_MENU_COMMIT,
		WAITING_ON_INPUT_DIALOG_COMMIT
	}
	
	private boolean isPopupMenuNotePresent;
	private boolean isInputDialogNotePresent;
	private DragAndDropOperationCompletor completor;
	private DragAndDropOperationValidator validator;
	private edu.cmu.cs.dennisc.croquet.InputDialogOperation.ExternalCommitButtonDisabler<?> externalOkButtonDisabler;
	public DragAndDropStep( 
			String title, 
			String text, 
			edu.cmu.cs.dennisc.croquet.RuntimeResolver< edu.cmu.cs.dennisc.croquet.DragAndDropModel > dragResolver,
			String dropText, 
			edu.cmu.cs.dennisc.croquet.RuntimeResolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > dropShapeResolver, 
			String popupMenuText, 
			String inputDialogText, 
			DragAndDropOperationCompletor completor, 
			DragAndDropOperationValidator validator, 
			edu.cmu.cs.dennisc.croquet.InputDialogOperation.ExternalCommitButtonDisabler<?> externalOkButtonDisabler ) {
		super( title, text, new Hole( new FirstComponentResolver( dragResolver ), Feature.ConnectionPreference.EAST_WEST ), dragResolver );
		this.completor = completor;
		this.validator = validator;
		this.externalOkButtonDisabler = externalOkButtonDisabler;
		Note dropNote = new Note( dropText );
		dropNote.addFeature( new Hole( dropShapeResolver, Feature.ConnectionPreference.NORTH_SOUTH ) );
		this.addNote( dropNote );
		
		this.isPopupMenuNotePresent = popupMenuText != null;
		this.isInputDialogNotePresent = inputDialogText != null;
		if( this.isPopupMenuNotePresent ) {
			this.addNote( new Note( popupMenuText ) );
		}
		if( this.isInputDialogNotePresent ) {
			this.addNote( new Note( inputDialogText ) );
		}
		
		final int N = this.getNoteCount();
		for( int i=0; i<N; i++ ) {
			this.getNoteAt( i ).setLabel( Integer.toString(i+1) );
		}
	}
	
	@Override
	protected java.awt.Point calculateLocationOfFirstNote( edu.cmu.cs.dennisc.croquet.Container< ? > container ) {
		
		Note noteA = this.getNoteAt( 0 );
		Feature featureA = noteA.getFeatures().get( 0 );
		java.awt.Shape shapeA = featureA.getShape(container, null);

		Note noteB = this.getNoteAt( 1 );
		Feature featureB = noteB.getFeatures().get( 0 );
		java.awt.Shape shapeB = featureB.getShape(container, null);

		if( shapeA != null && shapeB != null ) {
			java.awt.geom.Rectangle2D boundsA = shapeA.getBounds2D();
			java.awt.geom.Rectangle2D boundsB = shapeB.getBounds2D();
			
			//java.awt.geom.Line2D vector = new java.awt.geom.Line2D.Double( boundsA.getCenterX(), boundsA.getCenterY(), boundsB.getCenterX(), boundsB.getCenterY() );
			
			double xVectorCenter = ( boundsA.getCenterX() + boundsB.getCenterX() ) * 0.5;
			double yVectorCenter = ( boundsA.getCenterY() + boundsB.getCenterY() ) * 0.5;
			
			double xCenter;
			double yCenter;

			int halfWidth = container.getWidth()/2;
			int halfHeight = container.getHeight()/2;
			if( xVectorCenter < halfWidth ) {
				xCenter = xVectorCenter + 200;
			} else {
				xCenter = xVectorCenter - 200;
			}
			if( yVectorCenter < halfHeight ) {
				yCenter = yVectorCenter + 200;
			} else {
				yCenter = yVectorCenter - 200;
			}
			
			return new java.awt.Point( (int)xCenter, (int)yCenter );
		} else {
			return super.calculateLocationOfFirstNote( container );
		}
	}
	
	@Override
	protected void complete() {
		Note dropNote = this.getNoteAt( 1 );
		java.util.List< Feature > features = dropNote.getFeatures();
		edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape;
		if( features.size() > 0 ) {
			Feature feature0 = features.get( 0 );
			feature0.bind();
			trackableShape = feature0.getTrackableShape();
			feature0.unbind();
		} else {
			trackableShape = null;
		}
		TutorialStencil.complete( this.completor.createEdit( this.getModel(), trackableShape ) );
	}
	@Override
	public void reset() {
		super.reset();
		this.setActiveNote( 0 );
	}
	@Override
	public boolean isAlreadyInTheDesiredState() {
		return false;
	}

	private State getState() {
		int index = this.getIndexOfFirstActiveNote();
		switch( index ) {
		case 0:
			return State.WAITING_ON_DRAG;
		case 1:
			return State.WAITING_ON_DROP;
		case 2:
			if( this.isPopupMenuNotePresent ) {
				return State.WAITING_ON_POPUP_MENU_COMMIT;
			} else {
				return State.WAITING_ON_INPUT_DIALOG_COMMIT;
			}
		case 3:
			return State.WAITING_ON_INPUT_DIALOG_COMMIT;
		default:
			return null;
		}
	}
	@Override
	public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
		State state = this.getState();
		if( child instanceof edu.cmu.cs.dennisc.croquet.CancelEvent ) {
			if( state != State.WAITING_ON_INPUT_DIALOG_COMMIT || child.getParent() instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperationContext) {
				SoundCache.FAILURE.startIfNotAlreadyActive();
				this.reset();
			}
			return false;
		} else {
			boolean rv = false;
			switch( state ) {
			case WAITING_ON_DRAG:
				if( child instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext ) {
					edu.cmu.cs.dennisc.croquet.DragAndDropContext context = (edu.cmu.cs.dennisc.croquet.DragAndDropContext)child;
					Note noteB = this.getNoteAt( 1 );
					Feature featureB = noteB.getFeatures().get( 0 );
					java.awt.Dimension size = context.getDragSource().getDropProxySize();
					featureB.setHeightConstraint( size.height * 2 );
					this.setActiveNote( 1 );
				}
				break;
			case WAITING_ON_DROP:
				if( child instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext.DroppedEvent ) {
					//edu.cmu.cs.dennisc.croquet.ModelContext< ? > parent = child.getParent(); 
					//return parent.getModel() == this.getModel();
					rv = this.getNoteCount() == 2;
					if( rv ) {
						//pass
					} else {
						this.setActiveNote( 2 );
					}
				}
				break;
			case WAITING_ON_POPUP_MENU_COMMIT:
				if( this.isInputDialogNotePresent ) {
					if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent ) {
						this.setActiveNote( 3 );

						edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent windowOpenedEvent = (edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent)child;
						edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> model = windowOpenedEvent.getParent();
						if( model instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<?> ) {
							edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<?> inputDialogOperationContext = (edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<?>)model;
							edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation = inputDialogOperationContext.getModel();
							if( inputDialogOperation != null ) {
								edu.cmu.cs.dennisc.croquet.Dialog dialog = inputDialogOperation.getActiveDialog();
								if( dialog != null ) {
									java.awt.Rectangle dialogLocalBounds = dialog.getLocalBounds();
									Note note3 = this.getNoteAt( 3 );
									java.awt.Rectangle bounds = note3.getBounds( dialog );
									if( bounds.intersects( dialogLocalBounds ) ) {
										note3.setLocation( dialog.getWidth()+100, dialog.getHeight()/2, dialog );
									}
								}
								
								inputDialogOperation.setExternalCommitButtonDisabler( this.externalOkButtonDisabler );
								
							}
						}
					}
				} else {
					if( child instanceof edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent ) {
						edu.cmu.cs.dennisc.croquet.Edit edit;
						if (child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent) {
							edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = (edu.cmu.cs.dennisc.croquet.CommitEvent) child;
							edit = commitEvent.getEdit();
						} else {
							edit = null;
						}
						if( this.validator != null ) {
							edu.cmu.cs.dennisc.tutorial.Validator.Result result = this.validator.checkValidity( this.getModel(), edit );
							rv = result.isProcedeApprorpiate();
						} else {
							rv = true;
						}
					}
				}
				break;
			case WAITING_ON_INPUT_DIALOG_COMMIT:
				if( this.isPopupMenuNotePresent ) {
					//pass
				} else {
					if( this.isInputDialogNotePresent ) {
						if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent ) {
							edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent windowOpenedEvent = (edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowOpenedEvent)child;
							edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> model = windowOpenedEvent.getParent();
							if( model instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<?> ) {
								edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<?> inputDialogOperationContext = (edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<?>)model;
								edu.cmu.cs.dennisc.croquet.InputDialogOperation inputDialogOperation = inputDialogOperationContext.getModel();
								if( inputDialogOperation != null ) {
									inputDialogOperation.setExternalCommitButtonDisabler( this.externalOkButtonDisabler );
								}
							}
						}
					}
				}
				if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosingEvent ) {
					edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosingEvent windowClosingEvent = (edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosingEvent)child;
					edu.cmu.cs.dennisc.croquet.AbstractModelContext<?> c = windowClosingEvent.getParent();
					while( c != null ) {
						if( c.getModel() == this.getModel() ) {
							rv = true;
							break;
						}
						c = c.getParent();
					}
				}
				break;
			}
// todo
//			if( rv ) {
//				inputDialogOperation.setExternalOkButtonDisabler( null );
//			}
			return rv;
		}
	}
}
