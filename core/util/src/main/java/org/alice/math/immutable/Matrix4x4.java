package org.alice.math.immutable;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;

public interface Matrix4x4 {
  AffineMatrix4x4 IDENTITY = new AffineMatrix4x4(Matrix3x3.IDENTITY, Vector3.ZERO);
  AffineMatrix4x4 NaN = new AffineMatrix4x4(Matrix3x3.NaN, Vector3.NaN);

  boolean isAffine();
  boolean isNaN();
  boolean isIdentity();

  boolean isWithinEpsilonOf(Matrix4x4 other, double epsilon);

  default boolean isWithinReasonableEpsilonOf(Matrix4x4 other) {
    return isWithinEpsilonOf(other, EpsilonUtilities.REASONABLE_EPSILON);
  }

  static Matrix4x4 create(double e11, double e12, double e13, double e14, double e21, double e22, double e23, double e24, double e31, double e32, double e33, double e34, double e41, double e42, double e43, double e44) {
    // Comes in as row major values
    if (e41 == 0 && e42 == 0 && e43 == 0 && e44 == 1.0) {
      // Affine matrix always have the same 4th row
      // It stores the rest as a 3x3 orientation plus a translation vector
      return new AffineMatrix4x4(
          Matrix3x3.create(e11, e12, e13, e21, e22, e23, e31, e32, e33),
          new Vector3(e14, e24, e34));
    }
    // FullMatrix stores a column in each vector
    return new FullMatrix4x4(
        new Vector4(e11, e21, e31, e41),
        new Vector4(e12, e22, e32, e42),
        new Vector4(e13, e23, e33, e43),
        new Vector4(e14, e24, e34, e44));
  }

  static Matrix4x4 fromTranslation(Vector3 p) {
    return new AffineMatrix4x4(Matrix3x3.IDENTITY, p);
  }

  static Matrix4x4 fromScale(double x, double y, double z) {
    return new AffineMatrix4x4(
        new Matrix3x3(
            new Vector3(x, 0, 0),
            new Vector3(0, y, 0),
            new Vector3(0, 0, z)),
        Vector3.ZERO);
  }

  default Point3 transform(Point3 b) {
    double x = (e11() * b.x()) + (e12() * b.y()) + (e13() * b.z());
    double y = (e21() * b.x()) + (e22() * b.y()) + (e23() * b.z());
    double z = (e31() * b.x()) + (e32() * b.y()) + (e33() * b.z());
    return new Point3(x, y, z);
  }

  default Vector3 transform(Vector3 b) {
    double x = (e11() * b.x()) + (e12() * b.y()) + (e13() * b.z());
    double y = (e21() * b.x()) + (e22() * b.y()) + (e23() * b.z());
    double z = (e31() * b.x()) + (e32() * b.y()) + (e33() * b.z());
    return new Vector3(x, y, z);
  }

  default Vector4 transform(Vector4 b) {
    double x = (e11() * b.x()) + (e12() * b.y()) + (e13() * b.z() + e14() * b.w());
    double y = (e21() * b.x()) + (e22() * b.y()) + (e23() * b.z() + e24() * b.w());
    double z = (e31() * b.x()) + (e32() * b.y()) + (e33() * b.z() + e34() * b.w());
    double w = (e41() * b.x()) + (e42() * b.y()) + (e43() * b.z() + e44() * b.w());
    return new Vector4(x, y, z, w);
  }

  // Transform with full matrix multiplication
  void transformPoint3(double[] dest, int offsetDest, double[] src, int offsetSrc);

  // Transform just the orientation, ignoring the translation
  void transformVector3(float[] dest, int offsetDest, float[] src, int offsetSrc);

  default Matrix4x4 invert() {
    double d = determinant();
    if (d == 0) {

      return AffineMatrix4x4.IDENTITY;
    }
    double e11 = (((((e23() * e34() * e42()) - (e24() * e33() * e42())) + (e24() * e32() * e43())) - (e22() * e34() * e43()) - (e23() * e32() * e44())) + (e22() * e33() * e44())) / d;
    double e12 = ((((e14() * e33() * e42()) - (e13() * e34() * e42()) - (e14() * e32() * e43())) + (e12() * e34() * e43()) + (e13() * e32() * e44())) - (e12() * e33() * e44())) / d;
    double e13 = (((((e13() * e24() * e42()) - (e14() * e23() * e42())) + (e14() * e22() * e43())) - (e12() * e24() * e43()) - (e13() * e22() * e44())) + (e12() * e23() * e44())) / d;
    double e14 = ((((e14() * e23() * e32()) - (e13() * e24() * e32()) - (e14() * e22() * e33())) + (e12() * e24() * e33()) + (e13() * e22() * e34())) - (e12() * e23() * e34())) / d;
    double e21 = ((((e24() * e33() * e41()) - (e23() * e34() * e41()) - (e24() * e31() * e43())) + (e21() * e34() * e43()) + (e23() * e31() * e44())) - (e21() * e33() * e44())) / d;
    double e22 = (((((e13() * e34() * e41()) - (e14() * e33() * e41())) + (e14() * e31() * e43())) - (e11() * e34() * e43()) - (e13() * e31() * e44())) + (e11() * e33() * e44())) / d;
    double e23 = ((((e14() * e23() * e41()) - (e13() * e24() * e41()) - (e14() * e21() * e43())) + (e11() * e24() * e43()) + (e13() * e21() * e44())) - (e11() * e23() * e44())) / d;
    double e24 = (((((e13() * e24() * e31()) - (e14() * e23() * e31())) + (e14() * e21() * e33())) - (e11() * e24() * e33()) - (e13() * e21() * e34())) + (e11() * e23() * e34())) / d;
    double e31 = (((((e22() * e34() * e41()) - (e24() * e32() * e41())) + (e24() * e31() * e42())) - (e21() * e34() * e42()) - (e22() * e31() * e44())) + (e21() * e32() * e44())) / d;
    double e32 = ((((e14() * e32() * e41()) - (e12() * e34() * e41()) - (e14() * e31() * e42())) + (e11() * e34() * e42()) + (e12() * e31() * e44())) - (e11() * e32() * e44())) / d;
    double e33 = (((((e12() * e24() * e41()) - (e14() * e22() * e41())) + (e14() * e21() * e42())) - (e11() * e24() * e42()) - (e12() * e21() * e44())) + (e11() * e22() * e44())) / d;
    double e34 = ((((e14() * e22() * e31()) - (e12() * e24() * e31()) - (e14() * e21() * e32())) + (e11() * e24() * e32()) + (e12() * e21() * e34())) - (e11() * e22() * e34())) / d;
    double e41 = ((((e23() * e32() * e41()) - (e22() * e33() * e41()) - (e23() * e31() * e42())) + (e21() * e33() * e42()) + (e22() * e31() * e43())) - (e21() * e32() * e43())) / d;
    double e42 = (((((e12() * e33() * e41()) - (e13() * e32() * e41())) + (e13() * e31() * e42())) - (e11() * e33() * e42()) - (e12() * e31() * e43())) + (e11() * e32() * e43())) / d;
    double e43 = ((((e13() * e22() * e41()) - (e12() * e23() * e41()) - (e13() * e21() * e42())) + (e11() * e23() * e42()) + (e12() * e21() * e43())) - (e11() * e22() * e43())) / d;
    double e44 = (((((e12() * e23() * e31()) - (e13() * e22() * e31())) + (e13() * e21() * e32())) - (e11() * e23() * e32()) - (e12() * e21() * e33())) + (e11() * e22() * e33())) / d;
    return create(
        e11, e12, e13, e14,
        e21, e22, e23, e24,
        e31, e32, e33, e34,
        e41, e42, e43, e44);
  }

  default double determinant() {
    return (((((e14() * e23() * e32() * e41()
        - e13() * e24() * e32() * e41()
        - e14() * e22() * e33() * e41()
        + e12() * e24() * e33() * e41()
        + e13() * e22() * e34() * e41())
        - e12() * e23() * e34() * e41()
        - e14() * e23() * e31() * e42()
        + e13() * e24() * e31() * e42()
        + e14() * e21() * e33() * e42())
        - e11() * e24() * e33() * e42()
        - e13() * e21() * e34() * e42()
        + e11() * e23() * e34() * e42()
        + e14() * e22() * e31() * e43())
        - e12() * e24() * e31() * e43()
        - e14() * e21() * e32() * e43()
        + e11() * e24() * e32() * e43()
        + e12() * e21() * e34() * e43())
        - e11() * e22() * e34() * e43()
        - e13() * e22() * e31() * e44()
        + e12() * e23() * e31() * e44()
        + e13() * e21() * e32() * e44())
        - e11() * e23() * e32() * e44()
        - e12() * e21() * e33() * e44()
        + e11() * e22() * e33() * e44();
  }

  Matrix4x4 times(double scale);

  default Matrix4x4 times(Matrix4x4 b) {
    Vector4 rowX = rowX();
    Vector4 rowY = rowY();
    Vector4 rowZ = rowZ();
    Vector4 rowW = rowW();
    return create(
        rowX.dotProduct(b.columnRight()), rowX.dotProduct(b.columnUp()), rowX.dotProduct(b.columnBackward()), rowX.dotProduct(b.columnTranslation()),
        rowY.dotProduct(b.columnRight()), rowY.dotProduct(b.columnUp()), rowY.dotProduct(b.columnBackward()), rowY.dotProduct(b.columnTranslation()),
        rowZ.dotProduct(b.columnRight()), rowZ.dotProduct(b.columnUp()), rowZ.dotProduct(b.columnBackward()), rowZ.dotProduct(b.columnTranslation()),
        rowW.dotProduct(b.columnRight()), rowW.dotProduct(b.columnUp()), rowW.dotProduct(b.columnBackward()), rowW.dotProduct(b.columnTranslation()));
  }

  // Access by row
  Vector4 rowX();
  Vector4 rowY();
  Vector4 rowZ();
  Vector4 rowW();

  // Access by column
  Vector4 columnRight();
  Vector4 columnUp();
  Vector4 columnBackward();
  Vector4 columnTranslation();

  // access by element (row, column)
  double e11();
  double e12();
  double e13();
  double e14();
  double e21();
  double e22();
  double e23();
  double e24();
  double e31();
  double e32();
  double e33();
  double e34();
  double e41();
  double e42();
  double e43();
  double e44();

  default double[] asColumnMajorArray16() {
    return asColumnMajorArray16(new double[16]);
  }

  default double[] asColumnMajorArray16(double[] rv) {
    assert rv.length == 16;
    rv[0] = e11();
    rv[1] = e21();
    rv[2] = e31();
    rv[3] = e41();
    rv[4] = e12();
    rv[5] = e22();
    rv[6] = e32();
    rv[7] = e42();
    rv[8] = e13();
    rv[9] = e23();
    rv[10] = e33();
    rv[11] = e43();
    rv[12] = e14();
    rv[13] = e24();
    rv[14] = e34();
    rv[15] = e44();
    return rv;
  }

  default float[] asColumnMajorArray16(float[] rv) {
    assert rv.length == 16;
    rv[0] = (float) e11();
    rv[1] = (float) e21();
    rv[2] = (float) e31();
    rv[3] = (float) e41();
    rv[4] = (float) e12();
    rv[5] = (float) e22();
    rv[6] = (float) e32();
    rv[7] = (float) e42();
    rv[8] = (float) e13();
    rv[9] = (float) e23();
    rv[10] = (float) e33();
    rv[11] = (float) e43();
    rv[12] = (float) e14();
    rv[13] = (float) e24();
    rv[14] = (float) e34();
    rv[15] = (float) e44();
    return rv;
  }

  default double[] asRowMajorArray16(double[] rv) {
    assert rv.length == 16;
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
    rv[12] = e41();
    rv[13] = e42();
    rv[14] = e43();
    rv[15] = e44();

    return rv;
  }

  default double[] asRowMajorArray16() {
    return asRowMajorArray16(new double[16]);
  }

  Matrix4x4 scaleTranslation(Matrix3x3 scale);

  // Temporary use during transition to immutable Records
  @Deprecated(forRemoval = true)
  edu.cmu.cs.dennisc.math.AbstractMatrix4x4 mutable();
}
