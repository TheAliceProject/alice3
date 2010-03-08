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
	public String getUndoRedoDescription() {
		return "Camera Rotate";
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
