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
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.PickHint;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.LinearDragHandle;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Plane;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;

import java.awt.Point;

/**
 * @author David Culyba
 */
public class LinearDragManipulator extends AbstractManipulator implements CameraInformedManipulator, OnscreenPicturePlaneInformedManipulator {
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
    return "LinearDrag - Object Move";
  }

  @Override
  protected void initializeEventMessages() {
    this.setMainManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, null, this.manipulatedTransformable));
    this.clearManipulationEvents();
    if (this.linearHandle != null) {
      this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, this.linearHandle.getMovementDescription(), this.manipulatedTransformable));
      MovementDirection oppositeDirection = this.linearHandle.getMovementDescription().direction.getOpposite();
      if (oppositeDirection != this.linearHandle.getMovementDescription().direction) {
        this.addManipulationEvent(new ManipulationEvent(ManipulationEvent.EventType.Translate, new MovementDescription(oppositeDirection, this.linearHandle.getMovementDescription().type), this.manipulatedTransformable));
      }
    }
  }

  protected double getDistanceAlongAxisBasedOnMouse(Point mouseLocation) {
    Ray pickRay = this.onscreenRenderTarget.getRayAtAwtPoint(mouseLocation, this.getCamera());
    if (pickRay != null) {
      Vector3 cameraBack = this.getCamera().getAbsoluteTransformation().orientation().backward();
      double axisCameraDot = this.absoluteDragAxis.dotProduct(cameraBack);
      if (Math.abs(axisCameraDot) > .98d) {
        Point3 pointInPlane = this.cameraFacingPlane.getIntersection(pickRay);
        if (pointInPlane == null || pointInPlane.isNaN()) {
          return 0;
        }
        Vector3 fromOriginalMouseToCurrentMouse = pointInPlane.minus(this.previousClickPoint);
        this.previousClickPoint = pointInPlane;
        Vector3 dragRightAxis = this.getCamera().getAbsoluteTransformation().orientation().right().normalized();
        Vector3 dragUpAxis = this.getCamera().getAbsoluteTransformation().orientation().up().normalized();

        double leftRightSign = 1.0d;
        if (this.absoluteDragAxis.dotProduct(dragRightAxis) < 0.0d) {
          leftRightSign = -1.0d;
        }
        double upDownSign = 1.0d;
        if (this.absoluteDragAxis.dotProduct(dragUpAxis) < 0.0d) {
          upDownSign = -1.0d;
        }

        double mouseYDistance = upDownSign * fromOriginalMouseToCurrentMouse.dotProduct(dragUpAxis);
        double mouseXDistance = leftRightSign * fromOriginalMouseToCurrentMouse.dotProduct(dragRightAxis);
        double newDistance = this.currentDistanceAlongAxis + mouseYDistance + mouseXDistance;
        return newDistance;
      } else {
        Point3 pointInPlane = this.handleAlignedPlane.getIntersection(pickRay);
        if (pointInPlane != null) {
          Vector3 pointVector = pointInPlane.minus(this.originalOrigin);
          double dragAmount = pointVector.dotProduct(this.absoluteDragAxis);
          return dragAmount;
        }
      }
    }
    return 0;
  }

  protected void updateBasedOnHandlePull(double initialPull, double newPull) {
    Vector3 translationFromOriginal = this.linearHandle.getDragAxis().times((newPull - initialPull));

    //Translate the translation vector into scene space for snapping
    AffineMatrix4x4 toSceneTransform = this.linearHandle.getReferenceFrame().getAbsoluteTransformation();
    Vector3 sceneSpaceTranslation = translationFromOriginal;
    toSceneTransform.transform(sceneSpaceTranslation);

    //Calculate the new position based on the current mouse position
    Point3 absoluteNewPosition = this.originalOrigin.plus(sceneSpaceTranslation);

    //Apply any snap as necessary
    absoluteNewPosition = SnapUtilities.doMovementSnapping(this.manipulatedTransformable, absoluteNewPosition, this.dragAdapter, this.linearHandle.getSnapReferenceFrame(), this.getCamera());

    //Calculate handle-relative translation vector
    Vector3 movementVector = absoluteNewPosition.minus(this.manipulatedTransformable.getAbsoluteTransformation().translation());
    Vector3 movementDif = this.linearHandle.getReferenceFrame().getAbsoluteTransformation().transform(movementVector).normalized();

    for (ManipulationEvent event : this.getManipulationEvents()) {
      double dot = event.getMovementDescription().direction.getVector().dotProduct(movementDif);
      if (dot > 0.1d) {
        this.dragAdapter.triggerManipulationEvent(event, true);
      } else if (dot < -.07d) {
        this.dragAdapter.triggerManipulationEvent(event, false);
      }
    }
    //    this.manipulatedTransformable.setTranslationOnly(this.originalOrigin, AsSeenBy.SCENE);
    //    this.manipulatedTransformable.applyTranslation(translationFromOriginal, this.linearHandle.getReferenceFrame());
    //    Point3 finalPosition = this.manipulatedTransformable.getAbsoluteTransformation().translation;
    this.manipulatedTransformable.setTranslationOnly(absoluteNewPosition, AsSeenBy.SCENE);
  }

  @Override
  public void doDataUpdateManipulator(InputState currentInput, InputState previousInput) {
    if (!currentInput.getMouseLocation().equals(previousInput.getMouseLocation())) {
      this.currentDistanceAlongAxis = getDistanceAlongAxisBasedOnMouse(currentInput.getMouseLocation());
      updateBasedOnHandlePull(this.initialDistanceAlongAxis, this.currentDistanceAlongAxis);
    }

  }

  @Override
  public void doEndManipulator(InputState endInput, InputState previousInput) {
  }

  @Override
  public boolean doStartManipulator(InputState startInput) {
    if (startInput.getClickPickHint().intersects(PickHint.PickType.THREE_D_HANDLE.pickHint())) {
      AbstractTransformable clickedHandle = startInput.getClickPickedTransformable(true);
      if (clickedHandle instanceof LinearDragHandle handle) {
        this.linearHandle = handle;
        this.setManipulatedTransformable(this.linearHandle.getManipulatedObject());
        this.initializeEventMessages();
        this.absoluteDragAxis = this.linearHandle.getReferenceFrame().getAbsoluteTransformation().transform(this.linearHandle.getDragAxis());

        this.initialClickPoint = startInput.getClickPickResult().getPositionInSource();
        this.initialClickPoint = startInput.getClickPickResult().getSource().transformTo(this.initialClickPoint, startInput.getClickPickResult().getSource().getRoot());
        this.previousClickPoint = this.initialClickPoint;

        Vector3 toCamera = this.getCamera().getAbsoluteTransformation().translation().
            minus(this.manipulatedTransformable.getAbsoluteTransformation().translation()).
            normalized();
        Vector3 axisAlignedNormal = null;
        if (Math.abs(toCamera.dotProduct(this.absoluteDragAxis)) > .99d) {
          axisAlignedNormal = toCamera;
        } else {
          Vector3 axisAlignedCameraVector = toCamera.projectedOnto(this.absoluteDragAxis);
          axisAlignedNormal = toCamera.minus(axisAlignedCameraVector).normalized();
        }
        this.handleAlignedPlane = Plane.createInstance(this.linearHandle.getAbsoluteTransformation().translation(), axisAlignedNormal);
        this.cameraFacingPlane = Plane.createInstance(this.initialClickPoint, this.getCamera().getAbsoluteTransformation().orientation().backward());
        this.originalOrigin = this.manipulatedTransformable.getAbsoluteTransformation().translation();
        this.currentDistanceAlongAxis = this.linearHandle.getCurrentHandleLength();
        this.initialDistanceAlongAxis = getDistanceAlongAxisBasedOnMouse(startInput.getMouseLocation());
        this.currentDistanceAlongAxis = this.initialDistanceAlongAxis;
        return true;
      }
    }
    return false;
  }

  @Override
  public void doClickManipulator(InputState clickInput, InputState previousInput) {
    //Do nothing
  }

  @Override
  public void doTimeUpdateManipulator(double time, InputState currentInput) {
  }

  @Override
  protected HandleSet getHandleSetToEnable() {
    HandleSet.HandleGroup translationType;
    if (this.linearHandle.getMovementDescription().type == MovementType.STOOD_UP) {
      translationType = HandleSet.HandleGroup.STOOD_UP_TRANSLATION;
    } else if (this.linearHandle.getMovementDescription().type == MovementType.ABSOLUTE) {
      translationType = HandleSet.HandleGroup.ABSOLUTE_TRANSLATION;
    } else {
      translationType = HandleSet.HandleGroup.TRANSLATION;
    }
    return new HandleSet(this.linearHandle.getMovementDescription().direction.getHandleGroup(), HandleSet.HandleGroup.VISUALIZATION, translationType);
  }

  protected LinearDragHandle linearHandle;
  private Vector3 absoluteDragAxis;
  private Point3 initialClickPoint = Point3.ORIGIN;
  private Point3 previousClickPoint = Point3.ORIGIN;
  private double initialDistanceAlongAxis;
  private double currentDistanceAlongAxis;
  private Point3 originalOrigin;
  private Plane cameraFacingPlane;
  private Plane handleAlignedPlane;
  private AbstractCamera camera = null;
  private OnscreenRenderTarget onscreenRenderTarget;
}
