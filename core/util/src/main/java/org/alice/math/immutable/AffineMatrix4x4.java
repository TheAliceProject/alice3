package org.alice.math.immutable;

public record AffineMatrix4x4(Matrix3x3 orientation, Vector3 translation) implements Matrix4x4 {
  // All zeros is not an affine matrix, but is useful when summing up affine matrices using plusPreservingAffine
  public static AffineMatrix4x4 ZERO = new AffineMatrix4x4(Matrix3x3.ZERO, Vector3.ZERO);

  @Override
  public boolean isAffine() {
    return true;
  }

  @Override
  public boolean isNaN() {
    return orientation.isNaN() || translation.isNaN();
  }

  @Override
  public boolean isIdentity() {
    return orientation.isIdentity() && translation.isZero();
  }

  @Override
  public boolean isWithinEpsilonOf(Matrix4x4 b, double epsilon) {
    if (b instanceof AffineMatrix4x4 affineMatrix) {
      return orientation.isWithinEpsilonOf(affineMatrix.orientation, epsilon)
          && translation.isWithinEpsilonOf(affineMatrix.translation, epsilon);
    }
    return b.isWithinReasonableEpsilonOf(this);
  }

  @Override
  public AffineMatrix4x4 times(double scale) {
    return new AffineMatrix4x4(orientation.times(scale), translation.times(scale));
  }

  // Not a full matrix addition of every element. Preserves the implicit value of 1.0 in e44().
  public AffineMatrix4x4 plusPreservingAffine(AffineMatrix4x4 b) {
    return new AffineMatrix4x4(orientation.plus(b.orientation), translation.plus(b.translation));
  }

  @Override
  public Matrix4x4 scaleTranslation(Matrix3x3 scale) {
    return new AffineMatrix4x4(orientation(),
        new Vector3(
            translation.x() * scale.right().x(),
            translation.y() * scale.up().y(),
            translation.z() * scale.backward().z()));
  }

  @Override
  public void transformPoint3(double[] afRV, int offsetDest, double[] afSrc, int offsetSrc) {
    if (afRV == null) {
      return;
    }
    orientation.transformVector(afRV, offsetDest, afSrc, offsetSrc);
    afRV[offsetDest] += this.translation.x();
    afRV[offsetDest + 1] += this.translation.y();
    afRV[offsetDest + 2] += this.translation.z();
  }

  @Override
  public void transformVector3(float[] afRV, int offsetDest, float[] afSrc, int offsetSrc) {
    if (afRV == null) {
      return;
    }
    orientation.transformVector(afRV, offsetDest, afSrc, offsetSrc);
  }

  public Vector4 transform(Vector4 b) {
    double x = (e11() * b.x()) + (e12() * b.y()) + (e13() * b.z()) + (e14() * b.w());
    double y = (e21() * b.x()) + (e22() * b.y()) + (e23() * b.z()) + (e24() * b.w());
    double z = (e31() * b.x()) + (e32() * b.y()) + (e33() * b.z()) + (e34() * b.w());
    double w = (e41() * b.x()) + (e42() * b.y()) + (e43() * b.z()) + (e44() * b.w());
    return new Vector4(x, y, z, w);
  }

  public Vector4 rowX() {
    return new Vector4(e11(), e12(), e13(), e14());
  }

  public Vector4 rowY() {
    return new Vector4(e21(), e22(), e23(), e24());
  }

  public Vector4 rowZ() {
    return new Vector4(e31(), e32(), e33(), e34());
  }

  public Vector4 rowW() {
    return Vector4.UNIT_W;
  }

  @Override
  public Vector4 columnRight() {
    return new Vector4(e11(), e21(), e31(), e41());
  }

  @Override
  public Vector4 columnUp() {
    return new Vector4(e12(), e22(), e32(), e42());
  }

  @Override
  public Vector4 columnBackward() {
    return new Vector4(e13(), e23(), e33(), e43());
  }

  @Override
  public Vector4 columnTranslation() {
    return new Vector4(e14(), e24(), e34(), e44());
  }

  @Override
  public double e11() {
    return orientation.right().x();
  }

  @Override
  public double e21() {
    return orientation.right().y();
  }

  @Override
  public double e31() {
    return orientation.right().z();
  }

  @Override
  public double e41() {
    return 0.0;
  }

  @Override
  public double e12() {
    return orientation.up().x();
  }

  @Override
  public double e22() {
    return orientation.up().y();
  }

  @Override
  public double e32() {
    return orientation.up().z();
  }

  @Override
  public double e42() {
    return 0.0;
  }

  @Override
  public double e13() {
    return orientation.backward().x();
  }

  @Override
  public double e23() {
    return orientation.backward().y();
  }

  @Override
  public double e33() {
    return orientation.backward().z();
  }

  @Override
  public double e43() {
    return 0.0;
  }

  @Override
  public double e14() {
    return translation.x();
  }

  @Override
  public double e24() {
    return translation.y();
  }

  @Override
  public double e34() {
    return translation.z();
  }

  @Override
  public double e44() {
    return 1.0;
  }

  // Temporary use during transition to immutable Records
  @Deprecated(forRemoval = true)
  public edu.cmu.cs.dennisc.math.AffineMatrix4x4 mutable() {
    return new edu.cmu.cs.dennisc.math.AffineMatrix4x4(orientation.mutable(), translation.mutablePoint());
  }

  public static AffineMatrix4x4 createFromColumnMajorArray12(double[] columnMajorArray) {
    assert columnMajorArray.length == 12;

    Vector3 right = new Vector3(columnMajorArray[0], columnMajorArray[1], columnMajorArray[2]);
    Vector3 up = new Vector3(columnMajorArray[3], columnMajorArray[4], columnMajorArray[5]);
    Vector3 back = new Vector3(columnMajorArray[6], columnMajorArray[7], columnMajorArray[8]);
    Matrix3x3 orientation = new Matrix3x3(right, up, back);
    Vector3 translation = new Vector3(columnMajorArray[9], columnMajorArray[10], columnMajorArray[11]);
    return new AffineMatrix4x4(orientation, translation);
  }


  public static AffineMatrix4x4 createFromRowMajorArray12(double[] rowMajorArray) {
    assert rowMajorArray.length == 12;

    Vector3 right = new Vector3(rowMajorArray[0], rowMajorArray[4], rowMajorArray[8]);
    Vector3 up = new Vector3(rowMajorArray[1], rowMajorArray[5], rowMajorArray[9]);
    Vector3 back = new Vector3(rowMajorArray[2], rowMajorArray[6], rowMajorArray[10]);
    Matrix3x3 orientation = new Matrix3x3(right, up, back);
    Vector3 translation = new Vector3(rowMajorArray[3], rowMajorArray[7], rowMajorArray[11]);
    return new AffineMatrix4x4(orientation, translation);
  }

  public double[] asColumnMajorArray12(double[] rv) {
    assert rv.length == 12;
    rv[0] = e11();
    rv[1] = e21();
    rv[2] = e31();

    rv[3] = e12();
    rv[4] = e22();
    rv[5] = e32();

    rv[6] = e13();
    rv[7] = e23();
    rv[8] = e33();

    rv[9] = e14();
    rv[10] = e24();
    rv[11] = e34();
    return rv;
  }

  public double[] asColumnMajorArray12() {
    return asColumnMajorArray12(new double[12]);
  }

  public double[] asRowMajorArray12(double[] rv) {
    assert rv.length == 12;
    rv[0] = e11();
    rv[1] = e12();
    rv[2] = e13();
    rv[3] = e14();

    rv[4] = e21();
    rv[5] = e22();
    rv[6] = e23();
    rv[7] = e24();

    rv[8] = e31();
    rv[9] = e32();
    rv[10] = e33();
    rv[11] = e34();
    return rv;
  }

  public double[] asRowMajorArray12() {
    return asRowMajorArray12(new double[12]);
  }
}
