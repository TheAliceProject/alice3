package org.alice.math.immutable;

public record AngleInRadians(double radians) implements Angle {
  @Override
  public boolean isNaN() {
    return Double.isNaN(radians);
  }

  @Override
  public boolean isZero() {
    return radians == 0.0;
  }

  @Override
  public double getAsRadians() {
    return radians;
  }

  @Override
  public double getAsDegrees() {
    return radians * Angle.RADIANS_TO_DEGREES;
  }

  @Override
  public double getAsRevolutions() {
    return radians * Angle.RADIANS_TO_REVOLUTIONS;
  }

  @Override
  public Angle negated() {
    return new AngleInRadians(-radians);
  }

  @Override
  public Angle minus(Angle b) {
    return b.isZero() ? this : new AngleInRadians(radians - b.getAsRadians());
  }

  @Override
  public Angle times(double factor) {
    return new AngleInRadians(radians * factor);
  }

  @Override
  public Angle interpolateToward(Angle b, double portion) {
    return new AngleInRadians(Angle.interpolate(radians, b.getAsRadians(), portion));
  }
}
