/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.interact;

import java.awt.event.MouseEvent;

import org.alice.interact.ModifierMask.ModifierKey;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.manipulator.CameraMoveDragManipulator;
import org.alice.interact.manipulator.CameraOrbitDragManipulator;
import org.alice.interact.manipulator.CameraPanDragManipulator;
import org.alice.interact.manipulator.CameraTiltDragManipulator;
import org.alice.interact.manipulator.HandlelessObjectRotateDragManipulator;
import org.alice.interact.manipulator.ObjectTranslateDragManipulator;
import org.alice.interact.manipulator.ObjectUpDownDragManipulator;
import org.lgna.story.implementation.AbstractTransformableImp;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author David Culyba
 */
public class RuntimeDragAdapter extends AbstractDragAdapter {
	public RuntimeDragAdapter() {
		this.setUpControls();
	}

	private void setUpControls() {
		ManipulatorConditionSet mouseTranslateObject = new ManipulatorConditionSet( new ObjectTranslateDragManipulator() );
		MouseDragCondition moveableObject = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.PickType.MOVEABLE.pickHint() ), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ) );
		mouseTranslateObject.addCondition( moveableObject );
		this.addManipulatorConditionSet( mouseTranslateObject );

		ManipulatorConditionSet mouseUpDownTranslateObject = new ManipulatorConditionSet( new ObjectUpDownDragManipulator() );
		MouseDragCondition moveableObjectWithShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.PickType.MOVEABLE.pickHint() ), new ModifierMask( ModifierKey.SHIFT ) );
		mouseUpDownTranslateObject.addCondition( moveableObjectWithShift );
		this.addManipulatorConditionSet( mouseUpDownTranslateObject );

		ManipulatorConditionSet mouseRotateObjectLeftRight = new ManipulatorConditionSet( new HandlelessObjectRotateDragManipulator( MovementDirection.UP ) );
		MouseDragCondition moveableObjectWithCtrl = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.PickType.TURNABLE.pickHint() ), new ModifierMask( ModifierKey.CONTROL ) );
		mouseRotateObjectLeftRight.addCondition( moveableObjectWithCtrl );
		this.addManipulatorConditionSet( mouseRotateObjectLeftRight );

		//Camera mouse control
		MouseDragCondition leftAndNoModifiers = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.getNonInteractiveHint() ), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ) );
		MouseDragCondition leftAndShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.getNonInteractiveHint() ), new ModifierMask( ModifierMask.JUST_SHIFT ) );
		MouseDragCondition leftAndControl = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.getNonInteractiveHint() ), new ModifierMask( ModifierMask.JUST_CONTROL ) );
		MouseDragCondition middleMouseAndAnything = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON2, new PickCondition( PickHint.getAnythingHint() ) );
		MouseDragCondition rightMouseAndNonInteractive = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON3, new PickCondition( PickHint.getNonInteractiveHint() ) );

		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( new CameraOrbitDragManipulator() );
		//		cameraOrbit.addCondition(rightMouseAndNonInteractive);
		cameraOrbit.addCondition( middleMouseAndAnything );
		this.addManipulatorConditionSet( cameraOrbit );

		ManipulatorConditionSet cameraTilt = new ManipulatorConditionSet( new CameraTiltDragManipulator() );
		cameraTilt.addCondition( rightMouseAndNonInteractive );
		cameraTilt.addCondition( leftAndControl );
		this.addManipulatorConditionSet( cameraTilt );

		ManipulatorConditionSet cameraMouseTranslate = new ManipulatorConditionSet( new CameraMoveDragManipulator() );
		cameraMouseTranslate.addCondition( leftAndNoModifiers );
		this.addManipulatorConditionSet( cameraMouseTranslate );

		ManipulatorConditionSet cameraMousePan = new ManipulatorConditionSet( new CameraPanDragManipulator() );
		cameraMousePan.addCondition( leftAndShift );
		this.addManipulatorConditionSet( cameraMousePan );

		for( ManipulatorConditionSet manipulatorConditionSet : this.getManipulatorConditionSets() ) {
			manipulatorConditionSet.getManipulator().setDragAdapter( this );
		}
	}

	@Override
	protected void updateHandleSelection( AbstractTransformableImp selected ) {
	}

	@Override
	protected void handleMouseEntered( MouseEvent e ) {
		//Overridden to do nothing
	}

	@Override
	protected void handleMouseMoved( java.awt.event.MouseEvent e ) {
		//Overridden to prevent picking every frame since there is no need for rollover events
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.fireStateChange();
	}

	@Override
	public boolean shouldSnapToRotation() {
		return false;
	}

	@Override
	public boolean shouldSnapToGround() {
		return false;
	}

	@Override
	public boolean shouldSnapToGrid() {
		return false;
	}

	@Override
	public double getGridSpacing() {
		return 1.0;
	}

	@Override
	public edu.cmu.cs.dennisc.math.Angle getRotationSnapAngle() {
		return new edu.cmu.cs.dennisc.math.AngleInRadians( Math.PI / 16.0 );
	}

	@Override
	public void undoRedoEndManipulation( org.alice.interact.manipulator.AbstractManipulator manipulator, AffineMatrix4x4 originalTransformation ) {
	}

}
