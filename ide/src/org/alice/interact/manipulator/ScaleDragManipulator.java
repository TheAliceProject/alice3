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
import org.alice.interact.PickHint;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.LinearScaleHandle;
import org.alice.interact.operations.PredeterminedScaleActionOperation;
import org.alice.interact.operations.PredeterminedSetLocalTransformationActionOperation;

import edu.cmu.cs.dennisc.alice.Project;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.scale.ScaleUtilities;
import edu.cmu.cs.dennisc.zoot.ZManager;

/**
 * @author David Culyba
 */
public class ScaleDragManipulator extends LinearDragManipulator {

	private static final double MIN_HANDLE_PULL = .1d;

	private Vector3 accumulatedScaleVector = new Vector3(1.0, 1.0, 1.0);


	private Criterion< Component > handleCriterion = new Criterion< Component >() {
		protected boolean isHandle( Component c ) {
			if( c == null )
				return false;
			Object bonusData = c.getBonusDataFor( PickHint.PICK_HINT_KEY );
			if( bonusData instanceof PickHint && ((PickHint)bonusData).intersects( PickHint.THREE_D_HANDLES ) )
				return true;
			else
				return isHandle( c.getParent() );
		}

		public boolean accept( Component c ) {
			return !isHandle( c );
		}
	};

	private Vector3 getInvertedScaleVector( Vector3 scaleVector )
	{
		Vector3 invertedScale = new Vector3();
		if (scaleVector.x != 0)
		{
			invertedScale.x = 1.0 / scaleVector.x;
		}
		else
		{
			invertedScale.x = 1;
		}
		if (scaleVector.y != 0)
		{
			invertedScale.y = 1.0 / scaleVector.y;
		}
		else
		{
			invertedScale.y = 1;
		}
		if (scaleVector.z != 0)
		{
			invertedScale.z = 1.0 / scaleVector.z;
		}
		else
		{
			invertedScale.z = 1;
		}
		return invertedScale;
	}
	
	@Override
	protected void updateBasedOnHandlePull( double initialPull, double newPull ) 
	{
		double pullDif = (newPull) / (initialPull);
		LinearScaleHandle scaleHandle = (LinearScaleHandle)this.linearHandle;
		Vector3 scaleVector;
		if( scaleHandle.applyAlongAxis() ) {
			scaleVector = new Vector3( 1.0d, 1.0d, 1.0d );
			if( scaleHandle.getDragAxis().x != 0.0d )
				scaleVector.x = Math.abs( scaleHandle.getDragAxis().x ) * pullDif;
			if( scaleHandle.getDragAxis().y != 0.0d )
				scaleVector.y = Math.abs( scaleHandle.getDragAxis().y ) * pullDif;
			if( scaleHandle.getDragAxis().z != 0.0d )
				scaleVector.z = Math.abs( scaleHandle.getDragAxis().z ) * pullDif;
		} else {
			scaleVector = new Vector3( pullDif, pullDif, pullDif );
		}

		//Don't scale if the handles are pulled past their origin
		if( newPull <= MIN_HANDLE_PULL ) {
			scaleVector = new Vector3( 1.0d, 1.0d, 1.0d );
		}
		
		//First remove the old scale
		Vector3 inverseScale = getInvertedScaleVector(accumulatedScaleVector);
		ScaleUtilities.applyScale( this.manipulatedTransformable, inverseScale, handleCriterion );
		//Now apply the new scale
		accumulatedScaleVector.set( scaleVector );
		ScaleUtilities.applyScale( this.manipulatedTransformable, scaleVector, handleCriterion );

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
			PredeterminedScaleActionOperation undoOperation = new PredeterminedScaleActionOperation( Project.GROUP_UUID, false, animator, this.getManipulatedTransformable(), accumulatedScaleVector, handleCriterion, getUndoRedoDescription() );
			ZManager.performIfAppropriate( undoOperation, null, false );
		}
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Resize";
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return new HandleSet( this.linearHandle.getMovementDescription().direction.getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.RESIZE );
	}

}
