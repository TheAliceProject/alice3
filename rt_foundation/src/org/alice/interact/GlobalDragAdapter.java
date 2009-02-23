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

import org.alice.interact.ModifierMask.ModifierKey;
import org.alice.interact.event.SelectionEvent;
import org.alice.interact.event.SelectionListener;


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
public class GlobalDragAdapter extends AbstractDragAdapter {
	
	@Override
	protected void setUpControls()
	{
		MovementKey[] movementKeys = {
				//Forward
//				new MovementKey(KeyEvent.VK_UP, new Point3(0, 0, -1)),
//				new MovementKey(KeyEvent.VK_NUMPAD8, new Point3(0, 0, -1)),
				new MovementKey(KeyEvent.VK_W, new Point3(0, 0, -1)),
				//Backward
//				new MovementKey(KeyEvent.VK_DOWN, new Point3(0, 0, 1)),
//				new MovementKey(KeyEvent.VK_NUMPAD2, new Point3(0, 0, 1)),
				new MovementKey(KeyEvent.VK_S, new Point3(0, 0, 1)),
				//Left
//				new MovementKey(KeyEvent.VK_LEFT, new Point3(-1, 0, 0)),
//				new MovementKey(KeyEvent.VK_NUMPAD4, new Point3(-1, 0, 0)),
				new MovementKey(KeyEvent.VK_A, new Point3(-1, 0, 0)),
				//Right
//				new MovementKey(KeyEvent.VK_RIGHT, new Point3(1, 0, 0)),
//				new MovementKey(KeyEvent.VK_NUMPAD6, new Point3(1, 0, 0)),
				new MovementKey(KeyEvent.VK_D, new Point3(1, 0, 0)),
				//Up
				new MovementKey(KeyEvent.VK_PAGE_UP, new Point3(0, 1, 0), MovementType.LOCAL, .5d),
				//Down
				new MovementKey(KeyEvent.VK_PAGE_DOWN, new Point3(0, -1, 0), MovementType.LOCAL, .5d),
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
		this.currentHandleSet = this.handleSets.lastElement();
		
		RotationRingHandle rotateAboutYAxis = new StoodUpRotationRingHandle(Vector3.accessPositiveYAxis(), RotationRingHandle.HandlePosition.BOTTOM );
		RotationRingHandle rotateAboutYAxis2 = new StoodUpRotationRingHandle(Vector3.accessPositiveYAxis(), RotationRingHandle.HandlePosition.BOTTOM );
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
		
		LinearTranslateHandle translateYAxis = new StoodUpLinearTranslateHandle(Vector3.accessPositiveYAxis(), Color4f.GREEN);
		translateYAxis.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP);
		currentManipulationHandles.add( translateYAxis );
		LinearTranslateHandle translateYAxis2 = new StoodUpLinearTranslateHandle(Vector3.accessPositiveYAxis(), Color4f.GREEN);
		translateYAxis2.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		nextManipulationHandles.add( translateYAxis2 );
		
		LinearTranslateHandle translateXAxis = new StoodUpLinearTranslateHandle(Vector3.accessNegativeXAxis(), Color4f.RED);
		translateXAxis.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		currentManipulationHandles.add( translateXAxis );
		LinearTranslateHandle translateXAxis2 = new StoodUpLinearTranslateHandle(Vector3.accessNegativeXAxis(), Color4f.RED);
		translateXAxis2.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		nextManipulationHandles.add( translateXAxis2 );
		
		LinearTranslateHandle translateZAxis = new StoodUpLinearTranslateHandle(Vector3.accessNegativeZAxis(), Color4f.BLUE);
		translateZAxis.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		currentManipulationHandles.add( translateZAxis );
		LinearTranslateHandle translateZAxis2 = new StoodUpLinearTranslateHandle(Vector3.accessNegativeZAxis(), Color4f.BLUE);
		translateZAxis2.addToGroups( HandleGroup.TRANSLATION, HandleGroup.STOOD_UP );
		nextManipulationHandles.add( translateZAxis2 );
		
		Vector3 uniformScaleDirection = new Vector3(-1.0d, 1.0d, 0.0d);
		uniformScaleDirection.normalize();
		LinearScaleHandle scaleAxis = new LinearScaleHandle(uniformScaleDirection, Color4f.PINK);
		scaleAxis.addToGroups( HandleGroup.RESIZE );
		currentManipulationHandles.add( scaleAxis );
		nextManipulationHandles.add(  new LinearScaleHandle(scaleAxis) );
		
		LinearScaleHandle scaleAxisX = new LinearScaleHandle(Vector3.accessNegativeXAxis(), Color4f.MAGENTA, true);
		scaleAxisX.addToGroups( HandleGroup.RESIZE );
		currentManipulationHandles.add( scaleAxisX );
		nextManipulationHandles.add(  new LinearScaleHandle(scaleAxisX) );
		
		LinearScaleHandle scaleAxisY = new LinearScaleHandle(Vector3.accessPositiveYAxis(), Color4f.YELLOW, true);
		scaleAxisY.addToGroups( HandleGroup.RESIZE );
		currentManipulationHandles.add( scaleAxisY );
		nextManipulationHandles.add(  new LinearScaleHandle(scaleAxisY) );
		
		LinearScaleHandle scaleAxisZ = new LinearScaleHandle(Vector3.accessNegativeZAxis(), Color4f.CYAN, true);
		scaleAxisZ.addToGroups( HandleGroup.RESIZE );
		currentManipulationHandles.add( scaleAxisZ );
		nextManipulationHandles.add(  new LinearScaleHandle(scaleAxisZ) );
	}
	
	
}
