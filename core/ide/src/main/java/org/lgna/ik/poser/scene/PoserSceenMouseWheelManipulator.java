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
package org.lgna.ik.poser.scene;

import org.alice.interact.InputState;
import org.alice.interact.QuaternionAndTranslation;
import org.alice.stageide.sceneeditor.interact.manipulators.CameraZoomMouseWheelManipulator;
import org.lgna.story.implementation.ModelImp;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/**
 * @author Matt May
 */
public class PoserSceenMouseWheelManipulator extends CameraZoomMouseWheelManipulator {

	private ModelImp model;

	public void setModel( ModelImp model ) {
		this.model = model;
	}

	@Override
	public String getUndoRedoDescription() {
		return "Camera Zoom";
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( isTooClose() ) {
			return false;
		} else {
			return super.doStartManipulator( startInput );
		}
	}

	private boolean isTooClose() {
		double distance = getDistance();
		return distance < .33;
	}

	private double getDistance() {
		Point3 modelLoc = model.getAbsoluteTransformation().translation;
		Point3 cameraLoc = camera.getAbsoluteTransformation().translation;
		modelLoc.z = 1;
		cameraLoc.z = 1;
		double distance = Point3.calculateDistanceBetween( modelLoc, cameraLoc );
		return distance;
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if( !isTooClose() && !currentInput.isAnyMouseButtonDown() && !( currentInput.getMouseWheelState() < 0 ) ) {
			super.doDataUpdateManipulator( currentInput, previousInput );
		}
	}

	@Override
	protected void zoomCamera( int direction ) {
		if( this.camera instanceof SymmetricPerspectiveCamera ) {
			AbstractTransformable cameraTransformable = getManipulatedTransformable();
			//			super.zoomCamera( direction );
			AffineMatrix4x4 originalTransformation = cameraTransformable.getAbsoluteTransformation();
			OrthogonalMatrix3x3 orientation = originalTransformation.orientation;
			Vector3 movementDirection = Vector3.createMultiplication( orientation.backward, direction );
			movementDirection.normalize();
			movementDirection.multiply( getZoomSpeed() );
			originalTransformation.translation.add( movementDirection );
			AffineMatrix4x4 targetTransform = new AffineMatrix4x4( orientation, originalTransformation.translation );
			this.cameraAnimation.setTarget( new QuaternionAndTranslation( targetTransform ) );
		} else {
			super.zoomCamera( direction );
		}
	}

	private double getZoomSpeed() {
		return getDistance() / 10;
	}

	@Override
	public void doClickManipulator( InputState endInput, InputState previousInput ) {
		// do nothing
	}

}
