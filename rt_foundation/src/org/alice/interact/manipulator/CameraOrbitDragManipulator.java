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

import org.alice.interact.InputState;
import org.alice.interact.PlaneUtilities;

import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.StandIn;

/**
 * @author David Culyba
 */
public class CameraOrbitDragManipulator extends CameraManipulator {

	Point3 pivotPoint = null;
	static final Plane GROUND_PLANE = new edu.cmu.cs.dennisc.math.Plane( 0.0d, 1.0d, 0.0d, 0.0d );
	static final double TURN_RATE = 2.0d;
	
	
	public void setPivotPoint( Point3 pivotPoint )
	{
		this.pivotPoint = pivotPoint;
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		int xChange = currentInput.getMouseLocation().x - previousInput.getMouseLocation().x;
		int yChange = currentInput.getMouseLocation().y - previousInput.getMouseLocation().y;
		
		
		
		double leftRightRotationAngle = xChange * TURN_RATE;
		double upDownRotationAngle = yChange * TURN_RATE;
		
		StandIn standIn = new StandIn();
		standIn.vehicle.setValue( this.getCamera().getRoot() );
		standIn.setTranslationOnly( this.pivotPoint, AsSeenBy.SCENE );
		standIn.setAxesOnlyToPointAt( this.getCamera() );
		standIn.setAxesOnlyToStandUp();
		this.manipulatedTransformable.applyRotationAboutXAxis( new AngleInDegrees(upDownRotationAngle), standIn );
		this.manipulatedTransformable.applyRotationAboutYAxis( new AngleInDegrees(leftRightRotationAngle), standIn );
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if (super.doStartManipulator( startInput ))
		{
			Vector3 cameraForward = this.manipulatedTransformable.getAbsoluteTransformation().orientation.backward;
			cameraForward.multiply( -1.0d );
			Point3 pickPoint = PlaneUtilities.getPointInPlane( GROUND_PLANE, new edu.cmu.cs.dennisc.math.Ray(this.manipulatedTransformable.getAbsoluteTransformation().translation, cameraForward));
			if ( pickPoint != null)
			{
				this.setPivotPoint( pickPoint );
				return true;
			}
		}
		return false;
		
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub
		
	}
	

}
