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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.alice.interact.DragAdapter;
import org.alice.interact.handle.RotationRingHandle;
import org.alice.interact.manipulator.scenegraph.SnapLine;
import org.alice.interact.manipulator.scenegraph.SnapSphere;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Angle;
import org.alice.math.immutable.AngleInRadians;
import org.alice.math.immutable.AxisAlignedBox;
import org.alice.math.immutable.ForwardAndUpGuide;
import org.alice.math.immutable.Matrix3x3;
import org.alice.math.immutable.OrthogonalMatrix3x3;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Vector3;

import java.util.List;

public class SnapUtilities {
  public static final double SNAP_LINE_VISUAL_HEIGHT = .01d;

  public static final double SNAP_TO_GROUND_DISTANCE = .05d;
  public static final double SNAP_TO_GRID_DISTANCE = .1d;
  public static final double DEFAULT_GRID_SPACING = .5d;
  public static final double MIN_SNAP_RETURN_VALUE = .1d;
  public static final double ANGLE_SNAP_DISTANCE_IN_RADIANS = (2 * Math.PI) / 30d;

  private static final SnapLine X_AXIS_LINE = new SnapLine(Vector3.POSITIVE_X_AXIS);
  private static final SnapLine Y_AXIS_LINE = new SnapLine(Vector3.POSITIVE_Y_AXIS);
  private static final SnapLine Z_AXIS_LINE = new SnapLine(Vector3.POSITIVE_Z_AXIS);
  private static final SnapLine ARBITRARY_AXIS_LINE = new SnapLine(Vector3.POSITIVE_X_AXIS);
  private static final SnapSphere ANGLE_SNAP_SPHERE = new SnapSphere();

  private static final double MIN_SNAP_DELTA = .000001d;

  public static void showXAxis(Point3 position, Composite parent) {
    X_AXIS_LINE.setParent(parent);
    X_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
  }

  public static void showXAxis(Point3 position, Vector3 newDirection, Composite parent) {
    X_AXIS_LINE.setLine(newDirection);
    X_AXIS_LINE.setParent(parent);
    X_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
  }

  public static void hideXAxis() {
    X_AXIS_LINE.setLine(Vector3.POSITIVE_X_AXIS); //Restore the axis line to the absolute direction
    X_AXIS_LINE.setParent(null);
  }

  public static void showYAxis(Point3 position, Composite parent) {
    Y_AXIS_LINE.setParent(parent);
    Y_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
  }

  public static void showYAxis(Point3 position, Vector3 newDirection, Composite parent) {
    Y_AXIS_LINE.setLine(newDirection);
    Y_AXIS_LINE.setParent(parent);
    Y_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
  }

  public static void hideYAxis() {
    Y_AXIS_LINE.setLine(Vector3.POSITIVE_Y_AXIS); //Restore the axis line to the absolute direction
    Y_AXIS_LINE.setParent(null);
  }

  public static void showZAxis(Point3 position, Composite parent) {
    Z_AXIS_LINE.setParent(parent);
    Z_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
  }

  public static void showZAxis(Point3 position, Vector3 newDirection, Composite parent) {
    Z_AXIS_LINE.setLine(newDirection);
    Z_AXIS_LINE.setParent(parent);
    Z_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
  }

  public static void hideZAxis() {
    Z_AXIS_LINE.setLine(Vector3.POSITIVE_Z_AXIS); //Restore the axis line to the absolute direction
    Z_AXIS_LINE.setParent(null);
  }

  public static void showArbitraryAxis(Point3 position, Vector3 direction, Composite parent) {
    ARBITRARY_AXIS_LINE.setLine(direction);
    ARBITRARY_AXIS_LINE.setParent(parent);
    ARBITRARY_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
  }

  public static void hideArbitraryAxis() {
    ARBITRARY_AXIS_LINE.setParent(null);
  }

  public static void hideMovementSnapVisualization() {
    hideZAxis();
    hideYAxis();
    hideXAxis();
    hideArbitraryAxis();
  }

  public static void hideRotationSnapVisualization() {
    hideSnapSphere();
  }

  public static void showSnapSphere(Point3 location, Point3 ringCenter, Composite parent) {
    ANGLE_SNAP_SPHERE.setParent(parent);
    ANGLE_SNAP_SPHERE.setTranslationOnly(location, AsSeenBy.SCENE);
    ANGLE_SNAP_SPHERE.setLineDirection(ringCenter, location);
  }

  public static void hideSnapSphere() {
    ANGLE_SNAP_SPHERE.setParent(null);
  }

  public static Visual getSGVisualForTransformable(AbstractTransformable t) {
    if (t != null) {
      for (int i = 0; i < t.getComponentCount(); i++) {
        Component c = t.getComponentAt(i);
        if (c instanceof Visual visual) {
          return visual;
        }
      }
      return null;
    } else {
      return null;
    }
  }

  public static Matrix3x3 getTransformableScale(AbstractTransformable t) {
    Visual objectVisual = getSGVisualForTransformable(t);
    return objectVisual != null ? objectVisual.scale.getValue() : Matrix3x3.IDENTITY;
  }

  public static AxisAlignedBox getBoundingBox(AbstractTransformable t) {
    AxisAlignedBox boundingBox = null;
    if (t != null) {
      Object bbox = t.getBonusDataFor(DragAdapter.BOUNDING_BOX_KEY);
      if (bbox instanceof AxisAlignedBox axisAlignedBox) {
        boundingBox = axisAlignedBox;
        if (boundingBox.isNaN()) {
          boundingBox = null;
        }
      }
    }
    if (boundingBox == null) {
      boundingBox = new AxisAlignedBox(new Point3(-1, 0, -1), new Point3(1, 1, 1));
    }
    if (boundingBox != null) {
      boundingBox.scale(getTransformableScale(t));
    }

    Point3 boxMin = t.transformToAbsolute(boundingBox.minimum());
    Point3 boxMax = t.transformToAbsolute(boundingBox.maximum());
    return new AxisAlignedBox(boxMin, boxMax);
  }

  public static Point3 snapObjectToGround(AbstractTransformable toSnap, Point3 newPosition) {

    Vector3 movementDelta = newPosition.minus(toSnap.getAbsoluteTransformation().translation());
    if (movementDelta.y() != 0) {
      //move the bounding box to where the newPosition would place it
      AxisAlignedBox bbox = getBoundingBox(toSnap).translate(movementDelta);
      double boxBottom = bbox.getYMinimum();
      if (Math.abs(boxBottom) <= SNAP_TO_GROUND_DISTANCE) {
        //translate the object point the amount the bottom of the bounding box is away from y
        return newPosition.withY(newPosition.y() - boxBottom);
      }
      //      if (Math.abs(rv.y) <= SNAP_TO_GROUND_DISTANCE)
      //      {
      //        rv.y = 0.0d;
      //      }
    }
    return newPosition;
  }

  public static boolean isEdgeOn(AbstractCamera camera, Vector3 upVector) {
    return isEdgeOn(camera, upVector, 0);
  }

  public static boolean isEdgeOn(AbstractCamera camera, Vector3 upVector, double epsilon) {
    double dotProd = camera.getAbsoluteTransformation().orientation().backward().dotProduct(upVector);
    return (Math.abs(dotProd)) <= epsilon;
  }

  public static void showHorizontalSnap(AbstractCamera camera, Point3 currentPosition, Point3 snapPosition, ReferenceFrame referenceFrame) {
    Vector3 snapVector = currentPosition.minus(snapPosition);
    Point3 linePosition = snapPosition;
    AffineMatrix4x4 snapTransform = getFrameTransform(referenceFrame);
    Vector3 xSnapLine = snapTransform.orientation().getRight();
    Vector3 zSnapLine = snapTransform.orientation().getBackward();
    Vector3 ySnapLine = snapTransform.orientation().getUp();

    getFrameInverseTransform(referenceFrame).transform(snapVector);

    //If we're looking edge on, use a vertical line to draw the snap
    if (isEdgeOn(camera, snapTransform.orientation().getUp())) {
      if ((Math.abs(snapVector.x()) > MIN_SNAP_DELTA) || (Math.abs(snapVector.z()) > MIN_SNAP_DELTA)) {
        SnapUtilities.showYAxis(linePosition, ySnapLine, camera.getRoot());
      } else {
        SnapUtilities.hideYAxis();
      }
    } else {
      linePosition = linePosition.withY(snapPosition.y());
      //If the position is too close to the ground to show up well, bump it up a little bit
      if (Math.abs(linePosition.y()) < SNAP_LINE_VISUAL_HEIGHT) {
        linePosition = linePosition.withY(SNAP_LINE_VISUAL_HEIGHT);
      }
      if (Math.abs(snapVector.x()) > MIN_SNAP_DELTA) {
        SnapUtilities.showZAxis(linePosition, zSnapLine, camera.getRoot());
      } else {
        SnapUtilities.hideZAxis();
      }
      if (Math.abs(snapVector.z()) > MIN_SNAP_DELTA) {
        SnapUtilities.showXAxis(linePosition, xSnapLine, camera.getRoot());
      } else {
        SnapUtilities.hideXAxis();
      }
    }
  }

  public static void showVerticalSnap(AbstractCamera camera, Point3 currentPosition, Point3 snapPosition, ReferenceFrame referenceFrame) {
    Vector3 snapVector = currentPosition.minus(snapPosition);
    AffineMatrix4x4 snapTransform = getFrameTransform(referenceFrame);
    getFrameInverseTransform(referenceFrame).transform(snapVector);

    //    if (isEdgeOn(camera, snapTransform.orientation().getUp(), .9)) {
    if (Math.abs(snapVector.y()) > MIN_SNAP_DELTA) {
      AffineMatrix4x4 cameraTransform = camera.getAbsoluteTransformation();
      double dotRight = snapTransform.orientation().getRight().dotProduct(cameraTransform.orientation().getBackward());
      double dotBackward = snapTransform.orientation().getBackward().dotProduct(cameraTransform.orientation().getBackward());
      Vector3 lineToUse = (Math.abs(dotRight) < Math.abs(dotBackward)) ? snapTransform.orientation().getRight() : snapTransform.orientation().getBackward();
      SnapUtilities.showArbitraryAxis(snapPosition, lineToUse, camera.getRoot());
    } else {
      SnapUtilities.hideArbitraryAxis();
    }
    //}
  }

  public static void showSnapLines(AbstractCamera camera, Point3 currentPosition, Point3 snapPosition, ReferenceFrame referenceFrame) {
    showHorizontalSnap(camera, currentPosition, snapPosition, referenceFrame);
    showVerticalSnap(camera, currentPosition, snapPosition, referenceFrame);
  }

  public static Point3 snapObjectToAbsoluteGrid(Transformable toSnap, Point3 newPosition) {
    return snapObjectToGrid(toSnap, newPosition, DEFAULT_GRID_SPACING, toSnap.getRoot());
  }

  public static Point3 snapObjectToGrid(AbstractTransformable toSnap, Point3 originalPositionIn, double gridSpacing, ReferenceFrame referenceFrame) {
    AffineMatrix4x4 toReferenceFrame = getFrameInverseTransform(referenceFrame);
    AffineMatrix4x4 backToScene = getFrameTransform(referenceFrame);
    Point3 originalPosition = toReferenceFrame.transform(originalPositionIn);
    Point3 returnSnapPosition = originalPosition;
    Point3 currentPosition = toSnap.getAbsoluteTransformation().translation();
    currentPosition = toReferenceFrame.transform(currentPosition);
    Vector3 movementDelta = originalPosition.minus(currentPosition);
    if (movementDelta.x() != 0) {
      double currentPos = originalPosition.x();

      int lowerMultiplier = (int) (currentPos / gridSpacing);
      int upperMultiplier = (currentPos < 0) ? lowerMultiplier - 1 : lowerMultiplier + 1;
      double lowerSnap = gridSpacing * lowerMultiplier;
      double upperSnap = gridSpacing * upperMultiplier;
      if (Math.abs(lowerSnap - currentPos) <= SNAP_TO_GRID_DISTANCE) {
        currentPos = gridSpacing * lowerMultiplier;
      } else if (Math.abs(upperSnap - currentPos) <= SNAP_TO_GRID_DISTANCE) {
        currentPos = gridSpacing * upperMultiplier;
      }

      returnSnapPosition = returnSnapPosition.withX(currentPos);
    }
    //Don't snap in Y
    //    if( movementDelta.y() != 0 )
    //    {
    //      double currentPos = originalPosition.y();
    //
    //      int lowerMultiplier = (int)( currentPos / gridSpacing );
    //      int upperMultiplier = ( currentPos < 0 ) ? lowerMultiplier - 1 : lowerMultiplier + 1;
    //      double lowerSnap = gridSpacing * lowerMultiplier;
    //      double upperSnap = gridSpacing * upperMultiplier;
    //      if( Math.abs( lowerSnap - currentPos ) <= SNAP_TO_GRID_DISTANCE )
    //      {
    //        currentPos = gridSpacing * lowerMultiplier;
    //      }
    //      else if( Math.abs( upperSnap - currentPos ) <= SNAP_TO_GRID_DISTANCE )
    //      {
    //        currentPos = gridSpacing * upperMultiplier;
    //      }
    //
    //      returnSnapPosition.y() = currentPos;
    //    }
    if (movementDelta.z() != 0) {
      double currentPos = originalPosition.z();

      int lowerMultiplier = (int) (currentPos / gridSpacing);
      int upperMultiplier = (currentPos < 0) ? lowerMultiplier - 1 : lowerMultiplier + 1;
      double lowerSnap = gridSpacing * lowerMultiplier;
      double upperSnap = gridSpacing * upperMultiplier;
      if (Math.abs(lowerSnap - currentPos) <= SNAP_TO_GRID_DISTANCE) {
        currentPos = gridSpacing * lowerMultiplier;
      } else if (Math.abs(upperSnap - currentPos) <= SNAP_TO_GRID_DISTANCE) {
        currentPos = gridSpacing * upperMultiplier;
      }

      returnSnapPosition = returnSnapPosition.withZ(currentPos);
    }
    return backToScene.transform(returnSnapPosition);
  }

  private static AffineMatrix4x4 getFrameTransform(ReferenceFrame referenceFrame) {
    return referenceFrame == null ? AffineMatrix4x4.IDENTITY : referenceFrame.getAbsoluteTransformation();
  }

  private static AffineMatrix4x4 getFrameInverseTransform(ReferenceFrame referenceFrame) {
    return referenceFrame == null ? AffineMatrix4x4.IDENTITY : referenceFrame.getInverseAbsoluteTransformation();
  }

  public static Point3 doMovementSnapping(AbstractTransformable t, Point3 currentPosition, DragAdapter dragAdapter, ReferenceFrame referenceFrame, AbstractCamera camera) {
    Point3 snapPosition = currentPosition;
    if (dragAdapter != null) {
      //Try snapping to various snaps
      if (dragAdapter.shouldSnapToGround()) {
        snapPosition = SnapUtilities.snapObjectToGround(t, currentPosition);
      }
      if (dragAdapter.shouldSnapToGrid()) {
        snapPosition = SnapUtilities.snapObjectToGrid(t, snapPosition, dragAdapter.getGridSpacing(), referenceFrame);
      }
      //Visualize any snapping that happened
      if (camera != null) {
        SnapUtilities.showSnapLines(camera, currentPosition, snapPosition, referenceFrame);
      }
    }
    //Apply the new snap position
    return snapPosition;
  }

  private static List<Vector3> getSnapVectors(Vector3 guideForwardAxis, Vector3 guideUpAxis, Angle snapAmount) {
    List<Vector3> snapVectors = Lists.newLinkedList();
    OrthogonalMatrix3x3 rotationMatrix = (new ForwardAndUpGuide(guideForwardAxis, guideUpAxis)).asMatrix3x3();
    double currentAngle = 0;
    while (currentAngle < 360d) {
      snapVectors.add(rotationMatrix.getBackward().negate());
      rotationMatrix.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_Y_AXIS, snapAmount);
      currentAngle += snapAmount.getAsDegrees();
    }
    return snapVectors;
  }

  private static Vector3 snapAxis(Vector3 inputAxis, Vector3 guideForwardAxis, Vector3 guideUpAxis, Angle snapDegrees) {
    List<Vector3> snapVectors = getSnapVectors(guideForwardAxis, guideUpAxis, snapDegrees);
    for (Vector3 snapVector : snapVectors) {
      Angle angleBetween = inputAxis.angleWith(snapVector);
      if (Math.abs(angleBetween.getAsRadians()) <= ANGLE_SNAP_DISTANCE_IN_RADIANS) {
        return snapVector;
      }
    }
    return inputAxis;
  }

  //  public static Vector3 snapObjectRotation(Vector3 preRotateForward, Vector3 currentForward, Vector3 rotationAxis, Angle snapAngle, ReferenceFrame referenceFrame)
  //  {
  //    AffineMatrix4x4 toReferenceFrame = referenceFrame.getInverseAbsoluteTransformation();
  //    AffineMatrix4x4 referenceFrameTransform = referenceFrame.getAbsoluteTransformation();
  //    OrthogonalMatrix3x3 currentOrientationInReferenceFrame = new OrthogonalMatrix3x3(currentOrientation);
  //    toReferenceFrame.applyOrientation(currentOrientationInReferenceFrame);
  //    boolean didSnap = false;
  //    OrthogonalMatrix3x3 originalOrientationInReferenceFrame = preRotateTransform.orientation;
  //    toReferenceFrame.applyOrientation(originalOrientationInReferenceFrame);
  //
  //    Vector3 snapRightAxis = currentOrientation.right;
  //    Vector3 snapUpAxis = currentOrientation.up;
  //    Vector3 snapBackwardAxis = currentOrientation.backward;
  //    if (!originalOrientationInReferenceFrame.right.isWithinEpsilonOf(currentOrientationInReferenceFrame.right, MIN_SNAP_DELTA))
  //    {
  //      snapRightAxis = snapAxis(currentOrientation.right, referenceFrameTransform.orientation().getRight(), referenceFrameTransform.orientation().getUp(), snapAngle);
  //    }
  //    if (!originalOrientationInReferenceFrame.up.isWithinEpsilonOf(currentOrientationInReferenceFrame.up, MIN_SNAP_DELTA))
  //    {
  //      snapUpAxis = snapAxis(currentOrientation.up, referenceFrameTransform.orientation().getUp(), referenceFrameTransform.orientation().getBackward(), snapAngle);
  //    }
  //    if (!originalOrientationInReferenceFrame.backward.isWithinEpsilonOf(currentOrientationInReferenceFrame.backward, MIN_SNAP_DELTA))
  //    {
  //      snapBackwardAxis = snapAxis(currentOrientation.backward, referenceFrameTransform.orientation().getBackward(), referenceFrameTransform.orientation().getUp(), snapAngle);
  //    }
  //    return new OrthogonalMatrix3x3(snapRightAxis, snapUpAxis, snapBackwardAxis);
  //  }

  //  public static OrthogonalMatrix3x3 doRotationSnapping(AffineMatrix4x4 preRotateTransform, OrthogonalMatrix3x3 currentOrientation, DragAdapter dragAdapter, ReferenceFrame referenceFrame, AbstractCamera camera)
  //  {
  //    OrthogonalMatrix3x3 snapOrientation = new OrthogonalMatrix3x3(currentOrientation);
  //
  //    //Try snapping to various snaps
  //    if (dragAdapter.getSnapState().shouldSnapToRotation())
  //    {
  //      snapOrientation = SnapUtilities.snapObjectRotation(preRotateTransform, currentOrientation, dragAdapter.getSnapState().getRotationSnapAngle(), referenceFrame);
  //    }
  //    //Visualize any snapping that happened
  ////    if (camera != null)
  ////    {
  ////      SnapUtilities.showSnaprotation(camera, currentOrientation, snapOrientation, referenceFrame);
  ////    }
  //    //Apply the new snap position
  //    return snapOrientation;
  //  }

  public static Angle snapObjectToAngle(Angle currentAngle, Angle snapAngleAmount) {
    double currentAngleInRadians = currentAngle.getAsRadians();
    double snapAmountInRadians = snapAngleAmount.getAsRadians();

    int lowerMultiplier = (int) (currentAngleInRadians / snapAmountInRadians);
    int upperMultiplier = (currentAngleInRadians < 0) ? lowerMultiplier - 1 : lowerMultiplier + 1;
    double lowerSnap = snapAmountInRadians * lowerMultiplier;
    double upperSnap = snapAmountInRadians * upperMultiplier;
    if (Math.abs(lowerSnap - currentAngleInRadians) <= ANGLE_SNAP_DISTANCE_IN_RADIANS) {
      return new AngleInRadians(lowerSnap);
    }
    if (Math.abs(upperSnap - currentAngleInRadians) <= ANGLE_SNAP_DISTANCE_IN_RADIANS) {
      return new AngleInRadians(upperSnap);
    }
    return currentAngle;
  }

  public static void showSnapRotation(RotationRingHandle rotationHandle) {
    AffineMatrix4x4 handleTransform = rotationHandle.getAbsoluteTransformation();
    Vector3 snapDirection = handleTransform.orientation().getBackward().times(rotationHandle.getRadius() * -1);
    Point3 snapSphereLocation = handleTransform.translation().plus(snapDirection);
    showSnapSphere(snapSphereLocation, handleTransform.translation(), rotationHandle.getRoot());
  }

  public static Angle doRotationSnapping(Angle currentAngle, DragAdapter dragAdapter) {
    if (dragAdapter == null || !dragAdapter.shouldSnapToRotation()) {
      return currentAngle;
    }
    //Try snapping to various snaps
    return snapObjectToAngle(currentAngle, dragAdapter.getRotationSnapAngle());
  }

}
