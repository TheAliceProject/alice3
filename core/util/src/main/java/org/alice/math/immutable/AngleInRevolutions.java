package org.alice.math.immutable;

public record AngleInRevolutions(double revolutions) implements Angle {
  @Override
  public boolean isNaN() {
    return Double.isNaN(revolutions);
  }

  @Override
  public boolean isZero() {
    return revolutions == 0.0;
  }

  @Override
  public double getAsRadians() {
    return revolutions * Angle.REVOLUTIONS_TO_RADIANS;
  }

  @Override
  public double getAsDegrees() {
    return revolutions * Angle.REVOLUTIONS_TO_DEGREES;
  }

  @Override
  public double getAsRevolutions() {
    return revolutions;
  }

  @Override
  public Angle negated() {
    return new AngleInRevolutions(-revolutions);
  }

  @Override
  public Angle minus(Angle b) {
    return b.isZero() ? this : new AngleInRevolutions(revolutions - b.getAsRevolutions());
  }

  @Override
  public Angle times(double factor) {
    return new AngleInRevolutions(revolutions * factor);
  }

  @Override
  public Angle interpolateToward(Angle b, double portion) {
    return new AngleInRevolutions(Angle.interpolate(revolutions, b.getAsRevolutions(), portion));
  }
}
