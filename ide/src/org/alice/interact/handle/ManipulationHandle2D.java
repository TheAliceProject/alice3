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

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JLabel;

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.event.EventCriteriaManager;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.manipulator.AbstractManipulator;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.math.Vector2;
import edu.cmu.cs.dennisc.scenegraph.Transformable;


/**
 * @author David Culyba
 */
public abstract class ManipulationHandle2D extends JLabel implements ManipulationHandle, ManipulationListener {
	protected Animator animator;
	private EventCriteriaManager criteriaManager = new EventCriteriaManager();
	protected HandleState state = new HandleState();
	protected HandleManager handleManager = null;
	protected HandleSet handleSet = new HandleSet();
	protected AbstractManipulator manipulation = null;
	protected AbstractDragAdapter dragAdapter = null;
	
	public ManipulationHandle2D()
	{
		super();
	}
	
	@Override
	public ManipulationHandle2D clone()
	{
		return null;
	}
	
	public Vector2 getCenter()
	{
		Dimension ourSize = this.getSize();
		return new Vector2(ourSize.width*.5d, ourSize.height*.5d);
	}
	
	@Override
	public void setVisible( boolean flag ) {
		boolean wasVisible = this.isVisible();
		super.setVisible( flag );
		if (wasVisible != flag && this.dragAdapter != null)
		{
			if (this.isVisible())
			{
				this.dragAdapter.addListeners( this );
				this.dragAdapter.addManipulationListener( this );
			}
			else
			{
				this.dragAdapter.removeListeners( this );
				this.dragAdapter.removeManipulationListener( this );
			}
		}
	}
	
	public void setHandleShowing(boolean showing)
	{
		//Do nothing
	}
	
	public void setDragAdapter( AbstractDragAdapter dragAdapter ) {
		this.dragAdapter = dragAdapter;
		if (this.dragAdapter != null)
		{
			this.dragAdapter.addHandle( this );
			this.dragAdapter.addListeners( this );
		}
	}

	public void addToSet( HandleSet set )
	{
		this.handleSet.addSet( set );
	}
	
	public HandleSet getHandleSet()
	{
		return this.handleSet;
	}
	
	public boolean isAlwaysVisible()
	{
		return false;
	}
	
	public void addToGroup( HandleSet.HandleGroup group )
	{
		this.handleSet.addGroup( group );
	}
	
	public void addToGroups( HandleSet.HandleGroup...groups )
	{
		this.handleSet.addGroups( groups );
	}
	
	public boolean isMemberOf( HandleSet set)
	{
		return this.handleSet.intersects( set );
	}
	
	public boolean isPickable()
	{
		return true;
	}
	
	public boolean isMemberOf( HandleSet.HandleGroup group)
	{
		return this.handleSet.get( group.getIndex() );
	}


	public Transformable getManipulatedObject() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setManipulatedObject( Transformable manipulatedObject ) {
		// TODO Auto-generated method stub
		
	}

	public PickHint getPickHint() {
		return PickHint.TWO_D_HANDLES;
	}

	public void setManipulation(AbstractManipulator manipulation)
	{
		this.manipulation = manipulation;
	}
	
	public AbstractManipulator getManipulation( InputState input )
	{
		return this.manipulation;
	}

	public void setHandleManager(HandleManager handleManager)
	{
		this.handleManager = handleManager;
	}
	
	public HandleManager getHandleManager()
	{
		return this.handleManager;
	}

	public HandleState getHandleStateCopy()
	{
		return new HandleState(this.state);
	}
	
	public boolean isRenderable() {
		if (this.isAlwaysVisible())
		{
			return true;
		}
		return this.state.shouldRender();
	}
	
	protected void updateVisibleState( HandleRenderState renderState )
	{
		
	}
	
	public void activate( ManipulationEvent event ) {
		this.setHandleActive( true );
	}

	public void deactivate( ManipulationEvent event ) {
		this.setHandleActive(false);
	}
	

	public void setHandleRollover( boolean rollover ) {
		this.state.setRollover(rollover);
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	public boolean isHandleVisible()
	{
		return this.state.isVisible() || this.isAlwaysVisible();
	}
	
	public void setHandleVisible( boolean visible ) {
		this.state.setVisible(visible);
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}
	
	public void setHandleActive( boolean active ) {
		this.state.setActive(active);
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	public void setSelectedObject( Transformable manipulatedObject ) {
		// TODO Auto-generated method stub
		
	}

	public boolean matches(ManipulationEvent event)
	{
		return this.criteriaManager.matches( event );
	}
	
	public void addCondition(ManipulationEventCriteria condition)
	{
		this.criteriaManager.addCondition( condition );
	}
	
	public void removeCondition(ManipulationEventCriteria condition)
	{
		this.criteriaManager.removeCondition( condition );
	}

	public void setAnimator( Animator animator ) {
		this.animator = animator;
	}
	
	@Override
	public String toString() 
	{
		return this.getClass().getSimpleName()+":"+this.hashCode();
	}
}
