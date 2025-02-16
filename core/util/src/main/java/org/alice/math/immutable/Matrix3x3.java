package org.alice.math.immutable;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;

import java.io.Serializable;


public record Matrix3x3(Vector3 right, Vector3 up, Vector3 backward) implements Serializable, Orientation {
  public static Matrix3x3 IDENTITY = new Matrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
  public static Matrix3x3 ZERO = new Matrix3x3(Vector3.ZERO, Vector3.ZERO, Vector3.ZERO);
  public static Matrix3x3 NaN = new Matrix3x3(Vector3.NaN, Vector3.NaN, Vector3.NaN);

  public static Matrix3x3 create(double e11, double e12, double e13, double e21, double e22, double e23, double e31, double e32, double e33) {
    // Comes in as row major values, but stored as columns
    return new Matrix3x3(
        new Vector3(e11, e21, e31),
        new Vector3(e12, e22, e32),
        new Vector3(e13, e23, e33));
  }

  public Matrix3x3 applyRotationAboutArbitraryAxis(Vector3 axis, Angle theta) {
    double angleInRadians = theta.getAsRadians();
    double c = Math.cos(angleInRadians);
    double s = Math.sin(angleInRadians);
    double t = 1 - c;

    double w = axis.magnitude();

    assert w > 0;

    double x = axis.x() / w;
    double y = axis.y() / w;
    double z = axis.z() / w;

    return this.times(new Matrix3x3(
        new Vector3((t * x * x) + c, (t * x * y) + (s * z), (t * x * z) - (s * y)),
        new Vector3((t * x * y) - (s * z), (t * y * y) + c, (t * y * z) + (s * x)),
        new Vector3((t * x * z) + (s * y), (t * y * z) - (s * x), (t * z * z) + c)));
  }

  public boolean isNaN() {
    return right.isNaN() || up.isNaN() || backward.isNaN();
  }

  public boolean isZero() {
    return right.isZero() && up.isZero() && backward.isZero();
  }

  public boolean isIdentity() {
    return this.equals(IDENTITY);
  }

  @Override
  public Matrix3x3 asMatrix3x3() {
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
    return new EulerAngles(new AngleInRadians(Math.asin(-e31)),
        new AngleInRadians(Math.atan2(up.z(), backward.z())),
        new AngleInRadians(Math.atan2(right.y(), right.x())),
        EulerAngles.Order.YAW_PITCH_ROLL);
  }

  @Override
  public ForwardAndUpGuide asForwardAndUpGuide() {
    return new ForwardAndUpGuide(this.backward().negate(), this.up());
  }

  public boolean isWithinEpsilonOf(Matrix3x3 other, double epsilon) {
    return right.isWithinEpsilonOf(other.right, epsilon) && up.isWithinEpsilonOf(other.up, epsilon) && backward.isWithinEpsilonOf(other.backward, epsilon);
  }

  public boolean isWithinReasonableEpsilonOf(Matrix3x3 other) {
    return isWithinEpsilonOf(other, EpsilonUtilities.REASONABLE_EPSILON);
  }

  public double determinant() {
    return ((right.x() * up.y() * backward.z())
        + (right.y() * up.z() * backward.x())
        + (right.z() * up.x() * backward.y()))
        - (right.x() * up.z() * backward.y())
        - (right.y() * up.x() * backward.z())
        - (right.z() * up.y() * backward.x());
  }

  public Matrix3x3 invert() {
    double d = determinant();
    return create(((up.y() * backward.z()) - (backward.y() * up.z())) / d,
        ((backward.x() * up.z()) - (up.x() * backward.z())) / d,
        ((up.x() * backward.y()) - (backward.x() * up.y())) / d,
        ((backward.y() * right.z()) - (right.y() * backward.z())) / d,
        ((right.x() * backward.z()) - (backward.x() * right.z())) / d,
        ((backward.x() * right.y()) - (right.x() * backward.y())) / d,
        ((right.y() * up.z()) - (up.y() * right.z())) / d,
        ((up.x() * right.z()) - (right.x() * up.z())) / d,
        ((right.x() * up.y()) - (up.x() * right.y())) / d);
  }

  public Matrix3x3 plus(Matrix3x3 b) {
    return new Matrix3x3(right.plus(b.right), up.plus(b.up), backward.plus(b.backward));
  }

  public Matrix3x3 times(double factor) {
    return new Matrix3x3(right.times(factor), up.times(factor), backward.times(factor));
  }

  public Matrix3x3 times(Matrix3x3 b) {
    double e11 = (right.x() * b.right.x()) + (up.x() * b.right.y()) + (backward.x() * b.right.z());
    double e12 = (right.x() * b.up.x()) + (up.x() * b.up.y()) + (backward.x() * b.up.z());
    double e13 = (right.x() * b.backward.x()) + (up.x() * b.backward.y()) + (backward.x() * b.backward.z());

    double e21 = (right.y() * b.right.x()) + (up.y() * b.right.y()) + (backward.y() * b.right.z());
    double e22 = (right.y() * b.up.x()) + (up.y() * b.up.y()) + (backward.y() * b.up.z());
    double e23 = (right.y() * b.backward.x()) + (up.y() * b.backward.y()) + (backward.y() * b.backward.z());

    double e31 = (right.z() * b.right.x()) + (up.z() * b.right.y()) + (backward.z() * b.right.z());
    double e32 = (right.z() * b.up.x()) + (up.z() * b.up.y()) + (backward.z() * b.up.z());
    double e33 = (right.z() * b.backward.x()) + (up.z() * b.backward.y()) + (backward.z() * b.backward.z());

    return create(e11, e12, e13, e21, e22, e23, e31, e32, e33);
  }

  public Vector3 transform(Vector3 v) {
    double x = (right.x() * v.x()) + (up.x() * v.y()) + (backward.x() * v.z());
    double y = (right.y() * v.x()) + (up.y() * v.y()) + (backward.y() * v.z());
    double z = (right.z() * v.x()) + (up.z() * v.y()) + (backward.z() * v.z());
    return new Vector3(x, y, z);
  }

  public Point3 transform(Point3 p) {
    double x = (right.x() * p.x()) + (up.x() * p.y()) + (backward.x() * p.z());
    double y = (right.y() * p.x()) + (up.y() * p.y()) + (backward.y() * p.z());
    double z = (right.z() * p.x()) + (up.z() * p.y()) + (backward.z() * p.z());
    return new Point3(x, y, z);
  }

  void transformVector(double[] dest, int offsetDest, double[] src, int offsetSrc) {
    dest[offsetDest] = (right.x() * src[offsetSrc]) + (up.x() * src[offsetSrc + 1]) + (backward.x() * src[offsetSrc + 2]);
    dest[offsetDest + 1] = (right.y() * src[offsetSrc]) + (up.y() * src[offsetSrc + 1]) + (backward.y() * src[offsetSrc + 2]);
    dest[offsetDest + 2] = (right.z() * src[offsetSrc]) + (up.z() * src[offsetSrc + 1]) + (backward.z() * src[offsetSrc + 2]);
  }

  public void transformVector(double[] dest, int offsetDest, float[] src, int offsetSrc) {
    dest[offsetDest] = (right.x() * src[offsetSrc]) + (up.x() * src[offsetSrc + 1]) + (backward.x() * src[offsetSrc + 2]);
    dest[offsetDest + 1] = (right.y() * src[offsetSrc]) + (up.y() * src[offsetSrc + 1]) + (backward.y() * src[offsetSrc + 2]);
    dest[offsetDest + 2] = (right.z() * src[offsetSrc]) + (up.z() * src[offsetSrc + 1]) + (backward.z() * src[offsetSrc + 2]);
  }

  public void transformVector(float[] dest, int offsetDest, float[] src, int offsetSrc) {
    dest[offsetDest] = (float) ((right.x() * src[offsetSrc]) + (up.x() * src[offsetSrc + 1]) + (backward.x() * src[offsetSrc + 2]));
    dest[offsetDest + 1] = (float) ((right.y() * src[offsetSrc]) + (up.y() * src[offsetSrc + 1]) + (backward.y() * src[offsetSrc + 2]));
    dest[offsetDest + 2] = (float) ((right.z() * src[offsetSrc]) + (up.z() * src[offsetSrc + 1]) + (backward.z() * src[offsetSrc + 2]));
  }

  public double[] getAsColumnMajorArray16(double[] rv) {
    assert rv.length == 16;
    rv[0] = right.x();
    rv[1] = right.y();
    rv[2] = right.z();
    rv[3] = 0.0;
    rv[4] = up.x();
    rv[5] = up.y();
    rv[6] = up.z();
    rv[7] = 0.0;
    rv[8] = backward.x();
    rv[9] = backward.y();
    rv[10] = backward.z();
    rv[11] = 0.0;
    rv[12] = 0.0;
    rv[13] = 0.0;
    rv[14] = 0.0;
    rv[15] = 1.0;
    return rv;
  }

  public double[] getAsColumnMajorArray16() {
    return getAsColumnMajorArray16(new double[16]);
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

  // Temporary use during transition to immutable Records
  @Deprecated(forRemoval = true)
  public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 mutable() {
    return new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3(right.mutable(), up.mutable(), backward.mutable());
  }
}

