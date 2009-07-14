/**
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
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
		return PickHint.HANDLES;
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
}
