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

package org.alice.interact;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventManager;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.event.SelectionEvent;
import org.alice.interact.event.SelectionListener;
import org.alice.interact.handle.HandleManager;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.ManipulationHandle;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.CameraInformedManipulator;


import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.lookingglass.PickResult;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public abstract class AbstractDragAdapter implements java.awt.event.MouseWheelListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener, java.awt.event.KeyListener {
	
	public static final String BOUNDING_BOX_KEY = "BOUNDING_BOX_KEY";
	
	protected java.util.Vector< ManipulatorConditionSet > manipulators = new java.util.Vector< ManipulatorConditionSet >();

	protected edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
	protected edu.cmu.cs.dennisc.animation.Animator animator;
	private java.awt.Component lookingGlassComponent = null;
	
	protected InputState currentInputState = new InputState();
	protected InputState previousInputState = new InputState(); 
	private double timePrev = Double.NaN;
	private boolean hasSetCameraTransformables = false;
	
	private Component currentRolloverComponent = null;
	
	protected java.util.Map<Integer, HandleSet> keyToHandleSetMap = new java.util.HashMap<Integer, HandleSet>();
	
	protected HandleManager handleManager = new HandleManager();
	protected ManipulationEventManager manipulationEventManager = new ManipulationEventManager();
	
	private Transformable selectedObject = null;
	
	
	private List< SelectionListener > selectionListeners = new java.util.LinkedList< SelectionListener >(); 
	public void addPropertyListener( SelectionListener selectionListener ) {
		synchronized( this.selectionListeners ) {
			this.selectionListeners.add( selectionListener );
		}
	}
	public void removeSelectionListener( SelectionListener selectionListener ) {
		synchronized( this.selectionListeners ) {
			this.selectionListeners.remove( selectionListener );
		}
	}
	public Iterable< SelectionListener > getSelectionListeners() {
		return this.selectionListeners;
	}

	private void fireSelecting( SelectionEvent e ) {
		synchronized( this.selectionListeners ) {
			for( SelectionListener selectionListener : this.selectionListeners ) {
				selectionListener.selecting( e );
			}
		}
	}
	private void fireSelected( SelectionEvent e ) {
		synchronized( this.selectionListeners ) {
			for( SelectionListener selectionListener : this.selectionListeners ) {
				selectionListener.selected( e );
			}
		}
	}
	
	public AbstractDragAdapter()
	{
		this.setUpControls();
	}	
	
	protected abstract void setUpControls();
	
	public void triggerManipulationEvent( ManipulationEvent event, boolean isActivate )
	{
		event.setInputState( this.currentInputState );
		this.manipulationEventManager.triggerEvent( event, isActivate );
	}
	
	protected java.awt.Component getAWTComponentToAddListenersTo( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		return this.onscreenLookingGlass.getAWTComponent();
	}
	
	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return this.onscreenLookingGlass;
	}
	
	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
		setAWTComponent( getAWTComponentToAddListenersTo( onscreenLookingGlass ) );
		onscreenLookingGlass.getLookingGlassFactory().addAutomaticDisplayListener( new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
			public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
				AbstractDragAdapter.this.handleDisplayed();
			}
		} );
	}
	
	protected java.awt.Component getAWTComponent() {
		return this.lookingGlassComponent;
	}
	public void setAWTComponent( java.awt.Component awtComponent ) {
		if( this.lookingGlassComponent != null ) {
			removeListeners( this.lookingGlassComponent  );
		}
		this.lookingGlassComponent  = awtComponent;
		if( this.lookingGlassComponent  != null ) {
			addListeners( awtComponent );
		}
	}
	
	public void setAnimator( Animator animator )
	{
		this.animator = animator;
		this.handleManager.setAnimator(animator);
	}
	
	public void setSelectedObject(Transformable selected)
	{
		if (this.selectedObject != selected)
		{
			this.fireSelecting( new SelectionEvent(this, selected) );
			this.handleManager.setSelectedObject( selected );
			this.currentInputState.setCurrentlySelectedObject( selected ); 
			selectedObject = selected;
			this.fireSelected( new SelectionEvent(this, selected) );
			this.handleStateChange();
		}
	}
	
	public void setHandleSet( HandleSet handleSet )
	{
		this.handleManager.setHandleSet( handleSet );
	}
	
	public void pushHandleSet( HandleSet handleSet )
	{
		this.handleManager.pushNewHandleSet( handleSet );
	}
	
	public HandleSet popHandleSet()
	{
		return this.handleManager.popHandleSet();
	}
	
	protected void handleStateChange()
	{
		java.util.Vector< AbstractManipulator > toStart = new java.util.Vector< AbstractManipulator >();
		java.util.Vector< AbstractManipulator > toEnd = new java.util.Vector< AbstractManipulator >();
		java.util.Vector< AbstractManipulator > toUpdate = new java.util.Vector< AbstractManipulator >();
		for (int i=0; i<this.manipulators.size(); i++)
		{
			ManipulatorConditionSet currentManipulatorSet = this.manipulators.get( i );
			if (currentManipulatorSet.stateChanged( this.currentInputState, this.previousInputState))
			{
				if ( currentManipulatorSet.shouldContinue( this.currentInputState, this.previousInputState ))
				{
					toUpdate.add( currentManipulatorSet.getManipulator() );
				}
				else if ( currentManipulatorSet.justStarted( this.currentInputState, this.previousInputState ) )
				{
					toStart.add( currentManipulatorSet.getManipulator() );
				}
				else if ( currentManipulatorSet.justEnded( this.currentInputState, this.previousInputState ) )
				{
					toEnd.add( currentManipulatorSet.getManipulator() );
				}
			}
		}
		//End manipulators first
		for (int i=0; i<toEnd.size(); i++)
		{
			toEnd.get( i ).endManipulator( this.currentInputState, this.previousInputState );
		}
		for (int i=0; i<toStart.size(); i++)
		{
			toStart.get( i ).startManipulator( this.currentInputState );
		}
		for (int i=0; i<toUpdate.size(); i++)
		{
			//If the manipulator we're updating was just started, don't update it with previous data (it's out of scope for the manipulator)
			if (toStart.contains( toUpdate.get( i ) ))
			{
				toUpdate.get( i ).dataUpdateManipulator( this.currentInputState, this.currentInputState );
			}
			else
			{
				toUpdate.get( i ).dataUpdateManipulator( this.currentInputState, this.previousInputState );
			}
		}
		
		
		if (this.currentInputState.getRolloverHandle() != this.previousInputState.getRolloverHandle())
		{
			if (this.currentInputState.getRolloverHandle() != null){
				this.handleManager.setHandleRollover( this.currentInputState.getRolloverHandle(), true );
			}
			if (this.previousInputState.getRolloverHandle() != null){
				this.handleManager.setHandleRollover( this.previousInputState.getRolloverHandle(), false );
			}
		}
		
		if (this.currentInputState.getCurrentlySelectedObject() != this.previousInputState.getCurrentlySelectedObject())
		{
			this.setSelectedObject( this.currentInputState.getCurrentlySelectedObject() );
		}
		
		this.previousInputState.copyState(this.currentInputState);
	}
	
	protected void update( double timeDelta )
	{
		for (int i=0; i<this.manipulators.size(); i++)
		{
			ManipulatorConditionSet currentManipulatorSet = this.manipulators.get( i );
			if ( currentManipulatorSet.getManipulator().hasStarted() && currentManipulatorSet.shouldContinue( this.currentInputState, this.previousInputState ))
			{
				currentManipulatorSet.getManipulator().timeUpdateManipulator( timeDelta, this.currentInputState );
			}
		}
	}

	public void setSGCamera( AbstractCamera camera )
	{
		for (int i=0; i<this.manipulators.size(); i++)
		{
			if (this.manipulators.get( i ).getManipulator() instanceof CameraInformedManipulator)
			{
				((CameraInformedManipulator)this.manipulators.get( i ).getManipulator()).setOnscreenLookingGlass( getOnscreenLookingGlass() );
			}
		}
	}
	
	protected void handleDisplayed() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			if (!hasSetCameraTransformables)
			{
				setSGCamera(sgCamera);
				hasSetCameraTransformables = true;
			}
			double timeCurr = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
			if( sgCamera != null ) {
				if( Double.isNaN( this.timePrev ) ) {
					this.timePrev = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
				}
				double timeDelta = timeCurr - this.timePrev;
				update( timeDelta );
			}
			this.timePrev = timeCurr;
		}
	}
	
	private int cameraIndex = 0;
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
		if( onscreenLookingGlass != null ) {
			if( this.cameraIndex < onscreenLookingGlass.getCameraCount() ) {
				return onscreenLookingGlass.getCameraAt( this.cameraIndex );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.scenegraph.Transformable getSGCameraTransformable() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			return (edu.cmu.cs.dennisc.scenegraph.Transformable)sgCamera.getParent();
		} else {
			return null;
		}
	}

	protected boolean isComponentListener(java.awt.Component component)
	{
		for (MouseListener listener : component.getMouseListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		for (MouseMotionListener listener : component.getMouseMotionListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		for (KeyListener listener : component.getKeyListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		for (MouseWheelListener listener : component.getMouseWheelListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		return false;
	}
	
	public void addListeners( java.awt.Component component ) {
		if (!this.isComponentListener(component))
		{
			component.addMouseListener( this );
			component.addMouseMotionListener( this );
			component.addKeyListener( this );
			component.addMouseWheelListener( this );
		}
	}

	public void removeListeners( java.awt.Component component ) {
		if (this.isComponentListener(component))
		{
			component.removeMouseListener( this );
			component.removeMouseMotionListener( this );
			component.removeKeyListener( this );
			component.removeMouseWheelListener( this );
		}
	}
	
	public void addManipulationListener( ManipulationListener listener )
	{
		this.manipulationEventManager.addManipulationListener( listener );
	}
	
	public void removeManipulationListener( ManipulationListener listener )
	{
		this.manipulationEventManager.removeManipulationListener( listener );
	}
	
	public void addHandle(ManipulationHandle handle)
	{
		this.handleManager.addHandle( handle );
	}

	private ManipulationHandle getHandleForComponent( Component c)
	{
		if (c == null)
		{
			return null;
		}
		else if (c instanceof ManipulationHandle)
		{
			return (ManipulationHandle)c;
		}
		else
		{
			return getHandleForComponent(c.getParent());
		}
	}
	
	public void mousePressed( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseState( e.getButton(), true );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DOWN );
		
		e.getComponent().requestFocus();
		
		if (e.getComponent() == this.lookingGlassComponent)
		{
			this.currentInputState.setClickPickResult( pickIntoScene( e.getPoint() ) );
		}
		else
		{
			this.currentInputState.setClickHandle( this.getHandleForComponent( e.getComponent() ) );
		}
		this.handleStateChange();
	}
	
	public void mouseReleased( java.awt.event.MouseEvent e) {
		this.currentInputState.setMouseState( e.getButton(), false );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_UP );
		
		if (this.currentRolloverComponent == this.lookingGlassComponent)
		{
			this.currentInputState.setRolloverPickResult( pickIntoScene( e.getPoint() ) );
		}
		else
		{
			this.currentInputState.setRolloverHandle( this.getHandleForComponent( this.currentRolloverComponent ));
		}
		this.handleStateChange();
	}
	
	private PickResult pickIntoScene( Point mouseLocation )
	{
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
		assert onscreenLookingGlass != null;
		edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = onscreenLookingGlass.pickFrontMost( mouseLocation.x, mouseLocation.y, /*isSubElementRequired=*/false );
		return pickResult;
	}
	
	public void mouseDragged( java.awt.event.MouseEvent e ) {
		try {
			this.currentInputState.setMouseLocation( e.getPoint() );
			this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DRAGGED );
			this.handleStateChange();
		} catch( RuntimeException re ) {
			re.printStackTrace();
		}
	}
	
	public void mouseMoved( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseLocation( e.getPoint() );
		if (e.getComponent() == this.lookingGlassComponent)
		{
			this.currentInputState.setRolloverPickResult( pickIntoScene( e.getPoint() ) );
		}
		else
		{
			this.currentInputState.setRolloverHandle( this.getHandleForComponent( e.getComponent() ));
		}
		this.handleStateChange();
	}
	
	public void mouseWheelMoved( MouseWheelEvent e ) {
		this.currentInputState.setMouseWheelState( e.getWheelRotation() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_WHEEL );
		this.handleStateChange();
		this.currentInputState.setMouseWheelState( 0 );
	}
	
	public void keyPressed( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), true );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_DOWN );
		
		HandleSet toSet = this.keyToHandleSetMap.get( e.getKeyCode() );
		if (toSet != null)
		{
			this.setHandleSet( toSet );
		}
		handleStateChange();
		
	}
	
	public void keyReleased( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), false );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_UP );
		handleStateChange();

	}

	public void mouseClicked( MouseEvent e ) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered( MouseEvent e ) {
		this.currentRolloverComponent = e.getComponent();
		if (!this.currentInputState.isAnyMouseButtonDown())
		{
			this.currentInputState.setMouseLocation( e.getPoint() );
			if (e.getComponent() == this.lookingGlassComponent)
			{
				this.currentInputState.setRolloverPickResult( pickIntoScene( e.getPoint() ) );
			}
			else
			{
				this.currentInputState.setRolloverHandle( this.getHandleForComponent( e.getComponent() ));
			}
			this.handleStateChange();
		}
	}

	public void mouseExited( MouseEvent e ) {
		this.currentRolloverComponent = null;
		if (!this.currentInputState.isAnyMouseButtonDown())
		{
			this.currentInputState.setMouseLocation( e.getPoint() );
			this.currentInputState.setRolloverHandle( null );
			this.currentInputState.setRolloverPickResult( null );
			this.handleStateChange();
		}
		
	}

	public void keyTyped( KeyEvent e ) {
		System.out.println("Key typed!");
		
	}

}
