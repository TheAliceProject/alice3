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
package org.alice.interact.manipulator;

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AngleUtilities;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

/**
 * @author David Culyba
 */
public class HandlelessObjectRotateDragManipulator extends AbstractManipulator implements CameraInformedManipulator {
	protected static final double MOUSE_DISTANCE_TO_RADIANS_MULTIPLIER = .025d;
	
	protected Vector3 rotateAxis;
	protected MovementDirection rotateAxisDirection;
	protected OnscreenLookingGlass onscreenLookingGlass = null;
	
	protected Point initialPoint;
	protected Vector3 absoluteRotationAxis;
	
	
	public AbstractCamera getCamera()
	{
		if( this.onscreenLookingGlass != null )
		{
			return onscreenLookingGlass.getCameraAt( 0 );
		} 
		return null;
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Rotate";
	}
	
	public void setOnscreenLookingGlass( OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
	}
	
	public HandlelessObjectRotateDragManipulator( MovementDirection rotateAxisDirection )
	{
		this.rotateAxisDirection = rotateAxisDirection;
		this.rotateAxis = this.rotateAxisDirection.getVector();
	}
	
	protected Angle getRotationBasedOnMouse( Point mouseLocation )
	{
		Ray pickRay = PlaneUtilities.getRayFromPixel( this.onscreenLookingGlass, this.getCamera(), mouseLocation.x, mouseLocation.y );
		if (pickRay != null)
		{
			int xDif = mouseLocation.x - this.initialPoint.x;
			return new AngleInRadians(xDif * MOUSE_DISTANCE_TO_RADIANS_MULTIPLIER);
		}
		return null;
	}
	
	protected void initManipulator( InputState startInput )
	{
		this.absoluteRotationAxis = this.manipulatedTransformable.getAbsoluteTransformation().createTransformed( this.rotateAxis );
		this.initialPoint = new Point(startInput.getMouseLocation());
	}
	
	@Override
	public boolean doStartManipulator( InputState startInput ) 
	{
		this.manipulatedTransformable = startInput.getClickPickTransformable();
		if (this.manipulatedTransformable != null)
		{
			this.initManipulator( startInput );
			return true;
		}
		else
		{
			return false;
		}
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
				this.manipulatedTransformable.applyRotationAboutArbitraryAxis( this.rotateAxis, angleDif, this.manipulatedTransformable );
			}
		}

	}
	
	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput )
	{
	}
	
	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return new HandleSet(this.rotateAxisDirection.getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.ROTATION);
	}
}
