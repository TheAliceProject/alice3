package org.alice.math.immutable;


import edu.cmu.cs.dennisc.math.EpsilonUtilities;

public record UnitQuaternion(double x, double y, double z, double w) implements Orientation {
  public static final UnitQuaternion IDENTITY = new UnitQuaternion(0, 0, 0, 1);
  public static final UnitQuaternion NaN = new UnitQuaternion(Double.NaN, Double.NaN, Double.NaN, Double.NaN);

  //<editor-fold desc="Condition Checks">
  @Override
  public boolean isNaN() {
    return Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z) || Double.isNaN(w);
  }

  @Override
  public boolean isIdentity() {
    return this == IDENTITY;
  }

  public boolean isUnit() {
    return Math.abs(1 - (x * x + y * y + z * z + w * w)) < EpsilonUtilities.REASONABLE_EPSILON;
  }
  //</editor-fold>

  //<editor-fold desc="Comparisons">
  @Override
  public boolean isAlignedWith(Orientation other) {
    return isAlignedWith(other.asUnitQuaternion(), EpsilonUtilities.REASONABLE_EPSILON);
  }

  private boolean isAlignedWith(UnitQuaternion q, double epsilon) {
    return isWithinEpsilon(q, epsilon) || this.negated().isWithinEpsilon(q, epsilon);
  }

  private boolean isWithinEpsilon(UnitQuaternion q, double epsilon) {
    return (Math.abs(x - q.x) < epsilon)
        && (Math.abs(y - q.y) < epsilon)
        && (Math.abs(z - q.z) < epsilon)
        && (Math.abs(w - q.w) < epsilon);
  }

  //</editor-fold>

  //<editor-fold desc="Operations">
  public UnitQuaternion interpolate(UnitQuaternion b, double portion) {
    final double EPSILON = 0.0001;
    assert !this.isNaN();
    assert !b.isNaN();
    if (portion == 0.0) {
      return this;
    }
    if (portion == 1.0 || this.isAlignedWith(b, EPSILON)) {
      return b;
    }
    double dotProduct = dotProduct(b);
    UnitQuaternion bPrime = (dotProduct < 0.0) ? b.negated() : b;
    dotProduct = Math.abs(dotProduct);

    double aPortion = 1 - portion;
    double bPortion = portion;
    final double THRESHOLD_TO_PERFORM_SIMPLE_LINEAR_INTERPOLATION = 0.05;
    if (!((1 - dotProduct) < THRESHOLD_TO_PERFORM_SIMPLE_LINEAR_INTERPOLATION)) {
      double halfAngle = Math.acos(dotProduct);
      double sineHalfAngle = Math.sqrt(1.0 - (dotProduct * dotProduct));
      aPortion = Math.sin(aPortion * halfAngle) / sineHalfAngle;
      bPortion = Math.sin(bPortion * halfAngle) / sineHalfAngle;
    }
    return new UnitQuaternion(
        (x * aPortion) + (bPrime.x * bPortion),
        (y * aPortion) + (bPrime.y * bPortion),
        (z * aPortion) + (bPrime.z * bPortion),
        (w * aPortion) + (bPrime.w * bPortion));
  }

  public UnitQuaternion negated() {
    return new UnitQuaternion(-x, -y, -z, -w);
  }

  private double dotProduct(UnitQuaternion b) {
    return (x * b.x) + (y * b.y) + (z * b.z) + (w * b.w);
  }

  public UnitQuaternion plus(UnitQuaternion b) {
    return new UnitQuaternion(x + b.x, y + b.y, z + b.z, w + b.w);
  }

  public UnitQuaternion times(UnitQuaternion b) {
    return new UnitQuaternion(x * b.x, y * b.y, z * b.z, w * b.w);
  }

  public UnitQuaternion times(double factor) {
    return new UnitQuaternion(x * factor, y * factor, z * factor, w * factor);
  }

  public UnitQuaternion normalized() {
    double magnitudeSquared = magnitudeSquared();
    if (magnitudeSquared == 1.0 || magnitudeSquared == 0) {
      return this;
    }
    return this.times(1.0 / Math.sqrt(magnitudeSquared));
  }

  private double magnitudeSquared() {
    return (x * x) + (y * y) + (z * z) + (w * w);
  }
  //</editor-fold>

  //<editor-fold desc="Orientation Conversions">
  @Override
  public OrthogonalMatrix3x3 asMatrix3x3() {
    double wx = w * x;
    double wy = w * y;
    double wz = w * z;

    double xx = x * x;
    double xy = x * y;
    double xz = x * z;

    double yy = y * y;
    double yz = y * z;

    double zz = z * z;

    Vector3 right = new Vector3(1 - (2 * (yy + zz)), 2 * (xy + wz), 2 * (xz - wy));
    Vector3 up = new Vector3(2 * (xy - wz), 1 - (2 * (xx + zz)), 2 * (yz + wx));
    Vector3 backward = new Vector3(2 * (xz + wy), 2 * (yz - wx), 1 - (2 * (xx + yy)));
    return new OrthogonalMatrix3x3(right, up, backward);
  }

  @Override
  public UnitQuaternion asUnitQuaternion() {
    return this;
  }

  @Override
  public AxisRotation asAxisRotation() {
    double s = Math.sqrt(1 - (w * w));
    Vector3 v = s < EpsilonUtilities.MAXIMUM_FOR_WITHIN_REASONABLE_EPSILON_OF_0_IN_SQUARED_SPACE ? Vector3.POSITIVE_X_AXIS : new Vector3(x / s, y / s, z / s);

    return new AxisRotation(v, new AngleInRadians(2 * Math.acos(w)));
  }

  @Override
  public EulerAngles asEulerAngles() {
    return asMatrix3x3().asEulerAngles();
  }

  @Override
  public ForwardAndUpGuide asForwardAndUpGuide() {
    return asMatrix3x3().asForwardAndUpGuide();
  }
  //</editor-fold>
}
