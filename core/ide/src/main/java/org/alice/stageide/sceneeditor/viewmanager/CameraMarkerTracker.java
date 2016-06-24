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

package org.alice.stageide.sceneeditor.viewmanager;

import org.lgna.story.CameraMarker;
import org.lgna.story.implementation.CameraMarkerImp;
import org.lgna.story.implementation.OrthographicCameraMarkerImp;
import org.lgna.story.implementation.PerspectiveCameraMarkerImp;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class CameraMarkerTracker implements PropertyListener, org.lgna.croquet.event.ValueListener<org.alice.stageide.sceneeditor.View>
{
	private SymmetricPerspectiveCamera perspectiveCamera = null;
	private OrthographicCamera orthographicCamera = null;
	private Animator animator;
	private PointOfViewAnimation pointOfViewAnimation = null;
	private CameraMarkerImp markerToUpdate = null;
	private boolean shouldAnimate = true;
	private org.alice.stageide.sceneeditor.StorytellingSceneEditor sceneEditor;
	private CameraMarkerImp activeMarker = null;

	private java.util.Map<org.alice.stageide.sceneeditor.View, CameraMarkerImp> mapViewToMarker = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private java.util.Map<CameraMarkerImp, org.alice.stageide.sceneeditor.View> mapMarkerToView = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public CameraMarkerTracker( org.alice.stageide.sceneeditor.StorytellingSceneEditor sceneEditor, Animator animator )
	{
		this.sceneEditor = sceneEditor;
		this.animator = animator;
	}

	public void mapViewToMarkerAndViceVersa( org.alice.stageide.sceneeditor.View view, CameraMarkerImp cameraMarker ) {
		this.mapViewToMarker.put( view, cameraMarker );
		this.mapMarkerToView.put( cameraMarker, view );
	}

	public CameraMarkerImp getCameraMarker( org.alice.stageide.sceneeditor.View view ) {
		return this.mapViewToMarker.get( view );
	}

	public org.alice.stageide.sceneeditor.View getView( CameraMarker cameraMarker ) {
		return this.mapMarkerToView.get( cameraMarker );
	}

	public void setCameras( SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera )
	{
		if( ( perspectiveCamera == null ) && ( this.markerToUpdate instanceof PerspectiveCameraMarkerImp ) )
		{
			this.stopTrackingCamera();
			this.activeMarker = null;
		}
		this.perspectiveCamera = perspectiveCamera;
		if( ( orthographicCamera == null ) && ( this.markerToUpdate instanceof OrthographicCameraMarkerImp ) )
		{
			this.stopTrackingCamera();
			this.activeMarker = null;
		}
		if( this.orthographicCamera != null )
		{
			this.orthographicCamera.picturePlane.removePropertyListener( this );
		}
		this.orthographicCamera = orthographicCamera;
		if( this.orthographicCamera != null )
		{
			this.orthographicCamera.picturePlane.addPropertyListener( this );
		}
	}

	public boolean getShouldAnimate()
	{
		return this.shouldAnimate;
	}

	public void setShouldAnimate( boolean shouldAnimate )
	{
		this.shouldAnimate = shouldAnimate;
	}

	private boolean doEpilogue = true;

	private boolean transformsAreWithinReasonableEpsilonOfEachOther( AffineMatrix4x4 a, AffineMatrix4x4 b )
	{
		if( a.orientation.isWithinReasonableEpsilonOf( b.orientation ) )
		{
			return a.translation.isWithinReasonableEpsilonOf( b.translation );
		}
		return false;
	}

	private void animateToTargetView()
	{
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 currentTransform = this.perspectiveCamera.getAbsoluteTransformation();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 targetTransform = this.activeMarker.getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE );

		if( this.pointOfViewAnimation != null )
		{
			this.doEpilogue = false;
			this.pointOfViewAnimation.complete( null );
			this.doEpilogue = true;
		}
		if( !this.shouldAnimate || transformsAreWithinReasonableEpsilonOfEachOther( currentTransform, targetTransform ) )
		{
			startTrackingCamera( CameraMarkerTracker.this.perspectiveCamera, CameraMarkerTracker.this.activeMarker );
		}
		else
		{
			Transformable cameraParent = (Transformable)this.perspectiveCamera.getParent();
			this.pointOfViewAnimation = new PointOfViewAnimation( cameraParent, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE, currentTransform, targetTransform )
			{
				@Override
				protected void epilogue()
				{
					if( CameraMarkerTracker.this.doEpilogue )
					{
						startTrackingCamera( CameraMarkerTracker.this.perspectiveCamera, CameraMarkerTracker.this.activeMarker );
					}
				}
			};
			this.animator.invokeLater( this.pointOfViewAnimation, null );
		}
	}

	private void switchToOrthographicView()
	{
		this.sceneEditor.switchToOthographicCamera();
	}

	private void switchToPerspectiveView()
	{
		this.sceneEditor.switchToPerspectiveCamera();
	}

	@Override
	public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.stageide.sceneeditor.View> e ) {
		if( ( this.perspectiveCamera == null ) || ( this.orthographicCamera == null ) )
		{
			return;
		}
		else
		{
			CameraMarkerImp previousMarker = this.activeMarker;
			this.activeMarker = this.getCameraMarker( e.getNextValue() );
			if( previousMarker != this.activeMarker )
			{
				stopTrackingCamera();
				if( this.activeMarker != null )
				{
					setCameraToSelectedMarker();
				}
			}
		}
	}

	public void startTrackingCameraView( org.alice.stageide.sceneeditor.View view ) {
		if( ( this.perspectiveCamera == null ) || ( this.orthographicCamera == null ) )
		{
			return;
		}
		else
		{
			this.activeMarker = this.getCameraMarker( view );
			if( this.activeMarker != null )
			{
				setCameraToSelectedMarker();
			}
		}
	}

	private void setCameraToSelectedMarker()
	{
		stopTrackingCamera();
		if( this.activeMarker instanceof OrthographicCameraMarkerImp )
		{
			OrthographicCameraMarkerImp orthoMarker = (OrthographicCameraMarkerImp)this.activeMarker;
			switchToOrthographicView();
			Transformable cameraParent = (Transformable)CameraMarkerTracker.this.orthographicCamera.getParent();
			CameraMarkerTracker.this.orthographicCamera.picturePlane.setValue( new ClippedZPlane( orthoMarker.getPicturePlane() ) );
			cameraParent.setTransformation( CameraMarkerTracker.this.activeMarker.getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE ), CameraMarkerTracker.this.orthographicCamera.getRoot() );
			startTrackingCamera( this.orthographicCamera, orthoMarker );
		}
		else
		{
			switchToPerspectiveView();
			animateToTargetView();
		}
	}

	//Sets the given camera to the absolute orientation of the given marker
	//Parents the given marker to the camera and then zeros out the local transform
	private void startTrackingCamera( AbstractCamera camera, CameraMarkerImp markerToUpdate )
	{
		if( this.markerToUpdate != null )
		{
			stopTrackingCamera();
		}
		this.markerToUpdate = markerToUpdate;
		if( this.markerToUpdate != null )
		{
			Transformable cameraParent = (Transformable)camera.getParent();
			edu.cmu.cs.dennisc.scenegraph.Composite root = cameraParent.getRoot();
			if( root != null ) {
				cameraParent.setTransformation( this.markerToUpdate.getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE ), root );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( cameraParent );
			}
			this.markerToUpdate.setShowing( false );
			this.markerToUpdate.setLocalTransformation( AffineMatrix4x4.accessIdentity() );
			this.markerToUpdate.getSgComposite().setParent( cameraParent );
			this.sceneEditor.setHandleVisibilityForObject( this.markerToUpdate, false );
		}
	}

	private void stopTrackingCamera()
	{
		if( this.markerToUpdate != null )
		{
			AffineMatrix4x4 previousMarkerTransform = this.markerToUpdate.getTransformation( org.lgna.story.implementation.AsSeenBy.SCENE );
			this.markerToUpdate.getSgComposite().setParent( this.markerToUpdate.getSgComposite().getRoot() );
			this.markerToUpdate.getSgComposite().setTransformation( previousMarkerTransform, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
			this.markerToUpdate.setShowing( true );
			this.sceneEditor.setHandleVisibilityForObject( this.markerToUpdate, true );
		}
		this.markerToUpdate = null;
	}

	private boolean doesMarkerMatchCamera( CameraMarkerImp marker, AbstractCamera camera )
	{
		if( camera instanceof OrthographicCamera )
		{
			return marker instanceof OrthographicCameraMarkerImp;
		}
		else if( camera instanceof SymmetricPerspectiveCamera )
		{
			return marker instanceof PerspectiveCameraMarkerImp;
		}
		return false;
	}

	@Override
	public void propertyChanged( PropertyEvent e ) {
		if( ( e.getOwner() == this.orthographicCamera ) && ( e.getTypedSource() == this.orthographicCamera.picturePlane ) )
		{
			if( doesMarkerMatchCamera( this.activeMarker, this.orthographicCamera ) )
			{
				( (OrthographicCameraMarkerImp)this.activeMarker ).setPicturePlane( this.orthographicCamera.picturePlane.getValue() );
			}
		}
	}

	@Override
	public void propertyChanging( PropertyEvent e )
	{
		//Do Nothing
	}
}
