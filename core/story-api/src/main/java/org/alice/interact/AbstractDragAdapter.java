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

package org.alice.interact;

import org.alice.interact.InteractionGroup.InteractionInfo;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.event.SelectionEvent;
import org.alice.interact.handle.HandleStyle;
import org.alice.interact.handle.ManipulationHandle;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.CameraInformedManipulator;
import org.alice.interact.manipulator.OnscreenPicturePlaneInformedManipulator;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraMarkerImp;
import org.lgna.story.implementation.ObjectMarkerImp;
import org.lgna.story.implementation.PerspectiveCameraMarkerImp;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

/**
 * @author David Culyba
 */
public abstract class AbstractDragAdapter extends HandleSupportingDragAdapter {
	public static enum CameraView {
		MAIN,
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		ACTIVE_VIEW,
		PICK_CAMERA
	}

	private static final class CameraPair {
		public CameraPair( SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera ) {
			this.perspectiveCamera = perspectiveCamera;
			this.orthographicCamera = orthographicCamera;
		}

		public void setActiveCamera( AbstractCamera camera ) {
			this.activeCamera = camera;
		}

		public AbstractCamera getActiveCamera() {
			return this.activeCamera;
		}

		public boolean hasCamera( AbstractCamera camera ) {
			return ( this.perspectiveCamera == camera ) || ( this.orthographicCamera == camera );
		}

		private final SymmetricPerspectiveCamera perspectiveCamera;
		private final OrthographicCamera orthographicCamera;

		private AbstractCamera activeCamera;
	}

	public static final edu.cmu.cs.dennisc.scenegraph.Element.Key<edu.cmu.cs.dennisc.math.AxisAlignedBox> BOUNDING_BOX_KEY = edu.cmu.cs.dennisc.scenegraph.Element.Key.createInstance( "BOUNDING_BOX_KEY" );

	private static final double MARKER_SELECTION_DURATION = .25;

	protected void preUndo() {
		stopMouseWheel();
		fireStateChange();
	}

	public boolean hasSceneEditor() {
		return false;
	}

	public void clear() {
		this.clearCameraViews();
		this.handleManager.clear();
	}

	public void clearCameraViews() {
		for( CameraPair cameraPair : this.cameraMap.values() ) {
			if( cameraPair.perspectiveCamera != null ) {
				cameraPair.perspectiveCamera.removeAbsoluteTransformationListener( this.cameraTransformationListener );
			}
		}
		this.cameraMap.clear();
	}

	public void addCameraView( CameraView viewType, SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera ) {
		addCameraView( viewType, new CameraPair( perspectiveCamera, orthographicCamera ) );
	}

	private void addCameraView( CameraView viewType, CameraPair cameras ) {
		if( cameras.perspectiveCamera != null ) {
			cameras.perspectiveCamera.addAbsoluteTransformationListener( this.cameraTransformationListener );
			this.handleManager.updateCameraPosition( cameras.perspectiveCamera.getAbsoluteTransformation().translation );
		}
		this.cameraMap.put( viewType, cameras );
	}

	public void setSelectedCameraMarker( CameraMarkerImp selected ) {
		if( selected != this.selectedCameraMarker ) {
			this.fireSelecting( new SelectionEvent( this, selected ) );
			if( this.selectedCameraMarker != null ) {
				this.selectedCameraMarker.opacity.setValue( .3f );
				if( this.selectedCameraMarker instanceof PerspectiveCameraMarkerImp ) {
					( (PerspectiveCameraMarkerImp)this.selectedCameraMarker ).setDetailedViewShowing( false );
				}
			}
			this.selectedCameraMarker = selected;
			if( this.selectedCameraMarker != null ) {
				this.selectedCameraMarker.opacity.setValue( 1f );
				if( this.hasSceneEditor() && ( this.selectedCameraMarker instanceof PerspectiveCameraMarkerImp ) ) {
					//TODO: Resolve the issue of showing the selection details of an active camera mark (active meaning it's currently attached to the camera)
					//					boolean isNewSelectedActiveCameraMarker = this.sceneEditor.isCameraMarkerActive(this.selectedCameraMarker);
					//					if (!isNewSelectedActiveCameraMarker) {
					( (PerspectiveCameraMarkerImp)this.selectedCameraMarker ).setDetailedViewShowing( true );
					//}
				}
			}
		}
	}

	public void setSelectedObjectMarker( ObjectMarkerImp selected ) {
		if( selected != this.selectedObjectMarker ) {
			this.fireSelecting( new SelectionEvent( this, selected ) );
			if( this.selectedObjectMarker != null ) {
				this.selectedObjectMarker.opacity.setValue( .3f );
			}
			this.selectedObjectMarker = selected;
			if( this.selectedObjectMarker != null ) {
				this.selectedObjectMarker.opacity.setValue( 1f );
			}
		}
	}

	protected void setHandleSelectionState( org.alice.interact.handle.HandleStyle handleStyle ) {
		//Default behavior is to set the interaction state directly
		//CroquetSupportingDragAdapter sets the croquet selection state object to make this happen
		this.setInteractionState( handleStyle );
	}

	@Override
	public void setSelectedImplementation( AbstractTransformableImp selected ) {
		if( this.isInStateChange() ) {
			this.setToBeSelected( selected );
			return;
		}
		if( selected != null ) {
			if( selected.getSgComposite() instanceof Joint ) {
				//If we're selecting a joint for the first time or from a selection that wasn't a joint, set the handle state to rotation
				if( ( this.selectedObject == null ) || !( this.selectedObject.getSgComposite() instanceof Joint ) ) {
					if( this.getDefaultJointHandleStyle() != null ) {
						this.setHandleSelectionState( this.getDefaultJointHandleStyle() );
					}
				}
			}
			if( selected instanceof ObjectMarkerImp ) {
				setSelectedObjectMarker( (ObjectMarkerImp)selected );
			} else if( selected instanceof CameraMarkerImp ) {
				setSelectedCameraMarker( (CameraMarkerImp)selected );
			} else {
				setSelectedSceneObjectImplementation( selected );
			}

			//If the current handle state is null, force it to update by setting the interaction state again
			//The handle state can be null if the previous selected object didn't match the selected state
			if( this.handleManager.getCurrentHandleSet() == null ) {
				this.setCurrentInterationState( this.currentInteractionState );
			}
			updateHandleSelection( selected );
		} else {
			setSelectedSceneObjectImplementation( null );
		}
	}

	//	public void setSelectedSgTransformable( Transformable selected )
	//	{
	//		if (this.selectedObject != selected)
	//		{
	//			this.fireSelecting( new SelectionEvent(this, selected) );
	//			if (HandleManager.canHaveHandles(selected))
	//			{
	//				this.handleManager.setHandlesShowing(true);
	//				this.handleManager.setSelectedObject( selected );
	//			}
	//			else
	//			{
	//				this.handleManager.setSelectedObject( null );
	//			}
	//			this.currentInputState.setCurrentlySelectedObject( selected );
	//			this.currentInputState.setTimeCaptured();
	//			selectedObject = selected;
	//			this.handleStateChange();
	//		}
	//	}

	protected void setCurrentInterationState( InteractionGroup interactionState ) {
		this.currentInteractionState = interactionState;
		if( this.currentInteractionState != null ) {
			InteractionInfo interactionInfo = this.currentInteractionState.getMatchingInfo( ObjectType.getObjectType( this.selectedObject ) );
			if( interactionInfo != null ) {
				this.handleManager.setHandleSet( interactionInfo.getHandleSet() );
			}
			this.currentInteractionState.enabledManipulators( true );
		}
	}

	public void setInteractionState( org.alice.interact.handle.HandleStyle handleStyle ) {
		if( this.currentInteractionState != null ) {
			this.currentInteractionState.enabledManipulators( false );
		}
		setCurrentInterationState( this.mapHandleStyleToInteractionGroup.get( handleStyle ) );
	}

	public void makeCameraActive( AbstractCamera camera ) {
		boolean activated = false;
		for( java.util.Map.Entry<CameraView, CameraPair> cameras : this.cameraMap.entrySet() ) {
			if( cameras.getValue().hasCamera( camera ) ) {
				cameras.getValue().setActiveCamera( camera );
				activated = true;
			}
		}
		if( camera instanceof SymmetricPerspectiveCamera ) {
			this.handleManager.updateCameraPosition( camera.getAbsoluteTransformation().translation );
		} else {
			this.handleManager.updateCameraPosition( null );
		}
	}

	public AbstractCamera getActiveCamera() {
		//TODO: introduce a true sense of "active"
		CameraPair activeCameraPair = this.cameraMap.get( CameraView.MAIN );
		if( ( activeCameraPair != null ) && ( activeCameraPair.getActiveCamera() != null ) ) {
			return activeCameraPair.getActiveCamera();
		} else {
			return null;
		}
	}

	public AbstractCamera getCameraForManipulator( CameraInformedManipulator cameraManipulator ) {
		CameraView cameraView = cameraManipulator.getDesiredCameraView();
		AbstractCamera cameraToReturn;
		if( ( cameraView == CameraView.ACTIVE_VIEW ) || ( cameraView == CameraView.PICK_CAMERA ) ) {
			cameraToReturn = getActiveCamera();
		} else {
			CameraPair cameras = this.cameraMap.get( cameraView );
			if( cameras != null ) {
				cameraToReturn = cameras.getActiveCamera();
			} else {
				cameraToReturn = null;
			}
		}
		return cameraToReturn;
	}

	public void setCameraOnManipulator( CameraInformedManipulator manipulator, InputState startInput ) {
		//The pick camera can be null if we roll over a 2D handle while we're moving
		if( ( manipulator.getDesiredCameraView() == CameraView.PICK_CAMERA ) && ( this.currentInputState.getPickCamera() != null ) ) {
			manipulator.setCamera( this.currentInputState.getPickCamera() );
		} else {
			manipulator.setCamera( this.getCameraForManipulator( manipulator ) );
		}
	}

	@Override
	protected void setManipulatorStartState( AbstractManipulator manipulator, InputState startState ) {
		if( manipulator instanceof OnscreenPicturePlaneInformedManipulator ) {
			OnscreenPicturePlaneInformedManipulator lookingGlassManipulator = (OnscreenPicturePlaneInformedManipulator)manipulator;
			this.setLookingGlassOnManipulator( lookingGlassManipulator );
		}
		if( manipulator instanceof CameraInformedManipulator ) {
			CameraInformedManipulator cameraInformed = (CameraInformedManipulator)manipulator;
			this.setCameraOnManipulator( cameraInformed, startState );
		}
	}

	public void setSGCamera( AbstractCamera camera ) {
		//		assert camera != null;
		//		System.out.println("Setting Camera from DragAdapter: ");
		//		Thread.dumpStack();
		//		for (int i=0; i<this.manipulators.size(); i++)
		//		{
		//			if (this.manipulators.get( i ).getManipulator() instanceof CameraInformedManipulator)
		//			{
		//				CameraInformedManipulator cameraManipulator = ((CameraInformedManipulator)this.manipulators.get( i ).getManipulator());
		//				cameraManipulator.setCamera( getCameraForManipulator( cameraManipulator ) );
		//			}
		//			if (this.manipulators.get( i ).getManipulator() instanceof OnScreenLookingGlassInformedManipulator)
		//			{
		//				((OnScreenLookingGlassInformedManipulator)this.manipulators.get( i ).getManipulator()).setOnscreenLookingGlass( getOnscreenLookingGlass() );
		//			}
		//		}
	}

	private void update( double timeDelta ) {
		if( isMouseWheelActive() ) {
			mouseWheelTimeoutTime -= timeDelta;
			if( !isMouseWheelActive() ) {
				stopMouseWheel();
				fireStateChange();
			}
		}
		for( int i = 0; i < this.manipulators.size(); i++ ) {
			ManipulatorConditionSet currentManipulatorSet = this.manipulators.get( i );
			if( currentManipulatorSet.getManipulator().hasStarted() && currentManipulatorSet.shouldContinue( this.currentInputState, this.previousInputState ) ) {
				currentManipulatorSet.getManipulator().timeUpdateManipulator( timeDelta, this.currentInputState );
			}
		}
	}

	@Override
	protected void handleAutomaticDisplayCompleted( edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent e ) {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			if( !hasSetCameraTransformables ) {
				setSGCamera( sgCamera );
				hasSetCameraTransformables = true;
			}
			double timeCurr = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
			if( Double.isNaN( this.timePrev ) ) {
				this.timePrev = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
			}
			double timeDelta = timeCurr - this.timePrev;
			update( timeDelta );
			this.timePrev = timeCurr;
		}
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
		edu.cmu.cs.dennisc.render.OnscreenRenderTarget<?> onscreenRenderTarget = this.getOnscreenRenderTarget();
		if( onscreenRenderTarget != null ) {
			if( this.cameraIndex < onscreenRenderTarget.getSgCameraCount() ) {
				return onscreenRenderTarget.getSgCameraAt( this.cameraIndex );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public edu.cmu.cs.dennisc.scenegraph.Transformable getSGCameraTransformable() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			return (edu.cmu.cs.dennisc.scenegraph.Transformable)sgCamera.getParent();
		} else {
			return null;
		}
	}

	public void addHandle( ManipulationHandle handle ) {
		this.handleManager.addHandle( handle );
	}

	protected HandleStyle getDefaultJointHandleStyle() {
		return org.alice.interact.handle.HandleStyle.ROTATION;
	}

	public abstract boolean shouldSnapToGround();

	public abstract boolean shouldSnapToGrid();

	public abstract boolean shouldSnapToRotation();

	public abstract double getGridSpacing();

	public abstract edu.cmu.cs.dennisc.math.Angle getRotationSnapAngle();

	public abstract void undoRedoEndManipulation( AbstractManipulator manipulator, AffineMatrix4x4 originalTransformation );

	private final AbsoluteTransformationListener cameraTransformationListener = new AbsoluteTransformationListener() {
		@Override
		public void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent ) {
			if( absoluteTransformationEvent.getSource() instanceof SymmetricPerspectiveCamera ) {
				SymmetricPerspectiveCamera camera = (SymmetricPerspectiveCamera)absoluteTransformationEvent.getSource();
				AbstractDragAdapter.this.handleManager.updateCameraPosition( camera.getAbsoluteTransformation().translation );
			}
		}
	};

	private double timePrev = Double.NaN;
	private boolean hasSetCameraTransformables = false;

	private InteractionGroup currentInteractionState = null;

	private CameraMarkerImp selectedCameraMarker = null;
	private ObjectMarkerImp selectedObjectMarker = null;

	private int cameraIndex = 0;

	protected final java.util.Map<org.alice.interact.handle.HandleStyle, InteractionGroup> mapHandleStyleToInteractionGroup = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private final java.util.Map<CameraView, CameraPair> cameraMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
}
