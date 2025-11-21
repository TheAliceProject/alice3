package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import java.io.Serializable;

// This is a 3x4 matrix that is ready to be used for affine transformation math.
// The top 3x3 is orientation, with translation in the final column and a final row of 0, 0, 0, 1
public record AffineMatrix4x4(OrthogonalMatrix3x3 orientation, Point3 translation) implements Matrix4x4, Serializable {

  public static AffineMatrix4x4 createTranslation(double x, double y, double z) {
    return new AffineMatrix4x4(OrthogonalMatrix3x3.IDENTITY, new Point3(x, y, z));
  }

  public static AffineMatrix4x4 createOrientation(Orientation orientation) {
    return new AffineMatrix4x4(orientation.asMatrix3x3(), Point3.ORIGIN);
  }

  public static AffineMatrix4x4 createWithDiagonal(Dimension3 diagonal) {
    return new AffineMatrix4x4(diagonal.asScaleMatrix(), Point3.ORIGIN);
  }

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

  public AffineMatrix4x4 invert() {
    Matrix4x4 invert = Matrix4x4.super.invert();
    if (invert instanceof AffineMatrix4x4 matrix4x4) {
      return matrix4x4;
    }
    throw new RuntimeException("AffineMatrix4x4 " + this + " invert() returned non affine matrix " + invert);
  }

  public AffineMatrix4x4 times(AffineMatrix4x4 b) {
    Matrix4x4 product = Matrix4x4.super.times(b);
    if (product instanceof AffineMatrix4x4 matrix4x4) {
      return matrix4x4;
    }
    throw new RuntimeException("AffineMatrix4x4 " + this + " times() returned non affine matrix " + product);
  }

  public Matrix4x4 times(FullMatrix4x4 b) {
   return Matrix4x4.super.times(b);
  }

  //<editor-fold desc="Weighted Mesh Support">
  @Override
  public AffineMatrix4x4 times(double scale) {
    if (scale == 1.0) {
      return this;
    }
    // Scaling orientation denormalizes it, so it is not orthonormal, but not scaling orientation breaks the rendering.
    // The accumulated values along a skeleton are computed below in plusPreservingAffine, and those remain orthonormal.
    return new AffineMatrix4x4(orientation.times(scale), translation.times(scale));
  }

  // Not a full matrix addition of every element. Preserves the implicit value of 1.0 in e44().
  public AffineMatrix4x4 plusPreservingAffine(AffineMatrix4x4 b) {
    if (isNaN()) {
      return b;
    }
    return new AffineMatrix4x4(orientation.plus(b.orientation), translation.plus(b.translation.asVector()));
  }
  //</editor-fold>

  @Override
  public Matrix4x4 scaleTranslation(Matrix3x3 scale) {
    if (scale.isIdentity()) {
      return this;
    }
    return new AffineMatrix4x4(orientation(),
        new Point3(
            translation.x() * scale.getRight().x(),
            translation.y() * scale.getUp().y(),
            translation.z() * scale.getBackward().z()));
  }

  public AffineMatrix4x4 scaleTranslation(double scale) {
    if (scale == 1.0) {
      return this;
    }
    return new AffineMatrix4x4(orientation(),
        new Point3(
            translation.x() * scale,
            translation.y() * scale,
            translation.z() * scale));
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

  public static AffineMatrix4x4 createFromColumnMajorArray12(double[] columnMajorArray) {
    assert columnMajorArray.length == 12;

    Vector3 right = new Vector3(columnMajorArray[0], columnMajorArray[1], columnMajorArray[2]);
    Vector3 up = new Vector3(columnMajorArray[3], columnMajorArray[4], columnMajorArray[5]);
    Vector3 back = new Vector3(columnMajorArray[6], columnMajorArray[7], columnMajorArray[8]);
    OrthogonalMatrix3x3 orientation = new OrthogonalMatrix3x3(right, up, back);
    Point3 translation = new Point3(columnMajorArray[9], columnMajorArray[10], columnMajorArray[11]);
    return new AffineMatrix4x4(orientation, translation);
  }


  public static AffineMatrix4x4 createFromRowMajorArray(double[] rowMajorArray) {
    assert rowMajorArray.length >= 12;
    Vector3 right = new Vector3(rowMajorArray[0], rowMajorArray[4], rowMajorArray[8]);
    Vector3 up = new Vector3(rowMajorArray[1], rowMajorArray[5], rowMajorArray[9]);
    Vector3 back = new Vector3(rowMajorArray[2], rowMajorArray[6], rowMajorArray[10]);
    OrthogonalMatrix3x3 orientation = new OrthogonalMatrix3x3(right, up, back);
    Point3 translation = new Point3(rowMajorArray[3], rowMajorArray[7], rowMajorArray[11]);
    if (rowMajorArray.length == 16) {
      if (rowMajorArray[12] != 0 || rowMajorArray[13] != 0 || rowMajorArray[14] != 0 || rowMajorArray[15] != 1.0) {
        Logger.warning("Row major array was not affine");
      }
    }
    return new AffineMatrix4x4(orientation, translation);
  }

  public double[] asColumnMajorArray12() {
    double[] dest = new double[12];
    int offset = 0;
    dest[offset++] = e11();
    dest[offset++] = e21();
    dest[offset++] = e31();
    dest[offset++] = e12();
    dest[offset++] = e22();
    dest[offset++] = e32();
    dest[offset++] = e13();
    dest[offset++] = e23();
    dest[offset++] = e33();
    dest[offset++] = e14();
    dest[offset++] = e24();
    dest[offset] = e34();
    return dest;
  }

  public AffineMatrix4x4 normalizeOnlyOrientation() {
    if (orientation.isNormalized()) {
      return this;
    }
    return new AffineMatrix4x4(orientation.normalized(), translation);
  }

  public AffineMatrix4x4 normalizeOrientation() {
      if (orientation.isNormalized()) {
        return this;
      }
      double xScale = orientation.right().magnitude();
      double yScale = orientation.up().magnitude();
      double zScale = orientation.backward().magnitude();

      Matrix3x3 inverseScale = Matrix3x3.create(
          1 / xScale, 0, 0,
          0, 1 / yScale, 0,
          0, 0, 1 / zScale);

    Matrix3x3 scaledOrientation = orientation.times(inverseScale);
    if (scaledOrientation.isNormalized() && scaledOrientation instanceof OrthogonalMatrix3x3 matrix3x3) {
      return new AffineMatrix4x4(matrix3x3, inverseScale.transform(translation));
    }
    Logger.warning("Unable to normalize orientation. Using identity matrix to replace:\n" + orientation);
    return new AffineMatrix4x4(OrthogonalMatrix3x3.IDENTITY, inverseScale.transform(translation));
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    binaryEncoder.encodeRecord(this);
  }

  public AffineMatrix4x4 withTranslation(Point3 newTranslation) {
    return new AffineMatrix4x4(orientation, newTranslation);
  }

  public AffineMatrix4x4 withOrientation(OrthogonalMatrix3x3 newOrientation) {
    return new AffineMatrix4x4(newOrientation, translation);
  }

  public AffineMatrix4x4 rotateAboutXAxis(Angle angle) {
    return this.times(AffineMatrix4x4.createOrientation(AxisRotation.createXAxisRotation(angle)));
  }

  public AffineMatrix4x4 rotateAboutYAxis(Angle angle) {
    return this.times(AffineMatrix4x4.createOrientation(AxisRotation.createYAxisRotation(angle)));
  }
}
