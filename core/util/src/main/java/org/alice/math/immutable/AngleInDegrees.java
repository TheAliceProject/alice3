package org.alice.math.immutable;

public record AngleInDegrees(double degrees) implements Angle {
  @Override
  public boolean isNaN() {
    return Double.isNaN(degrees);
  }

  @Override
  public boolean isZero() {
    return degrees == 0.0;
  }

  @Override
  public double getAsRadians() {
    return degrees * Angle.DEGREES_TO_RADIANS;
  }

  @Override
  public double getAsDegrees() {
    return degrees;
  }

  @Override
  public double getAsRevolutions() {
    return degrees * Angle.DEGREES_TO_REVOLUTIONS;
  }

  @Override
  public Angle negated() {
    return new AngleInDegrees(-degrees);
  }

  @Override
  public Angle minus(Angle b) {
    return b.isZero() ? this : new AngleInDegrees(degrees - b.getAsDegrees());
  }

  @Override
  public Angle times(double factor) {
    return new AngleInDegrees(degrees * factor);
  }

  @Override
  public Angle interpolateToward(Angle b, double portion) {
    return new AngleInDegrees(Angle.interpolate(degrees, b.getAsDegrees(), portion));
  }
}
