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
package org.alice.interact;

import org.alice.interact.event.SelectionEvent;
import org.alice.interact.event.SelectionListener;
import org.alice.interact.handle.HandleManager;
import org.alice.interact.handle.HandleSet;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

/**
 * @author Dennis Cosgrove
 */
public abstract class HandleSupportingDragAdapter extends BareBonesDragAdapter {

	public void addSelectionListener( SelectionListener selectionListener ) {
		this.selectionListeners.add( selectionListener );
	}

	public void removeSelectionListener( SelectionListener selectionListener ) {
		this.selectionListeners.remove( selectionListener );
	}

	public Iterable<SelectionListener> getSelectionListeners() {
		return this.selectionListeners;
	}

	protected void fireSelecting( SelectionEvent e ) {
		for( SelectionListener selectionListener : this.selectionListeners ) {
			selectionListener.selecting( e );
		}
	}

	protected void fireSelected( SelectionEvent e ) {
		for( SelectionListener selectionListener : this.selectionListeners ) {
			selectionListener.selected( e );
		}
	}

	@Override
	public void setAnimator( edu.cmu.cs.dennisc.animation.Animator animator ) {
		super.setAnimator( animator );
		this.handleManager.setAnimator( animator );
	}

	public HandleManager getHandleManager() {
		return this.handleManager;
	}

	public void pushHandleSet( HandleSet handleSet ) {
		this.handleManager.pushNewHandleSet( handleSet );
	}

	public HandleSet popHandleSet() {
		return this.handleManager.popHandleSet();
	}

	public void setToBeSelected( AbstractTransformableImp toBeSelected ) {
		this.toBeSelected = toBeSelected;
		this.hasObjectToBeSelected = true;
	}

	protected abstract void updateHandleSelection( AbstractTransformableImp selected );

	public abstract void setSelectedImplementation( AbstractTransformableImp selected );

	@Override
	protected void handleStateChange() {
		super.handleStateChange();
		if( this.currentInputState.getRolloverHandle() != this.previousInputState.getRolloverHandle() ) {
			if( this.currentInputState.getRolloverHandle() != null ) {
				this.handleManager.setHandleRollover( this.currentInputState.getRolloverHandle(), true );
			}
			if( this.previousInputState.getRolloverHandle() != null ) {
				this.handleManager.setHandleRollover( this.previousInputState.getRolloverHandle(), false );
			}
		}

		if( !this.hasObjectToBeSelected && ( this.currentInputState.getCurrentlySelectedObject() != this.previousInputState.getCurrentlySelectedObject() ) ) {
			this.triggerImplementationSelection( EntityImp.getInstance( this.currentInputState.getCurrentlySelectedObject(), AbstractTransformableImp.class ) );
		}

		this.previousInputState.copyState( this.currentInputState );
	}

	@Override
	protected void fireStateChange() {
		super.fireStateChange();
		if( this.hasObjectToBeSelected ) {
			this.hasObjectToBeSelected = false;
			this.setSelectedImplementation( this.toBeSelected );
		}
	}

	public AbstractTransformableImp getSelectedImplementation() {
		return this.selectedObject;
	}

	public void setSelectedSceneObjectImplementation( AbstractTransformableImp selected ) {
		if( this.selectedObject != selected ) {
			this.fireSelecting( new SelectionEvent( this, selected ) );
			AbstractTransformable sgTransformable = selected != null ? selected.getSgComposite() : null;
			if( HandleManager.isSelectable( sgTransformable ) ) {
				this.handleManager.setHandlesShowing( true );
				this.handleManager.setSelectedObject( sgTransformable );
			} else {
				this.handleManager.setSelectedObject( null );
			}
			this.currentInputState.setCurrentlySelectedObject( sgTransformable );
			this.currentInputState.setTimeCaptured();
			selectedObject = selected;
			this.fireStateChange();
		}
	}

	public void setHandleShowingForSelectedImplementation( AbstractTransformableImp object, boolean handlesShowing ) {
		if( this.selectedObject == object ) {
			this.handleManager.setHandlesShowing( handlesShowing );
		}
	}

	public void setHandleVisibility( boolean isVisible ) {
		if( this.handleManager != null ) {
			this.handleManager.setHandlesShowing( isVisible );
		}
	}

	public void triggerImplementationSelection( AbstractTransformableImp selected ) {
		if( this.selectedObject != selected ) {
			this.fireSelected( new SelectionEvent( this, selected ) );
		}
	}

	public void triggerSgObjectSelection( AbstractTransformable selected ) {
		triggerImplementationSelection( EntityImp.getInstance( selected, AbstractTransformableImp.class ) );
	}

	private final java.util.List<SelectionListener> selectionListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	protected final HandleManager handleManager = new HandleManager();
	private AbstractTransformableImp toBeSelected = null;
	private boolean hasObjectToBeSelected = false;
	private AbstractTransformableImp selectedObject = null;
}
