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

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.ManipulationHandle3D;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.stageide.sceneeditor.interact.croquet.PredeterminedScaleActionOperation;

public class ResizeDragManipulator extends AbstractManipulator {
	private static final double RESIZE_SCALE = .005;
	public static final double MIN_SCALE = .1;

	public ResizeDragManipulator( edu.cmu.cs.dennisc.scenegraph.scale.Resizer... resizers ) {
		this.resizers = resizers;
	}

	@Override
	public void doClickManipulator( InputState endInput, InputState previousInput ) {
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if( !currentInput.getMouseLocation().equals( previousInput.getMouseLocation() ) ) {
			int xDif = currentInput.getMouseLocation().x - this.initialPoint.x;
			int yDif = -( currentInput.getMouseLocation().y - this.initialPoint.y );

			double scaleAmount = ( ( xDif + yDif ) * RESIZE_SCALE );
			applyScale( scaleAmount );
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
	}

	protected void initManipulator( InputState startInput ) {
		this.initialPoint = new Point( startInput.getMouseLocation() );
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.setManipulatedTransformable( startInput.getClickPickTransformable() );
		if( this.manipulatedTransformable != null ) {
			edu.cmu.cs.dennisc.scenegraph.scale.Scalable scalable = this.manipulatedTransformable.getBonusDataFor( edu.cmu.cs.dennisc.scenegraph.scale.Scalable.KEY );
			if( scalable != null ) {
				this.activeResizer = null;
				for( edu.cmu.cs.dennisc.scenegraph.scale.Resizer toUse : this.resizers ) {
					for( edu.cmu.cs.dennisc.scenegraph.scale.Resizer r : scalable.getResizers() ) {
						if( r == toUse ) {
							this.activeResizer = r;
							break;
						}
					}
					if( this.activeResizer != null ) {
						break;
					}
				}
				if( this.activeResizer != null ) {
					initialScale = scalable.getValueForResizer( this.activeResizer );
				} else {
					return false;
				}
			}
			this.initManipulator( startInput );
			return true;
		} else {
			return false;
		}
	}

	protected void applyScale( double scaleAmount ) {
		edu.cmu.cs.dennisc.scenegraph.scale.Scalable scalable = this.manipulatedTransformable.getBonusDataFor( edu.cmu.cs.dennisc.scenegraph.scale.Scalable.KEY );
		if( scalable != null ) {
			if( ( this.initialScale + scaleAmount ) < MIN_SCALE ) {
				scaleAmount = MIN_SCALE - this.initialScale;
			}
			this.accumulatedScale = this.initialScale + scaleAmount;
			scalable.setValueForResizer( this.activeResizer, this.accumulatedScale );
		}
	}

	@Override
	public void doTimeUpdateManipulator( double dTime, InputState currentInput ) {
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
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
			edu.cmu.cs.dennisc.scenegraph.scale.Scalable scalable = this.manipulatedTransformable.getBonusDataFor( edu.cmu.cs.dennisc.scenegraph.scale.Scalable.KEY );
			org.lgna.story.SThing aliceThing = ( (org.lgna.story.implementation.ModelImp)scalable ).getAbstraction();
			org.lgna.project.ast.UserField scalableField = org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().getFieldForInstanceInJavaVM( aliceThing );
			PredeterminedScaleActionOperation undoOperation = new PredeterminedScaleActionOperation( org.lgna.croquet.Application.PROJECT_GROUP, false, animator, scalableField, this.activeResizer, this.initialScale, this.accumulatedScale, ManipulationHandle3D.NOT_3D_HANDLE_CRITERION, getUndoRedoDescription() );
			undoOperation.fire();
		}
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Resize";
	}

	private Point initialPoint;
	private double initialScale = 1d;
	private double accumulatedScale = 1d;
	private final edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] resizers;
	private edu.cmu.cs.dennisc.scenegraph.scale.Resizer activeResizer;
}
