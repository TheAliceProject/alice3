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

import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.math.immutable.Angle;
import org.alice.math.immutable.Plane;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;

import java.awt.Point;

/**
 * @author David Culyba
 */
public class ObjectTranslateDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {

  private static final double BAD_ANGLE_THRESHOLD = 2.0d * Math.PI * (8.0d / 360.0d);
  private static final double MIN_BAD_ANGLE_THRESHOLD = 0.0d;

  @Override
  public AbstractCamera getCamera() {
    return this.camera;
  }

  @Override
  public void setCamera(AbstractCamera camera) {
    this.camera = camera;
    if ((this.camera != null) && (this.camera.getParent() instanceof AbstractTransformable)) {
      this.setManipulatedTransformable((AbstractTransformable) this.camera.getParent());
    }

  }

  @Override
  public void setDesiredCameraView(CameraView cameraView) {
    //this can only be ACTIVE_VIEW
  }

  @Override
  public CameraView getDesiredCameraView() {
    return CameraView.PICK_CAMERA;
  }

  @Override
  public OnscreenRenderTarget getOnscreenRenderTarget() {
    return this.onscreenRenderTarget;
  }

  @Override
  public void setOnscreenRenderTarget(OnscreenRenderTarget onscreenRenderTarget) {
    this.onscreenRenderTarget = onscreenRenderTarget;
  }

  @Override
  public String getUndoRedoDescription() {
    return "Obj Translate - Object Move";
  }

  @Override
  protected void initializeEventMessages() {
    this.setMainManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, null, this.manipulatedTransformable));
    this.clearManipulationEvents();
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.LEFT, MovementType.ABSOLUTE), this.manipulatedTransformable));
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.RIGHT, MovementType.ABSOLUTE), this.manipulatedTransformable));
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.FORWARD, MovementType.ABSOLUTE), this.manipulatedTransformable));
    this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(MovementDirection.BACKWARD, MovementType.ABSOLUTE), this.manipulatedTransformable));
  }

  protected Point3 getPositionForPlane(Plane movementPlane, Ray pickRay) {
    if (pickRay == null) {
      return null;
    }
    Point3 pointInPlane = movementPlane.getIntersection(pickRay);
    if (pointInPlane == null) {
      return null;
    }
    return pointInPlane.plus(offsetToOrigin).withY(this.initialObjectPosition.y());
  }

  protected Point3 getPositionBasedOnMouseLocation(Point mouseLocation) {
    Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(mouseLocation, this.getCamera());
    if (pickRay != null) {
      Plane toMoveIn = this.movementPlane;
      double badAngleAmount = this.getBadAngleAmount(this.movementPlane, pickRay);
      if (badAngleAmount > 0.0d) {
        Vector3 newNormal = this.movementPlane.getNormal().interpolate(this.badAnglePlane.getNormal(), badAngleAmount).normalized();
        toMoveIn = Plane.createInstance(this.initialClickPoint, newNormal);
      }
      Point3 newPosition = this.getPositionForPlane(toMoveIn, pickRay);
      return newPosition;
    } else {
      return null;
    }
  }

  protected double getBadAngleAmount(Plane plane, Ray pickRay) {
    Vector3 cameraDirection = this.getCamera().getAbsoluteTransformation().orientation().backward();
    double cameraDistanceFactor = Math.abs(plane.distanceTo(this.getCamera().getAbsoluteTransformation().translation()));
    Angle angleBetweenVector = cameraDirection.angleWith(plane.getNormal());
    double distanceToRightAngle = Math.abs((Math.PI * .5d) - angleBetweenVector.getAsRadians());

    double scaledBadAngleThreshold = BAD_ANGLE_THRESHOLD / cameraDistanceFactor;
    double scaledMinBadAngleThreshold = MIN_BAD_ANGLE_THRESHOLD / cameraDistanceFactor;

    if (distanceToRightAngle < scaledBadAngleThreshold) {
      if (distanceToRightAngle < scaledMinBadAngleThreshold) {
        return 1.0d;
      }
      distanceToRightAngle -= scaledMinBadAngleThreshold;
      double thresholdDif = scaledBadAngleThreshold - scaledMinBadAngleThreshold;
      return (thresholdDif - distanceToRightAngle) / thresholdDif;
    } else {
      return 0.0d;
    }
  }

  protected Plane createCameraFacingStoodUpPlane(Point3 clickPoint) {
    return Plane.createInstance(clickPoint, createCameraFacingStoodUpVector());
  }

  protected Vector3 createCameraFacingStoodUpVector() {
    Vector3 cameraBackward = this.getCamera().getAxes(AsSeenBy.SCENE).backward();
    if (EpsilonUtilities.isWithinReasonableEpsilon(cameraBackward.y(), 1.0d)) { //handle case where camera is pointing down or up
      return getCamera().getAbsoluteTransformation().orientation().up().negate();
    } else {
      return cameraBackward.withY(0).normalized();
    }
  }

  protected Plane createBadAnglePlane(Point3 clickPoint) {
    Vector3 badPlaneNormal = createCameraFacingStoodUpVector();
    // Make the bad plane slightly tilted so moving the mouse will always move the object in the plane
    badPlaneNormal = badPlaneNormal.withY(badPlaneNormal.y() + 2d).normalized();
    return Plane.createInstance(clickPoint, badPlaneNormal);
  }

  protected Plane createPickPlane(Point3 clickPoint) {
    return Plane.createInstance(clickPoint, Vector3.POSITIVE_Y_AXIS);
  }

  @Override
  public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
    if (!currentInput.getMouseLocation().equals(previousInput.getMouseLocation()) && (this.manipulatedTransformable != null)) {
      if (!this.hasMoved) {
        this.hasMoved = true;
      }

      Point3 newPosition = getPositionBasedOnMouseLocation(currentInput.getMouseLocation());

      newPosition = SnapUtilities.doMovementSnapping(this.manipulatedTransformable, newPosition, this.dragAdapter, this.manipulatedTransformable.getRoot(), this.getCamera());

      Vector3 movementDif = newPosition.minus(this.manipulatedTransformable.getAbsoluteTransformation().translation()).normalized();
      for (ManipulationEvent event : this.getManipulationEvents()) {
        double dot = event.getMovementDescription().direction.getVector().dotProduct(movementDif);
        if (dot > 0.1d) {
          this.dragAdapter.triggerManipulationEvent(event, true);
        } else if (dot < -.07d) {
          this.dragAdapter.triggerManipulationEvent(event, false);
        }
      }

      if (newPosition != null) {
        this.manipulatedTransformable.setTranslationOnly(newPosition, AsSeenBy.SCENE);
      }
    }
  }

  @Override
  public void doClickManipulator(InputState clickInput, InputState previousInput) {
    //Do nothing
  }

  @Override
  public void doEndManipulator(InputState endInput, InputState previousInput) {
  }

  @Override
  public boolean doStartManipulator(InputState startInput) {
    this.setManipulatedTransformable(startInput.getClickPickTransformable());
    if (this.manipulatedTransformable == null) {
      return false;
    }
    this.initializeEventMessages();
    this.initialMouseLocation.setLocation(startInput.getMouseLocation());
    this.hasMoved = false;
    this.initialObjectPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation();
    this.initialClickPoint = startInput.getClickPickResult().getPositionInSource();
    this.initialClickPoint = startInput.getClickPickResult().getSource().transformTo(this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot());
    this.movementPlane = createPickPlane(this.initialClickPoint);
    this.badAnglePlane = createBadAnglePlane(this.initialClickPoint);

    Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(startInput.getMouseLocation(), this.getCamera());
    if (pickRay != null) {
      this.initialClickPoint = this.movementPlane.getIntersection(pickRay); //  null
      this.offsetToOrigin = manipulatedTransformable.getAbsoluteTransformation().translation().minus(initialClickPoint);
      this.movementPlane = createPickPlane(this.initialClickPoint);
      this.badAnglePlane = createBadAnglePlane(this.initialClickPoint);
    } else {
      this.setManipulatedTransformable(null);
    }
    return this.manipulatedTransformable != null;
  }

  @Override
  public void doTimeUpdateManipulator(double time, InputState currentInput) {
  }

  @Override
  protected HandleSet getHandleSetToEnable() {
    return HandleSet.ABSOLUTE_GROUND_TRANSLATION_VISUALIZATION;
  }

  private Point3 initialClickPoint = Point3.ORIGIN;
  protected Point3 initialObjectPosition = Point3.ORIGIN;
  private Plane movementPlane = Plane.XZ_PLANE;
  private Plane badAnglePlane = null;
  protected Vector3 offsetToOrigin = null;
  private Point initialMouseLocation = new Point();
  private Boolean hasMoved = false;

  private AbstractCamera camera = null;
  private OnscreenRenderTarget onscreenRenderTarget;
}
