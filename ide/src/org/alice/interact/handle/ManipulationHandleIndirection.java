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

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

//import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class ManipulationHandleIndirection implements ManipulationListener, ManipulationHandle {
	private ManipulationHandle currentHandle;
	private ManipulationHandle nextHandle;

	private EventCriteriaManager criteriaManager = new EventCriteriaManager();
	private HandleManager handleManager = null;
	protected AbstractDragAdapter dragAdapter = null;

	public ManipulationHandleIndirection( ManipulationHandle handle )
	{
		this.currentHandle = handle;
		this.nextHandle = this.currentHandle.clone();
	}

	public void setAnimator( Animator animator )
	{
		this.currentHandle.setAnimator( animator );
		this.nextHandle.setAnimator( animator );
	}

	@Override
	public ManipulationHandleIndirection clone()
	{
		ManipulationHandle newCurrent = this.currentHandle.clone();
		ManipulationHandleIndirection newHandle = new ManipulationHandleIndirection( newCurrent );
		newHandle.handleManager = this.handleManager;
		return newHandle;
	}

	public void setDragAdapter( AbstractDragAdapter dragAdapter )
	{
		this.dragAdapter = dragAdapter;
		if( this.currentHandle != null )
		{
			this.currentHandle.setDragAdapter( dragAdapter );
		}
		if( this.nextHandle != null )
		{
			this.nextHandle.setDragAdapter( dragAdapter );
		}
	}

	public void setDragAdapterAndAddHandle( AbstractDragAdapter dragAdapter )
	{
		this.setDragAdapter( dragAdapter );
		if( this.dragAdapter != null )
		{
			this.dragAdapter.addHandle( this );
		}
	}

	public void setSelectedObject( AbstractTransformable manipulatedObject )
	{
		if( this.currentHandle.getManipulatedObject() != manipulatedObject )
		{
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
			ManipulationHandle tempHandle = this.currentHandle;
			this.currentHandle = this.nextHandle;
			this.nextHandle = tempHandle;
			this.criteriaManager.setTargetTransformable( manipulatedObject );
		}
		//		else
		//		{
		//			PrintUtilities.println("Not setting manipulated object because "+this.currentHandle.getManipulatedObject()+" == "+manipulatedObject);
		//		}
	}

	public ManipulationHandle getCurrentHandle()
	{
		return this.currentHandle;
	}

	public ManipulationHandle getNextHandle()
	{
		return this.nextHandle;
	}

	public void activate( ManipulationEvent event )
	{
		if( this.currentHandle instanceof ManipulationListener )
		{
			( (ManipulationListener)this.currentHandle ).activate( event );
		}
	}

	public void deactivate( ManipulationEvent event )
	{
		if( this.currentHandle instanceof ManipulationListener )
		{
			( (ManipulationListener)this.currentHandle ).deactivate( event );
		}
	}

	public boolean matches( ManipulationEvent event )
	{
		return this.criteriaManager.matches( event );
	}

	public void addCondition( ManipulationEventCriteria condition )
	{
		this.criteriaManager.addCondition( condition );
	}

	public void removeCondition( ManipulationEventCriteria condition )
	{
		this.criteriaManager.removeCondition( condition );
	}

	public void addToGroups( HandleSet.HandleGroup... groups )
	{
		this.currentHandle.addToGroups( groups );
		this.nextHandle.addToGroups( groups );
	}

	public void addToGroup( HandleSet.HandleGroup group ) {
		this.currentHandle.addToGroup( group );
		this.nextHandle.addToGroup( group );
	}

	public void addToSet( HandleSet handleSet ) {
		this.currentHandle.addToSet( handleSet );
		this.nextHandle.addToSet( handleSet );

	}

	public HandleManager getHandleManager() {
		return this.handleManager;
	}

	public HandleState getHandleStateCopy() {
		return this.currentHandle.getHandleStateCopy();
	}

	public AbstractTransformable getManipulatedObject() {
		return this.currentHandle.getManipulatedObject();
	}

	public HandleSet getHandleSet() {
		return this.currentHandle.getHandleSet();
	}

	public boolean isPickable()
	{
		return this.currentHandle.isPickable();
	}

	public boolean isAlwaysVisible() {
		return this.currentHandle.isAlwaysVisible();
	}

	public boolean isMemberOf( HandleSet.HandleGroup group ) {
		return this.currentHandle.isMemberOf( group );
	}

	public boolean isMemberOf( HandleSet set )
	{
		return this.currentHandle.isMemberOf( set );
	}

	public AbstractManipulator getManipulation( InputState input )
	{
		return this.currentHandle.getManipulation( input );
	}

	public void setManipulation( AbstractManipulator manipulation )
	{
		this.currentHandle.setManipulation( manipulation );
		this.nextHandle.setManipulation( manipulation );
	}

	public boolean isRenderable() {
		return this.currentHandle.isRenderable();
	}

	public void setHandleActive( boolean active ) {
		this.currentHandle.setHandleActive( active );
	}

	public void setHandleManager( HandleManager handleManager ) {
		this.handleManager = handleManager;
		this.currentHandle.setHandleManager( handleManager );
		this.nextHandle.setHandleManager( handleManager );

	}

	public void setHandleRollover( boolean rollover ) {
		this.currentHandle.setHandleRollover( rollover );
	}

	public boolean isHandleVisible()
	{
		return this.currentHandle.isHandleVisible();
	}

	public void setHandleVisible( boolean visible )
	{
		this.currentHandle.setHandleVisible( visible );
	}

	public void setVisualsShowing( boolean showing )
	{
		this.currentHandle.setVisualsShowing( showing );
	}

	public PickHint getPickHint() {
		return this.currentHandle.getPickHint();
	}

	public void setCameraPosition( Point3 cameraPosition )
	{
		if( this.currentHandle != null )
		{
			this.currentHandle.setCameraPosition( cameraPosition );
		}
		if( this.nextHandle != null )
		{
			this.nextHandle.setCameraPosition( cameraPosition );
		}
	}

	@Override
	public String toString()
	{
		String returnString = "";
		if( this.currentHandle != null )
		{
			returnString = "Current: " + this.currentHandle.getClass().getSimpleName() + ":" + this.currentHandle.hashCode() + "; ";
		}
		else
		{
			returnString = "Current: null; ";
		}
		if( this.nextHandle != null )
		{
			returnString += "Next: " + this.nextHandle.getClass().getSimpleName() + ":" + this.nextHandle.hashCode();
		}
		else
		{
			returnString += "Next: null";
		}
		return returnString;
	}

}
