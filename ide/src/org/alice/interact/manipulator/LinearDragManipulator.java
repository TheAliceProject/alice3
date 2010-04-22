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

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.PickHint;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.VectorUtilities;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.LinearDragHandle;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class LinearDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnScreenLookingGlassInformedManipulator{
	
	protected LinearDragHandle linearHandle;
	protected Vector3 absoluteDragAxis;
	protected Point3 initialClickPoint = new Point3();
	protected Point3 originalOrigin;
	protected Plane cameraFacingPlane;
	protected Plane handleAlignedPlane;
	protected AbstractCamera camera = null;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	public AbstractCamera getCamera()
	{
		return this.camera;
	}
	
	public void setCamera( AbstractCamera camera ) 
	{
		this.camera = camera;
		if (this.camera != null && this.camera.getParent() instanceof Transformable)
		{
			this.manipulatedTransformable = (Transformable)this.camera.getParent();
		}	
	}
	
	public void setDesiredCameraView( CameraView cameraView )
	{
		//this can only be ACTIVE_VIEW
	}
	
	public CameraView getDesiredCameraView() {
		return CameraView.ACTIVE_VIEW;
	}
	
	public OnscreenLookingGlass getOnscreenLookingGlass()
	{
		return this.onscreenLookingGlass;
	}
	
	public void setOnscreenLookingGlass( OnscreenLookingGlass lookingGlass )
	{
		this.onscreenLookingGlass = lookingGlass;
	}
	
	@Override
	public String getUndoRedoDescription() {
		return "Object Move";
	}
	
	@Override
	protected void initializeEventMessages()
	{
		this.manipulationEvents.clear();
		this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, this.linearHandle.getMovementDescription(), this.manipulatedTransformable ) );
		MovementDirection oppositeDirection = this.linearHandle.getMovementDescription().direction.getOpposite();
		if (oppositeDirection != this.linearHandle.getMovementDescription().direction)
		{
			this.manipulationEvents.add( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription(oppositeDirection, this.linearHandle.getMovementDescription().type), this.manipulatedTransformable ) );
		}
	}
	
	protected double getDistanceAlongAxisBasedOnMouse( Point mouseLocation )
	{
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.getOnscreenLookingGlass(), this.getCamera(), mouseLocation.x, mouseLocation.y );
		if (pickRay != null)
		{
			double axisCameraDot = Vector3.calculateDotProduct( this.absoluteDragAxis, this.getCamera().getAbsoluteTransformation().orientation.backward );
			if (Math.abs( axisCameraDot ) > .98d )
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.cameraFacingPlane, pickRay );
				Vector3 fromOriginalMouseToCurrentMouse = Vector3.createSubtraction( pointInPlane, this.initialClickPoint );
				Vector3 dragRightAxis = this.getCamera().getAbsoluteTransformation().orientation.right;
				dragRightAxis.normalize();
				Vector3 dragUpAxis = this.getCamera().getAbsoluteTransformation().orientation.up;
				dragUpAxis.normalize();
				
				double leftRightSign = 1.0d;
				if (Vector3.calculateDotProduct( this.absoluteDragAxis, dragRightAxis ) < 0.0d)
				{
					leftRightSign = -1.0d;
				}
				double upDownSign = 1.0d;
				if (Vector3.calculateDotProduct( this.absoluteDragAxis, dragUpAxis ) < 0.0d)
				{
					upDownSign = -1.0d;
				}
				
				double mouseYDistance = upDownSign * Vector3.calculateDotProduct( fromOriginalMouseToCurrentMouse, dragUpAxis );
				double mouseXDistance = leftRightSign * Vector3.calculateDotProduct( fromOriginalMouseToCurrentMouse, dragRightAxis );
				return this.linearHandle.getCurrentHandleLength() + mouseYDistance + mouseXDistance;
			}
			else
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.handleAlignedPlane, pickRay );
				if (pointInPlane != null)
				{
					Vector3 pointVector = Vector3.createSubtraction( pointInPlane, this.originalOrigin );
					double dragAmount = Vector3.calculateDotProduct( pointVector, this.absoluteDragAxis );
					return dragAmount;
				}
			}
		}
		return 0;
	}
	
	protected void updateBasedOnHandlePull( double previousPull, double newPull )
	{
		Vector3 translation = Vector3.createMultiplication( this.linearHandle.getDragAxis(), (newPull - previousPull));
		
		Vector3 movementDif = new Vector3(translation);
		movementDif.normalize();
		for (ManipulationEvent event : this.manipulationEvents)
		{
			double dot = Vector3.calculateDotProduct( event.getMovementDescription().direction.getVector(), movementDif );
			if (dot > 0.1d)
			{
				this.dragAdapter.triggerManipulationEvent( event, true );
			}
			else if ( dot < -.07d)
			{
				this.dragAdapter.triggerManipulationEvent( event, false );
			}
		}
		
		this.manipulatedTransformable.applyTranslation( translation, this.linearHandle.getReferenceFrame() );
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) )
		{
			double currentDistance = getDistanceAlongAxisBasedOnMouse( currentInput.getMouseLocation() );
			double previousDistance  = getDistanceAlongAxisBasedOnMouse( previousInput.getMouseLocation() );
			updateBasedOnHandlePull(previousDistance, currentDistance);
		}

	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		Transformable clickedHandle = PickHint.THREE_D_HANDLES.getMatchingTransformable( startInput.getClickPickedTransformable(true) );
		if (clickedHandle instanceof LinearDragHandle)
		{
			this.linearHandle = (LinearDragHandle)clickedHandle;
			this.manipulatedTransformable = this.linearHandle.getManipulatedObject();
			this.initializeEventMessages();
			this.absoluteDragAxis = this.linearHandle.getReferenceFrame().getAbsoluteTransformation().createTransformed( this.linearHandle.getDragAxis() );
			
			startInput.getClickPickResult().getPositionInSource(this.initialClickPoint);
			startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
			
			Vector3 toCamera = Vector3.createSubtraction( this.getCamera().getAbsoluteTransformation().translation, this.manipulatedTransformable.getAbsoluteTransformation().translation );
			toCamera.normalize();
			Vector3 axisAlignedNormal = null;
			if (Math.abs(Vector3.calculateDotProduct( toCamera, this.absoluteDragAxis )) > .99d)
			{
				axisAlignedNormal = toCamera;
			}
			else
			{
				Vector3 axisAlignedCameraVector = VectorUtilities.projectOntoVector( toCamera, this.absoluteDragAxis );
				Vector3 awayFromAxis = Vector3.createSubtraction( toCamera, axisAlignedCameraVector );
				awayFromAxis.normalize();
				axisAlignedNormal = awayFromAxis;
			}
			this.handleAlignedPlane = new Plane(this.linearHandle.getAbsoluteTransformation().translation, axisAlignedNormal);
			this.cameraFacingPlane = new Plane( this.initialClickPoint, this.getCamera().getAbsoluteTransformation().orientation.backward);
			this.originalOrigin = this.manipulatedTransformable.getAbsoluteTransformation().translation; 
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void doClickManipulator(InputState clickInput, InputState previousInput) {
		//Do nothing
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return new HandleSet(this.linearHandle.getMovementDescription().direction.getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.TRANSLATION);
	}

}
