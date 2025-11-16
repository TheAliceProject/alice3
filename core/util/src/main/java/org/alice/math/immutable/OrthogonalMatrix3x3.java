package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;

/**
 * Orthogonal3x3Matrix is generally expected to be orthonormal, meaning its three vectors are mutually perpendicular
 * unit vectors, but it may not be strictly true. Examples include:
 * <ul>
 * <li>Scaling matrices use non-unit vectors to represent scale.</li>
 * <li>Repeated matrix operations accumulate rounding errors and can violate both the unit length and orthogonality.</li>
 * </ul>
 * In cases where the vectors are not unit length, calling normalized() will produce
 * a corrected version. There is no operation provided to attempt to fix it if the vectors are not orthogonal.
 *
 */
public record OrthogonalMatrix3x3(Vector3 right, Vector3 up, Vector3 backward)
    implements Matrix3x3, Orientation {
  static OrthogonalMatrix3x3 NaN = new OrthogonalMatrix3x3(Vector3.NaN, Vector3.NaN, Vector3.NaN);

  //<editor-fold desc="Accessors">
  @Override
  public Vector3 getRight() {
    return right;
  }

  @Override
  public Vector3 getUp() {
    return up;
  }

  @Override
  public Vector3 getBackward() {
    return backward;
  }
  //</editor-fold>

  //<editor-fold desc="Condition Checks">

  public boolean isNaN() {
    return Matrix3x3.super.isNaN();
  }

  public boolean isIdentity() {
    return Matrix3x3.super.isIdentity();
  }

  // This is primarily for debugging and not a formal notion
  public double deviationFromNormal() {
    return right.dotProduct(up) + backward.dotProduct(right) + up.dotProduct(backward);
  }
  //</editor-fold>

  //<editor-fold desc="Comparisons">
  @Override
  public boolean isAlignedWith(Orientation other) {
    OrthogonalMatrix3x3 o = other.asMatrix3x3();
    return this.right.isWithinReasonableEpsilonOf(o.right)
        && this.up.isWithinReasonableEpsilonOf(o.up)
        && this.backward.isWithinReasonableEpsilonOf(o.backward);
  }
  //</editor-fold>

  //<editor-fold desc="Matrix Operations">
  public OrthogonalMatrix3x3 applyRotationAboutArbitraryAxis(Vector3 axis, Angle theta) {
    double angleInRadians = theta.getAsRadians();
    double c = Math.cos(angleInRadians);
    double s = Math.sin(angleInRadians);
    double t = 1 - c;

    double w = axis.magnitude();

    assert w > 0;

    double x = axis.x() / w;
    double y = axis.y() / w;
    double z = axis.z() / w;

    // Multiplying two orthogonal matrices will produce an orthogonal matrix
    Matrix3x3 result = this.times(new OrthogonalMatrix3x3(
        new Vector3((t * x * x) + c, (t * x * y) + (s * z), (t * x * z) - (s * y)),
        new Vector3((t * x * y) - (s * z), (t * y * y) + c, (t * y * z) + (s * x)),
        new Vector3((t * x * z) + (s * y), (t * y * z) - (s * x), (t * z * z) + c)));
    if (result instanceof OrthogonalMatrix3x3 matrix3x3) {
      return matrix3x3;
    }
    throw new ArithmeticException("Problem creating an orthogonal matrix from " + result);
  }

  public OrthogonalMatrix3x3 plus(OrthogonalMatrix3x3 b) {
    return new OrthogonalMatrix3x3(right.plus(b.right), up.plus(b.up), backward.plus(b.backward));
  }

  public OrthogonalMatrix3x3 times(double factor) {
    return new OrthogonalMatrix3x3(right.times(factor), up.times(factor), backward.times(factor));
  }
  //</editor-fold>

  //<editor-fold desc="Orientation Conversions">
  @Override
  public OrthogonalMatrix3x3 asMatrix3x3() {
    return this;
  }

  @Override
  public UnitQuaternion asUnitQuaternion() {
    return asAxisRotation().asUnitQuaternion();
  }

  @Override
  public AxisRotation asAxisRotation() {
    if (this.isNaN()) {
      return AxisRotation.NaN;
    } else {
      return asAxisRotation(right().x(), up().x(), backward().x(), right().y(), up().y(), backward().y(), right().z(), up().z(), backward().z());
    }
  }

  @Override
  public EulerAngles asEulerAngles() {
    double e31 = right.z();
    e31 = Math.max(e31, -1);
    e31 = Math.min(e31, 1);
    return new EulerAngles(
        new AngleInRadians(Math.atan2(up.z(), backward.z())),
        new AngleInRadians(Math.asin(-e31)),
        new AngleInRadians(Math.atan2(right.y(), right.x())),
        EulerAngles.Order.YAW_PITCH_ROLL);
  }

  @Override
  public ForwardAndUpGuide asForwardAndUpGuide() {
    return new ForwardAndUpGuide(this.backward().negate(), this.up());
  }

  private static boolean isWithinReasonableEpsilonOfZero(double d) {
    return Math.abs(d) < EpsilonUtilities.REASONABLE_EPSILON;
  }

  private static AxisRotation asAxisRotation(double e11, double e12, double e13, double e21, double e22, double e23, double e31, double e32, double e33) {
    //assert this.isWithinReasonableEpsilonOfUnitLengthSquared();
    //todo: assert orthogonal
    if (isWithinReasonableEpsilonOfZero(e12 - e21)
        && isWithinReasonableEpsilonOfZero(e13 - e31)
        && isWithinReasonableEpsilonOfZero(e23 - e32)) {
      //singularity
      if (isWithinReasonableEpsilonOfZero(e12 + e21)
          && isWithinReasonableEpsilonOfZero(e13 + e31)
          && isWithinReasonableEpsilonOfZero(e23 + e32)
          && isWithinReasonableEpsilonOfZero((e11 + e22 + e33) - 3)) {
        // Identity Matrix
        return AxisRotation.IDENTITY;
      } else {
        double x = getCardinal(e11);
        double y = getCardinal(e22);
        double z = getCardinal(e33);

        boolean isXZero = isWithinReasonableEpsilonOfZero(x);
        boolean isYZero = isWithinReasonableEpsilonOfZero(y);
        boolean isZZero = isWithinReasonableEpsilonOfZero(z);

        boolean isXYNegativeOrZero = e12 <= 0;
        boolean isXZNegativeOrZero = e13 <= 0;
        boolean isYZNegativeOrZero = e23 <= 0;

        if (isXZero && !isYZero && !isZZero) {
          if (isYZNegativeOrZero) {
            y = -y;
          }
        } else if (isYZero && !isZZero) {
          if (isXZNegativeOrZero) {
            z = -z;
          }
        } else if (isZZero) {
          if (isXYNegativeOrZero) {
            x = -x;
          }
        }
        return new AxisRotation(Vector3.createNormalized(x, y, z), Angle.PI);
      }
    } else {
      double c = ((e11 + e22 + e33) - 1) * 0.5;
      // TODO replace max/min with `Math.clamp(c, -1.0, 1.0)` when we go to Java 21
      c = Math.max(c, -1.0);
      c = Math.min(c, 1.0);
      Angle theta = new AngleInRadians(Math.acos(c));
      Vector3 axis = Vector3.createNormalized(e32 - e23, e13 - e31, e21 - e12);
      return new AxisRotation(axis, theta);
    }
  }

  private static double getCardinal(double el) {
    double c = (el + 1) * 0.5;
    return c > 0 ? Math.sqrt(c) : 0.0;
  }

  public OrthogonalMatrix3x3 normalized() {
    if (isNormalized()) {
      // If we enforced this on construction this could happen every time, but the code can produce exceptions.
      return this;
    }
    return new OrthogonalMatrix3x3(right.normalized(), up.normalized(), backward.normalized());
  }

  public OrthogonalMatrix3x3 asStandUp() {
    if (EpsilonUtilities.isWithinReasonableEpsilon(getUp().y(), 1.0)) {
      return this;
    }
    if (EpsilonUtilities.isWithinReasonableEpsilon(getBackward().x(), 0)
        && EpsilonUtilities.isWithinReasonableEpsilon(getBackward().z(), 0)) {
      double theta = getBackward().y() < 0.0 ? -0.25 : +0.25;

      return (OrthogonalMatrix3x3) this.times((new AxisRotation(Vector3.POSITIVE_X_AXIS, new AngleInRevolutions(theta))).asMatrix3x3());
    }

    Vector3 zAxis = EpsilonUtilities.isWithinReasonableEpsilon(getBackward().y(), 0.0)
        ? getBackward()
        : Vector3.createNormalized(getBackward().x(), 0, getBackward().z());

    Vector3 xAxis = Vector3.POSITIVE_Y_AXIS.crossProduct(zAxis).normalized();
    return new OrthogonalMatrix3x3(xAxis, Vector3.POSITIVE_Y_AXIS, zAxis);
  }
  //</editor-fold>

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    binaryEncoder.encodeRecord(this);
  }
}

