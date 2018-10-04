/*******************************************************************************
 * Copyright (c) 2018 Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.modelviewer;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import org.alice.interact.DragAdapter;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementKey;
import org.alice.interact.MovementType;
import org.alice.interact.PickHint;
import org.alice.interact.condition.KeyPressCondition;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.CameraOrbitDragManipulator;
import org.alice.interact.manipulator.CameraOrbitKeyManipulator;
import org.alice.interact.manipulator.CameraTranslateKeyManipulator;
import org.lgna.story.implementation.AbstractTransformableImp;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class SingleViewerDragAdapter extends DragAdapter {
	SingleViewerDragAdapter() {
		addDragControl();
		addZoomKeyControl();
		addCameraKeyControl();

		for( ManipulatorConditionSet manipulatorConditionSet : getManipulatorConditionSets() ) {
			manipulatorConditionSet.getManipulator().setDragAdapter( this );
		}
	}

	private void addDragControl() {
		MouseDragCondition leftClick = new MouseDragCondition( MouseEvent.BUTTON1, new PickCondition( PickHint.getAnythingHint() ) );
		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( new CameraOrbitDragManipulator() );
		cameraOrbit.addCondition( leftClick );
		addManipulatorConditionSet( cameraOrbit );
	}

	private void addZoomKeyControl() {
		MovementKey[] zoomKeys = {
				//Zoom out
				new MovementKey( KeyEvent.VK_MINUS, new MovementDescription( MovementDirection.BACKWARD, MovementType.LOCAL ) ),
				new MovementKey( KeyEvent.VK_SUBTRACT, new MovementDescription( MovementDirection.BACKWARD, MovementType.LOCAL) ),
				//Zoom in
				new MovementKey( KeyEvent.VK_EQUALS, new MovementDescription( MovementDirection.FORWARD, MovementType.LOCAL ) ),
				new MovementKey( KeyEvent.VK_ADD, new MovementDescription( MovementDirection.FORWARD, MovementType.LOCAL ) ),
		};
		CameraTranslateKeyManipulator cameraTranslateManip = new CameraTranslateKeyManipulator( zoomKeys );
		ManipulatorConditionSet cameraTranslate = new ManipulatorConditionSet( cameraTranslateManip );
		for( MovementKey zoomKey : zoomKeys ) {
			cameraTranslate.addCondition( new KeyPressCondition( zoomKey.keyValue ) );
		}
		addManipulatorConditionSet( cameraTranslate );
	}

	private void addCameraKeyControl() {
		// Directions are at right angles to expected motion. Orbit uses a cross product to get the axis of rotation.
		MovementKey[] movementKeys = {
				//Up
				new MovementKey( KeyEvent.VK_PAGE_UP, new MovementDescription( MovementDirection.RIGHT, MovementType.ABSOLUTE ), 10d ),
				new MovementKey( KeyEvent.VK_UP, new MovementDescription( MovementDirection.RIGHT, MovementType.ABSOLUTE ), 10d ),
				//Down
				new MovementKey( KeyEvent.VK_PAGE_DOWN, new MovementDescription( MovementDirection.LEFT, MovementType.ABSOLUTE ), 10d ),
				new MovementKey( KeyEvent.VK_DOWN, new MovementDescription( MovementDirection.LEFT, MovementType.ABSOLUTE ), 10d ),
				//Left
				new MovementKey( KeyEvent.VK_OPEN_BRACKET, new MovementDescription( MovementDirection.UP, MovementType.ABSOLUTE ), 10d ),
				new MovementKey( KeyEvent.VK_LEFT, new MovementDescription( MovementDirection.UP, MovementType.ABSOLUTE ), 10d ),
				//Right
				new MovementKey( KeyEvent.VK_CLOSE_BRACKET, new MovementDescription( MovementDirection.DOWN, MovementType.ABSOLUTE ), 10d ),
				new MovementKey( KeyEvent.VK_RIGHT, new MovementDescription( MovementDirection.DOWN, MovementType.ABSOLUTE ), 10d ),
		};

		CameraOrbitKeyManipulator orbitKeyManipulator = new CameraOrbitKeyManipulator( movementKeys );
		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( orbitKeyManipulator );
		for( MovementKey movementKey : movementKeys ) {
			cameraOrbit.addCondition( new KeyPressCondition( movementKey.keyValue ) );
		}
		addManipulatorConditionSet( cameraOrbit );
	}

	@Override
	protected void updateHandleSelection( AbstractTransformableImp selected ) {
	}

	@Override
	public void setSGCamera( AbstractCamera camera )
	{
		super.setSGCamera( camera );
		AxisAlignedBox cameraBounds = new AxisAlignedBox();
		Vector3 cameraBackwards = camera.getAbsoluteTransformation().orientation.backward;

		Point3 cameraMin = new Point3( camera.getAbsoluteTransformation().translation );
		Point3 cameraMax = new Point3( cameraMin );
		double originalY = cameraMin.y;
		cameraMin.add( Vector3.createMultiplication( cameraBackwards, 1.5d ) );
		cameraMin.y = .25d;
		cameraMax.subtract( Vector3.createMultiplication( cameraBackwards, 4.5d ) );
		cameraMax.y = originalY + 1.5d;
		cameraBounds.setMinimum( cameraMin );
		cameraBounds.setMaximum( cameraMax );
	}

	@Override
	protected void handleMouseMoved( MouseEvent e ) {
		//Overridden to prevent picking every frame since there is no need for rollover events
		currentInputState.setMouseLocation( e.getPoint() );
		fireStateChange();
	}

	@Override
	public void undoRedoEndManipulation( AbstractManipulator manipulator, AffineMatrix4x4 originalTransformation ) {
	}
}
