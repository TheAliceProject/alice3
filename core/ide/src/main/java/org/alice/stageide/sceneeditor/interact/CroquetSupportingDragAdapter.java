/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.stageide.sceneeditor.interact;

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.InteractionGroup;
import org.alice.interact.InteractionGroup.InteractionInfo;
import org.alice.interact.PickHint;
import org.alice.interact.PickUtilities;
import org.lgna.croquet.SingleSelectListState;
import org.lgna.story.implementation.AbstractTransformableImp;

public abstract class CroquetSupportingDragAdapter extends AbstractDragAdapter {
	private java.awt.Point getDragAndDropPoint( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
		java.awt.Point pointInLookingGlass = javax.swing.SwingUtilities.convertPoint( eSource.getComponent(), eSource.getPoint(), this.getAWTComponent() );
		return pointInLookingGlass;
	}

	public void dragUpdated( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		this.currentInputState.setDragAndDropContext( dragAndDropContext );
		this.currentInputState.setIsDragEvent( true );
		this.currentInputState.setMouseLocation( getDragAndDropPoint( dragAndDropContext ) );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( dragAndDropContext.getLatestMouseEvent() );
		this.fireStateChange();
	}

	public void dragEntered( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		this.currentInputState.setDragAndDropContext( dragAndDropContext );
		this.currentInputState.setIsDragEvent( true );
		this.currentInputState.setMouseLocation( getDragAndDropPoint( dragAndDropContext ) );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( dragAndDropContext.getLatestMouseEvent() );
		this.fireStateChange();
	}

	public void dragExited( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		this.currentInputState.setDragAndDropContext( dragAndDropContext ); //We need a valid dragAndDropContext when we handle the update
		this.currentInputState.setIsDragEvent( false );
		this.currentInputState.setMouseLocation( getDragAndDropPoint( dragAndDropContext ) );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( dragAndDropContext.getLatestMouseEvent() );
		this.fireStateChange();
		this.currentInputState.setDragAndDropContext( null );
	}

	protected abstract SingleSelectListState<org.alice.interact.handle.HandleStyle, ?> getHandleStyleState();

	@Override
	protected void setHandleSelectionState( org.alice.interact.handle.HandleStyle handleStyle ) {
		SingleSelectListState<org.alice.interact.handle.HandleStyle, ?> handleStyleListSelectionState = this.getHandleStyleState();
		if( handleStyleListSelectionState != null ) {
			handleStyleListSelectionState.setValueTransactionlessly( handleStyle );
		}
	}

	@Override
	protected void updateHandleSelection( AbstractTransformableImp selected ) {
		SingleSelectListState<org.alice.interact.handle.HandleStyle, ?> handleStyleListSelectionState = this.getHandleStyleState();
		if( handleStyleListSelectionState != null ) {
			org.alice.interact.handle.HandleStyle currentHandleStyle = handleStyleListSelectionState.getValue();
			InteractionGroup selectedStateGroup = this.mapHandleStyleToInteractionGroup.get( currentHandleStyle );
			InteractionInfo selectedState = selectedStateGroup.getMatchingInfo( ObjectType.getObjectType( selected ) );
			if( selectedState != null ) { //Sometimes we don't support handles--like in the create-a-sim editor
				PickHint pickHint = PickUtilities.getPickTypeForImp( selected );
				if( !selectedState.canUseIteractionGroup( pickHint ) ) {
					for( org.alice.interact.handle.HandleStyle handleStyle : handleStyleListSelectionState ) {
						InteractionGroup interactionStateGroup = this.mapHandleStyleToInteractionGroup.get( handleStyle );
						InteractionInfo interactionState = interactionStateGroup.getMatchingInfo( ObjectType.getObjectType( selected ) );
						if( interactionState.canUseIteractionGroup( pickHint ) ) {
							handleStyleListSelectionState.setValueTransactionlessly( handleStyle );
							break;
						}
					}
				}
			}
		}
	}
}
