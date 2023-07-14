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
import edu.cmu.cs.dennisc.math.*;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import org.alice.ide.IDE;
import org.alice.stageide.run.RunComposite;
import org.alice.stageide.sceneeditor.CameraOption;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.alice.stageide.sceneeditor.interact.CameraNavigatorWidget;
import org.alice.stageide.sceneeditor.interact.manipulators.OrthographicCameraDragZoomManipulator;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.project.ast.UserField;
import org.lgna.story.*;
import org.lgna.story.implementation.*;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

import java.util.Map;

// This handles the fact that we have multiple cameras (perspective and orthographic) and each of those cameras
// could be set to one of several different "markers" which can be re-positioned by outside sources (and allow us to switch
// between views without losing our previous position, since the marker remains where we used to be)

public class CameraMarkerTracker implements PropertyListener, ValueListener<CameraOption> {
  private final double DEFAULT_TOP_CAMERA_Y_OFFSET = 10.0;
  private final double DEFAULT_SIDE_CAMERA_X_OFFSET = 10.0;
  private final double DEFAULT_FRONT_CAMERA_Z_OFFSET = 10.0;
  private final double DEFAULT_LAYOUT_CAMERA_Y_OFFSET = 12.0;
  private final double DEFAULT_LAYOUT_CAMERA_Z_OFFSET = 10.0;
  private final int DEFAULT_LAYOUT_CAMERA_ANGLE = -40;
  private StartingCameraMarkerConfiguration startingCamera;
  private SymmetricPerspectiveCamera perspectiveCamera = null;
  private OrthographicCamera orthographicCamera = null;
  private final Animator animator;
  private PointOfViewAnimation pointOfViewAnimation = null;
  private final StorytellingSceneEditor sceneEditor;
  private CameraMarkerImp selectedMarker = null;

  private final Map<CameraOption, CameraMarkerConfiguration<?>> mapViewToMarker = Maps.newHashMap();

  public CameraMarkerTracker(StorytellingSceneEditor sceneEditor, Animator animator) {
    this.sceneEditor = sceneEditor;
    this.animator = animator;
    initializeCameraMarkers();
  }

  public CameraMarkerImp getCameraMarker(CameraOption cameraOption) {
    return mapViewToMarker.get(cameraOption).getMarkerImp();
  }

  public void setCameras(SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera) {
    this.perspectiveCamera = perspectiveCamera;
    if (this.orthographicCamera == orthographicCamera) {
      return;
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
    CameraMarkerImp nextMarker = getCameraMarker(e.getNextValue());
    if (nextMarker != null && nextMarker != selectedMarker) {
      stopTrackingCamera();
      selectedMarker = nextMarker;
      setCameraToSelectedMarker();
    }
  }

  public void trackStartingCameraView() {
    if ((perspectiveCamera == null) || (orthographicCamera == null)) {
      return;
    }
    selectedMarker = getCameraMarker(CameraOption.STARTING_CAMERA_VIEW);
    setCameraToSelectedMarker();
  }

  // we're not changing the active/selected marker, but we may have moved it
  private void updateCameraToNewMarkerLocation() {
    if ((perspectiveCamera == null) || (orthographicCamera == null) || selectedMarker == null) {
      return;
    }

    // because the local transform of the marker has been set outside this class, we need to temporarily unhook the
    // marker from the parent, but we don't want anything to change the transform (which stopTracking would do)
    selectedMarker.getSgComposite().setParent(selectedMarker.getSgComposite().getRoot());
    setCameraToSelectedMarker();
  }

  private void setCameraToSelectedMarker() {
    if (selectedMarker instanceof OrthographicCameraMarkerImp) {
      sceneEditor.switchToOrthographicCamera();
      OrthographicCameraMarkerImp orthoMarker = (OrthographicCameraMarkerImp) selectedMarker;
      orthographicCamera.picturePlane.setValue(new ClippedZPlane(orthoMarker.getPicturePlane()));
      animateToTargetView(orthographicCamera);
    } else {
      sceneEditor.switchToPerspectiveCamera();
      animateToTargetView(perspectiveCamera); // or vrUser for main in VR mode
    }
  }

  //Sets the given camera to the absolute orientation of the given marker
  //Parents the given marker to the camera and then zeros out the local transform
  private void startTrackingCamera(AbstractCamera camera) {
    if (selectedMarker == null) {
      return;
    }
    AbstractTransformable cameraParent = camera.getMovableParent();
    Composite root = cameraParent.getRoot();
    if (root != null) {
      cameraParent.setTransformation(selectedMarker.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE), root);
    } else {
      Logger.severe(cameraParent);
    }
    selectedMarker.setShowing(false);
    selectedMarker.setLocalTransformation(AffineMatrix4x4.accessIdentity());
    selectedMarker.getSgComposite().setParent(cameraParent);
    sceneEditor.setHandleVisibilityForObject(selectedMarker, false);
  }

  private void stopTrackingCamera() {
    if (selectedMarker == null) {
      return;
    }
    AffineMatrix4x4 previousMarkerTransform = selectedMarker.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);
    selectedMarker.getSgComposite().setParent(selectedMarker.getSgComposite().getRoot());
    selectedMarker.getSgComposite().setTransformation(previousMarkerTransform, AsSeenBy.SCENE);
    selectedMarker.setShowing(true);
    sceneEditor.setHandleVisibilityForObject(selectedMarker, true);
  }

  private boolean doesMarkerMatchCamera(CameraMarkerImp marker, AbstractCamera camera) {
    if (camera instanceof OrthographicCamera) {
      return marker instanceof OrthographicCameraMarkerImp;
    } else if (camera instanceof SymmetricPerspectiveCamera) {
      return marker instanceof PerspectiveCameraMarkerImp;
    }
    return false;
  }

  private void initializeCameraMarkers() {
    startingCamera = new StartingCameraMarkerConfiguration();
    addMarker(startingCamera);
    addMarker(new LayoutCameraMarkerConfiguration());
    addMarker(new TopCameraMarkerConfiguration());
    addMarker(new SideCameraMarkerConfiguration());
    addMarker(new FrontCameraMarkerConfiguration());
  }

  private void addMarker(CameraMarkerConfiguration<?> cameraMarker) {
    mapViewToMarker.put(cameraMarker.cameraOption, cameraMarker);
  }

  public void updateMarkersForNewScene(TransformableImp startingCamera) {
    AffineMatrix4x4 openingViewTransform = startingCamera.getAbsoluteTransformation();
    for (CameraMarkerConfiguration<?> marker: mapViewToMarker.values()) {
      marker.resetForScene(openingViewTransform);
    }
  }

  public void centerMarkersOn(UserField field) {
    // Changes the markers for the layout camera and the 3 ortho camera views, not just whatever marker is active.
    Object instanceInJava = IDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForField(field);
    EntityImp target = EmployeesOnly.getImplementation((SThing) instanceInJava);
    AxisAlignedBox alignedBox = target.getDynamicAxisAlignedMinimumBoundingBox(org.lgna.story.implementation.AsSeenBy.SCENE);
    for (CameraMarkerConfiguration<?> marker: mapViewToMarker.values()) {
      marker.centerOn(alignedBox);
    }
    // Update camera to the latest markers
    updateCameraToNewMarkerLocation();
  }

  private double clampCameraValue(double val) {
    final double CAMERA_CLAMP_MAX = 100.0;
    final double CAMERA_CLAMP_MIN = 2.0;
    return clampValue(val, CAMERA_CLAMP_MIN, CAMERA_CLAMP_MAX);
  }

  private double clampPictureValue(double val) {
    // we intentionally allow a slightly larger max here, and the manipulator deals with it later.
    final double PICTURE_CLAMP_MAX = 100;
    final double PICTURE_CLAMP_MIN = OrthographicCameraDragZoomManipulator.MIN_ZOOM;
    return clampValue(val, PICTURE_CLAMP_MIN, PICTURE_CLAMP_MAX);
  }

  private double clampValue(double val, double min, double max) {
    return Math.min(max, Math.max(min, val));
  }

  public void setIsVrActive(boolean isVrScene) {
    startingCamera.setVrActive(isVrScene);
  }

  // Used only by PredeterminedSetStartCameraTransformationActionOperation
  public void setStartingCameraMarkerTransformation(AffineMatrix4x4 transform) {
    getStartingCameraMarkerImp().setLocalTransformation(transform);
  }

  public PerspectiveCameraMarkerImp getStartingCameraMarkerImp() {
    return startingCamera.getMarkerImp();
  }

  @Override
  public void propertyChanged(PropertyEvent e) {
    if ((e.getOwner() == orthographicCamera) && (e.getTypedSource() == orthographicCamera.picturePlane)) {
      if (doesMarkerMatchCamera(selectedMarker, orthographicCamera)) {
        ((OrthographicCameraMarkerImp) selectedMarker).setPicturePlane(orthographicCamera.picturePlane.getValue());
      }
    }
  }

  private abstract static class CameraMarkerConfiguration<T extends CameraMarkerImp> {
    final T markerImp;
    final CameraOption cameraOption;
    final CameraNavigatorWidget.CameraMode cameraMode;
    public CameraMarkerConfiguration(CameraMarker marker, CameraOption cameraOption, CameraNavigatorWidget.CameraMode cameraMode, String iconName) {
      this.cameraOption = cameraOption;
      this.cameraMode = cameraMode;
      markerImp = EmployeesOnly.getImplementation(marker);
      MarkerUtilities.addIconForCamera(marker, iconName);
      MarkerUtilities.setViewForCamera(marker, cameraOption);
      markerImp.getAbstraction().setName(MarkerUtilities.getNameForCameraImp(markerImp));
      initialize();
    }

    protected abstract void initialize();
    protected abstract void centerOn(AxisAlignedBox box);

    T getMarkerImp() {
      return markerImp;
    }

    public void resetForScene(AffineMatrix4x4 startingView) {
      // Ortho cameras could override to focus on the starting camera or the overall scene
      // Default to initialize to remove any changes from previous scene editing
      initialize();
    }
  }

  class StartingCameraMarkerConfiguration extends CameraMarkerConfiguration<PerspectiveCameraMarkerImp> {
    public StartingCameraMarkerConfiguration() {
      super(new PerspectiveCameraMarker(), CameraOption.STARTING_CAMERA_VIEW, CameraNavigatorWidget.CameraMode.PERSPECTIVE, "mainCamera");
    }

    @Override
    protected void initialize() {
      markerImp.setVrActive(sceneEditor.isVrActive());
      markerImp.getAbstraction().setColorId(Color.DARK_GRAY);
      markerImp.setDisplayVisuals(true);
    }

    @Override
    protected void centerOn(AxisAlignedBox box) {
      // This is for the staging cameras. The starting camera does not move.
    }

    @Override
    public void resetForScene(AffineMatrix4x4 startingView) {
      markerImp.setLocalTransformation(startingView);
    }

    public void setVrActive(boolean isVrScene) {
      markerImp.setVrActive(isVrScene);
      // Update menu icon
      MarkerUtilities.addIconForCameraImp(markerImp, isVrScene ? "vrHeadset" : "mainCamera");
    }
  }

  class LayoutCameraMarkerConfiguration extends CameraMarkerConfiguration<PerspectiveCameraMarkerImp> {
    public LayoutCameraMarkerConfiguration() {
      super(new PerspectiveCameraMarker(), CameraOption.LAYOUT_SCENE_VIEW, CameraNavigatorWidget.CameraMode.PERSPECTIVE, "sceneEditorCamera");
    }

    @Override
    protected void initialize() {
      markerImp.getAbstraction().setColorId(Color.LIGHT_BLUE);
      markerImp.setDisplayVisuals(true);
    }

    @Override
    protected void centerOn(AxisAlignedBox box) {
      // Attempting to find the least disruptive move, ideally a bit above of the object (because looking up through the
      // object/ground feels bad) and far enough away that we can see most of it, but hopefully close enough that there
      // isn't another object in between. Usually we succeed.
      double targetHeight = box.getHeight();
      Point3 targetTranslation = box.getCenter();

      // Since targetTranslation is already centered in y, this is effectively height * 1.5.
      double adjustedY = targetTranslation.y + Math.max(targetHeight, box.getDiagonal() * .5);
      Point3 adjustedPos = new Point3(targetTranslation.x,  adjustedY, targetTranslation.z);

      // if the camera is already above it, great. Otherwise we get the best results by using the same adjusted y.
      Point3 adjustedCameraPos = markerImp.getAbsoluteTransformation().translation;
      adjustedCameraPos.y = Math.max(adjustedCameraPos.y, adjustedY);

      Vector3 direction = Vector3.createSubtraction(adjustedCameraPos, adjustedPos);
      direction.normalize();
      Ray ray = new Ray(adjustedPos, direction);
      Point3 layoutCamTranslation = ray.getPointAlong(box.getDiagonal() * 1.5);

      // orientation calculated from wherever our camera ended up to look at the center of the object.
      Vector3 cameraDirection = Vector3.createSubtraction(layoutCamTranslation, targetTranslation);
      cameraDirection.normalize();
      ForwardAndUpGuide forwardAndUpGuide = new ForwardAndUpGuide(Vector3.createNegation(cameraDirection), null);
      OrthogonalMatrix3x3 layoutCamOrientation = forwardAndUpGuide.createOrthogonalMatrix3x3();

      AffineMatrix4x4 layoutTransform = AffineMatrix4x4.createIdentity();
      layoutTransform.applyTranslation(layoutCamTranslation);
      layoutTransform.applyOrientation(layoutCamOrientation);
      markerImp.setLocalTransformation(layoutTransform);
    }

    @Override
    public void resetForScene(AffineMatrix4x4 startingView) {
      AffineMatrix4x4 layoutTransform = new AffineMatrix4x4(startingView);
      layoutTransform.applyTranslationAlongYAxis(DEFAULT_LAYOUT_CAMERA_Y_OFFSET);
      layoutTransform.applyTranslationAlongZAxis(DEFAULT_LAYOUT_CAMERA_Z_OFFSET);
      layoutTransform.applyRotationAboutXAxis(new AngleInDegrees(DEFAULT_LAYOUT_CAMERA_ANGLE));
      markerImp.setLocalTransformation(layoutTransform);
    }
  }

  class TopCameraMarkerConfiguration extends CameraMarkerConfiguration<OrthographicCameraMarkerImp> {
    public TopCameraMarkerConfiguration() {
      super(new OrthographicCameraMarker(), CameraOption.TOP, CameraNavigatorWidget.CameraMode.ORTHOGRAPHIC, "top");
    }

    @Override
    protected void initialize() {
      AffineMatrix4x4 topTransform = AffineMatrix4x4.createIdentity();
      topTransform.translation.y = 10;
      topTransform.translation.z = -10;
      topTransform.orientation.up.set(0, 0, 1);
      topTransform.orientation.right.set(-1, 0, 0);
      topTransform.orientation.backward.set(0, 1, 0);
      assert topTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
      markerImp.setLocalTransformation(topTransform);
      ClippedZPlane picturePlane = new ClippedZPlane();
      picturePlane.setCenter(0, 0);
      picturePlane.setHeight(16);
      markerImp.setPicturePlane(picturePlane);
    }

    @Override
    protected void centerOn(AxisAlignedBox box) {
      double targetHeight = box.getHeight();
      double targetWidth = box.getWidth();
      double targetDepth = box.getDepth();
      Point3 targetTranslation = box.getCenter();

      // Update Orthographic camera markers
      // Translation helps clip things that might be over our object, based on how large it is. (It doesn't always succeed)

      AffineMatrix4x4 topTransform = AffineMatrix4x4.createIdentity();
      topTransform.translation.x = targetTranslation.x;
      topTransform.translation.y = targetTranslation.y + (targetHeight != 0 ? clampCameraValue(targetHeight * DEFAULT_TOP_CAMERA_Y_OFFSET) : DEFAULT_TOP_CAMERA_Y_OFFSET);
      topTransform.translation.z = targetTranslation.z;
      topTransform.orientation.up.set(0, 0, 1);
      topTransform.orientation.right.set(-1, 0, 0);
      topTransform.orientation.backward.set(0, 1, 0);
      markerImp.setLocalTransformation(topTransform);
      // PicturePlane controls how much of the scene is in our view, aka 'zoom'
      ClippedZPlane picturePlane = new ClippedZPlane();
      picturePlane.setCenter(0, 0);
      picturePlane.setHeight(clampPictureValue(Math.max(targetDepth, RunComposite.WIDTH_TO_HEIGHT_RATIO * targetWidth)));
      markerImp.setPicturePlane(picturePlane);
    }
  }

  class SideCameraMarkerConfiguration extends CameraMarkerConfiguration<OrthographicCameraMarkerImp> {
    public SideCameraMarkerConfiguration() {
      super(new OrthographicCameraMarker(), CameraOption.SIDE, CameraNavigatorWidget.CameraMode.ORTHOGRAPHIC, "side");
    }

    @Override
    protected void initialize() {
      AffineMatrix4x4 sideTransform = AffineMatrix4x4.createIdentity();
      sideTransform.translation.x = 10;
      sideTransform.translation.y = 1;
      sideTransform.orientation.setValue(new ForwardAndUpGuide(Vector3.accessNegativeXAxis(), Vector3.accessPositiveYAxis()));
      assert sideTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
      markerImp.setLocalTransformation(sideTransform);
      ClippedZPlane picturePlane = new ClippedZPlane();
      picturePlane.setHeight(4);
      markerImp.setPicturePlane(picturePlane);
    }

    @Override
    protected void centerOn(AxisAlignedBox box) {
      double targetHeight = box.getHeight();
      double targetWidth = box.getWidth();
      double targetDepth = box.getDepth();
      Point3 targetTranslation = box.getCenter();

      // Translation helps clip things that might be over our object, based on how large it is. (It doesn't always succeed)
      AffineMatrix4x4 sideTransform = AffineMatrix4x4.createIdentity();
      sideTransform.translation.x = targetTranslation.x + (targetWidth != 0 ? clampCameraValue(targetWidth * DEFAULT_SIDE_CAMERA_X_OFFSET) : DEFAULT_SIDE_CAMERA_X_OFFSET);
      sideTransform.translation.y = targetTranslation.y;
      sideTransform.translation.z = targetTranslation.z;
      sideTransform.orientation.setValue(new ForwardAndUpGuide(Vector3.accessNegativeXAxis(), Vector3.accessPositiveYAxis()));
      markerImp.setLocalTransformation(sideTransform);

      // PicturePlane controls how much of the scene is in our view, aka 'zoom'
      ClippedZPlane picturePlane = new ClippedZPlane();
      picturePlane.setHeight(clampPictureValue(Math.max(targetDepth, RunComposite.WIDTH_TO_HEIGHT_RATIO * targetHeight)));
      markerImp.setPicturePlane(picturePlane);
    }
  }

  class FrontCameraMarkerConfiguration extends CameraMarkerConfiguration<OrthographicCameraMarkerImp> {
    public FrontCameraMarkerConfiguration() {
      super(new OrthographicCameraMarker(), CameraOption.FRONT, CameraNavigatorWidget.CameraMode.ORTHOGRAPHIC, "front");
    }

    @Override
    protected void initialize() {
      AffineMatrix4x4 frontTransform = AffineMatrix4x4.createIdentity();
      frontTransform.translation.z = -10;
      frontTransform.translation.y = 1;
      frontTransform.orientation.setValue(new ForwardAndUpGuide(Vector3.accessPositiveZAxis(), Vector3.accessPositiveYAxis()));
      assert frontTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
      markerImp.setLocalTransformation(frontTransform);
      ClippedZPlane picturePlane = new ClippedZPlane();
      picturePlane.setHeight(4);
      markerImp.setPicturePlane(picturePlane);
    }

    @Override
    protected void centerOn(AxisAlignedBox box) {
      double targetHeight = box.getHeight();
      double targetWidth = box.getWidth();
      double targetDepth = box.getDepth();
      Point3 targetTranslation = box.getCenter();

      // Translation helps clip things that might be over our object, based on how large it is. (It doesn't always succeed)
      AffineMatrix4x4 frontTransform = AffineMatrix4x4.createIdentity();
      frontTransform.translation.x = targetTranslation.x;
      frontTransform.translation.y = targetTranslation.y;
      frontTransform.translation.z = targetTranslation.z - (targetDepth != 0 ? clampCameraValue(targetDepth * DEFAULT_FRONT_CAMERA_Z_OFFSET) : DEFAULT_FRONT_CAMERA_Z_OFFSET);
      frontTransform.orientation.setValue(new ForwardAndUpGuide(Vector3.accessPositiveZAxis(), Vector3.accessPositiveYAxis()));
      markerImp.setLocalTransformation(frontTransform);

      // PicturePlane controls how much of the scene is in our view, aka 'zoom'
      ClippedZPlane picturePlane = new ClippedZPlane();
      picturePlane.setHeight(clampPictureValue(Math.max(targetWidth, RunComposite.WIDTH_TO_HEIGHT_RATIO * targetHeight)));
      markerImp.setPicturePlane(picturePlane);
    }
  }
}
