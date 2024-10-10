package org.alice.math.immutable;


// The columns of a 4x4 matrix
public record FullMatrix4x4(Vector4 right, Vector4 up, Vector4 backward, Vector4 translation) implements Matrix4x4 {
  public static FullMatrix4x4 ZERO = new FullMatrix4x4(Vector4.ZERO, Vector4.ZERO, Vector4.ZERO, Vector4.ZERO);

  @Override
  public Matrix4x4 times(double factor) {
    return new FullMatrix4x4(right.times(factor), up.times(factor), backward.times(factor), translation.times(factor));
  }

  @Override
  public void transformPoint3(double[] dest, int offsetDest, double[] src, int offsetSrc) {
    if (dest == null || src == null) {
      return;
    }
    dest[offsetDest] = (right.x() * src[offsetSrc]) + (up.x() * src[offsetSrc + 1]) + (backward.x() * src[offsetSrc + 2]);
    dest[offsetDest + 1] = (right.y() * src[offsetSrc]) + (up.y() * src[offsetSrc + 1]) + (backward.y() * src[offsetSrc + 2]);
    dest[offsetDest + 2] = (right.z() * src[offsetSrc]) + (up.z() * src[offsetSrc + 1]) + (backward.z() * src[offsetSrc + 2]);
    dest[offsetDest] += translation.x();
    dest[offsetDest + 1] += translation.y();
    dest[offsetDest + 2] += translation.z();
  }

  @Override
  public void transformVector3(float[] dest, int offsetDest, float[] src, int offsetSrc) {
    if (dest == null || src == null) {
      return;
    }
    dest[offsetDest] = (float) ((right.x() * src[offsetSrc]) + (up.x() * src[offsetSrc + 1]) + (backward.x() * src[offsetSrc + 2]));
    dest[offsetDest + 1] = (float) ((right.y() * src[offsetSrc]) + (up.y() * src[offsetSrc + 1]) + (backward.y() * src[offsetSrc + 2]));
    dest[offsetDest + 2] = (float) ((right.z() * src[offsetSrc]) + (up.z() * src[offsetSrc + 1]) + (backward.z() * src[offsetSrc + 2]));
  }

  @Override
  public boolean isAffine() {
    return Vector4.UNIT_W.equals(rowX());
  }

  @Override
  public boolean isNaN() {
    return right.isNaN() || up.isNaN() || backward.isNaN() || translation.isNaN();
  }

  @Override
  public boolean isIdentity() {
    return Vector4.UNIT_X.equals(right)
        && Vector4.UNIT_Y.equals(up)
        && Vector4.UNIT_Z.equals(backward)
        && Vector4.UNIT_W.equals(translation);
  }

  @Override
  public boolean isWithinEpsilonOf(Matrix4x4 other, double epsilon) {
    return rowX().isWithinEpsilonOf(other.rowX(), epsilon)
        && rowY().isWithinEpsilonOf(other.rowY(), epsilon)
        && rowZ().isWithinEpsilonOf(other.rowZ(), epsilon);
  }

  public Vector4 rowX() {
    return new Vector4(right.x(), up.x(), backward.x(), translation.x());
  }

  public Vector4 rowY() {
    return new Vector4(right.y(), up.y(), backward.y(), translation.y());
  }

  public Vector4 rowZ() {
    return new Vector4(right.z(), up.z(), backward.z(), translation.z());
  }

  public Vector4 rowW() {
    return new Vector4(right.w(), up.w(), backward.w(), translation.w());
  }

  @Override
  public Vector4 columnRight() {
    return right;
  }

  @Override
  public Vector4 columnUp() {
    return up;
  }

  @Override
  public Vector4 columnBackward() {
    return backward;
  }

  @Override
  public Vector4 columnTranslation() {
    return translation;
  }

  @Override
  public double e11() {
    return right.x();
  }

  @Override
  public double e12() {
    return up.x();
  }

  @Override
  public double e13() {
    return backward.x();
  }

  @Override
  public double e14() {
    return translation.x();
  }

  @Override
  public double e21() {
    return right.y();
  }

  @Override
  public double e22() {
    return up.y();
  }

  @Override
  public double e23() {
    return backward.y();
  }

  @Override
  public double e24() {
    return translation.y();
  }

  @Override
  public double e31() {
    return right.z();
  }

  @Override
  public double e32() {
    return up.z();
  }

  @Override
  public double e33() {
    return backward.z();
  }

  @Override
  public double e34() {
    return translation.z();
  }

  @Override
  public double e41() {
    return right.w();
  }

  @Override
  public double e42() {
    return up.w();
  }

  @Override
  public double e43() {
    return backward.w();
  }

  @Override
  public double e44() {
    return translation.w();
  }

  @Override
  public Matrix4x4 scaleTranslation(Matrix3x3 scale) {
    // TODO Define meaning and implement if useful
    throw new RuntimeException("Unexpected scaling of non affine translation");
  }

  // Temporary use during transition to immutable Records
  @Deprecated(forRemoval = true)
  public edu.cmu.cs.dennisc.math.Matrix4x4 mutable() {
    return new edu.cmu.cs.dennisc.math.Matrix4x4(asColumnMajorArray16());
  }
}
