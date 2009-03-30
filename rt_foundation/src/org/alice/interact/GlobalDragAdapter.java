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
import java.awt.event.KeyEvent;
import org.alice.interact.ModifierMask.ModifierKey;
import org.alice.interact.event.ManipulationEvent;



import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author David Culyba
 */
public class GlobalDragAdapter extends AbstractDragAdapter {
	
	@Override
	protected void setUpControls()
	{
		MovementKey[] movementKeys = {
				//Forward
//				new MovementKey(KeyEvent.VK_UP, new Point3(0, 0, -1)),
//				new MovementKey(KeyEvent.VK_NUMPAD8, new Point3(0, 0, -1)),
				new MovementKey(KeyEvent.VK_W, new MovementDescription(MovementDirection.FORWARD)),
				//Backward
//				new MovementKey(KeyEvent.VK_DOWN, new Point3(0, 0, 1)),
//				new MovementKey(KeyEvent.VK_NUMPAD2, new Point3(0, 0, 1)),
				new MovementKey(KeyEvent.VK_S, new MovementDescription(MovementDirection.BACKWARD)),
				//Left
//				new MovementKey(KeyEvent.VK_LEFT, new Point3(-1, 0, 0)),
//				new MovementKey(KeyEvent.VK_NUMPAD4, new Point3(-1, 0, 0)),
				new MovementKey(KeyEvent.VK_A, new MovementDescription(MovementDirection.LEFT)),
				//Right
//				new MovementKey(KeyEvent.VK_RIGHT, new Point3(1, 0, 0)),
//				new MovementKey(KeyEvent.VK_NUMPAD6, new Point3(1, 0, 0)),
				new MovementKey(KeyEvent.VK_D,  new MovementDescription(MovementDirection.RIGHT)),
				//Up
				new MovementKey(KeyEvent.VK_PAGE_UP, new MovementDescription(MovementDirection.UP, MovementType.LOCAL), .5d),
				//Down
				new MovementKey(KeyEvent.VK_PAGE_DOWN, new MovementDescription(MovementDirection.DOWN, MovementType.LOCAL), .5d),
				//Up Left
//				new MovementKey(KeyEvent.VK_NUMPAD7, new Point3(-1, 0, -1)),
//				//Up Right
//				new MovementKey(KeyEvent.VK_NUMPAD9, new Point3(1, 0, -1)),
//				//Back Left
//				new MovementKey(KeyEvent.VK_NUMPAD1, new Point3(-1, 0, 1)),
//				//Back Right
//				new MovementKey(KeyEvent.VK_NUMPAD3, new Point3(1, 0, 1)),
		};
		
		MovementKey[] zoomKeys = {
				//Zoom out
				new MovementKey(KeyEvent.VK_MINUS,  new MovementDescription(MovementDirection.BACKWARD, MovementType.LOCAL)),
				new MovementKey(KeyEvent.VK_SUBTRACT,  new MovementDescription(MovementDirection.BACKWARD, MovementType.LOCAL)),
				//Zoom in
				new MovementKey(KeyEvent.VK_EQUALS,  new MovementDescription(MovementDirection.FORWARD, MovementType.LOCAL)),
				new MovementKey(KeyEvent.VK_ADD,  new MovementDescription(MovementDirection.FORWARD, MovementType.LOCAL)),
		};
		
		MovementKey[] turnKeys = {
				//Left
				new MovementKey(KeyEvent.VK_OPEN_BRACKET,  new MovementDescription(MovementDirection.LEFT, MovementType.LOCAL), 2.0d),
				//Right
				new MovementKey(KeyEvent.VK_CLOSE_BRACKET,  new MovementDescription(MovementDirection.RIGHT, MovementType.LOCAL), -2.0d),
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
	//	this.manipulators.add( cameraTranslate );
		
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
//		this.manipulators.add( cameraRotate );
	
//		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( new CameraOrbitDragManipulator() );
//		MouseDragCondition leftAndNoModifiers = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1 , new PickCondition( PickHint.NON_INTERACTIVE ), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
//		//cameraOrbit.addCondition( new MousePressCondition( java.awt.event.MouseEvent.BUTTON1 , new NotPickCondition( PickCondition.PickType.MOVEABLE_OBJECT ) ) );
//		cameraOrbit.addCondition(leftAndNoModifiers);
//		cameraOrbit.addCondition( new MouseDragCondition( java.awt.event.MouseEvent.BUTTON3 , new PickCondition( PickHint.EVERYTHING ) ) );
//		this.manipulators.add(cameraOrbit);
		
		ManipulatorConditionSet mouseTranslateObject = new ManipulatorConditionSet( new ObjectTranslateDragManipulator() );
		MouseDragCondition moveableObject = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		mouseTranslateObject.addCondition( moveableObject );
		this.manipulators.add( mouseTranslateObject );
		
		ManipulatorConditionSet mouseUpDownTranslateObject = new ManipulatorConditionSet( new ObjectUpDownDragManipulator() );
		MouseDragCondition moveableObjectWithShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierKey.SHIFT ));
		mouseUpDownTranslateObject.addCondition( moveableObjectWithShift );
		this.manipulators.add( mouseUpDownTranslateObject );
		
		ManipulatorConditionSet mouseRotateObjectLeftRight = new ManipulatorConditionSet( new HandlelessObjectRotateDragManipulator(Vector3.accessPositiveYAxis()) );
		MouseDragCondition moveableObjectWithCtrl = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierKey.CONTROL ));
		mouseRotateObjectLeftRight.addCondition( moveableObjectWithCtrl );
		this.manipulators.add( mouseRotateObjectLeftRight );
		
		ManipulatorConditionSet mouseHandleDrag = new ManipulatorConditionSet( new ObjectGlobalHandleDragManipulator() );
		MouseDragCondition handleObjectCondition = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.HANDLES));
		mouseHandleDrag.addCondition( handleObjectCondition );
		this.manipulators.add( mouseHandleDrag );
		
		ManipulatorConditionSet selectObject = new ManipulatorConditionSet( new SelectObjectDragManipulator(this) );
		selectObject.addCondition( new MouseDragCondition(java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.EVERYTHING)) );
		this.manipulators.add( selectObject );
		
		for (int i=0; i<this.manipulators.size(); i++)
		{
			this.manipulators.get( i ).getManipulator().setDragAdapter( this );
		}
		
		this.handleSets.add( new HandleSet( java.awt.event.KeyEvent.VK_4, HandleGroup.RESIZE) );
		this.handleSets.add( new HandleSet( java.awt.event.KeyEvent.VK_3, HandleGroup.ROTATION) );
		this.handleSets.add( new HandleSet( java.awt.event.KeyEvent.VK_2, HandleGroup.TRANSLATION) );
		this.handleSets.add( new HandleSet( java.awt.event.KeyEvent.VK_1, HandleGroup.DEFAULT) );
		
		
		ManipulationHandleIndirection rotateAboutYAxis = new ManipulationHandleIndirection(new StoodUpRotationRingHandle(Vector3.accessPositiveYAxis(), RotationRingHandle.HandlePosition.BOTTOM ));
		rotateAboutYAxis.addToGroups( HandleGroup.ROTATION, HandleGroup.DEFAULT, HandleGroup.STOOD_UP);
		this.manipulationEventManager.addManipulationListener( rotateAboutYAxis,
				ManipulationEvent.EventType.Rotate,
				new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( rotateAboutYAxis, 
				ManipulationEvent.EventType.Rotate,
				new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationHandles.add( rotateAboutYAxis );
		
		ManipulationHandleIndirection rotateAboutXAxis = new ManipulationHandleIndirection( new RotationRingHandle(Vector3.accessPositiveXAxis()) );
		rotateAboutXAxis.addToGroups( HandleGroup.ROTATION );
		this.manipulationHandles.add( rotateAboutXAxis );
		
		ManipulationHandleIndirection rotateAboutZAxis = new ManipulationHandleIndirection(new RotationRingHandle(Vector3.accessPositiveZAxis()));
		rotateAboutZAxis.addToGroups( HandleGroup.ROTATION );
		this.manipulationHandles.add( rotateAboutZAxis );
		
		ManipulationHandleIndirection translateUp = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP), Color4f.GREEN));
		ManipulationHandleIndirection translateDown = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP), Color4f.GREEN));
		translateUp.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP);
		this.manipulationEventManager.addManipulationListener( translateDown,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateUp, 
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateDown,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.DOWN, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateUp,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.UP, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationHandles.add(translateUp);
		this.manipulationHandles.add(translateDown);
		
		ManipulationHandleIndirection translateXAxisRight = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.RIGHT, MovementType.ABSOLUTE), Color4f.RED));
		ManipulationHandleIndirection translateXAxisLeft = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.LEFT, MovementType.ABSOLUTE), Color4f.RED));	
		//Add the left handle to the group to be shown by the system
		translateXAxisLeft.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		this.manipulationEventManager.addManipulationListener( translateXAxisLeft,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.LEFT, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateXAxisRight,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.RIGHT, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateXAxisLeft,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.LEFT, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateXAxisRight,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.RIGHT, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationHandles.add( translateXAxisRight );
		this.manipulationHandles.add( translateXAxisLeft );
		
		ManipulationHandleIndirection translateForward = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.FORWARD, MovementType.ABSOLUTE), Color4f.BLUE));
		ManipulationHandleIndirection translateBackward = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.BACKWARD, MovementType.ABSOLUTE), Color4f.BLUE));
		translateForward.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		this.manipulationEventManager.addManipulationListener( translateBackward,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.BACKWARD, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateForward,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.FORWARD, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateBackward,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.BACKWARD, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationEventManager.addManipulationListener( translateForward,
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.FORWARD, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS );
		this.manipulationHandles.add( translateForward );
		this.manipulationHandles.add( translateBackward );
		
		ManipulationHandleIndirection scaleAxis = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.RESIZE, MovementType.STOOD_UP), Color4f.PINK));
		scaleAxis.addToGroups( HandleGroup.RESIZE );
		this.manipulationHandles.add( scaleAxis );
		
		ManipulationHandleIndirection scaleAxisX = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.RIGHT, MovementType.STOOD_UP), Color4f.MAGENTA, true));
		scaleAxisX.addToGroups( HandleGroup.RESIZE );
		this.manipulationHandles.add( scaleAxisX );
		
		ManipulationHandleIndirection scaleAxisY = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP), Color4f.YELLOW, true));
		scaleAxisY.addToGroups( HandleGroup.RESIZE );
		this.manipulationHandles.add( scaleAxisY );
		
		ManipulationHandleIndirection scaleAxisZ = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.FORWARD, MovementType.STOOD_UP), Color4f.CYAN, true));
		scaleAxisZ.addToGroups( HandleGroup.RESIZE );
		this.manipulationHandles.add( scaleAxisZ );
		
		this.setHandleGroup( this.handleSets.lastElement() );
	}
	
	
}
