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
package org.alice.interact.handle;

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.event.EventCriteriaManager;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.manipulator.AbstractManipulator;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

//import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public abstract class ManipulationHandle2D extends javax.swing.JLabel implements ManipulationHandle, ManipulationListener {
	public ManipulationHandle2D() {
		super();
	}

	@Override
	public void setVisible( boolean flag ) {
		boolean wasVisible = this.isVisible();
		super.setVisible( flag );
		if( ( wasVisible != flag ) && ( this.dragAdapter != null ) ) {
			if( this.isVisible() ) {
				this.dragAdapter.addListeners( this );
				this.dragAdapter.addManipulationListener( this );
			} else {
				this.dragAdapter.removeListeners( this );
				this.dragAdapter.removeManipulationListener( this );
			}
		}
	}

	@Override
	public void clear() {
		this.setManipulatedObject( null );
	}

	@Override
	public void setVisualsShowing( boolean showing ) {
		//Do nothing
	}

	@Override
	public void setDragAdapter( AbstractDragAdapter dragAdapter ) {
		this.dragAdapter = dragAdapter;
	}

	@Override
	public void setDragAdapterAndAddHandle( AbstractDragAdapter dragAdapter ) {
		this.setDragAdapter( dragAdapter );
		if( this.dragAdapter != null ) {
			this.dragAdapter.addHandle( this );
			this.dragAdapter.addListeners( this );
		}
	}

	@Override
	public void addToSet( HandleSet set ) {
		this.handleSet.addSet( set );
	}

	@Override
	public HandleSet getHandleSet() {
		return this.handleSet;
	}

	@Override
	public boolean isAlwaysVisible() {
		return true;
	}

	@Override
	public void addToGroup( HandleSet.HandleGroup group ) {
		this.handleSet.addGroup( group );
	}

	@Override
	public void addToGroups( HandleSet.HandleGroup... groups ) {
		this.handleSet.addGroups( groups );
	}

	@Override
	public boolean isMemberOf( HandleSet set ) {
		return this.handleSet.intersects( set ) || set.intersects( this.handleSet );
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean isMemberOf( HandleSet.HandleGroup group ) {
		return this.handleSet.get( group.ordinal() );
	}

	@Override
	public AbstractTransformable getManipulatedObject() {
		return null;
	}

	public void setManipulatedObject( AbstractTransformable manipulatedObject ) {
	}

	@Override
	public PickHint getPickHint() {
		return PickHint.PickType.TWO_D_HANDLE.pickHint();
	}

	@Override
	public void setManipulation( AbstractManipulator manipulation ) {
		this.manipulation = manipulation;
	}

	@Override
	public AbstractManipulator getManipulation( InputState input ) {
		return this.manipulation;
	}

	@Override
	public void setHandleManager( HandleManager handleManager ) {
		this.handleManager = handleManager;
	}

	@Override
	public HandleManager getHandleManager() {
		return this.handleManager;
	}

	@Override
	public HandleState getHandleStateCopy() {
		return new HandleState( this.state );
	}

	@Override
	public boolean isRenderable() {
		if( this.isAlwaysVisible() ) {
			return true;
		} else {
			return this.state.shouldRender();
		}
	}

	protected void updateVisibleState( HandleRenderState renderState ) {
	}

	@Override
	public void activate( ManipulationEvent event ) {
		this.setHandleActive( true );
	}

	@Override
	public void deactivate( ManipulationEvent event ) {
		this.setHandleActive( false );
	}

	@Override
	public void setHandleRollover( boolean rollover ) {
		this.state.setRollover( rollover );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	@Override
	public boolean isHandleVisible() {
		return this.state.isVisible() || this.isAlwaysVisible();
	}

	@Override
	public void setHandleVisible( boolean visible ) {
		this.state.setVisible( visible );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	@Override
	public void setHandleActive( boolean active ) {
		this.state.setActive( active );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	@Override
	public void setSelectedObject( AbstractTransformable manipulatedObject ) {
	}

	@Override
	public boolean matches( ManipulationEvent event ) {
		return this.criteriaManager.matches( event );
	}

	@Override
	public void addCondition( ManipulationEventCriteria condition ) {
		this.criteriaManager.addCondition( condition );
	}

	@Override
	public void removeCondition( ManipulationEventCriteria condition ) {
		this.criteriaManager.removeCondition( condition );
	}

	@Override
	public void setCameraPosition( Point3 cameraPosition ) {
		//Do Nothing
	}

	private final EventCriteriaManager criteriaManager = new EventCriteriaManager();
	protected final HandleState state = new HandleState();
	private HandleManager handleManager = null;
	private final HandleSet handleSet = new HandleSet();
	private AbstractManipulator manipulation = null;
	private AbstractDragAdapter dragAdapter = null;
}
