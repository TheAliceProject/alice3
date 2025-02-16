package org.alice.math.immutable;

public record AxisRotation(Vector3 axis, Angle angle) implements Orientation {
  static AxisRotation NaN = new AxisRotation(Vector3.NaN, Angle.NaN);
  // Any axis will do when angle is 0.
  static AxisRotation IDENTITY = new AxisRotation(Vector3.POSITIVE_Z_AXIS, Angle.ZERO);

  public static AxisRotation createXAxisRotation(Angle angle) {
    return new AxisRotation(Vector3.POSITIVE_X_AXIS, angle);
  }

  public static AxisRotation createYAxisRotation(Angle angle) {
    return new AxisRotation(Vector3.POSITIVE_Y_AXIS, angle);
  }

  public static AxisRotation createZAxisRotation(Angle angle) {
    return new AxisRotation(Vector3.POSITIVE_Y_AXIS, angle);
  }

  @Override
  public boolean isNaN() {
    return this.axis.isNaN() || this.angle.isNaN();
  }

  @Override
  public boolean isIdentity() {
    return angle.isZero();
  }

  @Override
  public Matrix3x3 asMatrix3x3() {
    if (isNaN()) {
      return Matrix3x3.NaN;
    } else {
      // TODO optimize for special axes
      double thetaInRadians = angle.getAsRadians();
      double c = Math.cos(thetaInRadians);
      double s = Math.sin(thetaInRadians);
      double t = 1 - c;

      double xyt = axis.x() * axis.y() * t;
      double zs = axis.z() * s;

      double xzt = axis.x() * axis.z() * t;
      double ys = axis.y() * s;

      double yzt = axis.y() * axis.z() * t;
      double xs = axis.x() * s;

      Vector3 right = new Vector3(c + (axis.x() * axis.x() * t), xyt + zs, xzt - ys);
      Vector3 up = new Vector3(xyt - zs, c + (axis.y() * axis.y() * t), yzt + xs);
      Vector3 backward = new Vector3(xzt + ys, yzt - xs, c + (axis.z() * axis.z() * t));
      return new Matrix3x3(right, up, backward);
    }
  }

  @Override
  public UnitQuaternion asUnitQuaternion() {
    double halfThetaInRadians = angle.getAsRadians() * 0.5;
    double c = Math.cos(halfThetaInRadians);
    double s = Math.sin(halfThetaInRadians);
    return new UnitQuaternion(s * axis.x(), s * axis.y(), s * axis.z(), c);
  }

  @Override
  public AxisRotation asAxisRotation() {
    return this;
  }

  @Override
  public EulerAngles asEulerAngles() {
    return asMatrix3x3().asEulerAngles();
  }

  @Override
  public ForwardAndUpGuide asForwardAndUpGuide() {
    return asMatrix3x3().asForwardAndUpGuide();
  }
}
