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
/*package-private*/ class DragAndDropStep extends WaitingStep<edu.cmu.cs.dennisc.croquet.DragAndDropOperation> {
	private enum State {
		WAITING_ON_DRAG,
		WAITING_ON_DROP,
		WAITING_ON_POPUP_MENU_COMMIT,
		WAITING_ON_INPUT_DIALOG_COMMIT,
		COMPLETE
	}
	
	private boolean isPopupMenuNotePresent;
	private boolean isInputDialogNotePresent;
	private Completor completor;
	private Validator validator;
	public DragAndDropStep( String title, String text, edu.cmu.cs.dennisc.croquet.Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, edu.cmu.cs.dennisc.croquet.Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > dropShapeResolver, String popupMenuText, String inputDialogText, Completor completor, Validator validator ) {
		super( title, text, new Hole( dragResolver, Feature.ConnectionPreference.EAST_WEST ), dragResolver );
		this.completor = completor;
		this.validator = validator;
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
	protected void complete( edu.cmu.cs.dennisc.croquet.ModelContext< ? > context ) {
		context.commitAndInvokeDo( this.completor.getEdit() );
	}
	private void setActiveNote( int activeIndex ) {
		final int N = this.getNoteCount();
		for( int i=0; i<N; i++ ) {
			this.getNoteAt( i ).setActive( i==activeIndex );
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

	private int getIndexOfFirstActiveNote() { 
		final int N = this.getNoteCount();
		for( int i=0; i<N; i++ ) {
			Note note = this.getNoteAt( i );
			if( note.isActive() ) {
				return i;
			}
		}
		return -1;
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
	public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryTreeNode<?> child ) {
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
					}
				} else {
					if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent ) {
						edu.cmu.cs.dennisc.croquet.Edit edit;
						if (child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent) {
							edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = (edu.cmu.cs.dennisc.croquet.CommitEvent) child;
							edit = commitEvent.getEdit();
						} else {
							edit = null;
						}
						if( this.validator != null ) {
							rv = this.validator.checkValidity( edit ).isProcedeApprorpiate();
						} else {
							rv = true;
						}
					}
				}
				break;
			case WAITING_ON_INPUT_DIALOG_COMMIT:
				if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosedEvent ) {
					edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosedEvent windowClosedEvent = (edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext.WindowClosedEvent)child;
					edu.cmu.cs.dennisc.croquet.ModelContext<?> c = windowClosedEvent.getParent();
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
			return rv;
		}
//		if( child instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext ) {
//			edu.cmu.cs.dennisc.croquet.DragAndDropContext context = (edu.cmu.cs.dennisc.croquet.DragAndDropContext)child;
//			Note noteB = this.getNoteAt( 1 );
//			Feature featureB = noteB.getFeatures().get( 0 );
//			java.awt.Dimension size = context.getDragSource().getDropProxySize();
//			featureB.setHeightConstraint( size.height * 2 );
//			this.setActiveNote( 1 );
//			return false;
//		} else if( child instanceof edu.cmu.cs.dennisc.croquet.CancelEvent ) {
//			SoundCache.FAILURE.startIfNotAlreadyActive();
//			this.reset();
//			return false;
//		} else if( child instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext.DroppedEvent ) {
//			//edu.cmu.cs.dennisc.croquet.ModelContext< ? > parent = child.getParent(); 
//			//return parent.getModel() == this.getModel();
//			final int N = this.getNoteCount();
//			if( N == 3 ) {
//				this.setActiveNote( 2 );
//				return false;
//			} else {
//				return true;
//			}
//		} else if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent ) {
//			final int N = this.getNoteCount();
//			if( N == 3 ) {
//				if( this.getNoteAt( 2 ).isActive() ) {
//					edu.cmu.cs.dennisc.croquet.Edit edit;
//					if (child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent) {
//						edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = (edu.cmu.cs.dennisc.croquet.CommitEvent) child;
//						edit = commitEvent.getEdit();
//					} else {
//						edit = null;
//					}
//					if( this.validator != null ) {
//						return this.validator.checkValidity( edit ).isProcedeApprorpiate();
//					} else {
//						return true;
//					}
//				} else {
//					return false;
//				}
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
	}
}
