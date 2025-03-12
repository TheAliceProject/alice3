package org.alice.math.immutable;

public record ForwardAndUpGuide(Vector3 forward, Vector3 upGuide) implements Orientation {
  public static ForwardAndUpGuide IDENTITY = new ForwardAndUpGuide(Vector3.NEGATIVE_Z_AXIS, Vector3.POSITIVE_Y_AXIS);

  //<editor-fold desc="Condition Checks">
  @Override
  public boolean isNaN() {
    return forward.isNaN() || upGuide.isNaN();
  }

  @Override
  public boolean isIdentity() {
    return this == IDENTITY;
  }
  //</editor-fold>

  //<editor-fold desc="Comparisons">
  @Override
  public boolean isAlignedWith(Orientation other) {
    ForwardAndUpGuide o = other.asForwardAndUpGuide();
    return this.forward.isWithinReasonableEpsilonOf(o.forward) && this.upGuide.isWithinReasonableEpsilonOf(o.upGuide);
  }
  //</editor-fold>

  //<editor-fold desc="Orientation Conversions">
  @Override
  public OrthogonalMatrix3x3 asMatrix3x3() {
    if (forward.isNaN() || forward.isZero()) {
      return OrthogonalMatrix3x3.NaN;
    }
    Vector3 up = getSafeUpGuide();
    Vector3 zAxis = forward.negate().normalized();
    Vector3 xAxis = up.crossProduct(zAxis).normalized();
    Vector3 yAxis = zAxis.crossProduct(xAxis).normalized();

    return new OrthogonalMatrix3x3(xAxis, yAxis, zAxis);
  }

  private Vector3 getSafeUpGuide() {
    if (upGuide != null && !upGuide.isNaN()) {
      return upGuide.normalized();
    }
    if (forward.x() == 0 && forward.z() == 0) {
      return Vector3.POSITIVE_X_AXIS;
    }
    return Vector3.POSITIVE_Y_AXIS;
  }

  @Override
  public UnitQuaternion asUnitQuaternion() {
    return asMatrix3x3().asUnitQuaternion();
  }

  @Override
  public AxisRotation asAxisRotation() {
    return asMatrix3x3().asAxisRotation();
  }

  @Override
  public EulerAngles asEulerAngles() {
    return asMatrix3x3().asEulerAngles();
  }

  @Override
  public ForwardAndUpGuide asForwardAndUpGuide() {
    return this;
  }
  //</editor-fold>
}
