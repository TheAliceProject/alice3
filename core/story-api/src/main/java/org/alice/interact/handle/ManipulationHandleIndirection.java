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
public class ManipulationHandleIndirection implements ManipulationListener, ManipulationHandle {
	public ManipulationHandleIndirection( ManipulationHandle3D handle ) {
		this.currentHandle = handle;
		this.nextHandle = this.currentHandle.clone();
	}

	@Override
	public ManipulationHandleIndirection clone() {
		ManipulationHandle3D newCurrent = this.currentHandle.clone();
		ManipulationHandleIndirection newHandle = new ManipulationHandleIndirection( newCurrent );
		newHandle.handleManager = this.handleManager;
		return newHandle;
	}

	@Override
	public void setDragAdapter( AbstractDragAdapter dragAdapter ) {
		this.dragAdapter = dragAdapter;
		if( this.currentHandle != null ) {
			this.currentHandle.setDragAdapter( dragAdapter );
		}
		if( this.nextHandle != null ) {
			this.nextHandle.setDragAdapter( dragAdapter );
		}
	}

	@Override
	public void setDragAdapterAndAddHandle( AbstractDragAdapter dragAdapter ) {
		this.setDragAdapter( dragAdapter );
		if( this.dragAdapter != null ) {
			this.dragAdapter.addHandle( this );
		}
	}

	@Override
	public void setSelectedObject( AbstractTransformable manipulatedObject ) {
		if( this.currentHandle.getManipulatedObject() != manipulatedObject ) {
			//			if (this.isHandleVisible())
			//				PrintUtilities.println("setting manipulated object to "+manipulatedObject+" on "+this);
			//			PrintUtilities.println("Current selected = "+this.currentHandle.getManipulatedObject());
			//			PrintUtilities.println("Next selected = "+this.nextHandle.getManipulatedObject());
			HandleState currentHandleState = this.currentHandle.getHandleStateCopy();
			this.currentHandle.setHandleVisible( false );
			this.currentHandle.setHandleActive( false );
			this.currentHandle.setHandleRollover( false );
			this.currentHandle.setSelectedObject( null );
			this.nextHandle.setSelectedObject( manipulatedObject );

			//If the handle was previously part of the active group (i.e. the state was not GROUP_NOT_VISIBLE)
			//then make the next handle marked as part of the active group.
			//Do not copy more state than this in case there is rollover or other active stateness lingering
			this.nextHandle.setHandleVisible( currentHandleState.isVisible() );
			ManipulationHandle3D tempHandle = this.currentHandle;
			this.currentHandle = this.nextHandle;
			this.nextHandle = tempHandle;
			this.criteriaManager.setTargetTransformable( manipulatedObject );
		}
		//		else
		//		{
		//			PrintUtilities.println("Not setting manipulated object because "+this.currentHandle.getManipulatedObject()+" == "+manipulatedObject);
		//		}
	}

	public ManipulationHandle getCurrentHandle() {
		return this.currentHandle;
	}

	public ManipulationHandle getNextHandle() {
		return this.nextHandle;
	}

	@Override
	public void activate( ManipulationEvent event ) {
		if( this.currentHandle instanceof ManipulationListener ) {
			( (ManipulationListener)this.currentHandle ).activate( event );
		}
	}

	@Override
	public void deactivate( ManipulationEvent event ) {
		if( this.currentHandle instanceof ManipulationListener ) {
			( (ManipulationListener)this.currentHandle ).deactivate( event );
		}
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
	public void addToGroups( HandleSet.HandleGroup... groups ) {
		this.currentHandle.addToGroups( groups );
		this.nextHandle.addToGroups( groups );
	}

	@Override
	public void addToGroup( HandleSet.HandleGroup group ) {
		this.currentHandle.addToGroup( group );
		this.nextHandle.addToGroup( group );
	}

	@Override
	public void addToSet( HandleSet handleSet ) {
		this.currentHandle.addToSet( handleSet );
		this.nextHandle.addToSet( handleSet );
	}

	@Override
	public HandleManager getHandleManager() {
		return this.handleManager;
	}

	@Override
	public HandleState getHandleStateCopy() {
		return this.currentHandle.getHandleStateCopy();
	}

	@Override
	public AbstractTransformable getManipulatedObject() {
		return this.currentHandle.getManipulatedObject();
	}

	@Override
	public HandleSet getHandleSet() {
		return this.currentHandle.getHandleSet();
	}

	@Override
	public boolean isPickable() {
		return this.currentHandle.isPickable();
	}

	@Override
	public boolean isAlwaysVisible() {
		return this.currentHandle.isAlwaysVisible();
	}

	@Override
	public boolean isMemberOf( HandleSet.HandleGroup group ) {
		return this.currentHandle.isMemberOf( group );
	}

	@Override
	public boolean isMemberOf( HandleSet set ) {
		return this.currentHandle.isMemberOf( set );
	}

	@Override
	public AbstractManipulator getManipulation( InputState input ) {
		return this.currentHandle.getManipulation( input );
	}

	@Override
	public void setManipulation( AbstractManipulator manipulation ) {
		this.currentHandle.setManipulation( manipulation );
		this.nextHandle.setManipulation( manipulation );
	}

	@Override
	public boolean isRenderable() {
		return this.currentHandle.isRenderable();
	}

	@Override
	public void setHandleActive( boolean active ) {
		this.currentHandle.setHandleActive( active );
	}

	@Override
	public void setHandleManager( HandleManager handleManager ) {
		this.handleManager = handleManager;
		this.currentHandle.setHandleManager( handleManager );
		this.nextHandle.setHandleManager( handleManager );
	}

	@Override
	public void setHandleRollover( boolean rollover ) {
		this.currentHandle.setHandleRollover( rollover );
	}

	@Override
	public boolean isHandleVisible() {
		return this.currentHandle.isHandleVisible();
	}

	@Override
	public void setHandleVisible( boolean visible ) {
		this.currentHandle.setHandleVisible( visible );
	}

	@Override
	public void setVisualsShowing( boolean showing ) {
		this.currentHandle.setVisualsShowing( showing );
	}

	@Override
	public PickHint getPickHint() {
		return this.currentHandle.getPickHint();
	}

	@Override
	public void setCameraPosition( Point3 cameraPosition ) {
		if( this.currentHandle != null ) {
			this.currentHandle.setCameraPosition( cameraPosition );
		}
		if( this.nextHandle != null ) {
			this.nextHandle.setCameraPosition( cameraPosition );
		}
	}

	@Override
	public void clear() {
		if( this.currentHandle != null ) {
			this.currentHandle.clear();
		}
		if( this.nextHandle != null ) {
			this.nextHandle.clear();
		}
	}

	@Override
	public String toString() {
		String returnString;
		if( this.currentHandle != null ) {
			returnString = "Current: " + this.currentHandle.getClass().getSimpleName() + ":" + this.currentHandle.hashCode() + "; ";
		} else {
			returnString = "Current: null; ";
		}
		if( this.nextHandle != null ) {
			returnString += "Next: " + this.nextHandle.getClass().getSimpleName() + ":" + this.nextHandle.hashCode();
		} else {
			returnString += "Next: null";
		}
		return this.getClass().getSimpleName() + "[" + returnString + "]";
	}

	private ManipulationHandle3D currentHandle;
	private ManipulationHandle3D nextHandle;

	private EventCriteriaManager criteriaManager = new EventCriteriaManager();
	private HandleManager handleManager = null;
	private AbstractDragAdapter dragAdapter = null;
}
