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

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.*;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.*;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.alice.ide.IDE;
import org.alice.stageide.run.RunComposite;
import org.alice.stageide.sceneeditor.CameraOption;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.alice.stageide.sceneeditor.viewmanager.edits.MoveTransformableEdit;
import org.lgna.croquet.EditOperation;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.UserField;
import org.lgna.story.*;
import org.lgna.story.implementation.*;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation;

import java.util.Map;
import java.util.Objects;

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
  private StartingCameraMarkerConfiguration startingCameraConfig;
  private SymmetricPerspectiveCamera mainCamera = null;
  private SymmetricPerspectiveCamera layoutCamera = null;
  private OrthographicCamera orthographicCamera = null;
  private final Animator animator;
  private final StorytellingSceneEditor sceneEditor;
  private CameraMarkerConfiguration<?> activeMarker;

  private final Map<CameraOption, CameraMarkerConfiguration<?>> mapViewToMarker = Maps.newHashMap();

  public CameraMarkerTracker(StorytellingSceneEditor sceneEditor, Animator animator) {
    this.sceneEditor = sceneEditor;
    this.animator = animator;
    initializeCameraMarkers();
  }

  public void setCameras(SymmetricPerspectiveCamera mainCamera, SymmetricPerspectiveCamera layoutCamera, OrthographicCamera orthographicCamera) {
    this.mainCamera = mainCamera;
    this.layoutCamera = layoutCamera;
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

  @Override
  public void valueChanged(ValueEvent<CameraOption> e) {
    CameraMarkerConfiguration<?> nextMarker = mapViewToMarker.get(e.getNextValue());
    if (isMissingAnyCameras() || nextMarker == null || nextMarker == activeMarker) {
      return;
    }
    AbstractCamera previousCamera = null;
    if (activeMarker != null) {
      activeMarker.stopTrackingCamera();
      previousCamera = activeMarker.getCamera();
    }
    activeMarker = nextMarker;
    activeMarker.animateToTargetView(previousCamera);
  }

  private boolean isMissingAnyCameras() {
    return mainCamera == null || layoutCamera == null || orthographicCamera == null;
  }


  public void trackStartingCameraView() {
    if (isMissingAnyCameras()) {
      return;
    }
    activeMarker = mapViewToMarker.get(CameraOption.STARTING_CAMERA_VIEW);
    activeMarker.switchToCamera();
    activeMarker.startTrackingCamera();
  }

  private void initializeCameraMarkers() {
    startingCameraConfig = new StartingCameraMarkerConfiguration();
    addMarker(startingCameraConfig);
    addMarker(new LayoutCameraMarkerConfiguration());
    addMarker(new TopCameraMarkerConfiguration());
    addMarker(new SideCameraMarkerConfiguration());
    addMarker(new FrontCameraMarkerConfiguration());
  }

  private void addMarker(CameraMarkerConfiguration<?> cameraMarker) {
    mapViewToMarker.put(cameraMarker.cameraOption, cameraMarker);
  }

  public void updateMarkersForNewScene(SceneImp sceneImp, TransformableImp startingCamera) {
    AffineMatrix4x4 openingViewTransform = startingCamera.getAbsoluteTransformation();
    for (CameraMarkerConfiguration<?> marker: mapViewToMarker.values()) {
      marker.resetForScene(sceneImp, openingViewTransform);
    }
    startingCameraConfig.intializeOnStartingCamera(startingCamera);
  }

  public void centerMarkersOn(UserField field) {
    // Changes the markers for the layout camera and the 3 ortho camera views, not just whatever marker is active.
    AxisAlignedBox alignedBox = getBoundingBox(field);
    for (CameraMarkerConfiguration<?> marker: mapViewToMarker.values()) {
      marker.centerOn(alignedBox);
    }
    // Update camera to the latest marker
    activeMarker.updateCameraToNewMarkerLocation();
  }

  public void centerMainCameraOnField(UserActivity activity, TransformableImp movableCamera, UserField field) {
    AffineMatrix4x4 end = startingCameraConfig.getViewingPerspective(getBoundingBox(field));
    MoveTransformableEdit edit = new MoveTransformableEdit(activity, movableCamera, end);
    new EditOperation(edit).fire(activity);
  }

  private static AxisAlignedBox getBoundingBox(UserField field) {
    Object instanceInJava = IDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForField(field);
    EntityImp target = EmployeesOnly.getImplementation((SThing) instanceInJava);
    return target.getDynamicAxisAlignedMinimumBoundingBox(org.lgna.story.implementation.AsSeenBy.SCENE);
  }

  private double clampCameraValue(double val) {
    final double CAMERA_CLAMP_MAX = 100.0;
    final double CAMERA_CLAMP_MIN = 2.0;
    return clampValue(val, CAMERA_CLAMP_MIN, CAMERA_CLAMP_MAX);
  }

  private double clampPictureValue(double val) {
    // we intentionally allow a slightly larger max here, and the manipulator deals with it later.
    final double PICTURE_CLAMP_MAX = 100;
    final double PICTURE_CLAMP_MIN = 1.5;
    return clampValue(val, PICTURE_CLAMP_MIN, PICTURE_CLAMP_MAX);
  }

  private double clampValue(double val, double min, double max) {
    return Math.min(max, Math.max(min, val));
  }

  public void setIsVrActive(boolean isVrScene) {
    startingCameraConfig.setVrActive(isVrScene);
  }

  @Override
  public void propertyChanged(PropertyEvent e) {
    InstancePropertyOwner owner = e.getOwner();
    if (!(owner instanceof OrthographicCamera)) {
      return;
    }
    if (!orthographicCamera.equals(owner)) {
      Logger.warning("Multiple Orthogonal cameras in use");
      return;
    }
    if (!Objects.equals(e.getTypedSource(), orthographicCamera.picturePlane)) {
      return;
    }
    activeMarker.updatePicturePlaneFromCamera();
  }

  private abstract class CameraMarkerConfiguration<T extends CameraMarkerImp> {
    final T markerImp;
    final CameraOption cameraOption;
    private boolean doEpilogue = true;
    private PointOfViewAnimation pointOfViewAnimation = null;
    public CameraMarkerConfiguration(CameraMarker marker, CameraOption cameraOption, String iconName) {
      this.cameraOption = cameraOption;
      markerImp = EmployeesOnly.getImplementation(marker);
      MarkerUtilities.addIconForCameraOption(cameraOption, iconName);
      markerImp.getAbstraction().setName(MarkerUtilities.getNameForCamera(cameraOption));
      initialize();
    }

    protected boolean isActive() {
      return this.equals(activeMarker);
    }

    protected abstract void initialize();
    protected abstract void centerOn(AxisAlignedBox box);

    public abstract void resetForScene(SceneImp sceneImp, AffineMatrix4x4 startingView);

    protected abstract void stopTrackingCamera();

    // We have not switched the active marker, but we may have moved it
    protected void updateCameraToNewMarkerLocation() {
      // The local transform of the marker has been updated while it is active. We need to temporarily unhook the
      // marker from the parent camera without changing the marker (which stopTrackingCamera would do)
      // The animation of the camera will reconnect them when finished.
      markerImp.getSgComposite().setParent(markerImp.getSgComposite().getRoot());
      animateToTargetView(getCamera());
    }

    protected abstract void switchToCamera();

    protected abstract AbstractCamera getCamera();

    private boolean transformsAreWithinReasonableEpsilonOfEachOther(AffineMatrix4x4 a, AffineMatrix4x4 b) {
      return a.orientation.isWithinReasonableEpsilonOf(b.orientation)
          && a.translation.isWithinReasonableEpsilonOf(b.translation);
    }

    public void animateToTargetView(AbstractCamera previousCamera) {
      AbstractTransformable cameraParent = getCamera().getMovableParent();
      AffineMatrix4x4 lastCamTransform =
          previousCamera == null
              ? AffineMatrix4x4.createIdentity()
              : previousCamera.getMovableParent().getAbsoluteTransformation();
      AffineMatrix4x4 targetTransform = getTargetTransform();
      cameraParent.setTransformation(lastCamTransform, AsSeenBy.SCENE);
      switchToCamera();

      if (pointOfViewAnimation != null) {
        doEpilogue = false;
        pointOfViewAnimation.complete(null);
        doEpilogue = true;
      }
      if (transformsAreWithinReasonableEpsilonOfEachOther(lastCamTransform, targetTransform)) {
        startTrackingCamera();
      } else {
        pointOfViewAnimation = new PointOfViewAnimation(cameraParent, AsSeenBy.SCENE, lastCamTransform, targetTransform) {
          @Override
          protected void epilogue() {
            if (doEpilogue) {
              startTrackingCamera();
            }
          }
        };
        animator.invokeLater(pointOfViewAnimation, null);
      }
    }

    protected abstract AffineMatrix4x4 getTargetTransform();

    // Sets the given camera to the absolute orientation of the given marker
    // Parents the given marker to the camera and then zeros out the local transform
    protected abstract void startTrackingCamera();

    public abstract void updatePicturePlaneFromCamera();
  }

  abstract class PerspectiveCameraMarkerConfiguration extends CameraMarkerConfiguration<PerspectiveCameraMarkerImp> {
    public PerspectiveCameraMarkerConfiguration(CameraOption cameraOption, String iconName) {
      super(new PerspectiveCameraMarker(), cameraOption, iconName);
    }

    @Override
    protected void switchToCamera() {
      sceneEditor.switchToPerspectiveCamera(getCamera());
    }

    @Override
    public void updatePicturePlaneFromCamera() {
      // No change for perspective camera
    }

    AffineMatrix4x4 getViewingPerspective(AxisAlignedBox box) {
      // Attempting to find the least disruptive move, ideally a bit above of the object (because looking up through the
      // object/ground feels bad) and far enough away that we can see most of it, but hopefully close enough that there
      // isn't another object in between. Usually we succeed.
      final double targetHeight = box.getHeight();
      final Point3 targetTranslation = box.getCenter();
      // some things (e.g. other cameras) don't have a diagonal.
      double targetDiagonal = box.getDiagonal();
      targetDiagonal = targetDiagonal > 0 ? targetDiagonal :  4;

      // Since targetTranslation is already centered in y, this is effectively height * 1.5.
      final double adjustedY = targetTranslation.y + Math.max(targetHeight, targetDiagonal * .5);
      final Point3 adjustedPos = new Point3(targetTranslation.x,  adjustedY, targetTranslation.z);

      // if the camera is already above it, great, otherwise we get the best results by using the same adjusted y.
      Point3 adjustedCameraPos = getCamera().getAbsoluteTransformation().translation;
      adjustedCameraPos.y = Math.max(adjustedCameraPos.y, adjustedY);

      Vector3 direction = Vector3.createSubtraction(adjustedCameraPos, adjustedPos);
      if (direction.isZero()) {
        direction = new Vector3(0, DEFAULT_LAYOUT_CAMERA_Y_OFFSET, DEFAULT_LAYOUT_CAMERA_Z_OFFSET);
      }
      direction.normalize();

      final Ray ray = new Ray(adjustedPos, direction);
      Point3 layoutCamTranslation = ray.getPointAlong(targetDiagonal * 1.5);

      // orientation calculated from wherever our camera ended up to look at the center of the object.
      Vector3 cameraDirection = Vector3.createSubtraction(layoutCamTranslation, targetTranslation);
      cameraDirection.normalize();
      final ForwardAndUpGuide forwardAndUpGuide = new ForwardAndUpGuide(Vector3.createNegation(cameraDirection), null);
      OrthogonalMatrix3x3 layoutCamOrientation = forwardAndUpGuide.createOrthogonalMatrix3x3();

      AffineMatrix4x4 layoutTransform = AffineMatrix4x4.createIdentity();
      layoutTransform.applyTranslation(layoutCamTranslation);
      layoutTransform.applyOrientation(layoutCamOrientation);
      adjustForVRIfNeeded(layoutTransform);
      return layoutTransform;
    }

    protected void adjustForVRIfNeeded(AffineMatrix4x4 layoutTransform) {
      if (!sceneEditor.isVrActive()) {
        return;
      }
      AbstractCamera cam = getCamera();
      AffineMatrix4x4 camTransform = cam.getTransformation(cam.getMovableParent());
      camTransform.invert();
      layoutTransform.multiply(camTransform);
    }

    // Starting and Layout markers directly track their cameras
    protected void startTrackingCamera() {
      markerImp.getSgComposite().setParent(getCamera().getMovableParent());
      markerImp.getSgComposite().setLocalTransformation(AffineMatrix4x4.createIdentity());
      sceneEditor.setHandleVisibilityForObject(markerImp, false);
    }

    // Starting and Layout markers remain on their cameras at all times
    @Override
    protected void stopTrackingCamera() {
      sceneEditor.setHandleVisibilityForObject(markerImp, true);
    }
  }

  class StartingCameraMarkerConfiguration extends PerspectiveCameraMarkerConfiguration {
    public StartingCameraMarkerConfiguration() {
      super(CameraOption.STARTING_CAMERA_VIEW, "mainCamera");
      markerImp.setShowing(false);
    }

    @Override
    protected void initialize() {
      markerImp.setVrActive(sceneEditor.isVrActive());
      markerImp.setDisplayVisuals(false);
    }

    @Override
    protected void centerOn(AxisAlignedBox box) {
      // This is for the staging cameras. The starting camera does not move.
    }

    @Override
    protected AffineMatrix4x4 getTargetTransform() {
      return markerImp.getAbsoluteTransformation();
    }

    @Override
    public void resetForScene(SceneImp sceneImp, AffineMatrix4x4 startingView) {
      markerImp.setLocalTransformation(startingView);
    }

    @Override
    protected AbstractCamera getCamera() {
      return mainCamera;
    }

    public void setVrActive(boolean isVrScene) {
      markerImp.setVrActive(isVrScene);
      // Update menu icon
      MarkerUtilities.addIconForCameraOption(CameraOption.STARTING_CAMERA_VIEW, isVrScene ? "vrHeadset" : "mainCamera");
    }

    public void intializeOnStartingCamera(TransformableImp startingCamera) {
      SimpleAppearance sgAppearance = new SimpleAppearance();
      Color4f darkGrey = EmployeesOnly.getColor4f(Color.DARK_GRAY);
      sgAppearance.diffuseColor.setValue(darkGrey);
      if (sceneEditor.isVrActive() && startingCamera instanceof VrUserImp) {
        VrUserImp vrUser = (VrUserImp) startingCamera;
        visuals = PerspectiveCameraMarkerImp.createVRVisual(sgAppearance, startingCamera.getSgComposite());
        // Set visuals scale to match and listen for changes
        scaleVisuals(vrUser.scale.getValue());
        vrUser.scale.addPropertyListener(() -> scaleVisuals(vrUser.scale.getValue()));
      } else {
        visuals = PerspectiveCameraMarkerImp.createCameraVisuals(sgAppearance, startingCamera.getSgComposite());
      }
    }

    private void scaleVisuals(Double scale) {
      Matrix3x3 scaleMatrix = scaleMatrix(scale);
      for (Visual visual : visuals) {
        visual.scale.setValue(scaleMatrix);
      }
    }

    private Matrix3x3 scaleMatrix(Double scale) {
      Matrix3x3 m = Matrix3x3.createZero();
      m.right.x = scale;
      m.up.y = scale;
      m.backward.z = scale;
      return m;
    }

    @Override
    protected void startTrackingCamera() {
      super.startTrackingCamera();
      // Hide visuals when camera is active
      for (Visual v : visuals) {
        v.isShowing.setValue(false);
      }
    }

    @Override
    protected void stopTrackingCamera() {
      super.stopTrackingCamera();
      // Show visuals when other views are active
      for (Visual v : visuals) {
        v.isShowing.setValue(true);
      }
    }

    Visual[] visuals;
  }

  class LayoutCameraMarkerConfiguration extends PerspectiveCameraMarkerConfiguration {
    public LayoutCameraMarkerConfiguration() {
      super(CameraOption.LAYOUT_SCENE_VIEW, "sceneEditorCamera");
    }

    @Override
    protected void initialize() {
      markerImp.setVrActive(sceneEditor.isVrActive());
      markerImp.getAbstraction().setColorId(Color.LIGHT_BLUE);
      markerImp.setDisplayVisuals(true);
    }

    @Override
    protected void centerOn(AxisAlignedBox box) {
      setLocalTransformation(getViewingPerspective(box));
    }

    private void setLocalTransformation(AffineMatrix4x4 layoutTransform) {
      if (isActive()) {
        markerImp.setLocalTransformation(layoutTransform);
      } else {
        getCamera().getMovableParent().setLocalTransformation(layoutTransform);
      }
    }

    @Override
    protected AffineMatrix4x4 getTargetTransform() {
      return markerImp.getAbsoluteTransformation();
    }

    @Override
    public void resetForScene(SceneImp sceneImp, AffineMatrix4x4 startingView) {
      AffineMatrix4x4 layoutTransform = new AffineMatrix4x4(startingView);
      layoutTransform.applyTranslationAlongYAxis(DEFAULT_LAYOUT_CAMERA_Y_OFFSET);
      layoutTransform.applyTranslationAlongZAxis(DEFAULT_LAYOUT_CAMERA_Z_OFFSET);
      layoutTransform.applyRotationAboutXAxis(new AngleInDegrees(DEFAULT_LAYOUT_CAMERA_ANGLE));
      adjustForVRIfNeeded(layoutTransform);
      getCamera().getMovableParent().setLocalTransformation(layoutTransform);
      markerImp.getSgComposite().setParent(getCamera().getMovableParent());
      markerImp.getSgComposite().setLocalTransformation(AffineMatrix4x4.createIdentity());
    }

    @Override
    protected AbstractCamera getCamera() {
      return layoutCamera;
    }

    @Override
    protected void startTrackingCamera() {
      super.startTrackingCamera();
      // Hide visual when camera is active
      markerImp.setShowing(false);
    }

    @Override
    protected void stopTrackingCamera() {
      super.stopTrackingCamera();
      // Show visual when other views are active
      markerImp.setShowing(true);
    }
  }

  abstract class OrthographicCameraMarkerConfiguration extends CameraMarkerConfiguration<OrthographicCameraMarkerImp> {
    public OrthographicCameraMarkerConfiguration(CameraOption cameraOption, String iconName) {
      super(new OrthographicCameraMarker(), cameraOption, iconName);
    }

    @Override
    public void resetForScene(SceneImp sceneImp, AffineMatrix4x4 startingView) {
      Component[] existingComponents = sceneImp.getSgComposite().getComponentsAsArray();
      boolean alreadyHasIt = false;
      for (Component c : existingComponents) {
        if (c == markerImp.getSgComposite()) {
          alreadyHasIt = true;
          break;
        }
      }
      if (!alreadyHasIt) {
        markerImp.setVehicle(sceneImp);
      }
      // Ortho cameras could override to focus on the starting camera or the overall scene
      // Default to initialize to remove any changes from previous scene editing
      initialize();
    }

    // Move marker in the scene graph to be directly under scene, not camera, while maintaining the absolute position
    @Override
    protected void stopTrackingCamera() {
      AffineMatrix4x4 previousMarkerTransform = markerImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);
      markerImp.getSgComposite().setParent(markerImp.getSgComposite().getRoot());
      markerImp.getSgComposite().setTransformation(previousMarkerTransform, AsSeenBy.SCENE);
      markerImp.setShowing(true);
      sceneEditor.setHandleVisibilityForObject(markerImp, true);
    }

    // Sets the given camera to the absolute orientation of the given marker
    // Parents the given marker to the camera and then zeros out the local transform
    @Override
    protected void startTrackingCamera() {
      AbstractTransformable cameraParent = getCamera().getMovableParent();
      Composite root = cameraParent.getRoot();
      if (root != null) {
        cameraParent.setTransformation(markerImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE), root);
      } else {
        Logger.severe(cameraParent);
      }
      markerImp.setShowing(false);
      markerImp.setLocalTransformation(AffineMatrix4x4.createIdentity());
      markerImp.getSgComposite().setParent(cameraParent);
      sceneEditor.setHandleVisibilityForObject(markerImp, false);
    }

    @Override
    protected AbstractCamera getCamera() {
      return orthographicCamera;
    }

    @Override
    protected void switchToCamera() {
      sceneEditor.switchToOrthographicCamera();
      orthographicCamera.picturePlane.setValue(new ClippedZPlane(markerImp.getPicturePlane()));
    }

    @Override
    public void updatePicturePlaneFromCamera() {
      markerImp.setPicturePlane(orthographicCamera.picturePlane.getValue());
    }

    @Override
    protected AffineMatrix4x4 getTargetTransform() {
      return markerImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE);
    }
  }

  class TopCameraMarkerConfiguration extends OrthographicCameraMarkerConfiguration {
    public TopCameraMarkerConfiguration() {
      super(CameraOption.TOP, "top");
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

  class SideCameraMarkerConfiguration extends OrthographicCameraMarkerConfiguration {
    public SideCameraMarkerConfiguration() {
      super(CameraOption.SIDE, "side");
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

  class FrontCameraMarkerConfiguration extends OrthographicCameraMarkerConfiguration {
    public FrontCameraMarkerConfiguration() {
      super(CameraOption.FRONT, "front");
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
