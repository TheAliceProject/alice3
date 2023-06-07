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

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import org.alice.stageide.sceneeditor.CameraOption;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
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
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

import java.util.Map;

// This handles the fact that we have multple cameras (perspective and orthographic) and each of those cameras
// could be set to one of several different "markers" which can be re-positioned by outside sources (and allow us to switch
// between views without losing our previous position, since the marker remains where we used to be)

public class CameraMarkerTracker implements PropertyListener, ValueListener<CameraOption> {
  private SymmetricPerspectiveCamera perspectiveCamera = null;
  private OrthographicCamera orthographicCamera = null;
  private final Animator animator;
  private PointOfViewAnimation pointOfViewAnimation = null;
  private CameraMarkerImp trackedMarker = null;
  private final StorytellingSceneEditor sceneEditor;
  private CameraMarkerImp selectedMarker = null;

  private final Map<CameraOption, CameraMarkerImp> mapViewToMarker = Maps.newHashMap();

  public CameraMarkerTracker(StorytellingSceneEditor sceneEditor, Animator animator) {
    this.sceneEditor = sceneEditor;
    this.animator = animator;
  }

  public void mapViewToMarker(CameraOption cameraOption, CameraMarkerImp cameraMarker) {
    mapViewToMarker.put(cameraOption, cameraMarker);
  }

  public CameraMarkerImp getCameraMarker(CameraOption cameraOption) {
    return mapViewToMarker.get(cameraOption);
  }

  public void setCameras(SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera) {
    if ((perspectiveCamera == null) && (trackedMarker instanceof PerspectiveCameraMarkerImp)) {
      stopTrackingCamera();
      selectedMarker = null;
    }
    this.perspectiveCamera = perspectiveCamera;
    if ((orthographicCamera == null) && (trackedMarker instanceof OrthographicCameraMarkerImp)) {
      stopTrackingCamera();
      selectedMarker = null;
    }
    if (this.orthographicCamera != null) {
      this.orthographicCamera.picturePlane.removePropertyListener(this);
    }
    this.orthographicCamera = orthographicCamera;
    if (this.orthographicCamera != null) {
      this.orthographicCamera.picturePlane.addPropertyListener(this);
    }
  }

  private boolean doEpilogue = true;

  private boolean transformsAreWithinReasonableEpsilonOfEachOther(AffineMatrix4x4 a, AffineMatrix4x4 b) {
    return a.orientation.isWithinReasonableEpsilonOf(b.orientation)
        && a.translation.isWithinReasonableEpsilonOf(b.translation);
  }

  private void animateToTargetView(AbstractCamera camera) {
    AffineMatrix4x4 currentTransform = camera.getAbsoluteTransformation();
    AffineMatrix4x4 targetTransform = selectedMarker.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);

    if (pointOfViewAnimation != null) {
      doEpilogue = false;
      pointOfViewAnimation.complete(null);
      doEpilogue = true;
    }
    if (transformsAreWithinReasonableEpsilonOfEachOther(currentTransform, targetTransform)) {
      startTrackingCamera(camera);
    } else {
      AbstractTransformable cameraParent = camera.getMovableParent();
      pointOfViewAnimation = new PointOfViewAnimation(cameraParent, AsSeenBy.SCENE, currentTransform, targetTransform) {
        @Override
        protected void epilogue() {
          if (doEpilogue) {
            startTrackingCamera(camera);
          }
        }
      };
      animator.invokeLater(pointOfViewAnimation, null);
    }
  }

  @Override
  public void valueChanged(ValueEvent<CameraOption> e) {
    if ((perspectiveCamera == null) || (orthographicCamera == null)) {
      return;
    }
    CameraMarkerImp previousMarker = selectedMarker;
    selectedMarker = getCameraMarker(e.getNextValue());
    if (previousMarker != selectedMarker) {
      stopTrackingCamera();
      if (selectedMarker != null) {
        setCameraToSelectedMarker();
      }
    }
  }

  public void startTrackingCameraView(CameraOption cameraOption) {
    if ((perspectiveCamera == null) || (orthographicCamera == null)) {
      return;
    }
    selectedMarker = getCameraMarker(cameraOption);
    if (selectedMarker != null) {
      stopTrackingCamera();
      setCameraToSelectedMarker();
    }
  }

  // we're not changing the active/selected marker, but we may have moved it
  public void updateCameraToNewMarkerLocation() {
    if ((perspectiveCamera == null) || (orthographicCamera == null)) {
      return;
    }

    if (selectedMarker != null && trackedMarker != null) {
      // because the local transform of the marker has been set outside this class, we need to temporarily unhook the
      // marker from the parent but we don't want anything to change the transform (which stopTracking would do)
      trackedMarker.getSgComposite().setParent(trackedMarker.getSgComposite().getRoot());
      trackedMarker = null;
      setCameraToSelectedMarker();
    }
  }

  private void setCameraToSelectedMarker() {
    if (selectedMarker instanceof OrthographicCameraMarkerImp) {
      sceneEditor.switchToOrthographicCamera();
      OrthographicCameraMarkerImp orthoMarker = (OrthographicCameraMarkerImp) selectedMarker;
      orthographicCamera.picturePlane.setValue(new ClippedZPlane(orthoMarker.getPicturePlane()));
      animateToTargetView(orthographicCamera);
    } else {
      sceneEditor.switchToPerspectiveCamera();
      animateToTargetView(perspectiveCamera);
    }
  }

  //Sets the given camera to the absolute orientation of the given marker
  //Parents the given marker to the camera and then zeros out the local transform
  private void startTrackingCamera(AbstractCamera camera) {
    if (trackedMarker != null && trackedMarker != selectedMarker) {
      stopTrackingCamera();
    }
    trackedMarker = selectedMarker;
    if (trackedMarker != null) {
      AbstractTransformable cameraParent = camera.getMovableParent();
      Composite root = cameraParent.getRoot();
      if (root != null) {
        cameraParent.setTransformation(this.trackedMarker.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE), root);
      } else {
        Logger.severe(cameraParent);
      }
      this.trackedMarker.setShowing(false);
      this.trackedMarker.setLocalTransformation(AffineMatrix4x4.accessIdentity());
      this.trackedMarker.getSgComposite().setParent(cameraParent);
      sceneEditor.setHandleVisibilityForObject(this.trackedMarker, false);
    }
  }

  private void stopTrackingCamera() {
    if (trackedMarker != null) {
      AffineMatrix4x4 previousMarkerTransform = trackedMarker.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);
      trackedMarker.getSgComposite().setParent(trackedMarker.getSgComposite().getRoot());
      trackedMarker.getSgComposite().setTransformation(previousMarkerTransform, AsSeenBy.SCENE);
      trackedMarker.setShowing(true);
      sceneEditor.setHandleVisibilityForObject(trackedMarker, true);
    }
    trackedMarker = null;
  }

  private boolean doesMarkerMatchCamera(CameraMarkerImp marker, AbstractCamera camera) {
    if (camera instanceof OrthographicCamera) {
      return marker instanceof OrthographicCameraMarkerImp;
    } else if (camera instanceof SymmetricPerspectiveCamera) {
      return marker instanceof PerspectiveCameraMarkerImp;
    }
    return false;
  }

  @Override
  public void propertyChanged(PropertyEvent e) {
    if ((e.getOwner() == orthographicCamera) && (e.getTypedSource() == orthographicCamera.picturePlane)) {
      if (doesMarkerMatchCamera(selectedMarker, orthographicCamera)) {
        ((OrthographicCameraMarkerImp) selectedMarker).setPicturePlane(orthographicCamera.picturePlane.getValue());
      }
    }
  }
}
