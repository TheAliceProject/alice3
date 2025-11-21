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
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.debug.DebugSphere;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Angle;
import org.alice.math.immutable.Plane;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;

import java.awt.Point;

/**
 * @author David Culyba
 */
public class OmniDirectionalDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {

  private static final boolean SHOW_PLANE_TRANSITION_POINT = false;
  private static final double MAX_DISTANCE_PER_PIXEL = .17d;

  protected void addPlaneTransitionPointSphereToScene() {
    if (SHOW_PLANE_TRANSITION_POINT) {
      if ((this.camera != null) && (this.planeTransitionPointDebugSphere.getParent() == null)) {
        this.camera.getRoot().addComponent(this.planeTransitionPointDebugSphere);
      }
    }
  }

  private void removePlaneTransitionPointSphereFromScene() {
    if (SHOW_PLANE_TRANSITION_POINT) {
      if ((this.camera != null) && (this.planeTransitionPointDebugSphere.getParent() == this.camera.getRoot())) {
        this.camera.getRoot().removeComponent(this.planeTransitionPointDebugSphere);
      }
    }
  }

  @Override
  public AbstractCamera getCamera() {
    return this.camera;
  }

  @Override
  public void setCamera(AbstractCamera camera) {
    this.camera = camera;
    if (this.camera != null) {
      setManipulatedTransformable(this.camera.getMovableParent());
    }
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
    return "Omni-Drag - Object Move";
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

  protected Plane createCameraPickPlane(Point3 clickPoint) {
    Vector3 clickPlaneNormal = this.getCamera().getAxes(AsSeenBy.SCENE).backward();
    //    clickPlaneNormal.y += 2d;  //Make the bad plane slightly tilted so moving the mouse will always move the object in the plane
    return Plane.createInstance(clickPoint, clickPlaneNormal.normalized());
  }

  protected Plane createLevelPickPlane(Point3 clickPoint) {
    Vector3 levelPlaneNormal = MovementDirection.UP.getVector();
    //    clickPlaneNormal.y += 2d;  //Make the bad plane slightly tilted so moving the mouse will always move the object in the plane
    return Plane.createInstance(clickPoint, levelPlaneNormal.normalized());
  }

  private Vector3 getMouseMovementFromVector(Point mouseVector) {
    if ((mouseVector.x == 0) && (mouseVector.y == 0)) {
      return Vector3.ZERO;
    }

    Vector3 mouseRelativeMovement = new Vector3(mouseVector.x, 0d, mouseVector.y);
    mouseRelativeMovement = getCamera().getRoot().transformFrom(mouseRelativeMovement, getCamera());
    mouseRelativeMovement = mouseRelativeMovement.withY(0).normalized();

    double MOVEMENT_SCALE = .02d;
    double movementAmount = mouseVector.distance(0f, 0f) * MOVEMENT_SCALE;
    mouseRelativeMovement = mouseRelativeMovement.times(movementAmount);

    if (mouseRelativeMovement.isNaN()) {
      System.out.println("NaN!");
    }

    return mouseRelativeMovement;
  }

  private Vector3 getMovementVectorBasedOnCamera(InputState currentInput, InputState previousInput) {
    if (this.getCamera() instanceof OrthographicCamera) {
      return getOrthographicMovementVector(currentInput, previousInput);
    } else {
      return getPerspectiveMovementVector(currentInput, previousInput);
    }
  }

  protected Vector3 getOrthographicMovementVector(InputState currentInput, InputState previousInput) {
    Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(currentInput.getMouseLocation(), this.getCamera());
    Point3 pickPoint = this.orthographicPickPlane.getIntersection(pickRay);
    Point3 newPosition = pickPoint != null ? pickPoint.plus(this.orthographicOffsetToOrigin) : pickPoint;

    return newPosition.minus(this.getManipulatedTransformable().getAbsoluteTransformation().translation());
  }

  protected Point3 getPerspectivePositionBasedOnInput(InputState currentInput) {
    Point mousePoint = new Point(currentInput.getMouseLocation().x + this.mousePlaneOffset.x, currentInput.getMouseLocation().y + this.mousePlaneOffset.y);
    Vector3 cameraForward = this.getCamera().getParent().getAbsoluteTransformation().orientation().backward().negate();
    Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(mousePoint, this.getCamera());

    Point3 levelPickPoint = null;
    Point3 skewedPickPoint = null;
    if (this.pickPlane != null) {
      levelPickPoint = this.pickPlane.getIntersection(pickRay);
      //force the pick point to be at the original Y level
      if (levelPickPoint != null) {
        levelPickPoint = levelPickPoint.withY(originalPosition.y());
      }
    }
    if (this.backPlane != null) {
      skewedPickPoint = this.backPlane.getIntersection(pickRay);
      //force the pick point to be at the original Y level
      if (skewedPickPoint != null) {
        skewedPickPoint = skewedPickPoint.withY(originalPosition.y());
      }
    }
    Point3 pointToUse;
    if ((levelPickPoint == null) && (skewedPickPoint == null)) {
      pointToUse = null;
    } else if ((levelPickPoint == null) && (skewedPickPoint != null)) {
      pointToUse = skewedPickPoint;
    } else if ((levelPickPoint != null) && (skewedPickPoint == null)) {
      pointToUse = levelPickPoint;
    } else {
      Point3 cameraPosition = this.getCamera().getParent().getAbsoluteTransformation().translation();
      double levelDistanceToCamera = cameraPosition.distanceFrom(levelPickPoint);
      double skewedDistanceToCamera = cameraPosition.distanceFrom(skewedPickPoint);
      if (levelDistanceToCamera <= skewedDistanceToCamera) {
        pointToUse = levelPickPoint;
      } else {
        pointToUse = skewedPickPoint;
      }
    }
    if (pointToUse != null) {
      return pointToUse.withY(this.manipulatedTransformable.getAbsoluteTransformation().translation().y());
    } else {
      return null;
    }
  }

  private Vector3 getPerspectiveMovementVector(InputState currentInput, InputState previousInput) {
    if ((this.pickPlane == null) && (this.backPlane == null)) {
      int mouseDifX = (currentInput.getMouseLocation().x - previousInput.getMouseLocation().x);
      double rightMovement = mouseDifX * this.movementScale;
      Vector3 rightVector = this.camera.getAbsoluteTransformation().orientation().right().withY(0).normalized();
      Vector3 movementVector = rightVector.times(rightMovement);
      int mouseDifY = (currentInput.getMouseLocation().y - previousInput.getMouseLocation().y);
      double forwardMovement = mouseDifY * this.movementScale;
      Vector3 backwardVector = this.camera.getAbsoluteTransformation().orientation().backward().withY(0).normalized();
      if (backwardVector.isNaN()) {
        backwardVector = this.camera.getAbsoluteTransformation().orientation().up().withY(0).negate().normalized();
      }
      return movementVector.plus(backwardVector.times(forwardMovement));
    }
    Point3 pointToUse = getPerspectivePositionBasedOnInput(currentInput);
    if (pointToUse != null) {
      return pointToUse.minus(this.getManipulatedTransformable().getAbsoluteTransformation().translation()).withY(0);
    } else {
      return Vector3.ZERO;
    }
  }

  @Override
  public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
    if (!currentInput.getMouseLocation().equals(previousInput.getMouseLocation()) && (this.manipulatedTransformable != null)) {
      if (!this.hasMoved) {
        this.hasMoved = true;
        this.hideCursor();
      }

      Vector3 movementVector = getMovementVectorBasedOnCamera(currentInput, previousInput);
      Point3 currentPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation();
      Point3 newPosition = currentPosition.plus(movementVector);

      newPosition = SnapUtilities.doMovementSnapping(this.manipulatedTransformable, newPosition, this.dragAdapter, this.manipulatedTransformable.getRoot(), this.getCamera());

      //Send manipulation events
      Vector3 movementDif = newPosition.minus(this.manipulatedTransformable.getAbsoluteTransformation().translation());
      if (movementDif.x() > .1) {
        movementVector = getMovementVectorBasedOnCamera(currentInput, previousInput);
      }
      movementDif = movementDif.normalized();
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
        planeTransitionPointDebugSphere.setLocalTranslation(newPosition);
        //        Point awtPoint = getMouseCursorPositionInLookingGlass();
        //        if (!isPointInsideLookingGlass(awtPoint))
        //        {
        //          moveCursorToPointInLookingGlass(awtPoint);
        //        }
      }
    }
  }

  @Override
  public void doClickManipulator(InputState clickInput, InputState previousInput) {
    //Do nothing
  }

  @Override
  public void doEndManipulator(InputState endInput, InputState previousInput) {
    removePlaneTransitionPointSphereFromScene();
    this.showCursor();
  }

  protected AbstractTransformable getInitialTransformable(InputState startInput) {
    return startInput.getCurrentlySelectedObject() == null
        ? startInput.getClickPickTransformable()
        : startInput.getCurrentlySelectedObject();
  }

  protected Point3 getInitialClickPoint(InputState startInput) {
    Point3 initialClickPoint = startInput.getClickPickResult().getPositionInSource();
    initialClickPoint = startInput.getClickPickResult().getSource().transformTo(initialClickPoint, startInput.getClickPickResult().getSource().getRoot());
    return initialClickPoint;
  }

  @Override
  public boolean doStartManipulator(InputState startInput) {
    this.setManipulatedTransformable(this.getInitialTransformable(startInput));
    this.hidCursor = false;
    if (this.manipulatedTransformable != null) {
      this.initializeEventMessages();
      this.hasMoved = false;
      this.originalPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation();
      AffineMatrix4x4 cameraTransform = this.getCamera().getParent().getAbsoluteTransformation();
      Vector3 cameraFacingNormal = cameraTransform.orientation().getBackward().negate();
      this.orthographicPickPlane = Plane.createInstance(this.originalPosition, cameraFacingNormal);

      Ray orthoPickRay = this.onscreenRenderTarget.getRayAtAwtPoint(startInput.getMouseLocation(), this.getCamera());
      Point3 orthoPickPoint = orthographicPickPlane.getIntersection(orthoPickRay);
      orthographicOffsetToOrigin = orthoPickPoint != null
          ? originalPosition.minus(orthoPickPoint)
          : originalPosition.asVector();

      Point3 initialClickPoint = this.getInitialClickPoint(startInput);

      this.offsetFromOrigin = initialClickPoint.minus(this.originalPosition);
      this.mousePlaneOffset = calculateMousePlaneOffset(startInput.getMouseLocation(), this.manipulatedTransformable);

      //We don't need special planes for the orthographic camera
      if (!(this.getCamera() instanceof OrthographicCamera)) {
        Point mousePoint = new Point(startInput.getMouseLocation().x + this.mousePlaneOffset.x, startInput.getMouseLocation().y + this.mousePlaneOffset.y);
        setUpPlanes(this.originalPosition, mousePoint);
      }

      addPlaneTransitionPointSphereToScene();
      return true;
    }
    return false;
  }

  protected void setUpPlanes(Point3 planePosition, Point mousePoint) {
    Plane horizontalPlane = Plane.createInstance(planePosition, Vector3.POSITIVE_Y_AXIS);
    AffineMatrix4x4 cameraTransform = this.getCamera().getParent().getAbsoluteTransformation();
    boolean isAbove = cameraTransform.translation().y() > planePosition.y();

    Point3 pointInCameraSidewaysPlane = Plane.createInstance(cameraTransform.translation(), cameraTransform.orientation().getRight()).projected(planePosition);
    Vector3 toCamera = cameraTransform.translation().minus(pointInCameraSidewaysPlane).normalized();
    double verticalDistance = Math.abs(cameraTransform.translation().y() - planePosition.y());
    double distance = planePosition.distanceFrom(cameraTransform.translation());
    double dot = toCamera.dotProduct(cameraTransform.orientation().getBackward());
    double dotLevel = Vector3.POSITIVE_Y_AXIS.dotProduct(cameraTransform.orientation().getUp());
    Angle angle = toCamera.angleWith(cameraTransform.orientation().getBackward());
    Angle angleUp = cameraTransform.orientation().getUp().angleWith(Vector3.POSITIVE_Y_AXIS);

    Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(mousePoint, this.getCamera());
    double pickDotHorizontal = pickRay.direction().dotProduct(Vector3.POSITIVE_Y_AXIS);
    //    PrintUtilities.println("pick: "+pickDotHorizontal+", object dot: "+dot+", leveDot: "+dotLevel+", angle: "+angle.getAsDegrees()+", angle up: "+angleUp.getAsDegrees());
    if (Math.abs(pickDotHorizontal) < .001) {
      //      PrintUtilities.println("Special!");
      this.pickPlane = null;
      this.backPlane = null;
      double distanceToObject = planePosition.distanceFrom(cameraTransform.translation());
      this.movementScale = distanceToObject * .001;
      return;
    }

    double newY = Math.tan((Math.PI * 75.0) / (180.0));
    if (!isAbove) {
      newY *= -1;
    }
    Vector3 worstCasePlaneNormal = cameraTransform.orientation().getBackward().withY(0).normalized().withY(newY).normalized();

    Ray centerRay = this.onscreenRenderTarget.getRayAtAwtPoint(mousePoint, this.getCamera());
    Ray oneUp = this.onscreenRenderTarget.getRayAtAwtPoint(new Point(mousePoint.x, mousePoint.y - 1), this.getCamera());
    Ray oneDown = this.onscreenRenderTarget.getRayAtAwtPoint(new Point(mousePoint.x, mousePoint.y + 1), this.getCamera());
    Point3 centerPoint = horizontalPlane.getIntersection(centerRay);
    Point3 upPoint = horizontalPlane.getIntersection(oneUp);
    Point3 downPoint = horizontalPlane.getIntersection(oneDown);

    boolean shouldUseHorizontalPlane = false;
    if ((centerPoint != null) && (upPoint != null) && (downPoint != null)) {
      // double cameraHeight = Math.abs(cameraTransform.translation().y() - planePosition.y());
      // double cameraDotUp = cameraTransform.orientation().getUp().dotProduct(Vector3.POSITIVE_Y_AXIS);
      //      System.out.println("height: "+cameraHeight+", dot: "+cameraDotUp+", ratio: "+cameraDotUp/cameraHeight);

      double distanceUp = centerPoint.distanceFrom(upPoint);
      double distanceDown = centerPoint.distanceFrom(downPoint);
      double higher, lower;
      Vector3 awayFromCamera;
      if (distanceUp >= distanceDown) {
        higher = distanceUp;
        lower = distanceDown;
        awayFromCamera = upPoint.minus(centerPoint).normalized();
      } else {
        higher = distanceDown;
        lower = distanceUp;
        awayFromCamera = downPoint.minus(centerPoint).normalized();
      }
      double distanceIncreasePerPixel = (higher - lower);
      double pixelsToMaxPixel = (MAX_DISTANCE_PER_PIXEL - higher) / distanceIncreasePerPixel;
      double totalExtraDistance = MAX_DISTANCE_PER_PIXEL * pixelsToMaxPixel * .5; //basically the integration of this linear equations
      Vector3 extraVector = awayFromCamera.times(totalExtraDistance);

      planePosition = planePosition.plus(extraVector);
      shouldUseHorizontalPlane = true;
    }

    //    planeTransitionPointDebugSphere.setLocalTranslation(planePosition);
    Plane backPlane = Plane.createInstance(planePosition, worstCasePlaneNormal);
    if (shouldUseHorizontalPlane) {
      this.pickPlane = horizontalPlane;
    } else {
      this.pickPlane = backPlane;
    }
    this.backPlane = backPlane;
  }

  protected Point calculateMousePlaneOffset(Point mousePosition, AbstractTransformable transformable) {
    Point3 pointInCamera = transformable.getTranslation(this.getCamera());
    Point awtPoint = this.onscreenRenderTarget.transformFromCameraToAWT(pointInCamera, this.getCamera());
    return new Point(awtPoint.x - mousePosition.x, awtPoint.y - mousePosition.y);
  }

  protected boolean isPointInsideLookingGlass(Point awtPoint) {
    return this.onscreenRenderTarget.getAwtComponent().contains(awtPoint);
  }

  protected Point getMouseCursorPositionInLookingGlass() {
    Point3 new3DPoint = this.manipulatedTransformable.getAbsoluteTransformation().translation().plus(this.offsetFromOrigin);
    Point3 pointInCamera = this.camera.transformFrom(new3DPoint, this.camera.getRoot());
    Point awtPoint = this.onscreenRenderTarget.transformFromCameraToAWT(pointInCamera, this.getCamera());
    return awtPoint;
  }

  protected void moveCursorToPointInLookingGlass(Point awtPoint) {
    RobotUtilities.mouseMove(this.onscreenRenderTarget.getAwtComponent(), awtPoint);
  }

  protected void moveCursorToObjectRelativePosition() {
    Point awtPoint = getMouseCursorPositionInLookingGlass();
    moveCursorToPointInLookingGlass(awtPoint);
  }

  protected void hideCursor() {
    CursorUtilities.pushAndSet(this.onscreenRenderTarget.getAwtComponent(), CursorUtilities.NULL_CURSOR);
    this.hidCursor = true;
  }

  protected void showCursor() {
    if (this.hidCursor) {
      this.moveCursorToObjectRelativePosition();
      CursorUtilities.popAndSet(this.onscreenRenderTarget.getAwtComponent());
    }
  }

  @Override
  public void doTimeUpdateManipulator(double time, InputState currentInput) {
  }

  @Override
  protected HandleSet getHandleSetToEnable() {
    return HandleSet.ABSOLUTE_GROUND_TRANSLATION_VISUALIZATION;
  }

  private final DebugSphere planeTransitionPointDebugSphere = new DebugSphere();
  private Plane pickPlane = Plane.XZ_PLANE;
  private Plane backPlane;
  protected Plane orthographicPickPlane = Plane.XZ_PLANE;
  protected Vector3 orthographicOffsetToOrigin = null;
  protected Boolean hasMoved = false;
  protected Point3 originalPosition = null;
  protected Vector3 offsetFromOrigin = null;
  protected Point mousePlaneOffset;
  protected boolean hidCursor = false;
  private double movementScale = 1.0;
  protected AbstractCamera camera = null;
  protected OnscreenRenderTarget onscreenRenderTarget;
}
