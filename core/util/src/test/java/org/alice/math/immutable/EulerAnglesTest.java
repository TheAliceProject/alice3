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
