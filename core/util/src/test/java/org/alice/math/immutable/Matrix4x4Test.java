package org.alice.math.immutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix4x4Test {

  static final Matrix4x4 M1 = Matrix4x4.create(
      1, 2, 3, 7,
      6, 5, 4, 2,
      0, 8, 9, 5,
      0, 5, 8, 1
  );
  static final double M1_DET = -781.0;

  static final AffineMatrix4x4 A1 = new AffineMatrix4x4(OrthogonalMatrix3x3.IDENTITY, new Point3(4, 6, 2));

  static final Matrix4x4 LOCAL_IDENTITY = new AffineMatrix4x4(OrthogonalMatrix3x3.IDENTITY, Point3.ORIGIN);

  @Test
  void createShouldMakeMatrix() {
    Matrix4x4 m = new FullMatrix4x4(Vector4.UNIT_X, Vector4.UNIT_Y, Vector4.UNIT_Z, Vector4.UNIT_W);
    assertNotNull(m, "Matrix should not be null");
  }

  @Test
  void createIdentityShouldEqualIdentityMatrix() {
    assertEquals(Matrix4x4.IDENTITY, LOCAL_IDENTITY, "Matrix should be identity");
  }

  @Test
  void createIdentityShouldReplyTrueOnIsIdentity() {
    assertTrue(LOCAL_IDENTITY.isIdentity(), "Matrix should be identity");
  }

  @Test
  void createIdentityShouldReplyFalseOnIsNaN() {
    assertFalse(LOCAL_IDENTITY.isNaN(), "Matrix should not be NaN");
  }

  @Test
  void isNaNShouldRecognizeNaN() {
    assertTrue(Matrix4x4.NaN.isNaN(), "Matrix should be NaN");
  }

  @Test
  void isNaNShouldRejectValidMatrix() {
    assertFalse(M1.isNaN(), "Matrix should not be NaN");
  }

  @Test
  void isIdentityShouldRecognizeIdentity() {
    assertTrue(Matrix4x4.IDENTITY.isIdentity(), "Matrix should be identity");
  }

  @Test
  void isIdentityShouldRejectNotIdentity() {
    assertFalse(M1.isIdentity(), "Matrix should not be identity");
  }

  @Test
  void matrixHasDeterminant() {
    double d = M1.determinant();
    assertEquals(M1_DET, d);
  }

  @Test
  void invertShouldExist() {
    Matrix4x4 inverted = M1.invert();
    assertNotNull(inverted);
  }

  @Test
  void invertShouldHaveValues() {
    Matrix4x4 inverted = M1.invert();
    assertFalse(inverted.isNaN(), "Matrix should not be NaN");
  }

  @Test
  void doubleInvertShouldBeIdempotent() {
    Matrix4x4 inverted = M1.invert();
    Matrix4x4 twiceInverted = inverted.invert();
    assertTrue(M1.isWithinReasonableEpsilonOf(twiceInverted), "Matrix should be within reasonable epsilon");
  }

  @Test
  void multiplyingIdentityShouldReturnIdentityMatrix() {
    Matrix4x4 i1 = Matrix4x4.IDENTITY;
    Matrix4x4 i2 = Matrix4x4.IDENTITY;
    Matrix4x4 product = i1.times(i2);
    assertTrue(product.isIdentity(), "Matrix should be identity");
  }
}