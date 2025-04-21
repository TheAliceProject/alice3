package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;

public interface Matrix4x4 extends BinaryEncodableAndDecodable {
  AffineMatrix4x4 IDENTITY = new AffineMatrix4x4(OrthogonalMatrix3x3.IDENTITY, Point3.ORIGIN);
  AffineMatrix4x4 NaN = new AffineMatrix4x4(OrthogonalMatrix3x3.NaN, Point3.NaN);

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
      // An AffineMatrix always has the same 4th row [0, 0, 0, 1]
      // It stores the first three rows as a 3x3 orientation matrix and a translation vector.
      OrthogonalMatrix3x3 orientation = new OrthogonalMatrix3x3(
          new Vector3(e11, e21, e31),
          new Vector3(e12, e22, e32),
          new Vector3(e13, e23, e33));
      // Ideally, the orientation matrix should be orthonormal, containing three mutually perpendicular unit vectors.
      // We do not enforce that, but this can make note of those that aren't close enough
      if (orientation.deviationFromNormal() > .05) {
        System.out.println("Not quite orthogonal.\n  up    X right: " + orientation.getUp().dotProduct(orientation.getRight())
            + "\n  right X  back: " + orientation.getRight().dotProduct(orientation.getBackward())
            + "\n  back  X    up: " + orientation.getBackward().dotProduct(orientation.getUp())
            + "\n  sum deviation: " + orientation.deviationFromNormal());
      }
      return new AffineMatrix4x4(orientation, new Point3(e14, e24, e34));
    }
    // FullMatrix stores a column in each vector
    return new FullMatrix4x4(
        new Vector4(e11, e21, e31, e41),
        new Vector4(e12, e22, e32, e42),
        new Vector4(e13, e23, e33, e43),
        new Vector4(e14, e24, e34, e44));
  }

  static Matrix4x4 fromTranslation(Point3 p) {
    return new AffineMatrix4x4(OrthogonalMatrix3x3.IDENTITY, p);
  }

  static Matrix4x4 fromScale(double x, double y, double z) {
    return new AffineMatrix4x4(
        new OrthogonalMatrix3x3(
            new Vector3(x, 0, 0),
            new Vector3(0, y, 0),
            new Vector3(0, 0, z)),
        Point3.ORIGIN);
  }

  default Point3 transform(Point3 b) {
    if (this.isIdentity()) {
      return b;
    }
    double x = (e11() * b.x()) + (e12() * b.y()) + (e13() * b.z()) + e14();
    double y = (e21() * b.x()) + (e22() * b.y()) + (e23() * b.z()) + e24();
    double z = (e31() * b.x()) + (e32() * b.y()) + (e33() * b.z()) + e34();
    return new Point3(x, y, z);
  }

  // Alice treats Vector3 (& 3f) as direction only and does not expect the translation (4th column) to contribute.
  // If that is the required behavior consider using Point3, or Vector4
  default Vector3 transform(Vector3 b) {
    if (this.isIdentity()) {
      return b;
    }
    double x = (e11() * b.x()) + (e12() * b.y()) + (e13() * b.z());
    double y = (e21() * b.x()) + (e22() * b.y()) + (e23() * b.z());
    double z = (e31() * b.x()) + (e32() * b.y()) + (e33() * b.z());
    return new Vector3(x, y, z);
  }

  default Vector3f transform(Vector3f b) {
    if (this.isIdentity()) {
      return b;
    }
    float x = (float) ((e11() * b.x()) + (e12() * b.y()) + (e13() * b.z()));
    float y = (float) ((e21() * b.x()) + (e22() * b.y()) + (e23() * b.z()));
    float z = (float) ((e31() * b.x()) + (e32() * b.y()) + (e33() * b.z()));
    return new Vector3f(x, y, z);
  }

  default Vector4 transform(Vector4 b) {
    if (this.isIdentity()) {
      return b;
    }
    double x = (e11() * b.x()) + (e12() * b.y()) + (e13() * b.z() + e14() * b.w());
    double y = (e21() * b.x()) + (e22() * b.y()) + (e23() * b.z() + e24() * b.w());
    double z = (e31() * b.x()) + (e32() * b.y()) + (e33() * b.z() + e34() * b.w());
    double w = (e41() * b.x()) + (e42() * b.y()) + (e43() * b.z() + e44() * b.w());
    return new Vector4(x, y, z, w);
  }

  default Ray transform(Ray ray) {
    if (this.isIdentity()) {
      return ray;
    }
    return new Ray(transform(ray.origin()), transform(ray.direction()).normalized());
  }

  // Transform with full matrix multiplication
  void transformPoint3(double[] dest, int offsetDest, double[] src, int offsetSrc);

  // Transform just the orientation, ignoring the translation
  void transformVector3(float[] dest, int offsetDest, float[] src, int offsetSrc);

  default Matrix4x4 invert() {
    if (this.isIdentity()) {
      return Matrix4x4.IDENTITY;
    }
    double d = determinant();
    if (d == 0) {
      return Matrix4x4.IDENTITY;
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
    if (this.isIdentity()) {
      return b;
    }
    if (b.isIdentity()) {
      return this;
    }
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
    double[] array = new double[16];
    writeColumnMajorArray16(array);
    return array;
  }

  default void writeColumnMajorArray16(double[] dest) {
    assert dest.length == 16;
    int offset = 0;
    dest[offset++] = e11();
    dest[offset++] = e21();
    dest[offset++] = e31();
    dest[offset++] = e41();
    dest[offset++] = e12();
    dest[offset++] = e22();
    dest[offset++] = e32();
    dest[offset++] = e42();
    dest[offset++] = e13();
    dest[offset++] = e23();
    dest[offset++] = e33();
    dest[offset++] = e43();
    dest[offset++] = e14();
    dest[offset++] = e24();
    dest[offset++] = e34();
    dest[offset] = e44();
  }

  default void writeColumnMajorArray16(float[] dest) {
    assert dest.length == 16;
    int offset = 0;
    dest[offset++] = (float) e11();
    dest[offset++] = (float) e21();
    dest[offset++] = (float) e31();
    dest[offset++] = (float) e41();
    dest[offset++] = (float) e12();
    dest[offset++] = (float) e22();
    dest[offset++] = (float) e32();
    dest[offset++] = (float) e42();
    dest[offset++] = (float) e13();
    dest[offset++] = (float) e23();
    dest[offset++] = (float) e33();
    dest[offset++] = (float) e43();
    dest[offset++] = (float) e14();
    dest[offset++] = (float) e24();
    dest[offset++] = (float) e34();
    dest[offset] = (float) e44();
  }

  default double[] asRowMajorArray16() {
    double[] dest = new double[16];
    int offset = 0;
    dest[offset++] = e11();
    dest[offset++] = e12();
    dest[offset++] = e13();
    dest[offset++] = e14();
    dest[offset++] = e21();
    dest[offset++] = e22();
    dest[offset++] = e23();
    dest[offset++] = e24();
    dest[offset++] = e31();
    dest[offset++] = e32();
    dest[offset++] = e33();
    dest[offset++] = e34();
    dest[offset++] = e41();
    dest[offset++] = e42();
    dest[offset++] = e43();
    dest[offset] = e44();
    return dest;
  }

  Matrix4x4 scaleTranslation(Matrix3x3 scale);
}
