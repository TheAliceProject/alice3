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

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.java.awt.RobotUtilities;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Plane;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;

import java.awt.Point;

public class MouseRelativeObjectDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {

  private static final double PIXEL_DISTANCE_FACTOR = 200.0d;

  private static final double MAX_DISTANCE_PER_PIXEL = .1d;

  public MouseRelativeObjectDragManipulator() {
    super();
  }

  @Override
  public AbstractCamera getCamera() {
    return this.camera;
  }

  @Override
  public void setCamera(AbstractCamera camera) {
    this.camera = camera;
  }

  @Override
  public void setDesiredCameraView(CameraView cameraView) {
    //this can only be PICK_CAMERA
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
    return "Object Move";
  }

  @Override
  protected HandleSet getHandleSetToEnable() {
    return HandleSet.ABSOLUTE_GROUND_TRANSLATION_VISUALIZATION;
  }

  private Vector3 getMouseMovementFromVector(InputState currentInput, InputState previousInput) {
    int xChange = currentInput.getMouseLocation().x - originalMousePoint.x;
    int yChange = currentInput.getMouseLocation().y - originalMousePoint.y;
    yChange *= -1; //invert X

    //    horizontalPlacementPlane = calculateCameraFacingPlane();
    if (horizontalPlacementPlane != null) {
      Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(currentInput.getMouseLocation(), this.getCamera());
      Point3 pickPoint = horizontalPlacementPlane.getIntersection(pickRay);
      pickPoint = pickPoint.minus(this.offsetFromOrigin).withY(0);
      Vector3 translationX = pickPoint.minus(this.originalPosition);
      Vector3 translationY = moveYVector.times(yChange * worldUnitsPerPixelY);
      return translationX.plus(translationY);
    } else {
      Vector3 translationX = moveXVector.times(xChange * worldUnitsPerPixelX);
      Vector3 translationY = moveYVector.times(yChange * worldUnitsPerPixelY);
      return translationX.plus(translationY);
    }
  }

  private Vector3 getOrthographicMovementVector(InputState currentInput, InputState previousInput) {
    Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(currentInput.getMouseLocation(), this.getCamera());
    Point3 pickPoint = this.orthographicPickPlane.getIntersection(pickRay);
    Point3 newPosition = pickPoint.plus(this.orthographicOffsetToOrigin);

    return newPosition.minus(this.originalPosition);
  }

  private Vector3 getMovementVectorBasedOnCamera(InputState currentInput, InputState previousInput) {
    if (this.getCamera() instanceof OrthographicCamera) {
      return getOrthographicMovementVector(currentInput, previousInput);
    } else {
      return getMouseMovementFromVector(currentInput, previousInput);
    }
  }

  @Override
  public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
    if (!currentInput.getMouseLocation().equals(previousInput.getMouseLocation()) && (this.manipulatedTransformable != null)) {
      if (!this.hasMoved) {
        this.hasMoved = true;
      }

      Vector3 movementVector = getMovementVectorBasedOnCamera(currentInput, previousInput);
      Point3 newPosition = this.originalPosition.plus(movementVector);

      newPosition = SnapUtilities.doMovementSnapping(this.manipulatedTransformable, newPosition, this.dragAdapter, this.manipulatedTransformable.getRoot(), this.getCamera());

      //Send manipulation events
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
  public void doEndManipulator(InputState endInput, InputState previousInput) {
    if (this.hidCursor) {
      this.showCursor();
    }
  }

  @Override
  public void doClickManipulator(InputState clickInput, InputState previousInput) {
    //Do nothing
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

  @Override
  public boolean doStartManipulator(InputState startInput) {
    this.setManipulatedTransformable(startInput.getClickPickTransformable());
    hidCursor = false;
    if (this.manipulatedTransformable != null) {
      this.initializeEventMessages();

      this.hasMoved = false;

      this.originalLocalTransformation = manipulatedTransformable.getLocalTransformation();
      this.originalMousePoint = new Point(startInput.getMouseLocation());
      this.originalPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation();
      this.orthographicPickPlane = Plane.createInstance(this.originalPosition, this.getCamera().getAxes(AsSeenBy.SCENE).backward());

      Ray orthoPickRay = this.onscreenRenderTarget.getRayAtAwtPoint(startInput.getMouseLocation(), this.getCamera());
      Point3 orthoPickPoint = orthographicPickPlane.getIntersection(orthoPickRay);
      this.orthographicOffsetToOrigin = this.originalPosition.minus(orthoPickPoint);

      Point3 initialClickPoint = startInput.getClickPickResult().getPositionInSource();
      initialClickPoint = startInput.getClickPickResult().getSource().transformTo(initialClickPoint, startInput.getClickPickResult().getSource().getRoot());

      Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(startInput.getMouseLocation(), this.getCamera());
      if (pickRay != null) {
        this.offsetFromOrigin = initialClickPoint.minus(this.originalPosition);
      }

      AffineMatrix4x4 cameraTransform = this.camera.getParent().getAbsoluteTransformation();
      initialCameraDotVertical = cameraTransform.orientation().getBackward().dotProduct(Vector3.POSITIVE_Y_AXIS);
      initialCameraDotVertical = Math.abs(initialCameraDotVertical);
      if (this.camera instanceof OrthographicCamera) {
        moveXVector = cameraTransform.orientation().getRight();
        moveYVector = cameraTransform.orientation().getUp();
      } else {

        if (initialCameraDotVertical > .99999) {
          moveYVector = cameraTransform.orientation().getUp();
        } else {
          moveYVector = cameraTransform.orientation().getBackward().negate();
        }
        moveXVector = cameraTransform.orientation().getRight().withY(0).normalized();
        moveYVector = moveYVector.withY(0).normalized();

        horizontalPlacementPlane = calculateCameraFacingPlane();
      }

      initialDistanceToGround = Math.abs(cameraTransform.translation().y());
      pickDistance = -1;
      Vector3 cameraForward = cameraTransform.orientation().getBackward().negate();
      Point3 pickPoint = Plane.XZ_PLANE.getIntersection(new Ray(this.manipulatedTransformable.getAbsoluteTransformation().translation(), cameraForward));
      if (pickPoint != null) {
        pickDistance = pickPoint.distanceFrom(cameraTransform.translation());
      }

      calculateMovementFactors(startInput.getMouseLocation());

      this.hideCursor();
      return true;
    }
    return false;

  }

  private void calculateMovementFactors(Point mousePoint) {
    Ray centerRay = this.onscreenRenderTarget.getRayAtAwtPoint(mousePoint, this.getCamera());
    Ray oneUp = this.onscreenRenderTarget.getRayAtAwtPoint(new Point(mousePoint.x, mousePoint.y - 1), this.getCamera());
    Ray oneDown = this.onscreenRenderTarget.getRayAtAwtPoint(new Point(mousePoint.x, mousePoint.y + 1), this.getCamera());
    Ray oneRight = this.onscreenRenderTarget.getRayAtAwtPoint(new Point(mousePoint.x + 1, mousePoint.y), this.getCamera());
    Ray oneLeft = this.onscreenRenderTarget.getRayAtAwtPoint(new Point(mousePoint.x - 1, mousePoint.y), this.getCamera());

    double distancePerUpPixel = MAX_DISTANCE_PER_PIXEL;
    double distancePerDownPixel = MAX_DISTANCE_PER_PIXEL;
    double distancePerRightPixel = MAX_DISTANCE_PER_PIXEL;
    double distancePerLeftPixel = MAX_DISTANCE_PER_PIXEL;
    Point3 centerPoint = Plane.XZ_PLANE.getIntersection(centerRay);
    if (centerPoint != null) {
      Point3 offsetPoint = Plane.XZ_PLANE.getIntersection(oneUp);
      if (offsetPoint != null) {
        double pixelDistance = centerPoint.distanceFrom(offsetPoint);
        if (pixelDistance < MAX_DISTANCE_PER_PIXEL) {
          distancePerUpPixel = pixelDistance;
        }
      }
      offsetPoint = Plane.XZ_PLANE.getIntersection(oneDown);
      if (offsetPoint != null) {
        double pixelDistance = centerPoint.distanceFrom(offsetPoint);
        if (pixelDistance < MAX_DISTANCE_PER_PIXEL) {
          distancePerDownPixel = pixelDistance;
        }
      }
      offsetPoint = Plane.XZ_PLANE.getIntersection(oneRight);
      if (offsetPoint != null) {
        double pixelDistance = centerPoint.distanceFrom(offsetPoint);
        if (pixelDistance < MAX_DISTANCE_PER_PIXEL) {
          distancePerRightPixel = pixelDistance;
        }
      }
      offsetPoint = Plane.XZ_PLANE.getIntersection(oneLeft);
      if (offsetPoint != null) {
        double pixelDistance = centerPoint.distanceFrom(offsetPoint);
        if (pixelDistance < MAX_DISTANCE_PER_PIXEL) {
          distancePerLeftPixel = pixelDistance;
        }
      }
    }

    worldUnitsPerPixelX = (distancePerLeftPixel + distancePerRightPixel) / 2.0;
    worldUnitsPerPixelY = (distancePerUpPixel + distancePerDownPixel) / 2.0;
  }

  private Plane calculateCameraFacingPlane() {
    AffineMatrix4x4 cameraTransform = this.camera.getParent().getAbsoluteTransformation();
    Vector3 cameraFacingVector = cameraTransform.orientation().getBackward().withY(0).normalized();
    if (!cameraFacingVector.isNaN()) {
      Point3 planeLocation = manipulatedTransformable.getAbsoluteTransformation().translation().plus(offsetFromOrigin);
      return Plane.createInstance(planeLocation, cameraFacingVector);
    }
    return null;
  }

  private double calculateWorldUnitsPerPixelX() {
    worldUnitsPerPixelX = initialDistanceToGround / 150.0d;
    return worldUnitsPerPixelX;
  }

  private double calculateWorldUnitsPerPixelY() {
    double parallelToGroundFactor = 30d;
    double perpToGroundFactor = 200d;
    double yFactor = parallelToGroundFactor + ((perpToGroundFactor - parallelToGroundFactor) * initialCameraDotVertical);
    worldUnitsPerPixelY = initialDistanceToGround / yFactor;
    return worldUnitsPerPixelX;
  }

  @Override
  public void doTimeUpdateManipulator(double time, InputState currentInput) {
  }

  protected void hideCursor() {
    CursorUtilities.pushAndSet(this.onscreenRenderTarget.getAwtComponent(), CursorUtilities.NULL_CURSOR);
    this.hidCursor = true;
  }

  protected void showCursor() {
    try {
      Point3 new3DPoint = manipulatedTransformable.getAbsoluteTransformation().translation().plus(offsetFromOrigin);

      Point3 pointInCamera = this.camera.transformFrom(new3DPoint, this.camera.getRoot());
      Point awtPoint = this.onscreenRenderTarget.transformFromCameraToAWT(pointInCamera, this.getCamera());
      RobotUtilities.mouseMove(this.onscreenRenderTarget.getAwtComponent(), awtPoint);
    } finally {
      CursorUtilities.popAndSet(this.onscreenRenderTarget.getAwtComponent());
    }
  }

  private Point originalMousePoint;
  private AffineMatrix4x4 originalLocalTransformation;
  private Vector3 moveXVector;
  private Vector3 moveYVector;
  private double worldUnitsPerPixelX = .01d;
  private double worldUnitsPerPixelY = .01d;
  private double initialDistanceToGround;
  private double initialCameraDotVertical;
  private double pickDistance;
  protected Plane orthographicPickPlane = Plane.XZ_PLANE;
  protected Vector3 orthographicOffsetToOrigin = null;
  protected Point3 originalPosition = null;
  protected Boolean hasMoved = false;
  protected Vector3 offsetFromOrigin = null;
  protected Plane horizontalPlacementPlane;

  protected boolean hidCursor = false;

  protected AbstractCamera camera = null;
  private OnscreenRenderTarget onscreenRenderTarget;
}
