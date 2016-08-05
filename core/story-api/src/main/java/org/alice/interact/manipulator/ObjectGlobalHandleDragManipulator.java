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
package org.alice.interact.manipulator;

import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.ManipulationHandle;

import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

/**
 * @author David Culyba
 */
public class ObjectGlobalHandleDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {
	@Override
	public AbstractCamera getCamera() {
		return this.camera;
	}

	@Override
	public void setCamera( AbstractCamera camera ) {
		this.camera = camera;
		if( ( this.camera != null ) && ( this.camera.getParent() instanceof AbstractTransformable ) ) {
			this.setManipulatedTransformable( (AbstractTransformable)this.camera.getParent() );
		}
		if( ( this.activeManipulator != null ) && ( this.activeManipulator instanceof CameraInformedManipulator ) ) {
			( (CameraInformedManipulator)this.activeManipulator ).setCamera( camera );
		}

	}

	@Override
	public void setDesiredCameraView( CameraView cameraView ) {
		if( ( this.activeManipulator != null ) && ( this.activeManipulator instanceof CameraInformedManipulator ) ) {
			( (CameraInformedManipulator)this.activeManipulator ).setDesiredCameraView( cameraView );
		} else {
			//pass
		}
	}

	@Override
	public CameraView getDesiredCameraView() {
		if( ( this.activeManipulator != null ) && ( this.activeManipulator instanceof CameraInformedManipulator ) ) {
			return ( (CameraInformedManipulator)this.activeManipulator ).getDesiredCameraView();
		} else {
			return CameraView.PICK_CAMERA;
		}
	}

	@Override
	public edu.cmu.cs.dennisc.render.OnscreenRenderTarget getOnscreenRenderTarget() {
		return this.onscreenRenderTarget;
	}

	@Override
	public void setOnscreenRenderTarget( edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget ) {
		this.onscreenRenderTarget = onscreenRenderTarget;
		if( this.activeManipulator instanceof OnscreenPicturePlaneInformedManipulator ) {
			( (OnscreenPicturePlaneInformedManipulator)this.activeManipulator ).setOnscreenRenderTarget( onscreenRenderTarget );
		}
	}

	@Override
	public String getUndoRedoDescription() {
		if( this.activeManipulator != null ) {
			return this.activeManipulator.getUndoRedoDescription();
		}
		return "Handle Drag";
	}

	@Override
	public boolean hasUpdated() {
		if( this.activeManipulator != null ) {
			return this.activeManipulator.hasUpdated();
		}
		return super.hasUpdated();
	}

	@Override
	public void undoRedoBeginManipulation() {
		if( this.activeManipulator != null ) {
			this.activeManipulator.undoRedoBeginManipulation();
		} else {
			super.undoRedoBeginManipulation();
		}
	}

	@Override
	public void undoRedoEndManipulation() {
		if( this.activeManipulator != null ) {
			this.activeManipulator.undoRedoEndManipulation();
		} else {
			super.undoRedoEndManipulation();
		}
	}

	@Override
	protected void setHasUpdated( boolean hasUpdated ) {
		if( this.activeManipulator != null ) {
			this.activeManipulator.setHasUpdated( hasUpdated );
		}
		super.setHasUpdated( hasUpdated );
	}

	@Override
	public AbstractTransformable getManipulatedTransformable() {
		if( this.activeManipulator != null ) {
			return this.activeManipulator.getManipulatedTransformable();
		} else {
			return null;
		}
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		if( this.activeManipulator != null ) {
			this.activeManipulator.doDataUpdateManipulator( currentInput, previousInput );
		}
	}

	@Override
	public void triggerAllDeactivateEvents() {
		if( this.activeManipulator != null ) {
			this.activeManipulator.triggerAllDeactivateEvents();
		}
	}

	@Override
	public org.alice.interact.event.ManipulationEvent getMainManipulationEvent() {
		if( this.activeManipulator != null ) {
			return this.activeManipulator.getMainManipulationEvent();
		} else {
			return null;
		}
	}

	protected void setManipulatorStartState( AbstractManipulator manipulator, InputState startState ) {
		manipulator.setDragAdapter( this.dragAdapter );
		if( manipulator instanceof OnscreenPicturePlaneInformedManipulator ) {
			OnscreenPicturePlaneInformedManipulator lookingGlassManipulator = (OnscreenPicturePlaneInformedManipulator)manipulator;
			this.dragAdapter.setLookingGlassOnManipulator( lookingGlassManipulator );
		}
		if( manipulator instanceof CameraInformedManipulator ) {
			CameraInformedManipulator cameraInformed = (CameraInformedManipulator)manipulator;
			this.dragAdapter.setCameraOnManipulator( cameraInformed, startState );
		}
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		this.activeHandle = clickInput.getClickHandle();
		if( this.activeHandle != null ) {
			this.activeManipulator = this.activeHandle.getManipulation( clickInput );
			if( this.activeManipulator != null ) {
				//Since the active manipulator was just set, we need to set its start state properly
				setManipulatorStartState( this.activeManipulator, clickInput );
				this.activeManipulator.doClickManipulator( clickInput, previousInput );
			}
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		if( this.activeManipulator != null ) {
			this.activeManipulator.doEndManipulator( endInput, previousInput );
		}
	}

	@Override
	public void endManipulator( InputState endInput, InputState previousInput ) {
		super.endManipulator( endInput, previousInput );
		this.activeManipulator = null;
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		this.activeHandle = startInput.getClickHandle();
		if( this.activeHandle != null ) {
			this.activeManipulator = this.activeHandle.getManipulation( startInput );
			if( this.activeManipulator != null ) {
				//Since the active manipulator was just set, we need to set its start state properly
				setManipulatorStartState( this.activeManipulator, startInput );
				return this.activeManipulator.doStartManipulator( startInput );
			}
		}
		return false;
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		if( this.activeManipulator != null ) {
			this.activeManipulator.doTimeUpdateManipulator( time, currentInput );
		}
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		if( this.activeManipulator != null ) {
			return this.activeManipulator.getHandleSetToEnable();
		} else {
			return null;
		}
	}

	private AbstractManipulator activeManipulator;
	private edu.cmu.cs.dennisc.render.OnscreenRenderTarget onscreenRenderTarget;
	private ManipulationHandle activeHandle = null;
	private AbstractCamera camera = null;
}
