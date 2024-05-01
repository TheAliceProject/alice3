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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.animation.Animated;
import edu.cmu.cs.dennisc.animation.DurationBasedAnimation;
import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.Orientation;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.math.animation.AffineMatrix4x4Animation;
import edu.cmu.cs.dennisc.math.animation.Point3Animation;
import edu.cmu.cs.dennisc.math.animation.UnitQuaternionAnimation;
import edu.cmu.cs.dennisc.math.polynomial.HermiteCubic;
import edu.cmu.cs.dennisc.pattern.DefaultPool;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTransformableImp extends EntityImp implements Animated {
  @Override
  public abstract AbstractTransformable getSgComposite();

  public boolean isFacing(EntityImp other) {
    AffineMatrix4x4 m = other.getTransformation(this);
    return m.translation.z < 0.0;
  }

  @Override
  public void applyAnimation() {
    getSgComposite().notifyTransformationListeners();
  }

  public AffineMatrix4x4 getLocalTransformation() {
    return this.getSgComposite().getLocalTransformation();
  }

  public Point3 getLocalPosition() {
    return this.getLocalTransformation().translation;
  }

  public OrthogonalMatrix3x3 getLocalOrientation() {
    return this.getLocalTransformation().orientation;
  }

  public void setLocalTransformation(AffineMatrix4x4 transformation) {
    this.getSgComposite().setLocalTransformation(transformation);
  }

  void setLocalOrientation(OrthogonalMatrix3x3 orientation) {
    AffineMatrix4x4 m = this.getLocalTransformation();
    m.orientation.setValue(orientation);
    this.setLocalTransformation(m);
  }

  @Override
  protected void postCheckSetVehicle(EntityImp vehicle) {
    SceneImp scene = this.getScene();
    AffineMatrix4x4 absTransform = scene != null ? this.getTransformation(scene) : null;
    super.postCheckSetVehicle(vehicle);
    if ((vehicle != null) && (scene != null)) {
      this.setTransformation(scene, absTransform);
      applyAnimation();
    }
  }

  public void applyTranslation(double x, double y, double z, ReferenceFrame asSeenBy) {
    this.getSgComposite().applyTranslation(x, y, z, asSeenBy.getSgReferenceFrame());
  }

  public void applyTranslation(Point3 translation, ReferenceFrame asSeenBy) {
    this.applyTranslation(translation.x, translation.y, translation.z, asSeenBy);
  }

  public void animateApplyTranslation(Point3 translation, ReferenceFrame asSeenBy, double duration, Style style) {
    assert !translation.isNaN();
    assert duration >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
    assert style != null;
    assert asSeenBy != null;
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      this.applyTranslation(translation, asSeenBy);
      applyAnimation();
    } else {
      class TranslateAnimation extends DurationBasedAnimation {
        private ReferenceFrame asSeenBy;
        private double x;
        private double y;
        private double z;
        private double xSum;
        private double ySum;
        private double zSum;

        private TranslateAnimation(Number duration, Style style, Point3 translation, ReferenceFrame asSeenBy) {
          super(duration, style);
          this.x = translation.x;
          this.y = translation.y;
          this.z = translation.z;
          this.asSeenBy = asSeenBy;
        }

        @Override
        protected void prologue() {
          this.xSum = 0;
          this.ySum = 0;
          this.zSum = 0;
        }

        @Override
        protected void setPortion(double portion) {
          double xPortion = (this.x * portion) - this.xSum;
          double yPortion = (this.y * portion) - this.ySum;
          double zPortion = (this.z * portion) - this.zSum;

          AbstractTransformableImp.this.applyTranslation(xPortion, yPortion, zPortion, this.asSeenBy);

          this.xSum += xPortion;
          this.ySum += yPortion;
          this.zSum += zPortion;
        }

        @Override
        protected void epilogue() {
          AbstractTransformableImp.this.applyTranslation(this.x - this.xSum, this.y - this.ySum, this.z - this.zSum, this.asSeenBy);
        }

        @Override
        public Animated getAnimated() {
          return AbstractTransformableImp.this;
        }
      }
      this.perform(new TranslateAnimation(duration, style, translation, asSeenBy));
    }
  }

  public void animateApplyTranslation(double x, double y, double z, ReferenceFrame asSeenBy, double duration, Style style) {
    this.animateApplyTranslation(new Point3(x, y, z), asSeenBy, duration, style);
  }

  public void applyRotationInRadians(Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy) {
    this.getSgComposite().applyRotationAboutArbitraryAxisInRadians(axis, angleInRadians, asSeenBy.getSgReferenceFrame());
  }

  public void applyRotationInRadians(Vector3 axis, double angleInRadians) {
    this.applyRotationInRadians(axis, angleInRadians, this);
  }

  private void applyRotationInRevolutions(Vector3 axis, double angleInRevolutions, ReferenceFrame asSeenBy) {
    this.applyRotationInRadians(axis, AngleUtilities.revolutionsToRadians(angleInRevolutions), asSeenBy);
  }

  public void applyRotationInRevolutions(Vector3 axis, double angleInRadians) {
    this.applyRotationInRevolutions(axis, angleInRadians, this);
  }

  private void animateApplyRotationInRadians(Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy, double duration, Style style) {
    assert axis != null;
    assert duration >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      this.applyRotationInRadians(axis, angleInRadians, asSeenBy);
      applyAnimation();
    } else {
      class RotateAnimation extends DurationBasedAnimation {
        private ReferenceFrame asSeenBy;
        private Vector3 axis;
        private double angleInRadians;
        private double angleSumInRadians;

        private RotateAnimation(Number duration, Style style, Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy) {
          super(duration, style);
          this.axis = axis;
          this.angleInRadians = angleInRadians;
          this.asSeenBy = asSeenBy;
        }

        @Override
        protected void prologue() {
          this.angleSumInRadians = 0;
        }

        @Override
        protected void setPortion(double portion) {
          double anglePortionInRadians = (this.angleInRadians * portion) - this.angleSumInRadians;

          AbstractTransformableImp.this.applyRotationInRadians(this.axis, anglePortionInRadians, this.asSeenBy);

          this.angleSumInRadians += anglePortionInRadians;
        }

        @Override
        protected void epilogue() {
          AbstractTransformableImp.this.applyRotationInRadians(this.axis, this.angleInRadians - this.angleSumInRadians, this.asSeenBy);
        }

        @Override
        public Animated getAnimated() {
          return AbstractTransformableImp.this;
        }
      }
      this.perform(new RotateAnimation(duration, style, axis, angleInRadians, asSeenBy));
    }
  }

  public void animateApplyRotationInRevolutions(Vector3 axis, double angleInRevolutions, ReferenceFrame asSeenBy, double duration, Style style) {
    this.animateApplyRotationInRadians(axis, AngleUtilities.revolutionsToRadians(angleInRevolutions), asSeenBy, duration, style);
  }

  protected abstract static class VantagePointData {
    private final AbstractTransformableImp subject;

    VantagePointData(AbstractTransformableImp subject) {
      this.subject = subject;
    }

    public AbstractTransformableImp getSubject() {
      return this.subject;
    }

    protected abstract void setM(AffineMatrix4x4 m);

    protected abstract AffineMatrix4x4 getM0();

    protected abstract AffineMatrix4x4 getM1();

    protected abstract Point3 getT0();

    protected abstract Point3 getT1();

    protected abstract UnitQuaternion getQ0();

    protected abstract UnitQuaternion getQ1();

    public void setPortion(double portion) {
      Point3 t0 = this.getT0();
      Point3 t1 = this.getT1();
      Point3 t = Point3.createInterpolation(t0, t1, portion);
      UnitQuaternion q0 = this.getQ0();
      UnitQuaternion q1 = this.getQ1();
      UnitQuaternion q = UnitQuaternion.createInterpolation(q0, q1, portion);
      this.setM(new AffineMatrix4x4(q, t));
    }

    public void epilogue() {
      this.setM(this.getM1());
    }
  }

  protected static class PreSetVantagePointData extends VantagePointData {
    private final EntityImp other;
    private final AffineMatrix4x4 m0;
    private final AffineMatrix4x4 m1;
    private final UnitQuaternion q0;
    private final UnitQuaternion q1;

    PreSetVantagePointData(AbstractTransformableImp subject, EntityImp other) {
      super(subject);
      this.other = other;
      this.m0 = subject.getTransformation(other);
      this.m1 = AffineMatrix4x4.createIdentity();
      this.q0 = this.m0.orientation.createUnitQuaternion();
      this.q1 = this.m1.orientation.createUnitQuaternion();
    }

    @Override
    protected AffineMatrix4x4 getM0() {
      return this.m0;
    }

    @Override
    protected AffineMatrix4x4 getM1() {
      return this.m1;
    }

    @Override
    protected UnitQuaternion getQ0() {
      return this.q0;
    }

    @Override
    protected UnitQuaternion getQ1() {
      return this.q1;
    }

    @Override
    protected Point3 getT0() {
      return this.m0.translation;
    }

    @Override
    protected Point3 getT1() {
      return this.m1.translation;
    }

    @Override
    protected void setM(AffineMatrix4x4 m) {
      this.getSubject().getSgComposite().setTransformation(m, other.getSgReferenceFrame());
    }
  }

  void animateVantagePoint(final VantagePointData data, double duration, Style style) {
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      data.epilogue();
    } else {
      perform(new DurationBasedAnimation(duration, style) {
        @Override
        public Animated getAnimated() {
          return data.subject;
        }

        @Override
        protected void prologue() {
        }

        @Override
        protected void setPortion(double portion) {
          data.setPortion(portion);
        }

        @Override
        protected void epilogue() {
          data.epilogue();
        }
      });
    }
  }

  private abstract static class OrientationData {
    private final AbstractTransformableImp subject;

    OrientationData(AbstractTransformableImp subject) {
      this.subject = subject;
    }

    public AbstractTransformableImp getSubject() {
      return this.subject;
    }

    protected abstract void setM(OrthogonalMatrix3x3 m);

    protected final void setQ(UnitQuaternion q) {
      this.setM(q.createOrthogonalMatrix3x3());
    }

    protected abstract OrthogonalMatrix3x3 getM0();

    protected abstract OrthogonalMatrix3x3 getM1();

    protected abstract UnitQuaternion getQ0();

    protected abstract UnitQuaternion getQ1();

    public void setPortion(double portion) {
      UnitQuaternion q0 = this.getQ0();
      UnitQuaternion q1 = this.getQ1();
      assert !q0.isNaN() : this;
      assert !q1.isNaN() : this;
      this.setQ(UnitQuaternion.createInterpolation(q0, q1, portion));
    }

    public void epilogue() {
      this.setM(this.getM1());
    }
  }

  private abstract static class PreSetOrientationData extends OrientationData {
    private final OrthogonalMatrix3x3 m0;
    private final OrthogonalMatrix3x3 m1;
    private UnitQuaternion q0;
    private UnitQuaternion q1;

    PreSetOrientationData(AbstractTransformableImp subject, OrthogonalMatrix3x3 m0, OrthogonalMatrix3x3 m1) {
      super(subject);
      this.m0 = m0;
      this.m1 = m1;
    }

    @Override
    protected final OrthogonalMatrix3x3 getM0() {
      return this.m0;
    }

    @Override
    protected final OrthogonalMatrix3x3 getM1() {
      return this.m1;
    }

    @Override
    protected UnitQuaternion getQ0() {
      if (this.q0 == null) {
        this.q0 = this.m0.createUnitQuaternion();
      }
      return this.q0;
    }

    @Override
    protected UnitQuaternion getQ1() {
      if (this.q1 == null) {
        this.q1 = this.m1.createUnitQuaternion();
      }
      return this.q1;
    }
  }

  private static class LocalOrientationData extends PreSetOrientationData {
    LocalOrientationData(AbstractTransformableImp subject, OrthogonalMatrix3x3 m1) {
      super(subject, subject.getSgComposite().getLocalTransformation().orientation, m1);
    }

    @Override
    protected void setM(OrthogonalMatrix3x3 orientation) {
      AffineMatrix4x4 prevM = this.getSubject().getSgComposite().getLocalTransformation();
      AffineMatrix4x4 nextM = new AffineMatrix4x4(orientation, prevM.translation);
      this.getSubject().getSgComposite().setLocalTransformation(nextM);
    }
  }

  private static DefaultPool<StandInImp> s_standInPool = new DefaultPool<>(StandInImp.class);

  private static StandInImp acquireStandIn(EntityImp composite) {
    StandInImp rv = s_standInPool.acquire();
    rv.setVehicle(composite);
    rv.setLocalTransformation(AffineMatrix4x4.accessIdentity());
    return rv;
  }

  private static void releaseStandIn(StandInImp standIn) {
    s_standInPool.release(standIn);
  }

  private static OrthogonalMatrix3x3 calculateTurnToFaceAxes(AbstractTransformableImp subject, EntityImp target) {
    StandInImp standInA = acquireStandIn(subject);
    try {
      standInA.setPositionOnly(subject);
      Point3 targetPos = target.getTransformation(subject).translation;
      if (EpsilonUtilities.isWithinReasonableEpsilon(targetPos.x, 0.0) && EpsilonUtilities.isWithinReasonableEpsilon(targetPos.z, 0.0)) {
        //todo
        return subject.getLocalOrientation();
      } else {
        targetPos.y = 0;
        targetPos.normalize();
        StandInImp standInB = acquireStandIn(subject);
        try {
          //Move a standin to the position of the target
          standInB.applyTranslation(targetPos.x, targetPos.y, targetPos.z, standInB);
          Point3 standinPosition = standInB.getTransformation(subject.getVehicle()).translation;
          //Calculate the vector pointing from the subject to the target all in the reference frame of the subject's vehicle
          Point3 forwardPos = (Point3) Point3.setReturnValueToSubtraction(new Point3(), standinPosition, subject.getLocalPosition());
          //Take that vector and normalize it to create the new "forward" vector for the subject
          Vector3 newForward = new Vector3(forwardPos);
          newForward.normalize();
          //Make a new orientation using this new "forward" and the existing "up" from the subject
          ForwardAndUpGuide fAndG = new ForwardAndUpGuide(newForward, subject.getLocalOrientation().up);
          return new OrthogonalMatrix3x3(fAndG);
        } finally {
          releaseStandIn(standInB);
        }
      }
    } finally {
      releaseStandIn(standInA);
    }
  }

  private static class TurnToFaceOrientationData extends LocalOrientationData {
    TurnToFaceOrientationData(AbstractTransformableImp subject, EntityImp target) {
      super(subject, calculateTurnToFaceAxes(subject, target));
    }
  }

  private static class OrientToUprightData extends PreSetOrientationData {
    private final ReferenceFrame upAsSeenBy;

    public static OrientToUprightData createInstance(AbstractTransformableImp subject, ReferenceFrame upAsSeenBy) {
      OrthogonalMatrix3x3 orientation0 = subject.getTransformation(upAsSeenBy).orientation;
      OrthogonalMatrix3x3 orientation1 = OrthogonalMatrix3x3.createFromStandUp(orientation0);
      return new OrientToUprightData(subject, orientation0, orientation1, upAsSeenBy);
    }

    private OrientToUprightData(AbstractTransformableImp subject, OrthogonalMatrix3x3 orientation0, OrthogonalMatrix3x3 orientation1, ReferenceFrame upAsSeenBy) {
      super(subject, orientation0, orientation1);
      this.upAsSeenBy = upAsSeenBy;
    }

    @Override
    protected void setM(OrthogonalMatrix3x3 m) {
      this.getSubject().getSgComposite().setAxesOnly(m, this.upAsSeenBy.getSgReferenceFrame());
    }
  }

  private static class OrientToPointAtData extends PreSetOrientationData {
    private final ReferenceFrame upAsSeenBy;

    public static OrientToPointAtData createInstance(AbstractTransformableImp subject, EntityImp target, ReferenceFrame upAsSeenBy) {
      AffineMatrix4x4 m0 = subject.getTransformation(upAsSeenBy);
      Point3 t0 = m0.translation;
      Point3 t1 = target.getTransformation(upAsSeenBy).translation;
      Vector3 forward = Vector3.createSubtraction(t1, t0);
      OrthogonalMatrix3x3 o1;
      if (forward.isZero()) {
        o1 = m0.orientation;
        //no op
      } else {
        ForwardAndUpGuide forwardAndUpGuide = new ForwardAndUpGuide(forward, null);
        o1 = forwardAndUpGuide.createOrthogonalMatrix3x3();
      }
      return new OrientToPointAtData(subject, m0.orientation, o1, upAsSeenBy);
    }

    private OrientToPointAtData(AbstractTransformableImp subject, OrthogonalMatrix3x3 orientation0, OrthogonalMatrix3x3 orientation1, ReferenceFrame upAsSeenBy) {
      super(subject, orientation0, orientation1);
      this.upAsSeenBy = upAsSeenBy;
    }

    @Override
    protected void setM(OrthogonalMatrix3x3 m) {
      this.getSubject().getSgComposite().setAxesOnly(m, this.upAsSeenBy.getSgReferenceFrame());
    }
  }

  private void setOrientationOnly(OrientationData data) {
    data.epilogue();
  }

  private void animateOrientationOnly(final OrientationData data, double duration, Style style) {
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      data.epilogue();
    } else {
      perform(new DurationBasedAnimation(duration, style) {
        @Override
        public Animated getAnimated() {
          return data.subject;
        }

        @Override
        protected void prologue() {
        }

        @Override
        protected void setPortion(double portion) {
          data.setPortion(portion);
        }

        @Override
        protected void epilogue() {
          data.epilogue();
        }
      });
    }
  }

  void setLocalOrientationOnly(OrthogonalMatrix3x3 localOrientation) {
    this.setOrientationOnly(new LocalOrientationData(this, localOrientation));
  }

  public void animateLocalOrientationOnly(final OrthogonalMatrix3x3 localOrientation, double duration, Style style) {
    this.animateOrientationOnly(new LocalOrientationData(this, localOrientation), duration, style);
  }

  //  private edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 createOrientation( EntityImp target, edu.cmu.cs.dennisc.math.Orientation offset ) {
  //    edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getTransformation( target );
  //    if( offset != null ) {
  //      //todo
  //    }
  //    return m.orientation;
  //  }
  private void setOrientationOnly(EntityImp target, Orientation offset) {
    this.getSgComposite().setAxesOnly(offset != null ? offset : OrthogonalMatrix3x3.accessIdentity(), target.getSgComposite());
  }

  public void animateOrientationOnly(final EntityImp target, Orientation offset, double duration, Style style) {
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      this.setOrientationOnly(target, offset);
      applyAnimation();
    } else {
      final OrthogonalMatrix3x3 buffer = new OrthogonalMatrix3x3();
      final OrthogonalMatrix3x3 targetOrientation = this.getTransformation(target).orientation;
      targetOrientation.normalizeColumns();
      UnitQuaternion q0 = new UnitQuaternion(targetOrientation);
      UnitQuaternion q1;
      if (offset != null) {
        q1 = offset.createUnitQuaternion();
      } else {
        q1 = UnitQuaternion.accessIdentity();
      }
      perform(new UnitQuaternionAnimation(duration, style, q0, q1) {
        @Override
        protected void updateValue(UnitQuaternion q) {
          buffer.setValue(q);
          AbstractTransformableImp.this.setOrientationOnly(target, buffer);
        }
        @Override
        public Animated getAnimated() {
          return AbstractTransformableImp.this;
        }
      });
    }
  }

  public void animateOrientationOnlyToFace(EntityImp target, Point3 offset, double duration, Style style) {
    this.animateOrientationOnly(new TurnToFaceOrientationData(this, target), duration, style);
  }

  public void animateOrientationToUpright(ReferenceFrame upAsSeenBy, double duration, Style style) {
    this.animateOrientationOnly(OrientToUprightData.createInstance(this, upAsSeenBy), duration, style);
  }

  public void animateOrientationToPointAt(EntityImp target, ReferenceFrame upAsSeenBy, double duration, Style style) {
    this.animateOrientationOnly(OrientToPointAtData.createInstance(this, target, upAsSeenBy), duration, style);
  }

  public void setOrientationOnlyToPointAt(ReferenceFrame target) {
    this.getSgComposite().setAxesOnlyToPointAt(target.getActualEntityImplementation(this).getSgComposite());
  }

  private static final boolean DEFAULT_IS_SMOOTH = true;
  private static final double DEFAULT_PLACE_ALONG_AXIS_OFFSET = 0.0;

  private abstract static class SmoothAffineMatrix4x4Animation extends DurationBasedAnimation {
    final AffineMatrix4x4 m1;
    final HermiteCubic xHermite;
    final HermiteCubic yHermite;
    final HermiteCubic zHermite;

    SmoothAffineMatrix4x4Animation(AffineMatrix4x4 m0, AffineMatrix4x4 m1, double duration, Style style) {
      super(duration, style);
      this.m1 = m1;

      double s = -8; //this.m0.translation.calculateMagnitude();
      this.xHermite = new HermiteCubic(m0.translation.x, m1.translation.x, s * m0.orientation.backward.x, s * m1.orientation.backward.x);
      this.yHermite = new HermiteCubic(m0.translation.y, m1.translation.y, s * m0.orientation.backward.y, s * m1.orientation.backward.y);
      this.zHermite = new HermiteCubic(m0.translation.z, m1.translation.z, s * m0.orientation.backward.z, s * m1.orientation.backward.z);
    }

    @Override
    protected void prologue() {
    }
  }

  private static class SmoothPositionAnimation extends SmoothAffineMatrix4x4Animation {
    private final AbstractTransformableImp subject;
    private final ReferenceFrame asSeenBy;

    SmoothPositionAnimation(AbstractTransformableImp subject, AffineMatrix4x4 m1, ReferenceFrame asSeenBy, double duration, Style style) {
      super(subject.getTransformation(asSeenBy), m1, duration, style);
      this.subject = subject;
      this.asSeenBy = asSeenBy;
    }
    @Override
    public Animated getAnimated() {
      return subject;
    }

    @Override
    protected void setPortion(double portion) {
      double x = this.xHermite.evaluate(portion);
      double y = this.yHermite.evaluate(portion);
      double z = this.zHermite.evaluate(portion);

      this.subject.getSgComposite().setTranslationOnly(x, y, z, this.asSeenBy.getSgReferenceFrame());
    }

    @Override
    protected void epilogue() {
      this.subject.getSgComposite().setTranslationOnly(this.m1.translation, this.asSeenBy.getSgReferenceFrame());
    }
  }

  private static class PlaceData {
    private final AbstractTransformableImp subject;
    private final SpatialRelationImp spatialRelation;
    private final EntityImp target;
    private final double alongAxisOffset;
    private final ReferenceFrame asSeenBy;

    PlaceData(AbstractTransformableImp subject, SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy) {
      assert subject != null;
      //assert target != null;
      assert asSeenBy != null;
      assert spatialRelation != null;
      assert !Double.isNaN(alongAxisOffset);
      this.subject = subject;
      this.spatialRelation = spatialRelation;
      this.target = target;
      this.alongAxisOffset = alongAxisOffset;
      this.asSeenBy = asSeenBy;
    }

    AffineMatrix4x4 calculateTranslation0() {
      return this.subject.getTransformation(this.asSeenBy);
    }

    AffineMatrix4x4 calculateTranslation1(AffineMatrix4x4 t0) {
      AxisAlignedBox bbSubject = this.subject.getAxisAlignedMinimumBoundingBox();
      if (this.target != null) {
        AxisAlignedBox bbTarget;
        bbTarget = this.target.getAxisAlignedMinimumBoundingBox(this.asSeenBy);
        assert bbSubject != null;
        assert bbTarget != null;
        if (bbSubject.isNaN() || bbTarget.isNaN()) {
          Logger.severe("bounding box is NaN", bbSubject, bbTarget);
          return t0;
        } else {
          AffineMatrix4x4 m = this.target.getTransformation(this.asSeenBy);
          m.translation.set(this.spatialRelation.getPlaceLocation(this.alongAxisOffset, bbSubject, bbTarget));
          return m;
        }
      } else {
        AffineMatrix4x4 m = AffineMatrix4x4.createIdentity();
        double y;
        if (bbSubject.isNaN()) {
          y = 0;
        } else {
          y = -bbSubject.getMinimum().y;
        }
        m.translation.set(t0.translation.x, y, t0.translation.z);
        return m;
      }
    }

    public void setTranslation(AffineMatrix4x4 translation) {
      this.subject.getSgComposite().setTransformation(translation, this.asSeenBy.getSgReferenceFrame());
    }
  }

  private static class PlaceAnimation extends DurationBasedAnimation {
    private final PlaceData placeData;
    private Point3 p0;
    private UnitQuaternion q0;
    private Point3 p1;
    private UnitQuaternion q1;

    PlaceAnimation(PlaceData placeData, double duration, Style style) {
      super(duration, style);
      this.placeData = placeData;
    }

    @Override
    protected void prologue() {
      AffineMatrix4x4 m0 = this.placeData.calculateTranslation0();
      AffineMatrix4x4 m1 = this.placeData.calculateTranslation1(m0);
      this.p0 = m0.translation;
      this.q0 = m0.orientation.createUnitQuaternion();
      this.p1 = m1.translation;
      this.q1 = m1.orientation.createUnitQuaternion();
    }

    @Override
    protected void setPortion(double portion) {
      Point3 p = Point3.createInterpolation(this.p0, this.p1, portion);
      UnitQuaternion q = UnitQuaternion.createInterpolation(this.q0, this.q1, portion);
      this.placeData.setTranslation(new AffineMatrix4x4(q, p));
    }

    @Override
    public Animated getAnimated() {
      return placeData.subject;
    }

    @Override
    protected void epilogue() {
      this.placeData.setTranslation(new AffineMatrix4x4(this.q1, this.p1));
    }
  }

  private void setPositionOnly(EntityImp target, Point3 offset) {
    this.getSgComposite().setTranslationOnly(offset != null ? offset : Point3.ORIGIN,
                                             target != null ? target.getSgComposite() : AsSeenBy.SCENE);
  }

  void setPositionOnly(EntityImp target) {
    this.setPositionOnly(target, Point3.ORIGIN);
  }

  public void animatePositionOnly(final EntityImp target, Point3 offset, boolean isSmooth, double duration, Style style) {
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      this.setPositionOnly(target, offset);
      applyAnimation();
    } else {
      if (isSmooth) {
        this.perform(new SmoothPositionAnimation(this, AffineMatrix4x4.createIdentity(), target, duration, style));
      } else {
        AffineMatrix4x4 m0 = this.getTransformation(target);
        perform(new Point3Animation(duration, style, m0.translation, offset != null ? offset : Point3.ORIGIN) {
          @Override
          public Animated getAnimated() {
            return AbstractTransformableImp.this;
          }

          @Override
          protected void updateValue(Point3 t) {
            AbstractTransformableImp.this.setPositionOnly(target, t);
          }
        });
      }
    }
  }

  public void place(SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy) {
    PlaceData placeData = new PlaceData(this, spatialRelation, target, alongAxisOffset, asSeenBy);
    placeData.setTranslation(placeData.calculateTranslation1(placeData.calculateTranslation0()));
  }

  public void place(SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset) {
    this.place(spatialRelation, target, alongAxisOffset, target);
  }

  public void place(SpatialRelationImp spatialRelation, EntityImp target) {
    this.place(spatialRelation, target, DEFAULT_PLACE_ALONG_AXIS_OFFSET);
  }

  public void animatePlace(SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy, boolean isSmooth, double duration, Style style) {
    PlaceData placeData = new PlaceData(this, spatialRelation, target, alongAxisOffset, asSeenBy);
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      AffineMatrix4x4 m0 = placeData.calculateTranslation0();
      assert !m0.isNaN() : this;
      AffineMatrix4x4 m1 = placeData.calculateTranslation1(m0);
      assert !m1.isNaN() : this;
      placeData.setTranslation(m1);
    } else {
      this.perform(new PlaceAnimation(placeData, duration, style));
    }
  }

  public void setTransformation(ReferenceFrame target, AffineMatrix4x4 offset) {
    assert target != null : this;
    assert this.getSgComposite() != null : this;
    if (offset == null) {
      offset = AffineMatrix4x4.accessIdentity();
    }
    this.getSgComposite().setTransformation(offset, target.getSgReferenceFrame());
  }

  public void setTransformation(ReferenceFrame target) {
    this.setTransformation(target, AffineMatrix4x4.accessIdentity());
  }

  public void animateTransformation(final ReferenceFrame target, AffineMatrix4x4 offset, boolean isSmooth, double duration, Style style) {
    duration = adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(duration, RIGHT_NOW)) {
      if ((offset == null) || !offset.isNaN()) {
        this.setTransformation(target, offset);
        applyAnimation();
      }
    } else {
      AffineMatrix4x4 m1;
      if (offset != null) {
        m1 = offset;
      } else {
        m1 = AffineMatrix4x4.accessIdentity();
      }
      AffineMatrix4x4 m0 = getTransformation(target);
      //      if( isSmooth ) {
      //        this.perform( new SmoothAffineMatrix4x4Animation( duration, style, m0, m1 ) );
      //      } else {
      perform(new AffineMatrix4x4Animation(duration, style, m0, m1) {
        @Override
        public Animated getAnimated() {
          return AbstractTransformableImp.this;
        }

        @Override
        protected void updateValue(AffineMatrix4x4 m) {
          setTransformation(target, m);
        }

        @Override
        protected void epilogue() {
          super.epilogue();
          getSgComposite().notifyTransformationListeners();
        }
      });
      //      }
    }
  }

  public void animateTransformation(ReferenceFrame target, AffineMatrix4x4 offset) {
    this.animateTransformation(target, offset, DEFAULT_IS_SMOOTH, DEFAULT_DURATION, DEFAULT_STYLE);
  }

  public double getDistanceTo(EntityImp other) {
    Point3 translation = this.getSgComposite().getTranslation(other.getSgComposite());
    return translation.calculateMagnitude();
  }

  private Point3 getMax(EntityImp entity, ReferenceFrame asSeenBy) {
    if (entity instanceof ModelImp) {
      AxisAlignedBox bbox = entity.getDynamicAxisAlignedMinimumBoundingBox(asSeenBy);
      return bbox.getMaximum();
    } else {
      return entity.getSgComposite().getTranslation(asSeenBy.getSgReferenceFrame());
    }
  }

  private Point3 getMin(EntityImp entity, ReferenceFrame asSeenBy) {
    if (entity instanceof ModelImp) {
      AxisAlignedBox bbox = entity.getDynamicAxisAlignedMinimumBoundingBox(asSeenBy);
      return bbox.getMinimum();
    } else {
      return entity.getSgComposite().getTranslation(asSeenBy.getSgReferenceFrame());
    }
  }

  public double getDistanceAbove(EntityImp other, ReferenceFrame asSeenBy) {
    return getDistanceAbove(other, this, asSeenBy);
  }

  public double getDistanceBelow(EntityImp other, ReferenceFrame asSeenBy) {
    return getDistanceAbove(this, other, asSeenBy);
  }

  private double getDistanceAbove(EntityImp a, EntityImp b, ReferenceFrame asSeenBy) {
    return differenceToEpsilon(getMin(b, asSeenBy).y, getMax(a, asSeenBy).y);
  }

  public double getDistanceToTheLeftOf(EntityImp other, ReferenceFrame asSeenBy) {
    return getDistanceToTheLeftOf(this, other, asSeenBy);
  }

  public double getDistanceToTheRightOf(EntityImp other, ReferenceFrame asSeenBy) {
    return getDistanceToTheLeftOf(other, this, asSeenBy);
  }

  private double getDistanceToTheLeftOf(EntityImp a, EntityImp b, ReferenceFrame asSeenBy) {
    return differenceToEpsilon(getMin(b, asSeenBy).x, getMax(a, asSeenBy).x);
  }

  public double getDistanceBehind(EntityImp other, ReferenceFrame asSeenBy) {
    return getDistanceBehind(this, other, asSeenBy);
  }

  public double getDistanceInFrontOf(EntityImp other, ReferenceFrame asSeenBy) {
    return getDistanceBehind(other, this, asSeenBy);
  }

  private double getDistanceBehind(EntityImp a, EntityImp b, ReferenceFrame asSeenBy) {
    //Front and back calculations are flipped because -Z is front
    return differenceToEpsilon(getMin(a, asSeenBy).z, getMax(b, asSeenBy).z);
  }

  private double differenceToEpsilon(double a, double b) {
    double value = a - b;
    if (Math.abs(value) < .01d) {
      return 0;
    }
    return value;
  }
}
