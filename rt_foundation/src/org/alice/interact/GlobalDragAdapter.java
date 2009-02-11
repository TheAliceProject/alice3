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

import org.alice.interact.ModifierMask.ModifierKey;


import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.lookingglass.PickResult;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.ui.DragStyle;
import edu.cmu.cs.dennisc.ui.UndoRedoManager;
import edu.cmu.cs.dennisc.ui.lookingglass.OnscreenLookingGlassDragAdapter;

/**
 * @author David Culyba
 */
public class GlobalDragAdapter implements java.awt.event.MouseWheelListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener, java.awt.event.KeyListener {
	
	public static final String BOUNDING_BOX_KEY = "BOUNDING_BOX_KEY";
	
	private java.util.Vector< ManipulatorConditionSet > manipulators = new java.util.Vector< ManipulatorConditionSet >();

	private edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
	private edu.cmu.cs.dennisc.animation.Animator animator;
	private java.awt.Component awtComponent = null;
	
	private InputState currentInputState = new InputState();
	private InputState previousInputState = new InputState(); 
	private double timePrev = Double.NaN;
	private boolean hasSetCameraTransformables = false;
	
	private java.util.Vector<HandleSet> handleSets = new java.util.Vector<HandleSet>();
	HandleSet currentHandleSet = null;
	
	private java.util.Vector< ManipulationHandle > currentManipulationHandles = new java.util.Vector< ManipulationHandle >();
	private java.util.Vector< ManipulationHandle > nextManipulationHandles = new java.util.Vector< ManipulationHandle >();
	
	private Transformable selectedObject = null;
	
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
	
	public GlobalDragAdapter()
	{
		MovementKey[] movementKeys = {
				//Forward
				new MovementKey(KeyEvent.VK_UP, new Point3(0, 0, -1)),
				new MovementKey(KeyEvent.VK_NUMPAD8, new Point3(0, 0, -1)),
				new MovementKey(KeyEvent.VK_W, new Point3(0, 0, -1)),
				//Backward
				new MovementKey(KeyEvent.VK_DOWN, new Point3(0, 0, 1)),
				new MovementKey(KeyEvent.VK_NUMPAD2, new Point3(0, 0, 1)),
				new MovementKey(KeyEvent.VK_S, new Point3(0, 0, 1)),
				//Left
				new MovementKey(KeyEvent.VK_LEFT, new Point3(-1, 0, 0)),
				new MovementKey(KeyEvent.VK_NUMPAD4, new Point3(-1, 0, 0)),
				new MovementKey(KeyEvent.VK_A, new Point3(-1, 0, 0)),
				//Right
				new MovementKey(KeyEvent.VK_RIGHT, new Point3(1, 0, 0)),
				new MovementKey(KeyEvent.VK_NUMPAD6, new Point3(1, 0, 0)),
				new MovementKey(KeyEvent.VK_D, new Point3(1, 0, 0)),
				//Up
				new MovementKey(KeyEvent.VK_PAGE_UP, new Point3(0, 1, 0)),
				//Down
				new MovementKey(KeyEvent.VK_PAGE_DOWN, new Point3(0, -1, 0)),
				//Up Left
				new MovementKey(KeyEvent.VK_NUMPAD7, new Point3(-1, 0, -1)),
				//Up Right
				new MovementKey(KeyEvent.VK_NUMPAD9, new Point3(1, 0, -1)),
				//Back Left
				new MovementKey(KeyEvent.VK_NUMPAD1, new Point3(-1, 0, 1)),
				//Back Right
				new MovementKey(KeyEvent.VK_NUMPAD3, new Point3(1, 0, 1)),
		};
		
		MovementKey[] zoomKeys = {
				//Zoom out
				new MovementKey(KeyEvent.VK_MINUS, new Point3(0, 0, 1), MovementType.LOCAL),
				new MovementKey(KeyEvent.VK_SUBTRACT, new Point3(0, 0, 1), MovementType.LOCAL),
				//Zoom in
				new MovementKey(KeyEvent.VK_EQUALS, new Point3(0, 0, -1), MovementType.LOCAL),
				new MovementKey(KeyEvent.VK_ADD, new Point3(0, 0, -1), MovementType.LOCAL),
		};
		
		MovementKey[] turnKeys = {
				//Left
				new MovementKey(KeyEvent.VK_OPEN_BRACKET, new Vector3(0, 1, 0), MovementType.LOCAL, 2.0d),
				//Right
				new MovementKey(KeyEvent.VK_CLOSE_BRACKET, new Vector3(0, 1, 0), MovementType.LOCAL, -2.0d),
		};
		
		ModifierMask noModifiers = new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN );
		
		CameraTranslateKeyManipulator cameraTranslateManip = new CameraTranslateKeyManipulator( movementKeys );
		cameraTranslateManip.addKeys( zoomKeys );
		ManipulatorConditionSet cameraTranslate = new ManipulatorConditionSet( cameraTranslateManip );
		for (int i=0; i<movementKeys.length; i++)
		{
			AndInputCondition keyAndNotSelected = new AndInputCondition( new KeyPressCondition( movementKeys[i].keyValue ), new SelectedObjectCondition( PickHint.NON_INTERACTIVE, InvertedSelectedObjectCondition.ObjectSwitchBehavior.IGNORE_SWITCH ) );
			cameraTranslate.addCondition( keyAndNotSelected );
		}
		for (int i=0; i<zoomKeys.length; i++)
		{
			AndInputCondition keyAndNotSelected = new AndInputCondition( new KeyPressCondition( zoomKeys[i].keyValue, noModifiers), new SelectedObjectCondition( PickHint.NON_INTERACTIVE, InvertedSelectedObjectCondition.ObjectSwitchBehavior.IGNORE_SWITCH  ) );
			cameraTranslate.addCondition( keyAndNotSelected );
		}
		this.manipulators.add( cameraTranslate );
		
		ManipulatorConditionSet objectTranslate = new ManipulatorConditionSet( new ObjectTranslateKeyManipulator( movementKeys ) );
		for (int i=0; i<movementKeys.length; i++)
		{
			AndInputCondition keyAndSelected = new AndInputCondition( new KeyPressCondition( movementKeys[i].keyValue ), new SelectedObjectCondition( PickHint.MOVEABLE_OBJECTS ) );
			objectTranslate.addCondition( keyAndSelected );
		}
		this.manipulators.add( objectTranslate );
		
		ManipulatorConditionSet cameraRotate = new ManipulatorConditionSet( new CameraRotateKeyManipulator( turnKeys ) );
		for (int i=0; i<turnKeys.length; i++)
		{
			AndInputCondition keyAndNotSelected = new AndInputCondition( new KeyPressCondition( turnKeys[i].keyValue), new SelectedObjectCondition( PickHint.NON_INTERACTIVE, InvertedSelectedObjectCondition.ObjectSwitchBehavior.IGNORE_SWITCH  ) );
			cameraRotate.addCondition( keyAndNotSelected );
		}
		this.manipulators.add( cameraRotate );
		
		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( new CameraOrbitDragManipulator() );
		MouseDragCondition leftAndNoModifiers = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1 , new PickCondition( PickHint.NON_INTERACTIVE ), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		//cameraOrbit.addCondition( new MousePressCondition( java.awt.event.MouseEvent.BUTTON1 , new NotPickCondition( PickCondition.PickType.MOVEABLE_OBJECT ) ) );
		cameraOrbit.addCondition(leftAndNoModifiers);
		cameraOrbit.addCondition( new MouseDragCondition( java.awt.event.MouseEvent.BUTTON3 , new PickCondition( PickHint.EVERYTHING ) ) );
		this.manipulators.add(cameraOrbit);
		
		ManipulatorConditionSet mouseTranslateObject = new ManipulatorConditionSet( new ObjectTranslateDragManipulator() );
		MouseDragCondition moveableObject = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		mouseTranslateObject.addCondition( moveableObject );
		this.manipulators.add( mouseTranslateObject );
		
		ManipulatorConditionSet mouseUpDownTranslateObject = new ManipulatorConditionSet( new ObjectUpDownDragManipulator() );
		MouseDragCondition moveableObjectWithShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierKey.SHIFT ));
		mouseUpDownTranslateObject.addCondition( moveableObjectWithShift );
		this.manipulators.add( mouseUpDownTranslateObject );
		
		ManipulatorConditionSet mouseHandleDrag = new ManipulatorConditionSet( new ObjectGlobalHandleDragManipulator() );
		MouseDragCondition handleObjectCondition = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.HANDLES));
		mouseHandleDrag.addCondition( handleObjectCondition );
		this.manipulators.add( mouseHandleDrag );
		
		for (int i=0; i<this.manipulators.size(); i++)
		{
			this.manipulators.get( i ).getManipulator().setDragAdapter( this );
		}
		
		this.handleSets.add( new HandleSet( java.awt.event.KeyEvent.VK_3, HandleGroup.ROTATION) );
		this.handleSets.add( new HandleSet( java.awt.event.KeyEvent.VK_2, HandleGroup.TRANSLATION) );
		this.handleSets.add( new HandleSet( java.awt.event.KeyEvent.VK_1, HandleGroup.DEFAULT) );
		this.currentHandleSet = this.handleSets.lastElement();
		
		RotationRingHandle rotateAboutYAxis = new StoodUpRotationRingHandle(Vector3.accessPositiveYAxis());
		RotationRingHandle rotateAboutYAxis2 = new StoodUpRotationRingHandle(Vector3.accessPositiveYAxis());
		rotateAboutYAxis.addToGroups( HandleGroup.ROTATION, HandleGroup.DEFAULT, HandleGroup.STOOD_UP);
		rotateAboutYAxis2.addToGroups( HandleGroup.ROTATION, HandleGroup.DEFAULT, HandleGroup.STOOD_UP);
		currentManipulationHandles.add( rotateAboutYAxis );
		nextManipulationHandles.add( rotateAboutYAxis2 );
		
		RotationRingHandle rotateAboutXAxis = new RotationRingHandle(Vector3.accessPositiveXAxis());
		RotationRingHandle rotateAboutXAxis2 = new RotationRingHandle(Vector3.accessPositiveXAxis());
		rotateAboutXAxis.addToGroups( HandleGroup.ROTATION );
		rotateAboutXAxis2.addToGroups( HandleGroup.ROTATION );
		
		currentManipulationHandles.add( rotateAboutXAxis );
		nextManipulationHandles.add( rotateAboutXAxis2 );
		
		RotationRingHandle rotateAboutZAxis = new RotationRingHandle(Vector3.accessPositiveZAxis());
		RotationRingHandle rotateAboutZAxis2 = new RotationRingHandle(Vector3.accessPositiveZAxis());
		rotateAboutZAxis.addToGroups( HandleGroup.ROTATION );
		rotateAboutZAxis2.addToGroups( HandleGroup.ROTATION );
		currentManipulationHandles.add( rotateAboutZAxis );
		nextManipulationHandles.add( rotateAboutZAxis2 );
		
		LinearTranslateHandle translateYAxis = new StoodUpLinearTranslateHandle(Vector3.accessPositiveYAxis(), Color4f.BLUE);
		translateYAxis.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		currentManipulationHandles.add( translateYAxis );
		LinearTranslateHandle translateYAxis2 = new StoodUpLinearTranslateHandle(Vector3.accessPositiveYAxis(), Color4f.BLUE);
		translateYAxis2.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		nextManipulationHandles.add( translateYAxis2 );
		
		LinearTranslateHandle translateXAxis = new StoodUpLinearTranslateHandle(Vector3.accessPositiveXAxis(), Color4f.RED);
		translateXAxis.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		currentManipulationHandles.add( translateXAxis );
		LinearTranslateHandle translateXAxis2 = new StoodUpLinearTranslateHandle(Vector3.accessPositiveXAxis(), Color4f.RED);
		translateXAxis2.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		nextManipulationHandles.add( translateXAxis2 );
		
		LinearTranslateHandle translateZAxis = new StoodUpLinearTranslateHandle(Vector3.accessPositiveZAxis(), Color4f.GREEN);
		translateZAxis.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		currentManipulationHandles.add( translateZAxis );
		LinearTranslateHandle translateZAxis2 = new StoodUpLinearTranslateHandle(Vector3.accessPositiveZAxis(), Color4f.GREEN);
		translateZAxis2.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		nextManipulationHandles.add( translateZAxis2 );
		
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
				GlobalDragAdapter.this.handleDisplayed();
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
	
	protected void setSelectedObject(Transformable selected)
	{
		if (selectedObject != selected)
		{
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
			
			selectedObject = selected;
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
						currentHandle.setFaded( false );
				}
			}
		}
	}
	
	protected void handleStateChange()
	{
		for (int i=0; i<this.manipulators.size(); i++)
		{
			ManipulatorConditionSet currentManipulatorSet = this.manipulators.get( i );
			if (currentManipulatorSet.stateChanged( this.currentInputState, this.previousInputState))
			{
				if ( currentManipulatorSet.shouldContinue( this.currentInputState, this.previousInputState ))
				{
					currentManipulatorSet.getManipulator().dataUpdateManipulator( this.currentInputState, this.previousInputState );
				}
				else if ( currentManipulatorSet.justStarted( this.currentInputState, this.previousInputState ) )
				{
					currentManipulatorSet.getManipulator().startManipulator( this.currentInputState );
				}
				else if ( currentManipulatorSet.justEnded( this.currentInputState, this.previousInputState ) )
				{
					currentManipulatorSet.getManipulator().endManipulator( this.currentInputState, this.previousInputState );
				}
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

	private void handleDisplayed() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			if (!hasSetCameraTransformables)
			{
				for (int i=0; i<this.manipulators.size(); i++)
				{
					if (this.manipulators.get( i ).getManipulator() instanceof CameraInformedManipulator)
					{
						((CameraInformedManipulator)this.manipulators.get( i ).getManipulator()).setCamera( sgCamera );
						((CameraInformedManipulator)this.manipulators.get( i ).getManipulator()).setOnscreenLookingGlass( getOnscreenLookingGlass() );
					}
				}
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
		
		PickHint clickedObjectType = PickCondition.getPickType( this.currentInputState.getClickPickResult() );
		if ( clickedObjectType.intersects( PickHint.MOVEABLE_OBJECTS) )
		{
			this.currentInputState.setCurrentlySelectedObject( this.currentInputState.getClickPickedTransformable(true) );
		}
		else if (clickedObjectType.intersects( PickHint.HANDLES) )
		{
			Transformable pickedHandle = this.currentInputState.getClickPickedTransformable(true);
			if (pickedHandle instanceof RotationRingHandle)
			{
				this.currentInputState.setCurrentlySelectedObject( ((RotationRingHandle)pickedHandle).getManipulatedObject() ); 
			}
		}
		else
		{
			this.currentInputState.setCurrentlySelectedObject( null );
		}
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
