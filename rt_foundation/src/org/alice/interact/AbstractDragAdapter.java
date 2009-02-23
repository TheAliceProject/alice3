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
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import org.alice.interact.event.SelectionEvent;
import org.alice.interact.event.SelectionListener;


import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.lookingglass.PickResult;
import edu.cmu.cs.dennisc.math.Vector3;
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
	private java.awt.Component awtComponent = null;
	
	protected InputState currentInputState = new InputState();
	protected InputState previousInputState = new InputState(); 
	private double timePrev = Double.NaN;
	private boolean hasSetCameraTransformables = false;
	
	protected java.util.Vector<HandleSet> handleSets = new java.util.Vector<HandleSet>();
	protected HandleSet currentHandleSet = null;
	
	protected java.util.Vector< ManipulationHandle > currentManipulationHandles = new java.util.Vector< ManipulationHandle >();
	protected java.util.Vector< ManipulationHandle > nextManipulationHandles = new java.util.Vector< ManipulationHandle >();
	
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

	
	protected class HandleSet
	{
		private java.util.BitSet handleGroups = new java.util.BitSet();
		private int keyID;
		
		public HandleSet( int keyID, HandleGroup...groups)
		{
			this.keyID = keyID;
			for (int i=0; i<groups.length; i++)
			{
				this.handleGroups.set( groups[i].getIndex() );
			}
		}
		
		public int getKeyID()
		{
			return this.keyID;
		}
		
		public java.util.BitSet getBitSet()
		{
			return this.handleGroups;
		}
		
	}
	
	
	public AbstractDragAdapter()
	{
		this.setUpControls();
	}	
	protected abstract void setUpControls();
	
	
	
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
		return this.awtComponent;
	}
	public void setAWTComponent( java.awt.Component awtComponent ) {
		if( this.awtComponent != null ) {
			removeListeners( this.awtComponent  );
		}
		this.awtComponent  = awtComponent;
		if( this.awtComponent  != null ) {
			addListeners( awtComponent );
		}
	}
	
	public void setAnimator( Animator animator )
	{
		this.animator = animator;
		for (int i=0; i<currentManipulationHandles.size(); i++)
		{
			currentManipulationHandles.get( i ).setAnimator( this.animator );
		}
		for (int i=0; i<nextManipulationHandles.size(); i++)
		{
			nextManipulationHandles.get( i ).setAnimator( this.animator );
		}
	}
	
	public void setSelectedObject(Transformable selected)
	{
		if (selectedObject != selected)
		{
			this.fireSelecting( new SelectionEvent(this, selected) );
			for (int i=0; i<currentManipulationHandles.size(); i++)
			{
				ManipulationHandle handle = currentManipulationHandles.get( i );
				if (handle.isVisible())
				{
					handle.setVisible( false );
				}
			}
			
			for (int i=0; i<nextManipulationHandles.size(); i++)
			{
				ManipulationHandle handle = nextManipulationHandles.get( i );
				handle.setManipulatedObject( selected );
				if (handle.isMemberOf( this.currentHandleSet.getBitSet() ))
				{
					handle.setVisible( true );
				}
			}
			
			java.util.Vector< ManipulationHandle >  tempHandles = currentManipulationHandles;
			currentManipulationHandles = nextManipulationHandles;
			nextManipulationHandles = tempHandles;
			
			this.currentInputState.setCurrentlySelectedObject( selected ); 
			selectedObject = selected;
			this.fireSelected( new SelectionEvent(this, selected) );
			this.handleStateChange();
		}
	}
	
	public void setHandleGroup( HandleSet handleGroup )
	{
		this.currentHandleSet = handleGroup;
		for (int i=0; i<currentManipulationHandles.size(); i++)
		{
			ManipulationHandle handle = currentManipulationHandles.get( i );
			if (handle.isMemberOf( this.currentHandleSet.getBitSet() ))
			{
				if (!handle.isVisible())
				{
					handle.setVisible( true );
				}
			}
			else
			{
				if (handle.isVisible())
				{
					handle.setVisible( false );
				}
			}
		}
	}
	
	public RotationRingHandle getCurrentRotationRingForAxis( Vector3 rotationAxis, Transformable object )
	{
		for (int i=0; i<this.currentManipulationHandles.size(); i++)
		{
			ManipulationHandle currentHandle = this.currentManipulationHandles.get( i );
			if (currentHandle instanceof RotationRingHandle)
			{
				RotationRingHandle rotationRing = (RotationRingHandle)currentHandle;
				if (rotationRing.getRotationAxis().equals( rotationAxis ) && rotationRing.getManipulatedObject() == object)
				{
					return rotationRing;
				}
			}
		}
		for (int i=0; i<this.nextManipulationHandles.size(); i++)
		{
			ManipulationHandle currentHandle = this.nextManipulationHandles.get( i );
			if (currentHandle instanceof RotationRingHandle)
			{
				RotationRingHandle rotationRing = (RotationRingHandle)currentHandle;
				if (rotationRing.getRotationAxis().equals( rotationAxis ) && rotationRing.getManipulatedObject() == object)
				{
					return rotationRing;
				}
			}
		}
		return null;
	}
	
	public void setActivateHandle( ManipulationHandle handle, boolean isActive )
	{
		if (handle != null)
		{
			handle.setActive( isActive );
			for (int i=0; i<this.currentManipulationHandles.size(); i++)
			{
				ManipulationHandle currentHandle = this.currentManipulationHandles.get( i );
				if ( currentHandle != handle )
				{
					if (isActive) //Fade out all the other visible handles
					{
						if (currentHandle.isVisible())
						{
							currentHandle.setFaded( true );
						}
					}
					else // unfade all handles
					{
						if (currentHandle.isFaded())
						{
							currentHandle.setFaded( false );
						}
					}
				}
			}
		}
	}
	
	public void setActivateTransformable( Transformable transformable, boolean isActive )
	{
		if (transformable != null)
		{
			for (int i=0; i<this.currentManipulationHandles.size(); i++)
			{
				ManipulationHandle currentHandle = this.currentManipulationHandles.get( i );
				if (isActive) //Fade out all the other visible handles
				{
					if (currentHandle.isVisible())
					{
						currentHandle.setFaded( true );
					}
				}
				else // unfade all handles
				{
					if (currentHandle.isFaded())
					{
						currentHandle.setFaded( false );
					}
				}
			}
		}
	}
	
	protected void handleStateChange()
	{
		java.util.Vector< DragManipulator > toStart = new java.util.Vector< DragManipulator >();
		java.util.Vector< DragManipulator > toEnd = new java.util.Vector< DragManipulator >();
		java.util.Vector< DragManipulator > toUpdate = new java.util.Vector< DragManipulator >();
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
		
		
		if (this.currentInputState.getRolloverPickObject() != this.previousInputState.getRolloverPickObject())
		{
			if (this.currentInputState.getRolloverPickObject() instanceof ManipulationHandle)
			{
				((ManipulationHandle)this.currentInputState.getRolloverPickObject()).setRollover( true );
			}
			if (this.previousInputState.getRolloverPickObject() instanceof ManipulationHandle)
			{
				((ManipulationHandle)this.previousInputState.getRolloverPickObject()).setRollover( false );
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

	protected void addListeners( java.awt.Component component ) {
		component.addMouseListener( this );
		component.addMouseMotionListener( this );
		component.addKeyListener( this );
		component.addMouseWheelListener( this );
	}

	protected void removeListeners( java.awt.Component component ) {
		component.removeMouseListener( this );
		component.removeMouseMotionListener( this );
		component.removeKeyListener( this );
		component.removeMouseWheelListener( this );
	}
	
	public void mousePressed( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseState( e.getButton(), true );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DOWN );
		this.currentInputState.setClickPickResult( pickIntoScene( e.getPoint() ) );
		this.handleStateChange();
	}
	
	public void mouseReleased( java.awt.event.MouseEvent e) {
		this.currentInputState.setMouseState( e.getButton(), false );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_UP );
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
		this.currentInputState.setRolloverPickResult( pickIntoScene( e.getPoint() ) );
		
		PickHint rolloverObjectType = PickCondition.getPickType( this.currentInputState.getRolloverPickResult() );
		if ( rolloverObjectType.intersects( PickHint.MOVEABLE_OBJECTS) )
		{
			this.currentInputState.setRolloverPickObject( this.currentInputState.getRolloverPickedTransformable(true) );
		}
		else if (rolloverObjectType.intersects( PickHint.HANDLES) )
		{
			this.currentInputState.setRolloverPickObject(this.currentInputState.getRolloverPickedTransformable(true));
		}
		else
		{
			this.currentInputState.setRolloverPickObject( null );
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
		
		for (int i=0; i<this.handleSets.size(); i++)
		{
			if (this.handleSets.get( i ).getKeyID() == e.getKeyCode())
			{
				this.setHandleGroup( this.handleSets.get( i ) );
			}
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
		// TODO Auto-generated method stub
		
	}

	public void mouseExited( MouseEvent e ) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped( KeyEvent e ) {
		// TODO Auto-generated method stub
		
	}

}
