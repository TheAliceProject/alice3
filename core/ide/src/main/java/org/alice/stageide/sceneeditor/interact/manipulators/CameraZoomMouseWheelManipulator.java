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

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.QuaternionAndTranslation;
import org.alice.interact.animation.QuaternionAndTranslationTargetBasedAnimation;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.manipulator.AnimatorDependentManipulator;
import org.alice.interact.manipulator.CameraManipulator;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.ClippedZPlane;
import org.alice.math.immutable.OrthogonalMatrix3x3;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Vector3;
import org.alice.stageide.sceneeditor.interact.croquet.PredeterminedSetOrthographicPicturePlaneActionOperation;
import org.lgna.croquet.Application;
import org.lgna.story.SCamera;

public class CameraZoomMouseWheelManipulator extends CameraManipulator implements AnimatorDependentManipulator {

  private static final double CAMERA_SPEED = 10.0;
  private static final double TARGET_LOW_HEIGHT = SCamera.DEFAULT_POSITION.getUp();
  private static final double ORTHOGRAPHIC_ZOOM_PER_WHEEL_CLICK = .2d;

  private static final double TARGET_LOW_DOWN_AMOUNT = .1;
  private static final double TARGET_HIGH_DOWN_AMOUNT = .9;

  private static final double COEFFICIENT = .1;
  private static final double FLATTENING_FACTOR = .3;

  @Override
  public Animator getAnimator() {
    return this.animator;
  }

  @Override
  public void setAnimator(Animator animator) {
    this.animator = animator;
  }

  @Override
  public String getUndoRedoDescription() {
    return "Camera Zoom";
  }

  @Override
  public CameraView getDesiredCameraView() {
    return CameraView.PICK_CAMERA;
  }

  @Override
  public void setCamera(AbstractCamera camera) {
    super.setCamera(camera);
    isVr = camera.getParent() != manipulatedTransformable;
  }

  private Vector3 getIdealBackwardForX(double x) {
    double y = 0;
    if (x > 0) {
      y = COEFFICIENT * 2 * x * FLATTENING_FACTOR;
    } else if (this.useUpCurve && (x > (-this.lateralDistanceForUp))) {
      //Get the derivative for the cosine function we use for the up-curve
      x += this.lateralDistanceForUp; //bump the x value so we're evaluating the sine curve correctly
      y = ((this.distanceUpScale * x * Math.PI) / this.lateralDistanceForUp) * (-Math.sin((x * Math.PI) / this.lateralDistanceForUp));
    }
    if (y < TARGET_LOW_DOWN_AMOUNT) {
      y = TARGET_LOW_DOWN_AMOUNT;
    }
    return new Vector3(-movementDirection.x(), y, -movementDirection.z()).normalized();
  }

  private OrthogonalMatrix3x3 getOrientationForBackward(Vector3 backward) {
    Vector3 up = backward.crossProduct(rightVector).normalized();
    return new OrthogonalMatrix3x3(this.rightVector, up, backward);
  }

  private void createInterpolationTargets() {
    AffineMatrix4x4 currentTransform = manipulatedTransformable.getAbsoluteTransformation();

    movementDirection = currentTransform.orientation().backward().negate();
    if (Math.abs(movementDirection.x()) < .000001) {
      movementDirection = movementDirection.withX(0);
    }
    if (Math.abs(movementDirection.z()) < .000001) {
      movementDirection = movementDirection.withZ(0);
    }
    movementDirection = movementDirection.withY(0).normalized();
    if (movementDirection.isNaN()) {
      if (currentTransform.orientation().backward().y() > 0) {
        movementDirection = currentTransform.orientation().up();
      } else {
        movementDirection = currentTransform.orientation().up().negate();
      }
      movementDirection = movementDirection.withY(0).normalized();
    }
    Vector3 lowBackwardVector = movementDirection.negate().withY(TARGET_LOW_DOWN_AMOUNT).normalized();
    rightVector = Vector3.POSITIVE_Y_AXIS.crossProduct(lowBackwardVector).normalized();
    if (currentTransform.translation().y() > TARGET_LOW_HEIGHT) {
      originalX = (currentTransform.translation().y() - TARGET_LOW_HEIGHT) / COEFFICIENT;
      inflectionPoint = currentTransform.translation().plus(movementDirection.times(currentX)).withY(TARGET_LOW_HEIGHT);
      useUpCurve = false;
    } else {
      originalX = 0;
      inflectionPoint = originalTransformation.translation();
      distanceUp = TARGET_LOW_HEIGHT - inflectionPoint.y();
      distanceUpScale = distanceUp / 2.0;
      lateralDistanceForUp = distanceUp;
      useUpCurve = true;
    }
    currentX = originalX;
  }

  @Override
  public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
    zoomCamera(currentInput.getMouseWheelState());
    manipulatedTransformable.notifyTransformationListeners();
  }

  private double getHeightForX(double x) {
    if (x < 0) {
      if (this.useUpCurve) {
        if (x < -this.lateralDistanceForUp) {
          return TARGET_LOW_HEIGHT;
        } else {

          double newx = x + this.lateralDistanceForUp; //bump the x value so we're evaluating the cosine curve correctly
          double cosineValue = (this.distanceUpScale) * Math.cos((newx * Math.PI) / this.lateralDistanceForUp);
          double height = cosineValue + this.distanceUpScale + this.originalTransformation.translation().y();
          //          PrintUtilities.println("New height for "+x+" -> f("+newx+") = "+cosineValue+" + "+this.distanceUpScale+" + "+this.originalTransformation.translation(.y()+" = "+height);
          return height;
        }
      } else {
        return TARGET_LOW_HEIGHT;
      }
    } else {
      return COEFFICIENT * x + this.inflectionPoint.y();
    }
  }

  private static Vector3 interpolateNormalizedVector(Vector3 a, Vector3 b, double percent) {
    double x = a.x() + ((b.x() - a.x()) * percent);
    double y = a.y() + ((b.y() - a.y()) * percent);
    double z = a.z() + ((b.z() - a.z()) * percent);
    return new Vector3(x, y, z).normalized();
  }

  private OrthogonalMatrix3x3 getOrientationTargetForX(double x) {
    if (isVr) {
      return this.originalTransformation.orientation();
    }
    Vector3 targetBackward = this.getIdealBackwardForX(x);
    double lowerX = this.originalX - this.lowerInterpolationRange;
    double upperX = this.originalX + this.upperInterpolationRange;
    if ((x > lowerX) && (x < upperX)) {
      if (x < this.originalX) {
        double percent = (x - lowerX) / (this.originalX - lowerX);
        targetBackward = interpolateNormalizedVector(targetBackward, this.originalTransformation.orientation().backward(), percent);
      } else {
        double percent = (x - this.originalX) / (upperX - this.originalX);
        targetBackward = interpolateNormalizedVector(this.originalTransformation.orientation().backward(), targetBackward, percent);
      }
    }
    OrthogonalMatrix3x3 targetOrientation = getOrientationForBackward(targetBackward);
    return targetOrientation;
  }

  private Point3 getNewPointForX(double x) {
    Point3 newPoint = inflectionPoint.plus(movementDirection.times(-x));
    return newPoint.withY(isVr ? 0.0 : this.getHeightForX(x));
  }

  @Override
  protected void initializeEventMessages() {
    this.setMainManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Zoom, null, this.manipulatedTransformable));
    this.clearManipulationEvents();
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Zoom, new MovementDescription(MovementDirection.FORWARD, MovementType.LOCAL), this.manipulatedTransformable));
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Zoom, new MovementDescription(MovementDirection.BACKWARD, MovementType.LOCAL), this.manipulatedTransformable));
  }

  private double getCameraZoom() {
    OrthographicCamera orthoCam = (OrthographicCamera) this.camera;
    ClippedZPlane picturePlane = orthoCam.picturePlane.getValue();
    return picturePlane.getHeight();
  }

  private void setCameraZoom(double amount) {
    OrthographicCamera orthoCam = (OrthographicCamera) this.camera;
    ClippedZPlane picturePlane = orthoCam.picturePlane.getValue();
    double newZoom = picturePlane.getHeight() + amount;
    if (newZoom > OrthographicCameraDragZoomManipulator.MAX_ZOOM) {
      newZoom = OrthographicCameraDragZoomManipulator.MAX_ZOOM;
    } else if (newZoom < OrthographicCameraDragZoomManipulator.MIN_ZOOM) {
      newZoom = OrthographicCameraDragZoomManipulator.MIN_ZOOM;
    }
    orthoCam.picturePlane.setValue(picturePlane.withHeight(newZoom));
  }

  protected void zoomCamera(int direction) {

    if (this.camera instanceof SymmetricPerspectiveCamera) {
      this.currentX += direction * X_CHANGE_PER_CLICK;
      if (this.cameraAnimation != null) {
        Point3 targetPosition = getNewPointForX(this.currentX);
        OrthogonalMatrix3x3 targetOrientation = this.getOrientationTargetForX(this.currentX);
        AffineMatrix4x4 targetTransform = new AffineMatrix4x4(targetOrientation, targetPosition);
        this.cameraAnimation.setTarget(new QuaternionAndTranslation(targetTransform));
      } else {
        Logger.severe("Mouse Wheel Camera Zoom: null cameraAnimation.");
      }
    } else {
      double amountToZoom = ORTHOGRAPHIC_ZOOM_PER_WHEEL_CLICK * direction;
      this.applyZoom(amountToZoom);
    }
  }

  protected void applyZoom(double zoomAmount) {
    this.setCameraZoom(zoomAmount);

    for (ManipulationEvent event : this.getManipulationEvents()) {
      if (event.getMovementDescription().direction == MovementDirection.FORWARD) {
        this.dragAdapter.triggerManipulationEvent(event, zoomAmount < 0.0d);
      } else if (event.getMovementDescription().direction == MovementDirection.BACKWARD) {
        this.dragAdapter.triggerManipulationEvent(event, zoomAmount >= 0.0d);
      }
    }
  }

  @Override
  public void doEndManipulator(InputState endInput, InputState previousInput) {
    if (this.cameraAnimation != null) {
      this.cameraAnimation.complete();
      this.animator.removeFrameObserver(this.cameraAnimation);
      this.cameraAnimation = null;
    }
  }

  @Override
  public void doClickManipulator(InputState clickInput, InputState previousInput) {
    //Do nothing
  }

  @Override
  public boolean doStartManipulator(InputState startInput) {
    if (super.doStartManipulator(startInput)) {
      this.originalTransformation = this.manipulatedTransformable.getAbsoluteTransformation();
      createInterpolationTargets();
      if (this.cameraAnimation != null) {
        this.animator.removeFrameObserver(this.cameraAnimation);
      }
      this.cameraAnimation = new QuaternionAndTranslationTargetBasedAnimation(new QuaternionAndTranslation(this.manipulatedTransformable.getAbsoluteTransformation()), CAMERA_SPEED) {
        @Override
        protected void updateValue(QuaternionAndTranslation value) {
          if (CameraZoomMouseWheelManipulator.this.camera != null) {
            AffineMatrix4x4 m = value.getAffineMatrix();
            manipulatedTransformable.setTransformation(m, AsSeenBy.SCENE);
          }
        }
      };
      this.animator.addFrameObserver(this.cameraAnimation);
      zoomCamera(startInput.getMouseWheelState());
      manipulatedTransformable.notifyTransformationListeners();
      return true;
    }
    return false;
  }

  @Override
  public void undoRedoBeginManipulation() {
    if ((this.getCamera() != null) && (this.getCamera() instanceof OrthographicCamera)) {
      this.originalOrthographicZoomValue = this.getCameraZoom();
    } else {
      super.undoRedoBeginManipulation();
    }
  }

  @Override
  public void undoRedoEndManipulation() {
    if ((this.getCamera() != null) && (this.getCamera() instanceof OrthographicCamera)) {
      double newZoom = this.getCameraZoom();
      Animator animator;
      if (this.dragAdapter != null) {
        animator = this.dragAdapter.getAnimator();
      } else {
        animator = null;
      }
      PredeterminedSetOrthographicPicturePlaneActionOperation undoOperation =
          new PredeterminedSetOrthographicPicturePlaneActionOperation(Application.DOCUMENT_UI_GROUP, false,
              animator, (OrthographicCamera) this.camera, this.originalOrthographicZoomValue, newZoom, getUndoRedoDescription());
      undoOperation.fire();
    } else {
      super.undoRedoEndManipulation();
    }
  }

  @Override
  public void doTimeUpdateManipulator(double time, InputState currentInput) {
    //Do Nothing
  }

  protected QuaternionAndTranslationTargetBasedAnimation cameraAnimation = null;
  private AffineMatrix4x4 originalTransformation;
  private Animator animator;

  private double initialOrthographicZoomValue = 0.0d;
  private double originalOrthographicZoomValue = 0.0d;

  private Point3 inflectionPoint;
  private boolean useUpCurve = false;
  private double distanceUp = 1;
  private double distanceUpScale = distanceUp / 2.0;
  private double lateralDistanceForUp = 1;

  private boolean isVr;

  private double currentX = 0;
  private double originalX = 0;
  private static double X_CHANGE_PER_CLICK = .3;
  private Vector3 movementDirection;

  private Vector3 rightVector;

  private double lowerInterpolationRange = 6;
  private double upperInterpolationRange = 6;
}
