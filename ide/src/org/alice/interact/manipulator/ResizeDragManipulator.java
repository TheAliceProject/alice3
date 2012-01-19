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
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.ManipulationHandle3D;
import org.alice.interact.operations.PredeterminedScaleActionOperation;

import edu.cmu.cs.dennisc.math.Vector3;

public class ResizeDragManipulator extends AbstractManipulator
{

	protected Point initialPoint;
	private Vector3 accumulatedScaleVector = new Vector3(1.0, 1.0, 1.0);
	private static final double RESIZE_SCALE = .005;
	
	@Override
	public void doClickManipulator(InputState endInput, InputState previousInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
		if ( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) )
		{
			int xDif = currentInput.getMouseLocation().x - this.initialPoint.x;
			int yDif = -(currentInput.getMouseLocation().y - this.initialPoint.y);
			
			double scaleAmount = 1.0 + ((xDif + yDif)*RESIZE_SCALE);
			if (scaleAmount < ScaleDragManipulator.MIN_HANDLE_PULL)
			{
				scaleAmount = ScaleDragManipulator.MIN_HANDLE_PULL;
			}
			setScale(scaleAmount);
		}
	}

	@Override
	public void doEndManipulator(InputState endInput, InputState previousInput) {
		// TODO Auto-generated method stub
		
	}

	protected void initManipulator( InputState startInput )
	{
		this.initialPoint = new Point(startInput.getMouseLocation());
	}
	
	@Override
	public boolean doStartManipulator(InputState startInput) {
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
	
	protected void setScale( double scaleAmount ) 
	{
		Scalable scalable = this.manipulatedTransformable.getBonusDataFor( Scalable.KEY );
		if( scalable != null ) {
			edu.cmu.cs.dennisc.math.Dimension3 scale = new edu.cmu.cs.dennisc.math.Dimension3( scaleAmount, scaleAmount, scaleAmount );
			scalable.setScale( scale );
		}
//		
//		Vector3 scaleVector = new Vector3( scaleAmount, scaleAmount, scaleAmount );
//		//First remove the old scale
//		Vector3 inverseScale = ScaleDragManipulator.getInvertedScaleVector(accumulatedScaleVector);
//		ScaleUtilities.applyScale( this.manipulatedTransformable, inverseScale, ManipulationHandle3D.NOT_3D_HANDLE_CRITERION );
//		//Now apply the new scale
//		accumulatedScaleVector.set( scaleVector );
//		ScaleUtilities.applyScale( this.manipulatedTransformable, scaleVector, ManipulationHandle3D.NOT_3D_HANDLE_CRITERION );
//
	}

	@Override
	public void doTimeUpdateManipulator(double dTime, InputState currentInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undoRedoBeginManipulation() {
		accumulatedScaleVector = new Vector3(1.0, 1.0, 1.0);
	}

	@Override
	public void undoRedoEndManipulation() {
		if( this.getManipulatedTransformable() != null ) 
		{
			edu.cmu.cs.dennisc.animation.Animator animator;
			if( this.dragAdapter != null ) {
				animator = this.dragAdapter.getAnimator();
			} else {
				animator = null;
			}
			PredeterminedScaleActionOperation undoOperation = new PredeterminedScaleActionOperation( org.alice.ide.IDE.PROJECT_GROUP, false, animator, this.getManipulatedTransformable(), accumulatedScaleVector, ManipulationHandle3D.NOT_3D_HANDLE_CRITERION, getUndoRedoDescription() );
			undoOperation.fire();
		}
	}
	
	@Override
	public String getUndoRedoDescription() {
		// TODO Auto-generated method stub
		return "Object Resize";
	}

}
