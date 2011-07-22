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
package org.alice.interact.manipulator;

import java.awt.Color;

import org.alice.interact.InputState;
import org.alice.interact.condition.ClickedObjectCondition;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.ImageBasedManipulationHandle2D;
import org.lookingglassandalice.storytelling.Entity;
import org.lookingglassandalice.storytelling.implementation.TransformableImplementation;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector2;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public abstract class Camera2DDragManipulator extends CameraManipulator2D {

	protected static final double MIN_TIME = .001d;
	protected static final double MAX_TIME = .1d;
	protected static final double MIN_AMOUNT_TO_MOVE = .005d;
	protected static final double WORLD_DISTANCE_PER_PIXEL_SECONDS = .08d;
	protected static final double RADIANS_PER_PIXEL_SECONDS = .02d;
	protected static final double MIN_PIXEL_MOVE_AMOUNT = 15.0d;
	protected static final double OPPOSITE_DIRECTION_MIN_PIXEL_MOVE_AMOUNT = 5.0d;
	
	protected Point3 initialMoveFactor = new Point3(0.0d, 0.0d, 0.0d);
	protected Point3 initialRotateFactor = new Point3(0.0d, 0.0d, 0.0d);
	protected static final double INITIAL_MOVE_FACTOR = 10.0d;
	protected static final double INITIAL_ROTATE_FACTOR = 8.0d;
	protected static final double MOVE_CLICK_FACTOR = .04d;
	protected static final double ROTATE_CLICK_FACTOR = .006d;
	
	protected TransformableImplementation standUpReference;
	
	protected Color initialHandleColor = null;
	protected Vector2 initialMousePosition = new Vector2();
	protected InputState mouseDownState = null;
	protected AffineMatrix4x4 initialTransform;
	
	public Camera2DDragManipulator( ImageBasedManipulationHandle2D handle)
	{
		super(handle);
	}
	
	@Override
	protected abstract void initializeEventMessages();
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) 
	{
		boolean wasClicked = false;
		if (this.mouseDownState != null)
		{
			long elapseTime = endInput.getTimeCaptured() - mouseDownState.getTimeCaptured();
			double mouseDistance = endInput.getMouseLocation().distance(mouseDownState.getMouseLocation());
			if (elapseTime <= ClickedObjectCondition.MAX_CLICK_TIME && mouseDistance <= ClickedObjectCondition.MAX_MOUSE_MOVE)
			{
				wasClicked = true;
			}
		}
		if (wasClicked)
		{
			this.doClickManipulator(endInput, previousInput);
		}
		this.mouseDownState = null;
		
	}
	
	@Override
	public void doClickManipulator(InputState clickInput, InputState previousInput) {
		//This lets the manipulator know that the object has changed and we should push the change onto the undo/redo stack
		this.hasDoneUpdate = true;
		Point3 amountToMoveClick = Point3.createMultiplication( this.initialMoveFactor, MOVE_CLICK_FACTOR );
		Point3 amountToRotateClick = Point3.createMultiplication( this.initialRotateFactor, ROTATE_CLICK_FACTOR );
		this.manipulatedTransformable.getSgComposite().setTransformation(this.initialTransform, AsSeenBy.SCENE);
		applyMovement(amountToMoveClick, amountToRotateClick);
	}

	
	@Override
	public String getUndoRedoDescription() {
		return "Camera Move";
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if (super.doStartManipulator( startInput ))
		{
			this.mouseDownState = new InputState(startInput);
			this.initializeEventMessages();
			this.standUpReference.setVehicle( this.getCamera().getParent() );
			this.standUpReference.localTransformation.setValue( AffineMatrix4x4.createIdentity() );
			this.initialTransform = this.manipulatedTransformable.getAbsoluteTransformation();
			this.standUpReference.setAxesOnlyToStandUp();
			this.initialMousePosition.x = startInput.getMouseLocation().x;
			this.initialMousePosition.y = startInput.getMouseLocation().y;
			if (this.handle instanceof ImageBasedManipulationHandle2D)
			{
				this.initialHandleColor = ((ImageBasedManipulationHandle2D)this.handle).getColor( (int)this.initialMousePosition.x, (int)this.initialMousePosition.y );
			}
			this.initialMoveFactor = this.getMovementVectorForColor( this.initialHandleColor );
			this.initialRotateFactor = this.getRotationVectorForColor( this.initialHandleColor );
			return true;
		}
		this.mouseDownState = null;
		return false;
	}

	protected abstract Point3 getRotationVectorForColor(Color color);
	protected abstract Point3 getMovementVectorForColor(Color color);
	protected abstract Point3 getRelativeMovementAmount(Vector2 mousePos, double time);
	protected abstract Point3 getRelativeRotationAmount(Vector2 mousePos, double time);
	protected abstract org.lookingglassandalice.storytelling.implementation.ReferenceFrame getRotationReferenceFrame();
	protected abstract org.lookingglassandalice.storytelling.implementation.ReferenceFrame getMovementReferenceFrame();
	
	protected Point3 getTotalMovementAmount(Vector2 mousePos, double time)
	{
		Point3 relativeMovementAmount = this.getRelativeMovementAmount( mousePos, time );
		Point3 amountToMoveInitial = Point3.createMultiplication( this.initialMoveFactor, WORLD_DISTANCE_PER_PIXEL_SECONDS * time );
		Point3 amountToMove = Point3.createAddition(relativeMovementAmount, amountToMoveInitial);
		return amountToMove;
	}
	
	protected Point3 getTotalRotationAmount(Vector2 mousePos, double time)
	{
		Point3 relativeRotationAmount = this.getRelativeRotationAmount( mousePos, time );
		Point3 amountToRotateInitial = Point3.createMultiplication( this.initialRotateFactor, RADIANS_PER_PIXEL_SECONDS * time );
		Point3 amountToRotate = Point3.createAddition(relativeRotationAmount, amountToRotateInitial);	
		return amountToRotate;
	}
	
	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		if (time < MIN_TIME)
			time = MIN_TIME;
		else if (time > MAX_TIME)
			time = MAX_TIME;
		
		Vector2 mousePos = new Vector2( currentInput.getMouseLocation().x, currentInput.getMouseLocation().y);
		Point3 moveVector = this.getTotalMovementAmount( mousePos, time );
		Point3 rotateVector = this.getTotalRotationAmount( mousePos, time );

		applyMovement(moveVector, rotateVector);
	}
	
	protected void applyMovement(Point3 moveVector, Point3 rotateVector)
	{
		this.manipulatedTransformable.applyTranslation( moveVector, this.getMovementReferenceFrame() );
		if (rotateVector.x != 0.0d)
			this.manipulatedTransformable.applyRotationInRadians(edu.cmu.cs.dennisc.math.Vector3.accessPositiveXAxis(), rotateVector.x, getRotationReferenceFrame() );
		if (rotateVector.y != 0.0d)
			this.manipulatedTransformable.applyRotationInRadians(edu.cmu.cs.dennisc.math.Vector3.accessPositiveYAxis(), rotateVector.y, getRotationReferenceFrame() );
		if (rotateVector.z != 0.0d)
			this.manipulatedTransformable.applyRotationInRadians(edu.cmu.cs.dennisc.math.Vector3.accessPositiveZAxis(), rotateVector.z, getRotationReferenceFrame() );
		
		for (ManipulationEvent event : this.manipulationEvents)
		{
			Point3 dotVector = null;
			if (event.getType() == ManipulationEvent.EventType.Rotate)
			{
				dotVector = rotateVector;
			}
			else if (event.getType() == ManipulationEvent.EventType.Translate)
			{
				dotVector = moveVector;
			}
			if (dotVector != null)
			{
				Vector3 normalizedDotVector = new Vector3(dotVector);
				normalizedDotVector.normalize();
				double dot = Vector3.calculateDotProduct( event.getMovementDescription().direction.getVector(), normalizedDotVector );
				if (!Double.isNaN( dot ) && dot > 0.0d)
				{
					this.dragAdapter.triggerManipulationEvent( event, true );
				}
				else
				{
					this.dragAdapter.triggerManipulationEvent( event, false );
				}
			}
		}
	}


}
