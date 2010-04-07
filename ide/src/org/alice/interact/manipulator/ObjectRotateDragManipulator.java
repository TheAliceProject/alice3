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

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;

import javax.swing.SwingUtilities;

import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.RotationRingHandle;
import org.alice.interact.operations.PredeterminedSetLocalTransformationActionOperation;
import org.alice.interact.VectorUtilities;

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AngleUtilities;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities;

/**
 * @author David Culyba
 */
public class ObjectRotateDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnScreenLookingGlassInformedManipulator {

	protected static final double BAD_ANGLE_THRESHOLD = 2.0d*Math.PI * (15.0d/360.0d);
	protected static final double WORLD_DISTANCE_TO_RADIANS_MULTIPLIER = 1.1d;
	
	protected Point3 initialClickPoint = new Point3();
	protected Point3 objectOriginInPlane;
	protected Plane rotationPlane;
	protected Vector3 originalMouseDirection;
	protected Vector3 originalMouseRightDirection;
	protected Vector3 absoluteRotationAxis;
	protected Plane cameraFacingPlane;
	protected RotationRingHandle rotationHandle;
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
	
	public OnscreenLookingGlass getOnscreenLookingGlass()
	{
		return this.onscreenLookingGlass;
	}
	
	public void setOnscreenLookingGlass( OnscreenLookingGlass lookingGlass )
	{
		this.onscreenLookingGlass = lookingGlass;
	}
	
	public  ObjectRotateDragManipulator()
	{
	}
	
	protected void initManipulator( RotationRingHandle handle, InputState startInput )
	{
		this.rotationHandle = handle;
		this.manipulatedTransformable = this.rotationHandle.getManipulatedObject();
		this.absoluteRotationAxis = this.rotationHandle.getReferenceFrame().getAbsoluteTransformation().createTransformed( this.rotationHandle.getRotationAxis() );
		//PickResult pick = this.onscreenLookingGlass.pickFrontMost( startInput.getMouseLocation().x, startInput.getMouseLocation().y, /*isSubElementRequired=*/false );
		startInput.getClickPickResult().getPositionInSource(this.initialClickPoint);
		startInput.getClickPickResult().getSource().transformTo_AffectReturnValuePassedIn( this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot() );
		Vector3 rotationAxis = this.absoluteRotationAxis;
		this.rotationPlane = new Plane(this.initialClickPoint, rotationAxis);

		Ray originRay = new Ray( this.manipulatedTransformable.getAbsoluteTransformation().translation, rotationAxis );
		double intersection = this.rotationPlane.intersect( originRay );
		if ( Double.isNaN( intersection ))
		{
			originRay = new Ray( this.manipulatedTransformable.getAbsoluteTransformation().translation,  Vector3.createMultiplication( rotationAxis, -1.0d ) );
			intersection = this.rotationPlane.intersect( originRay );
		}
		if ( !Double.isNaN( intersection ))
		{
			this.objectOriginInPlane = originRay.getPointAlong( intersection );
			
			Vector3 toMouse = Vector3.createSubtraction( this.initialClickPoint, this.objectOriginInPlane );
			toMouse.normalize();
			this.originalMouseDirection = new Vector3(toMouse);
			this.originalMouseRightDirection = Vector3.createCrossProduct( this.originalMouseDirection, rotationAxis );
			
			this.rotationHandle.setSphereVisibility( true );
			Vector3 sphereDirection = TransformationUtilities.transformFromAbsolute_New( toMouse, this.rotationHandle );
			this.rotationHandle.setSphereDirection( sphereDirection );
			
			//Hide the cursor
			this.hideCursor();
			
		}
		this.cameraFacingPlane = new Plane( this.initialClickPoint, this.getCamera().getAbsoluteTransformation().orientation.backward);
	}
	
	@Override
	public boolean doStartManipulator( InputState startInput ) {
		Transformable clickedHandle = PickHint.HANDLES.getMatchingTransformable( startInput.getClickPickedTransformable(true) );
		if (clickedHandle instanceof RotationRingHandle)
		{
			this.initManipulator( (RotationRingHandle)clickedHandle, startInput );
			return true;
		}
		return false;

	}
	
	protected Angle getRotationBasedOnMouse( Point mouseLocation )
	{
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.getOnscreenLookingGlass(), this.getCamera(), mouseLocation.x, mouseLocation.y );
		if (pickRay != null)
		{
			AngleInRadians angleBetweenVector = VectorUtilities.getAngleBetweenVectors(this.absoluteRotationAxis, this.getCamera().getAbsoluteTransformation().orientation.backward);
			double distanceToRightAngle = Math.abs(Math.PI*.5d - angleBetweenVector.getAsRadians());
			if (distanceToRightAngle < BAD_ANGLE_THRESHOLD)
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.cameraFacingPlane, pickRay );
				Vector3 fromOriginalMouseToCurrentMouse = Vector3.createSubtraction( pointInPlane, this.initialClickPoint );
				Vector3 rotationRightAxis = Vector3.createCrossProduct( this.absoluteRotationAxis, this.getCamera().getAbsoluteTransformation().orientation.backward);
				double mouseDistance = Vector3.calculateDotProduct( fromOriginalMouseToCurrentMouse, rotationRightAxis );
				
				return new AngleInRadians(mouseDistance * WORLD_DISTANCE_TO_RADIANS_MULTIPLIER);
				
			}
			else
			{
				Point3 pointInPlane = PlaneUtilities.getPointInPlane( this.rotationPlane, pickRay );
				if (pointInPlane != null)
				{
					Vector3 toMouse = Vector3.createSubtraction( pointInPlane, this.objectOriginInPlane );
					
					double toMouseDotOriginalRight = Vector3.calculateDotProduct( toMouse, this.originalMouseRightDirection );
					boolean isToTheRight = toMouseDotOriginalRight > 0.0d;
					
					toMouse.normalize();
					Vector3 toMouseDirection = new Vector3(toMouse);
					double cosOfAngleBetween = Vector3.calculateDotProduct(this.originalMouseDirection, toMouseDirection );
					if (cosOfAngleBetween > 1.0d)
					{
						cosOfAngleBetween = 1.0d;
					}
					else if (cosOfAngleBetween < -1.0d)
					{
						cosOfAngleBetween = -1.0d;
					}
					double angleInRadians = Math.acos( cosOfAngleBetween );
					if (isToTheRight)
					{
						angleInRadians = (Math.PI*2.0d) - angleInRadians;
					}
					return new AngleInRadians( angleInRadians );
				}
			}
		}
		return null;
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) )
		{
			Angle currentAngle = getRotationBasedOnMouse( currentInput.getMouseLocation() );
			Angle previousAngle = getRotationBasedOnMouse( previousInput.getMouseLocation() );
			if (currentAngle != null && previousAngle != null)
			{
				Angle angleDif = AngleUtilities.createSubtraction( currentAngle, previousAngle );
				this.manipulatedTransformable.applyRotationAboutArbitraryAxis( this.rotationHandle.getRotationAxis(), angleDif, this.rotationHandle.getReferenceFrame() );
			}
		}

	}
	
	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub

	}
	
	protected void hideCursor()
	{
		CursorUtilities.pushAndSet( this.getOnscreenLookingGlass().getAWTComponent(), CursorUtilities.NULL_CURSOR );
	}
	
	protected void showCursor()
	{
		try {
			Point3 pointInCamera = this.rotationHandle.getSphereLocation( this.getCamera() );
			Point awtPoint = edu.cmu.cs.dennisc.lookingglass.util.TransformationUtilities.transformFromCameraToAWT_New( pointInCamera, this.getOnscreenLookingGlass(), this.getCamera() );
			SwingUtilities.convertPointToScreen( awtPoint, this.getOnscreenLookingGlass().getAWTComponent() );
			Robot mouseMover = new Robot();
			mouseMover.mouseMove(awtPoint.x, awtPoint.y);
		} catch( AWTException e ) {
		}
		finally
		{
			CursorUtilities.popAndSet( this.getOnscreenLookingGlass().getAWTComponent() );
		}
	}
	
	@Override
	public String getUndoRedoDescription() {
		return "Object Rotate";
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		this.rotationHandle.setSphereVisibility( false );
		this.showCursor();
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return new HandleSet(this.rotationHandle.getRotationDirection().getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.ROTATION);
	}

}
