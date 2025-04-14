package org.alice.math.immutable;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;

import java.io.Serializable;

public interface Angle extends Serializable {
  Angle NaN = new AngleInRadians(Double.NaN);
  Angle ZERO = new AngleInRadians(0);
  Angle PI = new AngleInRadians(Math.PI);
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
  Angle times(double factor);

  Angle interpolateToward(Angle b, double portion);

  static double interpolate(double a, double b, double portion) {
    return a + ((b - a) * portion);
  }

  default Angle toNearestPi() {
    return toNearest(PI);
  }

  default Angle toNearest(Angle unit) {
    double radians = getAsRadians();
    double unitRadians = unit.getAsRadians();
    int unitCount = (int) (radians / unitRadians);
    // Set to absolute lower bound
    unitCount = (radians < 0) ? unitCount - 1 : unitCount;

    // Pick closest half, below or above
    if (radians > (0.5 + unitCount) * Math.PI) {
      unitCount++;
    }
    return new AngleInRadians(unitCount * unitRadians);
  }
}
