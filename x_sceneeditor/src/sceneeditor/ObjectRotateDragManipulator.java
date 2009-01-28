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
package sceneeditor;

import java.awt.Point;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.lookingglass.PickResult;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AngleUtilities;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class ObjectRotateDragManipulator extends DragManipulator implements CameraInformedManipulator {

	protected AbstractCamera camera = null;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	protected Point3 initialClickPoint = new Point3();
	protected Point3 objectOriginInPlane;
	protected Plane rotationPlane;
	protected Vector3 originalMouseDirection;
	protected Vector3 originalMouseRightDirection;
	protected Vector3 absoluteRotationAxis;
	
	protected RotationRingHandle rotationHandle;
	
	public void setCamera( AbstractCamera camera ) {
		this.camera = camera;
	}

	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
	}
	
	public  ObjectRotateDragManipulator()
	{
	}
	

	@Override
	public void startManipulator( InputState startInput ) {
		Transformable clickedHandle = PickHint.HANDLES.getMatchingTransformable( startInput.getClickPickedTransformable() );
		if (clickedHandle instanceof RotationRingHandle)
		{
			this.rotationHandle = (RotationRingHandle)clickedHandle;
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
			}
		}

	}
	
	protected Angle getRotationBasedOnMouse( Point mouseLocation )
	{
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.camera, mouseLocation.x, mouseLocation.y );
		if (pickRay != null)
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
		return null;
	}
	
	@Override
	public void dataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) )
		{
			Angle currentAngle = getRotationBasedOnMouse( currentInput.getMouseLocation() );
			Angle previousAngle = getRotationBasedOnMouse( previousInput.getMouseLocation() );
			
			Angle angleDif = AngleUtilities.createSubtraction( currentAngle, previousAngle );
			
			this.manipulatedTransformable.applyRotationAboutArbitraryAxis( this.rotationHandle.getRotationAxis(), angleDif, this.rotationHandle.getReferenceFrame() );
		}

	}
	
	@Override
	public void timeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endManipulator( InputState endInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

}
