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

import org.alice.interact.condition.KeyPressCondition;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.manipulator.CameraTranslateKeyManipulator;
import org.alice.interact.manipulator.ObjectRotateKeyManipulator;
import org.alice.interact.manipulator.HandlelessObjectRotateDragManipulator;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

/**
 * @author David Culyba
 */
public class CreateASimDragAdapter extends AbstractDragAdapter {

	@Override
	protected void setUpControls() {
		MovementKey[] movementKeys = {
				//Up
				new MovementKey(KeyEvent.VK_PAGE_UP, new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP), .35d),
				new MovementKey(KeyEvent.VK_UP, new MovementDescription(MovementDirection.UP, MovementType.STOOD_UP), .35d),
				//Down
				new MovementKey(KeyEvent.VK_PAGE_DOWN, new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP), .35d),
				new MovementKey(KeyEvent.VK_DOWN, new MovementDescription(MovementDirection.DOWN, MovementType.STOOD_UP), .35d),
		};
		
		MovementKey[] zoomKeys = {
				//Zoom out
				new MovementKey(KeyEvent.VK_MINUS, new MovementDescription(MovementDirection.BACKWARD, MovementType.LOCAL)),
				new MovementKey(KeyEvent.VK_SUBTRACT, new MovementDescription(MovementDirection.BACKWARD, MovementType.LOCAL)),
				//Zoom in
				new MovementKey(KeyEvent.VK_EQUALS, new MovementDescription(MovementDirection.FORWARD, MovementType.LOCAL)),
				new MovementKey(KeyEvent.VK_ADD, new MovementDescription(MovementDirection.FORWARD, MovementType.LOCAL)),
		};
		
		MovementKey[] turnKeys = {
				//Left
				new MovementKey(KeyEvent.VK_OPEN_BRACKET, new MovementDescription(MovementDirection.UP, MovementType.ABSOLUTE), -25.0d),
				new MovementKey(KeyEvent.VK_LEFT, new MovementDescription(MovementDirection.UP, MovementType.ABSOLUTE), -25.0d),
				//Right
				new MovementKey(KeyEvent.VK_CLOSE_BRACKET, new MovementDescription(MovementDirection.UP, MovementType.ABSOLUTE), 25.0d),
				new MovementKey(KeyEvent.VK_RIGHT, new MovementDescription(MovementDirection.UP, MovementType.ABSOLUTE), 25.0d),
		};
		
		CameraTranslateKeyManipulator cameraTranslateManip = new CameraTranslateKeyManipulator( movementKeys );
		cameraTranslateManip.addKeys( zoomKeys );
		ManipulatorConditionSet cameraTranslate = new ManipulatorConditionSet( cameraTranslateManip );
		for (int i=0; i<movementKeys.length; i++)
		{
			cameraTranslate.addCondition( new KeyPressCondition( movementKeys[i].keyValue ) );
		}
		for (int i=0; i<zoomKeys.length; i++)
		{
			cameraTranslate.addCondition( new KeyPressCondition( zoomKeys[i].keyValue ) );
		}
		this.manipulators.add( cameraTranslate );
		
		
		ManipulatorConditionSet objectRotate = new ManipulatorConditionSet( new ObjectRotateKeyManipulator( turnKeys ) );
		for (int i=0; i<turnKeys.length; i++)
		{
			objectRotate.addCondition( new KeyPressCondition( turnKeys[i].keyValue ) );
		}
		this.manipulators.add( objectRotate );
		
		ManipulatorConditionSet mouseRotateObjectLeftRight = new ManipulatorConditionSet( new HandlelessObjectRotateDragManipulator(MovementDirection.UP) );
		MouseDragCondition moveableObjectWithCtrl = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON1, new PickCondition( PickHint.ANYTHING));
		mouseRotateObjectLeftRight.addCondition( moveableObjectWithCtrl );
		this.manipulators.add( mouseRotateObjectLeftRight );

		for (int i=0; i<this.manipulators.size(); i++)
		{
			this.manipulators.get( i ).getManipulator().setDragAdapter( this );
		}
	}
	
	@Override
	public void setSGCamera( AbstractCamera camera )
	{
		super.setSGCamera( camera );
		AxisAlignedBox cameraBounds = new AxisAlignedBox();
		Vector3 cameraBackwards = camera.getAbsoluteTransformation().orientation.backward;
		
		Point3 cameraMin = new Point3(camera.getAbsoluteTransformation().translation);
		Point3 cameraMax = new Point3(cameraMin);
		double originalY = cameraMin.y;
		cameraMin.add( Vector3.createMultiplication( cameraBackwards, 1.5d) );
		cameraMin.y = .25d;
		cameraMax.subtract( Vector3.createMultiplication( cameraBackwards, 4.5d) );
		cameraMax.y = originalY + 1.5d;
		cameraBounds.setMinimum( cameraMin );
		cameraBounds.setMaximum( cameraMax );
		for (int i=0; i<this.manipulators.size(); i++)
		{
			if (this.manipulators.get( i ).getManipulator() instanceof CameraTranslateKeyManipulator)
			{
				((CameraTranslateKeyManipulator)this.manipulators.get( i ).getManipulator()).setBounds( cameraBounds );
			}
		}
	}
	

}
