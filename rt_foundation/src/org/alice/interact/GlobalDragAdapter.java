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
import org.alice.interact.condition.AndInputCondition;
import org.alice.interact.condition.KeyPressCondition;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.condition.SelectedObjectCondition;
import org.alice.interact.condition.InvertedSelectedObjectCondition;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.LinearScaleHandle;
import org.alice.interact.handle.LinearTranslateHandle;
import org.alice.interact.handle.ManipulationHandleIndirection;
import org.alice.interact.handle.RotationRingHandle;
import org.alice.interact.handle.StoodUpRotationRingHandle;
import org.alice.interact.manipulator.CameraRotateKeyManipulator;
import org.alice.interact.manipulator.CameraTranslateKeyManipulator;
import org.alice.interact.manipulator.HandlelessObjectRotateDragManipulator;
import org.alice.interact.manipulator.LinearDragManipulator;
import org.alice.interact.manipulator.ObjectGlobalHandleDragManipulator;
import org.alice.interact.manipulator.ObjectRotateDragManipulator;
import org.alice.interact.manipulator.ObjectTranslateDragManipulator;
import org.alice.interact.manipulator.ObjectTranslateKeyManipulator;
import org.alice.interact.manipulator.ObjectUpDownDragManipulator;
import org.alice.interact.manipulator.ScaleDragManipulator;
import org.alice.interact.manipulator.SelectObjectDragManipulator;
import edu.cmu.cs.dennisc.color.Color4f;

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
		
		ManipulatorConditionSet mouseRotateObjectLeftRight = new ManipulatorConditionSet( new HandlelessObjectRotateDragManipulator(MovementDirection.UP) );
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
		
		this.keyToHandleSetMap.put( java.awt.event.KeyEvent.VK_4, HandleSet.RESIZE_INTERACTION );
		this.keyToHandleSetMap.put( java.awt.event.KeyEvent.VK_3, HandleSet.ROTATION_INTERACTION );
		this.keyToHandleSetMap.put( java.awt.event.KeyEvent.VK_2, HandleSet.TRANSLATION_INTERACTION );
		this.keyToHandleSetMap.put( java.awt.event.KeyEvent.VK_1, HandleSet.DEFAULT_INTERACTION );
		
		
		ManipulationHandleIndirection rotateAboutYAxis = new ManipulationHandleIndirection(new StoodUpRotationRingHandle(MovementDirection.UP, RotationRingHandle.HandlePosition.BOTTOM ));
		rotateAboutYAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateAboutYAxis.addToSet( HandleSet.ROTATION_INTERACTION );
		rotateAboutYAxis.addToGroups( HandleSet.HandleGroup.DEFAULT, HandleSet.HandleGroup.STOOD_UP, HandleSet.HandleGroup.Y_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		rotateAboutYAxis.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Rotate,
				new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS ) );
		rotateAboutYAxis.addCondition( 
				new ManipulationEventCriteria(ManipulationEvent.EventType.Rotate,
				new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS ) );
		this.manipulationEventManager.addManipulationListener( rotateAboutYAxis );

		this.handleManager.addHandle( rotateAboutYAxis );
		
		ManipulationHandleIndirection rotateAboutXAxis = new ManipulationHandleIndirection( new RotationRingHandle(MovementDirection.LEFT) );
		rotateAboutXAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateAboutXAxis.addToSet( HandleSet.ROTATION_INTERACTION );
		rotateAboutXAxis.addToGroups( HandleSet.HandleGroup.X_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		this.handleManager.addHandle( rotateAboutXAxis );
		
		ManipulationHandleIndirection rotateAboutZAxis = new ManipulationHandleIndirection(new RotationRingHandle(MovementDirection.BACKWARD));
		rotateAboutZAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateAboutZAxis.addToSet( HandleSet.ROTATION_INTERACTION );
		rotateAboutZAxis.addToGroups( HandleSet.HandleGroup.Z_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		this.handleManager.addHandle( rotateAboutZAxis );
		
		ManipulationHandleIndirection translateUp = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP), Color4f.GREEN));
		ManipulationHandleIndirection translateDown = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP), Color4f.GREEN));
		translateUp.setManipulation( new LinearDragManipulator() );
		translateUp.addToGroups( HandleSet.HandleGroup.TRANSLATION, HandleSet.HandleGroup.STOOD_UP, HandleSet.HandleGroup.Y_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		translateDown.addToGroups( HandleSet.HandleGroup.TRANSLATION, HandleSet.HandleGroup.STOOD_UP, HandleSet.HandleGroup.Y_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		translateUp.addToGroup( HandleSet.HandleGroup.INTERACTION );
		translateUp.addToGroup( HandleSet.HandleGroup.VISUALIZATION );
		translateDown.addToGroup( HandleSet.HandleGroup.VISUALIZATION );
		translateDown.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS ) );
		translateUp.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS ) );
		translateDown.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.DOWN, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS ) );
		translateUp.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.UP, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS ) );
		this.manipulationEventManager.addManipulationListener( translateUp );
		this.manipulationEventManager.addManipulationListener( translateDown );
		this.handleManager.addHandle(translateUp);
		this.handleManager.addHandle(translateDown);
		
		ManipulationHandleIndirection translateXAxisRight = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.RIGHT, MovementType.ABSOLUTE), Color4f.RED));
		ManipulationHandleIndirection translateXAxisLeft = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.LEFT, MovementType.ABSOLUTE), Color4f.RED));
		translateXAxisLeft.setManipulation( new LinearDragManipulator() );
		//Add the left handle to the group to be shown by the system
		translateXAxisLeft.addToGroups( HandleSet.HandleGroup.TRANSLATION, HandleSet.HandleGroup.STOOD_UP, HandleSet.HandleGroup.X_AXIS, HandleSet.HandleGroup.X_AND_Z_AXIS );
		translateXAxisRight.addToGroups( HandleSet.HandleGroup.TRANSLATION, HandleSet.HandleGroup.STOOD_UP, HandleSet.HandleGroup.X_AXIS, HandleSet.HandleGroup.X_AND_Z_AXIS );
		translateXAxisLeft.addToGroup( HandleSet.HandleGroup.INTERACTION );
		translateXAxisLeft.addToGroup( HandleSet.HandleGroup.VISUALIZATION );
		translateXAxisRight.addToGroup( HandleSet.HandleGroup.VISUALIZATION );
		translateXAxisLeft.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.LEFT, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS ) );
		translateXAxisRight.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.RIGHT, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS ) );
		translateXAxisLeft.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.LEFT, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS ) );
		translateXAxisRight.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.RIGHT, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS ) );
		this.manipulationEventManager.addManipulationListener( translateXAxisRight );
		this.manipulationEventManager.addManipulationListener( translateXAxisLeft );
		this.handleManager.addHandle( translateXAxisRight );
		this.handleManager.addHandle( translateXAxisLeft );
		
		ManipulationHandleIndirection translateForward = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.FORWARD, MovementType.ABSOLUTE), Color4f.BLUE));
		ManipulationHandleIndirection translateBackward = new ManipulationHandleIndirection(new LinearTranslateHandle(new MovementDescription(MovementDirection.BACKWARD, MovementType.ABSOLUTE), Color4f.BLUE));
		translateForward.setManipulation( new LinearDragManipulator() );
		translateForward.addToGroups( HandleSet.HandleGroup.TRANSLATION, HandleSet.HandleGroup.STOOD_UP, HandleSet.HandleGroup.Z_AXIS, HandleSet.HandleGroup.X_AND_Z_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		translateBackward.addToGroups( HandleSet.HandleGroup.TRANSLATION, HandleSet.HandleGroup.STOOD_UP, HandleSet.HandleGroup.Z_AXIS, HandleSet.HandleGroup.X_AND_Z_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		translateForward.addToGroup( HandleSet.HandleGroup.INTERACTION );
		translateForward.addToGroup( HandleSet.HandleGroup.VISUALIZATION );
		translateBackward.addToGroup( HandleSet.HandleGroup.VISUALIZATION );
		translateBackward.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.BACKWARD, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS ) );
		translateForward.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.FORWARD, MovementType.STOOD_UP),
				PickHint.MOVEABLE_OBJECTS ) );
		translateBackward.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.BACKWARD, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS ) );
		translateForward.addCondition( new ManipulationEventCriteria(
				ManipulationEvent.EventType.Translate,
				new MovementDescription(MovementDirection.FORWARD, MovementType.ABSOLUTE),
				PickHint.MOVEABLE_OBJECTS ) );
		this.manipulationEventManager.addManipulationListener( translateForward );
		this.manipulationEventManager.addManipulationListener( translateBackward );
		this.handleManager.addHandle( translateForward );
		this.handleManager.addHandle( translateBackward );
		
		ManipulationHandleIndirection scaleAxis = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.RESIZE, MovementType.STOOD_UP), Color4f.PINK));
		scaleAxis.setManipulation( new ScaleDragManipulator() );
		scaleAxis.addToSet( HandleSet.RESIZE_INTERACTION );
		scaleAxis.addToGroups( HandleSet.HandleGroup.RESIZE_AXIS, HandleSet.HandleGroup.VISUALIZATION );
		this.handleManager.addHandle( scaleAxis );
		
		ManipulationHandleIndirection scaleAxisX = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.RIGHT, MovementType.STOOD_UP), Color4f.MAGENTA, true));
		scaleAxisX.setManipulation( new ScaleDragManipulator() );
		scaleAxisX.addToSet( HandleSet.RESIZE_INTERACTION );
		scaleAxisX.addToGroups( HandleSet.HandleGroup.X_AXIS, HandleSet.HandleGroup.VISUALIZATION );
		this.handleManager.addHandle( scaleAxisX );
		
		ManipulationHandleIndirection scaleAxisY = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP), Color4f.YELLOW, true));
		scaleAxisY.setManipulation( new ScaleDragManipulator() );
		scaleAxisY.addToSet( HandleSet.RESIZE_INTERACTION );
		scaleAxisY.addToGroups( HandleSet.HandleGroup.Y_AXIS, HandleSet.HandleGroup.VISUALIZATION );
		this.handleManager.addHandle( scaleAxisY );
		
		ManipulationHandleIndirection scaleAxisZ = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.FORWARD, MovementType.STOOD_UP), Color4f.CYAN, true));
		scaleAxisZ.setManipulation( new ScaleDragManipulator() );
		scaleAxisZ.addToSet( HandleSet.RESIZE_INTERACTION );
		scaleAxisZ.addToGroups( HandleSet.HandleGroup.Z_AXIS, HandleSet.HandleGroup.VISUALIZATION );
		this.handleManager.addHandle( scaleAxisZ );
		
		this.setHandleSet( HandleSet.DEFAULT_INTERACTION );
	}
	
	
}
