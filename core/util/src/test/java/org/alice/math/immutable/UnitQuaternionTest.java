package org.alice.math.immutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitQuaternionTest {

  @Test
  void identityUnitQuaternionConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(UnitQuaternion.IDENTITY);
  }

  @Test
  void arbitraryUnitQuaternionConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(new UnitQuaternion(0.36801338078947526, -0.11105403604599343, 0.6129703620993356, 0.6902901475652142));
  }

  private static void checkConversionsAndBack(UnitQuaternion src) {
    assertSame(src, src.asUnitQuaternion(), "Should be equal to original angles");
    compareTo(src, src.asMatrix3x3());
    compareTo(src, src.asForwardAndUpGuide());
    compareTo(src, src.asAxisRotation());
    compareTo(src, src.asEulerAngles());
  }

  private static void compareTo(UnitQuaternion src, Orientation uq) {
    assertTrue(src.isAlignedWith(uq), "Source:\n" + src + "\nShould be the same as destination:\n" + uq);
  }
}
