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

package org.alice.interact;
import java.awt.event.KeyEvent;

import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.alice.interact.ModifierMask.ModifierKey;
import org.alice.interact.condition.AndInputCondition;
import org.alice.interact.condition.DoubleClickedObjectCondition;
import org.alice.interact.condition.DragAndDropCondition;
import org.alice.interact.condition.InvertedSelectedObjectCondition;
import org.alice.interact.condition.KeyPressCondition;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseCondition;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.MousePressCondition;
import org.alice.interact.condition.MouseWheelCondition;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.condition.SelectedObjectCondition;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.LinearScaleHandle;
import org.alice.interact.handle.LinearTranslateHandle;
import org.alice.interact.handle.ManipulationHandleIndirection;
import org.alice.interact.handle.RotationRingHandle;
import org.alice.interact.handle.StoodUpRotationRingHandle;
import org.alice.interact.manipulator.CameraMoveDragManipulator;
import org.alice.interact.manipulator.CameraOrbitDragManipulator;
import org.alice.interact.manipulator.CameraPanDragManipulator;
import org.alice.interact.manipulator.CameraRotateKeyManipulator;
import org.alice.interact.manipulator.CameraTiltDragManipulator;
import org.alice.interact.manipulator.CameraTranslateKeyManipulator;
import org.alice.interact.manipulator.CameraZoomMouseWheelManipulator;
import org.alice.interact.manipulator.GetAGoodLookAtManipulator;
import org.alice.interact.manipulator.HandlelessObjectRotateDragManipulator;
import org.alice.interact.manipulator.LinearDragManipulator;
import org.alice.interact.manipulator.ObjectGlobalHandleDragManipulator;
import org.alice.interact.manipulator.ObjectRotateDragManipulator;
import org.alice.interact.manipulator.ObjectTranslateKeyManipulator;
import org.alice.interact.manipulator.ObjectUpDownDragManipulator;
import org.alice.interact.manipulator.OmniDirectionalBoundingBoxManipulator;
import org.alice.interact.manipulator.OmniDirectionalDragManipulator;
import org.alice.interact.manipulator.ResizeDragManipulator;
import org.alice.interact.manipulator.ScaleDragManipulator;
import org.alice.interact.manipulator.SelectObjectDragManipulator;
import org.alice.interact.manipulator.TargetManipulator;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author David Culyba
 */
public class GlobalDragAdapter extends AbstractDragAdapter {
	
	TargetManipulator dropTargetManipulator;
	
	public GlobalDragAdapter( AbstractSceneEditor sceneEditor )
	{
		super(sceneEditor);
	}
	
	public GlobalDragAdapter()
	{
		super();
	}
	
	public org.alice.stageide.croquet.models.sceneditor.HandleStyleListSelectionState getInteractionSelectionStateList() {
		return org.alice.stageide.croquet.models.sceneditor.HandleStyleListSelectionState.getInstance();
	}
	@Override
	protected void setUpControls()
	{
		MovementKey[] movementKeys = {
				//Forward
				new MovementKey(KeyEvent.VK_UP, new MovementDescription(MovementDirection.FORWARD)),
//				new MovementKey(KeyEvent.VK_NUMPAD8, new MovementDescription(MovementDirection.FORWARD)),
//				new MovementKey(KeyEvent.VK_W, new MovementDescription(MovementDirection.FORWARD)),
				//Backward
				new MovementKey(KeyEvent.VK_DOWN, new MovementDescription(MovementDirection.BACKWARD)),
//				new MovementKey(KeyEvent.VK_NUMPAD2, new MovementDescription(MovementDirection.BACKWARD)),
//				new MovementKey(KeyEvent.VK_S, new MovementDescription(MovementDirection.BACKWARD)),
				//Left
				new MovementKey(KeyEvent.VK_LEFT, new MovementDescription(MovementDirection.LEFT)),
//				new MovementKey(KeyEvent.VK_NUMPAD4, new MovementDescription(MovementDirection.LEFT)),
//				new MovementKey(KeyEvent.VK_A, new MovementDescription(MovementDirection.LEFT)),
				//Right
				new MovementKey(KeyEvent.VK_RIGHT, new MovementDescription(MovementDirection.RIGHT)),
//				new MovementKey(KeyEvent.VK_NUMPAD6, new MovementDescription(MovementDirection.RIGHT)),
//				new MovementKey(KeyEvent.VK_D,  new MovementDescription(MovementDirection.RIGHT)),
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
		this.manipulators.add( cameraRotate );
	
		//Camera mouse control
		MouseDragCondition leftAndNoModifiers = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1 , new PickCondition( PickHint.NON_INTERACTIVE ), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		MouseDragCondition leftAndShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1 , new PickCondition( PickHint.NON_INTERACTIVE ), new ModifierMask( ModifierMask.JUST_SHIFT ));
		MouseDragCondition leftAndControl = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1 , new PickCondition( PickHint.NON_INTERACTIVE ), new ModifierMask( ModifierMask.JUST_CONTROL ));
		MouseDragCondition middleMouseAndAnything = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON2 , new PickCondition( PickHint.EVERYTHING ) );
		MouseDragCondition rightMouseAndNonInteractive = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON3 , new PickCondition( PickHint.NON_INTERACTIVE ) );
		
		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( new CameraOrbitDragManipulator() );
//		cameraOrbit.addCondition(rightMouseAndNonInteractive);
		cameraOrbit.addCondition(middleMouseAndAnything);
		this.manipulators.add(cameraOrbit); 
		
		ManipulatorConditionSet cameraTilt = new ManipulatorConditionSet( new CameraTiltDragManipulator() );
		cameraTilt.addCondition(rightMouseAndNonInteractive);
		cameraTilt.addCondition(leftAndControl);
		this.manipulators.add(cameraTilt);
		
		ManipulatorConditionSet cameraMouseTranslate = new ManipulatorConditionSet( new CameraMoveDragManipulator() );
		cameraMouseTranslate.addCondition(leftAndNoModifiers);
		this.manipulators.add(cameraMouseTranslate);
		
		ManipulatorConditionSet cameraMousePan = new ManipulatorConditionSet( new CameraPanDragManipulator() );
		cameraMousePan.addCondition(leftAndShift);
		this.manipulators.add(cameraMousePan);
		
		
		//Object Manipulation
		OmniDirectionalBoundingBoxManipulator boundingBoxManipulator = new OmniDirectionalBoundingBoxManipulator();
		this.dropTargetManipulator = boundingBoxManipulator;
		ManipulatorConditionSet dragFromGallery = new ManipulatorConditionSet( boundingBoxManipulator, "Bounding Box Translate" );
		dragFromGallery.addCondition( new DragAndDropCondition() );
		this.manipulators.add( dragFromGallery );
		
		ManipulatorConditionSet leftClickMouseTranslateObject = new ManipulatorConditionSet( new OmniDirectionalDragManipulator(), "Mouse Translate" );
		MouseDragCondition leftClickMoveableObjects = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		leftClickMouseTranslateObject.addCondition( leftClickMoveableObjects );
		this.manipulators.add( leftClickMouseTranslateObject );
		
		ManipulatorConditionSet leftClickMouseRotateObjectLeftRight = new ManipulatorConditionSet( new HandlelessObjectRotateDragManipulator(MovementDirection.UP) );
		leftClickMouseRotateObjectLeftRight.addCondition( leftClickMoveableObjects );
		//This manipulation is used only when the "rotation" interaction group is selected. Disabled by default.
		leftClickMouseRotateObjectLeftRight.setEnabled(false);
		this.manipulators.add( leftClickMouseRotateObjectLeftRight );
		
		ManipulatorConditionSet leftClickMouseResizeObject = new ManipulatorConditionSet( new ResizeDragManipulator() );
		leftClickMouseResizeObject.addCondition( leftClickMoveableObjects );
		//This manipulation is used only when the "rotation" interaction group is selected. Disabled by default.
		leftClickMouseResizeObject.setEnabled(false);
		this.manipulators.add( leftClickMouseResizeObject );
		
		ManipulatorConditionSet mouseUpDownTranslateObject = new ManipulatorConditionSet( new ObjectUpDownDragManipulator() );
		MouseDragCondition moveableObjectWithShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierKey.SHIFT ));
		mouseUpDownTranslateObject.addCondition( moveableObjectWithShift );
		this.manipulators.add( mouseUpDownTranslateObject );
		
		ManipulatorConditionSet mouseRotateObjectLeftRight = new ManipulatorConditionSet( new HandlelessObjectRotateDragManipulator(MovementDirection.UP) );
		MouseDragCondition moveableObjectWithCtrl = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS), new ModifierMask( ModifierKey.CONTROL ));
		mouseRotateObjectLeftRight.addCondition( moveableObjectWithCtrl );
		this.manipulators.add( mouseRotateObjectLeftRight );
		
		ManipulatorConditionSet mouseHandleDrag = new ManipulatorConditionSet( new ObjectGlobalHandleDragManipulator() );
		MouseDragCondition handleObjectCondition = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.THREE_D_HANDLES), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		MouseCondition handleObjectClickCondition = new MouseCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.TWO_D_HANDLES), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		mouseHandleDrag.addCondition( handleObjectCondition );
		mouseHandleDrag.addCondition( handleObjectClickCondition );
		this.manipulators.add( mouseHandleDrag );
		
//		ManipulatorConditionSet mouseHandleClick = new ManipulatorConditionSet( new ObjectGlobalHandleDragManipulator() );
//		MousePressCondition handleObjectClickCondition = new MousePressCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.TWO_D_HANDLES));
//		System.out.println("Looking for condition: "+handleObjectCondition.hashCode()+", in set "+mouseHandleClick.hashCode());
//		mouseHandleClick.addCondition( handleObjectClickCondition );
//		this.manipulators.add( mouseHandleClick );
		
		ManipulatorConditionSet selectObject = new ManipulatorConditionSet( new SelectObjectDragManipulator(this) );
		selectObject.addCondition( new MousePressCondition(java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.EVERYTHING)) );
		this.manipulators.add( selectObject );
		
		ManipulatorConditionSet getAGoodLookAtObject = new ManipulatorConditionSet( new GetAGoodLookAtManipulator());
		getAGoodLookAtObject.addCondition( new DoubleClickedObjectCondition(java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.MOVEABLE_OBJECTS)) );
		this.manipulators.add( getAGoodLookAtObject );
		
		ManipulatorConditionSet mouseWheelCameraZoom = new ManipulatorConditionSet( new CameraZoomMouseWheelManipulator() );
		MouseWheelCondition mouseWheelCondition = new MouseWheelCondition(new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ));
		mouseWheelCameraZoom.addCondition( mouseWheelCondition );
		this.manipulators.add( mouseWheelCameraZoom );
		
		
		
		
		
		for (int i=0; i<this.manipulators.size(); i++)
		{
			this.manipulators.get( i ).getManipulator().setDragAdapter( this );
		}
		
		
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
		rotateAboutYAxis.setDragAdapterAndAddHandle( this );
		
		ManipulationHandleIndirection rotateAboutXAxis = new ManipulationHandleIndirection( new RotationRingHandle(MovementDirection.LEFT) );
		rotateAboutXAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateAboutXAxis.addToSet( HandleSet.ROTATION_INTERACTION );
		rotateAboutXAxis.addToGroups( HandleSet.HandleGroup.X_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		rotateAboutXAxis.setDragAdapterAndAddHandle( this );
		
		ManipulationHandleIndirection rotateAboutZAxis = new ManipulationHandleIndirection(new RotationRingHandle(MovementDirection.BACKWARD));
		rotateAboutZAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateAboutZAxis.addToSet( HandleSet.ROTATION_INTERACTION );
		rotateAboutZAxis.addToGroups( HandleSet.HandleGroup.Z_AXIS, HandleSet.HandleGroup.VISUALIZATION);
		rotateAboutZAxis.setDragAdapterAndAddHandle( this );
		
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
		translateDown.setDragAdapterAndAddHandle( this );
		translateUp.setDragAdapterAndAddHandle( this );
		
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
		translateXAxisRight.setDragAdapterAndAddHandle( this );
		translateXAxisLeft.setDragAdapterAndAddHandle( this );
		
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
		translateForward.setDragAdapterAndAddHandle( this );
		translateBackward.setDragAdapterAndAddHandle( this );
		
		ManipulationHandleIndirection scaleAxis = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.RESIZE, MovementType.STOOD_UP), Color4f.PINK));
		scaleAxis.setManipulation( new ScaleDragManipulator() );
		scaleAxis.addToSet( HandleSet.RESIZE_INTERACTION );
		scaleAxis.addToGroups( HandleSet.HandleGroup.RESIZE_AXIS, HandleSet.HandleGroup.VISUALIZATION );
		scaleAxis.setDragAdapterAndAddHandle( this );
		
		ManipulationHandleIndirection scaleAxisX = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.RIGHT, MovementType.STOOD_UP), Color4f.MAGENTA, true));
		scaleAxisX.setManipulation( new ScaleDragManipulator() );
		scaleAxisX.addToSet( HandleSet.RESIZE_INTERACTION );
		scaleAxisX.addToGroups( HandleSet.HandleGroup.X_AXIS, HandleSet.HandleGroup.VISUALIZATION );
		scaleAxisX.setDragAdapterAndAddHandle( this );
		
		ManipulationHandleIndirection scaleAxisY = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP), Color4f.YELLOW, true));
		scaleAxisY.setManipulation( new ScaleDragManipulator() );
		scaleAxisY.addToSet( HandleSet.RESIZE_INTERACTION );
		scaleAxisY.addToGroups( HandleSet.HandleGroup.Y_AXIS, HandleSet.HandleGroup.VISUALIZATION );
		scaleAxisY.setDragAdapterAndAddHandle( this );
		
		ManipulationHandleIndirection scaleAxisZ = new ManipulationHandleIndirection(new LinearScaleHandle(new MovementDescription(MovementDirection.FORWARD, MovementType.STOOD_UP), Color4f.CYAN, true));
		scaleAxisZ.setManipulation( new ScaleDragManipulator() );
		scaleAxisZ.addToSet( HandleSet.RESIZE_INTERACTION );
		scaleAxisZ.addToGroups( HandleSet.HandleGroup.Z_AXIS, HandleSet.HandleGroup.VISUALIZATION );
		scaleAxisZ.setDragAdapterAndAddHandle( this );

		
		if (this.sceneEditor != null)
		{
			InteractionGroup defaultInteraction = new InteractionGroup(HandleSet.DEFAULT_INTERACTION, leftClickMouseTranslateObject);
			InteractionGroup rotationInteraction = new InteractionGroup(HandleSet.ROTATION_INTERACTION, leftClickMouseRotateObjectLeftRight);
			InteractionGroup translationInteraction = new InteractionGroup(HandleSet.TRANSLATION_INTERACTION, leftClickMouseTranslateObject);
			InteractionGroup resizeInteraction = new InteractionGroup(HandleSet.RESIZE_INTERACTION, leftClickMouseResizeObject);
			
			this.mapHandleStyleToInteractionGroup.put( org.alice.stageide.sceneeditor.HandleStyle.DEFAULT, defaultInteraction );
			this.mapHandleStyleToInteractionGroup.put( org.alice.stageide.sceneeditor.HandleStyle.ROTATION, rotationInteraction );
			this.mapHandleStyleToInteractionGroup.put( org.alice.stageide.sceneeditor.HandleStyle.TRANSLATION, translationInteraction );
			this.mapHandleStyleToInteractionGroup.put( org.alice.stageide.sceneeditor.HandleStyle.RESIZE, resizeInteraction );
//			this.interactionSelectionState.addItem(defaultInteraction);
//			this.interactionSelectionState.addItem(rotationInteraction);
//			this.interactionSelectionState.addItem(translationInteraction);
//			this.interactionSelectionState.addItem(resizeInteraction);
//			
//			this.interactionSelectionState.setSelectedItem(defaultInteraction);
			
			org.alice.stageide.croquet.models.sceneditor.HandleStyleListSelectionState.getInstance().addAndInvokeValueObserver(this.handleStateValueObserver);
		}
	}
	
	@Override
	public AffineMatrix4x4 getDropTargetTransformation() 
	{
		return this.dropTargetManipulator.getTargetTransformation();
	}
	
	
	
	
}
