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
	private Completor completor;
	private Validator validator;
	public DragAndDropStep( String title, String text, edu.cmu.cs.dennisc.croquet.Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, edu.cmu.cs.dennisc.croquet.Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > dropShapeResolver, String cascadeText, Completor completor, Validator validator ) {
		super( title, text, new Hole( dragResolver, Feature.ConnectionPreference.EAST_WEST ), dragResolver );
		this.completor = completor;
		this.validator = validator;
		Note dropNote = new Note( dropText );
		dropNote.addFeature( new Hole( dropShapeResolver, Feature.ConnectionPreference.NORTH_SOUTH ) );
		this.addNote( dropNote );
		
		if( cascadeText != null ) {
			Note cascadeNote = new Note( cascadeText );
			this.addNote( cascadeNote );
		}
		
		final int N = this.getNoteCount();
		for( int i=0; i<N; i++ ) {
			this.getNoteAt( i ).setLabel( Integer.toString(i+1) );
		}
	}
	
//	@Override
//	protected edu.cmu.cs.dennisc.croquet.DragAndDropOperation getModel() {
//		edu.cmu.cs.dennisc.croquet.DragComponent dragComponent = this.dragSource.getDragComponent();
//		if( dragComponent != null ) {
//			return dragComponent.getDragAndDropOperation();
//		} else {
//			return null;
//		}
//	}
//	
	@Override
	protected java.awt.Point calculateLocationOfFirstNote( edu.cmu.cs.dennisc.croquet.Container< ? > container ) {
		java.awt.Point p0 = this.calculateLocationForNoteAt( container, 0 );
		java.awt.Point p1 = this.calculateLocationForNoteAt( container, 1 );
		int xCenter = ( p0.x + p1.x )/2;
		int yCenter = ( p0.y + p1.y )/2;
		return new java.awt.Point( xCenter, yCenter );
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
	@Override
	public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryTreeNode<?> child ) {
		if( child instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext ) {
			this.setActiveNote( 1 );
			return false;
		} else if( child instanceof edu.cmu.cs.dennisc.croquet.CancelEvent ) {
			this.reset();
			return false;
		} else if( child instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext.DroppedEvent ) {
			//edu.cmu.cs.dennisc.croquet.ModelContext< ? > parent = child.getParent(); 
			//return parent.getModel() == this.getModel();
			final int N = this.getNoteCount();
			if( N == 3 ) {
				this.setActiveNote( 2 );
				return false;
			} else {
				return true;
			}
		} else if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractCompleteEvent ) {
			final int N = this.getNoteCount();
			if( N == 3 ) {
				if( this.getNoteAt( 2 ).isActive() ) {
					edu.cmu.cs.dennisc.croquet.Edit edit;
					if (child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent) {
						edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = (edu.cmu.cs.dennisc.croquet.CommitEvent) child;
						edit = commitEvent.getEdit();
					} else {
						edit = null;
					}
					if( this.validator != null ) {
						return this.validator.checkValidity( edit ).isProcedeApprorpiate();
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
