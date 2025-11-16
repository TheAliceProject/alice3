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

package edu.cmu.cs.dennisc.scenegraph;

import org.alice.interact.debug.DebugSphere;
import org.alice.math.immutable.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTransformable extends Composite {
  protected abstract Composite getVehicle();

  protected abstract void touchLocalTransformation(AffineMatrix4x4 m);

  public abstract AffineMatrix4x4 getLocalTransformation();

  protected void setLocalTransformation(AffineMatrix4x4 transformation, TransformationAffect affect) {
    if (transformation == null) {
      throw new NullPointerException();
    }
    if (transformation.isNaN()) {
      throw new RuntimeException("isNaN");
    }

    assert affect != null;
    AffineMatrix4x4 m = getLocalTransformation();
    this.touchLocalTransformation(affect.set(m, transformation));
  }

  public void notifyTransformationListeners() {
    fireAbsoluteTransformationChange();
  }

  public final void setLocalTransformation(AffineMatrix4x4 transformation) {
    setLocalTransformation(transformation, TransformationAffect.AFFECT_ALL);
  }

  // todo: cache this information
  @Override
  public AffineMatrix4x4 getAbsoluteTransformation() {
    Composite vehicle = getVehicle();
    if ((vehicle == null) || vehicle.isSceneOf(this)) {
      return getLocalTransformation();
    }
    return vehicle.getAbsoluteTransformation().times(this.getLocalTransformation());
  }

  // todo: cache this information
  @Override
  public AffineMatrix4x4 getInverseAbsoluteTransformation() {
    return getAbsoluteTransformation().invert();
  }

  @Override
  public AffineMatrix4x4 getTransformation(ReferenceFrame asSeenBy) {
    if (asSeenBy.isVehicleOf(this)) {
      return getLocalTransformation();
    } else if (asSeenBy.isSceneOf(this)) {
      return getAbsoluteTransformation();
    } else if (asSeenBy.isLocalOf(this)) {
      return AffineMatrix4x4.IDENTITY;
    }
    return asSeenBy.getInverseAbsoluteTransformation().normalizeOrientation().times(getAbsoluteTransformation());
  }

  public void setTransformation(AffineMatrix4x4 transformation, ReferenceFrame asSeenBy, TransformationAffect affect) {
    if (asSeenBy.isVehicleOf(this)) {
      setLocalTransformation(transformation, affect);
    } else if (asSeenBy.isLocalOf(this)) {
      applyTransformation(transformation, asSeenBy, affect);
      //    } else if( asSeenBy.isSceneOf( this ) ) {
      //      applyTransformation( transformation, asSeenBy, affectMask );
    } else {
      Composite vehicle = getVehicle();
      //todo: optimize
      AffineMatrix4x4 m = vehicle == null
              ? AffineMatrix4x4.IDENTITY
              : vehicle.getInverseAbsoluteTransformation();
      if (!asSeenBy.isSceneOf(this)) {
        final AffineMatrix4x4 seenBy = asSeenBy.getAbsoluteTransformation();
        m = m.times(seenBy.normalizeOnlyOrientation());
      }
      m = m.times(transformation);

      setLocalTransformation(m, affect);
      //return LinearAlgebra.multiply( transformation, LinearAlgebra.multiply( asSeenBy.getAbsoluteTransformation(), vehicleInverse ) );
      //      throw new RuntimeException( "todo.  this: " + this + "; vehicle: " + getVehicle() + "; asSeenBy: " + asSeenBy );
    }
  }

  public void setTransformation(AffineMatrix4x4 transformation, ReferenceFrame asSeenBy) {
    setTransformation(transformation, asSeenBy, TransformationAffect.AFFECT_ALL);
  }

  public void setTranslationOnly(double x, double y, double z, ReferenceFrame asSeenBy) {
    if (Double.isNaN(x)) {
      x = 0;
    }
    if (Double.isNaN(y)) {
      y = 0;
    }
    if (Double.isNaN(z)) {
      z = 0;
    }
    setTransformation(AffineMatrix4x4.createTranslation(x, y, z), asSeenBy, TransformationAffect.getTranslationAffect(x, y, z));
  }

  public void setTranslationOnly(Tuple3 t, ReferenceFrame asSeenBy) {
    setTranslationOnly(t.x(), t.y(), t.z(), asSeenBy);
    notifyTransformationListeners();
  }

  public void setAxesOnly(Orientation orientation, ReferenceFrame asSeenBy) {
    setTransformation(new AffineMatrix4x4(orientation.asMatrix3x3(), Point3.ORIGIN), asSeenBy, TransformationAffect.AFFECT_ORIENTAION_ONLY);
  }

  public void setAxesOnlyToPointAt(Component target) {
    AffineMatrix4x4 mSelf = getAbsoluteTransformation();
    AffineMatrix4x4 mTarget = target.getAbsoluteTransformation();

    Vector3 forward = mTarget.translation().minus(mSelf.translation());

    if (forward.magnitudeSquared() != 0) {
      setAxesOnly(new ForwardAndUpGuide(forward, null).asMatrix3x3(), AsSeenBy.SCENE);
    }
  }

  public void setAxesOnlyToStandUp(ReferenceFrame asSeenBy) {
    OrthogonalMatrix3x3 axes = getAxes(asSeenBy);
    setAxesOnly(axes.asStandUp(), asSeenBy);
  }

  public void setAxesOnlyToStandUp() {
    setAxesOnlyToStandUp(AsSeenBy.SCENE);
    notifyTransformationListeners();
  }

  private void applyTransformation(AffineMatrix4x4 transformation, ReferenceFrame asSeenBy, TransformationAffect affect) {
    //todo: handle affect
    if (asSeenBy.isLocalOf(this)) {
      setLocalTransformation(getLocalTransformation().times(transformation));
    } else if (asSeenBy.isVehicleOf(this)) {
      setLocalTransformation(transformation.times(getLocalTransformation()));
      //todo?
      //    } else if( asSeenBy.isSceneOf( this ) ) {
    } else {
      setTransformation(transformation.times(getTransformation(asSeenBy)), asSeenBy);
    }
  }

  public void applyTransformation(AffineMatrix4x4 transformation, ReferenceFrame asSeenBy) {
    applyTransformation(transformation, asSeenBy, TransformationAffect.AFFECT_ALL);
  }

  public void applyTranslation(double x, double y, double z, ReferenceFrame asSeenBy) {
    applyTransformation(AffineMatrix4x4.createTranslation(x, y, z), asSeenBy);
  }

  public void applyTranslation(Tuple3 t, ReferenceFrame asSeenBy) {
    applyTranslation(t.x(), t.y(), t.z(), asSeenBy);
  }

  public void applyTranslation(double x, double y, double z) {
    applyTranslation(x, y, z, AsSeenBy.SELF);
  }

  public void applyTranslation(Tuple3 t) {
    applyTranslation(t.x(), t.y(), t.z());
  }

  @Deprecated
  public void applyRotationAboutXAxisInRadians(double angleInRadians, ReferenceFrame asSeenBy) {
    applyTransformation(AffineMatrix4x4.createOrientation(AxisRotation.createXAxisRotation(new AngleInRadians(angleInRadians))), asSeenBy);
  }

  @Deprecated
  public void applyRotationAboutXAxisInRadians(double angleInRadians) {
    applyRotationAboutXAxisInRadians(angleInRadians, AsSeenBy.SELF);
  }

  @Deprecated
  public void applyRotationAboutYAxisInRadians(double angleInRadians, ReferenceFrame asSeenBy) {
    applyTransformation(AffineMatrix4x4.createOrientation(AxisRotation.createYAxisRotation(new AngleInRadians(angleInRadians))), asSeenBy);
  }

  @Deprecated
  public void applyRotationAboutYAxisInRadians(double angleInRadians) {
    applyRotationAboutYAxisInRadians(angleInRadians, AsSeenBy.SELF);
  }

  @Deprecated
  public void applyRotationAboutZAxisInRadians(double angleInRadians, ReferenceFrame asSeenBy) {
    applyTransformation(AffineMatrix4x4.createOrientation(AxisRotation.createZAxisRotation(new AngleInRadians(angleInRadians))), asSeenBy);
  }

  @Deprecated
  public void applyRotationAboutArbitraryAxisInRadians(Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy) {
      applyTransformation(AffineMatrix4x4.createOrientation(new AxisRotation(axis, new AngleInRadians(angleInRadians))), asSeenBy);
  }

  @Deprecated
  public void applyRotationAboutArbitraryAxisInRadians(Vector3 axis, double angleInRadians) {
    applyRotationAboutArbitraryAxisInRadians(axis, angleInRadians, AsSeenBy.SELF);
  }

  public void applyRotationAboutXAxis(Angle angle, ReferenceFrame asSeenBy) {
    applyRotationAboutXAxisInRadians(angle.getAsRadians(), asSeenBy);
  }

  public void applyRotationAboutXAxis(Angle angle) {
    applyRotationAboutXAxis(angle, AsSeenBy.SELF);
  }

  public void applyRotationAboutYAxis(Angle angle, ReferenceFrame asSeenBy) {
    applyRotationAboutYAxisInRadians(angle.getAsRadians(), asSeenBy);
  }

  public void applyRotationAboutYAxis(Angle angle) {
    applyRotationAboutYAxis(angle, AsSeenBy.SELF);
  }

  public void applyRotationAboutZAxis(Angle angle, ReferenceFrame asSeenBy) {
    applyRotationAboutZAxisInRadians(angle.getAsRadians(), asSeenBy);
  }

  public void applyRotationAboutZAxis(Angle angle) {
    applyRotationAboutZAxis(angle, AsSeenBy.SELF);
  }

  public void applyRotationAboutArbitraryAxis(Vector3 axis, Angle angle, ReferenceFrame asSeenBy) {
    applyRotationAboutArbitraryAxisInRadians(axis, angle.getAsRadians(), asSeenBy);
  }

  public void applyRotationAboutArbitraryAxis(Vector3 axis, Angle angle) {
    applyRotationAboutArbitraryAxis(axis, angle, AsSeenBy.SELF);
  }

  // For debugging placement in a live scene. The sphere remains in the scene.
  // The sphere defaults to red, but can be given a different color.
  public void addBreadcrumbToScene() {
    DebugSphere debugSphere = new DebugSphere();
    getRoot().addComponent(debugSphere);
    debugSphere.setLocalTranslation(getAbsoluteTransformation().translation());
  }
}
