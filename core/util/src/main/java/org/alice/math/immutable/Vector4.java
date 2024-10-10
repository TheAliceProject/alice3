package org.alice.math.immutable;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;

public record Vector4(double x, double y, double z, double w) {

  public static final Vector4 ZERO = new Vector4(0,0, 0, 0);
  public static final Vector4 UNIT_X = new Vector4(1.0, 0, 0, 0);
  public static final Vector4 UNIT_Y = new Vector4(0, 1.0, 0, 0);
  public static final Vector4 UNIT_Z = new Vector4(0, 0, 1.0, 0);
  public static final Vector4 UNIT_W = new Vector4(0, 0, 0, 1.0);
  public static final Vector4 NaN = new Vector4(Double.NaN, Double.NaN, Double.NaN, Double.NaN);

  public boolean isWithinEpsilonOf(Vector4 other, double epsilon) {
    return EpsilonUtilities.isWithinEpsilon(this.x, other.x, epsilon)
        && EpsilonUtilities.isWithinEpsilon(this.y, other.y, epsilon)
        && EpsilonUtilities.isWithinEpsilon(this.z, other.z, epsilon)
        && EpsilonUtilities.isWithinEpsilon(this.w, other.w, epsilon);
  }

  public boolean isWithinReasonableEpsilonOf(Vector4 other) {
    return isWithinEpsilonOf(other, EpsilonUtilities.REASONABLE_EPSILON_FLOAT);
  }

  public boolean isZero() {
    return x == 0.0 && y == 0.0 && z == 0.0 && w == 0.0;
  }

  public boolean isNaN() {
    return Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z) || Double.isNaN(w);
  }

  public Vector4 plus(Vector4 b) {
    return new Vector4(x + b.x, y + b.y, z + b.z, w + b.w);
  }

  public Vector4 minus(Vector4 b) {
    return new Vector4(x - b.x, y - b.y, z - b.z, w - b.w);
  }

  public Vector4 negate() {
    return new Vector4(-x, -y, -z, -w);
  }

  public Vector4 times(double b) {
    return new Vector4(x * b, y * b, z * b, w * b);
  }

  public Vector4 dividedBy(double b) {
    return new Vector4(x / b, y / b, z / b, w / b);
  }

  public double dotProduct(Vector4 b) {
    return x * b.x + y * b.y + z * b.z + w * b.w;
  }

  public Vector4 interpolate(Vector4 b, double portion) {
    return new Vector4(
        x + ((b.x - x) * portion),
        y + ((b.y - y) * portion),
        z + ((b.z - z) * portion),
        z + ((b.w - w) * portion));
  }
  public double magnitudeSquared() {
    return (x * x) + (y * y) + (z * z) + (w * w);
  }

  public double magnitude() {
    double magnitudeSquared = magnitudeSquared();
    return magnitudeSquared == 1.0 ? 1.0 : Math.sqrt(magnitudeSquared);
  }

  public Vector4 normalized() {
    double magnitudeSquared = magnitudeSquared();
    return magnitudeSquared == 1.0 ? this : this.dividedBy(Math.sqrt(magnitudeSquared));
  }
}
