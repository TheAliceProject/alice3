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

import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.debug.DebugSphere;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Angle;
import org.alice.math.immutable.OrthogonalMatrix3x3;
import org.alice.math.immutable.Plane;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;

public class CameraTiltDragManipulator extends CameraManipulator implements OnscreenPicturePlaneInformedManipulator {

  private static final boolean SHOW_PICK_POINT = false;

  @Override
  public OnscreenRenderTarget getOnscreenRenderTarget() {
    return this.onscreenRenderTarget;
  }

  @Override
  public void setOnscreenRenderTarget(OnscreenRenderTarget onscreenRenderTarget) {
    this.onscreenRenderTarget = onscreenRenderTarget;
  }

  private void addPickPointSphereToScene() {
    if (SHOW_PICK_POINT) {
      if ((this.camera != null) && (this.pickPointDebugSphere.getParent() == null)) {
        this.camera.getRoot().addComponent(this.pickPointDebugSphere);
      }
    }
  }

  private void removePickPointSphereFromScene() {
    if (SHOW_PICK_POINT) {
      if ((this.camera != null) && (this.pickPointDebugSphere.getParent() == this.camera.getRoot())) {
        this.camera.getRoot().removeComponent(this.pickPointDebugSphere);
      }
    }
  }

  private void setPickPoint(Point3 position) {
    if (SHOW_PICK_POINT) {
      this.pickPointDebugSphere.setLocalTranslation(position);
    }
  }

  public void setPlaneDiscPoint(Point3 planeDiscPoint) {
    this.pickPoint = planeDiscPoint;
    setPickPoint(this.pickPoint);

  }

  @Override
  public String getUndoRedoDescription() {
    return "Camera Rotate";
  }

  @Override
  public CameraView getDesiredCameraView() {
    return CameraView.PICK_CAMERA;
  }

  @Override
  public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
    Ray oldPickRay = this.onscreenRenderTarget.getRayAtAwtPoint(previousInput.getMouseLocation(), this.getCamera());
    Ray newPickRay = this.onscreenRenderTarget.getRayAtAwtPoint(currentInput.getMouseLocation(), this.getCamera());
    Point3 oldPickPoint = this.cameraFacingPickPlane.getIntersection(oldPickRay);
    Point3 newPickPoint = this.cameraFacingPickPlane.getIntersection(newPickRay);
    if (newPickPoint == null || oldPickPoint == null) {
      return;
    }
    this.setPlaneDiscPoint(pickPoint);

    Point3 oldPointInCamera = this.camera.transformFrom(oldPickPoint, this.camera.getRoot());
    Point3 newPointInCamera = this.camera.transformFrom(newPickPoint, this.camera.getRoot());

    Vector3 xDif = new Vector3(newPointInCamera.x(), oldPointInCamera.y(), oldPointInCamera.z()).normalized();
    Vector3 yDif = new Vector3(oldPointInCamera.x(), newPointInCamera.y(), oldPointInCamera.z()).normalized();

    Vector3 oldDirection = oldPointInCamera.asVector().normalized();

    Angle xAngle = oldDirection.angleWith(xDif);
    if (currentInput.getMouseLocation().x < previousInput.getMouseLocation().x) {
      xAngle = xAngle.negated();
    }
    Angle yAngle = oldDirection.angleWith(yDif);
    if (currentInput.getMouseLocation().y < previousInput.getMouseLocation().y) {
      yAngle = yAngle.negated();
    }

    StandIn standIn = new StandIn();
    standIn.setName("CameraOrbitStandIn");
    standIn.setVehicle(this.getCamera().getRoot());
    try {
      standIn.setTransformation(this.manipulatedTransformable.getAbsoluteTransformation(), AsSeenBy.SCENE);
      standIn.setAxesOnlyToStandUp();
      this.manipulatedTransformable.applyRotationAboutXAxis(yAngle, standIn);
      this.manipulatedTransformable.applyRotationAboutYAxis(xAngle, standIn);
    } finally {
      standIn.setVehicle(null);
    }

    //Make sure the camera's x-axis is still horizontal
    AffineMatrix4x4 cameraTransform = this.manipulatedTransformable.getAbsoluteTransformation();
    OrthogonalMatrix3x3 camOrientation = cameraTransform.orientation();
    Vector3 rightAxis = camOrientation.getRight().withY(0).normalized();
    Vector3 upAxis = camOrientation.getBackward().crossProduct(rightAxis).normalized();
    camOrientation = new OrthogonalMatrix3x3(rightAxis, upAxis, camOrientation.backward());
    this.manipulatedTransformable.setTransformation(new AffineMatrix4x4(camOrientation, cameraTransform.translation()), AsSeenBy.SCENE);

    this.cameraFacingPickPlane = Plane.createInstance(newPickPoint, this.manipulatedTransformable.getAbsoluteTransformation().orientation().backward());
    manipulatedTransformable.notifyTransformationListeners();
  }

  @Override
  public void doEndManipulator(InputState endInput, InputState previousInput) {
    removePickPointSphereFromScene();
  }

  @Override
  public void doClickManipulator(InputState clickInput, InputState previousInput) {
    //Do nothing
  }

  @Override
  public boolean doStartManipulator(InputState startInput) {
    if (super.doStartManipulator(startInput) && (this.camera instanceof SymmetricPerspectiveCamera)) {
      boolean success = false;
      Vector3 cameraForward = this.manipulatedTransformable.getAbsoluteTransformation().orientation().backward().times(-10.0d);

      addPickPointSphereToScene();

      Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(startInput.getMouseLocation(), this.getCamera());

      Point3 planePoint = manipulatedTransformable.getAbsoluteTransformation().translation().plus(cameraForward);
      this.cameraFacingPickPlane = Plane.createInstance(planePoint, this.manipulatedTransformable.getAbsoluteTransformation().orientation().backward());

      Point3 pickPoint = this.cameraFacingPickPlane.getIntersection(pickRay);
      if (pickPoint != null) {
        this.setPlaneDiscPoint(pickPoint);
        success = true;
      }
      return success;
    }
    return false;
  }

  @Override
  public void doTimeUpdateManipulator(double time, InputState currentInput) {
  }

  private final DebugSphere pickPointDebugSphere = new DebugSphere();

  private Plane cameraFacingPickPlane;
  private Point3 pickPoint = null;
  private OnscreenRenderTarget onscreenRenderTarget;
}
