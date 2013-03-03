/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.poser;

import java.awt.event.MouseEvent;
import java.util.List;

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.ModifierMask;
import org.alice.interact.ModifierMask.ModifierKey;
import org.alice.interact.MovementDirection;
import org.alice.interact.PickHint;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.ManipulationHandleIndirection;
import org.alice.interact.manipulator.CameraMoveDragManipulator;
import org.alice.interact.manipulator.CameraOrbitDragManipulator;
import org.alice.interact.manipulator.CameraPanDragManipulator;
import org.alice.interact.manipulator.CameraTiltDragManipulator;
import org.alice.interact.manipulator.ObjectRotateDragManipulator;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class PoserDragAdapter extends AbstractDragAdapter implements PoserSphereManipulatorListener {

	private PoserSphereManipulator manipulator;
	private PoserEvent queuedPoserEvent;
	private List<PoserSphereManipulatorListener> listeners = Collections.newArrayList();

	public PoserDragAdapter() {
		manipulator.addListener( this );
	}

	public void addListener( PoserSphereManipulatorListener listener ) {
		listeners.add( listener );
	}

	@Override
	public void mousePressed( MouseEvent e ) {
		super.mousePressed( e );
	}

	@Override
	public void mouseDragged( MouseEvent e ) {
		if( queuedPoserEvent != null ) {
			fireStartEvents( queuedPoserEvent );
		}
		super.mouseDragged( e );
	}

	@Override
	public void mouseMoved( MouseEvent e ) {
	}

	@Override
	public void mouseReleased( MouseEvent e ) {
		super.mouseReleased( e );
		if( queuedPoserEvent != null ) {
			fireFinishEvents( queuedPoserEvent );
		}
		queuedPoserEvent = null;
	}

	public void fireStartEvents( PoserEvent event ) {
		for( PoserSphereManipulatorListener listener : listeners ) {
			listener.fireStart( event );
		}
	}

	public void fireFinishEvents( PoserEvent event ) {
		for( PoserSphereManipulatorListener listener : listeners ) {
			listener.fireFinish( event );
		}
	}

	public void fireStart( PoserEvent poserEvent ) {
		this.queuedPoserEvent = poserEvent;
	}

	public void fireFinish( PoserEvent poserEvent ) {
		//unused here?
	}

	@Override
	protected void setUpControls() {
		setUpSphereManipulators();
		setUpCameraControls();
		setUpJointRotators();
		for( int i = 0; i < this.manipulators.size(); i++ ) {
			this.manipulators.get( i ).getManipulator().setDragAdapter( this );
		}
	}

	private void setUpSphereManipulators() {
		MouseDragCondition leftClickMoveableObjects = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.PickType.JOINT.pickHint() ), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ) );

		manipulator = new PoserSphereManipulator();
		ManipulatorConditionSet leftClickMouseTranslateObject = new ManipulatorConditionSet( manipulator.getOmni(), "Mouse Translate" );
		leftClickMouseTranslateObject.addCondition( leftClickMoveableObjects );
		this.manipulators.add( leftClickMouseTranslateObject );

		ManipulatorConditionSet mouseUpDownTranslateObject = new ManipulatorConditionSet( manipulator.getUppy() );
		MouseDragCondition moveableObjectWithShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.PickType.MOVEABLE.pickHint() ), new ModifierMask( ModifierKey.SHIFT ) );
		mouseUpDownTranslateObject.addCondition( moveableObjectWithShift );
		this.manipulators.add( mouseUpDownTranslateObject );

	}

	private void setUpJointRotators() {
		ManipulationHandleIndirection rotateJointAboutZAxis = new ManipulationHandleIndirection( new org.alice.interact.handle.JointRotationRingHandle( MovementDirection.BACKWARD, Color4f.BLUE ) );
		rotateJointAboutZAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateJointAboutZAxis.addToSet( HandleSet.JOINT_ROTATION_INTERACTION );
		rotateJointAboutZAxis.addToGroups( HandleSet.HandleGroup.Z_AXIS, HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.JOINT );
		rotateJointAboutZAxis.setDragAdapterAndAddHandle( this );

		ManipulationHandleIndirection rotateJointAboutYAxis = new ManipulationHandleIndirection( new org.alice.interact.handle.JointRotationRingHandle( MovementDirection.UP, Color4f.GREEN ) );
		rotateJointAboutYAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateJointAboutYAxis.addToSet( HandleSet.JOINT_ROTATION_INTERACTION );
		rotateJointAboutYAxis.addToGroups( HandleSet.HandleGroup.Y_AXIS, HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.JOINT );
		rotateJointAboutYAxis.setDragAdapterAndAddHandle( this );

		ManipulationHandleIndirection rotateJointAboutXAxis = new ManipulationHandleIndirection( new org.alice.interact.handle.JointRotationRingHandle( MovementDirection.LEFT, Color4f.RED ) );
		rotateJointAboutXAxis.setManipulation( new ObjectRotateDragManipulator() );
		rotateJointAboutXAxis.addToSet( HandleSet.JOINT_ROTATION_INTERACTION );
		rotateJointAboutXAxis.addToGroups( HandleSet.HandleGroup.X_AXIS, HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.JOINT );
		rotateJointAboutXAxis.setDragAdapterAndAddHandle( this );
	}

	private void setUpCameraControls() {
		MouseDragCondition leftAndNoModifiers = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.getNonInteractiveHint() ), new ModifierMask( ModifierMask.NO_MODIFIERS_DOWN ) );
		MouseDragCondition leftAndShift = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.getNonInteractiveHint() ), new ModifierMask( ModifierMask.JUST_SHIFT ) );
		MouseDragCondition leftAndControl = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.getNonInteractiveHint() ), new ModifierMask( ModifierMask.JUST_CONTROL ) );
		MouseDragCondition middleMouseAndAnything = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON2, new PickCondition( PickHint.getAnythingHint() ) );
		MouseDragCondition rightMouseAndNonInteractive = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON3, new PickCondition( PickHint.getNonInteractiveHint() ) );

		ManipulatorConditionSet cameraOrbit = new ManipulatorConditionSet( new CameraOrbitDragManipulator() );
		cameraOrbit.addCondition( middleMouseAndAnything );
		this.manipulators.add( cameraOrbit );

		ManipulatorConditionSet cameraTilt = new ManipulatorConditionSet( new CameraTiltDragManipulator() );
		cameraTilt.addCondition( rightMouseAndNonInteractive );
		cameraTilt.addCondition( leftAndControl );
		this.manipulators.add( cameraTilt );

		ManipulatorConditionSet cameraMouseTranslate = new ManipulatorConditionSet( new CameraMoveDragManipulator() );
		cameraMouseTranslate.addCondition( leftAndNoModifiers );
		this.manipulators.add( cameraMouseTranslate );

		ManipulatorConditionSet cameraMousePan = new ManipulatorConditionSet( new CameraPanDragManipulator() );
		cameraMousePan.addCondition( leftAndShift );
		this.manipulators.add( cameraMousePan );
	}

}
