package org.alice.math.immutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AxisRotationTest {
  
  @Test
  void identityAxisRotationConversionsToAndFromShouldBeEqual() {
    checkConversionsAndBack(AxisRotation.IDENTITY);
  }

  @Test
  void identityAxisShouldNotMatter() {
    compareTo(new AxisRotation(Vector3.POSITIVE_Y_AXIS, Angle.ZERO), AxisRotation.IDENTITY);
    compareTo(new AxisRotation(Vector3.POSITIVE_Z_AXIS, Angle.ZERO), AxisRotation.IDENTITY);
  }

  @Test
  void arbitraryAxisRotationConversionsToAndFromShouldBeEqual() {
    AxisRotation rotated = new AxisRotation((new Vector3(3, 4, 5)).normalized(), new AngleInRadians(0.7));
    checkConversionsAndBack(rotated);
  }

  private static void checkConversionsAndBack(AxisRotation src) {
    assertSame(src, src.asAxisRotation(), "Should be equal to original angles");
    compareTo(src, src.asMatrix3x3());
    compareTo(src, src.asForwardAndUpGuide());
    compareTo(src, src.asUnitQuaternion());
    compareTo(src, src.asEulerAngles());
  }

  private static void compareTo(AxisRotation src, Orientation uq) {
    AxisRotation dest = uq.asAxisRotation();
    assertTrue(src.isAlignedWith(dest), "Source:\n" + src + "\nShould be the same as destination:\n" + dest);
  }
}
