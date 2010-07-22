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
import org.alice.interact.PlaneUtilities;
import org.alice.interact.VectorUtilities;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.debug.DebugSphere;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;

public class CameraPanDragManipulator extends CameraManipulator {

	protected static final double MOVEMENT_PER_PIXEL = .02d;
	
	protected Point originalMousePoint;
	protected Vector3 xDirection;
	protected Vector3 yDirection;
	
	public CameraPanDragManipulator()
	{
		super();
	}
	
	
	@Override
	public String getUndoRedoDescription() {
		return "Camera Move";
	}
	
	@Override
	public CameraView getDesiredCameraView() 
	{
		return CameraView.PICK_CAMERA;
	}
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) 
	{
		double yDif = -(currentInput.getMouseLocation().y - previousInput.getMouseLocation().y);
		double xDif = currentInput.getMouseLocation().x - previousInput.getMouseLocation().x;
		
		Vector3 xMovement = Vector3.createMultiplication(this.xDirection, xDif*MOVEMENT_PER_PIXEL);
		Vector3 yMovement = Vector3.createMultiplication(this.yDirection, yDif*MOVEMENT_PER_PIXEL);
		
		this.manipulatedTransformable.applyTranslation(xMovement, AsSeenBy.SCENE);
		this.manipulatedTransformable.applyTranslation(yMovement, AsSeenBy.SCENE);
	}
	
	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
	}
	
	@Override
	public void doClickManipulator(InputState clickInput, InputState previousInput) {
		//Do nothing
	}


	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if (super.doStartManipulator( startInput ) && this.camera instanceof SymmetricPerspectiveCamera)
		{
			boolean success = false;
			AffineMatrix4x4 cameraTransform = this.manipulatedTransformable.getAbsoluteTransformation();
			this.yDirection = new Vector3(Vector3.accessPositiveYAxis());
			this.xDirection = new Vector3(cameraTransform.orientation.right);
			
			double xDoty = Vector3.calculateDotProduct(this.yDirection, this.xDirection);
			if (Math.abs(xDoty) > EpsilonUtilities.REASONABLE_EPSILON)
			{
//				System.out.println("Boom!");
				return false;
			}
			return true;
		}
		return false;
		
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		// TODO Auto-generated method stub
		
	}
	

}
