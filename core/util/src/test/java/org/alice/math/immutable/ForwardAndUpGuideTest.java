package org.alice.math.immutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ForwardAndUpGuideTest {
  Vector3 forward = (new Vector3(1, 1, 0)).normalized();
  Vector3 up = (new Vector3(0, 0, 1));
  ForwardAndUpGuide rotated = new ForwardAndUpGuide(forward, up);
  ForwardAndUpGuide lookUp = new ForwardAndUpGuide(up, forward);

  @Test
  void identityForwardAndUpGuideConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(ForwardAndUpGuide.IDENTITY);
  }

  @Test
  void arbitraryForwardAndUpGuideConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(rotated);
  }

  @Test
  void arbitraryFlippedForwardAndUpGuideConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(lookUp);
  }

  private static void checkConversionsAndBack(ForwardAndUpGuide src) {
    assertSame(src, src.asForwardAndUpGuide(), "Should be equal to original angles");
    compareTo(src, src.asAxisRotation());
    compareTo(src, src.asUnitQuaternion());
    compareTo(src, src.asEulerAngles());
    compareTo(src, src.asMatrix3x3());
  }

  private static void compareTo(ForwardAndUpGuide src, Orientation uq) {
    ForwardAndUpGuide dest = uq.asForwardAndUpGuide();
    assertTrue(src.isAlignedWith(dest), "Source:\n" + src + "\nShould be the same as destination:\n" + dest);
  }
}
