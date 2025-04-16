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
  private final OrthogonalMatrix3x3 rotatedMatrix =
      OrthogonalMatrix3x3.IDENTITY.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_X_AXIS, new AngleInRadians(2));

  @Test
  void createShouldMakeMatrix() {
    OrthogonalMatrix3x3 m = new OrthogonalMatrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertNotNull(m, "Matrix should not be null");
  }

  @Test
  void createIdentityByVectorsShouldEqualIdentityMatrix() {
    OrthogonalMatrix3x3 m = new OrthogonalMatrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertEquals(Matrix3x3.IDENTITY, m, "Matrix should be identity");
  }

  @Test
  void createIdentityByValuesShouldBeOrthogonalMatrix() {
    Matrix3x3 m = Matrix3x3.create(
        1, 0, 0,
        0,1, 0,
        0, 0, 1);
    assertInstanceOf(OrthogonalMatrix3x3.class, m, "Matrix should be OrthogonalMatrix3x3");
  }

  @Test
  void createTranspositionByValuesShouldBeOrthogonalMatrix() {
    Matrix3x3 m = Matrix3x3.create(
        0, 1, 0,
        1,0, 0,
        0, 0, 1);
    assertInstanceOf(OrthogonalMatrix3x3.class, m, "Matrix should be OrthogonalMatrix3x3");
  }

  @Test
  void createUnitButNotOrthogonalByValuesShouldNotBeOrthogonalMatrix() {
    Matrix3x3 m = Matrix3x3.create(
        0, 0, 0,
        1,1, 0,
        0, 0, 1);
    assertInstanceOf(FullMatrix3x3.class, m, "Matrix should not be OrthogonalMatrix3x3");
  }

  @Test
  void createOrthogonalButNotUnitByValuesShouldBeOrthogonalMatrix() {
    Matrix3x3 m = Matrix3x3.create(
        2, 0, 0,
        0,1, 0,
        0, 0, 1);
    assertInstanceOf(OrthogonalMatrix3x3.class, m, "Matrix should be OrthogonalMatrix3x3");
  }

  @Test
  void createIdentityByValuesShouldEqualIdentityMatrix() {
    Matrix3x3 m = Matrix3x3.create(
        1, 0, 0,
        0,1, 0,
        0, 0, 1);
    assertEquals(OrthogonalMatrix3x3.IDENTITY, m, "Matrix should be identity");
  }

  @Test
  void createIdentityShouldReplyTrueOnIsIdentity() {
    OrthogonalMatrix3x3 m = new OrthogonalMatrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertTrue(m.isIdentity(), "Matrix should be identity");
  }

  @Test
  void createIdentityShouldReplyFalseOnIsZero() {
    OrthogonalMatrix3x3 m = new OrthogonalMatrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertFalse(m.isZero(), "Matrix should not be zero");
  }

  @Test
  void createIdentityShouldReplyFalseOnIsNaN() {
    OrthogonalMatrix3x3 m = new OrthogonalMatrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.POSITIVE_Z_AXIS);
    assertFalse(m.isNaN(), "Matrix should not be NaN");
  }

  @Test
  void isNaNShouldRecognizeNaN() {
    assertTrue(OrthogonalMatrix3x3.NaN.isNaN(), "Matrix should be NaN");
  }

  @Test
  void isNaNShouldRejectValidMatrix() {
    assertFalse(M1.isNaN(), "Matrix should not be NaN");
  }

  @Test
  void isZeroShouldRecognizeZero() {
    assertTrue(OrthogonalMatrix3x3.ZERO.isZero(), "Matrix should be zero");
  }

  @Test
  void isZeroShouldRejectNotZero() {
    assertFalse(M1.isZero(), "Matrix should not be zero");
  }

  @Test
  void isIdentityShouldRecognizeIdentity() {
    assertTrue(OrthogonalMatrix3x3.IDENTITY.isIdentity(), "Matrix should be identity");
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
  void rotatedMatrixHasDeterminant() {
    double d = rotatedMatrix.determinant();
    assertEquals(1.0, d);
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
    OrthogonalMatrix3x3 i1 = OrthogonalMatrix3x3.IDENTITY;
    OrthogonalMatrix3x3 i2 = OrthogonalMatrix3x3.IDENTITY;
    Matrix3x3 product = i1.times(i2);
    assertTrue(product.isIdentity(), "Matrix should be identity");
  }

  @Test
  void rotatedIdentityMatrixShouldBeOrthogonal() {
    assertInstanceOf(OrthogonalMatrix3x3.class, rotatedMatrix);
  }

  @Test
  void rotatedIdentityMatrixShouldNotBeIdentity() {
    assertFalse(rotatedMatrix.isIdentity(), "Identity should be false");
  }

  @Test
  void twiceRotatedIdentityMatrixShouldBeIdentity() {
    OrthogonalMatrix3x3 m = rotatedMatrix.applyRotationAboutArbitraryAxis(Vector3.POSITIVE_X_AXIS, new AngleInRadians(-2));
    assertTrue(m.isIdentity(), "Identity should be true");
  }

  @Test
  void identityOrthogonalMatrixConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(OrthogonalMatrix3x3.IDENTITY);
  }

  @Test
  void rotatedMatrixConversionToAndFromShouldRemainEqual() {
    checkConversionsAndBack(rotatedMatrix);
  }

  private static void checkConversionsAndBack(OrthogonalMatrix3x3 src) {
    compareTo(src, src.asEulerAngles());
    compareTo(src, src.asUnitQuaternion());
    assertSame(src, src.asMatrix3x3(), "Should be equal to original matrix");
    compareTo(src, src.asForwardAndUpGuide());
    compareTo(src, src.asAxisRotation());
  }

  private static void compareTo(OrthogonalMatrix3x3 src, Orientation uq) {
    OrthogonalMatrix3x3 dest = uq.asMatrix3x3();
    assertTrue(src.isAlignedWith(dest), "Source:\n" + src + "\nShould be the same as destination:\n" + dest);
  }
}