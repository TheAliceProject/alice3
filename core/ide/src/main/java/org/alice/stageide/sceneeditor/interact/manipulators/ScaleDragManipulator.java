/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.stageide.sceneeditor.interact.manipulators;

import org.alice.interact.InputState;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.LinearScaleHandle;
import org.alice.interact.handle.ManipulationHandle3D;
import org.alice.interact.manipulator.LinearDragManipulator;
import org.alice.stageide.sceneeditor.interact.croquet.PredeterminedScaleActionOperation;

import edu.cmu.cs.dennisc.scenegraph.scale.Scalable;

/**
 * @author David Culyba
 */
public class ScaleDragManipulator extends LinearDragManipulator {

	public static final double MIN_HANDLE_PULL = .1d;

	@Override
	protected void initializeEventMessages() {
		this.setMainManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Scale, null, this.manipulatedTransformable ) );
		this.clearManipulationEvents();
		if( this.linearHandle != null ) {
			this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Scale, this.linearHandle.getMovementDescription(), this.manipulatedTransformable ) );
		}
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		boolean started = super.doStartManipulator( startInput );

		if( started ) {
			LinearScaleHandle scaleHandle = (LinearScaleHandle)this.linearHandle;
			Scalable scalable = this.manipulatedTransformable.getBonusDataFor( Scalable.KEY );
			if( scalable != null ) {
				edu.cmu.cs.dennisc.scenegraph.scale.Resizer resizer = scaleHandle.getResizer();
				this.initialScale = scalable.getValueForResizer( resizer );
			}
		}

		return started;
	}

	@Override
	protected void updateBasedOnHandlePull( double initialPull, double newPull ) {
		double pullDif = newPull - initialPull;
		LinearScaleHandle scaleHandle = (LinearScaleHandle)this.linearHandle;
		double scale = pullDif;
		//Don't scale if the handles are pulled past their origin
		if( newPull <= MIN_HANDLE_PULL ) {
			scale = ResizeDragManipulator.MIN_SCALE - this.initialScale;
		}
		if( ( this.initialScale + scale ) < ResizeDragManipulator.MIN_SCALE ) {
			scale = ResizeDragManipulator.MIN_SCALE - this.initialScale;
		}
		accumulatedScale = scale + this.initialScale;

		Scalable scalable = this.manipulatedTransformable.getBonusDataFor( Scalable.KEY );
		if( scalable != null ) {
			scalable.setValueForResizer( scaleHandle.getResizer(), accumulatedScale );
		}

	}

	@Override
	public void undoRedoBeginManipulation() {
		accumulatedScale = this.initialScale;
	}

	@Override
	public void undoRedoEndManipulation() {
		if( this.getManipulatedTransformable() != null ) {
			edu.cmu.cs.dennisc.animation.Animator animator;
			if( this.dragAdapter != null ) {
				animator = this.dragAdapter.getAnimator();
			} else {
				animator = null;
			}
			Scalable scalable = this.manipulatedTransformable.getBonusDataFor( Scalable.KEY );
			org.lgna.story.SThing aliceThing = ( (org.lgna.story.implementation.ModelImp)scalable ).getAbstraction();
			org.lgna.project.ast.UserField manipulatedField = org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().getFieldForInstanceInJavaVM( aliceThing );

			LinearScaleHandle scaleHandle = (LinearScaleHandle)this.linearHandle;
			PredeterminedScaleActionOperation undoOperation = new PredeterminedScaleActionOperation( org.lgna.croquet.Application.PROJECT_GROUP, false, animator, manipulatedField, scaleHandle.getResizer(), initialScale, accumulatedScale, ManipulationHandle3D.NOT_3D_HANDLE_CRITERION, getUndoRedoDescription() );
			undoOperation.fire();
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

	private double initialScale;
	private double accumulatedScale = 1.0d;
}
