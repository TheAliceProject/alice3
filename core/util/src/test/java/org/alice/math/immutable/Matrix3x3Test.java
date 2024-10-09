package org.alice.math.immutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix3x3Test {

  static final Matrix3x3 M1 = Matrix3x3.create(
      1, 2, 3,
      6, 5, 4,
      0, 8, 9
      );
  static final double M1_DET = 49.0;

  @Test
  void createShouldMakeMatrix() {
    Matrix3x3 m = new Matrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertNotNull(m, "Matrix should not be null");
  }

  @Test
  void createIdentityShouldEqualIdentityMatrix() {
    Matrix3x3 m = new Matrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertEquals(Matrix3x3.IDENTITY, m, "Matrix should be identity");
  }

  @Test
  void createIdentityShouldReplyTrueOnIsIdentity() {
    Matrix3x3 m = new Matrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertTrue(m.isIdentity(), "Matrix should be identity");
  }

  @Test
  void createIdentityShouldReplyFalseOnIsZero() {
    Matrix3x3 m = new Matrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertFalse(m.isZero(), "Matrix should not be zero");
  }

  @Test
  void createIdentityShouldReplyFalseOnIsNaN() {
    Matrix3x3 m = new Matrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertFalse(m.isNaN(), "Matrix should not be NaN");
  }

  @Test
  void isNaNShouldRecognizeNaN() {
    assertTrue(Matrix3x3.NaN.isNaN(), "Matrix should be NaN");
  }

  @Test
  void isNaNShouldRejectValidMatrix() {
    assertFalse(M1.isNaN(), "Matrix should not be NaN");
  }

  @Test
  void isZeroShouldRecognizeZero() {
    assertTrue(Matrix3x3.ZERO.isZero(), "Matrix should be zero");
  }

  @Test
  void isZeroShouldRejectNotZero() {
    assertFalse(M1.isZero(), "Matrix should not be zero");
  }

  @Test
  void isIdentityShouldRecognizeIdentity() {
    assertTrue(Matrix3x3.IDENTITY.isIdentity(), "Matrix should be identity");
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
    Matrix3x3 inverted = M1.invert();
    assertNotNull(inverted);
  }

  @Test
  void invertShouldHaveValues() {
    Matrix3x3 inverted = M1.invert();
    assertFalse(inverted.isNaN(), "Matrix should not be NaN");
  }

  @Test
  void doubleInvertShouldBeIdempotent() {
    Matrix3x3 inverted = M1.invert();
    Matrix3x3 twiceInverted = inverted.invert();
    assertTrue(M1.isWithinReasonableEpsilonOf(twiceInverted), "Matrix should be within reasonable epsilon");
  }

  @Test
  void multiplyingIdentityShouldReturnIdentityMatrix() {
    Matrix3x3 i1 = Matrix3x3.IDENTITY;
    Matrix3x3 i2 = Matrix3x3.IDENTITY;
    Matrix3x3 product = i1.times(i2);
    assertTrue(product.isIdentity(), "Matrix should be identity");
  }

  @Test
  void conversionToMutableShouldBeIdempotent() {
    edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 mutable = M1.mutable();
    Matrix3x3 twiceConverted = mutable.immutable();
    assertEquals(M1, twiceConverted, "Matrix should be the same");
  }

  @Test
  void conversionToMutableAndTwiceInvertedShouldBeIdempotent() {
    edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 mutable = M1.mutable();
    mutable.invert();
    mutable.invert();
    Matrix3x3 twiceConverted = mutable.immutable();
    assertTrue(M1.isWithinReasonableEpsilonOf(twiceConverted), "Matrix should be the same");
  }
}