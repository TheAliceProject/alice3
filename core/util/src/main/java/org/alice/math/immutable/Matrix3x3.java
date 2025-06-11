package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.print.Printable;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;

public interface Matrix3x3 extends Printable, Serializable, BinaryEncodableAndDecodable {
  OrthogonalMatrix3x3 IDENTITY = new OrthogonalMatrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
  FullMatrix3x3 ZERO = new FullMatrix3x3(Vector3.ZERO, Vector3.ZERO, Vector3.ZERO);

  //<editor-fold desc="Accessors">
  Vector3 getRight();
  Vector3 getUp();
  Vector3 getBackward();


  default Vector3 getForward() {
    return getBackward().negate();
  }
  //</editor-fold>

  //<editor-fold desc="Static Constructors">
  static Matrix3x3 create(double[] values) {
    return create(values[0], values[1], values[2],
                  values[3], values[4], values[5],
                  values[6], values[7], values[8]);
  }

  static Matrix3x3 create(double e11, double e12, double e13,
                          double e21, double e22, double e23,
                          double e31, double e32, double e33) {
    // Comes in as row major values, but stored as columns
    return create(
        new Vector3(e11, e21, e31),
        new Vector3(e12, e22, e32),
        new Vector3(e13, e23, e33));
  }

  static Matrix3x3 create(Vector3 right, Vector3 up, Vector3 backward) {
    // Create Orthogonal Matrix if these are mutually orthogonal. It is not required or forced to be orthonormal.
    if (right.isOrthogonalTo(up) && backward.isOrthogonalTo(right) && up.isOrthogonalTo(backward)) {
      return new OrthogonalMatrix3x3(right, up, backward);
    }
    return new FullMatrix3x3(right, up, backward);
  }
  //</editor-fold>

  //<editor-fold desc="Condition Checks">

  default boolean isNaN() {
    return getRight().isNaN() || getUp().isNaN() || getBackward().isNaN();
  }

  default boolean isZero() {
    return getRight().isZero() && getUp().isZero() && getBackward().isZero();
  }

  default boolean isIdentity() {
    return getRight().isWithinReasonableEpsilonOf(Vector3.POSITIVE_X_AXIS)
        && getUp().isWithinReasonableEpsilonOf(Vector3.POSITIVE_Y_AXIS)
        && getBackward().isWithinReasonableEpsilonOf(Vector3.POSITIVE_Z_AXIS);
  }

  default boolean isNormalized() {
    return getRight().isNormalized() && getUp().isNormalized() && getBackward().isNormalized();
  }

  default boolean isWithinReasonableEpsilonOf(Matrix3x3 other) {
    return this.isWithinEpsilonOf(other, EpsilonUtilities.REASONABLE_EPSILON);
  }

  default boolean isWithinEpsilonOf(Matrix3x3 other, double epsilon) {
    return getRight().isWithinEpsilonOf(other.getRight(), epsilon)
        && getUp().isWithinEpsilonOf(other.getUp(), epsilon)
        && getBackward().isWithinEpsilonOf(other.getBackward(), epsilon);
  }
  //</editor-fold>

  //<editor-fold desc="Matrix Operations">
  default double determinant() {
    return ((getRight().x() * getUp().y() * getBackward().z())
        + (getRight().y() * getUp().z() * getBackward().x())
        + (getRight().z() * getUp().x() * getBackward().y()))
        - (getRight().x() * getUp().z() * getBackward().y())
        - (getRight().y() * getUp().x() * getBackward().z())
        - (getRight().z() * getUp().y() * getBackward().x());
  }

  default Matrix3x3 invert() {
    double d = determinant();
    return create(((getUp().y() * getBackward().z()) - (getBackward().y() * getUp().z())) / d,
        ((getBackward().x() * getUp().z()) - (getUp().x() * getBackward().z())) / d,
        ((getUp().x() * getBackward().y()) - (getBackward().x() * getUp().y())) / d,
        ((getBackward().y() * getRight().z()) - (getRight().y() * getBackward().z())) / d,
        ((getRight().x() * getBackward().z()) - (getBackward().x() * getRight().z())) / d,
        ((getBackward().x() * getRight().y()) - (getRight().x() * getBackward().y())) / d,
        ((getRight().y() * getUp().z()) - (getUp().y() * getRight().z())) / d,
        ((getUp().x() * getRight().z()) - (getRight().x() * getUp().z())) / d,
        ((getRight().x() * getUp().y()) - (getUp().x() * getRight().y())) / d);
  }

  default Matrix3x3 scale(double d) {
    if (d == 1.0) {
      return this;
    }
    return create(getRight().times(d), getUp().times(d), getBackward().times(d));
  }

  default Matrix3x3 times(OrthogonalMatrix3x3 b) {
    return times((Matrix3x3) b);
  }

  default Matrix3x3 times(FullMatrix3x3 b) {
    return times((Matrix3x3) b);
  }

  default Matrix3x3 times(Matrix3x3 b) {
    double e11 = (getRight().x() * b.getRight().x()) + (getUp().x() * b.getRight().y()) + (getBackward().x() * b.getRight().z());
    double e12 = (getRight().x() * b.getUp().x()) + (getUp().x() * b.getUp().y()) + (getBackward().x() * b.getUp().z());
    double e13 = (getRight().x() * b.getBackward().x()) + (getUp().x() * b.getBackward().y()) + (getBackward().x() * b.getBackward().z());

    double e21 = (getRight().y() * b.getRight().x()) + (getUp().y() * b.getRight().y()) + (getBackward().y() * b.getRight().z());
    double e22 = (getRight().y() * b.getUp().x()) + (getUp().y() * b.getUp().y()) + (getBackward().y() * b.getUp().z());
    double e23 = (getRight().y() * b.getBackward().x()) + (getUp().y() * b.getBackward().y()) + (getBackward().y() * b.getBackward().z());

    double e31 = (getRight().z() * b.getRight().x()) + (getUp().z() * b.getRight().y()) + (getBackward().z() * b.getRight().z());
    double e32 = (getRight().z() * b.getUp().x()) + (getUp().z() * b.getUp().y()) + (getBackward().z() * b.getUp().z());
    double e33 = (getRight().z() * b.getBackward().x()) + (getUp().z() * b.getBackward().y()) + (getBackward().z() * b.getBackward().z());

    return create(e11, e12, e13, e21, e22, e23, e31, e32, e33);
  }
  //</editor-fold>

  //<editor-fold desc="Matrix Applications">
  default Vector3 transform(Vector3 v) {
    double x = (getRight().x() * v.x()) + (getUp().x() * v.y()) + (getBackward().x() * v.z());
    double y = (getRight().y() * v.x()) + (getUp().y() * v.y()) + (getBackward().y() * v.z());
    double z = (getRight().z() * v.x()) + (getUp().z() * v.y()) + (getBackward().z() * v.z());
    return new Vector3(x, y, z);
  }

  default Point3 transform(Point3 p) {
    double x = (getRight().x() * p.x()) + (getUp().x() * p.y()) + (getBackward().x() * p.z());
    double y = (getRight().y() * p.x()) + (getUp().y() * p.y()) + (getBackward().y() * p.z());
    double z = (getRight().z() * p.x()) + (getUp().z() * p.y()) + (getBackward().z() * p.z());
    return new Point3(x, y, z);
  }

  default void transformVector(double[] dest, int offsetDest, double[] src, int offsetSrc) {
    dest[offsetDest] = (getRight().x() * src[offsetSrc]) + (getUp().x() * src[offsetSrc + 1]) + (getBackward().x() * src[offsetSrc + 2]);
    dest[offsetDest + 1] = (getRight().y() * src[offsetSrc]) + (getUp().y() * src[offsetSrc + 1]) + (getBackward().y() * src[offsetSrc + 2]);
    dest[offsetDest + 2] = (getRight().z() * src[offsetSrc]) + (getUp().z() * src[offsetSrc + 1]) + (getBackward().z() * src[offsetSrc + 2]);
  }

  default void transformVector(double[] dest, int offsetDest, float[] src, int offsetSrc) {
    dest[offsetDest] = (getRight().x() * src[offsetSrc]) + (getUp().x() * src[offsetSrc + 1]) + (getBackward().x() * src[offsetSrc + 2]);
    dest[offsetDest + 1] = (getRight().y() * src[offsetSrc]) + (getUp().y() * src[offsetSrc + 1]) + (getBackward().y() * src[offsetSrc + 2]);
    dest[offsetDest + 2] = (getRight().z() * src[offsetSrc]) + (getUp().z() * src[offsetSrc + 1]) + (getBackward().z() * src[offsetSrc + 2]);
  }

  default void transformVector(float[] dest, int offsetDest, float[] src, int offsetSrc) {
    dest[offsetDest] = (float) ((getRight().x() * src[offsetSrc]) + (getUp().x() * src[offsetSrc + 1]) + (getBackward().x() * src[offsetSrc + 2]));
    dest[offsetDest + 1] = (float) ((getRight().y() * src[offsetSrc]) + (getUp().y() * src[offsetSrc + 1]) + (getBackward().y() * src[offsetSrc + 2]));
    dest[offsetDest + 2] = (float) ((getRight().z() * src[offsetSrc]) + (getUp().z() * src[offsetSrc + 1]) + (getBackward().z() * src[offsetSrc + 2]));
  }
  //</editor-fold>

  default void writeColumnMajorArray16(double[] dest) {
    assert dest.length >= 16;
    int offset = 0;
    dest[offset++] = getRight().x();
    dest[offset++] = getRight().y();
    dest[offset++] = getRight().z();
    dest[offset++] = 0.0;
    dest[offset++] = getUp().x();
    dest[offset++] = getUp().y();
    dest[offset++] = getUp().z();
    dest[offset++] = 0.0;
    dest[offset++] = getBackward().x();
    dest[offset++] = getBackward().y();
    dest[offset++] = getBackward().z();
    dest[offset++] = 0.0;
    dest[offset++] = 0.0;
    dest[offset++] = 0.0;
    dest[offset++] = 0.0;
    dest[offset] = 1.0;
  }

  //</editor-fold>

  //<editor-fold desc="Printing">
  default Appendable append(Appendable appendable, DecimalFormat decimalFormat, boolean isLines) throws IOException {
    if (isLines) {
      appendBoundary(appendable, decimalFormat);
    }
    appendRow(appendable, decimalFormat, isLines, getRight().x(), getUp().x(), getBackward().x());
    appendRow(appendable, decimalFormat, isLines, getRight().y(), getUp().y(), getBackward().y());
    appendRow(appendable, decimalFormat, isLines, getRight().z(), getUp().z(), getBackward().z());
    if (isLines) {
      appendBoundary(appendable, decimalFormat);
    }
    return appendable;
  }

  private static void appendBoundary(Appendable appendable, DecimalFormat decimalFormat) throws IOException {
    int n = decimalFormat.format(0.0).length() + 1;
    appendable.append("+-");
    for (int i = 0; i < 3 * n; i++) {
      appendable.append(' ');
    }
    appendable.append("-+\n");
  }

  private static void appendRow(Appendable appendable, DecimalFormat decimalFormat, boolean isLines, double e1, double e2, double e3) throws IOException {
    appendable.append(isLines ? "| " : "[ ");
    appendable.append(decimalFormat.format(e1));
    appendable.append(' ');
    appendable.append(decimalFormat.format(e2));
    appendable.append(' ');
    appendable.append(decimalFormat.format(e3));
    appendable.append(isLines ? "  |\n" : "  ] ");
  }
  //</editor-fold>
}
