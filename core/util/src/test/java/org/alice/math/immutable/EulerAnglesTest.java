package org.alice.math.immutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EulerAnglesTest {
  @Test
  void identityEulerAnglesConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(EulerAngles.IDENTITY);
  }

  @Test
  void arbitraryEulerAnglesConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(new EulerAngles(new AngleInRadians(0.4), new AngleInRadians(0.7), new AngleInRadians(1.6), EulerAngles.Order.YAW_PITCH_ROLL));
  }

  @Test
  void arbitraryEulerAnglesConversionsToAndFromWhenMutableShouldBeEqual() {
    EulerAngles ea = new EulerAngles(new AngleInRadians(0.4), new AngleInRadians(0.7), new AngleInRadians(1.6), EulerAngles.Order.YAW_PITCH_ROLL);
    edu.cmu.cs.dennisc.math.EulerAngles mea = ea.mutable();
    edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 mm = mea.createOrthogonalMatrix3x3();
    edu.cmu.cs.dennisc.math.EulerAngles mea2 =mm.createEulerAngles();
    EulerAngles ea2 = mea2.immutable();
    compareTo(ea, ea2);
  }

  @Test
  void arbitraryMutableEulerAnglesConversionsToAndFromShouldBeEqual() {
    edu.cmu.cs.dennisc.math.EulerAngles mea = new edu.cmu.cs.dennisc.math.EulerAngles(
        new edu.cmu.cs.dennisc.math.AngleInRadians(0.4),
        new edu.cmu.cs.dennisc.math.AngleInRadians(0.7),
        new edu.cmu.cs.dennisc.math.AngleInRadians(1.6),
        edu.cmu.cs.dennisc.math.EulerAngles.Order.YAW_PITCH_ROLL);
    edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 mm = mea.createOrthogonalMatrix3x3();
    edu.cmu.cs.dennisc.math.EulerAngles mea2 =mm.createEulerAngles();
    assertEquals(mea.toString(), mea2.toString());
  }

  @Test
  void arbitraryMutableAndImmutableEulerAnglesConversionsToMatrixShouldBeEqual() {
    edu.cmu.cs.dennisc.math.EulerAngles mea = new edu.cmu.cs.dennisc.math.EulerAngles(
        new edu.cmu.cs.dennisc.math.AngleInRadians(0.4),
        new edu.cmu.cs.dennisc.math.AngleInRadians(0.7),
        new edu.cmu.cs.dennisc.math.AngleInRadians(1.6),
        edu.cmu.cs.dennisc.math.EulerAngles.Order.YAW_PITCH_ROLL);
    edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 mm = mea.createOrthogonalMatrix3x3();

    EulerAngles ea = new EulerAngles(new AngleInRadians(0.4), new AngleInRadians(0.7), new AngleInRadians(1.6), EulerAngles.Order.YAW_PITCH_ROLL);
    OrthogonalMatrix3x3 im = ea.asMatrix3x3();
    assertEquals(mm.immutable().toString(), im.toString());
  }

  private static void checkConversionsAndBack(EulerAngles src) {
    assertSame(src, src.asEulerAngles(), "Should be equal to original angles");
    compareTo(src, src.asMatrix3x3());
    compareTo(src, src.asForwardAndUpGuide());
    compareTo(src, src.asAxisRotation());
    compareTo(src, src.asUnitQuaternion());
  }

  private static void compareTo(EulerAngles src, Orientation uq) {
    EulerAngles dest = uq.asEulerAngles();
    assertTrue(src.isAlignedWith(dest), "Source:\n" + src + "\nShould be the same as destination:\n" + dest);
  }
}
