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
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.MovementType;
import org.alice.interact.PickHint;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.RotationRingHandle;
import org.alice.interact.handle.StoodUpRotationRingHandle;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Angle;
import org.alice.math.immutable.AngleInRadians;
import org.alice.math.immutable.Plane;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;

import java.awt.Point;

/**
 * @author David Culyba
 */
public class ObjectRotateDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {

  private static final double BAD_ANGLE_THRESHOLD = 2.0d * Math.PI * (15.0d / 360.0d);
  private static final double WORLD_DISTANCE_TO_RADIANS_MULTIPLIER = 1.1d;

  //  protected Sphere sgSphere = new Sphere();
  //  protected Transformable sphereTransformable = new Transformable();
  //  protected Visual sgSphereVisual = new Visual();

  //  private void DEBUG_setupDebugSphere()
  //  {
  //    SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
  //    sgFrontFacingAppearance.diffuseColor.setValue( Color4f.RED );
  //    sgFrontFacingAppearance.opacity.setValue(1.0);
  //
  //    this.sgSphereVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
  //    this.sgSphereVisual.setParent( this.sphereTransformable );
  //    this.sgSphereVisual.geometries.setValue( new Geometry[] { this.sgSphere } );
  //    this.sgSphere.radius.setValue( .2d);
  //  }
  //
  //  private void DEBUG_addDebugSphereToScene()
  //  {
  //    if (this.camera != null && this.sphereTransformable.getParent() == null)
  //    {
  //      this.camera.getRoot().addComponent(this.sphereTransformable);
  //    }
  //  }
  //
  //  private void DEBUG_removeDebugSphereFromScene()
  //  {
  //    if (this.camera != null && this.sphereTransformable.getParent() == this.camera.getRoot())
  //    {
  //      this.camera.getRoot().removeComponent(this.sphereTransformable);
  //    }
  //  }
  //
  //  private void DEBUG_setDebugSpherePosition(Tuple3 position)
  //  {
  //    this.sphereTransformable.setTranslationOnly(position, AsSeenBy.SCENE);
  //  }

  public ObjectRotateDragManipulator() {
    //    DEBUG_setupDebugSphere();
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
  protected void initializeEventMessages() {
    this.setMainManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Rotate, null, this.manipulatedTransformable));
    this.clearManipulationEvents();
    if (rotationHandle != null) {
      MovementType type = this.rotationHandle instanceof StoodUpRotationRingHandle ? MovementType.STOOD_UP : MovementType.LOCAL;
      this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Rotate, new MovementDescription(this.rotationHandle.getRotationDirection(), type), this.manipulatedTransformable));
    }
  }

  protected void initManipulator(RotationRingHandle handle, InputState startInput) {
    //    DEBUG_addDebugSphereToScene();
    this.hidCursor = false;
    this.rotationHandle = handle;
    this.setManipulatedTransformable(this.rotationHandle.getManipulatedObject());
    this.absoluteRotationAxis = this.rotationHandle.getReferenceFrame().getAbsoluteTransformation().transform(rotationHandle.getRotationAxis()).normalized();
    //PickResult pick = this.onscreenLookingGlass.pickFrontMost( startInput.getMouseLocation().x, startInput.getMouseLocation().y, /*isSubElementRequired=*/false );
    this.initialClickPoint = startInput.getClickPickResult().getPositionInSource();
    this.initialClickPoint = startInput.getClickPickResult().getSource().transformTo(this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot());
    Vector3 rotationAxis = this.absoluteRotationAxis;
    this.rotationPlane = Plane.createInstance(this.initialClickPoint, rotationAxis);

    this.rotationHandle.initializeSnapReferenceFrame();

    Ray originRay = new Ray(this.manipulatedTransformable.getAbsoluteTransformation().translation(), rotationAxis);

    this.objectOriginInPlane = this.rotationPlane.getIntersection(originRay);
    if (this.objectOriginInPlane == null) {
      originRay = new Ray(this.manipulatedTransformable.getAbsoluteTransformation().translation(), rotationAxis.negate());
      this.objectOriginInPlane = this.rotationPlane.getIntersection(originRay);
    }
    if (this.objectOriginInPlane != null) {
      Vector3 toMouse = this.initialClickPoint.minus(this.objectOriginInPlane).normalized();
      this.originalMouseDirection = toMouse;
      this.originalMouseRightDirection = this.originalMouseDirection.crossProduct(rotationAxis);

      this.rotationHandle.setSphereVisibility(true);
      Vector3 sphereDirection = this.rotationHandle.transformFromAbsolute(toMouse);
      this.rotationHandle.setSphereDirection(sphereDirection);
      //Hide the cursor

    }

    //    DEBUG_setDebugSpherePosition(this.initialClickPoint);

    this.cameraFacingPlane = Plane.createInstance(this.initialClickPoint, this.getCamera().getAbsoluteTransformation().orientation().backward());
    this.originalLocalTransformation = manipulatedTransformable.getLocalTransformation();
    this.originalAbsoluteTransformation = manipulatedTransformable.getAbsoluteTransformation();
    this.originalAngleBasedOnMouse = getRotationBasedOnMouse(startInput.getMouseLocation());
  }

  @Override
  public boolean doStartManipulator(InputState startInput) {
    if (startInput.getClickPickHint().intersects(PickHint.PickType.THREE_D_HANDLE.pickHint())) {
      AbstractTransformable clickedHandle = startInput.getClickPickedTransformable(true);
      if (clickedHandle instanceof RotationRingHandle handle) {
        this.initManipulator(handle, startInput);
        return true;
      }
    }
    return false;

  }

  protected Angle getRotationBasedOnMouse(Point mouseLocation) {
    Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(mouseLocation, this.getCamera());
    if (pickRay == null) {
      return null;
    }
    Angle angleBetweenVector = this.absoluteRotationAxis.angleWith(this.getCamera().getAbsoluteTransformation().orientation().backward());
    double distanceToRightAngle = Math.abs((Math.PI * .5d) - angleBetweenVector.getAsRadians());
    if (distanceToRightAngle < BAD_ANGLE_THRESHOLD) {
      Point3 pointInPlane = this.cameraFacingPlane.getIntersection(pickRay);
      if (pointInPlane == null) {
        return null;
      }
      Vector3 fromOriginalMouseToCurrentMouse = pointInPlane.minus(this.initialClickPoint);
      Vector3 rotationRightAxis = this.absoluteRotationAxis.crossProduct(this.getCamera().getAbsoluteTransformation().orientation().backward());
      double mouseDistance = fromOriginalMouseToCurrentMouse.dotProduct(rotationRightAxis);

      return new AngleInRadians(mouseDistance * WORLD_DISTANCE_TO_RADIANS_MULTIPLIER);
    }
    Point3 pointInPlane = this.rotationPlane.getIntersection(pickRay);
    if (pointInPlane == null) {
      return null;
    }

    //<DEBUG>
    //          Point3 pickOrigin = new Point3(pickRay.accessOrigin());
    //          pickOrigin.y = 0;
    //          DEBUG_setDebugSpherePosition(pickOrigin);
    //</DEBUG>

    Vector3 toMouse = pointInPlane.minus(this.objectOriginInPlane);
    double toMouseDotOriginalRight = toMouse.dotProduct(this.originalMouseRightDirection);
    //          double toMouseDotOriginalRight =  toMouse.dotProduct(this.originalAbsoluteTransformation.orientation().getRight() );
    boolean isToTheRight = toMouseDotOriginalRight > 0.0d;
    Vector3 toMouseDirection = toMouse.normalized();
    double cosOfAngleBetween = this.originalMouseDirection.dotProduct(toMouseDirection);
    //          double cosOfAngleBetween = this.originalAbsoluteTransformation.orientation().getBackward().times(-1).dotProduct(toMouseDirection );
    if (cosOfAngleBetween > 1.0d) {
      cosOfAngleBetween = 1.0d;
    } else if (cosOfAngleBetween < -1.0d) {
      cosOfAngleBetween = -1.0d;
    }
    double angleInRadians = Math.acos(cosOfAngleBetween);
    if (isToTheRight) {
      angleInRadians = (Math.PI * 2.0d) - angleInRadians;
    }
    return new AngleInRadians(angleInRadians);
  }

  @Override
  public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
    if (currentInput.getMouseLocation().equals(previousInput.getMouseLocation())) {
      return;
    }
    if (!this.hidCursor) {
      this.hideCursor();
    }
    Angle currentAngle = getRotationBasedOnMouse(currentInput.getMouseLocation());
    if ((currentAngle != null) && (this.originalAngleBasedOnMouse != null)) {
      Angle angleDif = currentAngle.minus(this.originalAngleBasedOnMouse);
      //The angleDif is the amount the object is rotated relative to the start of the manipulation
      //By snapping on angleDif, we're snapping to snap angles relative to the orientation at the start of the manipulation
      Angle snappedAngle = SnapUtilities.doRotationSnapping(angleDif, this.dragAdapter);

      this.manipulatedTransformable.setLocalTransformation(this.originalLocalTransformation);
      this.manipulatedTransformable.applyRotationAboutArbitraryAxis(this.rotationHandle.getRotationAxis(), snappedAngle, this.rotationHandle.getReferenceFrame());
      manipulatedTransformable.notifyTransformationListeners();

      if (snappedAngle.isCloseTo(angleDif)) {
        SnapUtilities.hideRotationSnapVisualization();
      } else {
        SnapUtilities.showSnapRotation(this.rotationHandle);
      }
    }
  }

  @Override
  public void doTimeUpdateManipulator(double time, InputState currentInput) {
  }

  @Override
  public void doClickManipulator(InputState clickInput, InputState previousInput) {
    //Do nothing
  }

  protected void hideCursor() {
    CursorUtilities.pushAndSet(this.onscreenRenderTarget.getAwtComponent(), CursorUtilities.NULL_CURSOR);
    this.hidCursor = true;
  }

  protected void showCursor() {
    if (this.hidCursor) {
      try {
        Point3 pointInCamera = this.rotationHandle.getSphereLocation(this.getCamera());
        Point awtPoint = this.onscreenRenderTarget.transformFromCameraToAWT(pointInCamera, this.getCamera());
        RobotUtilities.mouseMove(this.onscreenRenderTarget.getAwtComponent(), awtPoint);
      } finally {
        CursorUtilities.popAndSet(this.onscreenRenderTarget.getAwtComponent());
        //mmay ask dave?
        hidCursor = false;
      }
    }
  }

  @Override
  public String getUndoRedoDescription() {
    return "Object Rotate";
  }

  @Override
  public void doEndManipulator(InputState endInput, InputState previousInput) {
    this.rotationHandle.setSphereVisibility(false);
    SnapUtilities.hideRotationSnapVisualization();
    this.showCursor();
    //    DEBUG_removeDebugSphereFromScene();
  }

  @Override
  protected HandleSet getHandleSetToEnable() {
    return new HandleSet(this.rotationHandle.getRotationDirection().getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.ROTATION);
  }

  private Point3 initialClickPoint = Point3.ORIGIN;
  private Point3 objectOriginInPlane;
  private Plane rotationPlane;
  private Vector3 originalMouseDirection;
  private Vector3 originalMouseRightDirection;
  private Vector3 absoluteRotationAxis;
  private Angle originalAngleBasedOnMouse;
  private AffineMatrix4x4 originalLocalTransformation;
  private AffineMatrix4x4 originalAbsoluteTransformation;
  private Plane cameraFacingPlane;
  protected RotationRingHandle rotationHandle;
  private AbstractCamera camera = null;
  private OnscreenRenderTarget onscreenRenderTarget;
  private boolean hidCursor = false;
}
