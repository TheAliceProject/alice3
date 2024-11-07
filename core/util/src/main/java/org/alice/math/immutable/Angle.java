package org.alice.math.immutable;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;

public interface Angle {
  Angle NaN = new AngleInRadians(Double.NaN);
  Angle ZERO = new AngleInRadians(0);
  double REVOLUTIONS_TO_DEGREES = 360;
  double DEGREES_TO_REVOLUTIONS = 1 / REVOLUTIONS_TO_DEGREES;

  double REVOLUTIONS_TO_RADIANS = 2 * Math.PI;
  double RADIANS_TO_REVOLUTIONS = 1 / REVOLUTIONS_TO_RADIANS;

  double RADIANS_TO_DEGREES = RADIANS_TO_REVOLUTIONS * REVOLUTIONS_TO_DEGREES;
  double DEGREES_TO_RADIANS = DEGREES_TO_REVOLUTIONS * REVOLUTIONS_TO_RADIANS;

  boolean isNaN();
  boolean isZero();
  default boolean isCloseTo(Angle other) {
    return this == other || isNaN() && other.isNaN() || EpsilonUtilities.isWithinReasonableEpsilon(getAsRadians(), other.getAsRadians());
  }

  double getAsRadians();
  double getAsDegrees();
  double getAsRevolutions();

  Angle negated();
  Angle minus(Angle b);

  Angle interpolateToward(Angle b, double portion);

  static double interpolate(double a, double b, double portion) {
    return a + ((b - a) * portion);
  }

  @Deprecated
  default edu.cmu.cs.dennisc.math.Angle mutable() {
    return new edu.cmu.cs.dennisc.math.AngleInRadians(getAsRadians());
  }
}
