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

package sceneeditor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;

import sceneeditor.SelectedObjectCondition.ObjectSwitchBehavior;

import edu.cmu.cs.dennisc.lookingglass.PickResult;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.ui.DragStyle;
import edu.cmu.cs.dennisc.ui.lookingglass.OnscreenLookingGlassDragAdapter;

/**
 * @author David Culyba
 */
public class GlobalDragAdapter extends OnscreenLookingGlassDragAdapter implements java.awt.event.MouseWheelListener, java.awt.event.KeyListener {
	
	private java.util.Vector< ManipulatorConditionSet > manipulators = new java.util.Vector< ManipulatorConditionSet >();
	
	/*
	 * Manipulators that manipulate the camera must have their transformable set to be the scene graph camera.
	 * Unfortunately, the camera doesn't exist until the scene is displayed. To overcome this, we store the
	 * cameras in a vector and set their cameras when the scene is 
	 */

	
	private InputState currentInputState = new InputState();
	private InputState previousInputState = new InputState(); 
	private double timePrev = Double.NaN;
	private boolean hasSetCameraTransformables = false;
	
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
			AndInputCondition keyAndNotSelected = new AndInputCondition( new KeyPressCondition( movementKeys[i].keyValue ), new InvertedSelectedObjectCondition( PickCondition.PickType.MOVEABLE_OBJECT, InvertedSelectedObjectCondition.ObjectSwitchBehavior.IGNORE_SWITCH ) );
			cameraTranslate.addCondition( keyAndNotSelected );
		}
		for (int i=0; i<zoomKeys.length; i++)
		{
			AndInputCondition keyAndNotSelected = new AndInputCondition( new KeyPressCondition( zoomKeys[i].keyValue, noModifiers), new InvertedSelectedObjectCondition( PickCondition.PickType.MOVEABLE_OBJECT, InvertedSelectedObjectCondition.ObjectSwitchBehavior.IGNORE_SWITCH  ) );
			cameraTranslate.addCondition( keyAndNotSelected );
		}
		this.manipulators.add( cameraTranslate );
		
		ManipulatorConditionSet objectTranslate = new ManipulatorConditionSet( new ObjectTranslateKeyManipulator( movementKeys ) );
		for (int i=0; i<movementKeys.length; i++)
		{
			AndInputCondition keyAndSelected = new AndInputCondition( new KeyPressCondition( movementKeys[i].keyValue ), new SelectedObjectCondition( PickCondition.PickType.MOVEABLE_OBJECT ) );
			objectTranslate.addCondition( keyAndSelected );
		}
		this.manipulators.add( objectTranslate );
		
		ManipulatorConditionSet cameraRotate = new ManipulatorConditionSet( new CameraRotateKeyManipulator( turnKeys ) );
		for (int i=0; i<turnKeys.length; i++)
		{
			AndInputCondition keyAndNotSelected = new AndInputCondition( new KeyPressCondition( turnKeys[i].keyValue), new InvertedSelectedObjectCondition( PickCondition.PickType.MOVEABLE_OBJECT, InvertedSelectedObjectCondition.ObjectSwitchBehavior.IGNORE_SWITCH  ) );
			cameraRotate.addCondition( keyAndNotSelected );
		}
		this.manipulators.add( cameraRotate );
		
		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( new CameraOrbitDragManipulator() );
		MouseDragCondition leftAndNoModifiers = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1 , new InvertedPickCondition( PickCondition.PickType.MOVEABLE_OBJECT ), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		//cameraOrbit.addCondition( new MousePressCondition( java.awt.event.MouseEvent.BUTTON1 , new NotPickCondition( PickCondition.PickType.MOVEABLE_OBJECT ) ) );
		cameraOrbit.addCondition(leftAndNoModifiers);
		cameraOrbit.addCondition( new MouseDragCondition( java.awt.event.MouseEvent.BUTTON3 , new PickCondition( PickCondition.PickType.ANYTHING ) ) );
		this.manipulators.add(cameraOrbit);
		
		ManipulatorConditionSet mouseTranslateObject = new ManipulatorConditionSet( new ObjectTranslateDragManipulator() );
		MouseDragCondition moveableObject = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickCondition.PickType.MOVEABLE_OBJECT), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		mouseTranslateObject.addCondition( moveableObject );
		this.manipulators.add( mouseTranslateObject );
		
		ManipulatorConditionSet mouseRotateObject = new ManipulatorConditionSet( new ObjectRotateDragManipulator(Vector3.accessPositiveYAxis(), MovementType.STOOD_UP) );
		MouseDragCondition rotatableObject = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickCondition.PickType.MOVEABLE_OBJECT), new ModifierMask( ModifierMask.ModifierKey.CONTROL ));
		mouseRotateObject.addCondition( rotatableObject );
		this.manipulators.add( mouseRotateObject );
		
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
	
	@Override
	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		super.setOnscreenLookingGlass( onscreenLookingGlass );
		onscreenLookingGlass.getLookingGlassFactory().addAutomaticDisplayListener( new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
			public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
				GlobalDragAdapter.this.handleDisplayed();
			}
		} );
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
	
	@Override
	protected void addListeners( java.awt.Component component ) {
		super.addListeners( component );
		this.getOnscreenLookingGlass().getAWTComponent().addMouseWheelListener( this );
	}
	@Override
	protected void removeListeners( java.awt.Component component ) {
		super.removeListeners( component );
		this.getOnscreenLookingGlass().getAWTComponent().removeMouseWheelListener( this );
	}
	
	
	@Override
	protected void handleMouseDrag(Point current, int deltaSince0,
			int deltaSince02, int xDeltaSincePrevious, int yDeltaSincePrevious,
			DragStyle dragStyle) {

		//this.cameraTranslator.updateManipulator( current, xDeltaSincePrevious, yDeltaSincePrevious );

	}

	@Override
	protected void handleMousePress(Point current, DragStyle dragStyle,
			boolean isOriginalAsOpposedToStyleChange) {
		
//		this.cameraTranslator.setManipulatedTransformable( this.getSGCameraTransformable() );
//		this.cameraTranslator.startManipulator( current );
	}

	@Override
	protected Point handleMouseRelease(Point rvCurrent, DragStyle dragStyle,
			boolean isOriginalAsOpposedToStyleChange) {
//		this.cameraTranslator.endManipulator( rvCurrent );
		return rvCurrent;
	}
	
	@Override
	public void mousePressed( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseState( e.getButton(), true );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DOWN );
		this.currentInputState.setPickResult( pickIntoScene( e.getPoint() ) );
		
		PickCondition.PickType clickedObjectType = PickCondition.getPickType( this.currentInputState.getPickResult() );
		if (clickedObjectType == PickCondition.PickType.MOVEABLE_OBJECT)
		{
			this.currentInputState.setCurrentlySelectedObject( this.currentInputState.getPickedTransformable() );
		}
		else
		{
			this.currentInputState.setCurrentlySelectedObject( null );
		}
		
		System.out.println(" SELECTED OBJECT: " + this.currentInputState.getCurrentlySelectedObject());
		
		
		super.mousePressed( e );
		this.handleStateChange();
	}
	
	@Override
	public void mouseReleased( java.awt.event.MouseEvent e) {
		this.currentInputState.setMouseState( e.getButton(), false );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_UP );
		super.mouseReleased( e );
		this.handleStateChange();
	}
	
	private PickResult pickIntoScene( Point mouseLocation )
	{
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
		assert onscreenLookingGlass != null;
		edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = onscreenLookingGlass.pickFrontMost( mouseLocation.x, mouseLocation.y, /*isSubElementRequired=*/false );
		return pickResult;
	}
	
	@Override
	public void mouseDragged( java.awt.event.MouseEvent e ) {
		try {
			this.currentInputState.setMouseLocation( e.getPoint() );
			this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DRAGGED );
			this.handleStateChange();
		} catch( RuntimeException re ) {
			re.printStackTrace();
		}
	}
	
	@Override
	public void mouseMoved( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.handleStateChange();
	}
	
	public void mouseWheelMoved( MouseWheelEvent e ) {
		this.currentInputState.setMouseWheelState( e.getWheelRotation() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_WHEEL );
		this.handleStateChange();
		this.currentInputState.setMouseWheelState( 0 );
	}
	
	@Override
	public void keyPressed( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), true );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_DOWN );
		
		//System.out.println(KeyEvent.getKeyText( e.getKeyCode() ));
		
		super.keyPressed( e );
		handleStateChange();
		
	}
	
	@Override
	public void keyReleased( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), false );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_UP );
		super.keyPressed( e );
		handleStateChange();

	}

}
